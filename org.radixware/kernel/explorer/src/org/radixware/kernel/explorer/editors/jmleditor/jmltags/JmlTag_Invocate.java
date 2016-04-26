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

package org.radixware.kernel.explorer.editors.jmleditor.jmltags;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.jml.JmlTagInvocation;


public class JmlTag_Invocate extends JmlTag {

    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_INVOCATE";

    private JmlTag_Invocate(final IClientEnvironment environment, final JmlTag_Invocate source) {
        super(environment, source);
    }

    @Override
    public JmlTag_Invocate copy() {
        final JmlTag_Invocate res = new JmlTag_Invocate(environment, this);
        res.tag = tag;
        return res;
    }

    public JmlTag_Invocate(final IClientEnvironment environment, final JmlTagInvocation tag, final long pos, final boolean isDeprecated, final EDefinitionDisplayMode showMode) {
        super(environment, pos, isDeprecated);
        this.tag = tag;
        setDisplayedInfo(tag.getToolTip(environment.getLanguage()), tag.getDisplayName(), showMode);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (((JmlTagInvocation)tag).getDefinition() instanceof AdsUserFuncDef) {
            setDisplayedInfo(null, fullName);
            return true;
        } else {
            return super.setDisplayedInfo(showMode);
        }
    }
}
