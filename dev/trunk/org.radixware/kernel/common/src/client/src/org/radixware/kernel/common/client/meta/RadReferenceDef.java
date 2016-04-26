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

package org.radixware.kernel.common.client.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;


public class RadReferenceDef extends NamedDefinition {

    public final Id referencedTableId;
    public final Id ownerTableId;
    public final Id[] childPropertyIds;
    public final String[] childColumnNames;
    public final Id[] parentPropertyIds;
    public final String[] parentColumnNames;

    public RadReferenceDef(final Id id,
            final String name,//автогенерируемое имя
            final Id ownerTableId,//идентификатор собственной таблицы
            final Id referencedTableId,//идентификатор родительской таблицы
            final Id[] childPropertyIds,
            final String[] childColumnNames,
            final Id[] parentPropertyIds,
            final String[] parentColumnNames) {
        super(id, name);
        this.referencedTableId = referencedTableId;
        this.ownerTableId = ownerTableId;
        this.childPropertyIds = childPropertyIds;
        this.childColumnNames = childColumnNames;
        this.parentPropertyIds = parentPropertyIds;
        this.parentColumnNames = parentColumnNames;
    }

    public RadClassPresentationDef getReferencedClassDef() {
        return getDefManager().getClassPresentationDef(referencedTableId);
    }

    public RadClassPresentationDef getOwnerClassDef() {
        return getDefManager().getClassPresentationDef(ownerTableId);
    }

    public List<Id> getChildColumnIds() {
        return childPropertyIds == null ? Collections.<Id>emptyList() : Arrays.asList(childPropertyIds);
    }

    public List<String> getChildColumnNames() {
        return childColumnNames == null ? Collections.<String>emptyList() : Arrays.asList(childColumnNames);
    }

    public List<Id> getParentColumnIds() {
        return parentPropertyIds == null ? Collections.<Id>emptyList() : Arrays.asList(parentPropertyIds);
    }

    public List<String> getParentColumnNames() {
        return parentColumnNames == null ? Collections.<String>emptyList() : Arrays.asList(parentColumnNames);
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "reference %s, owner definition is #%s");
        return String.format(desc, super.getDescription(), ownerTableId);
    }
}
