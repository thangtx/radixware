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

package org.radixware.kernel.common.client.models;

import java.lang.reflect.InvocationTargetException;
import org.radixware.schemas.eas.SelectRs;


public abstract class GroupModelDataSource {
    
    public interface Handler{
        void cancel();
    }
    
    public static abstract class Listener{
        
        public abstract void onDataReceived(final GroupModelData data);
        
        public void onException(final InvocationTargetException exception){};
    }
    
    private final GroupModel groupModel;
    private final SelectResponseParser parser;
    
    public GroupModelDataSource(final GroupModel groupModel){
        this.groupModel = groupModel;
        parser = new SelectResponseParser(groupModel);
    }
    
    public abstract Handler waitForData(final int startIndex, final int rowCount, final boolean withSelectorAddons, final Listener listener);
    
    protected final GroupModel getGroupModel(){
        return groupModel;
    }
    
    protected final GroupModelData parseResponse(final SelectRs response){
        return parser.parse(response);
    }
}
