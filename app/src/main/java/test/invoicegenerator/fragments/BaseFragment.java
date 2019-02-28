package test.invoicegenerator.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import butterknife.Unbinder;
import test.invoicegenerator.R;

/**
 * Created by User on 10/22/2018.
 */

public class BaseFragment extends Fragment {
    public Unbinder unbinder;

    public FirebaseFirestore db;

    public Dialog adminChangeDialog;
    DatabaseReference databaseReference;
    public StorageReference storageReference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_fragment,null);
        db = FirebaseFirestore.getInstance();
       // storageReference = FirebaseStorage.getInstance().getReference();
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
    public ProgressDialog showProgressDialog(String message) {
        dismissProgress();
        progressDialog = new ProgressDialog(getActivity(),0);//Utils.getProgressDialog(this, message);
        progressDialog.setMessage(message);
        progressDialog.show();
        progressDialog.setCancelable(false);
        return progressDialog;
    }


    /**
     * Uses snackbar to show error message.
     */
    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar
                .LENGTH_LONG);
        snackbar.show();


    }

}
