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

import org.radixware.kernel.common.enums.EValType;
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

    protected static class ReferenceAsStrParser implements Arr.ItemAsStrParser {

        @Override
        public Object fromStr(final String asStr) {
            return new Reference(Pid.fromStr(asStr));
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
        final ArrStr arr = new ArrStr(size());
        for (Reference r : this) {
            arr.add(r == null || r.getPid() == null ? null : r.getPid().toStr());
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
        return String.valueOf(get(i));
    }
}
