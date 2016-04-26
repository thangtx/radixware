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


package org.radixware.wps.rwt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.html.Html;


public class Slider extends AbstractContainer {

    public interface ChangeValueListener {

        public void onValueChange(Slider sl);
    }
    private final ChangeValueListener defStateListener = new ChangeValueListener() {
        @Override
        public void onValueChange(Slider sl) {
            List<ChangeValueListener> lss;
            synchronized (listeners) {
                lss = new ArrayList<>(listeners);
            }
            for (ChangeValueListener l : lss) {
                l.onValueChange(sl);
            }
        }
    };
    private final List<ChangeValueListener> listeners = new LinkedList<>();
    private final Container slider = new Container();
    private final Container thumb = new Container();
    private final Container maxDiv = new Container();
    private final Container minDiv = new Container();
    private final Container container;
    private int min;
    private int max;
    private int step;
    private int currentValue;
    private String legend = "";
    private boolean isLegendVisible = false;

    public Slider() {
        super();
        container = new Container();
        add(container);
        html.setCss("margin", "10px 5px 10px 0px");
        slider.html.setCss("width", "100%");
        slider.html.addClass("rwt-ui-slider");
        thumb.html.addClass("rwt-ui-slider-thumb");
        container.add(slider);

        html.setCss("width", "inherit");
        slider.add(thumb);
        setInputRange(0, 100);
        setValue(0);
        showRange(true);
        html.layout("$RWT.slider.layout");

    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if (actionName.equals("valueChanged")) {
            setValue(Integer.parseInt(actionParam));
        }
        super.processAction(actionName, actionParam);
    }

    public final void setInputRange(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException(String.format("Maximum input range value cannot be less or equal to minimum value. Minimum value: %d; maximun value: %d.", min, max));
        } else {
            this.min = min;
            this.max = max;
            slider.html.setAttr("min", min);
            slider.html.setAttr("max", max);
            setUnits(legend);
        }
    }

    public int getMinimalVal() {
        return min;
    }

    public int getMaximumVal() {
        return max;
    }

    public final void setValue(int val) {
        if (val <= max && val >= min) {
            slider.html.setAttr("value", val);
            currentValue = val;
            defStateListener.onValueChange(this);
        }
    }

    public void addChangeValueListener(ChangeValueListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeChangeValueListener(ChangeValueListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    public final void showRange(boolean showRange) {
        if (showRange != isLegendVisible) {
            isLegendVisible = showRange;
            if (showRange) {
                this.add(minDiv);
                minDiv.html.addClass("rwt-slider-range-label");
                minDiv.html.setCss("float", "left");
                minDiv.getHtml().setInnerText(min + legend);

                this.add(maxDiv);
                maxDiv.html.addClass("rwt-slider-range-label");
                maxDiv.getHtml().setCss("float", "right");
                maxDiv.getHtml().setInnerText(max + legend);
            } else {
                this.remove(minDiv);
                this.remove(maxDiv);
            }
        }
    }
    
    public boolean isRangeShown(){
        return isLegendVisible;
    }

    public final void setUnits(String units) {
        this.legend = units;
        minDiv.html.setInnerText(min + legend);
        maxDiv.html.setInnerText(max + legend);
    }

    public String getUnits(){
        return legend;
    }
    
    public int getValue() {
        return currentValue;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
        slider.html.setAttr("step", step > 0 ? step : 1);
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        if (container.getHtmlId().equals(id)) {
            return this;
        }
        if (slider.getHtmlId().equals(id)) {
            return this;
        }
        if (html.getId().equals(id)) {
            return this;
        }
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        return null;
    }
}
