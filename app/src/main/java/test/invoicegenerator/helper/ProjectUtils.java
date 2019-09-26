package test.invoicegenerator.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectUtils {

    public static boolean isEmptyString(String text) {

        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }


    public static boolean isValidEmailID(String email) {

        String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(PATTERN);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }
}
