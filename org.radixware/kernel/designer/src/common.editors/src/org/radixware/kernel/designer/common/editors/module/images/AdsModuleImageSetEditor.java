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

import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class AdsModuleImageSetEditor {

    private final AdsModule adsModule;
    
    public AdsModuleImageSetEditor(AdsModule adsModule) {
        this.adsModule = adsModule;
    }
    
    public void showAsDialog() {
        EditorsManager.getDefault().open(adsModule.getImages());
//        ChooseImagesDialog.getInstanceFor(adsModule).configureImages();
//        Id id = ImageSetDialog.getInstanceFor(adsModule).chooseImage();
//        Id[] ids = ImageSetDialog.getInstanceFor(adsModule).chooseImages();
//        for (Id id : ids) {
//            System.out.println(id);
//        }
    }
    
}
