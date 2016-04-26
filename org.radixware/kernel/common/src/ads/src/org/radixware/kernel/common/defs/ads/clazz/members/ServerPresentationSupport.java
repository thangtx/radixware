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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class ServerPresentationSupport {

    final AdsPropertyDef property;
    PropertyPresentation presentation;

    protected ServerPresentationSupport(AdsPropertyDef property, AbstractPropertyDefinition xProp) {
        this.property = property;
        if (xProp.isSetPresentation()) {
            this.presentation = PropertyPresentation.Factory.loadFrom(property, xProp.getPresentation());
        } else {
            this.presentation = PropertyPresentation.Factory.newInstance(property);
        }
    }

    protected ServerPresentationSupport(AdsPropertyDef property) {
        this.property = property;

        this.presentation = PropertyPresentation.Factory.newInstance(property);
    }

    protected ServerPresentationSupport(AdsPropertyDef property, ServerPresentationSupport source, boolean forOverride) {
        this.property = property;
        if (forOverride) {
            this.presentation = PropertyPresentation.Factory.newOverride(property, source.presentation);
        } else {
            this.presentation = PropertyPresentation.Factory.newOverwrite(property, source.presentation);
        }
    }

    public PropertyPresentation getPresentation() {
        return presentation;
    }

    void checkPresentation() {
        if (this.presentation != null) {
            this.presentation = PropertyPresentation.Factory.redefine(property, presentation);
        }
        property.setEditState(EEditState.MODIFIED);
    }

    public void appendTo(AbstractPropertyDefinition xDef, ESaveMode saveMode) {
        if (presentation != null) {
            presentation.appendTo(xDef.addNewPresentation(), saveMode);
        }
    }

    public void afterOverride() {
        if (presentation != null) {
            presentation.afterOverride();
        }
    }

    public void afterOverwrite() {
        if (presentation != null) {
            presentation.afterOverride();
        }
    }
}
