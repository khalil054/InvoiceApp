package test.invoicegenerator.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;
import test.invoicegenerator.general.DrawingView;
import test.invoicegenerator.general.Util;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class DigitalSignatureActivity extends AppCompatActivity {
    @BindView(R.id.layout_sign)
    ConstraintLayout layout_edit;
    Snackbar snackbar;
    @BindView(R.id.save_btn)
    Button save_btn;

    @BindView(R.id.drawing)
    DrawingView drawingView;

    public static boolean is_signed=false;
   // public static String imagePath;
   // private DBHelper db;

    private ImageView edit_btn, clear_btn;
    Button erase_all;

    @BindView(R.id.image)
    ImageView image;
    private String signature_path="";
    @BindView(R.id.signature_by)
    EditText Signed_name;
    public static boolean isredrawimage=false;
    Bitmap decodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_digital_sign);
        ButterKnife.bind(this);//
        init();
        //setActionBar();
        // loadFragment(new FragmentLogin());
    }

    private void init() {
        Picasso.Builder builder = new Picasso.Builder(DigitalSignatureActivity.this);
        LruCache picassoCache = new LruCache(DigitalSignatureActivity.this);
        builder.memoryCache(picassoCache);
        try {
            Picasso.setSingletonInstance(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        picassoCache.clear();

     //   db=new DBHelper(this);
        initializeObject();
        drawingView.setColor("#000000");
        signature_path=getIntent().getStringExtra("signature");
        if(FragmentEditReport.IsNewInvoice){
            FragmentEditReport.StrImagePath="";
            setSignatureToImageView();

        }else{
         //   Toast.makeText(this, "On update Section", Toast.LENGTH_SHORT).show();
            setSignatureToImageViewUpdate();

        }
                /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });*/
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FragmentEditReport.IsNewInvoice){
                    String Str=Signed_name.getText().toString();
                    if(TextUtils.isEmpty(Str)){
                        showMessage("Enter Signed By Name First");
                    }else{
                        FragmentEditReport.StrSignedBy=Str;
                        drawingView.setDrawingCacheEnabled(true);
                        String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                        String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        String StoredPath = DIRECTORY + pic_name + ".png";
                        if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(DigitalSignatureActivity.this)){
                            //drawingView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            Bitmap b = drawingView.getDrawingCache();
                            createDirectoryAndSaveFile(b,pic_name+".png");
                            Intent intent = new Intent();
                            intent.putExtra("drawPath", FragmentEditReportUpdate.ImagePath);
                            setResult(13, intent);
                            is_signed=true;


                        }else {
                            Toast.makeText(DigitalSignatureActivity.this, String.valueOf("Storage Permission not allowed"), Toast.LENGTH_SHORT).show();

                        }
                        finish();
                    }

                }else {
                    String Str=Signed_name.getText().toString();
                    FragmentEditReportUpdate.ImageHaseBeenEdited=false;
                    if(isredrawimage){

                        if(!TextUtils.isEmpty(Str)){
                            FragmentEditReportUpdate.StrSignedBy=Str;
                            drawingView.setDrawingCacheEnabled(true);
                            String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                            String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            String StoredPath = DIRECTORY + pic_name + ".png";
                            if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(DigitalSignatureActivity.this)){
                                Bitmap b = drawingView.getDrawingCache();
                                createDirectoryAndSaveFile(b,pic_name+".png");
                                Intent intent = new Intent();
                                intent.putExtra("drawPath", FragmentEditReportUpdate.ImagePath);
                                setResult(13, intent);
                                is_signed=true;


                            }else {
                                Toast.makeText(DigitalSignatureActivity.this, String.valueOf("Storage Permission not allowed"), Toast.LENGTH_SHORT).show();

                            }
                            finish();
                        }else {
                            showMessage("Enter Name");
                        }

                    }else {
                     //   Toast.makeText(DigitalSignatureActivity.this, String.valueOf("Image Not Edited"), Toast.LENGTH_SHORT).show();

                        if(!TextUtils.isEmpty(Str)){
                            FragmentEditReportUpdate.StrSignedBy=Str;
                            image.buildDrawingCache();
                            Bitmap bitmap = image.getDrawingCache();
                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                            byte[] image=stream.toByteArray();
                            FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);
                         //   Toast.makeText(DigitalSignatureActivity.this,  FragmentEditReportUpdate.StBase64ImageToSave, Toast.LENGTH_SHORT).show();

                            if(!TextUtils.isEmpty(FragmentEditReportUpdate.StBase64ImageToSave)){
                                finish();
                            }else {
                                showMessage("Signature Not Found");
                            }


                        }else {
                            showMessage("Enter Name");
                        }
                    }

                }



            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isredrawimage=true;
                FragmentEditReportUpdate.ImageHaseBeenEdited=true;
                drawingView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                erase_all.setVisibility(View.VISIBLE);
                drawingView.setErase(false);
                drawingView.setBrushSize(5);
                drawingView.setLastBrushSize(5);
                drawingView.setColor("#000000");
            }
        });

      /*  clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // imgPen.setImageResource(R.drawable.pencil);
                //   imgErase.setImageResource(R.drawable.eraser);
                drawingView.setErase(true);
               // drawingView.invalidate();
                erase_all.setVisibility(View.VISIBLE);
                drawingView.setBrushSize(15);
            }
        });*/

        erase_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isredrawimage=true;
               drawingView.ClearAll();
               drawingView.invalidate();
               // drawingView.setErase(true);
                drawingView.invalidate();
                setSignatureToImageView();
            }
        });
    }

    private void setSignatureToImageViewUpdate() {
        Signed_name.setText(FragmentEditReportUpdate.StrSignedBy);
        image.setVisibility(View.VISIBLE);
        erase_all.setVisibility(View.GONE);
        drawingView.setVisibility(View.GONE);
        if(FragmentEditReportUpdate.ImageHaseBeenEdited){

            ChangeBase64ToImage(FragmentEditReportUpdate.StBase64ImageToSave);
        }else{
           // getBitmapFromURL(NetworkURLs.BaseURLForImages+FragmentEditReportUpdate.ImagePath);

            image.setVisibility(View.VISIBLE);

            Picasso.get().load(NetworkURLs.BaseURLForImages+FragmentEditReportUpdate.ImagePath).placeholder(R.color.grey).into(image);
           /* if(hasImage(image)){
                Toast.makeText(this, "Image found", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            }*/
        }



        /*File imgFile = new File(FragmentEditReport.StrImagePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);

        }*/
    }

    private void setSignatureToImageView() {
        /*if(!signature_path.equals(""))*/
        if(!TextUtils.isEmpty(FragmentEditReport.StrImagePath)){

                Signed_name.setText(FragmentEditReport.StrSignedBy);
                image.setVisibility(View.VISIBLE);
                erase_all.setVisibility(View.GONE);
                drawingView.setVisibility(View.GONE);

                File imgFile = new File(FragmentEditReport.StrImagePath);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    image.setImageBitmap(myBitmap);

                }


        }else {
            Toast.makeText(this, "signature_path null", Toast.LENGTH_SHORT).show();
        }
    }
    private void initializeObject() {
        edit_btn = (ImageView) findViewById(R.id.edit_btn);
       // clear_btn = (ImageView) findViewById(R.id.clear_btn);
        erase_all = (Button) findViewById(R.id.erase_all);
    }
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        File fileToBeReturn = null;
        File direct = new File(Environment.getExternalStorageDirectory() + "/UserSignatues");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/UserSignatues/");
            wallpaperDirectory.mkdirs();
        }
    if(FragmentEditReport.IsNewInvoice){
        FragmentEditReport.StrImagePath="/sdcard/UserSignatues/"+fileName;

    }else {
        FragmentEditReportUpdate.StrImagePath="/sdcard/UserSignatues/"+fileName;

    }
        File file = new File(new File("/sdcard/UserSignatues/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);


            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            FragmentEditReport.Signaturefile=file;

         //   Toast.makeText(this, "Signature Saved Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

      //  finish();
    }

    public String store_image(Bitmap _bitmapScaled, String dirPath, String fileName) {
        //you can create a new file name "test.jpg" in sdcard folder.
        File f = new File(dirPath, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            _bitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
             out.flush();
            out.close();
           // Toast.makeText(this, String.valueOf(dirPath + File.separator + fileName), Toast.LENGTH_SHORT).show();
            Log.d("mypath****************", dirPath + File.separator + fileName);
          //  Toast.makeText(DigitalSignatureActivity.this, String.valueOf("after storing :"+String.valueOf(dirPath + fileName)), Toast.LENGTH_SHORT).show();

            return dirPath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
      //      Toast.makeText(DigitalSignatureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return "";
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff drawingView.setDrawingCacheEnabled(true);
                    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String StoredPath = DIRECTORY + pic_name + ".png";
                    if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(DigitalSignatureActivity.this))
                        FragmentEditReportUpdate.ImagePath = store_image(drawingView.getDrawingCache(), DIRECTORY, pic_name+".png");
                    Intent intent = new Intent();
                    intent.putExtra("drawPath", FragmentEditReportUpdate.ImagePath);
                    setResult(13, intent);
                    is_signed=true;
                    finish();
                } else {
                    Toast.makeText(DigitalSignatureActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    public void showMessage(String Str){
        snackbar = Snackbar.make(layout_edit,Str, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /*Convert Base64 To Image*/

    public void ChangeBase64ToImage(String str){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(str, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image.setVisibility(View.VISIBLE);
            image.setImageBitmap(decodedImage);
    }





    public  String convertImageToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        FragmentEditReportUpdate.StrImagePath=Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


    public boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }
}