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

package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.sqml.translate.SqmlProcessor;
import org.radixware.kernel.common.sqml.translate.SqmlTagTranslatorFactory;
import org.radixware.kernel.common.types.Id;


abstract class AdsSqlClassSourceProcessor extends SqmlProcessor {

    private final CodePrinter cp;

    public AdsSqlClassSourceProcessor(CodePrinter cp) {
        this.cp = cp;
    }

    protected abstract void processParam(AdsDynamicPropertyDef param, final Id pkParamPropId, EParamDirection direction, boolean isLiteral);

    protected void processSqlTag(Tag tag) {
    }
    
    protected void startIfBranch() {
    }
    
    protected void endIfBranch() {
    }

    @Override
    protected void processTag(Tag tag) {
        if (tag instanceof ParameterTag) {
            final ParameterTag paramTag = (ParameterTag) tag;
            final IParameterDef iParam = paramTag.findParameter();
            final AdsDynamicPropertyDef propertyAsParameter = (iParam != null ? (AdsDynamicPropertyDef) iParam.getDefinition() : null);
            if (propertyAsParameter != null) {
                final EParamDirection direction = (AdsParameterPropertyDef.canBeOutput(propertyAsParameter) ? paramTag.getDirection() : EParamDirection.IN);
                final boolean isLiteral = (AdsParameterPropertyDef.canBeLiteral(propertyAsParameter) ? paramTag.isLiteral() : false);
                final Id propId = paramTag.getPropId();
                processParam(propertyAsParameter, propId, direction, isLiteral);
            } else {
                cp.printError();
            }
        } else if (tag instanceof IfParamTag || tag instanceof TargetDbPreprocessorTag || tag instanceof ElseIfTag || tag instanceof EndIfTag) {
            final SqmlTagTranslatorFactory tagTranslatorFactory = SqmlTagTranslatorFactory.Factory.newInstance(new IProblemHandler() {

                @Override
                public void accept(RadixProblem problem) {
                    if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                        throw new RadixError(problem.getMessage());
                    }
                }
            });
            final ITagTranslator<Scml.Tag> tagTranslator = tagTranslatorFactory.findTagTranslator(tag);
            if (tagTranslator != null) {
                try {
                    tagTranslator.translate(tag, cp);
                    if (tag instanceof ElseIfTag || tag instanceof EndIfTag) {
                        endIfBranch();
                    }
                    if (tag instanceof IfParamTag || tag instanceof TargetDbPreprocessorTag || tag instanceof ElseIfTag) {
                        startIfBranch();
                    }
                } catch (RadixError cause) {
                    cp.printError();
                }
            } else {
                cp.printError();
            }
            cp.print('\n');
        } else {
            processSqlTag(tag);
        }
    }
}
