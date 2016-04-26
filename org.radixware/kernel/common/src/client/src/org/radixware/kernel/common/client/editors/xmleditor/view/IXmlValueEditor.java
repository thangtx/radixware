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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;


public interface IXmlValueEditor<T> {

    public static interface ValueListener {

        void valueChanged(final String newValue);
    }

    public static interface WidgetListener<T> {

        void widgetChanged(final T newWidget);
    }

    String getValue();

    void setValue(final String value);

    void setReadOnly(final boolean isReadOnly);

    void addValueListener(final ValueListener listener);

    void removeValueListener(final ValueListener listener);

    void clearValueListeners();

    void addWidgetListener(final WidgetListener<T> listener);

    void removeWidgetListener(final WidgetListener<T> listener);

    void clearWidgetListeners();

    XmlModelItem getModelItem();

    T asWidget();
}