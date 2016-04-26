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

package org.radixware.kernel.common.builder.check.ads.clazz.sql;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.builder.check.ads.clazz.AdsClassChecker;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassSqlProcessor;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassWriter;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.ARR_BIN;
import static org.radixware.kernel.common.enums.EValType.ARR_BLOB;
import static org.radixware.kernel.common.enums.EValType.ARR_BOOL;
import static org.radixware.kernel.common.enums.EValType.ARR_CHAR;
import static org.radixware.kernel.common.enums.EValType.ARR_CLOB;
import static org.radixware.kernel.common.enums.EValType.ARR_DATE_TIME;
import static org.radixware.kernel.common.enums.EValType.ARR_INT;
import static org.radixware.kernel.common.enums.EValType.ARR_NUM;
import static org.radixware.kernel.common.enums.EValType.ARR_STR;
import static org.radixware.kernel.common.enums.EValType.BIN;
import static org.radixware.kernel.common.enums.EValType.BLOB;
import static org.radixware.kernel.common.enums.EValType.BOOL;
import static org.radixware.kernel.common.enums.EValType.CHAR;
import static org.radixware.kernel.common.enums.EValType.CLOB;
import static org.radixware.kernel.common.enums.EValType.DATE_TIME;
import static org.radixware.kernel.common.enums.EValType.INT;
import static org.radixware.kernel.common.enums.EValType.NUM;
import static org.radixware.kernel.common.enums.EValType.STR;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsSqlClassChecker<T extends AdsSqlClassDef> extends AdsClassChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsSqlClassDef.class;
    }

    private boolean setupInputParam(final PreparedStatement stmt, final AdsDynamicPropertyDef param, final Id pkParamPropId, int parameterIndex) throws SQLException {
        final int propCount;
        final AdsPropertyDef prop;
        final DdsPrimaryKeyDef pk;
        final AdsEntityObjectClassDef entity;
        final AdsTypeDeclaration typeDeclaration = param.getValue().getType();
        final EValType valType = typeDeclaration.getTypeId();
        final boolean isArray = valType.isArrayType();

        if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
            entity = AdsParameterPropertyDef.findEntity(param);
            if (entity == null) {
                return false;
            }

            if (pkParamPropId != null) {
                prop = entity.getProperties().findById(pkParamPropId, ExtendableDefinitions.EScope.ALL).get();
                if (prop == null) {
                    return false;
                }
                propCount = 1;
                pk = null;
            } else {
                final DdsTableDef table = entity.findTable(param);
                if (table == null) {
                    return false;
                }
                prop = null;
                pk = table.getPrimaryKey();
                propCount = pk.getColumnsInfo().size();
            }
        } else {
            propCount = 1;
            prop = null;
            pk = null;
            entity = null;
        }


        for (int i = 0; i < propCount; i++) {

            final EValType propValType;

            if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                final AdsPropertyDef curProp;
                if (prop != null) {
                    curProp = prop;
                } else {
                    final Id columnId = pk.getColumnsInfo().get(i).getColumnId();
                    curProp = entity.getProperties().findById(columnId, ExtendableDefinitions.EScope.ALL).get();
                }
                if (curProp != null) {
                    propValType = curProp.getValue().getType().getTypeId();
                } else {
                    propValType = null;
                }
            } else {
                propValType = valType;
            }
            if (propValType == null) {
                return false;
            }
            switch (propValType) {
                case INT:
                case ARR_INT:
                    stmt.setLong(parameterIndex, 0);
                    break;
                case NUM:
                case ARR_NUM:
                    stmt.setBigDecimal(parameterIndex, null);
                    break;
                case STR:
                case ARR_STR:
                case CHAR:
                case ARR_CHAR:
                    stmt.setString(parameterIndex, "");
                    break;
                case DATE_TIME:
                case ARR_DATE_TIME:
                    stmt.setTimestamp(parameterIndex, null);
                    break;
                case CLOB:
                case ARR_CLOB:
                    stmt.setClob(parameterIndex, (Clob) null);
                    break;
                case BLOB:
                case ARR_BLOB:
                    stmt.setBlob(parameterIndex, (Blob) null);
                    break;
                case BIN:
                case ARR_BIN:
                    stmt.setBytes(parameterIndex, null);
                    break;
                case BOOL:
                case ARR_BOOL:
                    stmt.setBoolean(parameterIndex, false);
                    break;
                default:
                    stmt.setObject(parameterIndex, null);
                    break;
            }
        }
        return true;
    }

    @Override
    public void check(T sqlClass, IProblemHandler problemHandler) {
        super.check(sqlClass, problemHandler);

        checkUsedTables(sqlClass, problemHandler);

        CheckOptions options = getCheckOptions();
        if (!sqlClass.isIgnoreSqlCheck() && options != null && options.isCheckSqlClassQuerySyntax() && options.getDbConnection() != null) {
            AdsSqlClassSqlProcessor.QueryInfo[] variants = AdsSqlClassWriter.getQueryVariants(sqlClass, options.getMaxSqlQueryVariants());
            for (AdsSqlClassSqlProcessor.QueryInfo v : variants) {
                try {
                    String query = v.query;
                    PreparedStatement stmt = options.getDbConnection().prepareStatement(query);
                    if (stmt != null) {
//                        int parameterIndex = 1;
//                        for (AdsSqlClassSqlProcessor.ParamInfo param : v.params) {
//                            setupInputParam(stmt, param.property, param.pkPropId, parameterIndex);
//                            parameterIndex++;
//                        }
                        stmt.getMetaData();
                    }
                } catch (SQLException e) {
                    StringBuilder problemMessage = new StringBuilder();
                    problemMessage.append("Problem in sql expression: ").append(e.getMessage()).append(" \nBad query variant: \n").append(v.query);
                    problemHandler.accept(RadixProblem.Factory.newError(sqlClass, problemMessage.toString()));
                }
            }
        }
        //    checkFieldUsagePolicy(sqlClass, problemHandler);
    }

    private void checkUsedTables(AdsSqlClassDef sqlClass, IProblemHandler problemHandler) {
        final Set<String> aliases = new HashSet<String>();

        for (AdsSqlClassDef.UsedTable usedTable : sqlClass.getUsedTables()) {
            final DdsTableDef table = usedTable.findTable();
            if (table != null) {
                if (!table.isGeneratedInDb()) {
                    warning(usedTable, problemHandler, "Illegal used table: '" + table.getQualifiedName() + "'");
                }
            } else {
                warning(usedTable, problemHandler, "Used table not found: #" + String.valueOf(usedTable.getTableId()));
            }
            final String alias = usedTable.getAlias();
            if (alias != null && !alias.isEmpty()) {
                if (!DbNameUtils.isCorrectDbName(alias)) {
                    error(sqlClass, problemHandler, "Illegal alias name: '" + String.valueOf(alias) + "'");
                }
                if (!aliases.add(alias)) {
                    error(sqlClass, problemHandler, "Duplicated alias: '" + String.valueOf(alias) + "'");
                }
            }
        }
    }

    private void checkFieldUsagePolicy(T sqlClass, IProblemHandler problemHandler) {
        if (sqlClass instanceof AdsCursorClassDef || sqlClass instanceof AdsReportClassDef) {
            Sqml sqml = sqlClass.getSource();
            if (sqml != null) {
                final CommentsAnalizer commentsAnalizer = CommentsAnalizer.Factory.newSqlCommentsAnalizer();
                Map<Id, PropSqlNameTag> props = new HashMap<>();
                for (Scml.Item item : sqml.getItems()) {
                    if (item instanceof Scml.Text) {
                        commentsAnalizer.process(((Scml.Text) item).getText());
                    } else if (item instanceof PropSqlNameTag) {
                        if (!commentsAnalizer.isInComment()) {
                            final PropSqlNameTag tag = (PropSqlNameTag) item;
                            if (tag.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) {
                                PropSqlNameTag found = props.get(tag.getPropId());
                                if (found != null) {
                                    ISqmlProperty prop = found.findProperty();
                                    String name = prop == null ? "" : (": " + prop.getName());
                                    problemHandler.accept(RadixProblem.Factory.newError(found, "Duplicate field usage in sqml" + name));
                                    problemHandler.accept(RadixProblem.Factory.newError(tag, "Duplicate field usage in sqml" + name));
                                } else {
                                    props.put(tag.getPropId(), tag);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean findToken(String token, StringBuilder sb) {
        String[] words = sb.toString().split(" ");
        for (String word : words) {
            if (word.toLowerCase().indexOf(token) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(T clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);

        if (clazz instanceof AdsCursorClassDef) {
            AdsClassDef anc = findBaseHandler(clazz, AdsCursorClassDef.PREDEFINED_ID);
            if (anc == null) {
                error(clazz, problemHandler, "Cursor class must be based on publishing of " + AdsCursorClassDef.PLATFORM_CLASS_NAME);
            } else {
                error(clazz, problemHandler, "Cursor class must extend " + anc.getQualifiedName());
            }
        }
    }
}
