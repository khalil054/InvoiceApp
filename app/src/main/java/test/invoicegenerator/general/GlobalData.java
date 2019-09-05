package test.invoicegenerator.general;

import java.util.ArrayList;

import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.model.AddressModel;
import test.invoicegenerator.model.GetSingleInvoiceDetailModel;
import test.invoicegenerator.model.JsonInvoiceModel;
import test.invoicegenerator.model.TaxCodeModel;
import test.invoicegenerator.model.TaxModel;

public class GlobalData {
    public static String clientId;
    public static AddressModel addressModel;
    public static JsonInvoiceModel invoiceModel;
    public static TaxModel taxModel;
    public static TaxCodeModel taxCodeModel;
    public static ArrayList<TaxModel> taxModels = new ArrayList<TaxModel>();
    public static ArrayList<TaxCodeModel> taxCodeModels = new ArrayList<TaxCodeModel>();
    public static GetSingleInvoiceDetailModel singleInvoiceDetailModel;
    public static Item SelectedInvoiceItem;

    public static  String Text_Code_ID="";


    public static String StrCompanyName;
    public static String StrCompanyEmail;
    public static String StrCompanyPhone;
    public static String StrCompanyAddress;
    public static String StrCompanyCity;
    public static String StrCompanyState;
    public static String StrCompanyCountry;
    public static String StrCompanyZipCode;
    public static String StrCompanyLogo;
    public static String StrCompanyLogoUrl;
    public static String StrCompanyHeader;
    public static String StrCompanyStamp;
    public static String StrCompanyStampUrl;
    public static String StrUserSignature;
    public static String StrUserProfile;
    public static String StrUserProfileUrl;
    public static String StrCountryID;
    public  static  String StrHeaderLogo;
    public  static  String StrHeaderLogoUrl;

}
