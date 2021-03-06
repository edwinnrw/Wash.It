package com.project.edn.washit.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.project.edn.washit.Config;
import com.project.edn.washit.FirebaseApplication;
import com.project.edn.washit.api.ServiceHelper;
import com.project.edn.washit.fragment.HistoryFragment;
import com.project.edn.washit.fragment.HomeFragment;
import com.project.edn.washit.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener, SmartTabLayout.TabProvider {

    private ViewPager viewPager;
    private  SmartTabLayout viewPagerTab;
    private boolean loggedIn;
    private String token;
    private boolean twice;
    private ProgressDialog progressDialog;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

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
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            token = sharedPreferences.getString(Config.TOKEN_SHARED_PREF, "");
            loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            if (!loggedIn) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
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
                progressDialog.show();
                RequestJsonLogout();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    private void RequestJsonLogout() {
        ServiceHelper.getInstance().logout(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                editor.putString(Config.TOKEN_SHARED_PREF, "");
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("telp","");
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        FirebaseApplication.getInstance().setMaintActivityOpen(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseApplication.getInstance().setMaintActivityOpen(false);
    }
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageHandler(Map<String, String> mapResponse) {
        notificationBuilder(mapResponse.get("notif"));

    }
    private void notificationBuilder(String message){

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WashIT Notifikasi")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

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

