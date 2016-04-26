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

package org.radixware.kernel.common.defs.ads.clazz;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.IPlatformClassPublishingSupport;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.PlatformClassPublisherChangesListener;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.PlatformClassPublishingChangedEvent;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AccessRules.Transparence;


class PlatformClassPublishingSupport implements IPlatformClassPublishingSupport {

    private IPlatformClassPublisher owner;
    private AdsTransparence classTransparence = null;
    private RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> changesSupport = null;
    private IRadixEventListener transparentListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            synchronized (PlatformClassPublishingSupport.this) {
                if (changesSupport != null) {
                    changesSupport.fireEvent(new PlatformClassPublishingChangedEvent(owner));
                }
            }
        }
    };

    @SuppressWarnings("unchecked")
    PlatformClassPublishingSupport(IPlatformClassPublisher owner) {
        this.owner = owner;
        if (owner instanceof AdsClassDef) {
            classTransparence = ((AdsClassDef) owner).getTransparence();
            if (classTransparence != null) {
                classTransparence.getTransparenceChangeSupport().addEventListener(transparentListener);
            }
        } else {
            throw new DefinitionError("Invalid usage: AdsClassDef instance required");
        }
    }

    @Override
    public RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> getPlatformClassPublishingChengesSupport() {
        synchronized (this) {
            if (changesSupport == null) {
                changesSupport = new RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent>();
            }
            return changesSupport;
        }
    }

    @Override
    public boolean isPlatformClassPublisher() {
        return classTransparence != null && classTransparence.isTransparent();
    }

    @Override
    public String getPlatformClassName() {
        if (classTransparence != null) {
            return classTransparence.getPublishedName();
        } else {
            return null;
        }
    }

    @Override
    public boolean isExtendablePublishing() {
        if (classTransparence != null) {
            return classTransparence.isExtendable();
        } else {
            return false;
        }
    }
}
