package test.invoicegenerator.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.R;


public class BaseFragment extends Fragment {
    public Unbinder unbinder;
    Progressbar progressbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_fragment, null);

        return null;
    }

    ProgressDialog progressDialog;

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //show progress dialog within the app
    public void showProgressBar() {
        progressbar = new Progressbar(getActivity());
        progressbar.ShowProgress();
    }

    public void hideProgressBar() {
        progressbar.HideProgress();
    }


    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar
                .LENGTH_LONG);
        snackbar.show();


    }

    public void loadFragment(Fragment dashboardFragment, Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(/*dashboardFragment.toString()*/null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


}
