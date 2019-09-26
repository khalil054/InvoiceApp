package test.invoicegenerator.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.TaxCodeModel;
import test.invoicegenerator.model.TaxModel;

public class ViewTax extends BaseFragment {


    public static ViewTax newInstance() {
        return new ViewTax();
    }

    Snackbar snackbar;
    Spinner spinner_taxes, spinner_tax_code;
    EditText tax_ammount;
    TextView tax_per_txt, taxcode_per_txt;


    private FragmentAddClient.OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_tax, container, false);

        taxcode_per_txt = view.findViewById(R.id.taxcode_per_txt);
        tax_per_txt = view.findViewById(R.id.tax_per_txt);
        tax_ammount = view.findViewById(R.id.tax_ammount);
        spinner_taxes = view.findViewById(R.id.spinner_taxes);
        spinner_tax_code = view.findViewById(R.id.spinner_tax_code);
        Button cal_tax_button = view.findViewById(R.id.cal_tax_button);

        cal_tax_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(tax_ammount.getText().toString())) {

                    if (GlobalData.taxModels.size() != 0) {
                        int position1 = spinner_taxes.getSelectedItemPosition();
                        double Ammount = Double.valueOf(tax_ammount.getText().toString());
                        double percentage = (Ammount / 100) * GlobalData.taxModels.get(position1).getPercent();
                        double total = percentage + Ammount;

                        tax_per_txt.setText(String.valueOf(total));
                        taxcode_per_txt.setText(String.valueOf(GlobalData.taxModels.get(position1).getPercent()));
                    }

                    if (GlobalData.taxModels.size() != 0) {

                    }


                } else {

                    tax_ammount.setError(getString(R.string.error_field_required));

                }

            }
        });

        TaxesSpinnerSetup();
        TaxesCodeSpinnerSetup();


        return view;
    }

    public void TaxesSpinnerSetup() {
        ArrayList<String> Data = new ArrayList<String>();
        for (TaxModel taxModel : GlobalData.taxModels) {
            Data.add(taxModel.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, Data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_taxes.setAdapter(adapter);

        spinner_taxes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Change the selected item's text color
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void TaxesCodeSpinnerSetup() {
        ArrayList<String> Data = new ArrayList<>();
        for (TaxCodeModel taxCodeModel : GlobalData.taxCodeModels) {
            Data.add(taxCodeModel.getCode());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, Data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tax_code.setAdapter(adapter);
        spinner_tax_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Change the selected item's text color
                //  ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


}

