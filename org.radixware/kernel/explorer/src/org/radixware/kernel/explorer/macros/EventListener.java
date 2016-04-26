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

package org.radixware.kernel.explorer.macros;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.macros.actions.IMacroAction;
import org.radixware.kernel.explorer.macros.actions.MacroActionFactory;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


final class EventListener extends QObject{

    public final Signal1<IMacroAction> newAction = new Signal1<IMacroAction>();

    private EventListener(){
        super();
    }

    private final static EventListener INSTANCE = new EventListener();

    public static EventListener getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean eventFilter(final QObject targetObject, final QEvent event) {
        if (event!=null && targetObject instanceof QWidget){
            try{
                final IMacroAction action = MacroActionFactory.getInstance().createAction(event, (QWidget)targetObject);
                if (action!=null){
                    newAction.emit(action);
                    return action.ignoreEvent();
                }
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return false;
    }

    
}
