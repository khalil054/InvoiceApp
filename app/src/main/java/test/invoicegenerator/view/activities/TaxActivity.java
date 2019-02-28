package test.invoicegenerator.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;

/**
 * Created by User on 1/17/2019.
 */

public class TaxActivity extends AppCompatActivity {
    @BindView(R.id.tax_spinner)
    Spinner tax_spinner;

    @BindView(R.id.tax_value)
    EditText tax_value;

    public static String type;
    public static int tax_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_activity);
        ButterKnife.bind(this);//
        init();
        //setActionBar();
        // loadFragment(new FragmentLogin());
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        // setActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                type=tax_spinner.getSelectedItem().toString();
                if(!tax_value.getText().toString().equals(""))
                tax_amount=Integer.parseInt(tax_value.getText().toString());
                else
                    tax_amount=0;
                finish();
            }
        });
        setComponentsValues();
    }

    private void setComponentsValues() throws NullPointerException {
        String tax_type=getIntent().getStringExtra("tax_type");
        int tax=getIntent().getIntExtra("tax",0);
        if(!tax_type.equals("") && tax_type!=null)
        {
            if(tax_type.equals("On the total"))
                tax_spinner.setSelection(0);
            else if(tax_type.equals("Deducted"))
                tax_spinner.setSelection(1);
        }
        // if (!discount.equals("") && discount != null)
        tax_value.setText(tax+"");
    }
}
