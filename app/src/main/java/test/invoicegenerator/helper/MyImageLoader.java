package test.invoicegenerator.helper;


import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import test.invoicegenerator.R;

public class MyImageLoader {
    private  Context context;

    public MyImageLoader(Context activity) {
        this.context=activity;
    }


    public void loadImage(String ImgUrl, ImageView imageView){
        Picasso.get().load(ImgUrl).placeholder(R.color.grey).into(imageView);
    }
}
