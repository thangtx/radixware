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

package org.radixware.kernel.server.types;

import org.radixware.kernel.common.types.Id;


public final class Messages {   
    public static final Id MLS_OWNER_ID = Id.Factory.loadFrom("adcWVZ2HLBXWNBZLFZT4EKNYASGKM");
    
    //PresentationEntityAdapter
    public static final Id MLS_ID_CONFIRM_TO_CLEAR_REFS_TO_OBJECT = Id.Factory.loadFrom("mls57IZ3IRS25C7XF4KYB46AGNUSM");//"Do you want to clear child references to this object: %1?"
    public static final Id MLS_ID_CONFIRM_TO_DELETE_SUBOBJECT = Id.Factory.loadFrom("mlsCWATYS6G4NHQDMP5ATUSND7G6E");//"Do you want to delete subobject: %1?"   
    public static final Id MLS_ID_CANT_DELETE_USED_OBJECT = Id.Factory.loadFrom("mls4MQJUCQPB5C2LJYOOCKLMSKW4Q");//"Can't delete the object because it is used by %1 object."
}
