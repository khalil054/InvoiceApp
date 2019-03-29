package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;

public class WriteSMS extends BaseFragment {


    @BindView(R.id.main_layout)
    ConstraintLayout main_layout;

    @BindView(R.id.msg_txt)
    EditText msg_txt;

    @BindView(R.id.next_btn)
    Button next_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_write_sms, container, false);
        unbinder= ButterKnife.bind(this,view);

        next_btn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(msg_txt.getText().toString()))
                {
                    msg_txt.setError(getString(R.string.error_field_required));
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("msg_txt", msg_txt.getText().toString());
                loadFragment(new SendSMSClient(),bundle);
            }
        });

        return view;
    }

}