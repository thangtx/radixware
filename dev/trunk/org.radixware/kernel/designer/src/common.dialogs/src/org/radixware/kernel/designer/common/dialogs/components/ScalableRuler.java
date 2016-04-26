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

package org.radixware.kernel.designer.common.dialogs.components;


public class ScalableRuler {
    private final double baseSize;
    private final double currSize;
    private final double scaleFactor;

    public ScalableRuler(double baseSize, double currSize) {
        this.baseSize = baseSize;
        this.currSize = currSize;
        this.scaleFactor = currSize / baseSize;
    }

    public int scale(int val) {
        return (int) (val * scaleFactor);
    }

    public float scale(float val) {
        return (int) (val * scaleFactor);
    }

    public double scale(double val) {
        return (int) (val * scaleFactor);
    }
}
