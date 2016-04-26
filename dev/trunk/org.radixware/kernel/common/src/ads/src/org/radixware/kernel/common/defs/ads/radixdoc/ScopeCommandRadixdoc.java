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
package org.radixware.kernel.common.defs.ads.radixdoc;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class ScopeCommandRadixdoc extends CommandRadixdoc {

    public ScopeCommandRadixdoc(AdsScopeCommandDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeCommandDefInfo(Table geneticAttrTable) {
        AdsScopeCommandDef scopeComm = (AdsScopeCommandDef) source;
        getWriter().addAllStrRow(geneticAttrTable, "Command accessibility", scopeComm.getPresentation().getAccessebility().getName());
    }
}
