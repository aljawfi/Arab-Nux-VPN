/*
 * Copyright (c) 2017-2018 Abdulkader Alrazj
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */
package de.blinkt.openvpn.core;

import android.app.Application;

import de.blinkt.openvpn.BuildConfig;

/*
   created by Abdulkader Alrazj // www.facebook.com/AbdSenRzj
 */
public class ICSOpenVPNApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PRNGFixes.apply();
        if (BuildConfig.DEBUG) {
            //ACRA.init(this);
        }
        VpnStatus.initLogCache(getApplicationContext().getCacheDir());
    }
}
