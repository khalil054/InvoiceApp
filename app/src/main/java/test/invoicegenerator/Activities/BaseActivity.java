package test.invoicegenerator.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Unbinder;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.R;


public class BaseActivity extends AppCompatActivity {
    //TODO move app level permission checks and users alerts to base class
    // and make sure permission checks happen in every on Resume and users gets notified if we
    // are missing some essential permission


    private Snackbar snackbar;
    Progressbar cdd;

    /**
     * Reference to {@link Unbinder}
     */
    public Unbinder unbinder;


    public Dialog adminChangeDialog;

    ProgressDialog progressDialog;

    //  @BindView(R.id.toolbar)
    Toolbar tool_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        tool_bar = findViewById(R.id.toolbar);
        cdd = new Progressbar(BaseActivity.this);
        cdd.setCancelable(false);
    }

    public void dismissProgress() {
        cdd.HideProgress();
    }

    //show progress dialog within the app
    public void showProgressDialog() {
        cdd.show();

    }


    /**
     * Uses snackbar to show error message.
     */
    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar
                .LENGTH_LONG);
        snackbar.show();


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
