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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Caret;
import javax.swing.undo.UndoManager;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.EditorUI;
import org.netbeans.editor.Utilities;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.check.ProblemAnnotationFactory;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExecAppByFileName;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.TextComponentUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AdsXmlEditor extends JPanel {

    private JEditorPaneEx jEditorPane1;
    private static final String tempFile = "______temp___.xsd";
    boolean mustUpdate = true;

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
    private AccessEditPanel accessEditPanel;
    //private boolean isCheckFocusLoose = true;

    class JEditorPaneEx extends JEditorPane {

        JEditorPaneEx() {
            super();

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
                continue;
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

    private void initComponents2() {
        jEditorPane1 = new JEditorPaneEx();
        jEditorPane1.setContentType("text/xml");
        EditorUI eui = Utilities.getEditorUI(jEditorPane1);
        if (eui != null) {
            JToolBar toolBar = eui.getToolBarComponent();
            toolBar.setBorderPainted(true);
            toolBar.setFloatable(false);

            edit = new JButton();
            toolBar.add(edit, 0);
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
            toolBar.add(load, 1);
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

            unLoad = new JButton();
            toolBar.add(unLoad, 2);
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

            format = new JButton();
            toolBar.add(format, 2);
            format.setIcon(RadixWareIcons.EDIT.FIX.getIcon());
            format.setToolTipText("Forman Text");
            format.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    update(true);
                    schema.setXmlText(jEditorPane1.getText());
                }
            });

            unLoadWithImport = new JButton();
            toolBar.add(unLoadWithImport, 3);
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

            toolBar.addSeparator();
            Component extComponent = eui.getExtComponent();

            jPanel4.setLayout(
                    new BorderLayout());
            JPanel panel = new JPanel();

            jPanel4.add(toolBar, BorderLayout.NORTH);
            accessEditPanel = new AccessEditPanel();
            JLabel lAcceess = new JLabel();

            lAcceess.setText(
                    "Access: ");

            JPanel panel2 = new JPanel();

            panel2.setLayout(
                    new BorderLayout());

            panel2.add(extComponent, BorderLayout.CENTER);

            panel2.add(panel, BorderLayout.NORTH);

            jEditorPane1.addFocusListener(
                    new java.awt.event.FocusAdapter() {
                        @Override
                        public void focusLost(java.awt.event.FocusEvent evt) {
//                    SearchBar sb = org.netbeans.modules.editor.impl.SearchBar.getInstance();
//                    sb.setVisible(false);
                    /*
                             if (isCheckFocusLoose && !schema.setXmlText(jEditorPane1.getText()))
                             {

                             if (!DialogUtils.messageConfirmation("This scheme is invalid. Finish editing and loose changes?"))
                             // if (!DialogUtils.messageConfirmation(String.valueOf(jEditorPane1.hasFocus())))
                             //evt.paramString()
                             {
                             mustUpdate = false;
                             isCheckFocusLoose = false;
                             jEditorPane1.requestFocus();
                             }
                             else
                             {
                             mustUpdate = true;
                             update();
                             }
                             }
                             */
                        }
                    });

            JPanel panel3 = new JPanel();

            panel3.setLayout(
                    new BorderLayout());
            panel3.add(lblMess, BorderLayout.NORTH);

            panel3.add(panel2, BorderLayout.CENTER);

            jPanel4.add(panel3, BorderLayout.CENTER);
//            panel2.add(lblMess, BorderLayout.NORTH);
            Font font = lblMess.getFont();
            Font f = new Font(font.getFontName(), Font.BOLD, font.getSize());

            lblMess.setFont(f);
            javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(panel);

            panel.setLayout(jPanel19Layout);

            jPanel19Layout.setHorizontalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addContainerGap().addComponent(lAcceess).addComponent(accessEditPanel)));

            jPanel19Layout.setVerticalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel19Layout.createSequentialGroup().addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lAcceess).addComponent(accessEditPanel))));
        }

        jEditorPane1.getDocument().addDocumentListener(documentListener);
        TextComponentUtils.installUndoRedoAction(jEditorPane1);
    }
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
        update(false);
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
            if (radixObject instanceof AdsXmlSchemeDef) {
                return true;
            }
            return false;
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
                ns = ns = ns.substring(i + 1);
                break;
            }
        }
        if (ns.isEmpty()) {
            ns = schema.getName().toLowerCase() + ".xsd";
        }
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

                                if (val != null && val.length() == 29) {
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
}
