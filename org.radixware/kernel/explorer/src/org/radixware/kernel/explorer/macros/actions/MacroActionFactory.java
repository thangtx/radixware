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
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.explorer.macros.widgets.QWidgetPath;


public final class MacroActionFactory implements IMacroActionFactory{

    private final List<IMacroActionFactory> factories = new LinkedList<IMacroActionFactory>();

    private MacroActionFactory(){
        factories.add(ExplorerTreeMouseClickActionFactory.getInstance());
        factories.add(MouseActionFactory.getInstance());
    }

    private final static MacroActionFactory INSTANCE = new MacroActionFactory();

    public static MacroActionFactory getInstance(){
        return INSTANCE;
    }

    public IMacroAction createAction(final QEvent event, final QWidget targetWidget) {
        IMacroAction action = null;
        for (IMacroActionFactory factory: factories){
            action = factory.createAction(event, targetWidget);
            if (action!=null){
                break;
            }
        }
        return action;
    }

}
