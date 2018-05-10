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

import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;

public class ArrRef extends Arr<Reference> {

    /**
     *
     */
    private static final long serialVersionUID = 1459471198585309528L;    
    private static final ReferenceAsStrParser ITEM_PARSER = new ReferenceAsStrParser();

    public ArrRef(final int initialCapacity) {
        super(initialCapacity);
    }

    public ArrRef() {
        super();
    }

    public ArrRef(final Reference[] array) {
        super(array);
    }

    public ArrRef(final Reference item1) {
        super(1);
        add(item1);
    }

    public ArrRef(final Reference item1, final Reference item2) {
        super(2);
        add(item1);
        add(item2);
    }

    public ArrRef(final Reference item1, final Reference item2, final Reference item3) {
        super(3);
        add(item1);
        add(item2);
        add(item3);
    }

    public ArrRef(final Reference item1, final Reference item2, final Reference item3, final Reference item4) {
        super(4);
        add(item1);
        add(item2);
        add(item3);
        add(item4);
    }

    public ArrRef(final Reference item1, final Reference item2, final Reference item3, final Reference item4, final Reference item5) {
        super(5);
        add(item1);
        add(item2);
        add(item3);
        add(item4);
        add(item5);
    }

    public ArrRef(final Id entityId, final String[] pidsAsStrs) {
        super(createRefs(entityId, pidsAsStrs));
    }
    
    public ArrRef(final ArrRef source){
        super(source);
    }

    protected static class ReferenceAsStrParser implements Arr.ItemAsStrParser {

        @Override
        public Object fromStr(final String asStr) {
            if (asStr==null){
                return null;
            }else if (asStr.length()>1 && asStr.charAt(1)=='['){
                return Reference.fromValAsStr(asStr);
            }else{
                return new Reference(Pid.fromStr(asStr));
            }
        }
    }    

    public static ArrRef fromValAsStr(final String valAsStr) {
        if (valAsStr == null) {
            return null;
        }
        final ArrRef arr = new ArrRef();
        restoreArrFromValAsStr(arr, valAsStr, EValType.PARENT_REF, ITEM_PARSER);
        return arr;
    }

    public final ArrStr toArrStr() {
        return toArrStr(true);
    }
    
    public final ArrStr toArrStr(final boolean writeTableId) {
        final ArrStr arr = new ArrStr(size());
        Pid pid;
        for (Reference r : this) {            
            pid = r==null ? null : r.getPid();
            if (pid==null){
                arr.add(null);
            }else{
                arr.add(writeTableId ? pid.toStr() : pid.toString());
            }
        }
        return arr;
    }    

    public final boolean contains(final Pid pid) {
        for (Reference r : this) {
            if (r.getPid().equals(pid)) {
                return true;
            }
        }
        return false;
    }

    private static Reference[] createRefs(final Id entityId, final String[] pidsAsStrs) {
        if (pidsAsStrs == null) {
            return null;
        }
        final Reference[] refs = new Reference[pidsAsStrs.length];
        for (int i = 0; i < refs.length; i++) {
            if (pidsAsStrs[i] == null) {
                refs[i] = null;
            } else {
                refs[i] = new Reference(entityId, pidsAsStrs[i], null);//NOPMD
            }
        }
        return refs;
    }

    @Override
    public EValType getItemValType() {
        return EValType.PARENT_REF;
    }

    @Override
    protected String getAsStr(final int i) {
        final Reference item = get(i);
        return item==null || item.getPid()==null || item.getPid().getTableId()==null ? null : item.toValAsStr();
    }
    
    public ArrRef actualizeTitles(final IClientEnvironment environment, final Id tableId) throws InterruptedException, ServiceClientException{
        return actualizeTitles(environment, tableId, (Id)null);
    }    
    
    public ArrRef actualizeTitles(final IClientEnvironment environment, final Id tableId, final Id presentationId) throws InterruptedException, ServiceClientException{
        if (isEmpty()) {
            return new ArrRef();
        }else{
            final EntityObjectTitlesProvider titlesProvider = new EntityObjectTitlesProvider(environment, tableId, presentationId);
            return actualizeTitles(titlesProvider, tableId);
        }
    }
    
    public ArrRef actualizeTitles(final IClientEnvironment environment, final Id tableId, final IContext.Abstract context) throws InterruptedException, ServiceClientException{
        if (isEmpty()) {
            return new ArrRef();
        }else{
            final EntityObjectTitlesProvider titlesProvider = new EntityObjectTitlesProvider(environment, tableId, context);
            return actualizeTitles(titlesProvider, tableId);
        }
    }
    
    private ArrRef actualizeTitles(final EntityObjectTitlesProvider titlesProvider, final Id tableId) throws InterruptedException, ServiceClientException{
        boolean needToActualize = false;
        for (Reference ref : this) {
            if (ref != null && ref.getPid() != null) {
                if (!Objects.equals(ref.getPid().getTableId(), tableId)) {
                    final String message = "Object %1$s belongs to a different entity (%2$s)";
                    throw new IllegalArgumentException(String.format(message, ref.getPid().toString(), tableId));
                }
                titlesProvider.addEntityObjectReference(ref);
                needToActualize = true;
            }
        }
        final ArrRef newValues = new ArrRef();
        if (needToActualize){
            final EntityObjectTitles titles = titlesProvider.getTitles();
            for (Reference ref : this) {
                if (ref == null || ref.getPid() == null) {
                    newValues.add(ref);
                } else {
                    newValues.add(titles.getEntityObjectReference(ref.getPid()));
                }
            }            
        }else{
            newValues.addAll(this);            
        }
        return newValues;
    }
}
