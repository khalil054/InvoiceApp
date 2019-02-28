package test.invoicegenerator.adapters;

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
import test.invoicegenerator.model.InvoiceModel;

public class ReportAdapter extends BaseAdapter {
    private final Context context;
    ArrayList<InvoiceModel> reportModelArrayList = new ArrayList<InvoiceModel>();
    int index = 0;
    String Day = "";

    public ReportAdapter(Context context, ArrayList<InvoiceModel> reportModelArrayList) {
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
        InvoiceModel reportModel1 = reportModelArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

       /* if (!(Day.equals(day))) {

               // Day = reportModel1.getDay();
                rowView = inflater.inflate(R.layout.report_list_header, parent, false);

                TextView header_txt = (TextView) rowView.findViewById(R.id.header_txt);
                header_txt.setText(Day);
                index = index + 1;

            }else */{

          //  ReportModel reportModel2 = reportModelArrayList.get(position);
            rowView = inflater.inflate(R.layout.report_item, parent, false);
            TextView name_txt = (TextView) rowView.findViewById(R.id.name_txt);
            TextView no_txt = (TextView) rowView.findViewById(R.id.no_txt);
            TextView price_txt = (TextView) rowView.findViewById(R.id.price_txt);
            TextView time_txt = (TextView) rowView.findViewById(R.id.time_txt);

            name_txt.setText(reportModel1.getInvoice_name());
           // no_txt.setText(reportModel1.);
            price_txt.setText(reportModel1.getTotal_value());
            time_txt.setText(reportModel1.getDue_date());

        }






        return rowView;
    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }
}
