package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.SharedPreferenceHelper;


public class FragmentUpdatePassword extends BaseFragment{

    @BindView(R.id.et_confirm_password)
    EditText confirm_password;

    @BindView(R.id.et_password_new)
    EditText password;


    @BindView(R.id.et_password_current)
    EditText current_password;

    @BindView(R.id.done_btn)
    Button done_btn;

    Progressbar cdd;
    private VolleyService mVolleyService;
    IResult mResultCallback = null;

    RelativeLayout relativeLayoutMain;

    String NewPassword;
    SharedPreferenceHelper sharedPreferenceHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        relativeLayoutMain=view.findViewById(R.id.layout_main);
        unbinder= ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view) {
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity());
        cdd=new Progressbar(getActivity());
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_password.getText().toString().equals(""))
                {
                    current_password.setError(getString(R.string.error_field_required));
                    current_password.requestFocus();
                }else if(password.getText().toString().equals(""))
                {
                    password.setError(getString(R.string.error_field_required));
                    password.requestFocus();
                }
                else if(confirm_password.getText().toString().equals(""))
                {
                    confirm_password.setError(getString(R.string.error_field_required));
                    confirm_password.requestFocus();
                }
                else if(!password.getText().toString().equals(confirm_password.getText().toString()))
                {
                    confirm_password.setError(getString(R.string.password_mismatch));
                    confirm_password.requestFocus();
                }
                else{


                    String CurrentPassword=sharedPreferenceHelper.getValue("password");

                    if(!current_password.getText().toString().equals(CurrentPassword)){
                        showErrorMessage(relativeLayoutMain,"Current Password is not Correct");
                    }else {
                        if(!password.getText().toString().equals(confirm_password.getText().toString())){
                            showErrorMessage(relativeLayoutMain,"Mismatch New Password");
                        }else {
                            NewPassword=password.getText().toString();
                            sendDataForChangePassword(current_password.getText().toString(),password.getText().toString(),confirm_password.getText().toString());
                        }
                    }
                }

            }
        });
    }
    void sendDataForChangePassword(String Current_Pass,String New_Pass,String Reconfirm_Pass)
    {

        cdd.ShowProgress();


        initVolleyCallbackForSettingPassword();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        Map<String, String> data = new HashMap<>();
        data.put("current_password",Current_Pass);
        data.put("password",New_Pass);
        data.put("password_confirmation",Reconfirm_Pass);

        String StrUrl=NetworkURLs.BaseURL + NetworkURLs.ChangePassword;

        mVolleyService.putDataVolley("PUTCALL", StrUrl,data);
    }

    private void initVolleyCallbackForSettingPassword() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status=jsonObject.getString("status");


                    if(status.equals("true"))
                    {
                        Toasty.success(getActivity(),"Your password has been successfully updated.", Toast.LENGTH_SHORT).show();
                        sharedPreferenceHelper.setValue("password",NewPassword);

                        cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new DashboardFragment());
                                // finish();

                            }
                        }, 1000);//1000
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                cdd.HideProgress();
                if(error.networkResponse != null && error.networkResponse.data != null) {
                    String error_response = new String(error.networkResponse.data);
                    // Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_obj = new JSONObject(error_response);
                        {
                            JSONObject error_obj = response_obj.getJSONObject("errors");
                            String message = error_obj.getString("message");

                            Toast.makeText(getActivity(),"Error" + message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
             //   Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };

    }
    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
