package test.invoicegenerator.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ReportAdapter;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.model.InvoiceModel;

public class FragmentReport extends BaseFragment{

    ListView report_list;


    @BindView(R.id.add_invoice)
    FloatingActionButton add_invoice;

    DBHelper db;

    private ArrayList<InvoiceModel> list=new ArrayList<>();
    private ArrayList<String> items;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        report_list = (ListView) view.findViewById(R.id.invoice_list);

        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        db=new DBHelper(getActivity());
        items=new ArrayList<>();
        getInvoiceList();
        ReportAdapter adapter = new ReportAdapter(getActivity(), list);
        report_list.setAdapter(adapter);

        add_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("new", "true");
                args.putString("clicked", "false");
                loadFragment(new FragmentEditReport(),args);
            }
        });
        report_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putString("new", "false");
                args.putString("clicked", "true");
                args.putSerializable("invoice",list.get(position));
                loadFragment(new FragmentReportDetail(),args);
            }
        });
    }

    private void getInvoiceList() {
        {
            Cursor rs = db.getInvoiceData();
            rs.moveToFirst();

            items.clear();
            list.clear();
            while (!rs.isAfterLast()) {
                InvoiceModel value_item=new InvoiceModel();
                String id = rs.getString(rs.getColumnIndex("id"));
                String client_key=rs.getString(rs.getColumnIndex("client_key"));
                String subtotal = rs.getString(rs.getColumnIndex("subtotal"));
                String discount = rs.getString(rs.getColumnIndex("discount"));
                String dis_type = rs.getString(rs.getColumnIndex("dis_type"));
                String tax_rate = rs.getString(rs.getColumnIndex("tax_rate"));
                String tax_type = rs.getString(rs.getColumnIndex("tax_type"));
                String total_value = rs.getString(rs.getColumnIndex("total_value"));
                String comment = rs.getString(rs.getColumnIndex("comment"));
                String invoice_name = rs.getString(rs.getColumnIndex("invoice_name"));
                String due_date = rs.getString(rs.getColumnIndex("due_date"));
                String invoice_date = rs.getString(rs.getColumnIndex("invoice_date"));
                String attachment = rs.getString(rs.getColumnIndex("photo"));
                String signatue = rs.getString(rs.getColumnIndex("signature"));
                String signature_date = rs.getString(rs.getColumnIndex("signature_date"));

                value_item.setId(id);
                value_item.setClient_key(client_key);
                // value_item.setItem_key(item_key);
                value_item.setSubtotal(subtotal);
                value_item.setDiscount(discount);
                value_item.setDis_type(dis_type);
                value_item.setTax_rate(tax_rate);
                value_item.setTax_type(tax_type);
                value_item.setTotal_value(total_value);
                value_item.setComment(comment);
                value_item.setInvoice_name(invoice_name);
                value_item.setDue_date(due_date);
                value_item.setPhoto(attachment);
                value_item.setSignture_date(signature_date);
                value_item.setSignature(signatue);
                value_item.setInvoice_date(invoice_date);
                list.add(value_item);
                rs.moveToNext();
            }


        }
        for(int i=0;i<list.size();i++)
        {
            items.add(list.get(i).getInvoice_name()+"");
        }
    }

    private void loadFragment(Fragment dashboardFragment, Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(/*dashboardFragment.toString()*/null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
