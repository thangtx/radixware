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

package org.radixware.kernel.common.msdl;

import java.util.prefs.Preferences;
import org.radixware.kernel.common.utils.Utils;

/**
 *
 * @author npopov
 */
public final class MsdlSettings {
    
    private final static String RADIX_MSDL_SETTINGS_KEY = "RadixMsdlSettings";
    private final static  Preferences prefs = Utils.findOrCreatePreferences(RADIX_MSDL_SETTINGS_KEY);
    private final static MsdlSettings INSTANCE = new MsdlSettings();
    
    private MsdlSettings() {
        //Singletone
    }
    
    public static MsdlSettings getInstance() {
        return INSTANCE;
    }
    
    public String get(String key) {
        return prefs.get(key, null);
    }
    
    public void set(String key, String value) {
        prefs.put(key, value);
    }
}
