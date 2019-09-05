package test.invoicegenerator.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.SharedPref;
/**
 * Created by User on 1/14/2019.
 */

public class ActivityAddItemUpdate extends AppCompatActivity {
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


    @BindView(R.id.remove_invoice_item)
    CheckBox remove_invoice_item;

    @BindView(R.id.tax_rate_field)
    EditText tax_rate_field;

    @BindView(R.id.additional_field)
    EditText additional_field;

    @BindView(R.id.tax_rate_layout)
    LinearLayout tax_rate_layout;
    @BindView(R.id.tax_code_field)
    TextView tax_code_field;
   // DBHelper db;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_invoice_item_activity);
        ButterKnife.bind(this);
        init();

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
        SharedPref.init(ActivityAddItemUpdate.this);
        assignValuesToFields();
        /*FragmentEditReport.is_new="false";
        if(FragmentEditReport.IsNewInvoice){
            setValuesOfFields();
        }else {
           // setValuesOfFieldsUpdate();
        }*/

        Toolbar toolbar =  findViewById(R.id.toolbar); // Attaching the layout to the toolbar object

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // finish();
               validateAndSaveData();
            }
        });

       /* TextWatcher textWatcher = new TextWatcher() {
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
        quantity_field.addTextChangedListener(textWatcher);*/
        handleVisibilityOfTaxRate();



    }

    private void assignValuesToFields() {
        item_id=GlobalData.SelectedInvoiceItem.getId();
        description.setText(GlobalData.SelectedInvoiceItem.getDescription());
        unit_cost_field.setText(GlobalData.SelectedInvoiceItem.getUnit_cost());
        quantity_field.setText(GlobalData.SelectedInvoiceItem.getQuantity());
        Double d1=Double.parseDouble(GlobalData.SelectedInvoiceItem.getUnit_cost());
        Integer qty=Integer.parseInt(GlobalData.SelectedInvoiceItem.getQuantity());
      //  Toast.makeText(this, String.valueOf(d1*qty), Toast.LENGTH_SHORT).show();
         amount_field.setText(String.valueOf(d1*qty));
        taxable_field.setChecked(true);
        additional_field.setText(GlobalData.SelectedInvoiceItem.getAdditional());

        tax_code_field.setText(GlobalData.SelectedInvoiceItem.getTax_Id());
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
                if(remove_invoice_item.isChecked()){
                    InvoiceItem.put("_destroy", 1);
                }


                Item value_item = new Item();
                String nam = description.getText().toString();
                String amount = amount_field.getText().toString();
                Double sub_amount=Double.parseDouble(amount);
               // Toast.makeText(this, String.valueOf(sub_amount), Toast.LENGTH_SHORT).show();

                if(!TextUtils.isEmpty(amount))
                    FragmentEditReportUpdate.subtotal_value = FragmentEditReportUpdate.subtotal_value + sub_amount;

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

                if(FragmentEditReport.IsNewInvoice){
                    FragmentEditReport.item_values.add(value_item);
                    FragmentEditReport.InvoicesArray.put(InvoiceItem);
                }else {
                    if(FragmentEditReportUpdate.shouldupdatepreviousvalue){
                        FragmentEditReportUpdate.shouldupdatepreviousvalue=false;
                        InvoiceItem.put("id",FragmentEditReportUpdate.SelctedItemID);
                        FragmentEditReportUpdate.InvoicesArray.remove(FragmentEditReportUpdate.selectedLvPosition);
                        FragmentEditReportUpdate.item_values.remove(FragmentEditReportUpdate.selectedLvPosition);
                    }
                    FragmentEditReportUpdate.item_values.add(value_item);
                    FragmentEditReportUpdate.InvoicesArray.put(InvoiceItem);
                }
                finish();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
