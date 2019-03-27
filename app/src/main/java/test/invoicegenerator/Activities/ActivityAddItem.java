package test.invoicegenerator.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;

/**
 * Created by User on 1/14/2019.
 */

public class ActivityAddItem extends AppCompatActivity {
    @BindView(R.id.save_data)
    TextView save_data;

    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.unit_cost_field)
    EditText unit_cost_field;

    @BindView(R.id.quantity_field)
    EditText quantity_field;

    @BindView(R.id.amount_field)
    TextView amount_field;

    @BindView(R.id.taxable_field)
    CheckBox taxable_field;

    @BindView(R.id.tax_rate_field)
    EditText tax_rate_field;

    @BindView(R.id.additional_field)
    EditText additional_field;

    @BindView(R.id.tax_rate_layout)
    LinearLayout tax_rate_layout;

    DBHelper db;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        ButterKnife.bind(this);//
        init();
        //setActionBar();
        // loadFragment(new FragmentLogin());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb =  findViewById(R.id.toolbar);
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
    private void init() {
        SharedPref.init(ActivityAddItem.this);

        FragmentEditReport.is_new="false";
        setValuesOfFields();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        // setActionBar(toolbar);
        db=new DBHelper(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    FragmentEditReport.productList.add(new ProductModel("First item","item des","10","45","450","0.0","1","51"));

               validateAndSaveData();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Util.isZipCodeValid(unit_cost_field.getText().toString()) && Util.isZipCodeValid(quantity_field.getText().toString())) {
                    if (!unit_cost_field.getText().toString().equals("") && !quantity_field.getText().toString().equals(""))
                        amount_field.setText(Integer.parseInt(unit_cost_field.getText().toString()) * Integer.parseInt(quantity_field.getText().toString()) + "");
                    else if (quantity_field.getText().toString().equals(""))
                        amount_field.setText(unit_cost_field.getText().toString());
                    else if (!quantity_field.getText().toString().equals("") && unit_cost_field.getText().toString().equals(""))
                        amount_field.setText("");
                }
            }
        };

        unit_cost_field.addTextChangedListener(textWatcher);
        quantity_field.addTextChangedListener(textWatcher);

        handleVisibilityOfTaxRate();


    }

    private void setValuesOfFields() {
        Bundle bundle=getIntent().getExtras();


        item_id=bundle.getString("id");
        description.setText(bundle.getString("des"));
        unit_cost_field.setText(bundle.getString("unit_cost"));
        quantity_field.setText(bundle.getString("quntity"));
        amount_field.setText(bundle.getString("amount"));
        additional_field.setText(bundle.getString("additional"));
    }

    private void handleVisibilityOfTaxRate() {


        taxable_field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(taxable_field.isChecked())
                    tax_rate_layout.setVisibility(View.VISIBLE);
                else
                    tax_rate_layout.setVisibility(View.GONE);
            }
        });
    }

    private void validateAndSaveData() {
      /*  JSONObject InvoiceItem = new JSONObject();
        try {
            InvoiceItem.put("name", "First item");
            InvoiceItem.put("description","item des");
            InvoiceItem.put("qty", "10");
            InvoiceItem.put("price","45");
            InvoiceItem.put("subtotal", "450");
            InvoiceItem.put("subtotal_with_tax_applied", "0.0");
            *//*InvoiceItem.put("tax_code_id","1");*//*
            InvoiceItem.put("company_id", "2");

            FragmentEditReport.InvoicesArray.put(InvoiceItem);

            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
       /* if(unit_cost_field.getText().toString().equals(""))
        {
            unit_cost_field.setError(getString(R.string.error_field_required));
            unit_cost_field.requestFocus();
        }
        if(!Util.isZipCodeValid(unit_cost_field.getText().toString()))
        {
            unit_cost_field.setError(getString(R.string.invalid_cost));
            unit_cost_field.requestFocus();
        }
        if(!Util.isZipCodeValid(quantity_field.getText().toString()))
        {
            quantity_field.setError(getString(R.string.invalid_quantity));
            quantity_field.requestFocus();
        }

        else
        {
          *//*  User user = new User();
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
            RealmManager.createUserDao().save(user);*//*
          *//*long item_key=db.insertItemData(description.getText().toString(),unit_cost_field.getText().toString(),quantity_field.getText().toString(),
                  amount_field.getText().toString(),String.valueOf(taxable_field.isChecked()),tax_rate_field.getText().toString(),additional_field.getText().toString()
                  , FragmentEditReport.invoice_id);
            if(item_key!=-1){
                Toast.makeText(getApplicationContext(), "done",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else{
                Toast.makeText(getApplicationContext(), "not done",
                        Toast.LENGTH_SHORT).show();
            }
            db.updateInvoiceData("item_key",String.valueOf(item_key), FragmentEditReport.invoice_id);*//*





            ;
        }*/
        String CompanyID= String.valueOf(SharedPref.read(SharedPref.CompanyID,""));
        if(TextUtils.isEmpty(description.getText().toString())){
            description.setError(getString(R.string.invalid_quantity));
            description.requestFocus();
        }else if(TextUtils.isEmpty(quantity_field.getText().toString())){
            quantity_field.setError(getString(R.string.invalid_quantity));
            quantity_field.requestFocus();
        }else if(TextUtils.isEmpty(unit_cost_field.getText().toString())){
            unit_cost_field.setError(getString(R.string.invalid_cost));
            unit_cost_field.requestFocus();
        }else if(TextUtils.isEmpty(amount_field.getText().toString())){
            amount_field.setError(getString(R.string.invalid_quantity));
            amount_field.requestFocus();
        }else if(TextUtils.isEmpty(CompanyID)){
             /* amount_field.setError(getString(R.string.invalid_quantity));
              amount_field.requestFocus();*/
            Toast.makeText(this, "Invalid Company:"+CompanyID, Toast.LENGTH_SHORT).show();
        }else {

/*
            JsonObject InvoiceItem = new JsonObject();

            InvoiceItem.addProperty("name", "First item");
            InvoiceItem.addProperty("description","item des");
            InvoiceItem.addProperty("qty", "10");
            InvoiceItem.addProperty("price","45");
            InvoiceItem.addProperty("subtotal", "450");
            InvoiceItem.addProperty("subtotal_with_tax_applied", "0.0");
          *//*  InvoiceItem.addProperty("tax_code_id","1");*//*
            InvoiceItem.addProperty("company_id", "2");

            FragmentEditReport.InvoicesArray.add(InvoiceItem);

            finish();*/





            Item value_item = new Item();
            String nam = description.getText().toString();
            String amount = amount_field.getText().toString();

            if(!amount.equals(""))
                FragmentEditReport.subtotal_value = FragmentEditReport.subtotal_value + Integer.parseInt(amount);
          //  FragmentEditReport.items.add(nam);
            value_item.setDescription(description.getText().toString());
            value_item.setAmount(amount_field.getText().toString());
            value_item.setQuantity( quantity_field.getText().toString());
            value_item.setUnit_cost(unit_cost_field.getText().toString());
            if(taxable_field.isChecked()){
                value_item.setTaxable("true");
            }else {
                value_item.setTaxable("false");
            }

            value_item.setAdditional(additional_field.getText().toString());
            FragmentEditReport.item_values.add(value_item);



            JSONObject InvoiceItem = new JSONObject();

            try {
                InvoiceItem.put("name", description.getText().toString());
                InvoiceItem.put("description", description.getText().toString());
                InvoiceItem.put("qty", quantity_field.getText().toString());
                InvoiceItem.put("price", unit_cost_field.getText().toString());
                InvoiceItem.put("subtotal",amount_field.getText().toString());
                InvoiceItem.put("subtotal_with_tax_applied", "0.0");
               // InvoiceItem.put("tax_code_id","1");
                InvoiceItem.put("company_id", String.valueOf(SharedPref.read(SharedPref.CompanyID,"")));



              /*  Item value_item = new Item();
               String nam = description.getText().toString();
                String amount = amount_field.getText().toString();

                if(!amount.equals(""))
                    FragmentEditReport.subtotal_value = FragmentEditReport.subtotal_value + Integer.parseInt(amount);
                FragmentEditReport.items.add(nam);
                value_item.setDescription(description.getText().toString());
                value_item.setAmount(amount_field.getText().toString());
                value_item.setQuantity( quantity_field.getText().toString());
                value_item.setUnit_cost(unit_cost_field.getText().toString());
                if(taxable_field.isChecked()){
                    value_item.setTaxable("true");
                }else {
                    value_item.setTaxable("false");
                }

                value_item.setAdditional(additional_field.getText().toString());
                FragmentEditReport.item_values.add(value_item);
*/


     /*            InvoiceItem.put("name", "First item");
                InvoiceItem.put("description","item des");
                InvoiceItem.put("qty", "10");
                InvoiceItem.put("price","45");
                InvoiceItem.put("subtotal", "450");
                InvoiceItem.put("subtotal_with_tax_applied", "0.0");
                InvoiceItem.put("tax_code_id","1");
                InvoiceItem.put("company_id", "2");*/

                FragmentEditReport.InvoicesArray.put(InvoiceItem);

                finish();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
