package test.invoicegenerator.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.VisualsFragment;

public class CompanyDetailActivity extends Fragment implements View.OnClickListener{
    @BindView(R.id.address)
    Button address;

    @BindView(R.id.contact)
    Button contact;

    @BindView(R.id.signature)
    Button signature;

    @BindView(R.id.address_selector)
    View address_selector;

    @BindView(R.id.contact_selector)
    View contact_selector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_company_detail,container,
                false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init()
    {
        address.setOnClickListener(this);
        contact.setOnClickListener(this);
        signature.setOnClickListener(this);

        contact.setBackgroundColor(Color.WHITE);
        address.setBackgroundColor(Color.WHITE);



        address_selector.setVisibility(View.VISIBLE);
        contact_selector.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.address:


                address_selector.setVisibility(View.VISIBLE);
                contact_selector.setVisibility(View.INVISIBLE);

                break;
            case R.id.contact:
                loadFragment(new VisualsFragment());

                contact_selector.setVisibility(View.VISIBLE);
                address_selector.setVisibility(View.INVISIBLE);
                break;
            case R.id.signature:
//                DigitalSignatureFragment sign_frag=new DigitalSignatureFragment();
//                loadFragment(sign_frag);
                break;
        }
    }

    private void loadFragment(Fragment frag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.commit();//
    }
}
