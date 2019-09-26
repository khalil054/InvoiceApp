package test.invoicegenerator.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.addressAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.ClientWithAddressModel;


public class FragmentUpdateClient extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.add_client_button)
    Button Btn_AddClient;
    @BindView(R.id.client_name)
    EditText Et_Client_Name;
    @BindView(R.id.client_email)
    EditText Et_Client_Email;
    @BindView(R.id.client_phone)
    EditText Et_Client_Phone;
    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;
    SwipeMenuListView listView;
    ClientWithAddressModel clientWithAddressModel;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    int DeletePosition = 0;
    addressAdapter addressAdapter;
    FloatingActionButton floating_AddClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_client, container, false);
        listView = view.findViewById(R.id.addressList);
        unbinder = ButterKnife.bind(this, view);
        floating_AddClient = view.findViewById(R.id.floating_add_new_client);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        GetClientDetail(GlobalData.clientId);
        Btn_AddClient.setText("Update");
        floating_AddClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(new AddAddress(), null);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 0,
                        0)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalData.addressModel = clientWithAddressModel.getAddressModels().get(position);
                loadFragment(new UpdateAddress(), null);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DeletePosition = position;
                        DeleteAddress(clientWithAddressModel.getAddressModels().get(position).getId());
                        break;
                    case 1:
//                        GlobalData.clientModel =  clientModels.get(position);
//                        loadFragment(new FragmentUpdateClient(),null);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    public void loadData() {
        Et_Client_Name.setText(clientWithAddressModel.getName());
        Et_Client_Email.setText(clientWithAddressModel.getEmail());
        Et_Client_Phone.setText(clientWithAddressModel.getPhone());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Btn_AddClient.setOnClickListener(this);
        Et_Client_Name.setOnClickListener(this);
        Et_Client_Email.setOnClickListener(this);
        Et_Client_Phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.add_client_button) {
            String StrName = Et_Client_Name.getText().toString();
            String StrEmail = Et_Client_Email.getText().toString();
            String StrPhone = Et_Client_Phone.getText().toString();

            validateAndSaveData(StrName, StrEmail, StrPhone);
        }
    }

    private void validateAndSaveData(String StrName, String StrEmail, String StrPhone) {
        if (StrEmail.equals("")) {
            Et_Client_Email.setError(getString(R.string.error_field_required));
            Et_Client_Email.requestFocus();
        }
        if (StrName.equals("")) {
            Et_Client_Name.setError(getString(R.string.error_field_required));
            Et_Client_Name.requestFocus();
        } else if (!Util.isFullname(StrName)) {
            Et_Client_Name.setError(getString(R.string.error_invalid_name));
            Et_Client_Name.requestFocus();
        } else if (!Util.isEmailValid(StrEmail)) {
            Et_Client_Email.setError(getString(R.string.error_invalid_email));
            Et_Client_Email.requestFocus();
        } else {
            DataSendToServerForUpdate();
        }

    }

    void DataSendToServerForUpdate() {
        showProgressBar();

        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        Map<String, String> data = new HashMap<>();

        data.put("client[name]", Et_Client_Name.getText().toString());
        data.put("client[email]", Et_Client_Email.getText().toString());
        data.put("client[phone]", Et_Client_Phone.getText().toString());

        mVolleyService.putDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.UpdateClient + clientWithAddressModel.getId() + ".json", data);
    }

    void initVolleyCallbackForUpdate() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        confirmationView.setVisibility(View.VISIBLE);
                        confirmationView.playAnimation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                confirmationView.setVisibility(View.GONE);

                            }
                        }, 1000);

                        snackbar = Snackbar.make(main_layout, "Client Added Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {


                        String error = jsonObject.getString("Error");
                        Toasty.error(getActivity(), error, Toast.LENGTH_SHORT).show();
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


    public void GetClientDetail(String id) {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetClientDetails + id + ".json");

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject clients = data.getJSONObject("client");
                        clientWithAddressModel = new ClientWithAddressModel(clients);

                        loadData();

                        addressAdapter = new addressAdapter(getActivity(), clientWithAddressModel.getAddressModels());
                        listView.setAdapter(addressAdapter);
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

    public void DeleteAddress(String id) {

        showProgressBar();
        initVolleyCallbackForDeleteAddress();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.DeleteDataVolley(NetworkURLs.BaseURL + NetworkURLs.DeleteClient + GlobalData.clientId + NetworkURLs.DeleteAddress + id + ".json");

    }

    void initVolleyCallbackForDeleteAddress() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        clientWithAddressModel.getAddressModels().remove(DeletePosition);
                        addressAdapter = new addressAdapter(getActivity(), clientWithAddressModel.getAddressModels());
                        listView.setAdapter(addressAdapter);

                        snackbar = Snackbar.make(main_layout, "Address Deleted Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
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

    public interface OnItemSelectedListener {

        void onAddClientFragCallBack(int position);
    }

}

