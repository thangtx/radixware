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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.QSize;
import java.util.Set;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;


interface IStrOptionsContainer {
    IEditMaskEditorSetting getSetting(EEditMaskOption option);
    void setVisibleOptions(Set<EEditMaskOption> options);
    void setHiddenOptions(Set<EEditMaskOption> options);
    void setEnabledOptions(Set<EEditMaskOption> options);
    void setDisabledOptions(Set<EEditMaskOption> options);
    void setMinimumSize(QSize size);
    void addToXml(org.radixware.schemas.editmask.EditMask editMask);
    void loadFromXml(org.radixware.schemas.editmask.EditMask editMask);
    void show();
    void hide();
    boolean checkOptions();
}
