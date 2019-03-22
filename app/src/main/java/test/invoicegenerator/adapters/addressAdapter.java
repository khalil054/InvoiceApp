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
import test.invoicegenerator.model.AddressModel;
import test.invoicegenerator.model.ClientModel;
import test.invoicegenerator.model.ClientWithAddressModel;

public class addressAdapter extends ArrayAdapter<String> {

    ArrayList<AddressModel> addressModels;
    private Activity context;


    public addressAdapter( Activity context, ArrayList<AddressModel> addressModels)
    {
        super(context, R.layout.all_client_adapter);
        this.context=context;
        this.addressModels = addressModels;

    }

    @Override
    public int getCount() {
        return addressModels.size();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rootView = view;
        if (rootView == null) {
            rootView= inflater.inflate(R.layout.all_client_adapter, null, true);
        }

        AddressModel addressModel = addressModels.get(position);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_client_name_adapter);
        TextView tvPhone = (TextView) rootView.findViewById(R.id.tv_client_phone);
        TextView tvAddress = (TextView) rootView.findViewById(R.id.tv_client_address);

        tvName.setText(addressModel.getName());
        tvPhone.setText(addressModel.getCity());
        tvAddress.setText(addressModel.getCountry_name());



        return rootView;
    }





}

