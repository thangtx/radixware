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
package org.radixware.kernel.radixdoc.xmlexport;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.dialogs.chooseobject.ISelectableObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.w3c.dom.Node;

/**
 *
 * @author dlastochkin
 */
public interface IExportableXmlSchema extends ISelectableObject{
    
    public Id getId();   
    
    public Collection<IExportableXmlSchema> getLinkedSchemas();
    
    public InputStream getDefinitionInputStream();
    
    public InputStream getInputStream();
    
    public Map<Id, String> getLocalizedStrings(final EIsoLanguage lang);
    
    public void processEnumerations(Node node);
    
    public Collection<Id> getEnumIds();
    
    public XmlObject findKernelSchema(String name, String namespace);
}
