package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;

import test.invoicegenerator.Activities.DigitalSignatureActivity;
import test.invoicegenerator.R;
import test.invoicegenerator.Activities.MainActivity;
import test.invoicegenerator.general.GlobalData;


public class DashboardFragment extends BaseFragment  {


   /* Uri filePath;

    StorageReference storageReference;*/
    LinearLayout layoutClient,layoutReports,layoutAddInvoice,layoutInvoiceReport,layoutConfiguration,layoutSettings;
    public static boolean ShowInvoiceInfo=false;
    ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);
        return view;
    }

    private void init(View v)
    {

       /* imageView=v.findViewById(R.id.img_dummy);


        Picasso.get().load("http://9091abb2.ngrok.io/uploads/invoice/signature/76/signature.jpeg").placeholder(R.color.grey).into(imageView);

*/

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
                GlobalData.Text_Code_ID="";
                FragmentReport.CanUpdateInvoice=false;
                Bundle args = new Bundle();
                args.putString("new", "true");
                args.putString("clicked", "false");
                FragmentEditReport.StrImagePath="";
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

                FragmentReport.CanUpdateInvoice=false;
                loadFragment(new Configrations(),null);
               // loadFragment(new FragmentReportDetail(),null);

            }
        });

        layoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               loadFragment(new SettingsFragment(),null);

            }
        });


    }




}

