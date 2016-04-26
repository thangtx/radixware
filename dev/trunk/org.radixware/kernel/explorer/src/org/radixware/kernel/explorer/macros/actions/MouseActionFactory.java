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

package org.radixware.kernel.explorer.macros.actions;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


final class MouseActionFactory implements IMacroActionFactory {

    private MouseActionFactory(){

    }

    private final static MouseActionFactory INSTANCE = new MouseActionFactory();

    public static MouseActionFactory getInstance(){
        return INSTANCE;
    }

    public IMacroAction createAction(final QEvent event, final QWidget targetWidget) {        
        if (targetWidget!=null && (event instanceof QMouseEvent) && event.type()!=QEvent.Type.MouseMove){
            final QWidgetPath path = QWidgetPath.Factory.newInstance(targetWidget);
            if (path!=null && !path.isSelf()){
                return new MouseAction((QMouseEvent)event, path);
            }
        }
        return null;
    }

}
