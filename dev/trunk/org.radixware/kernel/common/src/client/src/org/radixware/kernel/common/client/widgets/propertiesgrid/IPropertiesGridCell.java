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

package org.radixware.kernel.common.client.widgets.propertiesgrid;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;


interface IPropertiesGridCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> extends IPropertiesGridPresenter.IPresenterItem<L,E,G>{

    public enum ELinkageDirection{
        LEFT,RIGHT,TOP,BOTTOM
    }
    
    boolean isEmpty();
    boolean isVisible();
    boolean isLinked(ELinkageDirection direction);
    IPropertiesGridCell<L, E, G> getLinkedCell(ELinkageDirection direction);
    void linkWith(IPropertiesGridCell<L, E, G> cell, ELinkageDirection direction);    
    int getLinkedCellsCount(ELinkageDirection direction);
    String getDescription(final MessageProvider mp);    
    IPropertiesGridCell<L,E, G> createCopy();
    void close(IPropertiesGridPresenter<L, E, G> presenter);
}
