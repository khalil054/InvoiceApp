package test.invoicegenerator.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;

public class InvoiceInfoActivity extends AppCompatActivity {
    @BindView(R.id.invoice_date)
    EditText invoice_date;

    @BindView(R.id.due_date)
    EditText due_date;

    @BindView(R.id.invoice_name)
    EditText invoice_name;

 //   private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_info_activity);
        ButterKnife.bind(this);//
        init();
        //setActionBar();
        // loadFragment(new FragmentLogin());
    }

    private void init() {

        if(!FragmentEditReport.IsNewInvoice){
            setDateToFields();
        }
       /* InvoiceModel invoice= (InvoiceModel) getIntent().getSerializableExtra("info");
        invoice_name.setText(invoice.getInvoice_name());
        due_date.setText(invoice.getDue_date());
        invoice_date.setText(invoice.getInvoice_date());*/
      //  db=new DBHelper(this);
        Toolbar toolbar =  findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        // setActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FragmentEditReport.IsNewInvoice){
                    FragmentEditReport.InvoiceDueDate=due_date.getText().toString();
                    FragmentEditReport.InvoiceCreateDate=invoice_date.getText().toString();
                    FragmentEditReport.StrInvoiceName=invoice_name.getText().toString();
                }else {
                    FragmentEditReportUpdate.InvoiceDueDate=due_date.getText().toString();
                    FragmentEditReportUpdate.InvoiceCreateDate=invoice_date.getText().toString();
                    FragmentEditReportUpdate.StrInvoiceName=invoice_name.getText().toString();
                }

                // perform whatever you want on back arrow click
               // db.updateInvoiceInfo(invoice_name.getText().toString(),invoice_date.getText().toString(),due_date.getText().toString(), FragmentEditReport.invoice_id);
                finish();
            }
        });
        invoice_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog("invoice");
            }
        });
        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DateDialog("Due");
            }
        });
    }

    private void setDateToFields() {
        invoice_date.setText(FragmentEditReportUpdate.InvoiceCreateDate);
        due_date.setText(FragmentEditReportUpdate.InvoiceDueDate);
    }

    public void DateDialog(final String identifier){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear=monthOfYear+1;
                if(FragmentEditReport.IsNewInvoice){
                    if(identifier.equals("invoice")){
                        invoice_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                        FragmentEditReport.InvoiceCreateDate=year + "-" + monthOfYear + "-" + dayOfMonth;
                    }
                    else{
                        FragmentEditReport.InvoiceDueDate=year + "-" + monthOfYear + "-" + dayOfMonth;
                        /* due_date.setText(dayOfMonth + "/" + monthOfYear+ "/" + year);*/
                        due_date.setText(year + "-" + monthOfYear+ "-" + dayOfMonth);
                    }

                }else {
                    if(identifier.equals("invoice")){
                        invoice_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                        FragmentEditReportUpdate.InvoiceCreateDate=year + "-" + monthOfYear + "-" + dayOfMonth;
                    }
                    else{
                        FragmentEditReportUpdate.InvoiceDueDate=year + "-" + monthOfYear + "-" + dayOfMonth;
                        /* due_date.setText(dayOfMonth + "/" + monthOfYear+ "/" + year);*/
                        due_date.setText(year + "-" + monthOfYear+ "-" + dayOfMonth);
                    }
                }

            }};
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }
}

