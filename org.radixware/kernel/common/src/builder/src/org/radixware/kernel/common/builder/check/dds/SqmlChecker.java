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

package org.radixware.kernel.common.builder.check.dds;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.EntityRefParameterTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * SQML checker
 *
 */
@RadixObjectCheckerRegistration
public class SqmlChecker<T extends Sqml> extends RadixObjectChecker<T> {

    public SqmlChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Sqml.class;
    }

    private void checkPreprocessor(T sqml, IProblemHandler problemHandler) {

        final CommentsAnalizer commentsAnalizer = CommentsAnalizer.Factory.newSqlCommentsAnalizer();

        Stack<Sqml.Item> ifTags = new Stack<>();
        for (Sqml.Item item : sqml.getItems()) {

            if (item instanceof Scml.Text) {
                String text = ((Scml.Text) item).getText();
                commentsAnalizer.process(text);
            } else {
                if (!commentsAnalizer.isInComment()) { // ignore tags in comments
                    if (item instanceof IfParamTag || item instanceof TargetDbPreprocessorTag) {
                        if (item instanceof TargetDbPreprocessorTag) {
                            checkTargetDbPreprocessorTag((TargetDbPreprocessorTag) item, problemHandler);
                        }
                        ifTags.push(item);
                    } else if (item instanceof ElseIfTag) {
                        if (ifTags.isEmpty()) {
                            error(item, problemHandler, "#ELSE tag not opened by #IF tag.");
                            break;
                        }
                    } else if (item instanceof EndIfTag) {
                        if (ifTags.isEmpty()) {
                            error(item, problemHandler, "#END tag not opened by #IF tag.");
                            break;
                        } else {
                            ifTags.pop();
                        }
                    }
                }
            }
        }
        if (!ifTags.isEmpty()) {
            error(ifTags.peek(), problemHandler, "#IF tag not closed by #END tag.");
        }
    }

    private void checkTargetDbPreprocessorTag(final TargetDbPreprocessorTag tag, final IProblemHandler ph) {
        if (tag == null) {
            return;
        }
        if (tag.getDbTypeName() == null || tag.getDbTypeName().isEmpty()) {
            error(tag, ph, "Database Type is not defined");
            return;
        }
        boolean found = false;
        for (Layer.TargetDatabase availableTarget : tag.getLayer().getTargetDatabases()) {
            if (tag.getDbTypeName().equals(availableTarget.getType())) {
                found = true;
                break;
            }
        }
        if (!found) {
            error(tag, ph, "Unsupported database type '" + tag.getDbTypeName() + "'");
            return;
        }
        if (tag.isCheckVersion()) {
            if (tag.getDbVersion() == null || tag.getDbVersion().isEmpty()) {
                error(tag, ph, "Database Version is not defined");
            }
            try {
                final BigDecimal tagVersion = new BigDecimal(tag.getDbVersion());
                for (Layer.TargetDatabase targetDb : tag.getLayer().getTargetDatabases()) {
                    if (targetDb.getType().equals(tag.getDbTypeName())) {
                        if (tagVersion.compareTo(targetDb.getMinVersion()) < 0) {
                            error(tag, ph, "Database version '" + tag.getDbVersion() + "' is less then minimal version ('" + targetDb.getMinVersion() + "')");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                error(tag, ph, "'" + tag.getDbVersion() + "' is not a valid version");
                return;
            }

        }
        final Set<String> allOptions = new HashSet<>();
        Layer.HierarchyWalker.walk(tag.getLayer(), new Layer.HierarchyWalker.Acceptor<T>() {
            @Override
            public void accept(HierarchyWalker.Controller<T> controller, Layer baseLayer) {
                if (baseLayer.getTargetDatabases() != null) {
                    for (Layer.TargetDatabase targetDb : baseLayer.getTargetDatabases()) {
                        if (tag.getDbTypeName().equals(targetDb.getType())) {
                            for (Layer.DatabaseOption baseOpt : targetDb.getOptions()) {
                                allOptions.add(baseOpt.getQualifiedName());
                            }
                        }
                    }
                }
            }
        });
        if (tag.isCheckOptions() && tag.getDbOptions() != null) {
            for (DbOptionValue dbOpt : tag.getDbOptions()) {
                if (!allOptions.contains(dbOpt.getOptionName())) {
                    error(tag, ph, "Option '" + dbOpt.getOptionName() + "' is not exist");
                    return;
                }
            }
        }
    }

    private void checkParameters(T sqml, IProblemHandler problemHandler) {
        for (Sqml.Item item : sqml.getItems()) {
            if (item instanceof ParameterTag) {
                final ParameterTag paramTag = (ParameterTag) item;
                final boolean isLiteral = paramTag.isLiteral();
                final boolean isOutput = paramTag.getDirection() != EParamDirection.IN;
                final Id propId = paramTag.getPropId();
                if (isLiteral || isOutput || propId != null) {
                    final IParameterDef iParam = paramTag.findParameter();
                    if (iParam != null && iParam.getDefinition() instanceof AdsDynamicPropertyDef) {
                        final AdsDynamicPropertyDef propertyAsParameter = (AdsDynamicPropertyDef) iParam.getDefinition();
                        if (isLiteral && !AdsParameterPropertyDef.canBeLiteral(propertyAsParameter)) {
                            error(paramTag, problemHandler, "Parameter '" + propertyAsParameter.getName() + "' can't be literal");
                        }
                        if (isOutput && !AdsParameterPropertyDef.canBeOutput(propertyAsParameter)) {
                            error(paramTag, problemHandler, "Parameter '" + propertyAsParameter.getName() + "' can't be output");
                        }
                        if (propId != null) {
                            final AdsEntityObjectClassDef entity = AdsParameterPropertyDef.findEntity(propertyAsParameter);
                            if (entity != null) {
                                final AdsPropertyDef prop = entity.getProperties().findById(propId, EScope.ALL).get();
                                if (prop != null) {
                                    if (!AdsVisitorProviders.newPropertyForParameterTagFilter(entity).isTarget(prop)) {
                                        error(paramTag, problemHandler, "Illegal property used in parameter tag: '" + prop.getQualifiedName() + "'");
                                    }
                                } else {
                                    error(paramTag, problemHandler, "Property not found: " + entity.getQualifiedName() + ".#" + propId.toString());
                                }
                            }
                        }
                    }
                }
            }

            if (item instanceof EntityRefParameterTag) {
                final EntityRefParameterTag tag = (EntityRefParameterTag) item;
                final IParameterDef paramDef = tag.findParameter();
                final AdsFilterDef.Parameter filterParamDef = (Parameter) paramDef.getDefinition();
                if (filterParamDef != null) {
                    if (filterParamDef.getType().getTypeId() == EValType.PARENT_REF || filterParamDef.getType().getTypeId() == EValType.OBJECT) {
                        final AdsType type = filterParamDef.getType().resolve(filterParamDef).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(filterParamDef, problemHandler));
                        if (type instanceof EntityObjectType) {
                            if (!Utils.equalsNotNull(((EntityObjectType) type).getSourceEntityId(), tag.getReferencedTableId())) {
                                error(tag, problemHandler, "EntityID from parameter and ReferencedTableID from tag are not the same");
                            }
                        } else {
                            error(tag, problemHandler, "Can not obtain Entity id from parameter");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void check(T sqml, IProblemHandler problemHandler) {
        final SqmlTranslator sqmlt = SqmlTranslator.Factory.newInstance();
        sqmlt.check(sqml, problemHandler);

        checkPreprocessor(sqml, problemHandler);
        checkParameters(sqml, problemHandler);
    }
}
