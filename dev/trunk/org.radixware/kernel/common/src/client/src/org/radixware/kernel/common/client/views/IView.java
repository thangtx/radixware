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

package org.radixware.kernel.common.client.views;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.AdsPublication;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public interface IView extends IWidget {

    public interface IViewListener<T extends IView> {

        public void opened(T view);

        public void closed(T view);
    }
    
    public interface Visitor{
        public void visit(IEmbeddedView embeddedView);
        public boolean cancelled();
    }

    public void open(Model model);

    public void setFocus();

    public boolean setFocusedProperty(Id id);
        
    public boolean close(boolean forced);
    
    public boolean canSafelyClose(CleanModelController cleanController);

    public void finishEdit();

    public Model getModel();

    public void reread() throws ServiceClientException;

    public boolean hasUI();

    //widget hierarchy navigation
    public IWidget getParentWindow();

    public IView findParentView();

    public boolean isDisabled();

    public IClientEnvironment getEnvironment();
    
    public void visitChildren(Visitor visitor, boolean recursively);
    
    @AdsPublication("mth3US7P4Q25JDD3BEILVHMJOO5AU")
    public ViewRestrictions getRestrictions();
}
