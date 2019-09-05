package test.invoicegenerator.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;
import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;
import static test.invoicegenerator.general.Constants.PIC_CROP;

public class AddStamp extends BaseFragment {
    ConstraintLayout constraintLayout;
    @BindView(R.id.save_btn)
    Button save_btn;
    String encodedImageData;
    @BindView(R.id.SelectStamp)
    Button imageButton;

    @BindView(R.id.stamp_image)
    ImageView stamp_image;

    @BindView(R.id.add_stamp_text)
    TextView add_stamp_text;

    private Uri FilePathUri;
    private Bitmap logo_bitmap;
/*    private VolleyService mVolleyService;
    IResult mResultCallback = null;*/

    public static AddStamp newInstance() {
        AddStamp fragment = new AddStamp();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_add_stamp, container, false);
        ButterKnife.bind(this,rootView);
        constraintLayout=rootView.findViewById(R.id.layout_header);
        init();
        return rootView;

    }
    private void init() {

        showImageStamp(GlobalData.StrCompanyStampUrl);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectLogoPic();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                stamp_image.buildDrawingCache();
                Bitmap bmap = stamp_image.getDrawingCache();
                encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
                GlobalData.StrCompanyStamp=encodedImageData;



                if(GlobalData.StrCompanyStamp.isEmpty()){
                    showErrorMessage(constraintLayout,"Select Your Stamp Logo");
                }else {
                   /* Toast.makeText(getActivity(), CompanyFrame.StrCompanyStamp, Toast.LENGTH_SHORT).show();*/
                    showErrorMessage(constraintLayout,"Switch Tab To Fill Other Informations,Thanks");

                }



            }
        });
    }
    private void selectLogoPic()
    {

        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();


            try {

                // Getting selected image into Bitmap.
                logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                stamp_image.setImageBitmap(logo_bitmap);
                stamp_image.buildDrawingCache();

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                stamp_image.setImageBitmap(selectedBitmap);
            }
        }
      //  imageButton.setVisibility(View.GONE);
        add_stamp_text.setVisibility(View.GONE);
    }
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
    ////////////////////////////upload data to server


  /*  void initVolleyCallbackForUpdate(){
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
    }


    void DataSendToServerForAddCompany()
    {
        showProgressBar();


        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject data=new JSONObject();
        JSONObject FinalObj=new JSONObject();

        try {
            data.put("email",GlobalData.StrCompanyEmail);

            data.put("address", GlobalData.StrCompanyAddress);
            data.put("city",GlobalData.StrCompanyCity);
            data.put("state",GlobalData.StrCompanyState);
            data.put("zip_code",GlobalData.StrCompanyZipCode);
            data.put("country_id",GlobalData.StrCountryID);
            data.put("user_id", SharedPref.read(SharedPref.LoginID,"0"));
            data.put("phone",GlobalData.StrCompanyPhone);
            data.put("logo","data:image/png;base64,"+GlobalData.StrCompanyLogo);
            // data.put("company[cover_image]",zip_code.getText().toString());
            data.put("stamp","data:image/png;base64,"+GlobalData.StrCompanyStamp);
            *//* data.put("signature",CompanyFrame.StrCompanySignature);*//*
            // data.put("company[image]",zip_code.getText().toString());


            FinalObj.put("company",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        String Str= NetworkURLs.BaseURL + NetworkURLs.UpdateCompanuDetail;
        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL",Str ,FinalObj );


    }
*/

    public void showImageStamp(String ImgPath){
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages+ImgPath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(stamp_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }
}
