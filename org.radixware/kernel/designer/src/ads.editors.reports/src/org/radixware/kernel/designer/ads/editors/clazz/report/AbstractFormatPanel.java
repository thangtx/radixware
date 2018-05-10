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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EValType;

public abstract class AbstractFormatPanel extends JPanel {

    public static class EmptyFormatPanel extends AbstractFormatPanel {

        private EmptyFormatPanel(final AdsReportFormat cell, boolean isReadOnly) {
            super(cell, isReadOnly);
        }

        @Override
        protected void setupInitialValues() {
        }

    }

    public static class Factory {

        static AbstractFormatPanel newInstance(AdsReportFormat cell, EValType type, boolean isReadOnly) {
            switch (type) {
                case INT:
                    return new IntFormatPanel(cell, isReadOnly);
                case NUM:
                    return new NumFormatPanel(cell, isReadOnly);
                case DATE_TIME:
                    return new DateTimeFormatPanel(cell, isReadOnly);
            }

            return new EmptyFormatPanel(cell, isReadOnly);
        }
    }

    protected AdsReportFormat cell;
    protected boolean isReadOnly;

    protected abstract void setupInitialValues();

    protected AbstractFormatPanel(final AdsReportFormat cell, boolean isReadOnly) {
        this.cell = cell;
        this.isReadOnly = isReadOnly;
    }

    public void update(AdsReportFormat cell) {
        this.cell = cell;
        setupInitialValues();
    }
}
