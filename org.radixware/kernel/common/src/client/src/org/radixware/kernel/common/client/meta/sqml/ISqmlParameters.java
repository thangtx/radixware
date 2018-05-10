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

package org.radixware.kernel.common.client.meta.sqml;

import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Id;


public interface ISqmlParameters{

    int size();

    ISqmlParameter getParameterById(Id parameterId);

    ISqmlParameter get(int index);

    List<ISqmlParameter> getAll();

    ISqmlModifiableParameter createNewParameter(IClientEnvironment environment, IWidget parent);

    int addParameter(ISqmlModifiableParameter parameter);

    boolean removeParameter(int index);
    
    boolean swapParameters(int index1, int index2);

    DialogResult openParameterEditor(IClientEnvironment environment, ISqmlParameter parameter, boolean readonly, IWidget parent);        
    
    boolean isReadonly();   
    
    XmlObject exportToXml();
    
    boolean importFromXml(XmlObject xml, IClientEnvironment environment);
}