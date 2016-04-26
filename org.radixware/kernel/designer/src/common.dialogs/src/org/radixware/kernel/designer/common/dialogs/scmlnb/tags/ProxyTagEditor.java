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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.*;
import org.radixware.kernel.common.scml.Scml;

/**
 * TagEditPanel that delegates its functions to some ModalObjectEdtor.
 */
public class ProxyTagEditor<T extends Scml.Tag> extends TagEditor<T> {

    private ModalObjectEditor delegate;

    public ProxyTagEditor(ModalObjectEditor delegate) {
        this.delegate = delegate;
    }

    @Override
    protected boolean tagDefined() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void applyChanges() {
    }

    @Override
    protected void afterOpen() {
    }

    @Override
    public boolean showModal() {
        return delegate.showModal();
    }
}
