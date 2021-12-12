package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.http.response.HttpFile;

public class QuizListActivity extends AppCompatActivity {

    // UI Elements
    private LinearLayout mLinearLayout;
    private Button mCreateButton;

    // Model
    private List<StepContainer> mQuizList;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        StepContainer.saveStepContainerListInBundle(outState, mQuizList);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_list);

        // Model
        mQuizList = StepContainer.getStepContainerListFromBundle(savedInstanceState);
        mQuizList.addAll(StepContainer.readAllFromSharedPreferences(getSharedPreferences(MainActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE)));

        // UI Elements
        mLinearLayout = findViewById(R.id.quizz_list_linear_layout_buttons);
        mCreateButton = findViewById(R.id.quizz_list_activity_create_button);

        // UI Actions
        for (int i = 0; i < mQuizList.size(); i++) {
            StepContainer sc = mQuizList.get(i);
            addQuizToLinearLayout(sc);
        }

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Launch the create quiz view
                Intent gameActivityIntent = new Intent(QuizListActivity.this, CreateQuizzActivity.class);
                startActivityForResult(gameActivityIntent, MainActivity.CREATE_QUIZ_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (MainActivity.CREATE_QUIZ_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            // A new quiz has been created, add it to the list
            StepContainer qs = (StepContainer)data.getParcelableExtra(CreateQuizzActivity.INTENT_CREATE_STEP_CONTAINER);
            if(qs != null) {
                addQuizToLinearLayout(qs);
                qs.addToSharedPreferences(getSharedPreferences(MainActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    // Add a quiz to the LinearLayout
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addQuizToLinearLayout(StepContainer sc){

        LinearLayout.LayoutParams quizBtnLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,5);
        LinearLayout.LayoutParams shareBtnLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.addView(ll);

        // Quiz button
        Button quizBtn = new Button(new ContextThemeWrapper(getBaseContext(),R.style.Theme_K2hoot));
        quizBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
        quizBtn.setTextColor(getResources().getColor(R.color.white));
        quizBtn.setText(sc.getName());
        ll.addView(quizBtn, quizBtnLp);
        quizBtn.setTag(sc);
        quizBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Play a quiz
                StepContainer sc = (StepContainer) quizBtn.getTag();
                Intent gameActivityIntent = new Intent(QuizListActivity.this, GameActivity.class);
                gameActivityIntent.putExtra(GameActivity.INTENT_EXTRA_STEP_CONTAINER, sc);
                startActivity(gameActivityIntent);
            }
        });

        // Share Button
        ImageButton shareBtn = new ImageButton(getApplicationContext());
        shareBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        shareBtn.setImageResource(android.R.drawable.ic_menu_set_as);
        ll.addView(shareBtn,shareBtnLp);
        shareBtn.setTag(sc);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Share a quiz
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                HttpFile h = sc.storeInServer(getActivity());
                sendIntent.putExtra(Intent.EXTRA_TEXT,"http://www.k2hoot.com/load?key="+h.getKey());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }

    public AppCompatActivity getActivity() {
        return this;
    }
}