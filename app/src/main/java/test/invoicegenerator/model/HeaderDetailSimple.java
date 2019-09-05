package test.invoicegenerator.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 11/12/2018.
 */

public class HeaderDetailSimple {


    /*,company_name*/
    private String CompanyName, Company_Email, Company_Phone_no;
    public HeaderDetailSimple(String n,String mail,String phone,String c_name)
    {
      /*  this.name=n;*/
        this.Company_Email=mail;
        this.Company_Phone_no=phone;
        CompanyName=c_name;
    }




    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompany_Email() {
        return Company_Email;
    }

    public void setCompany_Email(String company_Email) {
        Company_Email = company_Email;
    }

    public String getCompany_Phone_no() {
        return Company_Phone_no;
    }

    public void setCompany_Phone_no(String company_Phone_no) {
        Company_Phone_no = company_Phone_no;
    }
}
