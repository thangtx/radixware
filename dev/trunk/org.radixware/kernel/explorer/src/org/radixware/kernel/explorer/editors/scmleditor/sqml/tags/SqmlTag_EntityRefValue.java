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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.schemas.xscml.Sqml.Item.EntityRefValue;


public class SqmlTag_EntityRefValue  extends SqmlTag_AbstractReference{
    
    private final EntityRefValue entityRefValue;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_ENTITY_REF_PARAMETER";
    
    public SqmlTag_EntityRefValue(final IClientEnvironment environment,final EntityRefValue entityRefValue){
        super(environment);
        this.entityRefValue = (EntityRefValue) entityRefValue.copy();
    }
    
     public SqmlTag_EntityRefValue(final IClientEnvironment environment,final Pid pid, final boolean isPrimaryKey, final Id secondaryKeyId){
        super(environment);
        entityRefValue = EntityRefValue.Factory.newInstance();
        entityRefValue.setReferencedObjectPidAsStr(pid.toString());
        entityRefValue.setReferencedTableId(pid.getTableId());
        if (isPrimaryKey) {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.PRIMARY_KEY_PROPS);
        } else if (secondaryKeyId != null) {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.SECONDARY_KEY_PROPS);
            entityRefValue.setPidTranslationSecondaryKeyId(secondaryKeyId);
        } else {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.AS_STR);
        }
        try {
           // String refObjectTitle = 
            pid.getDefaultEntityTitle(environment.getEasSession());
        } catch (ServiceClientException ex) {
            Logger.getLogger(SqmlTag_EntityRefValue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {           
        }
        entityRefValue.setLiteral(true);
    }
    
    @Override  
    public IScmlItem  getCopy() {
        return new SqmlTag_EntityRefValue( environment, entityRefValue);
    }
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public XmlObject saveToXml() {
        return entityRefValue;
    }

    @Override
    protected ISqmlTableIndexDef getIndexDef() {
        final ISqmlTableDef referencedTable = getReferencedTable();
        if (referencedTable != null) {
            final ISqmlTableIndices indices = referencedTable.getIndices();
            if (entityRefValue.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                return indices.getPrimaryIndex();
            } else {
                return indices.getIndexById(entityRefValue.getPidTranslationSecondaryKeyId());
            }
        }
        return null;
    }

    @Override
    protected ISqmlTableDef getReferencedTable() {
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        return definitions.findTableById(entityRefValue.getReferencedTableId());
    }

    @Override
    protected EPidTranslationMode getPidTranslationMode() {
        return entityRefValue.getPidTranslationMode();
    }
    
}
