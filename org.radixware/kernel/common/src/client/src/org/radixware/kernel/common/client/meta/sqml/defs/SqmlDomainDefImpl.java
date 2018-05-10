/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;

final class SqmlDomainDefImpl extends SqmlDefinitionImpl implements ISqmlDomainDef{
    
    private final List<ISqmlDomainDef> subDomains;
    private final Id titleId;
    private ISqmlDomainDef parent;

    public SqmlDomainDefImpl(final SqmlModule module, final Attributes attributes, final List<SqmlDomainDefImpl> subDomains){
        super(module, attributes);
        this.subDomains = new LinkedList<>();
        for (SqmlDomainDefImpl domain: subDomains){            
            this.subDomains.add(domain);
            domain.parent = this;
        }
        titleId = SqmlDefinitionImpl.parseOptionalId(attributes, "TitleId");
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(AdsDefinitionIcon.DOMAIN);
    }

    @Override
    public List<ISqmlDomainDef> getChildDomains() {
        return Collections.unmodifiableList(subDomains);
    }

    @Override
    public ISqmlDomainDef getParentDomain() {
        return parent;
    }

    @Override
    public String getTitle() {
        if (titleId==null){
            return getShortName();
        }else{
            ISqmlDomainDef topLevelParent = this;
            for (; topLevelParent.getParentDomain()!=null; topLevelParent=topLevelParent.getParentDomain()){}
            return getTitle(topLevelParent.getId(), titleId);            
        }
    }
    
    private Stack<ISqmlDomainDef> getPath(){
        final Stack<ISqmlDomainDef> path = new Stack<>();
        for (ISqmlDomainDef domain=this; domain!=null; domain=domain.getParentDomain()){
            path.push(domain);
        }
        return path;
    }

    @Override
    public String getFullName() {
        final StringBuilder nameBuilder = new StringBuilder(getModuleName());
        nameBuilder.append("::");
        final Stack<ISqmlDomainDef> path = getPath();
        while(!path.isEmpty()){
            nameBuilder.append(path.pop().getShortName());
            if (!path.isEmpty()){
                nameBuilder.append(':');
            }
        }        
        return nameBuilder.toString();
    }

    @Override
    public Id[] getIdPath() {
        final Stack<ISqmlDomainDef> path = getPath();
        final int size = path.size();
        final Id[] idPath = new Id[size];
        for (int i=0; i<size; i++){
            idPath[i]=path.pop().getId();
        }
        return idPath;
    }
    
    
}
