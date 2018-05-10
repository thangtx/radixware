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

package org.radixware.wps.views.editor;

import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.wps.rwt.GroupBox;
import org.radixware.wps.views.editor.property.AbstractPropEditor;
import org.radixware.wps.views.editor.property.PropLabel;


public class PropertiesGroupWidget extends GroupBox implements IModelWidget, IPropertiesGroupWidget{
    
    private final PropertiesGroupModelItem group;
    private final PropertiesGrid propertiesGrid;
    
    PropertiesGroupWidget(final PropertiesGroupModelItem propertiesGroup, final PropertiesGridController<PropLabel, AbstractPropEditor, PropertiesGroupWidget> parentController){
        this.group = propertiesGroup;
        propertiesGrid = new PropertiesGrid(propertiesGroup, parentController);        
        propertiesGrid.setObjectName("rx_props_grid_"+group.getId().toString());
        propertiesGrid.getHtml().setCss("overflow-y", "hidden");
        propertiesGrid.adjustToContent(true);
        propertiesGrid.setSizePolicy(SizePolicy.EXPAND, SizePolicy.MINIMUM_EXPAND);        
        add(propertiesGrid);
        setBorderBoxSizingEnabled(true);
        setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
        setAdjustMode(AdjustMode.EXPAND_HEIGHT_BY_CONTENT);
        getHtml().addClass("properties-group");
        setObjectName("propGroup #"+group.getId().toString());
    }
    
    PropertiesGrid getPropertiesGrid(){
        return propertiesGrid;
    }    

    @Override
    public int getVisibleRowsCount() {
        return propertiesGrid.getVisibleRowsCount();
    }

    @Override
    public void finishEdit() {
        propertiesGrid.finishEdit();
    }

    @Override
    public void refresh(final ModelItem modelItem) {
        if (modelItem==group){            
            if (group.isEnabled()!=isEnabled()){
                setEnabled(group.isEnabled());
            }
            setFrameVisible(group.isFrameVisible());
            setTitle(group.getTitle());
            setVisible(group.isVisible());
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
        refresh(group);
    }
    
    public void close(){
        propertiesGrid.close();
        group.unsubscribe(this);
    }    
}
