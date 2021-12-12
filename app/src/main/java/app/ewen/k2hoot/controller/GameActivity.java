package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.StepContainer;

public class GameActivity extends AppCompatActivity {

    // UI Elements
    private TextView mWelcomeTextView;
    private TextView mScoreTextView;
    private Button mPlayButton;

    // Model
    private int mScore;
    private StepContainer mStepContainer;

    // Activity communication
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String INTENT_EXTRA_STEP_CONTAINER = "INTENT_EXTRA_STEP_CONTAINER";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_EXTRA_SCORE, mScore);
        outState.putParcelable(INTENT_EXTRA_STEP_CONTAINER, mStepContainer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Model
        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {

            // Import a quiz
            mStepContainer = StepContainer.loadFromServer(data.getQueryParameter("key"));
            mStepContainer.addToSharedPreferences(getSharedPreferences(MainActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE));
        } else {

            // Play a created quiz
            if (savedInstanceState == null) {

                // Load quiz from intent
                Intent intent = getIntent();
                mStepContainer = (StepContainer) intent.getParcelableExtra(INTENT_EXTRA_STEP_CONTAINER);
                mScore = intent.getIntExtra(BUNDLE_EXTRA_SCORE,0);
            } else {

                // Load quiz from bundle
                mScore = savedInstanceState.getInt(BUNDLE_EXTRA_SCORE);
                mStepContainer = savedInstanceState.getParcelable(INTENT_EXTRA_STEP_CONTAINER);
            }
        }

        // UI Elements
        mWelcomeTextView = findViewById(R.id.game_activity_text_view_welcome);
        mScoreTextView = findViewById(R.id.game_activity_text_view_score);
        mPlayButton  = findViewById(R.id.game_activity_button_play);

        // UI Actions
        mScoreTextView.setText(mStepContainer.getBestScore() > 0 ? "Meilleur score : " + mStepContainer.getBestScore() : "Vous n'avez encore jamais fait ce quiz.");

        mWelcomeTextView.setText("Welcome to the quizzz :\n \n" + mStepContainer.getName());

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCurrentStep();
            }
        });

    }

    // Go to the next step and display it
    private void nextStep() {
        mStepContainer.nextStep();
        displayCurrentStep();
    }

    // Update the screen to print the current step
    private void displayCurrentStep() {
        Step currentStep = (Step) mStepContainer.currentStep();

        // End of quiz
        if (currentStep == null) {
            displayCurrentScore();
            return ;
        }

        // Launch a QuestionStep view
        if(currentStep instanceof QuestionStep){
            QuestionStep qs = (QuestionStep) currentStep;

            Intent gameActivityIntent = new Intent(GameActivity.this, GameQuestionActivity.class);
            gameActivityIntent.putExtra(GameQuestionActivity.INTENT_QUESTION_STEP, qs);
            startActivityForResult(gameActivityIntent, MainActivity.GAME_QUESTION_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (MainActivity.GAME_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            boolean increase = data.getBooleanExtra(GameQuestionActivity.BUNDLE_EXTRA_VALIDATE, false);
            if (increase) mScore++;
            nextStep();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void displayCurrentScore() {

        boolean newBest = mScore > mStepContainer.getBestScore();

        if(newBest)
            mStepContainer.setBestScore(mScore);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Well done!")
                .setMessage("Your score is " + mScore + " / " + mStepContainer.stepNb())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        if(newBest)
                            intent.putExtra(INTENT_EXTRA_STEP_CONTAINER, mStepContainer);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}