package dev.AbdRzjSy.arabnuxvpn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.blinkt.openvpn.R;
/*
   created by Abdulkader Alrazj // www.facebook.com/AbdSenRzj
 */
public class UserRaside extends AppCompatActivity {
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_raside);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String url="";
        int type = intent.getIntExtra("TYPE",1);
        if (type==1){
            url=Constent.URL_DMA;
        }else if (type==2){
            url=Constent.URL_USER_MANAGER;
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webview=(WebView)findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());
        openURL(url);
    }
    private void openURL(String url) {

        webview.loadUrl(url);
        webview.requestFocus();
    }
}
