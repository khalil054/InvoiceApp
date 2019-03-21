package test.invoicegenerator.NetworksCall;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by apple on 04/10/2018.
 */

public interface IResult {
    public void notifySuccess(String requestType, String response);
    public void notifyError(String requestType, VolleyError error);
    public void notifySuccessResponseHeader( NetworkResponse response);


}