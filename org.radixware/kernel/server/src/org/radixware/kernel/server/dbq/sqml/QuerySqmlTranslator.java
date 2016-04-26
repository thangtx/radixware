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

package org.radixware.kernel.server.dbq.sqml;

import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.scml.TagTranslatorFactory;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.SqmlPreprocessor;
import org.radixware.kernel.common.sqml.translate.SqmlProcessor;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.common.repository.DbConfiguration;


public class QuerySqmlTranslator {

    public static enum EMode {

        QUERY_TREE_CONSTRUCTION,
        SQL_CONSTRUCTION
    }
    private final EMode mode;
    private final SqlBuilder queryBuilder;

    protected QuerySqmlTranslator(final SqlBuilder queryBuilder, final EMode mode) {
        super();
        this.mode = mode;
        this.queryBuilder = queryBuilder;
    }

    private TagTranslatorFactory getTagTranslatorFactory() {
        return new SqmlTagTranslatorFactory(queryBuilder, mode);
    }

    public static class Factory {

        private Factory() {
        }

        public static QuerySqmlTranslator newInstance(final SqlBuilder queryBuilder, final EMode mode) {
            return new QuerySqmlTranslator(queryBuilder, mode);
        }
    }

    private static final class TranslateProcessor extends SqmlProcessor {

        private final CodePrinter cp;
        final TagTranslatorFactory tagTranslatorFactory;

        public TranslateProcessor(final CodePrinter cp, final TagTranslatorFactory tagTranslatorFactory) {
            this.cp = cp;
            this.tagTranslatorFactory = tagTranslatorFactory;
        }

        @Override
        protected void processTag(final Tag tag) {
            final ITagTranslator<Tag> translator = tagTranslatorFactory.findTagTranslator(tag);
            translator.translate(tag, cp);
        }

        @Override
        protected void processText(final Text text) {
            cp.print(text.getText());
        }
    }

    public final void translate(final Sqml sqml, final CodePrinter cp, final DbConfiguration dbConfiguration) {
        final TranslateProcessor translateProcessor = new TranslateProcessor(cp, getTagTranslatorFactory());
        translateProcessor.process(preprocess(sqml, dbConfiguration));
    }

    public static Sqml preprocess(final Sqml sqml, final DbConfiguration dbConfiguration) {
        final Sqml preprocessedSqml = new SqmlPreprocessor().preprocess(sqml, new PreprocessorConfigByDbConfiguration(dbConfiguration));
        preprocessedSqml.setEnvironment(sqml.getEnvironment());
        return preprocessedSqml;
    }
}
