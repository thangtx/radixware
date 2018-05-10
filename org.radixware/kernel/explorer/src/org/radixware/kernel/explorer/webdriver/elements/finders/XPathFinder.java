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

package org.radixware.kernel.explorer.webdriver.elements.finders;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper.DocumentWrapper;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.w3c.dom.NodeList;

final class XPathFinder extends UIElementFinder{
    
    private final XPathExpression xpathExpr;
    
    public XPathFinder(final String selector) throws WebDrvException{
        final XPathFactory xPathfactory = XPathFactory.newInstance();
        final XPath xpath = xPathfactory.newXPath();
        try{
            xpathExpr = xpath.compile(selector);
        }catch(XPathExpressionException exception){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, exception.getMessage());
        }
    }

    @Override
    public Collection<UIElementReference> findElements(QWidget startFrom, boolean greedy, WebDrvUIElementsManager manager) throws WebDrvException {
        final DocumentWrapper doc = new DocumentWrapper(startFrom);
        final NodeList nl;
        try{
            nl = (NodeList) xpathExpr.evaluate(doc, XPathConstants.NODESET);
        }catch(XPathExpressionException exception){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, exception.getMessage());
        }
        if (nl.getLength()==0){
            return Collections.emptyList();
        }else{
            Collection<UIElementReference> result = new LinkedList<>();
            UIElementReference reference;
            for (int i=0,count=nl.getLength(); i<count; i++){
                reference = DocumentWrapper.getElementReference(nl.item(i), manager);
                if (reference!=null){
                    result.add(reference);
                }
            }
            return result;
        }        
    }

    @Override
    protected boolean isTarget(QAction action) {
        return false;
    }

    @Override
    protected boolean isTarget(QWidget widget) {
        return false;
    }

    @Override
    protected boolean isTargetRow(QAbstractItemModel model, QModelIndex index) {
        return false;
    }

    @Override
    protected boolean isTargetCell(QAbstractItemModel model, QModelIndex index) {
        return false;
    }

    @Override
    protected boolean isTargetTabButton(QTabBar tabSet, int index) {
        return false;
    }        
}
