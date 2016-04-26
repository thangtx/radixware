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

package org.radixware.kernel.server.widgets;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;
import javax.swing.JTextPane;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlException;


public class XMLViewer {

    private static final class XMLTree extends JTree {

        private static final long serialVersionUID = -6083939876419546616L;

        private static final class XMLTreeRenderer extends DefaultTreeCellRenderer {

            private static final long serialVersionUID = 3698256689962888422L;
            private final JTextPane textPane;
            private final StyledDocument doc;
            private final Style tagStyle;
            private final Style attrStyle;
            private final Style textStyle;

            public XMLTreeRenderer() {
                super();
                setLeafIcon(null);
                setOpenIcon(null);
                setClosedIcon(null);

                textPane = new JTextPane();
                textPane.setMargin(new Insets(0, 0, 0, 0));
                doc = (StyledDocument) textPane.getDocument();

                tagStyle = doc.addStyle("tagStyle", null);
                StyleConstants.setFontFamily(tagStyle, "Tahoma");
                StyleConstants.setFontSize(tagStyle, 11);
                StyleConstants.setBold(tagStyle, true);
                StyleConstants.setForeground(tagStyle, Color.RED);

                attrStyle = doc.addStyle("attrStyle", null);
                StyleConstants.setFontFamily(attrStyle, "Tahoma");
                StyleConstants.setFontSize(attrStyle, 11);
                StyleConstants.setBold(attrStyle, true);
                StyleConstants.setForeground(attrStyle, Color.BLUE);

                textStyle = doc.addStyle("textStyle", null);
                StyleConstants.setFontFamily(textStyle, "Tahoma");
                StyleConstants.setFontSize(textStyle, 11);
                StyleConstants.setForeground(textStyle, Color.BLACK);
            }

            private String[] mySpaceSplit(String str) {
                ArrayList<String> ret = new ArrayList<String>();
                boolean flag = false;
                int prev = -1;
                while (true) {
                    int next = prev + 1;
                    while (next < str.length()) {
                        if (str.charAt(next) == ' ' && !flag) {
                            break;
                        }
                        if (str.charAt(next) == '"') {
                            flag = !flag;
                        }
                        ++next;
                    }
                    ret.add(str.substring(prev + 1, next));
                    if (next == str.length()) {
                        break;
                    }
                    prev = next;
                }
                String[] o = new String[1];
                return ret.toArray(o);
            }

            private String[] myAssignSplit(String str) {
                int idx = str.indexOf('=');
                String[] ret = new String[2];
                ret[0] = str.substring(0, idx);
                ret[1] = str.substring(idx + 1);
                return ret;
            }

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                    boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                textPane.setText("");
                if (sel) {
                    textPane.setBackground(textPane.getSelectionColor());
                } else {
                    textPane.setBackground(Color.WHITE);
                }
                if (value != null && value.toString() != null && value.toString().matches("<(.)*>")) {
                    String[] items = mySpaceSplit(value.toString());
                    try {
                        doc.insertString(0, "<", textStyle);
                        if (items.length > 1) {
                            doc.insertString(doc.getLength(), items[0].substring(1), tagStyle);
                            for (int i = 1; i < items.length - 1; i++) {
                                String[] attr = myAssignSplit(items[i]);
                                doc.insertString(doc.getLength(), " " + attr[0], attrStyle);
                                doc.insertString(doc.getLength(), "=" + attr[1], textStyle);
                            }
                            String[] attr = myAssignSplit(items[items.length - 1]);
                            doc.insertString(doc.getLength(), " " + attr[0], attrStyle);
                            doc.insertString(doc.getLength(), "=" + attr[1].substring(0, attr[1].length() - 1), textStyle);
                        } else {
                            doc.insertString(doc.getLength(), items[0].substring(1, items[0].length() - 1), tagStyle);
                        }
                        doc.insertString(doc.getLength(), ">", textStyle);
                    } catch (BadLocationException ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                } else if (value != null && value.toString() != null) {
                    try {
                        doc.insertString(0, value.toString(), textStyle);
                    } catch (BadLocationException ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                } else {
                    return this;
                }
                return textPane;
            }
        }
        private final DefaultMutableTreeNode root;
        private final JScrollPane scrollPane;

        public XMLTree() {
            super();
            scrollPane = new JScrollPane(this);
            root = (DefaultMutableTreeNode) getModel().getRoot();
            setCellRenderer(new XMLTreeRenderer());
            putClientProperty("JTree.lineStyle", "None");
            setName("SelectOnFinding");
            addPropertyChangeListener(getName(), new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    int row = ((Integer) e.getNewValue()).intValue();
                    XMLTree.this.setSelectionRow(row);
                    int max = XMLTree.this.getHeight();
                    int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
                    int value = Math.round((float) row * (max - extent) / (XMLTree.this.getRowCount() - 1 > 0 ? XMLTree.this.getRowCount() - 1 : 1));
                    scrollPane.getVerticalScrollBar().getModel().setRangeProperties(value, extent, 0, max, false);
                }
            });
        }

        private boolean isEmpty(Node xmlNode) {
            if (xmlNode.getLocalName() != null) {
                return false;
            }
            String str = xmlNode.getNodeValue();
            return str == null || str.trim().isEmpty();
        }

        private void next(Node xmlNode, DefaultMutableTreeNode treeNode) {
            String tagName;
            String prefix = xmlNode.getPrefix();
            if (prefix != null && !prefix.equals("")) {
                tagName = prefix + ":" + xmlNode.getLocalName();
            } else {
                tagName = xmlNode.getLocalName();
            }
            if (tagName != null) {
                final StringBuilder fullName = new StringBuilder("<" + tagName);
                if (xmlNode.hasAttributes()) {
                    NamedNodeMap attr = xmlNode.getAttributes();
                    for (int i = 0; i < attr.getLength(); i++) {
                        fullName.append(" " + attr.item(i).getNodeName() + "=\"" + attr.item(i).getNodeValue() + "\"");
                    }
                }
                fullName.append('>');
                treeNode.setUserObject(fullName.toString());
            } else {
                treeNode.setUserObject(xmlNode.getNodeValue().trim());
            }
            for (int i = 0; i < xmlNode.getChildNodes().getLength(); i++) {
                Node node = xmlNode.getChildNodes().item(i);
                if (!isEmpty(node)) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode();
                    treeNode.add(child);
                    next(node, child);
                }
            }
        }

        public void buildTree(String xmlDocument) {
            try {
                clearTree(root);
//                this.updateUI();
                String header = "";
                if (xmlDocument.startsWith("<?")) {
                    int headerEnd = xmlDocument.indexOf("?>");
                    if (headerEnd > 0) {
                        header = xmlDocument.substring(0, headerEnd + 2) + "\n";
                        xmlDocument = xmlDocument.substring(headerEnd + 2);
                    }
                }
                xmlDocument = header  + "<root>" + xmlDocument.trim() + "</root>";
                Node node = XmlObject.Factory.parse(xmlDocument).getDomNode().getFirstChild().getFirstChild();
                next(node, root);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        XMLTree.this.updateUI();
                    }
                });
            } catch (XmlException e) {
                JOptionPane.showMessageDialog(scrollPane, e.getMessage());
            }
        }

        private void clearTree(DefaultMutableTreeNode node) {
            for (int i = 0; i < node.getChildCount(); i++) {
                clearTree((DefaultMutableTreeNode) node.getChildAt(i));
            }
            node.removeAllChildren();
        }

        public void expandAll(DefaultMutableTreeNode node) {
            for (int i = 0; i < node.getChildCount(); i++) {
                expandAll((DefaultMutableTreeNode) node.getChildAt(i));
            }
            expandPath(new TreePath(node.getPath()));
        }

        public void collapseAll(DefaultMutableTreeNode node) {
            for (int i = 0; i < node.getChildCount(); i++) {
                collapseAll((DefaultMutableTreeNode) node.getChildAt(i));
            }
            collapsePath(new TreePath(node.getPath()));
        }

        public DefaultMutableTreeNode getRoot() {
            return root;
        }

        public JScrollPane getScrollPane() {
            return scrollPane;
        }
    }
    private final XMLTree tree;
    private Object parent;
    private final JOptionPane pane;
    private JDialog dialog = null;
    private final SearchXMLDialog searchXMLDialog;
    private Dimension prevSize;
    private Point loc;
    private boolean maximized = false;

    public XMLViewer(Object parent) {
        this.parent = parent;
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        tree = new XMLTree();
        panel.add(tree.getScrollPane(), BorderLayout.CENTER);
        panel.add(createButtonBar(), BorderLayout.SOUTH);
        searchXMLDialog = new SearchXMLDialog(parent, tree);

        String[] name = {Messages.BTN_CLOSE};
        pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION, null, name, null);
        pane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                    return;
                }
                String t = e.getNewValue().toString();
                if (t.equals(Messages.BTN_CLOSE)) {
                    pane.setValue(null);
                    dialog.setVisible(false);
                }
            }
        });
    }

    private JPanel createButtonBar() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        JButton expandButton = new JButton(Messages.BTN_EXPAND);
        expandButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.expandAll(tree.getRoot());
            }
        });
        buttonPanel.add(expandButton);

        JButton collapseButton = new JButton(Messages.BTN_COLLAPSE);
        collapseButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.collapseAll(tree.getRoot());
            }
        });
        buttonPanel.add(collapseButton);

        JButton findButton = new JButton(Messages.BTN_FIND);
        findButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchXMLDialog.show();
            }
        });
        buttonPanel.add(findButton);

        final JButton maximizeButton = new JButton(Messages.BTN_MAXIMIZE);
        maximizeButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!maximized) {
                    prevSize = dialog.getSize();
                    loc = dialog.getLocation();
                    dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                    maximizeButton.setText(Messages.BTN_MINIMIZE);
                    maximized = true;
                } else {
                    dialog.setSize(prevSize);
                    dialog.setLocation(loc);
                    maximizeButton.setText(Messages.BTN_MAXIMIZE);
                    maximized = false;
                }
            }
        });
        buttonPanel.add(maximizeButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    public void show(String xmlDocument) {
        tree.buildTree(xmlDocument);
        if (dialog == null) {
            while (parent != null && !(parent instanceof Frame)) {
                parent = ((Component) parent).getParent();
            }
            dialog = new JDialog((Frame) parent, Messages.TITLE_XML_TREE_VIEWER, true);
            dialog.setContentPane(pane);
            //dialog.pack();
            dialog.setMinimumSize(new Dimension(480, 380));
//			dialog.setSize(new Dimension(400, 400));
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(true);
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dialog.setVisible(false);
                }
            };
            KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });

    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            BTN_FIND = bundle.getString("BTN_FIND");
            BTN_CLOSE = bundle.getString("BTN_CLOSE");
            TITLE_XML_TREE_VIEWER = bundle.getString("TITLE_XML_TREE_VIEWER");
            BTN_EXPAND = bundle.getString("BTN_EXPAND");
            BTN_COLLAPSE = bundle.getString("BTN_COLLAPSE");
            BTN_MAXIMIZE = bundle.getString("BTN_MAXIMIZE");
            BTN_MINIMIZE = bundle.getString("BTN_MINIMIZE");
        }
        static final String BTN_FIND;
        static final String BTN_MAXIMIZE;
        static final String BTN_MINIMIZE;
        static final String BTN_CLOSE;
        static final String TITLE_XML_TREE_VIEWER;
        static final String BTN_EXPAND;
        static final String BTN_COLLAPSE;
    }
}
