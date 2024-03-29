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
import test.invoicegenerator.general.Util;


public class FragmentSetPassword extends BaseFragment {

    @BindView(R.id.confirm_password)
    EditText confirm_password;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.done_btn)
    Button done_btn;

    Progressbar cdd;
    private VolleyService mVolleyService;
    IResult mResultCallback = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        cdd = new Progressbar(getActivity());
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("")) {
                    password.setError(getString(R.string.error_field_required));
                    password.requestFocus();
                } else if (confirm_password.getText().toString().equals("")) {
                    confirm_password.setError(getString(R.string.error_field_required));
                    confirm_password.requestFocus();
                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    confirm_password.setError(getString(R.string.password_mismatch));
                    confirm_password.requestFocus();
                } else
                    sendDataForSettingPassword();
            }
        });
    }

    void sendDataForSettingPassword() {

        cdd.ShowProgress();


        initVolleyCallbackForSettingPassword();
        mVolleyService = new VolleyService(mResultCallback, getActivity());


        Map<String, String> data = new HashMap<>();
        data.put("password", password.getText().toString());
        data.put("password_confirmation", confirm_password.getText().toString());


        mVolleyService.putDataVolleyForHeaders("PUTCALL", NetworkURLs.BaseURL + NetworkURLs.password, data);
    }

    private void initVolleyCallbackForSettingPassword() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");


                    if (status.equals("true")) {
                        Toasty.success(getActivity(), "Your password has been successfully updated.", Toast.LENGTH_SHORT).show();


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentLogin());
                                // finish();

                            }
                        }, 1000);//1000
                    } else {

                        cdd.HideProgress();
                        // String error = data_obj.getString("Error");

                        Toasty.error(getActivity(), "Unable To Update Password", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                cdd.HideProgress();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                cdd.HideProgress();
                error.printStackTrace();
                Toasty.error(getActivity(), Util.getMessage(error), Toast.LENGTH_SHORT).show();
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
