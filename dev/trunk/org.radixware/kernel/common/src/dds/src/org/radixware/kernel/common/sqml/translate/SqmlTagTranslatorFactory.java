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

package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.TagTranslatorFactory;
import org.radixware.kernel.common.sqml.tags.*;

/**
 * SQML tag translators factory.
 * Pattern: Factory Method.
 */
public class SqmlTagTranslatorFactory implements TagTranslatorFactory {

    private final IProblemHandler problemHandler;

    protected SqmlTagTranslatorFactory(IProblemHandler problemHandler) {
        this.problemHandler = problemHandler;
    }

    @Override
    public <T extends Scml.Tag> ITagTranslator<T> findTagTranslator(T tag) {
        if (tag instanceof ConstValueTag) {
            return new ConstValueTagTranslator();
        } else if (tag instanceof DbFuncCallTag) {
            return new DbFuncCallTagTranslator(problemHandler);
        } else if (tag instanceof IfParamTag) {
            return new IfParamTagTranslator();
        } else if (tag instanceof ElseIfTag) {
            return new ElseIfTagTranslator();
        } else if (tag instanceof EndIfTag) {
            return new EndIfTagTranslator();
        } else if (tag instanceof IdTag) {
            return new IdTagTranslator(problemHandler);
        } else if (tag instanceof DataTag) {
            return new DataTagTranslator(problemHandler);
        } else if (tag instanceof DbNameTag) {
            return new DbNameTagTranslator(problemHandler);
        } else if (tag instanceof IndexDbNameTag) {
            return new IndexDbNameTagTranslator(problemHandler);
        } else if (tag instanceof JoinTag) {
            return new JoinTagTranslator();
        } else if (tag instanceof ParameterTag) {
            return new ParameterTagTranslator();
        } else if (tag instanceof PropSqlNameTag) {
            return new PropSqlNameTagTranslator(problemHandler);
        } else if (tag instanceof SequenceDbNameTag) {
            return new SequenceDbNameTagTranslator();
        } else if (tag instanceof TableSqlNameTag) {
            return new TableSqlNameTagTranslator(problemHandler);
        } else if (tag instanceof ThisTableIdTag) {
            return new ThisTableIdTagTranslator(problemHandler);
        } else if (tag instanceof ThisTableSqlNameTag) {
            return new ThisTableSqlNameTagTranslator(problemHandler);
        } else if (tag instanceof TypifiedValueTag) {
            return new TypifiedValueTagTranslator();
        } else if (tag instanceof EntityRefParameterTag) {
            return new EntityRefParameterTagTranslator();
        } else if (tag instanceof ThisTableRefTag) {
            return new ThisTableRefTagTranslator(problemHandler);
        } else if (tag instanceof XPathTag) {
            return new XPathTagTranslator();
        } else if (tag instanceof TaskTag) {
            return new TaskTagTranslator();
        }else if (tag instanceof TargetDbPreprocessorTag) {
            return new TargetDbPreprocessorTagTranslator();
        } else {
            return null;
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static SqmlTagTranslatorFactory newInstance(IProblemHandler problemHandler) {
            return new SqmlTagTranslatorFactory(problemHandler);
        }
    }
}
