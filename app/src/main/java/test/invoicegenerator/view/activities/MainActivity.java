package test.invoicegenerator.view.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.pepperonas.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
import test.invoicegenerator.databaseutilities.DBHelper;
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
        FragmentAddClient.OnItemSelectedListener,FragmentUpdateClient.OnItemSelectedListener
        {

    Progressbar cdd;
    Snackbar snackbar;
    ConstraintLayout main_layout;

    DrawerLayout mDrawerLayout;
    View leftMenuView;
    RelativeLayout menu_icon;
    ListView menulist;
    VolleyService mVolleyService;
    IResult mResultCallback = null;
    boolean doubleBackToExitPressedOnce = false;

    ArrayList<String> Name = new ArrayList<>();
    ArrayList<Integer> Pic = new ArrayList<>();
    DBHelper sqliteHelper;
    private DBHelper db;

    Button back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainframe);
        ButterKnife.bind(this);//
        sqliteHelper=new DBHelper(MainActivity.this);

        loadFragment(new DashboardFragment(),null);

        init();

        hookClickEvents();


    }
/*
    public void replaceMenuButton(){

        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            android.support.v4.app.Fragment f =
                    getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
            if (f instanceof FragmentAllClients) {
                Toast.makeText(this, "FragmentAllClients", Toast.LENGTH_SHORT).show();
            }

        }



    }*/

    private void init() {

        cdd=new Progressbar(MainActivity.this);
        db=new DBHelper(this);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu_icon =  findViewById(R.id.menu_icon);
        leftMenuView = findViewById(R.id.leftDrawerLayout);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        menulist = findViewById(R.id.menu_list);
        back_btn = (Button) findViewById(R.id.back_btn);



    }

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
                        Cursor rs=db.getInvoiceData();
                        if(rs.isAfterLast() == false)
                            loadFragment(new FragmentReport(),null);
                        else
                        {
                            Bundle args = new Bundle();
                            args.putString("new", "true");
                            args.putString("clicked", "false");
                            loadFragment(new FragmentEditReport(),args);
                        }

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
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
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
                Cursor rs=db.getInvoiceData();
                if(rs.isAfterLast() == false)
                    loadFragment(new FragmentReport(),null);
                else
                {
                    Bundle args = new Bundle();
                    args.putString("new", "true");
                    args.putString("clicked", "false");
                    loadFragment(new FragmentEditReport(),args);
                }
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
//                        SharedPref.init(MainFrame.this.getApplicationContext());
//                        SharedPref.write(SharedPref.LoginID, "0");
//                        SharedPref.write(SharedPref.PROFILE_COMPLETED,"No");
//
//                        Intent intent = new Intent(MainFrame.this,AuthenticationFrame.class);
//                        startActivity(intent);
//                        finish();
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

  /*  @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }*/


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

        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
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

        }
