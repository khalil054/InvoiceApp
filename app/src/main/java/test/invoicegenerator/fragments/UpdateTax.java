package test.invoicegenerator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import test.invoicegenerator.adapters.addressAdapter;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.SharedPref;

public class UpdateTax extends BaseFragment{
    @BindView(R.id.switch_idActive)
    android.support.v7.widget.SwitchCompat aSwitch_Admin;
    @BindView(R.id.add_tax_button)
    Button Btn_AddTax;
    @BindView(R.id.delete_btn)
    Button Btn_DeleteTax;
    @BindView(R.id.tax_name)
    EditText Et_Tax_Name;
    @BindView(R.id.tax_agency)
    EditText Et_Tax_Agency;
    @BindView(R.id.tax_percentage)
    EditText Et_Tax_Percentage;
    @BindView(R.id.tax_description)
    public EditText Et_Tax_Description;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;
    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    private FragmentAddClient.OnItemSelectedListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_tax,container,false);

        progressbar = new Progressbar(getActivity());
        unbinder= ButterKnife.bind(this,view);



        LoadData();

        Btn_DeleteTax.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               DeleteTax();

            }
        });

        Btn_AddTax.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String TaxName=Et_Tax_Name.getText().toString();
                String TaxAgency=Et_Tax_Agency.getText().toString();
                String TaxPercentage=Et_Tax_Percentage.getText().toString();
                String TaxDescription=Et_Tax_Description.getText().toString();

                validateAndSaveData(TaxName,TaxAgency,TaxPercentage,TaxDescription);

            }
        });

        return view;
    }

    private void validateAndSaveData(String TaxName, String TaxAgency, String TaxPercentage, String TaxDescription) {
        if(TaxName.equals(""))
        {
            Et_Tax_Agency.setError(getString(R.string.error_field_required));
            Et_Tax_Agency.requestFocus();
        }
        if(TaxAgency.equals(""))
        {
            Et_Tax_Name.setError(getString(R.string.error_field_required));
            Et_Tax_Name.requestFocus();
        }

        else if(TaxPercentage.equals(""))
        {
            Et_Tax_Percentage.setError(getString(R.string.error_field_required));
            Et_Tax_Percentage.requestFocus();
        }
        else if(TaxDescription.equals(""))
        {
            Et_Tax_Description.setError(getString(R.string.error_field_required));
            Et_Tax_Description.requestFocus();
        }
        else
        {

            DataSendToServerForAddTax();

        }



    }

    public void LoadData()
    {
        Btn_AddTax.setText("Update");
        Et_Tax_Name.setText(GlobalData.taxModel.getName());
        Et_Tax_Agency.setText(GlobalData.taxModel.getAgency_name());
        Et_Tax_Percentage.setText(String.valueOf(GlobalData.taxModel.getPercent()));
        Et_Tax_Description.setText(GlobalData.taxModel.getDescription());

        if(GlobalData.taxModel.getActive()){
            aSwitch_Admin.setChecked(true);
        }else {
            aSwitch_Admin.setChecked(false);
        }
    }
    void DataSendToServerForAddTax()
    {
        progressbar.ShowProgress();

        initVolleyCallbackForAddTax();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        SharedPref.init(getActivity());
        String company_id = SharedPref.read(SharedPref.CompanyID,"");

        Map<String, String> data = new HashMap<String, String>();
        data.put("company_tax[name]",Et_Tax_Name.getText().toString());
        data.put("company_tax[agency_name]",Et_Tax_Agency.getText().toString());
        data.put("company_tax[description]",Et_Tax_Description.getText().toString());
        data.put("company_tax[percent]",Et_Tax_Percentage.getText().toString());
        data.put("company_tax[active]","true");
        data.put("company_tax[company_id]",company_id);
        mVolleyService.putDataVolleyForHeaders("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.UpdateTax+GlobalData.taxModel.getId()+".json",data );

    }

    void initVolleyCallbackForAddTax(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    boolean status = jsonObject.getBoolean("status");
                    if(status)
                    {

                        progressbar.HideProgress();
                        progressbar.ShowConfirmation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                progressbar.HideConfirmation();
                                loadFragment(new TaxConfigurations(),null);

                            }
                        }, 1000);

                        snackbar = Snackbar.make(main_layout,"Tax Updated Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        String error = jsonObject.getString("Error");
                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.HideProgress();
                }

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

    public boolean isValidString(String Str){

        return !Str.isEmpty() && !Str.equalsIgnoreCase("null");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentAddClient.OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (FragmentAddClient.OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SavedCoupansLocationFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAddClientFragCallBack(int position);
    }


    public void DeleteTax()
    {

        showProgressBar();
        initVolleyCallbackForDeleteTax();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.DeleteDataVolley(NetworkURLs.BaseURL+ NetworkURLs.DeleteTax + GlobalData.taxModel.getId() +".json" );

    }

    void initVolleyCallbackForDeleteTax() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        progressbar.HideConfirmation();
                        loadFragment(new TaxConfigurations(),null);

                        snackbar = Snackbar.make(main_layout,"Tax Deleted Successfully", Snackbar.LENGTH_LONG);
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

}

