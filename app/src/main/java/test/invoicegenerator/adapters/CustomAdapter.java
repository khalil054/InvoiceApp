package test.invoicegenerator.adapters;

/*
 * Created by User on 9/14/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.model.User;

public class CustomAdapter extends BaseAdapter{
    private final Context context;
    private final ArrayList<User> values;

    public CustomAdapter(Context context, ArrayList<User> values) {
       // super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView textView = rowView.findViewById(R.id.label);
        ImageView imageView =rowView.findViewById(R.id.icon);
        textView.setText(values.get(position).getName());
        // change the icon for Windows and iPhone
        String s = "";


        return rowView;
    }
}
