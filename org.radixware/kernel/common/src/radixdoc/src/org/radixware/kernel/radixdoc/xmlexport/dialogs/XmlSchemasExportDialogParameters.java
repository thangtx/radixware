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
package org.radixware.kernel.radixdoc.xmlexport.dialogs;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.radixdoc.xmlexport.IExportableXmlSchema;

public class XmlSchemasExportDialogParameters {
    final Collection<IExportableXmlSchema> schemas;
    final List<EIsoLanguage> languages;
    final Set<String> topLayerVariants;

    public XmlSchemasExportDialogParameters(Collection<IExportableXmlSchema> schemas, List<EIsoLanguage> languages, Set<String> topLayerVariants) {
        this.schemas = schemas;
        this.languages = languages;
        this.topLayerVariants = topLayerVariants;
    }        
}
