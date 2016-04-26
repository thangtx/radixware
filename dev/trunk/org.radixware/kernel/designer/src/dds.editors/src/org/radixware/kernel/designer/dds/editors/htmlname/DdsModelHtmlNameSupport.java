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
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangedEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;

/**
 * {@linkplain HtmlNameSupport} for {@linkplain DdsModelDef}.
 */
public class DdsModelHtmlNameSupport extends HtmlNameSupport {

    RenameListener moduleRenameListener = new RenameListener() {

        @Override
        public void onEvent(RenameEvent e) {
            fireChanged();
        }
    };
    RadixObject.EditStateChangeListener containersEditState = new RadixObject.EditStateChangeListener() {

        @Override
        public void onEvent(EditStateChangedEvent e) {
            fireChanged();
        }
    };

    protected DdsModelHtmlNameSupport(DdsModelDef model) {
        super(model);

        DdsModule module = model.getModule();
        if (module != null) {
            module.addRenameListener(moduleRenameListener);
        }

        for (DdsDefinitions ddsDefinitions : model.getDiagramContainers()) {
            ddsDefinitions.addEditStateListener(containersEditState);
        }
    }

    public DdsModelDef getModel() {
        return (DdsModelDef) getRadixObject();
    }

    @Override
    public String getDisplayName() {
        return "Diagram"; // displayed in tree.
    }

    @Override
    public Color getColor() {
        DdsModelDef model = (DdsModelDef) getRadixObject();
        EEditState editState = model.getEditState();
        if (editState == EEditState.NEW) {
            return EEditState.NEW.getColor();
        }
        for (DdsDefinitions ddsDefinitions : model.getDiagramContainers()) {
            editState = ddsDefinitions.getEditState();
            if (editState == EEditState.MODIFIED) {
                return EEditState.MODIFIED.getColor();
            }
        }
        return EEditState.NONE.getColor();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        /**
         * Registeren in layer.xml
         */
        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new DdsModelHtmlNameSupport((DdsModelDef) object);
        }
    }
}
