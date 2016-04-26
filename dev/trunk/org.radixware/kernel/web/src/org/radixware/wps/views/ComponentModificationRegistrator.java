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

package org.radixware.wps.views;

import org.radixware.kernel.common.client.views.AbstractComponentModificationRegistrator;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.wps.rwt.UIObject;


public final class ComponentModificationRegistrator extends AbstractComponentModificationRegistrator {

    public ComponentModificationRegistrator(final IModifableComponent component, final UIObject ownerWidget) {
        super(component, new ModificationListenerLookup() {

            @Override
            public IModificationListener findParentModificationListener() {
                return findParentListener(ownerWidget);
            }
        });
    }

    public ComponentModificationRegistrator(final IModifableComponent component, final IModificationListener listener) {
        super(component, listener);
    }

    private static IModificationListener findParentListener(final UIObject ownerWidget) {
        for (UIObject parentWidget = ownerWidget.getParent(); parentWidget != null; parentWidget = parentWidget.getParent()) {
            if (parentWidget instanceof IModificationListener) {
                return (IModificationListener) parentWidget;
            }
        }
        return null;
    }
}