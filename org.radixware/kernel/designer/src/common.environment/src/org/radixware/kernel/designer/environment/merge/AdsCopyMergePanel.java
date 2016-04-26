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
import org.radixware.kernel.common.defs.Definition;

public class AdsCopyMergePanel extends CopyMergePanel {

    private AdsMergeChangesOptions options;
    private AdsCopyMergeTable table;

    AdsCopyMergePanel(final AdsMergeChangesOptions options) {
        setOptions(options);
    }

    @Override
    final protected void setOptions(final AbstractMergeChangesOptions options) {
        super.setOptions(this.options = (AdsMergeChangesOptions) options);
    }

    @Override
    protected CopyMergeTable recreateTable() {
        return table = new AdsCopyMergeTable();
    }

    @Override
    protected void openTable(final AbstractMergeChangesOptions options) {
        final AdsMergeChangesOptions adsOptions = (AdsMergeChangesOptions) options;
        table.open(adsOptions);
    }

    @Override
    protected void mergeToAnotherBranch() {
        try {
            final List<Definition> defList = options.getNatureDefList();

            if (defList.isEmpty()) {
                MergeUtils.messageError("Definition list is empty.");
                return;
            }

            final AdsMergeChangesOptions options = MergeUtils.collectAdsOptions(defList);
            if (options != null) {
                setOptions(options);
            }
        } catch (Exception ex) {
            MergeUtils.messageError(ex);
        }
    }
}
