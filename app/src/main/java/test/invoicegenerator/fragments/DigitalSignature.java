package test.invoicegenerator.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import test.invoicegenerator.R;
import test.invoicegenerator.general.DrawingView;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.SharedPref;

public class DigitalSignature extends BaseFragment {

    ConstraintLayout constraintLayout;
   /* private VolleyService mVolleyService;
    IResult mResultCallback = null;*/

    public static DigitalSignature newInstance() {
        DigitalSignature fragment = new DigitalSignature();
        return fragment;
    }

    private DrawingView mDrawingView = null;
    private ImageView edit_btn, clear_btn;
    Button save_btn,erase_all;
    String StrSignautirePath="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_digital_sign, container, false);

        initializeObject(rootView);
        constraintLayout=rootView.findViewById(R.id.layout_sign);
        eventListeners();

        return rootView;

    }


    private void initializeObject(View view) {
        SharedPref.init(getActivity());
        edit_btn =  view.findViewById(R.id.edit_btn);
        save_btn =  view.findViewById(R.id.save_btn);
        clear_btn = view.findViewById(R.id.clear_btn);
        mDrawingView =  view.findViewById(R.id.drawing);
        erase_all = view.findViewById(R.id.erase_all);

        mDrawingView.setColor("#000000");
    }
    private void eventListeners() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  imgPen.setImageResource(R.drawable.pencil);
                //  imgErase.setImageResource(R.drawable.eraser);
                mDrawingView.setErase(false);
                mDrawingView.setBrushSize(5);
                mDrawingView.setLastBrushSize(5);
                mDrawingView.setColor("#000000");
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setDrawingCacheEnabled(true);
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name + ".png";
                Bitmap b = mDrawingView.getDrawingCache();
                createDirectoryAndSaveFile(b,pic_name+".png");

               /* String imagePath = store_image(mDrawingView.getDrawingCache(), DIRECTORY, pic_name+".png");
                Intent intent = new Intent();
                intent.putExtra("drawPath", imagePath);
                getActivity().setResult(13, intent);
                getActivity().finish();*/

                GlobalData.StrUserSignature=ChangeFileToBase64(StrSignautirePath);

                if(GlobalData.StrUserSignature.isEmpty()){
                    showErrorMessage(constraintLayout,"Select Your Signature");
                }else {

                   /* DataSendToServerForAddCompany();*/
                   // Toast.makeText(getActivity(), GlobalData.StrUserSignature, Toast.LENGTH_SHORT).show();
                    showErrorMessage(constraintLayout,"Switch Tab To Fill Other Informations,Thanks");
                }



            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // imgPen.setImageResource(R.drawable.pencil);
                //   imgErase.setImageResource(R.drawable.eraser);
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(15);
            }
        });

        erase_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
               mDrawingView.ClearAll();
            }
        });
    }
    public String store_image(Bitmap _bitmapScaled, String dirPath, String fileName) {
        //you can create a new file name "test.jpg" in sdcard folder.
        File f = new File(dirPath, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            _bitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.d("mypath****************", dirPath + File.separator + fileName);
            return dirPath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        File fileToBeReturn = null;
        File direct = new File(Environment.getExternalStorageDirectory() + "/UserSignatues");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/UserSignatues/");
            wallpaperDirectory.mkdirs();
        }
        StrSignautirePath="/sdcard/UserSignatues/"+fileName;

        File file = new File(new File("/sdcard/UserSignatues/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);


            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            FragmentEditReport.Signaturefile=file;

            //   Toast.makeText(this, "Signature Saved Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  finish();
    }
    public String ChangeFileToBase64(String filepath){
        String base64Image;
        File imgFile = new File(filepath);
        if (imgFile.exists() && imgFile.length() > 0) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
        }else {
            base64Image="";
        }
        return base64Image;
    }



  /*  void DataSendToServerForAddCompany()
    {
        showProgressBar();


        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject data=new JSONObject();
        JSONObject FinalObj=new JSONObject();

        try {
            data.put("email",CompanyFrame.StrCompanyEmail);

            data.put("address",CompanyFrame.StrCompanyAddress);
            data.put("city",CompanyFrame.StrCompanyCity);
            data.put("state",CompanyFrame.StrCompanyState);
            data.put("zip_code",CompanyFrame.StrCompanyZipCode);
            data.put("country_id",CompanyFrame.StrCountryID);
            data.put("user_id",SharedPref.read(SharedPref.LoginID,"0"));
            data.put("phone",CompanyFrame.StrCompanyPhone);
           data.put("logo","data:image/png;base64,"+CompanyFrame.StrCompanyLogo);
            // data.put("company[cover_image]",zip_code.getText().toString());
            data.put("stamp","data:image/png;base64,"+CompanyFrame.StrCompanyStamp);
           *//* data.put("signature",CompanyFrame.StrCompanySignature);*//*
            // data.put("company[image]",zip_code.getText().toString());


            FinalObj.put("company",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        String Str=NetworkURLs.BaseURL + NetworkURLs.UpdateCompanuDetail;
        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL",Str ,FinalObj );


    }

    void initVolleyCallbackForUpdate(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String status = data.getString("status");


                    if(status.equals("true"))
                    {
                        loadFragment(new CompanyDetails());

                    }else {


                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                error.printStackTrace();

                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        String status=response_obj.getString("status");
                        if(status.equals("false"))
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            Toasty.error(getActivity(),message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //  return error;
                // snackbar = Snackbar.make(main_layout, error.toString(), Snackbar.LENGTH_LONG);
                //snackbar.show();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };
    }
    private void loadFragment(Fragment dashboardFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(dashboardFragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }*/
}