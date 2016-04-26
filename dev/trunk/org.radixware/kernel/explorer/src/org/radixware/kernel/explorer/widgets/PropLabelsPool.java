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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.gui.QWidget;
import java.util.Stack;
import org.radixware.kernel.common.client.models.items.properties.Property;


public final class PropLabelsPool {

    private static final PropLabelsPool INSTANCE = new PropLabelsPool();
    private static final int MAX_POOL_SIZE = 64;
    
    private final Stack<PropLabel> pool = new Stack<>();
    
    private PropLabelsPool(){
    }
    
    public static PropLabelsPool getInstance(){
        return INSTANCE;
    }
    
    public PropLabel getPropLabel(final QWidget parent, final Property property){
        final PropLabel label;
        if (pool.isEmpty()){
            label = new PropLabel(parent);
            label.setProperty(property);            
        }else{
            label = pool.pop();
            label.setInCache(false);
            if (parent!=null){
                label.setParent(parent);
                label.setVisible(true);
            }            
            label.setProperty(property);
        }
        return label;
    }
    
    public void cachePropLabel(final PropLabel propLabel){        
        if (pool.size()<MAX_POOL_SIZE){
            propLabel.release();
            pool.push(propLabel);
            propLabel.setInCache(true);
        }
    }
    
    public void clear() {        
        for (PropLabel propLabel: pool){
            propLabel.close();
            propLabel.setInCache(false);
            propLabel.dispose();
        }
        pool.clear();
    }    
}
