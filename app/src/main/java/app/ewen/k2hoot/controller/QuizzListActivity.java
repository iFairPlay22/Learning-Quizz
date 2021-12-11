package app.ewen.k2hoot.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_list);

        mQuizzList = new ArrayList<>();
        mLinearLayout = findViewById(R.id.quizz_list_linear_layout_buttons);


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
        mQuizzList.add(sc1);
        mQuizzList.add(sc2);

        //Ajout des boutons

        LinearLayout.LayoutParams quizzBtnLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,5);
        //quizzBtnLp.rightMargin = 128 * (int)getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams shareBtnLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,1);
        for (int i = 0; i < mQuizzList.size(); i++) {
            StepContainer sc = mQuizzList.get(i);

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
    }

    public AppCompatActivity getActivity() {
        return this;
    }
}