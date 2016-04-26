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

import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.IconSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.IIconSupportFactory;
import org.radixware.kernel.designer.common.general.displaying.IconSupport;

public class AdsMethodIconSupport extends IconSupport {

    @IconSupportFactoryRegistration
    public static class Factory implements IIconSupportFactory<AdsMethodDef> {

        @Override
        public IconSupport newInstance(final AdsMethodDef radixObject) {
            return new AdsMethodIconSupport(radixObject);
        }
    }
    private final IRadixEventListener<?> accListener = new IRadixEventListener<RadixEvent>() {
        @Override
        public void onEvent(final RadixEvent e) {
            notifyChanged();
        }
    };

    @SuppressWarnings("unchecked")
    public AdsMethodIconSupport(AdsMethodDef method) {
        super(method);
        method.getProfile().getAccessFlags().getAccessFlagsChangesSupport().addEventListener(accListener);
    }
}
