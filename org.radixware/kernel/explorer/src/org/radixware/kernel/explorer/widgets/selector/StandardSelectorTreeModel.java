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

package org.radixware.kernel.explorer.widgets.selector;

import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public class StandardSelectorTreeModel extends SelectorModel {//DBP-1649

    private final Id explorerItemId;

    public StandardSelectorTreeModel(final GroupModel group, final Id explorerItemId) {
        super(group);
        this.explorerItemId = explorerItemId;
    }

    @Override
    protected GroupModel createChildGroupModel(EntityModel parentEntity) {
        try {
            return (GroupModel) parentEntity.getChildModel(explorerItemId);
        } catch (ServiceClientException ex) {
            throw new RadixError("can't get child group model by explorer item #" + explorerItemId, ex);
        } catch (InterruptedException ex) {
            return null;
        }
    }

    @Override
    protected boolean hasChildren(EntityModel parent) {
        return true;
    }
}
