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
package org.radixware.kernel.server.arte.rights;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.enums.EAccessAreaMode;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.ArteInitializationError;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.PresentationEntityAdapter;

public final class Rights {

    protected static final String ERR_CANT_CHECK_USER_RIGHTS_ = "Can\'t check user rights: ";
    protected static final String ERR_CANT_PREPARE_DB_QRY = "Can\'t prepare Radix rights system service queries:\n";

    private static final String qrySelectTitleStmtSQL = "select TITLE from RDX_AC_AppRole where guid=?";
    private static final Stmt qrySelectTitleStmt = new Stmt(qrySelectTitleStmtSQL, Types.VARCHAR);

    private static final String qrySelectQuidAndTitleStmtSQL = "select guid, title from RDX_AC_AppRole";
    private static final Stmt qrySelectQuidAndTitleStmt = new Stmt(qrySelectQuidAndTitleStmtSQL);

    private static final String qryGetCurUserAllRolesForObjectStmtSQL = "begin ? := RDX_Acs.getCurUserAllRolesForObject(''); end;";
    private static final Stmt qryGetCurUserAllRolesForObjectStmt = new Stmt(new Stmt(qryGetCurUserAllRolesForObjectStmtSQL, Types.VARCHAR), 1);

    private static final String qryIsCurUserHaveUserRightsStmtSQL = "begin ?:=RDX_ACS.isCurUserHaveUserRights(?); end;";
    private static final Stmt qryIsCurUserHaveUserRightsStmt = new Stmt(new Stmt(qryIsCurUserHaveUserRightsStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryIsCurUserHaveGroupRightsStmtSQL = "begin ?:=RDX_ACS.isCurUserHaveGroupRights(?); end;";
    private static final Stmt qryIsCurUserHaveGroupRightsStmt = new Stmt(new Stmt(qryIsCurUserHaveGroupRightsStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryIsGroupExistStmtSQL = "begin ?:=RDX_ACS.isGroupExist(?); end;";
    private static final Stmt qryIsGroupExistStmt = new Stmt(new Stmt(qryIsGroupExistStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryIsGroupHaveRightsStmtSQL = "begin ?:=RDX_ACS.isGroupHaveRights(?); end;";
    private static final Stmt qryIsGroupHaveRightsStmt = new Stmt(new Stmt(qryIsGroupHaveRightsStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryCurUserIsInGroupStmtSQL = "begin ?:=RDX_ACS.CurUserIsInGroup(?); end;";
    private static final Stmt qryCurUserIsInGroupStmt = new Stmt(new Stmt(qryCurUserIsInGroupStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryUserHasRightsU2RIdStmtSQL = "begin ? := RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_ARTE.getUserName(), ?); end;";
    private static final Stmt qryUserHasRightsU2RIdStmt = new Stmt(new Stmt(qryUserHasRightsU2RIdStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryUserHasRightsG2RIdStmtSQL = "begin ? := RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_ARTE.getUserName(), ?); end;";
    private static final Stmt qryUserHasRightsG2RIdStmt = new Stmt(new Stmt(qryUserHasRightsG2RIdStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryIsSuperAdminStmtSQL = "begin ? := RDX_ACS_UTILS.isSuperAdmin(RDX_ARTE.getUserName()); end;";
    private static final Stmt qryIsSuperAdminStmt = new Stmt(new Stmt(qryIsSuperAdminStmtSQL, Types.BIGINT), 1);
    
    
    private static final String qryReadPartitionsStmtSQL = "begin ? := RDX_ACS.readPartitions(?); end;";
    private static final Stmt qryReadPartitionsStmt = new Stmt(new Stmt(qryReadPartitionsStmtSQL, Types.CLOB, Types.BIGINT), 1);

    private static final String qryUserHasRoleInAreaStmtSQL = "begin ? := RDX_Acs.userHasRoleInArea(?, ?, null); end;";
    private static final Stmt qryUserHasRoleInAreaStmt = new Stmt(new Stmt(qryUserHasRoleInAreaStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR), 1);

    private static final String qryCurUserHasRoleInAreaStmtSQL = "begin ? := RDX_Acs.curUserHasRoleInArea(?, null); end;";
    private static final Stmt qryCurUserHasRoleInAreaStmt = new Stmt(new Stmt(qryCurUserHasRoleInAreaStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryCurUserAllRolesInAllAreasStmtSQL = "begin ? := RDX_Acs.curUserAllRolesInAllAreas(); end;";
    private static final Stmt qryCurUserAllRolesInAllAreasStmt = new Stmt(new Stmt(qryCurUserAllRolesInAllAreasStmtSQL, Types.VARCHAR), 1);

    private static final String qryIsUserHaveOwnRightsStmtSQL = "begin ?:= RDX_ACS.isUserHaveOwnRights(?); end;";
    private static final Stmt qryIsUserHaveOwnRightsStmt = new Stmt(new Stmt(qryIsUserHaveOwnRightsStmtSQL, Types.BIGINT, Types.VARCHAR), 1);

    private static final String qryMoveRightsFromUserToGroupStmtSQL = "begin RDX_ACS_UTILS.moveRightsFromUserToGroup(?, ?); end;";
    private static final Stmt qryMoveRightsFromUserToGroupStmt = new Stmt(new Stmt(qryMoveRightsFromUserToGroupStmtSQL, Types.VARCHAR, Types.VARCHAR));

    private static final String qryCompileRightsForGroupStmtSQL = "begin RDX_ACS_UTILS.compileRightsForGroup(?); end;";
    private static final Stmt qryCompileRightsForGroupStmt = new Stmt(new Stmt(qryCompileRightsForGroupStmtSQL, Types.VARCHAR));

//    private static final String qryCompileRightsForGrpBeforeDelStmtSQL = "begin RDX_ACS_UTILS.compileRightsForGrpBeforeDel(?); end;";
//    private static final Stmt qryCompileRightsForGrpBeforeDelStmt = new Stmt(new Stmt(qryCompileRightsForGrpBeforeDelStmtSQL, Types.VARCHAR));

    private static final String qryCompileRightsForUserStmtSQL = "begin RDX_ACS_UTILS.compileRightsForUser(?); end;";
    private static final Stmt qryCompileRightsForUserStmt = new Stmt(new Stmt(qryCompileRightsForUserStmtSQL, Types.VARCHAR));

    private static final String qryCompileRightsStmtSQL = "begin RDX_ACS_UTILS.compileRights(); end;";
    private static final Stmt qryCompileRightsStmt = new Stmt(new Stmt(qryCompileRightsStmtSQL));

        private final IDbQueries delegate = new DelegateDbQueries(this, null);
    private final Arte arte;
    private final RightsDualControlController dualControlController;

    private Rights() {
        this.arte = null;
        this.dualControlController = null;
    }

    public Rights(final Arte arte) {
        this.arte = arte;
        this.dualControlController = new RightsDualControlController(arte);
    }

    public RightsDualControlController getDualControlController() {
        return dualControlController;
    }
    private CallableStatement qryAppRoleDefNameById = null;

    public String getRoleTitleById(final Id id) {
        final EDefinitionIdPrefix prefix = id.getPrefix();
        if (prefix != null) {
            if (prefix.equals(EDefinitionIdPrefix.APPLICATION_ROLE)) {
                return getAppRoleTitleById(id.toString());
            } else {
                final RadRoleDef role = arte.getDefManager().getRoleDef(id);
                if (role != null) {
                    String title = role.getTitle();
                    if (title == null) {
                        title = role.getQualifiedName();
                    }
                    return title;
                }
            }
        }
        throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
    }

    private String getAppRoleTitleById(final String guid) {
        if (qryAppRoleDefNameById == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryAppRoleDefNameById = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qrySelectTitleStmt);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryAppRoleDefNameById.setString(1, guid);
            try (final ResultSet rs = qryAppRoleDefNameById.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TITLE");
                } else {
                    throw new DatabaseError("Can't find application role name, ID = \"" + guid + "\"", null);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't get application role name, ID = \"" + guid + "\":\n" + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryAppRoleDefIds = null;

    public List<AppRole> getAppRoleList() {
        final List<AppRole> list = new ArrayList();
        if (qryAppRoleDefIds == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryAppRoleDefIds = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qrySelectQuidAndTitleStmt);
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            final ResultSet rs = qryAppRoleDefIds.executeQuery();
            try {
                while (rs.next()) {
                    list.add(new AppRole(rs.getString(1), rs.getString(2)));
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't get application role list:\n" + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        return list;
    }

    public final boolean getCurUserHasRole(final String roleIdsStr) {
        return getCurUserHasRole(roleIdsStr, true);
    }

    private boolean getCurUserHasRole(final String roleIdsStr, final boolean useCachedQry) {
        try {
            final CallableStatement qryCurUserHasRole = getQryCurUserHasRole(useCachedQry);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryCurUserHasRole.setString(2, roleIdsStr);
                qryCurUserHasRole.execute();
                return qryCurUserHasRole.getLong(1) != 0;
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            if (useCachedQry) {
                return getCurUserHasRole(roleIdsStr, false);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
            }
        }
    }

    protected final String getRoleAncestorIdList(final RadRoleDef role) {
        final StringBuilder sList = new StringBuilder(role.getId().toString());
        for (RadRoleDef ancestor : role.getAncestors()) {
            sList.append(getRoleAncestorIdList(ancestor));
        }
        return sList.toString();
    }

    public final String getFinalRoleAncestorList(final Id roleId) {
        final RadRoleDef role = arte.getDefManager().getRoleDef(roleId);
        return getRoleAncestorIdList(role);
    }

    /**
     *
     * @param userName
     * @param roleId
     * @param accessArea - район доступа: ключи - ID-ы семейств, значения -
     * разделы доступа этих семейств и режимы (старые)
     * @return
     */
    public final boolean getUserHasRoleForObject(final String userName, final String roleId, final Map<Id, AccessPartition> accessArea) {
        return RightsUtils.existsRightsOnArea(arte, userName, roleId, accessArea);
    }

    public final boolean getUserHasRole(final String userName, final String roleIdsStr) {
        return getUserHasRole(userName, roleIdsStr, true);
    }

    private boolean getUserHasRole(final String userName, final String roleIdsStr, final boolean useCachedQry) {
        try {
            final CallableStatement qryUserHasRole = getQryUserHasRole(useCachedQry);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryUserHasRole.setString(2, userName);
                qryUserHasRole.setString(3, roleIdsStr);
                qryUserHasRole.execute();
                return qryUserHasRole.getLong(1) != 0;
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            if (useCachedQry) {
                return getUserHasRole(userName, roleIdsStr, false);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
            }
        }
    }

    /**
     * Все роли пользователя без фильтрации по координатам в системе прав,
     * ипользуются для отсева ролей которые не смогут получить доступ к новому
     * объекту ни при каких координатах. (при создании координаты объекта в
     * системе прав заранее не известны) После создания необходим расчет списка
     * ролей с учетом координат объекта в системе прав. В общем случае после
     * создания объекта прав может стать меньше (!!!).
     */
    public final List<Id> getCurUserAllRolesInAllAreas() {
        return getCurUserAllRolesInAllAreas(true);
    }

    public final Set<RadRoleDef> getCurUserAllRolesInAllAreasWithRolesHierarchy() {

        final List<Id> startedIds = getCurUserAllRolesInAllAreas();
        final List<RadRoleDef> unprocessedRoles = new ArrayList(startedIds.size() * 4);
        for (Id id : startedIds) {
            try {
                final RadRoleDef role = arte.getDefManager().getRoleDef(id);
                unprocessedRoles.add(role);
            } catch (DefinitionNotFoundError ex) {
                arte.getTrace().put(EEventSeverity.ERROR, ex.getMessage(), EEventSource.ARTE);
            }

        }

        final Set<RadRoleDef> processedRoles = new HashSet();

        while (!unprocessedRoles.isEmpty()) {
            final int lastIndex = unprocessedRoles.size() - 1;
            final RadRoleDef currRole = unprocessedRoles.get(lastIndex);
            unprocessedRoles.remove(lastIndex);

            processedRoles.add(currRole);

            final List<RadRoleDef> ancestors = currRole.getAncestors();
            for (RadRoleDef ancestor : ancestors) {
                if (!processedRoles.contains(ancestor) && !unprocessedRoles.contains(ancestor)) {
                    unprocessedRoles.add(ancestor);
                }
            }
        }
        return processedRoles;
    }

    /**
     * Все роли пользователя с фильтрацией по указанным координатам в системе
     * прав. Используется в случае, когда для выборки задано декларативное
     * условие отбора объектов, для отсева ролей, который не могут получить
     * доступ к объектам в указанных координатах. Список координат формируется
     * на основе декларативного условия.
     *
     * @param areaListAsSql координаты в системе прав в формате параметра вызова
     * sql-метода
     * @return список идентификаторов ролей текущего пользователя
     */
    public final List<Id> getCurUserAllRolesInAreas(final String areaListAsSql) {
        final CallableStatement qryCurUsrAllRolesInAreas;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            qryCurUsrAllRolesInAreas = arte.getDbConnection().get().prepareCall(qryGetCurUserAllRolesForObjectStmt.getText().replace("''", areaListAsSql));// parameters result out
            qryCurUsrAllRolesInAreas.registerOutParameter(1, java.sql.Types.VARCHAR);
        } catch (SQLException e) {
            throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryCurUsrAllRolesInAreas.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            return getIdsListFromStr(qryCurUsrAllRolesInAreas.getString(1));
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    private List<Id> getCurUserAllRolesInAllAreas(final boolean useCachedQry) {
        try {
            final CallableStatement qryCurUserAllRolesInAllAreas = getQryCurUserAllRolesInAllAreas(useCachedQry);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryCurUserAllRolesInAllAreas.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            return getIdsListFromStr(qryCurUserAllRolesInAllAreas.getString(1));
        } catch (SQLException e) {
            if (useCachedQry) {
                return getCurUserAllRolesInAllAreas(false);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
            }
        }
    }

    private static List<Id> getIdsListFromStr(final String input) {
        if (input == null) {
            return Collections.emptyList();
        }
        final String[] roleIdsArr = input.split(",");
        final List<Id> roleIds = new ArrayList<>(roleIdsArr.length);
        for (String id : roleIdsArr) {
            roleIds.add(Id.Factory.loadFrom(id));
        }
        return Collections.unmodifiableList(roleIds);
    }
    private CallableStatement qryCheckCurrUserHaveUserRights = null;

    public boolean isCurUserHaveUserRights(final String group) {
        if (qryCheckCurrUserHaveUserRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCheckCurrUserHaveUserRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsCurUserHaveUserRightsStmt);
                qryCheckCurrUserHaveUserRights.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryCheckCurrUserHaveUserRights.setString(2, group);
            qryCheckCurrUserHaveUserRights.execute();
            return qryCheckCurrUserHaveUserRights.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryCheckCurrUserHaveGroupRights = null;

    public boolean isCurUserHaveGroupRights(final String group) {
        if (qryCheckCurrUserHaveGroupRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCheckCurrUserHaveGroupRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsCurUserHaveGroupRightsStmt);
                qryCheckCurrUserHaveGroupRights.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryCheckCurrUserHaveGroupRights.setString(2, group);
            qryCheckCurrUserHaveGroupRights.execute();
            return qryCheckCurrUserHaveGroupRights.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryUserGroupExists = null;

    public boolean isUserGroupExists(final String group) {
        if (qryUserGroupExists == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryUserGroupExists = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsGroupExistStmt);
                qryUserGroupExists.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryUserGroupExists.setString(2, group);
            qryUserGroupExists.execute();
            return qryUserGroupExists.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryGroupHaveRights = null;

    public boolean isGroupHaveRights(final String group) {
        if (qryGroupHaveRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryGroupHaveRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsGroupHaveRightsStmt);
                qryGroupHaveRights.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryGroupHaveRights.setString(2, group);
            qryGroupHaveRights.execute();
            return qryGroupHaveRights.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryCurrUserIsInGroup = null;

    public boolean isCurUserInGroup(final String group) {
        if (qryCurrUserIsInGroup == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCurrUserIsInGroup = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCurUserIsInGroupStmt);
                qryCurrUserIsInGroup.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryCurrUserIsInGroup.setString(2, group);
            qryCurrUserIsInGroup.execute();
            return qryCurrUserIsInGroup.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryCurUserHasRightsUser2RoleId = null;

    public final boolean curUserHasRightsUser2Role(final int id) {

        if (qryCurUserHasRightsUser2RoleId == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCurUserHasRightsUser2RoleId = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryUserHasRightsU2RIdStmt);
                qryCurUserHasRightsUser2RoleId.registerOutParameter(1, java.sql.Types.INTEGER);

            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryCurUserHasRightsUser2RoleId.setInt(2, id);
            qryCurUserHasRightsUser2RoleId.execute();
            return qryCurUserHasRightsUser2RoleId.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private CallableStatement qryCurUserHasRightsUserGroup2RoleId = null;

    public final boolean curUserHasRightsUserGroup2Role(final int id) {

        if (qryCurUserHasRightsUserGroup2RoleId == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCurUserHasRightsUserGroup2RoleId = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryUserHasRightsG2RIdStmt);
                qryCurUserHasRightsUserGroup2RoleId.registerOutParameter(1, java.sql.Types.INTEGER);

            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryCurUserHasRightsUserGroup2RoleId.setInt(2, id);
            qryCurUserHasRightsUserGroup2RoleId.execute();
            return qryCurUserHasRightsUserGroup2RoleId.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    public final boolean curUserHasRightsOnPartitionGroup(final Id familyId, final int partitionGroupId) {
        if (isSuperAdmin()) {
            return true;
        }
        final String partitions = readPartitions(partitionGroupId);        
        return RightsUtils.curUserHasRightsOnPartitionGroup(arte, familyId, partitions);
    }
    
    public final boolean curUserHasRightsOnPartitionGroup(final Id familyId, final String partitions) {
        if (isSuperAdmin()) {
            return true;
        }
        return RightsUtils.curUserHasRightsOnPartitionGroup(arte, familyId, partitions);
    }    
    
    
    private CallableStatement qryReadPartitions = null;

    private String readPartitions(final int id) {
        if (qryReadPartitions == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryReadPartitions = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryReadPartitionsStmt);
                qryReadPartitions.registerOutParameter(1, java.sql.Types.CLOB);

            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryReadPartitions.setInt(2, id);
            qryReadPartitions.execute();
            final Clob partitions = qryReadPartitions.getClob(1);
            if (partitions == null) {
                return null;
            }
            return  partitions.getSubString(1, (int)partitions.length());
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }    
    
    
     
    
    private CallableStatement qryIsSuperAdmin = null;

    private boolean isSuperAdmin() {
        if (qryIsSuperAdmin == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryIsSuperAdmin = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsSuperAdminStmt);
                qryIsSuperAdmin.registerOutParameter(1, java.sql.Types.INTEGER);

            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryIsSuperAdmin.execute();
            return qryIsSuperAdmin.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    //
    public final boolean getCurUserCanAccess(final Entity entity) {
        final List<RadEditorPresentationDef> allPres = entity.getPresentationMeta().getEditorPresentations();
        final PresentationEntityAdapter<Entity> presentationAdapter = arte.getCache().getPresentationAdapter(entity);
        return presentationAdapter.selectEditorPresentation(allPres) != null;
    }

    public final boolean getCurUserCanAccess(final EDrcServerResource resource) {
        return getCurUserHasRole(arte.getDefManager().getServerResourceRoleIdsStr(resource));
    }

    public final boolean getUserCanAccess(final String userName, final EDrcServerResource resource) {
        return getUserHasRole(userName, arte.getDefManager().getServerResourceRoleIdsStr(resource));
    }
    private CallableStatement qryUsrHasRole = null;

    private CallableStatement getQryUserHasRole(final boolean useCached) {
        if (qryUsrHasRole == null || !useCached) {
            if (qryUsrHasRole != null) {
                try {
                    qryUsrHasRole.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryUsrHasRole = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryUserHasRoleInAreaStmt);// parameters result out, user in, roleList in
                qryUsrHasRole.registerOutParameter(1, java.sql.Types.INTEGER);

            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryUsrHasRole;
    }
    private CallableStatement qryCurUsrHasRole = null;

    private CallableStatement getQryCurUserHasRole(final boolean useCached) {
        if (qryCurUsrHasRole == null || !useCached) {
            if (qryCurUsrHasRole != null) {
                try {
                    qryCurUsrHasRole.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCurUsrHasRole = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCurUserHasRoleInAreaStmt);// parameters result out, roleList in
                qryCurUsrHasRole.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryCurUsrHasRole;
    }
    private CallableStatement qryCurUsrAllRolesInAllAreas = null;

    private CallableStatement getQryCurUserAllRolesInAllAreas(final boolean useCached) {
        if (qryCurUsrAllRolesInAllAreas == null || !useCached) {
            if (qryCurUsrAllRolesInAllAreas != null) {
                try {
                    qryCurUsrAllRolesInAllAreas.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCurUsrAllRolesInAllAreas = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCurUserAllRolesInAllAreasStmt);// parameters result out
                qryCurUsrAllRolesInAllAreas.registerOutParameter(1, java.sql.Types.VARCHAR);
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryCurUsrAllRolesInAllAreas;
    }
    //User
    private static CallableStatement qryUserHasOwnRights = null;

    public boolean isUserHaveOwnRights(String user) {
        if (qryUserHasOwnRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryUserHasOwnRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryIsUserHaveOwnRightsStmt);
                qryUserHasOwnRights.registerOutParameter(1, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryUserHasOwnRights.setString(2, user);
            qryUserHasOwnRights.execute();
            return qryUserHasOwnRights.getLong(1) != 0;
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    private static CallableStatement qryMoveRights = null;
    private static CallableStatement qryMoveInheritRights = null;

    public void moveRightsFromUserToGroup(String user, String group) {
        if (qryMoveRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryMoveRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryMoveRightsFromUserToGroupStmt);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        try {
            if (qryMoveInheritRights == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryMoveInheritRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCompileRightsForGroupStmt);
                } catch (SQLException e) {
                    throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryMoveRights.setString(1, user);
                qryMoveRights.setString(2, group);
                qryMoveRights.execute();

                qryMoveInheritRights.setString(1, group);
                qryMoveInheritRights.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        }
    }
    // End User
    // UserGroup2Role
    private CallableStatement qryReCompileRightsU2Ug = null;

    private CallableStatement getQryReCompileRights(final boolean useCached) {
        if (qryReCompileRightsU2Ug == null || !useCached) {
            if (qryReCompileRightsU2Ug != null) {
                try {
                    qryReCompileRightsU2Ug.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryReCompileRightsU2Ug = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCompileRightsForGroupStmt/*"begin RDX_ACS_UTILS.compileRightsForGroup(?); end;"*/);
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryReCompileRightsU2Ug;
    }

    public void compileGroupRights(final String group, final boolean useCash) {
        try {
            final CallableStatement qry = getQryReCompileRights(useCash);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qry.setString(1, group);
                qry.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            if (useCash) {
                compileGroupRights(group, false);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
            }
        }
    }
    // End  UserGroup2Role
    // UserGroup
//    private CallableStatement qryReClearInheritGroupRights = null;
//
//    private CallableStatement getQryClearInheritGroupRights(final boolean useCached) {
//        if (qryReClearInheritGroupRights == null || !useCached) {
//            if (qryReClearInheritGroupRights != null) {
//                try {
//                    qryReClearInheritGroupRights.close();
//                } catch (SQLException ex) {
//                    //go on
//                }
//            }
//            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
//            try {
//                qryReClearInheritGroupRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCompileRightsForGrpBeforeDelStmt);
//            } catch (SQLException e) {
//                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
//            } finally {
//                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
//            }
//        }
//        return qryReClearInheritGroupRights;
//    }

//    public void compileGroupRightsBeforeDelete(final boolean useCash, final String name) {
//        try {
//            final CallableStatement qry = getQryClearInheritGroupRights(useCash);
//            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
//            try {
//                qry.setString(1, name);
//                qry.execute();
//            } finally {
//                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
//            }
//        } catch (SQLException e) {
//            if (useCash) {
//                compileGroupRightsBeforeDelete(false, name);
//            } else {
//                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
//            }
//        }
//    }
    // End UserGroup
    // User2UserGroup
    private CallableStatement qryReCompileRightsForUser = null;

    private CallableStatement getQryReCompileRightsU2Ug(final boolean useCached) {
        if (qryReCompileRightsForUser == null || !useCached) {
            if (qryReCompileRightsForUser != null) {
                try {
                    qryReCompileRightsForUser.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryReCompileRightsForUser = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCompileRightsForUserStmt);
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryReCompileRightsForUser;
    }

    public void compileUserRights(String name, boolean useCash) {
        try {
            final CallableStatement qry = getQryReCompileRightsU2Ug(useCash);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qry.setString(1, name);
                qry.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            if (useCash) {
                compileUserRights(name, false);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
            }
        }
    }
    
    //
    private CallableStatement qryCompileRights = null;
    public void compileRights() {
        if (qryCompileRights == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryCompileRights = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryCompileRightsStmt);
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        try {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try { 
                qryCompileRights.execute(); 
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
        }
    }    
    
    
    

    public static AccessPartition createAccessPartition(final Arte arte, final Id familyId,
            final EAccessAreaMode mode, final String singlePartitionVal, final Integer partitionGroupId) {
        return RightsUtils.createAccessPartition(arte, familyId, mode, singlePartitionVal, partitionGroupId);
    }

    // End User2UserGroup
    public final void close() {
        RightsUtils.close(qryAppRoleDefIds);
        RightsUtils.close(qryAppRoleDefNameById);
        RightsUtils.close(qryCheckCurrUserHaveGroupRights);
        RightsUtils.close(qryCheckCurrUserHaveUserRights);
        RightsUtils.close(qryCurUsrAllRolesInAllAreas);
        RightsUtils.close(qryCurUsrHasRole);
        RightsUtils.close(qryCurUserHasRightsUser2RoleId);
        RightsUtils.close(qryCurUserHasRightsUserGroup2RoleId);
        RightsUtils.close(qryIsSuperAdmin);
        RightsUtils.close(qryReadPartitions);
        RightsUtils.close(qryCurrUserIsInGroup);
        RightsUtils.close(qryUserGroupExists);
        RightsUtils.close(qryUsrHasRole);

        RightsUtils.close(qryUserHasOwnRights);
        RightsUtils.close(qryMoveRights);
        RightsUtils.close(qryMoveInheritRights);
        RightsUtils.close(qryReCompileRightsU2Ug);
//        RightsUtils.close(qryReClearInheritGroupRights);
        RightsUtils.close(qryReCompileRightsForUser);

        qryAppRoleDefIds = null;
        qryAppRoleDefNameById = null;
        qryCheckCurrUserHaveGroupRights = null;
        qryCheckCurrUserHaveUserRights = null;
        qryCurUsrAllRolesInAllAreas = null;
        qryCurUsrHasRole = null;
        qryCurUserHasRightsUser2RoleId = null;
        qryCurUserHasRightsUserGroup2RoleId = null;
        qryIsSuperAdmin = null;
        qryReadPartitions = null;
        qryCurrUserIsInGroup = null;
        qryUserGroupExists = null;
        qryUsrHasRole = null;

        qryUserHasOwnRights = null;
        qryMoveRights = null;
        qryMoveInheritRights = null;
        qryReCompileRightsU2Ug = null;
//        qryReClearInheritGroupRights = null;
        qryReCompileRightsForUser = null;
        //qryCurrUserIsInGroupU2Ug=null;
    }

    public static class AppRole {

        final String id;
        final String title;

        public AppRole(final String id, final String name) {
            this.id = id;
            this.title = name;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
