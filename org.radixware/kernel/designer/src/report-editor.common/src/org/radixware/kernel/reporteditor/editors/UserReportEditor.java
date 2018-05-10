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
package org.radixware.kernel.reporteditor.editors;

import org.radixware.kernel.designer.ads.editors.changelog.ChangeLogEditor;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.windows.TopComponent;
import static org.openide.windows.TopComponent.PERSISTENCE_NEVER;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.reporteditor.tree.dialogs.UnistallReportWizardWizardAction;

public class UserReportEditor extends TopComponent {

    private final UserReport page;
    private final VersionsList versionsList = new VersionsList();
    private final DescriptionPanel descPanel;
    private boolean updating = false;
    private final DefinitionLinkEditPanel link;
    private Definition contextParam;
    private final ChangeLogEditor changeLogEditor;

    public UserReportEditor(final UserReport page) {
        this.page = page;
        descPanel = new DescriptionPanel(page);
        link = new DefinitionLinkEditPanel() {
            @Override
            protected void chooseBtnActionPerformed(ActionEvent evt) {
                if (allowChangeContextType()) {
                    super.chooseBtnActionPerformed(evt);
                }
            }
        };
        changeLogEditor = new ChangeLogEditor(page.getVersions().getCurrent());

        initComponents();

        updateContextParamLink();

        link.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                final Definition def = link.getDefinition();
                if (def instanceof AdsEntityObjectClassDef) {
                    page.setContextParamType(AdsTypeDeclaration.Factory.newParentRef((AdsEntityObjectClassDef) def));
                } else {
                    page.setContextParamType(null);
                }
            }
        });

        versionsList.open(page);

        page.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("alive".equals(evt.getPropertyName())) {
                    page.closeEditor();
                    close();
                }
            }
        });
    }
    
    private boolean allowChangeContextType() {
        List<String> allReportPubs = UnistallReportWizardWizardAction.
                execDisableAction(page.getId(), "preview:remove");
        
        if (allReportPubs != null && !allReportPubs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>Report '").append(page.getName()).append("' has following publications:");
            for (String pub : allReportPubs) {
                //pub string contains tag <html>, so we must cut it off
                sb.append("<br/>").append(pub.replaceAll("</?html>", ""));
            }
            sb.append("<br/>").append("Do you really want to change context type?")
                    .append(" Publications will be deleted in this case!</html>");
            
            if (!DialogUtils.messageConfirmation(sb.toString())) {
                return false;
            }
            
            UnistallReportWizardWizardAction.
                execDisableAction(page.getId(), "remove");
        }
        return true;
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.add(versionsList, BorderLayout.CENTER);

        final JPanel optionsPanel = new JPanel(new GridBagLayout());
        add(optionsPanel, BorderLayout.NORTH);
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        optionsPanel.add(descPanel, gridBagConstraints);

        final JLabel label = new JLabel("Context parameter type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 6, 2, 2);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        optionsPanel.add(label, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        optionsPanel.add(link, gridBagConstraints);

        final JLabel lbChangeLog = new JLabel("Change log for current version: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(2, 6, 2, 2);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        optionsPanel.add(lbChangeLog, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        optionsPanel.add(changeLogEditor, gridBagConstraints);
    }

    @Override
    public String getHtmlDisplayName() {
        return page.getName();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();

        try {
            updating = true;
            versionsList.update();
            descPanel.update();
            changeLogEditor.update(page.getVersions().getCurrent());
            updateContextParamLink();
        } finally {
            updating = false;
        }

    }

    private void updateContextParamLink() {
        Definition def = null;
        if (page.getContextParamType() != null) {
            final AdsType type = page.getContextParamType().resolve(page.findModule()).get();
            if (type instanceof AdsDefinitionType) {
                def = ((AdsDefinitionType) type).getSource();
            }
        }
        if (def != null && contextParam == def) {
            return;
        }
        contextParam = def;
        link.open(ChooseDefinitionCfg.Factory.newInstance(page.findModule(),
                AdsVisitorProviders.newEntityObjectTypeProvider(null)),
                contextParam, contextParam != null ? contextParam.getId() : null);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }
}
