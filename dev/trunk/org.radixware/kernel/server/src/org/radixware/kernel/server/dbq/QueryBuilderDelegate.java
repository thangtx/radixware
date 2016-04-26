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

package org.radixware.kernel.server.dbq;

import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;


public class QueryBuilderDelegate {

    public boolean appendOrdColDirective(final StringBuilder query, final SqlBuilder builder, final Id propId, final boolean bForSelectParent, final EOrder order) {
        return false;
    }
    
    public boolean appendConditionFrom(final StringBuilder query, final SqlBuilder builder, final RadConditionDef condition) {
        return false;
    }
}
