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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.defs.ads.userfunc.IUserDefModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.Report;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;
import org.radixware.schemas.xscml.TypeDeclaration;

public class AdsUserReportClassDef extends AdsReportClassDef {

    private Id runtimeId = null;
    private AdsTypeDeclaration contextType;

    public AdsUserReportClassDef(String name, AdsEntityObjectClassDef contextType) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT), name);
        if (contextType != null) {
            this.contextType = AdsTypeDeclaration.Factory.newParentRef(contextType);
        }
    }

    public AdsTypeDeclaration getContextParameterType() {
        if (contextType != null) {
            return contextType;
        } else {
            AdsPropertyDef cp = findContextParameter();
            if (cp == null) {
                return null;
            } else {
                return cp.getValue().getType();
            }
        }
    }

    public AdsUserReportClassDef(ClassDefinition xDef) {
        super(xDef);
    }

    @Override
    public boolean isUserReport() {
        return true;
    }

    @Override
    public boolean isUserExtension() {
        return true;
    }

    @Override
    public String getRuntimeLocalClassName(boolean isHumanReadable) {
        if (isHumanReadable) {
            return getName();
        } else {
            return getRuntimeId().toString();
        }
    }

    public Id getRuntimeId() {
        synchronized (this) {
            if (runtimeId == null) {
                runtimeId = Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT);
            }
        }
        return runtimeId;
    }

    public boolean hasRuntimeId() {
        synchronized (this) {
            return runtimeId != null;
        }
    }

    public void resetRuntimeId(Id wellKnownId) {
        synchronized (this) {
            runtimeId = wellKnownId;
        }
    }

    public void resetRuntimeId() {
        resetRuntimeId(null);
    }

    public boolean isActualVersion() {
        return getId().toString().indexOf("_urv_") < 0;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }

    public List<AdsDefinition> getUsedWrappers() {
        /*Map<Id, AdsDefinition> usedWrappers = new HashMap<>();
         this.visitChildren(null, new VisitorProvider() {

         @Override
         public boolean isTarget(RadixObject radixObject) {
         return radixObject instanceof Jml;
         }
         });*/
        AdsModule ownerModule = getModule();
        if (ownerModule instanceof IUserDefModule) {
            ((IUserDefModule) ownerModule).resetKnownLibFuncs();
        }
        Map<Id, AdsDefinition> usedWrappers = new HashMap<>();
        for (AdsMethodDef m : this.getMethods().getLocal()) {
            if (m instanceof AdsUserMethodDef) {
                usedWrappers.putAll(getUsedWrappers(((AdsUserMethodDef) m).getSource()));
            }
        }
        for (AdsPropertyDef p : this.getProperties().getAll(ExtendableDefinitions.EScope.LOCAL)) {
            SearchResult<AdsPropertyDef.Getter> srGetter = p.findGetter(ExtendableDefinitions.EScope.LOCAL);
            if (srGetter != null && srGetter.get() != null) {
                AdsPropertyDef.Getter getter = srGetter.get();
                getter.getSources().getDefinedSourceTypes();
                for (ERuntimeEnvironmentType env : getter.getSources().getDefinedSourceTypes()) {
                    usedWrappers.putAll(getUsedWrappers(getter.getSources().getSource(env)));
                }
            }
            SearchResult<AdsPropertyDef.Setter> srSetter = p.findSetter(ExtendableDefinitions.EScope.LOCAL);
            if (srSetter != null && srSetter.get() != null) {
                AdsPropertyDef.Setter setter = srSetter.get();
                setter.getSources().getDefinedSourceTypes();
                for (ERuntimeEnvironmentType env : setter.getSources().getDefinedSourceTypes()) {
                    usedWrappers.putAll(getUsedWrappers(setter.getSources().getSource(env)));
                }
            }
        }
        usedWrappers.putAll(getUsedWrappers(this.getCsvInfo().getRowCondition()));
        usedWrappers.putAll(getReportFormJmlWrappers());
        return new ArrayList<>(usedWrappers.values());
    }

    private Map<Id, AdsDefinition> getReportFormJmlWrappers() {
        Map<Id, AdsDefinition> usedWrappers = new HashMap<>();
        getReportBandWrappers(this.getForm().getTitleBands(), usedWrappers);
        getReportBandWrappers(this.getForm().getPageHeaderBands(), usedWrappers);
        getReportBandWrappers(this.getForm().getPageFooterBands(), usedWrappers);
        getReportBandWrappers(this.getForm().getColumnHeaderBands(), usedWrappers);
        getReportBandWrappers(this.getForm().getDetailBands(), usedWrappers);
        getReportBandWrappers(this.getForm().getSummaryBands(), usedWrappers);
        for (AdsReportGroup group : this.getForm().getGroups()) {
            getReportBandWrappers(group.getHeaderBand(), usedWrappers);
            getReportBandWrappers(group.getFooterBand(), usedWrappers);
            usedWrappers.putAll(getUsedWrappers(group.getCondition()));
        }
        return usedWrappers;
    }

    private void getReportBandWrappers(Iterable<? extends IReportWidgetContainer> containers, Map<Id, AdsDefinition> usedWrappers) {
        if (containers != null) {
            for (IReportWidgetContainer container : containers) {
                getReportBandWrappers(container, usedWrappers);                
            }
        }
    }

    private void getReportBandWrappers(IReportWidgetContainer container, Map<Id, AdsDefinition> usedWrappers) {
        if (container != null) {
            if (container instanceof AdsReportBand) {
                usedWrappers.putAll(getUsedWrappers(((AdsReportBand) container).getOnAdding()));
            }
            for (AdsReportWidget w : container.getWidgets()) {
                if (w instanceof AdsReportCell) {
                    usedWrappers.putAll(getUsedWrappers(((AdsReportCell) w).getOnAdding()));
                    if (w instanceof AdsReportExpressionCell) {
                        usedWrappers.putAll(getUsedWrappers(((AdsReportExpressionCell) w).getExpression()));
                    }
                } else if (w instanceof IReportWidgetContainer) {
                    getReportBandWrappers((IReportWidgetContainer) w, usedWrappers);
                }
            }
        }
    }

    private Map<Id, AdsDefinition> getUsedWrappers(Jml source) {
        Map<Id, AdsDefinition> usedWrappers = new HashMap<>();
        if (source != null) {
            for (Scml.Item item : source.getItems()) {
                AdsPath path = null;
                if (item instanceof JmlTagTypeDeclaration) {
                    ((JmlTagTypeDeclaration) item).getType().resolve(this);
                    path = ((JmlTagTypeDeclaration) item).getType().getPath();
                } else if (item instanceof JmlTagId) {
                    ((JmlTagId) item).resolve(this);
                    path = ((JmlTagId) item).getPath();
                }
                if (path != null) {
                    Id[] ids = path.asArray();
                    if (ids.length == 1) {
                        final Id id = ids[0];

                        AdsModule ownerModule = getModule();
                        if (ownerModule instanceof IUserDefModule) {

                            AdsDefinition clazz = ((IUserDefModule) ownerModule).findDefWrapper(id);
                            if (clazz != null) {
                                if (clazz instanceof AdsLibUserFuncWrapper) {
                                    AdsLibUserFuncWrapper libUf = (AdsLibUserFuncWrapper) clazz;
                                    //if (libUf.isEqualTo(getOwnerPid())) {
                                    //    libUf.updateProfile(findProfile());
                                    //}
                                }
                                usedWrappers.put(ids[0], clazz);
                            }
                        }
                    }
                }
            }
        }
        return usedWrappers;
    }

    public static final class Lookup {

        public static Collection<Definition> listTopLevelDefinitions(final AdsUserReportClassDef context, final Set<EDefType> defTypes) {
            // Layer top = context.getModule().getSegment().getLayer();
            final ArrayList<Definition> resultList = new ArrayList<>();
            if (context.getModule() instanceof IUserDefModule) {
                final ArrayList<DefInfo> infos = new ArrayList<>();
                if (((IUserDefModule) context.getModule()).getObserver() != null) {
                    ((IUserDefModule) context.getModule()).getObserver().addUserDefInfo(defTypes, infos);
                }
                for (DefInfo info : infos) {
                    AdsDefinition wrapper = ((IUserDefModule) context.getModule()).findDefWrapper(info.getPath()[0]);
                    if (!resultList.contains(wrapper)) {
                        resultList.add(wrapper);
                    }
                }
            }

            return resultList;
        }

        //public static AdsDefinition findTopLevelDefinition(Definition context, final Id id) {
        //    AdsDefinition wrapper = ((IUserDefModule) context.getModule()).findDefWrapper(id);
        //    return wrapper;          
        //}
    }

    public static Id buildReportVersion(Id reportId, long reportVersion) {
        return Id.Factory.loadFrom(reportId.toString() + "_urv_" + reportVersion);
    }
    
    public static UserReportDefinitionType getReportDefWithCorrectIds(UserReportDefinitionType xRepDef, Id reportId, long version, boolean isCurrentVersion) {
        if (xRepDef == null) {
            throw new NullPointerException("Report xml is null");
        }
        
        final Id oldRepId, newRepId;
        String repAsStr = xRepDef.xmlText();
        if (isCurrentVersion) {
            oldRepId = buildReportVersion(reportId, version);
            newRepId = reportId;
        } else {
            oldRepId = reportId;
            newRepId = buildReportVersion(reportId, version);
            
            //avoid case with replacing part of newRepId:
            //id="rpuXXX_urv_1" => replace(oldRepId, newRepId) => id="rpuXXX_urv_1_urv_1"
            repAsStr = repAsStr.replace(newRepId.toString(), oldRepId.toString()); 
        }
        final String newRepStr = repAsStr.replace(oldRepId.toString(), newRepId.toString());
        
        try {
            return UserReportDefinitionType.Factory.parse(newRepStr);
        } catch (XmlException ex) {
            throw new IllegalArgumentException("Unable to actualize report denifition ids, according current version", ex);
        }
    }

    public static boolean loadFromDbStorage(AdsUserReportDefinitionDocument xReportDoc, 
            Id reportId, long version, boolean isCurrentVersion, List<IRepositoryAdsDefinition> repositoryList) {
        if (xReportDoc != null && xReportDoc.getAdsUserReportDefinition() != null) {
            UserReportDefinitionType xReportWithCorrectIds = getReportDefWithCorrectIds(
                    xReportDoc.getAdsUserReportDefinition(),
                    reportId, version, isCurrentVersion);
            AdsDefinitionDocument xDefDoc = AdsDefinitionDocument.Factory.newInstance();
            xDefDoc.addNewAdsDefinition().setAdsClassDefinition(xReportWithCorrectIds.getReport());
            xDefDoc.getAdsDefinition().setFormatVersion(AdsSqlClassDef.FORMAT_VERSION);

            TypeDeclaration superType = TypeDeclaration.Factory.newInstance();
            superType.setTypeId(EValType.USER_CLASS);
            superType.setPath(Arrays.asList(AdsReportClassDef.PREDEFINED_ID));
            xDefDoc.getAdsDefinition().getAdsClassDefinition().setExtends(superType);

            AdsReportClassDef reportClass = AdsUserReportClassDef.Factory.
                    loadFromUser(xDefDoc.getAdsDefinition().getAdsClassDefinition());
            FSRepositoryAdsDefinition definitionRepository = new FSRepositoryAdsDefinition(reportClass, false);
            repositoryList.add(definitionRepository);

            if (xReportWithCorrectIds.getStrings() != null) {
                xDefDoc = AdsDefinitionDocument.Factory.newInstance();
                xDefDoc.addNewAdsDefinition().addNewAdsLocalizingBundleDefinition().
                        setId(xReportWithCorrectIds.getStrings().getId());
                xDefDoc.getAdsDefinition().setFormatVersion(0);
                xDefDoc.getAdsDefinition().getAdsLocalizingBundleDefinition().
                        assignStringList(xReportWithCorrectIds.getStrings().getStringList());
                AdsLocalizingBundleDef bundle = AdsLocalizingBundleDef.Factory.
                        loadFrom(xDefDoc.getAdsDefinition().getAdsLocalizingBundleDefinition());
                definitionRepository.setBundle(bundle);
            }
            return true;
        }
        return false;
    }
    
    public static void migrateChangeLogToVersionForCompatibility(UserReportExchangeType exportDoc, UserReportDefinitionType xVersion) {
        if (exportDoc == null || xVersion == null || xVersion.getReport() == null || xVersion.getReport().getReport() == null) {
            return;
        }
        if (exportDoc.isSetChangeLog() && !xVersion.getReport().getReport().isSetChangeLog()) {
            xVersion.getReport().getReport().setChangeLog(exportDoc.getChangeLog());
        }
    }
}
