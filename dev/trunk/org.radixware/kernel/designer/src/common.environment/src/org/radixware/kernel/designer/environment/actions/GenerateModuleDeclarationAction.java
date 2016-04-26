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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.ContextAwareAction;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public abstract class GenerateModuleDeclarationAction extends AbstractContextAwareAction implements ActionListener{

    private Collection<? extends RadixObject> contexts;

    public GenerateModuleDeclarationAction(Collection<? extends RadixObject> contexts) {
        this.contexts = contexts;
    }

    public GenerateModuleDeclarationAction() {
    }

    protected abstract void generateDeclaration(AdsModule module) throws IOException;

    protected abstract String declarationDescription();
    private boolean isCancelled = false;

    private void collectModules(RadixObject obj, Set<AdsModule> modules) {
        if (!obj.isInBranch()) {
            return;
        }
        for (RadixObject c = obj; c != null; c = c.getContainer()) {
            if (c instanceof AdsModule && !c.isReadOnly()) {
                modules.add((AdsModule) c);
                break;
            } else if (c instanceof DdsModule || c instanceof DdsSegment) {
                break;
            } else if (c instanceof AdsSegment) {
                addAll((AdsSegment) c, modules);
                break;
            } else if (c instanceof Layer) {
                addAll((AdsSegment) ((Layer) c).getAds(), modules);
                break;
            } else if (c instanceof Branch) {
                for (Layer l : ((Branch) c).getLayers()) {
                    addAll((AdsSegment) l.getAds(), modules);
                }
                break;
            }
        }
    }

    private void addAll(AdsSegment s, Set<AdsModule> modules) {
        if (!s.isReadOnly()) {
            for (AdsModule m : s.getModules()) {
                modules.add(m);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (contexts == null || contexts.isEmpty()) {
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                RadixMutex.writeAccess(new Runnable() {

                    @Override
                    public void run() {
                        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Generate " + declarationDescription() + "...", new Cancellable() {

                            @Override
                            public boolean cancel() {
                                isCancelled = true;
                                return true;
                            }
                        });
                        progressHandle.start();
                        try {
                            progressHandle.progress("Collecting modules...");
                            final Set<AdsModule> modules = new HashSet<AdsModule>();
                            for (RadixObject obj : contexts) {
                                collectModules(obj, modules);
                            }
                            progressHandle.switchToDeterminate(modules.size());
                            int current = 0;
                            for (AdsModule m : modules) {
                                if (isCancelled) {
                                    return;
                                }
                                try {
                                    String message = "Updating " + declarationDescription() + " " + m.getQualifiedName();
                                    progressHandle.progress(message, current);
                                    generateDeclaration(m);
                                    progressHandle.progress(message, current + 1);
                                    current++;
                                } catch (IOException ex) {
                                    if (!DialogUtils.messageErrorWithIgnore(ex.getMessage())) {
                                        isCancelled = true;
                                        break;
                                    }
                                }
                            }
                        } finally {
                            progressHandle.finish();
                        }
                    }
                });
            }
        });
    }
    
}
