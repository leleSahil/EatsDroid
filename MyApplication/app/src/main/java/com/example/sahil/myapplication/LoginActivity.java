package com.example.sahil.myapplication;



/* attempt at webview here */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import feast.FeastAPI;
import feast.Menu;

public class LoginActivity extends Activity {

    private static String CLIENT_ID = "820762424096-qr8fr039sqs5nvch7qhfa3juu81k4r96.apps.googleusercontent.com";
    //Use your own client id
    private static String CLIENT_SECRET ="LH02vu5MmowRRBpzRrLLH1tA";
    //Use your own client secret
    private static String REDIRECT_URI="http://localhost";
    private static String GRANT_TYPE="authorization_code";
    private static String TOKEN_URL ="https://accounts.google.com/o/oauth2/token";
    private static String OAUTH_URL ="https://accounts.google.com/o/oauth2/auth";
    private static String OAUTH_SCOPE="https://www.googleapis.com/auth/userinfo.email";
    //Change the Scope as you need
    WebView web;
    Button auth;

    // making a button here for guest functionality
    Button guest;

    SharedPreferences pref;
    TextView Access;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(OAUTH_URL+"?redirect_uri="+REDIRECT_URI+"&response_type=code&client_id="+CLIENT_ID+"&scope="+OAUTH_SCOPE);
                web.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);

                    }
                    String authCode;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.contains("?code=") && authComplete != true) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("", "CODE : " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            LoginActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();
                            new TokenGet().execute();
                            //Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
                        }else if(url.contains("error=access_denied")){
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Authorize Eats4Droid User");
                auth_dialog.setCancelable(true);
            }
        });

        guest = (Button)findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(arg0.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });



        /*
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try
        {
            date = dateFormat.parse("2016/04/15");
            Log.w("Sahil", "date is " + date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FeastAPI.sharedAPI.fetchMenusForDateWithCompletion(date, new FeastAPI.MenusCallback() {
            @Override
            public void fetchedMenus(Set<Menu> menus) {
                Log.w("Riley", menus.toString());
                MyAppApplication mApp = ((MyAppApplication)getApplicationContext());
                mApp.setGlobalVarValue(menus);
            }
        });

        */



    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Contacting Google ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null){

                try {

                    String tok = json.getString("access_token");
                    String expire = json.getString("expires_in");
                    String refresh = json.getString("refresh_token");

                    Log.d("Token Access", tok);
                    Log.d("Expire", expire);
                    Log.d("Refresh", refresh);
                    Log.d("Cherrie", "Before Feast API call");
                    auth.setText("Authenticated");
                    //Access.setText("Access Token:"+tok+"nExpires:"+expire+"nRefresh Token:"+refresh);
                    Log.d("Cherrie", "Before Feast API call");
                    FeastAPI.sharedAPI.authorizeUserWithOAuthToken(tok, new FeastAPI.RequestCallback() {
                        @Override
                        public void requestFinishedWithSuccess(Boolean success, VolleyError error) {
                            Log.d("Cherrie", "Success: " + success);
                            if(success == true){
                                Log.d("Cherrie", "Made it inside of the if statement");
                                // stays on authenticated page for 2 seconds
//                                int timeout = 2000;
//
//                                Timer timer = new Timer();
//                                timer.schedule(new TimerTask() {
//
//                                    @Override
//                                    public void run() {
//                                        System.out.println("We are in timer");
//                                        finish();
//                                        Intent homepage = new Intent(LoginActivity.this, MainActivity.class);
//                                        // goes to the next activity
//                                        startActivity(homepage);
//                                        System.out.println("Did we start the next activity?");
//                                    }
//                                }, timeout);
                                Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goToNextActivity);

                            }
                        }
                    });


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }

}