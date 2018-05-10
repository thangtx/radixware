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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.TagTranslatorFactory;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ISqlTag;
import org.radixware.kernel.common.utils.Utils;

/**
 * SQML Translator.
 *
 */
public class SqmlTranslator {
    protected static final TagPostprocessor EMPTY_POSTPROCESSOR = new TagPostprocessor(){
                                                @Override
                                                public String postprocessTag(final String tagContent, final ISqlTag tag) {
                                                    return tagContent != null ? tagContent : "";
                                                }
                                            };
    
    protected final TagPostprocessor postprocessor;

    /**
     * Use SqmlTranslator.Factory.newInstance();
     */
    protected SqmlTranslator() {
        this(EMPTY_POSTPROCESSOR);
    }

    protected SqmlTranslator(final TagPostprocessor postProcessor) {
        this.postprocessor = postProcessor;
    }
    
    protected TagTranslatorFactory getTagTranslatorFactory(IProblemHandler problemHandler) {
        return SqmlTagTranslatorFactory.Factory.newInstance(problemHandler);
    }

    private static boolean isPossibleToActualize(Scml.Tag tag) {
        return ((tag instanceof ISqlTag) && (tag.getDefinition() instanceof DdsDefinition));
    }

    private class CheckProcessor extends SqmlProcessor {

        private final IProblemHandler problemHandler;

        public CheckProcessor(IProblemHandler problemHandler) {
            this.problemHandler = problemHandler;
        }

        private void error(final Scml.Tag tag, final String message) {
            final RadixProblem problem = RadixProblem.Factory.newError(tag, message);
            problemHandler.accept(problem);
        }

        @Override
        protected void processTag(Scml.Tag tag) {
            final TagTranslatorFactory tagTranslatorFactory = getTagTranslatorFactory(problemHandler);
            final ITagTranslator tagTranslator = tagTranslatorFactory.findTagTranslator(tag);
            if (tagTranslator != null) {
                final boolean isPossibleToActualize = isPossibleToActualize(tag);
                final CodePrinter cp;
                if (isPossibleToActualize) {
                    cp = CodePrinter.Factory.newSqlPrinter();
                } else {
                    cp = CodePrinter.Factory.newNullPrinter();
                }

                try {
                    tagTranslator.translate(tag, cp);

                    // check for actualization.
                    if (isPossibleToActualize) {
                        final ISqlTag sqlTag = (ISqlTag) tag;
                        final String newSql = cp.toString();
                        final String oldSql = sqlTag.getSql();
                        if (!Utils.equals(oldSql, newSql)) {
                            error(tag, "Tag is out of date, reopen it in editor to actualize.");
                        }
                    }
                } catch (RadixError cause) {
                    error(tag, "Unable to translate tag: " + cause.getMessage());
                }
            } else {
                error(tag, "Illegal tag type: " + tag.getClass().getSimpleName());
            }
        }

        @Override
        protected void processText(Scml.Text text) {
            // NOTHING
        }
    }

    public final void check(final Sqml sqml, final IProblemHandler problemHandler) {
        final CheckProcessor checkProcessor = new CheckProcessor(problemHandler);
        checkProcessor.process(sqml);
    }

    private void translate(final Sqml.Tag tag, final CodePrinter cp) {
        if (isPossibleToActualize(tag)) {
            final String sql = ((ISqlTag) tag).getSql();
            
            if (sql != null && !sql.isEmpty()) {
                cp.print(postprocessor.postprocessTag(sql,(ISqlTag)tag));
                return;
            }
        }

        final TagTranslatorFactory tagTranslatorFactory = getTagTranslatorFactory(new IProblemHandler() {
            @Override
            public void accept(RadixProblem problem) {
                if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                    throw new RadixError(problem.getMessage());
                }
            }
        });
        final ITagTranslator tagTranslator = tagTranslatorFactory.findTagTranslator(tag);
        if (tagTranslator != null) {
            try {
                tagTranslator.translate(tag, cp);
            } catch (RadixError cause) {
                cp.printError();
            }
        } else {
            cp.printError();
        }
    }

    public final void translate(final Sqml sqml, final CodePrinter cp) {
        for (Sqml.Item item : sqml.getItems()) {
            if (item instanceof Sqml.Tag) {
                final Sqml.Tag tag = (Sqml.Tag) item;
                translate(tag, cp);
            } else {
                final Scml.Text text = (Scml.Text) item;
                cp.print(text.getText());
            }
        }
    }

    private void actualize(Sqml.Tag tag) {
        if (tag instanceof ISqlTag) {
            if (isPossibleToActualize(tag)) {
                final TagTranslatorFactory tagTranslatorFactory = getTagTranslatorFactory(new IProblemHandler() {
                    @Override
                    public void accept(RadixProblem problem) {
                        if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                            throw new RadixError(problem.getMessage());
                        }
                    }
                });
                final ITagTranslator tagTranslator = tagTranslatorFactory.findTagTranslator(tag);
                if (tagTranslator != null) {
                    final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
                    try {
                        tagTranslator.translate(tag, cp);
                        final ISqlTag sqlTag = (ISqlTag) tag;
                        final String sql = cp.toString();
                        sqlTag.setSql(sql);
                    } catch (RadixError ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        // NOTHING - is is possible to actualize only corrected tags.
                    }
                }
            } else {
                final ISqlTag sqlTag = (ISqlTag) tag;
                sqlTag.setSql(null);
            }
        }
    }

    public final void actualize(final Sqml sqml) {
        // assert !sqml.isReadOnly(); // commenter for audit triggers

        for (Sqml.Item item : sqml.getItems()) {
            if (item instanceof Sqml.Tag) {
                final Sqml.Tag tag = (Sqml.Tag) item;
                actualize(tag);
            }
        }
    }

    public interface TagPostprocessor {
        String postprocessTag(String tagContent, ISqlTag tag);
    }
    
    public static class Factory {

        private Factory() {
        }

        public static SqmlTranslator newInstance() {
            return new SqmlTranslator();
        }

        public static SqmlTranslator newInstance(final TagPostprocessor postProcessor) {
            return new SqmlTranslator(postProcessor);
        }
    }

}
