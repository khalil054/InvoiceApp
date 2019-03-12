package test.invoicegenerator.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.R;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.Util;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.Image_Request_Code;
import static test.invoicegenerator.general.Constants.LOGO;
import static test.invoicegenerator.general.Constants.LOGO_COLLECTION;
import static test.invoicegenerator.general.Constants.PIC_CROP;
import static test.invoicegenerator.general.Constants.STAMP;
import static test.invoicegenerator.general.Constants.STAMP_COLLECTION;
import static test.invoicegenerator.general.Constants.Stamp_Image_Request_Code;

/**
 * Created by User on 10/25/2018.
 */

public class LogoFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.camera)
    ImageView camera;

    @BindView(R.id.stamp)
    ImageView stamp;

    @BindView(R.id.done)
    Button save;

    private Uri FilePathUri;
    private Bitmap logo_bitmap;
    private Bitmap stamp_bitmap;
  //  private FirebaseFirestore db;

    private boolean logo_flag,stamp_flag=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.logo_fragment,
                container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
    camera.setOnClickListener(this);
    stamp.setOnClickListener(this);
    save.setOnClickListener(this);

        getLogoFromFirebase();
        getStampFromFirebase();
    }

    private void getStampFromFirebase() {

        DocumentReference doc=db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(getActivity())).
                collection(Constants.STAMP_COLLECTION).document(Util.auth_idOfLoggedInUser(getActivity()));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                        DocumentSnapshot document=task.getResult();
                        dismissProgress();
                        if(document.exists()) {
                            String image = document.get(Constants.STAMP).toString();
                            try {
                                stamp.setImageBitmap(decodeFromFirebaseBase64(image));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stamp_flag=false;
                        }
                        else
                            stamp_flag=true;
                } else {
                    Log.d("", "error getting documents: ", task.getException());
                    dismissProgress();
                    Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                }
                dismissProgress();
            }
           /* @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();
                        Map<String, Object> clients=doc.getData();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Name: ").append(doc.get(Constants.NAME_KEY));
                    fields.append("\nEmail: ").append(doc.get(Constants.EMAIL_KEY));
                    fields.append("\nPhone: ").append(doc.get(Constants.PHONE_KEY));
                    view_client.setText(fields.toString());
                }
            }*/
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissProgress();
                        Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id)
        {
            case R.id.camera:
                selectLogoPic();
                break;
            case R.id.stamp:
                selectStampPic();
                break;
            case R.id.done:
                //FirebaseData firebase_data=new FirebaseData(getActivity());
                if(logo_bitmap!=null)
                encodeBitmapAndSaveToFirebase(logo_bitmap,getActivity(),LOGO_COLLECTION,LOGO,logo_flag);
                if(stamp_bitmap!=null)
                encodeBitmapAndSaveToFirebase(stamp_bitmap,getActivity(),STAMP_COLLECTION,STAMP,stamp_flag);
                break;
        }
    }
    private void selectLogoPic()
    {
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }
    private void selectStampPic()
    {
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Stamp_Image_Request_Code);
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
                camera.setImageBitmap(logo_bitmap);
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

            try {

                // Getting selected image into Bitmap.
                stamp_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                stamp.setImageBitmap(stamp_bitmap);
                int w=stamp_bitmap.getWidth();
                int h=stamp_bitmap.getHeight();
                Toast.makeText(getActivity(),w+" "+"x"+" "+h,Toast.LENGTH_LONG).show();

                // After selecting image change choose button above text.
                // ChooseButton.setText("Image Selected");

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

                camera.setImageBitmap(selectedBitmap);
            }
        }
    }

    public void getSignatureFromFirebase() {
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(getActivity())).collection(Constants.SIGNATURE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: task.getResult()){
                        Log.d("", document.getId() + " => " + document.getData());

                        /*db.collection("InvoiceDB").document(document.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("", "Error deleting document", e);
                                    }
                                });*/
                      /*  Map<String, Object> clients=document.getData();
                        StringBuilder fields = new StringBuilder("");
                        fields.append("Name: ").append(document.get(Constants.NAME_KEY));
                        fields.append("\nEmail: ").append(document.get(Constants.EMAIL_KEY));
                        fields.append("\nPhone: ").append(document.get(Constants.PHONE_KEY));
                        view_client.setText(view_client.getText()+fields.toString());*/
                        dismissProgress();
                        String image= document.get(Constants.SIGNATURE).toString();
                        //   sign_image.setImageBitmap(decodeFromFirebaseBase64(image));

                    }
                } else {
                    Log.d("", "error getting documents: ", task.getException());
                    dismissProgress();
                }
            }
           /* @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();
                        Map<String, Object> clients=doc.getData();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Name: ").append(doc.get(Constants.NAME_KEY));
                    fields.append("\nEmail: ").append(doc.get(Constants.EMAIL_KEY));
                    fields.append("\nPhone: ").append(doc.get(Constants.PHONE_KEY));
                    view_client.setText(fields.toString());
                }
            }*/
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissProgress();
                    }
                });
    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, final Context context, String col_name, String entity_name,boolean state) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] byte_arr = baos.toByteArray();
        //String image_str = Base64.encode(byte_arr);
        //String imageEncoded = ImageBase64.encodeTobase64(bitmap);
        String imageEncoded = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(entity_name, imageEncoded);
        // deleteSignature();
        DocumentReference doc= db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(context)).collection(col_name)
                .document(Util.auth_idOfLoggedInUser(getActivity()));
        if(state) {
            doc.set(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(getActivity(), getString(R.string.visual_detail_saved), Toast.LENGTH_SHORT, true).show();
                            dismissProgress();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                            dismissProgress();
                        }
                    });
        }
        else
        {
            doc.update(newContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(getActivity(), getString(R.string.visual_detail_updated), Toast.LENGTH_SHORT, true).show();
                            dismissProgress();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                            dismissProgress();
                        }
                    });
        }
    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(LOGO, imageEncoded);
        //deleteSignature();
        db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(getActivity())).collection(Constants.LOGO_COLLECTION).document().set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Client Registered",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }
    public void getLogoFromFirebase() {
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DocumentReference doc=db.collection("InvoiceDB").document(Util.auth_idOfLoggedInUser(getActivity())).
                collection(Constants.LOGO_COLLECTION).document(Util.auth_idOfLoggedInUser(getActivity()));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                        DocumentSnapshot document= task.getResult();
                        /*db.collection("InvoiceDB").document(document.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("", "Error deleting document", e);
                                    }
                                });*/
                      /*  Map<String, Object> clients=document.getData();
                        StringBuilder fields = new StringBuilder("");
                        fields.append("Name: ").append(document.get(Constants.NAME_KEY));
                        fields.append("\nEmail: ").append(document.get(Constants.EMAIL_KEY));
                        fields.append("\nPhone: ").append(document.get(Constants.PHONE_KEY));
                        view_client.setText(view_client.getText()+fields.toString());*/
                        dismissProgress();
                        if(document.exists()) {
                        String image = document.get(Constants.LOGO).toString();
                        try {
                            camera.setImageBitmap(decodeFromFirebaseBase64(image));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        logo_flag=false;
                    }
                    else
                        logo_flag=true;
                } else {
                    Log.d("", "error getting documents: ", task.getException());
                    Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                    dismissProgress();
                }
                dismissProgress();
            }
           /* @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();
                        Map<String, Object> clients=doc.getData();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Name: ").append(doc.get(Constants.NAME_KEY));
                    fields.append("\nEmail: ").append(doc.get(Constants.EMAIL_KEY));
                    fields.append("\nPhone: ").append(doc.get(Constants.PHONE_KEY));
                    view_client.setText(fields.toString());
                }
            }*/
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissProgress();
                        Toasty.error(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
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
}
