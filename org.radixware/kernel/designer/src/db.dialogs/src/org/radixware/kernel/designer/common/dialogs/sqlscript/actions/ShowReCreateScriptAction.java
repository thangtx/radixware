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

package org.radixware.kernel.designer.common.dialogs.sqlscript.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.script.defs.DdsScriptGeneratorUtils;


public class ShowReCreateScriptAction extends AbstractAction {

    private DdsDefinition definition;

    public ShowReCreateScriptAction(DdsDefinition def) {
        this.definition = def;
        putValue(NAME, "Generate re-create script");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String sql = DdsScriptGeneratorUtils.generateReCreateScript(definition);
        if (sql == null) {
            DialogUtils.messageError("Operation \"View re-create script is not supported for \"" + definition.getTypesTitle());
            return;
        }
        DialogUtils.showText(sql, "Re-create Script " + definition.getQualifiedName(), "sql");
    }
}
