package test.invoicegenerator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.R;
import test.invoicegenerator.model.HeaderDetail;


public class HeaderAdapterJSON extends BaseAdapter {
    private ArrayList<HeaderDetail> mData = new ArrayList();
    private LayoutInflater mInflater;
    private Context context;

    public HeaderAdapterJSON(Context con, ArrayList<HeaderDetail> data) {
        context=con;
        mData=data;
        /*myImageLoader=new MyImageLoader( this.context);*/
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }*/

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position).getStrCompanyID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("getView " + position + " " + convertView);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_header5, null);
            holder = new ViewHolder();

            holder.imageView=convertView.findViewById(R.id.img_lock);
            if(position==0){
                holder.imageView.setImageResource(R.drawable.headerpic4);
            }else  if(position==1){
                holder.imageView.setImageResource(R.drawable.headerpic3);
            }else  if(position==2){
                holder.imageView.setImageResource(R.drawable.headerpic2);
            }else  if(position==3){
                holder.imageView.setImageResource(R.drawable.headerpic1);
            }

           /* if(position==3)
                convertView = mInflater.inflate(R.layout.custom_header4, null);
            if(position==1)
                convertView = mInflater.inflate(R.layout.custome_header3, null);
            if(position==2)
                convertView = mInflater.inflate(R.layout.custom_header2, null);
            if(position==0)
                convertView = mInflater.inflate(R.layout.custom_header1, null);

            holder = new ViewHolder();

                holder.imageView=convertView.findViewById(R.id.img_lock);
                holder.textView = convertView.findViewById(R.id.text_name);
                holder.addressView=convertView.findViewById(R.id.text_company_name);
                holder.phone=convertView.findViewById(R.id.text_phone_num);
                holder.email=convertView.findViewById(R.id.text_email);

                convertView.setTag(holder);
                convertView.setBackgroundColor(Color.TRANSPARENT);*/



        } else {
            holder = (ViewHolder)convertView.getTag();
        }


        return convertView;
    }
    public static class ViewHolder {

        public ImageView imageView;

    }
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public void showImageStamp(String ImgPath,ImageView img){

        String Str=NetworkURLs.BaseURLForImages+ImgPath;
        Picasso.get()
                .load(Str)
                .placeholder(R.color.grey) // Your dummy image...
                .into(img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                         Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }

}



