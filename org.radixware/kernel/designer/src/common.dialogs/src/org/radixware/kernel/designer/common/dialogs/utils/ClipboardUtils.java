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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.MultiTransferObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ETransferType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

/**
 * RadixWare Designer utils for clipboard operations.
 *
 */
public class ClipboardUtils {

    public static class AskDuplicationResolver extends ClipboardSupport.DuplicationResolver {

        @Override
        public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
            if (isPasteMode()) {
                if (DialogUtils.messageConfirmation("Object already exists: " + oldObject.getQualifiedName() + "\n Replace with new object " + newObject.getQualifiedName() + "?")) {
                    return Resolution.REPLACE;
                } else {
                    return Resolution.CANCEL;
                }
            } else {
                return Resolution.REPLACE;
            }
        }
    }

    private ClipboardUtils() {
    }

    // ==================== UTILS ================
    public static Clipboard getClipboard() {
        Clipboard c = Lookup.getDefault().lookup(Clipboard.class);

        if (c == null) {
            c = Toolkit.getDefaultToolkit().getSystemClipboard();
        }

        return c;
    }

    public static void copyToClipboard(final Transferable transferable) {
        final Clipboard clipboard = getClipboard();
        clipboard.setContents(transferable, new StringSelection(""));
    }

    public static void copyToClipboard(final String s) {
        final StringSelection stringSelection = new StringSelection(s);
        final Clipboard clipboard = getClipboard();
        clipboard.setContents(stringSelection, new StringSelection(""));
    }

    // ==================== COPY ================
    public static boolean canCopy(final RadixObject radixObject) {
        final ClipboardSupport cs = radixObject.getClipboardSupport();
        return cs != null && cs.canCopy();
    }

    public static boolean canCopy(final List<RadixObject> radixObjects) {
        if (radixObjects.isEmpty()) {
            return false;
        }
        for (RadixObject radixObject : radixObjects) {
            if (!canCopy(radixObject)) {
                return false;
            }
        }
        return true;
    }

    public static void copyToClipboard(final List<RadixObject> radixObjects) {
        final Transferable transferable = ClipboardSupport.createTransferable(radixObjects, ETransferType.DUPLICATE);
        copyToClipboard(transferable);
    }

    public static void copyToClipboard(final RadixObject radixObject) {
        final Transferable transferable = radixObject.getClipboardSupport().createTransferable(ETransferType.DUPLICATE);
        copyToClipboard(transferable);
    }

    // ==================== CUT ================
    public static boolean canCut(final RadixObject radixObject) {
        final ClipboardSupport cs = radixObject.getClipboardSupport();
        return cs != null && cs.canCut();
    }

    public static boolean canCut(final List<RadixObject> radixObjects) {
        if (radixObjects.isEmpty()) {
            return false;
        }
        for (RadixObject radixObject : radixObjects) {
            if (!canCut(radixObject)) {
                return false;
            }
        }
        return true;
    }

    public static void cutToClipboard(final RadixObject radixObject) {
        final Transferable transferable = radixObject.getClipboardSupport().createTransferable(ETransferType.NONE);
        radixObject.delete();
        copyToClipboard(transferable);
    }

    public static void cutToClipboard(final List<RadixObject> radixObjects) {
        final Transferable transferable = ClipboardSupport.createTransferable(radixObjects, ETransferType.NONE);
        for (RadixObject radixObject : radixObjects) {
            radixObject.delete();
        }
        copyToClipboard(transferable);
    }

    // ==================== PASTE ================
    public static boolean canPaste(final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final Clipboard clipboard = getClipboard();
        final Transferable transferable = clipboard.getContents(destination);
        if (transferable == null) {
            return false;
        }
        final ClipboardSupport cs = destination.getClipboardSupport();
        return cs != null && cs.canPaste(transferable, resolver) == ClipboardSupport.CanPasteResult.YES;
    }

    /**
     * Perform the paste action: paste objects from clipboard into destination
     * and update clipboard if required.
     */
    public static void paste(final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final Clipboard clipboard = getClipboard();
        final Transferable transferable = clipboard.getContents(destination);
        final Transferable newTransferable = paste(transferable, destination, resolver);
        if (newTransferable != null) {
            copyToClipboard(newTransferable);
        }
    }

    // Modal editors already on write access
    public static void pasteInModalEditor(final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final Clipboard clipboard = getClipboard();
        final Transferable transferable = clipboard.getContents(destination);
        final ClipboardSupport cs = destination.getClipboardSupport();
        final List<RadixObject> pasted = cs.paste(transferable, resolver);
        final Transferable newTransferable = ClipboardSupport.createTransferable(pasted, ETransferType.DUPLICATE);
        copyToClipboard(newTransferable);
    }

    public static Transferable[] parseNbMultiTransferable(Transferable t) {
        final MultiTransferObject obj;
        try {
            obj = (MultiTransferObject) t.getTransferData(ExTransferable.multiFlavor);
        } catch (UnsupportedFlavorException ex) {
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        int count = obj.getCount();
        final Transferable[] transferables = new Transferable[count];
        for (int i = 0; i < count; i++) {
            transferables[i] = obj.getTransferableAt(i);
        }
        return transferables;
    }

    /**
     * Paste objects from clipboard into destination.
     *
     * @return transferable which should be inserted into the clipboard after
     * the paste action. It can be null, meaning that the clipboard content is
     * not affected, or {@link ExTransferable#EMPTY} to clear it. Used by
     * RadixObjectNode.
     */
    public static Transferable paste(final Transferable transferable, final RadixObject destination, final ClipboardSupport.DuplicationResolver resolver) {
        final Transferable finalTransferable;
        if (transferable.isDataFlavorSupported(ExTransferable.multiFlavor)) {
            final Transferable[] transferables = parseNbMultiTransferable(transferable);
            finalTransferable = ClipboardSupport.merge(transferables);
        } else {
            finalTransferable = transferable;
        }

        if (MoveUtils.isRefactoringMoveRequired(finalTransferable, destination)) {
            MoveUtils.performRefactoringMove(finalTransferable, destination, resolver);
            return ExTransferable.EMPTY;
        } else {
            return RadixMutex.writeAccess(new Mutex.Action<Transferable>() {

                @Override
                public Transferable run() {
                    final ClipboardSupport cs = destination.getClipboardSupport();
                    final List<RadixObject> pasted = cs.paste(finalTransferable, resolver);
                    final Transferable newTransferable = ClipboardSupport.createTransferable(pasted, ETransferType.DUPLICATE);
                    return newTransferable;
                }
            });
        }
    }

    public static ClipboardSupport.CanPasteResult canPaste(Transferable transferable, final RadixObject destination, ClipboardSupport.DuplicationResolver resolver) {
        final ClipboardSupport cs = destination.getClipboardSupport();
        if (cs == null) {
            return ClipboardSupport.CanPasteResult.NO;
        }

        if (transferable.isDataFlavorSupported(ExTransferable.multiFlavor)) {
            final Transferable[] transferables = parseNbMultiTransferable(transferable);
            for (Transferable t : transferables) {
                if (cs.canPaste(t, resolver) != ClipboardSupport.CanPasteResult.YES) {
                    return ClipboardSupport.CanPasteResult.NO;
                }
            }
            return ClipboardSupport.CanPasteResult.YES;
        } else {
            return cs.canPaste(transferable, resolver);
        }
    }

    public static Set<RadixObject> getCutObjects() {
        final Clipboard clipboard = getClipboard();
        final Transferable transferable = clipboard.getContents(null);
        final Set<RadixObject> result = new HashSet<RadixObject>();

        if (transferable.isDataFlavorSupported(ExTransferable.multiFlavor)) {
            final Transferable[] transferables = parseNbMultiTransferable(transferable);
            for (Transferable t : transferables) {
                if (ClipboardSupport.getTransferType(t) == ETransferType.NONE) {
                    List<Transfer> transfers = ClipboardSupport.getTransfers(t);
                    for (Transfer transfer : transfers) {
                        result.add(transfer.getObject());
                    }
                }
            }
        } else {
            if (ClipboardSupport.getTransferType(transferable) == ETransferType.NONE) {
                final List<Transfer> transfers = ClipboardSupport.getTransfers(transferable);
                for (Transfer transfer : transfers) {
                    result.add(transfer.getObject());
                }
            }
        }

        return result;
    }
}
