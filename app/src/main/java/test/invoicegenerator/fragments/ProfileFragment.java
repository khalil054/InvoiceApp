package test.invoicegenerator.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.Activities.CompanyDetailActivity;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;


public class ProfileFragment extends BaseFragment implements View.OnClickListener {
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

    Progressbar cdd;
    private ImageView profile_picture;
    public static String profile_pic_url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_profile,
                null);
        unbinder = ButterKnife.bind(this, view);
        cdd = new Progressbar(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

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

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
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


    private void showProfilePic() {
        final Dialog settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.profile_pic, null);
        settingsDialog.setContentView(view);
        profile_picture = view.findViewById(R.id.profile_pic);
        // ImageButton edit_dp=view.findViewById(R.id.edit_dp);
        Button update_pic = view.findViewById(R.id.update_pic);
        if (profile_pic_url != null && !profile_pic_url.equals(""))
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


    private void isValidProfileInfo() {
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(edit_name.getText().toString())) {
            edit_name.setError(getString(R.string.error_field_required));
            focusView = edit_name;
            cancel = true;
        } else if (!Util.isFullname(edit_name.getText().toString())) {
            edit_name.setError(getString(R.string.error_invalid_name));
            focusView = edit_name;
            cancel = true;
        } else if (TextUtils.isEmpty(edit_company_name.getText().toString())) {
            edit_company_name.setError(getString(R.string.error_field_required));
            focusView = edit_company_name;
            cancel = true;
        } else if (TextUtils.isEmpty(edit_email.getText())) {
            edit_email.setError(getString(R.string.error_field_required));
            focusView = edit_email;
            cancel = true;
        } else if (!Util.isEmailValid(edit_email.getText().toString())) {
            edit_email.setError(getString(R.string.error_invalid_email));
            focusView = edit_email;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        }
    }

    private void selectProfilePic() {
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathUri);

                // Setting up bitmap selected image into ImageView.
                profile_picture.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                // ChooseButton.setText("Image Selected");

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


}
