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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class AdsFilterParameterHtmlNameSupport extends HtmlNameSupport {

    private final HtmlNameSupport profileHtmlNameSupport;
    private final IRadixEventListener<RadixEvent> profileChangeListener = new IRadixEventListener<RadixEvent>() {
        @Override
        public void onEvent(RadixEvent e) {
            fireChanged();
        }
    };

    protected AdsFilterParameterHtmlNameSupport(AdsFilterDef.Parameter property) {
        super(property);
        profileHtmlNameSupport = HtmlNameSupportsManager.newInstance(property.getProfile());
        profileHtmlNameSupport.addEventListener(profileChangeListener);
    }

    @Override
    public String getDisplayName() {
        return profileHtmlNameSupport.getDisplayName();
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
            return new AdsFilterParameterHtmlNameSupport((AdsFilterDef.Parameter) object);
        }
    }
}
