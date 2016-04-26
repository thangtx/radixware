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

package org.radixware.kernel.designer.debugger.impl;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.designer.debugger.RadixDebugger;


public class ArrayReferenceWrapper extends ObjectReferenceWrapper {

    public class ValueRange {

        private int start, end;

        public ValueRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public List<Object> getValuesOrRanges() {
            List<Object> result = new LinkedList<Object>();
            int count = end - start;
            if (count > 50) {
                int rangeCount = count / 50;
                int lastSize = 0;
                if (rangeCount * 50 < count) {
                    lastSize = count - rangeCount * 50;
                }
                for (int i = 0; i < rangeCount; i++) {
                    result.add(new ValueRange(start, start + 50));
                    start += 50;
                }
                if (lastSize > 0) {
                    result.add(new ValueRange(start, start+lastSize));
                }
            } else {
                List<ValueWrapper> arrayValues = getValues(start, end);
                int index = start;
                for (ValueWrapper w : arrayValues) {
                    result.add(new ArrayElement(index, w));
                    index++;
                }
            }
            return result;
        }

        public String getDisplayName() {
            return "[" + start + " - " + (end -1)+ "]";
        }
    }
    private final Map<Value, ValueWrapper> values = new WeakHashMap<Value, ValueWrapper>();

    public ArrayReferenceWrapper(RadixDebugger debugger, ObjectReference value) {
        super(debugger, value);
    }

    public int getSize() {
        return ((ArrayReference) value).length();
    }

    private ValueWrapper getValue(Value v) {
        synchronized (values) {
            ValueWrapper w = values.get(v);
            if (w == null) {
                w = ValueWrapper.newInstance(debugger, v);
                values.put(v, w);
            }
            return w;
        }
    }

    public List<ValueWrapper> getValues(int from, int to) {
        List<Value> vals = new ArrayList<Value>(((ArrayReference) value).getValues(from, to - from));
        List<ValueWrapper> wrappers = new LinkedList<ValueWrapper>();
        for (Value v : vals) {
            wrappers.add(getValue(v));
        }
        return wrappers;
    }

    public List<Object> getValuesOrRanges() {
        ValueRange root = new ValueRange(0, getSize());
        return root.getValuesOrRanges();
    }

    @Override
    protected String calcTypeName(String name) {
        int bIndex = name.indexOf("[");
        if (bIndex > 0) {
            String elementTypeName = name.substring(0, bIndex);
            return super.calcTypeName(elementTypeName) + name.substring(bIndex);
        } else {
            return super.calcTypeName(name);
        }
    }
}
