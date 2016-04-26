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

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;


public class AdsClassHtmlNameSupport extends HtmlNameSupport {

    private final IRadixEventListener<RadixEvent> profileChangeListener = new IRadixEventListener<RadixEvent>() {
        @Override
        public void onEvent(RadixEvent e) {
            fireChanged();
        }
    };

    @SuppressWarnings("unchecked")
    protected AdsClassHtmlNameSupport(AdsClassDef clazz) {
        super(clazz);
        clazz.getAccessFlags().getAccessFlagsChangesSupport().addEventListener(profileChangeListener);

    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public String getTreeDisplayName() {
        RadixObject obj = getRadixObject();
        if (obj instanceof AdsUserReportClassDef) {
            Module module = obj.getModule();
            if (!(module instanceof UdsModule)) {
                String name = obj.getName();
                int index = name.lastIndexOf(".");
                if (index > 0) {
                    return new String(name.substring(index + 1));
                }
                return name;
            }
        }
        return super.getDisplayName();
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
            return new AdsClassHtmlNameSupport((AdsClassDef) object);
        }
    }
}
