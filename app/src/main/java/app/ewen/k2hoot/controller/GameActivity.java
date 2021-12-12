package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.StepContainer;

public class GameActivity extends AppCompatActivity {

    private int mScore;
    private StepContainer mStepContainer;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String INTENT_EXTRA_STEP_CONTAINER = "INTENT_EXTRA_STEP_CONTAINER";
    private static final int GAME_QUESTION_ACTIVITY_REQUEST_CODE = 3;


    private TextView mWelcomeTextView;
    private TextView mScoreTextView;
    private Button mPlayButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mWelcomeTextView = findViewById(R.id.game_activity_textview_welcome);

        mPlayButton  = findViewById(R.id.game_activity_button_play);
        mScoreTextView = findViewById(R.id.game_activity_textview_score);

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {

            String uri = this.getIntent().getDataString();
            mStepContainer = StepContainer.loadFromServer(data.getQueryParameter("key"));
            mStepContainer.addToSharedPreferences(getSharedPreferences(QuizzListActivity.SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE));

        }else{

            if (savedInstanceState == null) {
                Intent intent = getIntent();
                mStepContainer = (StepContainer) intent.getParcelableExtra(INTENT_EXTRA_STEP_CONTAINER);
                mScore = intent.getIntExtra(BUNDLE_EXTRA_SCORE,0);
            } else {
                mScore = savedInstanceState.getInt(BUNDLE_EXTRA_SCORE);
                mStepContainer = savedInstanceState.getParcelable(INTENT_EXTRA_STEP_CONTAINER);
            }

        }
        String score = mStepContainer.getBestScore() > 0 ? "Meilleur score : "+mStepContainer.getBestScore() : "Vous n'avez encore jamais fait ce quiz.";
        mWelcomeTextView.setText("Welcome to the quizz :\n \n" + mStepContainer.getName());
        mScoreTextView.setText(score);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        outState.putInt(BUNDLE_EXTRA_SCORE, mScore);
        outState.putParcelable(INTENT_EXTRA_STEP_CONTAINER, mStepContainer);
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
                mScore++;
            }
            nextQuestion();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void displayCurrentScore() {

        boolean newBest =mScore > mStepContainer.getBestScore();
        if(newBest){
            mStepContainer.setBestScore(mScore);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setTitle("Well done!")
                .setMessage("Your score is " + mScore + " / " + mStepContainer.stepNb())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        if(newBest){
                            intent.putExtra(INTENT_EXTRA_STEP_CONTAINER, mStepContainer);
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}