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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QModelIndex;
import org.radixware.kernel.common.defs.ads.AdsDefinition;


public interface IChooseDefFromList {

    void onItemClick(QModelIndex modelIndex);
    void onItemDoubleClick(QModelIndex modelIndex);
    boolean setCurItem(QModelIndex modelIndex);
    boolean setSelectedDefinition(AdsDefinition def);
    //public  void open(Collection<DefInfo> defList);
}
