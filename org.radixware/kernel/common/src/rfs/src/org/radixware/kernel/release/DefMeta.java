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

package org.radixware.kernel.release;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.types.Id;


final class DefMeta {

    private String exClassName;
    private String metaClassName;
    private String srvFactoryClassName;
    private Id titleId;
    private String fileName;
    private List<Id> ancestorIds;
    private List<Id> domainIds;
    private String targetNamespaceUri;

    public void regAncestorId(final Id id) {
        if (ancestorIds == null) {
            ancestorIds = new ArrayList<>();
        }
        if (!ancestorIds.contains(id)) {
            ancestorIds.add(id);
        }
    }

    public void regDomainIds(final List<Id> idList) {
        if (domainIds == null) {
            domainIds = new ArrayList<>();
        }
        if (idList != null) {
            for (Id id : idList) {
                if (!domainIds.contains(id)) {
                    domainIds.add(id);
                }
            }
        }
    }

    public void setExClassName(String exClassName) {
        if (this.exClassName == null) {
            this.exClassName = exClassName;
        }
    }

    public void setFileName(String fileName) {
        if (this.fileName == null) {
            this.fileName = fileName;
        }
    }

    public void setMetaClassName(String metaClassName) {
        if (this.metaClassName == null) {
            this.metaClassName = metaClassName;
        }
    }

    public void setSrvFactoryClassName(String srvFactoryClassName) {
        if (this.srvFactoryClassName == null) {
            this.srvFactoryClassName = srvFactoryClassName;
        }
    }

    public void setTargetNamespaceUri(String targetNamespaceUri) {
        if (this.targetNamespaceUri == null) {
            this.targetNamespaceUri = targetNamespaceUri;
        }
    }

    public void setTitleId(Id titleId) {
        if (this.titleId == null) {
            this.titleId = titleId;
        }
    }

    public List<Id> getAncestorIds() {
        return ancestorIds;
    }

    public List<Id> getDomainIds() {
        return domainIds;
    }

    public String getExClassName() {
        return exClassName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMetaClassName() {
        return metaClassName;
    }

    public String getSrvFactoryClassName() {
        return srvFactoryClassName;
    }

    public String getTargetNamespaceUri() {
        return targetNamespaceUri;
    }

    public Id getTitleId() {
        return titleId;
    }
}
