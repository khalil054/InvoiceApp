package test.invoicegenerator.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.HeaderAdapterJSON;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.helper.ProjectUtils;
import test.invoicegenerator.model.HeaderDetail;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;

public class HeadersFragment extends BaseFragment {

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

    LinearLayout LayoutParent;
    ArrayList<HeaderDetail> json_list = new ArrayList<>();
    HeaderAdapterJSON headerAdapterJSON;
    int SelectedHeaderID;
    public static final int RequestPermissionCode = 1;

    public static HeadersFragment newInstance() {
        return new HeadersFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header_fragment,
                container, false);
        ButterKnife.bind(this, view);
        ImgChose = view.findViewById(R.id.camera);
        LayoutParent = view.findViewById(R.id.layout_parent);
        init();
        return view;
    }


    private void init() {
        EnableRuntimePermission();

        GetHeaderList();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImgChose.buildDrawingCache();

                Bitmap bmap = ImgChose.getDrawingCache();

                encodedImageData = getEncoded64ImageStringFromBitmap(bmap);

                GlobalData.StrHeaderLogo = encodedImageData;

                if (SelectedHeaderID != 0) {

                    UpdateHeaderTemplete();

                } else {

                    showErrorMessage(LayoutParent, "Chose Header Templete");
                }
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showErrorMessage(LayoutParent, NetworkURLs.BaseURLForImages + json_list.get(i).getStrCompanyLogo());

                SelectedHeaderID = json_list.get(i).getObjID();

            }
        });

        ImgChose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectLogoPic();
                    }
                });

    }


    private void selectLogoPic() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePathUri = data.getData();


            try {

                // Getting selected image into Bitmap.
                Bitmap logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathUri);

                int targetWidth = 105;
                int targetHeight = 105;
                Bitmap targetBitmap = Bitmap.createBitmap(
                        targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(targetBitmap);

                canvas.drawBitmap(logo_bitmap,
                        new Rect(0, 0, logo_bitmap.getWidth(), logo_bitmap.getHeight()),
                        new Rect(0, 0, targetWidth, targetHeight), null);

                ImgChose.setImageBitmap(targetBitmap);
                Toast.makeText(getActivity(), targetBitmap.getWidth() + "," + targetBitmap.getHeight(), Toast.LENGTH_LONG).show();


            } catch (IOException e) {

                e.printStackTrace();
            }

        } else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = null;
                if (bundle != null) {
                    bitmap = bundle.getParcelable("data");
                }
                ImgChose.setImageBitmap(bitmap);

            }
        }

    }


    public void GetHeaderList() {
        showProgressBar();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL + NetworkURLs.GetCompanyHeaders);

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
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json_list.size() > 0) {
                    headerAdapterJSON = new HeaderAdapterJSON(getActivity(), json_list);
                    list_view.setAdapter(headerAdapterJSON);
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


    void initVolleyCallbackForUpdate() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String status = data.getString("status");


                    if (status.equals("true")) {
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                        loadFragment(new Configrations(), null);

                    } else {


                        String error = jsonObject.getString("Error");

                        Toasty.error(getActivity(), error, Toast.LENGTH_SHORT).show();
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

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    //  VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response = new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj = new JSONObject(error_response);
                        String status = response_obj.getString("status");
                        if (status.equals("false")) {
                            //JSONObject error_obj=response_obj.getJSONObject("error");
                            String message = response_obj.getString("error");
                            Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }

        };
    }

    void UpdateHeaderTemplete() {
        showProgressBar();


        initVolleyCallbackForUpdate();
        mVolleyService = new VolleyService(mResultCallback, getActivity());

        JSONObject data = new JSONObject();
        JSONObject FinalObj = new JSONObject();

        try {

            if (!ProjectUtils.isEmptyString(GlobalData.StrHeaderLogo)) {
                data.put("logo", "data:image/png;base64," + GlobalData.StrHeaderLogo);

            }

            data.put("active", true);
            FinalObj.put("header_template", data);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String Str = NetworkURLs.BaseURL + NetworkURLs.UpdateCompanyHeader + SelectedHeaderID + ".json";
        mVolleyService.putDataVolleyForHeadersWithJson("PUTCALL", Str, FinalObj);


    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {

            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

            }
        }
    }

}
