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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ETransferType;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


class MoveUtils {

    private static RadixObject getTopObject(RadixObject object) {
        while (true) {
            RadixObject owner = object.getContainer();
            if (owner != null) {
                object = owner;
            } else {
                return object;
            }
        }
    }

    private static Set<RadixObject> getTopObjects(List<RadixObject> radixObjects) {
        final Set<RadixObject> result = new HashSet<RadixObject>();
        for (RadixObject radixObject : radixObjects) {
            final RadixObject topObject = getTopObject(radixObject);
            result.add(topObject);
        }
        return result;
    }

    private static Set<Module> getModules(List<RadixObject> movedObjects) {
        final Set<Module> result = new HashSet<Module>();
        for (RadixObject movedObject : movedObjects) {
            final Module module = movedObject.getModule();
            if (module != null && module != movedObject) {
                result.add(module);
            }
        }
        return result;
    }

    private static List<RadixObject> getRadixObjects(final Transferable transferable) {
        final List<ClipboardSupport.Transfer> transfers = ClipboardSupport.getTransfers(transferable);
        final List<RadixObject> movedObjects = new ArrayList<RadixObject>(transfers.size());
        for (ClipboardSupport.Transfer transfer : transfers) {
            final RadixObject obj = transfer.getObject();
            movedObjects.add(obj);
        }
        return movedObjects;
    }

    public static boolean isRefactoringMoveRequired(final Transferable transferable, final RadixObject destination) {
        final ETransferType transferType = ClipboardSupport.getTransferType(transferable);
        if (transferType != ETransferType.NONE) { // MOVE
            return false;
        }

        final Module destinationModule = destination.getModule();
        if (destinationModule == null) {
            return true; // module moved in another layer
        }

        final List<RadixObject> movedObjects = getRadixObjects(transferable);
        final Set<Module> sourceModules = getModules(movedObjects);
        if (sourceModules.isEmpty()) { // real cut
            return false;
        }

        final boolean movedIntoSameModule = Collections.singleton(destinationModule).equals(sourceModules);
        return !movedIntoSameModule;
    }

    private static void fixDependencies(final List<RadixObject> movedObjects, final RadixObject destination) {
        final Module destinationModule = destination.getModule();
        if (destinationModule == null) {
            return;
        }

        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Preparing to move");
        try {
            progressHandle.start();

            final Set<Module> sourceModules = getModules(movedObjects);

            // find all modules which refers to source modules
            final Set<RadixObject> topObjects = getTopObjects(movedObjects);
            final Set<Module> candidates = new HashSet<Module>();

            for (RadixObject topObject : topObjects) {
                topObject.visit(new IVisitor() {

                    @Override
                    public void accept(RadixObject radixObject) {
                        final Module module = (Module) radixObject;
                        if (module != destinationModule && !module.getDependences().contains(destinationModule)) {
                            for (Module sourceModule : sourceModules) {
                                if (module == sourceModule || module.getDependences().contains(sourceModule)) {
                                    candidates.add(module);
                                    break;
                                }
                            }
                        }
                    }
                }, VisitorProviderFactory.createModuleVisitorProvider());
            }

            progressHandle.setDisplayName("Fix dependencies");
            progressHandle.switchToDeterminate(candidates.size());
            int i = 0;

            // add dependence to destination module for modules which refers to moved objects
            for (final Module candidate : candidates) {
                final VisitorProvider provider = VisitorProviderFactory.createDefaultVisitorProvider();
                candidate.visitChildren(new IVisitor() {

                    @Override
                    public void accept(RadixObject radixObject) {
                        final List<Definition> definitions = new ArrayList<Definition>();
                        radixObject.collectDependences(definitions);
                        for (RadixObject definition : definitions) {
                            for (RadixObject movedObject : movedObjects) {
                                if (!(movedObject instanceof Module)) {
                                    if (movedObject.isParentOf(definition) || movedObject == definition) { // radixObject referes to moved object
                                        if (!movedObject.isParentOf(radixObject) && movedObject != radixObject) { // radixObject is not moved object
                                            candidate.getDependences().add(destinationModule);
                                            try {
                                                candidate.saveDescription();
                                            } catch (IOException cause) {
                                                DialogUtils.messageError(cause);
                                            }
                                            provider.cancel();
                                            return; // from accept(RadixObject);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, provider);

                progressHandle.progress(++i);
            }
        } finally {
            progressHandle.finish();
        }
    }

    private static Layer getLayer(RadixObject radixObject) {
        for (RadixObject container = radixObject; container != null; container = container.getContainer()) {
            if (container instanceof Layer) {
                return (Layer) container;
            }
        }
        throw new IllegalStateException();
    }

    private static void moveModule(Module module, RadixObject destination) {
        final FileObject moduleDir = RadixFileUtil.toFileObject(module.getDirectory());
        final Layer layer = getLayer(destination);
        final Segment segment = layer.getSegmentByType(module.getSegmentType());
        final FileObject segmentDir = RadixFileUtil.toFileObject(segment.getDirectory());
        final File destModuleDir = new File(FileUtil.toFile(segmentDir), module.getName());
        if (destModuleDir.exists()) {
            throw new RadixError("File already exists: " + destModuleDir.getAbsolutePath());
        }

        try {
            FileUtil.moveFile(moduleDir, segmentDir, moduleDir.getName());
        } catch (IOException cause) {
            throw new RadixObjectError("Unable to move module directory.", module, cause);
        }
    }

    private static void doMove(final Transferable transferable, final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final List<RadixObject> movedObjects = getRadixObjects(transferable);

        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Preparing to move");
        progressHandle.start();

        try {
            progressHandle.setDisplayName("Move");

            for (RadixObject obj : movedObjects) {
                try {
                    if (obj.getContainer() != null && !obj.canDelete()) {
                        throw new RadixObjectError("Unable to cut object.", obj);
                    }
                    if (obj instanceof Module) {
                        moveModule((Module) obj, destination);
                    }
                    obj.delete();
                } catch (RadixError cause) {
                    RadixObjectError error = new RadixObjectError("Unable to move object.", obj, cause);
                    DialogUtils.messageError(error);
                    return;
                }
            }

            destination.getClipboardSupport().paste(transferable, resolver);
        } finally {
            progressHandle.finish();
        }
    }

    public static void performRefactoringMove(final Transferable transferable, final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final List<RadixObject> movedObjects = getRadixObjects(transferable);
        if (MovePanel.showModal(movedObjects, destination)) {
            doMove(transferable, destination, resolver);
        }
    }
}
