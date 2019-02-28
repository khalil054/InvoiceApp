package test.invoicegenerator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.invoicegenerator.R;

/**
 * Created by User on 10/31/2018.
 */

public class SimpleAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> moviesList = new ArrayList<>();

    public SimpleAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0 , list);
        mContext = context;
        moviesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.spinner_item,parent,false);

        //StateModel currentMovie = moviesList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.text);
        name.setText(/*currentMovie.getName()*/moviesList.get(position));


        return listItem;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}

