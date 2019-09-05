package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;

public class SettingsFragment extends BaseFragment {

    IResult mResultCallback = null;
    VolleyService mVolleyService;

    RadioButton RdFalse, RdActive;
    RadioGroup radioGroup;
    boolean shoul_active=false;



    String StrID;
    String StrDefaultNotes;
    String StrDefaultDays;

    EditText EtDefaultDays;
    EditText EtDefaultNotes;
    Button BtnUpdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);



        BtnUpdate=view.findViewById(R.id.btn_update);

        EtDefaultDays=view.findViewById(R.id.invoice_default_days);

        EtDefaultNotes=view.findViewById(R.id.invoice_default_notes);

        radioGroup = view.findViewById(R.id.myRadioGroup);

        RdActive=view.findViewById(R.id.rd_active);

        RdFalse=view.findViewById(R.id.rd_false);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rd_active) {
                    shoul_active=true;
                } else {
                   shoul_active=false;
                }
            }

        });

        GetCompanySettings();

        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrDefaultDays=EtDefaultDays.getText().toString();
                StrDefaultNotes=EtDefaultNotes.getText().toString();
                UpdateSettings();

            }
        });

        return view;
    }

    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    public void GetCompanySettings()
    {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL+ NetworkURLs.CompanySettings);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    /*    if (jsonObject.getString("status").equalsIgnoreCase("true")) {*/
                    if (status) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject settings_data = data.getJSONObject("setting");

                        StrID=settings_data.getString("id");
                        StrDefaultDays=settings_data.getString("invoice_default_due_at_days");
                        StrDefaultNotes=settings_data.getString("invoice_default_notes");
                        shoul_active=settings_data.getBoolean("apply_tax_on_items");


                        EtDefaultDays.setText(StrDefaultDays);
                        EtDefaultNotes.setText(StrDefaultNotes);
                        if(shoul_active){
                            RdFalse.setChecked(false);
                            RdActive.setChecked(true);
                        }else {
                            RdFalse.setChecked(true);
                            RdActive.setChecked(false);
                        }


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


    void UpdateSettings()
    {


        showProgressBar();


        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject data=new JSONObject();
        JSONObject FinalObj=new JSONObject();

        try {
            data.put("invoice_default_notes",StrDefaultNotes);

            data.put("apply_tax_on_items",shoul_active);
            data.put("invoice_default_due_at_days",StrDefaultDays);



            FinalObj.put("setting",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        String Str=NetworkURLs.BaseURL + NetworkURLs.UpdateCompanySettings;
        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL",Str ,FinalObj );


    }

    void initVolleyCallbackForUpdate(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status = data.getString("status");

                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();




                } catch (JSONException e) {
                    e.printStackTrace();
                }



                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                error.printStackTrace();

                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        String status=response_obj.getString("status");
                        if(status.equals("false"))
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            Toasty.error(getActivity(),message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //  return error;
                // snackbar = Snackbar.make(main_layout, error.toString(), Snackbar.LENGTH_LONG);
                //snackbar.show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };
    }

}
