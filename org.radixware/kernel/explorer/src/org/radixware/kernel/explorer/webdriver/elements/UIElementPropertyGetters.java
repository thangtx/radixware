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
import com.trolltech.qt.QtPropertyReader;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontInfo;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QRegion;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.client.enums.EWidgetMarker;

final class UIElementPropertyGetters {
    
    private final static List<Class> PROPERTY_CLASSES = 
        Arrays.<Class>asList(String.class, Boolean.class, Integer.class, Long.class, QSize.class, QRect.class, QRegion.class,
                             QFont.class, QFontInfo.class, QPoint.class, QSizePolicy.class);
    private final static Map<Class,UIElementPropertyGetters> INSTANCES_BY_CLASS = new HashMap<>(256);
    private final static Object SEMAPHORE = new Object();
    private final static String IS_TOP_LEVEL_WINDOW_METHOD_NAME="isTopLevelWindow";
    private final static String GET_WIDGET_MARKER_METHOD_NAME="widgetMarker";
    
    private final Map<String,Method> propertyGetters = new HashMap<>(64);
    private List<String> orderedNames;
    
    private UIElementPropertyGetters(){
        propertyGetters.put("class", null);
    }
    
    private void collectProperties(final Class cl){
        if (Modifier.isPublic(cl.getModifiers())){
            final Method[] methods = cl.getDeclaredMethods();
            for (Method method: methods){
                if (isPropertyGetter(method)){
                    propertyGetters.put(getPropertyName(method), method);
                }
            }
        }
        if (cl==QWidget.class){
            propertyGetters.put(IS_TOP_LEVEL_WINDOW_METHOD_NAME, null);
        }
        final Class superCl = cl.getSuperclass();
        if (superCl!=null){
            final Method widgetMarker = propertyGetters.get(GET_WIDGET_MARKER_METHOD_NAME);
            propertyGetters.putAll(UIElementPropertyGetters.getInstance(superCl).propertyGetters);
            if (superCl==QWidget.class
                && !propertyGetters.containsKey(GET_WIDGET_MARKER_METHOD_NAME)){
                propertyGetters.put(GET_WIDGET_MARKER_METHOD_NAME, null);
            }else if (widgetMarker!=null){
                propertyGetters.put(GET_WIDGET_MARKER_METHOD_NAME, widgetMarker);
            }
        }
    }
        
    private static boolean isPropertyGetter(final Method method){        
        return Modifier.isPublic(method.getModifiers())
               && !Modifier.isStatic(method.getModifiers())
               && method.getParameterTypes().length==0
               && (isPropertyGetterMethodName(method.getName()) || method.getAnnotation(QtPropertyReader.class)!=null)
               && isPropertyType(method.getReturnType());
    }
    
    private static boolean isPropertyType(final Class cl){
        return cl.isPrimitive() || cl.isEnum() || PROPERTY_CLASSES.contains(cl) || QFlags.class.isAssignableFrom(cl);
    }
    
    private static boolean isPropertyGetterMethodName(final String name){
        return (name.startsWith("get") && name.length()>3)
               || (name.startsWith("is") && name.length()>2);
    }
    
    private static String getPropertyName(final Method method){
        final QtPropertyReader propertyReader = method.getAnnotation(QtPropertyReader.class);
        if (propertyReader==null){
            final String methodName = method.getName();
            if (methodName.startsWith("get")){
                return lowerCaseFirstChar(methodName.substring(3));
            }else{
                return lowerCaseFirstChar(methodName);
            }
        }else{
            return propertyReader.name();
        }
    }    
    
    private static String lowerCaseFirstChar(final String string){
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
    
    public int size(){
        return propertyGetters.size();
    }
    
    public Set<String> getNames(){
        return propertyGetters.keySet();
    }   
    
    public List<String> getOrderedNames(){
        if (orderedNames==null){
            orderedNames = new ArrayList<>(getNames());
            Collections.sort(orderedNames);
        }
        return orderedNames;
    }
    
    public boolean containsProperty(final String name){
        return propertyGetters.containsKey(name);
    }
    
    public Object getValue(final String name, final Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        if ("class".equals(name)){
            if (obj instanceof ItemViewCellInfo){
                return "Cell";
            }else if (obj instanceof ItemViewRowInfo){
                return "Row";
            }else if (obj instanceof HeaderViewSectionInfo){
                return "Section";
            } else if (obj instanceof TabButtonInfo){
                return "TabButton";
            } else{
                return obj.getClass().getName();
            }
        }else{
            if (obj instanceof QWidget
                && IS_TOP_LEVEL_WINDOW_METHOD_NAME.equals(name)){
                return obj==getTopLevelWindow();
            }else if (obj instanceof QLineEdit
                && "text".equals(name)
                && ((QLineEdit)obj).echoMode()!=QLineEdit.EchoMode.Normal){
                //do not allow to get text from password fields
                return propertyGetters.get("displayText").invoke(obj);
            }else if (GET_WIDGET_MARKER_METHOD_NAME.equals(name)
                         && propertyGetters.get(name)==null){
                return obj instanceof QAbstractButton ? EWidgetMarker.BUTTON : null;
            }else{
                return propertyGetters.get(name).invoke(obj);
            }
        }
    }
    
    private static QWidget getTopLevelWindow(){
        QWidget activeWindow = QApplication.activePopupWidget();        
        if (activeWindow==null){
            activeWindow = QApplication.activeModalWidget();
        }
        if (activeWindow==null){
            activeWindow=QApplication.activeWindow();
        }
        return activeWindow==null ? null : activeWindow.window();            
    }
    
    public String getPropertyType(final String name){
        return propertyGetters.get(name).getReturnType().getName();
    }
            
    public static UIElementPropertyGetters getInstance(final Class cl){
        synchronized(SEMAPHORE){
            UIElementPropertyGetters instance = INSTANCES_BY_CLASS.get(cl);
            if (instance==null){
                instance = new UIElementPropertyGetters();
                instance.collectProperties(cl);
                INSTANCES_BY_CLASS.put(cl, instance);
            }
            return instance;
        }
    }        
}
