package test.invoicegenerator.general;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 9/15/2018.
 */

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("InvoiceGenerator", Context.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }
}
