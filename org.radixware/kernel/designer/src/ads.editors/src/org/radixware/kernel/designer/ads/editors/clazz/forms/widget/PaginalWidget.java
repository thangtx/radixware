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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.beans.PropertyChangeEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.TabPanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


public abstract class PaginalWidget extends BaseWidget {

    public PaginalWidget(GraphSceneImpl scene, AdsUIItemDef node) {
        super(scene, node);

        setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, AdsUIUtil.currentWidget((AdsUIItemDef) getNode())));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport || evt.getSource() instanceof TabPanel || evt.getSource() == this) {
            final AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            if (prop.getName().equals("currentIndex")) {
                visible(isVisible());

                setLayoutProcessor(LayoutProcessor.Factory.newInstance(this, AdsUIUtil.currentWidget((AdsUIItemDef) getNode())));

                getSceneImpl().getFormView().updateToolBar();
                getSceneImpl().updateSelection();
                getSceneImpl().revalidate();
                getSceneImpl().validate();
                return;
            }
        }
        super.propertyChange(evt);
    }

    public void setCurrentIndex(int index) {
        final AdsUIProperty.IntProperty property = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(getNode(), "currentIndex");

        if (property != null) {
            property.value = index;

            AdsUIUtil.fire(getNode(), property, this);
        }
    }
}
