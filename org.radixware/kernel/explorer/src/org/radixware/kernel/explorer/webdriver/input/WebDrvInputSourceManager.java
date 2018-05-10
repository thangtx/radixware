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

import com.trolltech.qt.core.Qt;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public final class WebDrvInputSourceManager {                    
    
    private final Map<InputSourceId, InputSource> inputSourceById = new HashMap<>(4);    
    private final List<InputAction> cancelActions = new LinkedList<>();
    private final WebDrvSession session;    
    
    public WebDrvInputSourceManager(final WebDrvSession session){
        this.session = session;
    }
    
    InputSource findInputSourceById(final InputSourceId id){
        return inputSourceById.get(id);
    } 
    
    InputSource newInputSource(final InputSource.Type type, final InputSourceId id){
        final InputSource inputSource;
        switch(type){
            case NONE:
                inputSource = new NullInputSource(id);
                break;
            case KEY:
                inputSource = new KeyInputSource(id);
                break;
            case POINTER:
                inputSource = new PointerInputSource(id);
                break;
            default:
                throw new IllegalArgumentException("Unknown input source type "+type.name());
        }
        inputSourceById.put(id, inputSource);
        return inputSource;        
    }
    
    void addCancelAction(final InputAction action){
        cancelActions.add(action);
    }
    
    private static void appendKeyboardModifiers(final Qt.KeyboardModifiers from, Qt.KeyboardModifiers to){
        for (Qt.KeyboardModifier modifier: Qt.KeyboardModifier.values()){
            if (from.isSet(modifier)){
                to.set(modifier);
            }
        }
    }
    
    Qt.KeyboardModifiers getKeyboardModifiers(){
        final Qt.KeyboardModifiers allModifiers = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier);        
        Qt.KeyboardModifiers modifiers;
        for (InputSource inputSource: inputSourceById.values()){
            if (inputSource instanceof KeyInputSource){
                modifiers = ((KeyInputSource)inputSource).getState().getModifiers();
                appendKeyboardModifiers(modifiers, allModifiers);
            }
        }
        return allModifiers;
    }
    
    private void dispatch(final InputActions actions, final int duration) throws WebDrvException{
        final ActionsDispatcher dispatcher = new ActionsDispatcher(session, actions);
        dispatcher.dispatch(duration);
        dispatcher.disposeLater();
    }
    
    public void dispatch(final InputActions actions) throws WebDrvException{
        dispatch(actions, -1);
    }
    
    public void reset() throws WebDrvException{
        try{
            if (!cancelActions.isEmpty()){
                dispatch(InputActions.fromCancelActions(cancelActions), 0);
            }
        }finally{
            cancelActions.clear();
            inputSourceById.clear();
        }
    }
}
