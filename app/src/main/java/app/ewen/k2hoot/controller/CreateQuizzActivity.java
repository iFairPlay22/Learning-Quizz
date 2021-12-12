package app.ewen.k2hoot.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class CreateQuizzActivity extends AppCompatActivity  {

    // UI Elements
    private EditText mNameEditText;
    private LinearLayout mLinearLayout;
    private ImageButton mAddStepButton;
    private Button mCreateButton;

    // Model
    private List<Step> mQuestionList;

    // Activity communication
    public static String BUNDLE_QUESTION_STEP_LIST = "BUNDLE_QUESTION_STEP_LIST";
    public static String INTENT_CREATE_STEP_CONTAINER = "INTENT_CREATE_STEP_CONTAINER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        // Model
        if (savedInstanceState == null) {
            mQuestionList = new ArrayList<>();
        } else {
            mQuestionList = savedInstanceState.getParcelableArrayList(BUNDLE_QUESTION_STEP_LIST);
        }

        // UI Elements
        mNameEditText = findViewById(R.id.create_quiz_activity_name_editText);
        mAddStepButton = findViewById(R.id.create_quiz_activity_add_button);
        mLinearLayout = findViewById(R.id.create_quiz_activity_linear_layout_buttons);
        mCreateButton = findViewById(R.id.create_quiz_activity_button_create);

        // UI Actions
        mNameEditText.addTextChangedListener(new TextWatcher() {
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
        });

        mAddStepButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                // Launch create question view
                Intent gameActivityIntent = new Intent(CreateQuizzActivity.this, CreateQuestionActivity.class);
                startActivityForResult(gameActivityIntent, MainActivity.CREATE_QUESTION_ACTIVITY_REQUEST_CODE);
            }
        });

        mCreateButton.setEnabled(false);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the quiz
                String name = mNameEditText.getText().toString();
                StepContainer sc = new StepContainer(mQuestionList, name);

                // Return it
                Intent intent = new Intent();
                intent.putExtra(INTENT_CREATE_STEP_CONTAINER, sc);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    // Make available the create quiz button
    private void updateCreateBtnState(){
        mCreateButton.setEnabled(!mNameEditText.getText().toString().isEmpty() & mQuestionList.size() != 0);
    }

    // Display all the questions
    private void UpdateView(){
        int count = mLinearLayout.getChildCount();
        int index = 0;

        for (int i = 0; i < count; i++) {

            View v = mLinearLayout.getChildAt(i);
            if(v.getTag() instanceof View){

                LinearLayout ll = (LinearLayout) v;
                int count2 = ll.getChildCount();

                for (int j = 0; j < count2; j++) {

                    View v2 = ll.getChildAt(j);
                    if(v2 instanceof TextView){

                        TextView t = (TextView) v2;
                        t.setText("Question " + (index++));
                        t.setTextColor(Color.WHITE);

                    }
                }

            }
        }
        updateCreateBtnState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BUNDLE_QUESTION_STEP_LIST, (ArrayList<? extends Parcelable>) mQuestionList);
    }

    // Display a new step in the linear layout
    private void addStepToLinearLayout(Step sc){

        LinearLayout.LayoutParams nameTextLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,5);
        LinearLayout.LayoutParams deleteBtnLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setGravity(Gravity.BOTTOM & Gravity.CENTER);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2 * (int)getResources().getDisplayMetrics().density);

        View v = new View(getApplicationContext());
        v.setBackgroundResource(android.R.color.black);
        ll.setTag(v);
        mLinearLayout.addView(ll);
        mLinearLayout.addView(v,viewLp);

        // Name text
        TextView nameText = new TextView(getApplicationContext());
        nameText.setText("Question");
        nameTextLp.setMargins(50,0,0,0);
        nameText.setGravity(Gravity.BOTTOM & Gravity.CENTER);
        ll.addView(nameText,nameTextLp);
        nameText.setTag(sc);

        // Delete step button
        ImageButton deleteBtn = new ImageButton(getApplicationContext());
        deleteBtnLp.setMargins(50,50,50,50);
        deleteBtn.setImageResource(android.R.drawable.ic_menu_delete);
        ll.addView(deleteBtn,deleteBtnLp);
        deleteBtn.setTag(sc);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Step sc = (Step) deleteBtn.getTag();
                Log.d("sc",sc.toString());

                mQuestionList.remove(sc);
                mLinearLayout.removeView(ll);
                mLinearLayout.removeView((View)ll.getTag());
                UpdateView();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (MainActivity.CREATE_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Get the created question and add it
            QuestionStep qs = (QuestionStep)data.getParcelableExtra(CreateQuestionActivity.INTENT_CREATE_QUESTION_STEP);
            mQuestionList.add(qs);

            // Update display
            addStepToLinearLayout(qs);
            UpdateView();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}