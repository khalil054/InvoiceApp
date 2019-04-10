package test.invoicegenerator.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.SharedPreferenceHelper;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.Country;
import test.invoicegenerator.model.StateModel;

import static test.invoicegenerator.general.Constants.COMPANY_NAME;
import static test.invoicegenerator.general.Constants.EMAIL_KEY;
import static test.invoicegenerator.general.Constants.SIGN_UP_COLLECTION;



public class GeneralsFragment extends BaseFragment  {
    private Unbinder unbinder;
    public static int countryID, stateID;
    private String stateNameTextView, countryName,countryCode, countryPhoneCode, countryCurrency, cityName;
    @BindView(R.id.country_spinner)
    Spinner country_spinner;

    @BindView(R.id.company_name)
    EditText company_name;

    @BindView(R.id.country_name)
    EditText country_name;
    @BindView(R.id.email)
    EditText email;

   /* @BindView(R.id.et_phone)
    EditText phone_num;*/

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.zip_code)
    EditText zip_code;

    @BindView(R.id.state_value)
    EditText state_value;

    @BindView(R.id.form)
    LinearLayout layout;

    @BindView(R.id.save)
    Button save;
    private boolean state;
    private ArrayList<StateModel> state_list;
    private ArrayList<Country> country_list;






    /////////////Khalil Code////////////
    /*@BindView(R.id.save.button_letsGo)
    Button buttonGo;*/
    @BindView(R.id.et_phone)
    EditText etPhone;


    ///////////////////State,City Picker................

    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;

   /* buttonGo =  getView().findViewById(R.id.button_letsGo);
    etPhone =  getView().findViewById(R.id.et_phone);
    countryCodePicker = getView().findViewById(R.id.ccp);*/


    //  private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.general_fragment,container,
                false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init()
    {

        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();

         List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("item1");
        spinnerArray.add("item2");



       /* state_spinner.setAdapter(new StateAdapter(getActivity(),state_list));
        country_spinner.setAdapter(new CountryAdapter(getActivity(),country_list));*/

       // spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //    state_spinner.setAdapter(spinner_adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGeneralData();
            }
        });
       // db = FirebaseFirestore.getInstance();




        loadCompanyGeneralData();
       // StateModel model= (StateModel) state_spinner.getSelectedItem();

        layout.setBackgroundColor(Util.getColorWithAlpha(Color.BLACK, 0.2f));





    }

    private void loadCompanyGeneralData()
    {

        DocumentReference user =  db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(getActivity())).
                collection(Constants.GENERAL).document(Util.auth_idOfLoggedInUser(getActivity()));
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot doc = task.getResult();
                    dismissProgress();
                    if(doc.exists()) {
                        state=false;

                        Toast.makeText(getActivity(), String.valueOf(doc.get(Constants.PHONE_KEY).toString()), Toast.LENGTH_SHORT).show();
                       // company_name.setText(doc.get(Constants.COMPANY_NAME).toString());
                        email.setText(doc.get(Constants.EMAIL_KEY).toString());
                        etPhone.setText(doc.get(Constants.PHONE_KEY).toString());
                        address.setText(doc.get(Constants.ADDRESS).toString());
                        city.setText(doc.get(Constants.CITY).toString());
                        zip_code.setText(doc.get(Constants.ZIP_CODE).toString());
                        state_value.setText(doc.get(Constants.STATE).toString());
                        /*country_spinner.setSelection(Integer.parseInt(Util.getSelectedCountryIndex(doc.get(Constants.COUNTRY).toString()
                                ,Constants.COUNTRY_LIST)));*/

                        country_spinner.setSelection(Integer.parseInt(Util.getSelectedCountryIndex(doc.get(Constants.COUNTRY).toString()
                                ,Constants.COUNTRY_LIST)));
                        final String country_id=Integer.parseInt(Util.getSelectedCountryIndex(doc.get(Constants.COUNTRY).toString() ,Constants.COUNTRY_LIST))+1+"";

                        final Handler handler = new Handler();
                          handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                try {
                                   // if(state_names!=null)
                                      /*  state_spinner.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
                                                ,state_names));*/
                                 //   List<String> states=Util.retrieveAllItems(state_spinner);
                                 //   String selected_state=Util.getSelectedStateIndex(doc.get(Constants.STATE).toString(),country_id,Constants.STATE_LIST);
                                  /*  if(!selected_state.equals(""))
                                        state_spinner.setSelection(Integer.parseInt(selected_state));*/
                                } catch (NullPointerException e) {
                                    System.out.print("Caught the NullPointerException");
                                }

                            }
                        }, 9000);

                    }
                    else
                    {
                        state=true;
                      //  FirebaseData firebase=new FirebaseData(getActivity());
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                        dismissProgress();
                    }
                });


    }

    private void saveGeneralData() {
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(company_name.getText().toString())) {
            company_name.setError(getString(R.string.error_field_required));
            focusView = company_name;
            cancel = true;
        }

        // Check for a valid email address.
        else if(TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        else if(TextUtils.isEmpty(address.getText().toString()))
        {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        else if (TextUtils.isEmpty(company_name.getText().toString())) {
            company_name.setError(getString(R.string.error_field_required));
            focusView = company_name;
            cancel = true;
        }
        else if (TextUtils.isEmpty(city.getText().toString())) {
            city.setError(getString(R.string.error_field_required));
            focusView = city;
            cancel = true;
        }
        else if (TextUtils.isEmpty(zip_code.getText().toString())) {
            zip_code.setError(getString(R.string.error_field_required));
            focusView = zip_code;
            cancel = true;
        }else if (!Util.isEmailValid(email.getText().toString())) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           /* showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
           String StrCountryName=country_name.getText().toString();
           String PhNumber=etPhone.getText().toString();
           if(!StrCountryName.isEmpty()||!PhNumber.isEmpty()){


           }else {
               Toast.makeText(getActivity(), "Enter All Informations", Toast.LENGTH_SHORT).show();
           }

        }
    }
    public String getCompanyName( final ProgressDialog dialog)
    {
        // db = FirebaseFirestore.getInstance();
        final String[] name = {""};
        SharedPreferenceHelper helper=new SharedPreferenceHelper(getActivity());
        String email=helper.getValue(EMAIL_KEY);

        final DocumentReference docRef = db.collection(SIGN_UP_COLLECTION).document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> clients=document.getData();
                    dialog.dismiss();
                    if (document.exists()) {
                        name[0] =getCompanyData(document);
                        company_name.setText(name[0]);
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                    dialog.dismiss();
                    Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();

                }
            }
        });
        return name[0];
    }
    public String getCompanyData(DocumentSnapshot doc)
    {
        return doc.get(COMPANY_NAME).toString();
    }

}
