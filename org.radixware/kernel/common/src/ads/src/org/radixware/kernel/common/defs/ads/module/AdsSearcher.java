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
package org.radixware.kernel.common.defs.ads.module;

import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

public class AdsSearcher {

    private abstract static class AdsSearcherImpl<T extends Definition> extends DefinitionSearcher<T> {

        AdsSearcherImpl(final AdsDefinition context) {
            super(context);
        }

        AdsSearcherImpl(final Definition context) {
            super(context);
        }

        AdsSearcherImpl(final AdsModule context) {
            super(context);
        }

        @Override
        protected boolean shouldVisitDependence(Id moduleId, Id key) {
            return canContainDef(moduleId, key);
        }

    }
    private static final Id TYPES_MODULE_ID = Id.Factory.loadFrom("mdlTSHSLPBGIJFDBPAOBIDEYAXWUI");
    private static final Id ADS_SQL_STATEMENT_CLASS_ID = Id.Factory.loadFrom("pdcSqlStatement______________");
    private static final Id ADS_SQL_BLOCK_CLASS_ID = Id.Factory.loadFrom("pdcSqlBlock__________________");
    private static final Id ADS_ALGO_CLASS_ID = Id.Factory.loadFrom("pdcAlgorithm_________________");
    private static final Id ADS_STATEMENT_CLASS_ID = Id.Factory.loadFrom("pdcStatement_________________");
    private static final Id ADS_ARTE_CLASS_ID = Id.Factory.loadFrom("pdcArte______________________");
    private static final Id ADS_TRACE_CLASS_ID = Id.Factory.loadFrom("pdcTrace_____________________");
    private static final Id ARTE_MODULE_ID = Id.Factory.loadFrom("mdlPEKYFVDRVZHGZCBQQDY2NOYFOY");

    private static boolean shouldSearchForDefInside(Module mdl, Id id) {
        if (!canContainDef(mdl.getId(), id)) {
            return false;
        }
        if (mdl.isUserExtension()) {
            EDefinitionIdPrefix prefix = id.getPrefix();
            if (prefix != null) {
                switch (prefix) {
                    case USER_DEFINED_REPORT:
                    case REPORT:
                    case USER_FUNC_CLASS:
                    case LIB_USERFUNC_PREFIX:
                    case APPLICATION_ROLE:
                    case MSDL_SCHEME:
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    private static boolean canContainDef(Id moduleId, Id key) {
        if (key == AdsReportClassDef.PREDEFINED_ID
                || key == AdsEntityClassDef.PREDEFINED_ID
                || key == AdsEntityGroupClassDef.PREDEFINED_ID
                || key == ADS_STATEMENT_CLASS_ID
                || key == ADS_SQL_STATEMENT_CLASS_ID
                || key == ADS_SQL_BLOCK_CLASS_ID
                || key == ADS_ALGO_CLASS_ID) {
            return TYPES_MODULE_ID == moduleId;
        } else if (key == ADS_ARTE_CLASS_ID
                || key == ADS_TRACE_CLASS_ID) {
            return ARTE_MODULE_ID == moduleId;
        }
        return true;
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static DefinitionSearcher<DdsTableDef> newDdsTableSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsTableDef>(context) {

                @Override
                public DefinitionSearcher<DdsTableDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsTableSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsTableDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsFunctionDef> newDdsFunctionSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsFunctionDef>(context) {

                @Override
                public DefinitionSearcher<DdsFunctionDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsFunctionSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsFunctionDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsPackageDef> newDdsPackageSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsPackageDef>(context) {

                @Override
                public DefinitionSearcher<DdsPackageDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsPackageSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsPackageDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsTypeDef> newDdsTypeSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsTypeDef>(context) {

                @Override
                public DefinitionSearcher<DdsTypeDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsTypeSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsTypeDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsReferenceDef> newDdsReferenceSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsReferenceDef>(context) {

                @Override
                public DefinitionSearcher<DdsReferenceDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsReferenceSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsReferenceDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsAccessPartitionFamilyDef> newDdsApfSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsAccessPartitionFamilyDef>(context) {

                @Override
                public DefinitionSearcher<DdsAccessPartitionFamilyDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsApfSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsAccessPartitionFamilyDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        public static DefinitionSearcher<DdsSequenceDef> newDdsSequenceSearcher(final Definition context) {
            return new AdsSearcherImpl<DdsSequenceDef>(context) {

                @Override
                public DefinitionSearcher<DdsSequenceDef> findSearcher(final Module module) {
                    if (module instanceof DdsModule) {
                        return ((DdsModule) module).getDdsSequenceSearcher();
                    } else {
                        return null;
                    }
                }

                @Override
                public DdsSequenceDef findInsideById(final Id id) {
                    return null;
                }
            };
        }

        private static class AdsDefinitionSearcher<T extends AdsDefinition> extends AdsSearcherImpl<T> {

            public AdsDefinitionSearcher(final AdsModule context) {
                super(context);
            }

            public AdsDefinitionSearcher(final AdsDefinition context) {
                super(context);
            }

            public AdsDefinitionSearcher(final Definition context) {
                super(context);
            }

            @Override
            @SuppressWarnings("unchecked")
            public T findInsideById(final Id id) {
                final Definition context = getContext();
                if (context == null) {
                    return null;
                }
                final Module mdl = context.getModule();
                if (mdl instanceof AdsModule) {
                    if (!shouldSearchForDefInside(mdl, id)) {
                        return null;
                    }
                    AdsModule module = (AdsModule) mdl;
                    AdsDefinition[] cache = new AdsDefinition[]{null};
                    if (module.containsDefinitionInRepository(id, cache)) {
                        if (cache[0] != null) {
                            return (T) cache[0];
                        } else {
                            return (T) module.getTopContainer().findById(id);
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }

            @Override
            public DefinitionSearcher<T> findSearcher(final Module module) {
                if (module instanceof AdsModule) {
                    AdsModule ads = (AdsModule) module;
                    return new AdsDefinitionSearcher<>(ads);
                } else {
                    return null;
                }
            }
        }

        private static class AdsClassSearcher extends AdsSearcherImpl<AdsClassDef> {

            public AdsClassSearcher(final AdsModule context) {
                super(context);
            }

            public AdsClassSearcher(final AdsDefinition context) {
                super(context);
            }

            @Override
            public AdsClassDef findInsideById(final Id id) {
                final AdsModule module = (AdsModule) getContext().getModule();
                if (module == null) {
                    return null;
                } else {
                    if (!shouldSearchForDefInside(module, id)) {
                        return null;
                    }
                    final AdsDefinition def = module.getTopContainer().findById(id);
                    if (def instanceof AdsClassDef) {
                        return (AdsClassDef) def;
                    } else {
                        return null;
                    }
                }
            }

            @Override
            public DefinitionSearcher<AdsClassDef> findSearcher(final Module module) {
                if (module instanceof AdsModule) {
                    return new AdsClassSearcher((AdsModule) module);
                } else {
                    return null;
                }
            }
        }

        public static DefinitionSearcher<AdsDefinition> newAdsDefinitionSearcher(final Definition context) {
            return new AdsDefinitionSearcher<>(context);
        }

        public static DefinitionSearcher<AdsDefinition> newAdsDefinitionSearcher(final AdsModule module) {
            return new AdsDefinitionSearcher<>(module);
        }

        public static DefinitionSearcher<AdsContextlessCommandDef> newAdsContextlessCommandSearcher(final AdsModule module) {
            return new AdsDefinitionSearcher<>(module);
        }

        public static DefinitionSearcher<AdsEnumDef> newAdsEnumSearcher(final Module module) {
            return new AdsDefinitionSearcher<>(module);
        }

        public static DefinitionSearcher<AdsClassDef> newAdsClassSearcher(final AdsDefinition context) {
            return new AdsClassSearcher(context);
        }

        public static DefinitionSearcher<AdsClassDef> newAdsClassSearcher(final AdsModule context) {
            return new AdsClassSearcher(context);
        }

        public static class EntityClassSearcher extends DefinitionSearcher<AdsEntityClassDef> {

            public EntityClassSearcher(final Module context) {
                super(context);
            }

            @Override
            public EntityClassSearcher findSearcher(final Module module) {
                if (module instanceof AdsModule) {
                    return AdsSearcher.Factory.newEntityClassSearcher((AdsModule) module);
                } else {
                    return null;
                }
            }

            @Override
            public AdsEntityClassDef findInsideById(final Id id) {
                if (!shouldSearchForDefInside(getContext().getModule(), id)) {
                    return null;
                }

                final AdsDefinition def = ((AdsModule) getContext().getModule()).getTopContainer().findById(id);
                if (def instanceof AdsEntityClassDef) {
                    return (AdsEntityClassDef) def;
                } else {
                    return null;
                }
            }

            public SearchResult<AdsEntityClassDef> findEntityClass(final DdsTableDef table) {
                return findById(Id.Factory.changePrefix(table.getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS));
            }
        }

        public static EntityClassSearcher newEntityClassSearcher(final AdsModule context) {
            return new EntityClassSearcher(context);
        }

        private static class NsSearchEngine extends SearchEngine<AdsDefinition, String> {

            public NsSearchEngine(final AdsDefinition context) {
                super(context);
            }

            public NsSearchEngine(final AdsModule context) {
                super(context);
            }

            @Override
            protected AdsDefinition findInsideByKey(final String key) {
                if (key == null) {
                    return null;
                }
                final Module module = context.getModule();

                if (module instanceof AdsModule) {
                    final AdsModule ads = (AdsModule) module;
                    for (AdsDefinition def : ads.getDefinitions()) {
                        if (def instanceof IXmlDefinition) {
                            final IXmlDefinition xml = (IXmlDefinition) def;
                            if (key.equals(xml.getTargetNamespace())) {
                                return def;
                            }
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            }

            @Override
            protected SearchEngine<AdsDefinition, String> findEngine(final Module module) {
                if (module instanceof AdsModule) {
                    return new NsSearchEngine((AdsModule) module);
                } else {
                    return null;
                }
            }
        }

        public static class XmlDefinitionSearcher {

            private final NsSearchEngine engine;

            private XmlDefinitionSearcher(final AdsDefinition context) {
                this.engine = new NsSearchEngine(context);
            }

            public SearchResult<IXmlDefinition> findByNs(final String namespace) {
                final SearchResult<AdsDefinition> result = this.engine.findByKey(namespace);
                return new SearchResult.Proxy<AdsDefinition, IXmlDefinition>(result) {

                    @Override
                    protected IXmlDefinition convert(AdsDefinition src) {
                        return (IXmlDefinition) src;
                    }
                };
            }
        }

        public static XmlDefinitionSearcher newXmlDefinitionSearcher(final AdsDefinition context) {
            return new XmlDefinitionSearcher(context);
        }

        public static DefinitionSearcher<AdsImageDef> newImageSearcher(final Definition context) {
            return new ImageSearcher(context);
        }
    }

    private static class ImageSearcher extends DefinitionSearcher<AdsImageDef> {

        private final AdsModule module;

        public ImageSearcher(final Definition context) {
            this(context.getModule());
        }

        public ImageSearcher(final Module context) {
            super(context);
            this.module = context instanceof AdsModule ? (AdsModule) context : null;
        }

        @Override
        public AdsImageDef findInsideById(final Id id) {
            return module == null ? null : module.getImages().findById(id);
        }

        @Override
        public DefinitionSearcher<AdsImageDef> findSearcher(final Module module) {
            if (module instanceof AdsModule) {
                return new ImageSearcher((AdsModule) module);
            } else {
                return null;
            }
        }
    }
}
