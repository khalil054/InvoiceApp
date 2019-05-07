package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;

public class Configrations extends BaseFragment {


    @BindView(R.id.layout_company)
    LinearLayout layout_company;

    @BindView(R.id.layot_currency)
    LinearLayout layot_currency;

    @BindView(R.id.layot_schedule)
    LinearLayout layot_schedule;

    @BindView(R.id.layot_tax)
    LinearLayout layot_tax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configrations, container, false);
        ButterKnife.bind(this, view);

        layout_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // loadFragment(new CompanyFrame());
            }
        });

        layot_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new CurrencyPickerFragment());
            }
        });

        layot_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddNote());
            }
        });

        layot_tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new TaxConfigurations());
            }
        });

        return view;
    }

    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


}
