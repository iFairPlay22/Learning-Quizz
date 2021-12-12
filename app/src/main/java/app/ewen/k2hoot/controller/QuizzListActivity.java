package app.ewen.k2hoot.controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.User;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class QuizzListActivity extends AppCompatActivity {
    private List<StepContainer> mQuizzList;
    private LinearLayout mLinearLayout;
    private Button mCreateButton;
    public static String BUNDLE_STEP_CONTAINER_LIST = "BUNDLE_STEP_CONTAINER_LIST";
    private static final int CREATE_QUIZZ_ACTIVITY_REQUEST_CODE = 14;
    public static final String SHARED_PREF_QUIZZ_LIST = "SHARED_PREF_QUIZZ_LIST";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_list);

        if (savedInstanceState == null) {
            mQuizzList = new ArrayList<>();
        }else{
            mQuizzList = savedInstanceState.getParcelableArrayList(BUNDLE_STEP_CONTAINER_LIST);
        }
        mLinearLayout = findViewById(R.id.quizz_list_linear_layout_buttons);

        mCreateButton = findViewById(R.id.quizz_list_activity_create_button);
        //Création du stepcontainer de test
        List<Step> steps = new ArrayList<>();
        String question = "Question 1 : numéro de la question";
        ArrayList<String> answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        QuestionStep qs = new QuestionStep(question,answers,0);
        steps.add(qs);
        question = "Question 2 : numéro de la question";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,1);
        steps.add(qs);
        StepContainer sc1 = new StepContainer(steps,"Quizz 1");
        steps = new ArrayList<>();
        question = "Question 1 : numéro de la question + 1";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,1);
        steps.add(qs);
        question = "Question 2 : numéro de la question + 1";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,2);
        steps.add(qs);
        StepContainer sc2 = new StepContainer(steps,"Quizz 2");
        //sc1.addToSharedPreferences(getSharedPreferences(SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE));
        //sc2.addToSharedPreferences(getSharedPreferences(SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE));

        /*mQuizzList.add(sc1);
        mQuizzList.add(sc2);*/

        mQuizzList.addAll(StepContainer.readAllFromSharedPreferences(getSharedPreferences(SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE)));
        Log.i("StepCo" , StepContainer.readAllFromSharedPreferences(getSharedPreferences(SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE)).toString());
        //Ajout des boutons
        for (int i = 0; i < mQuizzList.size(); i++) {
            StepContainer sc = mQuizzList.get(i);

            add(sc);
        }


        mCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gameActivityIntent = new Intent(QuizzListActivity.this, CreateQuizzActivity.class);
                startActivityForResult(gameActivityIntent, CREATE_QUIZZ_ACTIVITY_REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BUNDLE_STEP_CONTAINER_LIST, (ArrayList<? extends Parcelable>) mQuizzList);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(CREATE_QUIZZ_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            StepContainer qs = (StepContainer)data.getParcelableExtra(CreateQuizzActivity.INTENT_CREATE_STEP_CONTAINER);
            if(qs != null){
                add(qs);
                qs.addToSharedPreferences(getSharedPreferences(SHARED_PREF_QUIZZ_LIST, MODE_PRIVATE));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void add(StepContainer sc){


        LinearLayout.LayoutParams quizzBtnLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,5);
        //quizzBtnLp.rightMargin = 128 * (int)getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams shareBtnLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,1);


        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.addView(ll);

        Button quizzBtn = new Button(new ContextThemeWrapper(getBaseContext(),R.style.Theme_K2hoot));
        quizzBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_700)));
        quizzBtn.setTextColor(getResources().getColor(R.color.white));
        quizzBtn.setText(sc.getName());
        ll.addView(quizzBtn,quizzBtnLp);
        quizzBtn.setTag(sc);

        ImageButton shareBtn = new ImageButton(getApplicationContext());
        shareBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        shareBtn.setImageResource(android.R.drawable.ic_menu_set_as);
        ll.addView(shareBtn,shareBtnLp);
        shareBtn.setTag(sc);

        // Click sur le bouton
        quizzBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                StepContainer sc = (StepContainer) quizzBtn.getTag();
                Log.d("sc",sc.toString());

                Intent gameActivityIntent = new Intent(QuizzListActivity.this, GameActivity.class);
                gameActivityIntent.putExtra(GameActivity.INTENT_EXTRA_STEP_CONTAINER, sc);
                startActivity(gameActivityIntent);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
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