package com.project.edn.washit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.project.edn.washit.Fragment.HistoryFragment;
import com.project.edn.washit.Fragment.HomeFragment;
import com.project.edn.washit.R;


public class MainActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener, SmartTabLayout.TabProvider {

    private ViewPager viewPager;
    private  SmartTabLayout viewPagerTab;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            navigationView.setSelected(false);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("Home", HomeFragment.class)
                    .add("History", HistoryFragment.class)

                    .create());

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
            viewPagerTab.setCustomTabView(this);
            viewPagerTab.setViewPager(viewPager);

        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            Intent in;
            int id = item.getItemId();

            if (id == R.id.nav_account) {
                in=new Intent(MainActivity.this,AccountActivity.class);
                startActivity(in);
                // Handle the camera action
            } else if (id == R.id.nav_notif) {
                in=new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(in);

            } else if (id == R.id.nav_logout) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

//    public void RequestJsonLogout() {
        //{"success":true,"data":{"user":"SADAKO","name":"SADAKO THE RING"},"messages":"Logout Session Complete."}
//        //Buatkan Request Dalam bentuk String
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_Logout,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        //jika server respon server berupa success gagal
//                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                        //Getting editor
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        // put nilai false untuk login
//                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
//                        editor.putString(Config.TOKEN_SHARED_PREF, "");
//                        // put nilai untuk username
//                        editor.putString(Config.USER_SHARED_PREF, "");
//                        editor.putString("qr", "");
//                        //Simpan ke haredpreferences
//                        editor.commit();
//                        //Starting Class yang dipanggil
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivity(intent);
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(MainActivity.this, "Failed Load Your Data,Check Your Connection", Toast.LENGTH_LONG).show();
//
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Time-Request", Long.toString(timereq1));
//                params.put("Apps-Id", en.encr());
//                params.put("Finger-Print", en.encr1());
//                params.put("X-Key-Secret:", en.fingerprinttoken(token, ((int) timereq1)));
//                params.put("User-Agent", "SubAssitance/v1.0(oid:" + Config.KEY_ID + ")");
//                return params;
//            }
//
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put(Config.TOKEN_SHARED_PREF, token);
//                return params;
//            }
//        };
//
//        //Tambahkan Request String ke dalam Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        int socketTimeout = 100000;//30 seconds - change to what you want
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        stringRequest1.setRetryPolicy(policy);
//        requestQueue.add(stringRequest1);
//    }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View tab = inflater.inflate(R.layout.custom_tab_layout, container, false);
            TextView text=(TextView)tab.findViewById(R.id.custom_tab_text) ;
            text.setText(adapter.getPageTitle(position));
            ImageView icon = (ImageView) tab.findViewById(R.id.custom_tab_icon);
            switch (position) {
                case 0:
                    icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_home,null));
                    break;
                case 1:
                    icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_history,null));
                    break;
                default:
                    throw new IllegalStateException("Invalid position: " + position);
            }
            return tab;
        }


}

