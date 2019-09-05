package test.invoicegenerator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import test.invoicegenerator.R;
import test.invoicegenerator.model.HeaderDetail;
import test.invoicegenerator.model.HeaderDetailSimple;

/**
 * Created by User on 11/9/2018.
 */

public class HeaderAdapter extends BaseAdapter {

    private ArrayList<String> mData = new ArrayList();
    private LayoutInflater mInflater;
    private Context context;
    private HeaderDetailSimple header;
    private int index;

    public HeaderAdapter(Context con, ArrayList<String> data, HeaderDetailSimple header_detail, int selected_index) {
        context=con;
        mData=data;
        index=selected_index;
        header=header_detail;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return header.getCompanyName();
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
            if(position==3)
            convertView = mInflater.inflate(R.layout.custom_header1, null);
            if(position==1)
                convertView = mInflater.inflate(R.layout.custom_header2, null);
            if(position==2)
                convertView = mInflater.inflate(R.layout.custome_header3, null);
            if(position==0)
                convertView = mInflater.inflate(R.layout.custom_header4, null);
           // if(position==index)
               // convertView.setBackgroundColor(context.getResources().getColor(R.color.contentDividerLine));

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.text_name);
           holder.addressView=convertView.findViewById(R.id.text_company_name);
            holder.phone=convertView.findViewById(R.id.text_phone_num);
            holder.email=convertView.findViewById(R.id.text_email);
            convertView.setTag(holder);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(header.getCompanyName());
        holder.addressView.setText(header.getCompanyName());
        holder.email.setText(header.getCompany_Email());
        holder.phone.setText(header.getCompany_Phone_no());
      //  LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
       // convertView.setLayoutParams(params);

        // Set the height of the Item View
        return convertView;
    }
    public static class ViewHolder {
        public TextView textView;
        public TextView addressView;
        public TextView phone;
        public TextView email;
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
}



