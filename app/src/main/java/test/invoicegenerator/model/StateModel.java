package test.invoicegenerator.model;

/**
 * Created by User on 10/30/2018.
 */

public class StateModel {
    private String id,name,country_id;
    public StateModel(String N, String con,String Id) {
        this.name = N;
        this.country_id = con;
        this.id = Id;
    }
    public StateModel()
    {

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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
    @Override
    public String toString() {
        return "State [name=" + name + ", id=" + id
                + ", country_id=" +country_id+"]";
    }
}
