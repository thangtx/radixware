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

package org.radixware.kernel.common.defs.dds.providers;

import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;


public class DdsApfHeadProvider extends VisitorProvider {

    public DdsApfHeadProvider() {
    }
    
    public static interface ProblemDdsApfHead{
        public boolean isNotGeneratedInDb();
        public boolean isView();
        public boolean isInvalidPrimaryKey();
    }
    private ProblemDdsApfHead problemDdsApfHead;

    public void setProblemDdsApfHead(ProblemDdsApfHead problemDdsApfHead) {
        this.problemDdsApfHead = problemDdsApfHead;
    }
    
    @Override
    public boolean isTarget(RadixObject object) {
        if (object instanceof IEnumDef) {
            return true;
        }
        if (object instanceof DdsTableDef) {
            final DdsTableDef table = (DdsTableDef) object;
            if (!table.isGeneratedInDb()) {
                if (problemDdsApfHead != null){
                    problemDdsApfHead.isNotGeneratedInDb();
                }
                return false;
            }

            if (table instanceof DdsViewDef) {
                if (problemDdsApfHead != null){
                    problemDdsApfHead.isView();
                }
                return false;
            }

            if (table.getPrimaryKey().getColumnsInfo().size() != 1) {
                if (problemDdsApfHead != null){
                    problemDdsApfHead.isInvalidPrimaryKey();
                }
                return false;
            }

            return true;
        }
        return false;
    }
}
