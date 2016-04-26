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

package org.radixware.wps.views;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.wps.rwt.UIObject;


class EmbeddedViewsFinder implements UIObject.Visitor{
    
    private List<IEmbeddedView> views = new LinkedList<IEmbeddedView>();
    private final UIObject rootObject;
    
    public EmbeddedViewsFinder(UIObject startObject){
        this.rootObject = startObject;
    }

    @Override
    public void accept(final UIObject obj) {
        if (obj instanceof IEmbeddedView){
            for (UIObject parent = obj.getParent(); parent!=rootObject && parent!=null; parent=parent.getParent()){
                if (parent instanceof IEmbeddedView && views.contains((IEmbeddedView)parent)){
                    return;
                }
            }
            views.add((IEmbeddedView)obj);
        }
    }
    
    public List<IEmbeddedView> getEmbeddedViews(){
        return Collections.<IEmbeddedView>unmodifiableList(views);
    }
    
}
