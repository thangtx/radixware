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

package org.radixware.kernel.designer.common.dialogs.jmlnb;

import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;


public class JmlIndentTask implements IndentTask {

    private final Context context;
    private static final Pattern CLOSING_BRACE_PATTERN = Pattern.compile("\\s*}");
    private static final Pattern IF_EXPR_PATTERN = Pattern.compile("\\s*if\\s*\\(.*\\)\\s*");

    public JmlIndentTask(Context context) {
        this.context = context;
    }

    @Override
    public void reindent() throws BadLocationException {
        final String text = context.document().getText(0, context.document().getLength());
        final int currentLineStart = context.lineStartOffset(context.caretOffset());
        if (currentLineStart == 0) {
            context.modifyIndent(0, 0);
            return;
        }
        final int prevLineStart = context.lineStartOffset(currentLineStart - 1);
        final int prevLineIndent = context.lineIndent(prevLineStart);

        if (isClosingBrace(context)) {
            final int blockStartOffset = getBlockStartOffset(text, context.caretOffset());
            if (blockStartOffset >= 0) {
                final int blockLineStartOffset = context.lineStartOffset(blockStartOffset);
                final int blockStartIndent = context.lineIndent(blockLineStartOffset);
                context.modifyIndent(currentLineStart, blockStartIndent);
                return;
            }
        }

        if (matchIf(context.document(), prevLineStart) || isNewBlock(text, context.caretOffset()) && (context.caretOffset() < text.length()) && text.charAt(context.caretOffset()) != '}') {
            context.modifyIndent(currentLineStart, prevLineIndent + IndentUtils.indentLevelSize(context.document()));
            return;
        }

        final int prevSingleIfLineOffset = prevSingleIfLineOffset(context);
        if (prevSingleIfLineOffset >= 0) {
            context.modifyIndent(currentLineStart, context.lineIndent(prevSingleIfLineOffset));
            return;
        }

        context.modifyIndent(currentLineStart, prevLineIndent);
    }

    private boolean isClosingBrace(final Context context) {
        final String lineText = getCurrentLine(context);
        if (lineText != null && CLOSING_BRACE_PATTERN.matcher(lineText).matches()) {
            return true;
        }
        return false;
    }

    private int prevSingleIfLineOffset(final Context context) {
        int prevLineStart = -1;
        int prevPrevLineStart = -1;
        int offs = context.caretOffset() -1;
        try {
            final String text = context.document().getText(0, context.document().getLength());
            while (offs >= 0) {
                if (text.charAt(offs) == '\n') {
                    if (prevPrevLineStart != -1) {
                        break;
                    }
                    if (prevLineStart != -1) {
                        prevPrevLineStart = offs;
                    } else {
                        prevLineStart = offs;
                    }
                } else {
                    if (prevPrevLineStart != -1) {
                        prevPrevLineStart--;
                    } else if (prevLineStart != -1) {
                        prevLineStart--;
                    }
                    
                }
                offs--;
            }
            if (prevPrevLineStart == -1) {
                return -1;
            }
            if (matchIf(context.document(), prevPrevLineStart)) {
                return prevPrevLineStart;
            } else {
                return -1;
            }
        } catch (BadLocationException ex) {
            return -1;
        }
    }

    private String getCurrentLine(final Context context) {
        return getLineAtOffset(context.document(), context.caretOffset());
    }

    private String getLineAtOffset(final Document doc, final int offset) {
        if (offset >= 0 && offset < doc.getLength()) {
            final Element lineElement = DocumentUtilities.getParagraphElement(doc, offset);
            try {
                return doc.getText(
                        lineElement.getStartOffset(), lineElement.getEndOffset() - lineElement.getStartOffset() - 1);
            } catch (BadLocationException ex) {
                return null;
            }
        }
        return null;
    }

    private int getBlockStartOffset(final String str, int offset) {
        offset = offset - 1;
        while (offset >= 0 && str.charAt(offset) != '{') {
            offset--;
        }
        return offset;
    }

    private boolean isNewBlock(String str, int offset) {
        boolean isWhitespace = true;
        offset = offset - 1;
        boolean wasNewLine = false;
        while (offset >= 0 && isWhitespace) {
            char c = str.charAt(offset);
            if (wasNewLine) {
                if (c == '{') {
                    return true;
                }
                if (c == '\n') {
                    //no new block at prev line
                    return false;
                }
            }
            if (c == '\n') {
                wasNewLine = true;
            }
            if (!Character.isWhitespace(c)) {
                isWhitespace = false;
            }
            offset--;
        }
        return false;
    }

    private boolean matchIf(final Document doc, final int offset) {
        final String lineToMatch = getLineAtOffset(doc, offset);
        if (lineToMatch != null) {
            return IF_EXPR_PATTERN.matcher(lineToMatch).matches();
        }
        return false;
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }

    @MimeRegistration(mimeType = "text/x-jml", service = IndentTask.Factory.class)
    public static class JmlIndentTaskFactory implements IndentTask.Factory {

        @Override
        public IndentTask createTask(Context context) {
            return new JmlIndentTask(context);
        }
    }
}
