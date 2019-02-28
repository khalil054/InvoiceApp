package test.invoicegenerator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.Client;

public class AllClientsAdapter extends RecyclerView.Adapter<AllClientsAdapter.MyViewHolder> {

    private List<Client> ItemList;
    private Context context;
    // private RelativeLayout LayoutRemove;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPhone,tvAddress;
        //ImageView imageViewLogo;
        MyViewHolder(View view) {
            super(view);
            tvName=view.findViewById(R.id.tv_client_name_adapter);
            tvPhone=view.findViewById(R.id.tv_client_phone);
            tvAddress=view.findViewById(R.id.tv_client_address);
            //imageViewLogo=view.findViewById(R.id.p_logo_saved_location);
          //  LayoutRemove=view.findViewById(R.id.layout_remove_client);
        }
    }

    public AllClientsAdapter(List<Client> itemList, Context context) {
        this.ItemList = itemList;
        this.context=context;

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
        final Client info = ItemList.get(position);

        holder.tvName.setText(info.getClientName());
        holder.tvPhone.setText(info.getClientPhone());
        holder.tvAddress.setText(info.getClientAddress());



    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

}
