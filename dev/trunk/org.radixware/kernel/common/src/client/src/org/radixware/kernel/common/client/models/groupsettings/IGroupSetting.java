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

package org.radixware.kernel.common.client.models.groupsettings;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Id;


public interface IGroupSetting {

    boolean isUserDefined();

    boolean isValid();

    boolean hasAncestor();

    boolean wasModified();

    String getName();

    void setName(final String name);

    boolean hasTitle();

    String getTitle();
    
    Icon getIcon();

    Id getId();
    public static int APPLY_SETTING_RESULT = 2;

    DialogResult openEditor(final IClientEnvironment environment, final IWidget parent, final java.util.Collection<String> restrictedNames, final boolean showApplyButton);
}
