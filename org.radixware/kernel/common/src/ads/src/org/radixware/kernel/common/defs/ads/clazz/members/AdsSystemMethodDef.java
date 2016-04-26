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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.IDependentId;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.MethodGroups;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef.AdsReportSysMethod;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsSystemMethodWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassExecuteReportWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassExecuteWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassSendBatchWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassSetExecuteBatchWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsSystemMethodDef extends AdsMethodDef implements IDependentId {

    public static final class Factory {

        public static AdsSystemMethodDef newLoadByPK() {
            final AdsSystemMethodDef m = new AdsSystemMethodDef(ID_LOAD_BY_PK, "loadByPK");
            m.getProfile().getAccessFlags().setStatic(true);
            return m;
        }

        public static AdsSystemMethodDef newLoadByPidStr() {
            final AdsSystemMethodDef m = new AdsSystemMethodDef(ID_LOAD_BY_PID_STR, "loadByPidStr");
            m.getProfile().getAccessFlags().setStatic(true);
            return m;
        }

        public static AdsSystemMethodDef newExecuteStmt(EClassType classType) {
            final String name = (classType == EClassType.SQL_CURSOR ? "open" : "execute");
            final AdsSqlClassExecuteMethod m = new AdsSqlClassExecuteMethod(name);
            return m;
        }

        public static AdsSystemMethodDef newOpenReport() {
            final AdsSystemMethodDef m = new AdsReportSysMethod(ID_REPORT_OPEN, "open");
            return m;
        }

        public static AdsSystemMethodDef newExecuteReport() {
            final AdsSystemMethodDef m = new AdsReportSysMethod(ID_REPORT_EXECUTE, "execute");
            return m;
        }

        public static AdsSystemMethodDef newSetExecuteBatchStmt() {
            return new AdsSqlStatementSetExecuteBatchMethod();
        }

        public static AdsSystemMethodDef newSendBatchStmt() {
            return new AdsSqlStatementSendBatchMethod();
        }

        static AdsSystemMethodDef loadFrom(AbstractMethodDefinition xMethod) {
            if (Utils.equals(xMethod.getId(), ID_STMT_EXECUTE)) {
                return new AdsSqlClassExecuteMethod(xMethod);
            } else if (Utils.equals(xMethod.getId(), ID_STMT_SET_EXECUTE_BATCH)) {
                return new AdsSqlStatementSetExecuteBatchMethod(xMethod);
            } else if (Utils.equals(xMethod.getId(), ID_STMT_SEND_BATCH)) {
                return new AdsSqlStatementSendBatchMethod(xMethod);
            } else {
                return new AdsSystemMethodDef(xMethod);
            }
        }
    }
    public static final Id ID_LOAD_BY_PK = Id.Factory.loadFrom("mth_loadByPK_________________");
    public static final Id ID_LOAD_BY_PID_STR = Id.Factory.loadFrom("mth_loadByPidStr_____________");
    //public static final Id ID_ALGO_EXECUTE = Id.Factory.loadFrom("mth_algo_execute_____________");
    public static final Id ID_STMT_EXECUTE = Id.Factory.loadFrom("mth_stmt_execute_____________");
    public static final Id ID_STMT_SET_EXECUTE_BATCH = Id.Factory.loadFrom("mth_stmt_setExecuteBatch_____");
    public static final Id ID_STMT_SEND_BATCH = Id.Factory.loadFrom("mth_stmt_sendBatch___________");
    public static final Id ID_REPORT_CHECK_SUITABILITY = Id.Factory.loadFrom("mth_report_check_suitability_");
    public static final Id ID_REPORT_OPEN = Id.Factory.loadFrom("mth_report_open_dynamic______");
    public static final Id ID_REPORT_EXECUTE = Id.Factory.loadFrom("mth_report_execute_dynamic___");
    public static final String REPORT_STREAM_VAR_NAME = "stream";
    public static final String REPORT_FORMAT_VAR_NAME = "format";
    public static final String REPORT_PARAMS_VAR_NAME = "params";

    protected AdsSystemMethodDef(Id id, String name) {
        super(id, name);
    }

    protected AdsSystemMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.SYSTEM;
    }

    public boolean syncWithSystem() {
        ArgumentsProvider p = getArgumentsProvider();
        if (p != null && p.isValid()) {
            this.getProfile().getReturnValue().setType(p.getReturnValueInfo().type);
            this.getProfile().getParametersList().clear();
            for (ArgumentsProvider.ArgumentInfo info : p.getParameterInfos()) {
                this.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(info.id, info.name, info.type));
            }
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                final Id methodId = AdsSystemMethodDef.this.getId();
                if (methodId == ID_STMT_EXECUTE || methodId == ID_REPORT_OPEN) {
                    return new AdsSqlClassExecuteWriter(this, AdsSystemMethodDef.this, purpose);
                } else if (methodId == ID_STMT_SET_EXECUTE_BATCH) {
                    return new AdsSqlClassSetExecuteBatchWriter(this, AdsSystemMethodDef.this, purpose);
                } else if (methodId == ID_STMT_SEND_BATCH) {
                    return new AdsSqlClassSendBatchWriter(this, AdsSystemMethodDef.this, purpose);
                } else if (methodId == ID_REPORT_EXECUTE) {
                    return new AdsSqlClassExecuteReportWriter(this, AdsSystemMethodDef.this, purpose);
                } else {
                    return new AdsSystemMethodWriter(this, AdsSystemMethodDef.this, purpose);
                }
            }
        };
    }

    public class ArgumentsProvider {

        public class ArgumentInfo {

            public final String name;
            public final AdsTypeDeclaration type;
            public final DdsColumnDef columnAssoc;
            public final AdsPropertyDef propAssoc;
            public final Id id;

            public ArgumentInfo(Id id, String name, AdsTypeDeclaration type, DdsColumnDef columnAssoc, AdsPropertyDef propAssoc) {
                this.name = name;
                this.id = id;
                this.columnAssoc = columnAssoc;
                this.propAssoc = propAssoc;
                if (propAssoc != null) {
                    this.type = propAssoc.getValue().getType();
                } else if (columnAssoc != null) {
// RADIX-1351
//                    if (columnAssoc.getEnumId() != null) {
//                        AdsEnumDef enumDef = AdsSearcher.Factory.newAdsEnumSearcher(AdsSystemMethodDef.this.getModule()).findById(columnAssoc.getEnumId());
//                        if (enumDef != null) {
//                            this.type = AdsTypeDeclaration.Factory.newInstance(columnAssoc.getValType(), enumDef, null, 0);
//                        } else {
//                            this.type = AdsTypeDeclaration.Factory.newInstance(columnAssoc.getValType());
//                        }
//                    } else {
                    this.type = AdsTypeDeclaration.Factory.newInstance(columnAssoc.getValType());
//                    }
                } else {
                    this.type = type;
                }
            }
        }
        public final DdsTableDef tableAssoc;
        private ArgumentInfo returnValue;
        private ArrayList<ArgumentInfo> parameters;
        private boolean isValid;
        private String errorMessage = null;

        public ArgumentInfo getReturnValueInfo() {
            return returnValue;
        }

        public List<ArgumentInfo> getParameterInfos() {
            if (parameters == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<>(parameters);
            }
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
        private final Id pidAsStrParamId = Id.Factory.loadFrom("mprPidAsStr__________________");
        private final Id checkExistance = Id.Factory.loadFrom("mprCheckExisstance___________");

        private ArgumentsProvider(AdsSystemMethodDef source) {
            isValid = true;
            if (source.getId() == ID_LOAD_BY_PK || source.getId() == ID_LOAD_BY_PID_STR) {
                AdsClassDef clazz = getOwnerClass();
                if (clazz instanceof AdsEntityObjectClassDef) {
                    tableAssoc = ((AdsEntityObjectClassDef) clazz).findTable(source);
                    if (tableAssoc != null) {
                        this.returnValue = new ArgumentInfo(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_PARAMETER), null, AdsTypeDeclaration.Factory.newInstance(clazz), null, null);
                        this.parameters = new ArrayList<>();
                        if (source.getId() == ID_LOAD_BY_PID_STR) {
                            this.parameters.add(new ArgumentInfo(pidAsStrParamId, "pidAsStr", AdsTypeDeclaration.Factory.newInstance(EValType.STR), null, null));
                        } else {
                            for (org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo info : tableAssoc.getPrimaryKey().getColumnsInfo()) {
                                DdsColumnDef c = info.findColumn();
                                if (c != null) {
                                    AdsPropertyDef prop = clazz.getProperties().findById(c.getId(), EScope.ALL).get();
                                    this.parameters.add(new ArgumentInfo(c.getId(), c.getName(), AdsTypeDeclaration.Factory.newInstance(c.getValType()), c, prop));
                                } else {
                                    errorMessage = "Not all columns of primary key can be found";
                                    this.isValid = false;
                                    break;
                                }
                            }
                        }
                        this.parameters.add(new ArgumentInfo(checkExistance, "checkExistance", AdsTypeDeclaration.Factory.newPrimitiveType("boolean"), null, null));
                    } else {
                        this.isValid = false;
                        errorMessage = "Table not found";
                    }
                } else {
                    this.isValid = false;
                    tableAssoc = null;
                    errorMessage = "The method must be declared in entity based class";
                }
            } else {
                this.isValid = false;
                tableAssoc = null;
                errorMessage = "Unsupported system method type";
            }
        }
    }

    public ArgumentsProvider getArgumentsProvider() {
        return new ArgumentsProvider(this);
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (collection instanceof Methods.LocalMethods || collection instanceof MethodGroups) {
            return getOwnerClass() == RadixObjectsUtils.findContainer(collection, AdsClassDef.class);
        }
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly() || getId().equals(ID_REPORT_OPEN) || getId().equals(ID_REPORT_EXECUTE);
    }
}
