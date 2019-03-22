package test.invoicegenerator.view.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.fragments.DigitalSignature;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.general.DrawingView;
import test.invoicegenerator.general.Util;

import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

/**
 * Created by User on 1/17/2019.
 */

public class DigitalSignatureActivity extends AppCompatActivity {

    @BindView(R.id.save_btn)
    Button save_btn;

    @BindView(R.id.drawing)
    DrawingView drawingView;

    public static boolean is_signed=false;
    public static String imagePath;
    private DBHelper db;

    private ImageButton edit_btn, clear_btn;
    Button erase_all;

    @BindView(R.id.image)
    ImageView image;
    private String signature_path="";

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
        db=new DBHelper(this);
        initializeObject();
        drawingView.setColor("#FFFFFF");
        signature_path=getIntent().getStringExtra("signature");
        setSignatureToImageView();
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
               // validateAndSaveData();
                drawingView.setDrawingCacheEnabled(true);
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name + ".png";
                if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(DigitalSignatureActivity.this)){
                    Bitmap b = drawingView.getDrawingCache();
                    FragmentEditReport.bitmapSignature=b;
                   /* imagePath = store_image(drawingView.getDrawingCache(), DIRECTORY, pic_name+".png");*/
                    imagePath = store_image(b, DIRECTORY, pic_name+".png");
                    db.updateInvoice("signature",imagePath,"signature_date",Util.getTodayDate(), FragmentEditReport.invoice_id);
                    Intent intent = new Intent();
                    intent.putExtra("drawPath", imagePath);
                    setResult(13, intent);
                    is_signed=true;
                    //imageSignature.setImageBitmap(b);

                }else {
                    Toast.makeText(DigitalSignatureActivity.this, String.valueOf("Storage Permission not allowed"), Toast.LENGTH_SHORT).show();

                }
                finish();

            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  imgPen.setImageResource(R.drawable.pencil);
                //  imgErase.setImageResource(R.drawable.eraser);
                drawingView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                drawingView.setErase(false);
                drawingView.setBrushSize(5);
                drawingView.setLastBrushSize(5);
                drawingView.setColor("#FFFFFF");
            }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // imgPen.setImageResource(R.drawable.pencil);
                //   imgErase.setImageResource(R.drawable.eraser);
                drawingView.setErase(true);
                drawingView.setBrushSize(15);
            }
        });

        erase_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.ClearAll();
                drawingView.invalidate();
                setSignatureToImageView();
            }
        });
    }

    private void setSignatureToImageView() {
        if(!signature_path.equals(""))
        {
            image.setVisibility(View.VISIBLE);
            drawingView.setVisibility(View.GONE);

            File imgFile = new File(signature_path);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                image.setImageBitmap(myBitmap);

            }
        }

    }

    private void initializeObject() {
        edit_btn = (ImageButton) findViewById(R.id.edit_btn);
        clear_btn = (ImageButton) findViewById(R.id.clear_btn);
        erase_all = (Button) findViewById(R.id.erase_all);
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
            return dirPath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff drawingView.setDrawingCacheEnabled(true);
                    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String StoredPath = DIRECTORY + pic_name + ".png";
                    if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(DigitalSignatureActivity.this))
                        imagePath = store_image(drawingView.getDrawingCache(), DIRECTORY, pic_name+".png");
                    Intent intent = new Intent();
                    intent.putExtra("drawPath", imagePath);
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
}
