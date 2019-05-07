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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import me.gujun.android.taggroup.TagGroup;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.taxAdapter;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.model.TaxModel;

public class AddTaxCode extends BaseFragment{

    @BindView(R.id.add_tax_button)
    Button Btn_AddTax;
    @BindView(R.id.tax_name)
    EditText Et_Tax_Name;
    @BindView(R.id.tax_des)
    public EditText Et_Tax_Description;
    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;
    @BindView(R.id.confirmationView)
    LottieAnimationView confirmationView;

    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;

    String SelectedIds = "[";
    ArrayList<TaxModel> taxModels = new ArrayList<TaxModel>();
    ArrayList<MultiSelectModel> taxlist = new ArrayList<MultiSelectModel>();
    ArrayList<Integer> taxSelectedlist = new ArrayList<Integer>();

    String[] Ids;

    private FragmentAddClient.OnItemSelectedListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_taxcode,container,false);

        progressbar = new Progressbar(getActivity());
        unbinder= ButterKnife.bind(this,view);

        final TagGroup mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        mTagGroup.setTags(new String[]{"Select Combination of Taxes"});
        mTagGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                        .title("Select Combination To Make Tax Code") //setting title for dialog
                        .titleSize(25)
                        .positiveText("Done")
                        .negativeText("Cancel")
                        .preSelectIDsList(taxSelectedlist)
                        .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                        .multiSelectList(taxlist) // the multi select model list with ids and name
                        .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                            @Override
                            public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                //will return list of selected IDS
                                String[] title = new String[selectedNames.size()];
                                Ids = new String[selectedIds.size()];
                                for (int i = 0; i < selectedNames.size(); i++) {

                                    taxSelectedlist.add(selectedIds.get(i));
                                    title[i] = selectedNames.get(i);
                                    Ids[i] = String.valueOf(selectedIds.get(i));


                                }


                                mTagGroup.setTags(title);



                            }

                            @Override
                            public void onCancel() {

                            }


                        });

                multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");


            }
        });

        GetTaxList();



        Btn_AddTax.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String TaxName=Et_Tax_Name.getText().toString();
                String TaxDes=Et_Tax_Description.getText().toString();


                validateAndSaveData(TaxName,TaxDes);


            }
        });

        return view;
    }

    private void validateAndSaveData(String TaxName, String TaxDes) {

        if(TaxName.equals(""))
        {
            Et_Tax_Name.setError(getString(R.string.error_field_required));
            Et_Tax_Name.requestFocus();
        }
        else if(TaxDes.equals(""))
        {
            Et_Tax_Description.setError(getString(R.string.error_field_required));
            Et_Tax_Description.requestFocus();
        }
        else
        {

            DataSendToServerForAddTax();

        }


    }

    void DataSendToServerForAddTax()
    {
        progressbar.ShowProgress();

        initVolleyCallbackForAddTax();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject Data=new JSONObject();
        JSONObject jsonObject1=new JSONObject();
        JSONArray array = new JSONArray();

        for (String string : Ids) {
        array.put(string);

    }



        try {


            jsonObject1.put("code",Et_Tax_Name.getText().toString());
            jsonObject1.put("description",Et_Tax_Description.getText().toString());
            jsonObject1.put("company_tax_ids", array);

            Data.put("tax_code",jsonObject1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolleyForHeadersWithJson("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.AddTaxCode,Data );

    }

    void initVolleyCallbackForAddTax(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    boolean status = jsonObject1.getBoolean("status");
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

                        snackbar = Snackbar.make(main_layout,"Tax Code Added Successfully", Snackbar.LENGTH_LONG);
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


    public void GetTaxList() {
        if (taxModels.size() > 0) {
            taxModels.clear();
        }
        showProgressBar();
        initVolleyCallbackForTaxList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetTaxesList);

    }

    void initVolleyCallbackForTaxList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    /*    if (jsonObject.getString("status").equalsIgnoreCase("true")) {*/
                    if (status) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray company_taxes = data.getJSONArray("company_taxes");

                        for (int i = 0; i < company_taxes.length(); i++) {
                            TaxModel taxModel = new TaxModel(company_taxes.getJSONObject(i));
                            taxModels.add(taxModel);

                            MultiSelectModel multiSelectModel = new MultiSelectModel(Integer.valueOf(taxModel.getId()),taxModel.getName());
                            taxlist.add(multiSelectModel);
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

}
