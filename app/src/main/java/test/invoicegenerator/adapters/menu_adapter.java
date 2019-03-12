package test.invoicegenerator.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;


/**
 * Created by mac on 4/12/18.
 */

public class menu_adapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<Integer> Pic = new ArrayList<Integer>();
    int index;



    public menu_adapter(Activity context, ArrayList<String> Name, ArrayList<Integer> Images, int index )
    {
        super(context, R.layout.menu_item);
        this.context = context;
        this.Name = Name;
        this.Pic = Images;
        this.index = index;




    }

    @Override
    public int getCount() {
        return Name.size();
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.menu_item, null, true);

        ImageView menu_icon = (ImageView) rowView.findViewById(R.id.menu_icon);
        TextView menu_text = (TextView) rowView.findViewById(R.id.menu_text);

        if(position == index)
        {
            menu_text.setTextColor(Color.parseColor("#FF102644"));
        }else {

            menu_text.setTextColor(Color.parseColor("#898992"));
        }



        menu_icon.setImageDrawable(rowView.getResources().getDrawable(Pic.get(position)));
        menu_text.setText(Name.get(position));

        return rowView;
    }









}
