package test.invoicegenerator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import test.invoicegenerator.R;
import test.invoicegenerator.model.JsonInvoiceModel;

public class ReportAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    ArrayList<JsonInvoiceModel> reportModelArrayList;
    ArrayList<JsonInvoiceModel> list ;
    private ItemFilter mFilter = new ItemFilter();


    public ReportAdapter(Context context, ArrayList<JsonInvoiceModel> reportModelArrayList) {
        // super(context, -1, values);
        this.context = context;
        this.reportModelArrayList = reportModelArrayList;
        this.list = reportModelArrayList;
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
        JsonInvoiceModel reportModel1 = reportModelArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        {

            //  ReportModel reportModel2 = reportModelArrayList.get(position);
            rowView = inflater.inflate(R.layout.report_item, parent, false);
            TextView name_txt = rowView.findViewById(R.id.name_txt);
            TextView no_txt =rowView.findViewById(R.id.no_txt);
            TextView price_txt =  rowView.findViewById(R.id.price_txt);
            TextView time_txt = rowView.findViewById(R.id.time_txt);

            name_txt.setText(reportModel1.getSigned_by());
            // no_txt.setText(reportModel1.);
            price_txt.setText(reportModel1.getPayment_status());
            time_txt.setText(reportModel1.getDelivery_status());

        }
        return rowView;
    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();


            int count = list.size();
            final ArrayList<JsonInvoiceModel> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getSigned_by();
                if (filterableString.toLowerCase().contains(filterString)) {
                    JsonInvoiceModel InvoiceModel = list.get(i);
                    nlist.add(InvoiceModel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            reportModelArrayList = (ArrayList<JsonInvoiceModel>) results.values;
            notifyDataSetChanged();
        }
    }
}

/*package test.invoicegenerator.adapters;

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
        View rowView;{
          //  ReportModel reportModel2 = reportModelArrayList.get(position);
            rowView = inflater.inflate(R.layout.report_item, parent, false);
            TextView name_txt =rowView.findViewById(R.id.name_txt);
            TextView no_txt =  rowView.findViewById(R.id.no_txt);
            TextView price_txt = rowView.findViewById(R.id.price_txt);
            TextView time_txt = rowView.findViewById(R.id.time_txt);

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
}*/
