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

import java.util.List;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.repository.Layer;

import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Metainformation about org.radixware.schemas.product.Scripts.Script Contains
 * information about upgrade script environment on the time of script
 * generation: last update file name and release number of base layers.
 *
 */
public class DdsUpdateInfo extends RadixObject {

    /**
     * Metainformation about
     * org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer Contains
     * information about base layer on the time of update script generation:
     * last update file name and release number.
     *
     */
    public static class BaseLayerInfo extends RadixObject {

        private String uri;
        private String releaseNumber;
        private String lastUpdateFileName;

        protected BaseLayerInfo() {
        }

        protected void loadFrom(org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer xBaseLayer) {
            this.uri = xBaseLayer.getUri();
            this.releaseNumber = xBaseLayer.getReleaseNumber();
            this.lastUpdateFileName = xBaseLayer.getLastScriptFileName();
        }

        protected void appendTo(org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer xLayer) {
            xLayer.setUri(uri);
            if (releaseNumber != null && !releaseNumber.isEmpty()) {
                xLayer.setReleaseNumber(releaseNumber);
            }
            if (lastUpdateFileName != null && !lastUpdateFileName.isEmpty()) {
                xLayer.setLastScriptFileName(lastUpdateFileName);
            }
        }

        public DdsUpdateInfo getOwnerUpdateInfo() {
            for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
                if (container instanceof DdsUpdateInfo) {
                    return (DdsUpdateInfo) container;
                }
            }
            return null;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            if (!Utils.equals(this.uri, uri)) {
                this.uri = uri;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return last update file name or null or empty string if not defined
         */
        public String getLastUpdateFileName() {
            return lastUpdateFileName;
        }

        public void setLastUpdateFileName(String lastUpdateFileName) {
            if (!Utils.equals(this.lastUpdateFileName, lastUpdateFileName)) {
                this.lastUpdateFileName = lastUpdateFileName;
                setEditState(EEditState.MODIFIED);
            }
        }

        /**
         * @return previous release number or null or empty string if not
         * defined
         */
        public String getReleaseNumber() {
            return releaseNumber;
        }

        public void setReleaseNumber(String releaseNumber) {
            if (!Utils.equals(this.releaseNumber, releaseNumber)) {
                this.releaseNumber = releaseNumber;
                setEditState(EEditState.MODIFIED);
            }
        }

        public Layer findBaseLayer() {
            final DdsUpdateInfo updateInfo = getOwnerUpdateInfo();
            if (updateInfo == null) {
                return null;
            }
            final DdsScripts scripts = updateInfo.getOwnerScripts();
            if (scripts == null) {
                return null;
            }
            final DdsSegment segment = scripts.getOwnerSegment();
            if (segment == null) {
                return null;
            }
            final Layer layer = segment.getLayer();
            if (layer == null) {
                return null;
            }
            return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Layer>() {
                @Override
                public void accept(HierarchyWalker.Controller<Layer> controller, Layer layer) {
                    if (Utils.equals(BaseLayerInfo.this.uri, layer.getURI())) {
                        controller.setResultAndStop(layer);
                    }
                }
            });
        }

        public DdsScript findLastUpdateScript() {
            if (lastUpdateFileName == null || lastUpdateFileName.isEmpty()) {
                return null;
            }

            final Layer baseLayer = findBaseLayer();
            if (baseLayer == null) {
                return null;

            }
            final DdsSegment baseSegment = (DdsSegment) baseLayer.getDds();
            final DdsScriptsDir upgrades = baseSegment.getScripts().getDbScripts().getUpgradeScripts();
            return upgrades.findScriptByFileBaseName(lastUpdateFileName);
        }

        @Override
        public RadixIcon getIcon() {
            return RadixObjectIcon.LAYER;
        }
    }

    private static class BaseLayersInfo extends RadixObjects<BaseLayerInfo> {

        protected BaseLayersInfo(DdsUpdateInfo ownerUpdateInfo) {
            super(ownerUpdateInfo);
        }
    }
    private String updateFileName;
    private boolean backwardCompatible = false;
    private boolean reverse = false;
    private String straightFileName;
    private RadixObjects<BaseLayerInfo> baseLayersInfo = new BaseLayersInfo(this);
    private final DdsAadcTransform ddsAadcTransform = new DdsAadcTransform(this);

    protected DdsUpdateInfo() {
        super();
    }

    public RadixObjects<BaseLayerInfo> getBaseLayersInfo() {
        return baseLayersInfo;
    }

    protected void loadFrom(org.radixware.schemas.product.Scripts.Script xScript) {
        this.updateFileName = xScript.getFileName();
        this.backwardCompatible = xScript.isSetIsBackwardCompatible() && xScript.getIsBackwardCompatible();
        this.baseLayersInfo.clear();
        this.reverse = xScript.isSetIsReverse() && xScript.getIsReverse();
        this.straightFileName = xScript.isSetStraightFileName() ? xScript.getStraightFileName() : null;
        if (xScript.isSetBaseLayers()) {
            final List<org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer> xBaseLayers = xScript.getBaseLayers().getLayerList();
            if (xBaseLayers != null) {
                for (org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer xBaseLayer : xBaseLayers) {
                    final BaseLayerInfo layerInfo = new BaseLayerInfo();
                    layerInfo.loadFrom(xBaseLayer);
                    this.baseLayersInfo.add(layerInfo);
                }
            }
        }
        if (xScript.isSetAadcTransform()) {
            ddsAadcTransform.loadFrom(xScript.getAadcTransform());
        }
    }

    protected void appendTo(org.radixware.schemas.product.Scripts.Script xScript) {
        xScript.setFileName(updateFileName);
        if (backwardCompatible) {
            xScript.setIsBackwardCompatible(backwardCompatible);
        }
        if (reverse) {
            xScript.setIsReverse(reverse);
            if (straightFileName != null && !straightFileName.isEmpty()) {
                xScript.setStraightFileName(straightFileName);
            }
        }

        if (!baseLayersInfo.isEmpty()) {
            final org.radixware.schemas.product.Scripts.Script.BaseLayers xBaseLayers = xScript.addNewBaseLayers();
            for (BaseLayerInfo baseLayerInfo : baseLayersInfo) {
                org.radixware.schemas.product.Scripts.Script.BaseLayers.Layer xLayer = xBaseLayers.addNewLayer();
                baseLayerInfo.appendTo(xLayer);
            }
        }
        
        ddsAadcTransform.appendTo(xScript.addNewAadcTransform());
        
    }

    public String getUpdateFileName() {
        return updateFileName;
    }

    public void setUpdateFileName(String updateFileName) {
        if (!Utils.equals(this.updateFileName, updateFileName)) {
            this.updateFileName = updateFileName;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isReverse() {
        return reverse;
    }

    public String getStraightUpdateFileName() {
        if (isReverse()) {
            return straightFileName;
        } else {
            return null;
        }
    }

    public void setStraightUpdateFileName(String fileName) {
        if (!Utils.equals(straightFileName, fileName)) {
            straightFileName = fileName;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setReverse(boolean reverse) {
        if (this.reverse != reverse) {
            this.reverse = reverse;
            setEditState(EEditState.MODIFIED);
        }
    }

    public boolean isBackwardCompatible() {
        return backwardCompatible;
    }

    public void setBackwardCompatible(boolean isBackwardCompatible) {
        if (this.backwardCompatible != isBackwardCompatible) {
            this.backwardCompatible = isBackwardCompatible;
            setEditState(EEditState.MODIFIED);
        }
    }

    public DdsScripts getOwnerScripts() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof DdsScripts) {
                return (DdsScripts) container;
            }
        }
        return null;
    }

    public DdsScript findScript() {
        final DdsScripts ownerScripts = getOwnerScripts();
        if (ownerScripts == null) {
            return null;
        }
        final DdsScriptsDir upgrades = ownerScripts.getDbScripts().getUpgradeScripts();
        return upgrades.findScriptByFileBaseName(updateFileName);
    }

    @Override
    public String getName() {
        if (updateFileName != null) {
            return updateFileName;
        }
        return "UpdateInfo";
    }

    @Override
    public RadixIcon getIcon() {
        if (isReverse()) {
            return DdsDefinitionIcon.SQL_REVERSE_SCRIPT;
        }
        return DdsDefinitionIcon.SQL_SCRIPT;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        ddsAadcTransform.visit(visitor, provider);
    }

    public DdsAadcTransform getDdsAadcTransform() {
        return ddsAadcTransform;
    }
}
