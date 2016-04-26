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

import org.radixware.kernel.common.types.Id;


final class Messages {

    private Messages() {
    }

    static final Id MLS_OWNER_ID = Id.Factory.loadFrom("adcXCB5KK6HMJH7NP6E642OHPOMXY");
    static final Id MLS_ID_DBQ_UNIQUE_CONSTRAINT_VIOLATION_IDX = Id.Factory.loadFrom("mlsCIOYOOJHBVBBPLPG4EDXUTXEPM");
    static final Id MLS_ID_DBQ_UNIQUE_CONSTRAINT_VIOLATION = Id.Factory.loadFrom("mlsKT7XSPDEFVADXORET2BRXX56Q4"/*"mlsCIOYOOJHBVBBPLPG4EDXUTXEPM"*/); //"Unique constraint %1$s violated", Db Error    
    static final Id MLS_ID_DBQ_NOT_NULL_CONSTRAINT_VIOLATION = Id.Factory.loadFrom("mlsQJRYFH3I35FR3BN5VSJS2CKHTQ");
    
    static final Id MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION = Id.Factory.loadFrom("mlsD4PARGABCRFRVHLJX5UIVRE43E");
    static final Id MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_SEVERAL_COLUMNS = Id.Factory.loadFrom("mls5NVNHZMNSREF5HEBPJL6SMHFEQ");
    static final Id MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_CHILD_REF = Id.Factory.loadFrom("mlsYU6E6A3LIVELJN3Q55NBMGYF5A");
    static final Id MLS_ID_DBQ_INTEGRITY_CONSTRAINT_VIOLATION_NO_PARENT_RECORD = Id.Factory.loadFrom("mls55W3HLSN75CZPIY5QFWKYQOKPQ");
}
