package test.invoicegenerator.model;

import java.util.ArrayList;

/**
 * Created by User on 10/10/2018.
 */

public class Company {
    private String line1, line2, country, city, state, zip_code, tax_code, notes;
    private ArrayList<String> currency_codes;
    private int invoice_header;
    private boolean whole_invoice;
    private CompanyContactDetail company_contact_detail;
            public Company(String l1, String l2, String country, String city, String state, String zip, String tax, String notes,
                           ArrayList<String> currency_codes, int header, boolean whole, CompanyContactDetail company_detail)
            {
                this.line1=l1;
                this.line2=l2;
                this.country=country;
                this.city=city;
                this.state=state;
                this.zip_code=zip;
                this.tax_code=tax;
                this.notes=notes;
                this.currency_codes=currency_codes;
                this.invoice_header=header;
                this.whole_invoice=whole;
                this.company_contact_detail=company_detail;
            }
}
