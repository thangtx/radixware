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

package org.radixware.kernel.designer.common.dialogs.components.description;

import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizingStringContext;


public interface IDescriptionHandleInfo extends ILocalizingStringContext {

    EDescriptionType getDescriptionType();

    String getStringDescription();

    boolean setStringDescription(String description);

    boolean localize(EIsoLanguage language);

    boolean isLocalizable();

    IDescribable getDescribable();

    ILocalizedDescribable getLocalizedDescribable();
}
