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
package org.radixware.kernel.common.client.dashboard;

import java.util.Iterator;
import org.apache.xmlbeans.XmlCursor;
import org.radixware.schemas.monitoringcommand.DiagramRs;
import org.radixware.schemas.monitoringcommand.MetricRecord;

/**
 *
 * @author npopov
 */
public class FastMetricRecordIterator implements Iterator<MetricRecord>, AutoCloseable {

    private XmlCursor cursor;
    private MetricRecord curRec;
    private boolean isReverseOrder = false;
    private final boolean isEmpty;

    public FastMetricRecordIterator(DiagramRs.HistoryRs.Records records) {
        this(records, false);
    }

    public FastMetricRecordIterator(DiagramRs.HistoryRs.Records records, boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
        this.isEmpty = records.sizeOfRecordArray() == 0;
        if (!isEmpty) {
            this.cursor = records.newCursor();
            final boolean childFound = !isReverseOrder
                    ? this.cursor.toFirstChild()
                    : this.cursor.toLastChild();
            if (childFound) {
                curRec = (MetricRecord) this.cursor.getObject();
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (isEmpty) {
            return false;
        } else if (curRec != null) {
            return true;
        } else {
            final boolean siblingFound = !isReverseOrder
                    ? cursor.toNextSibling()
                    : cursor.toPrevSibling();
            if (siblingFound) {
                curRec = (MetricRecord) this.cursor.getObject();
            }
            return siblingFound;
        }
    }

    @Override
    public MetricRecord next() {
        if (isEmpty) {
            return null;
        } else if (curRec != null || hasNext()) {
            final MetricRecord res = curRec;
            curRec = null;
            return res;
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        if (cursor != null) {
            cursor.dispose();
        }
    }
}
