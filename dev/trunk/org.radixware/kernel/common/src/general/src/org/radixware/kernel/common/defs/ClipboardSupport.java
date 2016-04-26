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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.CbStringList;
import org.radixware.schemas.commondef.CbInfo;
import org.radixware.schemas.commondef.ClipboardInfoDocument;

/**
 * Clipboard support for RadixObject. See
 * {@linkplain RadixObject#getClipboardSupport()}. Логика работы буфера обмена:
 * 1. Копирование: * Имеется список RadixObject. * Проверяем для каждого
 * radixObject.getClipboardSupport()!=null &&
 * radixObject.getClipboardSupport().canCopy(). * Для каждого генерим Transfer:
 * radixObject.getClipboardSupport().newTransferInstance(). * В Transfer
 * сохранена дополнительная для переноса информация, например, используемые
 * многоязыковые строки. * Собираем список идентификаторов всех копируемых
 * дефиниций и поддефиниций, фильтруем по definition.getClipboardSupport()==null
 * || definition.getClipboardSupport().isIdChangeRequired(). * Фильтрация нужна,
 * чтобы отсеить замену автогенерируемых или зашитых в код идентификаторов. *
 * Каждый объект сохраняем в XML, при помощи
 * radixObject.getClipboardSupport().copyToXml(). * В полученных XML простой
 * заменой в тексте меняем старые идентификаторы на новые. * Таким образом
 * обновятся все внутренние ссылки. * Например, если скопировали две таблицы и
 * связь между ними, то получим две новые таблицы, новую связь и новая связь
 * будет ссылаться уже на новые таблицы. * При помощи
 * radixObject.getClipboardSupport().loadFrom(xmlObject) восстанавливаем объект,
 * получаем дубликат. * В каждом трансфере приватно подменяем указатель на
 * дубликат: transfer.setObject(duplicate). * Для каждого transfer вызывается
 * обработчик afterDuplicate(). * Все трансферы оборачиваем в приватный
 * RadixMultiTransfer. * RadixMultiTransfer является объектом с данными для
 * приватного RadixTransferable (implements java.awt.datatransfer.Transferable).
 * * Полученный Transferable может быть вставлен напрямую через
 * destination.getClipboardSupport().paste(transferable), * или положен в буфер
 * обмена стандартными срествами, см.
 * {@linkplain java.awt.datatransfer.Clipboard#setContents(java.awt.datatransfer.Transferable, java.awt.datatransfer.ClipboardOwner)}.
 * * Т.к. в Netbeans буфер обмена перекрыт, то эта операция стандартизирована
 * только в org.radixware.kernel.designer.common.dialogs.ClipboardUtils. 2.
 * Вставка: * Проверяем, что вставка допустима:
 * destination.getClipboardSupport()!=null &&
 * destination.getClipboardSupport().canPaste(transferable): * Transferable
 * приватно проверяется на RadixTransferable. * Достаются все transfers. *
 * Проверяется destination.getClipboardSupport().canPaste(transfers). *
 * Вызывается destination.getClipboardSupport().paste(transfers). * Вызывается
 * обработчик trasfer.afterPaste() (например, обновить авто-генерируемые имена).
 * 3. Cut: * Аналогично копированию, только дубликат не создается, * в Transfer
 * сохраняется указатель на оригинальный объект, который предварительно
 * удаляется. 4. Drag&Drop: * Аналогичен Cut, только удаление происходит перед
 * вставкой, иначе только потащили, а он уже удалился. * Т.к. в Netbeans
 * Drug&Drop работает через Cut, то вырезание из дерева работает так же. 5.
 * Override * Механизм перекрытия дефиниций работает через ClipboardSupport. *
 * Для объектов создаются точные копии. * Вместо transfer.afterDuplicate()
 * вызывается transfer.afterCopy(). 6.
 * org.radixware.kernel.designer.common.dialogs.utils.IMoveSupport *
 * Дополнительный обработчик в дизайнере, помогаем переносить объекты. *
 * Планируются Wizard'ы. * Для него в RadixMultiTransfer сохраняется
 * ETransferType, и если он был None, то значит происходит перенос объектов. *
 * Также для него вытащены ф-и ClipboardSupport.getTransfers(transferable) * и
 * ClipboardSupport.getTransferType(transferable). 7. После
 * ClipboardUtils.paste(destination) вставленные объекты копируются в буфер
 * обмена (через duplicate).
 *
 * Примеры использования: 1. Copy to clipboard:
 * ClipboardUtils.canCopy(radixObjects); ClipboardUtils.copy(radixObjects); 2.
 * Cut to clipboard: ClipboardUtils.canCut(radixObjects);
 * ClipboardUtils.cut(radixObjects); 3. Paste from clipboard:
 * ClipboardUtils.canPaste(destination); ClipboardUtils.paste(destination); 4.
 * Copy: Transferable t =
 * radixObject.getClipboardSupport().createTransferable(ETransferType.DUPLICATE);
 * или массово: t = ClipboardSupport.createTransferable(radixObjects,
 * ETransferType.DUPLICATE); destination.getClipboardSupport().paste(t); 5.
 * Move: Transferable t =
 * radixObject.getClipboardSupport().createTransferable(ETransferType.NONE);
 * destination.getClipboardSupport().paste(t); 6. Override: Transferable t =
 * radixObject.getClipboardSupport().createTransferable(ETransferType.COPY);
 * destination.getClipboardSupport().paste(t);
 *
 */
public abstract class ClipboardSupport<T extends RadixObject> {

    private static final class RadixDataFlavor extends DataFlavor {

        private RadixDataFlavor() {
            super(RadixObject.class, "RadixObjects");
        }
    }
    public static final DataFlavor SERIALIZED_RADIX_TRANSFER = new DataFlavor("application/x-radix-serialized-object;class=java.lang.String", "Serialized ADS transfer");
    public static final DataFlavor RADIX_DATA_FLAVOR = new RadixDataFlavor();

    /**
     * One of objects in {@linkplain Transferable}. Contains radix object. Can
     * contain additional information required for transfer, for example, used
     * multilingual strings.
     */
    public static class Transfer<T extends RadixObject> {

        private T radixObject;
        private final T initialObject;

        protected Transfer(T radixObject) {
            this.radixObject = radixObject;
            this.initialObject = radixObject;
        }

        public T getObject() {
            return radixObject;
        }

        /**
         * Get initial object with the help of was created copy or duplicate.
         */
        public T getInitialObject() {
            return initialObject;
        }

        /**
         * Used to replace object by its copy of duplicate.
         */
        protected void setObject(T radixObject) {
            this.radixObject = radixObject;
        }

        protected void afterDuplicate() {
        }

        protected void afterCopy() {
        }

        protected boolean beforePaste(RadixObjects container) {
            return true;
        }

        protected void afterPaste() {
        }

        @Override
        public String toString() {
            return super.toString() + ". RadixObject: " + String.valueOf(radixObject);
        }

        protected XmlObject copyToXml() {
            return getObject().getClipboardSupport().copyToXml();
        }

        /**
         * Restore object from XML. It is actual only if {@linkplain #canCopy()
         * } return true.
         */
        protected static RadixObject loadFrom(RadixObject context, XmlObject xmlObject) {
            return context.getClipboardSupport().loadFrom(xmlObject);
        }
    }
    protected final T radixObject;

    public ClipboardSupport(T radixObject) {
        this.radixObject = radixObject;
    }

    /**
     * Object can support paste operation and doesn't support copy operation. In
     * this case it is possible to return false in this function, and skip
     * realization of copyToXml and loadFrom functions. It is used, for example,
     * in collections of RadixObject.
     *
     * @return true if the copy operation is realized for object, false
     * otherwise, true by default.
     */
    public boolean canCopy() {
        return true;
    }

    /**
     * @return true if it is possible to cut object. Default realization based
     * on RadixObjects. The function is final - realize {@linkplain #canCopy()}
     * and {@linkplain RadixObject#canDelete() }.
     */
    public final boolean canCut() { // final - override canCopy and RadixObject.canDelete
        return canCopy() && radixObject.canDelete();
    }

    public enum CanPasteResult {

        YES,
        NO,
        NO_DUPLICATE
    }

    /**
     * Designer for overwriting and delegation.
     *
     * @return true it is possible to paste specified transfers in this object
     * now. Default realization based RadixObjects and on
     * {@linkplain RadixObject#isReadOnly()}.
     */
//    public final CanPasteResult canPaste(List<Transfer> transfers) {
//        return canPaste(transfers, new DuplicationResolver() {
//
//            @Override
//            public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
//                return Resolution.CANCEL;
//            }
//        });
//    }
    public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        final RadixObject container = radixObject.getContainer();
        if (!(container instanceof RadixObjects)) {
            return CanPasteResult.NO;
        }

        if (container.isReadOnly()) {
            return CanPasteResult.NO;
        }

        for (Transfer transfer : transfers) {
            final RadixObject objectInClipboard = transfer.getObject();
            if (objectInClipboard == radixObject) {
                return CanPasteResult.NO;
            }
            if (objectInClipboard.getContainer() != null && !objectInClipboard.canDelete()) {
                return CanPasteResult.NO;
            }
        }

        final ClipboardSupport containerClipboardSupport = container.getClipboardSupport();
        return containerClipboardSupport != null ? containerClipboardSupport.canPaste(transfers, resolver) : CanPasteResult.NO;
    }

    /**
     * @return true if it is possible to paste specified transferable into the
     * current object, false otherwise. Default realization based on private
     * RadixTransferable class. Final for common logic.
     */
    public final CanPasteResult canPaste(Transferable transferable, DuplicationResolver resolver) {
        final RadixMultiTransfer multiTransfer = RadixMultiTransfer.Factory.loadFrom(transferable);
        if (multiTransfer != null) {
            final List<Transfer> transfers = multiTransfer.getTransfers();
            return canPaste(transfers, resolver);
        } else {
            return CanPasteResult.NO;
        }
    }

    public static abstract class DuplicationResolver {

        private boolean isPasteMode;

        public enum Resolution {

            REPLACE,
            CANCEL
        }

        public abstract Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject);

        public void enterPasteMode() {
            synchronized (this) {
                isPasteMode = true;
            }
        }

        public void leavePasteMode() {
            synchronized (this) {
                isPasteMode = false;
            }
        }

        public boolean isPasteMode() {
            synchronized (this) {
                return isPasteMode;
            }
        }
    }

    public static final class DefaultDuplicationResolver extends DuplicationResolver {

        @Override
        public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
            return Resolution.CANCEL;
        }
    }

    /**
     * Call canPaste and throws RadixObjectError if canPaste returns false.
     * Simplifies frequent usage.
     */
//    protected final void checkForCanPaste(List<Transfer> transfers) {
//        if (canPaste(transfers) != CanPasteResult.YES) {
//            throw new RadixObjectError("Attempt to paste when canPaste() returns false.", radixObject);
//        }
//    }
    protected final boolean checkForCanPaste(List<Transfer> transfers, DuplicationResolver dupResolver) {
        if (canPaste(transfers, dupResolver) != CanPasteResult.YES) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Paste specified transfers into the current object. Default realization
     * based on RadixObjects. Designer for overwriting and delegation.
     *
     * @throws RadixObjectError if {@linkplain #canPaste(RadixObject)() } return
     * false.
     */
    @SuppressWarnings("unchecked")
    public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
        checkForCanPaste(transfers, resolver);

        final RadixObjects container = (RadixObjects) radixObject.getContainer();
        int index = container.indexOf(radixObject);

        for (Transfer transfer : transfers) {
            if (transfer.beforePaste(container)) {
                final RadixObject objectInClipboard = transfer.getObject();
                container.add(index++, objectInClipboard);
            }
        }
    }

    /**
     * @return true if buplicate must change identifier of this object (if this
     * object is definition, of couse). Used to not change calculated or
     * predefined identifiers.
     */
    protected boolean isIdChangeRequired(RadixObject copyRoot) {
        return true;
    }

    /**
     * Store object into XML. It is actual only if {@linkplain #canCopy() }
     * return true.
     */
    protected XmlObject copyToXml() {
        throw new RadixObjectError("Attempt to call not realized copyToXml()", radixObject);
    }

    /**
     * Restore object from XML. It is actual only if {@linkplain #canCopy() }
     * return true.
     */
    protected T loadFrom(XmlObject xmlObject) {
        throw new RadixObjectError("Attempt to call not realized loadFrom(XmlObject xmlObject)", radixObject);
    }

    /**
     * @return new transfer instance, that contains pointer to current object
     * and some additional information for transfer (optionally).
     */
    protected Transfer<T> newTransferInstance() {
        return new Transfer(radixObject);
    }

    protected Transfer<T> newTransferInstance(CbInfo cbInfo) {
        return new Transfer(radixObject);
    }

    private static class IdsCollector implements IVisitor {

        final Map<Id, Id> oldId2NewId = new HashMap<>();
        final Map<String, String> unprefixed = new HashMap<>();

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof Definition) {
                final Definition definition = (Definition) radixObject;
                final ClipboardSupport cs = definition.getClipboardSupport();
                if (cs == null || cs.isIdChangeRequired(copyRoot)) {
                    final Id oldId = definition.getId();
                    if (oldId == null || oldId.getPrefix() == null) {
                        return;
                    }
                    String prefix = oldId.getPrefix().getValue();
                    String upi = oldId.toString().substring(prefix.length());

                    String knownUnprefixedReplacement = unprefixed.get(upi);
                    final Id newId;
                    if (knownUnprefixedReplacement != null) {
                        newId = Id.Factory.loadFrom(oldId.getPrefix().getValue() + knownUnprefixedReplacement);
                    } else {
                        newId = Id.Factory.newInstance(oldId.getPrefix());
                        unprefixed.put(upi, newId.toString().substring(prefix.length()));
                    }

                    oldId2NewId.put(oldId, newId);
                }
            }
        }
        private RadixObject copyRoot;
    }

    /**
     * Creates duplicate specified Radix objects. Creates copies of the objects
     * and changes identifiers or all definitions and internal links to them.
     */
    public static <T extends RadixObject> List<T> duplicate(final List<T> radixObjects) {
        final IdsCollector idsCollector = new IdsCollector();
        for (T radixObject : radixObjects) {
            idsCollector.copyRoot = radixObject;
            radixObject.visit(idsCollector, VisitorProviderFactory.createDefaultVisitorProvider());
        }

        final List<T> result = new ArrayList<>(radixObjects.size());

        for (T radixObject : radixObjects) {
            final XmlObject xmlObject = radixObject.getClipboardSupport().copyToXml();
            String xmlText = xmlObject.xmlText();

            for (Map.Entry<Id, Id> e : idsCollector.oldId2NewId.entrySet()) {
                final Id oldId = e.getKey();
                final Id newId = e.getValue();
                final String oldIdAsStr = oldId.toString();
                final String newIdAsStr = newId.toString();
                xmlText = xmlText.replaceAll(oldIdAsStr, newIdAsStr);
            }

            final XmlObject duplicatedXmlObject;
            try {
                final XmlObject duplicatedAbstractXmlObject = XmlObject.Factory.parse(xmlText);
                duplicatedXmlObject = duplicatedAbstractXmlObject.changeType(xmlObject.schemaType());
            } catch (XmlException e) {
                throw new RadixObjectError("Unable to duplicate object: unable to change duplicated xml object type.", radixObject, e);
            }

            final T duplicate;
            try {
                duplicate = (T) radixObject.getClipboardSupport().loadFrom(duplicatedXmlObject);
            } catch (RadixError e) {
                throw new RadixObjectError("Unable to duplicate object: unable to restore object from xml.", radixObject, e);
            }

            result.add(duplicate);
        }

        return result;
    }

    /**
     * Creates copies of specified Radix objects.
     */
    public static <T extends RadixObject> List<T> copy(final List<T> radixObjects) {
        final List<T> result = new ArrayList<>(radixObjects.size());

        for (T radixObject : radixObjects) {
            T copy = (T) radixObject.getClipboardSupport().copy();
            result.add(copy);
        }

        return result;
    }

    /**
     * Create diplicate of current Radix objects. See {@linkplain ClipboardSupport#duplicate(java.util.List)
     * }. Do not use this function for multiple duplication - internal links
     * will not be changed.
     */
    public T duplicate() {
        final List<T> duplicates = duplicate(Collections.singletonList(radixObject));
        return duplicates.get(0);
    }

    public String encode(Transfer srcTransfer) {
        if (!isEncodedFormatSupported()) {
            return null;
        } else {
            Method decoder = getDecoderMethod();
            if (decoder == null || decoder.getParameterTypes().length != 1) {
                return null;
            } else {
                ClipboardInfoDocument xDoc = ClipboardInfoDocument.Factory.newInstance();
                CbInfo info = xDoc.addNewClipboardInfo();
                info.setDecoderClass(decoder.getDeclaringClass().getName());
                info.setDecoderMethod(decoder.getName());
                info.setDefinitionXmlClass(decoder.getParameterTypes()[0].getName());
                info.setDefinition(initialToXml(srcTransfer));
                CbStringList list = getExtInfoForEncodedFormat(srcTransfer);
                if (list != null) {
                    info.setExtInfo(list);
                }
                return xDoc.xmlText();
            }
        }
    }

    protected Method getDecoderMethod() {
        return null;
    }

    protected CbStringList getExtInfoForEncodedFormat(Transfer srcTransfer) {
        return null;
    }

    public boolean isEncodedFormatSupported() {
        return getDecoderMethod() != null;
    }

    @SuppressWarnings("unchecked")
    public Transfer decode(CbInfo info) {
        return newTransferInstance(info);
    }

    protected XmlObject initialToXml(Transfer t) {
        return t.getInitialObject().getClipboardSupport().copyToXml();
    }

    /**
     * Create copy of current Radix objects.
     */
    public T copy() {
        final XmlObject xmlObject = copyToXml();
        try {
            return loadFrom(xmlObject);
        } catch (RadixError e) {
            throw new RadixObjectError("Unable to copy object: unable to restore object from xml.", radixObject, e);
        }
    }

    /**
     * Create transferable for current Radix objects.
     */
    public final Transferable createTransferable(final ETransferType transferType) {
        final RadixMultiTransfer multiTransfer = RadixMultiTransfer.Factory.newInstance(radixObject, transferType);
        return multiTransfer.toTransferable();
    }

    /**
     * Create transferable for specified Radix objects.
     */
    public static Transferable createTransferable(List<RadixObject> radixObjects, ETransferType transferType) {
        final RadixMultiTransfer multiTransfer = RadixMultiTransfer.Factory.newInstance(radixObjects, transferType);
        return multiTransfer.toTransferable();
    }

    /**
     * Paste specified transfable into the current object. Final for common
     * logic. Overwrite {@linkplain ClipboardSupport#paste(java.util.List) }.
     *
     * @throws RadixObjectError if {@linkplain #canPaste(java.awt.datatransfer.Transferable)
     * } return false.
     */
    public final List<RadixObject> paste(Transferable transferable, DuplicationResolver resolver) {
        final RadixMultiTransfer multiTransfer = RadixMultiTransfer.Factory.loadFrom(transferable);
        if (multiTransfer == null) {
            throw new RadixObjectError("Attemp to paste unsupported transferable.", radixObject);
        }

        final List<Transfer> transfers = multiTransfer.getTransfers();
        checkForCanPaste(transfers, resolver); // check for can cut

        for (Transfer transfer : transfers) {
            final RadixObject objectInClipboard = transfer.getObject();
            if (objectInClipboard.getContainer() != null) {
                objectInClipboard.delete();
            }
        }


        paste(transfers, resolver);

        for (Transfer transfer : transfers) {
            transfer.afterPaste();
        }

        final List<RadixObject> pasted = new ArrayList<>(transfers.size());
        for (Transfer transfer : transfers) {
            pasted.add(transfer.getObject());
        }

        return pasted;
    }

    /**
     * Get transfers of specified transferable.
     *
     * @return transfers or null if unknown transferable
     * @see {@linkplain #canPaste(java.awt.datatransfer.Transferable).
     */
    public static List<Transfer> getTransfers(final Transferable transferable) {
        final RadixMultiTransfer radixMultiTransfer = RadixMultiTransfer.Factory.loadFrom(transferable);
        return radixMultiTransfer == null ? null : radixMultiTransfer.getTransfers();
    }

    /**
     * Get transfer type of specified transferable.
     *
     * @return transfer type or null if unknown transferable
     * @see {@linkplain #canPaste(java.awt.datatransfer.Transferable).
     */
    public static ETransferType getTransferType(final Transferable transferable) {
        final RadixMultiTransfer radixMultiTransfer = RadixMultiTransfer.Factory.loadFrom(transferable);
        return radixMultiTransfer == null ? null : radixMultiTransfer.getTransferType();
    }

    public static Transferable merge(Transferable[] transferables) {
        final RadixMultiTransfer multiTransfer = RadixMultiTransfer.Factory.merge(transferables);
        return multiTransfer.toTransferable();
    }
}
