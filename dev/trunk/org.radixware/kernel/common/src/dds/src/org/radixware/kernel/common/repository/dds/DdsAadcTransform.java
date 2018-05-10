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

package org.radixware.kernel.common.repository.dds;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.product.AadcTransform;
import org.radixware.schemas.product.AadcTransformTable;

public class DdsAadcTransform extends RadixObject {
    private List<String> warnings;
    private DdsAadcTransformTables tables;
    private boolean required = false;
    private boolean forbidden = false;
    
    public DdsAadcTransform() {
        warnings = new ArrayList<>();
        tables = new DdsAadcTransformTables(this);
    }
    
    protected DdsAadcTransform(DdsUpdateInfo info){
        this();
        setContainer(info);
    }
    
    protected void loadFrom(AadcTransform xAadcTransform) {
        required = xAadcTransform.getRequired();
        forbidden = xAadcTransform.getForbidden();
        List<String> warningList = xAadcTransform.getWarningList();
        warnings.clear();
        tables.clear();
        
        if (warningList != null && !warningList.isEmpty()) {
            for (String warning : warningList) {
                if (warning != null && !warning.isEmpty()) {
                    warnings.add(warning);
                }
            }
        }
        List<AadcTransformTable> tablesList = xAadcTransform.getTableList();
        if (tablesList != null && !tablesList.isEmpty()) {
            for (AadcTransformTable xTable : tablesList) {
                final DdsAadcTransformTable table = new DdsAadcTransformTable(xTable);
                tables.add(table);
            }
        }
    }
    
    
    public void loadFrom(DdsAadcTransform aadcTransform) {
        required = aadcTransform.isRequired();
        forbidden = aadcTransform.isForbidden();
        List<String> warningList = aadcTransform.getWarnings();
        warnings.clear();
        if (warningList != null && !warningList.isEmpty()) {
            for (String warning : warningList) {
                if (warning != null && !warning.isEmpty()) {
                    warnings.add(warning);
                }
            }
        }
        DdsAadcTransformTables tablesList = aadcTransform.getTables();
        tables.clear();
        if (tablesList != null && !tablesList.isEmpty()) {
            for (DdsAadcTransformTable xTable : tablesList) {
                final DdsAadcTransformTable table = new DdsAadcTransformTable();
                table.loadFrom(xTable);
                tables.add(table);
            }
        }
    }
    
    protected void appendTo(AadcTransform xAadcTransform) {
        xAadcTransform.setRequired(required);
        xAadcTransform.setForbidden(forbidden);
        if (!warnings.isEmpty()) {
            for (String warning : warnings) {
                xAadcTransform.addWarning(warning);
            }
            
        }
        
        if (!tables.isEmpty()) {
            for (DdsAadcTransformTable table : tables) {
                table.appendTo(xAadcTransform.addNewTable());
            }
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        if (this.required != required) {
            this.required = required;
            if (!required) {
                tables.clear();
                warnings.clear();
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(boolean forbiden) {
        if (this.forbidden != forbiden) {
            this.forbidden = forbiden;
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<String> getWarnings() {
        return warnings;
    }


    public DdsAadcTransformTables getTables() {
        return tables;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider); 
        tables.visit(visitor, provider);
    }
    
    public class DdsAadcTransformTables extends RadixObjects<DdsAadcTransformTable>{

        public DdsAadcTransformTables(DdsAadcTransform container) {
            super(container);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.SQL_OGG;
    }
    
    public boolean isEmpty() {
        return tables.isEmpty() && warnings.isEmpty();
    }
    
    public final String getStateTitle() {
        StringBuilder sb = new StringBuilder("AADC Transformation: ");
        if (isForbidden()) {
            sb.append("Forbidden");
        } else if (isRequired()) {
            sb.append("Required");
        } else {
            sb.append("Not Required");
        }
        return sb.toString();
    }
    
    
}
