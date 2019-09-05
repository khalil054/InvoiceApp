package test.invoicegenerator.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ClientAdapter;
import test.invoicegenerator.adapters.SelectionAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.ClientModel;
import test.invoicegenerator.model.ClientSelectModel;

public class SendSMSClient extends BaseFragment {


    ListView listView;
    SelectionAdapter selectionAdapter;
    FloatingActionButton select_all_btn;
    Button send_btn;

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    Boolean allSelect = true;
    String Message = "";
    RingProgressBar mRingProgressBar;

    ArrayList<ClientSelectModel> clientModels = new ArrayList<ClientSelectModel>();
    ArrayList<String> selectedNomber = new ArrayList<String>();
    int first_number=0;
    int last_number=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sendsms_client_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        listView = (SwipeMenuListView) view.findViewById(R.id.clientList);
        select_all_btn =  view.findViewById(R.id.select_all_btn);
        select_all_btn.hide();
        select_all_btn.setClickable(false);
        send_btn = getActivity().findViewById(R.id.back_btn);
        progressbar = new Progressbar(getActivity());
        send_btn.setText("Send");
        send_btn.setVisibility(View.INVISIBLE);
        send_btn.setClickable(false);
        mRingProgressBar =  view.findViewById(R.id.progress_bar);

        Bundle bundle = this.getArguments();
        Message = bundle.getString("msg_txt", "Testing from Invoice");

        init();

        GetClientList();

        return view;
    }

    public Boolean requestpermisson(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
        }else{         //already has permission granted

            return true;

        }

        return false;
    }

    private void init() {


        // Set the progress bar's progress
        mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener()
        {

            @Override
            public void progressToComplete()
            {


            }
        });

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  if(clientModels.get(position).getSelect())
                {

                    clientModels.get(position).setSelect(false);

                }else {

                    clientModels.get(position).setSelect(true);
                }

                selectionAdapter.notifyDataSetChanged();*/


                clientModels.get(position).setSelect(true);
                showProgressBar();
                selectionAdapter.notifyDataSetChanged();
               // select_all_btn.setImageResource(R.drawable.ic_unselect_list);
                sendSMS( clientModels.get(position).getPhone(), Message);
            }
        });
        select_all_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(allSelect)
                {
                    allSelect = false;
                    select_all_btn.setImageResource(R.drawable.ic_select_list);
                    changeSelection(false);
                }else {

                    allSelect = true;
                    select_all_btn.setImageResource(R.drawable.ic_unselect_list);
                    changeSelection(true);
                }

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                selectedNomber.clear();


                for (int i = 0; i <= clientModels.size() - 1; i++) {
                    if (clientModels.get(i).getSelect()) {

                        selectedNomber.add(clientModels.get(i).getPhone());

                    }
                }

                mRingProgressBar.setMax(selectedNomber.size());

                if (requestpermisson()) {


                    /*mRingProgressBar.setVisibility(View.VISIBLE);
                    mRingProgressBar.setProgress(1);*/
                    Handler handler1 = new Handler();

                    sendSMS(selectedNomber.get(0), Message,0);

                    for (int a = 1; a<= selectedNomber.size()-1 ;a++) {
                        final int finalA = a;

                        sendSMS(selectedNomber.get(finalA), Message);

                       /* handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                sendSMS(selectedNomber.get(finalA), Message,finalA);
                                mRingProgressBar.setProgress(finalA );

                                if(finalA == selectedNomber.size()-1)
                                {
                                    mRingProgressBar.setVisibility(View.GONE);
                                    progressbar.ShowConfirmation();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {

                                            progressbar.HideConfirmation();
                                            loadFragment(new WriteSMS(),null);

                                        }
                                    }, 1000);
                                }
                            }
                        }, 10000 * a);*/
                    }





                }
            }
        });


    }

    public void sendSMS(String phoneNo, String msg, int finalA) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getActivity(), "Message Sent" + finalA,
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(getActivity(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void changeSelection(Boolean status)
    {
        for(int i = 0; i<= clientModels.size()-1; i++)
        {
            clientModels.get(i).setSelect(status);
        }
        selectionAdapter.notifyDataSetChanged();

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
                            ClientSelectModel clientModel = new ClientSelectModel(clients.getJSONObject(i));
                            clientModels.add(clientModel);
                            clientModel.setSelect(false);
                        }

                        selectionAdapter = new SelectionAdapter(getActivity(),clientModels);
                        listView.setAdapter(selectionAdapter);
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

/////////////////////////

    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                hideProgressBar();
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        //---when the SMS has been delivered---
        getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                hideProgressBar();
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

    }

}




