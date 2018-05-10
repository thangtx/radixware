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

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Document;

final class RootElement extends UIElementWrapper{
    
    private final QWidget widget;
    private final Document doc;
    
    public RootElement(final QWidget widget, final DocumentWrapper document){
        super("Root", document, document);
        this.widget = widget;
        this.doc = document;
    }

    @Override
    public List<UIElementWrapper> getChildElements() {
        if (widget==null){
            final List<UIElementWrapper> children = new ArrayList<>();
            final List<QWidget> widgets = QApplication.topLevelWidgets();        
            for (QWidget topLevelWidget: widgets){
                if (topLevelWidget!=null
                    && topLevelWidget.nativeId()!=0
                    && topLevelWidget.parent()==null
                    && (topLevelWidget instanceof ICachableWidget==false || !((ICachableWidget)topLevelWidget).isInCache())
                   ){
                   children.add(new QWidgetWrapper(topLevelWidget, this, doc));
                }
            }
            return children;
        }else{
            return QWidgetWrapper.getChildElements(widget, this, doc);
        }        
    }

    @Override
    public UIElementPropertySet getPropertySet() {
        return UIElementPropertySet.EMPTY;
    }

    @Override
    public UIElementReference getElementReference(WebDrvUIElementsManager manager) {
        return null;
    }        
}
