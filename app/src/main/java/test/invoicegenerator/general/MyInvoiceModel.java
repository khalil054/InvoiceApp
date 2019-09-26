package test.invoicegenerator.general;


import org.json.JSONArray;

public class MyInvoiceModel {
    private String StrSignedBy;
    private String StrInvoice_number;
    private String StrDue_at;
    private String StrInvoiced_on;
    private String StrSigned_at;
    private String StrNotes;
    private String StrPayment_status;
    private String StrDelivery_status;
    private String StrUser_id;
    private String StrCompany_id;
    private String StrClient_id;
    private JSONArray InvoicejsonArray;


    public JSONArray getJsonArray() {
        return InvoicejsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.InvoicejsonArray = jsonArray;
    }

    public String getStrSignedBy() {
        return StrSignedBy;
    }

    public void setStrSignedBy(String strSignedBy) {
        StrSignedBy = strSignedBy;
    }

    public String getStrInvoice_number() {
        return StrInvoice_number;
    }

    public void setStrInvoice_number(String strInvoice_number) {
        StrInvoice_number = strInvoice_number;
    }

    public String getStrDue_at() {
        return StrDue_at;
    }

    public void setStrDue_at(String strDue_at) {
        StrDue_at = strDue_at;
    }

    public String getStrInvoiced_on() {
        return StrInvoiced_on;
    }

    public void setStrInvoiced_on(String strInvoiced_on) {
        StrInvoiced_on = strInvoiced_on;
    }

    public String getStrSigned_at() {
        return StrSigned_at;
    }

    public void setStrSigned_at(String strSigned_at) {
        StrSigned_at = strSigned_at;
    }

    public String getStrNotes() {
        return StrNotes;
    }

    public void setStrNotes(String strNotes) {
        StrNotes = strNotes;
    }

    public String getStrPayment_status() {
        return StrPayment_status;
    }

    public void setStrPayment_status(String strPayment_status) {
        StrPayment_status = strPayment_status;
    }

    public String getStrDelivery_status() {
        return StrDelivery_status;
    }

    public void setStrDelivery_status(String strDelivery_status) {
        StrDelivery_status = strDelivery_status;
    }

    public String getStrUser_id() {
        return StrUser_id;
    }

    public void setStrUser_id(String strUser_id) {
        StrUser_id = strUser_id;
    }

    public String getStrCompany_id() {
        return StrCompany_id;
    }

    public void setStrCompany_id(String strCompany_idy) {
        StrCompany_id = strCompany_idy;
    }

    public String getStrClient_id() {
        return StrClient_id;
    }

    public void setStrClient_id(String strClient_id) {
        StrClient_id = strClient_id;
    }

    public MyInvoiceModel(String strSignedBy, String strInvoice_number, String strDue_at, String strInvoiced_on, String strSigned_at, String strNotes, String strPayment_status, String strDelivery_status, String strUser_id, String strCompany_idy, String strClient_id, JSONArray jr) {
        StrSignedBy = strSignedBy;
        StrInvoice_number = strInvoice_number;
        StrDue_at = strDue_at;
        StrInvoiced_on = strInvoiced_on;
        StrSigned_at = strSigned_at;
        StrNotes = strNotes;
        StrPayment_status = strPayment_status;
        StrDelivery_status = strDelivery_status;
        StrUser_id = strUser_id;
        StrCompany_id = strCompany_idy;
        StrClient_id = strClient_id;
        InvoicejsonArray = jr;

        setStrSignedBy(StrSignedBy);
        setStrInvoice_number(StrInvoice_number);
        setStrDue_at(StrDue_at);
        setStrInvoiced_on(StrInvoiced_on);
        setStrSigned_at(strSigned_at);
        setStrNotes(StrNotes);
        setStrPayment_status(strPayment_status);
        setStrDelivery_status(StrDelivery_status);
        setStrUser_id(StrUser_id);
        setStrCompany_id(strCompany_idy);
        setStrClient_id(StrClient_id);
        setJsonArray(InvoicejsonArray);
    }
}
