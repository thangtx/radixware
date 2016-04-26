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

package org.radixware.kernel.common.client.types;

import java.util.ArrayList;
import java.util.Collection;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IEntitySelectionController;


public final class ChoosableEntitiesFilter implements IEntitySelectionController {

    private final Collection<org.radixware.kernel.common.client.types.Pid> restrictedEntities = new ArrayList<org.radixware.kernel.common.client.types.Pid>();

    @Override
    public boolean isEntityChoosable(EntityModel entity) {
        return !restrictedEntities.contains(entity.getPid());
    }

    public void add(final org.radixware.kernel.common.client.types.Pid pid) {
        restrictedEntities.add(pid);
    }

    public void addAll(final Collection<org.radixware.kernel.common.client.types.Pid> pids) {
        restrictedEntities.addAll(pids);
    }

    public void addAllRefs(final Collection<org.radixware.kernel.common.client.types.Reference> refs) {
        for (org.radixware.kernel.common.client.types.Reference ref : refs) {
            restrictedEntities.add(ref.getPid());
        }
    }

    public void addAllEntities(final Collection<EntityModel> entities) {
        for (EntityModel entity : entities) {
            restrictedEntities.add(entity.getPid());
        }
    }

    public void remove(final org.radixware.kernel.common.client.types.Pid pid) {
        restrictedEntities.remove(pid);
    }

    public void clear() {
        restrictedEntities.clear();
    }

    public boolean isEmpty() {
        return restrictedEntities.isEmpty();
    }

    public boolean contains(final org.radixware.kernel.common.client.types.Pid pid) {
        return restrictedEntities.contains(pid);
    }

    public int size() {
        return restrictedEntities.size();
    }
}
