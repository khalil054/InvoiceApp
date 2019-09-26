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

    public static AddStamp newInstance() {
        AddStamp fragment = new AddStamp();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_add_stamp, container, false);
        ButterKnife.bind(this, rootView);
        constraintLayout = rootView.findViewById(R.id.layout_header);
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
                encodedImageData = getEncoded64ImageStringFromBitmap(bmap);
                GlobalData.StrCompanyStamp = encodedImageData;


                if (GlobalData.StrCompanyStamp.isEmpty()) {
                    showErrorMessage(constraintLayout, "Select Your Stamp Logo");
                } else {
                    /* Toast.makeText(getActivity(), CompanyFrame.StrCompanyStamp, Toast.LENGTH_SHORT).show();*/
                    showErrorMessage(constraintLayout, "Switch Tab To Fill Other Informations,Thanks");

                }


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
                stamp_image.setImageBitmap(logo_bitmap);
                stamp_image.buildDrawingCache();

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

        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public void showImageStamp(String ImgPath) {
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages + ImgPath)
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
