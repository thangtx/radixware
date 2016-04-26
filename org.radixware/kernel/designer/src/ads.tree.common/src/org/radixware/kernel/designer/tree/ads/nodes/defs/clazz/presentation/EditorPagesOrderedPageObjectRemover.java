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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.designer.common.annotations.registrators.ObjectRemoverFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.operation.IObjectRemoverFactory;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemover;


public class EditorPagesOrderedPageObjectRemover extends ObjectRemover {

    @ObjectRemoverFactoryRegistration
    public static final class Factory implements IObjectRemoverFactory<OrderedPage> {

        @Override
        public ObjectRemover newInstance(OrderedPage obj) {
            return new EditorPagesOrderedPageObjectRemover(obj);
        }
    }
    private OrderedPage op;

    public EditorPagesOrderedPageObjectRemover(OrderedPage op) {
        this.op = op;
    }

    @Override
    public void removeObject() {
        if (this.op != null && this.op.isInBranch()) {
            op.removeFromOrder();
        }
    }
}
