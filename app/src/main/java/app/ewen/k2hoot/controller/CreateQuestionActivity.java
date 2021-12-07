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

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class CreateQuestionActivity extends AppCompatActivity  {

    private EditText mQuestionEditText;
    private EditText mAnswer1EditText;
    private EditText mAnswer2EditText;
    private EditText mAnswer3EditText;
    private EditText mAnswer4EditText;

    private CheckBox mAnswer1CheckBox;
    private CheckBox mAnswer2CheckBox;
    private CheckBox mAnswer3CheckBox;
    private CheckBox mAnswer4CheckBox;


    private Button mCreateButton;

    public static String INTENT_CREATE_QUESTION_STEP = "INTENT_CREATE_QUESTION_STEP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        mQuestionEditText = findViewById(R.id.create_question_activity_edittext_question);
        mAnswer1EditText = findViewById(R.id.create_question_activity_edittext_answer_1);
        mAnswer2EditText = findViewById(R.id.create_question_activity_edittext_answer_2);
        mAnswer3EditText = findViewById(R.id.create_question_activity_edittext_answer_3);
        mAnswer4EditText = findViewById(R.id.create_question_activity_edittext_answer_4);

        mAnswer1CheckBox = findViewById(R.id.create_question_activity_checkbox_answer_1);
        mAnswer2CheckBox = findViewById(R.id.create_question_activity_checkbox_answer_2);
        mAnswer3CheckBox = findViewById(R.id.create_question_activity_checkbox_answer_3);
        mAnswer4CheckBox = findViewById(R.id.create_question_activity_checkbox_answer_4);


        mCreateButton = findViewById(R.id.create_question_activity_button_create);
        mCreateButton.setEnabled(false);
        mQuestionEditText.addTextChangedListener(editTexWatcher);
        mAnswer1EditText.addTextChangedListener(editTexWatcher);
        mAnswer2EditText.addTextChangedListener(editTexWatcher);
        mAnswer3EditText.addTextChangedListener(editTexWatcher);
        mAnswer4EditText.addTextChangedListener(editTexWatcher);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = mQuestionEditText.getText().toString();

                ArrayList<String> answers = new ArrayList<>();
                answers.add(mAnswer1EditText.getText().toString());
                answers.add(mAnswer2EditText.getText().toString());
                answers.add(mAnswer3EditText.getText().toString());
                answers.add(mAnswer4EditText.getText().toString());


                QuestionStep qs = new QuestionStep(question,answers,getGoodAnswerIndex());
                Intent intent = new Intent();
                intent.putExtra(INTENT_CREATE_QUESTION_STEP, qs);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mAnswer1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if(isChecked){
                        mAnswer2CheckBox.setChecked(false);
                        mAnswer3CheckBox.setChecked(false);
                        mAnswer4CheckBox.setChecked(false);
                    }
                }
        });

        mAnswer2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    mAnswer1CheckBox.setChecked(false);
                    mAnswer3CheckBox.setChecked(false);
                    mAnswer4CheckBox.setChecked(false);
                }
            }
        });

        mAnswer3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    mAnswer1CheckBox.setChecked(false);
                    mAnswer2CheckBox.setChecked(false);
                    mAnswer4CheckBox.setChecked(false);
                }
            }
        });

        mAnswer4CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    mAnswer1CheckBox.setChecked(false);
                    mAnswer2CheckBox.setChecked(false);
                    mAnswer3CheckBox.setChecked(false);
                }
            }
        });

    }

    private boolean hasGoodAnswer(){

        return mAnswer1CheckBox.isChecked() || mAnswer2CheckBox.isChecked() || mAnswer3CheckBox.isChecked() || mAnswer4CheckBox.isChecked();
    }

    private int getGoodAnswerIndex(){
        if(mAnswer1CheckBox.isChecked()){
            return 1;
        }else if(mAnswer2CheckBox.isChecked()){
            return 2;
        }else if(mAnswer3CheckBox.isChecked()){
            return 3;
        }else{
            return 4;
        }
    }

    private TextWatcher editTexWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean enabled = s.toString().isEmpty();
            enabled |= mAnswer1EditText.getText().toString().isEmpty();
            enabled |= mAnswer2EditText.getText().toString().isEmpty();
            enabled |= mAnswer3EditText.getText().toString().isEmpty();
            enabled |= mAnswer4EditText.getText().toString().isEmpty();
            enabled |= !hasGoodAnswer();
            mCreateButton.setEnabled(!enabled);
        }
    };


}