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

package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;



public class TreeNode<T> extends AbstractNode {

    public static interface INodeSelector<T> {

        boolean acceptClass(Class<? extends T> nodeClass);

        boolean acceptNode(T node);
        
        boolean acceptNode(TreeNode<T> treeNode);
    }

    private final T object;

    public TreeNode(Children children, T object) {
        super(children);
        this.object = object;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

//    @Override
//    public Action[] getActions(boolean context) {
//        return new Action[] { };
//    }

    public T getObject() {
        return object;
    }
}
