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
package org.radixware.kernel.utils.traceview.utils;

import java.util.ArrayList;

/**
 *
 * @author danil fedonenko
 */
public class ContextSet {

    ArrayList<Long> bits = new ArrayList<>();
    int size;

    public ContextSet() {
        size = 0;
    }

    public ContextSet(ContextSet a) {
        this.size = a.size;
        for (int i = 0; i < a.bits.size(); i++) {
            this.bits.add(i, a.bits.get(i));
        }
    }

    public void add() {
        if (size % 63 == 0) {
            bits.add(0L);
        }
        size++;
    }

    public void set(int bit, boolean val) {
        if (bit <= size) {
            long a = 1L << (bit % 63);
            long mask = ~a;
            a = bits.get(bit / 63) & mask;
            a = a | (val ? 1L : 0L) << (bit % 63);
            bits.set(bit / 63, a);
        }
    }

    public boolean filter(ContextSet f) {
        int i = 0;
        long a;
        while (i < ((bits.size() > f.bits.size()) ? bits.size() : f.bits.size())) {
            if (i < bits.size() && i < f.bits.size()) {
                a = bits.get(i) & f.bits.get(i);
                if (a != 0) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    public String toString() {
        String rezult = "";
        long b, a;
        int j = 0;
        while (j < size) {
            a = bits.get(j / 63);
            b = a & (1L << (j % 63));
            if (b == 0) {
                rezult = 0 + rezult;
            } else {
                rezult = 1 + rezult;
            }
            j++;
        }
        return rezult;
    }
}
