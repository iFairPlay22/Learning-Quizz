package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.User;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.step.IStepData;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionData;
import app.ewen.k2hoot.model.step.question.QuestionInput;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.StepContainer;

public class GameActivity extends AppCompatActivity {

    private User mUser;
    private StepContainer mStepContainer;
    public static final String BUNDLE_EXTRA_USER = "BUNDLE_EXTRA_USER";
    public static final String BUNDLE_EXTRA_STEP_CONTAINER = "BUNDLE_EXTRA_STEP_CONTAINER";
    private static final int GAME_QUESTION_ACTIVITY_REQUEST_CODE = 3;


    private TextView mWelcomeTextView;
    private Button mPlayButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mWelcomeTextView = findViewById(R.id.game_activity_textview_welcome);

        mPlayButton  = findViewById(R.id.game_activity_button_play);


        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            mStepContainer = StepContainer.loadFromServer(data.getQueryParameter("key"));
        }else{

            if (savedInstanceState == null) {
                Intent intent = getIntent();
                mStepContainer = (StepContainer) intent.getParcelableExtra(BUNDLE_EXTRA_STEP_CONTAINER);
                mUser = (User)intent.getParcelableExtra(BUNDLE_EXTRA_USER);
            } else {
                mUser = (User)savedInstanceState.getParcelable(BUNDLE_EXTRA_USER);
                mStepContainer = savedInstanceState.getParcelable(BUNDLE_EXTRA_STEP_CONTAINER);
            }

        }
        mWelcomeTextView.setText("Welcome user : ");

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                HttpFile h = mStepContainer.storeInServer(getActivity());
                sendIntent.putExtra(Intent.EXTRA_TEXT,"http://www.k2hoot.com/load?key="+h.getKey());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);*/
                displayCurrentQuestion();
            }
        });
    }

    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_EXTRA_USER, mUser);
        outState.putParcelable(BUNDLE_EXTRA_STEP_CONTAINER, mStepContainer);
    }

    private void nextQuestion() {

        mStepContainer.nextStep();

        if (mStepContainer.currentStep() == null) {
            displayCurrentScore();
        } else {
            displayCurrentQuestion();
        }
    }


    private void displayCurrentQuestion() {
        Step currentStep = (Step) mStepContainer.currentStep();
        if (currentStep == null) return ;
        if(currentStep  instanceof  QuestionStep){
            QuestionStep qs = (QuestionStep) currentStep;

            Intent gameActivityIntent = new Intent(GameActivity.this, GameQuestionActivity.class);
            gameActivityIntent.putExtra(GameQuestionActivity.INTENT_QUESTION_STEP, qs);
            startActivityForResult(gameActivityIntent, GAME_QUESTION_ACTIVITY_REQUEST_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(GAME_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            boolean increase = data.getBooleanExtra(GameQuestionActivity.BUNDLE_EXTRA_VALIDATE, false);
            Log.i("QS","Increse "+increase);
            if(increase){
                mUser.incrementScore();
            }
            nextQuestion();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void displayCurrentScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setTitle("Well done!")
                .setMessage("Your score is " + mUser.getScore() + " / " + mStepContainer.size())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_USER, mUser);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}