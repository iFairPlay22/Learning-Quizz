package app.ewen.k2hoot.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionData;
import app.ewen.k2hoot.model.step.question.QuestionInput;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class GameQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTextView;
    private List<Button> mButtons;

    private StepContainer mStepContainer;
    private boolean mScore;
    public static final String BUNDLE_EXTRA_VALIDATE = "BUNDLE_EXTRA_VALIDATE";
    public static final String INTENT_QUESTION_STEP = "INTENT_QUESTION_STEP";
    private QuestionStep mQuestionStep;
    private boolean mEnableTouchEvents = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_question);

        mQuestionTextView = findViewById(R.id.game_activity_textview_question);

        mButtons = new ArrayList<>();
        mButtons.add(findViewById(R.id.game_activity_button_1));
        mButtons.add(findViewById(R.id.game_activity_button_2));
        mButtons.add(findViewById(R.id.game_activity_button_3));
        mButtons.add(findViewById(R.id.game_activity_button_4));

        for (int i = 0; i < mButtons.size(); i++)
            mButtons.get(i).setOnClickListener(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mQuestionStep = (QuestionStep)intent.getParcelableExtra(INTENT_QUESTION_STEP) ;
        } else {
            mQuestionStep = (QuestionStep)savedInstanceState.getParcelable(INTENT_QUESTION_STEP);
        }

        mQuestionTextView.setText(mQuestionStep.getQuestion());
        List<String> propositions = mQuestionStep.getChoiceList();
        for (int i = 0; i < propositions.size(); i++)
            mButtons.get(i).setText(propositions.get(i));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INTENT_QUESTION_STEP, mQuestionStep);
    }
    private  boolean isGoodAnswer = false;
    @Override
    public void onClick(View v) {


        for (int i = 0; i < mButtons.size(); i++) {
            if (mButtons.get(i) == v) {
                QuestionInput userInput = new QuestionInput(i);
                if (mQuestionStep.isGoodAnswer(userInput)) {
                    isGoodAnswer = true;
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    isGoodAnswer = false;
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                Log.i("QS", "Result "+isGoodAnswer);
                intent.putExtra(BUNDLE_EXTRA_VALIDATE, isGoodAnswer);
                setResult(RESULT_OK, intent);
                finish();
                mEnableTouchEvents = true;
            }
        }, 2_000);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }


    private void terminateStep() {

    }
}