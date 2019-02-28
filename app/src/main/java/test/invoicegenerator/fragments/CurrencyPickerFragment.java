package test.invoicegenerator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import test.invoicegenerator.R;

public class CurrencyPickerFragment extends Fragment {



    LinearLayout currency_layout;
    TextView selected_currency_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_currency_picker, container, false);

        currency_layout=(LinearLayout) rootView.findViewById(R.id.currency_layout);
        selected_currency_txt = (TextView) rootView.findViewById(R.id.selected_currency_txt);

        currency_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        selected_currency_txt.setText(name +"    "+ code +" ("+ symbol+")");
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });

        return rootView;

    }

}
