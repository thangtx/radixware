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
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsReportExpressionCell extends AdsReportCell {

    private final Jml expression;
    private static final String EXPRESSION_STR = "Expression";

    protected AdsReportExpressionCell() {
        expression = Jml.Factory.newInstance(this, EXPRESSION_STR);
        setName("Expression");
    }

    protected AdsReportExpressionCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);
        if (!xCell.isSetName()) {
            setName("Expression");
        }
        expression = Jml.Factory.loadFrom(this, xCell.getExpression(),EXPRESSION_STR);
    }
    
    protected AdsReportExpressionCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);
        if (!xCell.isSetName()) {
            setName("Expression");
        }
        expression = Jml.Factory.loadFrom(this, xCell.getExpression(), EXPRESSION_STR);
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);
        expression.appendTo(xCell.addNewExpression(), saveMode);
    }

    public Jml getExpression() {
        return expression;
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.EXPRESSION;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_EXPRESSION_CELL;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        expression.visit(visitor, provider);
    }

    // for codegen, see AdsReportClassWriter
    public String calcExpression() {
        return "";
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
    
    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return true;
    }
}