package test.invoicegenerator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.invoicegenerator.R;
import test.invoicegenerator.model.Country;

/**
 * Created by User on 10/30/2018.
 */

public class CountryAdapter extends ArrayAdapter<Country>  {

    private Context mContext;
    private List<Country> moviesList = new ArrayList<>();

    public CountryAdapter(@NonNull Context context, ArrayList<Country> list) {
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

        Country currentMovie = moviesList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.text);
        name.setText(currentMovie.getName());


        return listItem;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}

