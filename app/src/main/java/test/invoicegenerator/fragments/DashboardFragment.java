package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import test.invoicegenerator.R;
import test.invoicegenerator.Activities.MainActivity;


public class DashboardFragment extends BaseFragment  {


   /* Uri filePath;

    StorageReference storageReference;*/
    LinearLayout layoutClient,layoutReports,layoutAddInvoice,layoutInvoiceReport,layoutConfiguration,layoutSettings;
    public static boolean ShowInvoiceInfo=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);
        return view;
    }

    private void init(View v)
    {
        layoutClient=v.findViewById(R.id.layot_clients);
        layoutReports=v.findViewById(R.id.layot_reports);
        layoutAddInvoice=v.findViewById(R.id.layot_add_invoice);
        layoutInvoiceReport=v.findViewById(R.id.layot_invoice_report);
        layoutConfiguration=v.findViewById(R.id.layot_config);
        layoutSettings=v.findViewById(R.id.layot_settings);


        layoutClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadFragment(new FragmentAllClients(),null);
                //MainActivity.OpenFragment(new FragmentAllClients(),getResources().getString(R.string.tag_allclients),null,getResources().getString(R.string.tag_allclients));

            }
        });

        layoutReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowInvoiceInfo=true;
              loadFragment(new FragmentReport(),null);

               /* ((MainActivity)getActivity()).ChangeMenuOption(2);

                    Bundle args = new Bundle();
                    args.putString("new", "true");
                    args.putString("clicked", "false");
                    loadFragment(new FragmentEditReport(),args);
*/
            }
        });

        layoutAddInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentReport.CanUpdateInvoice=false;
                Bundle args = new Bundle();
                args.putString("new", "true");
                args.putString("clicked", "false");
                loadFragment(new FragmentEditReport(),args);
            }
        });
        layoutInvoiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).ChangeMenuOption(4);
                loadFragment(new WriteSMS(),null);
            }
        });
        layoutConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).ChangeMenuOption(5);
                loadFragment(new Configrations(),null);

            }
        });

        layoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }




}

