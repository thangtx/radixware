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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.openide.util.Mutex.Action;
import org.openide.util.Mutex.ExceptionAction;
import org.openide.util.MutexException;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

/**
 * Synchronization between file and its Radix object in memory.
 */
abstract class RadixObjectUploader<T extends RadixObject> {

    private static final int REFRESH_PERIOD_MILLISECONDS = 500;
    protected static final Logger LOGGER = Logger.getLogger(BranchUploader.class.getName());
    private static final IgnoreList ignoreList = IgnoreList.getDefault();
    private final T radixObject;

    public RadixObjectUploader(T radixObject) {
        this.radixObject = radixObject;
    }

    public T getRadixObject() {
        return radixObject;
    }

    /**
     * Close radix object (remove from memory but don't delete any file).
     */
    public abstract void close();

    /**
     * Update children or Radix object.
     */
    public abstract void updateChildren();

    /**
     * Reload Radix object.
     * Can recreate it.
     */
    public abstract void reload() throws IOException;

    /**
     * Get last time of Radix object saving or loading.
     */
    public abstract long getRememberedFileTime();

    public String getObjectTitleLowerCase() {
        final String typeTitle = radixObject.getTypeTitle().toLowerCase();
        final String qualifiedName = radixObject.getQualifiedName();
        return typeTitle + " '" + qualifiedName + "'";
    }

    public static String getObjectTitle(RadixObject radixObject) {
        final String typeTitle = radixObject.getTypeTitle();
        final String qualifiedName = radixObject.getQualifiedName();
        return typeTitle + " '" + qualifiedName + "'";
    }

    public String getObjectTitle() {
        return getObjectTitle(radixObject);
    }

    private void doClose() {
        final String status = getObjectTitle() + " closed."; // before close, to get qualified name
        close();
        AbstractRadixObjectUploaderUI.getDefault().logStatus(status);
    }

    private void doReload() throws IOException {
        final String status = getObjectTitle() + " reloaded."; // before reload, to get qualified name
        reload();
        AbstractRadixObjectUploaderUI.getDefault().logStatus(status);
    }

    private static void waitSomeTime(IOException cause) {
        LOGGER.fine(cause.getMessage());
        if (!AbstractRadixObjectUploaderUI.getDefault().isIgnoreAll()) {
            try {
                TimeUnit.MILLISECONDS.sleep(REFRESH_PERIOD_MILLISECONDS);
            } catch (InterruptedException interrupt) {
                LOGGER.severe(interrupt.getMessage());
                return;
            }
        }
    }

    private void tryReload(final File currentFile) {
        final boolean reload;

        if (radixObject.getEditState() != RadixObject.EEditState.NONE) {
            reload = AbstractRadixObjectUploaderUI.getDefault().confirmReload(
                    getObjectTitle() + "\n"
                    + "was modified externally and locally.\n"
                    + "Reload it?");
        } else {
            reload = true;
        }

        if (!reload) {
            ignoreList.add(currentFile);
            return;
        }

        try {
            doReload();
        } catch (IOException firstErrorIgnored) {
            waitSomeTime(firstErrorIgnored);

            while (true) {
                try {
                    doReload();
                    break;
                } catch (IOException cause) {
                    final boolean tryAgain = AbstractRadixObjectUploaderUI.getDefault().confirmRetry(
                            "Unable to reload " + getObjectTitleLowerCase() + "\n"
                            + "that was modified externally.\n"
                            + //"Cause: " + cause.getMessage() + "\n" +
                            "Try again?",
                            cause);
                    if (!tryAgain) {
                        final boolean close = AbstractRadixObjectUploaderUI.getDefault().confirmClose(
                                getObjectTitle() + "\n"
                                + "was erroneously modified externally.\n"
                                + "It is possible to close it or restore by save.\n"
                                + "Close " + getRadixObject().getTypeTitle().toLowerCase() + "?");

                        ignoreList.add(currentFile);

                        if (close) {
                            doClose();
                        }
                        break;
                    }
                }
            }
        }
    }

    private enum EUpdateResult {

        NONE,
        NEED_TO_UPDATE_CHILDS
    }
    
    protected boolean needClose(){
        final long rememberedFileTime = getRememberedFileTime();
        return rememberedFileTime > 0;
    }

    private EUpdateResult doUpdateOnWriteAccess() {
        final File file = radixObject.getFile();
        if (file == null && !(radixObject instanceof Branch)) {//for inspector branch is always branch.getFile()==null
            return EUpdateResult.NONE; // deleted from branch.
        }

        final long rememberedFileTime = getRememberedFileTime();

        if (file != null && file.isFile()) {
            if (file.lastModified() != rememberedFileTime && !ignoreList.contains(file)) { // file was modified externally and was not added into ignore for reload list.
                tryReload(file); // can recreate Radix object
            } else {
                return EUpdateResult.NEED_TO_UPDATE_CHILDS;
            }
        } else if (needClose() && !ignoreList.contains(file)) { // some file was removed externally and was not added into ignore for close list.
            final boolean close;
            if (hasModifiedInside()) {
                close = AbstractRadixObjectUploaderUI.getDefault().confirmClose(
                        getObjectTitle() + "\n"
                        + "was deleted externally, but has modified elements.\n"
                        + "It is possible to close it or restore by save.\n"
                        + "Close " + getRadixObject().getTypeTitle().toLowerCase() + "?");
            } else {
                close = true;
            }

            if (close) {
                doClose();
            } else {
                ignoreList.add(file);
            }
        } else if (rememberedFileTime == 0L) {
            return EUpdateResult.NEED_TO_UPDATE_CHILDS; // for DdsScripts
        }

        return EUpdateResult.NONE;
    }

    /**
     * Synchronize object in memory with its file.
     */
    public void update() {
        final EUpdateResult updateResult = RadixMutex.writeAccess(new Action<EUpdateResult>() {

            @Override
            public EUpdateResult run() {
                return doUpdateOnWriteAccess();
            }
        });

        if (updateResult == EUpdateResult.NEED_TO_UPDATE_CHILDS) {
            updateChildren();
        }
    }

    /**
     * Load child of the Radix object.
     * @param file - file that was called in tryToLoadChild(...).
     * @throws java.io.IOException if unable to load.
     */
    public abstract RadixObject uploadChild(final File file) throws IOException;

    private void uploadChildWithWriteAccess(final File file) throws IOException {
        try {
            RadixMutex.writeAccess(new ExceptionAction<Void>() {

                @Override
                public Void run() throws IOException {
                    final RadixObject newRadixObject = uploadChild(file);
                    if (newRadixObject != null) {
                        AbstractRadixObjectUploaderUI.getDefault().logStatus(getObjectTitle(newRadixObject) + " loaded.");
                    }
                    return null;
                }
            });
        } catch (MutexException exception) {
            throw (IOException) exception.getException();
        }
    }

    /**
     * Calls loadChild(primaryFile) and retry until successful or user canceled.
     * @param primaryFile - file that was passed in loadChild(...), also used for checking that file is in ignore list.
     * @param childTypeTitle - title of child type, used in confirmation for reload dialog.
     * @throws java.lang.InterruptedException
     */
    public final void tryToUploadChild(final File primaryFile, String childTypeTitle) {
        if (primaryFile == null) {
            return;
        }

        if (ignoreList.contains(primaryFile)) {
            return;
        }

        try {
            uploadChildWithWriteAccess(primaryFile);
        } catch (IOException firstErrorIgnored) {
            waitSomeTime(firstErrorIgnored);

            while (true) {
                try {
                    uploadChildWithWriteAccess(primaryFile);
                    break;
                } catch (IOException cause) {
                    boolean tryAgain = AbstractRadixObjectUploaderUI.getDefault().confirmRetry(
                            "Unable to load " + childTypeTitle.toLowerCase() + " of " + getObjectTitleLowerCase() + ".\n"
                            + //"Cause: " + cause.getMessage() + "\n" +
                            "Try again?",
                            cause);
                    if (!tryAgain) {
                        ignoreList.add(primaryFile);
                        break;
                    }
                }
            }
        }
    }

    private static class ModifiedSearcher implements IVisitor {

        boolean result = false;

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject.getEditState() != RadixObject.EEditState.NONE) {
                result = true;
            }
        }
    }

    /**
     * @return true if Radix object has modified elements inside, false otherwise.
     */
    private boolean hasModifiedInside() {
        ModifiedSearcher searcher = new ModifiedSearcher();
        radixObject.visit(searcher, VisitorProviderFactory.createDefaultVisitorProvider());
        return searcher.result;
    }
}
