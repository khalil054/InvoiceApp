package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Country_Model {
    String StrCountryID;
    String StrCountryName;
    boolean ischecked=false;

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getStrCountryID() {
        return StrCountryID;
    }

    public void setStrCountryID(String strCountryID) {
        StrCountryID = strCountryID;
    }

    public String getStrCountryName() {
        return StrCountryName;
    }

    public void setStrCountryName(String strCountryName) {
        StrCountryName = strCountryName;
    }

    public Country_Model(JSONObject jsonObject) {

        try {

            StrCountryID = jsonObject.getString("id");
            StrCountryName = jsonObject.getString("name");
            ischecked=false;
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
