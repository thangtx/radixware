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

import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;


public interface IXmlTreeItem {
    void setModelValue(int column, Object value, boolean isReadOnly);
    void setTextValue(int column, Object value);
    void refresh();
    XmlModelItem getXmlItem();
    IXmlTreeItem getNextSiblingItem();
    IXmlTreeItem getPreviousSiblingItem();
    IXmlTreeItem getParentItem();
    IXmlTreeItem getChildItem(int index);
    int getChildCount();
    int getIndexInParent();
    void expand();
    void delete();
    void reinitChildren();
    int[] getIndexPath();
    boolean isExpanded();
    boolean isReadOnly();
}
