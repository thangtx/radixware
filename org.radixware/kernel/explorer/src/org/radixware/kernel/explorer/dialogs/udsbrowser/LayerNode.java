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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;


final class LayerNode extends AbstractBranchNode{
    
    private final static ClientIcon LAYER_CLIENT_ICON = new ClientIcon("classpath:images/layer.svg"){
    };
    private final static QIcon LAYER_MODULE_ICON = ExplorerIcon.getQIcon(LAYER_CLIENT_ICON);    
    
    private final List<UdsModuleNode> udsModules = new LinkedList<>();
    private final String uri;
    private final String name;
    private final String title;
    
    public LayerNode(final IRepositoryEntry entry, 
                               final String uri,
                               final String name,
                               final String title,
                               final List<UdsModuleNode> udsModules){
        super(entry);
        this.udsModules.addAll(udsModules);
        this.uri = uri;
        this.name = name;
        this.title = title;
    }
    
    public List<UdsModuleNode> getUdsModules(){
        return Collections.unmodifiableList(udsModules);
    }
    
    public String getUri(){
        return uri;
    }
    
    public String getName(){
        return name;
    }

    @Override
    QIcon getIcon() {
        return LAYER_MODULE_ICON;
    }

    @Override
    String getTitle() {
        return title==null || title.isEmpty() ? name : title;
    }        

    @Override
    String getDescription() {
        return "";
    }
}
