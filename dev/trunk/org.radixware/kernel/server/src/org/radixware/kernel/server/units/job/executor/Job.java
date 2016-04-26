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

package org.radixware.kernel.server.units.job.executor;

import java.math.BigInteger;
import org.radixware.kernel.common.enums.EPriority;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.server.arte.JobQueue;
import org.radixware.schemas.aas.InvokeRq;
import org.radixware.schemas.aas.InvokeRq.Parameters;
import org.radixware.schemas.aasWsdl.InvokeDocument;

final class Job {

    final Long id;
    final String title;
    final long delayMillis;
    final int radixPriority;
    final int unlockCount;
    final String scpName;

    Job(
            final Long id,
            final String title,
            final long delayMillis,
            final int radixPriority,
            final String scpName,
            final int unlockCount) {
        this.id = id;
        this.title = title;
        this.delayMillis = delayMillis;
        this.radixPriority = radixPriority;
        this.scpName = scpName;
        this.unlockCount = unlockCount;
    }

    @Override
    public String toString() {
        return getFullTitle();
    }

    public String getScpName() {
        return scpName;
    }

    public int getRadixPriority() {
        return radixPriority;
    }

    protected String getFullTitle() {
        final StringBuilder sb = new StringBuilder();
        sb.append(JobExecutorMessages.JOB);
        sb.append(" \"#");
        sb.append(String.valueOf(id));
        sb.append("-");
        sb.append(title);
        sb.append("\" ");
        sb.append(getParametersDescription());
        return sb.toString();
    }

    protected String getParametersDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("SCP: ");
        sb.append(scpName);
        sb.append("; Radix priority: ");
        try {
            sb.append(EPriority.getForValue((long) radixPriority).getName());
        } catch (NoConstItemWithSuchValueError ex) {
            sb.append("<").append(radixPriority).append(">");
        }
        sb.append("; Unlock count: ");
        sb.append(unlockCount);
        sb.append("]");
        return sb.toString();
    }

    final void writeTo(final InvokeDocument invokeXml) {
        final InvokeRq invokeRq = invokeXml.addNewInvoke().addNewInvokeRq();
        invokeRq.setClassName(JobQueue.class.getName());
        invokeRq.setMethodId("execute");
        final Parameters paramsXml = invokeRq.addNewParameters();
        paramsXml.addNewItem().addNewArte();
        paramsXml.addNewItem().setInt(id);
    }
}
