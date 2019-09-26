package test.invoicegenerator.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.general.Util;

public class ClientActivity extends AppCompatActivity {

    @BindView(R.id.add_client_button)
    Button add_client_button;

    EditText name;

    @BindView(R.id.client_email)
    EditText email;

    @BindView(R.id.client_phone)
    EditText phone;


    EditText contact_text;
    private String is_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_client);
        ButterKnife.bind(this);//
        init();

    }

    @SuppressLint("WrongViewCast")
    private void init() {

        is_new = getIntent().getStringExtra("is_new");

        name = findViewById(R.id.client_name);

        add_client_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveData();
            }
        });
        setClientData();

    }

    private void setClientData() {

        //  ClientInfo clientInfo=db.getClientInformation(FragmentEditReport.invoice_id);
        if (is_new.equals("true")) {
            Client clientInfo = (Client) getIntent().getSerializableExtra("client_obj");
            name.setText(clientInfo.getClientName());
            email.setText(clientInfo.getClientEmail());
            phone.setText(clientInfo.getClientPhone());
            contact_text.setText(clientInfo.getClientAddress());

        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = findViewById(R.id.toolbar);
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
        if (item.getItemId() == R.id.action_save) {
            validateAndSaveData();
        }

        return true;
    }

    private void validateAndSaveData() {
        if (email.getText().toString().equals("")) {
            email.setError(getString(R.string.error_field_required));
            email.requestFocus();
        }
        if (name.getText().toString().equals("")) {
            name.setError(getString(R.string.error_field_required));
            name.requestFocus();
        } else if (!Util.isFullname(name.getText().toString())) {
            name.setError(getString(R.string.error_invalid_name));
            name.requestFocus();
        } else if (!Util.isEmailValid(email.getText().toString())) {
            email.setError(getString(R.string.error_invalid_email));
            email.requestFocus();
        } else if (contact_text.getText().toString().equals("")) {
            contact_text.setError(getString(R.string.error_field_required));
            contact_text.requestFocus();
        } else { }


    }
}
