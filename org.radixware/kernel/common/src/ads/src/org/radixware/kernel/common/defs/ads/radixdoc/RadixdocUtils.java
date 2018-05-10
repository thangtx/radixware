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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.text.MessageFormat;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.radixdoc.IReferenceResolver;

public class RadixdocUtils {

    public static String resolveXsdNode(Definition context, TypeDocument.Entry typeDocEntry, String schemaRef) {
        String[] nameParts = typeDocEntry.getString().split(":");
        if (nameParts.length < 2) {
            return schemaRef;
        }

        String type = nameParts[nameParts.length - 1].endsWith(".Factory") ? nameParts[nameParts.length - 1].substring(0, nameParts[nameParts.length - 1].lastIndexOf(".Factory")) : nameParts[nameParts.length - 1];
        String nodeRef = type.replace(".", "_");
        
        return schemaRef + (nodeRef.length() == 0 ? "" : IReferenceResolver.PATH_DELIMITER + nodeRef.toString());
    }
}
