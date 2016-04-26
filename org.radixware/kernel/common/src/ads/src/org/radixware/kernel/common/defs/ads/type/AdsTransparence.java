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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AccessRules;

/**
 * Transparence suppoert for ads definition
 *
 */
public class AdsTransparence extends RadixObject {

    public static boolean isTransparent(ITransparency transparency) {
        return transparency != null && transparency.getTransparence() != null && transparency.getTransparence().isTransparent();
    }

    /**
     * Checks that {@code transparency} is transparent and return value of
     * method {@code isExtendable} is equal to {@code extendable}.
     *
     * @param transparency instance of ITransparency
     * @param extendable the {@code Boolean} value, determines whether the
     * transparency is extendable.
     *
     * @return true if {@code transparency} is transparent and return value of
     * method {@link isExtendable} is equal to {@code extendable}, false
     * otherwise.
     */
    public static boolean isTransparent(ITransparency transparency, boolean extendable) {
        if (transparency == null) {
            return false;
        }
        final AdsTransparence transparence = transparency.getTransparence();
        return transparence != null && transparence.isTransparent()
            && (extendable ? transparence.isExtendable() : !transparence.isExtendable());
    }

    public static final class Factory {

        public static AdsTransparence newInstance(AdsDefinition context, String publishedName, boolean isExtendable) {
            return new AdsTransparence(context, publishedName, isExtendable);
        }

        public static AdsTransparence newInstance(AdsDefinition context) {
            return new AdsTransparence(context, null, false);
        }

        public static AdsTransparence loadFrom(AdsDefinition context, AccessRules xDef) {
            if (xDef.getTransparence() != null) {
                return new AdsTransparence(context, xDef.getTransparence());
            } else {
                return newInstance(context, null, false);
            }
        }
    }

    private boolean isExtendable;
    private String publishedName;

    protected AdsTransparence(AdsDefinition context, String publishedName, boolean isExtendable) {
        setContainer(context);
        this.isExtendable = isExtendable;
        this.publishedName = publishedName;
    }

    protected AdsTransparence(AdsDefinition context, AccessRules.Transparence xDef) {
        setContainer(context);
        if (xDef == null) {
            this.publishedName = null;
            this.isExtendable = false;
        } else {
            this.isExtendable = xDef.getExtendable();
            if (xDef.getPublishedName() != null && !xDef.getPublishedName().isEmpty()) {
                this.publishedName = xDef.getPublishedName();
            }
        }
    }

    public void appendTo(AccessRules.Transparence xDef) {
        xDef.setExtendable(isExtendable);
        if (publishedName != null) {
            xDef.setPublishedName(publishedName);
        }
    }

    public boolean isTransparent() {
        return publishedName != null;
    }

    public boolean isExtendable() {
        return isExtendable;
    }

    public void setIsExtendable(boolean isExtendable) {
        this.isExtendable = isExtendable;
        transparenceChanged();
        setEditState(EEditState.MODIFIED);
    }

    public String getPublishedName() {
        return publishedName;
    }

    public void setPublishedName(String publishedName) {
        this.publishedName = publishedName;
        transparenceChanged();
        setEditState(EEditState.MODIFIED);
    }

    public AdsTransparence newCopy(AdsDefinition context) {
        return new AdsTransparence(context, publishedName, isExtendable);
    }

    @SuppressWarnings("unchecked")
    private void transparenceChanged() {
        synchronized (this) {
            if (transparenceChangeSupport != null) {
                transparenceChangeSupport.fireEvent(new RadixEvent());
            }
        }
    }
    private RadixEventSource transparenceChangeSupport = null;

    public RadixEventSource getTransparenceChangeSupport() {
        synchronized (this) {
            if (transparenceChangeSupport == null) {
                transparenceChangeSupport = new RadixEventSource();
            }
            return transparenceChangeSupport;
        }
    }
}
