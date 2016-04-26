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

package org.radixware.kernel.designer.common.dialogs.components.description;

import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;


abstract class DescriptionHandleInfo implements IDescriptionHandleInfo {

    static EDescriptionType getEditorModeFor(Object object) {

        if (object == null) {
            return null;
        }

        // for "reports designer"
        if (object instanceof IDescribable) {
            if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null) {
                return EDescriptionType.STRING;
            }
        }

        if (object instanceof ILocalizedDescribable) {

            if (((ILocalizedDescribable) object).getDescriptionId() != null) {
                return EDescriptionType.LOCALIZED;
            }

            if (object instanceof IDescribable) {
                final String description = ((IDescribable) object).getDescription();
                if (description == null || description.isEmpty()) {
                    return EDescriptionType.LOCALIZED;
                }
            } else {
                return EDescriptionType.LOCALIZED;
            }
        }
        return EDescriptionType.STRING;
    }

    static EDescriptionType getDefaultEditorModeFor(Object object) {

        if (object == null) {
            return null;
        }

        // for "reports designer"
        if (object instanceof IDescribable) {
            if (BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null) {
                return EDescriptionType.STRING;
            }
            if (object instanceof RadixObject) {
                Module m = ((RadixObject) object).getModule();
                if (m != null && m.getSegmentType() == ERepositorySegmentType.UDS) {
                    return EDescriptionType.STRING;
                }
            }
            return object instanceof ILocalizedDescribable ? EDescriptionType.LOCALIZED : EDescriptionType.STRING;
        }

        if (object instanceof ILocalizedDescribable) {
            return EDescriptionType.LOCALIZED;
        }
        return null;
    }
    private final boolean proxy;

    public DescriptionHandleInfo(boolean proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean localize(EIsoLanguage language) {
        return false;
    }

    @Override
    public boolean isLocalizable() {
        return false;
    }

    @Override
    public boolean isProxy() {
        return proxy;
    }

    @Override
    public ILocalizedDescribable getLocalizedDescribable() {
        return null;
    }

    @Override
    public IDescribable getDescribable() {
        return null;
    }
}
