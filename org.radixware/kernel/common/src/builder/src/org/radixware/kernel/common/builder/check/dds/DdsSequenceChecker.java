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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef.Problems;


@RadixObjectCheckerRegistration
public class DdsSequenceChecker<T extends DdsSequenceDef> extends DdsDefinitionChecker<T> {

    public DdsSequenceChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsSequenceDef.class;
    }

    @Override
    public void check(T sequence, IProblemHandler problemHandler) {
        super.check(sequence, problemHandler);

        checkForDbNameDuplication(sequence, problemHandler);

        Long cache = sequence.getCache();
        Long maxValue = sequence.getMaxValue();
        Long minValue = sequence.getMinValue();
        Long startWith = sequence.getStartWith();
        Long incrementBy = sequence.getIncrementBy();

        if (incrementBy != null) {
            if ((incrementBy == 0) || (maxValue != null && minValue != null && Math.abs(incrementBy) >= maxValue - minValue)) {
                error(sequence, problemHandler, "'Increment by' value is out of range");
            }
        }

        if (startWith != null && maxValue != null && minValue != null &&
                Math.abs(startWith) >= maxValue - minValue) {
            error(sequence, problemHandler, "'Start with' value is out of range");
        }

        if (cache != null && maxValue != null && minValue != null && incrementBy != null && incrementBy != 0) {
            if (cache < 2 || (cache >= Math.round(Math.ceil((maxValue - minValue)) / Math.abs(incrementBy)))) {
                error(sequence, problemHandler, "'Cache' value is out of range");
            }
        }
        
        if ((minValue != null && minValue != 1) || (incrementBy != null && incrementBy != 1)) {
            if (!sequence.isWarningSuppressed(Problems.AADC_SEQUENCE)) {
                warning(sequence, problemHandler, Problems.AADC_SEQUENCE);
            }
        }
    }
}
