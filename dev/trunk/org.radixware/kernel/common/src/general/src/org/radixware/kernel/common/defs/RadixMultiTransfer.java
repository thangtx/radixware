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
package org.radixware.kernel.common.defs;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.commondef.CbInfo;
import org.radixware.schemas.commondef.ClipboardInfoDocument;

/**
 * Data object for RadixTransferable. Contains transfers and transfer type.
 *
 */
class RadixMultiTransfer {

    private final List<ClipboardSupport.Transfer> transfers;
    private final ETransferType transferType;

    private RadixMultiTransfer(final List<ClipboardSupport.Transfer> transfers, final ETransferType transferType) {
        this.transfers = transfers;
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return super.toString() + ". TransferType: " + String.valueOf(transferType) + ". Transfer: " + String.valueOf(transfers);
    }

    public static final class Factory {

        private Factory() {
        }

        public static RadixMultiTransfer newInstance(final List<RadixObject> radixObjects, final ETransferType transferType) {
            final int size = radixObjects.size();
            List<ClipboardSupport.Transfer> transfers = new ArrayList<>(size);
            for (RadixObject radixObject : radixObjects) {
                final ClipboardSupport.Transfer transfer = radixObject.getClipboardSupport().newTransferInstance();
                transfers.add(transfer);
            }

            final List<RadixObject> objectsForClipboard;

            switch (transferType) {
                case DUPLICATE:
                    objectsForClipboard = ClipboardSupport.duplicate(radixObjects);
                    break;
                case COPY:
                    objectsForClipboard = ClipboardSupport.copy(radixObjects);
                    break;
                case NONE:
                    objectsForClipboard = radixObjects;
                    break;
                default:
                    throw new IllegalStateException();
            }

            for (int i = 0; i < size; i++) {
                final RadixObject objectForClipboard = objectsForClipboard.get(i);
                final ClipboardSupport.Transfer transfer = transfers.get(i);
                transfer.setObject(objectForClipboard);
            }

            for (ClipboardSupport.Transfer transfer : transfers) {
                switch (transferType) {
                    case DUPLICATE:
                        transfer.afterDuplicate();
                        break;
                    case COPY:
                        transfer.afterCopy();
                        break;
                }
            }
            return new RadixMultiTransfer(transfers, transferType);
        }

        public static RadixMultiTransfer newInstance(final RadixObject radixObject, final ETransferType transferType) {
            return newInstance(Collections.singletonList(radixObject), transferType);
        }

        public static RadixMultiTransfer merge(final Transferable[] transferables) {
            final List<ClipboardSupport.Transfer> allTransfers = new ArrayList<>();
            ETransferType transferType = null;

            for (Transferable t : transferables) {
                List<ClipboardSupport.Transfer> transfers = ClipboardSupport.getTransfers(t);
                if (transfers == null) {
                    return null;
                }
                allTransfers.addAll(transfers);
                final ETransferType curTransferType = ClipboardSupport.getTransferType(t);
                if (transferType == null) {
                    transferType = curTransferType;
                } else if (curTransferType != transferType) {
                    return null;
                }
            }

            return new RadixMultiTransfer(allTransfers, transferType);
        }

        public static RadixMultiTransfer loadFrom(Transferable transferable) {
            if (!transferable.isDataFlavorSupported(ClipboardSupport.RADIX_DATA_FLAVOR)) {
                if (transferable.isDataFlavorSupported(ClipboardSupport.SERIALIZED_RADIX_TRANSFER)) {
                    Object transfer;
                    try {
                        transfer = transferable.getTransferData(ClipboardSupport.SERIALIZED_RADIX_TRANSFER);
                        if (transfer instanceof String) {
                            try {
                                ClipboardInfoDocument xDoc = ClipboardInfoDocument.Factory.parse((String) transfer);
                                CbInfo xInfo = xDoc.getClipboardInfo();
                                if (xInfo != null && xInfo.getDecoderClass() != null && xInfo.getDecoderMethod() != null && xInfo.getDefinitionXmlClass() != null) {
                                    try {
                                        Class c = RadixMultiTransfer.class.getClassLoader().loadClass(xInfo.getDecoderClass());
                                        Class argClass = RadixMultiTransfer.class.getClassLoader().loadClass(xInfo.getDefinitionXmlClass());

                                        try {
                                            Method m = c.getDeclaredMethod(xInfo.getDecoderMethod(), argClass);
                                            Object decoderArgument = XmlObjectProcessor.castToXmlClass(argClass.getClassLoader(), xInfo.getDefinition(), argClass);
                                            if (decoderArgument != null) {
                                                if (m != null) {
                                                    try {
                                                        Object result = m.invoke(null, decoderArgument);
                                                        if (result instanceof RadixObject) {
                                                            ClipboardSupport support = ((RadixObject) result).getClipboardSupport();
                                                            if (support != null) {
                                                                try {
                                                                    ClipboardSupport.Transfer cbTransfer = support.decode(xInfo);
                                                                    if (cbTransfer != null) {
                                                                        return new RadixMultiTransfer(Collections.singletonList(cbTransfer), ETransferType.COPY);
                                                                    }
                                                                } catch (Throwable e) {
                                                                    return null;
                                                                }
                                                            }
                                                        }
                                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                                        Logger.getLogger(RadixMultiTransfer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                                                    }
                                                }
                                            }
                                        } catch (NoSuchMethodException | SecurityException ex) {
                                            Logger.getLogger(RadixMultiTransfer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                                        }
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(RadixMultiTransfer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                                    }
                                }
                            } catch (XmlException ex) {
                                Logger.getLogger(RadixMultiTransfer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Logger.getLogger(RadixMultiTransfer.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                return null;
            }

            try {
                final Object transfer = transferable.getTransferData(ClipboardSupport.RADIX_DATA_FLAVOR);
                return (RadixMultiTransfer) transfer;
            } catch (UnsupportedFlavorException | IOException cause) {
                throw new IllegalStateException(cause);
            }
        }
    }

    public List<ClipboardSupport.Transfer> getTransfers() {
        return transfers;
    }

    public ETransferType getTransferType() {
        return transferType;
    }

    public Transferable toTransferable() {
        return new RadixTransferable(this);
    }

    private static final class RadixTransferable implements Transferable {

        private final RadixMultiTransfer multiTransfer;
        private final String asString;
        private final String asEncodedString;

        public RadixTransferable(RadixMultiTransfer transfer) {
            this.multiTransfer = transfer;
            if (!transfer.getTransfers().isEmpty()) {
                final ClipboardSupport.Transfer radixTransfer = transfer.getTransfers().get(0);
                final RadixObject radixObject = radixTransfer.getObject();
                if (radixObject.getClipboardSupport().canCopy()) {
                    XmlObject asXml = radixObject.getClipboardSupport().copyToXml();
                    this.asString = asXml == null ? null : asXml.xmlText();
                    if (radixObject.getClipboardSupport().isEncodedFormatSupported()) {
                        this.asEncodedString = radixObject.getClipboardSupport().encode(radixTransfer);
                    } else {
                        this.asEncodedString = null;
                    }
                } else {
                    this.asString = null;
                    this.asEncodedString = null;
                }

            } else {
                this.asString = null;
                this.asEncodedString = null;
            }
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (ClipboardSupport.RADIX_DATA_FLAVOR.equals(flavor)) {
                return multiTransfer;
            } else if (ClipboardSupport.SERIALIZED_RADIX_TRANSFER.equals(flavor)) {
                return asEncodedString;
            } else if (DataFlavor.stringFlavor.equals(flavor)) {
                return asString;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            List<DataFlavor> supportedFlavors = new LinkedList<>();
            if (asString != null) {
                supportedFlavors.add(DataFlavor.stringFlavor);
            }
            if (asEncodedString != null) {
                supportedFlavors.add(ClipboardSupport.SERIALIZED_RADIX_TRANSFER);
            }
            supportedFlavors.add(ClipboardSupport.RADIX_DATA_FLAVOR);
            return supportedFlavors.toArray(new DataFlavor[supportedFlavors.size()]);
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return ClipboardSupport.RADIX_DATA_FLAVOR.equals(flavor) || (asString != null && DataFlavor.stringFlavor.equals(flavor));
        }

        @Override
        public String toString() {
            return super.toString() + ". MultiTransfer: " + String.valueOf(multiTransfer);
        }
    }
}
