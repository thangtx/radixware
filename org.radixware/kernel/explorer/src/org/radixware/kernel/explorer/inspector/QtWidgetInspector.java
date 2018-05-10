/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.QtPropertyReader;
import com.trolltech.qt.QtPropertyWriter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.internal.QtJambiObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.inspector.delegates.ErrorInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.InspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QCursorInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QFontInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QLayoutInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QMarginsInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QPaletteInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QPointInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QRectInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QRegionInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QSizeInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QSizePolicyInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QSizePolicyStretchInspectorDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QWindowOpacityDelegate;
import org.radixware.kernel.explorer.inspector.delegates.QtInspectorDelegate;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class QtWidgetInspector extends WidgetInspector<QWidget> {

    private static volatile QtWidgetInspector instance;
    private IClientEnvironment environment;

    public static QtWidgetInspector getInstance(final IClientEnvironment environment) {
        if (instance == null) {
            instance = new QtWidgetInspector(environment);
        }
        return instance;
    }

    private static String getShortMethodName(final String oldString) {
        if (oldString.substring(0, 3).equals("get") || oldString.substring(0, 3).equals("set")) {
            return oldString.substring(3, oldString.length());
        } else {
            return oldString;
        }
    }

    private QtWidgetInspector(final IClientEnvironment environment) {
        this.environment = environment;
        registerDelegate(new QtInspectorDelegate());
        registerDelegate(new QSizeInspectorDelegate(environment));
        registerDelegate(new QRectInspectorDelegate(environment));
        registerDelegate(new QCursorInspectorDelegate());
        registerDelegate(new QPointInspectorDelegate(environment));
        registerDelegate(new QSizePolicyInspectorDelegate(environment));
        registerDelegate(new QRegionInspectorDelegate(environment));
        registerDelegate(new QFontInspectorDelegate());
        registerDelegate(new QPaletteInspectorDelegate());
        registerDelegate(new QMarginsInspectorDelegate());
        registerDelegate(new QSizePolicyStretchInspectorDelegate());
        registerDelegate(new QWindowOpacityDelegate());
        registerDelegate(new ErrorInspectorDelegate());
        try {
            registerDelegate(QWidget.class.getMethod("layout", (Class<?>[]) null), new QLayoutInspectorDelegate(environment));
        } catch (NoSuchMethodException | SecurityException ex) {
            environment.getTracer().error(ex);
        }
    }

    @Override
    public WidgetInfo<QWidget> selectWidget(final QObject parent) {
        WidgetSelector wdgSelector = new WidgetSelector(parent);
        QWidget selectedWdgt = wdgSelector.select();
        if (selectedWdgt == null) {
            return null;
        }
        return new WidgetInfo<>(instance, selectedWdgt, selectedWdgt.getClass().getSimpleName());
    }

    @Override
    public List<QWidget> getChildWidgets(QWidget parent) {
        List<QWidget> children = new LinkedList<>();
        for (QObject obj : parent.children()) {
            if (obj instanceof QWidget) {
                children.add((QWidget) obj);
            }
        }
        return children;
    }

    @Override
    public QWidget getParentWidget(QWidget child) {
        for (QObject parent = child.parent(); parent != null; parent = parent.parent()) {
            if (parent instanceof QWidget) {
                return (QWidget) parent;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WidgetProperty> getWidgetProperties(QWidget widget) {
        boolean isReadOnly;
        Method setterMethod;
        List<WidgetProperty> widgetPropList = new LinkedList<>();
        Class<? extends InspectorDelegate> delegate;
        InspectorDelegate inspectorDelegateInstance;

        for (Class obj = widget.getClass(); obj != QtJambiObject.class; obj = obj.getSuperclass()) {
            for (Method f : obj.getDeclaredMethods()) {
                inspectorDelegateInstance = null;
                delegate = null;
                isReadOnly = true;
                setterMethod = null;
                Inspectable isInspectable = f.getAnnotation(Inspectable.class);
                if (isInspectable != null) { //check if method has @Inspactable annotation
                    if (!isInspectable.delegate().isEmpty()) {
                        try {
                            delegate = (Class<InspectorDelegate>) Class.forName(isInspectable.delegate());
                        } catch (ClassNotFoundException ex) {
                            environment.getTracer().error(ex);
                        }
                    } else { //if delegate() is empty check on method returnType
                        Inspectable isReturnTypeInspactable = f.getReturnType().getAnnotation(Inspectable.class);
                        if (isReturnTypeInspactable != null) {
                            try {
                                delegate = (Class<InspectorDelegate>) Class.forName(isReturnTypeInspactable.delegate());
                            } catch (ClassNotFoundException ex) {
                                environment.getTracer().error(ex);
                            }
                        }
                    }

                    if (delegate != null) {
                        try {
                            Constructor<?>[] delegateConstructors = delegate.getClass().getConstructors();
                            for (Constructor<?> constructor : delegateConstructors) {
                                if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(IClientEnvironment.class)) {
                                    try {
                                        inspectorDelegateInstance = (InspectorDelegate) constructor.newInstance(environment);
                                    } catch (IllegalArgumentException | InvocationTargetException ex) {
                                        environment.getTracer().error(ex);
                                    }
                                } else if (constructor.getParameterTypes().length == 0) {
                                    try {
                                        inspectorDelegateInstance = (InspectorDelegate) constructor.newInstance();
                                    } catch (IllegalArgumentException | InvocationTargetException ex) {
                                        environment.getTracer().error(ex);
                                    }
                                }
                            }
                            inspectorDelegateInstance = delegate.newInstance();
                        } catch (InstantiationException | IllegalAccessException ex) {
                            environment.getTracer().error(ex);
                        }
                    } else {
                        inspectorDelegateInstance = findDelegateForMethod(f);
                        if (inspectorDelegateInstance == null) {
                            inspectorDelegateInstance = findDelegateForClass(f.getReturnType());
                        }
                    }

                    if (!isInspectable.name().isEmpty() && inspectorDelegateInstance != null) {
                        for (Method g : obj.getDeclaredMethods()) { //check if property has setter
                            InspectablePropertySetter hasSetter = g.getAnnotation(InspectablePropertySetter.class);
                            if (hasSetter != null) {
                                if (isInspectable.name().equals(hasSetter.name())) {
                                    isReadOnly = false;
                                    setterMethod = g;
                                    break;
                                }
                            }
                        }
                    }

                    if (inspectorDelegateInstance != null) {
                        try {
                            WidgetProperty wdgProp = new WidgetProperty(!isInspectable.name().isEmpty() ? isInspectable.name() : getShortMethodName(f.getName()), f.invoke(widget, (Object[]) null), obj.getSimpleName(), inspectorDelegateInstance, f.getReturnType(), isReadOnly, setterMethod);
                            widgetPropList.add(wdgProp);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            WidgetProperty wdgProp = new WidgetProperty(!isInspectable.name().isEmpty() ? isInspectable.name() : getShortMethodName(f.getName()), ex, obj.getSimpleName(), new ErrorInspectorDelegate(), Error.class, true, null);
                            widgetPropList.add(wdgProp);
                        }
                    }
                } else {
                    QtPropertyReader qpr = f.getAnnotation(QtPropertyReader.class);
                    if (qpr != null) {
                        try {
                            for (Method g : obj.getDeclaredMethods()) {
                                QtPropertyWriter qpw = g.getAnnotation(QtPropertyWriter.class);
                                if (qpw != null) {
                                    if (qpw.name().equals(qpr.name())) {
                                        inspectorDelegateInstance = findDelegateForMethod(g);
                                        if (inspectorDelegateInstance == null) {
                                            inspectorDelegateInstance = findDelegateForClass(g.getParameterTypes()[0]);
                                        }

                                        if (inspectorDelegateInstance != null) {
                                            isReadOnly = false;
                                            setterMethod = g;
                                        }
                                        break;
                                    }
                                } else {
                                    inspectorDelegateInstance = findDelegateForClass(f.getReturnType());
                                    isReadOnly = true;
                                    setterMethod = null;
                                }
                            }
                            WidgetProperty wdgProp = new WidgetProperty(qpr.name(), f.invoke(widget, (Object[]) null), obj.getSimpleName(), inspectorDelegateInstance, f.getReturnType(), isReadOnly, setterMethod);
                            widgetPropList.add(wdgProp);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            WidgetProperty wdgProp = new WidgetProperty(qpr.name(), ex, obj.getSimpleName(), new ErrorInspectorDelegate(), Error.class, true, null);
                            widgetPropList.add(wdgProp);
                        }
                    } else {
                        inspectorDelegateInstance = findDelegateForMethod(f);
                        if (inspectorDelegateInstance != null) {
                            try {
                                WidgetProperty wdgProp = new WidgetProperty(f.getName(), f.invoke(widget, (Object[]) null), obj.getSimpleName(), inspectorDelegateInstance, f.getReturnType(), true, null);
                                widgetPropList.add(wdgProp);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                WidgetProperty wdgProp = new WidgetProperty(f.getName(), ex, obj.getSimpleName(), new ErrorInspectorDelegate(), Error.class, true, null);
                                widgetPropList.add(wdgProp);
                            }
                        }
                    }
                }
            }
            if (obj.equals(QObject.class)) {
                return widgetPropList;
            }
        }
        return null;
    }

    @Override
    public boolean isExists(QWidget widget) {
        return widget != null && widget.nativeId() != 0;
    }

    @Override
    public boolean isVisible(QWidget widget) {
        return widget.isVisible();
    }

    @Override
    public String getWidgetXPath(final QWidget widget) {
        return isExists(widget) ? WidgetUtils.calcWidgetXPath(widget) : "";
    }

    @Override
    public boolean setWidgetProperty(QWidget widget, Method setterMehtod, Object value) {
        try {
            setterMehtod.invoke(widget, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            environment.processException(environment.getMessageProvider().translate("inspector", "Fail to set property value using method ") + setterMehtod.toString(), ex);
        }
        return true;
    }

    @Override
    public List<WidgetInfo<QWidget>> getTopLevelWidgets() {
        List<WidgetInfo<QWidget>> topLevelInfoWidgetItemList = new LinkedList<>();
        for (QWidget topLvlWidget : QApplication.topLevelWidgets()) {
            if (!(topLvlWidget instanceof InspectorDialog) 
                && topLvlWidget != null 
                && topLvlWidget.nativeId()!=0 
                && topLvlWidget.parent()==null) {
                topLevelInfoWidgetItemList.add((new WidgetInfo<>(instance, topLvlWidget, topLvlWidget.getClass().getSimpleName())));
            }
        }
        return topLevelInfoWidgetItemList;
    }

    @Override
    public String getDescription(QWidget widget) {
        if (widget == null) {
            return null;
        }
        String description;
        description = widget.getClass().getSimpleName().isEmpty() ? widget.parent().getClass().getSimpleName() + "$1" : widget.getClass().getSimpleName();
        if (!widget.objectName().isEmpty()) {
            description += " (" + widget.objectName() + ")";
        }
        return description;
    }

    public void setEvnironment(IClientEnvironment environment) {
        this.environment = environment;
    }

}
