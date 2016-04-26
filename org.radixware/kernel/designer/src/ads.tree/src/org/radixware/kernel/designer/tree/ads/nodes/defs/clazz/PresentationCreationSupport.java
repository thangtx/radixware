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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;


class PresentationCreationSupport extends CreationSupport {

    private IPresentationComponentContainer context;

    PresentationCreationSupport(IPresentationComponentContainer context) {
        this.context = context;
    }

    @Override
    public ICreatureGroup[] createCreatureGroups(RadixObject object) {
        return new ICreatureGroup[]{
            new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    List<AdsPresentationComponentContainerNode> containerNodes = context.getAvailableContainerNodes();
                    ArrayList<ICreature> ccs = new ArrayList<ICreature>();
                    if (containerNodes != null) {
                        for (AdsPresentationComponentContainerNode node : containerNodes) {
                            ccs.add(node.newItemCreature());
                        }
                    }
                    return ccs;
                }

                @Override
                public String getDisplayName() {
                    return "Presentation Components";
                }
            }
        };
    }
}
