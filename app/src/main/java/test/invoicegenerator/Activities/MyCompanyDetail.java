package test.invoicegenerator.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.AddStamp;
import test.invoicegenerator.fragments.CompanyDetailsUpdate;
import test.invoicegenerator.fragments.CountryFragment;
import test.invoicegenerator.fragments.DigitalSignature;
import test.invoicegenerator.fragments.HeadersFragment;
import test.invoicegenerator.fragments.PlaceLogo;
import test.invoicegenerator.fragments.UserProfilePic;

public class MyCompanyDetail extends AppCompatActivity {

  //  private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_detail);
/*
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        viewPager =  findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs_ss);
        tabLayout.setupWithViewPager(viewPager);
    /*    tabLayout.setTabTextColors(getResources().getColor(R.color.white_color),
                getResources().getColor(R.color.white_color));*/
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        setupTabIcons();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.enterprise,
                R.drawable.flag_white,
                R.drawable.podcast,
                R.drawable.podcast_cover,
                R.drawable.stamp,
                R.drawable.signature,
                R.drawable.user_pic
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
        tabLayout.getTabAt(6).setIcon(tabIcons[6]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CompanyDetailsUpdate(), "Company Details");
        adapter.addFrag(new CountryFragment(), "Country");
        adapter.addFrag(new PlaceLogo(), "Place Logo");
        adapter.addFrag(new HeadersFragment(), "Header Details");
        adapter.addFrag(new AddStamp(), "Stamp");
        adapter.addFrag(new DigitalSignature(), "Signature");
        adapter.addFrag(new UserProfilePic(), "Profile Pic");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }
}
