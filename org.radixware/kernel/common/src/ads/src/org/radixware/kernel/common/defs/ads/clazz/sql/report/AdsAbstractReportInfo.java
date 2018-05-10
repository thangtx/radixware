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
package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.jml.Jml;

public abstract class AdsAbstractReportInfo extends RadixObject implements AdsProfileSupport.IProfileable {

    protected Jml rowVisibleCondition = null;
    protected boolean exportColumnNames = true;
    
    protected static Jml getDefaultJml(RadixObject context, final String conditionName) {
        Jml jml = Jml.Factory.newInstance(context, conditionName);
        Jml.Item textItem = Jml.Text.Factory.newInstance("return true;");
        jml.getItems().add(textItem);
        return jml;
    }

    protected AdsAbstractReportInfo() {
        super();        
    }

    public boolean isExportColumnName() {
        return exportColumnNames;
    }

    public void setIsExportColumnName(boolean isExportColumnNames) {
        if (this.exportColumnNames != isExportColumnNames) {
            this.exportColumnNames = isExportColumnNames;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getRowCondition() {
        return rowVisibleCondition;
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);        
        if (rowVisibleCondition != null) {
            rowVisibleCondition.visit(visitor, provider);
        }
    }
    
    @Override
    public AdsProfileSupport getProfileSupport() {
        return new AdsProfileSupport(this);
    }

    @Override
    public boolean isProfileable() {
        return true;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return (AdsReportClassDef) getContainer();
    }
}
