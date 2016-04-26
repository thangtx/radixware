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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.DbName;


public class SqmlTag_DbName extends SqmlTag {

    private Sqml.Item.DbName dbName;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_DB_NAME";
    private final ISqmlDefinition definition;
    private final String sql;
    
    public SqmlTag_DbName(final IClientEnvironment env, final ISqmlDefinition definition, final long pos, final EDefinitionDisplayMode showMode) {
        super(env, pos, definition==null?false :definition.isDeprecated());
        this.definition = definition;
        this.sql = "";
        setDbNameXml();
        setDisplayText(showMode);
    }
    
    public SqmlTag_DbName(final IClientEnvironment env, final long pos, final Sqml.Item.DbName dbName, final EDefinitionDisplayMode showMode) {
        super(env, pos);
        this.dbName = (DbName) dbName.copy();
        this.definition = env.getSqmlDefinitions().findDefinitionByIdPath(dbName.getPath());        
        this.sql = dbName.getSql();
        setDisplayText(showMode);
    }
       
    public SqmlTag_DbName(final IClientEnvironment env, final SqmlTag_DbName source) {
        super(env, source);
        this.definition = source.definition;
        this.sql = source.sql;
        setDbNameXml();
    }
    
    private String calculateTooltip() {
       return "";
    }
    
    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setDbName(dbName);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }

    @Override
    public TagInfo copy() {
        return new SqmlTag_DbName(environment, this);
    }

    private void setDbNameXml() {
        dbName = DbName.Factory.newInstance();
        dbName.setPath(Arrays.asList(definition.getIdPath()));
        dbName.setSql(sql);
    }
   
    /**
     * Checks definition for validity and sets display text depending on the check result
     * @param showMode 
     */
    private void setDisplayText(final EDefinitionDisplayMode showMode) {
        if(definition == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("???");
            if(dbName.getPath() == null) {
                sb.append("DbName");
            } else {
                for(Id id : dbName.getPath()) {
                    sb.append(id.toString());
                    sb.append(' ');
                }
            }
            sb.append("???");
            setDisplayedInfo("", sb.toString());
        } else if(definition instanceof ISqmlBrokenDefinition) {
            setDisplayedInfo("", definition.getDisplayableText(showMode));
        } else {
            setIsDeprecated(definition.isDeprecated());
            setDisplayedInfo(showMode);
        }
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        
        final StringBuilder sb = new StringBuilder("dbName[");
        final ISqmlDefinitions defs = environment.getSqmlDefinitions();
        final List<Id> path = dbName.getPath();
        for(int i = 0; i < path.size(); i++) {
            final Id id = path.get(i);
            final ISqmlDefinition def = defs.findDefinitionByIdPath(Collections.singletonList(id));
            sb.append(def.getDisplayableText(showMode));
            if(i < path.size() - 1){
                sb.append('.');
            }
        }
        sb.append(']');
        setDisplayedInfo(calculateTooltip(), sb.toString());
        return true;
    }
}
