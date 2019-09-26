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

public class PlaceLogo extends BaseFragment {

    ConstraintLayout constraintLayoutl;

    @BindView(R.id.save_btn)
    Button save_btn;

    @BindView(R.id.SelectLog)
    Button imageButton;

    @BindView(R.id.logo_image)
    ImageView logo_image;

    @BindView(R.id.add_logo_text)
    TextView add_logo_text;

    String encodedImageData;


    public static PlaceLogo newInstance() {
        return new PlaceLogo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_place_logo, container, false);
        ButterKnife.bind(this, rootView);
        constraintLayoutl = rootView.findViewById(R.id.layout_headerr);
        init();
        return rootView;

    }

    private void init() {

        showImageStamp(GlobalData.StrCompanyLogoUrl);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectLogoPic();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logo_image.buildDrawingCache();
                Bitmap bmap = logo_image.getDrawingCache();
                encodedImageData = getEncoded64ImageStringFromBitmap(bmap);
                GlobalData.StrCompanyLogo = encodedImageData;

                if (GlobalData.StrCompanyLogo.isEmpty()) {
                    showErrorMessage(constraintLayoutl, "Select Logo Of Company");
                } else {
                    showErrorMessage(constraintLayoutl, "Switch Tab To Fill Other Informations,Thanks");
                }

                // Toast.makeText(getActivity(), "Click"+encodedImageData, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void selectLogoPic() {
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

            Uri filePathUri = data.getData();


            try {

                // Getting selected image into Bitmap.
                Bitmap logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathUri);

                // Setting up bitmap selected image into ImageView.
                logo_image.setImageBitmap(logo_bitmap);
                logo_image.buildDrawingCache();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                logo_image.setImageBitmap(selectedBitmap);
            }
        }
        //  imageButton.setVisibility(View.GONE);
        add_logo_text.setVisibility(View.GONE);
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public void showImageStamp(String ImgPath) {

        Picasso.get()
                .load(NetworkURLs.BaseURLForImages + ImgPath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(logo_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }
}
