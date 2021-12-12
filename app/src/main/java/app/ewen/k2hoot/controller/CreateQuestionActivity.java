package app.ewen.k2hoot.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class CreateQuestionActivity extends AppCompatActivity  {

    // UI Elements
    private EditText mQuestionEditText;
    private List<EditText> answersEt;
    private List<CheckBox> answersCb;
    private Button mCreateButton;

    // Activity communication
    public static final String INTENT_CREATE_QUESTION_STEP = "INTENT_CREATE_QUESTION_STEP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        // UI Elements
        mQuestionEditText = findViewById(R.id.create_question_activity_edittext_question);

        answersEt = new ArrayList<>();
        answersEt.add(findViewById(R.id.create_question_activity_edittext_answer_1));
        answersEt.add(findViewById(R.id.create_question_activity_edittext_answer_2));
        answersEt.add(findViewById(R.id.create_question_activity_edittext_answer_3));
        answersEt.add(findViewById(R.id.create_question_activity_edittext_answer_4));

        answersCb = new ArrayList<>();
        answersCb.add(findViewById(R.id.create_question_activity_checkbox_answer_1));
        answersCb.add(findViewById(R.id.create_question_activity_checkbox_answer_2));
        answersCb.add(findViewById(R.id.create_question_activity_checkbox_answer_3));
        answersCb.add(findViewById(R.id.create_question_activity_checkbox_answer_4));

        mCreateButton = findViewById(R.id.create_question_activity_button_create);

        // UI Actions

        mCreateButton.setEnabled(false);

        TextWatcher editTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCreateBtnState();
            }
        };

        mQuestionEditText.addTextChangedListener(editTextWatcher);

        for (int i = 0; i < answersEt.size(); i++)
            answersEt.get(i).addTextChangedListener(editTextWatcher);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the question step
                String question = mQuestionEditText.getText().toString();
                ArrayList<String> answers = new ArrayList<>();
                for (int i = 0; i < answersEt.size(); i++)
                    answers.add(answersEt.get(i).getText().toString());
                QuestionStep qs = new QuestionStep(question, answers, getSelectedGoodAnswerIndex());

                // And return it
                Intent intent = new Intent();
                intent.putExtra(INTENT_CREATE_QUESTION_STEP, qs);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        for (int i = 0; i < answersCb.size(); i++) {
            int finalI = i;
            answersCb.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if (isChecked){
                        for (int j = 0; j < answersCb.size(); j++)
                            if (finalI != j)
                                answersCb.get(j).setChecked(false);
                    }
                    updateCreateBtnState();
                }
            });
        }

    }

    // Return true if all the answers are not empty
    private boolean getAllAnswerWrote(){
        for (int i = 0; i < answersEt.size(); i++)
            if (answersEt.get(i).getText().toString().isEmpty())
                return false;
        return true;
    }

    // Return the index of the selected good answer
    private int getSelectedGoodAnswerIndex(){
        for (int i = 0; i < answersCb.size(); i++)
            if (answersCb.get(i).isChecked())
                return i + 1;
        return -1;
    }

    // Return true if a checkbox is ok
    private boolean getIsChecked(){
        return getSelectedGoodAnswerIndex() != -1;
    }

    // Allow the user to click if the conditions are ok
    private void updateCreateBtnState() {
        mCreateButton.setEnabled(!mQuestionEditText.getText().toString().isEmpty() &&  getAllAnswerWrote() && getIsChecked());
    }


}