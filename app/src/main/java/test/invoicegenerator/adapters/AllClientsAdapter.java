package test.invoicegenerator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.model.ClientModel;

public class AllClientsAdapter extends RecyclerView.Adapter<AllClientsAdapter.MyViewHolder> {

    private List<Client> ItemList;
    ArrayList<ClientModel> clientModels=new ArrayList<ClientModel>();
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPhone,tvAddress;
        MyViewHolder(View view) {
            super(view);
            tvName=view.findViewById(R.id.tv_client_name_adapter);
            tvPhone=view.findViewById(R.id.tv_client_phone);
            tvAddress=view.findViewById(R.id.tv_client_address);
        }
    }

    public AllClientsAdapter(List<Client> itemList, Context context,ArrayList<ClientModel> clientModels) {
        this.ItemList = itemList;
        this.context=context;
        this.clientModels = clientModels;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_client_adapter, parent, false);

        return new MyViewHolder(itemView); //bind view with item
    }
  //on Bind View holder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Client info = ItemList.get(position);
        ClientModel clientModel = clientModels.get(position);

        holder.tvName.setText(clientModel.getName());
        holder.tvPhone.setText(clientModel.getEmail());
        holder.tvAddress.setText(clientModel.getPhone());



    }

    @Override
    public int getItemCount() {

        return clientModels.size();
    }






}
