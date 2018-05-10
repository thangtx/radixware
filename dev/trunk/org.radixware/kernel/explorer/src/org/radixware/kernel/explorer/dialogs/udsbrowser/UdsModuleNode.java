/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.udsbrowser;

import com.trolltech.qt.gui.QIcon;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;


final class UdsModuleNode extends AbstractBranchNode{
    
    private final static ClientIcon UDS_MODULE_CLIENT_ICON = new ClientIcon("classpath:images/uds_module.svg"){
    };
    private final static QIcon UDS_MODULE_ICON = ExplorerIcon.getQIcon(UDS_MODULE_CLIENT_ICON);

    private final Id id;
    private final String name;
    private final String description;        
    
    public UdsModuleNode(final IRepositoryEntry entry,
                                       final Id moduleId,
                                       final String name,
                                       final String description){
        super(entry);
        id = moduleId;
        this.name = name;
        this.description = description;
    }
    
    public Id getId(){
        return id;
    }

    @Override
    public QIcon getIcon() {
        return UDS_MODULE_ICON;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }    
}
