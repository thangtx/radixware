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

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.Container;
import java.awt.Dialog;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.module.images.ModuleImagesEditor;
import org.radixware.kernel.designer.common.editors.module.images.ModuleImagesEditor.EnableChangeListener;


class ImagePanel extends ModuleImagesEditor implements PropertyChangeListener, EnableChangeListener {

    private PropertyEnv env;
    private ImageEditor editor;

    public ImagePanel(final ImageEditor editor, PropertyEnv env) {
        super(editor.getModule());
        addEnableChangeListener(this);

        this.env = env;
        this.editor = editor;

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);
    }

    @Override
    public void enableChanged(boolean enabled) {
        Container c = getParent();
        while (c != null && !(c instanceof Dialog)) {
            c = c.getParent();
        }
        if (c instanceof Dialog)
            c.setEnabled(enabled);
    }

    private Id getSelectedImageId() {
        List<AdsImageDef> images = getSelectedAdsImages();
        AdsImageDef img = (images.size() > 0) ? images.get(0) : null;
        if (img != null) {
            final AdsModule module = editor.getModule();
            final AdsModule imgModule = img.getModule();
            if (!module.equals(imgModule) && !module.getDependences().contains(imgModule))
                module.getDependences().add(imgModule);
            return img.getId();
        }
        return null;
    }

    private AdsUIProperty.ImageProperty getProperty() {
        return (AdsUIProperty.ImageProperty)editor.getValue();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.ImageProperty prop = getProperty();
            Id id = getSelectedImageId();
            if (id == null){
                return;
            }
            prop.setImageId(id);
            try {
                ((UIPropertySupport)editor.getSource()).setValue(prop);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            editor.setValue(prop);
        }
    }
}
