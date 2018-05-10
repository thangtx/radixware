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

/*
 * MergePanel.java
 *
 * Created on 6 Ноябрь 2008 г., 14:25
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage.CommandInfo;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage.IContextlessCommandsUser;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class CommandRefPanel extends JPanel implements PropertyChangeListener {

    private PropertyEnv env;
    private CommandRefEditor editor;
    private final ChooseDefinitionPanel panel = new ChooseDefinitionPanel();

    CommandRefPanel(CommandRefEditor editor, PropertyEnv env) {
        this.env = env;
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);

        initComponents();

        setLayout(new java.awt.BorderLayout());
        add(panel, BorderLayout.CENTER);

        final Set<AdsCommandDef> commands = new HashSet<>();
        final AdsPresentationDef presentation = RadixObjectsUtils.findContainer(getNode(), AdsPresentationDef.class);
        final IContextlessCommandsUser contextlessCommandsUser = RadixObjectsUtils.findContainer(getNode(), ContextlessCommandUsage.IContextlessCommandsUser.class);
        final ERuntimeEnvironmentType environmentType = AdsUIUtil.getUiDef(getNode()).getUsageEnvironment();

        if (contextlessCommandsUser != null) {
            for (final CommandInfo commandInfo : contextlessCommandsUser.getUsedContextlessCommands().getCommandInfos()) {
                final AdsContextlessCommandDef command = commandInfo.findCommand();
                if (command != null && acceptEnvironment(command, environmentType)) {
                    commands.add(command);
                }
            }
        } else {
            Collection<Definition> defs = DefinitionsUtils.collectTopAround(getNode(), new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsContextlessCommandDef) {
                        return acceptEnvironment((AdsContextlessCommandDef) radixObject, environmentType);
                    }
                    return false;
                }
            });
            for (Definition def : defs) {
                if (!commands.contains(def)) {
                    commands.add((AdsContextlessCommandDef) def);
                }
            }
        }

        if (presentation != null) {

            final List<AdsScopeCommandDef> scopeCommands = presentation.getCommandsLookup().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsScopeCommandDef>() {

                @Override
                public boolean isTarget(AdsScopeCommandDef command) {
                    return command != null && acceptEnvironment(command, environmentType);
                }
            });

            commands.addAll(scopeCommands);
        }
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(commands);
        panel.open(cfg, false);
    }

    private boolean acceptEnvironment(AdsCommandDef command, ERuntimeEnvironmentType environmentType) {
        return command.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT
            ||  command.getClientEnvironment() == environmentType;
    }

    private AdsUIProperty.CommandRefProperty getProperty() {
        return (AdsUIProperty.CommandRefProperty) editor.getValue();
    }

    private RadixObject getNode() {
        return ((UIPropertySupport) editor.getSource()).getNode();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(200, 140));
        setPreferredSize(new java.awt.Dimension(320, 260));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.CommandRefProperty prop = getProperty();

            Definition def = panel.getSelected();
            prop.setCommandId(def != null ? def.getId() : null);

            editor.setValue(prop);
            ((UIPropertySupport) editor.getSource()).setValue(prop);
        }
    }
}
