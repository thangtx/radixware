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
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.LocalizedStr_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class JmlTag_LocalizedString extends JmlTag {
     private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_LOCALIZED_STR";

    private JmlTag_LocalizedString(final IClientEnvironment environment, final JmlTag_LocalizedString source) {
        super(environment, source);
    }

    @Override
    public JmlTag_LocalizedString copy() {
        final JmlTag_LocalizedString res = new JmlTag_LocalizedString(environment, this);
        res.tag = this.tag;
        return res;
    }

    public JmlTag_LocalizedString(final IClientEnvironment environment, final JmlTagLocalizedString tag, final long pos, final EDefinitionDisplayMode showMode) {
        super(environment, pos, false);       
        this.tag = tag;

        setDisplayedInfo(tag.getToolTip(), tag.getDisplayName(), showMode);
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        final String name = fullName;
        setDisplayedInfo(null, name);
        return true;
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
     @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        JmlTagLocalizedString jmlTag=(JmlTagLocalizedString)tag;
        final LocalizedStr_Dialog dialog = new LocalizedStr_Dialog(editText, jmlTag, editText.getEnvironment().getMessageProvider().translate("SqmlEditor", "Edit Localized String"),(AdsUserFuncDef)jmlTag.getOwnerDefinition());
        if (dialog.exec() == 1) {
            jmlTag = dialog.getTag();
            jmlTag.setStringId(dialog.getLocalizedString().getId());
            setDisplayedInfo(null, jmlTag.getDisplayName());
            return true;
        }
        return false;
    }
}
