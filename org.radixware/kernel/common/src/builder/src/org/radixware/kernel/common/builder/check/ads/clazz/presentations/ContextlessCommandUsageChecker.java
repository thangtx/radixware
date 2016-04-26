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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class ContextlessCommandUsageChecker extends RadixObjectChecker<ContextlessCommandUsage> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return ContextlessCommandUsage.class;
    }

    @Override
    public void check(ContextlessCommandUsage clcs, IProblemHandler problemHandler) {
        super.check(clcs, problemHandler);
        ContextlessCommandUsage.IContextlessCommandsUser user = clcs.getUser();

        Definition def = clcs.getOwnerDefinition();
        AdsClassDef clazz = null;
        if (def instanceof AdsPresentationDef) {
            clazz = ((AdsPresentationDef) def).getModel();
        } else if (def instanceof AdsParagraphExplorerItemDef) {
            clazz = ((AdsParagraphExplorerItemDef) def).getModel();
        } else if (def instanceof AdsFilterDef) {
            clazz = ((AdsFilterDef) def).getModel();
        }
        if (clazz != null) {
            final ERuntimeEnvironmentType userEnv = user.getClientEnvironment();
            for (ContextlessCommandUsage.CommandInfo info : clcs.getCommandInfos()) {
                AdsContextlessCommandDef command = info.findCommand();
                if (command == null) {
                    error(clcs, problemHandler, "Referenced contextless command not found: #" + info.commmandId);
                } else {
                    if (command.getPresentation().getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && command.getPresentation().getClientEnvironment() != userEnv) {
                        error(clcs, problemHandler, "Contextless command " + command.getQualifiedName() + " with client environment " + command.getPresentation().getClientEnvironment().getName() + " can not be used in environment " + userEnv.getName());
                    }
                    AdsUtils.checkAccessibility(clcs, command, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(clcs, command, problemHandler);
                    Id handlerId = command.getHandlerId();
                    AdsMethodDef method = clazz.getMethods().findById(handlerId, EScope.LOCAL).get();
                    if (method == null) {
                        warning(clcs, problemHandler, "No handler for contextless command: " + command.getQualifiedName(clazz));
                    }
                }
            }
        }
    }
}
