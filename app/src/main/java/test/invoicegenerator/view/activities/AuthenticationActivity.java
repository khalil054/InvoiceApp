package test.invoicegenerator.view.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.FragmentLogin;

/**
 * Created by User on 1/4/2019.
 */

public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.fragment_frame)
    LinearLayout fragment_frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ButterKnife.bind(this);//
       /* PDFInvoice pdf=new PDFInvoice(this);
        try {
            pdf.create_pdf_file();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //loadFragment(new FragmentReport());
         loadFragment(new FragmentLogin());
       // loadFragment(new PDFInvoice());
    }
    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
