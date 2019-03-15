package test.invoicegenerator.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.model.ClientModel;

public class ClientAdapter extends ArrayAdapter<String> {

    ArrayList<ClientModel> clientModels=new ArrayList<ClientModel>();
    private Activity context;

    public ClientAdapter( Activity context, ArrayList<ClientModel> clientModels)
    {
        super(context, R.layout.all_client_adapter);
        this.context=context;
        this.clientModels = clientModels;


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
            rootView= inflater.inflate(R.layout.all_client_adapter, null, true);
        }

        ClientModel clientModel = clientModels.get(position);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_client_name_adapter);
        TextView tvPhone = (TextView) rootView.findViewById(R.id.tv_client_phone);
        TextView tvAddress = (TextView) rootView.findViewById(R.id.tv_client_address);

        tvName.setText(clientModel.getName());
        tvPhone.setText(clientModel.getEmail());
        tvAddress.setText(clientModel.getAddress());



        return rootView;
    }







}

