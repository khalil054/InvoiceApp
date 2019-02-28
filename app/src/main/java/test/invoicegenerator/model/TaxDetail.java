package test.invoicegenerator.model;

/**
 * Created by User on 10/11/2018.
 */

public class TaxDetail {
    private String tax_detail_id, agency_name, tax_name, description, account_number, status, country;
    public TaxDetail(String tax_detail_id,String agency, String t_name, String des, String account_no, String status, String country)
    {
        this.tax_detail_id=tax_detail_id;
        this.agency_name=agency;
        this.tax_name=t_name;
        this.description=des;
        this.account_number=account_no;
        this.status=status;
        this.country=country;
    }

    public String getTax_detail_id() {
        return tax_detail_id;
    }

    public void setTax_detail_id(String tax_detail_id) {
        this.tax_detail_id = tax_detail_id;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
