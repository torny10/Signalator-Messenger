package org.thoughtcrime.securesms.preferences;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;

import com.prism.commons.utils.RecentUtils;
import com.prism.commons.utils.ScreenSecurityUtils;
import com.prism.commons.utils.Tag;
import com.prism.hider.vault.commons.FingerprintUtils;
import com.prism.hider.vault.commons.Vault;
import com.prism.hider.vault.commons.certifier.FingerprintCertifier;
import com.prism.lib.signal.ExtSignalPreference;
import com.prism.lib.vault.signal.VaultVariant;

import org.thoughtcrime.securesms.ApplicationPreferencesActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.components.SwitchPreferenceCompat;

public class DisguisePreferenceFragment extends CorrectedPreferenceFragment {
    private static final String TAG = Tag.tag(DisguisePreferenceFragment.class);
    private static final String PREF_DISGUISE_MODE = "pref_enable_disguise_mode";
    private static final String PREF_RESET_PIN = "pref_reset_pin";
    private static final String PREF_USE_FINGERPRINT = "pref_use_fingerprint";
    private static final String PREF_SCREEN_SECURITY = "pref_screen_security";
    private static final String PREF_HIDE_FROM_RECENT = "pref_hide_from_recent";

    public static CharSequence getSummary(Context context) {

        final String on                 = context.getString(R.string.ApplicationPreferencesActivity_on);
        final String off                = context.getString(R.string.ApplicationPreferencesActivity_off);
        final String description =  context.getString(R.string.common_setting_desc_disguise_mode);
        if (VaultVariant.instance().isSetup(context))
            return on + "  " + description;
        else
            return off + "  " + description;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_disguise);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }


    private void initializeVaultPrefs() {
        Vault vault = VaultVariant.instance();
        boolean isSetup = vault.isSetup(getContext());
        boolean isSupportFingerprint  = FingerprintUtils.isSupportFingerprint(getContext());
        SwitchPreferenceCompat disguiseModePref = (SwitchPreferenceCompat) findPreference(PREF_DISGUISE_MODE);
        Preference resetPinPref = findPreference(PREF_RESET_PIN);
        SwitchPreferenceCompat useFingerprintPref = (SwitchPreferenceCompat) findPreference(PREF_USE_FINGERPRINT);
        SwitchPreferenceCompat hideFromRecentPref = (SwitchPreferenceCompat) findPreference(PREF_HIDE_FROM_RECENT);

        resetPinPref.setEnabled(isSetup);
        useFingerprintPref.setEnabled(isSetup && isSupportFingerprint);


        disguiseModePref.setChecked(isSetup);
        disguiseModePref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (Boolean)newValue;
            if (checked)
                vault.setupVault(getActivity(), false);
            else
                vault.disableVault(getActivity());
            return checked;
        });

        resetPinPref.setOnPreferenceClickListener(preference -> {
            vault.setupVault(getActivity(), true);
            return true;
        });

        boolean useFingerprint = isSetup && isSupportFingerprint && FingerprintCertifier.instance().isEnable(getContext());
        useFingerprintPref.setChecked(useFingerprint);
        useFingerprintPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (Boolean) newValue;
            FingerprintCertifier.instance().setEnable(getContext(), checked);
            return true;
        });


    }

    private void initializeScreenSecurity() {
        SwitchPreferenceCompat screenSecurityPref = (SwitchPreferenceCompat) findPreference(PREF_SCREEN_SECURITY);
        screenSecurityPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (Boolean) newValue;
            ScreenSecurityUtils.setScreenCaptureAllowed(getActivity(), !checked);
            return true;
        });
    }

    private void initializeHideFromRecent() {
        SwitchPreferenceCompat hideFromRecentPref = (SwitchPreferenceCompat) findPreference(PREF_HIDE_FROM_RECENT);
        boolean hide = ExtSignalPreference.hideFromRecent.get(getContext()).read();
        hideFromRecentPref.setChecked(hide);
        hideFromRecentPref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean checked = (Boolean) newValue;
            ExtSignalPreference.hideFromRecent.get(getContext()).save(checked);
            RecentUtils.setHideFromRecentEnable(getContext(), checked);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeVaultPrefs();
        initializeScreenSecurity();
        initializeHideFromRecent();
        ((ApplicationPreferencesActivity) getActivity()).getSupportActionBar()
                .setTitle(R.string.common_setting_disguise_setting);
    }
}
