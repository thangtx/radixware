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

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.AdsPublication;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IUIObject;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public interface IEmbeddedView extends IModifableComponent, IModelWidget, IUIObject, IWidget {

    public boolean isOpened();

    public void open() throws ServiceClientException, InterruptedException;

    public boolean close(final boolean forced);

    public boolean close();
    
    public void reread();    
    
    public boolean setFocus(Property property);

    public boolean isHidden();

    public void setUpdatesEnabled(boolean enabled);

    public void setExplorerItem(Model parentModel, Id explorerItemId);

    public void bind();
    
    @Override
    public Model getModel();

    public IView getView();
    
    public IEmbeddedViewContext getViewContext();
    
    public boolean isSynchronizedWithParentView();
    
    public void setSynchronizedWithParentView(boolean isSynchronized);
    
    @AdsPublication("mthTEICXWIEKFA7BKGYLFGWDPP7WA")
    public void setReadOnly(final boolean isReadOnly);
    
    @AdsPublication("mthN4XZOFOWVZGPBEGQ73XEDMPWEE")
    public boolean isReadOnly();
    
    @AdsPublication("mthBVW7ZKR6JJATDLYAWMLFHZCDPY")
    public ViewRestrictions getRestrictions();
}
