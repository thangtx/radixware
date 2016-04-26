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

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Report property cell.
 */
public class AdsReportPropertyCell extends AdsReportFormattedCell implements ILocalizedDef {

    private Id propertyId = null;
    private Id nullTitleId = null;

    protected AdsReportPropertyCell() {
        super();
    }

    protected AdsReportPropertyCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);
       
        if (xCell.isSetPropId()) {
            this.propertyId = xCell.getPropId();
        }

        if (xCell.isSetNullTitleId()) {
            this.nullTitleId = xCell.getNullTitleId();
        }
    }
    
    protected AdsReportPropertyCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);
       
        if (xCell.isSetPropId()) {
            this.propertyId = xCell.getPropId();
        }

        if (xCell.isSetNullTitleId()) {
            this.nullTitleId = xCell.getNullTitleId();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);        

        if (propertyId != null) {
            xCell.setPropId(propertyId);
        }

        if (nullTitleId != null) {
            xCell.setNullTitleId(nullTitleId);
        }
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.PROPERTY;
    }

    @Override
    public String getName() {
        if (getOwnerReport() == null) {
            return super.getName();
        }
        final AdsPropertyDef property = findProperty();
        return (property != null ? property.getName() : String.valueOf(propertyId));
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

    public Id getNullTitleId() {
        return nullTitleId;
    }

    public void setNullTitleId(Id nullTitleId) {
        if (!Utils.equals(this.nullTitleId, nullTitleId)) {
            this.nullTitleId = nullTitleId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return property or null if not found.
     */
    public AdsPropertyDef findProperty() {
        final AdsReportClassDef ownerReport = getOwnerReport();
        if (ownerReport != null) {
            return ownerReport.getProperties().findById(propertyId, EScope.LOCAL).get();
        }
        return null;
    }

    /**
     * @return field.
     * @throws DefinitionNotFoundError if not found.
     */
    public AdsPropertyDef getProperty() {
        final AdsPropertyDef prop = findProperty();
        if (prop != null) {
            return prop;
        } else {
            throw new DefinitionNotFoundError(propertyId);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final AdsPropertyDef prop = findProperty();
        if (prop != null) {
            list.add(prop);
        }

        final AdsMultilingualStringDef nullTitle = findNullTitle();
        if (nullTitle != null) {
            list.add(nullTitle);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_PROPERTY_CELL;
    }

    public AdsMultilingualStringDef findNullTitle() {
        return findLocalizedString(nullTitleId);
    }

    /**
     * @return MLS if defined or null.
     * @throws DefinitionNotFoundError if defined and not found.
     */
    public AdsMultilingualStringDef getNullTitle() {
        if (nullTitleId == null) {
            return null;
        }

        final AdsMultilingualStringDef text = findNullTitle();
        if (text != null) {
            return text;
        } else {
            throw new DefinitionNotFoundError(nullTitleId);
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(AdsReportPropertyCell.this) {

            @Override
            public Id getId() {
                return nullTitleId;
            }

            @Override
            public void updateId(Id newId) {
                nullTitleId = newId;
                setEditState(EEditState.MODIFIED);
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PRIVATE;
            }

            @Override
            public String getContextDescription() {
                return "Report Cell Property";
            }

            @Override
            public boolean isPublished() {
                return false;
            }
        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (stringId != null) {
            final AdsReportClassDef report = getOwnerReport();
            if (report != null) {
                return report.findLocalizedString(stringId);
            }
        }
        return null;
    }
 }
