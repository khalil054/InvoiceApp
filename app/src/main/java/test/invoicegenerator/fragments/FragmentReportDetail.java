package test.invoicegenerator.fragments;

/*
 * Created by User on 1/28/2019.
 */

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Toast;

import java.util.ArrayList;

import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.general.PDFInvoice;

public class FragmentReportDetail extends BaseFragment {


    IResult mResultCallback = null;
    VolleyService mVolleyService;
    TabLayout tabLayout;
    ConstraintLayout main_layout;
    int currentIndeX;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static String InvoiceId_ToBeFetch;
    ArrayList<String> tab_name = new ArrayList<>();
   /* private String is_new;
    private String is_clicked;
    private InvoiceModel invoice;
    private String invoice_id;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_companyframe,container, false);

        main_layout =  rootView.findViewById(R.id.main_layout);

        init(rootView);

        return rootView;
    }

    private void init(View rootView ) {

           BottomNavigationView navigation =  getActivity().findViewById(R.id.navigation);
           navigation.setVisibility(View.GONE);
           assig_data_to_viewpager(rootView);

    }

    void assig_data_to_viewpager(View view){

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(),null);
        // Set up the ViewPager with the sections adapter.
        mViewPager =  view.findViewById(R.id.container);

        mViewPager.setOffscreenPageLimit(0);

        tabLayout = view.findViewById(R.id.tabs);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(mViewPager);

        tab_name.add("Edit");

        tab_name.add("Preview");

        /* mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(), "Selcted Fragment Position is:"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_item, null);

            TextView tab_label = linearLayout.findViewById(R.id.label);

            ImageView tab_pic =  linearLayout.findViewById(R.id.img);
            // tab_pic.setImageResource(tab_icon.get(i));
            tab_label.setText(tab_name.get(i));

            tabLayout.getTabAt(i).setCustomView(linearLayout);
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                currentIndeX = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public  class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private Bundle fragmentBundle;

        public SectionsPagerAdapter(FragmentManager fm, Bundle data) {
            super(fm);
            fragmentBundle = data;
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment=new FragmentEditReportUpdate();
                    fragment.setArguments(fragmentBundle);
                    break;
                case 1:
                   fragment=new PDFInvoice();
                    //fragment.setArguments(fragmentBundle);
                    break;
                default:

                    break;
            }
            return fragment;

        }


        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}

