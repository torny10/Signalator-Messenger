package org.thoughtcrime.securesms.components.reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.prism.lib.signal.ExtSignalPreference;
import com.prism.lib.vault.signal.VaultVariant;

import org.thoughtcrime.securesms.ApplicationPreferencesActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.util.TextSecurePreferences;

public class VaultSetupReminder extends Reminder {
    public VaultSetupReminder(Context context) {
        super(context.getString(R.string.ext_reminder_title_vault_setup),
                context.getString(R.string.ext_reminder_content_vault_setup));

        setOkListener(this::onOK);
        setDismissListener(this::onDismiss);
    }

    private void onOK(View view) {
        Context context = view.getContext();
        Intent intent = ApplicationPreferencesActivity.getDisguisePreferenceIntent(context);
        context.startActivity(intent);

    }
    private void onDismiss(View view) {
        ExtSignalPreference.showVaultSetupReminder.get(view.getContext()).save(false);
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setTitle(R.string.ext_dialog_title_dismiss_vault_setup)
                .setMessage(R.string.ext_dialog_content_dismiss_vault_setup)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog1, which) -> {
                })
                .show();
    }


    public static boolean isEligible(Context context) {
        return (!VaultVariant.instance().isSetup(context)) &&
                ExtSignalPreference.showVaultSetupReminder.get(context).read();
    }

}
