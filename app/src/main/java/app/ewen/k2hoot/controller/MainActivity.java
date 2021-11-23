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

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.User;
import app.ewen.k2hoot.model.json.IJson;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_ACTIVITY_REQUEST_CODE = 2;
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_FIRST_NAME = "SHARED_PREF_USER_FIRST_NAME";
    private static final String SHARED_PREF_USER_LAST_SCORE = "SHARED_PREF_USER_LAST_SCORE";

    public static final String BUNDLE_STATE_USER = "BUNDLE_STATE_USER";

    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private Button mCreateButton;
    private Button mCreateBindingButton;
    private Button mCreateGapSentenceButton;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IJson.testJson();

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
        mCreateGapSentenceButton = findViewById(R.id.main_button_create_gap_sentence);

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

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString());

                Intent gameActivityIntent = new Intent(MainActivity.this, CreateQuestionActivity.class);
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
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_USER_FIRST_NAME, firstName)
                .putInt(SHARED_PREF_USER_LAST_SCORE, score)
                .apply();

            updateLabels();
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
}