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

package org.radixware.kernel.common.client.editors.xmleditor.view.operations;

import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;


public abstract class XmlOperation {
    
    final int[] parentItemIndexPath;
    
    public XmlOperation(final int[] parentItemIndexPath){
        this.parentItemIndexPath = new int[parentItemIndexPath.length];
        System.arraycopy(parentItemIndexPath, 0, this.parentItemIndexPath, 0, parentItemIndexPath.length);
    }
    
    public final int[] getParentItemIndexPath(){
        final int[] parItemIndexPath = new int[parentItemIndexPath.length];
        System.arraycopy(parentItemIndexPath, 0, parItemIndexPath, 0, parentItemIndexPath.length);
        return parItemIndexPath;
    }
    
    public abstract boolean needToReinitParent();
    
    public abstract boolean merge(final XmlOperation other);
    
    public abstract XmlOperation execute(final IXmlTree tree);
}
