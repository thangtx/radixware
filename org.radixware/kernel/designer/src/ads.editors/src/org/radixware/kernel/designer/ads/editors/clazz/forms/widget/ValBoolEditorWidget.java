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

/*
 * 11/10/11 12:42 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ValEditorLayoutProcessor;


public class ValBoolEditorWidget extends ValEditorWidget {

    public ValBoolEditorWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node, ValEditorLayoutProcessor.Align.LEFT);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (propertyAccept(evt)) {
            AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            Boolean value;
            if ("notNull".equals(prop.getName())) {
                value = ((AdsUIProperty.BooleanValueProperty) AdsUIUtil.getUiProperty(getNode(), "value")).value;
                if (value == null) {
                    value = Boolean.FALSE;
                }

                ((AdsUIProperty.BooleanValueProperty) AdsUIUtil.getUiProperty(getNode(), "value")).value = value;
            } else if ("value".equals(prop.getName())) {
                boolean notNull = ((AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(getNode(), "notNull")).value;
                value = ((AdsUIProperty.BooleanValueProperty) prop).value;
                if (notNull && value == null) {
                    ((AdsUIProperty.BooleanValueProperty) prop).value = Boolean.FALSE;
                }
            }
        }
        super.propertyChange(evt);
    }

    @Override
    public void notifyClicked(Point localLocation) {
    }
}
