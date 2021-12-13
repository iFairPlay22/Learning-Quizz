package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.User;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.step.binding.BindingStep;

public class MainActivity extends AppCompatActivity {


    // UI Elements
    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private Button mCreateButton;
    private Button mCreateBindingButton;
    private Button mPlayBindingButton;
    private Button mCreateGapSentenceButton;
    private Button mQuizListButton;

    // Model
    private User mUser;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mUser.saveUserInBundle(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Model
        mUser = User.getUserFromBundle(savedInstanceState);

        // UI Elements
        mGreetingTextView = findViewById(R.id.main_activity_text_view_greeting);
        mNameEditText = findViewById(R.id.main_edit_activity_text_name);
        mPlayButton = findViewById(R.id.main_activity_button_play);
        mCreateButton = findViewById(R.id.main_activity_button_create);
        mCreateBindingButton = findViewById(R.id.main_activity_button_create_binding);
        mPlayBindingButton = findViewById(R.id.main_activity_button_play_binding);
        mCreateGapSentenceButton = findViewById(R.id.main_activity_button_create_gap_sentence);
        mQuizListButton = findViewById(R.id.main_activity_button_quiz_list);

        // UI Actions

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    mPlayButton.setEnabled(false);
                } else {
                    mUser.setFirstName(mNameEditText.getText().toString());
                    mUser.storeInPreferences(getSharedPreferences(QuizListActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE));
                    mPlayButton.setEnabled(true);
                }
            }
        });

        mQuizListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch quiz list view
                Intent quizListIntent = new Intent(MainActivity.this, QuizListActivity.class);
                startActivity(quizListIntent);
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch create view
                Intent gameActivityIntent = new Intent(MainActivity.this, CreateQuizzActivity.class);
                startActivityForResult(gameActivityIntent, QuizListActivity.CREATE_QUIZ_ACTIVITY_REQUEST_CODE);
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create test QCM
                List<Step> steps = new ArrayList<>();

                String question = "Question 1 : Comment ça va ?";
                ArrayList<String> answers = new ArrayList<>();
                answers.add("bien");
                answers.add("moyen");
                answers.add("mal");
                answers.add("très mal");
                QuestionStep qs = new QuestionStep(question,answers,1);
                steps.add(qs);

                question = "Question 2 : Qui prefère tu ?";
                answers = new ArrayList<>();
                answers.add("Ewen");
                answers.add("Lucas");
                answers.add("Loic");
                answers.add("Fabien");
                qs = new QuestionStep(question,answers,2);
                steps.add(qs);

                StepContainer sc = new StepContainer(steps,"QCM");

                // Launch game view
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                gameActivityIntent.putExtra(GameActivity.INTENT_EXTRA_STEP_CONTAINER, sc);
                startActivityForResult(gameActivityIntent, QuizListActivity.GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mCreateBindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch create binding view
                Intent gameActivityIntent = new Intent(MainActivity.this, CreateBindingActivity.class);
                startActivityForResult(gameActivityIntent, QuizListActivity.GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mPlayBindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create test binding question
                HashMap<String,String> s = new HashMap<>();
                s.put("B", "Voiture");
                s.put("C", "Bateau");
                s.put("A2", "Moto");
                s.put("D", "Camion");
                BindingStep st = new BindingStep("Permis", s);

                // Launch game binding view
                Intent gameActivityIntent = new Intent(MainActivity.this, GameBindingActivity.class);
                gameActivityIntent.putExtra(GameBindingActivity.INTENT_INPUT_BINDING_STEP, st);
                startActivityForResult(gameActivityIntent, QuizListActivity.GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mCreateGapSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create gap sentence view
                Intent gameActivityIntent = new Intent(MainActivity.this, CreateGapSentenceActivity.class);
                startActivityForResult(gameActivityIntent, QuizListActivity.GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        updateLabels();

        mPlayButton.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (QuizListActivity.GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            StepContainer sc = (StepContainer)data.getParcelableExtra(GameActivity.INTENT_EXTRA_STEP_CONTAINER);
            if (sc != null) Log.i("GAME_A",sc.toString());

        } else if (CreateQuizzActivity.CREATE_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            QuestionStep qs = (QuestionStep)data.getParcelableExtra(CreateQuestionActivity.INTENT_CREATE_QUESTION_STEP);
            if (qs != null) Log.i("CREATE_QUESTION_A", qs.toString());

        } else if (GameActivity.GAME_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            boolean increase = data.getBooleanExtra(GameQuestionActivity.BUNDLE_EXTRA_VALIDATE, false);
            Log.i("GAME_QUESTION_A","" + increase);


        } else if (QuizListActivity.CREATE_QUIZ_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            StepContainer qs = (StepContainer)data.getParcelableExtra(CreateQuizzActivity.INTENT_CREATE_STEP_CONTAINER);
            Log.i("CREATE_QUIZ_A", qs.toString());

        }

        updateLabels();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabels() {
        mUser.loadFromPreferences(getSharedPreferences(QuizListActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE));

        String fp = getString(R.string.MainActivity_GreetingTextView_FirstPart);
        String mp = getString(R.string.MainActivity_GreetingTextView_MiddlePart);
        String lp = getString(R.string.MainActivity_GreetingTextView_LastPart);
        String txt = String.format("%s %s.\n%s %d %s", fp, mUser.getFirstName(), mp, mUser.getScore(), lp);

        mGreetingTextView.setText(txt);
        mNameEditText.setText(mUser.getFirstName());
        mNameEditText.setSelection(mUser.getFirstName().length());
        mPlayButton.setEnabled(true);

    }
}