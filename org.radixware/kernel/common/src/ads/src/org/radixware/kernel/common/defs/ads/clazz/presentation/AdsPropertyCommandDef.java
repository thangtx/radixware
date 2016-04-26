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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Commands.Command;
import org.radixware.schemas.adsdef.CommandDefinition;


public class AdsPropertyCommandDef extends AdsScopeCommandDef {

    private List<Id> propIds;

    AdsPropertyCommandDef(Command xCommand) {
        super(xCommand);
        if (xCommand.getPropIds() != null && !xCommand.getPropIds().isEmpty()) {
            this.propIds = new ArrayList<Id>(xCommand.getPropIds());
        } else {
            this.propIds = null;
        }
    }

    AdsPropertyCommandDef() {
        super("newCommand");
        this.propIds = null;
    }

    public static final class Factory extends AdsScopeCommandDef.Factory {

        public static final AdsPropertyCommandDef newInstance() {
            return new AdsPropertyCommandDef();
        }
    }

    @Override
    public ECommandScope getScope() {
        return ECommandScope.PROPERTY;
    }

    @Override
    public void appendTo(CommandDefinition xDef, ESaveMode saveMode) {
        assert xDef instanceof Command;
        super.appendTo(xDef, saveMode);
        if (propIds != null && !propIds.isEmpty()) {
            ((Command) xDef).setPropIds(propIds);
        }
    }

    public List<Id> getUsedPropIds() {
        if (propIds == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Id>(propIds);
        }
    }

    public void setUsedPropIds(List<Id> ids) {
        if (ids != null && !ids.isEmpty()) {
            if (propIds == null) {
                propIds = new ArrayList<Id>(ids);
            } else {
                propIds.clear();
                propIds.addAll(ids);
            }
            setEditState(EEditState.MODIFIED);
        } else {
            propIds = null;
            setEditState(EEditState.MODIFIED);
        }
    }

    public PropertyUsageSupport getUsedProperties() {
        return PropertyUsageSupport.newInstance(this, propIds);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        getUsedProperties().collectDependences(list);
    }
}
