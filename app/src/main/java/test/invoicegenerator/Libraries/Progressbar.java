package test.invoicegenerator.Libraries;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;

import test.invoicegenerator.R;

public class Progressbar extends Dialog  {

    public Activity c;
    public Dialog d;

    LottieAnimationView progressView,confirmationView;


    public Progressbar(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progressbar);

        progressView = (LottieAnimationView) findViewById(R.id.progressView);
        confirmationView = (LottieAnimationView) findViewById(R.id.confirmationView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void ShowProgress()
    {
        show();
        progressView.setVisibility(View.VISIBLE);
    }

    public void HideProgress()
    {
        progressView.setVisibility(View.GONE);
        dismiss();
    }

    public void ShowConfirmation()
    {
        show();
        confirmationView.setVisibility(View.VISIBLE);
        confirmationView.playAnimation();
    }

    public void HideConfirmation()
    {
        confirmationView.cancelAnimation();
        confirmationView.setVisibility(View.GONE);
        dismiss();
    }


}