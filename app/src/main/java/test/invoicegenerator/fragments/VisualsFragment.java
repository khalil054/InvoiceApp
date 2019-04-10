package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Util;


public class VisualsFragment extends Fragment implements View.OnClickListener{
    private Unbinder unbinder;

    @BindView(R.id.header)
    Button header;

    @BindView(R.id.sign)
    Button signature;

    @BindView(R.id.logo)
    Button logo;

    @BindView(R.id.header_selector)
    View header_selector;

    @BindView(R.id.sign_selector)
    View sign_selector;

    @BindView(R.id.logo_selector)
    View logo_selector;

    @BindView(R.id.visual_tab_layout)
    LinearLayout visual_tab_layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visual_fragment,container,
                false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }
private void init()
{
    header.setBackgroundColor(getResources().getColor(R.color.light_blue));
    signature.setBackgroundColor(getResources().getColor(R.color.light_blue));
    logo.setBackgroundColor(getResources().getColor(R.color.light_blue));

    loadHeaderFragment();
    header.setOnClickListener(this);
    signature.setOnClickListener(this);
    logo.setOnClickListener(this);

    setVisualTabMargin();
}
private void setVisualTabMargin()
{
    if(Util.gettingDeviceSize(getActivity()).widthPixels > 800&& Util.gettingDeviceSize(getActivity()).heightPixels >1000)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 15, 0, 0);
        visual_tab_layout.setLayoutParams(params);

    }
}
private void loadHeaderFragment()
{
    loadFragment(new HeadersFragment());
    header_selector.setVisibility(View.VISIBLE);
    sign_selector.setVisibility(View.INVISIBLE);
    logo_selector.setVisibility(View.INVISIBLE);
}
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.header:
                loadHeaderFragment();
                break;
            case R.id.logo:

                break;
            case R.id.sign:

                break;
        }
    }
    private void loadFragment(Fragment frag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_nested, frag);
        fragmentTransaction.commit();//
    }
}
