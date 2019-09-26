package test.invoicegenerator.model;


public class User {
    private String name,email,company_name,password,user_id;
    public User(String id,String n,String e,String c,String p)
    {
        this.name=n;
        this.email=e;
        this.company_name=c;
        this.password=p;
        user_id=id;
    }
public User()
{

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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
