package test.invoicegenerator.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.model.ClientSelectModel;
import test.invoicegenerator.model.Country_Model;

public class SelectionCountryAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<Country_Model> CountryModels;
    private Activity context;
    private SelectionCountryAdapter.ItemFilter mFilter = new SelectionCountryAdapter.ItemFilter();
    ArrayList<Country_Model> list = new ArrayList<Country_Model>();


    public SelectionCountryAdapter(Activity context, ArrayList<Country_Model> countryModels)
    {
        super(context, R.layout.selection_client_item);
        this.context=context;
        this.CountryModels = countryModels;
        this.list = CountryModels;

    }

    @Override
    public int getCount() {
        return CountryModels.size();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = view;
        if (rootView == null) {
            rootView= inflater.inflate(R.layout.selection_country_item, null, true);
        }

        Country_Model clientModel = CountryModels.get(position);
        TextView tvName =  rootView.findViewById(R.id.tv_country_name);

        ImageView selection_btn =  rootView.findViewById(R.id.selection_btn);
        selection_btn.setClickable(false);

        tvName.setText(clientModel.getStrCountryName());

        if(clientModel.isIschecked()){
            selection_btn.setImageResource(R.drawable.ic_checked);
        }else {
            selection_btn.setImageResource(R.drawable.ic_unchecked);
        }


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
            final ArrayList<Country_Model> nlist = new ArrayList<Country_Model>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getStrCountryName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    Country_Model clientModel = list.get(i);
                    nlist.add(clientModel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            CountryModels = (ArrayList<Country_Model>) results.values;
            notifyDataSetChanged();
        }
    }



}

