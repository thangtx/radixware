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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.UndoManager;
import net.miginfocom.swing.MigLayout;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.EditorUI;
import org.netbeans.editor.Utilities;
import org.openide.DialogDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.check.ProblemAnnotationFactory;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.radixdoc.xmlexport.AdsXmlSchemeExportableWrapper;
import org.radixware.kernel.common.defs.ads.xml.ChangeLogUtils;
import org.radixware.kernel.common.defs.ads.xml.IChangeLogActualizationListener;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExecAppByFileName;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RequestProcessor;
import org.radixware.kernel.common.utils.VersionNumber;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.ads.common.radixdoc.DesignerRadixdocGenerationContext;
import org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor;
import org.radixware.kernel.designer.ads.localization.merge.BranchFileChooser;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.EEditorMode;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.Options;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.XmlLocation;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;
import org.radixware.kernel.designer.common.dialogs.utils.TextComponentUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.html.LocalFileProvider;
import org.radixware.kernel.radixdoc.xmlexport.IExportableXmlSchema;
import org.radixware.kernel.radixdoc.xmlexport.XmlSchemaExporter;
import org.radixware.kernel.radixdoc.xmlexport.XmlSchemasExportTask;
import org.radixware.kernel.radixdoc.xmlexport.dialogs.XmlSchemasExportDialog;
import org.radixware.schemas.product.BranchDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AdsXmlEditor extends JPanel {

    private JEditorPaneEx jEditorPane1;
    private static final String tempFile = "______temp___.xsd";
    boolean mustUpdate = true;

    private static final String SOURCE_EDIT_MODE = "Source";
    private static final String DOCUMENTATION_EDIT_MODE = "Documentation";

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();

        setName("mainPanel"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 387, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
    private String strOldValue = null;
    private JButton edit = null;
    private JButton load = null;
    private JButton unLoad = null;
    private JButton unLoadWithImport = null;
    private JButton format = null;

    private final JToggleButton hideDocumentedBtn = new JToggleButton(RadixWareIcons.XML.HIDE_DOCUMENTED_NODES.getIcon());
    private final JToggleButton hideUndocumentedBtn = new JToggleButton(RadixWareIcons.XML.HIDE_UNDOCUMENTED_NODES.getIcon());
    private final JToggleButton hideServiceNodesBtn = new JToggleButton(RadixWareIcons.XML.HIDE_SERVICE_NODES.getIcon(), true);
    private final JToggleButton collapseAllBtn = new JToggleButton(RadixWareIcons.TREE.COLLAPSE.getIcon());

    private final JLabel documentedElementsInfoLabel = new JLabel();

    private final JCheckBox chbNeedsDoc = new JCheckBox("Should be documented");

    private AccessEditPanel accessEditPanel;
    private JPanel cardLayoutHolderPanel;
    private JPanel toolBarCardLayoutPanel;
    private JSplitPane documentationEditorPanel;

    private final JTextField selectedNodePath = new JTextField(100);
    private final JTextField sinceVersion = new JTextField();
    private final JTextField filterEditor = new JTextField();
    private final JLabel sinceLabel = new JLabel("Since: ");
    private final JButton addingDescriptionButton = new JButton("ADD NODE DESCRIPTION");

    private JScrollPane xmlPane;
    private XmlTree xmlTree;
    private MouseAdapter xmlTreeMouseListener;
    private FocusListener xmlTreeFocusListener;
    private TreeSelectionListener xmlTreeSelectionListener;

    private JPanel rootDescriptionPanel;

    private final LocalizingStringEditor descriptionEditor = LocalizingStringEditor.Factory.createEditor(
            new Options()
            .add(Options.COLLAPSABLE_KEY, false)
            .add(Options.TITLE_KEY, "Description")
            .add(Options.MODE_KEY, EEditorMode.MULTILINE)
    );

    private final HandleInfo handleInfo = new HandleInfo() {
        @Override
        public Definition getAdsDefinition() {
            return schema;
        }

        @Override
        public Id getTitleId() {
            if (schema.getXmlItemDocEntry(selectedNodePath.getText()) != null) {
                return schema.getXmlItemDocEntry(selectedNodePath.getText()).getId();
            } else {
                return null;
            }
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                AdsXmlSchemeDef.XmlItemDocEntry docEntry = new AdsXmlSchemeDef.XmlItemDocEntry(multilingualStringDef.getId(), sinceVersion.getText());
                schema.addNodeDescription(selectedNodePath.getText(), docEntry);
            } else {
                schema.removeNodeDescription(selectedNodePath.getText());
            }
            documentationChanged();
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            if (getAdsMultilingualStringDef() != null) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
                documentationChanged();
            }
        }
    };

    private String editMode;
    private boolean isXmlChanged = false;
    private boolean isCollapsed = false;
    private XmlTreeUtils.TreeState xmlTreeState;
    private Point xmlTreeScrollPosition;
    private int docEditorSplitterPosition;

    //private boolean isCheckFocusLoose = true;           
    class JEditorPaneEx extends JEditorPane {

        JEditorPaneEx() {
            super();
        }
    }

    private class PageViewer extends CardLayout {

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Component current = findCurrentComponent(parent);
            if (current != null) {
                Insets insets = parent.getInsets();
                Dimension pref = current.getPreferredSize();
                pref.width += insets.left + insets.right;
                pref.height += insets.top + insets.bottom;
                return pref;
            }
            return super.preferredLayoutSize(parent);
        }

        public Component findCurrentComponent(Container parent) {
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    return comp;
                }
            }
            return null;
        }
    }

    private String saveSchemaTree(File exportDir, String rootSchemaFileName, boolean forEditor) {
        String errMessage = "";
        Map<String, Handler> savingSchemas = new HashMap<>();
        try {
            collectImportSchemas(XmlUtils.newCopy(schema.getXmlDocument()), savingSchemas, null);
        } catch (final IOException ex) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.messageError(new Exception("Error on XML export", ex));
                }
            });
        }
        Map<String, String> fileNames = new HashMap<>();
        Map<String, Object> uniquefileNames = new HashMap<>();

        fileNames.put(schema.getTargetNamespace(), rootSchemaFileName);
        for (String nss : savingSchemas.keySet()) {
            if (fileNames.containsKey(nss)) {
            } else {
                int index = nss.lastIndexOf("/");
                String fileName;
                if (index > 0 && index + 1 < nss.length()) {
                    fileName = nss.substring(index + 1);
                } else {
                    Handler h = savingSchemas.get(nss);
                    if (h.initialLocation != null) {
                        fileName = h.initialLocation;
                    } else {
                        fileName = FileUtils.string2UniversalFileName(nss, '-');
                    }
                }
                int suffix = 0;
                String checkName = fileName;

                String name = FileUtils.getFileBaseName(fileName);
                String ext = FileUtils.getFileExt(fileName);

                while (uniquefileNames.containsKey(checkName)) {
                    suffix++;
                    checkName = name + String.valueOf(suffix) + "." + ext;
                }
                fileName = checkName;
                uniquefileNames.put(fileName, null);
                fileNames.put(nss, fileName);
            }
        }

        for (Map.Entry<String, Handler> e : savingSchemas.entrySet()) {
            if (e.getValue().locations != null) {
                for (XmlUtils.Namespace2Location loc : e.getValue().locations) {
                    String location = fileNames.get(loc.namespace);
                    loc.setActualLocation(location);
                }
                try {
                    if (!forEditor) {
                        addAdditionEnumInfoToAppInfo(e.getValue().schema.getDomNode());
                    }
                    e.getValue().schema.save(new File(exportDir, fileNames.get(e.getKey())));
                } catch (IOException ex) {
                    errMessage = ex.getMessage();
                }
            }
        }
        return errMessage;
    }

    private void documentationChanged() {
        XmlTreeCellRenderer renderer = (XmlTreeCellRenderer) xmlTree.getCellRenderer();
        renderer.setDocumentedNodes(schema.getNodesDocumentationStrings());

        XmlTreeModel treeModel = (XmlTreeModel) xmlTree.getModel();

        if (xmlTree.getSelectionPath() != null) {
            XmlTreeNode node = (XmlTreeNode) xmlTree.getSelectionPath().getLastPathComponent();
            treeModel.nodeChanged(node);
        }
    }

    private void initComponents2() {
        isXmlChanged = false;
        editMode = editMode == null ? SOURCE_EDIT_MODE : editMode;
        if (xmlPane != null) {
            xmlTreeScrollPosition = xmlPane.getViewport().getViewPosition();
        }

        if (documentationEditorPanel != null) {
            docEditorSplitterPosition = documentationEditorPanel.getDividerLocation();
        }

        if (xmlTree != null) {
            if (xmlTreeFocusListener != null) {
                xmlTree.removeFocusListener(xmlTreeFocusListener);
            }

            if (xmlTreeMouseListener != null) {
                xmlTree.removeMouseListener(xmlTreeMouseListener);
            }

            if (xmlTreeSelectionListener != null) {
                xmlTree.removeTreeSelectionListener(xmlTreeSelectionListener);
            }
        }

        jEditorPane1 = new JEditorPaneEx();
        jEditorPane1.setContentType("text/xml");
        descriptionEditor.update(handleInfo);
        rootDescriptionPanel = new XmlTreeRootDescriptionPanel(schema);

        EditorUI eui = Utilities.getEditorUI(jEditorPane1);
        if (eui != null) {
            selectedNodePath.setEditable(false);
            documentedElementsInfoLabel.setVisible(false);

            JToolBar toolBar = eui.getToolBarComponent();
            toolBar.setOpaque(false);
            toolBarCardLayoutPanel = new JPanel();
            toolBarCardLayoutPanel.setLayout(new PageViewer());
            toolBarCardLayoutPanel.add(prepareSourceEditorToolBar(toolBar), SOURCE_EDIT_MODE);
            toolBarCardLayoutPanel.add(prepareDocumentationEditorToolBar(new JToolBar()), DOCUMENTATION_EDIT_MODE);

            Component extComponent = eui.getExtComponent();
            ActionListener changeEditorListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editMode = e.getActionCommand();
                    ((CardLayout) cardLayoutHolderPanel.getLayout()).show(cardLayoutHolderPanel, e.getActionCommand());
                    ((CardLayout) toolBarCardLayoutPanel.getLayout()).show(toolBarCardLayoutPanel, e.getActionCommand());
                    if (DOCUMENTATION_EDIT_MODE.equals(editMode)) {
                        documentedElementsInfoLabel.setVisible(true);
                        jEditorPane1.getDocument().removeDocumentListener(documentListener);
                        if (isXmlChanged) {
                            updateDocEditor();
                        }
                    } else {
                        documentedElementsInfoLabel.setVisible(false);
                        jEditorPane1.getDocument().addDocumentListener(documentListener);
                    }
                }
            };

            JToggleButton sourceBtn = new JToggleButton(SOURCE_EDIT_MODE);
            sourceBtn.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "source-button-tooltip"));
            sourceBtn.addActionListener(changeEditorListener);

            JToggleButton documentationBtn = new JToggleButton(DOCUMENTATION_EDIT_MODE);
            documentationBtn.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "documentation-button-tooltip"));
            documentationBtn.addActionListener(changeEditorListener);

            ButtonGroup editModeBtns = new ButtonGroup();
            editModeBtns.add(sourceBtn);
            editModeBtns.add(documentationBtn);

            JPanel toolBarPanel = new JPanel();
            toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.LINE_AXIS));
            toolBarPanel.setBorder(new EmptyBorder(3, 3, 3, 3));

            toolBarPanel.add(sourceBtn);
            toolBarPanel.add(documentationBtn);
            toolBarPanel.add(toolBarCardLayoutPanel);
            toolBarPanel.add(Box.createHorizontalGlue());

            Font font = lblMess.getFont();
            Font f = new Font(font.getFontName(), Font.BOLD, font.getSize());
            lblMess.setFont(f);

            JPanel lblMessHolderPanel = new JPanel();
            lblMessHolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            lblMessHolderPanel.add(lblMess);

            JPanel xmlCodeEditorHolderPanel = new JPanel();
            xmlCodeEditorHolderPanel.setLayout(new BorderLayout());
            xmlCodeEditorHolderPanel.add(extComponent, BorderLayout.CENTER);
            xmlCodeEditorHolderPanel.add(lblMessHolderPanel, BorderLayout.NORTH);

            chbNeedsDoc.removeItemListener(needsDocListener);
            chbNeedsDoc.setSelected(schema.needsDocumentation());
            chbNeedsDoc.addItemListener(needsDocListener);

            accessEditPanel = new AccessEditPanel();
            JLabel lAcceess = new JLabel();
            lAcceess.setText("Access: ");
            JPanel accessEditorHolderPanel = new JPanel();

            javax.swing.GroupLayout accessEditorHolderLayout = new javax.swing.GroupLayout(accessEditorHolderPanel);
            accessEditorHolderLayout.setHorizontalGroup(accessEditorHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    accessEditorHolderLayout.createSequentialGroup().addContainerGap().addComponent(lAcceess).addComponent(accessEditPanel).addComponent(chbNeedsDoc)));
            accessEditorHolderLayout.setVerticalGroup(accessEditorHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    accessEditorHolderLayout.createSequentialGroup().addGroup(accessEditorHolderLayout.createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.CENTER).addComponent(lAcceess).addComponent(accessEditPanel).addComponent(chbNeedsDoc))));

            accessEditorHolderPanel.setLayout(accessEditorHolderLayout);

            JPanel sourceEditorPanel = new JPanel();
            sourceEditorPanel.setLayout(new BorderLayout());
            sourceEditorPanel.add(accessEditorHolderPanel, BorderLayout.NORTH);
            sourceEditorPanel.add(xmlCodeEditorHolderPanel, BorderLayout.CENTER);

            final XmlTreeCellRenderer renderer = new XmlTreeCellRenderer(schema.getLayer().getLanguages(), schema.getNodesDocumentationStrings());

            XmlObject obj = schema.getXmlDocument() != null ? schema.getXmlDocument() : schema.getXmlContent();
            if (obj == null) {
                editMode = SOURCE_EDIT_MODE;
                documentationEditorPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                documentedElementsInfoLabel.setVisible(false);

                jEditorPane1.getDocument().addDocumentListener(documentListener);
                sourceBtn.setVisible(false);
                documentationBtn.setVisible(false);
                ((CardLayout) toolBarCardLayoutPanel.getLayout()).show(toolBarCardLayoutPanel, SOURCE_EDIT_MODE);

                jPanel4.setLayout(new BorderLayout());
                jPanel4.add(toolBarPanel, BorderLayout.NORTH);
                jPanel4.add(sourceEditorPanel, BorderLayout.CENTER);

                return;
            }

            xmlTree = XmlTree.newInstance((Document) obj.getDomNode());
            xmlTree.setCellRenderer(renderer);
            xmlTree.setToggleClickCount(0);
            xmlTree.setServiceNodesVisible(hideServiceNodesBtn.isSelected());
            xmlTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            ToolTipManager.sharedInstance().registerComponent(xmlTree);

            xmlPane = new JScrollPane(xmlTree);
            xmlPane.setBorder(new EmptyBorder(7, 7, 7, 7));
            xmlPane.setBackground(Color.WHITE);

            JLabel filterLabel = new JLabel("Search:");
            filterEditor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    String xpath = selectedNodePath.getText();
                    xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                    filterEditor.requestFocusInWindow();
                    if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                        resetDocEditor();
                    }
                    isCollapsed = false;
                    collapseAllBtn.setSelected(false);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    String xpath = selectedNodePath.getText();
                    xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                    filterEditor.requestFocusInWindow();
                    if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                        resetDocEditor();
                    }
                    isCollapsed = false;
                    collapseAllBtn.setSelected(false);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    String xpath = selectedNodePath.getText();
                    xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                    filterEditor.requestFocusInWindow();
                    if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                        resetDocEditor();
                    }
                    isCollapsed = false;
                    collapseAllBtn.setSelected(false);
                }
            });

            String documentedElementsInfo = XmlTreeUtils.getDocumentedElementsCount(xmlTree) + " items of " + XmlTreeUtils.getUndocumentedElementsCount(xmlTree) + " documented";
            documentedElementsInfoLabel.setText(documentedElementsInfo);
            toolBarPanel.add(documentedElementsInfoLabel);

            JPanel xmlTreePanel = new JPanel(new MigLayout("fill", "[shrink][grow]", "[shrink][grow]"));
            xmlTreePanel.add(filterLabel);
            xmlTreePanel.add(filterEditor, "growx, wrap");
            xmlTreePanel.add(xmlPane, "span, grow");

            final JLabel pathLabel = new JLabel("Element: ");
            sinceLabel.setVisible(selectedNodePath.getText() != null && !"".equals(selectedNodePath.getText()));

            sinceVersion.setVisible(selectedNodePath.getText() != null && !"".equals(selectedNodePath.getText()));
            sinceVersion.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateSinceVersion();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateSinceVersion();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateSinceVersion();
                }
            });

            addingDescriptionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    descriptionEditor.setVisible(true);
                    sinceLabel.setVisible(true);
                    sinceVersion.setVisible(true);

                    addingDescriptionButton.setVisible(false);

                    schema.addNodeDescription(selectedNodePath.getText(), new AdsXmlSchemeDef.XmlItemDocEntry(null, null));
                    handleInfo.createAdsMultilingualStringDef();

                    descriptionEditor.requestEditorFocus();

                    documentationChanged();
                }
            });

            final JPanel descriptionEditorHolderPanel = new JPanel(new MigLayout("fill, hidemode 3", "[shrink][grow]", "[shrink][grow][shrink]"));
            descriptionEditorHolderPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
            descriptionEditorHolderPanel.add(pathLabel);
            descriptionEditorHolderPanel.add(selectedNodePath, "growx, wrap");
            descriptionEditorHolderPanel.add(descriptionEditor, "span, grow, wrap");
            descriptionEditorHolderPanel.add(sinceLabel);
            descriptionEditorHolderPanel.add(sinceVersion, "shrinkx, growx, wrap");
            descriptionEditorHolderPanel.add(addingDescriptionButton, "span, grow");
            descriptionEditorHolderPanel.add(rootDescriptionPanel, "span, grow");

            documentationEditorPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            documentationEditorPanel.add(xmlTreePanel);
            documentationEditorPanel.add(descriptionEditorHolderPanel);

            JScrollPane scrollPane = new JScrollPane(documentationEditorPanel);

            cardLayoutHolderPanel = new JPanel();
            cardLayoutHolderPanel.setLayout(new CardLayout());
            cardLayoutHolderPanel.add(sourceEditorPanel, sourceBtn.getText());
            cardLayoutHolderPanel.add(scrollPane, documentationBtn.getText());

            jPanel4.setLayout(new BorderLayout());
            jPanel4.add(toolBarPanel, BorderLayout.NORTH);
            jPanel4.add(cardLayoutHolderPanel, BorderLayout.CENTER);

            if (SOURCE_EDIT_MODE.equals(editMode)) {
                documentedElementsInfoLabel.setVisible(false);
                jEditorPane1.getDocument().addDocumentListener(documentListener);
                sourceBtn.setSelected(true);
                ((CardLayout) cardLayoutHolderPanel.getLayout()).show(cardLayoutHolderPanel, SOURCE_EDIT_MODE);
                ((CardLayout) toolBarCardLayoutPanel.getLayout()).show(toolBarCardLayoutPanel, SOURCE_EDIT_MODE);
            } else {
                documentedElementsInfoLabel.setVisible(true);
                jEditorPane1.getDocument().removeDocumentListener(documentListener);
                documentationBtn.setSelected(true);
                ((CardLayout) cardLayoutHolderPanel.getLayout()).show(cardLayoutHolderPanel, DOCUMENTATION_EDIT_MODE);
                ((CardLayout) toolBarCardLayoutPanel.getLayout()).show(toolBarCardLayoutPanel, DOCUMENTATION_EDIT_MODE);
                xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
            }

            if (xmlTreeState != null) {
                XmlTreeUtils.collapseAll(xmlTree);
                XmlTreeUtils.setTreeState(xmlTree, xmlTreeState);
            }

            if (selectedNodePath != null) {
                if (!XmlTreeUtils.setSelectedNode(xmlTree, selectedNodePath.getText())) {
                    resetDocEditor();
                }
            }

            if (xmlTreeScrollPosition != null) {
                xmlPane.getViewport().setViewPosition(xmlTreeScrollPosition);
            }

            if (documentationEditorPanel != null) {
                documentationEditorPanel.setDividerLocation(docEditorSplitterPosition);
            }

            if (!isRootSelectInTree()) {
                descriptionEditor.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                sinceLabel.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                sinceVersion.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                addingDescriptionButton.setVisible(!schema.getDocumentedNodes().contains(selectedNodePath.getText()) && selectedNodePath.getText() != null && !"".equals(selectedNodePath.getText()));

                rootDescriptionPanel.setVisible(false);
            } else {
                addingDescriptionButton.setVisible(false);
                descriptionEditor.setVisible(false);
                sinceLabel.setVisible(false);
                sinceVersion.setVisible(false);

                rootDescriptionPanel.setVisible(true);
            }

            xmlTree.addTreeSelectionListener(xmlTreeSelectionListener = new TreeSelectionListener() {

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (isRootSelectInTree()) {
                        addingDescriptionButton.setVisible(false);
                        descriptionEditor.setVisible(false);
                        sinceLabel.setVisible(false);
                        sinceVersion.setVisible(false);

                        rootDescriptionPanel.setVisible(true);
                    } else {
                        rootDescriptionPanel.setVisible(false);
                    }
                }
            });

            xmlTree.addTreeSelectionListener(xmlTreeSelectionListener = new TreeSelectionListener() {
                boolean treeSelectionListenerEnabled = true;

                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (treeSelectionListenerEnabled) {
                        boolean isDescriptionEmpty = true;

                        for (EIsoLanguage lang : schema.getLayer().getLanguages()) {
                            if (e.getOldLeadSelectionPath() == null) {
                                break;
                            }

                            XmlTreeNode node = (XmlTreeNode) e.getOldLeadSelectionPath().getLastPathComponent();
                            if (!schema.getDocumentedNodes().contains(node.getXPath()) && descriptionEditor.getValueMap().isEmpty()) {
                                isDescriptionEmpty = false;
                                break;
                            }

                            if (descriptionEditor.getValueMap().get(lang) != null && !descriptionEditor.getValueMap().get(lang).isEmpty()) {
                                isDescriptionEmpty = false;
                                break;
                            }
                        }

                        if (e.getOldLeadSelectionPath() != null && isDescriptionEmpty && !showEmptyDocWarning()) {
                            treeSelectionListenerEnabled = false;
                            try {
                                xmlTree.setSelectionPath(e.getOldLeadSelectionPath());
                            } finally {
                                treeSelectionListenerEnabled = true;
                            }
                        } else {
                            EIsoLanguage lang = descriptionEditor.getCurrentLanguage();

                            selectedNodePath.setText(((XmlTreeNode) e.getPath().getLastPathComponent()).getXPath());
                            String sinceVersionStr = "";
                            if (schema.getXmlItemDocEntry(selectedNodePath.getText()) != null) {
                                sinceVersionStr = schema.getXmlItemDocEntry(selectedNodePath.getText()).getSinceVersion();
                            }
                            sinceVersion.setText(sinceVersionStr == null ? "" : sinceVersionStr);
                            sinceVersion.setVisible(true);
                            sinceLabel.setVisible(true);
                            descriptionEditor.setVisible(true);

                            descriptionEditor.open(handleInfo);
                            descriptionEditor.invalidate();
                            descriptionEditorHolderPanel.validate();

                            descriptionEditor.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                            sinceLabel.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                            sinceVersion.setVisible(schema.getDocumentedNodes().contains(selectedNodePath.getText()));
                            addingDescriptionButton.setVisible(!schema.getDocumentedNodes().contains(selectedNodePath.getText()));

                            descriptionEditor.goToLanguage(lang);
                        }
                    }
                }
            });

            xmlTree.addFocusListener(xmlTreeFocusListener = new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                }

                @Override
                public void focusLost(FocusEvent e) {
                    xmlTreeState = XmlTreeUtils.getTreeState(xmlTree);
                }
            });

            JMenuItem deleteDocItem = new JMenuItem("Delete Node Description");
            deleteDocItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    descriptionEditor.setVisible(false);
                    sinceLabel.setVisible(false);
                    sinceVersion.setVisible(false);

                    addingDescriptionButton.setVisible(true);

                    schema.removeNodeDescription(selectedNodePath.getText());
                    documentationChanged();
                }
            });

            xmlTree.addMouseListener(xmlTreeMouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.getClickCount() == 2 && !isRootSelectInTree()) {
                            TreePath pathForLocation = xmlTree.getPathForLocation(e.getPoint().x, e.getPoint().y);

                            if (pathForLocation != null) {
                                XmlTreeNode node = (XmlTreeNode) pathForLocation.getLastPathComponent();
                                if (schema.getDocumentedNodes().contains(node.getXPath())) {
                                    return;
                                }

                                descriptionEditor.setVisible(true);
                                sinceLabel.setVisible(true);
                                sinceVersion.setVisible(true);

                                addingDescriptionButton.setVisible(false);

                                schema.addNodeDescription(selectedNodePath.getText(), new AdsXmlSchemeDef.XmlItemDocEntry(null, null));
                                handleInfo.createAdsMultilingualStringDef();

                                descriptionEditor.requestEditorFocus();

                                documentationChanged();
                            }
                        } else {
                            super.mousePressed(e);
                        }
                    }
                }
            });

            if (isCollapsed) {
                XmlTreeUtils.collapseRecursive(xmlTree);
            } else {
                XmlTreeUtils.expandAll(xmlTree);
            }

            XmlTreeUtils.addTreeRMBListener(xmlTree, deleteDocItem);
        }

        TextComponentUtils.installUndoRedoAction(jEditorPane1);
    }

    private boolean isRootSelectInTree() {
        if (xmlTree == null
                || xmlTree.getModel() == null
                || xmlTree.getSelectionPath() == null
                || xmlTree.getSelectionPath().getLastPathComponent() == null) {
            return false;
        }
        return ((XmlTreeModel) xmlTree.getModel()).isRoot(xmlTree.getSelectionPath().getLastPathComponent());
    }

    private final ItemListener needsDocListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            schema.setNeedsDoc(chbNeedsDoc.isSelected());
        }
    };

    DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent de) {
            changeText();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            changeText();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            changeText();
        }
    };

    //private EditorUI eui;
    AdsXmlEditor() {
        initComponents();

    }
    private JLabel lblMess = new JLabel();

    private void checkLabel() {
        //schema.setXmlText(strNewText);

        String message = AdsXmlSchemeDef.getExceptionMessageForXsd(jEditorPane1.getText());

        if (message == null || message.isEmpty()) {
            //lblMess.setForeground(Color.green);
            lblMess.setForeground(new Color(0, 155, 0));
            lblMess.setText("  Valid");
        } else {
            lblMess.setForeground(Color.red);
            lblMess.setText("  Parse error - " + message);
        }
    }

    private static class Handler {

        XmlObject schema;
        String initialLocation;
        List<XmlUtils.Namespace2Location> locations;

        public Handler(XmlObject schema) {
            this.schema = schema;
        }
    }

    private void collectImportSchemas(XmlObject root, Map<String, Handler> schemas, String initialLocation) throws IOException {
        String ns = XmlUtils.getTargetNamespace(root);
        if (schemas.containsKey(ns)) {
            return;
        }
        Handler handler = new Handler(root);
        schemas.put(ns, handler);
        handler.initialLocation = initialLocation;

        handler.locations = XmlUtils.getImportedNamespaces2Loc(root, false);
        AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(schema);

        for (XmlUtils.Namespace2Location imp : handler.locations) {
            XmlObject obj = null;
            if (imp.isSpecialLocation()) {
                try {
                    URI uri = new URI(imp.location);
                    File file = new File(uri.getPath());
                    obj = ((AdsSegment) schema.getLayer().getAds()).getBuildPath().getPlatformXml().findXmlSchema(imp.namespace, file.getName(), ERuntimeEnvironmentType.COMMON);
                } catch (URISyntaxException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } else {
                IXmlDefinition xmlDef = searcher.findByNs(imp.namespace).get();
                if (xmlDef instanceof AdsXmlSchemeDef) {
                    obj = xmlDef.getXmlDocument();
                }
            }
            if (obj != null) {
                collectImportSchemas(XmlUtils.newCopy(obj), schemas, imp.location);
            }
        }
    }

    private void changeText() {
        isXmlChanged = true;
        String strNewText = jEditorPane1.getText();
        if (strNewText != null && !strNewText.equals(strOldValue)) {
            //isCheckFocusLoose = true;
            mustUpdate = true;
            schema.setXmlText(strNewText);
        }
        checkLabel();
    }

    AdsXmlSchemeDef schema = null;

    public void open(RadixObject definition, OpenInfo info) {
        schema = (AdsXmlSchemeDef) definition;
        schema.setChangeLogActualizationListener(createChangeLogActualizationListener());

        update(false);
        documentationEditorPanel.setDividerLocation(0.5);
        if (info != null) {
            RadixProblem problem = info.getLookup().lookup(RadixProblem.class);
            if (problem != null) {
                RadixProblem.IAnnotation annotation = problem.getAnnotation();
                if (annotation instanceof ProblemAnnotationFactory.TextPositionAnnotation) {
                    int line = ((ProblemAnnotationFactory.TextPositionAnnotation) annotation).line;
                    if (line > 0 && jEditorPane1 != null) {
                        String text = jEditorPane1.getText();
                        int index = text.indexOf('\n');
                        int cline = 1;
                        int lastFoundIndex = index;
                        while (index > 0) {
                            cline++;
                            lastFoundIndex = index;
                            if (cline == line) {
                                break;
                            }
                            index = text.indexOf('\n', index + 1);
                        }
                        lastFoundIndex++;
                        if (lastFoundIndex >= 0 && lastFoundIndex < text.length()) {
                            final int pos = lastFoundIndex;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    jEditorPane1.setCaretPosition(pos);
                                    jEditorPane1.requestFocus();
                                }
                            });

                        }
                    }
                } else if (annotation instanceof ProblemAnnotationFactory.XmlDocumentationAnnotation) {
                    editMode = DOCUMENTATION_EDIT_MODE;
                    update(false);
                    String xpath = ((ProblemAnnotationFactory.XmlDocumentationAnnotation) annotation).getNodeXpath();
                    hideUndocumentedBtn.setSelected(false);
                    xmlTree.applyFilter("", hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());

                    if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                        resetDocEditor();
                    }
                    xmlTreeState = XmlTreeUtils.getTreeState(xmlTree);
                }
            } else {
                final XmlLocation xmlFindInfo = info.getLookup().lookup(XmlLocation.class);
                if (xmlFindInfo != null) {
                    if (jEditorPane1 != null) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                jEditorPane1.setCaretPosition(xmlFindInfo.getMatcherStart());
                                jEditorPane1.requestFocus();
                            }
                        });
                    }
                }
            }
        }
    }

    private String getDir() {
        return schema.getModule().getSegment().getLayer().getDirectory().getParent() + "/.xsd";
    }

    private boolean createDir(String dir) {

        if (!(new File(dir)).exists()) {
            if (!(new File(dir)).mkdir()) {
                return false;
            }
        }
        return true;
    }

    private class AdsXmlSchemeDefProvider extends AdsVisitorProvider {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            return radixObject instanceof AdsXmlSchemeDef;
        }
    }

    private static String readFileAsString(String filePath)
            throws java.io.IOException {
        return FileUtils.readTextFile(new File(filePath), FileUtils.XML_ENCODING);

    }

    private void loadFile() {
        String fullPath = getDir() + "/" + getSchemaFile(schema);
        if (!(new File(fullPath)).exists()) {
            DialogUtils.messageError("File not found \"" + fullPath + "\"");
            return;
        }

        if (!DialogUtils.messageConfirmation("Do you really want to load from file \'" + fullPath + "\' and replace schema content?")) {//RADIX-11618             
            return;
        }

        try {
            String s = readFileAsString(fullPath);
            schema.setXmlText(s);
            strOldValue = s;
            jEditorPane1.setText(s);
            checkLabel();
            this.jEditorPane1.setCaretPosition(0);
        } catch (java.io.IOException ex) {
            DialogUtils.messageError("Error :" + ex.getMessage());
        }
    }

    private static boolean copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void saveAndEdit() {
        String dir = getDir();
        if (!createDir(dir)) {
            DialogUtils.messageError("Unable to create directory \"" + dir + "\"");
            return;
        }

        String file = getSchemaFile(schema);
        String buDir = dir + "/.backup";

        File oldFile = new File(dir + "/" + file);
        if (oldFile.exists()) {
            if (!createDir(buDir)) {
                DialogUtils.messageError("Unable to create backup directory \"" + buDir + "\"");
                return;
            }
            if (!copyfile(dir + "/" + file, buDir + "/" + file)) {
                DialogUtils.messageError("Unable to create backup file \"" + file + "\"");
            }
        }
        String schemaFileName = getSchemaFile(schema);
        String errMess = saveSchemaTree(new File(dir), schemaFileName, true);
        if (!errMess.isEmpty()) {
            DialogUtils.messageError("Saving of the following schemes has failled:" + errMess);
        }
        String fullPath = dir + "/" + getSchemaFile(schema);
        ExecAppByFileName.exec(fullPath);
    }

    private String getSchemaFile(AdsXmlSchemeDef schema) {
        if (schema == null) {
            return null;
        }
        String ns = schema.getTargetNamespace();
        if (ns == null) {
            return null;
        }
        ns = ns.trim();
        for (int i = ns.length() - 1; i >= 0; i--) {
            if (ns.charAt(i) == '\\' || ns.charAt(i) == '/') {
                ns = ns.substring(i + 1);
                break;
            }
        }
        if (ns.isEmpty()) {
            ns = schema.getName().toLowerCase() + ".xsd";
        }
        ns = FileUtils.string2UniversalFileName(ns, '-');
        if (ns.isEmpty()) {
            return null;
        }
        return ns;
    }

    private boolean isFilesEqual(File f1, File f2) {
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(f1));
            bufferedReader2 = new BufferedReader(new FileReader(f2));

            String str;
            String str2;
            while (true) {
                str = bufferedReader.readLine();
                str2 = bufferedReader2.readLine();
                if (str2 == null && str == null) {
                    return true;
                }
                if (str2 == null || str == null) {
                    return false;
                }
                if (!str2.equals(str)) {
                    return false;
                }
            }
        } catch (IOException ex) {
            DialogUtils.messageError(ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
            } catch (IOException ex) {
                DialogUtils.messageError(ex);
            }
        }
        return false;
    }

    private void addAdditionEnumInfoToAppInfo(Node node) {
        NodeList nodeList = node.getChildNodes();

        boolean isUseChilds = true;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String locName = node.getLocalName();

            if (locName != null && locName.equals("appinfo")) {
                int len = node.getAttributes().getLength();
                boolean isMustAdd = false;
                for (int j = 0; j < len; j++) {
                    Node n = node.getAttributes().item(j);
                    if (n.getNodeName().equals("source")) {
                        if (//n.getNodeName().equals("source") && 
                                n.getNodeValue().equals("http://schemas.radixware.org/types.xsd")) {
                            isMustAdd = true;
                            break;
                        }
                    }
                }
                String tUri = "";
                if (isMustAdd) {
                    AdsDefinition definition = null;
                    int len2 = node.getChildNodes().getLength();
                    for (int i = 0; i < len2; i++) {
                        Node node3 = node.getChildNodes().item(i);
                        if (node3.getNodeType() == Node.ELEMENT_NODE
                                && node3.getLocalName().equals("class")
                                && node3.getChildNodes().getLength() == 1
                                && node3.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) {
                            tUri = node3.getNamespaceURI();
                            //first look at classid attribute
                            Node classIdAttr = node3.getAttributes().getNamedItem("classId");
                            if (classIdAttr != null) {
                                String idCandidate = classIdAttr.getNodeValue();
                                Id id = Id.Factory.loadFrom(idCandidate);
                                DefinitionSearcher<AdsDefinition> searcher
                                        = AdsSearcher.Factory.newAdsDefinitionSearcher(schema);
                                definition = searcher.findById(id).get();
                            }
                            if (definition == null) {

                                String val = node3.getChildNodes().item(0).getNodeValue();

                                if (val != null && val.length() == Id.DEFAULT_ID_AS_STR_LENGTH) {
                                    Id id = Id.Factory.loadFrom(val);
                                    DefinitionSearcher<AdsDefinition> searcher
                                            = AdsSearcher.Factory.newAdsDefinitionSearcher(schema);
                                    definition = searcher.findById(id).get();
                                } else {
                                    //try to look for published enumeration
                                    IPlatformClassPublisher publisher = ((AdsSegment) schema.getLayer().getAds()).getBuildPath().getPlatformPublishers().findPublisherByName(val);
                                    if (publisher instanceof AdsDefinition) {
                                        definition = (AdsDefinition) publisher;
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (definition != null && definition instanceof AdsEnumDef) {
                        AdsEnumDef enumDef = (AdsEnumDef) definition;

                        //String xsPreff = node.getPrefix().isEmpty() ? "" : node.getPrefix() + ":";
                        Node doc = node.getOwnerDocument().createElementNS(
                                node.getNamespaceURI(), "documentation");
                        Node text = node.getOwnerDocument().createTextNode("Enum \"" + enumDef.getName() + "\"");

                        node.appendChild(doc);
                        doc.appendChild(text);

                        Node enumItems = node.getOwnerDocument().createElementNS(tUri, "enumItems");
                        node.appendChild(enumItems);

                        List<AdsEnumItemDef> itemList = enumDef.getItems().get(EScope.ALL);
                        for (AdsEnumItemDef item : itemList) {
                            Element enumItem = node.getOwnerDocument().createElementNS(tUri, "enumItem");
                            enumItems.appendChild(enumItem);

                            enumItem.setAttribute("Val", item.getValue().toString());
                            enumItem.setAttribute("Name", item.getName());
                            List<EIsoLanguage> lngLst
                                    = schema.getModule().getSegment().getLayer().getLanguages();
                            for (EIsoLanguage l : lngLst) {   //Element lng = node.getOwnerDocument().createElementNS(tUri, "Title");
                                if (item.getTitle(l) != null && !item.getTitle(l).trim().isEmpty()) {
                                    Element lng = node.getOwnerDocument().createElement("Title");
                                    enumItem.appendChild(lng);
                                    lng.setAttribute("language", l.getValue().toUpperCase());

                                    Node text2 = node.getOwnerDocument().createTextNode(item.getTitle(l));
                                    lng.appendChild(text2);
                                }
                            }
                        }
                    }
                }
                isUseChilds = false;
            }
        }
        if (isUseChilds) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                addAdditionEnumInfoToAppInfo(nodeList.item(i));
            }
        }
    }

    private boolean saveSchemaEx(AdsXmlSchemeDef schema, File path) {
        EEditState buEditState = schema.getEditState();
        String buText = schema.getXmlText();

        XmlObject obj = schema.getXmlDocument();
        if (obj == null) {
            obj = schema.getXmlContent();
        }

        if (obj != null) {
            try {
                Node node = obj.getDomNode();
                addAdditionEnumInfoToAppInfo(node);

                obj.save(path);
                schema.setXmlText(buText);
                schema.setEditState(buEditState);
                return true;
            } catch (IOException ex) {
                schema.setXmlText(buText);
                schema.setEditState(buEditState);
                Exceptions.printStackTrace(ex);
            }
        }
        schema.setXmlText(buText);
        schema.setEditState(buEditState);
        return false;
    }

    private boolean saveSchema(AdsXmlSchemeDef schema, String dir) {
        String ns = getSchemaFile(schema);
        if (ns == null) {
            return false;
        }
        XmlObject obj = schema.getXmlDocument();
        if (obj == null) {
            obj = schema.getXmlContent();
        }

        if (obj != null) {
            try {

                if (schema != this.schema) {
                    File tmp = new File(new File(dir), tempFile);
                    File realFile = new File(new File(dir), schema.getId().toString());
                    if (!realFile.exists()) {
                        obj.save(realFile);
                    } else {
                        obj.save(tmp);
                        if (!isFilesEqual(tmp, realFile)) {
                            obj.save(realFile);
                        }
                    }
                    if (tmp.exists()) {
                        tmp.delete();
                    }
                }
                File tmp = new File(new File(dir), tempFile);
                File realFile = new File(new File(dir), ns);
                //File file = new File(new File(dir), ns);
                if (!realFile.exists()) {
                    obj.save(realFile);
                } else {
                    obj.save(tmp);
                    if (!isFilesEqual(tmp, realFile)) {
                        obj.save(realFile);
                    }
                }

                //obj.save(file);
                return true;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);

            }
        }

        return false;
    }

    public boolean canClose() {
        if (schema == null || !schema.isInBranch()) {
            return true;
        }

        //if (isCheckFocusLoose)
        try {
            String text = schema.getXmlText();
            if (text == null) {
                text = "";
            }
            EEditState state = schema.getEditState();
            if (state == null) {
                return true;
            }
            boolean isIncorrect = !schema.isTransparent();
            isIncorrect &= !schema.setXmlText(jEditorPane1.getText());

            if (!isIncorrect
                    && state.equals(EEditState.NONE)
                    && text.equals(schema.getXmlText())) {
                schema.setEditState(state);
            }

            if (isIncorrect) {
                if (!DialogUtils.messageConfirmation("This scheme is invalid. Finish editing and loose changes?")) {
                    mustUpdate = false;
                    jEditorPane1.requestFocus();
                    return false;
                } else {
                    mustUpdate = true;
                }
            }
        } catch (NullPointerException e) {
        }
        return true;
    }

    public void update(boolean format) {
        editMode = editMode == null ? SOURCE_EDIT_MODE : editMode;

        int pos = 0;
        if (jEditorPane1 != null) {
            pos = jEditorPane1.getCaretPosition();
        }

        jPanel4.removeAll();
        initComponents2();

        if (accessEditPanel == null || schema == null) {
            return;
        }
        if (schema != null) {
            accessEditPanel.open(schema);

            edit.setEnabled(!schema.isTransparent());
            load.setEnabled(!schema.isTransparent());

            strOldValue = null;
            XmlObject obj = schema.getXmlDocument();
            if (obj == null) {
                obj = schema.getXmlContent();
            }

            if (obj != null) {
                XmlOptions options = new XmlOptions();
                if (format) {
                    options.setSavePrettyPrint();
                }
                options.setSaveNamespacesFirst();

                options.put(XmlOptions.LOAD_ADDITIONAL_NAMESPACES);
                options.put(XmlOptions.CHARACTER_ENCODING);
                options.put(XmlOptions.DOCUMENT_TYPE);

                strOldValue = obj.xmlText(options);
            }

            if (strOldValue == null) {
                strOldValue = schema.getXmlText();
            }
            if (!schema.isTransparent() && !schema.isReadOnly()) {
                this.jEditorPane1.setText(strOldValue);
                this.jEditorPane1.setEditable(true);

                jEditorPane1.setBackground(Color.WHITE);
            } else {
                this.jEditorPane1.setText(strOldValue);
                this.jEditorPane1.setEditable(false);
                jEditorPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
            }
            //this.jEditorPane1.setCaretPosition(0);
            discardOldUndoableEdits();
        } else {
            edit.setEnabled(false);
            load.setEnabled(false);
        }
        checkLabel();

        jEditorPane1.getDocument().putProperty(AdsXmlSchemeDef.class, schema);

        if (jEditorPane1 != null) {
            try {
                this.jEditorPane1.setCaretPosition(pos);
            } catch (Exception ex) {
                this.jEditorPane1.setCaretPosition(0);
            }
        }

    }

    private void discardOldUndoableEdits() {
        final UndoManager manager = (UndoManager) jEditorPane1.getDocument().getProperty(BaseDocument.UNDO_MANAGER_PROP);
        if (manager != null) {
            manager.discardAllEdits();
        }
    }

    private JToolBar prepareSourceEditorToolBar(JToolBar sourceToolBar) {
        edit = new JButton();
        RadixNbEditorUtils.processToolbarButton(edit);
        sourceToolBar.add(edit);
        edit.setIcon(RadixWareIcons.DIALOG.EDIT.EDIT.getIcon());
        edit.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "edit-button-tooltip"));
        edit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                saveAndEdit();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        load = new JButton();
        RadixNbEditorUtils.processToolbarButton(load);
        sourceToolBar.add(load, 1);
        load.setIcon(RadixWareIcons.DIALOG.ARROW.MOVE_UP.getIcon());
        load.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "load-button-tooltip"));
        load.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadFile();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        format = new JButton();
        RadixNbEditorUtils.processToolbarButton(format);
        sourceToolBar.add(format, 2);
        format.setIcon(RadixWareIcons.EDIT.FIX.getIcon());
        format.setToolTipText("Format Text");
        format.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update(true);
                schema.setXmlText(jEditorPane1.getText());
            }
        });

        unLoad = new JButton();
        RadixNbEditorUtils.processToolbarButton(unLoad);
        sourceToolBar.add(unLoad, 3);
        unLoad.setIcon(RadixWareIcons.FILE.SAVE.getIcon());
        unLoad.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "unload-button-tooltip"));
        unLoad.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                JFileChooser c = new JFileChooser();
                String sFilePath = getSchemaFile(schema);
                if (sFilePath == null) {
                    DialogUtils.messageError("File unloading error");
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                c.setSelectedFile(new File(sFilePath));
                c.setFileFilter(new FileNameExtensionFilter("Xsd - schema", "xsd"));
                c.setAcceptAllFileFilterUsed(false);
                c.setDialogType(JFileChooser.SAVE_DIALOG);

                int rVal = c.showSaveDialog(jEditorPane1);
                if (rVal == JFileChooser.APPROVE_OPTION) {

                    if (!saveSchemaEx(schema, new File(c.getCurrentDirectory(),
                            c.getSelectedFile().getName()))) {
                        DialogUtils.messageError("File unloading error");
                    }
                }

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        unLoadWithImport = new JButton();
        RadixNbEditorUtils.processToolbarButton(unLoadWithImport);
        sourceToolBar.add(unLoadWithImport, 4);
        unLoadWithImport.setIcon(RadixWareIcons.FILE.SAVE_ALL.getIcon());
        unLoadWithImport.setToolTipText(NbBundle.getMessage(AdsXmlEditor.class, "unLoadWithImport-button-tooltip"));
        unLoadWithImport.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                JFileChooser c = new JFileChooser();
                String sFilePath = getSchemaFile(schema);
                if (sFilePath == null) {
                    DialogUtils.messageError("File unloading error");
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                c.setSelectedFile(new File(sFilePath));
                c.setFileFilter(new FileNameExtensionFilter("Xsd - schema", "xsd"));
                c.setAcceptAllFileFilterUsed(false);
                c.setDialogType(JFileChooser.SAVE_DIALOG);

                int rVal = c.showSaveDialog(jEditorPane1);
                if (rVal != JFileChooser.APPROVE_OPTION) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                String errMessage = saveSchemaTree(c.getCurrentDirectory(), c.getSelectedFile().getName(), false);
                if (!errMessage.isEmpty()) {
                    DialogUtils.messageError("Saving of the following schemes has failled:" + errMessage);
                }

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        unLoad.setVisible(Boolean.getBoolean("rdx.use.old.xsd.export"));
        unLoadWithImport.setVisible(Boolean.getBoolean("rdx.use.old.xsd.export"));

        JButton exportSchemaBtn = new JButton(RadixWareIcons.FILE.SAVE.getIcon());
        exportSchemaBtn.setToolTipText("Export Schema");
        RadixNbEditorUtils.processToolbarButton(exportSchemaBtn);
        exportSchemaBtn.setVisible(!Boolean.getBoolean("rdx.use.old.xsd.export"));
        sourceToolBar.add(exportSchemaBtn, 5);

        exportSchemaBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final List<IExportableXmlSchema> schemas = new ArrayList<>();
                schemas.add(new AdsXmlSchemeExportableWrapper((AdsXmlSchemeDef) schema));

                // layerEntries
                final Map<String, FileProvider.LayerEntry> layerEntries = new HashMap<>();
                final Set<EIsoLanguage> languages = new HashSet<>();

                Layer topLayer = schema.getLayer();
                Branch branch = topLayer.getBranch();

                List<Layer> localizingLayers = new ArrayList<>();
                for (Layer layer : branch.getLayers()) {
                    languages.addAll(layer.getLanguages());
                    List<FileProvider.ModuleEntry> modules = new ArrayList<>();
                    for (Module module : layer.getAds().getModules()) {
                        modules.add(new FileProvider.ModuleEntry(layer.getAds().getName().toLowerCase(), module.getName()));
                    }

                    if (!layer.isLocalizing()) {
                        layerEntries.put(layer.getURI(), new FileProvider.LayerEntry(layer.getURI(), layer.getURI(), 1, modules));
                    } else {
                        localizingLayers.add(layer);
                    }
                }

                for (Layer layer : localizingLayers) {
                    if (layer.getBaseLayerURIs() != null && !layer.getBaseLayerURIs().isEmpty()) {
                        FileProvider.LayerEntry layerEntry = layerEntries.get(layer.getBaseLayerURIs().get(0));
                        if (layer.getLanguages() != null && !layer.getLanguages().isEmpty()) {
                            layerEntry.addLocalizingLayer(layer.getURI(), layer.getLanguages().get(0));
                        }
                    }
                }

                // porvider
                final LocalFileProvider provider;

                String path = topLayer.getBranch().getFile().getParent();
                provider = new LocalFileProvider(path, null) {

                    @Override
                    public Collection<FileProvider.LayerEntry> getLayers() {
                        return layerEntries.values();
                    }
                };

                XmlSchemasExportDialog dialog = new XmlSchemasExportDialog(
                        (Window) SwingUtilities.getWindowAncestor(AdsXmlEditor.this),
                        schemas,
                        new ArrayList<>(languages),
                        layerEntries.keySet(),
                        false
                );

                XmlSchemasExportTask task = dialog.show();

                RadixdocGenerationContext context = new DesignerRadixdocGenerationContext(provider, dialog.getTopLayerUri());

                XmlSchemaExporter exporter = new XmlSchemaExporter(task, context);
                exporter.exportSchemas(false);
            }
        });

        return sourceToolBar;
    }

    private JToolBar prepareDocumentationEditorToolBar(JToolBar sourceToolBar) {
        sourceToolBar.removeAll();
        sourceToolBar.setOpaque(false);
        sourceToolBar.setFloatable(false);
        sourceToolBar.setBorderPainted(false);

        JButton openChangeLogEditorBtn = new JButton();
        openChangeLogEditorBtn.setIcon(RadixWareIcons.XML.CHANGE_LOG.getIcon());
        openChangeLogEditorBtn.setToolTipText("Open Change Log Editor");
        RadixNbEditorUtils.processToolbarButton(openChangeLogEditorBtn);
        openChangeLogEditorBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Map<AdsXmlSchemeDef.ChangeLogEntry, EEditState> changedEntries = new HashMap<>();
                ModalDisplayer displayer = new ModalDisplayer(createChangeLogEditor(changedEntries), "Change Log", new Object[]{DialogDescriptor.OK_OPTION});
                displayer.showModal();
                schema.applyChangeLogChanges(changedEntries);
            }
        });

        JButton parseXmlDocBtn = new JButton();
        parseXmlDocBtn.setIcon(RadixWareIcons.ARROW.UP_RIGHT.getIcon());
        parseXmlDocBtn.setToolTipText("Import Documentation");
        RadixNbEditorUtils.processToolbarButton(parseXmlDocBtn);
        parseXmlDocBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DocImportOptionsPanel importOptionsPanel = new DocImportOptionsPanel(schema.getLayer().getLanguages());
                ModalDisplayer modalDispl = new ModalDisplayer(importOptionsPanel, "Import Documentation");
                if (modalDispl.showModal()) {
                    MessageBuffer console = new MessageBuffer("Import Documentation", false);
                    Map<String, Map<EIsoLanguage, String>> finalXPathMap = parseDocument(importOptionsPanel.getLanguage(), console, schema.getLayer().getLanguages());
                    if (console.hasErrors()) {
                        console.addMessage("Import is failed!\n", true);
                        console.printMessages(true);
                    } else if (finalXPathMap.isEmpty()) {
                        console.addMessage("There where no 'documentation' tags in source document\n", true);
                        console.printMessages(true);
                    } else {
                        importDocumentation(finalXPathMap, importOptionsPanel.getVersion(), importOptionsPanel.isReplaceNeeded());
                        if (importOptionsPanel.isRemoveNeeded()) {
                            cleanSource();
                        }

                        console.addMessage(MessageBuffer.SUCCESSFUL_MESSAGE, false);
                        console.printMessages(true);

                        descriptionEditor.update(handleInfo);
                        if (importOptionsPanel.isReplaceNeeded()) {
                            sinceVersion.setText(importOptionsPanel.getVersion());
                        }
                        update(false);
                    }
                }
            }
        });

        JButton prevUndocumentedBtn = new JButton(RadixWareIcons.XML.PREV_UNDOCUMENTED.getIcon());
        prevUndocumentedBtn.setToolTipText("Select Previous Undocumented Node");
        RadixNbEditorUtils.processToolbarButton(prevUndocumentedBtn);

        prevUndocumentedBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                xmlTree.prevUndocumentedNode();
            }
        });

        JButton nextUndocumentedBtn = new JButton(RadixWareIcons.XML.NEXT_UNDOCUMENTED.getIcon());
        nextUndocumentedBtn.setToolTipText("Select Next Undocumented Node");
        RadixNbEditorUtils.processToolbarButton(nextUndocumentedBtn);

        nextUndocumentedBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                xmlTree.nextUndocumentedNode();
            }
        });

        hideDocumentedBtn.setToolTipText("Hide Documented Nodes");
        RadixNbEditorUtils.processToolbarButton(hideDocumentedBtn);

        hideUndocumentedBtn.setToolTipText("Hide Undocumented Nodes");
        RadixNbEditorUtils.processToolbarButton(hideUndocumentedBtn);

        hideServiceNodesBtn.setToolTipText("Hide Service Nodes");
        RadixNbEditorUtils.processToolbarButton(hideServiceNodesBtn);

        hideDocumentedBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String xpath = selectedNodePath.getText();
                xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                    resetDocEditor();
                }
                isCollapsed = false;
                collapseAllBtn.setSelected(false);
            }
        });

        hideUndocumentedBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String xpath = selectedNodePath.getText();
                xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                    resetDocEditor();
                }
                isCollapsed = false;
                collapseAllBtn.setSelected(false);
            }
        });

        hideServiceNodesBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String xpath = selectedNodePath.getText();
                xmlTree.setServiceNodesVisible(hideServiceNodesBtn.isSelected());
                xmlTree.applyFilter(filterEditor.getText(), hideDocumentedBtn.isSelected(), hideUndocumentedBtn.isSelected());
                if (!XmlTreeUtils.setSelectedNode(xmlTree, xpath)) {
                    resetDocEditor();
                }
                isCollapsed = false;
                collapseAllBtn.setSelected(false);
            }
        });

        collapseAllBtn.setToolTipText("Collapse All Nodes");
        RadixNbEditorUtils.processToolbarButton(collapseAllBtn);
        collapseAllBtn.setSelected(isCollapsed);

        collapseAllBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                isCollapsed = ((JToggleButton) e.getSource()).isSelected();
                if (isCollapsed) {
                    XmlTreeUtils.collapseRecursive(xmlTree);
                } else {
                    XmlTreeUtils.expandAll(xmlTree);
                }
            }
        });

        JButton editLinkedSchemasListBtn = new JButton(RadixWareIcons.XML.LINKED_SCHEMAS.getIcon());
        editLinkedSchemasListBtn.setToolTipText("Edit Linked Schemas List");
        RadixNbEditorUtils.processToolbarButton(editLinkedSchemasListBtn);

        editLinkedSchemasListBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ModalDisplayer displayer = new ModalDisplayer(new LinkedSchemasEditorPanel(schema), "Linked Schemas", new Object[]{DialogDescriptor.OK_OPTION});
                displayer.showModal();
            }
        });

        JButton analyzeChangesBtn = new JButton(RadixWareIcons.DIFF.XSD_DIFF.getIcon());
        analyzeChangesBtn.setToolTipText("Analyze Schema Changes");
        RadixNbEditorUtils.processToolbarButton(analyzeChangesBtn);

        analyzeChangesBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File currentDir = schema.getBranch().getDirectory();
                final AtomicReference<String> diffHtml = new AtomicReference<>();

                BranchFileChooser fileChooser = BranchFileChooser.createFileChooser(schema.getBranch().getFile(), schema.getBranch().getBaseDevelopmentLayerUri(), currentDir.getAbsolutePath());
                while (fileChooser.showOpenDialog(AdsXmlEditor.this) == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
                    File otherDir = fileChooser.getSelectedFile();
                    if (Branch.isBranchDir(otherDir) && !otherDir.equals(currentDir)) {
                        final FileProvider fileProvider = new LocalFileProvider(otherDir.getAbsolutePath(), null) {
                            @Override
                            public Collection<FileProvider.LayerEntry> getLayers() {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }
                        };
                        final AdsXmlSchemeExportableWrapper schemaWrapper = new AdsXmlSchemeExportableWrapper(schema);
                        final String currentBranchVersion = schema.getBranch().getLastReleaseVersion();
                        final String otherBranchVersion = getBranchVersion(otherDir);

                        final ProgressHandle progress = ProgressHandleFactory.createHandle("Analalyzing schemas diff");
                        Runnable tsk = new Runnable() {
                            @Override
                            public void run() {
                                progress.start();
                                progress.switchToIndeterminate();
                                diffHtml.set(org.radixware.kernel.radixdoc.xmlexport.XsdDiffAnalyzer.getDiffHtml(fileProvider, schemaWrapper, currentBranchVersion, otherBranchVersion));
                                progress.finish();

                                VersionNumber currentVersionNumber = VersionNumber.valueOf(currentBranchVersion);
                                VersionNumber otherVersionNumber = VersionNumber.valueOf(otherBranchVersion);
                                final String title;
                                if (currentVersionNumber.compareTo(otherVersionNumber) > 0) {
                                    title = "Changes between " + otherBranchVersion + " and " + currentBranchVersion;
                                } else {
                                    title = "Changes between " + currentBranchVersion + " and " + otherBranchVersion;
                                }
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        XsdDiffDispalyer.display(diffHtml.get(), title);
                                    }
                                });
                            }
                        };
                        RequestProcessor.submit(tsk);
                        break;
                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.messageError("Incorrect branch directory");
                            }
                        });
                    }
                }
            }
        });

        sourceToolBar.add(openChangeLogEditorBtn);
        sourceToolBar.add(parseXmlDocBtn);
        sourceToolBar.add(prevUndocumentedBtn);
        sourceToolBar.add(nextUndocumentedBtn);
        sourceToolBar.add(hideDocumentedBtn);
        sourceToolBar.add(hideUndocumentedBtn);
        sourceToolBar.add(hideServiceNodesBtn);
        sourceToolBar.add(collapseAllBtn);
        sourceToolBar.add(editLinkedSchemasListBtn);
        sourceToolBar.add(analyzeChangesBtn);

        return sourceToolBar;
    }

    private String getBranchVersion(File branchFile) {
        String lastReleaseNumber = null;
        final File branchXmlFile = new File(branchFile.getAbsolutePath() + File.separator + org.radixware.kernel.common.constants.FileNames.BRANCH_XML);
        if (branchXmlFile.exists()) {
            try {
                final BranchDocument branchDocument = BranchDocument.Factory.parse(branchXmlFile);
                final org.radixware.schemas.product.Branch xBranch = branchDocument.getBranch();
                lastReleaseNumber = xBranch.getLastRelease();
            } catch (XmlException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return lastReleaseNumber;
    }

    private void updateSinceVersion() {
        if (sinceVersion.getText() != null && !"".equals(sinceVersion.getText())) {
            if (selectedNodePath.getText() != null && !"".equals(selectedNodePath.getText())) {
                AdsXmlSchemeDef.XmlItemDocEntry docEntry;
                if (schema.getXmlItemDocEntry(selectedNodePath.getText()) != null) {
                    docEntry = schema.getXmlItemDocEntry(selectedNodePath.getText());
                    if (!docEntry.getSinceVersion().equals(sinceVersion.getText())) {
                        schema.addNodeDescription(selectedNodePath.getText(), new AdsXmlSchemeDef.XmlItemDocEntry(docEntry.getId(), sinceVersion.getText()));
                    }
                } else {
                    docEntry = new AdsXmlSchemeDef.XmlItemDocEntry(null, sinceVersion.getText());
                    schema.addNodeDescription(selectedNodePath.getText(), docEntry);
                }
            }
        }
    }

    private List<String> getSchemaElementsXPathList(Element node) {
        List<String> result = new ArrayList<>();

        List<Element> children = XmlUtils.getChildElements(node);
        for (Element child : children) {
            result.addAll(getSchemaElementsXPathList(child));
        }
        result.add(XPathUtils.getXPath(node));

        return result;
    }

    private void updateDocEditor() {
        isXmlChanged = false;
        update(false);

        XmlObject obj = schema.getXmlDocument();
        if (obj == null) {
            obj = schema.getXmlContent();
            if (obj == null) {
                return;
            }
        }
        Element root = XmlUtils.findFirstElement(obj.getDomNode());

        List<String> treeNodesXPathList = getSchemaElementsXPathList(root);
        for (String nodeXPath : schema.getDocumentedNodes()) {
            if (!treeNodesXPathList.contains(nodeXPath)) {
                schema.removeNodeDescription(nodeXPath);
            }
        }

        if (!treeNodesXPathList.contains(selectedNodePath.getText())) {
            selectedNodePath.setText("");
        }
        descriptionEditor.open(handleInfo);
    }

    private void resetDocEditor() {
        addingDescriptionButton.setVisible(false);
        descriptionEditor.setVisible(false);
        sinceLabel.setVisible(false);
        sinceVersion.setVisible(false);
        rootDescriptionPanel.setVisible(false);
        selectedNodePath.setText("");
    }

    private JPanel createChangeLogEditor(final Map<AdsXmlSchemeDef.ChangeLogEntry, EEditState> changedEntries) {
        final DefaultTableModel model = new DefaultTableModel(new String[]{"Version", "Author", "Date", "Guid"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable changeLogTable = new JTable(model);
        final List<AdsXmlSchemeDef.ChangeLogEntry> changeLog = schema.getChangeLog();

        for (EIsoLanguage lang : schema.getLayer().getLanguages()) {
            model.addColumn("Description (" + lang.getName() + ")");
        }
        final int firstLangIndex = model.getColumnCount() - schema.getLayer().getLanguages().size();

        changeLogTable.removeColumn(changeLogTable.getColumnModel().getColumn(firstLangIndex - 1));
        changeLogTable.getTableHeader().setReorderingAllowed(false);

        changeLogTable.getColumnModel().getColumn(0).setMinWidth(45);
        changeLogTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        changeLogTable.getColumnModel().getColumn(0).setMaxWidth(60);

        fillChangeLogTable(changeLogTable, changeLog, changedEntries);

        JScrollPane tablePane = new JScrollPane(changeLogTable);

        final JButton editChangeLogEntryBtn = new JButton("Edit", RadixWareIcons.EDIT.EDIT.getIcon());
        editChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        editChangeLogEntryBtn.setEnabled(false);
        editChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int guidRow = changeLogTable.getSelectedRow();
                int guidCol = firstLangIndex - 1;

                if (guidRow < 0) {
                    return;
                }

                AdsXmlSchemeDef.ChangeLogEntry entry = ChangeLogUtils.getFromChangeLog(changeLog, model.getValueAt(guidRow, guidCol).toString());
                ModalDisplayer displayer = new ModalDisplayer(createChangeLogEntryEditor(entry), "Change Log Entry");
                if (displayer.showModal()) {
                    changedEntries.put(entry, EEditState.MODIFIED);
                    ChangeLogUtils.applyChangesToChangeLog(changeLog, changedEntries);
                    fillChangeLogTable(changeLogTable, changeLog, changedEntries);
                    changeLogTable.setRowSelectionInterval(guidRow, guidRow);
                }
            }
        });

        final JButton removeChangeLogEntryBtn = new JButton("Remove", RadixWareIcons.DELETE.DELETE.getIcon());
        removeChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        removeChangeLogEntryBtn.setEnabled(false);
        removeChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = changeLogTable.getSelectedRows();
                if (rows.length > 0) {
                    for (int i = rows.length - 1; i >= 0; i--) {
                        AdsXmlSchemeDef.ChangeLogEntry entry = ChangeLogUtils.getFromChangeLog(changeLog, model.getValueAt(rows[i], firstLangIndex - 1).toString());
                        AdsXmlSchemeDef.ChangeLogEntry entryCopy = new AdsXmlSchemeDef.ChangeLogEntry(entry);

                        Iterator<Entry<AdsXmlSchemeDef.ChangeLogEntry, EEditState>> iter = changedEntries.entrySet().iterator();
                        while (iter.hasNext()) {
                            if (iter.next().getKey().getGuid().equals(entryCopy.getGuid())) {
                                iter.remove();
                            }
                        }

                        changedEntries.put(entryCopy, EEditState.NONE);
                        ChangeLogUtils.applyChangesToChangeLog(changeLog, changedEntries);
                        fillChangeLogTable(changeLogTable, changeLog, changedEntries);
                    }
                    if (rows[0] > 0) {
                        changeLogTable.setRowSelectionInterval(rows[0] - 1, rows[0] - 1);
                    } else if (rows[rows.length - 1] - model.getRowCount() - 1 > 0) {
                        changeLogTable.setRowSelectionInterval(0, 0);
                    }
                }
            }
        });

        JButton addChangeLogEntryBtn = new JButton("Add", RadixWareIcons.CREATE.ADD.getIcon());
        addChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsXmlSchemeDef.ChangeLogEntry entry = new AdsXmlSchemeDef.ChangeLogEntry();
                ModalDisplayer displayer = new ModalDisplayer(createChangeLogEntryEditor(entry), "Change Log Entry");
                if (displayer.showModal()) {
                    changedEntries.put(entry, EEditState.NEW);
                    ChangeLogUtils.applyChangesToChangeLog(changeLog, changedEntries);
                    fillChangeLogTable(changeLogTable, changeLog, changedEntries);
                    changeLogTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
                }
            }
        });

        changeLogTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                final boolean thereIsSelectedRows = changeLogTable.getSelectedRowCount() > 0;
                editChangeLogEntryBtn.setEnabled(thereIsSelectedRows);
                removeChangeLogEntryBtn.setEnabled(thereIsSelectedRows);
            }
        });
        if (!changeLog.isEmpty()) {
            changeLogTable.setRowSelectionInterval(0, 0);
        }

        JPanel result = new JPanel(new MigLayout("fill", "[grow][shrink]", "[][][][]"));
        result.setBorder(new EmptyBorder(7, 7, 7, 7));
        result.add(tablePane, "grow, cell 0 0 1 4");
        result.add(addChangeLogEntryBtn, "cell 1 0, growx, wrap");
        result.add(removeChangeLogEntryBtn, "cell 1 1, growx, wrap");
        result.add(editChangeLogEntryBtn, "cell 1 2, growx, wrap");

        return result;
    }

    private JPanel createChangeLogEntryEditor(final AdsXmlSchemeDef.ChangeLogEntry entry) {
        final Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis());

        JLabel authorLabel = new JLabel("Author:");
        final JTextField authorEditor = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        AdsValAsStrEditor dateEditor = new AdsValAsStrEditor();
        dateEditor.open(getDateEditorValueController());

        JLabel versionLabel = new JLabel("Version:");
        final JTextField versionEditor = new JTextField();

        if (entry != null) {
            if (entry.getAuthor() == null) {
                entry.setAuthor(System.getProperty("user.name"));
            }
            authorEditor.setText(entry.getAuthor());

            if (entry.getDate() == null) {
                entry.setDate(date);
            }
            ValAsStr val = ValAsStr.Factory.newInstance(entry.getDate().getTime(), EValType.DATE_TIME);
            dateEditor.setValue(AdsValAsStr.Factory.newInstance(val));

            if (entry.getVersion() != null) {
                versionEditor.setText(entry.getVersion());
            }
        }

        DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                entry.setAuthor(authorEditor.getText());
                entry.setVersion(versionEditor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                entry.setAuthor(authorEditor.getText());
                entry.setVersion(versionEditor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                entry.setAuthor(authorEditor.getText());
                entry.setVersion(versionEditor.getText());
            }
        };

        authorEditor.getDocument().addDocumentListener(docListener);
        versionEditor.getDocument().addDocumentListener(docListener);
        dateEditor.addValueChangeListener(new ValueChangeListener<AdsValAsStr>() {
            @Override
            public void valueChanged(ValueChangeEvent<AdsValAsStr> e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
                try {
                    date.setTime(dateFormat.parse(e.newValue.toString()));
                } catch (ParseException ex) {
                    date.setTimeInMillis(System.currentTimeMillis());
                }
                entry.setDate(date);
            }
        });

        LocalizingStringEditor changeLogDescriptionEditor = LocalizingStringEditor.Factory.createEditor(new Options()
                .add(Options.COLLAPSABLE_KEY, false)
                .add(Options.TITLE_KEY, "Description")
                .add(Options.MODE_KEY, EEditorMode.MULTILINE));
        changeLogDescriptionEditor.open(new HandleInfo() {

            @Override
            public Definition getAdsDefinition() {
                return schema;
            }

            @Override
            public Id getTitleId() {
                if (entry != null) {
                    return entry.getId();
                } else {
                    return null;
                }
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    if (entry.getId() != multilingualStringDef.getId()) {
                        entry.setId(multilingualStringDef.getId());
                    }
                    entry.setVersion(versionEditor.getText());
                    entry.setDate(date);
                    entry.setAuthor(authorEditor.getText());
                } else {
                    schema.removeChangeLogEntry(entry.getGuid());
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                if (getAdsMultilingualStringDef() != null) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        });

        JPanel result = new JPanel(new MigLayout("fill", "[shrink][grow]", "[shrink][shrink][grow][shrink]"));
        result.setBorder(new EmptyBorder(7, 7, 7, 7));
        result.add(authorLabel);
        result.add(authorEditor, "growx, wrap");
        result.add(dateLabel);
        result.add(dateEditor, "growx, wrap");
        result.add(changeLogDescriptionEditor, "span, grow, wrap");
        result.add(versionLabel);
        result.add(versionEditor, "growx");

        return result;
    }

    private void fillChangeLogTable(JTable changeLogTable, List<AdsXmlSchemeDef.ChangeLogEntry> changeLog, Map<AdsXmlSchemeDef.ChangeLogEntry, EEditState> changedEntries) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        final DefaultTableModel model = (DefaultTableModel) changeLogTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        Collections.sort(changeLog, new Comparator<AdsXmlSchemeDef.ChangeLogEntry>() {
            @Override
            public int compare(AdsXmlSchemeDef.ChangeLogEntry o1, AdsXmlSchemeDef.ChangeLogEntry o2) {
                if (o1.getVersion() != null && o2.getVersion() != null) {
                    VersionNumber v1 = VersionNumber.valueOf(o1.getVersion());
                    VersionNumber v2 = VersionNumber.valueOf(o2.getVersion());
                    return v2.compareTo(v1);
                } else {
                    if (o1.getDate() != null && o2.getDate() != null) {
                        return o2.getDate().compareTo(o1.getDate());
                    } else {
                        return 0;
                    }
                }
            }
        });

        int number = 0;
        for (AdsXmlSchemeDef.ChangeLogEntry entry : changeLog) {
            if (changedEntries.get(entry) != null) {
                if (changedEntries.get(entry) == EEditState.NONE) {
                    continue;
                }
            }
            AdsMultilingualStringDef mls = schema.findLocalizedString(entry.getId());
            model.addRow(new Object[]{entry.getVersion(), entry.getAuthor(), dateFormat.format(entry.getDate().getTime()), entry.getVersion()});

            int langIndex = model.getColumnCount() - schema.getLayer().getLanguages().size();
            model.setValueAt(entry.getGuid(), number, langIndex - 1);

            for (EIsoLanguage lang : schema.getLayer().getLanguages()) {
                String description;
                if (mls != null) {
                    description = mls.getValue(lang) == null ? "" : mls.getValue(lang);
                } else {
                    description = "";
                }
                model.setValueAt(description, number, langIndex);
                langIndex++;
            }
            number++;
        }
    }

    private AdsValAsStr.IValueController getDateEditorValueController() {
        return new AdsValAsStr.IValueController() {

            @Override
            public boolean isValueTypeAvailable(final AdsValAsStr.EValueType type) {
                return type != AdsValAsStr.EValueType.JML;
            }

            @Override
            public AdsTypeDeclaration getContextType() {
                return AdsTypeDeclaration.Factory.newInstance(EValType.DATE_TIME);
            }

            @Override
            public Definition getContextDefinition() {
                return null;
            }

            @Override
            public void setValue(final AdsValAsStr value) {
                //System.out.println(value);
            }

            @Override
            public AdsValAsStr getValue() {
                return null;
            }

            @Override
            public String getValuePresentation() {
                return AdsValAsStr.DefaultPresenter.getAsString(this);
            }
        };
    }

    private class MessageBuffer {

        final static String SUCCESSFUL_MESSAGE = "Import Successful!";

        List<String> buffer;
        String tabTitle;
        boolean isNewTab;
        boolean hasErrors;

        public MessageBuffer(String tabTitle, boolean isNewTab) {
            hasErrors = false;
            this.tabTitle = tabTitle;
            this.isNewTab = isNewTab;
            buffer = new ArrayList<>();
        }

        public void addMessage(String message, boolean isErrorMessage) {
            if (isErrorMessage) {
                hasErrors = true;
            }
            buffer.add(message);
        }

        public boolean isEmpty() {
            return buffer.isEmpty();
        }

        public void printMessages(boolean shouldClearBuffer) {
            if (!isEmpty()) {
                InputOutput io = IOProvider.getDefault().getIO(tabTitle, isNewTab);
                try {
                    io.getOut().reset();
                    io.select();
                    for (String message : buffer) {
                        Color lineColor = Color.BLACK;
                        if (hasErrors() || message.toLowerCase().contains("warning")) {
                            lineColor = Color.RED.darker();
                        }
                        if (message.equals(SUCCESSFUL_MESSAGE)) {
                            lineColor = Color.GREEN.brighter();
                        }

                        IOColorLines.println(io, message, lineColor);
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                if (shouldClearBuffer) {
                    clearBuffer();
                }
            }
        }

        private void clearBuffer() {
            buffer.clear();
        }

        public boolean hasErrors() {
            return hasErrors;
        }
    }

    private XmlObject[] xPathSelect(String namespace, String select) {
        return schema.getXmlDocument().selectPath("declare namespace " + namespace + ";" + select);
    }

    private Map<String, Map<EIsoLanguage, String>> parseDocument(EIsoLanguage defaultLang, MessageBuffer console, List<EIsoLanguage> langs) {
        Map<String, Map<EIsoLanguage, String>> finalXPathMap = new HashMap<>();
        final String xmlNS = "http://www.w3.org/XML/1998/namespace";
        for (org.apache.xmlbeans.XmlObject item : xPathSelect("xs='http://www.w3.org/2001/XMLSchema'",
                "$this//xs:annotation")) {
            NodeList nodeList = item.getDomNode().getChildNodes();
            Map<EIsoLanguage, String> langMap = new HashMap<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getLocalName() != null && nodeList.item(i).getLocalName().equals("documentation")) {
                    String nodeLang = "";
                    Node langAttrNode = nodeList.item(i).getAttributes().getNamedItemNS(xmlNS, "lang");
                    if (langAttrNode != null) {
                        nodeLang = langAttrNode.getNodeValue();
                    }

                    if (nodeList.item(i).getAttributes().getLength() == 0 || nodeLang.isEmpty()) {
                        if (langMap.containsKey(defaultLang)) {
                            console.addMessage("Error: Duplicated documentation for language 'default' in element \""
                                    + new XmlTreeNode((Element) item.getDomNode().getParentNode()).getXPath() + "\"", true);
                        } else {
                            if (((Element) nodeList.item(i)).getFirstChild() != null) {
                                langMap.put(defaultLang, ((Element) nodeList.item(i)).getFirstChild().getNodeValue());
                            }
                        }
                    } else {
                        try {
                            if (langs.contains(EIsoLanguage.getForValue(nodeLang))) {
                                if (langMap.containsKey(EIsoLanguage.getForValue(nodeLang))) {
                                    console.addMessage("Error: Duplicated documentation for language '" + EIsoLanguage.getForValue(nodeLang).getName() + "' in element \""
                                            + new XmlTreeNode((Element) item.getDomNode().getParentNode()).getXPath() + "\"", true);
                                } else {
                                    if (((Element) nodeList.item(i)).getFirstChild() != null) {
                                        langMap.put(EIsoLanguage.getForValue(nodeLang), ((Element) nodeList.item(i)).getFirstChild().getNodeValue());
                                    }
                                }
                            } else {
                                console.addMessage("Warning: There is no language  with code '"
                                        + nodeLang
                                        + "' in layer '"
                                        + schema.getLayer().getURI()
                                        + "', language is presented is documentation for \""
                                        + new XmlTreeNode((Element) item.getDomNode().getParentNode()).getXPath() + "\"", false);
                            }
                        } catch (NoConstItemWithSuchValueError e) {
                            console.addMessage("Error: Language with code '" + nodeLang + "' in element \""
                                    + new XmlTreeNode((Element) item.getDomNode().getParentNode()).getXPath() + "\" doesn't exists!", true);
                        }
                    }
                }
            }
            if (!(langMap.isEmpty())) {
                finalXPathMap.put(new XmlTreeNode((Element) item.getDomNode().getParentNode()).getXPath(), langMap);
            }
        }
        return finalXPathMap;
    }

    private void importDocumentation(Map<String, Map<EIsoLanguage, String>> finalXPathMap, String version, boolean replace) {
        Iterator<String> itr = finalXPathMap.keySet().iterator();
        while (itr.hasNext()) {
            String xPathStr = itr.next();
            Map<EIsoLanguage, String> langMap = finalXPathMap.get(xPathStr);
            Iterator<EIsoLanguage> langItr = langMap.keySet().iterator();

            if (schema.getXmlItemDocEntry(xPathStr) == null || replace) {
                IMultilingualStringDef newStr = schema.findLocalizingBundle().createString(ELocalizedStringKind.DESCRIPTION);
                while (langItr.hasNext()) {
                    EIsoLanguage langLang = langItr.next();
                    newStr.setValue(langLang, langMap.get(langLang));
                }
                schema.findLocalizingBundle().getStrings().getLocal().add((AdsMultilingualStringDef) newStr);
                schema.addNodeDescription(xPathStr, new AdsXmlSchemeDef.XmlItemDocEntry(newStr.getId(), version));
            }
        }
    }

    private boolean isNodeListContainsNode(NodeList nodeList, String nodeName) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE
                    && (nodeList.item(i).getLocalName() != null
                    && (!nodeList.item(i).getLocalName().equals(nodeName)))) {
                return true;
            }
        }
        return false;
    }

    private void cleanSource() {
        for (org.apache.xmlbeans.XmlObject item : xPathSelect("xs='http://www.w3.org/2001/XMLSchema'",
                "$this//xs:annotation")) {
            NodeList nodeList = item.getDomNode().getChildNodes();
            if (isNodeListContainsNode(nodeList, "documentation")) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getLocalName() != null && nodeList.item(i).getLocalName().equals("documentation")) {
                        item.getDomNode().removeChild(nodeList.item(i));
                        schema.setEditState(EEditState.MODIFIED);
                    }
                }
            } else {
                item.getDomNode().getParentNode().removeChild(item.getDomNode());
                schema.setEditState(EEditState.MODIFIED);
            }
        }
    }

    private boolean showEmptyDocWarning() {
        final String message = "Description is empty, leave it empty or mark node as undocumented?";
        final String leave_empty_text = "Leave Empty";
        final String mark_undocumented_text = "Mark Undocumented";
        final String cancel_text = "Cancel";

        List<String> buttonsList = Arrays.asList(new String[]{leave_empty_text, mark_undocumented_text, cancel_text});

        String result = DialogUtils.showCustomMessageBox(message, buttonsList, DialogDescriptor.WARNING_MESSAGE);
        if (result == null || result.isEmpty()) {
            return false;
        }

        switch (result) {
            case leave_empty_text:
                return true;
            case mark_undocumented_text:
                schema.removeNodeDescription(selectedNodePath.getText());
                documentationChanged();

                return true;
            case cancel_text:
                return false;
        }

        return true;
    }

    private IChangeLogActualizationListener createChangeLogActualizationListener() {
        return new IChangeLogActualizationListener() {

            @Override
            public boolean onSubmitChanges(AdsXmlSchemeDef.ChangeLogEntry entry) {
                ModalDisplayer displayer = new ModalDisplayer(createChangeLogEntryEditor(entry), "XML Schema was changed. Do you want to add the following entry to the change log?");
                return displayer.showModal();
            }
        };
    }
}
