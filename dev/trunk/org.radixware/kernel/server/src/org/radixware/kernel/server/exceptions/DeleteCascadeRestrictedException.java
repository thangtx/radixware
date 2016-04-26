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

package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.common.types.Id;


public class DeleteCascadeRestrictedException extends RadixPublishedException {    
    private static final long serialVersionUID = -5298283119252391747L;

    private final Id subobjectEntityId;
    private final String childEntityTitle;
    
    public DeleteCascadeRestrictedException(final String mess, final Id subobjectEntityId){
        this(mess,subobjectEntityId==null ? null : subobjectEntityId.toString(),subobjectEntityId);
    }

    public DeleteCascadeRestrictedException(final String mess, final String childTitle, final Id subobjectEntityId){
        super(mess);
        this.subobjectEntityId = subobjectEntityId;
        childEntityTitle = childTitle;
    }

    public final Id getSubobjectEntityId(){
            return subobjectEntityId;
    }

    public final String getChildEntityTitle(){
        return childEntityTitle;
    }
}
