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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JCheckBox;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.dialogs.db.DdsScriptUtils;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixAction;
import org.radixware.kernel.designer.common.dialogs.actions.AbstractRadixContextAwareAction;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class ScriptCompatibilitySetupAction extends AbstractRadixContextAwareAction {
    
    private static final String SETUP_COMPATIBOLITY_OF_SQL_SCRIPT_ACTION = "change-sqlscript-compatibility-action";
    
    public ScriptCompatibilitySetupAction() {
        super(SETUP_COMPATIBOLITY_OF_SQL_SCRIPT_ACTION, null);
    }
    
    @Override
    protected Action createAction(Lookup actionContext) {
        final FileObject fileObject = actionContext.lookup(FileObject.class);        
        
        RadixObject radixObject = fileObject == null ? null : RadixFileUtil.findRadixObject(fileObject);
        DdsUpdateInfo updateInfo = null;
        if (radixObject instanceof DdsScript) {
            DdsScript script = (DdsScript) radixObject;
            DdsScripts.UpdatesInfo upInfo = script.getOwnerDatabaseScripts().getOwnerScripts().getUpdatesInfo();
            for (DdsUpdateInfo info : upInfo) {
                if (info.findScript() == script) {
                    updateInfo = info;
                    break;
                }
            }
        }
        return new ScriptCompatibilitySetupActionImpl(updateInfo);
        
    }
    
    private static class ScriptCompatibilitySetupActionImpl extends AbstractRadixAction {
        
        private JCheckBox presenter = new JCheckBox("Backward compatible");
        
        public ScriptCompatibilitySetupActionImpl(final DdsUpdateInfo script) {
            super();
            
            if (script == null) {
                this.presenter.setVisible(false);
            } else {
                this.presenter.setOpaque(false);
                this.presenter.setSelected(script.isBackwardCompatible());
                this.presenter.setEnabled(!script.isReadOnly());
                this.presenter.setVisible(true);
                this.presenter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (DdsScriptUtils.showCompatibleWarning()) {
                            script.setBackwardCompatible(presenter.isSelected());
                        } else {
                            presenter.setSelected(script.isBackwardCompatible());
                        }
                    }
                });
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
        @Override
        public Component getToolbarPresenter() {
            return presenter;
        }
    }
}
