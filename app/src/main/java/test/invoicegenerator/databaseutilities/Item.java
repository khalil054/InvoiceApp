package test.invoicegenerator.databaseutilities;

public class Item {
    private String description;
    private String unit_cost;
    private String quantity;
    private String amount;
    private String tax_rate;
    private String additional;
    private String id;
    private String taxable;
    private String tax_Id;

    public String getTax_Id() {
        return tax_Id;
    }

    public void setTax_Id(String tax_Id) {
        this.tax_Id = tax_Id;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(String unit_cost) {
        this.unit_cost = unit_cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
    /*public static void insert_item(String name, String email, String phone, String mob, String fax, String contact, String line1, String line2, String line3)
    {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Item client = realm.createObject(Item.class);
        client.setEmail(email);
        client.setMobile(mob);
        client.setPhone(phone);
        client.setFax(fax);
        client.setContact(contact);
        client.setLine1(line1);
        client.setLine2(line2);
        client.setLine3(line3);

        realm.commitTransaction();
    }
    public static RealmResults<Item> get_all_clients() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Item> list = realm.where(Item.class).findAll();
        return list;
    }*/
}

