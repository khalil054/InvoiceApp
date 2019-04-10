package test.invoicegenerator.general;

import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.model.AddressModel;
import test.invoicegenerator.model.GetSingleInvoiceDetailModel;
import test.invoicegenerator.model.JsonInvoiceModel;

public class GlobalData {
    public static String clientId;
    public static AddressModel addressModel;
    public static JsonInvoiceModel invoiceModel;
    public static GetSingleInvoiceDetailModel singleInvoiceDetailModel;
    public static Item SelectedInvoiceItem;
}
