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

package org.radixware.kernel.server.meta.clazzes;

import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.Sqml.Tag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;

public abstract class RadPropDef extends RadDefinition implements ISqmlProperty {

    private RadClassDef classDef = null;
    private final Id titleId;
    protected final RadixDefaultValue initVal;
    private final Id constSetId;
    private final IRadPropAccessor accessor;
    private final EValType valType;
    private final boolean isValInheritable;
    private final EPropInitializationPolicy initPolicy;
    private final List<ValInheritancePath> valInheritPathes;
    private final ValAsStr valInheritMarkVal;

    protected RadPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id constSetId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkVal,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy,
            final RadixDefaultValue initVal,
            final IRadPropAccessor accessor) {
        super(id, name);
        this.titleId = titleId;
        this.valType = valType;
        this.constSetId = constSetId;
        this.accessor = accessor;
        this.isValInheritable = isValInheritable;
        this.valInheritMarkVal = valInheritMarkVal;
        if (isValInheritable) {
            if (valInheritPathes == null) {
                this.valInheritPathes = Collections.emptyList();
            } else {
                this.valInheritPathes = Collections.unmodifiableList(Arrays.asList(valInheritPathes));
            }
        } else {
            this.valInheritPathes = null;
        }
        this.initVal = initVal;
        this.initPolicy = initPolicy;
    }

    @Override
    public void link() {
        super.link();
        getLocalTitle();
    }

    private String getLocalTitle() {
        if (titleId != null) {
            return MultilingualString.get(Arte.get(), getClassDef().getId(), titleId);
        } else {
            return null;
        }
    }

    public abstract String getDbType();

    public final String getTitle() {
        final RadPropDef titleOwner = getTitleOwnerProp();
        if (titleOwner instanceof IRadRefPropertyDef && isTitleInherited(titleOwner.getId())) {
            final IRadRefPropertyDef ref = (IRadRefPropertyDef) titleOwner;
            try {
                final RadClassDef clazz = getClassDef().getRelease().getClassDef(ref.getDestinationClassId());
                return clazz.getTitle();
            } catch (DefinitionNotFoundError e) {
                return titleOwner.getLocalTitle();
            }
        } else {
            return titleOwner.getLocalTitle();
        }
    }

    private boolean isTitleInherited(Id propId) {
        if (classDef == null) {
            return false;
        }
        RadPropertyPresentationDef pres = classDef.getPresentation().getPropPresById(propId);
        return pres != null && pres.getInheritanceMask().contains(EPropAttrInheritance.TITLE);
    }

    private RadPropDef getTitleOwnerProp() {
        if (classDef == null) {
            return this;
        }
        if (!isTitleInherited(getId())) {
            return this;
        } else {
            RadPropDef prop = getAncestorProp();
            if (prop == null) {
                return this;
            } else {
                return prop.getTitleOwnerProp();
            }
        }
    }

    private RadPropDef getAncestorProp() {
        Id ancestorId = getClassDef().getAncestorId();
        if (ancestorId == null) {
            return null;
        } else {
            try {
                RadClassDef clazz = getClassDef().getRelease().getClassDef(ancestorId);
                return clazz.getPropById(getId());
            } catch (DefinitionNotFoundError e) {
                return null;
            }
        }
    }

    @Deprecated
    public final String getFinalTitle() {
        return getTitle();
    }

    void link(final RadClassDef classDef) {
        this.classDef = classDef;
    }

    protected RadClassDef getClassDef() {
        return classDef;
    }

    public final Object getValInheritMarkVal(final Arte arte) {
        if (valInheritMarkVal == null) {
            return null;
        }
        ValAsStr valAsStr = SrvValAsStr.Factory.loadFrom(arte, valInheritMarkVal.toString());
        return valAsStr == null ? null : valAsStr.toObject(getValType());
    }

    public final boolean getValIsInheritMark(final Arte arte, final Object val) {
        if (getValType() == EValType.CLOB) {
            if (val == null) {
                return valInheritMarkVal == null;
            } else if (valInheritMarkVal == null) {
                return false;
            }
            final Clob clobVal = (Clob) val;
            try {
                if (clobVal.length() == valInheritMarkVal.toString().length()) {
                    return clobVal.getSubString(1, valInheritMarkVal.toString().length()).equals(valInheritMarkVal.toString());
                }
            } catch (SQLException ex) {
                return false;
            }
        }

        if (val == null) {
            return getValInheritMarkVal(arte) == null;
        }
        
        final Object inheritMark = getValInheritMarkVal(arte);
        if (inheritMark instanceof Pid && val instanceof Entity) {
            return ((Pid)inheritMark).equals(((Entity) val).getPid());
        }

        return val.equals(getValInheritMarkVal(arte));
    }

    public final Object getInitVal(final Arte arte) {
        if (initVal == null) {
            return null;
        }
        switch (initVal.getChoice()) {
            case DATE_TIME:
            case EXACT_DATE_TIME:
                return new Timestamp(System.currentTimeMillis());
            case VAL_AS_STR:
                return initVal.getValAsStr().toObject(getValType());
            case EXPRESSION:
                return null;
            default:
                throw new DefinitionError("Unsupported RadixDefaultValue.choice: " + String.valueOf(initVal.getChoice()));
        }
    }

    @Override
    public DdsTableDef findOwnerTable() {
        return getClassDef().getTableDef();
    }

    public EPropInitializationPolicy getInitPolicy() {
        return initPolicy;
    }

    @Override
    public void check(final Tag source, final IProblemHandler problemHandler) {
        //do nothing because it's design time activity
    }

    /**
     * @return the constSetId
     */
    public Id getEnumId() {
        return constSetId;
    }

    @Override
    public Sqml getExpression() {
        return null;
    }

    /**
     * @return the accessor
     */
    public IRadPropAccessor getAccessor() {
        return accessor;
    }

    /**
     * @return the valType
     */
    @Override
    public EValType getValType() {
        return valType;
    }

    public boolean isVisibleForArte() {
        return true;
    }

    /**
     * @return the isValInheritable
     */
    public boolean getIsValInheritable() {
        return isValInheritable;
    }

    /**
     * @return the valInheritPathes
     */
    public List<ValInheritancePath> getValInheritPathes() {
        return valInheritPathes;
    }

    public static final class ValInheritancePath {

        private final Id destPropId;
        private final List<Id> refPropIds;

        public ValInheritancePath(
                final Id[] refPropIds,
                final Id destPropId) {
            if (refPropIds == null) {
                this.refPropIds = Collections.emptyList();
            } else {
                this.refPropIds = Collections.unmodifiableList(Arrays.asList(refPropIds));
            }
            this.destPropId = destPropId;
        }

        /**
         * @return the destPropId
         */
        public Id getDestPropId() {
            return destPropId;
        }

        /**
         * @return the refPropIds
         */
        public List<Id> getRefPropIds() {
            return refPropIds;
        }
    }
}
