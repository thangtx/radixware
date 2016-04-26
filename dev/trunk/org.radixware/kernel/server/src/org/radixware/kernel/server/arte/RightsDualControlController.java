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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.roles.RadRoleDef;

public class RightsDualControlController {

    final Arte arte;

    public RightsDualControlController(final Arte arte) {
        this.arte = arte;
    }
    
    public boolean isCurUserCanAccept(){
        final Id superAdminId = Id.Factory.loadFrom(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
        final Id acceptorId = Id.Factory.loadFrom(EDrcPredefinedRoleId.ACCEPTOR.getValue());
        
        final Set<RadRoleDef> roles = arte.getRights().getCurUserAllRolesInAllAreasWithRolesHierarchy();
        for (RadRoleDef role : roles){
            if (superAdminId.equals(role.getId()) || acceptorId.equals(role.getId())){
                return true;
            }
        }
        return false;
    }

    public void acceptRolesForUser(
            final String userName,
            
            final Reference<List<Integer>> unacceptedRoles,
            final Reference<List<String>> unacceptedGroups,
            
            final Reference<Integer> addedRCount,
            final Reference<Integer> replacedRCount,
            final Reference<Integer> removedRCount,
            
            
            final Reference<Integer> addedU2GCount,
            final Reference<Integer> removedU2GCount
            
    ) {
        acceptRoles(true, userName, unacceptedRoles, unacceptedGroups, addedRCount, replacedRCount, removedRCount, addedU2GCount, removedU2GCount);
    }

    public void acceptRolesForGroup(
            final String groupName,
                        
            final Reference<List<Integer>> unacceptedRoles,
            final Reference<List<String>> unacceptedUsers,
            
            final Reference<Integer> addedRCount,
            final Reference<Integer> replacedRCount,
            final Reference<Integer> removedRCount,
            
            final Reference<Integer> addedU2GCount,
            final Reference<Integer> removedU2GCount
            
    ) {
        acceptRoles(false, groupName, unacceptedRoles, unacceptedUsers, addedRCount, replacedRCount, removedRCount, addedU2GCount, removedU2GCount);
    }

    private CallableStatement qryHaveNotAcceptedEntities = null;

    public boolean haveNotAcceptedEntities() {
        if (qryHaveNotAcceptedEntities == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryHaveNotAcceptedEntities = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.haveNotAcceptedEntities(); end;");
            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryHaveNotAcceptedEntities.registerOutParameter(1, java.sql.Types.INTEGER);
            qryHaveNotAcceptedEntities.execute();

            final int rez = qryHaveNotAcceptedEntities.getInt(1);
            return rez != 0;

        } catch (SQLException e) {
            throw new DatabaseError(Rights.ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    
    
    private static List<String> parseLines(final String text) {
        List<String> lines = new ArrayList();
        if (text == null || text.isEmpty()) {
            return lines;
        }
        
        StringTokenizer stringTokenizer = new StringTokenizer(text, "\n");

        while (stringTokenizer.hasMoreTokens()) {
            String s = stringTokenizer.nextToken();
            s = s.trim();
            lines.add(s);
        }
        return lines;
    }

    private CallableStatement qryAcceptNewRoles = null;

    private void acceptRoles(
            final boolean isUser2Role,
            final String userOrGroupName,
            
            final Reference<List<Integer>> unacceptedRoles,
            final Reference<List<String>> unacceptedUsersOrGroups,
            
            final Reference<Integer> addedRCount,
            final Reference<Integer> replacedRCount,
            final Reference<Integer> removedRCount,
            
            final Reference<Integer> addedU2GCount,
            final Reference<Integer> removedU2GCount
    ) {

        if (qryAcceptNewRoles == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryAcceptNewRoles = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS.acceptRolesAndU2G(?, ?, ?, ?, ?, ?, ?, ?, ?); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            
            qryAcceptNewRoles.setInt(1, isUser2Role ? 1 : 0); //userTable - bool - user or group
            qryAcceptNewRoles.setString(2, userOrGroupName);

            qryAcceptNewRoles.registerOutParameter(3, java.sql.Types.VARCHAR);//ignoredRoles - list of unaccepted roles
            qryAcceptNewRoles.registerOutParameter(4, java.sql.Types.VARCHAR);//ignoredUsersOrGroups - list of unaccepted users or groups

            qryAcceptNewRoles.registerOutParameter(5, java.sql.Types.INTEGER);//addedRCount
            qryAcceptNewRoles.registerOutParameter(6, java.sql.Types.INTEGER);//replacedRCount
            qryAcceptNewRoles.registerOutParameter(7, java.sql.Types.INTEGER);//removedRCount

            qryAcceptNewRoles.registerOutParameter(8, java.sql.Types.INTEGER);//addedU2GCount
            qryAcceptNewRoles.registerOutParameter(9, java.sql.Types.INTEGER);//removedU2GCount
            
            qryAcceptNewRoles.execute();
            
            final String unacceptedRolesAsString = qryAcceptNewRoles.getString(3);
            final String unacceptedUsersOrGroupsAsString = qryAcceptNewRoles.getString(4);
            
            addedRCount.set(qryAcceptNewRoles.getInt(5));    
            replacedRCount.set(qryAcceptNewRoles.getInt(6));
            removedRCount.set(qryAcceptNewRoles.getInt(7));
            
            addedU2GCount.set(qryAcceptNewRoles.getInt(8));
            removedU2GCount.set(qryAcceptNewRoles.getInt(9));
            
            if (unacceptedUsersOrGroupsAsString != null && !unacceptedUsersOrGroupsAsString.isEmpty()){
                final List<String> unacceptedUsersOrGroupsAsList = parseLines(unacceptedUsersOrGroupsAsString);
                unacceptedUsersOrGroups.set(unacceptedUsersOrGroupsAsList);
            }
            else{
                unacceptedUsersOrGroups.set(new ArrayList());
            }
            
            
            if (unacceptedRolesAsString!=null && !unacceptedRolesAsString.isEmpty()){
                final List<String> unacceptedRolesAsStringList = parseLines(unacceptedRolesAsString);
                final List<Integer> unacceptedRolesAsList = new ArrayList();

                for (String unacceptedRoleAsString : unacceptedRolesAsStringList){
                    unacceptedRolesAsList.add(Integer.parseInt(unacceptedRoleAsString));
                }
                unacceptedRoles.set(unacceptedRolesAsList);
            }
            else{
                unacceptedRoles.set(new ArrayList());
            }
            
             

        } catch (SQLException e) {
            throw new DatabaseError(Rights.ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    public void getRolesCountForGroup(String userGroupName, Reference<Integer> accessedRCount, Reference<Integer> unaccessedRCount,
            Reference<Integer> accessedU2GCount, Reference<Integer> unaccessedU2GCount) {
        getRolesCount(false, userGroupName, accessedRCount, unaccessedRCount, accessedU2GCount, unaccessedU2GCount);
    }

    public void getRolesCountForUser(String userName, Reference<Integer> accessedRCount, Reference<Integer> unaccessedRCount,
            Reference<Integer> accessedU2GCount, Reference<Integer> unaccessedU2GCount) {
        getRolesCount(true, userName, accessedRCount, unaccessedRCount, accessedU2GCount, unaccessedU2GCount);
    }

    private CallableStatement qryGetOwn2RolesModifyCount = null;

    private void getRolesCount(boolean isUser2Role, String userOrGroupName,
            Reference<Integer> accessedRCount,
            Reference<Integer> unaccessedRCount,
            Reference<Integer> acceptedU2GCount,
            Reference<Integer> unacceptedU2GCount
    ) {

        if (qryGetOwn2RolesModifyCount == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryGetOwn2RolesModifyCount = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS.getRolesAndU2GCount(?, ?, ?, ?, ?, ?); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {

            qryGetOwn2RolesModifyCount.setInt(1, isUser2Role ? 1 : 0);
            qryGetOwn2RolesModifyCount.setString(2, userOrGroupName);
            qryGetOwn2RolesModifyCount.registerOutParameter(3, java.sql.Types.INTEGER);
            qryGetOwn2RolesModifyCount.registerOutParameter(4, java.sql.Types.INTEGER);
            qryGetOwn2RolesModifyCount.registerOutParameter(5, java.sql.Types.INTEGER);
            qryGetOwn2RolesModifyCount.registerOutParameter(6, java.sql.Types.INTEGER);
            qryGetOwn2RolesModifyCount.execute();
            accessedRCount.set(qryGetOwn2RolesModifyCount.getInt(3));
            unaccessedRCount.set(qryGetOwn2RolesModifyCount.getInt(4));
            acceptedU2GCount.set(qryGetOwn2RolesModifyCount.getInt(5));
            unacceptedU2GCount.set(qryGetOwn2RolesModifyCount.getInt(6));
        } catch (SQLException e) {
            throw new DatabaseError(Rights.ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    private CallableStatement qryIsUsedDualControlWhenAssigningRoles = null;

    public boolean isUsedDualControlWhenAssigningRoles() {

        if (qryIsUsedDualControlWhenAssigningRoles == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryIsUsedDualControlWhenAssigningRoles = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.usedDualControlWhenAssignRoles(); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }

        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryIsUsedDualControlWhenAssigningRoles.registerOutParameter(1, java.sql.Types.INTEGER);
            qryIsUsedDualControlWhenAssigningRoles.execute();
            return qryIsUsedDualControlWhenAssigningRoles.getInt(1) == 1;

        } catch (SQLException e) {
            throw new DatabaseError(Rights.ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }

    }

    private CallableStatement qryNextUser2RoleId = null;

    private int getNext2RoleId(boolean user) {
        if (qryNextUser2RoleId == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryNextUser2RoleId = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.getNext2RoleId(?); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryNextUser2RoleId.registerOutParameter(1, java.sql.Types.INTEGER);
            qryNextUser2RoleId.setInt(2, user ? 1 : 0);
            qryNextUser2RoleId.execute();
            return qryNextUser2RoleId.getInt(1);

        } catch (SQLException e) {
            throw new DatabaseError("Can't get next right sequence:\n" + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    public boolean isNewUser2Role(final int id) {
        return isNewUserOrGroup2Role(id, true);
    }
     
    public boolean isNewUserGroup2Role(final int id) {
        return isNewUserOrGroup2Role(id, false);
    }
    
    private CallableStatement qryIsNewUserOrGroup2Role = null;

    private boolean isNewUserOrGroup2Role(final int id, final boolean user) {
        if (qryIsNewUserOrGroup2Role == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryIsNewUserOrGroup2Role = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.isNewUserOrGroup2Role(?, ?); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryIsNewUserOrGroup2Role.registerOutParameter(1, java.sql.Types.INTEGER);
            qryIsNewUserOrGroup2Role.setInt(2, user ? 1 : 0);
            qryIsNewUserOrGroup2Role.setInt(3, id);
            qryIsNewUserOrGroup2Role.execute();
            return qryIsNewUserOrGroup2Role.getInt(1) == 1;
        } catch (SQLException e) {
            throw new DatabaseError("Can't get next right sequence:\n" + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }
    

    public final int getNextUser2RoleId() {
        return getNext2RoleId(true);
    }

    public final int getNextUserGroup2RoleId() {
        return getNext2RoleId(false);
    }    
    
    private CallableStatement qryMayReplaceOrRevokeRole = null;

    private boolean mayReplaceOrRevokeRole(final int id, final boolean user) {

        if (qryMayReplaceOrRevokeRole == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryMayReplaceOrRevokeRole = arte.getDbConnection().get().prepareCall(
                        "begin ?:=RDX_ACS.mayReplaceOrRevokeRole(?, ?); end;");

            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryMayReplaceOrRevokeRole.registerOutParameter(1, java.sql.Types.INTEGER);
            qryMayReplaceOrRevokeRole.setInt(2, user ? 1 : 0);
            qryMayReplaceOrRevokeRole.setInt(3, id);
            qryMayReplaceOrRevokeRole.execute();
            return qryMayReplaceOrRevokeRole.getInt(1) == 1;
        } catch (SQLException e) {
            throw new DatabaseError("Can't get next right sequence:\n" + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    public boolean mayReplaceOrRevokeUser2Role(int id) {
        return mayReplaceOrRevokeRole(id, true);
    }

    public boolean mayReplaceOrRevokeUserGroup2Role(int id) {
        return mayReplaceOrRevokeRole(id, false);
    }
    
    
    private RoleAndUserOrGroup createRoleAndUserOrGroup(final int user2RoleCount, final String firstUser2Role){
        List<String> lines;
        String firstUser = null;
        RadRoleDef roleForFirstUser = null;
        Id roleId=null;
        
        if (user2RoleCount>0){
            lines = Utils.parseLines(firstUser2Role);
            firstUser = lines.get(0);
            final String roleIdAsString = lines.get(1);
            roleId = Id.Factory.loadFrom(roleIdAsString);
            try{
               roleForFirstUser = arte.getDefManager().getRoleDef(roleId);
            }
            catch(DefinitionNotFoundError exception){
                //do nothing
            }
        }
        
        final RoleAndUserOrGroup unacceptedUsers = roleForFirstUser==null ? 
                new RoleAndUserOrGroup(roleId, firstUser, user2RoleCount) : 
                new RoleAndUserOrGroup(roleForFirstUser, firstUser, user2RoleCount);
        return unacceptedUsers;
    }
    
    private UnacceptedEntities createUnacceptedEntities(final int user2UserGroupCount,
                                                           final String firstUser2UserGroup,
                                                           
                                                           final int userGroup2RoleCount,
                                                           final String firstUserGroup2Role,
                                                           
                                                           final int user2RoleCount,
                                                           final String firstUser2Role){
        

        final RoleAndUserOrGroup unacceptedUsers = createRoleAndUserOrGroup(user2RoleCount, firstUser2Role);
        final RoleAndUserOrGroup unacceptedGroups = createRoleAndUserOrGroup(userGroup2RoleCount, firstUserGroup2Role);
                    
        String user = null;
        String group = null;
        if (user2UserGroupCount>0){
            List<String> lines;
            lines = Utils.parseLines(firstUser2UserGroup);
            user = lines.get(0);
            group = lines.get(1);
        }
       
        
        User2UserOrGroup unacceptedUser2UserGroups = new User2UserOrGroup(user, group, user2UserGroupCount);       
        
        UnacceptedEntities rezult = new UnacceptedEntities(unacceptedUsers, unacceptedGroups, unacceptedUser2UserGroups);
        
        return rezult;
        
    }
    

    private CallableStatement qryGetNotAcceptedEntities = null;

    public UnacceptedEntities getNotAcceptedEntities() {

        if (qryGetNotAcceptedEntities == null) {
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryGetNotAcceptedEntities = arte.getDbConnection().get().prepareCall(
                        "begin RDX_ACS.getNotAcceptedEntities(?, ?, ?, ?, ?, ?); end;");
            } catch (SQLException e) {
                throw new DatabaseError(Rights.ERR_CANT_PREPARE_DB_QRY + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryGetNotAcceptedEntities.registerOutParameter(1, java.sql.Types.INTEGER);
            qryGetNotAcceptedEntities.registerOutParameter(2, java.sql.Types.VARCHAR);
            qryGetNotAcceptedEntities.registerOutParameter(3, java.sql.Types.INTEGER);
            qryGetNotAcceptedEntities.registerOutParameter(4, java.sql.Types.VARCHAR);
            qryGetNotAcceptedEntities.registerOutParameter(5, java.sql.Types.INTEGER);
            qryGetNotAcceptedEntities.registerOutParameter(6, java.sql.Types.VARCHAR);
            qryGetNotAcceptedEntities.execute();

            final int user2UserGroupCount = qryGetNotAcceptedEntities.getInt(1);
            final String firstUser2UserGroup = qryGetNotAcceptedEntities.getString(2);
            final int userGroup2RoleCount = qryGetNotAcceptedEntities.getInt(3);
            final String firstUserGroup2Role = qryGetNotAcceptedEntities.getString(4);
            final int user2RoleCount = qryGetNotAcceptedEntities.getInt(5);
            final String firstUser2Role = qryGetNotAcceptedEntities.getString(6);
            
            return createUnacceptedEntities(user2UserGroupCount,
                                     firstUser2UserGroup,
                                     userGroup2RoleCount,
                                     firstUserGroup2Role,
                                     user2RoleCount,
                                     firstUser2Role);

        } catch (SQLException e) {
            throw new DatabaseError(Rights.ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), null);
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
    }

    public static class RoleAndUserOrGroup {

        private RoleAndUserOrGroup(final RadRoleDef role, final String userOrGroup, final int count) {
            this.role = role;
            this.userOrGroup = userOrGroup;
            this.count = count;
            this.roleId = role.getId();
        }

        private RoleAndUserOrGroup(final Id roleId, final String userOrGroup, final int count) {
            this.role = null;//Incorrect rosource
            this.userOrGroup = userOrGroup;
            this.count = count;
            this.roleId = roleId;
        }

        private final RadRoleDef role;
        private final Id roleId;
        private final String userOrGroup;
        private final int count;

        public RadRoleDef getRole() {
            return role;
        }

        public String getUserOrGroup() {
            return userOrGroup;
        }

        public int getCount() {
            return count;
        }

        public Id getRoleId() {
            return roleId;
        }
        
        
    }

    public static class User2UserOrGroup {

        private User2UserOrGroup(final String userGroup, final String user, final int count) {
            this.userGroup = userGroup;
            this.user = user;
            this.count = count;
        }

        private final String userGroup;
        private final String user;
        private final int count;

        public String getUserGroup() {
            return userGroup;
        }

        public String getUser() {
            return user;
        }

        public int getCount() {
            return count;
        }
    }

    public static class UnacceptedEntities {

        private UnacceptedEntities(RoleAndUserOrGroup unacceptedUsers, RoleAndUserOrGroup unacceptedUserGroups, User2UserOrGroup unacceptedUser2UserGroups) {
            this.unacceptedUsers = unacceptedUsers;
            this.unacceptedUserGroups = unacceptedUserGroups;
            this.unacceptedUser2UserGroups = unacceptedUser2UserGroups;
        }
        private final RoleAndUserOrGroup unacceptedUsers;
        private final RoleAndUserOrGroup unacceptedUserGroups;
        private final User2UserOrGroup unacceptedUser2UserGroups;

        public RoleAndUserOrGroup getUnacceptedUsers() {
            return unacceptedUsers;
        }

        public RoleAndUserOrGroup getUnacceptedUserGroups() {
            return unacceptedUserGroups;
        }

        public User2UserOrGroup getUnacceptedUser2UserGroups() {
            return unacceptedUser2UserGroups;
        }
    }

}
