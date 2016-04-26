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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.enums.EIsoLanguage;


interface ILocalizedEditor {
    static final String CONTENT = "content";

    EIsoLanguage getLanguage();

    void setLanguage(EIsoLanguage language);

    void setReadonly(boolean readonly);

    void addChangeListener(ChangeListener l);

    void removeChangeListener(ChangeListener l);

    void setText(String text);

    String getText();

}
