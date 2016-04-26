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
package org.radixware.wps.settings;

import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class PreviewWidget extends Container {

    private final ValStrEditorController readOnlyValController;
    private final ValStrEditorController mandatoryValController;
    private final ValStrEditorController otherValController;

    public PreviewWidget(UIObject parent) {
        super();
        VerticalBox box = new VerticalBox();
        
        readOnlyValController = new ValStrEditorController(getEnvironment());
        readOnlyValController.setMandatory(false);
        readOnlyValController.setLabel(getApplication().getMessageProvider().translate("Settings Dialog", "Readonly property text"));
        
        mandatoryValController = new ValStrEditorController(getEnvironment());
        mandatoryValController.setMandatory(true);
        mandatoryValController.setLabel(getApplication().getMessageProvider().translate("Settings Dialog", "Mandatory property text"));
                
        otherValController = new ValStrEditorController(getEnvironment());
        otherValController.setMandatory(false);
        otherValController.setLabel(getApplication().getMessageProvider().translate("Settings Dialog", "Other property text"));
        
        box.add((UIObject) readOnlyValController.getValEditor());
        box.addSpace(2);
        box.add((UIObject) mandatoryValController.getValEditor());
        box.addSpace(2);
        box.add((UIObject) otherValController.getValEditor());

        GroupBox b = new GroupBox();
        b.setHSizePolicy(SizePolicy.MINIMUM_EXPAND);
        b.setParent(this);
        b.add(box);
        b.setTitle(getApplication().getMessageProvider().translate("Settings Dialog", "Preview"));
        add(box);
    }

    public void update(final WpsTextOptions readOnlyOptions, final WpsTextOptions mandatoryOptions, final WpsTextOptions otherOptions, final boolean isDef) {
        if (isDef) {
            if (readOnlyOptions != null) {
                readOnlyValController.getController().setDefaultTextOptions(readOnlyOptions);
            }
            if (otherOptions != null) {
                otherValController.getController().setDefaultTextOptions(otherOptions);
            }
        } else {
            if (readOnlyOptions != null) {
                readOnlyValController.getController().setTextOptionsForMarker(ETextOptionsMarker.UNDEFINED_VALUE, readOnlyOptions);
            }
            if (otherOptions != null) {
                otherValController.getController().setTextOptionsForMarker(ETextOptionsMarker.UNDEFINED_VALUE, otherOptions);
            }
        }
        if (mandatoryOptions != null) {
            mandatoryValController.getController().setDefaultTextOptions(mandatoryOptions);
        }
    }
}
