package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
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
import test.invoicegenerator.model.ClientModel;

public class SelectClient extends BaseFragment {

    ListView listView;
    FloatingActionButton floating_AddClient;

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    ArrayList<ClientModel> clientModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_clients, container, false);
        listView = (SwipeMenuListView) view.findViewById(R.id.clientList);
        floating_AddClient = view.findViewById(R.id.floating_add_new_client);

        init();
        unbinder = ButterKnife.bind(this, view);

        GetClientList();

        return view;
    }

    private void init() {

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);

        floating_AddClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new FragmentAddClient(), null);
            }
        });
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAllClientFragCallBack(int position);
    }


    public void GetClientList() {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetClientList);

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

                        ClientAdapter cartAdapter = new ClientAdapter(getActivity(), clientModels);
                        listView.setAdapter(cartAdapter);
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
