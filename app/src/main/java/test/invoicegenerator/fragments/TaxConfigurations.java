package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;

public class TaxConfigurations extends Fragment {


    TabLayout tabLayout;

    ConstraintLayout main_layout;


    int currentIndeX;
    private ViewPager mViewPager;
    private TaxConfigurations.SectionsPagerAdapter mSectionsPagerAdapter;

    ArrayList<String> tab_name = new ArrayList<>();
    ArrayList<Integer> tab_icon = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_companyframe, container, false);


        main_layout = rootView.findViewById(R.id.main_layout);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.container);


        tabLayout = rootView.findViewById(R.id.tabs);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(mViewPager);

        tab_icon.add(R.drawable.ic_companytax);
        tab_icon.add(R.drawable.ic_taxcode);
        tab_icon.add(R.drawable.ic_viewtax);

        tab_name.add("Company Taxes");
        tab_name.add("Tax Code");
        tab_name.add("Calculate Tax");


        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_item, null);

            TextView tab_label = linearLayout.findViewById(R.id.label);
            ImageView tab_pic = linearLayout.findViewById(R.id.img);


            tab_pic.setImageResource(tab_icon.get(i));
            tab_label.setText(tab_name.get(i));

            tabLayout.getTabAt(i).setCustomView(linearLayout);
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentIndeX = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = TaxesList.newInstance();
                    break;

                case 1:
                    fragment = TaxCodeList.newInstance();
                    break;

                case 2:
                    fragment = ViewTax.newInstance();
                    break;

                default:
                    break;
            }

            return fragment;

        }


        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


}

