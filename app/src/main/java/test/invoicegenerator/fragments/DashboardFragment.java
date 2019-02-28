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
import test.invoicegenerator.view.activities.MainActivity;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.PICK_IMAGE_REQUEST;


public class DashboardFragment extends BaseFragment  {

    private DBHelper db;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    LinearLayout layoutClient,layoutReports,layoutAddInvoice,layoutInvoiceReport,layoutConfiguration,layoutSettings;
    DBHelper sqliteHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);
        return view;
    }

    private void init(View v)
    {
        sqliteHelper=new DBHelper(getActivity());
        db=new DBHelper(getActivity());
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


            }
        });

        layoutReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).ChangeMenuOption(2);
                Cursor rs=db.getInvoiceData();
                if(rs.isAfterLast() == false)
                    loadFragment(new FragmentReport(),null);
                else
                {
                    Bundle args = new Bundle();
                    args.putString("new", "true");
                    args.putString("clicked", "false");
                    loadFragment(new FragmentEditReport(),args);
                }
            }
        });

        layoutAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        layoutInvoiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Util.uploadProfilePic(storage,storageReference,getActivity(),filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadFragment(Fragment dashboardFragment,Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(/*dashboardFragment.toString()*/null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }




}

