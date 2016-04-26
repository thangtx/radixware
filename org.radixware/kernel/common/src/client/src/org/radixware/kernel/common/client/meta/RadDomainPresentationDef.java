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

package org.radixware.kernel.common.client.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.meta.RadDomainDef;
import org.radixware.kernel.common.types.Id;


public final class RadDomainPresentationDef extends TitledDefinition {

    private final RadDomainDef domain;
    private List<RadDomainPresentationDef> innerDomains;
    private String fullTitle = null;
    private String fullName = null;

    public RadDomainPresentationDef(RadDomainDef domainDef) {
        super(domainDef.getId(),
                domainDef.getName(),
                domainDef.getTitleOwnerDefId(),
                domainDef.getTitleId());
        domain = domainDef;
    }

    public List<RadDomainPresentationDef> getInnerDomains() {
        if (innerDomains == null) {
            innerDomains = new ArrayList<RadDomainPresentationDef>();
            final List<Id> innerDomainIds = domain.getInnerDomainIds();
            for (Id domainId : innerDomainIds) {
                innerDomains.add(getDefManager().getDomainPresentationDef(domainId));
            }
        }
        return Collections.unmodifiableList(innerDomains);
    }

    public RadDomainPresentationDef getOwnerDomain() {
        if (domain.getOwnerDomainId() == null) {
            return null;
        } else {
            return getDefManager().getDomainPresentationDef(domain.getOwnerDomainId());
        }
    }

    public String getFullTitle(final char separator) {
        if (fullTitle == null) {
            fullTitle = hasTitle() ? getTitle() : getName();
            for (RadDomainPresentationDef domainDef = getOwnerDomain(); domainDef != null; domainDef = domainDef.getOwnerDomain()) {
                fullTitle = (domainDef.hasTitle() ? domainDef.getTitle() : domainDef.getName()) + separator + fullTitle;
            }
        }
        return fullTitle;
    }

    public String getFullName(final char separator) {
        if (fullName == null) {
            fullName = getName();
            for (RadDomainPresentationDef domainDef = getOwnerDomain(); domainDef != null; domainDef = domainDef.getOwnerDomain()) {
                fullName = domainDef.getName() + separator + fullName;
            }
        }
        return fullName;
    }

    public static Collection<RadDomainPresentationDef> getRootDomains(IClientApplication app) {
        final Collection<RadDomainPresentationDef> rootDomains = new ArrayList<RadDomainPresentationDef>();
        final Collection<ReleaseRepository.DefinitionInfo> domainsInfo = app.getDefManager().getRepository().getDefinitions(EDefType.DOMAIN);
        for (ReleaseRepository.DefinitionInfo definitionInfo : domainsInfo) {
            try {
                rootDomains.add(app.getDefManager().getDomainPresentationDef(definitionInfo.id));
            } catch (DefinitionError err) {
                final String msg = app.getMessageProvider().translate("TraceMessage", "Cannot get root domain definition #%s with name %s");
                app.getTracer().error(String.format(msg, definitionInfo.id, definitionInfo.name), err);
            }
        }
        return rootDomains;
    }
}
