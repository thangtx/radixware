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
package org.radixware.kernel.common.defs.ads.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IDeprecatedInheritable;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.EType;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IJavaCodePrinter;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;

public class AdsUtils {

    public static Collection<AdsEntityObjectClassDef> findApplicationClassList(final DdsTableDef table) {

        if (table != null) {

            if (table.isDetailTable()) {
                final DdsReferenceDef masterReference = table.findMasterReference();
                if (masterReference == null) {
                    return Collections.emptyList();
                }

                final DdsTableDef masterTable = masterReference.findParentTable(table);
                final AdsEntityClassDef entityClass = findEntityClass(masterTable);
                if (entityClass == null) {
                    return Collections.emptyList();
                }

                final Collection<AdsApplicationClassDef> applicationClassList = findApplicationClassList(entityClass);
                if (applicationClassList == null) {
                    return Collections.emptyList();
                }

                final List<AdsEntityObjectClassDef> filterredApplicationClassList = new ArrayList<>();
                filterredApplicationClassList.add(entityClass);
                for (final AdsApplicationClassDef cls : applicationClassList) {
                    if (cls.isDetailAllowed(masterReference.getId())) {
                        filterredApplicationClassList.add(cls);
                    }
                }

                return filterredApplicationClassList;
            }
        }

        return null;
    }

    public static Collection<AdsApplicationClassDef> findApplicationClassList(final AdsEntityClassDef entityClass) {
        if (entityClass == null || !entityClass.isInBranch()) {
            return null;
        }

        final Set<AdsApplicationClassDef> classes = new HashSet<>();
        final Id entityId = entityClass.getEntityId();
        entityClass.getBranch().visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                final AdsApplicationClassDef application = (AdsApplicationClassDef) radixObject;
                if (Objects.equals(entityId, application.getEntityId())) {
                    final SearchResult<AdsClassDef> overwriteBase = application.getHierarchy().findOverwriteBase();
                    if (!overwriteBase.isEmpty()) {
                        classes.add((AdsApplicationClassDef) overwriteBase.get());
                    }
                }
            }
        }, AdsVisitorProviders.newClassVisitorProvider(EnumSet.of(EClassType.APPLICATION)));

        return classes;
    }

    public static AdsEntityClassDef findEntityClass(final DdsTableDef table) {

        if (table != null) {
            final Layer layer = table.getModule().getSegment().getLayer();
            final Id tableId = table.getId();
            final Id entityClassId = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);

            return findEntityClassDef(entityClassId, layer);

        }

        return null;
    }

    private static AdsEntityClassDef findEntityClassDef(Id id, Layer layer) {
        for (AdsModule module : ((AdsSegment) layer.getAds()).getModules()) {
            final AdsDefinition def = module.getTopContainer().findById(id);
            if (def instanceof AdsEntityClassDef) {
                return (AdsEntityClassDef) def;
            }
        }
        for (Layer base : layer.listBaseLayers()) {
            AdsEntityClassDef e = findEntityClassDef(id, base);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    private static void reportAccessError(RadixObject context, AdsDefinition def, IProblemHandler ph) {
        if (ph != null) {
            Definition cd = null;
            RadixObject co = context;
            while (co != null) {
                if (co instanceof Definition) {
                    cd = (Definition) co;
                    break;
                }
                co = co.getContainer();
            }
            if (cd != null) {
                co = cd;
            } else {
                co = context;
            }
            //  if(!def.isReadOnly() && !def.isPublished()){
            //      def.setPublished(true);
            //  }
            ph.accept(RadixProblem.Factory.newError(context, "Definition " + def.getQualifiedName() + " is not accessible for " + co.getQualifiedName()));
        }
    }

    private static boolean isOverwriteOf(AdsDefinition def, AdsDefinition of) {
        if (def.getId() == of.getId()) {
            AdsDefinition ovr = def.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                if (ovr == of) {
                    return true;
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
        }
        return false;
    }

    private static boolean checkInheritanceAccessibility(RadixObject context, AdsDefinition contextDef, AdsDefinition def, IProblemHandler ph) {
        if (contextDef != null) {
            AdsDefinition contextOwner = contextDef.getOwnerDef();
            AdsDefinition usedDefOwner = def.getOwnerDef();

            if (contextOwner != null && usedDefOwner != null) {//overwrite
                if (isOverwriteOf(contextOwner, usedDefOwner)) {
                    return true;
                }

                AdsClassDef contextOwnerClass = null;
                AdsClassDef usedOwnerClass = null;

                if (contextOwner instanceof AdsClassDef) {
                    contextOwnerClass = (AdsClassDef) contextOwner;
                } else if (contextOwner instanceof AdsClassMember) {
                    contextOwnerClass = ((AdsClassMember) contextOwner).getOwnerClass();
                }
                if (usedDefOwner instanceof AdsClassDef) {
                    usedOwnerClass = (AdsClassDef) usedDefOwner;
                } else if (usedDefOwner instanceof AdsClassMember) {
                    usedOwnerClass = ((AdsClassMember) usedDefOwner).getOwnerClass();
                }

                //not override
                if (contextOwnerClass != null && usedOwnerClass != null) {

                    if (isOverwriteOf(contextOwnerClass, usedOwnerClass)) {
                        return true;
                    }

                    AdsClassDef clazz = contextOwnerClass.getInheritance().findSuperClass().get();
                    while (clazz != null) {
                        if (clazz == usedOwnerClass || isOverwriteOf(clazz, usedOwnerClass)) {
                            return true;
                        }
                        if (def.getDefinitionType() == EDefType.CLASS_PROPERTY && clazz instanceof AdsModelClassDef) {
                            AdsModelClassDef model = (AdsModelClassDef) clazz;
                            AdsClassDef serverSide = model.findServerSideClasDef();
                            if (serverSide == usedOwnerClass || isOverwriteOf(serverSide, usedOwnerClass)) {
                                return true;
                            }
                        }
                        clazz = clazz.getInheritance().findSuperClass().get();
                    }
                }
            }
        }

        reportAccessError(context, def, ph);
        return false;
    }

    private static boolean isSameLayer(RadixObject obj1, RadixObject obj2) {
        Layer layer1 = null;
        Segment segment1 = null;
        Module module1 = obj1.getModule();
        if (module1 != null) {
            segment1 = module1.getSegment();
        }
        if (segment1 != null) {
            layer1 = segment1.getLayer();
        }
        Layer layer2 = null;
        Segment segment2 = null;
        Module module2 = obj2.getModule();
        if (module2 != null) {
            segment2 = module2.getSegment();
        }
        if (segment2 != null) {
            layer2 = segment2.getLayer();
        }
        if (layer1 == null || layer2 == null) {
            return false;
        }
        return layer1 == layer2;
    }

    public static boolean isDefinitionPublished(AdsDefinition def) {
        if (def.isPublished()) {
            return true;
        } else {
            AdsDefinition ovr = def.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                if (ovr.isPublished()) {
                    return true;
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
            ovr = def.getHierarchy().findOverridden().get();
            while (ovr != null) {
                if (ovr.isPublished()) {
                    return true;
                }
                ovr = ovr.getHierarchy().findOverridden().get();
            }
            return false;
        }
    }

    private static boolean isCompanion(Module contextModule, Definition def) {
        return contextModule instanceof AdsModule && ((AdsModule) contextModule).isCompanionOf(def.getModule());
    }

    public static boolean isUserExtension(RadixObject radixObject) {
        if (radixObject != null) {
            for (RadixObject obj = radixObject; obj != null; obj = obj.getContainer()) {
                if (obj instanceof AdsUserFuncDef) {
                    return true;
                }
            }
        }
        Module module = radixObject.getModule();
        if (module == null) {
            return false;
        }
        Segment segment = module.getSegment();
        if (segment == null) {
            return false;
        }
        return segment.getType() == ERepositorySegmentType.UDS;
    }

    public static boolean checkAccessibility(RadixObject context, AdsDefinition def, boolean isInheritance, IProblemHandler ph) {

        if (isUserExtension(context)) {
            if (!isDefinitionPublished(def) && !isUserExtension(def)) {
                reportAccessError(context, def, ph);
                return false;
            }
        }

        AdsDefinition contextDef = null;
        RadixObject contextObj = context;
        while (contextObj != null) {
            if (contextObj instanceof AdsDefinition) {
                contextDef = (AdsDefinition) contextObj;
                break;
            }
            contextObj = contextObj.getContainer();
        }
        Module contextModule = context.getModule();

        EAccess access = def.getAccessMode();
        if (access == EAccess.PROTECTED) {
            if (contextModule != def.getModule() && !isCompanion(contextModule, def)) {

                if (!isInheritance) {
                    if (!checkInheritanceAccessibility(context, contextDef, def, ph)) {
                        return false;
                    }
                }
                if (isDefinitionPublished(def)) {
                    return true;
                } else {

                    if (!isSameLayer(context, def)) {
                        reportAccessError(context, def, ph);
                        return false;
                    }
                }

            }
        } else if (access == EAccess.DEFAULT) {
            if (contextModule != def.getModule() && !isCompanion(contextModule, def)) {
                reportAccessError(context, def, ph);
                return false;//checkInheritanceAccessibility(context, contextDef, def, ph);
            }
            return true;
        } else if (access == EAccess.PRIVATE) {
            AdsDefinition topLevel = def.findTopLevelDef();
            if (topLevel == null) {
                reportAccessError(context, def, ph);
                return false;
            } else {
                if (!topLevel.isParentOf(context)) {
                    reportAccessError(context, def, ph);
                    return false;
                }
            }
        } else if (access == EAccess.PUBLIC) {
            if (isDefinitionPublished(def)) {
                return true;
            } else {
                if (!isSameLayer(context, def)) {
                    reportAccessError(context, def, ph);
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isEntityClassExists(final DdsTableDef table) {
        final AdsEntityClassDef aec = findEntityClass(table);
        return (aec != null);
    }

    public static boolean isEntityClassAllowed(final DdsTableDef table) {
        Set<DdsReferenceDef> refs = table.collectOutgoingReferences();
        for (DdsReferenceDef ref : refs) {
            if (ref.getType() == EType.MASTER_DETAIL) {
                return false;
            }
        }
        return true;
    }

    public static AdsEntityGroupClassDef findEntityGroupClass(final DdsTableDef table) {
        final Layer layer = table.getModule().getSegment().getLayer();
        final Id tableId = table.getId();
        final Id entityGroupClassId = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS);

        for (AdsModule module : ((AdsSegment) layer.getAds()).getModules()) {
            final AdsDefinition def = module.getTopContainer().findById(entityGroupClassId);
            if (def instanceof AdsEntityGroupClassDef) {
                return (AdsEntityGroupClassDef) def;
            }
        }

        return null;
    }

    /**
     * Find top level ADS definition by identifier, without dependencies, for
     * right system.
     *
     * @return definition or null if not found.
     */
    public static AdsDefinition findTopLevelDefById(Layer layer, final Id id) {

        return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<AdsDefinition>() {
            @Override
            public void accept(HierarchyWalker.Controller<AdsDefinition> controller, Layer layer) {
                for (AdsModule adsModule : ((AdsSegment) layer.getAds()).getModules()) {
                    final AdsDefinition def = adsModule.getTopContainer().findById(id);
                    if (def != null) {
                        controller.setResultAndStop(def);
                    }
                }
            }
        });
    }

    /**
     * Find top level DDS definition by identifier, without dependencies, for
     * data movement.
     *
     * @return definition or null if not found.
     */
    public static DdsDefinition findTopLevelDdsDefById(Layer layer, final Id id) {

        return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<DdsDefinition>() {
            @Override
            public void accept(HierarchyWalker.Controller<DdsDefinition> controller, Layer layer) {
                for (DdsModule ddsModule : ((DdsSegment) layer.getDds()).getModules()) {
                    final DdsDefinition def = (DdsDefinition) ddsModule.getDefinitionSearcher().findInsideById(id);
                    if (def != null) {
                        controller.setResultAndStop(def);
                    }
                }
            }
        });
    }

    public static DdsAccessPartitionFamilyDef findTopLevelApfById(Layer layer, final Id id) {
        return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<DdsAccessPartitionFamilyDef>() {
            @Override
            public void accept(HierarchyWalker.Controller<DdsAccessPartitionFamilyDef> controller, Layer layer) {
                if (layer.isLocalizing()) {
                    return;
                }
                for (DdsModule ddsModule : ((DdsSegment) layer.getDds()).getModules()) {
                    final DdsModelDef model = ddsModule.getModelManager().findModel();
                    if (model != null) {
                        final DdsAccessPartitionFamilyDef apf = model.getAccessPartitionFamilies().findById(id);
                        if (apf != null) {
                            controller.setResultAndStop(apf);
                        }
                    }
                }
            }
        });
    }

    public static boolean isEntityGroupClassExists(final DdsTableDef table) {
        final AdsEntityGroupClassDef egc = findEntityGroupClass(table);
        return (egc != null);
    }

    public static boolean isPresentationEntityAdapterAllowed(final AdsEntityObjectClassDef clazz) {
        AdsPresentationEntityAdapterClassDef adapter = clazz.findPresentationAdapter();
        if (adapter == null) {
            if (clazz instanceof AdsEntityClassDef) {
                return true;
            } else {
                AdsEntityObjectClassDef basis = clazz.findBasis();
                if (basis == null) {
                    return false;
                } else {
                    return basis.findPresentationAdapter() != null;
                }
            }
        } else {
            return false;
        }
    }

    public static boolean mayBeUsedByRoles(RadixObject object) {
        return object instanceof AdsSelectorPresentationDef
                || object instanceof AdsEditorPresentationDef
                || (object instanceof AdsExplorerItemDef && ((AdsExplorerItemDef) object).findOwnerEditorPresentation() == null)
                || object instanceof AdsContextlessCommandDef;

    }

    /**
     * Search title owner definition starting with <tt>startDefinition</tt>. If
     * <tt>startDefinition</tt> instance of {@link IInheritableTitledDefinition}
     * that result of
     * {@link IInheritableTitledDefinition#findOwnerTitleDefinition()} method
     * will be returned.
     */
    public static Definition findTitleOwnerDefinition(Definition startDefinition) {
        if (startDefinition instanceof IInheritableTitledDefinition) {
            return ((IInheritableTitledDefinition) startDefinition).findOwnerTitleDefinition();
        }
        return startDefinition;
    }

    private static final ValAsStr FALSE_AS_VAL_AS_STR = ValAsStr.Factory.newInstance(Boolean.FALSE, EValType.BOOL);

    public static void printTagCondition(final CodePrinter cp, final IfParamTag ifParamTag) {
        // supported only simple parameters
        final IParameterDef iParam = ifParamTag.findParameter();
        Definition def = null;
        Definition parameter = null;
        if (iParam != null) {
            def = iParam.getDefinition();
        }
        if (def instanceof AdsParameterPropertyDef || def instanceof AdsDynamicPropertyDef || def instanceof AdsFilterDef.Parameter) {
            parameter = def;
        }
        if (parameter == null) {
            cp.printError();
            return;
        }

        final String paramName;
        if (cp instanceof IJavaCodePrinter && (def instanceof AdsDynamicPropertyDef && !(def instanceof AdsParameterPropertyDef))) {
            paramName = "get" + parameter.getDefinition().getId() + "()";
        } else {
            paramName = parameter.getName();
        }

        if (ifParamTag.getOperator() == EIfParamTagOperator.NULL) {
            cp.print(paramName + "==null");
            return;
        }

        if (ifParamTag.getOperator() == EIfParamTagOperator.NOT_NULL) {
            cp.print(paramName + "!=null");
            return;
        }

        if (ifParamTag.getOperator() == EIfParamTagOperator.EMPTY) {
            cp.print(paramName + "!=null && " + paramName + ".isEmpty()");
            return;
        }

        if (ifParamTag.getOperator() == EIfParamTagOperator.NOT_EMPTY) {
            cp.print(paramName + "!=null && !" + paramName + ".isEmpty()");
            return;
        }

        final AdsTypeDeclaration type;
        if (parameter instanceof AdsPropertyDef) {
            type = ((AdsPropertyDef) parameter).getValue().getType();
        } else if (parameter instanceof AdsFilterDef.Parameter) {
            type = ((AdsFilterDef.Parameter) parameter).getType();
        } else {
            type = null;
        }

        if (type == null) {
            cp.printError();
            return;
        }

        final ValAsStr value = ifParamTag.getValue();
        final boolean multy = type.getTypeId().isArrayType();
        final boolean equal = ifParamTag.getOperator() == EIfParamTagOperator.EQUAL;

        if (value == null) {
            if (ifParamTag.getOperator() == EIfParamTagOperator.EQUAL) {
                if (multy) {
                    cp.print(paramName + "!=null && " + paramName + ".contains(null)");
                } else {
                    cp.print(paramName + "==null");
                }
            } else {
                if (multy) {
                    cp.print(paramName + "==null || !" + paramName + ".contains(null)");
                } else {
                    cp.print(paramName + "!=null");
                }
            }
            return;
        }

        if (equal) {
            if (multy) {
                cp.print(paramName + "!=null && " + paramName + ".contains(");
            }
        } else {
            if (multy) {
                cp.print(paramName + "==null || !" + paramName + ".contains(");
            }
        }
        final AdsType resolvedType = type.resolve(parameter).get();
        final AdsEnumDef enumDef;
        if (resolvedType instanceof AdsEnumType) {
            enumDef = ((AdsEnumType) resolvedType).getSource();
        } else {
            enumDef = null;
        }
        boolean isEnum = false;
        if (enumDef != null) {
            for (AdsEnumItemDef item : enumDef.getItems().list(ExtendableDefinitions.EScope.ALL)) {
                if (Utils.equals(item.getValue(), value)) {
                    if (!multy) {
                        if (equal) {
                            cp.print(paramName + "==");
                        } else {
                            cp.print(paramName + "!=");
                        }
                    }
                    item.getJavaSourceSupport().getCodeWriter(JavaSourceSupport.UsagePurpose.SERVER_EXECUTABLE).writeUsage(cp);
                    isEnum = true;
                    break;
                }
            }
        }

        if (!isEnum) {
            final String javaValue = value.toString();
            switch (type.getTypeId()) {
                case INT:
                case ARR_INT:
                case NUM:
                case ARR_NUM:
                case DATE_TIME:
                case ARR_DATE_TIME:
                    if (!multy) {
                        if (equal) {
                            cp.print(paramName + "==");
                        } else {
                            cp.print(paramName + "!=");
                        }
                    }
                    cp.print(javaValue);
                    break;
                case STR:
                case ARR_STR:
                case CHAR:
                case ARR_CHAR:
                case CLOB:
                case ARR_CLOB:
                case BLOB:
                case ARR_BLOB:
                case BIN:
                case ARR_BIN:
                    if (!multy) {
                        if (equal) {
                            cp.print(paramName + "!=null && " + paramName + ".equals(");
                        } else {
                            cp.print(paramName + "!=null && !" + paramName + ".equals(");
                        }
                    }

                    cp.printStringLiteral(javaValue);

                    if (!multy) {
                        cp.print(")");
                    }
                    break;
                case BOOL:
                case ARR_BOOL:
                    if (!multy) {
                        if (equal) {
                            cp.print(paramName + "!=null && " + paramName + ".equals(");
                        } else {
                            cp.print(paramName + "!=null && !" + paramName + ".equals(");
                        }
                    }

                    cp.print(FALSE_AS_VAL_AS_STR.equals(value) ? "false" : "true");

                    if (!multy) {
                        cp.print(")");
                    }
                    break;
            }
        }

        if (multy) {
            cp.print(")");
        }
    }

    public static File calcHumanReadableFile(RadixObject ro) {
        if (ro != null) {
            if (ro instanceof AdsDefinition && !(ro instanceof ILocalizingBundleDef)) {
                return calcHumanReadableFile(ro.getFile());
            }
        }
        return null;
    }
    private static final String humanReadableFileName = File.separator + Module.PREVIEW_DIR_NAME + File.separator;

    public static File calcHumanReadableFile(File sourceFile) {
        if (sourceFile == null) {
            return null;
        }
        String name = FileUtils.getFileBaseName(sourceFile);
        String path = sourceFile.getParent();
        if (name != null && path != null) {
            File file = new File(path + humanReadableFileName + name + ".java");
            return file;
        }
        return null;
    }

    public static boolean isEnableHumanReadable(RadixObject def) {
        if (def == null || BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null) {
            return false;
        }

        if ((def instanceof AdsClassDef) || (def instanceof AdsEnumDef)) {
            boolean isDef = def instanceof ITransparency && ((ITransparency) def).getTransparence() != null
                    && ((ITransparency) def).getTransparence().isTransparent()
                    && !((ITransparency) def).getTransparence().isExtendable();
            if (def instanceof AdsClassDef) {
                EClassType type = ((AdsClassDef) def).getClassDefType();
                if (type != EClassType.APPLICATION && type != EClassType.ENTITY
                        && type != EClassType.ENUMERATION && type != EClassType.INTERFACE
                        && type != EClassType.PRESENTATION_ENTITY_ADAPTER && type != EClassType.DYNAMIC
                        && type != EClassType.ENTITY_GROUP) {
                    return false;
                }
            }
            return !isDef;
        }
        return false;
    }

    public static boolean savePreview(RadixObject radixObject) {
        if (!(radixObject instanceof AdsDefinition)) {
            return false;
        }
        Module m = radixObject.getModule();
        if (!(m instanceof AdsModule)) {
            return false;
        }

        AdsModule module = (AdsModule) m;
        if (radixObject.isReadOnly()) {
            return false;
        }
        module.getDefinitions().savePreview((AdsDefinition) radixObject);
        return true;

    }

    public static AdsDefinition getDeprecatedOwner(AdsDefinition definition) {
        if (definition instanceof IDeprecatedInheritable) {
            AdsDefinition ovr = definition.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                if (!(ovr instanceof IDeprecatedInheritable)) {
                    break;
                }
                if (((IDeprecatedInheritable) ovr).getAccessFlags().isDefaultDeprecated()) {
                    return ovr;
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
            ovr = definition.getHierarchy().findOverridden().get();
            while (ovr != null) {
                if (!(ovr instanceof IDeprecatedInheritable)) {
                    break;
                }
                if (((IDeprecatedInheritable) ovr).getAccessFlags().isDefaultDeprecated()) {
                    return ovr;
                }
                ovr = ovr.getHierarchy().findOverridden().get();
            }
        }
        return definition;
    }
}
