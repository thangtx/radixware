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

package org.radixware.kernel.designer.dds.editors.htmlname;

import java.awt.Color;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;

/**
 * {@linkplain HtmlNameSupport} for {@linkplain DdsModule}.
 * Displays name, edit mode, edit state.
 */
public class DdsModuleHtmlNameSupport extends HtmlNameSupport {

    private IRadixEventListener modelListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            fireChanged();
        }
    };

    public DdsModule getModule() {
        return (DdsModule) getRadixObject();
    }

    protected DdsModuleHtmlNameSupport(DdsModule module) {
        super(module);
        getModule().getModelManager().getModelSupport().addEventListener(modelListener);
    }

    @Override
    public boolean isBold() {
        if (getModule().getModelManager().isInitialized()) {
            final DdsModelDef modifiedModel = getModule().getModelManager().getModifiedModelIfLoaded();
            if (modifiedModel != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Color getColor() {
        if (getModule().getModelManager().isInitialized()) {
            final DdsModelDef fixedModel = getModule().getModelManager().getFixedModelIfLoaded();
            if (fixedModel == null) {
                return Color.RED;
            }

            final DdsModelDef modifiedModel = getModule().getModelManager().getModifiedModelIfLoaded();
            if (modifiedModel != null && !modifiedModel.getModifierInfo().isOwn()) {
                return Color.GRAY;
            }
        }
        return super.getColor();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new DdsModuleHtmlNameSupport((DdsModule) object);
        }
    }
}
