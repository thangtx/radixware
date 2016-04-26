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

package org.radixware.kernel.server.arte;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.DebugUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.ArteInitializationError;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.PresentationEntityAdapter;

public final class Rights {

    protected static final String ERR_CANT_CHECK_USER_RIGHTS_ = "Can\'t check user rights: ";
    protected static final String ERR_CANT_PREPARE_DB_QRY = "Can\'t prepare Radix rights system service queries:\n";
    private final Arte arte;
    private final RightsDualControlController dualControlController;

    Rights(final Arte arte) {
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
                qryAppRoleDefNameById = arte.getDbConnection().get().prepareCall("select TITLE from RDX_AC_AppRole where guid=?");
            } catch (SQLException e) {
                throw new DatabaseError(ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryAppRoleDefNameById.setString(1, guid);
            final ResultSet rs = qryAppRoleDefNameById.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getString("TITLE");
                } else {
                    throw new DatabaseError("Can't find application role name, ID = \"" + guid + "\"", null);
                }
            } finally {
                rs.close();
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
                qryAppRoleDefIds = arte.getDbConnection().get().prepareCall("select guid, title from RDX_AC_AppRole");
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

    public class AccessPartition {

        private final String key;
        private final Boolean isProhibited;

        public AccessPartition(final String key_, final boolean isProhibited_) {
            key = key_;
            isProhibited = isProhibited_;
        }

        AccessPartition(final String key_) {
            key = key_;
            isProhibited = false;
        }

        public String getKey() {
            return key;
        }

        public Boolean getProhibited() {
            return Boolean.valueOf(isProhibited);
        }
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
     * @param roleIdsStr
     * @param accessArea - район доступа: ключи - ID-ы семейств, значения -
     * разделы доступа этих семейств, семейства не указанные в карте считаются
     * unbounded
     * @return
     */
    public final boolean getUserHasRoleForObject(final String userName, final String roleIdsStr, final Map<String, AccessPartition> accessArea) {
        return getUsrOrGrpHasRole(userName, null, roleIdsStr, accessArea, true, null, null);
    }

    /**
     *
     * @param userName
     * @param roleIdsStr
     * @param accessArea - район доступа: ключи - ID-ы семейств, значения -
     * разделы доступа этих семейств, семейства не указанные в карте считаются
     * unbounded
     * @return
     */
    public final boolean getUserHasRoleForObject(final String userName, final String roleIdsStr, 
            final Map<String, AccessPartition> accessArea, final boolean useInheritGroupRights, 
            final Integer ignoredId1, final Integer ignoredId2            
    //        , final boolean checkOnlyOld
    ) {
        return getUsrOrGrpHasRole(userName, null, roleIdsStr, accessArea, useInheritGroupRights, ignoredId1, ignoredId2
                //, checkOnlyOld
        );
    }

    /**
     *
     * @param groupName
     * @param roleIdsStr
     * @param accessArea - район доступа: ключи - ID-ы семейств, значения -
     * разделы доступа этих семейств, семейства не указанные в карте считаются
     * unbounded
     * @return
     */
    public final boolean getUserGroupHasRoleForObject(final String groupName, final String roleIdsStr, final Map<String, AccessPartition> accessArea, 
                    final Integer ignoredId1, final Integer ignoredId2
    //        , final boolean checkOnlyOld
    ) {
        return getUsrOrGrpHasRole(null, groupName, roleIdsStr, accessArea, true, ignoredId1, ignoredId2
                //, checkOnlyOld
        );
    }

    private boolean getUsrOrGrpHasRole(final String userName, final String groupName, 
                    final String roleIdsStr, final Map<String, AccessPartition> accessArea, 
                    final boolean useInheritGroupRights, final Integer ignoredId1, final Integer ignoredId2
                    //, final boolean checkOnlyOld
    ) {
        try {
            final StringBuilder qrySql = new StringBuilder("begin ? := RDX_Acs.");
            if (userName != null) {
                qrySql.append("user");
            } else {
                qrySql.append("group");
            }


            qrySql.append("HasRoleForObjectInternal(?, ?, TRdxAcsArea(TRdxAcsCoordinates(");


            boolean isFirst = true;
            final ArrayList<AccessPartition> coordVals = new ArrayList<AccessPartition>();

            for (Map.Entry<String, AccessPartition> coord : accessArea.entrySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    qrySql.append(',');
                }
                qrySql.append("TRdxAcsCoordinate(? ,'");
                qrySql.append(coord.getKey());
                qrySql.append("', ?)");

                coordVals.add(coord.getValue());
                //coordVals.add(coord.getValue());
            }
            qrySql.append("))");

            qrySql.append(", ?");
            qrySql.append(", ?");
            if (userName != null) {
                qrySql.append(", ?");
            }

            qrySql.append("); end;");

            final CallableStatement qry;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = arte.getDbConnection().get().prepareCall(qrySql.toString());
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                try {
                    // parameters result out, roleList in [, partition in ] x N
                    qry.registerOutParameter(1, java.sql.Types.INTEGER);

                    if (userName != null) {
                        qry.setString(2, userName);
                    } else {
                        qry.setString(2, groupName);
                    }
                    int i = 4;

                    qry.setString(3, roleIdsStr);

                    for (AccessPartition coordVal : coordVals) {
                        qry.setInt(i++, coordVal.getProhibited() ? 1 : 0);
                        qry.setString(i++, coordVal.getKey());
                    }

                    if (userName != null) {
                        qry.setInt(i++, useInheritGroupRights ? 1 : 0);
                    }

                    if (ignoredId1 != null) {
                        qry.setInt(i++, ignoredId1.intValue());
                    } else {
                        qry.setNull(i++, java.sql.Types.INTEGER);
                    }

                    if (ignoredId2 != null) {
                        qry.setInt(i++, ignoredId2.intValue());
                    } else {
                        qry.setNull(i++, java.sql.Types.INTEGER);
                    }
                    
                    //qry.setInt(i++, checkOnlyOld ? 1 : 0);

                    qry.execute();
                    return qry.getLong(1) != 0;
                } finally {
                    qry.close();
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        }
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
        for (Id id : startedIds){
            try{
                final RadRoleDef role = arte.getDefManager().getRoleDef(id);
                unprocessedRoles.add(role);
            }
            catch(DefinitionNotFoundError ex){
                arte.getTrace().put(EEventSeverity.ERROR, ex.getMessage(), EEventSource.ARTE);
            }
            
        }
        
        
        final Set<RadRoleDef> processedRoles = new HashSet();        
        
        while (!unprocessedRoles.isEmpty()){
            final int lastIndex = unprocessedRoles.size()-1;
            final RadRoleDef currRole = unprocessedRoles.get(lastIndex);
            unprocessedRoles.remove(lastIndex);
            
            processedRoles.add(currRole);
            
            final List<RadRoleDef> ancestors = currRole.getAncestors();
            for (RadRoleDef ancestor : ancestors){
                if (!processedRoles.contains(ancestor) && !unprocessedRoles.contains(ancestor)){
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
            qryCurUsrAllRolesInAreas = arte.getDbConnection().get().prepareCall(
                    "begin ? := RDX_Acs.getCurUserAllRolesForObject(" + areaListAsSql + "); end;");// parameters result out
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
                qryCheckCurrUserHaveUserRights = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.isCurUserHaveUserRights(?); end;");
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
                qryCheckCurrUserHaveGroupRights = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.isCurUserHaveGroupRights(?); end;");
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
                qryUserGroupExists = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.isGroupExist(?); end;");
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
                qryGroupHaveRights = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.isGroupHaveRights(?); end;");
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
                qryCurrUserIsInGroup = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.CurUserIsInGroup(?); end;");
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
                qryCurUserHasRightsUser2RoleId = arte.getDbConnection().get().prepareCall(
                        "begin ? := RDX_Acs.curUserHasRightsU2RId(?); end;");
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
                qryCurUserHasRightsUserGroup2RoleId = arte.getDbConnection().get().prepareCall(
                        "begin ? := RDX_Acs.curUserHasRightsG2RId(?); end;");
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
                qryUsrHasRole = arte.getDbConnection().get().prepareCall(
                        "begin ? := RDX_Acs.userHasRoleInArea(?, ?, null); end;");// parameters result out, user in, roleList in
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
                qryCurUsrHasRole = arte.getDbConnection().get().prepareCall(
                        "begin ? := RDX_Acs.curUserHasRoleInArea(?, null); end;");// parameters result out, roleList in
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
                qryCurUsrAllRolesInAllAreas = arte.getDbConnection().get().prepareCall(
                        "begin ? := RDX_Acs.curUserAllRolesInAllAreas(); end;");// parameters result out
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
                qryUserHasOwnRights = arte.getDbConnection().get().prepareCall(
                        "begin ?:= RDX_ACS.isUserHaveOwnRights(?); end;");
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
                qryMoveRights = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS_UTILS.moveRightsFromUserToGroup(?, ?); end;");

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
                    qryMoveInheritRights = arte.getDbConnection().get().prepareCall(
                            "begin RDX_ACS_UTILS.compileRightsForGroup(?); end;");

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
                qryReCompileRightsU2Ug = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS_UTILS.compileRightsForGroup(?); end;");
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
    private CallableStatement qryReClearInheritGroupRights = null;

    private CallableStatement getQryClearInheritGroupRights(final boolean useCached) {
        if (qryReClearInheritGroupRights == null || !useCached) {
            if (qryReClearInheritGroupRights != null) {
                try {
                    qryReClearInheritGroupRights.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryReClearInheritGroupRights = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS_UTILS.compileRightsForGrpBeforeDel(?); end;");
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryReClearInheritGroupRights;
    }

    public void compileGroupRightsBeforeDelete(final boolean useCash, final String name) {
        try {
            final CallableStatement qry = getQryClearInheritGroupRights(useCash);
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qry.setString(1, name);
                qry.execute();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            if (useCash) {
                compileGroupRightsBeforeDelete(false, name);
            } else {
                throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + e.getMessage(), e);
            }
        }
    }
    // End UserGroup
    // User2UserGroup
    private CallableStatement qryReCompileRights = null;

    private CallableStatement getQryReCompileRightsU2Ug(final boolean useCached) {
        if (qryReCompileRights == null || !useCached) {
            if (qryReCompileRights != null) {
                try {
                    qryReCompileRights.close();
                } catch (SQLException ex) {
                    //go on
                }
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryReCompileRights = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS_UTILS.compileRightsForUser(?); end;");
            } catch (SQLException e) {
                throw new ArteInitializationError(ERR_CANT_PREPARE_DB_QRY + e.getMessage(), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        return qryReCompileRights;
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

    // End User2UserGroup
    final void close() {
        close(qryAppRoleDefIds);
        close(qryAppRoleDefNameById);
        close(qryCheckCurrUserHaveGroupRights);
        close(qryCheckCurrUserHaveUserRights);
        close(qryCurUsrAllRolesInAllAreas);
        close(qryCurUsrHasRole);
        close(qryCurUserHasRightsUser2RoleId);
        close(qryCurUserHasRightsUserGroup2RoleId);
        close(qryCurrUserIsInGroup);
        close(qryUserGroupExists);
        close(qryUsrHasRole);

        close(qryUserHasOwnRights);
        close(qryMoveRights);
        close(qryMoveInheritRights);
        close(qryReCompileRightsU2Ug);
        close(qryReClearInheritGroupRights);
        close(qryReCompileRights);

        qryAppRoleDefIds = null;
        qryAppRoleDefNameById = null;
        qryCheckCurrUserHaveGroupRights = null;
        qryCheckCurrUserHaveUserRights = null;
        qryCurUsrAllRolesInAllAreas = null;
        qryCurUsrHasRole = null;
        qryCurUserHasRightsUser2RoleId = null;
        qryCurUserHasRightsUserGroup2RoleId = null;
        qryCurrUserIsInGroup = null;
        qryUserGroupExists = null;
        qryUsrHasRole = null;

        qryUserHasOwnRights = null;
        qryMoveRights = null;
        qryMoveInheritRights = null;
        qryReCompileRightsU2Ug = null;
        qryReClearInheritGroupRights = null;
        qryReCompileRights = null;
        //qryCurrUserIsInGroupU2Ug=null;
    }

    private void close(final Statement qry) {
        if (qry != null) {
            try {
                qry.close();
            } catch (SQLException ex) {
                DebugUtils.suppressException(ex);
            }
        }
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
