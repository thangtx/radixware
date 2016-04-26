/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.designer.common.editors.module.images;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ModuleImagesEditorOptions {

    private static final ModuleImagesEditorOptions INSTANCE = new ModuleImagesEditorOptions();
    private final RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> changeSupport = new RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent>();
    private boolean showNames = false;
    private boolean originalSize = false;
    private String filePath = "";

    public static ModuleImagesEditorOptions getDefault() {
        return INSTANCE;
    }

    private ModuleImagesEditorOptions() {
        super();
        load();
    }
    private static final String PREFERENCES_KEY = "ModuleImagesEditorOptions";
    private static final String SHOW_NAMES = "ShowNames";
    private static final String ORIGINAL_SIZE = "OriginalSize";
    private static final String FILE_PATH = "FilePath";

    private void load() {
        try {
            final Preferences preferences = Utils.findPreferences(PREFERENCES_KEY);
            if (preferences == null) {
                return;
            }

            this.showNames = preferences.getBoolean(SHOW_NAMES, showNames);
            this.originalSize = preferences.getBoolean(ORIGINAL_SIZE, originalSize);
            this.filePath = preferences.get(FILE_PATH, "");
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private void save() {
        final Preferences preferences = Utils.findOrCreatePreferences(PREFERENCES_KEY);
        preferences.putBoolean(SHOW_NAMES, showNames);
        preferences.putBoolean(ORIGINAL_SIZE, originalSize);
        preferences.put(FILE_PATH, filePath);

        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private void fireChanged() {
        changeSupport.fireEvent(new RadixEvent());
    }

    public synchronized void addChangeListener(IRadixEventListener<RadixEvent> listener) {
        changeSupport.addEventListener(listener);
    }

    public synchronized void removeChangeListener(IRadixEventListener<RadixEvent> listener) {
        changeSupport.removeEventListener(listener);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        assert filePath != null;
        if (!this.filePath.equals(filePath)) {
            this.filePath = filePath;
            fireChanged();
            save();
        }
    }
    
    public boolean isShowNames() {
        return showNames;
    }

    public void setShowNames(boolean showNames) {
        if (this.showNames != showNames) {
            this.showNames = showNames;
            fireChanged();
            save();
        }
    }

    public boolean isOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(boolean originalSize) {
        if (this.originalSize != originalSize) {
            this.originalSize = originalSize;
            fireChanged();
            save();
        }
    }

}
