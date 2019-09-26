package test.invoicegenerator.model;

import java.io.Serializable;

public class Country implements Serializable {
    private String name, id, sortname;

    public Country(String Name, String sortName, String Id) {
        this.name = Name;
        this.sortname = sortName;
        this.id = Id;
    }

    public Country() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    @Override
    public String toString() {
        return "Country [name=" + name + ", sortname=" + sortname
                + ", id=" + id + "]";
    }

}
