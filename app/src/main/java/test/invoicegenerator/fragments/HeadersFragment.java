package test.invoicegenerator.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.HeaderAdapter;
import test.invoicegenerator.adapters.HeaderAdapterJSON;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.helper.ProjectUtils;
import test.invoicegenerator.model.HeaderDetail;
import test.invoicegenerator.model.HeaderDetailSimple;
import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;
import static test.invoicegenerator.general.Constants.PIC_CROP;

public class HeadersFragment extends BaseFragment {

    Intent CropIntent ;
    DisplayMetrics displayMetrics ;
    int width, height;
    ImageView ImgChose;
    @BindView(R.id.save)
    Button save;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    @BindView(R.id.first_header)
    RadioButton first_header;
    String encodedImageData;
    @BindView(R.id.radio_visual)
    RadioGroup radioGroup;
    @BindView(R.id.list_view)
    ListView list_view;
    private Uri FilePathUri;
    Bitmap selectedBitmap;
    private boolean state=true;
    private Bitmap logo_bitmap;
    private boolean logo_flag;
    private int index;
    // private FirebaseFirestore db;
    ArrayList<String> list=new ArrayList<>();
    HeaderDetailSimple header;
    HeaderAdapter adapter;
    LinearLayout LayoutParent;
    ArrayList<HeaderDetail> json_list=new ArrayList<>();
    HeaderAdapterJSON headerAdapterJSON;
    int SelectedHeaderID;
    public  static final int RequestPermissionCode  = 1 ;
    /* ImageView imageViewA;
   ImageView imageViewB;
   ImageView imageViewC;
   int selectedImgID=R.drawable.img_ab;*/

    public static HeadersFragment newInstance() {
        HeadersFragment fragment = new HeadersFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.header_fragment,
                container, false);
        ButterKnife.bind(this,view);
        ImgChose=view.findViewById(R.id.camera);
        LayoutParent=view.findViewById(R.id.layout_parent);
        init();
        return view;
    }


    private void init() {
        EnableRuntimePermission();
      /*  name, email, phone_no,company_name*/
        GetHeaderList();
        /*list.add("");list.add("");list.add("");list.add("");
       header=new HeaderDetailSimple(GlobalData.StrCompanyName,GlobalData.StrCompanyEmail,GlobalData.StrCompanyPhone,GlobalData.StrCompanyName);
        HeaderAdapter adapter=new HeaderAdapter(getActivity(),list,header,index);
        list_view.setAdapter(adapter);
        list_view.setSelection(1);*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              /*  if(GlobalData.StrCompanyLogo.isEmpty()){
                    showErrorMessage(LayoutParent,"Select Header Of Company");
                }else {
                    showErrorMessage(LayoutParent,"Switch Tab To Fill Other Informations,Thanks");
                }
                showErrorMessage(LayoutParent,"Not Implement Yet");


                int selectedId = radioGroup.getCheckedRadioButtonId();


                String selected_index=list_view.getSelectedItemPosition()+"";

                if(logo_bitmap!=null)

                    encodeBitmapAndSaveToFirebase(logo_bitmap,getActivity(),LOGO_COLLECTION,LOGO,logo_flag);*/


                ImgChose.buildDrawingCache();
                Bitmap bmap = ImgChose.getDrawingCache();
                encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
                GlobalData.StrHeaderLogo=encodedImageData;



                    if(SelectedHeaderID!=0){
                        UpdateHeaderTemplete();
                    }else {
                        showErrorMessage(LayoutParent,"Chose Header Templete");
                    }




            }
        });


       /* ArrayList<String> headers=new ArrayList<>();
        headers.add("header 1");headers.add("header 2");headers.add("header 3");headers.add("header 4");*/




        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showErrorMessage(LayoutParent,NetworkURLs.BaseURLForImages+json_list.get(i).getStrCompanyLogo());
                SelectedHeaderID=json_list.get(i).getObjID();
              //  showImageStamp(json_list.get(i).getStrCompanyLogo(),ImgChose);
             //  Toast.makeText(getActivity(), SelectedHeaderID+" ", Toast.LENGTH_SHORT).show();

            }
        });

        ImgChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLogoPic();
            }
        });
    }


    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, final Context context, String col_name, String entity_name, boolean state) {
///(getString(R.string.saving_visual_detail));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] byte_arr = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(entity_name, imageEncoded);
        // deleteSignature();

    }

    private void selectLogoPic()
    {


        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }

    private String selectedHeaderId(int id)
    {
        int index=0;
        switch(id)
        {
            case R.id.first_header:
                index=1;
                break;
            case R.id.second_header:
                index=2;
                break;
            case R.id.third_header:
                index=3;
                break;
            case R.id.fourth_header:
                index=4;
                break;
        }
        return String.valueOf(index);
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


               /*ImgChose.setImageBitmap(logo_bitmap);
                ImgChose.buildDrawingCache();*/
                ImageCropFunction();
                //performCrop(FilePathUri);



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

                ImgChose.setImageBitmap(selectedBitmap);
                ImgChose.buildDrawingCache();
            }


        }else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                ImgChose.setImageBitmap(bitmap);

            }
        }
        //  imageButton.setVisibility(View.GONE);
       // add_stamp_text.setVisibility(View.GONE);

        /*Uri FilePathUri;
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                //   camera.setImageBitmap(logo_bitmap);
                performCrop(FilePathUri);
                // After selecting image change choose button above text.
                // ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (requestCode == Stamp_Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            FilePathUri = data.getData();
        }
        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                logo_bitmap = extras.getParcelable("data");
                //Toast.makeText(getActivity(), logo_bitmap.getWidth()+" "+logo_bitmap.getHeight(), Toast.LENGTH_SHORT).show();
                if(selectedBitmap.getHeight()>170 || selectedBitmap.getHeight()<130 || selectedBitmap.getWidth()>170 ||
                        selectedBitmap.getWidth()<130)
                {
                    Toasty.error(getActivity(), getString(R.string.logo_size_error), Toast.LENGTH_SHORT, true).show();
                   getActivity().finish();// return;
                }
                else
                {
                //    camera.setImageBitmap(logo_bitmap);

                }
            }
        }*/
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            // cropIntent.putExtra("aspectX", 2);
            //  cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
              cropIntent.putExtra("outputX", 105);
              cropIntent.putExtra("outputY", 105);


            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("scaleUpIfNeeded", true);
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




    public void GetHeaderList()
    {
        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL+ NetworkURLs.GetCompanyHeaders);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray Headertemplates = data.getJSONArray("header_templates");

                        for (int i = 0; i < Headertemplates.length(); i++) {
                            HeaderDetail headerDetail = new HeaderDetail(Headertemplates.getJSONObject(i));
                            json_list.add(headerDetail);
                            //clientModel.setSelect(false);
                        }
                       /* countryAdapter = new SelectionCountryAdapter(getActivity(),CountryModels);
                        listView.setAdapter(countryAdapter);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(json_list.size()>0){
                    headerAdapterJSON=new HeaderAdapterJSON(getActivity(),json_list);
                    list_view.setAdapter(headerAdapterJSON);
                   // Toast.makeText(getActivity(), "Header Templete list size:"+json_list.size(), Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
    ////////////////Update Logo////////////


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
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                     /*   Intent Send=new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(Send);
                        getActivity().finish();*/
                        /*loadFragment(new CompanyDetails());*/

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
                    //  VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        String status=response_obj.getString("status");
                        if(status.equals("false"))
                        {
                            //JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=response_obj.getString("error");
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



    void UpdateHeaderTemplete()
    {
        showProgressBar();


        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        JSONObject data=new JSONObject();
        JSONObject data_active=new JSONObject();
        JSONObject FinalObj=new JSONObject();

        try {

            if(!ProjectUtils.isEmptyString(GlobalData.StrHeaderLogo)){
                data.put("logo","data:image/png;base64,"+GlobalData.StrHeaderLogo);

            }

            data.put("active",true);
            FinalObj.put("header_template",data);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String Str= NetworkURLs.BaseURL + NetworkURLs.UpdateCompanyHeader+SelectedHeaderID+".json";
        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL",Str ,FinalObj );


    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public void showImageStamp(String ImgPath,ImageView img){

        String Str=NetworkURLs.BaseURLForImages.trim()+ImgPath.trim();
        Picasso.get()
                .load(Str)
                .placeholder(R.color.grey) // Your dummy image...
                .into(img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.create().show();

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }

    /////////////////////

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(FilePathUri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA))
        {

            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
