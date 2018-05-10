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

import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;

public class DefinitionDocInfo {

    private final Id moduleId;
    private final String moduleName;
    private final String modulePath;
    private final String layerUri;
    private final String layerVersion;
    private final String ownLayerName;

    private String baseLayerName;

    private final AbstractDefDocItem docItem;

    public DefinitionDocInfo(Id moduleId, String moduleName, String modulePath, String layerUri, String layerVersion, String layerName, AbstractDefDocItem docItem) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.modulePath = modulePath;
        this.layerUri = layerUri;
        this.layerVersion = layerVersion;
        this.baseLayerName = this.ownLayerName = layerName;
        
        this.docItem = docItem;
    }

    public Id getModuleId() {
        return moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModulePath() {
        return modulePath;
    }

    public String getLayerUri() {
        return layerUri;
    }

    public AbstractDefDocItem getDocItem() {
        return docItem;
    }

    public String getLayerVersion() {
        return layerVersion;
    }

    public String getOwnLayerName() {
        return ownLayerName;
    }

    public String getBaseLayerName() {
        return baseLayerName;
    }

    public void setBaseLayerName(String baseLayerName) {
        this.baseLayerName = baseLayerName;
    }

    public String getQualifiedName() {
        return getQualifiedName(false);
    }
    
    public String getQualifiedName(boolean useBaseLayerName) {
        String layerName = useBaseLayerName ? baseLayerName : ownLayerName;
        return layerName + ":" + moduleName + ":" + docItem.getName();
    }
}
