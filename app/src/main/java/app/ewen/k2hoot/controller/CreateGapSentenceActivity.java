package app.ewen.k2hoot.controller;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.R;

// WARNING : Ebauche de code (non finalisé)
public class CreateGapSentenceActivity extends AppCompatActivity {
    //Elements graphiques
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private TextView mPreview;
    private Button mOkButton;

    //Phrase sous forme de liste de mots
    private String[] mots;
    //Index dans la phrase des mots à compléter
    private List < Integer > gapIndexes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gap_sentence);

        mEditText = findViewById(R.id.gap_sentence_activity_sentance_edit_text);
        mLinearLayout = findViewById(R.id.binding_create_activity_linear_layout_buttons);
        mPreview = findViewById(R.id.binding_create_activity_gap_sentence_preview);
        mOkButton = findViewById(R.id.binding_create_activity_ok_button);

        mOkButton.setEnabled(false);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable s) {
                //Reset
                mLinearLayout.removeAllViews();
                gapIndexes = new ArrayList < > ();

                //Traitement de la chaine de caractères
                String str = s.toString();
                if (str.length() == 0) {
                    mPreview.setText("");
                    return;
                }

                mots = str.split(" ");

                //Ajout des boutons
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for (int i = 0; i < mots.length; i++) {
                    String mot = mots[i].trim();
                    if (mot.length() == 0) continue;

                    Button btn = new Button(getApplicationContext());
                    mLinearLayout.addView(btn, lp);
                    btn.setText(mot);

                    // Click sur le bouton
                    int finalI = i;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            if (gapIndexes.contains(finalI)) {
                                gapIndexes.remove((Integer) finalI);
                                btn.setTextColor(Color.WHITE);
                                updatePreview();
                            } else {
                                gapIndexes.add(finalI);
                                btn.setTextColor(Color.RED);
                                updatePreview();
                            }
                        }
                    });
                }
                updatePreview();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePreview() {
        if (mots == null) return;
        if (gapIndexes.size() != 0) {
            mOkButton.setEnabled(true);
        } else {
            mOkButton.setEnabled(false);
        }
        String[] motsPreview = mots.clone();
        for (Integer i: gapIndexes) {
            motsPreview[i] = "...";
        }
        mPreview.setText(getString(R.string.GreateGapSentanceActivity_Preview_Label) + String.join(" ", motsPreview));
    }
}