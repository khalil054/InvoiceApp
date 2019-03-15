package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientModel {

    String id;
    String name;
    String email;
    String address;
    String phone;


    public ClientModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            address = jsonObject.getString("address");
            phone = jsonObject.getString("phone");


            setId(id);
            setName(name);
            setEmail(email);
            setAddress(address);
            setPhone(phone);


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String company) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
