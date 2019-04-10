package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TaxModel {

    String id;
    String name;
    String description;
    Double percent;
    Boolean active;
    String agency_name;



    public TaxModel(JSONObject jsonObject) {

        try {

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            description = jsonObject.getString("description");
            percent = jsonObject.getDouble("percent");
            active = jsonObject.getBoolean("active");
            agency_name = jsonObject.getString("agency_name");


            setId(id);
            setName(name);
            setDescription(description);
            setPercent(percent);
            setActive(active);
            setAgency_name(agency_name);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
