package test.invoicegenerator.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.Activities.CompanyDetailActivity;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;

/**
 * Created by User on 10/22/2018.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.edit_photo_btn)
    CircleImageView edit_photo;

    @BindView(R.id.text_name)
    TextView text_name;

    @BindView(R.id.text_company_name)
    TextView text_company_name;

    @BindView(R.id.text_phone_num)
    TextView text_phone_num;

    @BindView(R.id.text_email)
    TextView text_email;

    @BindView(R.id.name)
    EditText edit_name;

    @BindView(R.id.company_name)
    EditText edit_company_name;

    @BindView(R.id.email)
    EditText edit_email;

    @BindView(R.id.phone_num)
    EditText edit_phone_num;


    @BindView(R.id.update_profile)
    Button update_profile;

    private Unbinder unbinder;
  //  private FirebaseFirestore db;
    Progressbar cdd;
    private Uri FilePathUri;
    private ImageView profile_picture;
    public static String profile_pic_url="";
    //private FirebaseFirestore db;
    //public StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     //   super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.activity_profile,
                 null);
        unbinder= ButterKnife.bind(this,view);
        cdd=new Progressbar(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }
    private void init(View v)
    {

        edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfilePic();
            }
        });
        cdd.ShowProgress();

        update_profile.setOnClickListener(this);
        edit_company_name.setOnClickListener(this);
        edit_email.setFocusable(false);

        //updateProfilePicture();

       // getProfilePicFromFirebase();
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.edit_photo_btn:
                showProfilePic();
                break;
            case R.id.update_profile:
                isValidProfileInfo();

                break;
            case R.id.company_name:
                loadCompanyFragment(new CompanyDetailActivity());
                break;
        }
    }

    private void loadCompanyFragment(Fragment frg) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, frg);
        fragmentTransaction.commit();
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //SharedPreferenceHelper.newUserPrefHelper(getActivity()).clearValueForKey(Constants.PREF_KEY_BASIC_AUTH_HEADER_VALUE);
    }*/
    private void showProfilePic()
    {
        final Dialog settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //    settingsDialog.onCreatePanelView()
        View view=getLayoutInflater().inflate(R.layout.profile_pic,null);
        settingsDialog.setContentView(view);
        profile_picture=view.findViewById(R.id.profile_pic);
        ImageButton edit_dp=view.findViewById(R.id.edit_dp);
        Button update_pic=view.findViewById(R.id.update_pic);
        if(profile_pic_url!=null && !profile_pic_url.equals(""))
        Picasso.get().load(profile_pic_url).into(profile_picture);
        update_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // UploadImageFileToFirebaseStorage(settingsDialog);
            }
        });
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfilePic();
            }
        });
        settingsDialog.show();
    }


    private void isValidProfileInfo()
    {
        View focusView=null;
        boolean cancel = false;
        if(TextUtils.isEmpty(edit_name.getText().toString()))
    {
        edit_name.setError(getString(R.string.error_field_required));
        focusView = edit_name;
        cancel = true;
    }
    else if(!Util.isFullname(edit_name.getText().toString()))
    {
        edit_name.setError(getString(R.string.error_invalid_name));
        focusView = edit_name;
        cancel = true;
    }
    else if (TextUtils.isEmpty(edit_company_name.getText().toString())) {
            edit_company_name.setError(getString(R.string.error_field_required));
        focusView = edit_company_name;
        cancel = true;
    }
    else if (TextUtils.isEmpty(edit_email.getText())) {
            edit_email.setError(getString(R.string.error_field_required));
        focusView = edit_email;
        cancel = true;
    } else if (!Util.isEmailValid(edit_email.getText().toString())) {
            edit_email.setError(getString(R.string.error_invalid_email));
        focusView = edit_email;
        cancel = true;
    }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
         //   signUpFirebase(email,password);
        }
    }
    private void selectProfilePic()
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profile_picture.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
               // ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
 /*   public void UploadImageFileToFirebaseStorage(final Dialog dialog) {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            // Showing progressDialog.

            SharedPreferenceHelper helper=new SharedPreferenceHelper(getActivity());
            final String email=helper.getValue(Constants.AUTH_ID);

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));//.child(email);

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dismissProgress();
                            dialog.dismiss();
                            // Getting image name from EditText and store into string variable.
                            //String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.

                            // Showing toast message after done uploading.
                            Toast.makeText(getActivity(), "Profile picture updted successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(email, taskSnapshot.getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            dismissProgress();
                            dialog.dismiss();
                            // Showing exception erro message.
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                           // progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }*/
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


}
