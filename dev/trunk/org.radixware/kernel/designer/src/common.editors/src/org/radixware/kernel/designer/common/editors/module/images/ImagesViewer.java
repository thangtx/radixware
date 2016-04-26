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

package org.radixware.kernel.designer.common.editors.module.images;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesCfg;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsages;


public class ImagesViewer extends JTextPane implements ComponentListener {

    private class ImagesTree extends JTree {

        private class Renderer extends DefaultTreeCellRenderer {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
//                if (node.getUserObject() instanceof ImagesList) {
//                    return (ImagesList)node.getUserObject();
//                }
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, leaf, row, hasFocus);
                label.setIcon(null);
                return label;
            }
        }

//        private final DefaultMutableTreeNode node;
        public ImagesTree(String name) {
            super();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(name);
            root.add(new DefaultMutableTreeNode(""));
//            root.add(new DefaultMutableTreeNode(name));
//            first.add(node = new DefaultMutableTreeNode(name));
            this.setModel(new DefaultTreeModel(root));
            this.setShowsRootHandles(true);
            this.setCellRenderer(new Renderer());
            this.collapseRow(0);

            this.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    JTree tree = (JTree) e.getSource();
                    TreePath path = tree.getSelectionPath();
                    for (ImagesTree tr : treeForImagesList.values()) {
                        if (tr != tree) {
                            tr.clearSelection();
                        }
                    }
                    if (path != null && tree.getSelectionPath() == null) {
                        tree.setSelectionPath(path);
                    }
                }
            });

            super.addTreeExpansionListener(new TreeExpansionListener() {
                @Override
                public void treeExpanded(TreeExpansionEvent event) {
                    ImagesViewer.this.showImagesList();
                }

                @Override
                public void treeCollapsed(TreeExpansionEvent event) {
                    ImagesViewer.this.hideImagesList();
                }
            });
        }

        public boolean isExpanded() {
            return super.isExpanded(0);
        }

        public void expand() {
            this.expandRow(0);
        }

        public void startWait() {
//            node.setUserObject("Please Wait...");
        }

        public void stopWait() {
//            node.setUserObject("");
        }
    }
//    private static final int MAX_CACHE_SIZE = 5;
//    private static final LinkedList<ModuleImages> MODULE_IMAGES = new LinkedList<ModuleImages>();
//    private static final Map<ModuleImages, ImagesList> globalImagesListForModuleImages = new HashMap<ModuleImages, ImagesList>();
    private final Style compStyle;
    private final Style lineStyle;
    private final ArrayList<ImagesList> imagesLists = new ArrayList<ImagesList>();
    private final ArrayList<Integer> imagesListPositions = new ArrayList<Integer>();
    private final ArrayList<Boolean> imagesListExpanded = new ArrayList<Boolean>();
    private final Map<ImagesList, ImagesTree> treeForImagesList = new HashMap<ImagesList, ImagesTree>();
    private final Map<ModuleImages, ImagesList> imagesListForModuleImages =
            new HashMap<ModuleImages, ImagesList>(/*globalImagesListForModuleImages*/);
    private boolean showNames = false;
    private boolean originalSize = false;
    private String pattern = "";
    private boolean inName = false, inKeywords = false, inDescription = false, wholeWord = false;

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList cur = (JList) e.getSource();
            int idx = cur.getSelectedIndex();
            for (ImagesList list : imagesLists) {
                if (list != cur) {
                    list.clearSelection();
                }
            }
            if (idx != -1 && cur.getSelectedIndex() == -1) {
                cur.setSelectedIndex(idx);
            }
        }
    };
    private final SelectionListener selectionListener = new SelectionListener();

    private class EnableChangeListener implements PropertyChangeListener {

        private final ModuleImagesEditor moduleImagesEditor;

        public EnableChangeListener(ModuleImagesEditor editor) {
            moduleImagesEditor = editor;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ImagesList.ENABLE_STATE)) {
                ImagesList list = (ImagesList) evt.getSource();
                boolean enabled = (Boolean) evt.getNewValue();
                moduleImagesEditor.fireEnableChanged(enabled);
//                if (enabled) {
//                    treeForImagesList.get(list).stopWait();
//                } else {
//                    treeForImagesList.get(list).startWait();
//                }
            }
        }
    }

    public ImagesViewer() {
        super();
        this.setEditable(false);
        this.setBackground(Color.WHITE);
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        compStyle = this.getStyledDocument().addStyle("comp", def);
        lineStyle = this.getStyledDocument().addStyle("line", def);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                registerCloseListener();
            }
        });
    }

    private void registerCloseListener() {
        ModuleImagesEditor.Mode mode = null;
        for (Container cont = this.getParent(); cont != null; cont = cont.getParent()) {
            if (cont instanceof ModuleImagesEditor) {
                mode = ((ModuleImagesEditor) cont).getMode();
                break;
            }
        }
        if (mode != ModuleImagesEditor.Mode.CONFIGURE_IMAGES) {
            for (Container cont = this.getParent(); cont != null; cont = cont.getParent()) {
                if (cont instanceof Dialog) {
                    ((Dialog) cont).addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {
                        }

                        @Override
                        public void windowClosing(WindowEvent e) {
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            interruptAllLoadThreads();
                        }

                        @Override
                        public void windowIconified(WindowEvent e) {
                        }

                        @Override
                        public void windowDeiconified(WindowEvent e) {
                        }

                        @Override
                        public void windowActivated(WindowEvent e) {
                        }

                        @Override
                        public void windowDeactivated(WindowEvent e) {
                        }
                    });
                    break;
                }
            }
        }
    }

    public void interruptAllLoadThreads() {
        for (ImagesList list : imagesLists) {
            list.interruptLoadThread();
        }
    }

//    public static void addToCache(ModuleImages moduleImages, ImagesList imagesList) {
//        if (globalImagesListForModuleImages.containsKey(moduleImages)) {
//            return;
//        }
//        MODULE_IMAGES.addFirst(moduleImages);
//        globalImagesListForModuleImages.put(moduleImages, imagesList);
//        if (MODULE_IMAGES.size() > MAX_CACHE_SIZE) {
//            globalImagesListForModuleImages.remove(MODULE_IMAGES.removeLast());
//        }
//    }
    private void hideImagesList() {
        for (int i = 0; i < imagesLists.size(); ++i) {
            ImagesList list = imagesLists.get(i);
            if (imagesListPositions.get(i).intValue() != -1
                    && treeForImagesList.get(list).isExpanded() != imagesListExpanded.get(i).booleanValue()) {
                imagesListExpanded.set(i, false);
                list.clearSelection();
                list.interruptLoadThread();
//                final int val = getScrollPane().getVerticalScrollBar().getValue();
                int moo = this.getStyledDocument().getLength();
                removeImagesList(imagesListPositions.get(i));
                moo = moo - this.getStyledDocument().getLength();
                for (int j = i + 1; j < imagesLists.size(); ++j) {
                    if (imagesListPositions.get(j).intValue() != -1) {
                        imagesListPositions.set(j, imagesListPositions.get(j) - moo);
                    }
                }
                this.setCaretPosition(imagesListPositions.get(i) - 1);
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        getScrollPane().getVerticalScrollBar().setValue(val);
//                    }
//                });
                break;
            }
        }
    }

    private void showImagesList() {
        for (int i = 0; i < imagesLists.size(); ++i) {
            ImagesList list = imagesLists.get(i);
            if (imagesListPositions.get(i).intValue() != -1
                    && treeForImagesList.get(list).isExpanded() != imagesListExpanded.get(i).booleanValue()) {
                imagesListExpanded.set(i, true);
//                final int val = getScrollPane().getVerticalScrollBar().getValue();
                int moo = this.getStyledDocument().getLength();
                insertImagesList(list, imagesListPositions.get(i));
                moo = this.getStyledDocument().getLength() - moo;
                if (pattern.isEmpty()) {
                    list.showAll();
                } else {
                    List<AdsImageDef> images = list.getImagesWithPattern(pattern, inName, inKeywords, inDescription, wholeWord);
                    list.showImages(images);
                }
                for (int j = i + 1; j < imagesLists.size(); ++j) {
                    if (imagesListPositions.get(j).intValue() != -1) {
                        imagesListPositions.set(j, imagesListPositions.get(j) + moo);
                    }
                }
                this.setCaretPosition(imagesListPositions.get(i));
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        getScrollPane().getVerticalScrollBar().setValue(val);
//                    }
//                });
                break;
            }
        }
    }

    private JScrollPane getScrollPane() {
        for (Container cont = this.getParent(); cont != null; cont = cont.getParent()) {
            if (cont instanceof JScrollPane) {
                return (JScrollPane) cont;
            }
        }
        return null;
    }

    private void removeImagesList(int pos) {
        try {
            this.getStyledDocument().remove(pos, 1);
            while (pos < this.getStyledDocument().getLength() && this.getStyledDocument().getText(pos, 1).equals("\n")) {
                this.getStyledDocument().remove(pos, 1);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

//    private void update() {
//        if (pattern.isEmpty()) {
//            showAll();
//        } else {
//            showImagesWithPattern(pattern, inName, inKeywords, inDescription, wholeWord);
//        }
//    }
    public void showAll() {
        pattern = "";
        clearDocument();
        for (int i = 0; i < imagesLists.size(); ++i) {
            ImagesList list = imagesLists.get(i);
            if (list.getModuleImages().size() > 0) {
                imagesListPositions.set(i, addImagesList(list));
                if (treeForImagesList.get(list).isExpanded()) {
                    imagesListExpanded.set(i, true);
                    list.showAll();
                } else {
                    imagesListExpanded.set(i, false);
                }
            } else {
                imagesListPositions.set(i, -1);
                imagesListExpanded.set(i, false);
            }
        }
    }

    private void clearDocument() {
        for (ImagesList list : imagesLists) {
            list.clearSelection();
        }
        try {
            this.getStyledDocument().remove(0, this.getStyledDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        clearDocument();
        imagesLists.clear();
        imagesListPositions.clear();
        imagesListExpanded.clear();
        treeForImagesList.clear();
    }

    public ImagesList getImagesListFor(ModuleImages moduleImages) {
        for (ImagesList list : imagesLists) {
            if (Utils.equals(list.getModuleImages(), moduleImages)) {
                return list;
            }
        }
        return null;
    }

    public void removeSelectedImages() {
//        for (ImagesList list : imagesLists)
//            list.removeSelectedImages();
        if (imagesLists.size() > 0) {
            imagesLists.get(0).removeSelectedImages();
        }
    }

    public void editSelectedImage() {
//        for (ImagesList list : imagesLists) {
//            if (list.getSelectedIndex() != -1) {
//                list.editImageAt(list.getSelectedIndex());
//                break;
//            }
//        }
        if (imagesLists.size() > 0) {
            ImagesList list = imagesLists.get(0);
            if (list.getSelectedIndex() != -1) {
                list.editImageAt(list.getSelectedIndex());
            }
        }
    }

    public void editPropsOfSelectedImage() {
//        for (ImagesList list : imagesLists) {
//            if (list.getSelectedIndex() != -1) {
//                list.editPropsOfImageAt(list.getSelectedIndex());
//                break;
//            }
//        }
        if (imagesLists.size() > 0) {
            ImagesList list = imagesLists.get(0);
            if (list.getSelectedIndex() != -1) {
                list.editPropsOfImageAt(list.getSelectedIndex());
            }
        }
    }

    public void replaceSelectedImage() {
        if (imagesLists.size() > 0) {
            ImagesList list = imagesLists.get(0);
            if (list.getSelectedIndex() != -1) {
                list.replaceImageAt(list.getSelectedIndex());
            }
        }
    }

    public void exportSelectedImage() {
        for (ImagesList list : imagesLists) {
            if (list.getSelectedIndex() != -1) {
                list.exportImageAt(list.getSelectedIndex());
                break;
            }
        }
    }

    public void findUsagesOfSelectedImage() {
        final Collection<AdsImageDef> selected = getSelectedAdsImages();
        if (selected.isEmpty()) {
            return;
        }
        final AdsImageDef image = selected.iterator().next();
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                final FindUsagesCfg cfg = new FindUsagesCfg(image);
                cfg.setSearchType(FindUsagesCfg.ESearchType.FIND_USAGES);
                FindUsages.search(cfg);
            }
        });

    }

//    public void copySelectedImage() {
////        for (ImagesList list : imagesLists) {
////            if (list.getSelectedIndex() != -1) {
////                list.copyImageAt(list.getSelectedIndex());
////                break;
////            }
////        }
//        if (imagesLists.size() > 0) {
//            ImagesList list = imagesLists.get(0);
//            if (list.getSelectedIndex() != -1) {
//                list.copyImageAt(list.getSelectedIndex());
//            }
//        }
//    }
    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
        for (ImagesList list : imagesLists) {
            list.setShowNames(showNames);
        }
    }

    public void setOriginalSize(boolean originalSize) {
        this.originalSize = originalSize;
        for (ImagesList list : imagesLists) {
            list.setOriginalSize(originalSize);
        }
    }

    public void setSelectionMode(int selectionMode) {
        for (ImagesList list : imagesLists) {
            list.setSelectionMode(selectionMode);
            for (ListSelectionListener listener : list.getListSelectionListeners()) {
                if (listener instanceof SelectionListener) {
                    list.removeListSelectionListener(listener);
                    break;
                }
            }
            if (selectionMode == ListSelectionModel.SINGLE_SELECTION) {
                list.addListSelectionListener(selectionListener);
            }
        }
    }

    public void showImagesWithPattern(String pattern, boolean inName, boolean inKeywords,
            boolean inDescription, boolean wholeWord) {
        pattern = pattern.trim().toLowerCase();
        this.pattern = pattern;
        this.inName = inName;
        this.inKeywords = inKeywords;
        this.inDescription = inDescription;
        this.wholeWord = wholeWord;
        clearDocument();
        for (int i = 0; i < imagesLists.size(); ++i) {
            ImagesList list = imagesLists.get(i);
            List<AdsImageDef> images = list.getImagesWithPattern(pattern, inName, inKeywords, inDescription, wholeWord);
            if (images.size() > 0/* || i == 0*/) {
                imagesListPositions.set(i, addImagesList(list));
                if (treeForImagesList.get(list).isExpanded()) {
                    imagesListExpanded.set(i, true);
                    list.showImages(images);
                } else {
                    imagesListExpanded.set(i, false);
                }
            } else {
                imagesListPositions.set(i, -1);
                imagesListExpanded.set(i, false);
            }
        }
    }

    public Id[] getSelectedImagesIds() {
        ArrayList<Id> ret = new ArrayList<Id>();
        for (ImagesList list : imagesLists) {
            Id[] ids = list.getSelectedImagesIds();
            for (Id id : ids) {
                ret.add(id);
            }
        }
        return ret.toArray(new Id[ret.size()]);
    }

    public List<AdsImageDef> getSelectedAdsImages() {
        List<AdsImageDef> ret = new ArrayList<AdsImageDef>();
        for (ImagesList list : imagesLists) {
            ret.addAll(list.getSelectedAdsImages());
        }
        return ret;
    }

    public void addModuleImages(ModuleImages moduleImages, String name, ModuleImagesEditor listener) {
        if (getImagesListFor(moduleImages) != null) {
            return;
        }
        ImagesList imagesList;
        if (imagesListForModuleImages.containsKey(moduleImages)) {
            imagesList = imagesListForModuleImages.get(moduleImages);
        } else {
            boolean first = imagesLists.size() == 0;
            imagesList = new ImagesList(moduleImages, first/* && !moduleImages.isReadOnly()*/);
            if (first) {
                this.setComponentPopupMenu(imagesList.getImagePopupMenu());
            }
//            imagesList.addListSelectionListener(listener);
            imagesListForModuleImages.put(moduleImages, imagesList);
        }
        for (ListSelectionListener lis : imagesList.getListSelectionListeners()) {
            if (lis instanceof ModuleImagesEditor) {
                imagesList.removeListSelectionListener(lis);
                break;
            }
        }
        for (PropertyChangeListener lis : imagesList.getPropertyChangeListeners(ImagesList.ENABLE_STATE)) {
            if (lis instanceof EnableChangeListener) {
                imagesList.removePropertyChangeListener(ImagesList.ENABLE_STATE, lis);
                break;
            }
        }
        imagesList.addListSelectionListener(listener);
        imagesList.addPropertyChangeListener(ImagesList.ENABLE_STATE, new EnableChangeListener(listener));
        imagesList.setShowNames(showNames);
        imagesList.setOriginalSize(originalSize);
        imagesLists.add(imagesList);
        imagesListPositions.add(-1);
        imagesListExpanded.add(false);
        ImagesTree imagesTree = new ImagesTree(name);
        if (imagesList.isEditable()) {
            imagesTree.expand();
        }
        treeForImagesList.put(imagesList, imagesTree);
    }

//    public void addAllModuleImages(List<ModuleImages> moduleImagesList, ListSelectionListener listener) {
//        for (ModuleImages moduleImages : moduleImagesList) {
//            addModuleImages(moduleImages, moduleImages.getModule().getQualifiedName());
//            ImagesList list = getImagesListFor(moduleImages);
//            if (list != null) {
//                list.addListSelectionListener(listener);
//            }
//        }
//    }
    private int addImagesList(ImagesList imagesList) {
//        addComponent(this.getStyledDocument(), createEditorPane(nameForImagesList.get(imagesList)));
        ImagesTree tree = treeForImagesList.get(imagesList);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tree, BorderLayout.CENTER);
        addComponent(this.getStyledDocument(), panel, this.getStyledDocument().getLength());
        addNewLine(this.getStyledDocument(), this.getStyledDocument().getLength());
        int ret = this.getStyledDocument().getLength();
        if (tree.isExpanded()) {
            ret = insertImagesList(imagesList, this.getStyledDocument().getLength());
//            addNewLine(this.getStyledDocument());
        }
//        addNewLine(this.getStyledDocument());
        return ret;
    }

    private int insertImagesList(ImagesList imagesList, int pos) {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBackground(Color.WHITE);
        pane.setBorder(new EmptyBorder(0, 20, 0, 0));
        pane.add(imagesList, BorderLayout.CENTER);
        int ret = addComponent(this.getStyledDocument(), pane, pos);
        addNewLine(this.getStyledDocument(), pos + 1);
//        addNewLine(this.getStyledDocument());
        return ret;
    }

    private int addComponent(StyledDocument doc, Component comp, int pos) {
        StyleConstants.setComponent(compStyle, comp);
        try {
//            int ret = doc.getLength();
            doc.insertString(pos, " ", compStyle);
            return pos;
        } catch (BadLocationException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void addNewLine(StyledDocument doc, int pos) {
        try {
            doc.insertString(pos, "\n", lineStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private JEditorPane createEditorPane(String name) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setBackground(Color.WHITE);
        editorPane.setContentType("text/html");
        editorPane.setText("<html><i><b>" + (name != null ? name : "<Unknown>")
                + "</b></i><br><hr width=\"99%\" align=\"left\"></html>");
        editorPane.setFocusable(false);
        return editorPane;
    }

//    public void update() {
//        for (ImagesList list : imagesLists) {
//            list.update();
//        }
//    }
    @Override
    public void componentResized(ComponentEvent e) {
        int w = (int) ((JViewport) getParent()).getBounds().getWidth();
        for (ImagesList list : imagesLists) {
            if (list.isVisible() && list.getParent() != null) {
                list.getParent().setMinimumSize(new Dimension(w, 10));
                list.getParent().setMaximumSize(new Dimension(w, 64000));
                list.updateUI();
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
