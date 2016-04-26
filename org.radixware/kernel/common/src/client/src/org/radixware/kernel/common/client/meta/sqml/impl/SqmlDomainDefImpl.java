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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;


final class SqmlDomainDefImpl extends SqmlDefinitionImpl implements ISqmlDomainDef{
    
    private final AdsDomainDef domainDef;  
    private final SqmlDomainDefImpl parentDomain;
    private final List<ISqmlDomainDef> childs = new ArrayList<>();
    
    public SqmlDomainDefImpl(final IClientEnvironment environment, final AdsDomainDef domainDef){
        this(environment, null, domainDef);
    }
    
    private SqmlDomainDefImpl(final IClientEnvironment environment, final SqmlDomainDefImpl parentDomain, final AdsDomainDef domainDef){
        super(environment,domainDef);
        this.parentDomain = parentDomain;
        this.domainDef = domainDef;        
        for (AdsDomainDef childDomain: domainDef.getChildDomains()){
            childs.add(new SqmlDomainDefImpl(environment, this, childDomain));
        }
    }    

    @Override
    public List<ISqmlDomainDef> getChildDomains() {
        return Collections.unmodifiableList(childs);
    }    

    @Override
    public ISqmlDomainDef getParentDomain() {
        return parentDomain;
    }        

    @Override
    public String getTitle() {
        if (domainDef.getTitleId() == null) {
            return domainDef.getName();
        }
        return checkTitle(domainDef.getTitle(environment.getLanguage()));
    }
}