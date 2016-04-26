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

package org.radixware.kernel.common.defs.ads.ui;

import java.beans.PropertyChangeListener;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;


public class WidgetAdapter implements IWidgetAdapter {

    private IWidgetAdapter adapter;

    public WidgetAdapter(AdsUIItemDef uIItemDef) {
        if (uIItemDef instanceof AdsWidgetDef) {
            adapter = new AdsWidgetAdapter((AdsWidgetDef) uIItemDef);
        } else if (uIItemDef instanceof AdsRwtWidgetDef) {
            adapter = new AdsRwtWidgetAdapter((AdsRwtWidgetDef) uIItemDef);
        } else {
            assert false : "Incompatible type of AdsUIItemDef";
        }
    }

    @Override
    public double getWeight() {
        return adapter.getWeight();
    }

    @Override
    public Definitions<? extends AdsUIItemDef> getWidgets() {
        return adapter.getWidgets();
    }

    @Override
    public AdsUIItemDef getWidget() {
        return adapter.getWidget();
    }

    @Override
    public String getClassName() {
        return adapter.getClassName();
    }

    @Override
    public Definitions<AdsUIActionDef> getActions() {
        return adapter.getActions();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        adapter.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        adapter.removePropertyChangeListener(listener);
    }

    @Override
    public void setWeight(double weight) {
        adapter.setWeight(weight);
    }

    private static class AdsWidgetAdapter implements IWidgetAdapter {

        private final AdsWidgetDef widgetDef;

        public AdsWidgetAdapter(AdsWidgetDef widgetDef) {
            this.widgetDef = widgetDef;
        }

        @Override
        public double getWeight() {
            return widgetDef.getWeight();
        }

        @Override
        public Definitions<? extends AdsUIItemDef> getWidgets() {
            return widgetDef.getWidgets();
        }

        @Override
        public AdsUIItemDef getWidget() {
            return widgetDef;
        }

        @Override
        public String getClassName() {
            return widgetDef.getClassName();
        }

        @Override
        public Definitions<AdsUIActionDef> getActions() {
            return widgetDef.getActions();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            widgetDef.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            widgetDef.removePropertyChangeListener(listener);
        }

        @Override
        public void setWeight(double weight) {
            widgetDef.setWeight(weight);
        }
    }

    private static class AdsRwtWidgetAdapter implements IWidgetAdapter {

        private final AdsRwtWidgetDef rwtWidgetDef;

        public AdsRwtWidgetAdapter(AdsRwtWidgetDef widgetDef) {
            this.rwtWidgetDef = widgetDef;
        }

        @Override
        public double getWeight() {
            return rwtWidgetDef.getWeight();
        }

        @Override
        public Definitions<? extends AdsUIItemDef> getWidgets() {
            return rwtWidgetDef.getWidgets();
        }

        @Override
        public AdsUIItemDef getWidget() {
            return rwtWidgetDef;
        }

        @Override
        public String getClassName() {
            return rwtWidgetDef.getClassName();
        }

        @Override
        public Definitions<AdsUIActionDef> getActions() {
            return rwtWidgetDef.getActions();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            rwtWidgetDef.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            rwtWidgetDef.removePropertyChangeListener(listener);
        }

        @Override
        public void setWeight(double weight) {
            rwtWidgetDef.setWeight(weight);
        }
    }
}
