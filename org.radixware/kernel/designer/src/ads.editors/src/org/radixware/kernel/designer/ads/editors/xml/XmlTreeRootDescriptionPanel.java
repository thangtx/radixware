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

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;

public class XmlTreeRootDescriptionPanel extends JPanel {
    
    private static final String PREFIX_EDITOR_TITLE = "Namespace Prefix";
    
    private final AdsXmlSchemeDef schema;
    
    private final HandleInfo titleHandleInfo = new HandleInfo() {
        
        @Override
        public Definition getAdsDefinition() {
            return schema;
        }
        
        @Override
        public Id getTitleId() {
            return schema.getDocumentationTitleId();
        }
        
        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                schema.setDocumentationTitleId(multilingualStringDef.getId());
            } else {
                schema.setDocumentationTitleId(null);
            }
        }
        
        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            if (getAdsMultilingualStringDef() != null) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        }
    };
    
    private final HandleInfo schemaZIPTitleHandleInfo = new HandleInfo() {
        
        @Override
        public Definition getAdsDefinition() {
            return schema;
        }
        
        @Override
        public Id getTitleId() {
            return schema.getSchemaZIPTitleId();
        }
        
        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                schema.setSchemaZIPTitleId(multilingualStringDef.getId());
            } else {
                schema.setSchemaZIPTitleId(null);
            }
        }
        
        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            if (getAdsMultilingualStringDef() != null) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        }
    };
    
    private final HandleInfo descriptionHandleInfo = new HandleInfo() {
        
        @Override
        public Definition getAdsDefinition() {
            return schema;
        }
        
        @Override
        public Id getTitleId() {
            return schema.getDescriptionId();
        }
        
        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                schema.setDescriptionId(multilingualStringDef.getId());
            } else {
                schema.setDescriptionId(null);
            }
        }
        
        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            if (getAdsMultilingualStringDef() != null) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        }
    };
    
    private final LocalizingStringEditor titleEditor = LocalizingStringEditor.Factory.createEditor(
            new LocalizingStringEditor.Options()
            .add(LocalizingStringEditor.Options.COLLAPSABLE_KEY, true)
            .add(LocalizingStringEditor.Options.TITLE_KEY, "Documentation Title")
            .add(LocalizingStringEditor.Options.MODE_KEY, LocalizingStringEditor.EEditorMode.MULTILINE)
    );
    
    private final LocalizingStringEditor schemaZIPTitleEditor = LocalizingStringEditor.Factory.createEditor(
            new LocalizingStringEditor.Options()
            .add(LocalizingStringEditor.Options.COLLAPSABLE_KEY, true)
            .add(LocalizingStringEditor.Options.TITLE_KEY, "Schema ZIP Title")
            .add(LocalizingStringEditor.Options.MODE_KEY, LocalizingStringEditor.EEditorMode.MULTILINE)
    );
    
    private final LocalizingStringEditor descriptionEditor = LocalizingStringEditor.Factory.createEditor(
            new LocalizingStringEditor.Options()
            .add(LocalizingStringEditor.Options.COLLAPSABLE_KEY, true)
            .add(LocalizingStringEditor.Options.TITLE_KEY, "Schema Description")
            .add(LocalizingStringEditor.Options.MODE_KEY, LocalizingStringEditor.EEditorMode.MULTILINE)
    );
    
    private final JTextField prefixEditor = new JTextField();
    private BorderedCollapsablePanel prefixPanel;
    
    public XmlTreeRootDescriptionPanel(AdsXmlSchemeDef schema) {
        this.schema = schema;
        initComponents();
    }
    
    private void initComponents() {        
        final JPanel prefixEditorHolderPanel = new JPanel();
        prefixEditorHolderPanel.setLayout(new MigLayout("fill", "[grow]", "[shrink]"));
        prefixEditorHolderPanel.add(prefixEditor, "grow");        
        
        prefixPanel = new BorderedCollapsablePanel(prefixEditorHolderPanel, PREFIX_EDITOR_TITLE, schema.getNamespacePrefix() != null);
        
        if (schema.getNamespacePrefix() != null) {
            prefixEditor.setText(schema.getNamespacePrefix());
        }
        
        prefixPanel.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                if (prefixPanel != null) {
                    if (prefixPanel.isExpanded()) {
                        schema.setNamespacePrefix(prefixEditor.getText());                        
                    } else {
                        schema.setNamespacePrefix(null);
                        prefixEditor.setText("");
                    }
                }
            }
        });        
        
        if (prefixEditor.getDocument() != null) {
            prefixEditor.getDocument().addDocumentListener(new DocumentListener() {
                
                @Override
                public void insertUpdate(DocumentEvent e) {
                    schema.setNamespacePrefix(prefixEditor.getText());
                }
                
                @Override
                public void removeUpdate(DocumentEvent e) {
                    schema.setNamespacePrefix(prefixEditor.getText());
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) {
                    schema.setNamespacePrefix(prefixEditor.getText());
                }
            });
        }

        this.setLayout(new MigLayout("hidemode 3", "[grow]", "[shrink][shrink][shrink][shrink]"));
        this.add(prefixPanel, "growx, shrinky, wrap");
        this.add(descriptionEditor, "growx, shrinky, wrap");
        this.add(schemaZIPTitleEditor, "growx, shrinky, wrap");
        this.add(titleEditor, "growx, shrinky");
        
        titleEditor.update(titleHandleInfo);
        schemaZIPTitleEditor.update(schemaZIPTitleHandleInfo);
        descriptionEditor.update(descriptionHandleInfo);
    }
    
}
