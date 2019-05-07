package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientSelectModel {

    String id;
    String name;
    String email;
    String phone;
    Boolean Select;


    public ClientSelectModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            phone = jsonObject.getString("phone");



            setId(id);
            setName(name);
            setEmail(email);
            setPhone(phone);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Boolean getSelect() {
        return Select;
    }

    public void setSelect(Boolean select) {
        Select = select;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
