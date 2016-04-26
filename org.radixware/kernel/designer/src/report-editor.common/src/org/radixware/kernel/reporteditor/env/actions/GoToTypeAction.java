/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.reporteditor.env.actions;

import org.netbeans.api.jumpto.type.TypeBrowser;
import org.netbeans.spi.jumpto.type.TypeProvider;

/**
 *
 * @author akrylov
 */

public class GoToTypeAction extends org.netbeans.modules.jumpto.type.GoToTypeAction {

    public GoToTypeAction() {
    }

    public GoToTypeAction(String string, TypeBrowser.Filter filter, boolean bln, TypeProvider... tps) {
        super(string, filter, bln, tps);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
