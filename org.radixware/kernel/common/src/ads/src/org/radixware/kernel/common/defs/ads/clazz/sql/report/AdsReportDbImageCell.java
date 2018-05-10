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
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EImageScaleType;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class AdsReportDbImageCell extends AdsReportCell {

    private Id dataPropertyId = null;
    private Id mimeTypePropertyId = null;
    private double adjustHeightMm;
    private double adjustWidthMm;
    private String mimeType;
    private EImageScaleType scaleType = EImageScaleType.CROP;
    
    //----------------RADIX-14249----------------
    private boolean isResizeImage = true;    
    //-------------------------------------------

    protected AdsReportDbImageCell() {
        super();
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.DB_IMAGE;
    }

    public EImageScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(EImageScaleType scaleType) {
        this.scaleType = scaleType;
        setEditState(EEditState.MODIFIED);
    }

    protected AdsReportDbImageCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);

        if (xCell.isSetImageDataPropId()) {
            dataPropertyId = xCell.getImageDataPropId();
        }
        if (xCell.isSetImageMimeTypePropId()) {
            mimeTypePropertyId = xCell.getImageMimeTypePropId();
        }
        if (xCell.isSetImageScaleType()) {
            scaleType = xCell.getImageScaleType();
        }
        if (xCell.isSetIsResizeImage()) {
            isResizeImage = xCell.getIsResizeImage();
        }
    }

    public double getAdjustHeightMm() {
        return adjustHeightMm;
    }

    public void setAdjustHeightMm(double adjustHeightMm) {
        this.adjustHeightMm = adjustHeightMm;
    }

    public double getAdjustWidthMm() {
        return adjustWidthMm;
    }

    public void setAdjustWidthMm(double adjustWidthMm) {
        this.adjustWidthMm = adjustWidthMm;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isResizeImage() {
        return isResizeImage;
    }

    public void setIsResizeImage(boolean isResizeImage) {
        if (this.isResizeImage != isResizeImage) {
            this.isResizeImage = isResizeImage;
            setEditState(EEditState.MODIFIED);
        }        
    }

    protected AdsReportDbImageCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);

        if (xCell.isSetImageDataPropId()) {
            dataPropertyId = xCell.getImageDataPropId();
        }
        if (xCell.isSetImageMimeTypePropId()) {
            mimeTypePropertyId = xCell.getImageMimeTypePropId();
        }
        if (xCell.isSetImageScaleType()) {
            scaleType = xCell.getImageScaleType();
        }
        if (xCell.isSetIsResizeImage()) {
            isResizeImage = xCell.getIsResizeImage();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);

        if (dataPropertyId != null) {
            xCell.setImageDataPropId(dataPropertyId);
        }

        if (mimeTypePropertyId != null) {
            xCell.setImageMimeTypePropId(mimeTypePropertyId);
        }
        if (scaleType != EImageScaleType.CROP) {
            xCell.setImageScaleType(scaleType);
        }
        xCell.setIsResizeImage(isResizeImage);
    }

    public Id getDataPropertyId() {
        return dataPropertyId;
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return mode == AdsReportForm.Mode.GRAPHICS;
    }

    public void setDataPropertyId(Id dataPropertyId) {
        if (!Utils.equals(this.dataPropertyId, dataPropertyId)) {
            this.dataPropertyId = dataPropertyId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getMimeTypePropertyId() {
        return mimeTypePropertyId;
    }

    public void setMimeTypePropertyId(Id mimeTypePropertyId) {
        if (!Utils.equals(this.mimeTypePropertyId, mimeTypePropertyId)) {
            this.mimeTypePropertyId = mimeTypePropertyId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return image data property or null if not found.
     */
    public AdsPropertyDef findDataProperty() {
        final AdsReportClassDef ownerReport = getOwnerReport();
        if (ownerReport != null) {
            return ownerReport.getProperties().findById(dataPropertyId, EScope.LOCAL).get();
        }
        return null;
    }

    /**
     * @return image data property.
     * @throws DefinitionNotFoundError if not found.
     */
    public AdsPropertyDef getDataProperty() {
        final AdsPropertyDef prop = findDataProperty();
        if (prop != null) {
            return prop;
        } else {
            throw new DefinitionNotFoundError(dataPropertyId);
        }
    }

    /**
     * @return image mime-type property or null if not found.
     */
    public AdsPropertyDef findMimeTypeProperty() {
        final AdsReportClassDef ownerReport = getOwnerReport();
        if (ownerReport != null) {
            return ownerReport.getProperties().findById(mimeTypePropertyId, EScope.LOCAL).get();
        }
        return null;
    }

    /**
     * @return image mime-type property.
     * @throws DefinitionNotFoundError if not found.
     */
    public AdsPropertyDef getMimeTypeProperty() {
        final AdsPropertyDef prop = findMimeTypeProperty();
        if (prop != null) {
            return prop;
        } else {
            throw new DefinitionNotFoundError(mimeTypePropertyId);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final AdsPropertyDef dataProperty = findDataProperty();
        if (dataProperty != null) {
            list.add(dataProperty);
        }

        final AdsPropertyDef mimeTypeProperty = findMimeTypeProperty();
        if (mimeTypeProperty != null) {
            list.add(mimeTypeProperty);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_DB_IMAGE_CELL;
    }
}
