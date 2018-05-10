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
package org.radixware.kernel.common.compiler.formatter;

import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextStore;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class JmlCodeFormatter {

    private final CodeFormatter codeFormatter;

    public JmlCodeFormatter() {
        @SuppressWarnings("unchecked")
        Map<String, String> options = DefaultCodeFormatterConstants.getEclipse21Settings();

        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);

        options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_JOIN_WRAPPED_LINES, DefaultCodeFormatterConstants.FALSE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "999");

        // change the option to wrap each enum constant on a new line        
        options.put(
                DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
                DefaultCodeFormatterConstants.createAlignmentValue(
                        true,
                        DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
                        DefaultCodeFormatterConstants.INDENT_ON_COLUMN));

        codeFormatter = ToolFactory.createCodeFormatter(options);
    }

    public void format(ITextStore textStore) throws MalformedTreeException, BadLocationException {
        format(new JmlDocument(textStore));
    }

    public void format(IDocument document) throws MalformedTreeException, BadLocationException {
        final String source = document.get();
        final TextEdit edit = codeFormatter.format(
                CodeFormatter.K_STATEMENTS,
                source,
                0,
                source.length(),
                0,
                System.getProperty("line.separator")
        );
        if (edit != null) {
            edit.apply(document);
        }
    }

    private class JmlDocument extends AbstractDocument {

        public JmlDocument(final ITextStore editorStore) {
            super();
            setTextStore(editorStore);
            setLineTracker(new DefaultLineTracker());
            getTracker().set(editorStore.get(0, editorStore.getLength()));
            completeInitialization();
        }
    }
}
