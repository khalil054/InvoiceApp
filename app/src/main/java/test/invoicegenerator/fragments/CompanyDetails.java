package test.invoicegenerator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import test.invoicegenerator.Activities.MyCompanyDetail;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.general.GlobalData;

public class CompanyDetails extends BaseFragment {

    private VolleyService mVolleyService;

    IResult mResultCallback = null;

    TextView TvName, TvEmail, TvPhone, TvAddress, TvCity, TvPostalCode, TvSlug, TvCountry, TvState;

    String StrName, StrEmail, StrPhone, StrAddress, StrCity, StrPostalCode, StrSlug, StrCountry, StrState, StrLogoImgae, StrStampImage;
    String StruserProfile, StrUserSignature;
    Button BtnUpdate;

    ImageView imageViewLogo, imageViewStamp, imageViewUser;

    public static CompanyDetails newInstance() {
        CompanyDetails fragment = new CompanyDetails();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_company_details, container, false);
        init(rootView);
        return rootView;

    }

    private void init(View v) {

        imageViewLogo = v.findViewById(R.id.logo_image);
        imageViewStamp = v.findViewById(R.id.stamp_image);
        imageViewUser = v.findViewById(R.id.profile_image);
        TvName = v.findViewById(R.id.tv_company_name);
        TvEmail = v.findViewById(R.id.tv_company_email);
        TvPhone = v.findViewById(R.id.tv_company_phone);
        TvCity = v.findViewById(R.id.tv_company_city);
        TvAddress = v.findViewById(R.id.tv_company_addres);
        TvPostalCode = v.findViewById(R.id.tv_company_postal_code);
        TvSlug = v.findViewById(R.id.tv_company_slug);
        TvCountry = v.findViewById(R.id.tv_company_country);
        TvState = v.findViewById(R.id.tv_company_state);
        BtnUpdate = v.findViewById(R.id.btn_update);

        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Send = new Intent(getActivity(), MyCompanyDetail.class);
                getActivity().startActivity(Send);

            }
        });

        GetCompanyDetail();

    }

    public void GetCompanyDetail() {

        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetCompanyDetail);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    /*    if (jsonObject.getString("status").equalsIgnoreCase("true")) {*/
                    if (status) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject settings_data = data.getJSONObject("company");

                        StrName = settings_data.getString("name");
                        StrEmail = settings_data.getString("email");
                        StrPhone = settings_data.getString("phone");
                        StrAddress = settings_data.getString("address");
                        StrPostalCode = settings_data.getString("zip_code");
                        StrCity = settings_data.getString("city");
                        StrCountry = settings_data.getString("country");
                        StrSlug = settings_data.getString("slug");
                        StrState = settings_data.getString("state");

                        StrLogoImgae = settings_data.getString("logo");
                        StrStampImage = settings_data.getString("stamp");

                        JSONObject user_data = settings_data.getJSONObject("user");

                        if (user_data.has("image")) {
                            StruserProfile = user_data.getString("image");

                        } else {
                            StruserProfile = "";

                        }

                        if (user_data.has("signature")) {
                            StrUserSignature = user_data.getString("signature");
                        } else {
                            StrUserSignature = "";

                        }


                        TvName.setText(StrName);
                        TvEmail.setText(StrEmail);
                        TvPhone.setText(StrPhone);
                        TvAddress.setText(StrAddress);
                        TvPostalCode.setText(StrPostalCode);
                        TvCity.setText(StrCity);
                        TvCountry.setText(StrCountry);
                        TvSlug.setText(StrSlug);
                        TvState.setText(StrState);

                        showImageLogo(StrLogoImgae);
                        showImageStamp(StrStampImage);

                        showUserProfile(StruserProfile);


                        GlobalData.StrCompanyName = StrName;
                        GlobalData.StrCompanyEmail = StrEmail;
                        GlobalData.StrCompanyAddress = StrAddress;
                        GlobalData.StrCompanyCity = StrCity;
                        GlobalData.StrCompanyPhone = StrPhone;
                        GlobalData.StrCompanyZipCode = StrPostalCode;
                        GlobalData.StrCompanyCountry = StrCountry;
                        GlobalData.StrCompanyState = StrState;
                        GlobalData.StrCompanyLogoUrl = StrLogoImgae;
                        GlobalData.StrCompanyStampUrl = StrStampImage;
                        GlobalData.StrUserProfileUrl = StruserProfile;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressBar();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String error_response = new String(error.networkResponse.data);
                   /* try {
                        JSONObject response_obj = new JSONObject(error_response);
                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");

                            Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), String.valueOf("Error" + e.getMessage()), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }*/
                } else {
                    //Toast.makeText(getActivity(), String.valueOf("Server not responding" ), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }


    public void showImageLogo(String ImgPath) {
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages + ImgPath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(imageViewLogo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                   /*     imageView_Dummy.buildDrawingCache();
                        Bitmap bitmap = imageView_Dummy.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] image=stream.toByteArray();
                        FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);
                        Toast.makeText(getActivity(), FragmentEditReportUpdate.StBase64ImageToSave, Toast.LENGTH_SHORT).show();*/
                       /* imageView_Dummy.buildDrawingCache();
                        Bitmap bitmap = imageView_Dummy.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                        byte[] image=stream.toByteArray();

                        //  System.out.println("byte array:"+image);
                        FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);*/
                    }

                    @Override
                    public void onError(Exception e) {
                        // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }


    public void showImageStamp(String ImgPath) {
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages + ImgPath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(imageViewStamp, new com.squareup.picasso.Callback() {
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


    public void showUserProfile(String ImgPath) {
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages + ImgPath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(imageViewUser, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        //   Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }
}
