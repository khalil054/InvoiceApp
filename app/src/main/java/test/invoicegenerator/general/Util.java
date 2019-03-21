package test.invoicegenerator.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import test.invoicegenerator.model.Country;
import test.invoicegenerator.model.StateModel;

import static com.itextpdf.text.xml.xmp.LangAlt.DEFAULT;
import static test.invoicegenerator.general.Constants.AUTH_ID;
import static test.invoicegenerator.general.Constants.EMAIL_KEY;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

/**
 * Created by User on 9/12/2018.
 */

public class Util {
    public static boolean isFullname(String str) {
        String expression = "[a-zA-Z ]*";//^[a-zA-Z\\s]+
        return str.matches(expression);
    }
   /* public static boolean isZipCodeValid(String s)
    {
        Pattern digitPattern = Pattern.compile("\\d");
         return digitPattern.matcher(s).matches();
    }*/
    public static boolean isZipCodeValid(String s)
    {
        String regex = "^\\d+$";
        //  Pattern digitPattern = Pattern.compile("[0-9]");
        return !regex.matches(regex);
    }
    public static void hideKeyPad(Activity context,View view) {
        View v = context.getCurrentFocus();
        if (v != null)
        {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void showToastAlpha(String message,Context context) {
        Toast toast = Toast.makeText(context, "" + message, Toast.LENGTH_SHORT);
        //  toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
        toast.show();
        //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
    }
    public static final void showAlertBox(String Message, String title,
                                          final Context context, final boolean finishOnOk) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (finishOnOk) {
                            ((Activity) context).finish();
                        }

                    }
                }).show();


    }
    public static void updateResources(Context context, String language) {
       /* Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());*/

        /*Resources res = context.getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);*/

       // String languageToLoad  = "fr"; // your language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
    public static void resetPassword(Context context)
    {
        SharedPreferenceHelper helper=new SharedPreferenceHelper(context);
        FirebaseAuth.getInstance().sendPasswordResetEmail(helper.getValue(EMAIL_KEY))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "Email sent.");
                        }
                    }
                });
    }
    public static void uploadProfilePic(FirebaseStorage storage,
                                        StorageReference storageReference, final Context context, Uri filePath)
    {
        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                       // progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }
    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    public static DisplayMetrics gettingDeviceSize(Activity context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return displayMetrics;
    }
    public static String auth_idOfLoggedInUser(Context context)
    {
        SharedPreferenceHelper helper=new SharedPreferenceHelper(context);
        return helper.getValue(AUTH_ID);
    }
public static ArrayList<String> getStateList(ArrayList<StateModel> list)
{
    ArrayList<String> states=new ArrayList<>();
    for(int i=0;i<list.size();i++)
    {
        states.add(list.get(i).getName());
    }
    return states;
}
    public static ArrayList<String> getCountryList(ArrayList<Country> list)
    {
        ArrayList<String> states=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            states.add(list.get(i).getName());
        }
        return states;
    }
    public static String getCountryId(String country_name, ArrayList<Country> list)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().equals(country_name))
                return list.get(i).getId();
        }
        return "";
    }
    public static String getSelectedCountryIndex(String country_name, ArrayList<Country> list)
    {
        try{
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().equals(country_name))
                return i+"";
        }
    } catch (NullPointerException e) {
        System.out.print("Caught the NullPointerException");
    }
        return "";
    }
    public static String getSelectedStateIndex(String country_name,String country_id, ArrayList<StateModel> list)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().equals(country_name) && list.get(i).getCountry_id().equals(country_id))
                return i+"";
        }
        return "";
    }
    public static List<String> retrieveAllItems(Spinner theSpinner) {
        Adapter adapter = theSpinner.getAdapter();
        int n = adapter.getCount();
        List<String> users = new ArrayList<String>(n);
        for (int i = 0; i < n; i++) {
            String user = (String) adapter.getItem(i);
            users.add(user);
        }
        return users;
    }
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
    public static String getMessage(VolleyError error){
        if (error instanceof TimeoutError) {
            return "Time out error";
        } else if(error instanceof NoConnectionError) {
            return "Please check your connection";
        } else if (error instanceof AuthFailureError) {
            return "Authentication failed";
        } else if (error instanceof ServerError) {
            return "Server error has been occured";
        } else if (error instanceof NetworkError) {
            return "There is network issue ";
        } else if (error instanceof ParseError) {
            return "Parsing error";
        } else{
            return DEFAULT;
        }
    }
    public static boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public static void showDialog(final String msg, final Context context,
                                  final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public static boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialogWrite("External storage", context,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public static void showDialogWrite(final String msg, final Context context,
                                       final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public static String getTodayDate()
    {
       /* SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-yyyy",Locale.ENGLISH);
        Date date = new Date();
        return formatter.format(date);
    }
    public static int calculateTotalValue(int sub_total,int discount,int tax,String discount_type,String tax_type)
    {
        if(discount_type.equals("Flat Item") && tax==0) {
            if (discount != 0 && discount<sub_total)
            {
                return sub_total-discount;
            }
        }
        else if(discount_type.equals("percentage") && tax==0)
        {
            if (discount != 0)
            {
                return (int) ((sub_total - ((discount* sub_total)/100))+0.9);

            }
        }
        else if(discount_type.equals("Flat Item") && discount!=0 && tax_type.equals("Deducted"))
        {
            int sum=sub_total-discount;
            return (int) ((sum-tax)+0.9);
        }
        else if(discount_type.equals("percentage")&& discount!=0 && tax_type.equals("Deducted"))
        {
            int sum=sub_total - ((discount* sub_total)/100);;
            return (int) ((sum-tax)+0.9);
        }
        else if(discount==0 && tax==0)
        {
            return (int) ((sub_total)+0.9);
        }
        else if(discount==0 && tax_type.equals("Deducted"))
        {
            return (int) ((sub_total-tax)+0.9);
        }
        return (int) (0+0.9);
    }
}
