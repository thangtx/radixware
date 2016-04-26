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

package org.radixware.kernel.designer.api;

import javax.swing.JComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.editors.components.IStatableEditor;

/**
 * Base interface for Radix object api editors. All api editors are registered
 * in layer.xml files.
 *
 */
public interface IApiEditor<T extends RadixObject> extends IStatableEditor {

    /**
     * Update api editor content and select object that specified by
     * openInfo.getTarget().
     */
    void open(OpenMode mode, ApiFilter filter, RadixObject subTarget);
    
    void open(OpenMode mode, ApiFilter filter);
    
    boolean select(RadixObject subTarget);
    
    /**
     * Update api editor, called each time when focused.
     */
    void update();

    JComponent getComponent();

    T getSource();

    boolean isEmbedded();
}
