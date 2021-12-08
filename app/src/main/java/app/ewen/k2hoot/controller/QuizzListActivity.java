package app.ewen.k2hoot.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class QuizzListActivity extends AppCompatActivity {
    List<StepContainer> mQuizzList;
    LinearLayout mLinearLayout;

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
        QuestionStep qs = new QuestionStep(question,answers,1);
        steps.add(qs);
        question = "Question 2 : numéro de la question";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,2);
        steps.add(qs);
        StepContainer sc1 = new StepContainer(steps,"Quizz 1");
        steps = new ArrayList<>();
        question = "Question 1 : numéro de la question + 1";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,2);
        steps.add(qs);
        question = "Question 2 : numéro de la question + 1";
        answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        answers.add("4");
        qs = new QuestionStep(question,answers,3);
        steps.add(qs);
        StepContainer sc2 = new StepContainer(steps,"Quizz 2");
        mQuizzList.add(sc1);
        mQuizzList.add(sc2);

        //Ajout des boutons
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mQuizzList.size(); i++) {
            StepContainer sc = mQuizzList.get(i);

            Button btn = new Button(getApplicationContext());
            mLinearLayout.addView(btn,lp);
            btn.setText(sc.getName());
            btn.setTag(sc);

            // Click sur le bouton
            btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    StepContainer sc = (StepContainer) v.getTag();

                    //Intent gameActivityIntent = new Intent(QuizzListActivity.this, GameQuestionActivity.class);
                    //gameActivityIntent.putExtra(GameQuestionActivity.INTENT_QUESTION_STEP, mQuestionStep);
                }
            });
        }
    }
}