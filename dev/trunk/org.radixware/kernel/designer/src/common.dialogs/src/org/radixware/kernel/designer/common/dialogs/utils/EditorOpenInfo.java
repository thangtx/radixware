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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class EditorOpenInfo extends OpenInfo {

    private final boolean readOnly;

    public static EditorOpenInfo DEFAULT_EDITABLE = new EditorOpenInfo(false, Lookup.EMPTY);
    public static EditorOpenInfo DEFAULT_READONLY = new EditorOpenInfo(true, Lookup.EMPTY);

    public EditorOpenInfo(boolean readOnly, Lookup lookup) {
        super(null, lookup == null ? Lookup.EMPTY : lookup);
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public <T extends Definition> T lookupDefinitionById(Class<T> clazz, Id id) {
        for (T def : getLookup().lookupAll(clazz)) {
            if (Utils.equals(def.getId(), id)) {
                return def;
            }
        }
        return null;
    }
}
