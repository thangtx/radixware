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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsDefinitionsNode;


public abstract class AdsPresentationComponentContainerNode extends AdsDefinitionsNode implements IPresentationComponentContainer {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public AdsPresentationComponentContainerNode(ExtendableDefinitions definitions) {
        super(definitions);
    }

    @Override
    public List<AdsPresentationComponentContainerNode> getAvailableContainerNodes() {
        return Collections.singletonList(this);
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new PresentationCreationSupport(this);
    }

    public abstract Creature<? extends AdsDefinition> newItemCreature();
}
