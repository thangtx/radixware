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

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.QSignalEmitter.Signal1;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.ConnectionType;
import com.trolltech.qt.gui.QApplication;
import java.util.List;
import org.radixware.kernel.explorer.macros.actions.IMacroAction;


public final class MacroRecorder extends QSignalEmitter {
    
    private MacroRecorder(){
        super();
    }

    private static final MacroRecorder INSTANCE = new MacroRecorder();
    public final Signal1<IMacroAction> newAction = new Signal1<IMacroAction>();

    public static MacroRecorder getInstance(){
        return INSTANCE;
    }

    private Macros currentMacros;

    public void startRecord(final Macros macros){
        if (macros==null)
            throw new NullPointerException();
        currentMacros = macros;
        EventListener.getInstance().newAction.connect(this,"onNewAction(IMacroAction)", ConnectionType.QueuedConnection);
        QApplication.instance().installEventFilter(EventListener.getInstance());
    }

    @SuppressWarnings("unused")
    private void onNewAction(final IMacroAction action){       
        newAction.emit(action);
        currentMacros.addAction(action);
    }

    public void stopRecord(){
        EventListener.getInstance().newAction.disconnect();
        QApplication.instance().removeEventFilter(EventListener.getInstance());        
    }  
}
