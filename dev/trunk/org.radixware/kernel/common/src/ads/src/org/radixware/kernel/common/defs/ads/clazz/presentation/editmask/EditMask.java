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

package org.radixware.kernel.common.defs.ads.clazz.presentation.editmask;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.EditMaskWriter;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;


public abstract class EditMask extends RadixObject implements IJavaSource {

    public static final class Factory {

        public static EditMask loadFrom(EditOptions context, org.radixware.schemas.editmask.EditMask xDef) {
            return loadFromImpl(context, xDef);
        }

        private static EditMask loadFromImpl(RadixObject context, org.radixware.schemas.editmask.EditMask xDef) {
            if (xDef == null) {
                return null;

            }
            if (xDef.isSetDateTime()) {
                return new EditMaskDateTime(context, xDef.getDateTime(), false);
            } else if (xDef.isSetEnum()) {
                return new EditMaskEnum(context, xDef.getEnum(), false);
            } else if (xDef.isSetInt()) {
                return new EditMaskInt(context, xDef.getInt(), false);
            } else if (xDef.isSetList()) {
                return new EditMaskList(context, xDef.getList(), false);
            } else if (xDef.isSetNum()) {
                return new EditMaskNum(context, xDef.getNum(), false);
            } else if (xDef.isSetStr()) {
                return new EditMaskStr(context, xDef.getStr(), false);
            } else if (xDef.isSetTimeInterval()) {
                return new EditMaskTimeInterval(context, xDef.getTimeInterval(), false);
            } else if (xDef.isSetBoolean()) {
                return new EditMaskBool(context, xDef.getBoolean(), false);
            } else if (xDef.isSetFilePath()) {
                return new EditMaskFilePath(context, xDef.getFilePath(), false);
                    
            } else {
                throw new DefinitionError("Invalid edit mask format.");
            }
        }

        public static EditMask newInstance(EditOptions context, EEditMaskType type) {
            return newInstanceImpl(context, type, false);
        }

        private static EditMask newInstanceImpl(RadixObject context, EEditMaskType type, boolean virtual) {
            EditMask em = null;
            switch (type) {
                case DATE_TIME:
                    em = new EditMaskDateTime(context, virtual);
                    break;
                case ENUM:
                    em = new EditMaskEnum(context, virtual);
                    break;
                case INT:
                    em = new EditMaskInt(context, virtual);
                    break;
                case STR:
                    em = new EditMaskStr(context, virtual);
                    break;
                case LIST:
                    em = new EditMaskList(context, virtual);
                    break;
                case NUM:
                    em = new EditMaskNum(context, virtual);
                    break;
                case TIME_INTERVAL:
                    em = new EditMaskTimeInterval(context, virtual);
                    break;
                case BOOL:
                    em = new EditMaskBool(context, virtual);
                    break;
                case FILE_PATH:
                    em = new EditMaskFilePath(context, virtual);
                    break;
            }
            if (em != null) {
                em.applyDbRestrictions();
            }
            return em;
        }

        public static EditMask newDefaultInstance(EditOptions context) {
            return newDefaultInstanceImpl(context, context.getTypedObject().getType(), null);
        }

        /**
         * switch (type){ case INT: case ARR_INT: return new EditMaskInt(); case
         * NUM: case ARR_NUM: return new EditMaskNum(); case DATE_TIME: case
         * ARR_DATE_TIME: return new EditMaskDateTime(); case STR: case ARR_STR:
         * case CHAR: case ARR_CHAR: case CLOB: case ARR_CLOB: return new
         * EditMaskStr(); default: return new EditMaskNone(); }
         */
        private static EditMask newDefaultInstanceImpl(RadixObject context, AdsTypeDeclaration type, EValType valType) {
            EValType typeId = type != null ? type.getTypeId() : valType;

            if (type != null) {
                AdsType resolvedType = type.resolve((AdsDefinition) context.getOwnerDefinition()).get();
                if (resolvedType != null) {
                    if (resolvedType instanceof AdsEnumType) {
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.ENUM, true);
                    }
                }
            }
            if (typeId != null) {
                switch (typeId) {
                    case INT:
                    case ARR_INT:
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.INT, true);
                    case STR:
                    case ARR_STR:
                    case CHAR:
                    case ARR_CHAR:
                    case CLOB:
                    case ARR_CLOB:
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.STR, true);
                    case NUM:
                    case ARR_NUM:
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.NUM, true);
                    case DATE_TIME:
                    case ARR_DATE_TIME:
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.DATE_TIME, true);
                    case ARR_BOOL:
                    case BOOL:
                        return EditMask.Factory.newInstanceImpl(context, EEditMaskType.BOOL, true);
                }
            }
            return null;
        }
//        public static final EditMaskDateTime newDateTimeEditMask() {
//            return new EditMaskDateTime();
//        }
//
//        public static final EditMaskEnum newEnumEditMask() {
//            return new EditMaskEnum();
//        }
//
//        public static final EditMaskInt newEnumMaskInt() {
//            return new EditMaskInt();
//        }
//
//        public static final EditMaskList newEnumMaskList() {
//            return new EditMaskList();
//        }
//
//        public static final EditMaskNum newEnumMaskNum() {
//            return new EditMaskNum();
//        }
//
//        public static final EditMaskStr newEnumMaskStr() {
//            return new EditMaskStr();
//        }
//
//        public static final EditMaskTimeInterval newEnumMaskTimeInterval() {
//            return new EditMaskTimeInterval();
//        }
    }
    private final boolean virtual;

    protected EditMask(RadixObject context, boolean virtual) {
        setContainer(context);
        this.virtual = virtual;
        //do nothing
    }

    protected void modified() {
        if (virtual) {
            return;

        } else {
            setEditState(EEditState.MODIFIED);
        }
    }

    public abstract void appendTo(org.radixware.schemas.editmask.EditMask xDef);

    public abstract boolean isCompatible(EValType valType);

    public abstract EEditMaskType getType();

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(
                    UsagePurpose purpose) {
                return new EditMaskWriter(this, EditMask.this, purpose);
            }
        };
    }

    @Override
    protected boolean isSingleSideLinked() {
        RadixObject thisContainer = getContainer();
        if ((thisContainer instanceof EditOptions && (((EditOptions) thisContainer).getEditMask() != this))) {
            return true;
        } else {
            return false;
        }
    }

    public AdsDefinition getOwnerDef() {
        for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsDefinition) {
                return (AdsDefinition) obj;
            }
        }
        return null;
    }

    public EValType getContextValType() {
        RadixObject context = getContainer();
        AdsTypeDeclaration decl = null;
        if (context instanceof EditOptions) {
            decl = ((EditOptions) context).getTypedObject().getType();
        } else {
            return null;
        }
        if (decl == null) {
            return null;
        } else {
            return decl.getTypeId();
        }
    }

    /**
     * Returns length-precision pair (if any) from database object of edit mask instance
     */
    protected int[] getDbRestrictions() {

        final DdsColumnDef col = findDdsColumn();
        if (col != null) {
            final EValType valType = col.getValType();
            if (valType == EValType.INT || valType == EValType.NUM || valType == EValType.STR) {
                return new int[]{col.getLength(), col.getPrecision()};
            }
        }
        return null;
    }

    protected DdsColumnDef findDdsColumn() {
        final RadixObject context = getContainer();
        if (context instanceof PropertyEditOptions) {
            final AdsPropertyDef prop = (AdsPropertyDef) ((PropertyEditOptions) context).getOwnerDefinition();
            if (prop instanceof ColumnProperty) {
                return ((ColumnProperty) prop).getColumnInfo().findColumn();
            }
        }
        return null;
    }

    public abstract void applyDbRestrictions();

    public boolean isDbRestrictionsAvailable() {
        return getDbRestrictions() != null;
    }
}
