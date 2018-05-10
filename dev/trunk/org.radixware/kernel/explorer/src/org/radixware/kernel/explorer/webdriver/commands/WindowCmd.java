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

package org.radixware.kernel.explorer.webdriver.commands;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

abstract class WindowCmd extends GuiCmd{
    
    protected static QWidget getTopLevelWindow() throws WebDrvException{
        final QWidget popupWidget = QApplication.activePopupWidget();
        if (isValidWindow(popupWidget)){
            return popupWidget;
        }
        final QWidget modalWidget = QApplication.activeModalWidget();
        if (isValidWindow(modalWidget)){
            return modalWidget;
        }
        final QWidget activeWindow = QApplication.activeWindow();
        if (isValidWindow(activeWindow)){
            return activeWindow;                    
        }
        final List<QWidget> topLevelWidgets = QApplication.topLevelWidgets();
        QWidget topLevelWindow = null;
        for (QWidget topWidget: topLevelWidgets){                                
            if (isValidWindow(topWidget)
                && topWidget.windowType()!=Qt.WindowType.ToolTip
                ){
                topLevelWindow = topWidget;
                if (topLevelWindow.windowType()==Qt.WindowType.Popup){
                    break;
                }
            }
        }
        if (topLevelWindow==null){
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_WINDOW, "Top level window not found");        
        }else{
            return  topLevelWindow;
        }
    }            
	
    private static boolean isValidWindow(final QWidget widget){
        return widget!=null && widget.nativeId()!=0 && widget.isVisible() && widget.isWindow();
    }    
    
    @SuppressWarnings("unchecked")
    protected static JSONObject getWidgetGeometry(final QWidget widget){
        final QPoint pos = widget.pos();
        final QSize size = widget.size();
        final JSONObject geometry = new JSONObject();
        geometry.put("x", pos.x());
        geometry.put("y", pos.y());
        geometry.put("width", size.width());
        geometry.put("height", size.height());
        return geometry;
    }

}
