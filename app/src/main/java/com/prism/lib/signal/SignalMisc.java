package com.prism.lib.signal;

import com.prism.commons.utils.StoreUtils;

import org.thoughtcrime.securesms.BuildConfig;

public class SignalMisc {

    public static String getInviteUrl() {
        return StoreUtils.getPlayUrl(BuildConfig.APPLICATION_ID);
    }
}
