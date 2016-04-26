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

package org.radixware.kernel.server.meta.presentations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.meta.RadTitledDef;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.xscml.SqmlDocument;

public final class RadFilterDef extends RadTitledDef implements IRadFilter {

    private final Sqml condition;
    private final Sqml hint;
    private final boolean isAnyBaseSortingEnabled;
    private final List<EnabledSorting> enabledBaseSortings;
    private final boolean isAnyCustomSortingEnabled;
    private final Id defaultSortingId;
    private final Collection<Id> paramIds;
    private RadSortingDef defaultSorting = null;
    private final Object defaultSortingSem = new Object();

    public RadFilterDef(
            final Id id,
            final Id ownerDefId,
            final String name,
            final Id titleId,
            final String condition,
            final String hint,
            final Id defaultSortingId,
            final boolean isAnyBaseSortingEnabled,
            final EnabledSorting[] enabledBaseSortings,
            final boolean isAnyCustomSortingEnabled) {
        this(id, ownerDefId, name, titleId, condition, hint, defaultSortingId, isAnyBaseSortingEnabled, enabledBaseSortings, isAnyCustomSortingEnabled, null);
    }

    //Constructor
    public RadFilterDef(
            final Id id,
            final Id ownerDefId,
            final String name,
            final Id titleId,
            final String condition,
            final String hint,
            final Id defaultSortingId,
            final boolean isAnyBaseSortingEnabled,
            final EnabledSorting[] enabledBaseSortings,
            final boolean isAnyCustomSortingEnabled,
            final String layerUri) {
        super(id, name, ownerDefId, titleId);
        SqmlDocument expr;
        try {
            expr = condition == null || condition.length() == 0 ? null : SqmlDocument.Factory.parse(condition);
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse filter #" + id + " condition SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        this.condition = expr != null ? Sqml.Factory.loadFrom("FltCond", expr.getSqml()) : null;
        if (this.condition != null) {
            this.condition.setLayerUri(layerUri);
            this.condition.switchOnWriteProtection();
        }

        try {
            expr = hint == null || hint.length() == 0 ? null : SqmlDocument.Factory.parse(hint);
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse filter (ID=\"" + id + "\") hint SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        this.hint = expr != null ? Sqml.Factory.loadFrom("FltHint", expr.getSqml()) : null;
        if (this.hint != null) {
            this.hint.setLayerUri(layerUri);
            this.hint.switchOnWriteProtection();
        }

        this.isAnyBaseSortingEnabled = isAnyBaseSortingEnabled;
        if (enabledBaseSortings != null) {
            this.enabledBaseSortings = Collections.unmodifiableList(Arrays.asList(enabledBaseSortings));
        } else {
            this.enabledBaseSortings = Collections.emptyList();
        }
        this.isAnyCustomSortingEnabled = isAnyCustomSortingEnabled;
        this.defaultSortingId = defaultSortingId;
        paramIds = getParamIds();
    }

    @Override
    public void link() {
        super.link();
    }

    /**
     * @return the condition
     */
    @Override
    public Sqml getCondition() {
        return condition;
    }

    /**
     * @return the hint
     */
    public Sqml getHint() {
        return hint;
    }

    /**
     * @return the isAnyBaseSortingEnabled
     */
    public boolean isAnyBaseSortingEnabled() {
        return isAnyBaseSortingEnabled;
    }

    /**
     * @return the enabledBaseSortings
     */
    public List<EnabledSorting> getEnabledBaseSortings() {
        return enabledBaseSortings;
    }

    /**
     * @return the isAnyCustomSortingEnabled
     */
    @Override
    public boolean isAnyCustomSortingEnabled() {
        return isAnyCustomSortingEnabled;
    }

    @Override
    public final boolean isBaseSortingEnabledById(final Id srtId) {
        if (isAnyBaseSortingEnabled()) {
            return true;
        }
        for (EnabledSorting enabledSrt : getEnabledBaseSortings()) {
            if (enabledSrt.getSortingId().equals(srtId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final Sqml getSortingHintById(final Id srtId, final Sqml sortingHint) {
        if (getEnabledBaseSortings() != null) {
            for (EnabledSorting enabledSrt : getEnabledBaseSortings()) {
                if (enabledSrt.getSortingId().equals(srtId)) {
                    if ((enabledSrt.getHint() != null)) {
                        return enabledSrt.getHint();
                    } else {
                        break;
                    }
                }
            }
        }
        if (getHint() != null) {
            return getHint();
        } else {
            return sortingHint;
        }
    }

    public final boolean hasParams() {
        return !getParamIds().isEmpty();
    }

    private final Collection<Id> getParamIds() {
        return paramIds;
    }

    private static final Collection<Id> getParamIds(final Sqml cond) {
        if (cond != null && !cond.getItems().isEmpty()) {
            final Collection<Id> res = new LinkedList<Id>();
            for (Sqml.Item itemXml : cond.getItems()) {
                if (itemXml instanceof ParameterTag) {
                    final ParameterTag paramXml = (ParameterTag) itemXml;
                    res.add(paramXml.getParameterId());
                }
            }
            return Collections.unmodifiableCollection(res);
        }
        return Collections.emptyList();
    }

    //XXX: remove synchronization
    @Override
    public final RadSortingDef getDefaultSorting(final RadClassPresentationDef classPres) {
        synchronized (defaultSortingSem) {
            if (defaultSorting == null) {
                if (defaultSortingId != null) {
                    defaultSorting = classPres.getSortingById(defaultSortingId);
                } else {
                    for (RadSortingDef srt : classPres.getSortings()) {
                        defaultSorting = srt;
                        if (!isBaseSortingEnabledById(defaultSorting.getId())) {
                            defaultSorting = null;
                        }
                        if (defaultSorting != null) {
                            break;
                        }
                    }
                }
            }
            return defaultSorting;
        }
    }

    @Override
    public String getInfo() {
        return String.valueOf(getName()) + " (#" + getId().toString() + ")";
    }

    static public final class EnabledSorting {

        private final Sqml hint;
        private final Id sortingId;

        public EnabledSorting(
                final String hint,
                final Id sortingId) {
            this(hint, sortingId, null);
        }

        public EnabledSorting(
                final String hint,
                final Id sortingId,
                final String layerUri) {
            final SqmlDocument expr;
            try {
                expr = hint == null || hint.length() == 0 ? null : SqmlDocument.Factory.parse(hint);
            } catch (XmlException e) {
                throw new WrongFormatError("Can't parse enabled sorting #" + sortingId + " hint SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
            }
            this.hint = expr != null ? Sqml.Factory.loadFrom("EnabledSrtHint", expr.getSqml()) : null;
            if (this.hint != null) {
                this.hint.setLayerUri(layerUri);
                this.hint.switchOnWriteProtection();
            }
            this.sortingId = sortingId;
        }

        /**
         * @return the hint
         */
        public Sqml getHint() {
            return hint;
        }

        /**
         * @return the sortingId
         */
        public Id getSortingId() {
            return sortingId;
        }
    }
}
