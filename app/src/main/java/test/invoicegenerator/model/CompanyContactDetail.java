package test.invoicegenerator.model;

/**
 * Created by User on 10/10/2018.
 */

public class CompanyContactDetail {
    private String name, email, phone_no;
    public CompanyContactDetail(String n,String mail,String phone)
    {
        this.name=n;
        this.email=mail;
        this.phone_no=phone;
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
}
