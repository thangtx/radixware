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
import org.radixware.kernel.common.defs.dds.DdsCustomTextDef;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;


public class DdsCustomTextHtmlNameSupport extends DdsPlSqlItemHtmlNameSupport {

    public DdsCustomTextHtmlNameSupport(DdsCustomTextDef customText) {
        super(customText);
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
            return new DdsCustomTextHtmlNameSupport((DdsCustomTextDef) object);
        }
    }
}
