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

package org.radixware.wps.views.editor.property;

import org.radixware.kernel.common.client.editors.property.PropLabelController;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;


public class PropLabel extends Label implements IPropLabel {

    private static class Controller extends PropLabelController<PropLabel> {

        public Controller(Property property, PropLabel widget) {
            super(property, widget);
        }

        @Override
        protected void updateLabelText(String text) {
            getWidget().setText(text);
        }

        @Override
        protected boolean isPropVisualizerInUITree() {
            return getWidget().getParent() != null;
        }

        @Override
        protected void updateSettings() {
            getWidget().updateTextOptions();
        }
    }
    private final Controller controller;
    private final WpsEnvironment env;
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            updateTextOptions();
        }
    };

    public PropLabel(Property property) {
        super("");
        this.controller = new Controller(property, this);
        setTextWrapDisabled(true);
        this.env = (WpsEnvironment) getEnvironment();
        this.setVAlign(Alignment.MIDDLE);
        env.addSettingsChangeListener(l);
    }

    @Override
    public void refresh(ModelItem aThis) {
        controller.refresh(aThis);
    }

    @Override
    public boolean setFocus(Property aThis) {
        return false;
    }

    @Override
    public void bind() {
        controller.bind();
    }

    private void updateTextOptions() {
        final Property property = controller.getProperty();
        if (property != null) {
            if (property.getTitleTextOptions() != null) {
                final WpsTextOptions options = (WpsTextOptions) property.getTitleTextOptions().getOptions();
                if (options != null) {
                    options.changeBackgroundColor(null);
                    setTextOptions(options);
                    setBackground(null);//background must be transparent (as default), not white}
                }
            }
        }
    }

    public void close() {
        controller.close();
        if (l!=null) {
            env.removeSettingsChangeListener(l);
        }
    }
}
