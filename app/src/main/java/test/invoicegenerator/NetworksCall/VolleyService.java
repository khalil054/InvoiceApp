package test.invoicegenerator.NetworksCall;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;
import test.invoicegenerator.general.Constants;
import test.invoicegenerator.general.Util;
import test.invoicegenerator.model.SharedPref;


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


    public void postDataVolley(final String requestType, String url, final Map<String, String> params){

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

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                String error_response=new String(error.networkResponse.data);
                                try {
                                    JSONObject response_obj=new JSONObject(error_response);
                                    String status=response_obj.getString("status");
                                    if(status.equals("false"))
                                    {
                                        JSONObject error_obj=response_obj.getJSONObject("error");
                                        String message=error_obj.getString("message");
                                     //   Toasty.error(mContext,message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                 //   Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{

                            }
                               // Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();

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
                protected Response<String> parseNetworkResponse(NetworkResponse response) {


                    SharedPref.init(mContext);
                    SharedPref.write(Constants.ACCESS_TOKEN, response.headers.get("access-token"));
                    SharedPref.write(Constants.CLIENT, response.headers.get("client"));
                    SharedPref.write(Constants.UID, response.headers.get("uid"));


                    return super.parseNetworkResponse(response);
                }

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);


    }
    public void DeleteDataVolley(String url){

            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest strRequest = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess("Delete",response);



                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if(mResultCallback != null)
                                mResultCallback.notifyError("Delete",error);
                        }
                    }
            )

            {
                //sending headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    SharedPref.init(mContext);
                    String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                    String client=SharedPref.read(Constants.CLIENT,"");
                    String uid=SharedPref.read(Constants.UID,"");
                    HashMap<String, String>  headers = new HashMap<String, String>();
                    headers.put("access-token", access_token);
                    headers.put("client",client );
                    headers.put("uid",uid);
                    return headers;


                }
                //close sending headers

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);


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

                    SharedPref.init(mContext);
                    String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                    String client=SharedPref.read(Constants.CLIENT,"");
                    String uid=SharedPref.read(Constants.UID,"");
                    HashMap<String, String>  headers = new HashMap<String, String>();
                    headers.put("access-token", access_token);
                    headers.put("client",client );
                    headers.put("uid",uid);

                    return headers;
                }
                //close sending headers
            };



            queue.add(strRequest);

        }catch(Exception e){

        }
    }
    public void postDataVolleyForHeadersWithJson(final String requestType, String url, JSONObject params){

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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPref.init(mContext);
                String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                String client=SharedPref.read(Constants.CLIENT,"");
                String uid=SharedPref.read(Constants.UID,"");
                HashMap<String, String>  headers = new HashMap<String, String>();
                headers.put("access-token", access_token);
                headers.put("client",client );
                headers.put("uid",uid);
                headers.put("Content-Type","application/json");

                return headers;
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        //first one

    }

    public void putDataVolleyForHeadersWithJson(final String requestType, String url, JSONObject params){

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonRequest request = new JsonRequest
                (Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {

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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPref.init(mContext);
                String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                String client=SharedPref.read(Constants.CLIENT,"");
                String uid=SharedPref.read(Constants.UID,"");
                HashMap<String, String>  headers = new HashMap<String, String>();
                headers.put("access-token", access_token);
                headers.put("client",client );
                headers.put("uid",uid);
                headers.put("Content-Type","application/json");

                return headers;
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        //first one

    }


                    /*get Request*/


    /*get Data using param*/
    public void getDataVolley(final String requestType, String url, final Map<String, String> params){
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

                   /* Map<String, String> data = new HashMap<>();

                    return data;*/
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    SharedPref.init(mContext);
                    String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                    String client=SharedPref.read(Constants.CLIENT,"");
                    String uid=SharedPref.read(Constants.UID,"");
                    HashMap<String, String>  headers = new HashMap<String, String>();
                    headers.put("access-token", access_token);
                    headers.put("client",client );
                    headers.put("uid",uid);
                    return headers;
                }

               /* @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                  *//*  boolean strStatus =SharedPref.readBol(SharedPref.IsLoginUser, false);
                    if(strStatus){*//*
                        SharedPref.write(SharedPref.Access_Token, response.headers.get("access-token"));
                        SharedPref.write(SharedPref.Client, response.headers.get("client"));
                        SharedPref.write(SharedPref.UID, response.headers.get("uid"));
                    *//*}*//*

                    return super.parseNetworkResponse(response);

                }*/
            };

            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /*get Request without params*/
   /* public void getDataVolleyWithoutparam(final String requestType, String url){
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

                    Map<String, String> data = new HashMap<>();

                    return data;
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
                    // boolean strStatus =SharedPref.readBol(SharedPref.IsLoginUser, false);
                    // if(strStatus) {
                    params.put(SharedPref.Access_Token, SharedPref.read(SharedPref.Access_Token, ""));
                    params.put(SharedPref.Client, SharedPref.read(SharedPref.Client, ""));
                    params.put(SharedPref.UID, SharedPref.read(SharedPref.UID, ""));
                    //}

                    return params;
                }


            };

            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }*/


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


    public void postDataVolleyForHeaders(final String requestType, String url, final Map<String, String> params){

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

                        if(error.networkResponse != null && error.networkResponse.data != null){
                            String error_response=new String(error.networkResponse.data);
                            try {
                                JSONObject response_obj=new JSONObject(error_response);
                                String status=response_obj.getString("status");
                                if(status.equals("false"))
                                {
                                    JSONObject error_obj=response_obj.getJSONObject("error");
                                    String message=error_obj.getString("message");
                                    Toasty.error(mContext,message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                            Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams()
            {

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPref.init(mContext);
                String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                String client=SharedPref.read(Constants.CLIENT,"");
                String uid=SharedPref.read(Constants.UID,"");
                HashMap<String, String>  headers = new HashMap<String, String>();
                headers.put("access-token", access_token);
                headers.put("client",client );
                headers.put("uid",uid);


                return headers;
            }
        }

                ;
        strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(strRequest);
        //first one

    }
    public void putDataVolleyForHeaders(final String requestType, String url, final Map<String, String> params){

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

                        if(error.networkResponse != null && error.networkResponse.data != null){
                            String error_response=new String(error.networkResponse.data);
                            try {
                                JSONObject response_obj=new JSONObject(error_response);
                                String status=response_obj.getString("status");
                                if(status.equals("false"))
                                {
                                    JSONObject error_obj=response_obj.getJSONObject("error");
                                    String message=error_obj.getString("message");
                                    Toasty.error(mContext,message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                            Toasty.error(mContext, Util.getMessage(error), Toast.LENGTH_SHORT).show();

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

                SharedPref.init(mContext);
                String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                String client=SharedPref.read(Constants.CLIENT,"");
                String uid=SharedPref.read(Constants.UID,"");
                HashMap<String, String>  headers = new HashMap<>();
                headers.put("access-token", access_token);
                headers.put("client",client );
                headers.put("uid",uid);


                return headers;
            }

        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(strRequest);
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

    public void putDataVolleyWithHeader(final String requestType, String url, final Map<String, String> params){
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

                /*@Override
                protected Map<String, String> getParams()
                {

                    return params;
                }*/

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return super.getBody();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    SharedPref.init(mContext);
                    String access_token=SharedPref.read(Constants.ACCESS_TOKEN,"");
                    String client=SharedPref.read(Constants.CLIENT,"");
                    String uid=SharedPref.read(Constants.UID,"");
                    HashMap<String, String>  headers = new HashMap<String, String>();
                    headers.put("access-token", access_token);
                    headers.put("client",client );
                    headers.put("uid",uid);


                    return headers;
                }

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}