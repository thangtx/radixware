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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleImages;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class ModuleImagesHtmlNameSupport extends HtmlNameSupport {

    private final RadixObjects.ContainerChangesListener containerChangesListener = new RadixObjects.ContainerChangesListener() {

        @Override
        public void onEvent(ContainerChangedEvent e) {
            fireChanged();
        }
    };
    private RenameListener moduleRenameListener = null;

    public ModuleImagesHtmlNameSupport(ModuleImages moduleImages) {
        super(moduleImages);
        moduleImages.getContainerChangesSupport().addEventListener(containerChangesListener);
    }

    public ModuleImages getModuleImages() {
        return (ModuleImages) getRadixObject();
    }

    @Override
    public Color getColor() {
        final ModuleImages moduleImages = getModuleImages();

        if (moduleImages.isEmpty()) {
            return Color.GRAY;
        } else {
            return super.getColor();
        }
    }

    @Override
    public Color getEditorColor() {
        return super.getColor(); // super
    }

    @Override
    public String getEditorDisplayName() {
        final ModuleImages moduleImages = getModuleImages();
        final AdsModule module = moduleImages.getModule();

        if (moduleRenameListener == null) {
            moduleRenameListener = new RadixObject.RenameListener() {

                @Override
                public void onEvent(RenameEvent e) {
                    fireChanged();
                }
            };
            module.addRenameListener(moduleRenameListener);
        }

        return module.getName() + " " + moduleImages.getName();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject radixObject) {
            return new ModuleImagesHtmlNameSupport((ModuleImages) radixObject);
        }
    }
}
