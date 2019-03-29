package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

import test.invoicegenerator.databaseutilities.Item;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;

public class GetSingleInvoiceItemDetail {


    private String StrId;
    private String StrInvoiceName;
    private String StrInvoiceDescription;
    private String StrInvoiceQty;
    private String StrInvoicePrice;
    private String StrInvoiceSubTotal;
    private String Str_subtotal_with_tax_applied;
    private String StrInvoiceCreatedDate;
    private String StrInvoiceDueDate;
    private String StrInvoiceTaxCode_Id;
    private String StrInvoiceID;
    private String StrCompanyID;

    public String getStrInvoiceId() {
        return StrInvoiceID;
    }

    public void setStrInvoiceId(String strInvoiceId) {
        StrInvoiceID = strInvoiceId;
    }

    public String getStrInvoiceName() {
        return StrInvoiceName;
    }

    public void setStrInvoiceName(String strInvoiceName) {
        StrInvoiceName = strInvoiceName;
    }

    public String getStrInvoiceDescription() {
        return StrInvoiceDescription;
    }

    public void setStrInvoiceDescription(String strInvoiceDescription) {
        StrInvoiceDescription = strInvoiceDescription;
    }

    public String getStrInvoiceQty() {
        return StrInvoiceQty;
    }

    public void setStrInvoiceQty(String strInvoiceQty) {
        StrInvoiceQty = strInvoiceQty;
    }

    public String getStrInvoicePrice() {
        return StrInvoicePrice;
    }

    public void setStrInvoicePrice(String strInvoicePrice) {
        StrInvoicePrice = strInvoicePrice;
    }

    public String getStrInvoiceSubTotal() {
        return StrInvoiceSubTotal;
    }

    public void setStrInvoiceSubTotal(String strInvoiceSubTotal) {
        StrInvoiceSubTotal = strInvoiceSubTotal;
    }
    public String getStrId() {
        return StrId;
    }

    public void setStrId(String strId) {
        StrId = strId;
    }

    public String getStr_subtotal_with_tax_applied() {
        return Str_subtotal_with_tax_applied;
    }

    public void setStr_subtotal_with_tax_applied(String str_subtotal_with_tax_applied) {
        Str_subtotal_with_tax_applied = str_subtotal_with_tax_applied;
    }

    public String getStrInvoiceCreatedDate() {
        return StrInvoiceCreatedDate;
    }

    public void setStrInvoiceCreatedDate(String strInvoiceCreatedDate) {
        StrInvoiceCreatedDate = strInvoiceCreatedDate;
    }

    public String getStrInvoiceDueDate() {
        return StrInvoiceDueDate;
    }

    public void setStrInvoiceDueDate(String strInvoiceDueDate) {
        StrInvoiceDueDate = strInvoiceDueDate;
    }

    public String getStrInvoiceTaxCode_Id() {
        return StrInvoiceTaxCode_Id;
    }

    public void setStrInvoiceTaxCode_Id(String strInvoiceTaxCode_Id) {
        StrInvoiceTaxCode_Id = strInvoiceTaxCode_Id;
    }

    public String getStrInvoiceID() {
        return StrInvoiceID;
    }

    public void setStrInvoiceID(String strInvoiceID) {
        StrInvoiceID = strInvoiceID;
    }

    public String getStrCompanyID() {
        return StrCompanyID;
    }

    public void setStrCompanyID(String strCompanyID) {
        StrCompanyID = strCompanyID;
    }

    public GetSingleInvoiceItemDetail(JSONObject js) {

        try {

            StrId = js.getString("id");
            StrInvoiceName = js.getString("name");
            StrInvoiceDescription = js.getString("description");
            StrInvoiceQty = js.getString("qty");
            StrInvoicePrice = js.getString("price");
            StrInvoiceSubTotal = js.getString("subtotal");
            Str_subtotal_with_tax_applied = js.getString("subtotal_with_tax_applied");
            StrInvoiceCreatedDate = js.getString("created_at");
            StrInvoiceDueDate = js.getString("updated_at");
            StrInvoiceTaxCode_Id = js.getString("tax_code_id");
            StrInvoiceID = js.getString("invoice_id");
            StrCompanyID = js.getString("company_id");


            setStrId(StrId);
            setStrInvoiceName(StrInvoiceName);
            setStrInvoiceDescription(StrInvoiceDescription);
            setStrInvoiceQty(StrInvoiceQty);
            setStrInvoicePrice(StrInvoicePrice);
            setStrInvoiceSubTotal(StrInvoiceSubTotal);
            setStr_subtotal_with_tax_applied(Str_subtotal_with_tax_applied);
            setStrInvoiceCreatedDate(StrInvoiceCreatedDate);
            setStrInvoiceDueDate(StrInvoiceDueDate);
            setStrInvoiceTaxCode_Id(StrInvoiceTaxCode_Id);
            setStrCompanyID(StrCompanyID);
            setStrInvoiceId(StrInvoiceID);





        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
