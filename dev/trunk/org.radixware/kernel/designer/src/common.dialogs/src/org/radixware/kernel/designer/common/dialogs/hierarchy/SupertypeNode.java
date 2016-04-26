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

package org.radixware.kernel.designer.common.dialogs.hierarchy;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class SupertypeNode extends AbstractClassNode {

    public SupertypeNode(AdsClassDef classDef, boolean hasChildren) {
        super(classDef, hasChildren ? Children.create(new SupertypeChidFactory(classDef), false) : Children.LEAF);
    }

    private static class SupertypeChidFactory extends ChildFactory<AdsClassDef> {

        private final AdsClassDef classDef;

        public SupertypeChidFactory(AdsClassDef classDef) {
            this.classDef = classDef;
        }

        @Override
        protected boolean createKeys(List<AdsClassDef> toPopulate) {
            final AdsClassDef superClassDef = InheritanceUtilities.getSuperOrOverwrittenClass(classDef);
            if (superClassDef != null) {
                toPopulate.add(superClassDef);
            }
            toPopulate.add(classDef);
            return true;
        }

        @Override
        protected Node[] createNodesForKey(AdsClassDef key) {
            List<Node> nodes = new ArrayList<Node>();
            if (classDef == key) {
                List<AdsTypeDeclaration> interfaces = classDef.getInheritance().getInerfaceRefList(EScope.LOCAL);
                if (interfaces != null) {
                    for (AdsTypeDeclaration interfaceDef : interfaces) {
//                        nodes.add(new InterfaceNode(interfaceDef, classDef));
                        final AdsClassDef interfaceClassDef = ((AdsClassType) interfaceDef.resolve(classDef).get()).getSource();
                        nodes.add(new SupertypeNode(interfaceClassDef, interfaceClassDef.getInheritance().getInerfaceRefList(EScope.LOCAL).size() > 0));
                    }
                }
            } else {
                if (InheritanceUtilities.getSuperOrOverwrittenClass(key) == null) {
                    nodes.add(new SupertypeNode(key, false));
                } else {
                    nodes.add(new SupertypeNode(key, true));
                }
            }
            return nodes.toArray(new Node[0]);
        }
    }
}
