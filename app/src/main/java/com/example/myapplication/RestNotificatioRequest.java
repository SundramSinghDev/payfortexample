/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RestNotificatioRequest extends AsyncTask<String ,String,String>
{
    public AsyncResponse delegate = null;
    String req="";
    HashMap<String,String> data;
    boolean connect=true;
    String Splash="";
    int responseCode;
    String url;
    JSONObject data_post;
    Context c;
    HashMap<String,String> params;

    public RestNotificatioRequest(AsyncResponse cedAsyncResponse, Context context, String RequestMethod, HashMap<String, String> postparam)
    {

        delegate = cedAsyncResponse;
        req=RequestMethod;
        params=postparam;
        Log.i("REposnse", "" + data);
        c=context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... params)
    {
        String json="";
        Log.i("REposnse",""+params[0].trim());
        url=params[0];
        if(req.equals("GET"))
        {
            json=Client(params[0]);
        }
        else
        {
            json=ClientPost(params[0]);
        }
        Log.i("REposnse", "" + json);

        return json;



    }
    @Override
    protected void onPostExecute(String av)
    {
        super.onPostExecute(av);
        if(connect)
        {
            try
            {
                delegate.processFinish(av);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {

            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
//                this.cedNewLoader = null;
            }
        }
        else
        {
            /*try
            {
                if ((this.cedNewLoader != null) && this.cedNewLoader.isShowing())
                {
                    this.cedNewLoader.dismiss();
                }
            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                this.cedNewLoader = null;
            }
            c.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    final Dialog listDialog = new Dialog(c,R.style.PauseDialog);
                    ((ViewGroup)((ViewGroup)listDialog.getWindow().getDecorView()).getChildAt(0)).getChildAt(1).setBackgroundColor(c.getResources().getColor(R.color.AppTheme));
                    listDialog.setTitle(c.getResources().getString(R.string.oops));
                    LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = li.inflate(R.layout.magenative_activity_no_module, null, false);
                    listDialog.setContentView(v);
                    listDialog.setCancelable(true);
                    TextView conti = (TextView) listDialog.findViewById(R.id.conti);
                    conti.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            listDialog.dismiss();
                            if(req.equals("GET"))
                            {
                                MageNative_ClientRequestResponse ced_requestwithoutLoader = new MageNative_ClientRequestResponse(delegate, c);
                                ced_requestwithoutLoader.execute(url);
                            }
                            else
                            {
                                if(Splash.isEmpty())
                                {
                                    MageNative_ClientRequestResponse ced_requestwithoutLoader = new MageNative_ClientRequestResponse(delegate, c, req, params);
                                    ced_requestwithoutLoader.execute(url);
                                }
                                else
                                {
                                    MageNative_ClientRequestResponse ced_requestwithoutLoader = new MageNative_ClientRequestResponse(delegate, c, req, params,Splash);
                                    ced_requestwithoutLoader.execute(url);
                                }
                            }

                        }
                    });
                    listDialog.show();
                }
            });*/

        }
    }
    public String ClientPost(String url)
    {
        URL url1;
        String response = "";
        try
        {
            url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.getDefaultUseCaches();
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
            conn.setRequestProperty("ced_mage_api", "mobiconnect");

            /*conn.setRequestProperty("uid","cedcommercetest");*/
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            responseCode=conn.getResponseCode();
            Log.i("REposnse",""+responseCode);
            String line1;
            BufferedReader br1=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line1=br1.readLine()) != null)
            {
                response+=line1;
            }
            Log.i("REposnse",response);
            if(responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }

            }
            else
            {

                connect=false;
                response="";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            connect=false;

        }

        return response;
    }
    public String Client(String url)
    {
        String result = "";
        try
        {
            URL apiurl =null;
            HttpURLConnection conn;
            String line;
            BufferedReader rd;
            apiurl = new URL(url);
            conn = (HttpURLConnection) apiurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1500000);
            conn.setConnectTimeout(1500000);
            conn.setRequestProperty("Mobiconnectheader", c.getResources().getString(R.string.header));
            conn.setRequestProperty("ced_mage_api", "mobiconnect");

            /*conn.setRequestProperty("uid","cedcommercetest");*/
            responseCode=conn.getResponseCode();
            Log.i("REposnse",""+responseCode);
            if(responseCode==HttpsURLConnection.HTTP_OK)
            {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null)
                {
                    result += line;
                }
                rd.close();

            }
            else
            {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = rd.readLine()) != null)
                {
                    result += line;
                }
                rd.close();

                connect=false;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            connect=false;

        }
        return result;
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.i("REposnse",""+params);
        return result.toString();
    }
}
