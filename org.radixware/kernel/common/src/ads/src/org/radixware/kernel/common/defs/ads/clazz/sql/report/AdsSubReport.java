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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class AdsSubReport extends RadixObject {

    private Id reportId;
    private RadixObjects<Association> associations = new Associations(this);

    public static class Association extends RadixObject {

        private Id propertyId;
        private Id parameterId;

        public static class Factory {

            private Factory() {
            }

            public static Association newInstance() {
                return new Association();
            }

            public static Association loadFrom(org.radixware.schemas.adsdef.SubReport.Association xAssociation) {
                return new Association(xAssociation);
            }
        }

        /**
         * Creates a new Association.
         */
        protected Association() {
        }

        protected Association(org.radixware.schemas.adsdef.SubReport.Association xAssociation) {
            this.propertyId = xAssociation.getPropertyId();
            this.parameterId = xAssociation.getParameterId();
        }

        public void appendTo(org.radixware.schemas.adsdef.SubReport.Association xAssociation, ESaveMode saveMode) {
            if (this.propertyId != null) {
                xAssociation.setPropertyId(this.propertyId);
            }
            if (this.parameterId != null) {
                xAssociation.setParameterId(this.parameterId);
            }
        }

        public Id getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Id propertyId) {
            if (!Utils.equals(this.propertyId, propertyId)) {
                this.propertyId = propertyId;
                setEditState(EEditState.MODIFIED);
            }
        }

        public Id getParameterId() {
            return parameterId;
        }

        public void setParameterId(Id parameterId) {
            if (!Utils.equals(this.parameterId, parameterId)) {
                this.parameterId = parameterId;
                setEditState(EEditState.MODIFIED);
            }
        }

        public AdsPropertyDef findProperty() {
            final AdsSubReport subReport = getOwnerSubReport();
            if (subReport != null) {
                final AdsReportClassDef ownerReport = subReport.getOwnerReport();
                if (ownerReport != null) {
                    return ownerReport.getProperties().findById(propertyId, EScope.ALL).get();
                }
            }
            return null;
        }

        public AdsPropertyDef getProperty() {
            final AdsPropertyDef property = findProperty();
            if (property != null) {
                return property;
            } else {
                throw new DefinitionNotFoundError(propertyId);
            }
        }

        public AdsParameterPropertyDef findParameter() {
            final AdsSubReport subReport = getOwnerSubReport();
            if (subReport != null) {
                final AdsReportClassDef report = subReport.findReport();
                if (report != null) {
                    final AdsPropertyDef prop = report.getProperties().getLocal().findById(parameterId);
                    if (prop instanceof AdsParameterPropertyDef) {
                        return (AdsParameterPropertyDef) prop;
                    }
                }
            }
            return null;
        }

        public AdsParameterPropertyDef getParameter() {
            final AdsParameterPropertyDef parameter = findParameter();
            if (parameter != null) {
                return parameter;
            } else {
                throw new DefinitionNotFoundError(parameterId);
            }
        }

        public AdsSubReport getOwnerSubReport() {
            for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
                if (container instanceof AdsSubReport) {
                    return (AdsSubReport) container;
                }
            }
            return null;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            Definition def = findParameter();
            if (def != null) {
                list.add(def);
            }
            def = findProperty();
            if (def != null) {
                list.add(def);
            }
        }
    }

    private static class Associations extends RadixObjects<Association> {

        public Associations(AdsSubReport ownerSubReport) {
            super(ownerSubReport);
        }
    }

    public static class Factory {

        private Factory() {
        }

        public static AdsSubReport newInstance() {
            return new AdsSubReport();
        }

        public static AdsSubReport loadFrom(org.radixware.schemas.adsdef.SubReport xSubReport) {
            return new AdsSubReport(xSubReport);
        }
    }

    /**
     * Creates a new AdsSubReport.
     */
    protected AdsSubReport() {
    }

    protected AdsSubReport(final org.radixware.schemas.adsdef.SubReport xSubReport) {
        this.reportId = xSubReport.getReportId();

        final List<org.radixware.schemas.adsdef.SubReport.Association> xAssociations = xSubReport.getAssociationList();
        if (xAssociations != null) {
            for (org.radixware.schemas.adsdef.SubReport.Association xAssociation : xAssociations) {
                final Association association = Association.Factory.loadFrom(xAssociation);
                associations.add(association);
            }
        }
    }

    public void appendTo(org.radixware.schemas.adsdef.SubReport xSubReport, ESaveMode saveMode) {
        xSubReport.setReportId(this.reportId);
        if (!associations.isEmpty()) {
            for (Association association : associations) {
                association.appendTo(xSubReport.addNewAssociation(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        final AdsReportBand ownerBand = getOwnerBand();
        final String prefix;
        if (ownerBand != null) {
            prefix = (ownerBand.getPreReports() == getContainer() ? "Pre" : "Post");
        } else {
            prefix = "Sub";
        }
        final AdsReportClassDef report = findReport();
        final String reportName = (report != null ? report.getName() : String.valueOf(reportId));
        return prefix + " Report " + reportName;
    }

    public Id getReportId() {
        return reportId;
    }

    public void setReportId(Id reportId) {
        if (!Utils.equals(this.reportId, reportId)) {
            this.reportId = reportId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public RadixObjects<Association> getAssociations() {
        return associations;
    }

    public AdsReportClassDef getOwnerReport() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportClassDef) {
                return (AdsReportClassDef) container;
            }
        }
        return null;
    }

    public AdsReportBand getOwnerBand() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsReportBand) {
                return (AdsReportBand) container;
            }
        }
        return null;
    }

    public AdsReportClassDef findReport() {
        if (reportId != null) {
            final AdsReportClassDef ownerReport = getOwnerReport();
            if (ownerReport != null) {
                final DefinitionSearcher<AdsClassDef> searcher = AdsSearcher.Factory.newAdsClassSearcher(ownerReport);
                final AdsClassDef clazz = searcher.findById(reportId).get();
                if (clazz instanceof AdsReportClassDef) {
                    return (AdsReportClassDef) clazz;
                }
            }
        }
        return null;
    }

    public AdsReportClassDef getReport() {
        final AdsReportClassDef report = findReport();
        if (report != null) {
            return report;
        } else {
            throw new DefinitionNotFoundError(reportId);
        }
    }

    @Override
    public String getTypeTitle() {
        return "Subreport";
    }

    @Override
    public String getTypesTitle() {
        return "Subreports";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_SUB_REPORT;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        associations.visit(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsReportClassDef report = this.findReport();
        if (report != null) {
            list.add(report);
        }
    }
}
