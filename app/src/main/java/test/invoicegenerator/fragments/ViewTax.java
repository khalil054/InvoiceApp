package test.invoicegenerator.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Activities.MainActivity;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.model.TaxCodeModel;
import test.invoicegenerator.model.TaxModel;

public class ViewTax extends BaseFragment  {


    public static ViewTax newInstance() {
        ViewTax fragment = new ViewTax();
        return fragment;
    }

    Snackbar snackbar;
    Spinner spinner_taxes,spinner_tax_code;
    EditText tax_ammount;
    TextView tax_per_txt,taxcode_per_txt;
    ArrayList<String> Data1 = new ArrayList<String>();

    private FragmentAddClient.OnItemSelectedListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_tax,container,false);

        taxcode_per_txt = (TextView) view.findViewById(R.id.taxcode_per_txt);
        tax_per_txt = (TextView) view.findViewById(R.id.tax_per_txt);
        tax_ammount = (EditText) view.findViewById(R.id.tax_ammount);
        spinner_taxes = (Spinner) view.findViewById(R.id.spinner_taxes);
        spinner_tax_code = (Spinner) view.findViewById(R.id.spinner_tax_code);
        Button cal_tax_button = (Button) view.findViewById(R.id.cal_tax_button);

        cal_tax_button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(tax_ammount.getText().toString())) {

                    if(GlobalData.taxModels.size() != 0)
                    {
                        int position1 = spinner_taxes.getSelectedItemPosition();
                        double Ammount = Double.valueOf(tax_ammount.getText().toString());
                        double percentage = (Ammount / 100) * GlobalData.taxModels.get(position1).getPercent();
                        double total = percentage + Ammount;

                        tax_per_txt.setText(String.valueOf(total));
                        taxcode_per_txt.setText(String.valueOf(GlobalData.taxModels.get(position1).getPercent()));
                    }

                    if(GlobalData.taxModels.size() != 0)
                    {
//                        int position2 = spinner_tax_code.getSelectedItemPosition();
//                        double Ammount = Double.valueOf(tax_ammount.getText().toString());
//                        double percentage = (Ammount / 100) * GlobalData.taxCodeModels.get(position2).getPercent();
//                        double total = percentage + Ammount;
//
//                        tax_per_txt.setText(String.valueOf(total));
//                        taxcode_per_txt.setText(String.valueOf(GlobalData.taxCodeModels.get(position2).getPercent()));
                    }





                }else {

                    tax_ammount.setError(getString(R.string.error_field_required));

                }

            }
        });

        TaxesSpinnerSetup();
        TaxesCodeSpinnerSetup();


        return view;
    }

    public void TaxesSpinnerSetup()
    {
        ArrayList<String> Data = new ArrayList<String>();
        for (TaxModel taxModel : GlobalData.taxModels) {
            Data.add(taxModel.getName());
        }



        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,Data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_taxes.setAdapter(adapter);

        spinner_taxes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
               // ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

    }

    public void TaxesCodeSpinnerSetup()
    {
        ArrayList<String> Data = new ArrayList<String>();
        for (TaxCodeModel taxCodeModel : GlobalData.taxCodeModels) {
            Data.add(taxCodeModel.getCode());
        }

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,Data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tax_code.setAdapter(adapter);
        spinner_tax_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Change the selected item's text color
              //  ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


    }



}

