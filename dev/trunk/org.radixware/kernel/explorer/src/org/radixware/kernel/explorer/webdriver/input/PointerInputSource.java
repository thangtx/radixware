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

package org.radixware.kernel.explorer.webdriver.input;

import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.WebDrvJsonUtils;
import org.radixware.kernel.explorer.webdriver.elements.UIElementId;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.InvalidPropertyValueTypeException;
import org.radixware.kernel.explorer.webdriver.exceptions.NullPropertyValueException;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class PointerInputSource extends InputSource{
    
    private final PointerInputSourceState state = new PointerInputSourceState();

    public PointerInputSource(final InputSourceId id){
        super(id);
    }

    @Override
    public InputAction parseAction(JSONObject actionItem) throws WebDrvException {
        final String type = WebDrvJsonUtils.getStringProperty(actionItem, "type");
        switch(type){
            case "pause":{
                return new NullAction<>(this, WebDrvJsonUtils.getNonNegativeInteger(actionItem, "duration", false));
            }
            case "pointerUp":{
                return new PointerUpAction(this, WebDrvJsonUtils.getNonNegativeInteger(actionItem, "button", true));
            }
            case "pointerDown":{
                return new PointerDownAction(this, WebDrvJsonUtils.getNonNegativeInteger(actionItem, "button", true));
            }
            case "pointerMove":{                
                final Object originValue = actionItem.get("origin");
                final PointerMoveAction.EOrigin origin;
                final UIElementId elementId;
                if (originValue==null){
                    throw new NullPropertyValueException("origin");
                }else if (originValue instanceof String){
                    elementId = null;
                    if ("viewport".equals(originValue)){
                        origin = PointerMoveAction.EOrigin.VIEWPORT;                        
                    }else if ("pointer".equals(originValue)){
                        origin = PointerMoveAction.EOrigin.POINTER;
                    }else{
                        throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown origin \'"+originValue+"\'");
                    }
                }else if (originValue instanceof JSONObject){
                    final Object elementIdValue = ((JSONObject)originValue).get(UIElementId.JSON_KEY);
                    if (elementIdValue==null){
                        throw new NullPropertyValueException(UIElementId.JSON_KEY);
                    }
                    if (elementIdValue instanceof String){
                        origin = PointerMoveAction.EOrigin.ELEMENT;
                        elementId = UIElementId.fromString((String)elementIdValue);
                    }
                    else throw new InvalidPropertyValueTypeException(UIElementId.JSON_KEY, elementIdValue);
                }else{
                    throw new InvalidPropertyValueTypeException("origin", originValue);
                }
                final Integer x;
                final Object xvalue = actionItem.get("x");
                if (xvalue==null){
                    x = null;
                }else if (xvalue instanceof Long){
                    x = ((Long)xvalue).intValue();
                }else{
                    throw new InvalidPropertyValueTypeException("x", xvalue);
                }
                final Integer y;
                final Object yvalue = actionItem.get("y");
                if (yvalue==null){
                    y = null;
                }else if (yvalue instanceof Long){
                    y = ((Long)yvalue).intValue();
                }else{
                    throw new InvalidPropertyValueTypeException("y", yvalue);
                }
                final Integer duration = WebDrvJsonUtils.getNonNegativeInteger(actionItem, "duration", false);
                if (elementId==null){
                    return new PointerMoveAction(this, origin, x, y, duration);
                }else{
                    return new PointerMoveAction(this, elementId, x, y, duration);
                }
            }case "pointerCancel":{
                return new PointerCancelAction(this);
            }default:{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown action type \'"+type+"\'");
            }         
        }
    }

    @Override
    public PointerInputSourceState getState() {
        return state;
    }   
}
