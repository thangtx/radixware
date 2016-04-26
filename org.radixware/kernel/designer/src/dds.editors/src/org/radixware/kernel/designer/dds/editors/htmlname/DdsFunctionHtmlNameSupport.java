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

package org.radixware.kernel.designer.dds.editors.htmlname;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;


public class DdsFunctionHtmlNameSupport extends DdsPlSqlItemHtmlNameSupport {
    private final HtmlNameSupport functionProfileHtmlNameSupport;
    private final IRadixEventListener profileChangeListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            fireChanged();
        }
    };

    public DdsFunctionHtmlNameSupport(DdsFunctionDef function) {
        super(function);
        
        final DdsFunctionProfile functionProfile = new DdsFunctionProfile(function);
        functionProfileHtmlNameSupport = HtmlNameSupportsManager.newInstance(functionProfile);
        functionProfileHtmlNameSupport.addEventListener(profileChangeListener);
    }

    @Override
    public String getDisplayName() {
        return functionProfileHtmlNameSupport.getDisplayName();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new DdsFunctionHtmlNameSupport((DdsFunctionDef) object);
        }
    }
}
