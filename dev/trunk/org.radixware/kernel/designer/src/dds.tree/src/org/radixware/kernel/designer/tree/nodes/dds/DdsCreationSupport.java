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

package org.radixware.kernel.designer.tree.nodes.dds;

import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


class DdsCreationSupport extends CreationSupport {

    private final List<ICreature> list;

    public DdsCreationSupport(ICreature... creatures) {
        list = Arrays.asList(creatures);
    }

    @Override
    public ICreatureGroup[] createCreatureGroups(final RadixObject object) {
        if (object instanceof RadixObjects) {
            return new ICreatureGroup[]{
                        new ICreatureGroup() {

                            @Override
                            public List<ICreature> getCreatures() {
                                return list;
                            }

                            @Override
                            public String getDisplayName() {
                                return "DDS Objects";
                            }
                        }
                    };
        } else {
            return null;
        }

    }
}
