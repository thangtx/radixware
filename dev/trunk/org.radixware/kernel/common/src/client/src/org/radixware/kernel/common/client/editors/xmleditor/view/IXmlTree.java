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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import java.awt.Color;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;


public interface IXmlTree {
    
    public interface IChangeCurrentItemListener{
        void currentItemChanged(final IXmlTreeItem currentItem);
    }
    
    IXmlTreeItem getCurrentItem();
    /**
     *
     * @param rootItem
     * @return
     */
    IXmlTreeItem createRootItem(XmlDocumentItem rootItem, XmlSchemaItem schemaItem, XmlEditorController controller, final boolean conform);
    IXmlTreeItem addChildItem(final IXmlTreeItem parentItem, final XmlModelItem childXmlItem, final int index);
    void setColumns(final String[] titles);
    void addChangeCurrentItemListener(IChangeCurrentItemListener listener); 
    void setSelectedItem(final IXmlTreeItem currentItem);
    IXmlTreeItem findByIndexPath(int[] indexPath, boolean nearest);
    IXmlTreeItem getRootItem();
    IClientEnvironment getEnvironment();
    XmlTreeController getTreeController();
    void setBackgroundColor(final Color color);
    void setUpdatesEnabled(final boolean enabled);
}
