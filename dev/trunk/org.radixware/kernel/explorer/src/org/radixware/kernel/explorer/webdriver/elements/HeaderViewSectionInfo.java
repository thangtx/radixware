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

package org.radixware.kernel.explorer.webdriver.elements;

import com.trolltech.qt.QtPropertyReader;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHeaderView;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class HeaderViewSectionInfo {

    private final QHeaderView view;   
    private final int section;    
    private final Qt.Orientation orientation;
    
    HeaderViewSectionInfo(final QHeaderView view, final int section){
        this.view = view;
        orientation = view.orientation();        
        this.section = section;
    }        
        
    private String getStringData(final int dataRole){
        final Object data = view.model().headerData(section, orientation, dataRole);
        return data instanceof String ? (String)data : null;
    }
    
    public boolean isVisible(){
        return !view.isSectionHidden(section);
    }
    
    public int getVisualIndex(){
        return view.visualIndex(section);
    }
    
    public String getObjectName(){
        return getStringData(WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE);
    }

    public String getDisplayText(){
        return getStringData(Qt.ItemDataRole.DisplayRole);
    }
    
    public String getToolTip(){
        return getStringData(Qt.ItemDataRole.ToolTipRole);
    }
    
    public String getStatusTip(){
        return getStringData(Qt.ItemDataRole.StatusTipRole);
    }
    
    public String getAccessibleText(){
        return getStringData(Qt.ItemDataRole.AccessibleTextRole);
    }
    
    public String getAccessibleDescription(){
        return getStringData(Qt.ItemDataRole.AccessibleDescriptionRole);
    }
    
    public QFont getFont(){
        final Object data = view.model().headerData(section, orientation, Qt.ItemDataRole.FontRole);
        return data instanceof QFont ? (QFont)data : null;
    }
    
    public Qt.Alignment getTextAlignment(){
        final Object data = view.model().headerData(section, orientation, Qt.ItemDataRole.FontRole);
        if (data instanceof Qt.Alignment){
            return (Qt.Alignment)data;
        }else if (data instanceof Qt.AlignmentFlag){
            return new Qt.Alignment((Qt.AlignmentFlag)data);
        }else if (data instanceof Integer){
            return new Qt.Alignment((Integer)data);
        }else{
            return null;
        }
    }
    
    public int getSizeHint(){
        return view.sectionSizeHint(section);
    }
    
    public int getSize(){
        return view.sectionSize(section);
    }

    public int getPosition(){
        return view.sectionPosition(section);
    }

    public int getViewportPosition(){
        return view.sectionViewportPosition(section);
    }
    
    @QtPropertyReader(name="resizeMode")
    public QHeaderView.ResizeMode getResizeMode(){
        return view.resizeMode(section);
    }
    
    public UIElementReference getSectionReference(final WebDrvUIElementsManager manager){
        return manager.getSectionReference(view, section);
    }
    
    public static List<HeaderViewSectionInfo> getSections(final QHeaderView header){
        final List<HeaderViewSectionInfo> sections = new ArrayList<>(header.count());
        for (int i=0, count=header.count(); i<count; i++){
            sections.add(new HeaderViewSectionInfo(header, i));
        }
        return sections;
    }
}
