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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import org.radixware.kernel.designer.ads.editors.clazz.report.tree.AdsReportClassNode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public class AdsClassNode extends AdsAbstractClassNode {

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsClassDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(final AdsClassDef clazz) {
            if (clazz instanceof AdsAlgoClassDef) {
                return new AdsAlgoClassNode((AdsAlgoClassDef) clazz);
            } else if (clazz instanceof AdsEntityClassDef) {
                return new AdsEntityClassNode((AdsEntityClassDef) clazz);
            } else if (clazz instanceof AdsApplicationClassDef) {
                return new AdsEntityClassNode((AdsApplicationClassDef) clazz);
            } else if (clazz instanceof AdsPresentationEntityAdapterClassDef) {
                return new AdsAbstractClassNode(clazz);
            } else if (clazz instanceof AdsReportClassDef) {
                return new AdsDefaultReportClassNode((AdsReportClassDef) clazz);
            } else if (clazz instanceof AdsSqlClassDef) {
                return new AdsSqlClassNode((AdsSqlClassDef) clazz);
            } else if (clazz instanceof AdsFormHandlerClassDef) {
                return new AdsFormHandlerClassNode((AdsFormHandlerClassDef) clazz);
            } else if (clazz instanceof AdsEntityGroupClassDef) {
                return new AdsEntityGroupClassNode((AdsEntityGroupClassDef) clazz);
//            } else if (clazz instanceof AdsEnumClassDef) {
//                return new AdsEnumClassNode((AdsEnumClassDef) clazz);
            } else {
                return new AdsAbstractClassNode(clazz);
            }
        }
    }

    private AdsClassNode(AdsClassDef clazz) {
        super(clazz);
    }
}
