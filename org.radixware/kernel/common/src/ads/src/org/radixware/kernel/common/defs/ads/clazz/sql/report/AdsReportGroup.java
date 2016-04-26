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
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsReportGroup extends RadixObject {

    private final Jml condition;
    private AdsReportGroupBand headerBand;
    private AdsReportGroupBand footerBand;

    protected AdsReportGroup() {
        super();
        condition = Jml.Factory.newInstance(this, "Condition");

        setHeaderBandUsed(true);
        setFooterBandUsed(true);

        getHeaderBand().getFont().setSizeMm(5.0);
        getFooterBand().getFont().setSizeMm(5.0);
    }

    protected AdsReportGroup(org.radixware.schemas.adsdef.Report.Form.Groups.Group xGroup) {
        super(xGroup.getName());

        if (xGroup.isSetHeader()) {
            headerBand = new AdsReportGroupBand(xGroup.getHeader());
            headerBand.setContainer(this);
        }
        if (xGroup.isSetFooter()) {
            footerBand = new AdsReportGroupBand(xGroup.getFooter());
            footerBand.setContainer(this);
        }
        condition = Jml.Factory.loadFrom(this, xGroup.getCondition(), "Condition");
    }

    protected void appendTo(org.radixware.schemas.adsdef.Report.Form.Groups.Group xGroup, ESaveMode saveMode) {
        xGroup.setName(getName());

        if (isHeaderBandUsed()) {
            headerBand.appendTo(xGroup.addNewHeader(), saveMode);
        }
        if (isFooterBandUsed()) {
            footerBand.appendTo(xGroup.addNewFooter(), saveMode);
        }
        condition.appendTo(xGroup.addNewCondition(), saveMode);
    }

    /**
     * Get group header band, displayed for each group before details.
     * @return group header band or null if not used.
     */
    public AdsReportBand getHeaderBand() {
        return headerBand;
    }

    /**
     * Get group footer band, displayed for each group after details.
     * @return group footer band or null if not used.
     */
    public AdsReportBand getFooterBand() {
        return footerBand;
    }

    /**
     * @return true if header band displayed, false otherwise.
     */
    public boolean isHeaderBandUsed() {
        return headerBand != null;
    }

    /**
     * Create or remove header band.
     */
    public void setHeaderBandUsed(boolean used) {
        if (used) {
            if (this.headerBand == null) {
                setHeaderBand(new AdsReportGroupBand());
            }
        } else {
            setHeaderBand(null);
        }
    }

    /**
     * Set or remove header band.
     */
    public void setHeaderBand(AdsReportGroupBand band) {
        if (band!=null && headerBand!=null && !band.equals(headerBand) ||
            band==null &&  headerBand!=null ||  band!=null &&  headerBand==null) { 
            if (headerBand != null) {
                headerBand.setContainer(null);
            }
            headerBand = band;
            if (band != null) {
                band.setContainer(this);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return true if footer band displayed, false otherwise.
     */
    public boolean isFooterBandUsed() {
        return footerBand != null;
    }

    /**
     * Create or remove footer band.
     */
    public void setFooterBandUsed(boolean used) {
        if (used) {
            if (this.footerBand == null) {
                setFooterBand(new AdsReportGroupBand());
            }
        } else {
            setFooterBand(null);
        }
    }

    /**
     * Set or remove footer band.
     */
    public void setFooterBand(AdsReportGroupBand band) {
        if (band!=null && footerBand!=null && !band.equals(footerBand) ||
            band==null &&  footerBand!=null ||  band!=null &&  footerBand==null) {
            if (footerBand != null) {
                footerBand.setContainer(null);
            }
            footerBand = band;
            if (band != null) {
                band.setContainer(this);
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getCondition() {
        return condition;
    }

    public AdsReportForm getOwnerForm() {
        for (RadixObject container = this.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportForm) {
                return (AdsReportForm) container;
            }
        }
        return null;
    }

    /**
     * @return group index in getOwnerForm().getGroups(), or -1 if group instance is not in form.
     */
    public int getIndex() {
        final AdsReportForm ownerForm = getOwnerForm();
        if (ownerForm != null) {
            return ownerForm.getGroups().indexOf(this);
        } else {
            return -1;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);

        if (headerBand != null) {
            headerBand.visit(visitor, provider);
        }

        if (footerBand != null) {
            footerBand.visit(visitor, provider);
        }

        condition.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_GROUP;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    @Override
    protected void onModified() {
        final AdsReportForm form = getOwnerForm();
        if (form != null) {
            form.onModified();
        }
    }
}
