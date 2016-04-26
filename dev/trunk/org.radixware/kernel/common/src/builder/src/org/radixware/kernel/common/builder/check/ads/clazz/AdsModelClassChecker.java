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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.*;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.JavaClassType;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetModelClassDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsModelClassChecker extends AdsClassChecker<AdsModelClassDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsModelClassDef.class;
    }

    @Override
    public void check(AdsModelClassDef clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
        switch (clazz.getClassDefType()) {
            case ENTITY_MODEL:

                AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) clazz).getOwnerEditorPresentation();
                if (epr != null) {
                    ERuntimeEnvironmentType eprEnv = epr.getClientEnvironment();

                    if (eprEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        ERuntimeEnvironmentType env = clazz.getClientEnvironment();
                        if (env != eprEnv) {
                            error(clazz, problemHandler, env.getName() + " model should not be inside of " + eprEnv.getName() + " presentation. Model must be " + eprEnv.getName() + " too");
                        }
                    }
                }

                break;

            case GROUP_MODEL:
                if (clazz.getProperties().getLocal().isEmpty()
                        && clazz.getMethods().getLocal().isEmpty()
                        && clazz.getHeader().isEmpty()
                        && clazz.getBody().isEmpty()
                        && clazz.getInheritance().getInerfaceRefList(EScope.LOCAL).isEmpty()) {
                    if (!clazz.isWarningSuppressed(AdsModelClassDef.Problems.DO_NOT_USE_EMPTY_MODEL)) {
                        problemHandler.accept(RadixProblem.Factory.newWarning(clazz, AdsModelClassDef.Problems.DO_NOT_USE_EMPTY_MODEL));
                    }
                }
                break;
        }

        new CycleDetecter().find(clazz, problemHandler);
    }

    @Override
    protected void checkSuperclassDetails(AdsModelClassDef clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        super.checkSuperclassDetails(clazz, resolvedBaseClass, problemHandler);
        if (clazz instanceof AdsEntityModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsEntityModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.ENTITY_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        } else if (clazz instanceof AdsGroupModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsGroupModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.GROUP_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        } else if (clazz instanceof AdsFormModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsFormModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.FORM_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        } else if (clazz instanceof AdsParagraphModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsParagraphModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.PARAGRAPH_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        } else if (clazz instanceof AdsPropEditorModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsPropEditorModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.PROP_EDITOR_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        } else if (clazz instanceof AdsCustomWidgetModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsCustomWidgetModelClassDef)) {
                if (clazz.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                    checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsCustomWidgetModelClassDef.CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME, problemHandler);
                } else {
                    checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsCustomWidgetModelClassDef.WEB_CUSTOM_WIDGET_MODEL_JAVA_CLASS_NAME, problemHandler);
                }
            }
        } else if (clazz instanceof AdsDialogModelClassDef) {
            if (!(resolvedBaseClass instanceof AdsDialogModelClassDef)) {
                checkTransparenceOfSuperclass(clazz, resolvedBaseClass, AdsModelClassDef.DIALOG_MODEL_JAVA_CLASS_NAME, problemHandler);
            }
        }
    }

    private void checkTransparenceOfSuperclass(AdsClassDef clazz, AdsClassDef superClass, String transparentName, IProblemHandler problemHandler) {
        if (superClass.getTransparence() == null || !superClass.getTransparence().isTransparent() || !superClass.getTransparence().getPublishedName().equals(transparentName)) {
            error(clazz, problemHandler, "Superclass must be publishing of " + transparentName);
        }
    }

    @Override
    protected void nullSuperclassReferenceDetails(AdsModelClassDef clazz, IProblemHandler problemHandler) {
        super.nullSuperclassReferenceDetails(clazz, problemHandler);
        if (clazz instanceof AdsEntityModelClassDef) {
            AdsEntityModelClassDef model = (AdsEntityModelClassDef) clazz;
            if (model.getOwnerEditorPresentation().getBasePresentationId() != null) {
                error(clazz, problemHandler, "Model of base editor presentation of owner presentation is expected to be referenced as superclass");
            } else {
                error(clazz, problemHandler, "Platform model class is expected to be referenced as superclass");
            }
        } else if (clazz instanceof AdsGroupModelClassDef) {
            AdsGroupModelClassDef model = (AdsGroupModelClassDef) clazz;
            if (model.getOwnerSelectorPresentation().getBasePresentationId() != null) {
                error(clazz, problemHandler, "Model of base editor presentation of owner presentation is expected to be referenced as superclass");
            } else {
                error(clazz, problemHandler, "Platform model class is expected to be referenced as superclass");
            }
        } else if (clazz instanceof AdsFormModelClassDef) {
            AdsFormModelClassDef model = (AdsFormModelClassDef) clazz;
            if (model.getOwnerClass().getInheritance().getSuperClassRef() != null) {
                error(clazz, problemHandler, "Model of base form of owner form is expected to be referenced as superclass");
            } else {
                error(clazz, problemHandler, "Platform model class is expected to be referenced as superclass");
            }
        }
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(AdsModelClassDef clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);
        if (ref instanceof JavaClassType) {
            warning(clazz, problemHandler, "No publishing of " + ((JavaClassType) ref).getJavaClassName() + " is found ");
        }
    }

    static class CycleDetecter {

        private final Set<Id> used = new HashSet<>();
        private final List<List<Id>> cycles = new ArrayList<>();

        private AdsPropertyPresentationPropertyDef getPropperty(Id id, AdsClassDef cls) {
            return (AdsPropertyPresentationPropertyDef) cls.getProperties().findById(id, EScope.ALL).get();
        }

        private void findCiclicDependencies(AdsClassDef cls, final AdsPropertyPresentationPropertyDef prop, Set<Id> way) {

            for (final Id id : prop.getDependents().getDependents(EScope.ALL, AdsPropertyPresentationPropertyDef.PROPERTY_FILTER)) {

                if (way.contains(id)) {
                    List<Id> cycle = new ArrayList<>(way);
                    cycle = cycle.subList(cycle.indexOf(id), cycle.size());
                    cycles.add(cycle);
                } else if (!used.contains(id)) {
                    used.add(id);

                    final AdsPropertyPresentationPropertyDef propperty = getPropperty(id, cls);
                    if (propperty != null) {
                        way.add(id);
                        findCiclicDependencies(cls, propperty, way);
                        way.remove(id);
                    }
                }
            }
        }

        void find(AdsModelClassDef clazz, IProblemHandler problemHandler) {
            final List<AdsPropertyDef> all = clazz.getProperties().getAll(EScope.LOCAL, new IFilter<AdsPropertyDef>() {
                @Override
                public boolean isTarget(AdsPropertyDef radixObject) {
                    return radixObject instanceof AdsPropertyPresentationPropertyDef;
                }
            });

            for (final AdsPropertyDef prop : all) {
                findCiclicDependencies(clazz, (AdsPropertyPresentationPropertyDef) prop, new LinkedHashSet<Id>());
            }

            for (final List<Id> list : cycles) {
                error(clazz, problemHandler, "Dependency cycle detected: " + getCycleMessage(clazz, list));
            }
        }

        private String getCycleMessage(AdsModelClassDef clazz, List<Id> cycle) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (final Id id : cycle) {
                if (first) {
                    first = false;
                } else {
                    sb.append("->");
                }
                final SearchResult<AdsPropertyDef> result = clazz.getProperties().findById(id, EScope.ALL);
                if (!result.isEmpty()) {
                    sb.append(result.get().getQualifiedName(clazz));
                }
            }
            return sb.toString();
        }
    }
}
