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

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Commands.Command;

/**
 * Commands collection
 */
class Commands extends ExtendablePresentations<AdsScopeCommandDef> implements IJavaSource {

    Commands(ClassPresentations context, ClassDefinition.Presentations xDef) {
        super(context, /*new LocalList(context),*/ xDef);
    }

    @Override
    protected void loadFrom(ClassDefinition.Presentations xPresentations) {
        ClassDefinition.Presentations.Commands xCommands = xPresentations.getCommands();
        if (xCommands != null) {
            List<Command> commands = xCommands.getCommandList();
            if (commands != null && !commands.isEmpty()) {
                for (Command c : commands) {
                    getLocal().add(AdsScopeCommandDef.Factory.loadFrom(c));
                }
            }
        }
    }

    protected void appendTo(ClassDefinition.Presentations xDef, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            ClassDefinition.Presentations.Commands xC = xDef.addNewCommands();
            for (AdsScopeCommandDef c : this.getLocal()) {
                //RAIDX-9029: for usage in roles
//                if (saveMode == ESaveMode.API && !c.isPublished()) {
//                    continue;
//                }
                c.appendTo(xC.addNewCommand(), saveMode);
            }
        }
    }

    @Override
    protected ExtendableDefinitions<AdsScopeCommandDef> findInstance(ClassPresentations prs) {
        if (prs != null) {
            return prs.getCommands();
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return "Commands";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.COMMAND;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        for (AdsCommandDef command : Commands.this.get(EScope.LOCAL_AND_OVERWRITE)) {
                            if (!writeCode(printer, command)) {
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                        //do not use dyrectly
                    }
                };
            }
        };
    }
}

