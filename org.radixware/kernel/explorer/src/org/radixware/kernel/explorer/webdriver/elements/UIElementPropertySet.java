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

import com.trolltech.qt.QFlags;
import com.trolltech.qt.QtEnumerator;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QSizePolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UIElementPropertySet {
    
    private final static class EmptySet extends UIElementPropertySet{
        
        public EmptySet(){
            super(null);
        }

        @Override
        public String getValue(String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return null;
        }

        @Override
        public boolean containsProperty(String name) {
            return false;
        }

        @Override
        public List<String> getOrderedNames() {
            return Collections.emptyList();
        }

        @Override
        public Set<String> getNames() {
            return Collections.emptySet();
        }

        @Override
        public int size() {
            return 0;
        }                
    }
    
    public final static UIElementPropertySet EMPTY = new EmptySet();
    
    private final Object uiElement;
    private Map<String,String> propertyValues;
    private UIElementPropertyGetters propertyGetters;

    public UIElementPropertySet(final Object obj){        
        uiElement = obj;        
    }
    
    private UIElementPropertyGetters getProps(){
        if (propertyGetters==null){
            propertyGetters = UIElementPropertyGetters.getInstance(uiElement.getClass());
        }
        return propertyGetters;
    }
    
    public int size(){
        return getProps().size();
    }
    
    public Set<String> getNames(){
        return getProps().getNames();
    }   
    
    public List<String> getOrderedNames(){
        return getProps().getOrderedNames();
    }
    
    public boolean containsProperty(final String name){
        return getProps().containsProperty(name);
    }        
    
    private static String sizePolicyToString(final QSizePolicy.Policy sizePolicy){
        return sizePolicy==null ? "null" : sizePolicy.name();
    }
    
    private static String sizePolicyToString(final QSizePolicy sizePolicy){
        final StringBuilder valueStringBuilder = new StringBuilder();
        valueStringBuilder.append("[controlType: ");
        final QSizePolicy.ControlType controlType = sizePolicy.controlType();
        if (controlType==null){
            valueStringBuilder.append("null");
        }else{
            valueStringBuilder.append(controlType.name());
        }
        valueStringBuilder.append(", expandingDirections: ");
        final Qt.Orientations orientation = sizePolicy.expandingDirections();
        if (orientation==null){
            valueStringBuilder.append("null");
        }else{
            valueStringBuilder.append('[');                
            if (orientation.isSet(Qt.Orientation.Horizontal)){
                valueStringBuilder.append("Horizontal");
                if (orientation.isSet(Qt.Orientation.Vertical)){
                    valueStringBuilder.append(", ");
                }
            }
            if (orientation.isSet(Qt.Orientation.Vertical)){
                valueStringBuilder.append("Vertical");
            }
            valueStringBuilder.append(']');
        }
        valueStringBuilder.append(", hasHeightForWidth: ");
        valueStringBuilder.append(String.valueOf(sizePolicy.hasHeightForWidth()));
        valueStringBuilder.append(", hasWidthForHeight: ");
        valueStringBuilder.append(String.valueOf(sizePolicy.hasWidthForHeight()));
        valueStringBuilder.append(", horizontalPolicy: ");
        valueStringBuilder.append(sizePolicyToString(sizePolicy.horizontalPolicy()));
        valueStringBuilder.append(", horizontalStretch: ");
        valueStringBuilder.append(String.valueOf(sizePolicy.horizontalStretch()));
        valueStringBuilder.append(", verticalPolicy: ");
        valueStringBuilder.append(sizePolicyToString(sizePolicy.verticalPolicy()));
        valueStringBuilder.append(", verticalStretch: ");
        valueStringBuilder.append(String.valueOf(sizePolicy.verticalStretch()));
        return valueStringBuilder.toString();
    }
    
    @SuppressWarnings("unchecked")
    private static String qFlagsToString(final QFlags qFlags){
        Class<?> flagClass = (Class)((ParameterizedType) qFlags.getClass()
                            .getGenericSuperclass()).getActualTypeArguments()[0];
        final Method valuesMethod;
        try {
            valuesMethod=flagClass.getMethod("values");
        }catch(NoSuchMethodException exception){
            return qFlags.toString();
        }
        final Object[] allFlags;
        try{
            allFlags = (Object[])valuesMethod.invoke(null);
        }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException exception){
            return qFlags.toString();
        }
        final StringBuilder flagsStringBuilder = new StringBuilder();
        flagsStringBuilder.append('[');
        boolean first = true;
        for (Object flag: allFlags){
            if (qFlags.isSet((QtEnumerator)flag)){
                if (first){
                    first = false;
                }else{
                    flagsStringBuilder.append(", ");
                }
                flagsStringBuilder.append(String.valueOf(flag));
            }        
        }
        flagsStringBuilder.append(']');
        return flagsStringBuilder.toString();
    }
    
    private static String value2String(final Object value){
        if (value==null){
            return "";
        }else if (value instanceof QSize){
            final QSize size = (QSize)value;
            return "[width: "+size.width()+", height: "+size.height()+"]";
        }else if (value instanceof QRect){
            final QRect rect = (QRect)value;
            return "[x: "+rect.x()+", y: "+rect.y()+", width: "+rect.width()+", height: "+rect.height()+"]";
        }else if (value instanceof QPoint){
            final QPoint point = (QPoint)value;
            return "[x: "+point.x()+", y: "+point.y()+"]";
        }else if (value instanceof QSizePolicy){
            return sizePolicyToString((QSizePolicy)value);            
        }else if (value instanceof QColor){
            return ((QColor)value).name();
        }else if (value instanceof QBrush){
            return ((QBrush)value).color().name();
        }else if (value instanceof QFlags){
            return qFlagsToString((QFlags)value);
        }else{
            return String.valueOf(value);
        }
    }
    
    public String getValue(final String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String value = propertyValues==null ? null : propertyValues.get(name);
        if (value==null){
            final Object rawValue = getProps().getValue(name, uiElement);           
            value = value2String(rawValue);
            if (propertyValues==null){
                propertyValues = new HashMap<>();                
            }
            propertyValues.put(name, value);
        }
        return value;
    }
    
}
