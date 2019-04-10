package test.invoicegenerator.general;

import java.util.ArrayList;

import test.invoicegenerator.model.AddressModel;
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
}
