package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 11/12/2018.
 */

public class HeaderDetail {
    private int ObjID;
    private String StrCompanyLogo;
    private String Str_Max_Width;
    private String Str_Max_Height;
    private String Str_ImgeHeight;
    private String Str_ImgeWidth;
    private int position;
    private boolean IsActive;
    private String StrCompanyID;




    public HeaderDetail(JSONObject jsonObject)
    {
        try {

            ObjID = jsonObject.getInt("id");
            StrCompanyLogo = jsonObject.getString("logo");
            Str_Max_Width = jsonObject.getString("max_width");
            Str_Max_Height = jsonObject.getString("max_height");
            Str_ImgeHeight = jsonObject.getString("image_width");
            Str_ImgeWidth = jsonObject.getString("image_height");
            position = jsonObject.getInt("position");
            IsActive=jsonObject.getBoolean("active");
            StrCompanyID = jsonObject.getString("company_id");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getObjID() {
        return ObjID;
    }

    public void setObjID(int objID) {
        ObjID = objID;
    }

    public String getStrCompanyLogo() {
        return StrCompanyLogo;
    }

    public void setStrCompanyLogo(String strCompanyLogo) {
        StrCompanyLogo = strCompanyLogo;
    }

    public String getStr_Max_Width() {
        return Str_Max_Width;
    }

    public void setStr_Max_Width(String str_Max_Width) {
        Str_Max_Width = str_Max_Width;
    }

    public String getStr_Max_Height() {
        return Str_Max_Height;
    }

    public void setStr_Max_Height(String str_Max_Height) {
        Str_Max_Height = str_Max_Height;
    }

    public String getStr_ImgeHeight() {
        return Str_ImgeHeight;
    }

    public void setStr_ImgeHeight(String str_ImgeHeight) {
        Str_ImgeHeight = str_ImgeHeight;
    }

    public String getStr_ImgeWidth() {
        return Str_ImgeWidth;
    }

    public void setStr_ImgeWidth(String str_ImgeWidth) {
        Str_ImgeWidth = str_ImgeWidth;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getStrCompanyID() {
        return StrCompanyID;
    }

    public void setStrCompanyID(String strCompanyID) {
        StrCompanyID = strCompanyID;
    }


}
