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
import com.trolltech.qt.gui.QWidget;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.InvalidPropertyValueTypeException;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;

final class SetWindowRectCmd extends WindowCmd{
    
    private final static QPoint ZERO_POINT = new QPoint(0, 0);

    @Override
    public String getName() {
        return "rect";
    }        

    @Override
    public String getGroupName() {
        return "window";
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    @Override
    public boolean isInputActionCommand() {
        return true;
    }
    
    private static int parseGeometryItem(final JSONObject json, final String fieldName, final int defaultValue) throws InvalidPropertyValueTypeException{
        final Object value = json.get(fieldName);
        if (value==null){
            return defaultValue;
        }
        if (value instanceof Long==false){
            throw new InvalidPropertyValueTypeException(fieldName, value);
        }
        final long intValue = (Long)value;
        if (intValue>Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        if (intValue<Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }
        return ((Long)value).intValue();
    }

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        super.execute(session, uri, parameter);
        final QWidget topLevelWindow = getTopLevelWindow();       
        final QPoint pos = topLevelWindow.mapToGlobal(ZERO_POINT);
        final QSize size = topLevelWindow.size();
        final int x = parseGeometryItem(parameter, "x", pos.x());
        final int y = parseGeometryItem(parameter, "y", pos.y());
        final int width = parseGeometryItem(parameter, "width", size.width());
        if (width<0 || width>WidgetUtils.MAXIMUM_SIZE){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT,"Value of width property is out of bounds");
        }
        final int height = parseGeometryItem(parameter, "height", size.height());        
        if (height<0 || height>WidgetUtils.MAXIMUM_SIZE){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT,"Value of height property is out of bounds");
        }
        topLevelWindow.resize(width, height);        
        topLevelWindow.move(x, y);
        return getWidgetGeometry(topLevelWindow);        
    }
}
