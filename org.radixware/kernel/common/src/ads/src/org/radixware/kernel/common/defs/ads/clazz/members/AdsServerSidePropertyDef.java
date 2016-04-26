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

import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;

import org.radixware.schemas.adsdef.PropertyDefinition;
import org.radixware.schemas.adsdef.UsageDescription;


public abstract class AdsServerSidePropertyDef extends AdsPropertyDef implements IAdsPresentableProperty {

    private final ServerPresentationSupport presentationSupport;

    protected AdsServerSidePropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        presentationSupport = new ServerPresentationSupport(this, xProp);
    }

    protected AdsServerSidePropertyDef(Id id, String name) {
        super(id, name);
        presentationSupport = new ServerPresentationSupport(this);
    }

    protected AdsServerSidePropertyDef(AdsServerSidePropertyDef source, boolean forOverride) {
        super(source, forOverride);
        presentationSupport = new ServerPresentationSupport(this, source.getPresentationSupport(), forOverride);

    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        presentationSupport.appendTo(xDef, saveMode);
    }

    @Override
    public void appendToUsage(UsageDescription xDef) {
        super.appendToUsage(xDef);
        presentationSupport.appendTo(xDef.getProperty(), ESaveMode.USAGE);
    }

    @Override
    public void afterOverride() {
        super.afterOverride();
        presentationSupport.afterOverride();
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        presentationSupport.afterOverwrite();
    }

    @Override
    public ServerPresentationSupport getPresentationSupport() {
        return presentationSupport;
    }
}
