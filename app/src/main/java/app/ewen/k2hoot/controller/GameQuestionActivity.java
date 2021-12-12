package app.ewen.k2hoot.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.question.QuestionInput;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class GameQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Elements
    private TextView mQuestionTextView;
    private List<Button> mButtons;

    // UI Actions
    private boolean mEnableTouchEvents = true;

    // Model
    private boolean isGoodAnswer;
    private QuestionStep mQuestionStep;

    // Activity communication
    public static final String BUNDLE_EXTRA_VALIDATE = "BUNDLE_EXTRA_VALIDATE";
    public static final String INTENT_QUESTION_STEP = "INTENT_QUESTION_STEP";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INTENT_QUESTION_STEP, mQuestionStep);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_question);

        // Model
        if (savedInstanceState == null) {

            // Load From intent
            Intent intent = getIntent();
            mQuestionStep = (QuestionStep)intent.getParcelableExtra(INTENT_QUESTION_STEP) ;
        } else {

            // Load from bundle
            mQuestionStep = (QuestionStep)savedInstanceState.getParcelable(INTENT_QUESTION_STEP);
        }

        // UI Elements
        mQuestionTextView = findViewById(R.id.game_activity_text_view_question);

        mButtons = new ArrayList<>();
        mButtons.add(findViewById(R.id.game_activity_button_1));
        mButtons.add(findViewById(R.id.game_activity_button_2));
        mButtons.add(findViewById(R.id.game_activity_button_3));
        mButtons.add(findViewById(R.id.game_activity_button_4));

        // UI Actions

        // Display question
        mQuestionTextView.setText(mQuestionStep.getQuestion());

        // Display propositions
        List<String> propositions = mQuestionStep.getChoiceList();
        for (int i = 0; i < mButtons.size(); i++) {
            Button btn = mButtons.get(i);
            btn.setOnClickListener(this);
            btn.setText(propositions.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        // Get the clicked btn
        for (int i = 0; i < mButtons.size(); i++) {
            if (mButtons.get(i) == v) {
                // Good or bad answer ?
                QuestionInput userInput = new QuestionInput(i);
                if (mQuestionStep.isGoodAnswer(userInput)) {
                    isGoodAnswer = true;
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    isGoodAnswer = false;
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

        // Terminate the activity
        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
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
}