package test.invoicegenerator.fragment_handler;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class Fragment_Model {
    private String TagName;
    private Fragment fragment;
    private Bundle bundle;
    private String Title;

    public Fragment_Model(String tagName, Fragment fragment, Bundle bundle, String title)
    {
        TagName = tagName;
        this.fragment = fragment;
        this.bundle = bundle;
        Title = title;
    }

    public String getTagName()
    {
        return TagName;
    }

    public void setTagName(String tagName)
    {
        TagName = tagName;
    }

    public Fragment getFragment()
    {
        return fragment;
    }

    public void setFragment(Fragment fragment)
    {
        this.fragment = fragment;
    }

    public Bundle getBundle()
    {
        return bundle;
    }

    public void setBundle(Bundle bundle)
    {
        this.bundle = bundle;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        Title = title;
    }
}
