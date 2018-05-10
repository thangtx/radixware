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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentations;
import org.radixware.kernel.common.types.Id;


final class SqmlSelectorPresentations implements ISqmlSelectorPresentations{
    
    public final static SqmlSelectorPresentations EMPTY = new SqmlSelectorPresentations();
    
    private final List<ISqmlSelectorPresentationDef> presentations;
    
    private SqmlSelectorPresentations(){
        presentations = null;
    }
    
    public SqmlSelectorPresentations(final List<SqmlSelectorPresentationImpl> presentations){
        this.presentations = new ArrayList<>();
        this.presentations.addAll(presentations);
    }

    @Override
    public int size() {
        return presentations==null ? 0 : presentations.size();
    }

    @Override
    public ISqmlSelectorPresentationDef getPresentationById(final Id presentationId) {
        if (presentations==null){
            return null;
        }else{
            for (ISqmlSelectorPresentationDef presentation: presentations){
                if (presentation.getId().equals(presentationId)){
                    return presentation;
                }
            }
            return null;
        }
    }

    @Override
    public ISqmlSelectorPresentationDef get(final int idx) {
        if (presentations==null){
            throw new IndexOutOfBoundsException();
        }else{
            return presentations.get(idx);
        }
    }

    @Override
    public Iterator<ISqmlSelectorPresentationDef> iterator() {
        return presentations==null ? Collections.<ISqmlSelectorPresentationDef>emptyIterator() : presentations.iterator();
    }
}
