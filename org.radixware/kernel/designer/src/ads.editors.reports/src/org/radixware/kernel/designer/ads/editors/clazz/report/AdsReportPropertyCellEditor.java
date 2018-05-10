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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.common.sql.AdsSqlClassVisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;

class AdsReportPropertyCellEditor extends JPanel {

    private volatile boolean updating = false;
    private final AdsReportPropertyCell cell;
    private final JLabel fieldLabel = new JLabel("Field:");
    private final DefinitionLinkEditPanel fieldEditor = new DefinitionLinkEditPanel();
    //private final LocalizingStringEditor notNullTitle ;
    private final FormattedCellPanel formatPanel;

    /**
     * Creates new form AdsReportFieldCellEditor
     */
    public AdsReportPropertyCellEditor(final AdsReportPropertyCell cell) {
        super();
        this.cell = cell;
        initComponents();

        final JPanel content = new JPanel();
        final GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        gbl.setConstraints(fieldLabel, c);
        content.add(fieldLabel);

        c.gridx = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 10, 0, 10);
        gbl.setConstraints(fieldEditor, c);
        content.add(fieldEditor);

        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;

        final AdsPropertyDef propertyDef = cell.findProperty();
        AdsTypeDeclaration type = null;
        if (propertyDef != null) {
            type = propertyDef.getValue().getType();
        }
        formatPanel = new FormattedCellPanel(cell.getFormat(), type == null ? EValType.ANY : type.getTypeId(), cell.isReadOnly());

        gbl.setConstraints(formatPanel, c);
        content.add(formatPanel);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        final String title = "Set title for null value";
        final LocalizingStringEditor notNullTitle = LocalizingStringEditor.Factory.createLineEditor(new LocalizingStringEditor.Options().add(LocalizingStringEditor.Options.COLLAPSABLE_KEY, true)
                .add(LocalizingStringEditor.Options.TITLE_KEY, title)
                .add(LocalizingStringEditor.Options.MODE_KEY, LocalizingStringEditor.EEditorMode.MULTILINE));
        gbl.setConstraints(notNullTitle, c);
        content.add(notNullTitle);

        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);

        fieldEditor.setComboMode();
        final List<AdsPropertyDef> list = cell.getOwnerBand().getOwnerForm().getOwnerReport().getProperties().get(
                EScope.ALL, AdsSqlClassVisitorProviderFactory.newPropertyForPropertyCell());
        RadixObjectsUtils.sortByName(list);
        fieldEditor.setComboBoxValues(list, false);
        fieldEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                if (!updating) {
                    final AdsReportPropertyCell propCell = AdsReportPropertyCellEditor.this.cell;
                    Id id = propCell.getPropertyId();
                    Id newId = fieldEditor.getDefinitionId();
                    propCell.setPropertyId(newId); 
                    final AdsPropertyDef propertyDef = propCell.findProperty();
                    AdsTypeDeclaration type = null;
                    if (propertyDef != null) {          
                        type = propertyDef.getValue().getType();
                    }
                    if (type != null) {
                        propCell.getFormat().setUseDefaultFormat(true);
                        AdsReportPropertyCellEditor.this.formatPanel.open(propCell.getFormat(), type.getTypeId());
                    }
                    if (!Utils.equals(id, newId)){
                        firePropertyChange(AdsReportWidgetNamePanel.CHANGE_NAME, false, true);
                    }               
                }
            }
        });
        setupInitialValues();

        notNullTitle.open(new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return AdsReportPropertyCellEditor.this.cell.getOwnerReport();
            }

            @Override
            public Id getTitleId() {
                final AdsReportPropertyCell propCell = AdsReportPropertyCellEditor.this.cell;
                return propCell.getNullTitleId();
            }

            @Override
            protected void onLanguagesPatternChange(final EIsoLanguage language, final String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (AdsReportPropertyCellEditor.this.cell != null) {
                    if (multilingualStringDef == null) {
                        AdsReportPropertyCellEditor.this.cell.setNullTitleId(null);
                    } else {
                        AdsReportPropertyCellEditor.this.cell.setNullTitleId(multilingualStringDef.getId());
                    }
                }
            }

        });

        fieldEditor.requestFocusInWindow();
    }

    private void setupInitialValues() {
        updating = true;
        fieldEditor.open(cell.findProperty(), cell.getPropertyId());
        updateEnableState();
        updating = false;
    }

    private void updateEnableState() {
        final boolean enabled = !cell.isReadOnly();
        if (formatPanel != null) {
            formatPanel.setEnabled(enabled);
        }
        fieldEditor.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
