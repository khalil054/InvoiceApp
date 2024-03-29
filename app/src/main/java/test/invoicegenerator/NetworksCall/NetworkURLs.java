package test.invoicegenerator.NetworksCall;


public class NetworkURLs {

    public static String BaseURL = "http://et-invoice.herokuapp.com/api/v1/";
    public static String BaseURLForImages = "http://et-invoice.herokuapp.com";

    // public static String BaseURL = "http://7188322f.ngrok.io/api/v1/";
    //public static String BaseURLForImages = " http://7188322f.ngrok.io";

    public static String SignUp = "auth.json";
    public static String SignIn = "auth/sign_in.json";
    public static String SignOut = "auth/sign_out.json";
    public static String ChangePassword = "change-password.json";
    public static String AddInvoices = "invoices.json";
    public static String ForgotPassword = "auth/password.json";
    public static String Confirm_User = "confirm.json";
    public static String VerifiyCode = "verify.json";
    public static String password = "auth/password.json";
    public static String GetClientList = "companies/list_clients.json";
    public static String AddClient = "clients.json";
    public static String DeleteClient = "clients/";
    public static String UpdateClient = "clients/";
    public static String UpdateAddress = "/addresses/";
    public static String DeleteAddress = "/addresses/";
    public static String AddeAddress = "/addresses";
    public static String GetClientDetails = "clients/";
    public static String DeleteInvoice = "invoices/";
    public static String GetInvoiceList = "invoices.json";
    public static String GetSingleInvoice = "invoices/";
    public static String GetTaxesList = "tax_codes.json";
    public static String DeleteTax = "company_taxes/";
    public static String DeleteTaxCode = "tax_codes/";
    public static String AddTax = "company_taxes.json";
    public static String GetTaxCodesList = "tax_codes.json";
    public static String UpdateTax = "company_taxes/";
    public static String AddTaxCode = "tax_codes.json";
    public static String UpdateTaxCode = "tax_codes/";
    public static String Country = "countries.json";
    public static String UpdateCompanuDetail = "companies/update.json";
    public static String CompanySettings = "companies/settings.json";
    public static String UpdateCompanySettings = "companies/update_settings.json";
    public static String GetCompanyDetail = "companies/detail.json";
    public static String GetCompanyHeaders = "header_templates.json";
    public static String UpdateCompanyHeader = "header_templates/";
}
