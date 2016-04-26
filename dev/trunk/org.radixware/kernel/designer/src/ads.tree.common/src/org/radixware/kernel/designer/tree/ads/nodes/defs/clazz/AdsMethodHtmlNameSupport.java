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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class AdsMethodHtmlNameSupport extends HtmlNameSupport {

    private final IRadixEventListener<RadixEvent> profileChangeListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(RadixEvent e) {
            fireChanged();
        }
    };
    private HtmlNameSupport profileHtmlNameSupport = null;
    private AdsMethodDef.Profile lastUsedProfile = null;

    private HtmlNameSupport getProfileHtmlNameSupport() {
        final AdsMethodDef.Profile profile = getRadixObject().getProfile();

        if (profileHtmlNameSupport == null) {
            profileHtmlNameSupport = HtmlNameSupportsManager.newInstance(profile);
            profileHtmlNameSupport.addEventListener(profileChangeListener);
            profile.getAccessFlags().getAccessFlagsChangesSupport().addEventListener(profileChangeListener);

        } else {
            if (lastUsedProfile != profile) {
                profileHtmlNameSupport = HtmlNameSupportsManager.newInstance(profile);
                profileHtmlNameSupport.addEventListener(profileChangeListener);
                lastUsedProfile.getAccessFlags().getAccessFlagsChangesSupport().removeEventListener(profileChangeListener);
                profile.getAccessFlags().getAccessFlagsChangesSupport().addEventListener(profileChangeListener);
            }
        }
        lastUsedProfile = profile;
        return profileHtmlNameSupport;
    }

    @Override
    public AdsMethodDef getRadixObject() {
        return ((AdsMethodDef) super.getRadixObject());
    }

    @SuppressWarnings("unchecked")
    protected AdsMethodHtmlNameSupport(AdsMethodDef method) {
        super(method);

    }

    @Override
    public String getDisplayName() {
        return getProfileHtmlNameSupport().getDisplayName();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        /**
         * Registeren in layer.xml
         */
        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new AdsMethodHtmlNameSupport((AdsMethodDef) object);
        }
    }
}
