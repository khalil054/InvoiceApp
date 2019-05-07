package test.invoicegenerator.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientWithAddressModel {

    String id;
    String name;
    String email;
    String phone;

    ArrayList<AddressModel> addressModels=new ArrayList<AddressModel>();
    JSONArray addresses;


    public ClientWithAddressModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            phone = jsonObject.getString("phone");
            addresses = jsonObject.getJSONArray("addresses");

            for (int i = 0; i < addresses.length(); i++) {
                AddressModel addressModel = new AddressModel(addresses.getJSONObject(i));
                addressModels.add(addressModel);
            }


            setId(id);
            setName(name);
            setEmail(email);
            setPhone(phone);
            setAddressModels(addressModels);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<AddressModel> getAddressModels() {
        return addressModels;
    }

    public void setAddressModels(ArrayList<AddressModel> addressModels) {
        this.addressModels = addressModels;
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
