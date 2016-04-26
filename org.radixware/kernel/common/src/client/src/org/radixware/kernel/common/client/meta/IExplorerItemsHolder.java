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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.types.Id;

/**
 * Interface for definitions that may contains {@link RadExplorerItemDef explorer items}.
 */
public interface IExplorerItemsHolder extends IModelDefinition {   
    /**
     * Returns the (possibly empty) container of children explorer items.
     * @return children explorer items. Cannot be <code>null</code>.
     */
    public RadExplorerItems getChildrenExplorerItems();
    
    /**
     * Returns ordered (possibly empty) array of children explorer item identifiers.
     * @param parentId identifier of parent definition. Cannot be <code>null</code>.
     * @return array of children explorer item identifiers. Cannot be <code>null</code>.
     */
    public Id[] getChildrenExplorerItemsOrder(final Id parentId);
    
    /**
     * Returns <code>true<code> if explorer item with specified identifier is visible in explorer tree.
     * @param parentId identifier of parent definition. Cannot be <code>null</code>.
     * @param explorerItemId identifier of child explorer item. Cannot be <code>null</code>.
     * @return <code>true<code> if explorer item with <code>explorerItemId<code> is visible. Otherwise returns <code>false<code>.
     */    
    public boolean isExplorerItemVisible(final Id parentId, final Id explorerItemId);
}