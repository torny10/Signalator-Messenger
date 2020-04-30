package com.prism.lib.signal;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.prism.commons.utils.RecentUtils;
import com.prism.commons.utils.ScreenSecurityUtils;
import com.prism.commons.utils.Tag;
import com.prism.hider.vault.commons.Vault;
import com.prism.lib.vault.signal.VaultVariant;

import org.thoughtcrime.securesms.ApplicationContext;
import org.thoughtcrime.securesms.logging.Log;

public class GlobalActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = Tag.tag(GlobalActivityLifecycleCallbacks.class);
    private static final String META_KEY = "is_signal_activity";


    private boolean firstTime = true;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (firstTime) {
            firstTime = false;
            if (!VaultVariant.instance().isSetup(activity))
                VaultVariant.instance().setupVault(activity, false);
            RecentUtils.setHideFromRecentEnable(activity, SignalPreference.hideFromRecent.get(activity).read());
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {


        Log.d(TAG, "screencapture allowed:" + SignalPreference.allowScreenCapture.get(activity).read());
        ScreenSecurityUtils.setScreenCaptureAllowed(activity, SignalPreference.allowScreenCapture.get(activity).read());
        Vault vault = VaultVariant.instance();
        if (vault.isSetup(activity)) {
            PackageManager pm = activity.getPackageManager();
            try {
                ActivityInfo ai = pm.getActivityInfo(activity.getComponentName(),
                        PackageManager.GET_META_DATA);
                boolean isSignalActivity = ai.metaData != null && ai.metaData.getBoolean(META_KEY, false);
                if (isSignalActivity)
                    VaultVariant.instance().certificateVault(activity);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "onActivityResumed", e);
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        VaultVariant.instance().releaseIfScreenOff(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
