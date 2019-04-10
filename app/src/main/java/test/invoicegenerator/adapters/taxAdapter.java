package test.invoicegenerator.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.model.ClientModel;
import test.invoicegenerator.model.TaxModel;

public class taxAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<TaxModel> taxModels;
    private Activity context;
    private taxAdapter.ItemFilter mFilter = new taxAdapter.ItemFilter();
    ArrayList<TaxModel> list = new ArrayList<TaxModel>();


    public taxAdapter( Activity context, ArrayList<TaxModel> taxModels)
    {
        super(context, R.layout.all_client_adapter);
        this.context=context;
        this.taxModels = taxModels;
        this.list = taxModels;

    }

    @Override
    public int getCount() {
        return taxModels.size();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = view;
        if (rootView == null) {
            rootView= inflater.inflate(R.layout.tax_item, null, true);
        }

        TaxModel taxModel = taxModels.get(position);
        TextView tvName = (TextView) rootView.findViewById(R.id.name_txt);
        TextView tvPhone = (TextView) rootView.findViewById(R.id.percentage_txt);

        tvName.setText(taxModel.getName());
        tvPhone.setText(String.valueOf(taxModel.getPercent()));



        return rootView;
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
            final ArrayList<TaxModel> nlist = new ArrayList<TaxModel>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    TaxModel taxModel = list.get(i);
                    nlist.add(taxModel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taxModels = (ArrayList<TaxModel>) results.values;
            notifyDataSetChanged();
        }
    }



}
