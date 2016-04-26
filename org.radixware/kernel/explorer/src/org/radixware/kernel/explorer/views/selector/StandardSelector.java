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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.widgets.selector.SelectorGrid;
import org.radixware.kernel.explorer.widgets.selector.SelectorGridModel;


public class StandardSelector extends Selector implements ICachableWidget{
    
    private static class CreateSelectorGrid extends QEvent{
        
        public final GroupModel groupModel;
        
        public CreateSelectorGrid(final GroupModel groupModel){
            super(QEvent.Type.User);
            this.groupModel = groupModel;
        }
    }
    
    private SelectorGrid selectorGrid;
    private boolean inCache;

    public StandardSelector(IClientEnvironment environment) {
        super(environment);
    }

    @Override
    public void open(Model model_) {
        Application.getInstance().getStandardViewsFactory().blockScheduledViewCreation();
        try{
            QApplication.postEvent(this, new CreateSelectorGrid((GroupModel)model_));
            super.open(model_);        
            if (selectorGrid==null){
                createSelectorGrid((GroupModel)model_);
            }
            setSelectorWidget(selectorGrid);
            notifyOpened();
        }finally{
            Application.getInstance().getStandardViewsFactory().unblockScheduledViewCreation();
        }
    }   
    
    private boolean selectorGridRecursionBlock;
    
    private void createSelectorGrid(final GroupModel group){
        if (selectorGrid==null && !selectorGridRecursionBlock){
            selectorGridRecursionBlock = true;
            try{
                final SelectorGridModel gridModel = new SelectorGridModel(group);
                selectorGrid = new SelectorGrid(this, gridModel);
                final QVBoxLayout layout = new QVBoxLayout(content);
                layout.setSpacing(0);
                layout.setMargin(0);
                layout.addWidget(selectorGrid);
                selectorGrid.setVisible(true);
                selectorGrid.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            }finally{
                selectorGridRecursionBlock = false;
            }
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof CreateSelectorGrid){
            event.accept();
            createSelectorGrid(((CreateSelectorGrid)event).groupModel);
        }else{
            super.customEvent(event);
        }
    }
    
    public void setInCache(final boolean inCache){
        this.inCache = inCache;
    }

    @Override
    public boolean isInCache() {
        return inCache;
    }   
}
