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

package org.radixware.kernel.server.sqml;

import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


public class Sqml extends org.radixware.kernel.common.sqml.Sqml {

    public static final class Factory {

        public static Sqml loadFrom(final String name, final org.radixware.schemas.xscml.Sqml xSqml) {
            if (xSqml == null || xSqml.getItemList() == null || xSqml.getItemList().isEmpty()) {
                return null;
            }
            final Sqml sqml = newInstance();
            sqml.loadFrom(xSqml);
            return sqml;
        }

        public static final Sqml newInstance() {
            return new Sqml();
        }

        public static final Sqml loadFrom(final String name, org.radixware.kernel.common.sqml.Sqml sqml) {
            final org.radixware.schemas.xscml.Sqml xSqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            sqml.appendTo(xSqml);
            return loadFrom(name, xSqml);
        }
    }

    protected Sqml() {
        super();
    }

    public void appendFrom(final org.radixware.kernel.common.sqml.Sqml sqml) throws XmlException {
        final org.radixware.schemas.xscml.Sqml xSqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        sqml.appendTo(xSqml);
        appendFrom(xSqml);
    }
    private boolean isReadonly = false;
    /**
     * Introduced for preprocessing Sqml depending on database parameters for
     * layer, but later separation of the database parameters by layer was
     * dropped out and it's no longer necessary to know layer for
     * preprocessing, so it's unused now.
     */
    private String layerUri = null;

    @Override
    protected void onModified() {
        if (isReadonly) {
            throw new IllegalUsageError("Readonly SQML was modified. Consequences aren't predictable.");
        }
    }

    public final void switchOnWriteProtection() {
        isReadonly = true;
    }

    public final void setLayerUri(final String uri) {
        this.layerUri = uri;
    }

    public String getLayerUri() {
        return layerUri;
    }
}
