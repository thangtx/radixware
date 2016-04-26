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

package org.radixware.kernel.common.client.editors.xmleditor;

import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.widgets.IWidget;


public interface IXmlEditor extends IWidget{
    
    void setValue(XmlObject value);
    XmlObject getValue();
    
    void setReadOnly(boolean isReadOnly);
    boolean isReadOnly();
    
    void setDisabledOperations(EnumSet<XmlEditorOperation> operations);
    boolean isOperationDisabled(XmlEditorOperation operation);
    
    boolean valueWasModified();
    List<XmlError> validate(final boolean isVisible);
    boolean close();
}
