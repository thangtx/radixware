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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;



public class Highlighter extends QSyntaxHighlighter {
    
    private static final QColor ERROR_LINE_COLOR = new QColor(255, 168, 168);

    private static class HighlightingRule {

        QRegExp pattern;
        QTextCharFormat format;
        String name;
        String path;

        public HighlightingRule(QRegExp pattern, QTextCharFormat format, String name, String path) {
            this.format = format;
            this.pattern = pattern;
            this.name = name;
            this.path = path;
        }

        private HighlightingRule() {
        }
    }

    private Vector<HighlightingRule> highlightingRules;
    private QRegExp commentMultiLineStartExpression;
    private QRegExp commentMultiLineEndExpression;
    private QTextCharFormat multiLineCommentFormat;
    private int keywordPatternsNumb;
    private final TagProcessor converter;
    private final String keyword_path;//="org.radixware.explorer/S_E/SYNTAX_G/RESERVED_WORDS";
    private final String separators_path;//="org.radixware.explorer/S_E/SYNTAX_G/SEPARATORS";
    private final String operators_path;//="org.radixware.explorer/S_E/SYNTAX_G/SYMBOLS";
    private final String comment_path;//="org.radixware.explorer/S_E/SYNTAX_G/COMMENTS";
    private final String strings_path;//="org.radixware.explorer/S_E/SYNTAX_G/STRINGS";
    private final String numbers_path;// ="org.radixware.explorer/S_E/SYNTAX_G/NUMBERS";
    private final String default_path;
    private final ClientSettings configStore;
    public static final String SEARCH = "SEARCH";   
    
    private final QTextEdit parent;
   
    public Highlighter(final IClientEnvironment environment, final QTextEdit parent, final TagProcessor converter, final String settingName) {
        super(parent.document());
        this.parent=parent;
        configStore = environment.getConfigStore();
        default_path = settingName + SettingNames.SourceCode.Syntax.DEFAULT;
        keyword_path = settingName + SettingNames.SourceCode.Syntax.RESERVED_WORDS;
        separators_path = settingName + SettingNames.SourceCode.Syntax.SEPARATORS;
        operators_path = settingName + SettingNames.SourceCode.Syntax.SYMBOLS;
        comment_path = settingName + SettingNames.SourceCode.Syntax.COMMENTS;
        strings_path = settingName + SettingNames.SourceCode.Syntax.STRINGS;
        numbers_path = settingName + SettingNames.SourceCode.Syntax.NUMBERS;

        this.converter = converter;
        highlightingRules = new Vector<>();

        fillHighlightingRules(converter instanceof SqmlProcessor);
    }

    public void updateSettings() {
        final ExplorerTextOptions textOptions = (ExplorerTextOptions)configStore.readTextOptions(default_path);
        final QColor fc = textOptions.getForeground();
        final QColor bc = textOptions.getBackground();
        final QFont font = textOptions.getQFont();   
        if (bc==null){
            parent.setStyleSheet("color: "+fc.name()+";");
        }else{
            parent.setStyleSheet("color: "+fc.name()+"; background-color: "+bc.name()+";");
        }
        parent.setFont(font);
        parent.update();        
        for (int i = 0, size = highlightingRules.size(); i < size; i++) {
            setHighlightingRules(highlightingRules.get(i).format, highlightingRules.get(i).path);
        }
        this.rehighlight();
    }
    
   /* private void setHighlightingRules(final String path, final int index) {
        setHighlightingRules(highlightingRules.get(index).format, path);
    }*/

    private QTextCharFormat setHighlightingRules(final QTextCharFormat format, final String path) {
        final ExplorerTextOptions textOptions = (ExplorerTextOptions)configStore.readTextOptions(path);
        final QColor fc = textOptions.getForeground();
        final QColor bc = textOptions.getBackground();
        final QFont font = textOptions.getQFont();
        format.setForeground(new QBrush(fc));
        if (bc!=null){
            format.setBackground(new QBrush(bc));
        }
        setFont(format, font);
        return format;
    }

    private void setFont(final QTextCharFormat charFormat,final QFont font) {
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

    private void addToHightlightRule(final String pattern,final  String path, final String settingName) {
        QRegExp reg = new QRegExp();
        reg.setPattern(pattern);
        addToHightlightRule(reg, path, settingName);
    }

    private void addToHightlightRule(final QRegExp reg, final String path, final String settingName) {
        QTextCharFormat format = setHighlightingRules(new QTextCharFormat(), path);
        highlightingRules.add(createHighlighRule(format, reg, settingName, path));
    }

    private void fillHighlightingRules(final boolean isSqml) {
        addToHightlightRule("\\b(\\d+)\\b", numbers_path, SettingNames.SourceCode.Syntax.NUMBERS);
        addToHightlightRule("[;,.\\{\\}\\[\\]()]", separators_path, SettingNames.SourceCode.Syntax.SEPARATORS);
        addToHightlightRule("[=\\:\\+\\-<>\\?\\*\\!%\\&\\|/~]", operators_path, SettingNames.SourceCode.Syntax.SYMBOLS);

        QRegExp reg4 = new QRegExp();
        reg4.setPattern("/\\*");
        commentMultiLineStartExpression = reg4;

        QRegExp reg5 = new QRegExp();
        reg5.setPattern("\\*/");
        commentMultiLineEndExpression = reg5;
        multiLineCommentFormat = setHighlightingRules(new QTextCharFormat(), comment_path);
        final ExplorerTextOptions textOptions = (ExplorerTextOptions)configStore.readTextOptions(comment_path);        
        multiLineCommentFormat.setUnderlineColor(textOptions.getForeground());
        highlightingRules.add(createHighlighRule(multiLineCommentFormat, commentMultiLineEndExpression, SettingNames.SourceCode.Syntax.COMMENTS, comment_path));
    
        /* //строки
        QRegExp reg7= new QRegExp();
        reg7.setPattern("\"[^\"]*\"");
        QTextCharFormat stringsFormat=setHighlightingRules(new QTextCharFormat(), strings_path);
        highlightingRules.add(createHighlighRule(stringsFormat,reg7,SettingNames.SourceCode.Syntax.STRINGS,strings_path));

        QRegExp reg8= new QRegExp();
        reg8.setPattern("\'[^\']*\'");
        highlightingRules.add(createHighlighRule(stringsFormat,reg8,SettingNames.SourceCode.Syntax.STRINGS,strings_path));*/

        if (isSqml) {
            fillHighlightingRulesForSqml();
        } else {
            fillHighlightingRulesForJml();
        }
    }

    private void fillHighlightingRulesForJml() {
        final List<String> keywordPatterns = addJmlKeywordPatterns();
        keywordPatternsNumb = keywordPatterns.size();
        for (int i = 0; i < keywordPatternsNumb; i++) {
            String pattern = keywordPatterns.get(i);
            addToHightlightRule(pattern, keyword_path, SettingNames.SourceCode.Syntax.RESERVED_WORDS);
        }
        
        //строки
        QRegExp stringsExpression = createStringsExpression();
        QTextCharFormat stringsFormat = setHighlightingRules(new QTextCharFormat(), strings_path);
        highlightingRules.add(createHighlighRule(stringsFormat, stringsExpression, SettingNames.SourceCode.Syntax.STRINGS, strings_path));

        //SingleLine Comment
        //addToHightlightRule("//[^\\n]*",comment_path,SettingNames.SourceCode.Syntax.COMMENTS);
        QRegExp reg2 = new QRegExp();
        reg2.setPattern("//[^\\n]*");
        highlightingRules.add(createHighlighRule(multiLineCommentFormat, reg2, SettingNames.SourceCode.Syntax.COMMENTS, comment_path));
       
        QTextCharFormat searchFormat=new QTextCharFormat();
        searchFormat.setBackground(new QBrush(new QColor(255,153,59)));
        searchFormat.setForeground(new QBrush(QColor.black));
        searchRule=new HighlightingRule(null, searchFormat, SEARCH, "");
        //highlightingRules.add(searchRule);
    }
    
    public static QRegExp createStringsExpression() {
        QRegExp regEx = new QRegExp("\"(?:\\\\.|[^\"\\\\])*\"|\'(?:\\\\.|[^\'\\\\])*\'");
        regEx.setMinimal(true);
        return regEx;
    }

    private void fillHighlightingRulesForSqml() {
        List<String> keywordPatterns = addSqmlKeywordPatterns();
        keywordPatternsNumb = keywordPatterns.size();
        for (int i = 0; i < keywordPatternsNumb; i++) {
            String pattern = keywordPatterns.get(i);
            QRegExp regKeyword = new QRegExp();
            regKeyword.setPattern(pattern);
            regKeyword.setCaseSensitivity(CaseSensitivity.CaseInsensitive);
            addToHightlightRule(regKeyword, keyword_path, SettingNames.SourceCode.Syntax.RESERVED_WORDS);
        }
        //строки
        QRegExp reg7 = new QRegExp();
        reg7.setPattern("\"(\\\\.|[^\"\\\\])*\"");
        //reg7.setPattern("\"(\\\"|[^\"])*\"");
        QTextCharFormat stringsFormat = setHighlightingRules(new QTextCharFormat(), strings_path);
        highlightingRules.add(createHighlighRule(stringsFormat, reg7, SettingNames.SourceCode.Syntax.STRINGS, strings_path));

        QRegExp reg8 = new QRegExp();
        reg8.setPattern("\'[^\']*\'");
        highlightingRules.add(createHighlighRule(stringsFormat, reg8, SettingNames.SourceCode.Syntax.STRINGS, strings_path));

        //SingleLine Comment
        //addToHightlightRule("--[^\\n]*",comment_path,SettingNames.SourceCode.Syntax.COMMENTS);
        QRegExp reg5 = new QRegExp();
        reg5.setPattern("--[^\\n]*");
        highlightingRules.add(createHighlighRule(multiLineCommentFormat, reg5, SettingNames.SourceCode.Syntax.COMMENTS, comment_path));

    }
    
    private HighlightingRule searchRule;
    public void addSearchText(final QRegExp getExp){
        searchRule.pattern=getExp;
    }
   
    private HighlightingRule createHighlighRule(final QTextCharFormat textFormat, final QRegExp reg, final String name, final String path) {
        return new HighlightingRule(reg, textFormat, name, path);
    }
    
    private int[] bracketIndex=new int[2];    
    public void clearBracketIndex(){
        setBracketIndex(-1,-1);
    }
    
    public void setBracketIndex(final int index0,final int index1){
        bracketIndex[0]=index0;
        bracketIndex[1]=index1;
    }    

    @Override
    public void highlightBlock(final String text) { 
        stringInfoList.clear();
        for (HighlightingRule rule : highlightingRules) {
            highlingtBlockByRule( rule, text);            
        }
        highliteComment(text, commentMultiLineStartExpression, commentMultiLineEndExpression);
        if(searchRule!=null)
            highlingtBlockByRule( searchRule, text);
    }
    
    private List<StringInfo> stringInfoList=new ArrayList<>();
    private void highlingtBlockByRule(final HighlightingRule rule, final String text){
        if(rule.pattern!=null){
                int index = rule.pattern.indexIn(text);
                while (index >= 0) {
                    int pos = this.currentBlock().position() + index + 1;
                    if(rule.name.equals(SettingNames.SourceCode.Syntax.COMMENTS)){
                        boolean isCommentInString=false;
                        for(StringInfo strInfo:stringInfoList){
                            if(pos<strInfo.getEnd() && pos>strInfo.getStart()){
                                isCommentInString=true;
                                break;
                            }
                        }
                        if(isCommentInString){
                            index = rule.pattern.indexIn(text, index + 1);
                            continue;
                        }
                    }
                    int length = rule.pattern.matchedLength();//format(pos).underlineStyle()==QTextCharFormat.UnderlineStyle.NoUnderline
                    if (length == 0) {
                        break;  //protection vs. cycling when regex is '|'
                    }
                    if((converter == null) || ((converter != null) && 
                        (SEARCH.equals(rule.name) || converter.getTagInfoForCursorMove(pos, false) == null))){                       
                        if(SettingNames.SourceCode.Syntax.SEPARATORS.equals(rule.name) && 
                            bracketIndex!=null && (index==bracketIndex[0] || index==bracketIndex[1])){
                            QTextCharFormat f=rule.format.clone();
                            f.setBackground(new QBrush(QColor.yellow));
                            setFormat(index, length, f);
                        }else{
                            setFormat(index, length, rule.format);
                        }
                    }
                    index = rule.pattern.indexIn(text, index + length);
                    if(rule.name.equals(SettingNames.SourceCode.Syntax.STRINGS)){
                        stringInfoList.add(new StringInfo(pos,length+pos));
                    }
                }
         }
    }
    
    private static class StringInfo{
        private int start=0;
        private int end=0;
        StringInfo(int start,int end){
            this.start=start;
            this.end=end;
        }
        
        int getStart(){
            return start;
        }
        
        int getEnd(){
            return end;
        }
        
    }
    
   /* private void checkOnFindText(String text){
        if(findTextPos!=null && !findTextPos.isEmpty()){
            int startBlockPos = this.currentBlock().position();
            int endBlockPos = startBlockPos+text.length();
            for(FindTextInfo info:findTextPos){
                if(startBlockPos<=info.getStartPos() && endBlockPos>=info.getStartPos()){
                    QTextCharFormat f=format(info.getStartPos()+1).clone();
                    f.setBackground(new QBrush(new QColor(255,153,59)));
                    int length=Math.min(info.getLength(), endBlockPos-info.getStartPos());
                    setFormat(info.getStartPos()-startBlockPos, length, f);
                    
                }
            }
        }
    }*/
    
    private void changeLineColor(QTextCursor tc, QColor color) {
        ArrayList<QTextEdit_ExtraSelection> selections = new ArrayList<>(1);
        QTextEdit_ExtraSelection selection = new QTextEdit_ExtraSelection();
        QTextCharFormat format = selection.format();
        format.setBackground(new QBrush(color));
        format.setProperty(QTextFormat.Property.FullWidthSelection.value(), true);
        selection.setFormat(format);
        tc.clearSelection();
        selection.setCursor(tc);
        selections.add(selection);
        parent.setExtraSelections(selections);
    }
    
    public void hightlightStackLine(final int endOfLinePos) {
        if (0 <= endOfLinePos && endOfLinePos <= parent.toPlainText().length()) {
            QTextCursor tc = parent.textCursor();
            try {
                tc.setPosition(endOfLinePos);
                changeLineColor(tc, ERROR_LINE_COLOR);
                parent.setTextCursor(tc);
            } finally {
                tc.dispose();
            }
        } else {
            converter.environment.messageError(converter.environment.getMessageProvider().translate("JmlEditor", "Given stack trace element not found."));
            parent.setExtraSelections(Collections.<QTextEdit_ExtraSelection>emptyList());
        }
    }

    public void clearLineHighlight() {
        parent.setExtraSelections(Collections.<QTextEdit_ExtraSelection>emptyList());
    }
    
    private int getStartCommentPos(final QRegExp commentStartExpression, String text, int startPos) {
        boolean isCommentInString;
        int posInLine, posInText;
        while ((posInLine = commentStartExpression.indexIn(text, startPos)) != -1) {
            isCommentInString = false;
            posInText = this.currentBlock().position() + posInLine + 1;
            for (StringInfo strInfo : stringInfoList) {
                if (posInText < strInfo.getEnd() && posInText > strInfo.getStart()) {
                    isCommentInString = true;
                    break;
                }
            }
            if (!isCommentInString) {
                return posInLine;
            }
            startPos = posInLine + 1;
        }
        return -1;
    }

    private void highliteComment(final String text, final QRegExp commentStartExpression, final QRegExp commentEndExpression) {
        setCurrentBlockState(0);
        int startIndex = 0;
        if (previousBlockState() != 1) {
            startIndex = getStartCommentPos(commentStartExpression, text, 0);
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
            setFormat(startIndex, commentLength, multiLineCommentFormat);
            startIndex =  getStartCommentPos(commentStartExpression, text, startIndex + commentLength);
        }
    }

    public void setForegroundColor(final QColor color, final String name) {
        if(name.equals(SettingNames.SourceCode.Syntax.DEFAULT)){
           String s=getColorName(parent.styleSheet(), "background-color: ");
           parent.setStyleSheet("color: "+color.name()+";"+s);
        }else{
            for (int i = 0, size = highlightingRules.size(); i < size; i++) {
                if (name.equals(highlightingRules.get(i).name)) {
                    highlightingRules.get(i).format.setForeground(new QBrush(color));
                }
            }
        }
        this.rehighlight();
    }

    public void setBackgroundColor(final QColor color, final String name) {
        if(name.equals(SettingNames.SourceCode.Syntax.DEFAULT)){
            String s=getColorName(parent.styleSheet(), "color: ");
            parent.setStyleSheet("background-color: "+color.name()+";"+s);   
        }else{
            for (int i = 0, size = highlightingRules.size(); i < size; i++) {
                if (name.equals(highlightingRules.get(i).name)) {
                    highlightingRules.get(i).format.setBackground(new QBrush(color));
                }
            }
        }
        this.rehighlight();
    }
    
    private String getColorName(final String s, final String serchText){
       int start=s.indexOf(serchText);
       if(start!=-1){
           int end=s.indexOf(";",start);
           if(end!=-1)
                return s.substring(start,end+1);                
       }
       return "";
    }
    
    public void setFont(final QFont font, final String name) {
         if(name.equals(SettingNames.SourceCode.Syntax.DEFAULT)){
           parent.setFont(new QFont(font));
        }else{
            for (int i = 0, size = highlightingRules.size(); i < size; i++) {
                if (name.equals(highlightingRules.get(i).name)) {
                    setFont(highlightingRules.get(i).format, font);
                }
            }
        }
        this.rehighlight();
    }

    private List<String> addJmlKeywordPatterns() {
        List<String> keywordPatterns = new ArrayList<>();
        
        keywordPatterns.add("\\b(String|Integer|class|const|int|enum|boolean|if|"
                + "else|for|super|while|do|new|this|true|false|return|void|public|"
                + "private|protected|extends|import|abstract|break|byte|case|catch|"
                + "char|continue|default|double|final|finally|float|goto|implements|"
                + "instanceof|interface|long|native|null|package|short|static|switch|"
                + "synchronized|throw|throws|try|transient|volatile)\\b");
        
       /* keywordPatterns.add("\\bString\\b");
        keywordPatterns.add("\\bInteger\\b");

        keywordPatterns.add("\\bclass\\b");
        keywordPatterns.add("\\bconst\\b");
        keywordPatterns.add("\\bint\\b");
        keywordPatterns.add("\\benum\\b");
        keywordPatterns.add("\\bboolean\\b");
        keywordPatterns.add("\\bif\\b");
        keywordPatterns.add("\\belse\\b");
        keywordPatterns.add("\\bfor\\b");
        keywordPatterns.add("\\bsuper\\b");
        keywordPatterns.add("\\bwhile\\b");
        keywordPatterns.add("\\bdo\\b");
        keywordPatterns.add("\\bnew\\b");
        keywordPatterns.add("\\bthis\\b");
        keywordPatterns.add("\\btrue\\b");
        keywordPatterns.add("\\bfalse\\b");
        keywordPatterns.add("\\breturn\\b");
        keywordPatterns.add("\\bvoid\\b");
        keywordPatterns.add("\\bpublic\\b");
        keywordPatterns.add("\\bprivate\\b");
        keywordPatterns.add("\\bprotected\\b");
        keywordPatterns.add("\\bextends\\b");
        keywordPatterns.add("\\bimport\\b");
        keywordPatterns.add("\\babstract\\b");
        keywordPatterns.add("\\bbreak\\b");
        keywordPatterns.add("\\bbyte\\b");
        keywordPatterns.add("\\bcase\\b");
        keywordPatterns.add("\\bcatch\\b");
        keywordPatterns.add("\\bchar\\b");
        keywordPatterns.add("\\bcontinue\\b");
        keywordPatterns.add("\\bdefault\\b");
        keywordPatterns.add("\\bdouble\\b");
        keywordPatterns.add("\\bfinal\\b");
        keywordPatterns.add("\\bfinally\\b");
        keywordPatterns.add("\\bfloat\\b");
        keywordPatterns.add("\\bgoto\\b");
        keywordPatterns.add("\\bimplements\\b");
        keywordPatterns.add("\\binstanceof\\b");
        keywordPatterns.add("\\binterface\\b");
        keywordPatterns.add("\\blong\\b");
        keywordPatterns.add("\\bnative\\b");
        keywordPatterns.add("\\bnull\\b");
        keywordPatterns.add("\\bpackage\\b");
        keywordPatterns.add("\\bshort\\b");
        keywordPatterns.add("\\bstatic\\b");
        keywordPatterns.add("\\bswitch\\b");
        keywordPatterns.add("\\bsynchronized\\b");
        keywordPatterns.add("\\bthrow\\b");
        keywordPatterns.add("\\bthrows\\b");
        keywordPatterns.add("\\btry\\b");
        keywordPatterns.add("\\btransient\\b");
        keywordPatterns.add("\\bvolatile\\b");*/
        return keywordPatterns;
    }

    private List<String> addSqmlKeywordPatterns() {
        List<String> keywordPatterns = new ArrayList<>();
         keywordPatterns.add("\\b(ACTIVE|ADD|ALL|AFTER|ALTER|AND|ANY|AS|ASC|ASCENDING"
                + "|AT|AUTO|AUTOINC|AVG|BASE_NAME|BEFORE|BEGIN|BETWEEN|BLOB|BOOLEAN|"
                + "BOTH|BY|BYTES|CACHE|CAST|CHAR|CHARACTER|CHECK|CHECK_POINT_LENGTH|"
                + "COLLATE|COLUMN|COMMIT|COMMITTED|COMPUTED|CONDITIONAL|CONSTRAINT|CONTAINING|COUNT|"
                + "CREATE|CSTRING|CURRENT|CURSOR|DATABASE|DATE|DAY|DEBUG|DEC|"
                + "DECIMAL|DECLARE|DEFAULT|DELETE|DESC|DESCENDING|DISTINCT|DO|DOMAIN|"
                + "DOUBLE|DROP|ELSE|END|ENTRY_POINT|ESCAPE|EXCEPTION|EXECUTE|EXISTS|"
                + "EXIT|EXTERNAL|EXTRACT|FILE|FILTER|FLOAT|FOREIGN|FROM|FULL|FUNCTION|"
                 + "GDSCODE|GENERATOR|GEN_ID|GRANT|GROUP|GROUP_COMMIT_WAIT_TIME|"
                 + "HAVING|HOUR|IF|IN|INT|INACTIVE|INDEX|INNER|INPUT_TYPE|INSERT|"
                 + "INTEGER|INTO|IS|ISOLATION|JOIN|KEY|LONG|LENGTH|LOGFILE|LOWER|"
                 + "LEADING|LEFT|LEVEL|LIKE|LOG_BUFFER_SIZE|MANUAL|MAX|MAXIMUM_SEGMENT|"
                 + "MERGE|MESSAGE|MIN|MINUTE|MODULE_NAME|MONEY|MONTH|NAMES|NATIONAL|"
                 + "NATURAL|NCHAR|NO|NOT|NULL|NUM_LOG_BUFFERS|NUMERIC|OF|ON|ONLY|"
                 + "OPTION|OR|ORDER|OUTER|OUTPUT_TYPE|OVERFLOW|PAGE_SIZE|PAGE|PAGES|"
                 + "PARAMETER|PASSWORD|PLAN|POSITIONPOST_EVENT|PRECISION|PROCEDURE|"
                 + "PROTECTED|PRIMARY|PRIVILEGES|RAW_PARTITIONS|READ|REAL|RECORD_VERSION|"
                 + "REFERENCES|RESERV|RESERVING|RETAIN|RETURNING_VALUES|RETURNS|"
                 + "REVOKE|RIGHT|ROLLBACK|SECOND|SEGMENT|SELECT|SET|SHARED|SHADOW|"
                 + "SCHEMA|SINGULAR|SIZE|SMALLINT|SNAPSHOT|SOME|SORT|SQLCODE|STABILITY|"
                 + "STARTING|STARTS|STATISTICS|SUB_TYPE|SUBSTRING|SUM|SUSPEND|TABLE|"
                 + "THEN|TIME|TIMESTAMP|TIMEZONE_HOUR|TIMEZONE_MINUTE|TO|TRAILING|"
                 + "TRANSACTION|TRIGGER|TRIM|UNCOMMITTED|UNIQUE|UPDATE|UPPER|USER|"
                 + "VALUE|VALUES|VARCHAR|VARIABLE|VARYING|VIEW|WAIT|WHEN|WHERE|"
                 + "WHILE|WITH|WORK|WRITE|YEAR)\\b");//210
        return keywordPatterns;
    }
    
    static class FindTextInfo{
        private int startPos;
        private int length;
        
        FindTextInfo(int startPos,int length){
            this.startPos=startPos;
            this.length=length;
        }
        
        int getStartPos(){
            return startPos;
        }
        
        int getLength(){
            return length;
        }
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() not implemented for Highlighter class.");
    }
}

/*
ACTIVE, ADD, ALL, AFTER, ALTER, AND, ANY, AS, ASC, ASCENDING, AT, AUTO, AUTOINC, AVG
BASE_NAME, BEFORE, BEGIN, BETWEEN, BLOB, BOOLEAN, BOTH, BY, BYTES
CACHE, CAST, CHAR, CHARACTER, CHECK, CHECK_POINT_LENGTH, COLLATE, COLUMN, COMMIT, COMMITTED, COMPUTED, CONDITIONAL, CONSTRAINT, CONTAINING, COUNT, CREATE, CSTRING, CURRENT, CURSOR
DATABASE, DATE, DAY, DEBUG, DEC, DECIMAL, DECLARE, DEFAULT, DELETE, DESC, DESCENDING, DISTINCT, DO, DOMAIN, DOUBLE, DROP
ELSE, END, ENTRY_POINT, ESCAPE, EXCEPTION, EXECUTE, EXISTS, EXIT, EXTERNAL, EXTRACT
FILE, FILTER, FLOAT, FOR, FOREIGN, FROM, FULL, FUNCTION
GDSCODE, GENERATOR, GEN_ID, GRANT, GROUP, GROUP_COMMIT_WAIT_TIME
HAVING, HOUR
IF, IN, INT, INACTIVE, INDEX, INNER, INPUT_TYPE, INSERT, INTEGER, INTO, IS, ISOLATION
JOIN
KEY
LONG, LENGTH, LOGFILE, LOWER, LEADING, LEFT, LEVEL, LIKE, LOG_BUFFER_SIZE
MANUAL, MAX, MAXIMUM_SEGMENT, MERGE, MESSAGE, MIN, MINUTE, MODULE_NAME, MONEY, MONTH
NAMES, NATIONAL, NATURAL, NCHAR, NO, NOT, NULL, NUM_LOG_BUFFERS, NUMERIC
OF, ON, ONLY, OPTION, OR, ORDER, OUTER, OUTPUT_TYPE, OVERFLOW
PAGE_SIZE, PAGE, PAGES, PARAMETER, PASSWORD, PLAN, POSITION, POST_EVENT, PRECISION, PROCEDURE, PROTECTED, PRIMARY, PRIVILEGES
RAW_PARTITIONS, RDB$DB_KEY, READ, REAL, RECORD_VERSION, REFERENCES, RESERV, RESERVING, RETAIN, RETURNING_VALUES, RETURNS, REVOKE, RIGHT, ROLLBACK
SECOND, SEGMENT, SELECT, SET, SHARED, SHADOW, SCHEMA, SINGULAR, SIZE, SMALLINT, SNAPSHOT, SOME, SORT, SQLCODE, STABILITY, STARTING, STARTS, STATISTICS, SUB_TYPE, SUBSTRING, SUM, SUSPEND
TABLE, THEN, TIME, TIMESTAMP, TIMEZONE_HOUR, TIMEZONE_MINUTE, TO, TRAILING, TRANSACTION, TRIGGER, TRIM
UNCOMMITTED, , UNIQUE, UPDATE, UPPER, USER
VALUE, VALUES, VARCHAR, VARIABLE, VARYING, VIEW
WAIT, WHEN, WHERE, WHILE, WITH, WORK, WRITE
YEAR*/
