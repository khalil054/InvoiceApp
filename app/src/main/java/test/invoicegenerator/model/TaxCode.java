package test.invoicegenerator.model;

/**
 * Created by User on 10/11/2018.
 */

public class TaxCode {
    private String tax_code, tax_detail_id;
    public TaxCode(String t_code, String tax_detail_id)
    {
        this.tax_code=t_code;
        this.tax_detail_id=tax_detail_id;
    }

    public String getTax_code() {
        return tax_code;
    }

    public void setTax_code(String tax_code) {
        this.tax_code = tax_code;
    }

    public String getTax_name() {
        return tax_detail_id;
    }

    public void setTax_name(String tax_name) {
        this.tax_detail_id = tax_name;
    }
}
