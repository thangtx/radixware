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

import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.EIsoLanguage;


public interface ILocalizingStringContext {

    void enableSpellcheck(boolean enable);

    boolean isSpellcheckEnable();

    String getValue(EIsoLanguage language);

    void setValue(EIsoLanguage language, String value);

    /**
     *
     * @return <tt>true</tt> if description is set, <tt>false</tt> otherwise
     */
    boolean hasValue();

    Definition getAdsDefinition();

    boolean isRemovable();
    
    void create();

    void remove();

    boolean commit();

    boolean isProxy();

    /**
     * Gets map of descriptions.
     * @return map of values if description is set, <tt>null</tt> otherwise
     */
    Map<EIsoLanguage, String> getValueMap();
    
    ILocalizedStringInfo getStringInfo();
}
