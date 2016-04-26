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

package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.dds.script.defs.DdsScriptGeneratorUtils;

public final class GenerateTriggersScriptAction implements ActionListener {

    private void performAction(Collection<Branch> branches) {
        final Set<RadixObject> radixObjects = RadixObjectsUtils.collectAllInside(branches, DdsVisitorProviderFactory.newTriggerProvider());
        final Set<DdsDefinition> triggers = new HashSet<DdsDefinition>();
        for (RadixObject radixObject : radixObjects) {
            final DdsTriggerDef trigger = (DdsTriggerDef) radixObject;
            triggers.add(trigger);
        }

        final String sql = DdsScriptGeneratorUtils.generateCreationScript(triggers);
        final String prefix =
                "/*\n"+
                "-- ====================== WARNING!!! =======================\n" +
                "\n" +
                "-- === ALL TRIGGERS IN CURRENT SCHEMA WILL BE DELETED!!! ===\n" +
                "\n" +
                "begin\n" +
                "   for i in (select TRIGGER_NAME from user_triggers) LOOP\n" +
                "       execute immediate 'DROP TRIGGER '||i.trigger_name;\n" +
                "   end loop;\n" +
                "end;\n/"+
                "*/\n\n";

        DialogUtils.showText(prefix + sql, "Triggers", "sql");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        if (branches.isEmpty()) {
            DialogUtils.messageError("There are no opened branches.");
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                performAction(branches);
            }
        });
    }
}
