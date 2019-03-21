package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressModel {


        String id;
        String name;
        String line_1;
        String city;
        String state;
        String zip_code;
        Boolean Defult;
        String country_name;


    public AddressModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            line_1 = jsonObject.getString("line_1");
            city = jsonObject.getString("city");
            state = jsonObject.getString("state");
            zip_code = jsonObject.getString("zip_code");
            Defult = jsonObject.getBoolean("defult");
            country_name = jsonObject.getString("country_name");


            setId(id);
            setName(name);
            setLine_1(line_1);
            setCity(city);
            setState(state);
            setZip_code(zip_code);
            setDefult(Defult);
            setCountry_name(country_name);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine_1() {
        return line_1;
    }

    public void setLine_1(String line_1) {
        this.line_1 = line_1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public Boolean getDefult() {
        return Defult;
    }

    public void setDefult(Boolean defult) {
        Defult = defult;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
