package test.invoicegenerator.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
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
import test.invoicegenerator.Activities.ActivityAddItem;
import test.invoicegenerator.Activities.ActivityAddItemUpdate;
import test.invoicegenerator.Activities.ClientSelectionActivity;
import test.invoicegenerator.Activities.DigitalSignatureActivity;
import test.invoicegenerator.Activities.DiscountActivity;
import test.invoicegenerator.Activities.InvoiceInfoActivity;
import test.invoicegenerator.Activities.TaxActivity;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ItemAdapter;
import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.GlobalData;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.helper.NonScrollListView;
import test.invoicegenerator.model.GetSingleInvoiceDetailModel;
import test.invoicegenerator.model.GetSingleInvoiceItemDetail;
import test.invoicegenerator.model.SharedPref;
import static test.invoicegenerator.general.Constants.ADD_ITEM_CODE;
import static test.invoicegenerator.general.Constants.CLIENT_CODE;
import static test.invoicegenerator.general.Constants.DISCOUNT_CODE;
import static test.invoicegenerator.general.Constants.INVOICE_INFO_CODE;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static test.invoicegenerator.general.Constants.SIGN_CODE;
import static test.invoicegenerator.general.Constants.TAX_CODE;
import static test.invoicegenerator.general.Constants.UPDATE_INVOICE_ITEM;

public class FragmentEditReportUpdate extends BaseFragment implements View.OnClickListener{

    int totalHeight = 0;
    int adapterCount=0;

  //  public static String InvoiceId_ToBeFetch;
  // public static GetSingleInvoiceDetailModel singleInvoiceDetailModel;

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
    NonScrollListView item_list;
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
    @BindView(R.id.layout_edit_header)
    LinearLayout layout_edit_header;
    public static String StBase64ImageToSave;
    public static String InvoiceDueDate,InvoiceCreateDate,StrSignedBy,StrInvoiceName;
    public static String SelectedClientId,SelectedUserID,SelectedCompanyId,SelectedClientName;
    //public static String StrSignatureImage;
    private int tax,discount=0;
    public static String tax_type,discount_type="";
  // public static String sign_path="";
    String realPath;
    public static String StrImagePath;
    //public static File Signaturefile;
    public static final int REQUEST_IMAGE = 100;
    public static Double subtotal_value=0.0;
    public static ArrayList<Item> item_values=new ArrayList<>();
    Snackbar snackbar;
    public static JSONArray InvoicesArray=new JSONArray();
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    public String InvoiceID;
    ItemAdapter itemsAdapter;
    public static String ImagePath="";
    public static boolean ImageHaseBeenEdited=false;
    public static boolean shouldupdatepreviousvalue=false;
    //public static int selectedInvoicePosition;
    public static int selectedLvPosition;
    public static String SelctedItemID;
    JSONArray Invoice_Items_values;
    public static Bitmap SignatureBitmap;

 //   public static GetSingleInvoiceDetailModel singleInvoiceDetailModel;

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
        Picasso.Builder builder = new Picasso.Builder(getActivity());
        LruCache picassoCache = new LruCache(getActivity());
        builder.memoryCache(picassoCache);
        try {
            Picasso.setSingletonInstance(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        picassoCache.clear();
       // Glide.with(this).load("http://9091abb2.ngrok.io/uploads/invoice/signature/76/signature.jpeg").into(image1);
        FragmentEditReport.IsNewInvoice=false;
        client_card.setOnClickListener(this);
        add_item_card.setOnClickListener(this);
        discount_card.setOnClickListener(this);
        tax_card.setOnClickListener(this);
        sign_card.setOnClickListener(this);
        save_invoice.setOnClickListener(this);
        save_invoice.setText(String.valueOf("Update"));
        attachment_card.setOnClickListener(this);
        layout_edit_header.setOnClickListener(this);

        //        GetSingleInvoiceDetail();
        SetInvoiceAttributesForUpdate(GlobalData.singleInvoiceDetailModel);
        //  singleInvoiceDetailModel=GlobalData.singleInvoiceDetailModel;
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                shouldupdatepreviousvalue=true;
                GlobalData.SelectedInvoiceItem=item_values.get(i);
              //  GlobalData.singleInvoiceDetailModel=
                SelctedItemID=GlobalData.SelectedInvoiceItem.getId();
                /*try {
                    SelectedItemsModel=new GetSingleInvoiceItemDetail(Invoice_Items_values.getJSONObject(i));
                    Toast.makeText(getActivity(), String.valueOf(SelectedItemsModel.getStrId()+","+SelectedItemsModel.getStrInvoiceDescription()), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                selectedLvPosition=i;
                Intent intent1=new Intent(getActivity(), ActivityAddItemUpdate.class);
                startActivityForResult(intent1, UPDATE_INVOICE_ITEM);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch(id)
        {
            case R.id.card2:
                openClientActivity();
                break;
            case R.id.card_item:
                Intent intent1=new Intent(getActivity(), ActivityAddItem.class);
                startActivityForResult(intent1, ADD_ITEM_CODE);
                break;
            case R.id.card5:
                Intent intent2=new Intent(getActivity(), DiscountActivity.class);
                startActivityForResult(intent2, Constants.DISCOUNT_CODE);
                break;
            case R.id.card6:
                Intent intent3=new Intent(getActivity(), TaxActivity.class);
                startActivityForResult(intent3, Constants.TAX_CODE);
                break;
            case R.id.card9:
                Intent intent4=new Intent(getActivity(), DigitalSignatureActivity.class);
                startActivityForResult(intent4, Constants.SIGN_CODE);
                break;
            case R.id.card11:
                selectLogoPic();
                break;
            case R.id.layout_edit_header:
                openInvoiceInfoActivity();
                break;
            case R.id.save_invoice:
            {
                SelectedCompanyId= SharedPref.read(SharedPref.CompanyID,"");
                SelectedUserID=SharedPref.read(SharedPref.CompanyID,"");
                if(InvoicesArray.length()>0){
                    if(isEmptyString(InvoiceDueDate)){
                        showMessage("Invoice Date is Missing");
                    }else if(isEmptyString(comment_field.getText().toString())){
                        showMessage("Add Some Notes");
                    }else if(isEmptyString(InvoiceCreateDate)){
                        showMessage("Invoice Create is Missing");
                    }else if(InvoicesArray.length()<0){
                        showMessage("Invoice Items are empty");
                    }else if(isEmptyString(SelectedCompanyId)){
                        showMessage("Compony Not Found");
                    }else if(isEmptyString(SelectedUserID)){
                        showMessage("User Not Found");
                    }else if(isEmptyString(SelectedClientId)){
                        showMessage("Client Not Found");
                    }else if(isEmptyString(signature_value.getText().toString())){
                        showMessage("Signature Date Not Found");
                    }else if(isEmptyString(StBase64ImageToSave)){
                        showMessage("Signature Image Not Found");
                    }else if(isEmptyString(StrSignedBy)){
                        showMessage("Signed By Name Not Found");
                    }else {
                        final JSONObject InvoiceToBeSend = new JSONObject();
                        final JSONObject js = new JSONObject();
                        try {
                            String SignatureValue=signature_value.getText().toString();
                            String[]SignedDateDummy=SignatureValue.split(":");
                            String SignedDate=SignedDateDummy[1];
                            InvoiceToBeSend.put("signed_by", StrSignedBy);
                            InvoiceToBeSend.put("invoice_number", "1234");
                            InvoiceToBeSend.put("due_at", InvoiceDueDate);
                            InvoiceToBeSend.put("invoiced_on", InvoiceCreateDate);
                            InvoiceToBeSend.put("signed_at", SignedDate);
                            InvoiceToBeSend.put("signature", "data:image/jpeg;base64,"+StBase64ImageToSave);
                            InvoiceToBeSend.put("notes",comment_field.getText().toString());
                            InvoiceToBeSend.put("payment_status", "unpaid");
                            InvoiceToBeSend.put("delivery_status", "draft");
                            InvoiceToBeSend.put("user_id", SelectedUserID);
                            InvoiceToBeSend.put("company_id",SelectedCompanyId);
                            InvoiceToBeSend.put("client_id", SelectedClientId);
                            InvoiceToBeSend.put("invoice_items_attributes", InvoicesArray);
                            js.put("invoice", InvoiceToBeSend);
                            DataSendToServerForUpdateInvoice(js);
                       //     Toast.makeText(getActivity(), "Invoice ID:"+InvoiceID, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else {
                    snackbar = Snackbar.make(layout_edit,"Please Add Some Invoice", Snackbar.LENGTH_LONG);
                    snackbar.show();
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
       // InvoiceModel model=getInvoiceInfo();
        //intent5.putExtra("info",model);
        startActivityForResult(intent5,INVOICE_INFO_CODE);
    }
    private void openClientActivity() {
        Intent intent=new Intent(getActivity(), ClientSelectionActivity.class);
        startActivityForResult(intent, Constants.CLIENT_CODE);
    }
    private void selectLogoPic()
    {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                          //  showImagePickerOptions();
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
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                  //  realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), FilePathUri);
                } else {
                    snackbar = Snackbar.make(layout_edit,"GET_ACCOUNTS Denied", Snackbar.LENGTH_LONG);
                    snackbar.show();
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
        if(requestCode==CLIENT_CODE)
        {
            add_client_text.setText(SelectedClientName);
        }
        if (requestCode == DISCOUNT_CODE) {
            if(DiscountActivity.type.equals("Flat Item")) {
                discount_value.setText(String.valueOf(DiscountActivity.discount_amount + ""));
                if(!subtotal_value_field.getText().toString().equals(""))
                    total_value.setText(String.valueOf(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                            discount, tax, discount_type,tax_type ) + ""));
            }
            else if(DiscountActivity.type.equals("percentage"))
            {
                discount_value.setText(String.valueOf(DiscountActivity.discount_amount + "%"));
                if (DiscountActivity.discount_amount != 0)
                {
                    if(!subtotal_value_field.getText().toString().equals(""))
                        total_value.setText(String.valueOf(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                                discount, tax, discount_type,tax_type ) + ""));
                }
            }

        }
        else if(requestCode==TAX_CODE)
        {
            tax_value.setText(String.valueOf(TaxActivity.tax_amount + ""));
            if(!subtotal_value_field.getText().toString().equals(""))
                total_value.setText(String.valueOf(Util.calculateTotalValue(Integer.parseInt(subtotal_value_field.getText().toString()),
                        discount, tax, discount_type,tax_type ) + ""));
        }
        else if(requestCode==ADD_ITEM_CODE)
        {

            subtotal_value_field.setText(String.valueOf(subtotal_value));
            itemsAdapter=new ItemAdapter(getActivity(),item_values);
            item_list.setAdapter(itemsAdapter);
            SetListViewHeight();


        }else  if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                realPath=String.valueOf(uri);

                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    // loading profile image from local cache
                   // loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else if(requestCode==SIGN_CODE)
        {

            if(DigitalSignatureActivity.is_signed)
                if(DigitalSignatureActivity.isredrawimage){
                    StBase64ImageToSave=ChangeFileToBase64(StrImagePath);

                }

            signature_value.setText(String.valueOf("Signed on: "+ Util.getTodayDate()));
        }
        else if(requestCode==INVOICE_INFO_CODE)
        {
            setUpdatedInvoiceInfo();
        }else if(requestCode==UPDATE_INVOICE_ITEM)
        {
                  Toast.makeText(getActivity(), String.valueOf("Update Invoice Item"), Toast.LENGTH_SHORT).show();
        }


    }

    private void SetListViewHeight() {
        adapterCount=itemsAdapter.getCount();
        totalHeight=0;
        for (int size = 0; size < adapterCount; size++) {
            View listItem = itemsAdapter.getView(size, null, item_list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = item_list.getLayoutParams();
        params.height = (totalHeight
                + (item_list.getDividerHeight() * (adapterCount)));
        item_list.setLayoutParams(params);
    }

    private void setUpdatedInvoiceInfo() {

        invoice_name.setText(StrInvoiceName);
        due_date.setText(InvoiceDueDate);
    }
    /*Convert Base64 To Image*/

           /* Convert To Base64*/
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
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openSettings();
            }
        });
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    public void showMessage(String Str){
        snackbar = Snackbar.make(layout_edit,Str, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
    /*/////////////////////////////////////////////////////////

    public void GetSingleInvoiceDetail()
    {

        showProgressBar();
        initVolleyCallbackForInvoiceList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String Str=NetworkURLs.BaseURL+ NetworkURLs.GetSingleInvoice+FragmentReportDetail.InvoiceId_ToBeFetch+".json";
        mVolleyService.getDataVolley("GETCALL", Str);

    }

    void initVolleyCallbackForInvoiceList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONObject mydata = jsonObject.getJSONObject("data");
                        JSONObject InvoiceModel = mydata.getJSONObject("invoice");
                  //  singleInvoiceDetailModel=new GetSingleInvoiceDetailModel(InvoiceModel);
                        GlobalData.singleInvoiceDetailModel=new GetSingleInvoiceDetailModel(InvoiceModel);
                        SetInvoiceAttributesForUpdate(GlobalData.singleInvoiceDetailModel);
                    }else {
                        Toast.makeText(getActivity(), "Status found false", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();
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
*/

    void  SetInvoiceAttributesForUpdate(GetSingleInvoiceDetailModel getSingleInvoiceDetailModel){

        if(item_values.size()>0){
            item_values.clear();
        }
        if(InvoicesArray.length()>0){
            InvoicesArray=new JSONArray();
        }
        discount_type="percentage";
        InvoiceID=GlobalData.singleInvoiceDetailModel.getId();

        //Toast.makeText(getActivity(), "Invoice Id: "+InvoiceID, Toast.LENGTH_SHORT).show();

        InvoiceDueDate=getSingleInvoiceDetailModel.getDue_at();
        StrSignedBy=getSingleInvoiceDetailModel.getSigned_by();
        InvoiceCreateDate=getSingleInvoiceDetailModel.getInvoiced_on();
        SelectedClientId=getSingleInvoiceDetailModel.getClient_id();
     //   StBase64ImageToSave=getSingleInvoiceDetailModel.getSignature();
        ImagePath=getSingleInvoiceDetailModel.getSignature();
        comment_field.setText(getSingleInvoiceDetailModel.getNotes());
        due_date.setText(InvoiceDueDate);
        add_client_text.setText(getSingleInvoiceDetailModel.getClient_name());
        signature_value.setText(String.valueOf("Signed on: "+ getSingleInvoiceDetailModel.getSigned_at()));
        Invoice_Items_values =getSingleInvoiceDetailModel.getInvoiceItemsArray();
        String ImgUrl=NetworkURLs.BaseURLForImages+ImagePath;
     //   Picasso.get().load(ImgUrl).placeholder(R.color.grey).into(image1);
        Picasso.get()
                .load(ImgUrl)
                .placeholder(R.color.grey) // Your dummy image...
                .into(image1, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    //    Toast.makeText(getActivity(), "Image Loaded", Toast.LENGTH_SHORT).show();
                        try {
                            image1.buildDrawingCache();
                            Bitmap bitmap = image1.getDrawingCache();
                            SignatureBitmap=bitmap;
                            StBase64ImageToSave=getEncoded64ImageStringFromBitmap(bitmap);

                              /*PDFInvoice.getInstance().create_pdf_file();*/

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getActivity(), "Image not Loaded", Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }
                });

 /*   if(hasImage(image1)){
        image1.buildDrawingCache();
        Bitmap bitmap = image1.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);

        if(!TextUtils.isEmpty(StBase64ImageToSave)){
            showMessage("Signature Found");
        }else {
            showMessage("Signature Not Found");
        }
    }*/


      /*  if(hasImage(image1)){
            convertImageToBase64FromImageView(image1);
        }else {
            Toast.makeText(getActivity(), "Image Not found", Toast.LENGTH_SHORT).show();
        }*/

        for (int i = 0; i < Invoice_Items_values.length(); i++) {
            try {
                GetSingleInvoiceItemDetail ItemsModel = new GetSingleInvoiceItemDetail(Invoice_Items_values.getJSONObject(i));
                Item value_item = new Item();
                value_item.setId(ItemsModel.getStrId());
                value_item.setAdditional(ItemsModel.getStrInvoiceDescription());
                value_item.setAmount(ItemsModel.getStrInvoicePrice());
                value_item.setQuantity(ItemsModel.getStrInvoiceQty());
                value_item.setTax_rate(ItemsModel.getStr_subtotal_with_tax_applied());
                value_item.setTaxable("true");
                value_item.setUnit_cost(ItemsModel.getStrInvoicePrice());
                value_item.setDescription(ItemsModel.getStrInvoiceDescription());
                item_values.add(value_item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        itemsAdapter=new ItemAdapter(getActivity(),item_values);
        item_list.setAdapter(itemsAdapter);
        SetListViewHeight();
        JSONObject InvoiceItem = new JSONObject();
        for (int i=0;i<item_values.size();i++){
            try {
                InvoiceItem.put("id",item_values.get(i).getId());
                InvoiceItem.put("name", item_values.get(i).getAdditional());
                InvoiceItem.put("description", item_values.get(i).getDescription());
                InvoiceItem.put("qty",  item_values.get(i).getQuantity());
                InvoiceItem.put("price", item_values.get(i).getUnit_cost());
                InvoiceItem.put("subtotal", item_values.get(i).getAmount());
                InvoiceItem.put("subtotal_with_tax_applied", "0.0");
                // InvoiceItem.put("tax_code_id","1");
                InvoiceItem.put("company_id", String.valueOf(SharedPref.read(SharedPref.CompanyID, "")));
                InvoicesArray.put(InvoiceItem);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
         //   Toast.makeText(getActivity(), String.valueOf(item_values.size()), Toast.LENGTH_SHORT).show();
    }
                        /*Volley Request For Update*/

    void DataSendToServerForUpdateInvoice(JSONObject Da)
    {
        showProgressBar();

        initVolleyCallbackForAddClient();
        mVolleyService = new VolleyService(mResultCallback,getActivity());
        String Str=NetworkURLs.BaseURL + NetworkURLs.GetSingleInvoice+InvoiceID+".json";
        mVolleyService.putDataVolleyForHeadersWithJson("POSTCALL",Str ,Da );
    }

    void initVolleyCallbackForAddClient(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectNew =jsonObject.getJSONObject("data");
                    boolean status = jsonObjectNew.getBoolean("status");


                    if(status)
                    {


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loadFragment(new FragmentReport(),null);

                            }
                        }, 1000);

                        snackbar = Snackbar.make(layout_edit,"Updated Successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {

                        String error = jsonObject.getString("Error");
                        snackbar = Snackbar.make(layout_edit,error, Snackbar.LENGTH_LONG);
                        snackbar.show();
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
                //    Toast.makeText(getActivity(), String.valueOf("Error" + error_response), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(error_response);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
                            AlertDialog.Builder builders=new AlertDialog.Builder(getActivity());
                            builders.setCancelable(false);
                            builders.setMessage(error_response);
                            builders.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builders.create().show();
                            snackbar = Snackbar.make(layout_edit,"Error" + message, Snackbar.LENGTH_LONG);
                            snackbar.show();
                         //   Toast.makeText(getActivity(), String.valueOf("Error" + message), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), String.valueOf("Server not responding" ), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {
            }
        };
    }

    public void convertImageToBase64FromImageView(ImageView imageView){
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        //  System.out.println("byte array:"+image);
        StBase64ImageToSave= Base64.encodeToString(image, 0);
        Toast.makeText(getActivity(), StBase64ImageToSave, Toast.LENGTH_SHORT).show();
    }

    public boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
}

