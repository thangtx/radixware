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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.WebDrvJsonUtils;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.InvalidPropertyValueTypeException;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public final class InputActions {    
    
    private final List<List<InputAction>> actionsByTick;
    
    private InputActions(final List<List<InputAction>> actions){
        actionsByTick = actions;
    }        

    private int getTickDuration(final int tick){
        final List<InputAction> actions = actionsByTick.get(tick);
        int duration = 0;
        for (InputAction action: actions){
            if (action instanceof IPauseAction){
                duration = Math.max(duration, ((IPauseAction)action).getDuration());
            }
        }
        return duration;                
    }
    
    void dispatch(final ActionsDispatcher dispatcher, 
                  final WebDrvSession session, 
                  final int tick,
                  final int duration) throws WebDrvException{
        final long startTick = System.currentTimeMillis();
        final int tickDuration = duration<0 ? getTickDuration(tick) : duration;
        for (InputAction action: actionsByTick.get(tick)){
            action.dispatch(session, tickDuration, startTick);
        }
        long tickProcessTime = System.currentTimeMillis() - startTick;
        long pause = tickDuration - tickProcessTime;        
        if (tick+1<actionsByTick.size()){
            dispatcher.dispatchTick(tick+1, (int)pause, -1);
        }else{
            dispatcher.stop();
        }        
    }

    public static InputActions parse(final JSONObject json, final WebDrvInputSourceManager manager) throws WebDrvException{
        final List<List<InputAction>> actionsByTick = new LinkedList<>();
        final JSONArray actions = WebDrvJsonUtils.getArray(json, "actions");
        for (Object actionSequence: actions){
            if (actionSequence instanceof JSONObject){
                actionsByTick.add(parseInputSourceActionSequence((JSONObject)actionSequence, manager));
            }else{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "action sequence must be json object");
            }
        }
        return new InputActions(actionsByTick);
    }        
    
    static InputActions fromCancelActions(final List<InputAction> cancelActions){
        final List<InputAction> actions = new ArrayList<>(cancelActions);
        Collections.reverse(actions);
        return new InputActions(Collections.<List<InputAction>>singletonList(actions));
    }
    
    private static List<InputAction> parseInputSourceActionSequence(final JSONObject json, final WebDrvInputSourceManager manager) throws WebDrvException{
        final String typeValue = WebDrvJsonUtils.getStringProperty(json, "type");
        final InputSource.Type type = InputSource.Type.fromString(typeValue);
        final String idValue = WebDrvJsonUtils.getStringProperty(json, "id");
        final InputSourceId inputSourceId = InputSourceId.fromString(idValue);
        InputSource inputSource = manager.findInputSourceById(inputSourceId);
        if (inputSource==null){
            inputSource = manager.newInputSource(type, inputSourceId);
        }
        if (type==InputSource.Type.POINTER){
            validatePointerParameters(json.get("parameters"));
        }
        final JSONArray actionItems = WebDrvJsonUtils.getArray(json, "actions");
        final List<InputAction> actions = new LinkedList<>();
        for (Object actionItem: actionItems){
            if (actionItem instanceof JSONObject){
                actions.add(inputSource.parseAction((JSONObject)actionItem));
            }else{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "action item must be json object");
            }
        }
        return actions;
    }
    
    private static void validatePointerParameters(final Object parametersObj) throws WebDrvException{
        if (parametersObj!=null){
            if (parametersObj instanceof JSONObject){
                final Object pointerTypeObj = ((JSONObject)parametersObj).get("pointerType");
                if (pointerTypeObj instanceof String){
                    if (!"mouse".equals(pointerTypeObj)){
                        throw new WebDrvException(EWebDrvErrorCode.UNSUPPORTED_OPERATION, pointerTypeObj+" is not supported");
                    }
                }else{
                    throw new InvalidPropertyValueTypeException("pointerType", pointerTypeObj);
                }
            }else{
                throw new InvalidPropertyValueTypeException("parameters", parametersObj);
            }            
        }
    }    

}
