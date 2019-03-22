package test.invoicegenerator.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ClientAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.ClientModel;

public class ClientSelection extends BaseFragment{


    ListView listView;
    FloatingActionButton floating_AddClient;
    ClientAdapter clientAdapter;

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;

    SearchView searchView;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    int AddPosition = 0;
    int OpenPosition = 0;

    ArrayList<ClientModel> clientModels=new ArrayList<ClientModel>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_selection,container,false);
        listView = (ListView) view.findViewById(R.id.clientList);
        searchView = (SearchView) view.findViewById(R.id.searchView); // inititate a search view

        floating_AddClient = (FloatingActionButton) view.findViewById(R.id.floating_add_new_client);

        init();
        unbinder= ButterKnife.bind(this,view);

        GetClientList();

        return view;
    }

    private void init() {


        searchView.setQueryHint("Search Client");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setHintTextColor(Color.GRAY);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                clientAdapter.getFilter().filter(newText);

                return false;
            }
        });

        BottomNavigationView navigation =  getActivity().findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);

        floating_AddClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new FragmentAddClient(),null);
            }
        });



        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPosition = position;
                GlobalData.clientModel =  clientModels.get(position);
                loadFragment(new FragmentUpdateClient(),null);
            }
        });
    }





    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAllClientFragCallBack(int position);
    }


    public void GetClientList()
    {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL+ NetworkURLs.GetClientList);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray clients = data.getJSONArray("clients");

                        for (int i = 0; i < clients.length(); i++) {
                            ClientModel clientModel = new ClientModel(clients.getJSONObject(i));
                            clientModels.add(clientModel);
                        }

                        clientAdapter = new ClientAdapter(getActivity(),clientModels);
                        listView.setAdapter(clientAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }






}
