/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentations;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.types.Id;


final class SqmlTableWithAlias implements ISqmlTableDef{
    
    private final ISqmlTableDef source;
    private final String alias;
    
    public SqmlTableWithAlias(final ISqmlTableDef source, final String alias){
        this.source = source;
        this.alias = alias;
    }

    @Override
    public Id getTableId() {
        return source.getTableId();
    }

    @Override
    public ISqmlTableColumns getColumns() {
        return source.getColumns();
    }

    @Override
    public ISqmlTableReferences getReferences() {
        return source.getReferences();
    }

    @Override
    public ISqmlTableIndices getIndices() {
        return source.getIndices();
    }

    @Override
    public ISqmlSelectorPresentations getSelectorPresentations() {
        return source.getSelectorPresentations();
    }

    @Override
    public ISqmlTableDef createCopyWithAlias(final String alias) {
        return new SqmlTableWithAlias(source, alias);
    }

    @Override
    public boolean hasEntityClass() {
        return source.hasEntityClass();
    }

    @Override
    public boolean hasDetails() {
        return source.hasDetails();
    }

    @Override
    public boolean hasAlias() {
        return true;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean isDeprecatedDdsDef() {
        return source.isDeprecatedDdsDef();
    }

    @Override
    public Id getId() {
        return source.getId();
    }

    @Override
    public String getShortName() {
        return source.getShortName();
    }

    @Override
    public String getModuleName() {
        return source.getModuleName();
    }

    @Override
    public String getFullName() {
        return source.getFullName();
    }

    @Override
    public String getTitle() {
        return source.getTitle();
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        return source.getDisplayableText(mode);
    }

    @Override
    public ClientIcon getIcon() {
        return source.getIcon();
    }

    @Override
    public Id[] getIdPath() {
        return source.getIdPath();
    }

    @Override
    public boolean isDeprecated() {
        return source.isDeprecated();
    }

}
