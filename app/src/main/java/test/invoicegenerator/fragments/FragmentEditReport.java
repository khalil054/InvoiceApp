package test.invoicegenerator.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import test.invoicegenerator.Activities.ClientSelectionActivity;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ItemAdapter;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.RealPathUtil;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.helper.GlideApp;
import test.invoicegenerator.model.InvoiceModel;
import test.invoicegenerator.model.SharedPref;
import test.invoicegenerator.Activities.ActivityAddItem;
import test.invoicegenerator.Activities.ClientActivity;
import test.invoicegenerator.Activities.DigitalSignatureActivity;
import test.invoicegenerator.Activities.DiscountActivity;
import test.invoicegenerator.Activities.ImagePickerActivity;
import test.invoicegenerator.Activities.InvoiceInfoActivity;
import test.invoicegenerator.Activities.TaxActivity;
import static test.invoicegenerator.general.Constants.ADD_ITEM_CODE;
import static test.invoicegenerator.general.Constants.CLIENT_CODE;
import static test.invoicegenerator.general.Constants.DISCOUNT_CODE;
import static test.invoicegenerator.general.Constants.INVOICE_INFO_CODE;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static test.invoicegenerator.general.Constants.SIGN_CODE;
import static test.invoicegenerator.general.Constants.TAX_CODE;


public class FragmentEditReport extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.layout_edit)
    RelativeLayout layout_edit;
    @BindView(R.id.card2)
    CardView client_card;
    @BindView(R.id.card_item)
    CardView add_item_card;
    @BindView(R.id.card5)
    CardView discount_card;
    @BindView(R.id.card6)
    CardView tax_card;
    @BindView(R.id.card9)
    CardView sign_card;
    @BindView(R.id.card11)
    CardView attachment_card;
    @BindView(R.id.items_list)
    ListView item_list;
    @BindView(R.id.subtotal)
    TextView subtotal_value_field;
    @BindView(R.id.discount_value)
    TextView discount_value;
    @BindView(R.id.total_value)
    TextView total_value;
    @BindView(R.id.attach_image_label)
    TextView attach_image_label;
    @BindView(R.id.add_client_text)
    TextView add_client_text;
    @BindView(R.id.tax_value)
    TextView tax_value;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.invoice_name)
    TextView invoice_name;
    @BindView(R.id.signature_value)
    TextView signature_value;
    @BindView(R.id.due_date)
    TextView due_date;
    @BindView(R.id.comment_field)
    EditText comment_field;
    @BindView(R.id.save_invoice)
    Button save_invoice;
    @BindView(R.id.invoice_info_layout)
    LinearLayout invoice_info_layout;
    public static int subtotal_value=0;
    private int tax,discount=0;
    private String tax_type,discount_type="";
    DBHelper db;
    public static ArrayList<Item> item_values=new ArrayList<>();
    private Uri FilePathUri;
    private Bitmap logo_bitmap;
    public static long invoice_id;
    public static String is_new;
    public static String is_clicked;
    public static String sign_path="";
    String realPath;
    public static final int REQUEST_IMAGE = 100;
    public static boolean isfromEditReport=false;
    public static String InvoiceDueDate,InvoiceCreateDate,InvoiceName,StrSignedBy;
    public static String SelectedClientId,SelectedUserID,SelectedCompanyId;
    Bitmap bitmap;
    VolleyService mVolleyService;
    IResult mResultCallback = null;
    Snackbar snackbar;
    public static JSONArray InvoicesArray=new JSONArray();
    public static String StrImagePath;
    public static File Signaturefile;
    String StBase64ImageToSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_edit_report, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        DashboardFragment.ShowInvoiceInfo=false;
        ImagePickerActivity.clearCache(getActivity());
        client_card.setOnClickListener(this);
        add_item_card.setOnClickListener(this);
        discount_card.setOnClickListener(this);
        tax_card.setOnClickListener(this);
        sign_card.setOnClickListener(this);
        save_invoice.setOnClickListener(this);
        attachment_card.setOnClickListener(this);
        invoice_info_layout.setOnClickListener(this);


        db=new DBHelper(getActivity());
        is_new = getArguments().getString("new");
        is_clicked= getArguments().getString("clicked");

        setAttachmentPath();
        //creating new invoice
        // Toast.makeText(getActivity(), String.valueOf(is_new), Toast.LENGTH_SHORT).show();
        if(is_new.equals("true"))
        {
            insertDataInInvoice();
        }
        else if(is_new.equals("false"))
        {

            InvoiceModel invoice= (InvoiceModel) getArguments().getSerializable("invoice");
            setAllComponentsOfInvoice(invoice);
            setClientInfo(invoice.getId());
            getItemsData();

            subtotal_value_field.setText(subtotal_value+"");
            if(Util.isZipCodeValid(discount_value.getText().toString()))
            {
                //   total_value.setText(subtotal_value+"");
                //   Toast.makeText(getActivity(), "Zip Code valid", Toast.LENGTH_SHORT).show();

            }
            else if(!Util.isZipCodeValid(discount_value.getText().toString()))
            {
                //  Toast.makeText(getActivity(), "Zip Code not valid", Toast.LENGTH_SHORT).show();
                if(!discount_value.getText().toString().equals(""))
                    total_value.setText(subtotal_value-Integer.parseInt(discount_value.getText().toString())+"");
            }
            //  db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

            db.numberOfRows();

            setInvoiceAndTaxValues();
        }

        itemClickListenerDetail();

    }

    private void setAttachmentPath() {
        realPath=db.getAttachmentPath(invoice_id);
    }

    private void setClientInfo(String client_key) throws NullPointerException{
        Client client_info=db.getClientInformation(Integer.parseInt(client_key));
        if(client_info!=null)
            add_client_text.setText(client_info.getClientName());
    }

    private void setAllComponentsOfInvoice(InvoiceModel invoice) throws NullPointerException {
        invoice_id=Long.valueOf(invoice.getId());
        invoice_name.setText(invoice.getInvoice_name());
        due_date.setText(invoice.getDue_date());
        discount_value.setText(invoice.getDiscount());
        tax_value.setText(invoice.getTax_rate());

        subtotal_value_field.setText(invoice.getSubtotal());
        if(invoice.getTotal_value().equalsIgnoreCase("0")){
            total_value.setText(invoice.getSubtotal());
        }else {
            total_value.setText(invoice.getTotal_value());
        }


        comment_field.setText(invoice.getComment());
        if(!invoice.getSignture_date().equals(""))
            signature_value.setText(String.valueOf("Signed On: "+invoice.getSignture_date()));
        if(!TextUtils.isEmpty(invoice.getPhoto())){
            loadProfile(invoice.getPhoto());
        }

        /*File imgFile = new File(invoice.getPhoto());

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            attach_image_label.setVisibility(View.GONE);
            image1.setImageBitmap(myBitmap);

        }*/

        sign_path = invoice.getSignature();

    }


    private void setInvoiceAndTaxValues() {
        getInvoiceValues();
        if(discount_type.equals("percentage"))
            discount_value.setText(discount+"%");
        else
            discount_value.setText(discount+"");
        tax_value.setText(tax+"");
    }

    private void insertDataInInvoice() {
        invoice_id=db.insertInvoiceData("invoice name","","","","","","",
                "","","","","","");
    }
    private void getItemsData() {
        Cursor rs = db.getItemsData(invoice_id);
        rs.moveToFirst();
        ArrayList<String> items=new ArrayList<>();
        item_values=new ArrayList<Item>();
        subtotal_value=0;
        if ( rs.moveToFirst()  && rs!=null) {
            while (!rs.isAfterLast()) {
                Item value_item = new Item();
                String nam = rs.getString(rs.getColumnIndex(DBHelper.DESCRIPTION));
                String amount = rs.getString(rs.getColumnIndex("amount"));
                String quantity = rs.getString(rs.getColumnIndex("quantity"));
                String unit_cost = rs.getString(rs.getColumnIndex("unit_cost"));
                String taxable = rs.getString(rs.getColumnIndex("taxable"));
                String additional = rs.getString(rs.getColumnIndex("additional"));
                if(!amount.equals(""))
                    subtotal_value = subtotal_value + Integer.parseInt(amount);
                items.add(nam);
                value_item.setDescription(nam);
                value_item.setAmount(amount);
                value_item.setQuantity(quantity);
                value_item.setUnit_cost(unit_cost);
                value_item.setTaxable(taxable);
                value_item.setAdditional(additional);
                item_values.add(value_item);
                rs.moveToNext();
            }
            subtotal_value_field.setText(subtotal_value+"");
        }
      /*  ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);*/
        ItemAdapter itemsAdapter=new ItemAdapter(getActivity(),item_values);
        ViewGroup.LayoutParams params = item_list.getLayoutParams();

        params.height = items.size()*100;
        item_list.setLayoutParams(params);
        item_list.requestLayout();
        item_list.setAdapter(itemsAdapter);

    }
    private void itemClickListenerDetail()
    {
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item= item_values.get(position);
                Intent intent=new Intent(getActivity(),ActivityAddItem.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",item.getId());
                bundle.putString("des",item.getDescription());
                bundle.putString("unit_cost",item.getUnit_cost());
                bundle.putString("quntity",item.getQuantity());
                bundle.putString("amount",item.getAmount());
                bundle.putString("taxable",item.getTaxable());
                bundle.putString("additional",item.getAdditional());
                bundle.putString("clicked","true");
                intent.putExtras(bundle);
                startActivityForResult(intent,ADD_ITEM_CODE);
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        // Toast.makeText(getActivity(), "Invoice ID:"+invoice_id, Toast.LENGTH_SHORT).show();

        //  getInvoiceValues();
        switch(id)
        {

            case R.id.card2:
                openClientActivity();
                break;
            case R.id.card_item:
                Intent intent1=new Intent(getActivity(), ActivityAddItem.class);
                intent1.putExtra("clicked","false");
                intent1.putExtra("invoice_id",invoice_id);
                startActivityForResult(intent1, ADD_ITEM_CODE);
                break;
            case R.id.card5:
                Intent intent2=new Intent(getActivity(), DiscountActivity.class);
                getInvoiceValues();
                intent2.putExtra("discount_type",discount_type);
                intent2.putExtra("discount",discount);
                startActivityForResult(intent2, Constants.DISCOUNT_CODE);
                break;
            case R.id.card6:
                Intent intent3=new Intent(getActivity(), TaxActivity.class);
                getInvoiceValues();
                intent3.putExtra("tax_type",tax_type);
                intent3.putExtra("tax",tax);

                startActivityForResult(intent3, Constants.TAX_CODE);
                break;
            case R.id.card9:
                Intent intent4=new Intent(getActivity(), DigitalSignatureActivity.class);
                intent4.putExtra("signature",sign_path);
                startActivityForResult(intent4, Constants.SIGN_CODE);
                break;
            case R.id.card11:
                selectLogoPic();
                break;
            case R.id.save_invoice:
            {
               /* String sign_date="";
                if(signature_value.getText().toString().contains(":"))
                {
                    String[] sign=signature_value.getText().toString().split(":");
                    sign_date=sign[1];
                }
                  *//*  db.updateinvoiceCalculation(subtotal_value_field.getText().toString(),total_value.getText().toString(),
                            comment_field.getText().toString(),realPath,sign_path,sign_date,
                            invoice_id);*//*
                total_value.setText(subtotal_value_field.getText().toString());

                  *//*  Toast.makeText(getActivity(), subtotal_value_field.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), realPath, Toast.LENGTH_SHORT).show();*//*

                if(db.updateinvoiceCalculation(subtotal_value_field.getText().toString(),total_value.getText().toString(),
                        comment_field.getText().toString(),realPath,sign_path,sign_date,
                        invoice_id)){
                    loadFragment(new FragmentReport());
                    //  Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                }else {
                    // Toast.makeText(getActivity(), "not updated", Toast.LENGTH_SHORT).show();
                }*/

                /*if(InvoicesArray.length()>0){
                    Toast.makeText(getActivity(), String.valueOf(InvoicesArray), Toast.LENGTH_SHORT).show();
                    if(isEmptyString(InvoiceDueDate)){
                        showMessage("Invoice Date is Missing");
                    }else if(isEmptyString( comment_field.getText().toString())){
                        showMessage("Add Some Notes");
                    }else if(isEmptyString(InvoiceCreateDate)){
                        showMessage("Invoice Create is Missing");
                    }else if(InvoicesArray.length()<0){
                        showMessage("Invoice Items are empty");
                    }else {
                        final JSONObject InvoiceToBeSend = new JSONObject();
                        final JSONObject js = new JSONObject();
                        try {


                            InvoiceToBeSend.put("signed_by", "Khalil");
                            InvoiceToBeSend.put("invoice_number", "1234");
                            InvoiceToBeSend.put("due_at", "2019-03-15");
                            InvoiceToBeSend.put("invoiced_on", "2019-03-10");
                            InvoiceToBeSend.put("signed_at", "2019-03-10");
                            InvoiceToBeSend.put("signature", "signature image file");
                            InvoiceToBeSend.put("notes","invoce notes");
                            InvoiceToBeSend.put("payment_status", 0);
                            InvoiceToBeSend.put("delivery_status", 0);
                            InvoiceToBeSend.put("user_id", "60");
                            InvoiceToBeSend.put("company_id","39");
                            InvoiceToBeSend.put("client_id", "2");
                            InvoiceToBeSend.put("invoice_items_attributes", InvoicesArray);


                            js.put("invoice", InvoiceToBeSend);
                            DataSendToServerForAddInvoice(js);
                           // SendJsonObject(js);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }else {
                    Toast.makeText(getActivity(), "Please Add Some Invoice", Toast.LENGTH_SHORT).show();
                }*/
               /* String sign_date="";
                if(signature_value.getText().toString().contains(":"))
                {
                    String[] sign=signature_value.getText().toString().split(":");
                    sign_date=sign[1];
                }
                  *//*  db.updateinvoiceCalculation(subtotal_value_field.getText().toString(),total_value.getText().toString(),
                            comment_field.getText().toString(),realPath,sign_path,sign_date,
                            invoice_id);*//*
                total_value.setText(subtotal_value_field.getText().toString());

                  *//*  Toast.makeText(getActivity(), subtotal_value_field.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), realPath, Toast.LENGTH_SHORT).show();*//*

                if(db.updateinvoiceCalculation(subtotal_value_field.getText().toString(),total_value.getText().toString(),
                        comment_field.getText().toString(),realPath,sign_path,sign_date,
                        invoice_id)){
                    loadFragment(new FragmentReport());
                    //  Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                }else {
                    // Toast.makeText(getActivity(), "not updated", Toast.LENGTH_SHORT).show();
                }*/

                SelectedCompanyId=SharedPref.read(SharedPref.CompanyID,"");
                SelectedUserID=SharedPref.read(SharedPref.CompanyID,"");

                if(InvoicesArray.length()>0){
                    Toast.makeText(getActivity(), String.valueOf(InvoicesArray), Toast.LENGTH_SHORT).show();
                    if(isEmptyString(InvoiceDueDate)){
                        showMessage("Invoice Date is Missing");
                    }else if(isEmptyString(comment_field.getText().toString())){
                        showMessage("Add Some Notes");
                    }else if(isEmptyString(InvoiceCreateDate)){
                        showMessage("Invoice Create is Missing");
                    }else if(InvoicesArray.length()<0){
                        showMessage("Invoice Items are empty");
                    }else if(TextUtils.isEmpty(SelectedCompanyId)){
                        showMessage("Compony Not Found");
                    }else if(TextUtils.isEmpty(SelectedUserID)){
                        showMessage("User Not Found");
                    }else if(TextUtils.isEmpty(SelectedClientId)){
                        showMessage("Client Not Found");
                    }else if(TextUtils.isEmpty(signature_value.getText().toString())){
                        showMessage("Signature Date Not Found");
                    }else if(TextUtils.isEmpty(StrImagePath)){
                        showMessage("Signature Image Not Found");
                    }else if(TextUtils.isEmpty(StrSignedBy)){
                        showMessage("Signed By Name Not Found");
                    }else {
                        final JSONObject InvoiceToBeSend = new JSONObject();
                        final JSONObject js = new JSONObject();
                        try {

                          //  Toast.makeText(getActivity(), StBase64ImageToSave, Toast.LENGTH_SHORT).show();
                            String SignatureValue=signature_value.getText().toString();
                            String[]SignedDateDummy=SignatureValue.split(":");
                            String SignedDate=SignedDateDummy[1];
                            InvoiceToBeSend.put("signed_by", StrSignedBy);
                            InvoiceToBeSend.put("invoice_number", "1234");
                            InvoiceToBeSend.put("due_at", InvoiceDueDate);
                            InvoiceToBeSend.put("invoiced_on", InvoiceCreateDate);
                            InvoiceToBeSend.put("signed_at", SignedDate);
                            InvoiceToBeSend.put("signature", StBase64ImageToSave);
                            InvoiceToBeSend.put("notes",comment_field.getText().toString());
                            InvoiceToBeSend.put("payment_status", "unpaid");
                            InvoiceToBeSend.put("delivery_status", "draft");
                            InvoiceToBeSend.put("user_id", SelectedUserID);
                            InvoiceToBeSend.put("company_id",SelectedCompanyId);
                            InvoiceToBeSend.put("client_id", SelectedClientId);
                            InvoiceToBeSend.put("invoice_items_attributes", InvoicesArray);


                            js.put("invoice", InvoiceToBeSend);
                            DataSendToServerForAddInvoice(js);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }else {
                    Toast.makeText(getActivity(), "Please Add Some Invoice", Toast.LENGTH_SHORT).show();
                }


            }
            break;
            case R.id.invoice_info_layout:
                openInvoiceInfoActivity();
                break;
        }
    }

    private void loadFragment(Fragment reportFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, reportFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void openInvoiceInfoActivity() {
        Intent intent5=new Intent(getActivity(), InvoiceInfoActivity.class);
        InvoiceModel model=getInvoiceInfo();
        intent5.putExtra("info",model);
        startActivityForResult(intent5,INVOICE_INFO_CODE);
    }

    private InvoiceModel getInvoiceInfo() {
        Cursor rs=db.getInvoiceData(FragmentEditReport.invoice_id);
        rs.moveToFirst();
        InvoiceModel invoiceModel=new InvoiceModel();
        while (!rs.isAfterLast()) {
            String name=rs.getString(rs.getColumnIndex("invoice_name"));
            String due_date=rs.getString(rs.getColumnIndex("due_date"));
            String invoice_date=rs.getString(rs.getColumnIndex("invoice_date"));
            invoiceModel.setInvoice_name(name);
            invoiceModel.setDue_date(due_date);
            invoiceModel.setInvoice_date(invoice_date);

            rs.moveToNext();
        }
        return invoiceModel;
    }

    private void openClientActivity() {
        Intent intent=new Intent(getActivity(), ClientSelectionActivity.class);
        startActivityForResult(intent, Constants.CLIENT_CODE);

        /*if(!db.isClientExist(invoice_id))
        {
            intent.putExtra("is_new", "false");
            startActivityForResult(intent, Constants.CLIENT_CODE);
            isfromEditReport=true;
           *//* Intent Send=new Intent(getActivity(),MainActivity.class);
            startActivity(Send);
            getActivity().finish();*//*
        }
        else
        {
            Client client=db.getClientInformation(invoice_id);
            intent.putExtra("client_obj", client);
            intent.putExtra("is_new", "true");
            startActivityForResult(intent, Constants.CLIENT_CODE);
        }*/

       /* isfromEditReport=true;
        Intent Send=new Intent(getActivity(),MainActivity.class);
        startActivity(Send);
        getActivity().finish();*/
    }

    private void selectLogoPic()
    {
        /*Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);*/

        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!is_new.equals("true"))
        {
            getItemsData();
            if(!subtotal_value_field.getText().toString().equals(""))
                total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                        discount, tax, discount_type,tax_type ) + "");

            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                    realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), FilePathUri);
                } else {
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        getInvoiceValues();
        if(requestCode==CLIENT_CODE)
        {
           /* if(ClientActivity.client_info!=null)
                add_client_text.setText("To: "+ClientActivity.client_info.getClientName());*/
        }
        if (requestCode == DISCOUNT_CODE) {
            db.updateDiscountInInvoice(DiscountActivity.type,String.valueOf(DiscountActivity.discount_amount), FragmentEditReport.invoice_id);
            getInvoiceValues();
            if(DiscountActivity.type.equals("Flat Item")) {
                discount_value.setText(DiscountActivity.discount_amount + "");
                if(!subtotal_value_field.getText().toString().equals(""))
                    total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                            discount, tax, discount_type,tax_type ) + "");
            }
            else if(DiscountActivity.type.equals("percentage"))
            {
                discount_value.setText(DiscountActivity.discount_amount + "%");
                if (DiscountActivity.discount_amount != 0)
                {
                    if(!subtotal_value_field.getText().toString().equals(""))
                        total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                                discount, tax, discount_type,tax_type ) + "");
                }
            }
            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }
        else if(requestCode==TAX_CODE)
        {
            db.updateTaxInInvoice(TaxActivity.type,String.valueOf(TaxActivity.tax_amount), FragmentEditReport.invoice_id);
            getInvoiceValues();
            tax_value.setText(TaxActivity.tax_amount + "");
            if(!subtotal_value_field.getText().toString().equals(""))
                total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                        discount, tax, discount_type,tax_type ) + "");

            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }
        else if(requestCode==ADD_ITEM_CODE)
        {
            subtotal_value_field.setText(subtotal_value+"");
            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value+"",invoice_id);

        }else  if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                realPath=String.valueOf(uri);
                Toast.makeText(getActivity(), String.valueOf(uri), Toast.LENGTH_SHORT).show();
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /*else if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {


            FilePathUri = data.getData();

            //start testing
            // SDK < API11
            if (Util.checkPermissionREAD_EXTERNAL_STORAGE(getActivity())) {
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());

            }

            try {

                // Getting selected image into Bitmap.
                logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                image1.setImageBitmap(logo_bitmap);
                db.updateAttachment(logo_bitmap,invoice_id);
                attach_image_label.setVisibility(View.GONE);

            }
            catch (IOException e) {

                e.printStackTrace();
            }

        }*/
        else if(requestCode==SIGN_CODE)
        {

            if(DigitalSignatureActivity.is_signed)
                StBase64ImageToSave=ChangeFileToBase64(StrImagePath);
                signature_value.setText(String.valueOf("Signed on: "+ Util.getTodayDate()));
        }
        else if(requestCode==INVOICE_INFO_CODE)
        {
            setUpdatedInvoiceInfo();
        }


    }

    private void setUpdatedInvoiceInfo() {
        Cursor rs=db.getInvoiceData(invoice_id);

        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            invoice_name.setText(rs.getString(rs.getColumnIndex("invoice_name")));
            due_date.setText(rs.getString(rs.getColumnIndex("due_date")));
            rs.moveToNext();
        }

    }

    private void getInvoiceValues()
    {
        Toast.makeText(getActivity(), String.valueOf(invoice_id), Toast.LENGTH_SHORT).show();

        Cursor rs = db.getInvoiceData(invoice_id);
        if(rs!=null){
            if(rs.getCount()>0){
                rs.moveToFirst();
                while (!rs.isAfterLast()) {
                    if(!rs.getString(rs.getColumnIndex("tax_rate")).equals(""))
                        tax = Integer.parseInt(rs.getString(rs.getColumnIndex("tax_rate")));
                    tax_type=rs.getString(rs.getColumnIndex("tax_type"));
                    if(!rs.getString(rs.getColumnIndex("discount")).equals(""))
                        discount=Integer.parseInt(rs.getString(rs.getColumnIndex("discount")));
                    discount_type=rs.getString(rs.getColumnIndex("dis_type"));
                    sign_path=rs.getString(rs.getColumnIndex("signature"));
                    rs.moveToNext();
                }
            }else {
                Toast.makeText(getActivity(), String.valueOf("No Record Found against this id"), Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(getActivity(), String.valueOf("No Db Found"), Toast.LENGTH_SHORT).show();

        }

       /* rs.moveToFirst();
        while (!rs.isAfterLast()) {
            if(!rs.getString(rs.getColumnIndex("tax_rate")).equals(""))
                tax = Integer.parseInt(rs.getString(rs.getColumnIndex("tax_rate")));
            tax_type=rs.getString(rs.getColumnIndex("tax_type"));
            if(!rs.getString(rs.getColumnIndex("discount")).equals(""))
                discount=Integer.parseInt(rs.getString(rs.getColumnIndex("discount")));
            discount_type=rs.getString(rs.getColumnIndex("dis_type"));
            sign_path=rs.getString(rs.getColumnIndex("signature"));
            rs.moveToNext();
        }*/

    }

    private void loadProfile(String url) {
        //Log.d(TAG, "Image cache path: " + url);

        GlideApp.with(this).load(url)
                .into(image1);
        image1.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.transparent));
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
//        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
//            dialog.cancel();
//            openSettings();
//        });
//        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

        public void showMessage(String Str){
            snackbar = Snackbar.make(layout_edit,Str, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    void DataSendToServerForAddInvoice(JSONObject Da)
    {
        showProgressBar();

        initVolleyCallbackForAddClient();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
     /*   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(String.valueOf(Da));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();*/
       /* JSONObject Data=new JSONObject();


        try {

            Data.put("invoice",D);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        mVolleyService.postDataVolleyForHeadersWithJson("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.AddInvoices,Da );
    }

    void initVolleyCallbackForAddClient(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {
                 //   Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectNew =jsonObject.getJSONObject("data");
                    Boolean status = jsonObjectNew.getBoolean("status");


                    if(status)
                    {


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentReport(),null);

                            }
                        }, 1000);

                        snackbar = Snackbar.make(layout_edit,"invoice Added Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {


                        String error = jsonObject.getString("Error");
                        Toasty.error(getActivity(),error, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressBar();




            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                hideProgressBar();

                if(error.networkResponse != null && error.networkResponse.data != null) {

                    String error_response = new String(error.networkResponse.data);
                    // dialogHelper.showErroDialog(error_response);
                    Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();

                    SharedPref.init(getActivity());
                    String access_token = SharedPref.read(Constants.ACCESS_TOKEN, "");
                    String client = SharedPref.read(Constants.CLIENT, "");
                    String uid = SharedPref.read(Constants.UID, "");

                    Toast.makeText(getActivity(), String.valueOf("Error" + error_response + "\n" + access_token + "\n" + client + "\n" + uid), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(String.valueOf("Error" + error_response + "\n" + access_token + "\n" + client + "\n" + uid));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.create().show();


                    try {
                        JSONObject response_obj = new JSONObject(error_response);

                        {
                            JSONObject error_obj = response_obj.getJSONObject("error");
                            String message = error_obj.getString("message");

                            Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), String.valueOf("Error not responding" ), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }



        };
    }


    public String ChangeFileToBase64(String filepath){
        String base64Image;
        File imgFile = new File(filepath);
        if (imgFile.exists() && imgFile.length() > 0) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
        }else {
            base64Image="";
        }
        return base64Image;
    }

/*
    JSONObject student2 = new JSONObject();
try {
        student2.put("id", "2");
        student2.put("name", "NAME OF STUDENT2");
        student2.put("year", "4rd");
        student2.put("curriculum", "scicence");
        student2.put("birthday", "5/5/1993");

    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    JSONArray jsonArray = new JSONArray();

jsonArray.put(student1);*/
}

/*
package test.invoicegenerator.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ItemAdapter;
import test.invoicegenerator.databaseutilities.Client;
import test.invoicegenerator.databaseutilities.DBHelper;
import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.RealPathUtil;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.InvoiceModel;
import test.invoicegenerator.view.activities.ActivityAddItem;
import test.invoicegenerator.view.activities.ClientActivity;
import test.invoicegenerator.view.activities.DigitalSignatureActivity;
import test.invoicegenerator.view.activities.DiscountActivity;
import test.invoicegenerator.view.activities.InvoiceInfoActivity;
import test.invoicegenerator.view.activities.TaxActivity;

import static android.app.Activity.RESULT_OK;
import static test.invoicegenerator.general.Constants.ADD_ITEM_CODE;
import static test.invoicegenerator.general.Constants.CLIENT_CODE;
import static test.invoicegenerator.general.Constants.DISCOUNT_CODE;
import static test.invoicegenerator.general.Constants.INVOICE_INFO_CODE;
import static test.invoicegenerator.general.Constants.Image_Request_Code;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static test.invoicegenerator.general.Constants.SIGN_CODE;
import static test.invoicegenerator.general.Constants.TAX_CODE;

*/
/**
 * Created by User on 1/14/2019.
 *//*


public class FragmentEditReport extends BaseFragment implements View.OnClickListener{


    @BindView(R.id.card2)
    CardView client_card;

    @BindView(R.id.card_item)
    CardView add_item_card;

    @BindView(R.id.card5)
    CardView discount_card;

    @BindView(R.id.card6)
    CardView tax_card;

    @BindView(R.id.card9)
    CardView sign_card;

    @BindView(R.id.card11)
    CardView attachment_card;

    @BindView(R.id.items_list)
    ListView item_list;

    @BindView(R.id.subtotal)
    TextView subtotal_value_field;

    @BindView(R.id.discount_value)
    TextView discount_value;

    @BindView(R.id.total_value)
    TextView total_value;

    @BindView(R.id.attach_image_label)
    TextView attach_image_label;

    @BindView(R.id.add_client_text)
    TextView add_client_text;

    @BindView(R.id.tax_value)
    TextView tax_value;

    @BindView(R.id.image1)
    ImageView image1;

    @BindView(R.id.invoice_name)
    TextView invoice_name;

    @BindView(R.id.signature_value)
    TextView signature_value;

    @BindView(R.id.due_date)
    TextView due_date;

    @BindView(R.id.comment_field)
    EditText comment_field;

    @BindView(R.id.save_invoice)
    Button save_invoice;

    @BindView(R.id.invoice_info_layout)
    LinearLayout invoice_info_layout;

    private int subtotal_value=0;

    private int tax,discount=0;
    private String tax_type,discount_type="";

    DBHelper db;
    private ArrayList<Item> item_values;
    private Uri FilePathUri;
    private Bitmap logo_bitmap;
    public static long invoice_id;
    public static String is_new;
    public static String is_clicked;
    public static String sign_path="";
    String realPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_edit_report, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        client_card.setOnClickListener(this);
        add_item_card.setOnClickListener(this);
        discount_card.setOnClickListener(this);
        tax_card.setOnClickListener(this);
        sign_card.setOnClickListener(this);
        save_invoice.setOnClickListener(this);
        attachment_card.setOnClickListener(this);
        invoice_info_layout.setOnClickListener(this);

        db=new DBHelper(getActivity());
        is_new = getArguments().getString("new");
        is_clicked= getArguments().getString("clicked");

        setAttachmentPath();
        //creating new invoice
        if(is_new.equals("true"))
        {
            insertDataInInvoice();
        }
        else if(is_new.equals("false"))
        {

            InvoiceModel invoice= (InvoiceModel) getArguments().getSerializable("invoice");
            setAllComponentsOfInvoice(invoice);
            setClientInfo(invoice.getId());
            getItemsData();

            subtotal_value_field.setText(subtotal_value+"");
            if(Util.isZipCodeValid(discount_value.getText().toString()))
            {
                total_value.setText(subtotal_value+"");
            }
            else if(!Util.isZipCodeValid(discount_value.getText().toString()))
            {
                if(!discount_value.getText().toString().equals(""))
                    total_value.setText(subtotal_value-Integer.parseInt(discount_value.getText().toString())+"");
            }
            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

            db.numberOfRows();

            setInvoiceAndTaxValues();
        }

        itemClickListenerDetail();

    }

    private void setAttachmentPath() {
        realPath=db.getAttachmentPath(invoice_id);
    }

    private void setClientInfo(String client_key) throws NullPointerException{
        Client client_info=db.getClientInformation(Integer.parseInt(client_key));
        if(client_info!=null)
        add_client_text.setText(client_info.getClientName());
    }

    private void setAllComponentsOfInvoice(InvoiceModel invoice) throws NullPointerException {
        invoice_id=Long.valueOf(invoice.getId());
        invoice_name.setText(invoice.getInvoice_name());
        due_date.setText(invoice.getDue_date());
        discount_value.setText(invoice.getDiscount());
        tax_value.setText(invoice.getTax_rate());
        total_value.setText(invoice.getTotal_value());
        subtotal_value_field.setText(invoice.getSubtotal());
        comment_field.setText(invoice.getComment());
        if(!invoice.getSignture_date().equals(""))
        signature_value.setText("Signed On: "+invoice.getSignture_date());
        File imgFile = new File(invoice.getPhoto());

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            attach_image_label.setVisibility(View.GONE);
            image1.setImageBitmap(myBitmap);

        }

        sign_path = invoice.getSignature();
    }

    private void setInvoiceAndTaxValues() {
        getInvoiceValues();
        if(discount_type.equals("percentage"))
            discount_value.setText(discount+"%");
        else
            discount_value.setText(discount+"");
        tax_value.setText(tax+"");
    }

    private void insertDataInInvoice() {
        invoice_id=db.insertInvoiceData("invoice name","","","","","","",
                "","","","","","");
    }
    private void getItemsData() {
        Cursor rs = db.getItemsData(invoice_id);
        rs.moveToFirst();
        ArrayList<String> items=new ArrayList<>();
        item_values=new ArrayList<Item>();;
        subtotal_value=0;
        if ( rs.moveToFirst()  && rs!=null) {
            while (!rs.isAfterLast()) {
                Item value_item = new Item();
                String nam = rs.getString(rs.getColumnIndex(DBHelper.DESCRIPTION));
                String amount = rs.getString(rs.getColumnIndex("amount"));
                String quantity = rs.getString(rs.getColumnIndex("quantity"));
                String unit_cost = rs.getString(rs.getColumnIndex("unit_cost"));
                String taxable = rs.getString(rs.getColumnIndex("taxable"));
                String additional = rs.getString(rs.getColumnIndex("additional"));
                if(!amount.equals(""))
                    subtotal_value = subtotal_value + Integer.parseInt(amount);
                items.add(nam);
                value_item.setDescription(nam);
                value_item.setAmount(amount);
                value_item.setQuantity(quantity);
                value_item.setUnit_cost(unit_cost);
                value_item.setTaxable(taxable);
                value_item.setAdditional(additional);
                item_values.add(value_item);
                rs.moveToNext();
            }
            subtotal_value_field.setText(subtotal_value+"");
        }
      */
/*  ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);*//*

        ItemAdapter itemsAdapter=new ItemAdapter(getActivity(),item_values);
        ViewGroup.LayoutParams params = item_list.getLayoutParams();

        params.height = items.size()*100;
        item_list.setLayoutParams(params);
        item_list.requestLayout();
        item_list.setAdapter(itemsAdapter);

    }
    private void itemClickListenerDetail()
    {
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item= item_values.get(position);
                Intent intent=new Intent(getActivity(),ActivityAddItem.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",item.getId());
                bundle.putString("des",item.getDescription());
                bundle.putString("unit_cost",item.getUnit_cost());
                bundle.putString("quntity",item.getQuantity());
                bundle.putString("amount",item.getAmount());
                bundle.putString("taxable",item.getTaxable());
                bundle.putString("additional",item.getAdditional());
                bundle.putString("clicked","true");
                intent.putExtras(bundle);
                startActivityForResult(intent,ADD_ITEM_CODE);
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        getInvoiceValues();
            switch(id)
            {

                case R.id.card2:
                    openClientActivity();
                    break;
                case R.id.card_item:
                    Intent intent1=new Intent(getActivity(), ActivityAddItem.class);
                    intent1.putExtra("clicked","false");
                    intent1.putExtra("invoice_id",invoice_id);
                    startActivityForResult(intent1, ADD_ITEM_CODE);
                    break;
                case R.id.card5:
                    Intent intent2=new Intent(getActivity(), DiscountActivity.class);
                    getInvoiceValues();
                    intent2.putExtra("discount_type",discount_type);
                    intent2.putExtra("discount",discount);
                    startActivityForResult(intent2, Constants.DISCOUNT_CODE);
                    break;
                case R.id.card6:
                    Intent intent3=new Intent(getActivity(), TaxActivity.class);
                    getInvoiceValues();
                    intent3.putExtra("tax_type",tax_type);
                    intent3.putExtra("tax",tax);

                    startActivityForResult(intent3, Constants.TAX_CODE);
                    break;
                case R.id.card9:
                    Intent intent4=new Intent(getActivity(), DigitalSignatureActivity.class);
                    intent4.putExtra("signature",sign_path);
                    startActivityForResult(intent4, Constants.SIGN_CODE);
                    break;
                case R.id.card11:
                    selectLogoPic();
                    break;
                case R.id.save_invoice:
                {
                    String sign_date="";
                    if(signature_value.getText().toString().contains(":"))
                    {
                        String[] sign=signature_value.getText().toString().split(":");
                        sign_date=sign[1];
                    }
                    db.updateinvoiceCalculation(subtotal_value_field.getText().toString(),total_value.getText().toString(),
                            comment_field.getText().toString(),realPath,sign_path,sign_date,
                            invoice_id);

                    loadFragment(new FragmentReport());
                }
                break;
                case R.id.invoice_info_layout:
                    openInvoiceInfoActivity();
                    break;
            }
    }

    private void loadFragment(Fragment reportFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, reportFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void openInvoiceInfoActivity() {
        Intent intent5=new Intent(getActivity(), InvoiceInfoActivity.class);
        InvoiceModel model=getInvoiceInfo();
        intent5.putExtra("info",model);
        startActivityForResult(intent5,INVOICE_INFO_CODE);
    }

    private InvoiceModel getInvoiceInfo() {
        Cursor rs=db.getInvoiceData(FragmentEditReport.invoice_id);
        rs.moveToFirst();
        InvoiceModel invoiceModel=new InvoiceModel();
        while (!rs.isAfterLast()) {
            String name=rs.getString(rs.getColumnIndex("invoice_name"));
            String due_date=rs.getString(rs.getColumnIndex("due_date"));
            String invoice_date=rs.getString(rs.getColumnIndex("invoice_date"));
            invoiceModel.setInvoice_name(name);
            invoiceModel.setDue_date(due_date);
            invoiceModel.setInvoice_date(invoice_date);

            rs.moveToNext();
        }
        return invoiceModel;
    }

    private void openClientActivity() {
        Intent intent=new Intent(getActivity(), ClientActivity.class);
        if(!db.isClientExist(invoice_id))
        {
            intent.putExtra("is_new", "false");
            startActivityForResult(intent, Constants.CLIENT_CODE);
        }
        else
        {
            Client client=db.getClientInformation(invoice_id);
            intent.putExtra("client_obj", client);
            intent.putExtra("is_new", "true");
            startActivityForResult(intent, Constants.CLIENT_CODE);
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
    @Override
    public void onResume() {
        super.onResume();
        if(!is_new.equals("true"))
        {
            getItemsData();
            if(!subtotal_value_field.getText().toString().equals(""))
                total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                        discount, tax, discount_type,tax_type ) + "");

            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), FilePathUri);

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), FilePathUri);

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), FilePathUri);
                } else {
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        getInvoiceValues();
        if(requestCode==CLIENT_CODE)
        {
            if(ClientActivity.client_info!=null)
                add_client_text.setText("To: "+ClientActivity.client_info.getClientName());
        }
        if (requestCode == DISCOUNT_CODE) {
            db.updateDiscountInInvoice(DiscountActivity.type,String.valueOf(DiscountActivity.discount_amount), FragmentEditReport.invoice_id);
            getInvoiceValues();
            if(DiscountActivity.type.equals("Flat Item")) {
                discount_value.setText(DiscountActivity.discount_amount + "");
                if(!subtotal_value_field.getText().toString().equals(""))
                    total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                            discount, tax, discount_type,tax_type ) + "");
            }
            else if(DiscountActivity.type.equals("percentage"))
            {
                discount_value.setText(DiscountActivity.discount_amount + "%");
                if (DiscountActivity.discount_amount != 0)
                {
                    if(!subtotal_value_field.getText().toString().equals(""))
                        total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                                discount, tax, discount_type,tax_type ) + "");
                }
            }
            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }
        else if(requestCode==TAX_CODE)
        {
            db.updateTaxInInvoice(TaxActivity.type,String.valueOf(TaxActivity.tax_amount), FragmentEditReport.invoice_id);
            getInvoiceValues();
            tax_value.setText(TaxActivity.tax_amount + "");
            if(!subtotal_value_field.getText().toString().equals(""))
                total_value.setText(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                        discount, tax, discount_type,tax_type ) + "");

            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value_field.getText().toString()+"",invoice_id);

        }
        else if(requestCode==ADD_ITEM_CODE)
        {
            subtotal_value_field.setText(subtotal_value+"");
            db.updateInvoice("total_value",total_value.getText().toString(),"subtotal",subtotal_value+"",invoice_id);

        }
        else if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {


            FilePathUri = data.getData();

            //start testing
            // SDK < API11
            if (Util.checkPermissionREAD_EXTERNAL_STORAGE(getActivity())) {
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());

            }

            //close testing

            try {

                // Getting selected image into Bitmap.
                logo_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                image1.setImageBitmap(logo_bitmap);
                db.updateAttachment(logo_bitmap,invoice_id);
                attach_image_label.setVisibility(View.GONE);

            }
            catch (IOException e) {

                e.printStackTrace();
            }

        }
        else if(requestCode==SIGN_CODE)
        {

            if(DigitalSignatureActivity.is_signed)
                signature_value.setText("Signed on: "+ Util.getTodayDate());
        }
        else if(requestCode==INVOICE_INFO_CODE)
        {
            setUpdatedInvoiceInfo();
        }
    }

    private void setUpdatedInvoiceInfo() {
        Cursor rs=db.getInvoiceData(invoice_id);

        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            invoice_name.setText(rs.getString(rs.getColumnIndex("invoice_name")));
            due_date.setText(rs.getString(rs.getColumnIndex("due_date")));
            rs.moveToNext();
        }

    }

    private void getInvoiceValues()
    {
        Cursor rs = db.getInvoiceData(FragmentEditReport.invoice_id);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            if(!rs.getString(rs.getColumnIndex("tax_rate")).equals(""))
                tax = Integer.parseInt(rs.getString(rs.getColumnIndex("tax_rate")));
            tax_type=rs.getString(rs.getColumnIndex("tax_type"));
            if(!rs.getString(rs.getColumnIndex("discount")).equals(""))
                discount=Integer.parseInt(rs.getString(rs.getColumnIndex("discount")));
            discount_type=rs.getString(rs.getColumnIndex("dis_type"));
            sign_path=rs.getString(rs.getColumnIndex("signature"));
            rs.moveToNext();
        }
    }
}
*/
