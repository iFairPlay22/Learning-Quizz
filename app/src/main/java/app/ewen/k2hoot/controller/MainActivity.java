package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashMap;
import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.User;
import app.ewen.k2hoot.model.http.HttpManager;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import app.ewen.k2hoot.model.step.binding.BindingStep;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_ACTIVITY_REQUEST_CODE = 2;
    private static final int GAME_QUESTION_ACTIVITY_REQUEST_CODE = 3;
    private static final int CREATE_QUESTION_ACTIVITY_REQUEST_CODE = 13;
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_FIRST_NAME = "SHARED_PREF_USER_FIRST_NAME";
    private static final String SHARED_PREF_USER_LAST_SCORE = "SHARED_PREF_USER_LAST_SCORE";
    private static final String SHARED_PREF_GAME = "SHARED_PREF_GAME_";
    private static final String SHARED_PREF_GAME_NB = "SHARED_PREF_GAME_NB";

    public static final String BUNDLE_STATE_USER = "BUNDLE_STATE_USER";

    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private Button mCreateButton;
    private Button mCreateBindingButton;
    private Button mPlayBindingButton;
    private Button mCreateGapSentenceButton;
    private Button mQuizzListButton;

    private User mUser;
    private QuestionStep mQuestionStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpManager.testFileUpload(this);

        if (savedInstanceState == null) {
            mUser = new User();
        } else {
            mUser = savedInstanceState.getParcelable(BUNDLE_STATE_USER);
        }

        // Initialisation
        mGreetingTextView = findViewById(R.id.main_text_view_greeting);
        mNameEditText = findViewById(R.id.main_edit_text_name);
        mPlayButton = findViewById(R.id.main_button_play);
        mCreateButton = findViewById(R.id.main_button_create);
        mCreateBindingButton = findViewById(R.id.main_button_create_binding);
        mPlayBindingButton = findViewById(R.id.main_button_play_binding);
        mCreateGapSentenceButton = findViewById(R.id.main_button_create_gap_sentence);
        mQuizzListButton = findViewById(R.id.main_button_quizz_list);

        // Activation / Désactivation du bouton
        mPlayButton.setEnabled(false);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });

        // Click sur le bouton
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent gameActivityIntent = new Intent(MainActivity.this, GameQuestionActivity.class);
                gameActivityIntent.putExtra(GameQuestionActivity.INTENT_QUESTION_STEP, mQuestionStep);
                startActivityForResult(gameActivityIntent, GAME_QUESTION_ACTIVITY_REQUEST_CODE);
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent gameActivityIntent = new Intent(MainActivity.this, CreateQuestionActivity.class);
                startActivityForResult(gameActivityIntent, CREATE_QUESTION_ACTIVITY_REQUEST_CODE);
            }
        });
        mPlayBindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());
                HashMap<String,String> s = new HashMap<>();
                s.put("B", "Voitrue");
                s.put("C", "Bateau");
                s.put("A2", "Moto");
                s.put("D", "Camion");
                BindingStep st = new BindingStep("permis",s );
                Intent gameActivityIntent = new Intent(MainActivity.this, GameBindingActivity.class);
                gameActivityIntent.putExtra(GameBindingActivity.INTENT_INPUT_BINDING_STEP, st);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
        mCreateBindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent gameActivityIntent = new Intent(MainActivity.this, CreateBindingActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
        mCreateGapSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent gameActivityIntent = new Intent(MainActivity.this, CreateGapSentenceActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mQuizzListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent quizzListIntent = new Intent(MainActivity.this, QuizzListActivity.class);
                startActivity(quizzListIntent);
            }
        });


        // Modification des textes
        updateLabels();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_STATE_USER, mUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            String firstName = mUser.getFirstName();
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_USER, 0);
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_USER_FIRST_NAME, firstName)
                .putInt(SHARED_PREF_USER_LAST_SCORE, score)
                .apply();

            updateLabels();
        }else if(CREATE_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            QuestionStep qs = (QuestionStep)data.getParcelableExtra(CreateQuestionActivity.INTENT_CREATE_QUESTION_STEP);
            Log.i("QS", qs.toString());
            mQuestionStep=qs;
        }else if(GAME_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            boolean increase = data.getBooleanExtra(GameQuestionActivity.BUNDLE_EXTRA_VALIDATE, false);
            Log.i("QS","Increse "+increase);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabels() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
        String lastFirstName = sharedPreferences.getString(SHARED_PREF_USER_FIRST_NAME, null);
        int lastScore = sharedPreferences.getInt(SHARED_PREF_USER_LAST_SCORE, -1);
        if (lastFirstName != null && lastScore != -1) {

            String fp = getString(R.string.MainActivity_GreetingTextView_FirstPart);
            String mp = getString(R.string.MainActivity_GreetingTextView_MiddlePart);
            String lp = getString(R.string.MainActivity_GreetingTextView_LastPart);
            String txt = String.format("%s %s.\n%s %d %s", fp, lastFirstName, mp, lastScore, lp);

            mGreetingTextView.setText(txt);
            mNameEditText.setText(lastFirstName);
            mNameEditText.setSelection(lastFirstName.length());
            mPlayButton.setEnabled(true);
        }
    }

    private void shareGame() {
        QuestionStep q1 = new QuestionStep("Question 1?", Arrays.asList("Answer 1.1", "Answer 1.2", "Answer 1.3", "Answer 1.4"), 0);
        QuestionStep q2 = new QuestionStep("Question 2?", Arrays.asList("Answer 2.1", "Answer 2.2", "Answer 2.3", "Answer 2.4"), 1);
        StepContainer sc = new StepContainer(Arrays.asList(q1, q2),"");

        int nSteps = 1;

        // Shared Preferences
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_USER_INFO + "_" + sc.getId(), MODE_PRIVATE);

        // Write
        sp
            .edit()
            .putInt(SHARED_PREF_GAME_NB, nSteps)
            .putString(SHARED_PREF_GAME, sc.toJson())
            .apply();

        // Read
        int gameNb = sp.getInt(SHARED_PREF_GAME_NB, 0);

        for (int i = 0; i < gameNb; i++) {
            String jsonTxt = sp.getString(SHARED_PREF_GAME + "_" + i, "");
            StepContainer sc2 = StepContainer.loadFromJson(jsonTxt);
            if (sc2 != null) Log.i("file_debug", sc2.toString());
        }
    }
}