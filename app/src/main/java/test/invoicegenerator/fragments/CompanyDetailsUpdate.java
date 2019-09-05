package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.general.Util;

public class CompanyDetailsUpdate extends BaseFragment {
    @BindView(R.id.company_name)
    EditText company_name;

    @BindView(R.id.company_email)
    EditText company_email;

    @BindView(R.id.company_phone)
    EditText company_phone;

    @BindView(R.id.company_address)
    EditText company_address;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.state)
    EditText state;


    @BindView(R.id.country)
    EditText country;

    @BindView(R.id.btn_save_data_company)
    Button BtnSave;

    @BindView(R.id.zip_code)
    EditText zip_code;

    @BindView(R.id.header_layout)
    LinearLayout LayoutParent;



    public static CompanyDetailsUpdate newInstance() {
        CompanyDetailsUpdate fragment = new CompanyDetailsUpdate();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_company_details_update, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;

    }
    private void init()
    {

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSendDataToNext();
            }
        });

        company_email.setText(GlobalData.StrCompanyEmail);
        company_address.setText(GlobalData.StrCompanyAddress);
        company_name.setText(GlobalData.StrCompanyName);
        company_phone.setText(GlobalData.StrCompanyPhone);
        city.setText(GlobalData.StrCompanyCity);
        country.setText(GlobalData.StrCompanyCountry);
        state.setText(GlobalData.StrCompanyState);
        zip_code.setText(GlobalData.StrCompanyZipCode);


    }

    private void validateAndSendDataToNext() {
        if(company_name.getText().toString().equals(""))
        {
            company_name.setError(getString(R.string.error_field_required));
            company_name.requestFocus();
        }
        else if(!Util.isFullname(company_name.getText().toString()))
        {
            company_name.setError(getString(R.string.error_invalid_name));
            company_name.requestFocus();
        }
        else if(company_email.getText().toString().equals(""))
        {
            company_email.setError(getString(R.string.error_field_required));
            company_email.requestFocus();
        }
        else if(!Util.isEmailValid(company_email.getText().toString()))
        {
            company_email.setError(getString(R.string.error_invalid_email));
            company_email.requestFocus();
        }
        else if(company_phone.getText().toString().equals(""))
        {
            company_phone.setError(getString(R.string.error_field_required));
            company_phone.requestFocus();
        }
        else if(company_address.getText().toString().equals(""))
        {
            company_address.setError(getString(R.string.error_field_required));
            company_address.requestFocus();
        }
        else if(city.getText().toString().equals(""))
        {
            city.setError(getString(R.string.error_field_required));
            city.requestFocus();
        }
        else if(country.getText().toString().equals(""))
        {
            country.setError(getString(R.string.error_field_required));
            country.requestFocus();
        }
        else if(state.getText().toString().equals(""))
        {
            state.setError(getString(R.string.error_field_required));
            state.requestFocus();
        }
        else if(zip_code.getText().toString().equals(""))
        {
            zip_code.setError(getString(R.string.error_field_required));
            zip_code.requestFocus();
        }
        else if(!Util.isZipCodeValid(zip_code.getText().toString()))
        {
            zip_code.setError(getString(R.string.error_invalid_zip_code));
            zip_code.requestFocus();
        }else {
            GlobalData.StrCompanyName=company_name.getText().toString();
            GlobalData.StrCompanyEmail=company_email.getText().toString();
            GlobalData.StrCompanyPhone=company_phone.getText().toString();
            GlobalData.StrCompanyAddress=company_address.getText().toString();
            GlobalData.StrCompanyCity=city.getText().toString();
            GlobalData.StrCompanyState=state.getText().toString();
            GlobalData.StrCompanyCountry=country.getText().toString();
            GlobalData.StrCompanyZipCode=zip_code.getText().toString();

            showErrorMessage(LayoutParent,"Switch Tab To Fill Other Informations,Thanks");


        }

    }



}
