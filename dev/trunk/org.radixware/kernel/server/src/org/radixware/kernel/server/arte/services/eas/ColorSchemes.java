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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;


public class ColorSchemes extends CommonSelectorAddons<ColorScheme> {

    private static final String COLOR_SCHEME_CLASS_GUID = "aclE65CQY2JMRHAVFOBY4ESCWHFPM";
    private final PreparedStatement qryEasCommonSortings;
    
    ColorSchemes(final Arte arte) throws SQLException {
        super(arte);

        qryEasCommonSortings = arte.getDbConnection().get().prepareStatement(
                "select "
                + "addons.guid, addons.title, addons.lastUpdateTime "
                + "from "
                + "rdx_easselectoraddons addons "
                + "where "
                + "addons.isActive=1 and "
                + "addons.classGuid='" + COLOR_SCHEME_CLASS_GUID + "' and "
                + "addons.tableGuid=? and "
                + "(addons.selPresentations is null or addons.selPresentations like ?) "
                + "order by "
                + "addons.seq");

        
    }
    
    public ColorScheme getColorScheme(final Arte arte, final RadClassDef classDef, final Id selPresentationId){
        try{
            Map<Id,ColorScheme> map=this.get(arte, classDef, selPresentationId);
            if (!map.isEmpty())
                 return map.values().iterator().next();
        }catch(SQLException exception){
            arte.getTrace().put(Messages.MLS_ID_EAS_UNABLE_TO_LOAD_COLOR_SCHEME, 
                                new ArrStr(classDef.getName(),
                                           classDef.getId().toString(),
                                           ExceptionTextFormatter.exceptionStackToString(exception)));
        }
        return null;
    }
    
    @Override
    protected Map<Id, ColorScheme> loadAddons(Arte arte, RadClassDef classDef, Id selPresentationId) throws SQLException {
        final Id tableId = classDef.getEntityId();
        final ResultSet rs;
        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryEasCommonSortings.setString(1, tableId.toString());
            qryEasCommonSortings.setString(2, "%" + selPresentationId.toString() + "%");
            rs = qryEasCommonSortings.executeQuery();
        } finally {
            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        final Map<Id, ColorScheme> colorSchemes = new LinkedHashMap<>();
        try {
            while (rs.next()) {
                final String colorSchemeIdAsStr = rs.getString("guid");
                final String title = rs.getString("title");
                final Timestamp timestamp = rs.getTimestamp("lastUpdateTime");
                final ColorScheme colorScheme =
                        new ColorScheme(Id.Factory.loadFrom(colorSchemeIdAsStr),
                        tableId,
                        title,
                        timestamp == null ? 0 : timestamp.getTime()
                        );
                colorSchemes.put(colorScheme.getId(), colorScheme);
            }
        } finally {
            rs.close();
        }
        return colorSchemes;
    }              
}
