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

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Document;

final class QActionWrapper extends UIElementWrapper{
    
    private final QAction action;
    private final QWidget owner;
    private final UIElementPropertySet props;
    
    public QActionWrapper(final QWidget owner, final QAction action, final BaseNodeWrapper parent, final Document document){
        super("QAction", parent, document);
        this.action = action;
        this.owner = owner;
        props = new UIElementPropertySet(action);
    }

    @Override
    public List<UIElementWrapper> getChildElements() {        
        final QMenu menu = action.menu();
        final List<QAction> childActions = menu==null ? null : menu.actions();
        if (childActions==null || childActions.isEmpty()){
            return Collections.emptyList();
        }
        final List<UIElementWrapper> children = new ArrayList<>();
        for (QAction childAction: childActions){
            if (childAction.nativeId()!=0){
                children.add(new QActionWrapper(owner, childAction, this, getOwnerDocument()));
            }
        }
        return children;
    }

    @Override
    public UIElementPropertySet getPropertySet() {
        return props;
    }        

    @Override
    public UIElementReference getElementReference(WebDrvUIElementsManager manager) {
        return manager.getActionReference(owner, action);
    }   
}
