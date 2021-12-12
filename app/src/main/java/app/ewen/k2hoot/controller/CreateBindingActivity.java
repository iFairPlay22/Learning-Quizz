package app.ewen.k2hoot.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import app.ewen.k2hoot.R;
import app.ewen.k2hoot.model.step.binding.BindingStep;

// WARNING : Ebauche de code (non finalisé)
public class CreateBindingActivity extends AppCompatActivity  {

    // UI Elements
    private EditText mSubjectEditText;
    private ListView mLeftListView;
    private ListView mRightListView;
    private CreateBindingListViewAdapter adpterLeft;
    private CreateBindingListViewAdapter adapterRight;
    private Button createButton;

    // Model
    private HashMap<Integer, Integer> hash;
    int lastRight, lastLeft;
    private List<String> listLeft;
    private List<String> listRight;
    private List listColor;
    private HashMap<Integer,Integer> bindingMap;

    // Activity communication
    private static String BUNDLE_BINDING_DATA = "BUNDLE_BINDING_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_binding);

        // UI Elements
        createButton = findViewById(R.id.create_binding_activity_button_create);
        hash = new HashMap<Integer,Integer>();
        mSubjectEditText = findViewById(R.id.edit_text_subject);
        mLeftListView =findViewById(R.id.linearLayout_horizontal_left);
        mRightListView =findViewById(R.id.linearLayout_horizontal_right);

        setListViewAdapter();
        lastLeft=lastRight=-1;
        setListener();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hash2 = new HashMap<>();
                for (Map.Entry<Integer, Integer> entry : hash.entrySet()) {
                    hash2.put(listLeft.get(entry.getKey()), listRight.get(entry.getValue()));
                }
                Intent intent = new Intent();
                intent.putExtra(BUNDLE_BINDING_DATA, new BindingStep(mSubjectEditText.getText().toString(), hash2));
                setResult(RESULT_OK, intent);
                finish();
            }
        });


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

        for (int i = 0; i < 4; i++) {
            addElementList();
        }

        adapterRight=new CreateBindingListViewAdapter(this,listRight);
        adpterLeft=new CreateBindingListViewAdapter(this,listLeft);

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
            public void onItemClick(AdapterView<?> adpterView, View view, int position, long id) {
                if (lastRight != -1) {
                    hash.put(position,lastRight);
                    lastRight = -1;
                    updateColors();
                } else {
                    lastLeft = position;
                }
            }
        });

        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position, long id) {
                if (lastLeft != -1) {
                    hash.put(lastLeft,position);
                    lastLeft = -1;
                    updateColors();
                } else {
                    lastRight = position;
                }
            }
        });
    }
}