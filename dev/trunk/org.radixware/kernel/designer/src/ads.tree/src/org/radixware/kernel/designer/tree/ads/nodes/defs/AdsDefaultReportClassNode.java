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

import java.util.List;
import javax.swing.Action;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.ads.editors.clazz.report.tree.AdsReportClassNode;


public class AdsDefaultReportClassNode extends AdsReportClassNode {

    public AdsDefaultReportClassNode(AdsReportClassDef definition) {
        super(definition);
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions); //To change body of generated methods, choose Tools | Templates.
        actions.add(null);
    }
}
