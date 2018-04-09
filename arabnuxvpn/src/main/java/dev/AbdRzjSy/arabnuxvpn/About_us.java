package dev.AbdRzjSy.arabnuxvpn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Locale;

import de.blinkt.openvpn.R;

/*
   created by Abdulkader Alrazj // www.facebook.com/AbdSenRzj
 */

public class About_us extends AppCompatActivity {
    private int APK_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent=getIntent();
        APK_TYPE = intent.getIntExtra("APK_TYPE",1);
        copyRight();




    }

    private void copyRight() {
        TextView copyRight = (TextView) findViewById(R.id.copyRight);
        if (APK_TYPE==1 || APK_TYPE==2){
            copyRight.setVisibility(View.GONE);
        }else if (APK_TYPE==3 || APK_TYPE==4){
            copyRight.setVisibility(View.VISIBLE);
        }
        if (Locale.getDefault().getLanguage().equals("ar")){
            copyRight.setText("جميع الحقوق محفوظة لفريق عمل  Arabnux صاحب تطبيق يرجوا من الله المغفرة والعفو ومنكم بدعوة صالحة في ظهر الغيب");
        }else {
            copyRight.setText("All rights reserved to Arabnux team work . the application's owner hopes from you to remember him in your prayers");
        }
    }




}


