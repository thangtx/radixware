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

import java.util.EnumSet;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.XPathUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.ValTypes;

/**
 * Collection of helper methods creating visitor providers for different needs
 *
 */
public class AdsVisitorProviders {

    /**
     * Returns visitor propvider instance that helps to locate all ads
     * enumerations that allowed for property based on given dds column if no
     * dds enumeration is associated with column any of ads enumeration with the
     * same type as column type are accepted otherwise only enumerations
     * publishing columns dds enumeration
     */
    public static final VisitorProvider newEnumForColumnPropertyProvider(final DdsColumnDef column) {
        if (column == null) {
            return new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return false;
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return false;
                }
            };
        }
        final EValType enumType = column.getValType().isArrayType()
                ? column.getValType().getArrayItemType() : column.getValType();
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    return ((AdsEnumDef) object).getItemType() == enumType;
                } else {
                    return false;
                }
            }
        };
    }

    /**
     * Returns visitor provider instance that helps to locate all ads
     * enumerations that allowed for given type if enumType is null all ads
     * enums means allowed for acception
     */
    public static final VisitorProvider newEnumBasedTypeProvider(EValType type) {
        final EValType enumType = type.isArrayType() ? type.getArrayItemType() : type;
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    if (enumType == null) {
                        return true;
                    } else {
                        AdsEnumDef ed = (AdsEnumDef) object;
                        return ed.getItemType() == enumType || ed.getItemType().getArrayType() == enumType;
                    }
                }
                return false;
            }
        };
    }

    public static final VisitorProvider newTypeProvider(EValType type, ERuntimeEnvironmentType env) {
        switch (type) {
            case INT:
            case CHAR:
            case STR:
                return newEnumBasedTypeProvider(type);
            case XML:
                return newXmlBasedTypesProvider(env);
            case USER_CLASS:
                return newClassBasedTypesProvider(env);
            default:
                return VisitorProviderFactory.createDefaultVisitorProvider();
        }

    }

    public static final VisitorProvider newSchemeVisitorProvider() {
        return new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsXmlSchemeDef) {
                    return XPathUtils.isSchemaDocument(((AdsXmlSchemeDef) radixObject).getXmlDocument());
                }
                return false;
            }
        };
    }

    public static final VisitorProvider newAdsEnumVisitorProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    return true;
                }
                return false;
            }
        };
    }

    public static final VisitorProvider newAdsEnumVisitorProvider(final EValType itemType) {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef && ((AdsEnumDef) object).getItemType() == itemType) {
                    return true;
                }
                return false;
            }
        };
    }

    public static final VisitorProvider newAdsPlatformPublishingEnumVisitorProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    AdsEnumDef e = (AdsEnumDef) object;
                    if (e.isPlatformEnumPublisher()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Returns provider helps to find class associated with tabl with given
     * tableId
     */
    public static final VisitorProvider newEntityObjectTypeProvider(final Id tableId) {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEntityObjectClassDef) {
                    if (tableId == null) {
                        return true;
                    }
                    AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) object;
                    if (tableId.equals(clazz.getEntityId())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static final VisitorProvider newEntityTypeProvider() {
        return newEntityTypeProvider(null);
    }

    /**
     * Returns provider helps to find class associated with tabl with given
     * tableId
     */
    public static final VisitorProvider newEntityTypeProvider(final Id tableId) {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEntityClassDef) {
                    if (tableId == null) {
                        return true;
                    }
                    AdsEntityClassDef clazz = (AdsEntityClassDef) object;
                    if (tableId.equals(clazz.getEntityId())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Returns provider helps to find class associated with tabl with given
     * tableId
     */
    public static final VisitorProvider newUserPropObjectTypeProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEntityObjectClassDef) {
                    AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) object;
                    DdsTableDef table = clazz.findTable(object);
                    if (table != null && table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Returns provider helps to find class associated with tabl with given
     * tableId
     */
    public static final VisitorProvider newXmlBasedTypesProvider(final ERuntimeEnvironmentType env) {
        final EnumSet<ERuntimeEnvironmentType> enums = EnumSet.of(env);
        if (env != ERuntimeEnvironmentType.COMMON) {
            enums.add(ERuntimeEnvironmentType.COMMON);
        }
        if (env != null && env.isClientEnv()) {
            enums.add(ERuntimeEnvironmentType.COMMON_CLIENT);
        }
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsXmlSchemeDef && enums.contains(((AdsXmlSchemeDef) object).getTargetEnvironment())) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Returns provider helps to find class associated with tabl with given
     * tableId
     */
    public static final VisitorProvider newClassBasedTypesProvider(final ERuntimeEnvironmentType env) {
        final EnumSet<ERuntimeEnvironmentType> enums = EnumSet.of(env);
        if (env != ERuntimeEnvironmentType.COMMON) {
            enums.add(ERuntimeEnvironmentType.COMMON);
        }
        if (env != null && env.isClientEnv()) {
            enums.add(ERuntimeEnvironmentType.COMMON_CLIENT);
        }
        return new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsClassDef) {
                    for (ERuntimeEnvironmentType c : ((AdsClassDef) object).getTypeUsageEnvironments()) {
                        if (enums.contains(c)) {
                            return true;
                        }
                    }
                } else if (object instanceof AdsAbstractUIDef && object instanceof IAdsTypeSource) {
                    return enums.contains(((AdsAbstractUIDef) object).getUsageEnvironment());
                }
                return false;
            }
        };
    }

    private static class AdsClassVisitorProvider extends AdsVisitorProvider {

        private final EnumSet<EClassType> allowedClassTypes;
        private boolean topLevelDefsLookupOnly = true;
        private IFilter<AdsClassDef> filter;

        AdsClassVisitorProvider(EnumSet<EClassType> allowedClassTypes, IFilter<AdsClassDef> filter) {
            this.filter = filter;
            this.allowedClassTypes = allowedClassTypes == null ? EnumSet.allOf(EClassType.class) : EnumSet.copyOf(allowedClassTypes);
            for (EClassType ct : this.allowedClassTypes) {
                if (ct.isModelClass()) {
                    topLevelDefsLookupOnly = false;
                    break;
                }
            }
        }

        @Override
        public boolean isTarget(RadixObject object) {
            if (object instanceof AdsClassDef) {
                if (allowedClassTypes.contains(((AdsClassDef) object).getClassDefType())) {
                    if (filter == null || filter.isTarget((AdsClassDef) object)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean isContainer(RadixObject object) {
            if (topLevelDefsLookupOnly) {
                if (object instanceof ModuleDefinitions) {
                    return true;
                } else if (object instanceof Module) {
                    return object instanceof AdsModule;
                } else if (object instanceof Segment) {
                    return object instanceof AdsSegment;
                } else if (object instanceof Layer) {
                    return true;
                } else if (object instanceof Branch) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (object instanceof Module) {
                    return object instanceof AdsModule;
                } else if (object instanceof Segment) {
                    return object instanceof AdsSegment;
                } else {
                    return true;
                }
            }
        }
    }

    public static final VisitorProvider newClassVisitorProvider(EnumSet<EClassType> requiredClassTypes) {
        return new AdsClassVisitorProvider(requiredClassTypes, null);
    }

    public static final VisitorProvider newClassVisitorProvider(EnumSet<EClassType> requiredClassTypes, IFilter<AdsClassDef> filter) {
        return new AdsClassVisitorProvider(requiredClassTypes, filter);
    }

    /**
     * Returns provider helps to find class based on given definition
     */
    public static final VisitorProvider newInheritanceProvider(final AdsClassDef baseDef) {
        return new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (!(object instanceof AdsClassDef)) {
                    return false;
                }
                AdsClassDef classDef = (AdsClassDef) object;
                while (!baseDef.equals(classDef)) {
                    AdsTypeDeclaration type = classDef.getInheritance().getSuperClassRef();
                    if (type == null) {
                        return false;
                    }
                    AdsType t = type.resolve(classDef).get();
                    if (!(t instanceof AdsClassType)) {
                        return false;
                    }
                    classDef = ((AdsClassType) t).getSource();
                    if (classDef == null) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static final VisitorProvider newParagraphVisitorProvider(final boolean topLevel) {
        if (topLevel) {
            return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

                @Override
                public boolean isTarget(RadixObject object) {
                    return object instanceof AdsParagraphExplorerItemDef;
                }
            };
        } else {
            return new AdsVisitorProvider() {

                @Override
                public boolean isTarget(RadixObject object) {
                    return object instanceof AdsParagraphExplorerItemDef;
                }
            };
        }
    }

    public static final VisitorProvider newCompileableDefinitionsVisitorProvider(final ERuntimeEnvironmentType env) {
        return new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof org.radixware.kernel.common.defs.ads.src.IJavaSource && object instanceof ICompilable && object instanceof Definition) {
                    IJavaSource src = (IJavaSource) object;
                    JavaSourceSupport support = src.getJavaSourceSupport();
                    if (support.isSeparateFilesRequired(env)) {
                        return support.getSupportedEnvironments().contains(env);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        };
    }

    public static final VisitorProvider newClassCatalogsVisitorProvider() {
        return new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsClassCatalogDef;
            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return radixObject instanceof Branch
                        || radixObject instanceof Layer
                        || radixObject instanceof AdsSegment
                        || radixObject instanceof AdsModule
                        || radixObject instanceof ModuleDefinitions
                        || radixObject instanceof AdsEntityObjectClassDef
                        || radixObject instanceof ExtendableMembers
                        || radixObject instanceof EntityObjectPresentations
                        || radixObject instanceof AdsDefinitions;
            }
        };
    }

    public static IFilter<AdsPropertyDef> newPropertyForParameterTagFilter(final AdsClassDef ownerClass) {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef prop) {
                if (!prop.isJavaAccessibleFor(ownerClass)) {
                    return false;
                }
                final EValType valType = prop.getValue().getType().getTypeId();
                return ValTypes.ADS_PARAMETER_TAG_PROP_TYPES.contains(valType);
            }
        };
    }

    public static IFilter<AdsPropertyDef> newPropertyForSqlClassParameterFilter() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef prop) {


                final AdsClassDef ownerClass = prop.getOwnerClass();
                if (ownerClass instanceof AdsReportClassDef) {
                    if (prop instanceof AdsParameterPropertyDef) {
                        return true;
                    } else if (prop instanceof AdsDynamicPropertyDef) {
                        final EValType valType = prop.getValue().getType().getTypeId();
                        return ValTypes.ADS_SQL_CLASS_PARAM_TYPES.contains(valType);
                    } else {
                        return false;
                    }
                } else if (ownerClass instanceof AdsSqlClassDef) {
                    return prop instanceof AdsParameterPropertyDef;
                } else {
                    return false;
                }
            }
        };
    }

    public static VisitorProvider newReportProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                return (object instanceof AdsReportClassDef);
            }
        };
    }

    public static VisitorProvider newUserReportProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsUserReportClassDef) {
                    AdsUserReportClassDef report = (AdsUserReportClassDef) object;
                    return report.isActualVersion();
                } else {
                    return false;
                }
            }
        };
    }

    public static IFilter<AdsPropertyDef> newDataPropForReportDbImageCellFilter() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef prop) {
                final EValType valType = prop.getValue().getType().getTypeId();
                return valType == EValType.BLOB;
            }
        };
    }

    public static IFilter<AdsPropertyDef> newMimeTypePropForReportDbImageCellFilter() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef prop) {
                final EValType valType = prop.getValue().getType().getTypeId();
                return valType == EValType.STR;
            }
        };
    }

    public static IFilter<AdsPropertyDef> newReportContextParameterFilter() {
        return new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(AdsPropertyDef prop) {
                final EValType valType = prop.getValue().getType().getTypeId();
                return valType == EValType.PARENT_REF;
            }
        };
    }
}
