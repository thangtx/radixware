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

package org.radixware.kernel.designer.common.general.nodes.operation;

import org.radixware.kernel.common.defs.RadixObject;


public class ObjectRemoverManager {

    private static class DefaultRemover extends ObjectRemover {

        private final RadixObject obj;

        public DefaultRemover(RadixObject obj) {
            this.obj = obj;
        }

        @Override
        public void removeObject() {
            if (obj.isInBranch() && obj.canDelete()) {
                obj.delete();
            }
        }
    }
    private static final ObjectRemoverFactoriesManager factoriesManager = new ObjectRemoverFactoriesManager();

    public static final ObjectRemover newObjectRemover(RadixObject obj) {
        IObjectRemoverFactory factory = factoriesManager.find(obj);
        if (factory != null) {
            ObjectRemover remover = factory.newInstance(obj);
            if (remover != null) {
                return remover;
            }
        }
        return new DefaultRemover(obj);
    }
}
