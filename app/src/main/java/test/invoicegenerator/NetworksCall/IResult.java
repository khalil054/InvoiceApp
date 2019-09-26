package test.invoicegenerator.NetworksCall;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    public void notifySuccess(String requestType, String response);
    public void notifyError(String requestType, VolleyError error);
    public void notifySuccessResponseHeader( NetworkResponse response);


}