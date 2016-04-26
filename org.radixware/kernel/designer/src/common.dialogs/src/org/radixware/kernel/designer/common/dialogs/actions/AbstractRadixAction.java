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

package org.radixware.kernel.designer.common.dialogs.actions;

import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;



public abstract class AbstractRadixAction extends AbstractAction implements Presenter.Toolbar, Presenter.Popup {

    public static final String ICON_BASE = "iconBase";
    public static final String POPUP_TEXT = "popupText";

    public AbstractRadixAction(String name) {
        super(name);
        putValue(SHORT_DESCRIPTION, NbBundle.getMessage(this.getClass(), RadixNbEditorUtils.createTooltipKey(name)));
        putValue(AbstractRadixAction.POPUP_TEXT, NbBundle.getMessage(this.getClass(), RadixNbEditorUtils.createTitleKey(name)));
    }

    public AbstractRadixAction() {
    }

    @Override
    public Component getToolbarPresenter() {
        return RadixNbEditorUtils.createToolbarButton(this, getMimeTypeForSettings());
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return RadixNbEditorUtils.createPopupMenuItem(this, getMimeTypeForSettings());
    }

    /**
     * Points where to find keyboard shortcuts to this action
     * @return
     */
    protected String getMimeTypeForSettings() {
        return null;
    }
}
