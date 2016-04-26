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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;


public abstract class AdsDefinitionsNode extends RadixObjectNode {

//    public static final class ExtFactory implements INodeFactory<ExtendableMembers> {
//
//        @Override
//        public Node newInstance(ExtendableMembers object) {
//            return new AdsDefinitionsNode(object);
//        }
//    }
//
//    public static final class Factory implements INodeFactory<AdsDefinitions> {
//
//        @Override
//        public Node newInstance(AdsDefinitions object) {
//            return new AdsDefinitionsNode(object);
//        }
//    }
    protected abstract class NamedDefinitionCreature<T extends AdsDefinition> extends AdsNamedDefinitionCreature<T> {

        protected NamedDefinitionCreature(String initialName, String displayName) {
            super(getRadixObject() instanceof RadixObjects ? (RadixObjects) getRadixObject() : ((ExtendableDefinitions) getRadixObject()).getLocal(), initialName, displayName);
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionsNode.this.getRadixObject().getIcon();
        }
    }

    @SuppressWarnings("unchecked")
    protected AdsDefinitionsNode(AdsDefinitions definitions) {
        super(definitions, new RadixObjectsNodeSortedChildren(definitions));
    }

    @SuppressWarnings("unchecked")
    protected AdsDefinitionsNode(ExtendableDefinitions definitions) {
        super(definitions, new RadixObjectsNodeSortedChildren(definitions.getLocal()));
    }
}
