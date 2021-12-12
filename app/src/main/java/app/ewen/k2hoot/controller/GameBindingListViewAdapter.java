package app.ewen.k2hoot.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.ewen.k2hoot.R;

// WARNING : Ebauche de code (non finalisé)
public class GameBindingListViewAdapter extends BaseAdapter {
    private Context context;
    private List < String > list;

    LayoutInflater mInflater;
    public GameBindingListViewAdapter(Context context, List < String > list) {
        this.context = context;
        this.list = list;
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
        final BindingViewHolder holder;
        convertView = null;
        if (convertView == null) {
            holder = new BindingViewHolder();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.create_binding_item_view, null);
            holder.caption = (TextView) convertView.findViewById(R.id.game_binding_item_view_text_view_ans);
            holder.caption.setTag(position);
            holder.caption.setText(list.get(position));
            convertView.setTag(holder);
        } else {
            holder = (BindingViewHolder) convertView.getTag();
        }
        int tag_position = (Integer) holder.caption.getTag();
        holder.caption.setId(tag_position);

        return convertView;
    }

}

class BindingViewHolder {
    TextView caption;
}