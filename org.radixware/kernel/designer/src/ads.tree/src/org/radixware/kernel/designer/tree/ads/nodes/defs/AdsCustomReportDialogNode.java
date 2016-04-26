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

import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomReportDialogDef;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;


class AdsCustomReportDialogNode extends AdsObjectNode<AdsCustomReportDialogDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsCustomReportDialogDef> {

        @Override
        public Node newInstance(AdsCustomReportDialogDef object) {
            return new AdsCustomReportDialogNode(object);
        }
    }

    private AdsCustomReportDialogNode(AdsCustomReportDialogDef definition) {
        super(definition, Children.LEAF);

    }
}
