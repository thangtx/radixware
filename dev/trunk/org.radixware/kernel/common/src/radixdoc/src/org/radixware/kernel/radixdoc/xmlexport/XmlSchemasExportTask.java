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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;

public class XmlSchemasExportTask {

    private final Collection<IExportableXmlSchema> exportingSchemas;
    
    private final boolean useExtendedNames;
    private final boolean embeddedDoc;
    private final boolean saveLinkledSchemas;
    private final boolean exportDoc;
    private final boolean zipSchemas;
    
    private final List<EIsoLanguage> targetLangs;
    private final List<EIsoLanguage> embeddedDocLangs;
    private final File targetDir;        

    public XmlSchemasExportTask(Collection<IExportableXmlSchema> exportingSchemas, boolean useExtendedNames, boolean embeddedDoc, boolean saveLinkledSchemas, boolean zipSchemas, boolean exportDoc, List<EIsoLanguage> targetLangs, List<EIsoLanguage> embeddedDocLangs, File targetDir) {
        this.exportingSchemas = exportingSchemas;
        this.useExtendedNames = useExtendedNames;
        this.embeddedDoc = embeddedDoc;
        this.saveLinkledSchemas = saveLinkledSchemas;
        this.zipSchemas = zipSchemas;
        this.exportDoc = exportDoc;
        this.targetLangs = targetLangs;
        this.embeddedDocLangs = embeddedDocLangs;
        this.targetDir = targetDir;        
    }

    public List<IExportableXmlSchema> getExportingSchemas() {
        return new ArrayList<>(exportingSchemas);
    }

    public boolean isEmbeddedDoc() {
        return embeddedDoc;
    }

    public boolean isSaveLinkledSchemas() {
        return saveLinkledSchemas;
    }

    public boolean isZipSchemas() {
        return zipSchemas;
    }

    public boolean isExportDoc() {
        return exportDoc;
    }

    public boolean isUseExtendedNames() {
        return useExtendedNames;
    }
    
    public List<EIsoLanguage> getTargetLangs() {
        return new ArrayList(targetLangs);
    }

    public List<EIsoLanguage> getEmbeddedDocLangs() {
        return embeddedDocLangs;
    }

    public File getTargetDir() {
        return targetDir;
    }

}
