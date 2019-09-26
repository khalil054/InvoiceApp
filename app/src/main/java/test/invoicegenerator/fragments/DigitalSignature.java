package test.invoicegenerator.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import test.invoicegenerator.R;
import test.invoicegenerator.general.DrawingView;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.model.SharedPref;

public class DigitalSignature extends BaseFragment {

    ConstraintLayout constraintLayout;

    public static DigitalSignature newInstance() {
        return new DigitalSignature();
    }

    private DrawingView mDrawingView = null;
    private ImageView edit_btn, clear_btn;
    Button save_btn, erase_all;
    String StrSignautirePath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_digital_sign, container, false);

        initializeObject(rootView);
        constraintLayout = rootView.findViewById(R.id.layout_sign);
        eventListeners();

        return rootView;

    }


    private void initializeObject(View view) {
        SharedPref.init(getActivity());
        edit_btn = view.findViewById(R.id.edit_btn);
        save_btn = view.findViewById(R.id.save_btn);
        clear_btn = view.findViewById(R.id.clear_btn);
        mDrawingView = view.findViewById(R.id.drawing);
        erase_all = view.findViewById(R.id.erase_all);

        mDrawingView.setColor("#000000");
    }

    private void eventListeners() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawingView.setErase(false);
                mDrawingView.setBrushSize(5);
                mDrawingView.setLastBrushSize(5);
                mDrawingView.setColor("#000000");
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setDrawingCacheEnabled(true);
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name + ".png";
                Bitmap b = mDrawingView.getDrawingCache();
                createDirectoryAndSaveFile(b, pic_name + ".png");


                GlobalData.StrUserSignature = ChangeFileToBase64(StrSignautirePath);

                if (GlobalData.StrUserSignature.isEmpty()) {
                    showErrorMessage(constraintLayout, "Select Your Signature");
                } else {


                    showErrorMessage(constraintLayout, "Switch Tab To Fill Other Informations,Thanks");
                }


            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(15);
            }
        });

        erase_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                mDrawingView.ClearAll();
            }
        });
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
            Log.d("mypath****************", dirPath + File.separator + fileName);
            return dirPath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("SdCardPath")
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        File fileToBeReturn = null;
        File direct = new File(Environment.getExternalStorageDirectory() + "/UserSignatues");

        if (!direct.exists()) {
            @SuppressLint("SdCardPath") File wallpaperDirectory = new File("/sdcard/UserSignatues/");
            wallpaperDirectory.mkdirs();
        }
        StrSignautirePath = "/sdcard/UserSignatues/" + fileName;

        File file = new File(new File("/sdcard/UserSignatues/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);


            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            FragmentEditReport.Signaturefile = file;

            //   Toast.makeText(this, "Signature Saved Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  finish();
    }

    public String ChangeFileToBase64(String filepath) {
        String base64Image;
        File imgFile = new File(filepath);
        if (imgFile.exists() && imgFile.length() > 0) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
        } else {
            base64Image = "";
        }
        return base64Image;
    }

}