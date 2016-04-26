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
package org.radixware.kernel.designer.environment.merge;

import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsModule;

public class DdsCopyMergePanel extends CopyMergePanel {

    public DdsCopyMergePanel(final DdsMergeChangesOptions options) {
        setOptions(options);
    }

    private DdsCopyMergeTable table;

    protected CopyMergeTable createTable() {
        return new DdsCopyMergeTable();
    }

    @Override
    protected CopyMergeTable recreateTable() {
        return table = new DdsCopyMergeTable();
    }

    @Override
    protected void openTable(final AbstractMergeChangesOptions options) {
        final DdsMergeChangesOptions ddsOptions = (DdsMergeChangesOptions) options;
        table.open(ddsOptions);
    }

    DdsMergeChangesOptions getCurrOptions() {
        return table.options;
    }

    @Override
    protected void mergeToAnotherBranch() {
        try {
            table.options.captureDestinationModules();

            final List<DdsModule> moduleList = table.options.getNatureDefList();

            if (moduleList.isEmpty()) {
                MergeUtils.messageError("Definition list is empty.");
                return;
            }

            final DdsMergeChangesOptions options = MergeUtils.collectDdsOptions(moduleList);
            if (options != null) {
                setOptions(options);
            }
        } catch (Exception ex) {
            MergeUtils.messageError(ex);
        }
    }

}
