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

import java.util.Collections;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ChooseImagesDialog extends ModalDisplayer {

    private final ModuleImagesEditor moduleImagesEditor;
//    private Id[] chosenImages = {};
    private List<AdsImageDef> chosenAdsImages = Collections.emptyList();

    private ChooseImagesDialog(ModuleImagesEditor moduleImagesEditor) {
        super(moduleImagesEditor);
        this.moduleImagesEditor = moduleImagesEditor;

        moduleImagesEditor.addEnableChangeListener(new ModuleImagesEditor.EnableChangeListener() {

            @Override
            public void enableChanged(boolean enabled) {
                ChooseImagesDialog.this.getDialog().setEnabled(enabled);
            }
        });

        moduleImagesEditor.addSelectedImagesCountChangeListener(new ModuleImagesEditor.SelectedImagesCountChangeListener() {

            @Override
            public void selectedImagesCountChanged(int selectedImagesCount) {
//                ImageSetDialog.this.getDialogDescriptor().setValid(selectedImagesCount > 0);
            }
        });

    }

    public static ChooseImagesDialog getInstanceFor(AdsModule adsModule) {
        ModuleImagesEditor editor = new ModuleImagesEditor(adsModule);
        ChooseImagesDialog dialog = new ChooseImagesDialog(editor);
        editor.setDialog(dialog);
        return dialog;
//        return new ChooseImagesDialog(new ModuleImagesEditor(adsModule));
    }

//    public static ChooseImagesDialog getInstanceFor(List<ModuleImages> list, AdsModule adsModule) {
//        return new ChooseImagesDialog(new ModuleImagesEditor(list, adsModule));
//    }

//    public void configureImages() {
//        okCancelOption = false;
//        moduleImagesEditor.setMode(Mode.CONFIGURE_IMAGES);
//        setTitle(NbBundle.getMessage(ChooseImagesDialog.class, "Configure_Images"));
//        getDialogDescriptor().setValid(true);
//        getDialog().addComponentListener(moduleImagesEditor.getImagesViewer());
//        getDialog().addWindowListener(new WindowAdapter() {
//
//            @Override
//            public void windowOpened(WindowEvent e) {
//                moduleImagesEditor.showAll();
//            }
//        });
////        dialog.setMinimumSize(new Dimension(560, 420));
//        showModal();
//        FileMonitor.cancel();
//    }

    public boolean chooseImage() {
//        moduleImagesEditor.setMode(Mode.CHOOSE_IMAGE_ONE);
        setTitle(NbBundle.getMessage(ChooseImagesDialog.class, "Choose_Image"));
        getDialogDescriptor().setValid(true);
        getDialog().addComponentListener(moduleImagesEditor.getImagesViewer());
//        dialog.setMinimumSize(new Dimension(560, 420));
        boolean result = showModal();
//        FileMonitor.cancel();
        return result;
    }

    public Id getSelectedImageId() {
        if (chosenAdsImages.size() > 0)
            return chosenAdsImages.get(0).getId();
        return null;
    }

    public AdsImageDef getSelectedAdsImageDef() {
        if (chosenAdsImages.size() > 0)
            return chosenAdsImages.get(0);
        return null;
    }

//    public Id[] chooseImages() {
//        moduleImagesEditor.setMode(Mode.CHOOSE_IMAGE_ONE);
//        setTitle(NbBundle.getMessage(ChooseImagesDialog.class, "Choose_Images"));
//        getDialogDescriptor().setValid(true);
//        getDialog().addComponentListener(moduleImagesEditor.getImagesViewer());
////        dialog.setMinimumSize(new Dimension(560, 420));
//        showModal();
////        FileMonitor.cancel();
//        Id[] ids = new Id[chosenAdsImages.size()];
//        for (int i = 0; i < chosenAdsImages.size(); ++i)
//            ids[i] = chosenAdsImages.get(i).getId();
//        return ids;
//    }

//    public List<AdsImageDef> chooseAdsImages() {
//        moduleImagesEditor.setMode(Mode.CHOOSE_IMAGE_ONE);
//        setTitle(NbBundle.getMessage(ChooseImagesDialog.class, "Choose_Images"));
//        getDialogDescriptor().setValid(true);
//        getDialog().addComponentListener(moduleImagesEditor.getImagesViewer());
////        dialog.setMinimumSize(new Dimension(560, 420));
//        showModal();
//        return chosenAdsImages;
//    }

//    @Override
//    public Object[] getOptions(){
//        if (okCancelOption)
//            return super.getOptions();
//        return new Object[] {DialogDescriptor.CLOSED_OPTION};
//    }

    @Override
    protected void apply() {
//        chosenImages = moduleImagesEditor.getSelectedImages();
        chosenAdsImages = moduleImagesEditor.getSelectedAdsImages();
    }
    
}
