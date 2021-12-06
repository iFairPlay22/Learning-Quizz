package app.ewen.k2hoot.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import app.ewen.k2hoot.R;

public class CreateBindingActivity extends AppCompatActivity  {

    private EditText mSubjectEditText;
    private NumberPicker mQuestionNumberPicker;
    private ListView mLeftListView;
    private ListView mRightListView;

    private List listLeft;
    private List listRight;
    private List listColor;
    private HashMap<Integer,Integer> bindingMap;
    private CreateBindingListViewAdapter adpterLeft;
    private CreateBindingListViewAdapter adapterRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_binding);


        mSubjectEditText = findViewById(R.id.edit_text_subject);
        mQuestionNumberPicker = findViewById(R.id.numberpicker_question);
        mLeftListView =findViewById(R.id.linearLayout_horizontal_left);
        mRightListView =findViewById(R.id.linearLayout_horizontal_right);

        mQuestionNumberPicker.setMinValue(2);
        mQuestionNumberPicker.setMaxValue(10);
        mQuestionNumberPicker.setWrapSelectorWheel(false);
        setListViewAdapter();

        setListener();


    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void addElementList(){

        listLeft.add("");
        listRight.add("");
        listColor.add(getRandomColor());
    }


    public void removeElementList(int i){
        listLeft.remove(i);
        listRight.remove(i);
        listColor.remove(i);
    }
    private void setListViewAdapter(){


        listLeft = new ArrayList<String>();
        listRight = new ArrayList<String>();
        listColor = new ArrayList<Integer>();

        for (int i = 0; i < 2; i++) {
            addElementList();
        }

        adapterRight=new CreateBindingListViewAdapter(this,listRight);
        adpterLeft=new CreateBindingListViewAdapter(this,listLeft);

        mLeftListView.setItemsCanFocus(true);
        mLeftListView.setAdapter(adpterLeft);

        mRightListView.setItemsCanFocus(true);
        mRightListView.setAdapter(adapterRight);
    }


    private void setListener(){
        mQuestionNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

                    Log.d("VChanged", "1) oldVal :"+oldVal+ " New Val : "+newVal);
                    if(oldVal<newVal){
                        int size = oldVal;

                        for (int i = 0; i < newVal-oldVal; i++) {

                            addElementList();
                        }
                    }else{
                        int size = oldVal-1;

                        for (int i = 0; i < oldVal-newVal; i++) {
                            removeElementList(size-i);
                        }
                    }

            }
        });

        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {
                Log.d("ClickItem", ""+id);
                for (int i = 0; i < mLeftListView.getChildCount(); i++) {
                    if(position == i ){
                        mLeftListView.getChildAt(i).setBackgroundColor((int)listColor.get(position));
                    }else{
                        mLeftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {
                Log.d("ClickItem", ""+id);
                for (int i = 0; i < mRightListView.getChildCount(); i++) {
                    if(position == i ){
                        mRightListView.getChildAt(i).setBackgroundColor((int)listColor.get(position));
                    }else{
                        mRightListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
    }


}