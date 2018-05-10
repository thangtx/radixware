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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class RoleReader {
    private final Arte arte;
    
    public RoleReader(final Arte arte) {
        
        this.arte = arte;
        
    }
    
    public ResultSet selectOwnCoords(final String username, final String[] ids) {
        return selectOwnCoords(username, Arrays.asList(ids));
//        try {
//            final PreparedStatement stmt = arte.getDbConnection().get().prepareStatement(
//                    prepareOwnQuery(username, ids));
//            final ResultSet result = stmt.executeQuery();
//            return result;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            return null;
//        }
    }
    
    public ResultSet selectOwnCoords(final String username, final List<String> ids) {
        try {
            final PreparedStatement stmt = arte.getDbConnection().get().prepareStatement(prepareOwnQuery(username, ids));
            final ResultSet result = stmt.executeQuery();
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public ResultSet selectGroupCoords(final String groupname, final List<String> ids) {
        try {
            final PreparedStatement stmt = arte.getDbConnection().get().prepareStatement(prepareGroupQuery(groupname, ids));
            final ResultSet result = stmt.executeQuery();
            
            return result;
        } catch(SQLException ex) {
            return null;
        }
    }
    
     public ResultSet selectGroupCoords(final String groupname, final String[] ids) {
        return selectGroupCoords(groupname, Arrays.asList(ids));
//        try {
//            final PreparedStatement stmt = arte.getDbConnection().get().prepareStatement(
//                    prepareGroupQuery(groupname, ids));
//            final ResultSet result = stmt.executeQuery();
//            
//            return result;
//        } catch(SQLException ex) {
//            return null;
//        }
    }
    
    
//    private String prepareOwnQuery(final String username, final String [] ids) {
//        return prepareQuery(username, "RDX_AC_USER2ROLE", ids, " WHERE isOwn=1 AND username='");
//    }
    
    private String prepareOwnQuery(final String username, final List<String> ids) {
        return prepareQuery(username, "RDX_AC_USER2ROLE", ids, " WHERE isOwn=1 AND username='");
    }
    
//    private String prepareGroupQuery(final String groupName, final String [] ids) {
//        return prepareQuery(groupName, "RDX_AC_USERGROUP2ROLE", ids, " WHERE groupname='");
//    }
    
    private String prepareGroupQuery(final String groupName, final List<String> ids) {
        return prepareQuery(groupName, "RDX_AC_USERGROUP2ROLE", ids, " WHERE groupname='");
    }
    
//    private String prepareQuery(final String name, final String table,final String [] ids, final String filter) {
//        
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        for(int i = 0; i < ids.length; i++) {
//            sb.append("MA$$");
//            sb.append(ids[i]);
//            sb.append(", PA$$");
//            sb.append(ids[i]);
//            if(i + 1 < ids.length) sb.append(",");
//            
//        }
//        sb.append(" FROM ");
//        sb.append(table);
//        sb.append(filter);
//        sb.append(name);
//        sb.append("'");
//        return sb.toString();
//    }
    
    private String prepareQuery(final String name, final String table,final List<String> ids, final String filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for(int i = 0; i < ids.size(); i++) {
            sb.append("MA$$");
            sb.append(ids.get(i));
            sb.append(", PA$$");
            sb.append(ids.get(i));
            if(i + 1 < ids.size()) sb.append(",");
            
        }
        sb.append(" FROM ");
        sb.append(table);
        sb.append(filter);
        sb.append(name);
        sb.append("'");
        return sb.toString();
    
    }
}
