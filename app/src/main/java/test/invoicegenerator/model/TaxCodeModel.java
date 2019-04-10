package test.invoicegenerator.model;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaxCodeModel {

    String id;
    String code;
    String des;
    JSONArray company_taxes;
    ArrayList<TaxModel> taxModels = new ArrayList<TaxModel>();

    public TaxCodeModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            code = jsonObject.getString("code");
            des = jsonObject.getString("description");
            company_taxes = jsonObject.getJSONArray("company_taxes");

            for (int i = 0; i<= company_taxes.length(); i++)
            {
                TaxModel taxModel = new TaxModel(company_taxes.getJSONObject(i));
                taxModels.add(taxModel);
            }

            setTaxModels(taxModels);
            setId(id);
            setCode(code);
            setDes(des);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<TaxModel> getTaxModels() {
        return taxModels;
    }

    public void setTaxModels(ArrayList<TaxModel> taxModels) {
        this.taxModels = taxModels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
