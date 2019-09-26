package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonInvoiceModel {
    String id;
    private String signed_by;
    private String invoice_number;
    private String due_at;
    private String invoiced_on;
    private String signed_at;
    private String signature;
    private String notes;
    private String payment_status;
    private String delivery_status;
    private String created_at;
    private String updated_at;
    private String user_id;
    private String company_id;
    private String client_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSigned_by() {
        return signed_by;
    }

    public void setSigned_by(String signed_by) {
        this.signed_by = signed_by;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getDue_at() {
        return due_at;
    }

    public void setDue_at(String due_at) {
        this.due_at = due_at;
    }

    public String getInvoiced_on() {
        return invoiced_on;
    }

    public void setInvoiced_on(String invoiced_on) {
        this.invoiced_on = invoiced_on;
    }

    public String getSigned_at() {
        return signed_at;
    }

    public void setSigned_at(String signed_at) {
        this.signed_at = signed_at;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public JsonInvoiceModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            signed_by = jsonObject.getString("signed_by");
            invoice_number = jsonObject.getString("invoice_number");
            due_at = jsonObject.getString("due_at");
            invoiced_on = jsonObject.getString("invoiced_on");
            signed_at = jsonObject.getString("signed_at");
            signature = jsonObject.getString("signature");
            notes = jsonObject.getString("notes");
            payment_status = jsonObject.getString("payment_status");
            delivery_status = jsonObject.getString("delivery_status");
            created_at = jsonObject.getString("created_at");
            updated_at = jsonObject.getString("updated_at");
            user_id = jsonObject.getString("user_id");
            company_id = jsonObject.getString("company_id");
            client_id = jsonObject.getString("client_id");

            setId(id);
            setSigned_by(signed_by);
            setInvoice_number(invoice_number);
            setDue_at(due_at);
            setInvoiced_on(invoiced_on);
            setSigned_at(signed_at);
            setSignature(signature);
            setNotes(notes);
            setPayment_status(payment_status);
            setDelivery_status(delivery_status);
            setCreated_at(created_at);
            setUpdated_at(updated_at);
            setUser_id(user_id);
            setCompany_id(company_id);
            setClient_id(client_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
