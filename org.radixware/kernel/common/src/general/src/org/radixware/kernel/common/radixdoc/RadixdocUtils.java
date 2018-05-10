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
package org.radixware.kernel.common.radixdoc;

import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;

public class RadixdocUtils {

    private static Definition radixdocOwnerDef = null;

    public static Definition getRadixdocOwnerDef(Layer context) {
        if (radixdocOwnerDef == null) {
            Layer.HierarchyWalker.walk(context, new Layer.HierarchyWalker.Acceptor<Object>() {
                @Override
                public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                    layer.visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            if (radixObject instanceof Definition) {
                                radixdocOwnerDef = (Definition) radixObject;
                            }

                        }
                    }, new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof Definition && ((Definition) radixObject).getId() == RadixdocConventions.LOCALIZING_DEF_ID;
                        }

                        @Override
                        public boolean isContainer(RadixObject radixObject) {
                            return radixObject instanceof Branch
                                    || radixObject instanceof Layer
                                    || radixObject instanceof Segment
                                    || (radixObject instanceof Module && ((Module) radixObject).getId() == RadixdocConventions.LOCALIZING_MODULE_ID)
                                    || radixObject instanceof Definitions;
                        }
                    });
                }
            });
        }
        
        return radixdocOwnerDef;
    }
}
