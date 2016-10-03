package com.samhith.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;


public class LandingScreen extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private ArrayList<AndroidVersion> data;
    private DataAdapter adapter;
    String usn_string;
    RelativeLayout landing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        landing=(RelativeLayout)findViewById(R.id.landing_relative);
        Intent i=getIntent();
        usn_string=i.getStringExtra("usn");
        LandingScreen.this.setTitle("Hi "+usn_string.toUpperCase()+"!");
        initViews();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        switch(position){

            case 0 : Toast.makeText(LandingScreen.this,"Seat Allocator App : Dept of ISE", Toast.LENGTH_LONG).show();

                break;
            case 1 :
            Toast.makeText(LandingScreen.this,"Android : Samhith G B, Backend : Prabhjeet", Toast.LENGTH_LONG).show();
                break;

            case 2 :
             //   Toast.makeText(LandingScreen.this,"Here",Toast.LENGTH_LONG).show();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(LandingScreen.this,"You are logged out",Toast.LENGTH_LONG).show();
                Intent i = new Intent(LandingScreen.this,Login.class);
                startActivity(i);
                finish();
                break;
            case 3 : Toast.makeText(LandingScreen.this,"Coming Soon. For now, run to office!",Toast.LENGTH_LONG).show();break;
            case 4 : Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                email.putExtra(Intent.EXTRA_EMAIL  , new String[]{"gbsamhith@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "FeedBack : Seat Allocator");
                try {
                    startActivity(Intent.createChooser(email, "Send FeedBack"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(LandingScreen.this, "There are no email clients installed!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }
    private void loadJSON(){

        final ProgressDialog loading = ProgressDialog.show(this,"Fetching Data","Please wait...",false,false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://careerportal.heliohost.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON(usn_string);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                loading.dismiss();
                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getResponse()));
                if(data.size()==0){
                    Snackbar.make(landing,"Error : Empty Response from Server",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         loadJSON();
                        }
                    }).setActionTextColor(Color.CYAN).show();

                }
                // Toast.makeText(MainActivity.this,jsonResponse.getResponse().length+"", Toast.LENGTH_LONG).show();
                adapter = new DataAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                loading.dismiss();
                    //Toast.makeText(LandingScreen.this,"Launch Again",Toast.LENGTH_LONG).show();
                Snackbar.make(landing,"Error: Couldn't Reach.Try Again",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadJSON();
                    }
                }).setActionTextColor(Color.CYAN).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.landing_screen, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.book) {
            loadJSON();
        }

        return super.onOptionsItemSelected(item);
    }


}
