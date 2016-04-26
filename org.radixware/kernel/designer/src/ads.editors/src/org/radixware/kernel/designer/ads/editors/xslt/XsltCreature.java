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

package org.radixware.kernel.designer.ads.editors.xslt;

import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.xslt.AdsXsltDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;


public class XsltCreature extends AdsNamedDefinitionCreature<AdsXsltDef> {

    public XsltCreature(AdsModule container) {
        super(container.getDefinitions(),"NewXSLTTransformation","XSLT Transformation");
    }

    @Override
    public AdsXsltDef createInstance() {
        return AdsXsltDef.Factory.newInstance();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.XML_TRANSFORMATION;
    }


}
