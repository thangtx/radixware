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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.explorer.macros.actions.IMacroAction;


public class Macros {

    private final List<IMacroAction> macroActions = new ArrayList<IMacroAction>();
    private String name;

    public Macros(final String name, final List<IMacroAction> actions){
        this.name = name;
        if (actions!=null)
            macroActions.addAll(actions);
    }

    public List<IMacroAction> getActions(){
        return Collections.unmodifiableList(macroActions);
    }

    public void addAction(final IMacroAction action){
        macroActions.add(action);
    }

    public String getName(){
        return name;
    }
}
