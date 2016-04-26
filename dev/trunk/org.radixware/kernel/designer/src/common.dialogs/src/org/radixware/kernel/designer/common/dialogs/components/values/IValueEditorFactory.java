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

/*
 * 10/14/11 4:47 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import javax.swing.JComponent;


public interface IValueEditorFactory<TEditor extends ICompositeEditor> {

    interface IFeature<TEditor> {
        JComponent install(TEditor editor);
        void enabled(boolean on);
        void checkCondition();
        void invoke();
    }

    boolean assembly();

    void buildDefaultEditor();
}