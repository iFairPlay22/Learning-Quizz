package app.ewen.k2hoot.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.binding.BindingStep;

public class GameBindingActivity extends AppCompatActivity  {

    private TextView mSubjectTextView;
    private ListView mLeftListView;
    private ListView mRightListView;
    private HashMap<Integer, Integer> hash;
    int lastRight, lastLeft;
    private List listLeft;
    private List listRight;
    private List listColor;
    private HashMap<Integer,Integer> bindingMap;
    private GameBindingListViewAdapter adpterLeft;
    private GameBindingListViewAdapter adapterRight;

    private BindingStep bindingStep;

    public static String INTENT_INPUT_BINDING_STEP = "INTENT_INPUT_BINDING_STEP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_binding);

        hash = new HashMap<Integer,Integer>();
        mSubjectTextView = findViewById(R.id.text_view_dubject);
        mLeftListView =findViewById(R.id.linearLayout_horizontal_left);
        mRightListView =findViewById(R.id.linearLayout_horizontal_right);


        Intent intent = getIntent();
        if(savedInstanceState == null){

        }

        Log.i("Binding3", "k");
        bindingStep = (BindingStep)intent.getParcelableExtra(INTENT_INPUT_BINDING_STEP);
        setListViewAdapter();
        lastLeft=lastRight=-1;
        setListener();
        Log.i("Binding3", bindingStep.getBindingMap().toString());
        Log.i("Binding3", "c");



    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void addElementList(String left, String Right){

        listLeft.add(left);
        listRight.add(Right);
        listColor.add(getRandomColor());
    }

    private void setListViewAdapter(){


        listLeft = new ArrayList<String>();
        listRight = new ArrayList<String>();
        listColor = new ArrayList<Integer>();

        for (Map.Entry<String, String> entry : bindingStep.getBindingMap().entrySet()) {
            addElementList(entry.getKey(),entry.getValue());
        }

        adapterRight=new GameBindingListViewAdapter(this,listRight);
        adpterLeft=new GameBindingListViewAdapter(this,listLeft);

        mLeftListView.setItemsCanFocus(true);
        mLeftListView.setAdapter(adpterLeft);

        mRightListView.setItemsCanFocus(true);
        mRightListView.setAdapter(adapterRight);
    }

    private void updateColors(){
        for (Map.Entry<Integer, Integer> entry : hash.entrySet()) {
            mLeftListView.getChildAt(entry.getKey()).setBackgroundColor((int)listColor.get(entry.getKey()));
            mRightListView.getChildAt(entry.getValue()).setBackgroundColor((int)listColor.get(entry.getKey()));
        }
    }
    private void setListener(){


        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {

                    //mLeftListView.getChildAt(position).setBackgroundColor((int)listColor.get(position));
                    if(lastRight != -1){
                        hash.put(position,lastRight);
                        lastRight = -1;
                        updateColors();
                    }else{

                    lastLeft = position;
                    }
            }


        });

        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {

                        if(lastLeft != -1){
                            hash.put(lastLeft,position);
                            lastLeft = -1;
                            updateColors();
                        }else{

                        lastRight = position;
                        }
            }
        });
    }


}