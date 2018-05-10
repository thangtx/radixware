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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.editors.property.PropLabelController;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class PropLabel extends QLabel implements IModelWidget, IPropLabel, IWidget, ICachableWidget {

    private static class Controller extends PropLabelController<PropLabel> {

        public Controller(PropLabel labelComponent, Property property) {
                    super(property, labelComponent);
        }

        public Controller(PropLabel labelComponent) {
            super(labelComponent);
        }

        @Override
        protected void updateLabelText(String text) {
            getWidget().setText(text);
        }

        @Override
        protected boolean isPropVisualizerInUITree() {
            return getWidget().parentWidget() != null;
        }

        @Override
        protected void updateSettings() {
            getWidget().updateSettings();
        }
    }
    
    private final PropLabelController<PropLabel> controller;
    private boolean wasBinded;
    private boolean inCache;

    public PropLabel(QWidget parent, Property property) {
        super(parent);
        this.controller = new Controller(this, property);
        setText(property.getTitle());
        adjustSize();
        updateSettings();
        Application.getInstance().getActions().settingsChanged.connect(this, "updateSettings()");
    }

    public PropLabel(QWidget parent) {
        super(parent);
        this.controller = new Controller(this);
    }

    public void setProperty(final Property property) {
        controller.setProperty(property);        
        updateSettings();
        Application.getInstance().getActions().settingsChanged.connect(this, "updateSettings()");            
        if (parentWidget()!=null){
            controller.bind();
            wasBinded = true;
        }
        if (property!=null){
            setObjectName("propLabel #"+property.getId().toString());
        }
    }

    @Override
    public void bind() {
        if (!wasBinded){
            controller.bind();
            if (parentWidget()==null){
                final Property property = controller.getProperty();
                if (RunParams.isDevelopmentMode()){
                    final String warningMessage = 
                        property.getEnvironment().getMessageProvider().translate("TraceMessage", "Call of bind method for property label with no parent widgget detected.\nIt is necessary to set parent widget for label of property '%1$s' (#%2$s) before call this method");
                    property.getEnvironment().getTracer().warning(String.format(warningMessage, property.getTitle(), property.getId().toString()));
                }
                if (isHidden() && property.isVisible()){
                    setVisible(true);
                }
            }        
            
            wasBinded = true;
        }
    }

    @Override
    public void refresh(ModelItem changedItem) {
        controller.refresh(changedItem);
    }

    @Override
    public boolean setFocus(Property property) {
        return false;
    }

    private void updateSettings() {
        final Property property = controller.getProperty();
        if (property != null) {
            final ExplorerTextOptions options = 
                    (ExplorerTextOptions)property.getTitleTextOptions().getOptions();
            WidgetUtils.applyTextOptions(options, this);
        }
    }
    
    void release(){
        controller.close();
        Application.getInstance().getActions().settingsChanged.disconnect(this);
        setVisible(false);
        setEnabled(true);
        setParent(null);
        wasBinded = false;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        controller.close();
        super.closeEvent(event);
    }

    public QWidget asQWidget() {
        return this;
    }

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }
    
    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        throw new UnsupportedOperationException("startTimer is not supported here.");
    }

    @Override
    public void killTimer(IPeriodicalTask task) {
        throw new UnsupportedOperationException("killTimer is not supported here.");
    }                
    
    public void setInCache(final boolean inCache){
        this.inCache = inCache;
    }

    @Override
    public boolean isInCache() {
        return inCache;
    }    
}