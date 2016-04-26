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

package org.radixware.kernel.designer.environment.actions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.PerformanceLogger;


public class ViewInformationAction extends CallableSystemAction {

    private static class Collector implements IVisitor, Cancellable {

        public boolean cancelled = false;
        private final VisitorProvider provider;
        private final Map<Class, Long> class2count = new HashMap<Class, Long>();
        private final Map<Class, Long> radixObjectsContainerClass2count = new HashMap<Class, Long>();
        private final Map<Class, Long> class2fileSize = new HashMap<Class, Long>();

        public Collector(VisitorProvider provider) {
            this.provider = provider;
        }

        private static void accept(Map<Class, Long> counter, Class clazz, long count) {
            while (clazz != Object.class) {
                Long totalCount = counter.get(clazz);
                if (totalCount != null) {
                    totalCount = Long.valueOf(totalCount.intValue() + count);
                } else {
                    totalCount = Long.valueOf(count);
                }
                counter.put(clazz, totalCount);
                clazz = clazz.getSuperclass();
            }
        }

        @Override
        public void accept(RadixObject radixObject) {
            accept(class2count, radixObject.getClass(), 1);

            if (radixObject instanceof RadixObjects) {
                RadixObject owner = radixObject.getOwnerDefinition();
                if (owner == null) {
                    owner = radixObject.getContainer();
                }
                final Class clazz = owner.getClass();
                accept(radixObjectsContainerClass2count, clazz, 1);
            }

            if (radixObject.isSaveable()) {
                final File file = radixObject.getFile();
                final long size = file.length();
                accept(class2fileSize, radixObject.getClass(), size);
            }
        }

        private static void fillCounts(Map<Class, Long> counter, Class parent, StringBuilder sb, String prefix) {
            final Long parentCount = counter.get(parent);
            sb.append(prefix + parentCount + " - " + parent.getSimpleName() + " (" + parent.getCanonicalName() + ")\n");
            counter.remove(parent);

            while (true) {
                int max = 0;
                Class clazz = null;

                for (Map.Entry<Class, Long> entry : counter.entrySet()) {
                    if (entry.getKey().getSuperclass() == parent) {
                        final int count = entry.getValue().intValue();
                        if (count > max) {
                            clazz = entry.getKey();
                            max = count;
                        }
                    }
                }

                if (clazz != null) {
                    fillCounts(counter, clazz, sb, prefix + "    ");
                } else {
                    break;
                }
            }
        }

        public String getResult() {
            StringBuilder sb = new StringBuilder();

            sb.append("---------------\n");
            sb.append("OBJECT COUNT\n");
            sb.append("---------------\n");

            fillCounts(class2count, RadixObject.class, sb, "");

//            sb.append("---------------\n");
//            sb.append("CONTAINERS OWNERS\n");
//            sb.append("---------------\n");
//
//            fillCounts(radixObjectsContainerClass2count, RadixObject.class, sb, "");

            sb.append("---------------\n");
            sb.append("FILE SPACE\n");
            sb.append("---------------\n");

            fillCounts(class2fileSize, RadixObject.class, sb, "");

            sb.append("---------------\n");
            sb.append("PERFOMANCE\n");
            sb.append("---------------\n");

            for (Map.Entry<String, PerformanceLogger> entry : PerformanceLogger.getAll().entrySet()) {
                final String name = entry.getKey();
                final long ms = entry.getValue().getTotal() / 1000000;
                sb.append(name + ": " + ms + "msec\n");
            }

            return sb.toString();
        }

        @Override
        public boolean cancel() {
            provider.cancel();
            return true;
        }
    }

    @Override
    public void performAction() {
        final RadixObject context = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(RadixObject.class);
        if (context == null) {
            DialogUtils.messageError("There is no object selected.");
            return;
        }

        final VisitorProvider provider = VisitorProviderFactory.createDefaultVisitorProvider();
        final Collector collector = new Collector(provider);

        final ProgressHandle handle = ProgressHandleFactory.createHandle("View Information", collector);
        handle.start();
        try {
            context.visit(collector, provider);
            if (!provider.isCancelled()) {
                DialogUtils.showText(collector.getResult(), "Result", "txt");
            }
        } finally {
            handle.finish();
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ViewInformationAction.class, "CTL_ViewInformationAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
