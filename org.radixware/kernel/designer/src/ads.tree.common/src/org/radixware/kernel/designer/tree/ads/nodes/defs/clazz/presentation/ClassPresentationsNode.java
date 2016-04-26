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

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityGroupPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

public class ClassPresentationsNode extends AdsMixedNode<ClassPresentations> {

    @Override
    protected Class[] getChildrenProviders() {
        final ClassPresentations presentations = getRadixObject();
        if (presentations instanceof EntityObjectPresentations) {

            return new Class[]{
                CollectionNode.Provider.class
            };

        } else if (presentations instanceof EntityGroupPresentations) {
            return new Class[]{
                CollectionNode.Provider.class};
        } else if (presentations instanceof AbstractFormPresentations) {
            return new Class[]{
                CollectionNode.Provider.class,
                ModelClassProvider.class,
                CustomViewProvider.class};
        } else {
            return null;
        }
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<ClassPresentations> {

        @Override
        public Node newInstance(final ClassPresentations object) {
            return new ClassPresentationsNode(object);
        }
    }

    public ClassPresentationsNode(final ClassPresentations presentations) {
        super(presentations);
    }

    @Override
    protected boolean transitiveCreation() {
        return true;
    }
}
