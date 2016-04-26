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

package org.radixware.kernel.explorer.editors.scmleditor;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QSyntaxHighlighter;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextDocument;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.editors.scml.IScmlHighliter;


public class Highlighter extends QSyntaxHighlighter{
    
    
    private List<IScmlHighliter> highlightingRules;
    //private QRegExp commentMultiLineStartExpression;
    //private QRegExp commentMultiLineEndExpression;
    //private QTextCharFormat multiLineCommentFormat;
    //private int keywordPatternsNumb;
    private final TagProcessor converter;
    //private final String keyword_path;//="org.radixware.explorer/S_E/SYNTAX_G/RESERVED_WORDS";
    //private final String separators_path;//="org.radixware.explorer/S_E/SYNTAX_G/SEPARATORS";
    //private final String operators_path;//="org.radixware.explorer/S_E/SYNTAX_G/SYMBOLS";
    //private final String comment_path;//="org.radixware.explorer/S_E/SYNTAX_G/COMMENTS";
    //private final String strings_path;//="org.radixware.explorer/S_E/SYNTAX_G/STRINGS";
    //private final String numbers_path;// ="org.radixware.explorer/S_E/SYNTAX_G/NUMBERS";
    //private final ClientSettings configStore;

    
    public Highlighter(/*IClientEnvironment environment,*/ QTextDocument parent,
            final TagProcessor converter, List<IScmlHighliter> rules) {
        super(parent);
        //configStore = environment.getConfigStore();
        //keyword_path = settingName + SettingNames.SourceCode.Syntax.RESERVED_WORDS;
        //separators_path = settingName + SettingNames.SourceCode.Syntax.SEPARATORS;
        //operators_path = settingName + SettingNames.SourceCode.Syntax.SYMBOLS;
        //comment_path = settingName + SettingNames.SourceCode.Syntax.COMMENTS;
        //strings_path = settingName + SettingNames.SourceCode.Syntax.STRINGS;
        //numbers_path = settingName + SettingNames.SourceCode.Syntax.NUMBERS;
        highlightingRules=rules;
        if(highlightingRules==null)
            highlightingRules=new ArrayList<>();
        this.converter = converter;
        //QRegExp reg = new QRegExp();
       // reg.setPattern("/\\*");
        //commentMultiLineStartExpression = reg;

        //QRegExp reg1 = new QRegExp();
        //reg1.setPattern("\\*/");
        //commentMultiLineEndExpression = reg1;
        //highlightingRules = new Vector<HighlightingRule>();

        //fillHighlightingRules(converter instanceof SqmlProcessor);
    }
    
    public void addRule(IScmlHighliter rule){
        highlightingRules.add(rule);
    }
    
    public void removeRule(IScmlHighliter rule){
        highlightingRules.remove(rule);
    }
    
   /* public void updateSettings() {
        for (int i = 0, size = highlightingRules.size(); i < size; i++) {
            highlightingRules.get(i).update();
        }
            setHighlightingRules(highlightingRules.get(i).getPattern()format, highlightingRules.get(i).path);
        //}
        //this.rehighlight();        
    }*/
    
    public void removeAllRules(){
        highlightingRules.clear();
    }

    @Override
    protected void highlightBlock(String text) {
       for (int i = 0, size = highlightingRules.size(); i < size; i++) {           
           IScmlHighliter rule = highlightingRules.get(i);
           QRegExp expression = new QRegExp();
           if(!rule.isMultiline()){                
                expression.setPattern(rule.getPattern());
                int index = expression.indexIn(text);
                while (index >= 0) {
                    int pos = this.currentBlock().position() + index + 1;
                    int length = expression.matchedLength();
                    if ((converter == null) || ((converter != null) && (converter.getTagInfoForCursorMove(pos, false) == null)))
                    {
                        setFormat(index, length, createCharFormat(rule));
                    }
                    index = expression.indexIn(text, index + length);
                }
           }else{
             highliteMultilineRule(text, rule)  ;
           }
        }
        //highliteComment(text, commentMultiLineStartExpression, commentMultiLineEndExpression);
    }
    
    private void highliteMultilineRule(final String text,IScmlHighliter rule) {
        QRegExp commentStartExpression = new QRegExp();
        commentStartExpression.setPattern(rule.getPattern(0));
        
        QRegExp commentEndExpression = new QRegExp();
        commentEndExpression.setPattern(rule.getPattern(1));
        
        setCurrentBlockState(0);
        int startIndex = 0;
        if (previousBlockState() != 1) {
            startIndex = commentStartExpression.indexIn(text);
        }
        int textLen = text.length();
        while (startIndex >= 0) {
            int endIndex = commentEndExpression.indexIn(text, startIndex);
            int commentLength;
            if (endIndex == -1) {
                setCurrentBlockState(1);
                commentLength = textLen - startIndex;
            } else {
                commentLength = endIndex - startIndex + commentEndExpression.matchedLength();
            }
            setFormat(startIndex, commentLength, createCharFormat(rule));
            startIndex = commentStartExpression.indexIn(text, startIndex + commentLength);
        }
    }
    
     private QTextCharFormat createCharFormat(IScmlHighliter rule) {
        QTextCharFormat format=new QTextCharFormat();
        QColor fc = rule.getColor();
        //QColor bc = configStore.readPropertySettings(path).background == null ? null : new QColor(configStore.readPropertySettings(path).background);
        QFont font = rule.getFont();
        format.setForeground(new QBrush(fc));
        format.setUnderlineColor(new QColor(fc));
        //format.setBackground(new QBrush(bc));
        setFont(format, font);
        return format;
    }

    private void setFont(QTextCharFormat charFormat, QFont font) {
        charFormat.setFontCapitalization(font.capitalization());
        charFormat.setFontFamily(font.family());
        charFormat.setFontFixedPitch(font.fixedPitch());
        charFormat.setFontItalic(font.italic());
        charFormat.setFontLetterSpacing(font.letterSpacing());
        charFormat.setFontOverline(font.overline());
        charFormat.setFontPointSize(font.pointSize());
        charFormat.setFontStrikeOut(font.strikeOut());
        charFormat.setFontWeight(font.weight());
        charFormat.setFontWordSpacing(font.wordSpacing());
    }   
    
    
}
