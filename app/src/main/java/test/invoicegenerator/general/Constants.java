package test.invoicegenerator.general;

import java.util.ArrayList;

import test.invoicegenerator.model.Country;
import test.invoicegenerator.model.StateModel;

public class Constants {
    public static final int RC_SIGN_IN = 1;
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String PHONE_KEY = "phone";
    public static final String COMPANY_NAME = "company_name";
    public static final String PASSWORD = "password";
    public static final String AUTH_ID = "auth_id";
    public static final String Storage_Path = "images/";
    public static final String SIGNATURE_PATH = "signature/";
    public static final String SIGN_UP_COLLECTION = "SignUp";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String CLIENT = "client";
    public static final String UID = "uid";


    public static final String ACCESS_TOKEN_FORGOT_PASSWORD = "access_token_forgot";
    public static final String CLIENT_FORGOT_PASSWORD = "client_forgot";
    public static final String UID_FORGOT_PASSWORD = "uid_forgot";


    public static final int CLIENT_CODE = 1;
    public static final int ADD_ITEM_CODE = 2;
    public static final int DISCOUNT_CODE = 3;
    public static final int TAX_CODE = 4;
    public static final int SIGN_CODE = 5;
    public static final int INVOICE_INFO_CODE = 6;

    public static final int UPDATE_INVOICE_ITEM = 7;

    public static String GENERAL = "General";
    public static String HEADER_FORMAT = "header";
    public static String SELECTED_HEADER = "selected_header";
    public static String SIGNATURE = "signature";
    public static String ADDRESS = "address";
    public static String LOGO = "logo";
    public static String LOGO_COLLECTION = "logo_collection";
    public static String STAMP_COLLECTION = "stamp_collection";
    public static String STAMP = "stamp";
    public static String CITY = "city";
    public static String ZIP_CODE = "zip_code";
    public static String COUNTRY = "country";
    public static String STATE = "state";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 231;
    public static final int Image_Request_Code = 7;
    public static final int Stamp_Image_Request_Code = 6;
    public static final int PICK_IMAGE_REQUEST = 0;
    public static final int PIC_CROP = 8;
    public static ArrayList<StateModel> STATE_LIST;
    public static ArrayList<Country> COUNTRY_LIST;
}
