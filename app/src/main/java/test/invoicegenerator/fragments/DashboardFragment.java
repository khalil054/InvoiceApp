package test.invoicegenerator.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.Activities.MainActivity;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.PICK_IMAGE_REQUEST;


public class DashboardFragment extends BaseFragment  {


    Uri filePath;

    StorageReference storageReference;
    LinearLayout layoutClient,layoutReports,layoutAddInvoice,layoutInvoiceReport,layoutConfiguration,layoutSettings;

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

                //loadFragment(new ClientSelection(),null);


            }
        });

        layoutReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // loadFragment(new FragmentAllClients(),null);
                ((MainActivity)getActivity()).ChangeMenuOption(2);

                    Bundle args = new Bundle();
                    args.putString("new", "true");
                    args.putString("clicked", "false");
                    loadFragment(new FragmentEditReport(),args);

            }
        });

        layoutAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

