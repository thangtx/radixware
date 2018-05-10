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

package org.radixware.kernel.common.check;

import org.radixware.kernel.common.defs.DefinitionPath;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 * Factory for general problem annotations.
 *
 */
public class ProblemAnnotationFactory {

    private ProblemAnnotationFactory() {
    }

    public static class PathAnnotation implements RadixProblem.IAnnotation {

        private final DefinitionPath path;

        private PathAnnotation(final DefinitionPath path) {
            this.path = path;
        }

        public DefinitionPath getPath() {
            return path;
        }
    }

    public static class TextPositionAnnotation implements RadixProblem.IAnnotation {

        public final int line;
        public final int column;

        public TextPositionAnnotation(int line, int column) {
            this.line = line;
            this.column = column;
        }
    }

    public static final PathAnnotation newPathAnnotation(final DefinitionPath path) {
        return new PathAnnotation(path);
    }

    private static class EmptyAnnotation implements RadixProblem.IAnnotation {

        private EmptyAnnotation() {
        }
        private static final EmptyAnnotation INSTANCE = new EmptyAnnotation();
    }

    public static final RadixProblem.IAnnotation newEmptyAnnotation() {
        return EmptyAnnotation.INSTANCE;
    }

    public static class SpellingAnnotation implements RadixProblem.IAnnotation {

        private final String word;
        private final EIsoLanguage language;

        private SpellingAnnotation(String word, EIsoLanguage language) {
            this.language = language;
            this.word = word;
        }

        public String getWord() {
            return word;
        }

        public EIsoLanguage getLanguage() {
            return language;
        }
    }

    public static SpellingAnnotation newSpellingAnnotation(String word, EIsoLanguage language) {
        return new SpellingAnnotation(word, language);
    }
    
    public static class XmlDocumentationAnnotation implements RadixProblem.IAnnotation {
        
        private final String nodeXpath;

        private XmlDocumentationAnnotation(String nodeXpath) {
            this.nodeXpath = nodeXpath;
        }

        public String getNodeXpath() {
            return nodeXpath;
        }        
    }
    
    public static XmlDocumentationAnnotation newXmlDocumentationAnnotation(String nodeXpath) {
        return new XmlDocumentationAnnotation(nodeXpath);
    }
}
