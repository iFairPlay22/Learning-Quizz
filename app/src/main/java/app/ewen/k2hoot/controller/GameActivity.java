package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.IStepData;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionData;
import app.ewen.k2hoot.model.step.question.QuestionInput;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.StepContainer;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTextView;
    private List<Button> mButtons;

    private StepContainer mStepContainer;
    private int mScore;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION_BANK = "BUNDLE_STATE_QUESTION";

    private boolean mEnableTouchEvents = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionTextView = findViewById(R.id.game_activity_textview_question);

        mButtons = new ArrayList<>();
        mButtons.add(findViewById(R.id.game_activity_button_1));
        mButtons.add(findViewById(R.id.game_activity_button_2));
        mButtons.add(findViewById(R.id.game_activity_button_3));
        mButtons.add(findViewById(R.id.game_activity_button_4));

        for (int i = 0; i < mButtons.size(); i++)
            mButtons.get(i).setOnClickListener(this);

        if (savedInstanceState == null) {
            mStepContainer = generateQuestionBank();
            mScore = 0;
        } else {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mStepContainer = savedInstanceState.getParcelable(BUNDLE_STATE_QUESTION_BANK);
        }

        displayCurrentQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putParcelable(BUNDLE_STATE_QUESTION_BANK, mStepContainer);
    }

    private StepContainer generateQuestionBank() {

        ArrayList<Step> questions = new ArrayList<>();

        String[] strQuestions = getResources().getStringArray(R.array.GameActivity_Questions);
        int[] goodAnswers = { 0, 3, 3 };

        int j = 0;
        for (int i = 0; i < strQuestions.length; i += 5) {
            questions.add(
                new QuestionStep(strQuestions[i], Arrays.asList(strQuestions[i+1], strQuestions[i+2], strQuestions[i+3], strQuestions[i+4]), goodAnswers[j++])
            );
        }

        return new StepContainer(questions,"");
    }

    @Override
    public void onClick(View v) {

        Step currentQuestion = mStepContainer.currentStep();

        for (int i = 0; i < mButtons.size(); i++) {
            if (mButtons.get(i) == v) {
                QuestionInput userInput = new QuestionInput(i);
                if (currentQuestion.isGoodAnswer(userInput)) {
                    mScore++;
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
                mEnableTouchEvents = true;
            }
        }, 2_000);
    }

    private void nextQuestion() {

        mStepContainer.nextStep();

        if (mStepContainer.currentStep() == null) {
            displayCurrentScore();
        } else {
            displayCurrentQuestion();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void displayCurrentQuestion() {
        QuestionStep currentQuestion = (QuestionStep) mStepContainer.currentStep();
        if (currentQuestion == null) return ;

        QuestionData data = currentQuestion.getData();

        mQuestionTextView.setText(data.getQuestion());
        List<String> propositions = data.getPropositions();
        for (int i = 0; i < propositions.size(); i++)
            mButtons.get(i).setText(propositions.get(i));
    }

    private void displayCurrentScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}