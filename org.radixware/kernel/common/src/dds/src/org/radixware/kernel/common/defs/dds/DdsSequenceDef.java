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
package org.radixware.kernel.common.defs.dds;

import java.util.List;
import org.radixware.kernel.common.defs.dds.radixdoc.DdsSequenceRadixdocSupport;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.radixdoc.Page;

/**
 * Metainformation about database sequence.
 */
public class DdsSequenceDef extends DdsDefinition implements IPlacementSupport, IDdsAutoDbNamedDefinition, IRadixdocProvider {

    private String dbName = "";

    /**
     * Получить имя sequence'а в базе данных.
     */
    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public void setDbName(String dbName) {
        if (!Utils.equals(this.dbName, dbName)) {
            this.dbName = dbName;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    boolean autoDbName = true;

    @Override
    public boolean isAutoDbName() {
        return autoDbName;
    }

    public void setAutoDbName(boolean autoDbName) {
        if (!Utils.equals(this.autoDbName, autoDbName)) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Long startWith = null;

    /**
     * Получить значение, подставляемое в качестве атрибута 'start with' при
     * генерации скрипта создания sequence'а в базе данных. Задает результат
     * первого обращения к значению sequence'а.
     *
     * @return 'start with' value, or null, if not used.
     */
    public Long getStartWith() {
        return startWith;
    }

    public void setStartWith(Long startWith) {
        if (!Utils.equals(this.startWith, startWith)) {
            this.startWith = startWith;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Long incrementBy = null;

    /**
     * Получить значение, подставляемое в качестве атрибута 'increment by' при
     * генерации скрипта создания sequence'а в базе данных. Задает разницу между
     * двумя последовательными значениями sequence'а.
     *
     * @return 'increment by' value, or null, if not used.
     */
    public Long getIncrementBy() {
        return incrementBy;
    }

    public void setIncrementBy(Long incrementBy) {
        if (!Utils.equals(this.incrementBy, incrementBy)) {
            this.incrementBy = incrementBy;
            setEditState(EEditState.MODIFIED);
        }
    }
    boolean cycled = false;

    /**
     * Является-ли sequence циклическим. Используется при генерации скрипта
     * создания sequence'а в базе данных.
     */
    public boolean isCycled() {
        return cycled;
    }

    public void setCycled(boolean cycled) {
        if (!Utils.equals(this.cycled, cycled)) {
            this.cycled = cycled;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Long minValue = null;

    /**
     * Получить минимальное значение sequence'а. Используется при генерации
     * скрипта создания sequence'а в базе данных.
     *
     * @return 'min value' value, or null, if not used.
     */
    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        if (!Utils.equals(this.minValue, minValue)) {
            this.minValue = minValue;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Long maxValue;

    /**
     * Получить максимальное значение sequence'а. Используется при генерации
     * скрипта создания sequence'а в базе данных.
     *
     * @return 'max value' value, or null, if not used.
     */
    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        if (!Utils.equals(this.maxValue, maxValue)) {
            this.maxValue = maxValue;
            setEditState(EEditState.MODIFIED);
        }
    }
    private boolean ordered = true;

    /**
     * Возвращает-ли sequence последовательные значения. Используется при
     * генерации скрипта создания sequence'а в базе данных.
     */
    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        if (!Utils.equals(this.ordered, ordered)) {
            this.ordered = ordered;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Long cache = 20L;

    /**
     * Получить значение, подставляемое в качестве атрибута 'cache' при
     * генерации скрипта создания sequence'а в базе данных.
     *
     * @return size of cache, null for NOCACHE.
     */
    public Long getCache() {
        return cache;
    }

    public void setCache(Long cache) {
        if (!Utils.equals(this.cache, cache)) {
            this.cache = cache;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final DdsDefinitionPlacement placement;

    @Override
    public DdsDefinitionPlacement getPlacement() {
        return placement;
    }

    protected DdsSequenceDef(final String name) {
        super(EDefinitionIdPrefix.DDS_SEQUENCE, name);
        this.placement = DdsDefinitionPlacement.Factory.newInstance(this);
    }

    public DdsSequenceDef(org.radixware.schemas.ddsdef.Sequence xSequence) {
        super(xSequence);

        this.dbName = xSequence.getDbName();

        if (xSequence.isSetAutoDbName()) {
            this.autoDbName = xSequence.getAutoDbName();
        }

        if (xSequence.isSetStartWith()) {
            this.startWith = xSequence.getStartWith();
        }

        if (xSequence.isSetIncrementBy()) {
            this.incrementBy = xSequence.getIncrementBy();
        }

        this.cycled = xSequence.getCycle();

        if (xSequence.isSetMinValue()) {
            this.minValue = xSequence.getMinValue();
        }

        if (xSequence.isSetMaxValue()) {
            this.maxValue = xSequence.getMaxValue();
        }

        this.ordered = xSequence.getOrder();

        if (xSequence.isSetCache()) {
            this.cache = xSequence.getCache();
        } else {
            this.cache = null;
        }

        if (xSequence.isSetSuppressedWarnings()) {
            this.warningsSupport = new Problems(this, xSequence.getSuppressedWarnings());
        }

        this.placement = DdsDefinitionPlacement.Factory.loadFrom(this, xSequence.getPlacement());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; AutoDbName: ");
        result.append(autoDbName);
        result.append("; Cache: ");
        result.append(cache);
        result.append("; IncrementBy: ");
        result.append(incrementBy);
        result.append("; MinValue: ");
        result.append(minValue);
        result.append("; MaxValue: ");
        result.append(maxValue);
        result.append("; Cycled: ");
        result.append(cycled);
        result.append("; Ordered: ");
        result.append(ordered);

        return result.toString();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsSequenceDef newInstance(final String name) {
            return new DdsSequenceDef(name);
        }

        public static DdsSequenceDef loadFrom(org.radixware.schemas.ddsdef.Sequence xSequence) {
            return new DdsSequenceDef(xSequence);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.SEQUENCE;
    }

    @Override
    public boolean isGeneratedInDb() {
        return true;
    }

    public static String calcAutoDbName(DdsModelDef ownerModel, String name) {
        String dbNamePrefix = ownerModel.getDbAttributes().getDbNamePrefix();
        String result = DbNameUtils.calcAutoDbName("SQN", dbNamePrefix, name);
        return result;
    }

    @Override
    public String calcAutoDbName() {
        DdsModelDef model = getOwnerModel();
        String name = getName();
        String result = calcAutoDbName(model, name);
        return result;
    }

    private class DdsSequenceClipboardSupport extends DdsClipboardSupport<DdsSequenceDef> {

        public DdsSequenceClipboardSupport() {
            super(DdsSequenceDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Sequence xSequence = org.radixware.schemas.ddsdef.Sequence.Factory.newInstance();
            DdsModelWriter.writeSequence(DdsSequenceDef.this, xSequence);
            return xSequence;
        }

        @Override
        protected DdsSequenceDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Sequence xSequence = (org.radixware.schemas.ddsdef.Sequence) xmlObject;
            return DdsSequenceDef.Factory.loadFrom(xSequence);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsSequenceDef> getClipboardSupport() {
        return new DdsSequenceClipboardSupport();
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Database name: " + getDbName());
    }
    public static final String SEQUENCE_TYPE_TITLE = "Sequence";
    public static final String SEQUENCE_TYPES_TITLE = "Sequences";

    @Override
    public String getTypeTitle() {
        return SEQUENCE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return SEQUENCE_TYPES_TITLE;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport(this) {

            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DdsSequenceRadixdocSupport((DdsSequenceDef) getSource(), page, options);
            }

        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    public static class Problems extends RadixProblem.WarningSuppressionSupport {

        public static final int AADC_SEQUENCE = 300000;

        public Problems(DdsDefinition owner, List<Integer> warnings) {
            super(owner);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case AADC_SEQUENCE:
                    return true;
                default:
                    return false;
            }
        }
    }
    private Problems warningsSupport = null;

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }
}
