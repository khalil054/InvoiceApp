package test.invoicegenerator.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
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

public class ClientAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<ClientModel> clientModels;
    private Activity context;
    private ItemFilter mFilter = new ItemFilter();
    ArrayList<ClientModel> list = new ArrayList<ClientModel>();


    public ClientAdapter( Activity context, ArrayList<ClientModel> clientModels)
    {
        super(context, R.layout.all_client_adapter);
        this.context=context;
        this.clientModels = clientModels;
        this.list = clientModels;

    }

    @Override
    public int getCount() {
        return clientModels.size();
    }

    @Override
    public View getView(final int position, View view, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = view;
        if (rootView == null) {
            rootView= inflater.inflate(R.layout.all_client_adapter, null, true);
        }

        ClientModel clientModel = clientModels.get(position);
        TextView tvName = rootView.findViewById(R.id.tv_client_name_adapter);
        TextView tvPhone = rootView.findViewById(R.id.tv_client_phone);
        TextView tvAddress =  rootView.findViewById(R.id.tv_client_address);

        tvName.setText(clientModel.getName());
        tvPhone.setText(clientModel.getEmail());
        tvAddress.setText(clientModel.getPhone());



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
            final ArrayList<ClientModel> nlist = new ArrayList<ClientModel>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    ClientModel clientModel = list.get(i);
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
            clientModels = (ArrayList<ClientModel>) results.values;
            notifyDataSetChanged();
        }
    }



}

