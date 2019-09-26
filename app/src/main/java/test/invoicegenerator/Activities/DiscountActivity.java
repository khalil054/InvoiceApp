package test.invoicegenerator.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;

public class DiscountActivity extends AppCompatActivity {

    @BindView(R.id.discount_value)
    EditText discount_value;

    @BindView(R.id.discount_spinner)
    Spinner discount_spinner;

    public static int discount_amount;
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_activity);
        ButterKnife.bind(this);//
        init();

    }

    private void init() {
        setComponentsValues();
        Toolbar toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        // setActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                if (!discount_value.getText().toString().equals(""))
                    discount_amount = Integer.parseInt(discount_value.getText().toString());
                type = discount_spinner.getSelectedItem().toString();
                TaxActivity.type = discount_spinner.getSelectedItem().toString();
                finish();
            }
        });

    }

    private void setComponentsValues() throws NullPointerException {
        String dis_type;
        int discount;
        if (FragmentEditReport.IsNewInvoice) {
            dis_type = getIntent().getStringExtra("discount_type");
            discount = getIntent().getIntExtra("discount", 0);
        } else {
            dis_type = FragmentEditReportUpdate.discount_type;
            discount = 0;
        }


        if (!dis_type.equals("")) {
            if (dis_type.equals("percentage")) {
                discount_spinner.setSelection(0);
                type = dis_type;
            } else if (dis_type.equals("Flat Item")) {
                discount_spinner.setSelection(1);
                type = dis_type;
            }
        }
        // if (!discount.equals("") && discount != null)
        discount_value.setText(String.valueOf(discount + ""));
        discount_amount = discount;
    }

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
        if (item.getItemId() == R.id.action_save) {//   validateAndSaveData();
        }

        return true;
    }
}
