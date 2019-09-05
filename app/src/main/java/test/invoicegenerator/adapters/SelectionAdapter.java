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
import android.widget.TextView;
import java.util.ArrayList;
import test.invoicegenerator.R;
import test.invoicegenerator.model.ClientSelectModel;

public class SelectionAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<ClientSelectModel> clientModels;
    private Activity context;
    private SelectionAdapter.ItemFilter mFilter = new SelectionAdapter.ItemFilter();
    ArrayList<ClientSelectModel> list = new ArrayList<ClientSelectModel>();


    public SelectionAdapter( Activity context, ArrayList<ClientSelectModel> clientModels)
    {
        super(context, R.layout.selection_client_item);
        this.context=context;
        this.clientModels = clientModels;
        this.list = clientModels;

    }

    @Override
    public int getCount() {
        return clientModels.size();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = view;
        if (rootView == null) {
            rootView= inflater.inflate(R.layout.selection_client_item, null, true);
        }

        ClientSelectModel clientModel = clientModels.get(position);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_client_name_adapter);
        TextView tvPhone = (TextView) rootView.findViewById(R.id.tv_client_phone);
        TextView tvAddress = (TextView) rootView.findViewById(R.id.tv_client_address);
        ImageButton selection_btn = (ImageButton) rootView.findViewById(R.id.selection_btn);
        selection_btn.setClickable(false);

        tvName.setText(clientModel.getName());
        tvPhone.setText(clientModel.getEmail());
        tvAddress.setText(clientModel.getPhone());

        if(clientModel.getSelect())
        {

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
            final ArrayList<ClientSelectModel> nlist = new ArrayList<ClientSelectModel>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = "" + list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    ClientSelectModel clientModel = list.get(i);
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
            clientModels = (ArrayList<ClientSelectModel>) results.values;
            notifyDataSetChanged();
        }
    }



}

