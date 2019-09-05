package test.invoicegenerator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import test.invoicegenerator.R;

public class AppPrivacyPolicy extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_privacy_policy);

        imageView=findViewById(R.id.cancel_privacy);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Send=new Intent(AppPrivacyPolicy.this,MainActivity.class);
                startActivity(Send);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent Send=new Intent(AppPrivacyPolicy.this,MainActivity.class);
        startActivity(Send);
        finish();
    }
}
