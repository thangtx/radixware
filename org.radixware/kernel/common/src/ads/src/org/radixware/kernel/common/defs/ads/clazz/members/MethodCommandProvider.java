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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AbstractFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


abstract class MethodCommandProvider {

    protected abstract Id getCommandId();
    private final AdsMethodDef method;

    public MethodCommandProvider(AdsMethodDef method) {
        this.method = method;
    }

    public AdsCommandDef findCommand() {
        final Id commandId = getCommandId();
        if (commandId.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
            final AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(method).findById(commandId).get();
            if (def instanceof AdsContextlessCommandDef) {
                return (AdsContextlessCommandDef) def;
            }
        } else {
            final AdsClassDef ownerClass = method.getOwnerClass();
            if (ownerClass instanceof AdsEntityModelClassDef) {
                final AdsEntityObjectClassDef modelOwnerClass = ((AdsEntityModelClassDef) ownerClass).findServerSideClasDef();
                if (modelOwnerClass != null) {
                    return findCommandInServerSideClass(modelOwnerClass, commandId);
                }
            } else if (ownerClass instanceof AdsGroupModelClassDef) {
                final AdsEntityObjectClassDef modelOwnerClass = ((AdsGroupModelClassDef) ownerClass).findServerSideClasDef();
                if (modelOwnerClass != null) {
                    final AdsEntityClassDef basis = modelOwnerClass.findRootBasis();
                    if (basis != null) {
                        final AdsEntityGroupClassDef groupHandler = basis.findEntityGroup();
                        if (groupHandler != null) {
                            return findCommandInServerSideClass(groupHandler, commandId);
                        } else {
                            return null;
                        }
                    }
                }
            } else if (ownerClass instanceof AbstractFormModelClassDef) {
                final AdsClassDef modelOwnerClass = ((AbstractFormModelClassDef) ownerClass).getOwnerClass();
                if (modelOwnerClass != null) {
                    return findCommandInServerSideClass(modelOwnerClass, commandId);
                }
            } else {
                return findCommandInServerSideClass(ownerClass, commandId);
            }
        }
        return null;
    }

    private AdsCommandDef findCommandInServerSideClass(final AdsClassDef clazz, final Id commandId) {
        if (clazz instanceof AdsEntityObjectClassDef) {
            return ((AdsEntityObjectClassDef) clazz).getPresentations().getCommands().findById(commandId, EScope.ALL).get();
        } else if (clazz instanceof AdsEntityGroupClassDef) {
            return ((AdsEntityGroupClassDef) clazz).getPresentations().getCommands().findById(commandId, EScope.ALL).get();
        } else if (clazz instanceof AdsFormHandlerClassDef) {
            return ((AdsFormHandlerClassDef) clazz).getPresentations().getCommands().findById(commandId, EScope.ALL).get();
        }
        if (clazz instanceof AdsReportClassDef) {
            return ((AdsReportClassDef) clazz).getPresentations().getCommands().findById(commandId, EScope.ALL).get();
        } else {
            return null;
        }
    }
}
