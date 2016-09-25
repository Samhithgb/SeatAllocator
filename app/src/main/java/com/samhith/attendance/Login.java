package com.samhith.attendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.lang.Object.*;
import android.support.design.*;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {

    EditText usn;
    RelativeLayout login_relative;
    ActionProcessButton login,admin_login;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usn=(EditText)findViewById(R.id.usn);
        login=(ActionProcessButton)findViewById(R.id.btnLogIn);
        admin_login = (ActionProcessButton)findViewById(R.id.btnLogInAdmin);
        login_relative=(RelativeLayout)findViewById(R.id.rlayout_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final String usn_string =usn.getText().toString();
                new AsyncLogin().execute(usn_string);


            }



             class AsyncLogin extends AsyncTask<String, String, String>
            {
                ProgressDialog pdLoading = new ProgressDialog(Login.this);
                HttpURLConnection conn;
                URL url = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    //this method will be running on UI thread
                    pdLoading.setMessage("\tLogging In...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();

                }
                @Override
                protected String doInBackground(String... params) {


                    try {

                        // Enter URL address where your php file resides
                        url = new URL("http://172.17.2.241/test/login.php");

                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return "exception";
                    }
                    try {

                        // Setup HttpURLConnection class to send and receive data from php and mysql
                        conn = (HttpURLConnection)url.openConnection();
                        conn.setReadTimeout(READ_TIMEOUT);
                        conn.setConnectTimeout(CONNECTION_TIMEOUT);
                        conn.setRequestMethod("POST");

                        // setDoInput and setDoOutput method depict handling of both send and receive
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        Log.d("If","Here2");

                        // Append parameters to URL
                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("username", params[0])
                                ;
                        String query = builder.build().getEncodedQuery();

                        // Open connection for sending data
                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();
                        conn.connect();

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        return "exception";
                    }

                    try {

                        int response_code = conn.getResponseCode();
                        Log.d("If","Here");
                        // Check if successful connection made
                        if (response_code == HttpURLConnection.HTTP_OK) {

                            // Read data sent from server
                            InputStream input = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                            StringBuilder result = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }

                            // Pass data to onPostExecute method
                            return(result.toString());

                        }else{

                            return("unsuccessful");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        return "exception";
                    } finally {
                        conn.disconnect();
                    }


                }

                @Override
                protected void onPostExecute(String result) {

                    //this method will be running on UI thread

                    pdLoading.dismiss();

                    if(result.equalsIgnoreCase("true"))
                    {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                        Toast.makeText(Login.this, "Success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this,LandingScreenStudent.class);
                        startActivity(intent);
                        Login.this.finish();

                    }else if (result.equalsIgnoreCase("false")){

                        // If username and password does not match display a error message
                        Snackbar.make(login_relative, "Invalid USN", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                usn.requestFocus();

                            }
                        }).setActionTextColor(Color.CYAN).show();

                    } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                        Snackbar.make(login_relative, "Check Your Connection", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                usn.requestFocus();
                            }
                        }).setActionTextColor(Color.CYAN).show();
                    }
                }

            }

        });




        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(Login.this, AdminLogin.class);
                    startActivity(i);

            }
        });

    }

}
