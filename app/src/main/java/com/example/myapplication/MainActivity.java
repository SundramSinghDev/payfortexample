package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payfort.fortpaymentsdk.FortSdk;
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager;
import com.payfort.fortpaymentsdk.callbacks.FortCallback;
import com.payfort.fortpaymentsdk.callbacks.FortInterfaces;
import com.payfort.fortpaymentsdk.domain.model.FortRequest;

import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //TODO Kndly relace the merchantID, accessCode, SHARequestPhrase and SHAResponsePhrase with your credentials because these details are the client details
    private String merchantID = "VONAagIF";
    private String accessCode = "SJfOunz2YauoKE2LLwGy";
    private String SHARequestPhrase = "SwsgCo2018";
    private String SHAResponsePhrase = "SwsgCoForU";
    String deviceID = "";
    private FortCallBackManager fortCallback = null;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (fortCallback == null)
            fortCallback = FortCallback.Factory.create();
        findViewById(R.id.pay).setOnClickListener(view -> {
            try {

                deviceID = FortSdk.getDeviceId(this);
                HashMap<String, String> params = new HashMap<>();

//                JSONObject params = new JSONObject();
                params.put("service_command", "SDK_TOKEN");
                params.put("access_code", accessCode);
                params.put("merchant_identifier", merchantID);
                params.put("language", "en");
                params.put("device_id", deviceID);
                params.put("signature", createSignature());
                Log.e(TAG, "onCreate: " + params);


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        "https://sbpaymentservices.payfort.com/FortAPI/paymentApi", new JSONObject(params),
                        response -> {
                            processToken(response);
                            Log.d(TAG, response.toString());
                        }, error -> VolleyLog.d(TAG, "Error: " + error.getMessage())) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                Volley.newRequestQueue(this).add(jsonObjReq);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    @SuppressWarnings("unchecked")
    private void processToken(JSONObject output) {
        try {
            Log.e(TAG, "processtoken: " + output);
            FortRequest fortRequest1 = new FortRequest();
            fortRequest1.setRequestMap(collectRequestMap(output.getString("sdk_token")));
            fortRequest1.setShowResponsePage(false);
            callSdk(fortRequest1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callSdk(FortRequest fortrequest) {
        try {
            FortSdk.getInstance().registerCallback(MainActivity.this, fortrequest,
                    FortSdk.ENVIRONMENT.TEST, 5, fortCallback, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map requestParamsMap, Map responseMap) {
                            Log.d(TAG + " Cancelled ", responseMap.toString());
                        }

                        @Override
                        public void onSuccess(Map requestParamsMap, Map fortResponseMap) {    //TODO: handle me
                            Log.i(TAG + " Success ", fortResponseMap.toString());
                        }

                        @Override
                        public void onFailure(Map requestParamsMap,
                                              Map fortResponseMap) {
                            Log.e(TAG + " Failure ", fortResponseMap.toString());
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String createSignature() {
        String signature = SHARequestPhrase + "access_code=" + accessCode + "device_id=" + deviceID + "language=en" + "merchant_identifier=" + merchantID + "service_command=SDK_TOKEN"
                + SHARequestPhrase;
        String sha256 = "";
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = messageDigest.digest(signature.getBytes(StandardCharsets.UTF_8));
            sha256 = String.format("%0" + (hashed.length * 2) + 'x', new BigInteger(1, hashed));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha256;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fortCallback.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map collectRequestMap(String sdkToken) {
        int c = 0;
        c++;
        Map requestMap = new HashMap<>();
        requestMap.put("command", "PURCHASE");
        requestMap.put("customer_email", "Sam@gmail.com");
        requestMap.put("currency", "SAR");
        requestMap.put("amount", "100");
        requestMap.put("language", "en");
        requestMap.put("merchant_reference", "ORD-000000768" + c);
        requestMap.put("customer_name", "Sam");
//        requestMap.put("customer_ip", "172.150.16.10");
//        requestMap.put("payment_option", "VISA");
//        requestMap.put("eci", "ECOMMERCE");
//        requestMap.put("order_description", "DESCRIPTION");
        requestMap.put("sdk_token", sdkToken);
        return requestMap;
    }

}