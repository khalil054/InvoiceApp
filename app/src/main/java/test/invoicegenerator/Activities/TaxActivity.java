package test.invoicegenerator.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;

public class TaxActivity extends AppCompatActivity {
    @BindView(R.id.tax_spinner)
    Spinner tax_spinner;

    @BindView(R.id.tax_value)
    EditText tax_value;

    public static String type = "On the total";
    public static int tax_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_activity);
        ButterKnife.bind(this);//
        init();

    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = tax_spinner.getSelectedItem().toString();
                if (!tax_value.getText().toString().equals(""))
                    tax_amount = Integer.parseInt(tax_value.getText().toString());
                else
                    tax_amount = 0;
                finish();
            }
        });
        setComponentsValues();
    }

    @SuppressLint("SetTextI18n")
    private void setComponentsValues() throws NullPointerException {
        if (!type.equals("")) {
            if (type.equals("On the total"))
                tax_spinner.setSelection(0);
            else if (type.equals("Deducted"))
                tax_spinner.setSelection(1);
        }
        // if (!discount.equals("") && discount != null)
        tax_value.setText(0 + "");
    }
}
