/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionGroupBox;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class PropertiesGroupWidget extends QGroupBox implements IExplorerModelWidget, IPropertiesGroupWidget{

    private final PropertiesGroupModelItem group;
    private final PropertiesGrid propertiesGrid;
    private final PropertiesGrid parentPropertiesGrid;
    private boolean wasBinded;
    private String currentStyleSheet;
    private boolean wasClosed;
    
    PropertiesGroupWidget(final PropertiesGroupModelItem propertiesGroup, final PropertiesGridController<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> parentController, final PropertiesGrid parentGrid, final QWidget parentWidget){
        super(parentWidget);
        this.group = propertiesGroup;
        parentPropertiesGrid = parentGrid;
        propertiesGrid = new PropertiesGrid(this);        
        final QVBoxLayout layout = WidgetUtils.createVBoxLayout(this);
        layout.addWidget(propertiesGrid);
        propertiesGrid.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        propertiesGrid.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        propertiesGrid.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.MinimumExpanding);
        setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Expanding);
        setFocusProxy(propertiesGrid);
        setObjectName("propGroup #"+propertiesGroup.getId().toString());        
    }
    
    public PropertiesGroupModelItem getPropertiesGroup(){
        return group;
    }
    
    PropertiesGrid getParentPropertiesGrid(){
        return parentPropertiesGrid;
    }
    
    PropertiesGrid getPropertiesGrid(){
        return propertiesGrid;
    }
    
    private void updateVisibility(){
        if (!wasClosed && wasBinded){
            setVisible(group.isVisible());
        }
    }        

    @Override
    protected void paintEvent(final QPaintEvent event) {
        if (!wasBinded || group.isFrameVisible()){
            super.paintEvent(event);
        }
    }

    @Override
    public void refresh(final ModelItem modelItem) {
        if (modelItem==group){            
            if (group.isEnabled()!=isEnabled()){
                setEnabled(group.isEnabled());
            }
            setTitle(group.getTitle());
            updateContentMargins();
            repaint();
        }
        updateVisibility();
    }
    
    private void updateContentMargins(){
        if (group.isFrameVisible()){
            final QStyleOptionGroupBox styleOption = new QStyleOptionGroupBox();
            initStyleOption(styleOption);
            final QRect contentsRect = style().subControlRect(QStyle.ComplexControl.CC_GroupBox, styleOption, QStyle.SubControl.SC_GroupBoxContents, this);
            final QRect boxRect = styleOption.rect();
            setContentsMargins(contentsRect.left() - boxRect.left(), contentsRect.top() - boxRect.top(),
                          boxRect.right() - contentsRect.right(), boxRect.bottom() - contentsRect.bottom());
        }else{            
            setContentsMargins(0, 0, 0, 0);
            layout().setContentsMargins(0,0,0,0);
        }
    }

    @Override
    public boolean setFocus(final Property property) {
        return propertiesGrid.setFocus(property);
    }

    @Override
    public void bind() {
        propertiesGrid.bind();
        group.subscribe(this);
        wasBinded = true;
        refresh(group);
        Application.getInstance().getActions().settingsChanged.connect(this,"applySettings()");
        applySettings();
        setVerticalMarging(propertiesGrid.getVerticalSpacing());
    }
    
    public void setVerticalMarging(final int marging){
        propertiesGrid.setContentVerticalMargins(marging);
    }

    @Override
    public int getVisibleRowsCount() {
        return propertiesGrid.getRowsCount();
    }

    @Override
    public void finishEdit() {
        propertiesGrid.finishEdit();
    }        

    @Override
    public QWidget asQWidget() {
        return this;
    }
    
    @Override
    public QSize sizeHint() {
        if (wasBinded){
            if (group.isFrameVisible()){
                return super.minimumSizeHint();
            }else{
                return propertiesGrid.minimumSizeHint();
            }
        }
        return super.sizeHint();
    }
    
    @Override
    protected void closeEvent(final QCloseEvent event) {
        wasClosed = true;
        group.unsubscribe(this);
        Application.getInstance().getActions().settingsChanged.disconnect(this);
        propertiesGrid.close();
        super.closeEvent(event);        
    }  
    
    int getMaxRowHeight(){
        return propertiesGrid.getMaxRowHeight();
    }
    
    private void applySettings(){
        final ExplorerTextOptions textOptions = 
            (ExplorerTextOptions)group.getEnvironment().getTextOptionsProvider().getOptions(EnumSet.of(ETextOptionsMarker.LABEL, ETextOptionsMarker.REGULAR_VALUE), null);
        final QFont font = propertiesGrid.font();
        setFont(textOptions.getQFont());
        propertiesGrid.setFont(font);
        final String styleSheet = textOptions.getStyleSheet("QGroupBox");
        if (!Objects.equals(currentStyleSheet, styleSheet)){
            currentStyleSheet = styleSheet;
            setStyleSheet(styleSheet);
        }        
    }
}
