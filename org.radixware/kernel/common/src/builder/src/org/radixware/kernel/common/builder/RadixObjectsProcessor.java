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

package org.radixware.kernel.common.builder;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public abstract class RadixObjectsProcessor {

    public interface ICollector extends IVisitor {

        public int getCount();

        public Collection<RadixObject> get();
    }

    private class Collector implements ICollector {

        public int count;
        public Set<RadixObject> radixObjects = new HashSet<>();

        @Override
        public void accept(RadixObject object) {
            if (radixObjects.add(object)) {
                count++;
            }
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Collection<RadixObject> get() {
            return radixObjects;
        }
    }

    protected interface ProcessFlow {

        public ERuntimeEnvironmentType getEnvironment();
    }

    protected static RadixObject getDisplayedObject(final RadixObject radixObject) {
        final Definition definition = radixObject.getDefinition();
        if (definition != null) {
            return definition;
        } else {
            return radixObject;
        }
    }
    private final Cancellable externalCanceller;

    public RadixObjectsProcessor(Cancellable externalCanceller) {
        this.externalCanceller = externalCanceller;
    }

    public RadixObjectsProcessor() {
        this.externalCanceller = null;
    }

    protected ICollector createCollector() {
        return new Collector();
    }
    private volatile boolean isCancelled = false;

    public boolean wasCancelled() {
        return isCancelled;
    }

    protected final void cancel() {
        isCancelled = true;
        externalCanceller.cancel();
    }

    protected boolean checkCancelProcess(final VisitorProvider visitorProvider, Cancellable cancellable) {
        if (visitorProvider.isCancelled() || (cancellable != null && cancellable.wasCancelled())) {
            cancel();
            if (!visitorProvider.isCancelled()) {
                visitorProvider.cancel();
            }
            return true;
        }
        return false;
    }

    protected final void process(Collection<? extends RadixObject> contexts, final VisitorProvider visitorProvider, IBuildDisplayer displayer, ProcessFlow flow) {

        IProgressHandle progressHandle = null;

        if (displayer != null) {
            final String preparingProcessName = getPreparingProcessName();
            progressHandle = displayer.getProgressHandleFactory().createHandle(preparingProcessName, new Cancellable() {
                @Override
                public boolean cancel() {
                    visitorProvider.cancel();
                    RadixObjectsProcessor.this.cancel();
                    return false;
                }

                @Override
                public boolean wasCancelled() {
                    return visitorProvider.isCancelled();
                }
            });
        }
        try {

            if (progressHandle != null) {
                progressHandle.start();
            }

            final ICollector collector = createCollector();

            for (RadixObject context : contexts) {
                context.visit(collector, visitorProvider);
                if (checkCancelProcess(visitorProvider, externalCanceller)) {
                    return;
                }
            }
            final int totalCount = collector.getCount();
            final int step = (totalCount < 100 ? 1 : totalCount / 100); // to prevent devision by zero

            final String processName = getProcessName(flow.getEnvironment());

            if (progressHandle != null) {
                progressHandle.setDisplayName(processName);
            }

            if (isBulkProcessor()) {
                processObjects(collector.get(), flow, progressHandle, externalCanceller);
            } else {
                if (progressHandle != null && totalCount > 0) {                    
                    progressHandle.switchToDeterminate(totalCount);
                }
                int processedCount = 0;

                for (RadixObject radixObject : collector.get()) {
                    if (checkCancelProcess(visitorProvider, externalCanceller)) {
                        return;
                    }

                    processObject(radixObject, flow);

                    processedCount++;
                    if (progressHandle != null && processedCount % step == 0 && totalCount > 0) { // for optimization
                        final String message = MessageFormat.format("Processed {0} objects of {1}", processedCount, totalCount);
                        progressHandle.progress(message, processedCount);
                    }
                }
            }
        } finally {
            if (progressHandle != null) {
                progressHandle.finish();
            }
        }
    }

    protected abstract String getProcessName(ERuntimeEnvironmentType env);

    protected abstract void processObject(RadixObject radixObject, ProcessFlow flow);

    protected void processObjects(Collection<RadixObject> radixObject, ProcessFlow flow, IProgressHandle progressHandle, ICancellable cancellable) {
    }

    protected boolean isBulkProcessor() {
        return false;
    }

    protected abstract String getPreparingProcessName();
}
