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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Commands.Command;
import org.radixware.schemas.adsdef.CommandDefinition;


public class AdsRPCCommandDef extends AdsScopeCommandDef {

    public static final class Factory {

        public static AdsRPCCommandDef newInstance(AdsRPCMethodDef method) {
            return new AdsRPCCommandDef(method.getCommandId(), method.getName(), method.getServerSideMethodId());
        }

        public static AdsRPCCommandDef loadFrom(Command xDef) {
            return new AdsRPCCommandDef(xDef);
        }
    }
    private Id methodId;

    public AdsRPCCommandDef(Id id, String name, Id methodId) {
        super(id, name);
        this.methodId = methodId;
    }

    public AdsRPCCommandDef(Command xDef) {
        super(xDef);
        this.methodId = xDef.getMethodId();
    }

    public Id getMethodId() {
        return methodId;
    }

    public void setMethodId(Id id) {
        this.methodId = id;
        setEditState(EEditState.MODIFIED);
    }

    public AdsMethodDef findMethod() {
        return getOwnerClass().getMethods().findById(methodId, EScope.LOCAL_AND_OVERWRITE).get();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.RPC_COMMAND;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.EXPLORER);
    }

    @Override
    public ECommandScope getScope() {
        return ECommandScope.RPC;
    }

    @Override
    public void appendTo(CommandDefinition xCommand, ESaveMode saveMode) {
        super.appendTo(xCommand, saveMode);
        xCommand.setMethodId(methodId);
    }

    @Override
    public String getTypeTitle() {
        return "Remote Call Command";
    }

    @Override
    public String getTypesTitle() {
        return "Remote Call Commands";
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        
        final AdsMethodDef serverMethod = findMethod();
        if (serverMethod != null) {
            list.add(serverMethod);
        }
    }
}
