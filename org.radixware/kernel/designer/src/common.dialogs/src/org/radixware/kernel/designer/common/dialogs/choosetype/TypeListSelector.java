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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import javax.swing.JComponent;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ListItemSelector;


abstract class TypeListSelector<TItem, TContext> extends ListItemSelector<TItem, TContext> {

    public TypeListSelector(ItemNameFilter<TItem> nameFilter) {
        super();

        getSelectorLayout().addFilterComponent(nameFilter);
        delegateNavigationKeys((JComponent) nameFilter.getComponent());
    }
}
