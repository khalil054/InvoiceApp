package test.invoicegenerator.NetworksCall;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by apple on 04/10/2018.
 */

public class VolleyService {

    IResult mResultCallback = null;
    Context mContext;

    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolley(final String requestType, String url, final Map<String, String> params,final Map<String,String> headers){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);



                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    }
                    )
            {
                @Override
                protected Map<String, String> getParams()
                {
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    return headers;
                }

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);

        }catch(Exception e){

        }
    }
    public void DeleteDataVolley(final String requestType, String url, final Map<String, String> params,final Map<String,String> headers){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);



                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    }
            )

            {

                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }
                //sending headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    return headers;
                }
                //close sending headers

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);

        }catch(Exception e){

        }
    }

    public void getDataVolley(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);
                        }

                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {

                    Map<String, String> data = new HashMap<String, String>();

                    return data;
                }
                //sending headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("User-Agent", "Nintendo Gameboy");
                    params.put("Accept-Language", "fr");

                    return params;
                }
                //close sending headers
            };



            queue.add(strRequest);

        }catch(Exception e){

        }
    }

//    public void MulltiPart(ArrayList<Part> filesParts, ProfileUpdateModel profile, String url) {
//
//
//        GlobalData.coordinates = "";
//        Ion.with(mContext)
//                .load("POST", url )
//                .uploadProgressHandler(new ProgressCallback() {
//                    @Override
//                    public void onProgress(long uploaded, long total) {
//                    }
//                })
//                .setMultipartParameter("userid", profile.getUserId())
//                .setMultipartParameter("City", profile.getCity())
//                .setMultipartParameter("fullname", profile.getFullname())
//                .setMultipartParameter("coordinates", profile.getCoordinates())
//                .setMultipartParameter("address", profile.getAddress())
//                .setMultipartParameter("category", profile.getCategory())
//                .setMultipartParameter("category2", profile.getSubcategory())
//                .addMultipartParts(filesParts)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//
//                        if(mResultCallback != null)
//                            mResultCallback.notifySuccess("",result);
//
//                    }
//
//                });
//    }

    public class JsonRequest extends JsonObjectRequest {

        public JsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener
                <JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                int status_code=response.statusCode;
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("data", new JSONObject(jsonString));
                jsonResponse.put("headers", new JSONObject(response.headers));
              //  mResultCallback.notifySuccessResponseHeader(response);
                return Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    public void postDataVolleyForHeaders(final String requestType, String url, JSONObject params){

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonRequest request = new JsonRequest
                (Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(mResultCallback != null)
                            mResultCallback.notifySuccess(requestType,response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(mResultCallback != null)
                            mResultCallback.notifyError(requestType,error);
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        //first one

    }
    public void putDataVolleyForHeaders(final String requestType, String url, JSONObject params, final HashMap<String,String> headers){

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonRequest request = new JsonRequest
                (Request.Method.PUT, url, params,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(mResultCallback != null)
                            mResultCallback.notifySuccess(requestType,response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(mResultCallback != null)
                            mResultCallback.notifyError(requestType,error);
                    }

                }
                )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return headers;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        //first one

    }

    public void putDataVolley(final String requestType, String url, final Map<String, String> params,final Map<String,String> headers){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(requestType,response);



                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError(requestType,error);
                        }
                    }
            )

            {

                @Override
                protected Map<String, String> getParams()
                {

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    return headers;
                }

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);

        }catch(Exception e){

        }
    }

}