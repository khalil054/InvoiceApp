package test.invoicegenerator.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.pepperonas.materialdialog.MaterialDialog;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.menu_adapter;
import test.invoicegenerator.fragment_handler.Fragment_Model;
import test.invoicegenerator.fragments.Configrations;
import test.invoicegenerator.fragments.DashboardFragment;
import test.invoicegenerator.fragments.FragmentAddClient;
import test.invoicegenerator.fragments.FragmentAllClients;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentLogin;
import test.invoicegenerator.fragments.FragmentReport;
import test.invoicegenerator.fragments.FragmentUpdateClient;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.model.SharedPref;


public class MainActivity extends BaseActivity  implements
        FragmentAllClients.OnItemSelectedListener,
        FragmentAddClient.OnItemSelectedListener,
        FragmentUpdateClient.OnItemSelectedListener
        {
           /* public static Fragment_Model CurrentFragment;
            public static FragmentTransaction fragmentTransaction;
            public static FragmentManager fm;
            public static List<Fragment_Model> FragmentsList;
            public static int FragmentPosition = -1;*/

    Progressbar cdd;
    Snackbar snackbar;
    ConstraintLayout main_layout;
    DrawerLayout mDrawerLayout;
    View leftMenuView;
    RelativeLayout menu_icon;
    ListView menulist;
    VolleyService mVolleyService;
    IResult mResultCallback = null;

    ArrayList<String> Name = new ArrayList<>();
    ArrayList<Integer> Pic = new ArrayList<>();
  /*  DBHelper sqliteHelper;
    private DBHelper db;*/

    Button back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainframe);
        ButterKnife.bind(this);
       // sqliteHelper=new DBHelper(MainActivity.this);

        loadFragment(new DashboardFragment(),null);

        init();

        hookClickEvents();


    }


    private void init() {
      /*  FragmentsList = new ArrayList<>();
        fm = getSupportFragmentManager();
*/

        cdd=new Progressbar(MainActivity.this);
        //db=new DBHelper(this);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu_icon =  findViewById(R.id.menu_icon);
        leftMenuView = findViewById(R.id.leftDrawerLayout);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        menulist = findViewById(R.id.menu_list);
        back_btn =findViewById(R.id.back_btn);

        //OpenFragment(new DashboardFragment(),getResources().getString(R.string.tag_dashboard),null,getResources().getString(R.string.tag_dashboard));

    }



          /*  public static void OpenFragment(Fragment fragment, String tag, Bundle bundle, String Title)
            {
                try
                {
                    for (int count = 0; count < FragmentsList.size(); count++)
                    {
                        if (FragmentsList.get(count).getTagName().equals(tag))
                        {
                            fragment = FragmentsList.get(count).getFragment();
                            FragmentsList.remove(count);
                            FragmentPosition--;
                            break;
                        }
                    }


                    Fragment_Model fragment_model = new Fragment_Model(tag, fragment, bundle, Title);
                    FragmentsList.add(fragment_model);

                    fragmentTransaction = fm.beginTransaction();

                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                            R.anim.slide_left, R.anim.slide_right);
                    fragmentTransaction.replace(R.id.fragment_frame, fragment, tag);
                    fragmentTransaction.commit();
                    FragmentPosition++;

                }
                catch (Exception e)
                {
                    Log.d("tag", "OpenFragment : \n" + e.getMessage());
                }
            }*/

    private void loadFragment(Fragment dashboardFragment,Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
    public void openLeftMenu() {
        mDrawerLayout.openDrawer(leftMenuView);
    }

    public void closeLeftMenu() {
        mDrawerLayout.closeDrawer(leftMenuView);
    }

    private void hookClickEvents() {

        slideMenuClickEvents();
    }

    private void slideMenuClickEvents() {
        menu_icon.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                openLeftMenu();
            }
        });

        menuData();
        menulist.setAdapter(new menu_adapter(this,Name,Pic,99) );
        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                closeLeftMenu();
                menuData();
                FragmentManager fragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position){
                    case 0:
                        Pic.set( position, R.drawable.ic_menu_clients );
                        loadFragment(new DashboardFragment(),null);
                        break;

                    case 1:
                        Pic.set( position, R.drawable.ic_menu_clients );

                        loadFragment(new FragmentAllClients(),null);

                        break;
                    case 2:
                        Pic.set( position, R.drawable.ic_menu_report );Pic.set(position, R.drawable.ic_menu_report);
                        loadFragment(new FragmentEditReport(),null);

                        break;
                    case 3:
                        Pic.set( position, R.drawable.ic_menu_addinvoice );
                        break;
                    case 4:
                        Pic.set( position, R.drawable.ic_menu_invoicereport );

                        break;
                    case 5:
                        Pic.set( position, R.drawable.ic_menu_configurations );
                        loadFragment(new Configrations(),null);
                        break;
                    case 6:
                        Pic.set( position, R.drawable.ic_menu_settings );

                        break;
                    case 7:
                        Pic.set( position, R.drawable.ic_menu_logout );
                        Logout();
                        break;

                }

                menulist.setAdapter(new menu_adapter(MainActivity.this,Name,Pic,position) );




            }
        });



    }

    public void ChangeMenuOption(int position)
    {
        switch (position) {
            case 0:
                Pic.set(position, R.drawable.ic_menu_clients);

                loadFragment(new FragmentAllClients(),null);
                break;

            case 1:
                Pic.set(position, R.drawable.ic_menu_clients);
                loadFragment(new DashboardFragment(),null);
                break;

            case 2:
                Pic.set(position, R.drawable.ic_menu_report);
                loadFragment(new FragmentReport(),null);
               /* Cursor rs=db.getInvoiceData();
                if(rs.isAfterLast() == false)
                    loadFragment(new FragmentReport(),null);
                else
                {
                    Bundle args = new Bundle();
                    args.putString("new", "true");
                    args.putString("clicked", "false");
                    loadFragment(new FragmentEditReport(),args);
                }*/
                break;

            case 3:
                Pic.set(position, R.drawable.ic_menu_addinvoice);
                break;

            case 4:
                Pic.set(position, R.drawable.ic_menu_invoicereport);
                break;

            case 5:
                Pic.set(position, R.drawable.ic_menu_configurations);
                break;

            case 6:
                Pic.set(position, R.drawable.ic_menu_settings);
                break;

            default:
                break;
        }
        menulist.setAdapter(new menu_adapter(MainActivity.this,Name,Pic,position) );

    }



    public void menuData()
    {
        Name.clear();
        Pic.clear();

        Name.add("DASHBOARD");
        Name.add("CLIENTS");
        Name.add("REPORTS");
        Name.add("ADD INVOICE");
        Name.add("INVOICE REPORT");
        Name.add("CONFIGRATION");
        Name.add("SETTINGS");
        Name.add("LOGOUT");

        Pic.add(R.drawable.ia_client);
        Pic.add(R.drawable.ia_client);
        Pic.add(R.drawable.ia_report);
        Pic.add(R.drawable.ia_addinvoice);
        Pic.add(R.drawable.ia_invoicereport);
        Pic.add(R.drawable.ia_configurations);
        Pic.add(R.drawable.ia_settings);
        Pic.add(R.drawable.ia_logout);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_privacy:
                    Intent Send=new Intent(MainActivity.this,AppPrivacyPolicy.class);
                    startActivity(Send);
                    finish();
                    return true;
                case R.id.navigation_share:

                    return true;
                case R.id.navigation_more:

                    return true;
                case R.id.navigation_rateus:

                    return true;

            }

            return false;
        }
    };

    void Logout()
    {
        new MaterialDialog.Builder(MainActivity.this)
                .title("Logout")
                .message("You really want to logout?")
                .positiveText("LOGOUT")
                .neutralText("NOT NOW")
                .positiveColor(R.color.colorAccent)
                .neutralColor(R.color.colorPrimaryDark)
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog d) {
                        super.onShow(d);

                    }
                })
                .dismissListener(new MaterialDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                    }
                })
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        DataSendToServerForSignOut();
                        return;

                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);

                    }

                })
                .show();
    }

    void DataSendToServerForSignOut()
    {

        cdd.ShowProgress();
        initVolleyCallbackForSignOut();
        mVolleyService = new VolleyService(mResultCallback,MainActivity.this);
        Map<String, String> data = new HashMap<String, String>();
        SharedPref.init(MainActivity.this);
        String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
        String client=SharedPref.read(Constants.CLIENT,"");
        String uid=SharedPref.read(Constants.UID,"");
        HashMap<String, String>  headers = new HashMap<String, String>();
        headers.put("access-token", access_token);
        headers.put("client",client );
        headers.put("uid",uid);
      //  mVolleyService.DeleteDataVolley("DELETECALL", NetworkURLs.BaseURL + NetworkURLs.SignOut,data,headers );


    }

    void initVolleyCallbackForSignOut(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");


                    if(status.equals("true"))
                    {

                        cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                loadFragment(new  FragmentLogin(),null);
                            }
                        }, 1000);//1000
                    }else {

                        cdd.HideProgress();
                        String error = jsonObject.getString("Error");
                        snackbar = Snackbar.make(main_layout, error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                cdd.HideProgress();
                error.printStackTrace();
                snackbar = Snackbar.make(main_layout, error.toString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
    void DataSendToServerForChangePassword()
    {

        initVolleyCallbackForChangePassword();
        mVolleyService = new VolleyService(mResultCallback,this);
        Map<String, String> data = new HashMap<String, String>();

        data.put("current_password","password123");
        data.put("password",/*pinView.getText().toString()*/"password789");
        data.put("password_confirmation",/*pinView.getText().toString()*/"password789");

        SharedPref.init(this);
        String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
        String client=SharedPref.read(Constants.CLIENT,"");
        String uid=SharedPref.read(Constants.UID,"");
        HashMap<String, String>  headers = new HashMap<String, String>();
        headers.put("access-token", access_token);
        headers.put("client",client );
        headers.put("uid",uid);

        mVolleyService.putDataVolley("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.ChangePassword,data,headers );


    }

    private void initVolleyCallbackForChangePassword() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status = jsonObject.getString("status");


                    if(status.equals("true"))
                    {


                        // cdd.HideProgress();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // finish();

                            }
                        }, 1000);//1000
                    }else {

                        // cdd.HideProgress();
                        String error = jsonObject.getString("Error");
                        String err="";
                        if(error.contains("AuthFail"))
                            err=getString(R.string.verification_code);
                        Toasty.error(MainActivity.this,err, Toast.LENGTH_SHORT).show();
                        // snackbar = Snackbar.make(main_layout, error, Snackbar.LENGTH_LONG);
                        //  snackbar.show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void notifyError(String requestType,VolleyError error) {
                error.printStackTrace();
                String err="";
                //  if(error.toString().contains("AuthFail"))
                err=getString(R.string.verification_code);

                Toasty.error(MainActivity.this,err, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }

    @Override
    public void onAllClientFragCallBack(int position) {

      //  Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        if(position==1){
            Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();

            loadFragment(new FragmentAllClients(),null);

        }else {
            Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();

            loadFragment(new FragmentAddClient(),null);

        }

    }

    @Override
    public void onAddClientFragCallBack(int position) {
        if(position==1){
            loadFragment(new FragmentAllClients(),null);
        }
    }

    public void LoadAddressFields(View view)
    {
        final PlacesAutocompleteTextView Et_Client_Address = (view.findViewById(R.id.places_autocomplete));
        final EditText Et_Client_City = ( view.findViewById(R.id.client_city));
        final EditText Et_Client_State = (view.findViewById(R.id.client_state));
        final EditText Et_Client_Country = ( view.findViewById(R.id.client_country));
        final EditText Et_Client_ZipCode = ( view.findViewById(R.id.client_zip));

        Et_Client_Address.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                Et_Client_Address.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {

                        Et_Client_City.setText("");
                        Et_Client_State.setText("");
                        Et_Client_Country.setText("");
                        Et_Client_ZipCode.setText("");

                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {


                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        Et_Client_City.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        Et_Client_State.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        Et_Client_Country.setText(component.long_name);
                                        break;
                                    case POSTAL_CODE:
                                        Et_Client_ZipCode.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(final Throwable failure) {

                    }
                });
            }
        });

    }

          /*  @Override
            public void onBackPressed() {
                if (FragmentsList.size() > 1) {


                    fragmentTransaction = fm.beginTransaction();

                    int PreviousFragmentPosition = FragmentPosition - 1;
                    Fragment_Model fragment_model = FragmentsList.get(PreviousFragmentPosition);
                    FragmentsList.remove(FragmentPosition);
                    FragmentPosition--;
                    CurrentFragment = fragment_model;
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                            R.anim.slide_right, R.anim.slide_left);
                    fragmentTransaction.replace(R.id.fragment_frame, CurrentFragment.getFragment());

                    fragmentTransaction.commit();
                }else {
                    Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
                }
            }*/
        }
