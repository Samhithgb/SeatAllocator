package com.samhith.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

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

public class AdminLogin extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    ActionProcessButton admin_login;
    EditText admin_id,password;
    String id,pass;
    LinearLayout admin_linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        admin_linear=(LinearLayout)findViewById(R.id.linear_admin);
        admin_login= (ActionProcessButton)findViewById(R.id.btnLogIn_admin);
        admin_id = (EditText)findViewById(R.id.admin_id);
        password=(EditText)findViewById(R.id.admin_password);

        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id=admin_id.getText().toString();
                pass=password.getText().toString();
                new AsyncLogin().execute(id,pass);


            }

            class AsyncLogin extends AsyncTask<String, String, String>
            {
                ProgressDialog pdLoading = new ProgressDialog(AdminLogin.this);
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
                        url = new URL("http://192.168.43.195/test/login_admin.php");

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

                        // Append parameters to URL
                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("username", params[0])
                                .appendQueryParameter("password", params[1]);
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

                        Intent intent = new Intent(AdminLogin.this,AdminLandingScreen.class);
                        startActivity(intent);
                        finish();

                    }else if (result.equalsIgnoreCase("false")){

                        // If username and password does not match display a error message
                        Snackbar.make(admin_linear, "Invalid ID or Password", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                admin_login.requestFocus();
                            }
                        }).setActionTextColor(Color.CYAN).show();
                    } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                        Snackbar.make(admin_linear, "Check Your Connection", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                admin_login.requestFocus();
                            }
                        }).setActionTextColor(Color.CYAN).show();
                    }
                }

            }


        });
    }




}
