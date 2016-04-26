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
 * 10/18/11 1:38 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;


public interface IContextableValueEditorModel<TValue, TContext> extends IValueEditorModel<TValue> {

    void open(TContext context);

    void open(TContext context, TValue value);
    
    boolean isOpened();

    TContext getContext();

    void commit();

    void updateFromContext();
}