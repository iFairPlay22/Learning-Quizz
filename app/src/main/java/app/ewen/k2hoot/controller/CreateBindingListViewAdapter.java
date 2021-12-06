package app.ewen.k2hoot.controller;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import app.ewen.k2hoot.R;

public class CreateBindingListViewAdapter extends BaseAdapter implements View.OnTouchListener {
    private Context context;
    private List list;

    LayoutInflater mInflater;
    public CreateBindingListViewAdapter(Context context,List list){
        this.context = context;
        this.list  =list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        convertView=null;
        if (convertView == null) {
            holder = new ViewHolder();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.create_binding_item_view, null);
            holder.caption = (EditText) convertView
                    .findViewById(R.id.edit_text_question);
            holder.caption.setTag(position);
            holder.caption.setText(""+position);
            holder.caption.setOnTouchListener(this);
            convertView.setOnTouchListener(this);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        int tag_position=(Integer) holder.caption.getTag();
        holder.caption.setId(tag_position);

        holder.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final int position2 = holder.caption.getId();
                final EditText Caption = (EditText) holder.caption;
                if(Caption.getText().toString().length()>0){
                    list.set(position2,Caption.getText().toString());
                }else{
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        return convertView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.caption.setFocusable(false);
            holder.caption.setFocusableInTouchMode(false);
        }
        return false;
    }
}

class ViewHolder {
    EditText caption;
}
