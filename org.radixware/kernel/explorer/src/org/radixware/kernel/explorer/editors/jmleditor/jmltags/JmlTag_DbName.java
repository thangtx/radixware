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
import org.radixware.kernel.common.jml.JmlTagDbName;


public class JmlTag_DbName extends JmlTag {

    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_JML/JML_DB_NAME";

    private JmlTag_DbName(final IClientEnvironment environment, final JmlTag_DbName source) {
        super(environment, source);
    }

    @Override
    public JmlTag_DbName copy() {
        final JmlTag_DbName res = new JmlTag_DbName(environment, this);
        res.tag = this.tag;
        return res;
    }

    public JmlTag_DbName(final IClientEnvironment environment, final JmlTagDbName tag, final long pos,final boolean isDeprecated, final EDefinitionDisplayMode showMode) {
        super(environment, pos, isDeprecated);
        this.tag = tag;
        setDisplayedInfo(tag.getToolTip(), tag.getDisplayName(), showMode);
    }

   @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        String name = fullName;
        if ((EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) && (name.indexOf("::") != -1)) {
            final String s = name.substring(0, name.indexOf('[') + 1);
            name = getNameWithoutModule(name);
            setDisplayedInfo(null, s + name);
        } else {
            setDisplayedInfo(null, name);
        }
        return true;
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}