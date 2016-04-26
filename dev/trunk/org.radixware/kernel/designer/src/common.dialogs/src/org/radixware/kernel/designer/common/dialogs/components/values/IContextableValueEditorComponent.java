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
 * 10/20/11 2:38 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;


public interface IContextableValueEditorComponent<TValue, TContext> extends IValueEditorComponent<TValue> {
        
    /**
     * Open editor in this context, with value currValue.
     * @param context current editing context
     * @param currValue current editing value
     */
    void open(TContext context, TValue currValue);

    /**
     * Open editor in this context.
     * @param context current editing context
     */
    void open(TContext context);

    /**
     * Indicates that editor was opened.
     * @return true if editor was opened, false otherwise
     */
    boolean isOpened();
    
    /**
     * Update value from current context.
     */
    void update();
    
    /**
     * Commits changes in the current context.
     */
    void commit();
}
