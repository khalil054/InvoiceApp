package test.invoicegenerator.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import test.invoicegenerator.Activities.ActivityAddItem;
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
import test.invoicegenerator.general.Util;
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


public class FragmentEditReportUpdate extends BaseFragment implements View.OnClickListener{

    public static String InvoiceId_ToBeFetch;
    GetSingleInvoiceDetailModel singleInvoiceDetailModel;
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
    @BindView(R.id.layout_edit_header)
    LinearLayout layout_edit_header;
    public static String StBase64ImageToSave;
    public static String InvoiceDueDate,InvoiceCreateDate,StrSignedBy,StrInvoiceName;
    public static String SelectedClientId,SelectedUserID,SelectedCompanyId,SelectedClientName;
    private int tax,discount=0;
    private String tax_type,discount_type="";
    public static String sign_path="";
    String realPath;
    public static String StrImagePath;
    public static File Signaturefile;
    public static final int REQUEST_IMAGE = 100;
    public static int subtotal_value=0;
    public static ArrayList<Item> item_values=new ArrayList<>();
    Snackbar snackbar;
    public static JSONArray InvoicesArray=new JSONArray();
    IResult mResultCallback = null;
    VolleyService mVolleyService;

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
        FragmentEditReport.IsNewInvoice=false;
        client_card.setOnClickListener(this);
        add_item_card.setOnClickListener(this);
        discount_card.setOnClickListener(this);
        tax_card.setOnClickListener(this);
        sign_card.setOnClickListener(this);
        save_invoice.setOnClickListener(this);
        attachment_card.setOnClickListener(this);
        layout_edit_header.setOnClickListener(this);

        GetSingleInvoiceDetail();
       // setInvoiceAndTaxValues();
    }




    /*private void setInvoiceAndTaxValues() {
       // getInvoiceValues();
        if(discount_type.equals("percentage"))
            discount_value.setText(discount+"%");
        else
            discount_value.setText(discount+"");
        tax_value.setText(tax+"");
    }*/

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
                    Toast.makeText(getActivity(), String.valueOf(InvoicesArray), Toast.LENGTH_SHORT).show();
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
                    }else if(isEmptyString(StrImagePath)){
                        showMessage("Signature Image Not Found");
                    }else if(isEmptyString(StrSignedBy)){
                        showMessage("Signed By Name Not Found");
                    }else {
                        final JSONObject InvoiceToBeSend = new JSONObject();
                        final JSONObject js = new JSONObject();
                        try {

                            //  Toast.makeText(getActivity(), StBase64ImageToSave, Toast.LENGTH_SHORT).show();
                            String SignatureValue=signature_value.getText().toString();
                            String[]SignedDateDummy=SignatureValue.split(":");
                            String SignedDate=SignedDateDummy[1];
                            Toast.makeText(getActivity(), String.valueOf(SignedDate), Toast.LENGTH_SHORT).show();
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

                            DataSendToServerForUpdateInvoice(js);


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
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                  //  realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), FilePathUri);
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
            Toast.makeText(getActivity(), String.valueOf(item_values.size()), Toast.LENGTH_SHORT).show();
            subtotal_value_field.setText(subtotal_value+"");
            ItemAdapter itemsAdapter=new ItemAdapter(getActivity(),item_values);
            item_list.setAdapter(itemsAdapter);

        }else  if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                realPath=String.valueOf(uri);
                Toast.makeText(getActivity(), String.valueOf(uri), Toast.LENGTH_SHORT).show();
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
                StBase64ImageToSave=ChangeFileToBase64(StrImagePath);
            signature_value.setText(String.valueOf("Signed on: "+ Util.getTodayDate()));
        }
        else if(requestCode==INVOICE_INFO_CODE)
        {
            setUpdatedInvoiceInfo();
        }


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
    /////////////////////////////////////////////////////////

    public void GetSingleInvoiceDetail()
    {

        showProgressBar();
        initVolleyCallbackForInvoiceList();
        mVolleyService = new VolleyService(mResultCallback, getActivity());
        String Str=NetworkURLs.BaseURL+ NetworkURLs.GetSingleInvoice+InvoiceId_ToBeFetch+".json";
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
                    singleInvoiceDetailModel=new GetSingleInvoiceDetailModel(InvoiceModel);

                        SetInvoiceAttributesForUpdate(singleInvoiceDetailModel);
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


    void  SetInvoiceAttributesForUpdate(GetSingleInvoiceDetailModel getSingleInvoiceDetailModel){

        InvoiceDueDate=getSingleInvoiceDetailModel.getDue_at();
        StrSignedBy=getSingleInvoiceDetailModel.getSigned_by();
        InvoiceCreateDate=getSingleInvoiceDetailModel.getInvoiced_on();
        SelectedClientId=getSingleInvoiceDetailModel.getClient_id();
        StBase64ImageToSave=getSingleInvoiceDetailModel.getSignature();
        comment_field.setText(getSingleInvoiceDetailModel.getNotes());
        due_date.setText(InvoiceDueDate);
        add_client_text.setText(SelectedClientId);
        signature_value.setText(String.valueOf("Signed on: "+ getSingleInvoiceDetailModel.getSigned_at()));
        JSONArray Invoice_Items_values=getSingleInvoiceDetailModel.getInvoiceItemsArray();

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
                value_item.setUnit_cost("not get from server");
                value_item.setDescription(ItemsModel.getStrInvoiceDescription());
                item_values.add(value_item);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ItemAdapter itemsAdapter=new ItemAdapter(getActivity(),item_values);
        item_list.setAdapter(itemsAdapter);
    }


                        /*Volley Request For Update*/

    void DataSendToServerForUpdateInvoice(JSONObject Da)
    {
        showProgressBar();

        initVolleyCallbackForAddClient();
        mVolleyService = new VolleyService(mResultCallback,getActivity());

        mVolleyService.postDataVolleyForHeadersWithJson("POSTCALL", NetworkURLs.BaseURL + NetworkURLs.AddInvoices,Da );
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
}

