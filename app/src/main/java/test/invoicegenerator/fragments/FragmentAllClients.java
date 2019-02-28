package test.invoicegenerator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.AllClientsAdapter;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.recyclerhelper.RecyclerItemClickListener;


public class FragmentAllClients extends BaseFragment implements View.OnClickListener{

    private static final int REQUEST_READ_CONTACTS = 0;
    DBHelper sqliteHelper;
    @BindView(R.id.recycler_view_all_clients)
    RecyclerView recyclerView;
    @BindView(R.id.floating_add_new_client)
    FloatingActionButton floating_AddClient;
    @BindView(R.id.floating_add_new_client_empty)
    FloatingActionButton floating_AddClient_Empty;
    private AllClientsAdapter mAdapter;
    private List<Client>ClientList;
    private OnItemSelectedListener listener;
    boolean isrecordfound=false;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutempty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sqliteHelper=new DBHelper(getActivity());
        View view;

        init();
        view = inflater.inflate(R.layout.fragment_all_clients,container,false);
        unbinder= ButterKnife.bind(this,view);

        return view;
    }

    private void init() {
        if(sqliteHelper.getClientTableSize()>0){
            isrecordfound=true;
        }else {
            isrecordfound=false;
        }

        BottomNavigationView navigation =  getActivity().findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!isrecordfound){

            layoutempty.setVisibility(View.VISIBLE);
           floating_AddClient_Empty.setOnClickListener(this);
            floating_AddClient.setClickable(false);
        }else {
            layoutempty.setVisibility(View.GONE);
        ClientList=new ArrayList<>();
       recyclerView = view.findViewById(R.id.recycler_view_all_clients);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareLocationData();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Client client=ClientList.get(position);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("client",client);
                        loadFragment(new FragmentUpdateClient(),bundle);
                        //listener.onAllClientFragCallBack(1);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        floating_AddClient.setOnClickListener(this);


        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.floating_add_new_client:
                listener.onAllClientFragCallBack(2);
                break;
            case R.id.floating_add_new_client_empty:
                listener.onAllClientFragCallBack(2);
                break;
        }
    }

    private void loadFragment(Fragment dashboardFragment,Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(getActivity(), "on Swiped ", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            String str=String.valueOf(ClientList.get(position).getClientID());
            sqliteHelper.DeleteSingleClient(str);
            //Toast.makeText(getActivity(), String.valueOf(str), Toast.LENGTH_SHORT).show();
           listener.onAllClientFragCallBack(1);

        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SavedCoupansLocationFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAllClientFragCallBack(int position);
    }


    private void prepareLocationData() {
        ClientList=sqliteHelper.AllClientDataList();
        mAdapter = new AllClientsAdapter(ClientList,getActivity());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }



}
