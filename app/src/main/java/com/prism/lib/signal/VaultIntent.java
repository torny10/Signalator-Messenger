package com.prism.lib.signal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.prism.commons.utils.Tag;
import com.prism.hider.vault.commons.Vault;
import com.prism.hider.vault.commons.VaultEntry;
import com.prism.hider.vault.commons.VaultEntryManager;
import com.prism.lib.vault.signal.VaultVariant;

import org.thoughtcrime.securesms.logging.Log;

public class VaultIntent {

    private static final String TAG = Tag.tag(VaultIntent.class);

    public static Intent vaultIntentIfNeeded(Context context,Intent intent) {
        Vault vault = VaultVariant.instance();
        if (!vault.isSetup(context))
            return intent;
        VaultEntryManager manager = vault.getVaultEntryManager(context);
        VaultEntry originEntry = manager.getOriginEntry();
        ComponentName originComponent = originEntry.getComponentName();
        ComponentName intentComponent = intent.getComponent();
        Log.d(TAG, "origin:" + originComponent.getClassName()
                + " intent:" + intentComponent.getClassName());
        if (originComponent.equals(intentComponent)) {
            Log.d(TAG, "origin component match");
            intent.setComponent(manager.findDisguiseEntry(context).getComponentName());
        }
        return intent;
    }
}
