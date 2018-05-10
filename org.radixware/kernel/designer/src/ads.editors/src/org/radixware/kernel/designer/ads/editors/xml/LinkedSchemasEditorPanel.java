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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

/**
 *
 * @author dlastochkin
 */
public class LinkedSchemasEditorPanel extends JPanel {

    private static final String ADD_BTN_TEXT = "Add";
    private static final String REMOVE_BTN_TEXT = "Remove";
    private static final String ACTUALIZE_IMPORTS = "Actualize Imports";
    private static final String ACTUALIZE_IMPORTS_CHB = "Actualize Imports on Save";

    private final JLabel schemaDescriptionLbl = new JLabel();
    private final JTextPane schemaIdTextPane = new JTextPane();

    private final JCheckBox actualizeImportsChb = new JCheckBox(ACTUALIZE_IMPORTS_CHB);

    AdsXmlSchemeDef schema;
    
    JScrollPane linkedSchemasTableHolderPane;
    
    LinkedSchemasTable linkedSchemasTable;
    
    Point scrollPositionPoint = new Point(0, 0);

    public LinkedSchemasEditorPanel(AdsXmlSchemeDef schema) {
        this.schema = schema;

        initComponents();
    }

    private void initComponents() {
        linkedSchemasTable = new LinkedSchemasTable(schema.getLinkedSchemas());

        linkedSchemasTableHolderPane = new JScrollPane(linkedSchemasTable);
        linkedSchemasTableHolderPane.getViewport().setViewPosition(scrollPositionPoint);

        schemaIdTextPane.setText("");
        schemaIdTextPane.setEditable(false);
        schemaIdTextPane.setBackground(null);
        schemaIdTextPane.setBorder(null);

        schemaDescriptionLbl.setText("");

        actualizeImportsChb.setSelected(schema.isNeedActualizeImportsOnSave());
        actualizeImportsChb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                schema.setNeedsActualizeImportsOnSave(actualizeImportsChb.isSelected());
            }
        });

        JPanel schemaDescriptionPanel = new JPanel(new MigLayout("fill", "[shrink][grow]", "[shrink]"));
        schemaDescriptionPanel.add(schemaDescriptionLbl, "shrink");
        schemaDescriptionPanel.add(schemaIdTextPane, "growx, shrinky");

        JButton addLinkedSchemasBtn = new JButton(ADD_BTN_TEXT, RadixWareIcons.CREATE.ADD.getIcon());
        addLinkedSchemasBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addLinkedSchemasBtn.addActionListener(getAddBtnListener());

        final JButton removeLinkedSchemasBtn = new JButton(REMOVE_BTN_TEXT, RadixWareIcons.DELETE.DELETE.getIcon());
        removeLinkedSchemasBtn.setHorizontalAlignment(SwingConstants.LEFT);
        removeLinkedSchemasBtn.setEnabled(false);
        removeLinkedSchemasBtn.addActionListener(getRemoveBtnListener());

        final JButton actualizeImportsBtn = new JButton(ACTUALIZE_IMPORTS, RadixWareIcons.EDIT.FIX.getIcon());
        actualizeImportsBtn.setHorizontalAlignment(SwingConstants.LEFT);
        actualizeImportsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schema.actualizeLinkedSchemas();
                update();
            }
        });

        linkedSchemasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel model = linkedSchemasTable.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Pair<AdsXmlSchemeDef, EXmlSchemaLinkMode> val = linkedSchemasTable.getSelectedRowValue();
                schemaDescriptionLbl.setText("Xml Schema '" + val.getFirst().getName() + "', ID: ");
                schemaIdTextPane.setText(val.getFirst().getId().toString());
                removeLinkedSchemasBtn.setEnabled(true);
            }
        });

        this.setLayout(new MigLayout("fill", "[grow][shrink]", "[][][][][][]"));
        this.add(linkedSchemasTableHolderPane, "grow, cell 0 0 1 5");
        this.add(addLinkedSchemasBtn, "cell 1 0, growx, wrap");
        this.add(removeLinkedSchemasBtn, "cell 1 1, growx, wrap");
        this.add(actualizeImportsBtn, "cell 1 2, growx, wrap");
        this.add(actualizeImportsChb, "cell 1 3, growx, wrap push");
        this.add(schemaDescriptionPanel, "growx, cell 0 5, span");
    }

    public void update() {
        if (linkedSchemasTableHolderPane != null) {
            scrollPositionPoint = linkedSchemasTableHolderPane.getViewport().getViewPosition();
        }
        
        this.removeAll();
        this.initComponents();        
        this.updateUI();
    }

    private ActionListener getAddBtnListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(schema.getBranch(), new VisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof AdsXmlSchemeDef && 
                                !((AdsXmlSchemeDef) radixObject).getId().toString().equals(schema.getId().toString()) && 
                                !schema.containsLinkedSchema(((AdsXmlSchemeDef) radixObject).getId());
                    }
                });

                List<Definition> selectedSchemas = ChooseDefinition.chooseDefinitionsEx(cfg);

                if (selectedSchemas == null || selectedSchemas.isEmpty()) {
                    return;
                }

                for (Definition selectedSchema : selectedSchemas) {
                    schema.addLinkedSchema((AdsXmlSchemeDef) selectedSchema);
                }

                update();
            }
        };
    }

    private ActionListener getRemoveBtnListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Pair<AdsXmlSchemeDef, EXmlSchemaLinkMode> selectedSchema = (Pair) linkedSchemasTable.getSelectedRowValue();

                if (selectedSchema == null) {
                    return;
                }

                if (selectedSchema.getSecond() == EXmlSchemaLinkMode.MANUAL) {
                    if (schema.getImportedNamespaces().contains(selectedSchema.getFirst().getTargetNamespace())) {
                        final String message = "This schema is imported. Do you want to change link mode to 'Import' or remove link completely?";
                        final String remove_link_text = "Remove Link";
                        final String chnage_link_type_text = "Change Link Type";
                        final String cancel_text = "Cancel";

                        List<String> buttonsList = Arrays.asList(new String[]{remove_link_text, chnage_link_type_text, cancel_text});

                        String result = DialogUtils.showCustomMessageBox(message, buttonsList, DialogDescriptor.WARNING_MESSAGE);
                        if (result == null || result.isEmpty()) {
                            return;
                        }

                        switch (result) {
                            case remove_link_text:
                                schema.removeLinkedSchema(selectedSchema.getFirst().getId());
                                break;
                            case chnage_link_type_text:
                                schema.removeLinkedSchema(selectedSchema.getFirst().getId());
                                schema.addImportedSchema(selectedSchema.getFirst());
                                break;
                            case cancel_text:
                                return;
                        }
                    } else {
                        schema.removeLinkedSchema(selectedSchema.getFirst().getId());
                    }
                } else {
                    schema.removeLinkedSchema(selectedSchema.getFirst().getId());
                }

                update();
            }
        };
    }
}
