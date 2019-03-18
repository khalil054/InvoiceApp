package test.invoicegenerator.view.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.general.Util;

/**
 * Created by User on 1/14/2019.
 */

public class ClientActivity extends AppCompatActivity {

    @BindView(R.id.add_client_button)
    Button add_client_button;

    public static Client client_info;

    EditText name;

    @BindView(R.id.client_email)
    EditText email;

    @BindView(R.id.client_phone)
    EditText phone;


    EditText contact_text;

    DBHelper db;
    private String is_new;




    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_client);
        ButterKnife.bind(this);//
        init();
        //setActionBar();
        // loadFragment(new FragmentLogin());
    }

   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    private void init() {
        db=new DBHelper(this);
        is_new=getIntent().getStringExtra("is_new");
        //initializing components
        name=(EditText)findViewById(R.id.client_name);
        //contact_text=(EditText)findViewById(R.id.client_address);


        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });*/
        add_client_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveData();
            }
        });
        setClientData();
     //   RealmResults<User> dataList = RealmManager.createUserDao().loadAllAsync();
      //  dataList.size();
       // getClientData();
        //setting list adapter
    }
    private void setClientData()
    {

      //  ClientInfo clientInfo=db.getClientInformation(FragmentEditReport.invoice_id);
        if(is_new.equals("true"))
        {
            Client clientInfo = (Client) getIntent().getSerializableExtra("client_obj");
            name.setText(clientInfo.getClientName());
            email.setText(clientInfo.getClientEmail());
            phone.setText(clientInfo.getClientPhone());
            contact_text.setText(clientInfo.getClientAddress());

        }
    }


    private void getClientData() {
        client_info=db.getClientInformation(FragmentEditReport.invoice_id);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.client_menu);
        tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                validateAndSaveData();
                break;

            default:
                break;
        }

        return true;
    }
    private void validateAndSaveData() {
        if(email.getText().toString().equals(""))
        {
            email.setError(getString(R.string.error_field_required));
            email.requestFocus();
        }
        if(name.getText().toString().equals(""))
        {
            name.setError(getString(R.string.error_field_required));
            name.requestFocus();
        }
        else if(!Util.isFullname(name.getText().toString()))
        {
            name.setError(getString(R.string.error_invalid_name));
            name.requestFocus();
        }

        else if(!Util.isEmailValid(email.getText().toString()))
        {
            email.setError(getString(R.string.error_invalid_email));
            email.requestFocus();
        }
        else if(contact_text.getText().toString().equals(""))
        {
            contact_text.setError(getString(R.string.error_field_required));
            contact_text.requestFocus();
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

            contentValues.put(DBHelper.CLIENT_Name,name.getText().toString());
            contentValues.put(DBHelper.CLIENT_EMAIL,email.getText().toString());
            contentValues.put(DBHelper.CLIENT_PHONE,phone.getText().toString());
            contentValues.put(DBHelper.CLIENT_ADDRESS,contact_text.getText().toString());
            contentValues.put("invoice_key", FragmentEditReport.invoice_id);
          long client_key=db.AddNewClient(contentValues);
            if(client_key!=0)
            {
                Toast.makeText(getApplicationContext(), "done",
                        Toast.LENGTH_SHORT).show();
                getClientData();
                finish();
            } else{
                Toast.makeText(getApplicationContext(), "not done",
                        Toast.LENGTH_SHORT).show();
            }
            db.updateInvoiceData("client_key",String.valueOf(client_key), FragmentEditReport.invoice_id);
        }


    }
}
