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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.enums.ERadixIconType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.merge.MergeEngineProvider;

public class ModuleImagesEditor extends RadixObjectEditor<ModuleImages> implements ListSelectionListener, PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ImagesList.ENABLE_STATE)) {
            fireEnableChanged((Boolean) evt.getNewValue());
        }
    }

    public interface EnableChangeListener {

        public void enableChanged(boolean enabled);
    }

    public interface SelectedImagesCountChangeListener {

        public void selectedImagesCountChanged(int selectedImagesCount);
    }

    public class OptionsChangeEvent extends RadixEvent {
    }

    public interface OptionsChangeListener extends IRadixEventListener<OptionsChangeEvent> {
    }

    public static class OptionsChangeSupport extends RadixEventSource<OptionsChangeListener, OptionsChangeEvent> {
    }

    public enum Mode {

        CONFIGURE_IMAGES, CHOOSE_IMAGE_DEPENDENT, CHOOSE_IMAGE_CURRENT, CHOOSE_IMAGE_ALL;
    };
    private Mode mode = Mode.CONFIGURE_IMAGES;
//    private static boolean showNames = false;
//    private static boolean originalSize = false;
    private static final OptionsChangeSupport OPTIONS_CHANGE_SUPPORT = new OptionsChangeSupport();
    private final OptionsChangeListener optionsChangeListener;
    private static final int FIND_DELAY = 1000;
    private static final String SVG_IMAGE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
            + "<svg\n"
            + "\txmlns:svg=\"http://www.w3.org/2000/svg\"\n"
            + "\txmlns=\"http://www.w3.org/2000/svg\"\n"
            + "\twidth=\"32\"\n"
            + "\theight=\"32\"\n"
            + "\tversion=\"1.0\">\n"
            + "</svg>";
    private final AdsModule adsModule;
//    private final List<ModuleImages> moduleImagesList;
    private final ImagesViewer imagesViewer;
    private final Timer timer, timer2;
//    private final HashSet<Id> usedModules = new HashSet<Id>(256);
    private boolean usedButtonEnabled;
//    private AdsImageDef imageDef;
    private String searchString = "";
    private ChooseImagesDialog dialog = null;
    private ArrayList<EnableChangeListener> enableChangeListeners = new ArrayList<EnableChangeListener>();
    private ArrayList<SelectedImagesCountChangeListener> selectedImagesCountChangeListeners = new ArrayList<SelectedImagesCountChangeListener>();
    private final ContainerChangesListener moduleImagesChangedListener = new ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
//            setMode(Mode.CONFIGURE_IMAGES);

            ImagesList imagesList = imagesViewer.getImagesListFor(adsModule.getImages());
            if (imagesList != null) {
                imagesList.updateImagesList();
            }
//            if (!imagesViewer.isVisible())
//                return;

//            showAll();
//            if (mode == Mode.CONFIGURE_IMAGES) {
            if (timer2.isRunning()) {
                timer2.restart();
            } else {
                timer2.start();
            }
//            } else {
            //TODO:
            //find(true);
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        find(false);
//                    }
//                });

//            }
        }
    };

    protected ModuleImagesEditor(ModuleImages moduleImages) {
        this(moduleImages, /*null,*/ moduleImages.getModule());
        usedButtonEnabled = true;
        currentModuleToggleButton.setVisible(false);
        dependentModulesToggleButton.setVisible(false);
        allModulesToggleButton.setVisible(false);
        mode = Mode.CONFIGURE_IMAGES;
    }

    protected ModuleImagesEditor(ModuleImages moduleImages, /*List<ModuleImages> moduleImagesList, */ AdsModule adsModule) {
        super(moduleImages);
        this.adsModule = adsModule;
//        this.moduleImagesList = moduleImagesList;

        initComponents();

//        showAllButton.setIcon(RadixWareIcons.EDIT.ALL_IMAGES.getIcon());
        addImageButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeImageButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
//        copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());
        String name = System.getProperty("os.name").toLowerCase();
        editButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon());
        propertiesButton.setIcon(RadixWareIcons.EDIT.PROPERTIES.getIcon());
        replaceButton.setIcon(RadixWareIcons.EDIT.REPLACE_IMAGE.getIcon());
        importButton.setIcon(RadixWareIcons.FILE.LOAD_IMAGE.getIcon());
        exportButton.setIcon(RadixWareIcons.FILE.SAVE_IMAGE.getIcon());
        usedByButton.setIcon(RadixWareIcons.TREE.DEPENDENCIES.getIcon());
        mergeChangesButton.setIcon(RadixWareIcons.SUBVERSION.UPDATE.getIcon());
        mergeChangesButton.setVisible(!adsModule.isUserModule());
        currentModuleToggleButton.setIcon(RadixWareIcons.EDIT.CURRENT_MODULE_IMAGES.getIcon());
        dependentModulesToggleButton.setIcon(RadixWareIcons.EDIT.DEPENDENT_MODULE_IMAGES.getIcon());
        allModulesToggleButton.setIcon(RadixWareIcons.EDIT.ALL_IMAGES.getIcon());

//        copyButton.setEnabled(false);
        editButton.setEnabled(false);
        propertiesButton.setEnabled(false);
        replaceButton.setEnabled(false);
        exportButton.setEnabled(false);
        usedByButton.setEnabled(false);
        removeImageButton.setEnabled(false);
        if (name.contains("linux")) {
            editButton.setVisible(false);
        }

        addImageButton.setEnabled(!moduleImages.isReadOnly());
        importButton.setEnabled(!moduleImages.isReadOnly());
        mergeChangesButton.setEnabled(false);

        nameCheckBox.setSelected(true);
        keywordsCheckBox.setSelected(true);
        descriptionCheckBox.setSelected(true);

        nameCheckBox.setVisible(false);
        keywordsCheckBox.setVisible(false);
        descriptionCheckBox.setVisible(false);
        wholeWordCheckBox.setVisible(false);

        imagesViewer = new ImagesViewer();
        imagesScrollPane.setViewportView(imagesViewer);
        imagesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        imagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.addComponentListener(imagesViewer);

        timer = new Timer(FIND_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                find();
            }
        });

        timer2 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer2.stop();
                find();
            }
        });

        showNamesCheckBox.setSelected(ModuleImagesEditorOptions.getDefault().isShowNames());
        imagesViewer.setShowNames(ModuleImagesEditorOptions.getDefault().isShowNames());
        realSizeCheckBox.setSelected(ModuleImagesEditorOptions.getDefault().isOriginalSize());
        imagesViewer.setOriginalSize(ModuleImagesEditorOptions.getDefault().isOriginalSize());

        optionsChangeListener = new OptionsChangeListener() {
            @Override
            public void onEvent(OptionsChangeEvent e) {
                showNamesCheckBox.setSelected(ModuleImagesEditorOptions.getDefault().isShowNames());
                imagesViewer.setShowNames(ModuleImagesEditorOptions.getDefault().isShowNames());
                realSizeCheckBox.setSelected(ModuleImagesEditorOptions.getDefault().isOriginalSize());
                imagesViewer.setOriginalSize(ModuleImagesEditorOptions.getDefault().isOriginalSize());
            }
        };

        OPTIONS_CHANGE_SUPPORT.addEventListener(optionsChangeListener);
        moduleImages.getContainerChangesSupport().addEventListener(moduleImagesChangedListener);

//        imagesViewer.addKeyListener(new KeyAdapter() {
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (Character.isLetterOrDigit(e.getKeyChar())) {
//                    findTextField.requestFocusInWindow();
//                    findTextField.dispatchEvent(e);
//                }
//            }
//        });
    }

    public ModuleImagesEditor(AdsModule adsModule) {
        this(adsModule.getImages(), /*null,*/ adsModule);
        usedButtonEnabled = false;
        setMode(Mode.CHOOSE_IMAGE_CURRENT);
        showAll();
    }

    @Override
    public void onClosed() {
        super.onClosed();
        imagesViewer.interruptAllLoadThreads();
    }

//    public ModuleImagesEditor(List<ModuleImages> moduleImagesList, AdsModule adsModule) {
//        this(adsModule.getImages(), moduleImagesList, null);
//        usedButtonEnabled = false;
//        addImageButton.setEnabled(false);
//        importButton.setEnabled(false);
////        copyButton.setEnabled(false);
//    }
    public Mode getMode() {
        return mode;
    }

    public void setDialog(ChooseImagesDialog dialog) {
        this.dialog = dialog;
    }

    public void apply() {
        if (dialog != null) {
            dialog.close(true);
        }
    }

    private void collectAllModules() {
        Collection<Definition> col = DefinitionsUtils.collectAllAround(
                adsModule.getSegment(), VisitorProviderFactory.createModuleVisitorProvider());
        imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//        imagesViewer.addToCache(adsModule.getImages(), imagesViewer.getImagesListFor(adsModule.getImages()));
        for (Definition def : col) {
            if (!(def instanceof AdsModule)) {
                continue;
            }
            AdsModule adsModule = (AdsModule) def;
            if (!adsModule.equals(this.adsModule) && adsModule.getImages().size() > 0) {
                imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//                imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(this);
            }
        }
    }

    private void collectDependentModules() {
        Collection<Definition> col = DefinitionsUtils.collectAllAround(adsModule, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsModule;
            }
        });
        imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//        imagesViewer.addToCache(adsModule.getImages(), imagesViewer.getImagesListFor(adsModule.getImages()));
//        imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(this);
        for (Definition def : col) {
            AdsModule adsModule = (AdsModule) def;
            if (!adsModule.equals(this.adsModule) && adsModule.getImages().size() > 0) {
                imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//                imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(this);
            }
        }
//        try {
//            if (usedModules.contains(adsModule.getId())) {
//                return;
//            }
//            if (adsModule.getImages().size() > 0 || adsModule == this.adsModule) {
//                imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName());
//                ImagesList list = imagesViewer.getImagesListFor(adsModule.getImages());
//                if (list != null) {
//                    list.addListSelectionListener(this);
//                }
//            }
//            usedModules.add(adsModule.getId());
//            for (AdsModule mod = adsModule.findOverwritten(); mod != null; mod = mod.findOverwritten()) {
//                if (!usedModules.contains(mod.getId())) {
//                    if (mod.getImages().size() > 0) {
//                        imagesViewer.addModuleImages(mod.getImages(), mod.getQualifiedName());
//                        ImagesList list = imagesViewer.getImagesListFor(mod.getImages());
//                        if (list != null) {
//                            list.addListSelectionListener(this);
//                        }
//                    }
//                    usedModules.add(mod.getId());
//                }
//            }
//            for (Dependence dependence : adsModule.getDependences()) {
//                Module module = dependence.findDependenceModule(null);
//                if (module instanceof AdsModule) {
//                    addAdsModule((AdsModule) module);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public ImagesViewer getImagesViewer() {
        return imagesViewer;
    }

    private void showAll() {
        timer.stop();
        searchString = "";
        findTextField.setText("");
        nameCheckBox.setVisible(false);
        keywordsCheckBox.setVisible(false);
        descriptionCheckBox.setVisible(false);
        wholeWordCheckBox.setVisible(false);

        if (!ModuleImagesEditor.this.isVisible()) {
            return;
        }

        imagesViewer.showAll();

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
//                        fireEnableChanged(false);
//                        handle.start();
//                    }
//                });
//
//                try {
//                    imagesViewer.showAll();
//                } catch (Exception e) {
//                    ErrorManager.getDefault().notify(e);
//                } finally {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            handle.finish();
//                            fireEnableChanged(true);
//                        }
//                    });
//                }
//            }
//        }, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        findTextField = new javax.swing.JTextField();
        nameCheckBox = new javax.swing.JCheckBox();
        keywordsCheckBox = new javax.swing.JCheckBox();
        descriptionCheckBox = new javax.swing.JCheckBox();
        wholeWordCheckBox = new javax.swing.JCheckBox();
        addImageButton = new javax.swing.JButton();
        removeImageButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        propertiesButton = new javax.swing.JButton();
        imagesScrollPane = new javax.swing.JScrollPane();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        showNamesCheckBox = new javax.swing.JCheckBox();
        realSizeCheckBox = new javax.swing.JCheckBox();
        usedByButton = new javax.swing.JButton();
        dependentModulesToggleButton = new javax.swing.JToggleButton();
        allModulesToggleButton = new javax.swing.JToggleButton();
        currentModuleToggleButton = new javax.swing.JToggleButton();
        replaceButton = new javax.swing.JButton();
        mergeChangesButton = new javax.swing.JButton();

        jLabel1.setLabelFor(findTextField);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.jLabel1.text")); // NOI18N

        findTextField.setColumns(21);
        findTextField.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.findTextField.text")); // NOI18N
        findTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                findTextFieldCaretUpdate(evt);
            }
        });

        nameCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.nameCheckBox.text")); // NOI18N
        nameCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameCheckBoxActionPerformed(evt);
            }
        });

        keywordsCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.keywordsCheckBox.text")); // NOI18N
        keywordsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keywordsCheckBoxActionPerformed(evt);
            }
        });

        descriptionCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.descriptionCheckBox.text")); // NOI18N
        descriptionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionCheckBoxActionPerformed(evt);
            }
        });

        wholeWordCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.wholeWordCheckBox.text")); // NOI18N
        wholeWordCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wholeWordCheckBoxActionPerformed(evt);
            }
        });

        addImageButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.addImageButton.text")); // NOI18N
        addImageButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addImageButtonActionPerformed(evt);
            }
        });

        removeImageButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.removeImageButton.text")); // NOI18N
        removeImageButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeImageButtonActionPerformed(evt);
            }
        });

        editButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.editButton.text")); // NOI18N
        editButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        propertiesButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.propertiesButton.text")); // NOI18N
        propertiesButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        propertiesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propertiesButtonActionPerformed(evt);
            }
        });

        importButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.importButton.text")); // NOI18N
        importButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        exportButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.exportButton.text")); // NOI18N
        exportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        showNamesCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.showNamesCheckBox.text")); // NOI18N
        showNamesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNamesCheckBoxActionPerformed(evt);
            }
        });

        realSizeCheckBox.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.realSizeCheckBox.text")); // NOI18N
        realSizeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                realSizeCheckBoxActionPerformed(evt);
            }
        });

        usedByButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.usedByButton.text")); // NOI18N
        usedByButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        usedByButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usedByButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(dependentModulesToggleButton);
        dependentModulesToggleButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.dependentModulesToggleButton.text")); // NOI18N
        dependentModulesToggleButton.setToolTipText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.dependentModulesToggleButton.toolTipText")); // NOI18N
        dependentModulesToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        dependentModulesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dependentModulesToggleButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(allModulesToggleButton);
        allModulesToggleButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.dependentModulesToggleButton.text")); // NOI18N
        allModulesToggleButton.setToolTipText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.allModulesToggleButton.toolTipText")); // NOI18N
        allModulesToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        allModulesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allModulesToggleButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(currentModuleToggleButton);
        currentModuleToggleButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.dependentModulesToggleButton.text")); // NOI18N
        currentModuleToggleButton.setToolTipText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.currentModuleToggleButton.toolTipText")); // NOI18N
        currentModuleToggleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        currentModuleToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentModuleToggleButtonActionPerformed(evt);
            }
        });

        replaceButton.setText(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.replaceButton.text")); // NOI18N
        replaceButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        replaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceButtonActionPerformed(evt);
            }
        });

        mergeChangesButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mergeChangesButton.setLabel(org.openide.util.NbBundle.getMessage(ModuleImagesEditor.class, "ModuleImagesEditor.mergeChangesButton.label")); // NOI18N
        mergeChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeChangesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(showNamesCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(realSizeCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(keywordsCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descriptionCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wholeWordCheckBox))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(findTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentModuleToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dependentModulesToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(allModulesToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(imagesScrollPane)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeImageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(importButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addImageButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(propertiesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(replaceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usedByButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mergeChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(findTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nameCheckBox)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(keywordsCheckBox)
                                .addComponent(descriptionCheckBox)
                                .addComponent(wholeWordCheckBox))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(allModulesToggleButton)
                        .addComponent(dependentModulesToggleButton)
                        .addComponent(currentModuleToggleButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(propertiesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addImageButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeImageButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usedByButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mergeChangesButton))
                    .addComponent(imagesScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showNamesCheckBox)
                    .addComponent(realSizeCheckBox)))
        );
    }// </editor-fold>//GEN-END:initComponents

    //showAll action
    //showAll(false);
    private File getImageFile() {
        ERadixIconType type = ChooseTypeDialog.Factory.newInstance().chooseType();
        if (type == null) {
            return null;
        }
        String ext = type.getValue();

        if (type == ERadixIconType.SVG) {
            try {
                File file = File.createTempFile("img", "." + ext);
                FileUtils.writeString(file, SVG_IMAGE, FileUtils.XML_ENCODING);
//                PrintWriter out = new PrintWriter(file);
//                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
//                out.println("<svg>");
//                out.println("</svg>");
//                out.close();
                return file;
            } catch (IOException e) {
                ErrorManager.getDefault().notify(e);
                return null;
            }
        } else {
            BufferedImage buf = new BufferedImage(16, 16, BufferedImage.TYPE_BYTE_BINARY);
            try {
                File file = File.createTempFile("img", "." + ext);
                ImageIO.write(buf, ext, file);
                return file;
            } catch (IOException e) {
                ErrorManager.getDefault().notify(e);
                return null;
            }
        }
    }

    private void addImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addImageButtonActionPerformed
        if (!Desktop.isDesktopSupported()) {
            ErrorManager.getDefault().notify(new RadixError("Desktop is not supported by the system."));
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN) && !desktop.isSupported(Desktop.Action.EDIT)) {
            ErrorManager.getDefault().notify(new RadixError("Open & edit file functions is not supported by the desktop."));
            return;
        }
        File file = getImageFile();
        if (file == null) {
            return;
        }

        try {
            AdsImageDef imageDef = adsModule.getImages().importImage(file);
            file.delete();
//            imagesViewer.getImagesListFor(adsModule.getImages()).addImage(imageDef);
            File imageFile = imageDef.getImageFile();
            if (desktop.isSupported(Desktop.Action.EDIT)) {
                desktop.edit(imageFile);
            } else {
                desktop.open(imageFile);
            }
        } catch (IOException e) {
            ErrorManager.getDefault().notify(e);
        }

//        try {
//            imageDef = null;
//            FileMonitor.register(file, new FileMonitor.FileListener() {
//
//                @Override
//                public void fileChanged(File file) {
//                    try {
//                        if (imageDef == null) {
//                            imageDef = adsModule.getImages().importImage(file);
//                            imagesViewer.getImagesListFor(adsModule.getImages()).addImage(imageDef);
//                        } else {
//                            File imageFile = imageDef.getImageDataFile();
//                            imageFile.delete();
//                            FileUtils.copyFile(file, imageFile);
//                            SwingUtilities.invokeLater(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    imagesViewer.getImagesListFor(adsModule.getImages()).updateImage(imageDef);
//                                }
//                            });
//                        }
//                    } catch (IOException e) {
//                        ErrorManager.getDefault().notify(e);
//                    }
//                }
//            });
//            if (desktop.isSupported(Desktop.Action.EDIT))
//                desktop.edit(file);
//            else
//                desktop.open(file);
//        } catch (IOException e) {
//            ErrorManager.getDefault().notify(e);
//        }

    }//GEN-LAST:event_addImageButtonActionPerformed

    private void removeImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeImageButtonActionPerformed
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(ModuleImagesEditor.class).getString("REMOVE_IMAGES_CONFIRM"))) {
            return;
        }
        imagesViewer.removeSelectedImages();
    }//GEN-LAST:event_removeImageButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        imagesViewer.editSelectedImage();
    }//GEN-LAST:event_editButtonActionPerformed

    private void propertiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertiesButtonActionPerformed
        imagesViewer.editPropsOfSelectedImage();
    }//GEN-LAST:event_propertiesButtonActionPerformed

    /* Copy action
     *
     if (adsModule == null) {
     return;
     }
     final List<ModuleImages> moduleImagesList = new ArrayList<ModuleImages>();
     adsModule.getSegment().getLayer().getBranch().visit(new IVisitor() {

     @Override
     public void accept(RadixObject radixObject) {
     if (((AdsModule) radixObject).getImages().size() > 0) {
     moduleImagesList.add(((AdsModule) radixObject).getImages());
     }
     }
     }, new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

     @Override
     public boolean isTarget(RadixObject object) {
     return object instanceof AdsModule;
     }
     });
     List<AdsImageDef> list = ChooseImagesDialog.getInstanceFor(moduleImagesList, adsModule).chooseAdsImages();

     //        AdsImageDef info = AdsSearcher.Factory.newImageSearcher(definition).findById(newImageId);

     ImagesList imagesList = imagesViewer.getImagesListFor(adsModule.getImages());
     for (AdsImageDef info : list) {
     File file = info.getImageFile();
     try {
     AdsImageDef imageDef = adsModule.getImages().importImage(file);
     imageDef.setName(info.getName());
     imageDef.setKeywords(info.getKeywords());
     imageDef.setDescription(info.getDescription());
     if (imagesList != null) {
     imagesList.addImage(imageDef);
     }
     } catch (IOException e) {
     ErrorManager.getDefault().notify(e);
     }
     }

     //        AdsImageDef adsImage = (AdsImageDef) model.getElementAt(index);
     //        File file = adsImage.getImageDataFile();
     //        try {
     //            AdsImageDef imageDef = moduleImages.importImage(file);
     //            imageDef.setName(adsImage.getName());
     //            imageDef.setKeywords(adsImage.getKeywords());
     //            imageDef.setDescription(adsImage.getDescription());
     //            addImage(imageDef);
     //        } catch (IOException e) {
     //            ErrorManager.getDefault().notify(e);
     //        }
     //        imagesViewer.copySelectedImage();
     */
    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        if (adsModule == null) {
            return;
        }
        ImageImportDialog.getInstanceFor(adsModule.getImages()).executeDialog();
//        if (info != null) {
//            ImagesList list = imagesViewer.getImagesListFor(adsModule.getImages());
//            if (list != null) {
//                list.addImage(info);
//            }
//        }
    }//GEN-LAST:event_importButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        imagesViewer.exportSelectedImage();
    }//GEN-LAST:event_exportButtonActionPerformed

    private void usedByButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usedByButtonActionPerformed
        imagesViewer.findUsagesOfSelectedImage();
    }//GEN-LAST:event_usedByButtonActionPerformed

    private void findTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_findTextFieldCaretUpdate
        if (searchString.equals(findTextField.getText())) {
            return;
        }
        searchString = findTextField.getText();
        boolean empty = searchString.isEmpty();
        nameCheckBox.setVisible(!empty);
        keywordsCheckBox.setVisible(!empty);
        descriptionCheckBox.setVisible(!empty);
        wholeWordCheckBox.setVisible(!empty);
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }//GEN-LAST:event_findTextFieldCaretUpdate

    /*if (includeCheckBox.isSelected()) {
     setMode(Mode.CHOOSE_IMAGE_DEPENDENT);
     find();
     } else {
     setMode(Mode.CHOOSE_IMAGE_CURRENT);
     find();
     }*/
    private void nameCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameCheckBoxActionPerformed
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }//GEN-LAST:event_nameCheckBoxActionPerformed

    private void keywordsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keywordsCheckBoxActionPerformed
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }//GEN-LAST:event_keywordsCheckBoxActionPerformed

    private void descriptionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionCheckBoxActionPerformed
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }//GEN-LAST:event_descriptionCheckBoxActionPerformed

    private void wholeWordCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wholeWordCheckBoxActionPerformed
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }//GEN-LAST:event_wholeWordCheckBoxActionPerformed

    private void currentModuleToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentModuleToggleButtonActionPerformed
        setMode(Mode.CHOOSE_IMAGE_CURRENT);
        find();
    }//GEN-LAST:event_currentModuleToggleButtonActionPerformed

    private void dependentModulesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dependentModulesToggleButtonActionPerformed
        setMode(Mode.CHOOSE_IMAGE_DEPENDENT);
        find();
    }//GEN-LAST:event_dependentModulesToggleButtonActionPerformed

    private void allModulesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allModulesToggleButtonActionPerformed
        setMode(Mode.CHOOSE_IMAGE_ALL);
        find();
    }//GEN-LAST:event_allModulesToggleButtonActionPerformed

    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
        imagesViewer.replaceSelectedImage();
    }//GEN-LAST:event_replaceButtonActionPerformed

    private void showNamesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNamesCheckBoxActionPerformed
        ModuleImagesEditorOptions.getDefault().setShowNames(showNamesCheckBox.isSelected());
        imagesViewer.setShowNames(showNamesCheckBox.isSelected());
        OPTIONS_CHANGE_SUPPORT.fireEvent(new OptionsChangeEvent());
    }//GEN-LAST:event_showNamesCheckBoxActionPerformed

    private void realSizeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_realSizeCheckBoxActionPerformed
        ModuleImagesEditorOptions.getDefault().setOriginalSize(realSizeCheckBox.isSelected());
        imagesViewer.setOriginalSize(realSizeCheckBox.isSelected());
        OPTIONS_CHANGE_SUPPORT.fireEvent(new OptionsChangeEvent());
    }//GEN-LAST:event_realSizeCheckBoxActionPerformed

    private void mergeChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeChangesButtonActionPerformed

        final MergeEngineProvider.MergeEngine engine = MergeEngineProvider.getEngine();
        if (engine != null) {
            List<AdsImageDef> selectedImages = getSelectedAdsImagesInCurrentModule();
            List<Definition> selectedDefs = new ArrayList();
            for (AdsImageDef image : selectedImages) {
                selectedDefs.add(image);
            }
            try {
                engine.doWithDefinitions(selectedDefs);
            } catch (Exception ex) {
                DialogUtils.messageError(ex);
            }
        }


    }//GEN-LAST:event_mergeChangesButtonActionPerformed

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case CHOOSE_IMAGE_DEPENDENT:
                dependentModulesToggleButton.setSelected(true);
                break;
            case CHOOSE_IMAGE_ALL:
                allModulesToggleButton.setSelected(true);
                break;
            case CHOOSE_IMAGE_CURRENT:
                currentModuleToggleButton.setSelected(true);
        }
        imagesViewer.clear();
        if (mode == Mode.CHOOSE_IMAGE_DEPENDENT) {
            if (adsModule != null) {
                collectDependentModules();
//                usedModules.clear();
            } /*else if (moduleImagesList != null) {
             imagesViewer.addAllModuleImages(moduleImagesList, this);
             }*/

            imagesViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            addImageButton.setEnabled(false);
//            removeImageButton.setEnabled(false);
        } else if (mode == Mode.CHOOSE_IMAGE_CURRENT) {
            if (adsModule == null) {
                return;
            }
            imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//            imagesViewer.addToCache(adsModule.getImages(), imagesViewer.getImagesListFor(adsModule.getImages()));
//            imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(this);

//            if (adsModule != null) {
//                collectAdsModules();
////                usedModules.clear();
//            } /*else if (moduleImagesList != null) {
//                imagesViewer.addAllModuleImages(moduleImagesList, this);
//            }*/
            imagesViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            addImageButton.setEnabled(false);
//            removeImageButton.setEnabled(false);
        } else if (mode == Mode.CHOOSE_IMAGE_ALL) {
            if (adsModule != null) {
                collectAllModules();
            }
            imagesViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } else {
            if (adsModule == null) {
                return;
            }
            imagesViewer.addModuleImages(adsModule.getImages(), adsModule.getQualifiedName(), this);
//            imagesViewer.addToCache(adsModule.getImages(), imagesViewer.getImagesListFor(adsModule.getImages()));
            imagesViewer.getImagesListFor(adsModule.getImages()).setupPopupMenu();
//            imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(this);
//            imagesViewer.getImagesListFor(adsModule.getImages()).addListSelectionListener(new ListSelectionListener() {
//
//                @Override
//                public void valueChanged(ListSelectionEvent e) {
//                    if (((JList)e.getSource()).getSelectedValues().length == 0)
//                        removeImageButton.setEnabled(false);
//                    else
//                        removeImageButton.setEnabled(true);
//                }
//            });
            imagesViewer.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//            addImageButton.setEnabled(true);
//            removeImageButton.setEnabled(false);
        }
    }

//    public Id[] getSelectedImages() {
//        return imagesViewer.getSelectedImagesIds();
//    }
    public List<AdsImageDef> getSelectedAdsImages() {
        return imagesViewer.getSelectedAdsImages();
    }

    private List<AdsImageDef> getSelectedAdsImagesInCurrentModule() {
        if (adsModule == null) {
            return Collections.EMPTY_LIST;
        }
        ImagesList list = imagesViewer.getImagesListFor(adsModule.getImages());
        if (list != null) {
            return list.getSelectedAdsImages();
        }
        return Collections.EMPTY_LIST;
    }

    private void find() {
        if (findTextField.getText().isEmpty()) {
            showAll();
            return;
        }

        if (!ModuleImagesEditor.this.isVisible()) {
            return;
        }

        imagesViewer.showImagesWithPattern(findTextField.getText(), nameCheckBox.isSelected(),
                keywordsCheckBox.isSelected(), descriptionCheckBox.isSelected(),
                wholeWordCheckBox.isSelected());

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
//                        fireEnableChanged(false);
//                        handle.start();
//                    }
//                });
//
//                try {
//                    imagesViewer.showImagesWithPattern(findTextField.getText(), nameCheckBox.isSelected(),
//                        keywordsCheckBox.isSelected(), descriptionCheckBox.isSelected(),
//                        wholeWordCheckBox.isSelected());
//                } catch (Exception e) {
//                    ErrorManager.getDefault().notify(e);
//                } finally {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            handle.finish();
//                            fireEnableChanged(true);
//                        }
//                    });
//                }
//            }
//        }, 0);
    }

    public void addEnableChangeListener(EnableChangeListener listener) {
        enableChangeListeners.add(listener);
    }

    public void addSelectedImagesCountChangeListener(SelectedImagesCountChangeListener listener) {
        selectedImagesCountChangeListeners.add(listener);
    }

    public void fireEnableChanged(boolean enabled) {
        for (EnableChangeListener l : enableChangeListeners) {
            l.enableChanged(enabled);
        }
    }

    private void fireSelectedImagesCountChanged(int count) {
        for (SelectedImagesCountChangeListener l : selectedImagesCountChangeListeners) {
            l.selectedImagesCountChanged(count);
        }
    }

    @Override
    public void update() {
        setMode(mode);
        find();
//        imagesViewer.update();
    }

    @Override
    public boolean open(OpenInfo openInfo) {

        setMode(mode);
        final RadixObject obj = openInfo.getTarget();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showAll();
                if (obj != null && adsModule != null && obj instanceof AdsImageDef) {
                    imagesViewer.getImagesListFor(adsModule.getImages()).setSelectedValue(obj, true);
                }
            }
        });
        return super.open(openInfo);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        List<AdsImageDef> selectedImages = getSelectedAdsImagesInCurrentModule();
        int cnt = selectedImages.size();
        if (mode == Mode.CONFIGURE_IMAGES) {
            final List<RadixObject> list = new ArrayList<RadixObject>(selectedImages);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (list.size() > 0) {
                        notifySelectionChanged(list);
                    } else {
                        notifySelectionChanged(Collections.singletonList(getRadixObject()));
                    }
                }
            });
        }

        boolean enabled = !getRadixObject().isReadOnly();
        removeImageButton.setEnabled(enabled && cnt > 0);
//        copyButton.setEnabled(cnt == 1);
        editButton.setEnabled(enabled && cnt == 1);
        propertiesButton.setEnabled(enabled && cnt == 1);
        replaceButton.setEnabled(enabled && cnt == 1);

        cnt = getSelectedAdsImages().size();
        exportButton.setEnabled(cnt == 1);
        usedByButton.setEnabled(cnt == 1 && usedButtonEnabled);
        mergeChangesButton.setEnabled(/*enabled && */cnt > 0);
        fireSelectedImagesCountChanged(cnt);
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<ModuleImages> {

        @Override
        public RadixObjectEditor<ModuleImages> newInstance(ModuleImages moduleImages) {
            return new ModuleImagesEditor(moduleImages);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addImageButton;
    private javax.swing.JToggleButton allModulesToggleButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton currentModuleToggleButton;
    private javax.swing.JToggleButton dependentModulesToggleButton;
    private javax.swing.JCheckBox descriptionCheckBox;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField findTextField;
    private javax.swing.JScrollPane imagesScrollPane;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox keywordsCheckBox;
    private javax.swing.JButton mergeChangesButton;
    private javax.swing.JCheckBox nameCheckBox;
    private javax.swing.JButton propertiesButton;
    private javax.swing.JCheckBox realSizeCheckBox;
    private javax.swing.JButton removeImageButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JCheckBox showNamesCheckBox;
    private javax.swing.JButton usedByButton;
    private javax.swing.JCheckBox wholeWordCheckBox;
    // End of variables declaration//GEN-END:variables
}
