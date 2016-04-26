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

package org.radixware.kernel.designer.common.dialogs.sqlscript;

import javax.swing.text.JTextComponent;
import org.openide.util.Lookup;


public interface ISQLScriptProvider {

    String getScript();

    /**
     * @return object corresponding to the line
     * at given line of the script
     */
    Object getObjectAtLine(int line);
    
    /**
     * Activates component.
     * This method must only
     * show the component which owns the object, there is no restrictions
     * to position at which it will be opened.
     */
    void activateObject(Object component);

    /**
     * @return line number in object which is
     * corresponding to the given line in sql script
     */
    int convertScriptLineToObjectLine(final Object object, int line);
    
    JTextComponent getObjectTextComponent(final Object object);
    
    void setPosition(Object object, int absLine, int absColumn);

    public static interface Factory {

        ISQLScriptProvider create(Lookup context);
    }
}
