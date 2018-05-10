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

package org.radixware.kernel.common.design.msdleditor;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.design.msdleditor.field.FieldPanel;
import org.radixware.kernel.common.design.msdleditor.field.RootPanel;
import org.radixware.kernel.common.design.msdleditor.field.ShowErrorsPanel;
import org.radixware.kernel.common.design.msdleditor.field.VariantFieldPanel;
import org.radixware.kernel.common.design.msdleditor.tree.Tree;
import org.radixware.kernel.common.design.msdleditor.tree.ItemNode;
import org.radixware.kernel.common.environment.IRadixDefManager;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlUtils.SchemeInternalVisitor;
import org.radixware.kernel.common.msdl.MsdlUtils.SchemeInternalVisitorProvider;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ISchemeSearcher;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.MsdlDefinition;
import org.radixware.schemas.msdl.Message;


public class Editor extends JPanel {

    public ToolBar toolBar;
    public Tree tree;
    public ItemNode curNode;
    private JPanel fieldView, field;
    public RootMsdlScheme root;
    private JSplitPane splitPanel;
    private AdsDefinitionDocument def = null;
    public File file;

    public Editor(File file) {
        open(file);
    }

    public Editor() {
        newScheme();
    }

    public Editor(Message message) {
        open(message);
    }

    public Tree getTree() {
        return tree;
    }

    public void save() {
        if (file == null) {
            saveAs();
        } else {
            save(file);
        }
    }

    public void save(File file) {
        try {
            final XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setSavePrettyPrint();
            if (def == null) {
                def = AdsDefinitionDocument.Factory.newInstance();
                def.addNewAdsDefinition().addNewAdsMsdlSchemeDefinition().addNewMessageElement();
            }
            final Message mess = root.getMessage();
            mess.setName("Root");
            def.getAdsDefinition().getAdsMsdlSchemeDefinition().getMessageElement().set(mess);
            def.save(file, xmlOptions);
            this.file = file;
        } catch (IOException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
        toolBar.setSaveModified(false);
    }

    protected final void open(File file) {
        def = null;
        try {
            def = AdsDefinitionDocument.Factory.parse(file);
        } catch (XmlException | IOException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (def != null) {
            final Message message = def.getAdsDefinition().getAdsMsdlSchemeDefinition().getMessageElement();
            this.file = file;
            open(message);
            root.setName(message.getName());
        }
    }

    protected final void open(Message message) {
        removeAll();
        repaint();
        root = new RootMsdlScheme(message);
        root.setName("MessageMsdl");
        createEditor();
    }
    
    void setSchemeSearcher(final IRadixDefManager manager) {
        root.setSchemeSearcher(new ISchemeSearcher() {
            @Override
            public AbstractFieldModel findField(Id templateSchemeId, String templateSchemePath, EFieldType type) {
                SchemeInternalVisitor visitor = new SchemeInternalVisitor();
                RootMsdlScheme scheme = manager.getMsdlScheme(templateSchemeId);
                if (scheme != null) {
                    scheme.visit(visitor, new SchemeInternalVisitorProvider(templateSchemePath, root, type));
                } else {
                    root.visit(visitor, new SchemeInternalVisitorProvider(templateSchemePath, root, type));
                }
                return visitor.target;
            }
        });
    }

    private void createEditor() {
        tree = new Tree(this, root);
        toolBar = new ToolBar();
        toolBar.setEditor(this);
        fieldView = new JPanel();
        fieldView.setLayout(new BorderLayout());
        final JSplitPane sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, fieldView);
        sp1.setDividerLocation(250);
        setLayout(new BorderLayout());
        add(sp1, BorderLayout.CENTER);
        //add(toolBar,BorderLayout.NORTH);
        tree.selectRoot();
    }

    public final void newScheme() {
        final AdsDefinitionDocument adsDefinitionDocument = AdsDefinitionDocument.Factory.newInstance();
        final MsdlDefinition def1 = adsDefinitionDocument.addNewAdsDefinition().addNewAdsMsdlSchemeDefinition();
        def1.setName("NewMsdlScheme");
        final Message message = def1.addNewMessageElement();
        message.addNewStructure();
        message.setName("NewMsdlScheme");
        open(message);
    }

    public void newNodeSelected(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        curNode = (ItemNode) node;
        if (node == null) {
            fieldView.removeAll();
            fieldView.repaint();
            return;
        }
        toolBar.setEnabled(node, parent);
        fieldView.removeAll();
        fieldView.repaint();
        final ItemNode item = (ItemNode) node.getUserObject();
        field = item.createView();
        if (field != null) {
            fieldView.add(field, BorderLayout.CENTER);
            fieldView.validate();
            /*
             if (field instanceof IEditorView)
             ((IEditorView)fieldView).update();
             */
        }
        validate();
    }

    public void saveAs() {
        JFileChooser dialog = new JFileChooser();
        if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = dialog.getSelectedFile();
            save(file);
        }
    }

    public void showErrors(RadixObject radixObject) {
        fieldView.removeAll();
        fieldView.repaint();
        splitPanel = new JSplitPane();
        splitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPanel.setTopComponent(field);
        ShowErrorsPanel ep = new ShowErrorsPanel();
        ep.open(this, radixObject);
        splitPanel.setBottomComponent(ep);
        fieldView.add(splitPanel, BorderLayout.CENTER);
        fieldView.validate();
    }

    public void hideErrors() {
        fieldView.removeAll();
        fieldView.repaint();
        fieldView.add(field);
        fieldView.validate();
    }

    public void goToErrorSource(RadixObject radixObject) {
        root.visit(new FindVisitor(radixObject), new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return true;
            }
        });
        tree.findNode(radixObject);
    }

    public class FindVisitor implements IVisitor {

        private RadixObject object;

        public FindVisitor(RadixObject object) {
            this.object = object;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject == object) {//NOPMD
                /*
                 if (field != null) {
                 .remove(field);
                 fieldView.repaint();
                 }
                 */
                if (radixObject instanceof MsdlField) {
                    FieldPanel fieldPanel = new FieldPanel();
                    fieldPanel.open((MsdlField) radixObject, null, null, null);
                    splitPanel.setTopComponent(fieldPanel);
                    return;
                }
                if (radixObject instanceof RootMsdlScheme) {
                    RootPanel rootPanel = new RootPanel();
                    rootPanel.open((RootMsdlScheme) radixObject, null);
                    splitPanel.setTopComponent(rootPanel);
                    return;
                }
                if (radixObject instanceof MsdlVariantField) {
                    VariantFieldPanel fieldPanel = new VariantFieldPanel();
                    fieldPanel.open((MsdlVariantField) radixObject, null, null, null);
                    splitPanel.setTopComponent(fieldPanel);
                    return;
                }
                tree.findNode(radixObject);
            }
        }
    }
}
