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
import test.invoicegenerator.model.TaxCodeModel;
import test.invoicegenerator.model.TaxModel;

public class taxcodeAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<TaxCodeModel> taxModels;
    private Activity context;
    private taxcodeAdapter.ItemFilter mFilter = new taxcodeAdapter.ItemFilter();
    ArrayList<TaxCodeModel> list = new ArrayList<TaxCodeModel>();


    public taxcodeAdapter( Activity context, ArrayList<TaxCodeModel> taxModels)
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

        TaxCodeModel taxModel = taxModels.get(position);
        TextView tvName = (TextView) rootView.findViewById(R.id.name_txt);
        TextView tvPhone = (TextView) rootView.findViewById(R.id.percentage_txt);

        tvName.setText(taxModel.getCode());
        tvPhone.setText(String.valueOf(taxModel.getDes()));



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
            final ArrayList<TaxCodeModel> nlist = new ArrayList<TaxCodeModel>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getCode();
                if (filterableString.toLowerCase().contains(filterString)) {
                    TaxCodeModel taxModel = list.get(i);
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
            taxModels = (ArrayList<TaxCodeModel>) results.values;
            notifyDataSetChanged();
        }
    }



}
