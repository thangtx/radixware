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

/*
 * 9/20/11 5:16 PM
 */
package org.radixware.kernel.common.jml;

import org.radixware.kernel.common.scml.Scml.Item;

/**
 * Displayer for JML, to display human readable presentation
 */
public class JmlDisplayer {
    private Jml jml;

    public JmlDisplayer(Jml jml) {
        this.jml = jml;
    }

    public void setJml(Jml jml) {
        this.jml = jml;
    }

    public Jml getJml() {
        return jml;
    }
    
    @Override
    public String toString() {
        return toReadableString(jml);
    }
    
    public static String toReadableString(Jml jml) {
        if (jml == null) return "";
        
        StringBuilder sb = new StringBuilder();

        for (Item item : jml.getItems()) {
            if (item instanceof Jml.Tag) {
                sb.append(((Jml.Tag) item).getDisplayName());
            } else {
                sb.append(item.toString());
            }
        }
        return sb.toString();
    }
}