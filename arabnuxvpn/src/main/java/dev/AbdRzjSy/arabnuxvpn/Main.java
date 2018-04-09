package dev.AbdRzjSy.arabnuxvpn;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import de.blinkt.openvpn.DisconnectVPNActivity;
import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.R;

import static dev.AbdRzjSy.arabnuxvpn.Constent.APK_TYPE;

/*
   created by Abdulkader Alrazj // www.facebook.com/AbdSenRzj
 */
public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BroadcastReceiver mReceiver ;
    IntentFilter mIntentFilter;
    private boolean isVpnStarted = true;
    CheckBox auto_connect;
    EditText username,Password;
    Button startVpn;
    SharedPreferences pfs;
    String password,Username;
    TextView data,statu,ipAdress;
    Boolean firstTime = false;
    String arbnuxAppID="ca-app-pub-7552788510470880/1116838801";
    String arabnuxADsID="ca-app-pub-7552788510470880/1116838801";
    private InterstitialAd mInterstitialAd;
    SharedPreferences prefs ;
    Boolean isConnected;
    private CardView cardView;
    private LinearLayout layout;
    private Menu nav_Menu;
    private TextView copyRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("isConnected",MODE_PRIVATE);
        prefs.edit().putBoolean("isConnected", true).apply();
        isConnected =true;



        mIntentFilter=new IntentFilter("MESSAGE");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {

                   if (intent.getAction()!=null && intent.getAction().equals("MESSAGE")){
                       String msg = intent.getStringExtra("MSG");
                       String IP_ADRESS = intent.getStringExtra("IP_ADRESS");
                       String Status = intent.getStringExtra("Status")+"";

                       startVpn.setEnabled(false);
                       Password.setEnabled(false);
                       username.setEnabled(false);
                       if (IP_ADRESS != null && !IP_ADRESS.equals("")){
                           ipAdress.setText(IP_ADRESS);
                       }

                       if (msg != null && !msg.equals("")){
                           statu.setText(msg);
                       }
                       if (Status.equals("LEVEL_CONNECTED")){
                           statu.setText(R.string.Online);
                           statu.setTextColor(Color.parseColor("#42b728"));
                           data.setText(msg);
                           startVpn.setEnabled(true);
                           isVpnStarted = false;
                           Password.setEnabled(false);
                           username.setEnabled(false);
                           startVpn.setText(R.string.disconnect);
                           startVpn.setBackgroundResource(R.drawable.btn_disconnect);
                           if (isConnected){
                               LoadInterstitialAd();
                               mInterstitialAd.setAdListener(new AdListener(){
                                  @Override
                                  public void onAdLoaded() {
                                      super.onAdLoaded();
                                      mInterstitialAd.show();
                                      isConnected=false;
                                  }
                              });
                           }
                       }else if (Status.equals("no process running")){
                           statu.setText(R.string.offline);
                           startVpn.setText(R.string.connect);
                           statu.setTextColor(Color.parseColor("#F70044"));
                           data.setText(R.string._0kb_s_0kb_0kb_s_0kb);
                           ipAdress.setText(R.string._0_0_0_0);
                       }else if (Status.equals("LEVEL_NOTCONNECTED")){
                           Password.setEnabled(true);
                           startVpn.setEnabled(true);
                           startVpn.setText(R.string.connect);
                           startVpn.setBackgroundResource(R.drawable.btn_connect);
                           username.setEnabled(true);
                           statu.setText(R.string.offline);
                           statu.setTextColor(Color.parseColor("#F70044"));
                           data.setText(R.string._0kb_s_0kb_0kb_s_0kb);
                           ipAdress.setText(R.string._0_0_0_0);
                           isVpnStarted=true;
                           firstTime=true;
                       }
                   }


            }
        };


        initViews();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        View headrview = navigationView.getHeaderView(0);

        TextView list_lgoo =headrview.findViewById(R.id.txt_logo);
        TextView logo2 =headrview.findViewById(R.id.txt_logo2);

        TextView fb_gourp =headrview.findViewById(R.id.fb_group);
        fb_gourp.setText(Constent.FB_Group);
        list_lgoo.setText(Constent.LIST_LOGO);
        logo2.setText(Constent.LIST_LOGO2);
        nav_Menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        AppliCationType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onDestroy() {
        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

 /* @Override
    protected void onPause() {
        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onPause();
    }*/

    public void initViews(){
        pfs = getSharedPreferences("SavePSW",MODE_PRIVATE);
        Switch save_psw = (Switch) findViewById(R.id.save_psw);
        startVpn = (Button) findViewById(R.id.startVpn);
        cardView = (CardView) findViewById(R.id.psw);
        layout = (LinearLayout) findViewById(R.id.rempsw);
        auto_connect= (CheckBox) findViewById(R.id.auto_connect);
        username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.Password);
        statu = (TextView) findViewById(R.id.statu_con);
        data = (TextView) findViewById(R.id.data);
        ipAdress = (TextView) findViewById(R.id.ip_adress);
        copyRight = (TextView) findViewById(R.id.copyRightMain);
        if (Locale.getDefault().getLanguage().equals("ar")){
            copyRight.setText("جميع الحقوق محفوظة لفريق عمل  Arabnux صاحب تطبيق يرجوا من الله المغفرة والعفو ومنكم بدعوة صالحة في ظهر الغيب");
        }else {
            copyRight.setText("All rights reserved to Arabnux team work . the application's owner hopes from you to remember him in your prayers");
        }
        final ImageButton show_hide_pass = (ImageButton)findViewById(R.id.show_hide_pass);

        show_hide_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    Password.setInputType( InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show_hide_pass.setImageResource(R.drawable.ic_visibility_black_24dp);
                }else {
                    show_hide_pass.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    Password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
                Password.setSelection(Password.getText().length());

            }
        });


       
        TextView logo =(TextView) findViewById(R.id.logo);
        TextView net =(TextView) findViewById(R.id.net_text);
        TextView phone =(TextView) findViewById(R.id.phone_number);

        net.setText(Constent.MOZAWIDE_INTERNET);
        logo.setText(Constent.Logo);
        phone.setText(Constent.PHONE_NUMBER);


        startVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (isVpnStarted){
                        startVpn();
                    }else {
                        Intent disconnectVPN = new Intent(Main.this, DisconnectVPNActivity.class);
                        disconnectVPN.setAction("de.blinkt.openvpn.DISCONNECT_VPN");
                        startActivity(disconnectVPN);

                    }




            }
        });
        try {
            Boolean tr= pfs.getBoolean("isChecked",false);
            if (tr){
                Username=pfs.getString("Username","");
                password=pfs.getString("Password","");
                username.setText(Username);
                Password.setText(password);
                save_psw.setOnCheckedChangeListener(null);
                save_psw.setChecked(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        
        auto_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pfs = getSharedPreferences("SavePSW",MODE_PRIVATE);
                SharedPreferences.Editor editor=pfs.edit();
                editor.putBoolean("AutoConnect",true);
                editor.apply();
            }
        });




        save_psw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    pfs = getSharedPreferences("SavePSW",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pfs.edit();
                    editor.putString("Username",username.getText().toString());
                    editor.putString("Password",Password.getText().toString());
                    editor.putBoolean("isChecked",true);
                    editor.apply();
                }else {
                    pfs = getSharedPreferences("SavePSW",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pfs.edit();
                    editor.putString("Username","");
                    editor.putString("Password","");
                    editor.putBoolean("isChecked",false);
                    editor.apply();
                }
            }
        });
    }



    public void AppliCationType(){
       if (APK_TYPE==1) {
           cardView.setVisibility(View.VISIBLE);
           layout.setVisibility(View.VISIBLE);
           copyRight.setVisibility(View.VISIBLE);
           hideSocialMenuItem(true);
       }else if (APK_TYPE==2){
           hideSocialMenuItem(true);
           cardView.setVisibility(View.GONE);
           layout.setVisibility(View.GONE);
           copyRight.setVisibility(View.VISIBLE);
       }else if (APK_TYPE==3){
           hideSocialMenuItem(false);
           cardView.setVisibility(View.VISIBLE);
           layout.setVisibility(View.VISIBLE);
           copyRight.setVisibility(View.GONE);
       }else if (APK_TYPE==4){
           hideSocialMenuItem(false);
           cardView.setVisibility(View.GONE);
           layout.setVisibility(View.GONE);
           copyRight.setVisibility(View.GONE);
       }
    }
    public void hideSocialMenuItem(boolean hide){
        nav_Menu.findItem(R.id.social).setVisible(hide);
        nav_Menu.findItem(R.id.support_dev).setVisible(hide);

    }
    private void startVpn() {
        Username=username.getText().toString();
        password=Password.getText().toString();
        String config = "";
        try {
            InputStream conf = getAssets().open("CL");
            InputStreamReader isr = new InputStreamReader(conf);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                config += line + "\n";
            }
        } catch (IOException ignore) {
        }
        try {
            if (Username.equals("")){
                Toast.makeText(this, R.string.empty_pass_user, Toast.LENGTH_SHORT).show();
            }else {
                OpenVpnApi.startVpn(this, config, Username, password);
                pfs = getSharedPreferences("SavePSW",MODE_PRIVATE);
                SharedPreferences.Editor editor=pfs.edit();
                editor.putString("Username",username.getText().toString());
                editor.putString("Password",Password.getText().toString());
                editor.putBoolean("isChecked",true);
                editor.apply();
            }
        } catch (RemoteException ignored) {
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.ic_facebook) {

            String pageId="100002101803783";
            Intent intent;
            try {
                startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + pageId)));
            } catch (Exception e) {
                startActivity (new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + pageId)));
            }

        } else if (id == R.id.ic_instgram) {

            Uri uri = Uri.parse("http://instagram.com/_u/ahmadbkye12");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/ahmadbkye12")));
            }
        } else if (id == R.id.ic_wordpress) {
            startActivity (new Intent(Intent.ACTION_VIEW, Uri.parse("https://abdrzjsy.wordpress.com/")));

        } else if (id == R.id.ic_gmail) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","alrazj.abdulkader@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ArabsNux.com");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "سلام عليكم");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "سلام عليكم تطبيق جميل ورائع تفضل حمله الان !  https://play.google.com/store/apps/details?id=arabs.nux.vpn");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.ic_about) {
            Intent intent = new Intent(Main.this,About_us.class);
            intent.putExtra("APK_TYPE",APK_TYPE);
            startActivity( intent);

        }else if (id == R.id.blance_bma) {
                Intent i = new Intent(Main.this,UserRaside.class);
                i.putExtra("TYPE",1);
                startActivity(i);

        }else if (id == R.id.blance_user) {
            Intent i = new Intent(Main.this,UserRaside.class);
            i.putExtra("TYPE",2);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void LoadInterstitialAd(){

        MobileAds.initialize(this,
                arbnuxAppID);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(arabnuxADsID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }
    public void showInterstitialAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.i("showInterstitialAd", "The interstitial wasn't loaded yet.");

        }
    }



}
