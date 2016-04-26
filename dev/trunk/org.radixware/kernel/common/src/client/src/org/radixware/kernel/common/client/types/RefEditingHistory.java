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

package org.radixware.kernel.common.client.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public final class RefEditingHistory extends AbstractEditingHistory {
    private final IClientEnvironment environment;
    private final Id tableId, defaultPresentationId;
    private final List<Reference> references = new ArrayList<Reference>();
    private boolean isActual = false;
    
    public RefEditingHistory(final IClientEnvironment environment, final String settingPath, final Id tableId, final Id defaultPresentationId) {
        super(environment, settingPath);
        this.environment = environment;
        this.tableId = tableId;
        this.defaultPresentationId = defaultPresentationId;
        init(settingPath);
    }
    
    private void actualize() {
        final EntityObjectTitlesProvider titlesProvider = new EntityObjectTitlesProvider(environment, tableId, defaultPresentationId);
        final List<Pid> pids = new LinkedList<Pid>();
        
        if(valuesAsStr.isEmpty()) {
            isActual = true;
            return;
        }
        
        for(String refsAsStr : valuesAsStr) {
            final Reference ref = Reference.fromValAsStr(refsAsStr);
            titlesProvider.addEntityObjectReference(ref);
            pids.add(ref.getPid());
        }
        
        try {
            final EntityObjectTitles titles = titlesProvider.getTitles();
            for(Pid p : pids) {
                final Reference ref = titles.getEntityObjectReference(p);
                if(titles.isEntityObjectAccessible(p)){
                    references.add(ref);
                } else {
                    valuesAsStr.remove(ref.toValAsStr());
                }
            }
            isActual = true;
        } catch (InterruptedException ex) {
            valuesAsStr.clear();
        } catch (ServiceClientException ex) {
            valuesAsStr.clear();
            environment.getTracer().error(ex);
        }
        
    }
    
    public Reference getReference(final int index) {
        if(!isActual) {
            actualize();
        }
        return references.get(index);
    }

    public void addReference(final Reference ref) {
        if(!isActual) {
            actualize();
        }
        
        if(references.contains(ref)) {
            references.remove(ref);
        } else if(references.size() >= MAX_SIZE) {
            references.remove(references.size() - 1);
        }
        references.add(0, ref);
        addEntry(ref.toValAsStr());
    }
    
    @Override
    public List<String> getEntries() {
        if(!isActual) {
            actualize();
        }
        return super.getEntries();
    }

    @Override
    public int getSize() {
        if(!isActual) {
            actualize();
        }
        return super.getSize();
    }
            
    public List<Reference> getReferences() {
        if(!isActual) {
            actualize();
        }
        return Collections.unmodifiableList(references);
    }
    
    public List<String> getTitles() {
        if(!isActual) {
            actualize();
        }
        final List<String> titles = new LinkedList<String>();
        for(Reference r : references) {
            titles.add(r.getTitle());
        }
        return titles;
    }
}