package test.invoicegenerator.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.HeaderAdapter;
import test.invoicegenerator.model.HeaderDetail;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;
import static test.invoicegenerator.general.Constants.LOGO;
import static test.invoicegenerator.general.Constants.LOGO_COLLECTION;
import static test.invoicegenerator.general.Constants.PIC_CROP;
import static test.invoicegenerator.general.Constants.Stamp_Image_Request_Code;

public class HeadersFragment extends Fragment {
    @BindView(R.id.save)
    Button save;

    @BindView(R.id.first_header)
    RadioButton first_header;


    @BindView(R.id.radio_visual)
    RadioGroup radioGroup;

    @BindView(R.id.list_view)
    ListView list_view;


    private boolean state=true;
    private Bitmap logo_bitmap;
    private boolean logo_flag;
    private int index;
    // private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.header_fragment,
                container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {

        ArrayList<String> list=new ArrayList<>();
        list.add("");list.add("");list.add("");list.add("");
        HeaderDetail header=new HeaderDetail("Evolverstech","noor@evolverstech.com","48327489379","Lahore");
        HeaderAdapter adapter=new HeaderAdapter(getActivity(),list,header,index);
        list_view.setAdapter(adapter);
        list_view.setSelection(1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();


                String selected_index=list_view.getSelectedItemPosition()+"";

                if(logo_bitmap!=null)
                    encodeBitmapAndSaveToFirebase(logo_bitmap,getActivity(),LOGO_COLLECTION,LOGO,logo_flag);
            }
        });


        ArrayList<String> headers=new ArrayList<>();
        headers.add("header 1");headers.add("header 2");headers.add("header 3");headers.add("header 4");

    }
    public static HeadersFragment newInstance() {
        HeadersFragment fragment = new HeadersFragment();
        return fragment;
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

        Uri FilePathUri;
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
               /* if(selectedBitmap.getHeight()>170 || selectedBitmap.getHeight()<130 || selectedBitmap.getWidth()>170 ||
                        selectedBitmap.getWidth()<130)
                {
                    Toasty.error(getActivity(), getString(R.string.logo_size_error), Toast.LENGTH_SHORT, true).show();
                   getActivity().finish();// return;
                }
                else*/
                {
                //    camera.setImageBitmap(logo_bitmap);

                }
            }
        }
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
              cropIntent.putExtra("outputX", 150);
              cropIntent.putExtra("outputY", 150);


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


}
