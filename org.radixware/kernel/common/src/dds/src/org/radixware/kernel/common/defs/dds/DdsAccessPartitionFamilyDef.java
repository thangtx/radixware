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

package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Access partition family - координатная ось в пространстве контролируемых
 * объектов. See rx_access_control.doc.
 *
 */
public class DdsAccessPartitionFamilyDef extends DdsDefinition {

    private Id headId = null;
    private Id parentFamilyReferenceId = null;

    private class CycleApf {

        CycleApf(DdsAccessPartitionFamilyDef apf, boolean x) {
            this.apf = apf;
            this.x = x;
        }
        DdsAccessPartitionFamilyDef apf;
        boolean x;
    }

    private boolean checkRecurrence(DdsAccessPartitionFamilyDef lastApf,
            DdsAccessPartitionFamilyDef currApf,
            HashMap<Id, CycleApf> map) {
        DdsReferenceDef ref = currApf.getParentFamilyReference();
        if (lastApf.equals(currApf)) {
            return true;
        }
        if (ref != null) {
            Id id = ref.getParentTableId();
            if (id != null) {
                CycleApf item = map.get(id);
                if (item != null) {
                    if (item.x) {
                        return true;
                    }
                    if (lastApf.equals(item.apf) || checkRecurrence(lastApf, item.apf, map)) {
                        return item.x = true;
                    }
                }
            }
        }
        return false;
    }

    public List<Id> getCorrectParentTableIds(Definition contextAsDefinition) {
        List<Id> parentIds = new ArrayList<Id>();

        //Get all APF - tables
        HashMap<Id, CycleApf> list = new HashMap<Id, CycleApf>();


        Map<Id, Dependence> modileId2Dependence = new HashMap<Id, Dependence>();
        contextAsDefinition.getDependenceProvider().collect(modileId2Dependence);
        List<DdsModule> ddsModules = new ArrayList<DdsModule>();

        for (Dependence dep : modileId2Dependence.values()) {
            List<Module> ms = dep.findDependenceModule(contextAsDefinition);
            if (ms != null) {
                for (Module m : ms) {
                    if (m != null && m instanceof DdsModule && ddsModules.indexOf(m) < 0) {
                        ddsModules.add((DdsModule) m);
                    }
                }
            }
        }
        Module m = contextAsDefinition.getModule();
        if (m != null && m instanceof DdsModule && ddsModules.indexOf(m) < 0) {
            ddsModules.add((DdsModule) m);
        }

        for (DdsModule module : ddsModules) {
            DdsModelDef model = module.getModelManager().findModel();
            if (model != null) {
                for (DdsAccessPartitionFamilyDef ap : model.getAccessPartitionFamilies()) {
                    Definition h = ap.findHead();
                    if (h != null && h instanceof DdsTableDef) {
                        list.put(h.getId(), new CycleApf(ap, ap.equals(this)));
                    }
                }
            }
        }
        for (CycleApf item : list.values()) {
            item.x = checkRecurrence(this, item.apf, list);
        }

        for (CycleApf item : list.values()) {
            if (!item.x) {
                parentIds.add(item.apf.getHeadId());
            }
        }
        return parentIds;
    }

    /**
     * Получить идентификатор таблицы набора констант, которым предоставляется
     * семейство партиции доступа.
     */
    public Id getHeadId() {
        return headId;
    }

    public void setHeadId(Id headId) {
        if (!Utils.equals(this.headId, headId)) {
            this.headId = headId;
            this.setEditState(EEditState.MODIFIED);
            this.setName(""); // fire name changed event
        }
    }

    /**
     * Получить идентификатор связи от текущего раздела партиции доступа до
     * родительского.
     *
     * @param parentFamilyReferenceId
     */
    public Id getParentFamilyReferenceId() {
        return parentFamilyReferenceId;
    }

    public void setParentFamilyReferenceId(Id parentFamilyReferenceId) {
        if (!Utils.equals(this.parentFamilyReferenceId, parentFamilyReferenceId)) {
            this.parentFamilyReferenceId = parentFamilyReferenceId;
            this.setEditState(EEditState.MODIFIED);
        }
    }

    protected DdsAccessPartitionFamilyDef() {
        super(EDefinitionIdPrefix.DDS_ACCESS_PARTITION_FAMILY, "");
    }

    protected DdsAccessPartitionFamilyDef(org.radixware.schemas.ddsdef.AccessPartitionFamily xApf) {
        super(xApf);
        this.headId = Id.Factory.loadFrom(xApf.getHeadId());
        if (xApf.isSetParentFamilyReferenceId()) {
            this.parentFamilyReferenceId = Id.Factory.loadFrom(xApf.getParentFamilyReferenceId());
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        Definition head = findHead();
        if (head != null) {
            return head.getName();
        } else if (headId != null) {
            return String.valueOf(headId);
        } else {
            return APF_TYPE_TITLE;
        }
    }

    public static class Factory {

        private Factory() {
        }

        public static DdsAccessPartitionFamilyDef newInstance() {
            return new DdsAccessPartitionFamilyDef();
        }

        /**
         * Do not use this function instead of APF creation wizard. This
         * function allows to simplify wizard.
         */
        public static DdsAccessPartitionFamilyDef newInstance(Definitions<DdsAccessPartitionFamilyDef> context) {
            DdsAccessPartitionFamilyDef apf = newInstance();
            apf.setContainer(context);
            return apf;
        }

        public static DdsAccessPartitionFamilyDef loadFrom(org.radixware.schemas.ddsdef.AccessPartitionFamily xApf) {
            return new DdsAccessPartitionFamilyDef(xApf);
        }
    }

    /**
     * Find head.
     *
     * @return head or null it not found.
     */
    public Definition findHead() {
        Id thisHeadId = getHeadId();
        if (thisHeadId != null) {
            DdsModule ownerModule = getModule();
            if (ownerModule != null) {
                if (thisHeadId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
                    return ownerModule.getDdsTableSearcher().findById(thisHeadId).get();
                } else {
                    return (Definition) ownerModule.findEnumById(thisHeadId);
                }
            }
        }

        return null;
    }

    /**
     * Find head.
     *
     * @throws DefinitionNotFoundError
     */
    public Definition getHead() {
        Definition head = findHead();
        if (head != null) {
            return head;
        }
        throw new DefinitionNotFoundError(getHeadId());
    }

    /**
     * Find head.
     *
     * @return head or null it not found.
     */
    public DdsReferenceDef findParentFamilyReference() {
        Id thisParentFamilyReferenceId = getParentFamilyReferenceId();
        if (thisParentFamilyReferenceId != null) {
            DdsModule ownerModule = getModule();
            if (ownerModule != null) {
                return ownerModule.getDdsReferenceSearcher().findById(thisParentFamilyReferenceId).get();
            }
        }

        return null;
    }

    /**
     * Find parent family reference.
     *
     * @return parent family reference or null if
     * getParentFamilyReferenceId()==null.
     * @throws DefinitionNotFoundError if getParentFamilyReferenceId()!=null and
     * reference not found.
     */
    public DdsReferenceDef getParentFamilyReference() {
        Id thisParentFamilyReferenceId = getParentFamilyReferenceId();
        if (thisParentFamilyReferenceId != null) {
            DdsReferenceDef parentFamilyReference = findParentFamilyReference();
            if (parentFamilyReference != null) {
                return parentFamilyReference;
            }
            throw new DefinitionNotFoundError(thisParentFamilyReferenceId);
        } else {
            return null;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.ACCESS_PARTITION_FAMILY;
    }

    @Override
    public String toString() {
        return super.toString()
                + "; HeadId: " + String.valueOf(headId);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        Definition head = findHead();
        if (head != null) {
            list.add(head);
        }
        DdsReferenceDef reference = findParentFamilyReference();
        if (reference != null) {
            list.add(reference);
        }
    }

    private class DdsApfClipboardSupport extends DdsClipboardSupport<DdsAccessPartitionFamilyDef> {

        public DdsApfClipboardSupport() {
            super(DdsAccessPartitionFamilyDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.AccessPartitionFamily xApf = org.radixware.schemas.ddsdef.AccessPartitionFamily.Factory.newInstance();
            DdsModelWriter.writeApf(DdsAccessPartitionFamilyDef.this, xApf);
            return xApf;
        }

        @Override
        protected DdsAccessPartitionFamilyDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.AccessPartitionFamily xApf = (org.radixware.schemas.ddsdef.AccessPartitionFamily) xmlObject;
            return DdsAccessPartitionFamilyDef.Factory.loadFrom(xApf);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsAccessPartitionFamilyDef> getClipboardSupport() {
        return new DdsApfClipboardSupport();
    }
    public static final String APF_TYPE_TITLE = "Access Partition Family";

    @Override
    public String getTypeTitle() {
        return APF_TYPE_TITLE;
    }
    public static final String APF_TYPES_TITLE = "Access Partition Families";

    @Override
    public String getTypesTitle() {
        return APF_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);

        DdsReferenceDef reference = findParentFamilyReference();
        if (reference != null) {
            sb.append("<br>Reference: " + reference.getName());
        }
    }
}
