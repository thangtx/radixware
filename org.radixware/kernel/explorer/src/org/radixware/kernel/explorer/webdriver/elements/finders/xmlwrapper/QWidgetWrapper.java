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

package org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.elements.HeaderViewSectionInfo;
import org.radixware.kernel.explorer.webdriver.elements.ItemViewRowInfo;
import org.radixware.kernel.explorer.webdriver.elements.TabButtonInfo;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Document;

final class QWidgetWrapper extends UIElementWrapper{
    
    private final QWidget widget;   
    private final UIElementPropertySet props;
    
    public QWidgetWrapper(final QWidget widget, final BaseNodeWrapper parent, final Document document){
        super(getTagName(widget), parent,document);
        this.widget = widget;
        props = new UIElementPropertySet(widget);
    }
    
    private static String getTagName(final QWidget widget){
        for (Class cl=widget.getClass(); cl!=null; cl=cl.getSuperclass()){
            if (!cl.isAnonymousClass() && !cl.getSimpleName().isEmpty()){
                return cl.getSimpleName();
            }
        }
        return "QWidget";
    }
    
    public static List<UIElementWrapper> getChildElements(final QWidget widget, final UIElementWrapper parent, final Document doc) {
        final List<UIElementWrapper> children = new ArrayList<>();
        final List<QAction> actions = widget.actions();
        for (QAction action: actions){
            if (action.nativeId()!=0){
                children.add(new QActionWrapper(widget, action, parent, doc));
            }
        }
        if (widget instanceof QHeaderView){
            final List<HeaderViewSectionInfo> sections = HeaderViewSectionInfo.getSections((QHeaderView)widget);
            for (HeaderViewSectionInfo section: sections){
                children.add(new QHeaderViewSectionWrapper(section, parent, doc));
            }
        }else if (widget instanceof QAbstractItemView){
            final List<ItemViewRowInfo> rows = ItemViewRowInfo.getTopLevelRows((QAbstractItemView)widget);
            for (ItemViewRowInfo row: rows){
                children.add(new QItemModelRowWrapper(row, parent, doc));
            }
        }else if (widget instanceof QTabBar){
            final QTabBar tabBar = (QTabBar)widget;
            for (int i=0,count=tabBar.count(); i<count; i++){
                children.add(new TabButtonWrapper(new TabButtonInfo(tabBar, i), parent, doc));
            }
        }
        final List<QObject> childObjects = widget.children();
        for (QObject object: childObjects){
            if (object instanceof QWidget && object.nativeId()!=0){
                children.add(new QWidgetWrapper((QWidget)object, parent, doc));
            }
        }
        return children;
    }
    
    @Override
    public List<UIElementWrapper> getChildElements() {
        return getChildElements(widget, this, getOwnerDocument());
    }

    @Override
    public UIElementPropertySet getPropertySet() {
        return props;
    }

    @Override
    public UIElementReference getElementReference(final WebDrvUIElementsManager manager) {
        return manager.getWidgetReference(widget);
    }        
}
