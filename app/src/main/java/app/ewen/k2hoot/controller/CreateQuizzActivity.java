package app.ewen.k2hoot.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class CreateQuizzActivity extends AppCompatActivity  {

    private EditText mNameEditText;
    private ImageButton mAddImageButton;
    private LinearLayout mLinearLayout;
    private List<Step> mQuestionList;


    private static final int CREATE_QUESTION_ACTIVITY_REQUEST_CODE = 13;
    private Button mCreateButton;
    public static String BUNDLE_QUESTION_STEP_LIST = "BUNDLE_QUESTION_STEP_LIST";
    public static String INTENT_CREATE_STEP_CONTAINER = "INTENT_CREATE_STEP_CONTAINER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz);

        mNameEditText = findViewById(R.id.activity_create_quizz_name_editText);
        mAddImageButton = findViewById(R.id.activity_create_quizz_add_button);
        mLinearLayout = findViewById(R.id.activity_create_quizz_linear_layout_buttons);
        mCreateButton = findViewById(R.id.activity_create_quizz_button_create);

        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent = new Intent(CreateQuizzActivity.this, CreateQuestionActivity.class);
                startActivityForResult(gameActivityIntent, CREATE_QUESTION_ACTIVITY_REQUEST_CODE);
            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setEnabledCreate();
            }
        });


        if (savedInstanceState == null) {

            mQuestionList = new ArrayList<>();
        }else{
            mQuestionList = savedInstanceState.getParcelableArrayList(BUNDLE_QUESTION_STEP_LIST);
        }
        mCreateButton.setEnabled(false);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString();


                StepContainer sc = new StepContainer(mQuestionList, name);
                Intent intent = new Intent();
                intent.putExtra(INTENT_CREATE_STEP_CONTAINER, sc);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void setEnabledCreate(){
        boolean enabled = !mNameEditText.getText().toString().isEmpty();
        enabled &= mQuestionList.size() != 0;
        Log.i("Enabled", enabled+" : Disabled");
        Log.i("Enabled", mNameEditText.getText().toString().isEmpty()+" : void");
        Log.i("Enabled",  mQuestionList.size()+" : 0");
        mCreateButton.setEnabled(enabled);
    }

    private void UpdateView(){
        int count = mLinearLayout.getChildCount();
        View v = null;
        View v2 = null;
        Log.i("QS", "UpdateView\n");
        for(int i=0; i<count; i++) {
            v = mLinearLayout.getChildAt(i);
            if(v.getTag() instanceof View){
                Log.i("QS", "Find\n");
                LinearLayout ll = (LinearLayout) v;
                int count2 = ll.getChildCount();
                for(int j=0; j<count2; j++) {
                    v2 = ll.getChildAt(j);
                    if(v2 instanceof TextView){

                        TextView t = (TextView) v2;
                        Log.i("QS", "Find2\n");
                        Log.i("QS", t.getTag().getClass().getSimpleName());
                        Log.i("QS", t.getText().toString());
                        Log.i("QS", mQuestionList.toString());
                        Log.i("QS", mQuestionList.size()+"");
                        t.setText("<QCM>   Question "+(mQuestionList.indexOf(t.getTag())+1));
                    }
                }

            }
        }
        setEnabledCreate();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BUNDLE_QUESTION_STEP_LIST, (ArrayList<? extends Parcelable>) mQuestionList);
    }
    private void add(Step sc){
//Ajout des boutons

        //LinearLayout.LayoutParams logoTextLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,2);
        LinearLayout.LayoutParams nameTextLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,5);
        LinearLayout.LayoutParams deleteBtnLp = new LinearLayout.LayoutParams(0 * (int)getResources().getDisplayMetrics().density, LinearLayout.LayoutParams.MATCH_PARENT,1);

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setGravity(Gravity.BOTTOM & Gravity.CENTER);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2 * (int)getResources().getDisplayMetrics().density);

        View v = new View(getApplicationContext());
        v.setBackgroundResource(android.R.color.black);
        ll.setTag(v);
        mLinearLayout.addView(ll);
        mLinearLayout.addView(v,viewLp);

        TextView nameText = new TextView(getApplicationContext());
        nameText.setText("Question");
        nameTextLp.setMargins(50,0,0,0);
        nameText.setGravity(Gravity.BOTTOM & Gravity.CENTER);
        ll.addView(nameText,nameTextLp);
        nameText.setTag(sc);

        ImageButton deleteBtn = new ImageButton(getApplicationContext());

        deleteBtnLp.setMargins(50,50,50,50);
        deleteBtn.setImageResource(android.R.drawable.ic_menu_delete);
        ll.addView(deleteBtn,deleteBtnLp);
        deleteBtn.setTag(sc);

        // Click sur le bouton
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

        if(CREATE_QUESTION_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            QuestionStep qs = (QuestionStep)data.getParcelableExtra(CreateQuestionActivity.INTENT_CREATE_QUESTION_STEP);
            Log.i("QS", qs.toString());
            mQuestionList.add(qs);
            add(qs);
            UpdateView();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}