/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.dds.script.ScriptDefinitionsCollector;
import org.radixware.kernel.designer.dds.script.defs.DdsScriptGeneratorUtils;

/**
 *
 * @author akrylov
 */
public class GenerateRunRoleScriptAction extends AbstractContextAwareAction implements ActionListener {

    private Layer layer;
    private static String preHeader = "--create role &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant create session to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant alter session to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant SELECT on SYS.V_$SESSION to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant SELECT on SYS.V_$PROCESS to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant create type to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant execute on DBMS_LOCK to &USER&_RUN_ROLE \n"
            + "--/\n"
            + "--grant execute on DBMS_LOB to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant execute on DBMS_SQL to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant execute on DBMS_RANDOM to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--grant alter system to &USER&_RUN_ROLE\n"
            + "--/\n"
            + "--\n"
            + "--\n"
            + "--grant &USER&_RUN_ROLE to &USER&\n"
            + "--/\n";

    public GenerateRunRoleScriptAction() {
    }

    private GenerateRunRoleScriptAction(Layer layer) {
        this.layer = layer;
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new GenerateRunRoleScriptAction(actionContext.lookup(Layer.class));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (layer == null) {
            return;
        }
        final DbUserNamePanel panel = new DbUserNamePanel(layer);
        ModalDisplayer displayer = new ModalDisplayer(panel, "Database User");
        while (true) {
            if (displayer.showModal()) {
                String userName = panel.getDbUserName();
                if (userName == null || userName.isEmpty()) {
                    continue;
                }
                final Set<DdsDefinition> definitions = new HashSet<>();
                Layer.HierarchyWalker walker = new Layer.HierarchyWalker();                
                walker.go(layer, new Layer.HierarchyWalker.Acceptor<Layer>() {

                    @Override
                    public void accept(HierarchyWalker.Controller<Layer> controller, Layer radixObject) {
                        ScriptDefinitionsCollector.collect(radixObject, definitions);
                    }
                });

                String script = preHeader + DdsScriptGeneratorUtils.generateRunRoleScript(definitions);
                script = script.replace("&USER&", userName);
                DialogUtils.showText(script, "RUN_ROLE script", "sql");
                return;
            } else {
                break;
            }
        }
    }

    @Override
    public Object getValue(final String key) {
        if ("Name".equals(key)) {
            return "Create RUN_ROLE script";
//            if (useLocaRepositary)                
//                return "Merge Changes to Local Branch";
//            return "Merge Changes to Subversion Branch";
        } else {
            return super.getValue(key);
        }
    }
}
