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

package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import java.util.List;
import java.util.Set;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.ads.editors.refactoring.components.RadixObjectTreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreeNode;


final class ClassHierarchyChildFactory extends ChildFactory<AdsClassDef> {

    private final TreeNode.INodeSelector<RadixObject> filter;
    private final Set<RadixObject> collision;
    private final AdsClassDef context;

    public ClassHierarchyChildFactory(AdsClassDef context, TreeNode.INodeSelector<RadixObject> filter, Set<RadixObject> collision) {

        assert filter != null && collision != null && context != null;

        this.filter = filter;
        this.collision = collision;
        this.context = context;
    }

    @Override
    protected boolean createKeys(List<AdsClassDef> toPopulate) {
        if (context != null && filter != null && filter.acceptNode(context)) {
            collectChilds(context, toPopulate);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(AdsClassDef key) {
        return new RadixObjectTreeNode<>(Children.create(new ClassHierarchyChildFactory(key, filter, collision), false), key);
    }

    private void collectNode(AdsClassDef node, List<AdsClassDef> level) {
        if (checkNode(node)) {
            synchronized (collision) {
                collision.add(node);
            }
            if (filter.acceptNode(node)) {
                level.add(node);
            } else {
                collectChilds(node, level);
            }
        }
    }

    private void collectChilds(AdsClassDef parent, List<AdsClassDef> level) {
        collectInterfaces(parent, level);
        if (parent.isOverwrite()) {
            collectOverwrite(parent, level);
        } else {
            collectSuperClass(parent, level);
        }
    }

    private void collectSuperClass(AdsClassDef context, List<AdsClassDef> level) {
        AdsClassDef superClass = context.getInheritance().findSuperClass().get();
        collectNode(superClass, level);
    }

    private AdsClassDef findInterface(AdsTypeDeclaration interfaceType) {
        if (interfaceType == null) {
            return null;
        }
        final AdsType type = interfaceType.resolve(context).get();
        if (type instanceof AdsClassType) {
            return ((AdsClassType) type).getSource();
        } else {
            return null;
        }
    }

    private void collectInterfaces(AdsClassDef context, List<AdsClassDef> level) {
        List<AdsTypeDeclaration> inerfaceRefList = context.getInheritance().getInerfaceRefList(EScope.LOCAL);

        for (AdsTypeDeclaration interfaceType : inerfaceRefList) {
            AdsClassDef currInterface = findInterface(interfaceType);
            collectNode(currInterface, level);
        }
    }

    private void collectOverwrite(AdsClassDef context, List<AdsClassDef> level) {
        AdsClassDef overwritten = context.getHierarchy().findOverwritten().get();
        collectNode(overwritten, level);
    }

    private boolean checkNode(RadixObject node) {
        synchronized (collision) {
            return node != null && !collision.contains(node);
        }
    }
}