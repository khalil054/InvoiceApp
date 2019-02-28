package test.invoicegenerator.model;

/**
 * Created by User on 11/12/2018.
 */

public class HeaderDetail {
    private String name, email, phone_no,company_name;
    public HeaderDetail(String n,String mail,String phone,String c_name)
    {
        this.name=n;
        this.email=mail;
        this.phone_no=phone;
        company_name=c_name;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAddressDetail() {
        return company_name;
    }

    public void setAddressDetail(String company_name) {
        this.company_name = company_name;
    }
}
