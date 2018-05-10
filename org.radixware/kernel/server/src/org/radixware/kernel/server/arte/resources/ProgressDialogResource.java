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
package org.radixware.kernel.server.arte.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.schemas.eas.ProgressDialogFinishProcessMess;
import org.radixware.schemas.eas.ProgressDialogSetMess;
import org.radixware.schemas.eas.ProgressDialogSetRq;
import org.radixware.schemas.eas.ProgressDialogStartProcessMess;
import org.radixware.schemas.eas.ProgressDialogStartProcessRq;
import org.radixware.schemas.eas.ProgressDialogTraceMess;
import org.radixware.schemas.eas.ProgressDialogTraceRq;
import org.radixware.schemas.eas.Trace;
import org.radixware.schemas.eas.TraceLevelEnum;
import org.radixware.schemas.eas.Trace.Item;
import org.radixware.schemas.easWsdl.ProgressDialogFinishProcessDocument;
import org.radixware.schemas.easWsdl.ProgressDialogSetDocument;
import org.radixware.schemas.easWsdl.ProgressDialogStartProcessDocument;
import org.radixware.schemas.easWsdl.ProgressDialogTraceDocument;

public final class ProgressDialogResource extends Resource {

    private final Arte arte;

    /**
     * Посылает клиенту запрос на отображение окна для показа прогресса
     * выполнения процессов на сервере
     *
     * @param caption - может быть null - в этом случае клиент должен отобразить
     * стандартный заголовок ("Пожалуйста, подождите...")
     * @param showTrace - отображать трассу, если хотя бы для одного из
     * отображаемых процессов трасса нужна, должен быть равен true
     */
    public ProgressDialogResource(final Arte arte, final String caption, final boolean showTrace) {
        this.showTrace = showTrace;
        this.caption = caption;
        this.arte = arte;
    }

    public Process startNewProcess(final String title, final boolean cancellable, final boolean showPercent) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        return  startNewProcess(title,cancellable,null,showPercent);
    }
    
    public Process startNewProcess(final String title, final boolean cancellable, final String cancelButtonTitle, final boolean showPercent) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
        if (firstProcess == null || firstProcess.closed) {
            firstProcess = new Process(arte, caption, showTrace, title, cancellable, cancelButtonTitle, showPercent, true);
        } else {
            throw new ResourceUsageException("Process already started", null);
        }
        return firstProcess;
    }    

    public static final class Process {

        private final Arte arte;
        private final String id;

        /**
         * Отображение на клиенте нового ProgressBar'а
         *
         * @param caption - заголовок диалога, используется только если isFirst
         * = true
         * @param showTrace - настройка диалога, используется только если
         * isFirst = true
         * @param title - заголовок текущего процесса, может быть null
         * @param cancellable - может ли быть кнопка "Cancel" при выполнении
         * текущего процесса
         * @param cancelButtonTitle - подпись для кнопки отмены если она есть. Этот параметр может быть null,
         * тогда подпись будет "Cancel".
         * @param showPercent - если false - ProgressBar будет без фиксированной
         * max границы
         * @param isFirst - первый ли процесс в этом диалоге (если первый то в
         * запросе будут переданы настройки самого диалога)
         * @throws ResourceUsageTimeout
         * @throws ResourceUsageException
         * @throws InterruptedException
         */
        protected Process(final Arte arte, final String caption, final boolean showTrace, final String title, final boolean cancellable, final String cancelButtonTitle, final boolean showPercent, final boolean isFirst)
                throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            this.arte = arte;
            final ProgressDialogStartProcessDocument ssDoc = ProgressDialogStartProcessDocument.Factory.newInstance();
            final ProgressDialogStartProcessRq rq = ssDoc.addNewProgressDialogStartProcess().addNewProgressDialogStartProcessRq();
            if (isFirst) {
                if (caption != null) {
                    rq.setCaption(caption);
                }
                rq.setShowTrace(showTrace);
            }
            rq.setTitle(title);
            rq.setCancellable(cancellable);            
            rq.setShowPercent(showPercent);
            if (cancelButtonTitle!=null){
                rq.setCancelButtonTitle(cancelButtonTitle);
            }
            final ProgressDialogStartProcessMess rs
                    = (ProgressDialogStartProcessMess) arte.getArteSocket().invokeResource(ssDoc, ProgressDialogStartProcessMess.class, DEFAULT_TIMEOUT);
            id = rs.getProgressDialogStartProcessRs().getId();
        }

        private String lastSetTitle = null;
        private Boolean lastSetCancelablle = null;
        private String lastSetCancelBtnTitle = null;
        private long lastDeliveredSetMillis = 0;
        private boolean lastResult;
        private static final int SET_DELIVERY_PERIOD_MILLIS = 500; //0.5 sec

        public boolean set(final String title, final Float progress, final Boolean cancellable, final String cancelButtonTitle)
                throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            if (lastDeliveredSetMillis + SET_DELIVERY_PERIOD_MILLIS > System.currentTimeMillis() // to little time passed
                    && Utils.equals(title, lastSetTitle) // title is not changed
                    && Utils.equals(cancellable, lastSetCancelablle) // cancelable option is not changed
                    && Utils.equals(cancelButtonTitle, lastSetCancelBtnTitle)//cancel button title is not changed
                    ) {
                return lastResult;//to decrease frequency of callbacks
            } else {
                lastSetTitle = title;
                lastSetCancelablle = cancellable;
                lastSetCancelBtnTitle = cancelButtonTitle;
            }
            final ProgressDialogSetDocument ssDoc = ProgressDialogSetDocument.Factory.newInstance();
            final ProgressDialogSetRq rq = ssDoc.addNewProgressDialogSet().addNewProgressDialogSetRq();
            rq.setId(id);
            if (title != null) {
                rq.setTitle(title);
            }
            if (progress != null) {
                float f = progress.floatValue();
                if (f < 0) {
                    f = 0;
                }
                if (f > 100) {
                    f = 100;
                }
                rq.setProgress(f);
            }
            if (cancellable != null) {
                rq.setCancellable(cancellable.booleanValue());
            }
            if (cancelButtonTitle!=null){
                rq.setCancelButtonTitle(cancelButtonTitle);
            }
            final ProgressDialogSetMess rs;
            rs = (ProgressDialogSetMess) arte.getArteSocket().invokeResource(ssDoc, ProgressDialogSetMess.class, DEFAULT_TIMEOUT);
            lastDeliveredSetMillis = System.currentTimeMillis();
            lastResult = rs.getProgressDialogSetRs().getCancelled();
            return lastResult;
        }
        
        public boolean set(final String title, final Float progress, final Boolean cancellable)
            throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
                return set(title, progress, cancellable, null);
            
        }        

        public boolean checkIsCancelled() throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            return set(null, null, null);
        }

        public void close() throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            if (nextProcess != null && !nextProcess.closed) {
                throw new ResourceUsageException("Unable to close process - child process not closed", null);
            }
            final ProgressDialogFinishProcessDocument ssDoc = ProgressDialogFinishProcessDocument.Factory.newInstance();
            ssDoc.addNewProgressDialogFinishProcess().addNewProgressDialogFinishProcessRq();
            arte.getArteSocket().invokeResource(ssDoc, ProgressDialogFinishProcessMess.class, DEFAULT_TIMEOUT);
            closed = true;
        }

        public Process startNewProcess(final String title, final boolean cancellable, final boolean showPercent) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            return startNewProcess(title, cancellable, null, showPercent);
        }
        
        public Process startNewProcess(final String title, final boolean cancellable, final String cancelButtonTitle, final boolean showPercent) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            nextProcess = new Process(
                    arte,
                    null /*не используется так как процесс не первый */,
                    false /*не используется так как процесс не первый */,
                    title,
                    cancellable,
                    cancelButtonTitle,
                    showPercent,
                    false
            );
            return nextProcess;
        }        

        /**
         * Пример формирования трассы: TraceType trace =
         * TraceType.Factory.newInstance();
         * org.radixware.schemas.eas.TraceType.Item[] traceItems = new
         * org.radixware.schemas.eas.TraceType.Item[1]; traceItems[0] =
         * org.radixware.schemas.eas.TraceType.Item.Factory.newInstance();
         * traceItems[0].setLevel( TraceLevelEnum.DEBUG );
         * traceItems[0].setStringValue("Debug Message"); trace.setItemArray(
         * traceItems );
         *
         * @throws ResourceUsageTimeout
         * @throws ResourceUsageException
         * @throws InterruptedException
         * @throws DbpResourceException
         */
        public boolean trace(final Trace trace) throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            final ProgressDialogTraceDocument ssDoc = ProgressDialogTraceDocument.Factory.newInstance();
            final ProgressDialogTraceRq rq = ssDoc.addNewProgressDialogTrace().addNewProgressDialogTraceRq();
            rq.setTrace(trace);
            final ProgressDialogTraceMess rs = (ProgressDialogTraceMess) arte.getArteSocket().invokeResource(ssDoc, ProgressDialogTraceMess.class, DEFAULT_TIMEOUT);
            return rs.getProgressDialogTraceRs().getCancelled();
        }

        public void trace(final TraceLevelEnum.Enum level, final String mess) throws ResourceUsageException {
            checkClosed();
            final Trace.Item i = Trace.Item.Factory.newInstance();
            i.setLevel(level);
            i.setStringValue(XmlUtils.getSafeXmlString(mess));
            traceItems.add(i);
        }

        public boolean flushTrace() throws ResourceUsageException, ResourceUsageTimeout, InterruptedException {
            checkClosed();
            final Trace.Item[] arr = new Trace.Item[traceItems.size()];
            traceItems.toArray(arr);
            final Trace trace = Trace.Factory.newInstance();
            trace.getItemList().addAll(Arrays.asList(arr));
            traceItems.clear();
            return trace(trace);
        }

        private void checkClosed() throws ResourceUsageException {
            if (closed) {
                throw new ResourceUsageException("Process is closed", null);
            }
        }

        private final List<Item> traceItems = new ArrayList<Item>();
        private Process nextProcess = null;
        private boolean closed = false;
    }

    private final String caption;
    private final boolean showTrace;
    private Process firstProcess = null;
}
