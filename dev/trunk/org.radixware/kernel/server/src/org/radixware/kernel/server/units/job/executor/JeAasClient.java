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

import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.aas.InvokeRs;
import org.radixware.schemas.aasWsdl.InvokeDocument;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoSapsAvailableException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.HttpFormatter;
import org.radixware.kernel.server.units.AasClient;
import org.radixware.kernel.server.utils.PriorityResourceManager;
import org.radixware.schemas.aas.InvokeMess;

/**
 * Клиент AAS. По запросу JobExecutor выполняет Job-ы в модуле ARTE. Реализация
 * асинхронная.
 *
 */
final class JeAasClient extends AasClient {

    //options
    private String lastScpName;
    private PriorityResourceManager.Ticket priorityTicket;
    private static final int AAS_INVOKE_TIMEOUT_MILLIS = 24 * 60 * 60 * 1000; //24 hours

    public JeAasClient(final JobExecutorUnit unit) throws SQLException {
        super(unit, unit.getManifestLoader(), unit.getTrace().newTracer(EEventSource.AAS_CLIENT.getValue()), unit.getDispatcher());
    }
    private Job curJob = null;

    public String getLastScpName() {
        return lastScpName;
    }

    public PriorityResourceManager.Ticket getPriorityTicket() {
        return priorityTicket;
    }

    public void setPriorityTicket(PriorityResourceManager.Ticket priorityTicket) {
        this.priorityTicket = priorityTicket;
    }

    void invoke(final Job job) {
        if (isStopped()) {
            throw new IllegalUsageError("AAS client is stopped");
        }
        curJob = job;
        lastScpName = job.getScpName();
        unit.getTrace().put(EEventSeverity.DEBUG, curJob.getFullTitle() + JobExecutorMessages._STARTED, JobExecutorMessages.MLS_ID_JOB_STARTED, new ArrStr(String.valueOf(curJob.id), String.valueOf(curJob.title), curJob.getParametersDescription()), unit.getEventSource(), false);
        final InvokeDocument invokeXml = InvokeDocument.Factory.newInstance();
        curJob.writeTo(invokeXml);
        lastRqFaulted = false;
        setScpName(job.getScpName() == null || job.getScpName().isEmpty() ? unit.getScpName() : job.getScpName());
        invoke(invokeXml, InvokeMess.class, HttpFormatter.appendPriorityHeaderAttr(null, job.radixPriority), true, AAS_INVOKE_TIMEOUT_MILLIS, this);
    }

    private void curJobDone() {
        final JobExecutorUnit jeUnit = (JobExecutorUnit) this.unit;
        jeUnit.setJobDone(curJob);
        jeUnit.getAasClientPool().freeClient(this);
        if (unit.isShuttingDown()) {
            closeActiveSeances();
        } else {
            jeUnit.isDoCurJobsSheduled = true;
        }
    }

    @Override
    protected void onInvokeException(final ServiceClientException exception) {
        if (exception instanceof NoSapsAvailableException) {
            unit.getTrace().put(EEventSeverity.DEBUG, curJob.getFullTitle() + JobExecutorMessages._TERMINATED_BY_ERR + ": " + exception.getMessage(), null, null, unit.getEventSource(), false);
            notifyNoSaps(lastScpName);
        } else {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(exception);
            unit.getTrace().put(EEventSeverity.ERROR, curJob.getFullTitle() + JobExecutorMessages._TERMINATED_BY_ERR + ": " + exStack, JobExecutorMessages.MLS_ID_JOB_TERMINATED_BY_ERR, new ArrStr(String.valueOf(curJob.id), String.valueOf(curJob.title), curJob.getParametersDescription(), exStack), unit.getEventSource(), false);
        }
        lastRqFaulted = true;
        curJobDone();
    }

    @Override
    protected void onInvokeResponse(final InvokeRs rs) {
        unit.getTrace().put(EEventSeverity.DEBUG, curJob.getFullTitle() + JobExecutorMessages._IS_DONE, JobExecutorMessages.MLS_ID_JOB_IS_DONE, new ArrStr(String.valueOf(curJob.id), String.valueOf(curJob.title), curJob.getParametersDescription()), unit.getEventSource(), false);
        lastRqFaulted = false;
        curJobDone();
    }
    private boolean lastRqFaulted = false;

    boolean isLastRqFaulted() {
        return lastRqFaulted;
    }
}
