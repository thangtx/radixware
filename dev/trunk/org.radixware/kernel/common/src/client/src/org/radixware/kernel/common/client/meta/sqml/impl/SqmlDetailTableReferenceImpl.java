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

package org.radixware.kernel.common.client.meta.sqml.impl;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDetailTableReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.types.Id;


final class SqmlDetailTableReferenceImpl extends SqmlReferenceImpl implements ISqmlDetailTableReference{

    public SqmlDetailTableReferenceImpl(final IClientEnvironment environment, final DdsReferenceDef refDef) {
        super(environment, refDef);
    }
    
    @Override
    public String getTitle() {
        final ISqmlTableDef table = findReferencedTable();
        if (table==null){
            return getReferenceDef().getName();
        }else{
            final String template = environment.getMessageProvider().translate("SqmlEditor", "%1$s details");
            return String.format(template, table.getTitle());
        }
    }

    @Override
    public Id getReferencedTableId() {
        return getReferenceDef().getChildTableId();
    }
    
}