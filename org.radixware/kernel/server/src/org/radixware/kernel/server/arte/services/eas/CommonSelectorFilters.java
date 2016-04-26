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

package org.radixware.kernel.server.arte.services.eas;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadFilterDef;
import org.radixware.kernel.server.sqml.Sqml;


final class CommonSelectorFilters extends CommonSelectorAddons<CommonSelectorFilter> {

    private static final String FILTER_CLASS_GUID = "aclLQBIKZWZABEIXKZOG4W3L6I5XI";
    private final PreparedStatement qryEasCommonFilters;

    public CommonSelectorFilters(final Arte arte) throws SQLException {
        super(arte);

        qryEasCommonFilters = arte.getDbConnection().get().prepareStatement(
                "select "
                + "addons.guid, addons.baseFilterGuid, addons.title, "
                + "addons.lastUpdateTime, addons.condition, addons.parameters "
                + "from "
                + "rdx_easselectoraddons addons "
                + "where "
                + "addons.isActive=1 and "
                + "addons.classGuid='" + FILTER_CLASS_GUID + "' and "
                + "addons.tableGuid=? and "
                + "(addons.selPresentations is null or addons.selPresentations like ?) "
                + "order by "
                + "addons.seq");

    }

    @Override
    protected Map<Id, CommonSelectorFilter> loadAddons(Arte arte, RadClassDef classDef, Id selPresentationId) throws SQLException {
        final Id tableId = classDef.getEntityId();
        final ResultSet rs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryEasCommonFilters.setString(1, tableId.toString());
            qryEasCommonFilters.setString(2, "%" + selPresentationId.toString() + "%");
            rs = qryEasCommonFilters.executeQuery();
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        final Map<Id, CommonSelectorFilter> commonFilters = new LinkedHashMap<Id, CommonSelectorFilter>();
        try {
            while (rs.next()) {
                final String filterIdAsStr = rs.getString("guid");
                final String baseFilterIdAsStr = rs.getString("baseFilterGuid");
                final Id baseFilterId = baseFilterIdAsStr == null ? null : Id.Factory.loadFrom(baseFilterIdAsStr);
                final RadFilterDef baseFilter;
                if (baseFilterId == null) {
                    baseFilter = null;
                } else {
                    try {
                        baseFilter = classDef.getPresentation().getFilterById(baseFilterId);
                    } catch (DefinitionError error) {
                        continue;//invalid filter - ignoring
                    }
                }
                final String title = rs.getString("title");
                final Clob conditionClob = rs.getClob("condition");
                final Timestamp timestamp = rs.getTimestamp("lastUpdateTime");
                final org.radixware.schemas.xscml.Sqml xsqml;
                final Sqml condition;
                if (conditionClob != null) {
                    final Reader conditionReader = conditionClob.getCharacterStream();
                    try {
                        xsqml = org.radixware.schemas.xscml.Sqml.Factory.parse(conditionReader);
                    } catch (IOException ex) {
                        traceFilterLoadingError(arte, filterIdAsStr, title, ex);
                        continue;
                    } catch (XmlException ex) {
                        traceFilterLoadingError(arte, filterIdAsStr, title, ex);
                        continue;
                    } finally {
                        try {
                            conditionReader.close();
                        } catch (IOException ex) {
                            traceReleasingResourcesError(arte, ex);
                        }

                        try {
                            conditionClob.free();
                        } catch (SQLException ex) {
                            traceReleasingResourcesError(arte, ex);
                        }
                    }

                    if (baseFilter == null) {
                        condition = Sqml.Factory.loadFrom("CommonFilterCondition", xsqml);
                    } else {//merging base and common filters conditions
                        condition = Sqml.Factory.newInstance();
                        condition.getItems().add(Sqml.Text.Factory.newInstance("("));
                        try {
                            condition.appendFrom(baseFilter.getCondition());
                        } catch (XmlException ex) {
                            traceFilterLoadingError(arte, filterIdAsStr, title, ex);
                            continue;
                        }
                        condition.getItems().add(Sqml.Text.Factory.newInstance("\n) and ("));
//                          try
                        condition.appendFrom(xsqml);
//                        } catch (XmlException ex) {
//                            traceFilterLoadingError(arte, filterIdAsStr, title, ex);
//                            continue;
//                        }
                        condition.getItems().add(Sqml.Text.Factory.newInstance("\n)"));
                    }

                } else {
                    continue;//no condition - ignoring filter
                }

                final Clob parametersClob = rs.getClob("parameters");
                final org.radixware.schemas.groupsettings.FilterParameters parameters;
                if (parametersClob != null) {
                    final Reader parametersReader = parametersClob.getCharacterStream();
                    try {
                        parameters =
                                org.radixware.schemas.groupsettings.FilterParameters.Factory.parse(parametersReader);
                    } catch (IOException ex) {
                        traceFilterLoadingError(arte, filterIdAsStr, title, ex);
                        continue;
                    } catch (XmlException ex) {
                        traceFilterLoadingError(arte, filterIdAsStr, title, ex);
                        continue;
                    } finally {
                        try {
                            parametersReader.close();
                        } catch (IOException ex) {
                            traceReleasingResourcesError(arte, ex);
                        }

                        try {
                            parametersClob.free();
                        } catch (SQLException ex) {
                            traceReleasingResourcesError(arte, ex);
                        }
                    }
                } else {
                    parameters = null;
                }

                final CommonSelectorFilter filter =
                        new CommonSelectorFilter(Id.Factory.loadFrom(filterIdAsStr),
                        tableId,
                        baseFilter,
                        title,
                        timestamp == null ? 0 : timestamp.getTime(),
                        condition,
                        parameters);
                commonFilters.put(filter.getId(), filter);
            }
        } finally {
            rs.close();
        }
        return commonFilters;
    }

    private void traceFilterLoadingError(final Arte arte, final String filterId, final String filterTitle, final Throwable error) {
        arte.getTrace().put(
                Messages.MLS_ID_EAS_UNABLE_TO_LOAD_COMMON_FILTERS,
                new ArrStr(filterTitle,
                filterId,
                ExceptionTextFormatter.exceptionStackToString(error)));
    }

    private void traceReleasingResourcesError(final Arte arte, final Throwable error) {
        arte.getTrace().put(
                Messages.MLS_ID_EAS_ERR_ON_RELEASING_RESOURCES,
                new ArrStr(ExceptionTextFormatter.exceptionStackToString(error)));
    }
}
