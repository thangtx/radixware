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
package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportAbstractAppearance;
import org.radixware.kernel.common.utils.XmlColor;

public class HtmlParser {

    private final static String notUseHtmlTag = "!HTML_TEXT";
    private final static String htmlTag = "html";

    private final static String tagFont = "font";
    private final static String attrFontFace = "face";
    private final static String attrFontSize = "size";
    private final static String attrFontColor = "color";
    private final static String attrFontStyle = "style";

    private final static String styleFontSize = "font-size";
    private final static String styleColor = "color";
    private final static String styleBackground = "background-color";
    private final static String styleFontFamily = "font-family";//Areal
    private final static String styleVerticalAlign = "vertical-align";
    private final static String styleTextDecoration = "text-decoration";
    //private final static String styleFontStyle="font-style";//normal | italic | oblique | inherit
    //private final static String styleFontWeight="font-weight";// bold|bolder|lighter|normal|100|200|300|400|500|600|700|800|900 

    private final static String tagItalic = "i";
    private final static String tagBold = "b";
    private final static String tagDel = "del";
    private final static String tagIns = "ins";

    static List<CellContents> parseHtmlText(String htmlText, final CellContents cellContent) {
        List<CellContents> cellContants = new ArrayList();
        int start = htmlText.indexOf('<', 0);
        if (start == -1) {
            cellContent.setText(htmlText);
            cellContants.add(cellContent);
        } else {
            int end = 0;
            while (start < htmlText.length() && (start = htmlText.indexOf('<', start)) != -1) {
                start = start + 1;
                if (checkNext(htmlText, start, tagFont) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, tagFont, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } else if (checkNext(htmlText, start, tagBold) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, tagBold, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } else if (checkNext(htmlText, start, tagIns) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, tagIns, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } else if (checkNext(htmlText, start, tagItalic) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, tagItalic, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } else if (checkNext(htmlText, start, tagDel) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, tagDel, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } else if (checkNext(htmlText, start, notUseHtmlTag) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, notUseHtmlTag, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                } if (checkNext(htmlText, start, htmlTag) != -1) {
                    int[] posInfo = parseTag(htmlText, cellContent, htmlTag, start, end, cellContants);
                    start = posInfo[0];
                    end = posInfo[1];
                }
            }
            if (htmlText.length() - end != 0) {
                String s = new String(htmlText.substring(end));
                CellContents prevContant = createContent(cellContent, s, s.length() + 1);
                cellContants.add(prevContant);
            }
        }
        return cellContants;
    }

    private static int[] parseTag(String htmlText, final CellContents cellContent, String tag, int start, int end, List<CellContents> cellContants) {
        if ((start - 1 - end) != 0) {
            String s = new String(htmlText.substring(end));
            CellContents prevContant = createContent(cellContent, s, start - end);
            cellContants.add(prevContant);
        }
        int startTag = start - 1;
        int index = htmlText.indexOf(tag, start) + tag.length();
        int[] endInfo = getEndTagInfo(htmlText, index, tag);
        if (endInfo != null) {
            end = endInfo[0] + endInfo[1];
            final List<CellContents> res = parseTag(htmlText, cellContent, tag, startTag, index, endInfo[0]);
            cellContants.addAll(res);
            start = end;
        } else {
            //throw new IllegalStateException("Illegal html in report cell");
            String s = new String(htmlText.substring(startTag));
            cellContent.setText(s);
            cellContants.add(cellContent);
            end = htmlText.length();
            start = end;
        }
        return new int[]{start, end};
    }

    private static List<CellContents> parseTag(String htmlText, CellContents cellContent, String tag, int tagStart, int start, int end) {
        List<CellContents> cellContants = new ArrayList<>();
        CellContents newCellContant = new CellContents(cellContent);
        //int start=tagStart +tag.length();
        start = createCellContant(newCellContant, htmlText, start, tag);
        int tagEnd;
        if ((tagEnd = checkNext(htmlText, start, ">")) != -1) {
            tagEnd = tagEnd + 1;
            String subStr = new String(htmlText.substring(tagEnd, end));
            if (tag.equals(notUseHtmlTag)) {
                newCellContant.setText(subStr);
                cellContants.add(newCellContant);
            } else {
                cellContants.addAll(parseHtmlText(subStr, newCellContant));
            }
        } else {
            CellContents content = new CellContents(cellContent);
            String subStr = new String(htmlText.substring(tagStart));
            content.setText(subStr);
            cellContants.add(content);
        }
        return cellContants;
    }

    private static int createCellContant(CellContents newCellContant, String htmlText, int start, String tag) {
        AdsReportAbstractAppearance.Font font = newCellContant.getFont();
        int oldStart = start;
        //boolean hasError=false;
        switch (tag) {
            case tagBold:
                font.setBold(true);
                break;
            case tagIns:
                font.setUnderline(true);
                break;
            case tagItalic:
                font.setItalic(true);
                break;
            case tagDel:
                font.setLineThrough(true);
                break;
            case tagFont:
                int end = htmlText.indexOf('>', start) - 1;
                int index;
                while (start < end) {
                    if ((index = checkNext(htmlText, start + 1, attrFontFace)) != -1) {
                        start = index + attrFontFace.length();
                        AttrInfo attrInfo = getAttrValue(htmlText, start);
                        if (attrInfo != null) {
                            font.setName(attrInfo.getValue());
                            start = start + attrInfo.getLength();
                        } else {
                            start = oldStart;
                            break;
                        }
                    } else if ((index = checkNext(htmlText, start + 1, attrFontSize)) != -1) {
                        start = index + attrFontSize.length();
                        AttrInfo attrInfo = getAttrValue(htmlText, start);
                        if (attrInfo != null) {
                            try {
                                Integer fontSize = Integer.valueOf(attrInfo.getValue());
                                /*if(fontSize==null){
                                 start=oldStart;
                                 break;
                                 }*/
                                font.setSizeMm(fontSize);
                                start = start + attrInfo.getLength();
                            } catch (NumberFormatException ex) {
                                start = oldStart;
                                break;
                            }
                        } else {
                            start = oldStart;
                            break;
                        }
                    } else if ((index = checkNext(htmlText, start + 1, attrFontColor)) != -1) {
                        start = index + attrFontColor.length();
                        AttrInfo attrInfo = getAttrValue(htmlText, start);
                        Color color = null;
                        if (attrInfo != null && (color = parseColor(attrInfo.getValue())) != null) {
                            newCellContant.setFgColor(color);
                            start = start + attrInfo.getLength();
                        } else {
                            start = oldStart;
                            break;
                        }
                    } else if ((index = checkNext(htmlText, start + 1, attrFontStyle)) != -1) {
                        start = index + attrFontStyle.length();
                        AttrInfo attrInfo = getAttrValue(htmlText, start);
                        if (attrInfo != null) {
                            String fontStyle = attrInfo.getValue();
                            if (fontStyle == null || !parseCss(fontStyle, newCellContant)) {
                                start = oldStart;
                                break;
                            }
                            start = start + attrInfo.getLength();
                        } else {
                            start = oldStart;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                break;
        }
        return start;
    }

    private static Color parseColor(String value) {
        Color color = XmlColor.parseColor(value);
        if (color == null) {
            value = value.replaceAll("( )+", "");
            Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
            Matcher m = c.matcher(value);
            if (m.matches()) {
                color = new Color(Integer.valueOf(m.group(1)),
                        Integer.valueOf(m.group(2)),
                        Integer.valueOf(m.group(3)));
            } else {
                try {
                    Field field = Color.class.getField(value);
                    color = (Color) field.get(null);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return color;
    }

    private static boolean parseCss(String text, CellContents newCellContant) {
        int start = 0, length = text.length(), index;
        while (start < length) {
            if ((index = checkNext(text, start, styleBackground)) != -1) {
                start = index + styleBackground.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                Color color = null;
                if (attrInfo != null && (color = parseColor(attrInfo.getValue())) != null) {
                    //Color color=parseColor( attrInfo.getValue());
                    newCellContant.setBgColor(color);
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else if ((index = checkNext(text, start, styleColor)) != -1) {
                start = index + styleColor.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                Color color;
                if (attrInfo != null && (color = parseColor(attrInfo.getValue())) != null) {
                    //Color color=parseColor( attrInfo.getValue());
                    newCellContant.setFgColor(color);
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else if ((index = checkNext(text, start, styleFontFamily)) != -1) {
                start = index + styleFontFamily.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                if (attrInfo != null) {
                    newCellContant.getFont().setName(attrInfo.getValue());
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else if ((index = checkNext(text, start, styleVerticalAlign)) != -1) {
                start = index + styleVerticalAlign.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                if (attrInfo != null) {
                    newCellContant.getFont().setVerticalAlign(attrInfo.getValue());
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else if ((index = checkNext(text, start, styleTextDecoration)) != -1) {
                start = index + styleTextDecoration.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                if (attrInfo != null) {
                    if (checkStyleTextDecoration(attrInfo.getValue(), "underline")) {
                        newCellContant.getFont().setUnderline(true);
                    }
                    if (checkStyleTextDecoration(attrInfo.getValue(), "line-through")) {
                        newCellContant.getFont().setLineThrough(true);
                    }
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else if ((index = checkNext(text, start, styleFontSize)) != -1) {
                start = index + styleFontSize.length();
                AttrInfo attrInfo = getAttrStyleValue(text, start);
                if (attrInfo != null) {
                    Integer fontSize = Integer.valueOf(attrInfo.getValue());
                    if (fontSize != null) {
                        newCellContant.getFont().setSizeMm(fontSize);
                    }
                    start = start + attrInfo.getLength();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean checkStyleTextDecoration(String value, String styleTextDecoration) {
        /* String[] arrStr=value.split("|");
         for(int i=0;i<arrStr.length;i++){
         if(styleTextDecoration.equals(arrStr[i])){
         return true;
         }
         } */
        return styleTextDecoration.equals(value);
    }

    private static CellContents createContent(CellContents cellContent, String htmlText, int start) {
        String prevContent = new String(htmlText.substring(0, start - 1));
        CellContents prevContant = new CellContents(cellContent);
        prevContant.setText(prevContent);
        return prevContant;
    }

    private static int[] getEndTagInfo(String htmlText, int start, String tag) {//result: 1)tag start position 2) tag length, where tag= end tag
        int tagIndex, index;
        int nestingLevel = 0;
        while ((tagIndex = htmlText.indexOf(tag, start)) != -1) {
            int startTag, endTag;
            if ((index = checkPrev(htmlText, tagIndex, "/")) != -1 && (startTag = checkPrev(htmlText, index, "<")) != -1) {
                if ((endTag = checkNext(htmlText, tagIndex + tag.length(), ">")) != -1) {
                    if (nestingLevel == 0) {
                        endTag = endTag + 1;
                        return new int[]{startTag, endTag - startTag};
                    }
                    nestingLevel--;
                }
            } else if (checkPrev(htmlText, tagIndex, "<") != -1) {
                nestingLevel++;
            }
            start = tagIndex + 1;
        }
        return null;
    }

    private static AttrInfo getAttrValue(String htmlText, int start) {
        int index;
        if ((index = checkNext(htmlText, start, "=")) != -1) {
            int indexStart;
            if ((indexStart = checkNext(htmlText, index + 1, "\"")) != -1) {
                int indexEnd = htmlText.indexOf("\"", indexStart + 1);
                if (indexEnd != -1) {
                    String value = new String(htmlText.substring(indexStart + 1, indexEnd));
                    return new AttrInfo(value.trim(), indexEnd - start + 1);
                }
            }
        }
        return null;
    }

    private static AttrInfo getAttrStyleValue(String htmlText, int start) {
        int indexStart;
        if ((indexStart = checkNext(htmlText, start, ":")) != -1) {
            int indexEnd = htmlText.indexOf(';', indexStart + 1);
            indexEnd = indexEnd == -1 ? htmlText.length() : indexEnd;
            String value = new String(htmlText.substring(indexStart + 1, indexEnd));
            return new AttrInfo(value.trim(), indexEnd - start + 1);
        }
        return null;
    }

    private static int checkPrev(String content, int index, String lex) {
        if (index - lex.length() >= 0 && index < content.length()) {
            String strLex = getPrevLex(content, index, lex.length());
            if (strLex != null && strLex.equals(lex)) {
                return content.substring(0, index).lastIndexOf(strLex);
            }
        }
        return -1;
    }

    private static int checkNext(String content, int index, String lex) {
        if (index >= 0 && index + lex.length() <= content.length()) {
            String strLex = getNextLex(content, index, lex.length());
            if (strLex != null && strLex.equals(lex)) {
                return index + content.substring(index).indexOf(strLex);
            }
        }
        return -1;
    }

    private static String getPrevLex(String content, int index, int lexLength) {
        while (index >= 0 && (content.toCharArray()[index - 1] == ' ')) {
            index--;
        }
        if (index < 0) {
            return null;
        }
        return new String(content.substring(index - lexLength, index));
    }

    private static String getNextLex(String content, int index, int lexLength) {
        int strLength = content.length();
        while (index < strLength && (content.toCharArray()[index] == ' ')) {
            index++;
        }
        if (index >= strLength) {
            return null;
        }
        return new String(content.substring(index, lexLength + index));
    }

    /*private static String getNextLex(String content, int start){
     int start=start,strLength=content.length();
     while(start<strLength && content.toCharArray()[start]!=',' && content.toCharArray()[start]!='=' &&
     content.toCharArray()[start]!=':' && content.toCharArray()[start]!=';' ){
     start++;
     }
     if(start>strLength)
     return null;
     return content.substring(start,start).trim();
     }*/
}
