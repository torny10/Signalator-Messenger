package com.prism.lib.signal;

import com.prism.commons.model.PreferenceModelHolder;
import com.prism.commons.utils.InitOnceP;
import com.prism.commons.utils.PreferenceWrapper;

public class ExtSignalPreference {

    private static final String PREFERENCE_NAME = "signal_preference";

    private static final String PREFERENCE_KEY_LAUNCH_COUNT = "PREFERENCE_KEY_LAUNCH_COUNT";
    private static final String PREFERENCE_KEY_NEVER_SHOW_RATEUS = "PREFERENCE_KEY_NEVER_SHOW_RATEUS";
    private static int  launchCount = -1;

    private static InitOnceP<PreferenceWrapper, Void> wrapperHolder = new InitOnceP<PreferenceWrapper, Void>(
            v -> new PreferenceWrapper(PREFERENCE_NAME));




//    private static final String ALLOW_SCREEN_CAPTURE = "ALLOW_SCREEN_CAPTURE";
//    public static PreferenceModelHolder<Boolean> allowScreenCapture = new PreferenceModelHolder<>(
//            wrapperHolder.get(null), ALLOW_SCREEN_CAPTURE, true,
//            Boolean.class);

    private static final String HIDE_FROM_RECENT = "HIDE_FROM_RECENT";
    public static PreferenceModelHolder<Boolean> hideFromRecent = new PreferenceModelHolder<>(
            wrapperHolder.get(null), HIDE_FROM_RECENT, false,
            Boolean.class);


    private static final String SHOW_VAULT_SETUP_REMINDER = "SHOW_VAULT_SETUP_REMINDER";
    public static PreferenceModelHolder<Boolean> showVaultSetupReminder = new PreferenceModelHolder<>(
            wrapperHolder.get(null), SHOW_VAULT_SETUP_REMINDER, true,
            Boolean.class);
}
