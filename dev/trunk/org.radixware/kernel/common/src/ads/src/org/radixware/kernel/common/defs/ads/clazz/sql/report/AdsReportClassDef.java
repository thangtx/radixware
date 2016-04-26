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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.UdsExportable;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.radixdoc.ReportClassRadixdoc;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsReportClassWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsReportWriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import static org.radixware.kernel.common.enums.EDefinitionIdPrefix.ADS_FORM_MODEL_CLASS;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EReportSpecialCellType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;
import org.radixware.schemas.radixdoc.Page;

/**
 * ADS Report Class Definition.
 *
 */
public class AdsReportClassDef extends AdsSqlClassDef implements IAdsFormPresentableClass, UdsExportable {

    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.Report";
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcReport____________________");
    private static final Id reportStreamParamId = Id.Factory.loadFrom("mprStream____________________");
    private static final Id reportFormatParamId = Id.Factory.loadFrom("mprFormat____________________");
    private static final String ADS_REPORT_TYPE_TITLE = "Report Class";
    private static final String ADS_REPORT_TYPES_TITLE = "Report Classes";
    private final transient AdsReportForm form;
    private final transient AbstractFormPresentations presentations;
    private Id contextParameterId = null;
    private boolean subreport = false;
    private final transient AdsCsvReportInfo csvReportInfo;
    private ContextParameterTypeRestriction contextParamTypeRestriction;

    public static class AdsReportSysMethod extends AdsSystemMethodDef {

        public AdsReportSysMethod(AbstractMethodDefinition xMethod) {
            super(xMethod);
        }

        public AdsReportSysMethod(Id id, String name) {
            super(id, name);
        }

        @Override
        public boolean canChangePublishing() {
            return false;
        }
    }

    public interface ContextParameterTypeRestriction {

        public boolean isSuitableTypeForParameter(AdsTypeDeclaration decl);
    }

    public ContextParameterTypeRestriction getContextParamTypeRestriction() {
        return contextParamTypeRestriction;
    }

    public void setContextParamTypeRestriction(ContextParameterTypeRestriction contextParamTypeRestriction) {
        this.contextParamTypeRestriction = contextParamTypeRestriction;
    }

    public boolean walkTwiceOnExport() {
        return this.form.find(new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsReportSpecialCell) {
                    final AdsReportSpecialCell cell = (AdsReportSpecialCell) radixObject;
                    if (cell.getSpecialType() == EReportSpecialCellType.FILE_PAGE_COUNT || cell.getSpecialType() == EReportSpecialCellType.TOTAL_PAGE_COUNT || cell.getSpecialType() == EReportSpecialCellType.FILE_COUNT) {
                        return true;
                    }
                }
                return false;
            }
        }) != null;
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        private static void addMethods(AdsReportClassDef report) {
            addMethod(report, AdsSystemMethodDef.ID_REPORT_EXECUTE);
            addMethod(report, AdsSystemMethodDef.ID_REPORT_OPEN);
        }

        private static void addMethod(AdsReportClassDef report, Id id) {
            AdsSystemMethodDef open = (AdsSystemMethodDef) report.getMethods().getLocal().findById(id);
            if (open == null) {
                open = id.equals(AdsSystemMethodDef.ID_REPORT_OPEN) ? AdsReportSysMethod.Factory.newOpenReport() : AdsReportSysMethod.Factory.newExecuteReport();
                addParams(report.getInputParameters(), open.getProfile(), id);
                report.getMethods().getLocal().add(open);
            } else {
                addParams(report.getInputParameters(), open.getProfile(), id);
            }
            open.setPublished(true);
            //open.canChangePublishing();
        }

        public static AdsReportClassDef loadFrom(final org.radixware.schemas.adsdef.ClassDefinition xDef) {
            return loadFrom(xDef, false);
        }

        public static AdsReportClassDef loadFrom(final org.radixware.schemas.adsdef.ClassDefinition xDef, boolean fromAPI) {
            if (xDef.getId() != null && xDef.getId().getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT && BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null) {
                return loadFromUser(xDef);
            }
            final AdsReportClassDef report = new AdsReportClassDef(xDef);
            if (!fromAPI) {
                addMethods(report);
            }
            return report;
        }

        public static AdsReportClassDef loadFromUser(final org.radixware.schemas.adsdef.ClassDefinition xDef) {
            final AdsReportClassDef report = new AdsUserReportClassDef(xDef);
            addMethods(report);
            return report;
        }

        public static AdsReportClassDef newInstance() {
            final AdsReportClassDef report = new AdsReportClassDef("NewReport");
            addMethods(report);
            return report;
        }

        public static AdsReportClassDef newUserInstance() {
            final AdsReportClassDef report = new AdsUserReportClassDef("NewReport", null);
            addMethods(report);
            return report;
        }

        public static AdsReportClassDef newUserInstance(AdsEntityClassDef contextType) {
            final AdsReportClassDef report = new AdsUserReportClassDef("NewReport", contextType);
            addMethods(report);
            return report;
        }
    }

    @Override
    public void updateMethodsParams() {
        super.updateMethodsParams();
        if (getMethods() != null) {
            AdsMethodDef openMethod = getMethods().getLocal().findById(AdsSystemMethodDef.ID_REPORT_OPEN);
            if (openMethod != null) {
                addParams(getInputParameters(), openMethod.getProfile(), AdsSystemMethodDef.ID_REPORT_OPEN);
            }
            AdsMethodDef executeMethod = getMethods().getLocal().findById(AdsSystemMethodDef.ID_REPORT_EXECUTE);
            if (executeMethod != null) {
                addParams(getInputParameters(), executeMethod.getProfile(), AdsSystemMethodDef.ID_REPORT_EXECUTE);
            }
        }
    }

    private static void addParams(final List<AdsParameterPropertyDef> params, final AdsMethodDef.Profile profile, Id id) {
        profile.getParametersList().clear();
        if (id.equals(AdsSystemMethodDef.ID_REPORT_EXECUTE)) {
            final AdsTypeDeclaration outputStreamType = AdsTypeDeclaration.Factory.newPlatformClass("java.io.OutputStream");
            MethodParameter paramOutStream = MethodParameter.Factory.newInstance(reportStreamParamId, AdsSystemMethodDef.REPORT_STREAM_VAR_NAME, outputStreamType);
            profile.getParametersList().add(paramOutStream);

            final AdsTypeDeclaration formatType = AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EReportExportFormat");
            MethodParameter paramFormat = MethodParameter.Factory.newInstance(reportFormatParamId, AdsSystemMethodDef.REPORT_FORMAT_VAR_NAME, formatType);
            profile.getParametersList().add(paramFormat);
        }
        addParams(params, profile);
    }

    private static void addParams(final List<AdsParameterPropertyDef> params, final AdsMethodDef.Profile profile) {
        for (AdsParameterPropertyDef param : params) {
            final AdsTypeDeclaration type = param.getValue().getType();
            MethodParameter mp = MethodParameter.Factory.newInstance(param.getId(), param.getName(), type);
            profile.getParametersList().add(mp);
        }
    }

    protected AdsReportClassDef(final org.radixware.schemas.adsdef.ClassDefinition xDef) {
        super(xDef);
        if (xDef != null) {
            final org.radixware.schemas.adsdef.Report xReport = xDef.getReport();
            if (xReport != null) {
                final org.radixware.schemas.adsdef.Report.Form xForm = xReport.getForm();
                if (xForm != null) {
                    this.form = new AdsReportForm(this, xForm);
                } else {
                    this.form = new AdsReportForm(this);
                }
                if (xReport.isSetContextParameterId()) {
                    this.contextParameterId = xReport.getContextParameterId();
                }
                if (xReport.isSetIsSubreport()) {
                    this.subreport = xReport.getIsSubreport();
                }
                //!{
                if (xReport.isSetExportReportToCsvInfo()) {
                    csvReportInfo = new AdsCsvReportInfo(this, xReport.getExportReportToCsvInfo());
                } else {
                    csvReportInfo = new AdsCsvReportInfo(this, null);
                }
                //!}
            } else {
                this.form = new AdsReportForm(this);
                csvReportInfo = new AdsCsvReportInfo(this, null);
            }
            if (xDef.getPresentations() == null) {
                this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.newInstance(this);
            } else {
                this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.loadFrom(this, xDef.getPresentations());
            }
        } else {
            this.form = new AdsReportForm(this);
            this.csvReportInfo = new AdsCsvReportInfo(this, null);
            this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.newInstance(this);
        }
    }

    protected AdsReportClassDef(final Id id, final String name) {
        super(id, name);
        this.form = new AdsReportForm(this);
        this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.newInstance(this);
        this.csvReportInfo = new AdsCsvReportInfo(this, null);
    }

    protected AdsReportClassDef(final String name) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.REPORT), name);
    }

    @Override
    public void appendTo(final ClassDefinition xClassDef, final ESaveMode saveMode) {
        super.appendTo(xClassDef, saveMode);

        final org.radixware.schemas.adsdef.Report xReport = xClassDef.addNewReport();
        if (saveMode == ESaveMode.NORMAL) {
            form.appendTo(xReport.addNewForm(), saveMode);
        }
        if (this.contextParameterId != null) {
            xReport.setContextParameterId(contextParameterId);
        }
        if (this.subreport) {
            xReport.setIsSubreport(true);
        }
        //!{
        if (csvReportInfo.isExportToCsvEnabled()) {
            csvReportInfo.appendTo(xReport.addNewExportReportToCsvInfo(), saveMode);
        }
        //!}
        this.presentations.appendTo(xClassDef.addNewPresentations(), saveMode);
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (getTitleId() != null) {
            sb.append("<br>Title:");
            final AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
            if (bundle != null) {
                for (EIsoLanguage lan : bundle.getLanguages()) {
                    sb.append("<br>").append("Title (").append(lan.toString()).append("): ").append(getTitle(lan));
                }
            }
        }
    }

    //!{ 
    public AdsCsvReportInfo getCsvInfo() {
        return csvReportInfo;
    }
    /*
     * public RadixObjects<AdsExportCsvColumn> getExportCsvColumn(){ return
     * csvReportInfo.getExportCsvColumn(); }
     *
     * public boolean isExportToCsvEnabled(){ return
     * csvReportInfo.isExportToCsvEnabled(); }
     *
     * public void setIsExportToCsvEnabled(boolean isExportToCsv) { if
     * (csvReportInfo.isExportToCsvEnabled() != isExportToCsv) {
     * csvReportInfo.setIsExportToCsvEnabled(isExportToCsv);
     * setEditState(EEditState.MODIFIED); } }
     *
     * public Jml getCsvRowVisibleCondition() { return
     * csvReportInfo.getCsvRowVisibleCondition(); }
     */
    //!}

    public AdsReportForm getForm() {
        return form;
    }

    public Long getSubType() {
        return EClassType.REPORT.getValue();
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.REPORT;
    }

    @Override
    public String getTypeTitle() {
        return ADS_REPORT_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ADS_REPORT_TYPES_TITLE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_REPORT;
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        csvReportInfo.visit(visitor, provider);
        form.visit(visitor, provider);        
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ClassJavaSourceSupport() {
            @Override
            @SuppressWarnings("unchecked")
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return new AdsReportClassWriter(this, AdsReportClassDef.this, purpose);
            }
        };
    }

    @Override
    public AbstractFormPresentations getPresentations() {
        return (AbstractFormPresentations) presentations;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(final Id id) {
        try {
            final EDefinitionIdPrefix prefix = id.getPrefix();
            if (prefix == null) {
                return SearchResult.empty();
            }
            switch (prefix) {
                case EDITOR_PAGE:
                    return getPresentations().getEditorPages().findById(id, EScope.ALL);
                case ADS_FORM_MODEL_CLASS:
                case ADS_REPORT_MODEL_CLASS:
                    AdsModelClassDef model = getPresentations().getModel();
                    return model == null ? SearchResult.<AdsModelClassDef>empty() : SearchResult.single(model);
                default:
                    return super.findComponentDefinition(id);
            }
        } catch (NoConstItemWithSuchValueError e) {
            return super.findComponentDefinition(id);
        }
    }

    @Override
    public Jml getSource(final String name) {
        if (name == null) {
            return super.getSource(name);
        }
        final Jml result = AdsReportWriterUtils.findJmlByMarker(this.getForm(), csvReportInfo, name);
        if (result != null) {
            return result;
        } else {
            return super.getSource(name);
        }
    }

    /**
     * @return identifier of context parameter or null if not defined.
     */
    public Id getContextParameterId() {
        return contextParameterId;
    }

    public void setContextParameterId(Id contextParameterClassId) {
        if (!Utils.equals(this.contextParameterId, contextParameterClassId)) {
            this.contextParameterId = contextParameterClassId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * @return context parameter or null if not defined or not found.
     */
    public AdsPropertyDef findContextParameter() {
        if (contextParameterId == null) {
            return null;
        }
        return this.getProperties().findById(contextParameterId, EScope.ALL).get();
    }

    /**
     * @return context parameter or null if not defined.
     * @throws DefinitionNotFoundError if not found and defined.
     */
    public AdsPropertyDef getContextParameter() {
        if (contextParameterId == null) {
            return null;
        }
        final AdsPropertyDef prop = findContextParameter();
        if (prop != null) {
            return prop;
        } else {
            throw new DefinitionNotFoundError(contextParameterId);
        }
    }

    /**
     * @return identifier of context object class or null if context parameter
     * not defined.
     * @throws DefinitionNotFoundError if context parameter not found and
     * defined.
     * @throws IllegalUsageError if context parameter is not valid reference.
     */
    public Id getContextClassId() {
        final AdsPropertyDef prop = getContextParameter();
        if (prop != null) {
            if (prop.getValue().getType().getTypeId() != EValType.PARENT_REF) {
                throw new IllegalUsageError("Context parameter '" + prop.getQualifiedName() + "' must be reference.");
            }
            final Id[] path = prop.getValue().getType().getPath().asArray();
            return path[path.length - 1];
        } else {
            return null;
        }
    }
//    /**
//     * @return context object class or null if not defined or not found.
//     */
//    public AdsClassDef findContextObjectClass() {
//        if (contextObjectClassId == null) {
//            return null;
//        }
//        final DefinitionSearcher<AdsClassDef> searcher = AdsSearcher.Factory.newAdsClassSearcher(this);
//        return searcher.findById(contextObjectClassId);
//    }
//
//    /**
//     * @return context object class or null if not defined.
//     * @throws DefinitionNotFoundError if not found and defined.
//     */
//    public AdsClassDef getContextObjectClass() {
//        if (contextObjectClassId == null) {
//            return null;
//        }
//        final DefinitionSearcher<AdsClassDef> searcher = AdsSearcher.Factory.newAdsClassSearcher(this);
//        return searcher.getById(contextObjectClassId);
//    }
//    @Override
//    public void collectDependences(List<Definition> list) {
//        super.collectDependences(list);
//        final AdsClassDef contextObjectClass = findContextObjectClass();
//        if (contextObjectClass != null) {
//            list.add(contextObjectClass);
//        }
//    }

    public boolean isSubreport() {
        return subreport;
    }

    public void setIsSubreport(boolean isSubreport) {
        if (this.subreport != isSubreport) {
            this.subreport = isSubreport;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean canChangeAccessMode() {
        return false;
    }

    @Override
    public boolean canChangePublishing() {
        return false;
    }

    @Override
    public EAccess getAccessMode() {
        return EAccess.PUBLIC;
    }

    @Override
    public boolean isPublished() {
        return true;
    }

    public boolean isUserReport() {
        return false;
    }

    @Override
    public boolean isModelRequred() {
        return true;//!isUserReport();
    }

    @Override
    public void exportToUds(OutputStream out) throws IOException {
        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.newInstance();
        UserReportExchangeType xEx = xDoc.addNewAdsUserReportExchange();
        xEx.setName(getName());
        xEx.setDescription(getDescription());
        xEx.setId(getId());
        UserReportDefinitionType xDef = xEx.addNewAdsUserReportDefinition();

        this.appendTo(xDef.addNewReport(), AdsDefinition.ESaveMode.NORMAL);
        AdsLocalizingBundleDef sb = findExistingLocalizingBundle();
        if (sb != null) {
            sb.appendTo(xDef.addNewStrings(), AdsDefinition.ESaveMode.NORMAL);
        }

        final List<Definition> dependences = new ArrayList<>();
        final Map<Id, AdsImageDef> usedImages = new HashMap<>();
        visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                dependences.clear();
                radixObject.collectDependences(dependences);
                for (Definition def : dependences) {
                    if (def instanceof AdsImageDef) {
                        usedImages.put(def.getId(), (AdsImageDef) def);
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        Map<Id, Id> imageIdReplaceMap = null;
        if (!usedImages.isEmpty()) {
            imageIdReplaceMap = new HashMap<>();
            File imagesZipFile = File.createTempFile("imgJarTmp", "imgJarTmp");

            FileOutputStream zipOut = null;
            ZipOutputStream zip = null;
            boolean done = false;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                XmlFormatter.save(xDoc, stream);
                String asString = new String(stream.toByteArray(), FileUtils.XML_ENCODING);

                for (Id oldId : usedImages.keySet()) {
                    Id newId = Id.Factory.newInstance(EDefinitionIdPrefix.IMAGE);
                    imageIdReplaceMap.put(oldId, newId);
                    asString = asString.replace(oldId.toString(), newId.toString());
                }
                try {
                    xDoc = AdsUserReportExchangeDocument.Factory.parse(asString);
                } catch (XmlException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }

                zipOut = new FileOutputStream(imagesZipFile);
                zip = new ZipOutputStream(zipOut);

                for (Id oldId : usedImages.keySet()) {
                    Id newId = imageIdReplaceMap.get(oldId);
                    AdsImageDef image = usedImages.get(oldId);
                    File inputFile = image.getImageFile();
                    byte[] imageBytes = FileUtils.readBinaryFile(inputFile);

                    ZipEntry e = new ZipEntry(inputFile.getName().replace(oldId.toString(), newId.toString()));
                    try {
                        zip.putNextEntry(e);
                        zip.write(imageBytes);
                    } finally {
                        zip.closeEntry();
                    }
                }
                done = true;
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                if (zipOut != null) {
                    try {
                        zip.close();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                if (done) {
                    byte[] zipBytes = FileUtils.readBinaryFile(imagesZipFile);
                    xDoc.getAdsUserReportExchange().setImages(Base64.encode(zipBytes));
                }
                FileUtils.deleteFile(imagesZipFile);
            }
        }
        XmlFormatter.save(xDoc, out);
    }

    public static class ReportUtils {

        public static class ParamInfo {

            public final String name;
            public final AdsTypeDeclaration type;

            public ParamInfo(String name, AdsTypeDeclaration type) {
                this.name = name;
                this.type = type;
            }
        }

        public static List<AdsParameterPropertyDef> getReportInputParams(AdsClassDef reportCandidate) {
            final List<AdsParameterPropertyDef> result = new ArrayList<>();
            for (AdsPropertyDef prop : reportCandidate.getProperties().getLocal()) {
                if (prop.getNature() == EPropNature.SQL_CLASS_PARAMETER) {
                    final AdsParameterPropertyDef parameterProperty = (AdsParameterPropertyDef) prop;
                    if (parameterProperty.calcDirection() != EParamDirection.OUT) {
                        result.add(parameterProperty);
                    }
                }
            }
            return result;
        }

        public static List<ParamInfo> getExecuteMethodProfile(AdsClassDef reportCandidate) {
            List<ParamInfo> result = new LinkedList<>();
            result.add(new ParamInfo("out", AdsTypeDeclaration.Factory.newPlatformClass("java.io.OutputStream")));
            result.add(new ParamInfo("format", AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EReportExportFormat")));
            result.addAll(getOpenMethodProfile(reportCandidate));
            return result;
        }

        public static List<ParamInfo> getOpenMethodProfile(AdsClassDef reportCandidate) {
            List<ParamInfo> result = new LinkedList<>();

            for (AdsParameterPropertyDef param : getReportInputParams(reportCandidate)) {
                result.add(new ParamInfo(param.getName(), param.getValue().getType()));
            }
            return result;
        }
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ReportClassRadixdoc(getSource(), page, options);
            }
        };
    }
}
