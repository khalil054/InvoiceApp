package test.invoicegenerator.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import test.invoicegenerator.R;
import test.invoicegenerator.general.DrawingView;

public class DigitalSignature extends Fragment {


    public static DigitalSignature newInstance() {
        DigitalSignature fragment = new DigitalSignature();
        return fragment;
    }

    private DrawingView mDrawingView = null;
    private ImageButton edit_btn, clear_btn;
    Button save_btn,erase_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View rootView = inflater.inflate(R.layout.fragment_digital_sign, container, false);

//        initializeObject(rootView);
//        eventListeners();

        return rootView;

    }


    private void initializeObject(View view) {
        edit_btn = (ImageButton) view.findViewById(R.id.edit_btn);
        save_btn = (Button) view.findViewById(R.id.save_btn);
        clear_btn = (ImageButton) view.findViewById(R.id.clear_btn);
        mDrawingView = (DrawingView) view.findViewById(R.id.drawing);
        erase_all = (Button) view.findViewById(R.id.erase_all);

        mDrawingView.setColor("#FFFFFF");
    }
    private void eventListeners() {
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  imgPen.setImageResource(R.drawable.pencil);
                //  imgErase.setImageResource(R.drawable.eraser);
                mDrawingView.setErase(false);
                mDrawingView.setBrushSize(5);
                mDrawingView.setLastBrushSize(5);
                mDrawingView.setColor("#FFFFFF");
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setDrawingCacheEnabled(true);
                String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UserSignature/";
                String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String StoredPath = DIRECTORY + pic_name + ".png";
                String imagePath = store_image(mDrawingView.getDrawingCache(), DIRECTORY, pic_name+".png");
                Intent intent = new Intent();
                intent.putExtra("drawPath", imagePath);
                getActivity().setResult(13, intent);
                getActivity().finish();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // imgPen.setImageResource(R.drawable.pencil);
                //   imgErase.setImageResource(R.drawable.eraser);
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(15);
            }
        });

        erase_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}