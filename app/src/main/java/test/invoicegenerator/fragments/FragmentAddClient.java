package test.invoicegenerator.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.Util;


public class FragmentAddClient extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_READ_CONTACTS = 0;
    DBHelper sqliteHelper;
    @BindView(R.id.add_client_button)
    Button Btn_AddClient;
    @BindView(R.id.client_name)
    EditText Et_Client_Name;
    @BindView(R.id.client_email)
    EditText Et_Client_Email;
    @BindView(R.id.client_phone)
    EditText Et_Client_Phone;
    @BindView(R.id.client_address)
    EditText Et_Client_Address;
    @BindView(R.id.client_city)
    EditText Et_Client_City;
    @BindView(R.id.client_state)
    EditText Et_Client_State;
    @BindView(R.id.client_country)
    EditText Et_Client_Country;
    @BindView(R.id.client_zip)
    EditText Et_Client_ZipCode;
    private OnItemSelectedListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_client,container,false);
        unbinder= ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sqliteHelper=new DBHelper(getActivity());
        Btn_AddClient.setOnClickListener(this);
        Et_Client_Name.setOnClickListener(this);
        Et_Client_Email.setOnClickListener(this);
        Et_Client_Phone.setOnClickListener(this);
        Et_Client_Address.setOnClickListener(this);
        Et_Client_City.setOnClickListener(this);
        Et_Client_State.setOnClickListener(this);
        Et_Client_Country.setOnClickListener(this);
        Et_Client_ZipCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.add_client_button:

                String StrName=Et_Client_Name.getText().toString();
                String StrEmail=Et_Client_Email.getText().toString();
                String StrPhone=Et_Client_Phone.getText().toString();
                String StrAddress=Et_Client_Address.getText().toString();
                String StrCity=Et_Client_City.getText().toString();
                String StrState=Et_Client_State.getText().toString();
                String StrCountry=Et_Client_Country.getText().toString();
                String StrPostalCode=Et_Client_ZipCode.getText().toString();
                validateAndSaveData(StrName,StrEmail,StrPhone,StrAddress,StrCity,StrState,StrCountry,StrPostalCode);
                //  loadFragment(new FragmentLogin());


                break;
        }
    }

    private void validateAndSaveData(String StrName,String StrEmail,String StrPhone,String StrAddress,String StrCity,
                                    String StrState,String StrCountry,String StrPostalCode) {
        if(StrEmail.equals(""))
        {
            Et_Client_Email.setError(getString(R.string.error_field_required));
            Et_Client_Email.requestFocus();
        }
        if(StrName.equals(""))
        {
            Et_Client_Name.setError(getString(R.string.error_field_required));
            Et_Client_Name.requestFocus();
        }
        else if(!Util.isFullname(StrName))
        {
            Et_Client_Name.setError(getString(R.string.error_invalid_name));
            Et_Client_Name.requestFocus();
        }

        else if(!Util.isEmailValid(StrEmail))
        {
            Et_Client_Email.setError(getString(R.string.error_invalid_email));
            Et_Client_Email.requestFocus();
        }
        else if(StrAddress.equals(""))
        {
            Et_Client_Address.setError(getString(R.string.error_field_required));
            Et_Client_Address.requestFocus();
        }
        else if(StrCity.equals(""))
        {
            Et_Client_City.setError(getString(R.string.error_field_required));
            Et_Client_City.requestFocus();
        }
        else if(StrState.equals(""))
        {
            Et_Client_State.setError(getString(R.string.error_field_required));
            Et_Client_State.requestFocus();
        }
        else if(StrCountry.equals("")) {
            Et_Client_Country.setError(getString(R.string.error_field_required));
            Et_Client_Country.requestFocus();
        }
        else if(!Util.isZipCodeValid(StrPostalCode))
        {
            Et_Client_ZipCode.setError(getString(R.string.error_invalid_zip_code));
            Et_Client_ZipCode.requestFocus();
        }
        else
        {
          /*  User user = new User();
            //  user.setId();
            user.setName(name.getText().toString() + 10);
            user.setEmail(email.getText().toString());
            user.setMobile(mobile.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setFax(fax.getText().toString());
            user.setContact(contact_text.getText().toString());
            user.setLine1(line1.getText().toString());
            user.setLine2(line2.getText().toString());
            user.setLine3(line3.getText().toString());
            RealmManager.createUserDao().save(user);*/
            ContentValues contentValues=new ContentValues();

            contentValues.put(DBHelper.CLIENT_Name,StrName);
            contentValues.put(DBHelper.CLIENT_EMAIL,StrEmail);
            contentValues.put(DBHelper.CLIENT_PHONE,StrPhone);
            contentValues.put(DBHelper.CLIENT_ADDRESS,StrAddress);
            contentValues.put(DBHelper.CLIENT_CITY,StrCity);
            contentValues.put(DBHelper.CLIENT_STATE,StrState);
            contentValues.put(DBHelper.CLIENT_COUNTRY,StrCountry);
            contentValues.put(DBHelper.CLIENT_ZIPCODE,StrPostalCode);

            if(sqliteHelper.AddNewClient(contentValues)>0){

                Et_Client_Name.getText().clear();
                Et_Client_Email.getText().clear();
                Et_Client_Address.getText().clear();
                Et_Client_City.getText().clear();
                Et_Client_Country.getText().clear();
                Et_Client_State.getText().clear();
                Et_Client_ZipCode.getText().clear();
                Et_Client_Phone.getText().clear();

                listener.onAddClientFragCallBack(1);
                Toast.makeText(getActivity(), "New Client Added", Toast.LENGTH_SHORT).show();

            }else
            {
                Toast.makeText(getActivity(), "Unable to Add Client", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public boolean isValidString(String Str){

        return !Str.isEmpty() && !Str.equalsIgnoreCase("null");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SavedCoupansLocationFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {

        void onAddClientFragCallBack(int position);
    }

}
