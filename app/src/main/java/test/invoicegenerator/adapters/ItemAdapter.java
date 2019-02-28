package test.invoicegenerator.adapters;

/**
 * Created by User on 1/29/2019.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.Item;

public class ItemAdapter extends BaseAdapter {
    private final Context context;
    ArrayList<Item> reportModelArrayList = new ArrayList<Item>();
    int index = 0;
    String Day = "";

    public ItemAdapter(Context context, ArrayList<Item> reportModelArrayList) {
        // super(context, -1, values);
        this.context = context;
        this.reportModelArrayList = reportModelArrayList;
    }

    @Override
    public int getCount() {
        return reportModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // position=position-1;
        if (convertView != null) return convertView;
        Item reportModel1 = reportModelArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

       /* if (!(Day.equals(day))) {

               // Day = reportModel1.getDay();
                rowView = inflater.inflate(R.layout.report_list_header, parent, false);

                TextView header_txt = (TextView) rowView.findViewById(R.id.header_txt);
                header_txt.setText(Day);
                index = index + 1;

            }else */
        {

            //  ReportModel reportModel2 = reportModelArrayList.get(position);
            rowView = inflater.inflate(R.layout.item_detail, parent, false);
            TextView name_txt = (TextView) rowView.findViewById(R.id.description);
            TextView quantity = (TextView) rowView.findViewById(R.id.quantity);
            TextView unit_cost = (TextView) rowView.findViewById(R.id.unit_cost);
            TextView amount = (TextView) rowView.findViewById(R.id.amount);

            name_txt.setText(reportModel1.getDescription());
            // no_txt.setText(reportModel1.);
            quantity.setText(reportModel1.getQuantity()+"x");
            unit_cost.setText(reportModel1.getUnit_cost());
            amount.setText(reportModel1.getAmount());

        }


        return rowView;
    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }
}