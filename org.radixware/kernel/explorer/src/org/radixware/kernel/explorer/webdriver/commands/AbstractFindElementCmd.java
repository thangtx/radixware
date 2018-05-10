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

import java.util.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvJsonUtils;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.finders.EElementLocationStrategy;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

abstract class AbstractFindElementCmd extends GuiCmd{

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    protected abstract boolean isGreedlySearch();
    
    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        final EElementLocationStrategy locationStrategy = EElementLocationStrategy.getForValue(WebDrvJsonUtils.getStringProperty(parameter, "using"));
        final String selector = WebDrvJsonUtils.getStringProperty(parameter, "value");
        final Collection<UIElementReference> references = 
            session.getUIElements().findElements(locationStrategy, selector, uri.getElementId(), isGreedlySearch());
        // http://www.w3.org/TR/webdriver/#find-elements
        // если ничего не нашли, то 
        // если искали множество элементов, то множество найденных элементов может быть пустым,
        // а если ищем один конкретный, то кидаем NO_SUCH_ELEMENT.
        if (isGreedlySearch()){
            final JSONArray refsList = new JSONArray();
            for (UIElementReference ref: references){
                ref.writeToJsonList(refsList);
            }
            return new WebDrvSessionCommandResult(refsList);
        }

        if (references.isEmpty()){
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ELEMENT);
        }
        else if (references.size()==1){
            return references.iterator().next().asJson();
        }else{
            // тут бы хорошо давать не просто первый попавшийся элемент
            // а первый, с которым можно что-то сделать (кликнуть и т.д.)
            // , т.е. не спрятанный, не на фоновом окне и т.д.
            for (UIElementReference ref: references){
                if(ref.isInteractiveNow()) return ref.asJson();
            }
            return references.iterator().next().asJson();
        }
    }    
}
