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

import java.util.Collection;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.ClassHierarchySupport;


public class SubtypeNode extends AbstractClassNode {

    public SubtypeNode(AdsClassDef classDef, ClassHierarchySupport chs, boolean hasChildren) {
        super(classDef, hasChildren ? Children.create(new SubtypeChildFactory(classDef, chs), false) : Children.LEAF);
    }

    private static class SubtypeChildFactory extends ChildFactory<AdsClassDef> {

        private final AdsClassDef classDef;
        private final ClassHierarchySupport chs;

        public SubtypeChildFactory(AdsClassDef classDef, ClassHierarchySupport chs) {
            this.classDef = classDef;
            this.chs = chs;
        }

        @Override
        protected boolean createKeys(List<AdsClassDef> toPopulate) {
            toPopulate.addAll(chs.findDirectImplementations(classDef));
            toPopulate.addAll(chs.findDirectSubclasses(classDef));
            return true;
        }

        @Override
        protected Node createNodeForKey(AdsClassDef key) {
            Collection<AdsClassDef> subClassChildren = chs.findDirectSubclasses(key);
            if (subClassChildren == null || subClassChildren.isEmpty()) {
                return new SubtypeNode(key, chs, false);
            }
            return new SubtypeNode(key, chs, true);
        }
    }
}
