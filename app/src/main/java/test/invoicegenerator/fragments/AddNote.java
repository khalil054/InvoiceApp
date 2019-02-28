package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.invoicegenerator.R;

public class AddNote extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_add_note, container, false);

        return rootView;

    }

}
