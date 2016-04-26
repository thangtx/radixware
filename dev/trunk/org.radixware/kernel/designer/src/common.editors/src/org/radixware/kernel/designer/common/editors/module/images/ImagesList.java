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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.PasteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ImagesList extends JList {

    private class ImagesListModel extends AbstractListModel {

        private final ArrayList<AdsImageDef> list = new ArrayList<AdsImageDef>();
        private final Map<AdsImageDef, RadixIcon> rdxIconForAdsImage = new HashMap<AdsImageDef, RadixIcon>();
        private final Map<AdsImageDef, Long> lastModifiedTimeForAdsImage = new HashMap<AdsImageDef, Long>();

        private volatile int allImagesCount;
        private volatile int loadedImagesCount;

        public ImagesListModel() {
        }

        @Override
        public int getSize() {
            if (lock.tryLock()) {
                try {
                    return list.size();
                } finally {
                    lock.unlock();
                }
            }
            return 1;
        }

        @Override
        public Object getElementAt(int index) {
            if (lock.tryLock()) {
                try {
                    return index < list.size() ? list.get(index) : null;
                } finally {
                    lock.unlock();
                }
            }
            return null;
        }

        public int getLoadPercent() {
            if (allImagesCount != 0)
                return (int)Math.round(((double)loadedImagesCount / (double)allImagesCount) * 100);
            return 0;
        }

        public void assignImages(List<AdsImageDef> imagesList) {
            lock.lock();
            allImagesCount = imagesList.size();
            loadedImagesCount = 0;
            setFixedCellHeight(32);
            setFixedCellWidth(256);
            Timer timer = new Timer(1000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                    if (handle != null)
                        handle.progress(getLoadPercent());
                }
            });
            timer.start();
            try {
                RadixObjectsUtils.sortByName(imagesList);
//            synchronized (list) {
//                final int cnt = getSize();
                list.clear();
    //            rdxIconForAdsImage.clear();
    //            lastModifiedTimeForAdsImage.clear();
//                if (cnt > 0) {
//                    ImagesList.this.repaint();
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            fireIntervalRemoved(ImagesListModel.this, 0, cnt - 1);
//                        }
//                    });
//                }
                list.addAll(imagesList);
//                M1: for (int i = 0; i < 100000; ++i) {
                    for (AdsImageDef imageDef : list) {
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
//                        Thread.yield();
                        File file = imageDef.getImageFile();
                        if (!rdxIconForAdsImage.containsKey(imageDef) || !lastModifiedTimeForAdsImage.containsKey(imageDef) ||
                                file == null || file.lastModified() != lastModifiedTimeForAdsImage.get(imageDef).longValue()) {
                            preLoadImage(imageDef);
                        }
                        ++loadedImagesCount;
                    }
//                }
//                if (getSize() > 0) {
//                    ImagesList.this.repaint();
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            fireContentsChanged(ImagesListModel.this, 0, getSize() - 1);
//                        }
//                    });
//                }
//            }
            } finally {
                lock.unlock();
                timer.stop();
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            updateDimension();
//            SwingUtilities.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
                    if (getSize() > 0) {
//                        ImagesList.this.repaint();
//                        SwingUtilities.invokeLater(new Runnable() {
//
//                            @Override
//                            public void run() {
                                fireContentsChanged(ImagesListModel.this, 0, getSize() - 1);
//                            }
//                        });
                    }
//                    ImagesList.this.revalidate();
//                    ImagesList.this.repaint();
//                }
//            });
        }

//        public void addImage(AdsImageDef image) {
//            list.add(image);
//            preLoadImage(image);
//            int sz = getSize();
//            fireIntervalAdded(this, getSize() - 1, getSize() - 1);
//        }
//        public void removeImage(AdsImageDef image) {
//            synchronized (list) {
//                int idx = list.indexOf(image);
//                list.remove(image);
//                rdxIconForAdsImage.remove(image);
//                lastModifiedTimeForAdsImage.remove(image);
//                if (idx != -1) {
//                    fireIntervalRemoved(this, idx, idx);
//                }
//            }
//        }

        public void editImageAt(final int index) {
            if (!Desktop.isDesktopSupported()) {
                DialogUtils.messageError("Could not launch external editor");
//                ErrorManager.getDefault().notify(new RadixError("Desktop is not supported by the system."));
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN) && !desktop.isSupported(Desktop.Action.EDIT)) {
                DialogUtils.messageError("Could not launch external editor");
//                ErrorManager.getDefault().notify(new RadixError("Open & edit file functions is not supported by the desktop."));
                return;
            }
            final AdsImageDef adsImage = (AdsImageDef) getElementAt(index);
            if (adsImage == null) {
                return;
            }
            final File imageFile = adsImage.getImageFile();
            if (imageFile == null) {
                ErrorManager.getDefault().notify(new RadixError("Could not retrieve image file from AdsImageDef instance."));
                return;
            }
            try {

                if (desktop.isSupported(Desktop.Action.EDIT)) {
                    desktop.edit(imageFile);
                } else {
                    desktop.open(imageFile);
                }

//                String ext = "."+adsImage.getIcon().getType().getValue();
//
////                File file = new File(imageFile.getParentFile(), adsImage.getName() + ext);
//                File file = File.createTempFile("img", ext);
////                if (!file.createNewFile()) {
////                    if (ext != ".img")
////                        file = new File(imageFile.getParentFile(), FileUtils.getFileNameWithoutExt(imageFile) + ext);
////                    else
////                        file = new File(imageFile.getParentFile(), imageFile.getName() + ext);
////                } else {
////                    file.delete();
////                }
////                if (file.exists())
////                    file.delete();
//                FileUtils.copyFile(imageFile, file);
////                if (imageFile.renameTo(file)) {
//                FileMonitor.register(file, new FileMonitor.FileListener() {
//
//                    @Override
//                    public void fileChanged(File file) {
//                        try {
//                            imageFile.delete();
//                            FileUtils.copyFile(file, imageFile);
//                            //                        if (file.renameTo(imageFile)) {
//                            SwingUtilities.invokeLater(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    adsImage.getIcon().reload();
//                                    fireContentsChanged(model, index, index);
//                                }
//                            });
//                        } catch (IOException e) {
//                            ErrorManager.getDefault().notify(e);
//                        }
////                        FileMonitor.unregister(file);
////                        }
//                    }
//                });
//                if (desktop.isSupported(Desktop.Action.EDIT)) {
//                    desktop.edit(file);
//                } else {
//                    desktop.open(file);
//                }
////                }
            } catch (IOException e) {
                DialogUtils.messageError("Could not launch external editor");
//                ErrorManager.getDefault().notify(e);
            }
        }

        private void preLoadImage(AdsImageDef image) {
            try {
                RadixIcon rdxIcon = image.getIcon();
                rdxIconForAdsImage.put(image, rdxIcon);
                rdxIcon.getIcon();
                rdxIcon.getOriginalImage();
                File file = image.getImageFile();
                if (file != null) {
                    lastModifiedTimeForAdsImage.put(image, file.lastModified());
                } else {
                    lastModifiedTimeForAdsImage.put(image, 0L);
                }
//                image.getIcon().getIcon();
//                image.getIcon().getOriginalImage();
//                image.getIcon().getType();
            } catch (RadixError e) {
                ErrorManager.getDefault().notify(e);
//                e.printStackTrace();
            }
        }

        public void update() {
            for (AdsImageDef imageDef : list) {
                File file = imageDef.getImageFile();
                if (file == null || file.lastModified() != lastModifiedTimeForAdsImage.get(imageDef).longValue()) {
                    preLoadImage(imageDef);
                    int idx = getIndexOf(imageDef);
                    fireContentsChanged(this, idx, idx);
                }
            }
        }

        public void updateAll() {
            if (getSize() > 0) {
                fireContentsChanged(this, 0, getSize() - 1);
            }
        }

        private int getIndexOf(AdsImageDef imageDef) {
            return list.indexOf(imageDef);
        }

        public RadixIcon getRadixIconForAdsImage(AdsImageDef imageDef) {
            return rdxIconForAdsImage.get(imageDef);
        }
//        public void updateImage(AdsImageDef imageDef) {
//            int idx = getIndexOf(imageDef);
//            if (idx != -1) {
//                imageDef.getIcon().reload();
//                fireContentsChanged(this, idx, idx);
//            }
//        }
    }

    private class ImagesListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                JLabel label = new JLabel("Please wait. Loading... " + model.getLoadPercent() + "%");
                label.setForeground(Color.RED);
                label.setHorizontalTextPosition(JLabel.LEFT);
                label.setVerticalTextPosition(JLabel.TOP);
                if (isSelected) {
                    label.setBackground(pattern.getBackground());
                } else {
                    label.setBackground(Color.WHITE);
                }
                return label;
            }
            AdsImageDef adsImage = (AdsImageDef) value;
            RadixIcon rdxIcon = model.getRadixIconForAdsImage(adsImage);
            Icon icon = null;
            if (rdxIcon != null) {
                if (originalSize) {
                    Image img = rdxIcon.getOriginalImage();
                    if (img != null) {
                        icon = new ImageIcon(img);
                    }
                } else {
                    icon = rdxIcon.getIcon(32, 32);
                }
            }
            JLabel label;
            if (icon == null) {
                label = new JLabel(adsImage.getName());
            } else {
                label = new JLabel(icon);
                if (showNames) {
                    label.setText(adsImage.getName());
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                }
            }
            label.setOpaque(true);
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            if (isSelected) {
                label.setBackground(pattern.getBackground());
            } else {
                label.setBackground(Color.WHITE);
            }
            return label;
        }
    }

    public class ImagePopupMenu extends JPopupMenu implements ListSelectionListener {

        private final Action copyAction = new AbstractAction("Copy", SystemAction.get(CopyAction.class).getIcon()) {

            @Override
            public void actionPerformed(ActionEvent e) {
                SystemAction.get(CopyAction.class).actionPerformed(e);
            }
        };
        private final Action cutAction = new AbstractAction("Cut", SystemAction.get(CutAction.class).getIcon()) {

            @Override
            public void actionPerformed(ActionEvent e) {
                SystemAction.get(CutAction.class).actionPerformed(e);
            }
        };
        private final Action pasteAction = new AbstractAction("Paste", SystemAction.get(PasteAction.class).getIcon()) {

            @Override
            public void actionPerformed(ActionEvent e) {
                ImagesList.this.clearSelection();
                SystemAction.get(PasteAction.class).actionPerformed(e);
            }
        };
        private final Action deleteAction = new AbstractAction("Delete", SystemAction.get(DeleteAction.class).getIcon()) {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedImages();
            }
        };

        public ImagePopupMenu() {
            super();
            this.add(copyAction);
            this.add(cutAction);
            this.add(pasteAction);
            this.add(deleteAction);
            copyAction.setEnabled(false);
            cutAction.setEnabled(false);
            deleteAction.setEnabled(false);
            pasteAction.setEnabled(editable);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean enabled = ImagesList.this.getSelectedIndex() != -1;
            copyAction.setEnabled(enabled);
            cutAction.setEnabled(enabled && editable);
            deleteAction.setEnabled(enabled && editable);
        }

    }

    public static final String ENABLE_STATE = "ENABLE_STATE";
    private final ModuleImages moduleImages;
    private final ImagesListModel model;
    private List<AdsImageDef> allImages = null;
    private boolean showNames = false;
    private boolean originalSize = false;
    private final boolean editable;
    private Thread loadThread;
    private volatile ProgressHandle handle = null;
    private final Lock lock = new ReentrantLock();

    public ImagesList(ModuleImages moduleImages, boolean editable) {
        super();
        this.moduleImages = moduleImages;
        this.editable = editable;
        this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.setFixedCellHeight(48);
        this.setFixedCellWidth(48);
        this.setVisibleRowCount(-1);
        this.setModel(model = new ImagesListModel());
        this.setCellRenderer(new ImagesListRenderer());
//        if (editable) {
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                        ModuleImagesEditor editor = getModuleImagesEditor();
                        if (editor != null) {
                            ModuleImagesEditor.Mode mode = editor.getMode();
                            if (mode != ModuleImagesEditor.Mode.CONFIGURE_IMAGES) {
                                editor.apply();
                                return;
                            }
                        }
                        int idx = ImagesList.this.locationToIndex(e.getPoint());
                        if (ImagesList.this.editable && idx != -1) {
                            AdsImageDef adsImage = (AdsImageDef) model.getElementAt(idx);
                            if (adsImage == null) {
                                return;
                            }
//                            ImageConfigureDialog.getInstanceFor(adsImage).executeDialog();
                            ImageConfigureDialog.getInstanceFor(adsImage).executeDialog();
                            update();
                        }
                    }
                }
            });
            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ModuleImagesEditor editor = getModuleImagesEditor();
                    if (editor != null) {
                        ModuleImagesEditor.Mode mode = editor.getMode();
                        if (mode != ModuleImagesEditor.Mode.CONFIGURE_IMAGES) {
                            editor.apply();
                            return;
                        }
                    }
                    int idx = ImagesList.this.getSelectedIndex();
                    if (ImagesList.this.editable && idx != -1) {
                        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(idx);
                        if (adsImage == null) {
                            return;
                        }
//                        ImageConfigureDialog.getInstanceFor(adsImage).executeDialog();
                        ImageConfigureDialog.getInstanceFor(adsImage).executeDialog();
                        update();
                    }
                }
            };
            KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
            this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//        }
    }

    public void interruptLoadThread() {
        if (loadThread != null && loadThread.getState() != Thread.State.TERMINATED) {
            loadThread.interrupt();
        }
    }

    private ModuleImagesEditor getModuleImagesEditor() {
        for (Container cont = this.getParent(); cont != null; cont = cont.getParent()) {
            if (cont instanceof ModuleImagesEditor)
                return (ModuleImagesEditor)cont;
        }
        return null;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setupPopupMenu() {
        ImagePopupMenu popupMenu = new ImagePopupMenu();
        this.setComponentPopupMenu(popupMenu);
        this.addListSelectionListener(popupMenu);
    }

    public ImagePopupMenu getImagePopupMenu() {
        return new ImagePopupMenu();
    }

//    public void updateImage(AdsImageDef imageDef) {
//        model.updateImage(imageDef);
//    }
    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
        updateDimension();
        if (this.isVisible() && this.getParent() != null) {
            model.updateAll();
        }
    }

    public void setOriginalSize(boolean originalSize) {
        this.originalSize = originalSize;
        updateDimension();
        if (this.isVisible() && this.getParent() != null) {
            model.updateAll();
        }
    }

    private void updateDimension() {
        if (lock.tryLock()) {
            try {
                if (originalSize) {
                    if (showNames) {
                        this.setFixedCellHeight(48);
                        this.setFixedCellWidth(48);
                    } else {
                        this.setFixedCellHeight(32);
                        this.setFixedCellWidth(32);
                    }
                } else {
                    if (showNames) {
                        this.setFixedCellHeight(56);
                        this.setFixedCellWidth(56);
                    } else {
                        this.setFixedCellHeight(48);
                        this.setFixedCellWidth(48);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public void editImageAt(int index) {
        model.editImageAt(index);
    }

    private File getImageFile(final String ext) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(NbBundle.getBundle(ImagesList.class).getString("Save_Image"));
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String fname = f.getName();
                return f.isDirectory() || fname.endsWith(ext);
            }

            @Override
            public String getDescription() {
                return NbBundle.getBundle(ImagesList.class).getString("Image") + " (*" + ext + ")";
            }
        });
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.getName().endsWith(ext)) {
                return file;
            }
            File imgFile = new File(file.getPath() + ext);
            file.renameTo(imgFile);
            return imgFile;
        }
        return null;
    }

    public void exportImageAt(int index) {
        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(index);
        if (adsImage == null) {
            return;
        }
        File file = adsImage.getImageFile();
        if (file != null) {
            final String ext = "." + FileUtils.getFileExt(file);
            File dest = getImageFile(ext);
            if (dest != null) {
                try {
                    FileUtils.copyFile(file, dest);
                } catch (IOException e) {
                    ErrorManager.getDefault().notify(e);
                }
            }
        }
    }

//    public void findUsagesOfImageAt(int index) {
//        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(index);
//        List<RadixObject> list = new ArrayList<RadixObject>();
//        list.add(adsImage);
//        RadixObjectsUsagesSearcher.Factory.newFindUsagesSeacher().searchForUsages(list);
//    }
    public void editPropsOfImageAt(int index) {
        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(index);
        if (adsImage == null) {
            return;
        }
        ImageConfigureDialog.getInstanceFor(adsImage).executeDialog();
        update();
    }

    public void replaceImageAt(int index) {
        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(index);
        if (adsImage == null) {
            return;
        }
        ImageReplaceDialog.getInstanceFor(adsImage).executeDialog();
        update();
    }

//    public void copyImageAt(int index) {
//
//    }
    private List<AdsImageDef> getAllImages() {
        if (allImages != null) {
            return allImages;
        }
        return allImages = moduleImages.list();
    }

    public ModuleImages getModuleImages() {
        return moduleImages;
    }

    @Override
    public String getToolTipText(MouseEvent evt) {
        int index = locationToIndex(evt.getPoint());
        if (index < 0 || index >= getModel().getSize()) {
            return null;
        }
        AdsImageDef image = (AdsImageDef) model.getElementAt(index);
        if (image == null) {
            return null;
        }
        StringBuilder kw = new StringBuilder();
        for (String k : image.getKeywords()) {
            kw.append(" ");
            kw.append(k);
        }
        return "<html><b>" + NbBundle.getBundle(ImagesList.class).getString("Name") + ": </b>" + image.getName() +
                "<br><b>" + NbBundle.getBundle(ImagesList.class).getString("Key_words") + ":</b>" + kw.toString() +
                "<br><b>" + NbBundle.getBundle(ImagesList.class).getString("Description") + ":</b><br>" + image.getDescription() + "</html>";
    }

//    private ModuleImagesEditor.Mode getMode() {
//        for (Container cont = this.getParent(); cont != null; cont = cont.getParent()) {
//            if (cont instanceof ModuleImagesEditor) {
//                return ((ModuleImagesEditor)cont).getMode();
//            }
//        }
//        return null;
//    }

    public void showAll() {
        showImages(getAllImages());

//        RequestProcessor.getDefault().post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                final ProgressHandle handle = ProgressHandleFactory.createHandle(NbBundle.getBundle(ModuleImagesEditor.class).getString("Loading_Images"));
//
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        firePropertyChange(ENABLE_STATE, true, false);
//                        handle.start();
//                    }
//                });
//
//                try {
//                    model.assignImages(getAllImages());
//                } catch (Exception e) {
//                    ErrorManager.getDefault().notify(e);
//                } finally {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            handle.finish();
//                            firePropertyChange(ENABLE_STATE, false, true);
//                        }
//                    });
//                }
//            }
//        }, 0);
    }

    public Id[] getSelectedImagesIds() {
        ArrayList<Id> ret = new ArrayList<Id>();
        for (int i = 0; i < model.getSize(); ++i) {
            if (this.isSelectedIndex(i)) {
                AdsImageDef imageDef = (AdsImageDef)model.getElementAt(i);
                if (imageDef != null) {
                    ret.add(imageDef.getId());
                }
            }
        }
        return ret.toArray(new Id[ret.size()]);
    }

    public List<AdsImageDef> getSelectedAdsImages() {
        List<AdsImageDef> ret = new ArrayList<AdsImageDef>();
        for (int i = 0; i < model.getSize(); ++i) {
            if (this.isSelectedIndex(i)) {
                AdsImageDef imageDef = (AdsImageDef)model.getElementAt(i);
                if (imageDef != null) {
                    ret.add(imageDef);
                }
            }
        }
        return ret;
    }

//    public void addImage(AdsImageDef info) {
//        model.addImage(info);
//        getAllImages().add(info);
//        this.setSelectedValue(info, true);
//    }
    public List<AdsImageDef> getImagesWithPattern(String pattern, boolean inName, boolean inKeywords, boolean inDescription, boolean wholeWord) {
        ArrayList<AdsImageDef> ret = new ArrayList<AdsImageDef>();
        if (pattern.isEmpty()) {
            return ret;
        }
        if (wholeWord) {
            for (AdsImageDef imageDef : getAllImages()) {
                if (inName && imageDef.getName().toLowerCase().equals(pattern)) {
                    ret.add(imageDef);
                    continue;
                }
                if (inKeywords) {
                    String[] kws = pattern.split("[ ,.;\t\n]");
                    List<String> kwl = imageDef.getKeywords();
                    String[] KWS = new String[kwl.size()];
                    kwl.toArray(KWS);
                    boolean ok = false;
                    for (int i = 0; i < kws.length; ++i) {
                        for (int j = 0; j < KWS.length; ++j) {
                            if (kws[i].equals(KWS[j])) {
                                ret.add(imageDef);
                                ok = true;
                                break;
                            }
                        }
                        if (ok) {
                            break;
                        }
                    }
                    if (ok) {
                        continue;
                    }
                }
                if (inDescription) {
                    String[] desc = imageDef.getDescription().toLowerCase().split("[ ,.;!:?\t\n]");
                    for (int j = 0; j < desc.length; ++j) {
                        if (pattern.equals(desc[j])) {
                            ret.add(imageDef);
                            break;
                        }
                    }
                }
            }
        } else {
            for (AdsImageDef imageDef : getAllImages()) {
                if (inName && imageDef.getName().toLowerCase().indexOf(pattern) != -1) {
                    ret.add(imageDef);
                    continue;
                }
                if (inKeywords) {
                    List<String> keys = imageDef.getKeywords();
                    boolean ok = false;
                    for (String key : keys) {
                        if (key.toLowerCase().indexOf(pattern) >= 0) {
                            ret.add(imageDef);
                            ok = true;
                            break;
                        }
                    }
                    if (ok) {
                        continue;
                    }
                }
                if (inDescription && imageDef.getDescription().toLowerCase().indexOf(pattern) != -1) {
                    ret.add(imageDef);
                }
            }
        }
        return ret;
    }

    private void update() {
        model.update();
    }

    public void showImages(final List<AdsImageDef> list) {
        interruptLoadThread();
//        if (loadThread != null && loadThread.isAlive())
//            return;
        loadThread = new Thread(new Runnable() {

            @Override
            public void run() {
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        firePropertyChange(ENABLE_STATE, true, false);
//                    }
//                });
//                boolean enableHandle = getMode() == ModuleImagesEditor.Mode.CONFIGURE_IMAGES;
//                if (enableHandle) {
                handle = ProgressHandleFactory.createHandle(NbBundle.getBundle(
                        ModuleImagesEditor.class).getString("Loading_Images"));
                handle.start(100);
//                }
                try {
                    model.assignImages(list);
//                    int moo = 1;
//                    d = 0;
//                    while (moo < 100000000 && !Thread.currentThread().isInterrupted()) {
//                        d += Math.sqrt(moo);
//                        ++moo;
//                        Thread.yield();
//                        if (moo % 100000000 == 0)
                } catch (Exception e) {
                    ErrorManager.getDefault().notify(e);
                } finally {
//                    if (enableHandle) {
                        handle.finish();
//                    }
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            firePropertyChange(ENABLE_STATE, false, true);
//                        }
//                    });
                }
            }
        });
//        loadThread.setPriority(Thread.MIN_PRIORITY);
        loadThread.start();

//        RequestProcessor.getDefault().post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                final ProgressHandle handle = ProgressHandleFactory.createHandle(NbBundle.getBundle(ModuleImagesEditor.class).getString("Loading_Images"));
//
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        firePropertyChange(ENABLE_STATE, true, false);
//                        handle.start();
//                    }
//                });
//
//                try {
//                    model.assignImages(list);
//                } catch (Exception e) {
//                    ErrorManager.getDefault().notify(e);
//                } finally {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            handle.finish();
//                            firePropertyChange(ENABLE_STATE, false, true);
//                        }
//                    });
//                }
//            }
//        }, 0);
    }

    public void updateImagesList() {
        allImages = null;
    }

    public void removeSelectedImages() {
        List<AdsImageDef> selectedImageDefs = getSelectedAdsImages();
        for (AdsImageDef imageDef : selectedImageDefs) {
            if (!imageDef.delete()) {
                DialogUtils.messageError(new DefinitionError("Unable to delete image.", imageDef));
            }
//            model.removeImage(imageDef);
//            getAllImages().remove(imageDef);
        }
        this.clearSelection();
    }
}
