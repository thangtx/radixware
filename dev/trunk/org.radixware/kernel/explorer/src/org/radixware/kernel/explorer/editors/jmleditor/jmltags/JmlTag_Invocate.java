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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QMouseEvent;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.utils.DialogUtils;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.types.Id;


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
    
    @Override
    public void onMouseReleased(QMouseEvent e, IClientEnvironment env) {
        //ctrl + click, event was checked in XscmlEditor
        Id tableId = null;
        String pidAsStr = null;
        if (tag instanceof JmlTagInvocation) {
            final JmlTagInvocation invTag = (JmlTagInvocation) tag;
            final List<Id> path = invTag.getPath().asList();
                if (path.size() == 1 && path.get(0).getPrefix() == EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
                    tableId = AdsUserFuncDef.LIB_USER_FUNC_TABLE;
                    pidAsStr = path.get(0).toString();
                }
        }
        if (tableId != null && pidAsStr != null) {
            try {
                DialogUtils.showEntityEditor(tableId, pidAsStr, env);
            } catch (InterruptedException | ServiceClientException ex) {
                final String cause;
                if (ex instanceof ObjectNotFoundError) {
                    cause = "Object not found:\n" + ex.getMessage();
                } else {
                    cause = ex.getClass().getName() + "\n" + ex.getMessage();
                }
                environment.messageError("Can not open editor dialog. " + cause);
            }
        }
    }

    @Override
    public Qt.CursorShape getCursorShape(QMouseEvent e) {
        Id tableId = null;
        String pidAsStr = null;
        if (tag instanceof JmlTagInvocation) {
            final JmlTagInvocation invTag = (JmlTagInvocation) tag;
            final List<Id> path = invTag.getPath().asList();
                if (path.size() == 1 && path.get(0).getPrefix() == EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
                    tableId = AdsUserFuncDef.LIB_USER_FUNC_TABLE;
                    pidAsStr = path.get(0).toString();
                }
        }
        return tableId != null && pidAsStr != null ? Qt.CursorShape.PointingHandCursor : null;
    }
    
}
