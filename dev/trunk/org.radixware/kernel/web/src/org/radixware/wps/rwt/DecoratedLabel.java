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

package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;



public final class DecoratedLabel extends UIObject {

    private Html table = new Html("table");
    private List<UIObject> objects;
    private List<Html> labels;
    private boolean isWrapDisabled;

    public DecoratedLabel(String text) {
        super(new Div());
        this.html.add(table);
        appendLine(text);//create new row, set inner text into a label

        table.setCss("width", "100%");
        table.setCss("display", "table");

        this.html.addClass("rwt-decorated-label");
        this.html.addClass("rwt-gradient-bar");
    }

    public DecoratedLabel getDecoratedLabel() {
        return this;
    }

    //create new row, set inner text into a label
    public void appendLine(String text) {

        if (text != null) {
            if (text.contains("\n")) {
                Pattern pattern = Pattern.compile("\n");
                String[] arrayOfString = pattern.split(text);
                for (String res : arrayOfString) {
                    Html nline = createLine();
                    nline.setInnerText(res);
                }
            } else {
                Html nline = createLine();
                nline.setInnerText(text);
            }
        }
    }

    //extract inner text from the label
    public String getText() {
        //int rowc = table.childCount(); //number of rows
        StringBuilder res = new StringBuilder();//result text

        for (Html l : labels) {
            res.append(l.getInnerText()).append("\n");
        }
        return res.toString();
    }

    //clear the table, create new row and set new text inside DL
    public void setText(String text) {
        table.clear();
        appendLine(text);
    }

    //add new uiobject into DL
    public void appendObject(UIObject obj) {
        if (objects == null) {
            objects = new LinkedList<>();
        }
        if (labels == null) {
            labels = new LinkedList<>();
        }
        if (obj.getParent() != this) {
            obj.setParent(this);
            obj.html.setCss("position", "relative");
            objects.add(obj);

            Html l = createLine();
            labels.add(l);
            l.add(obj.html);
        }
    }

    //delete html-element from the dl
   public void removeObject(UIObject obj) {
        for (Html l : labels) {
            if (objects.contains(obj) && obj.html.getParent() == l) {
                l.clear();
                this.table.remove(l);
                labels.remove(l);
                objects.remove(obj);
                obj.setParent(null);
               }
        }
    }
    
    //set alignment for html-element in the dl
    public void setObjectAlign(UIObject obj, Alignment a) {
        if (objects != null) {
            for (UIObject o : objects) {
                for (Html l : labels) {
                    if (o.html.getParent() == l) {
                        switch (a) {
                            case RIGHT:

                                o.html.setCss("float", "right");
                                break;
                            case CENTER:

                                Html c = new Html("center");
                                l.remove(o.html);
                                l.add(c);
                                c.add(o.html);
                                o.html.setCss("float", null);
                                break;

                            case LEFT:

                                o.html.setCss("float", "left");
                                break;

                            case INHERIT:

                                o.html.setCss("margin", "inherit");
                                o.html.setCss("float", "none");
                                break;

                        }
                    }
                }
            }
        }
    }

    public void setVerticalAlign(Alignment va) {
        switch (va) {
            case BASELINE:
                html.setCss("vertical-align", "baseline");
                break;
            case BOTTOM:
                html.setCss("vertical-align", "bottom");
                break;
            case MIDDLE:
                html.setCss("vertical-align", "middle");
                break;
            case SUB:
                html.setCss("vertical-align", "sub");
                break;
            case SUPER:
                html.setCss("vertical-align", "super");
                break;
            case TEXT_BOTTOM:
                html.setCss("vertical-align", "text-bottom");
                break;
            case TEXT_TOP:
                html.setCss("vertical-align", "text-top");
                break;
            case TOP:
                html.setCss("vertical-align", "top");
                break;
        }
    }

    //set text-align in the label
    public void setHorizontalAlign(Alignment ha) {
        switch (ha) {
            case CENTER:
                html.setCss("text-align", "center");
                break;
            case JUSTIFY:
                html.setCss("text-align", "justify");
                break;
            case LEFT:
                html.setCss("text-align", "left");
                break;
            case RIGHT:
                html.setCss("text-align", "right");
                break;
            case START:
                html.setCss("text-align", "start");
                break;
            case END:
                html.setCss("text-align", "end");
                break;
            case INHERIT:
                html.setCss("text-align", "inherit");
                break;
        }
    }

    public Alignment getVerticalAlign() {
        String va = this.html.getCss("vertical-align");
        switch (va) {
            case "bottom":
                return Alignment.BOTTOM;
            case "middle":
                return Alignment.MIDDLE;
            case "top":
                return Alignment.TOP;
            case "text-bottom":
                return Alignment.TEXT_BOTTOM;
            case "text-top":
                return Alignment.TEXT_TOP;
            case "sub":
                return Alignment.SUB;
            default:
                return Alignment.SUPER;
        }
    }

    //get text-align
    public Alignment getHorizontalAlign() {
        String ha = this.html.getAttr("text-align");
        switch (ha) {
            case "center":
                return Alignment.CENTER;
            case "justify":
                return Alignment.JUSTIFY;
            case "right":
                return Alignment.RIGHT;
            case "inherit":
                return Alignment.INHERIT;
            case "start":
                return Alignment.START;
            case "end":
                return Alignment.END;
            default:
                return Alignment.LEFT;
        }
    }

    @Override
    public void setBackground(Color c) {
        getBackgroundHolder().setCss("background", c == null ? null
                : color2Str(c));
    }

    public void setBorderRadius(int r) {
        if (r == 0) {
            html.setCss("border-radius", null);
        } else {
            html.setCss("border-radius", String.valueOf(r) + "px");
        }
    }

    public void setBorderWidth(int w) {
        if (w == 0) {
            html.setCss("border-width", null);
        } else {
            html.setCss("border-width", String.valueOf(w) + "px");
        }
    }

    public void setLabelOpacity(final double lo) {
        String slo = String.valueOf(lo);
        html.setCss("opacity", slo == null ? null : String.valueOf(lo));
    }

    public String getOpacity() {
        String lo = html.getCss("opacity");
        return lo;
    }

    private Html createLine() {
        Html nrow = new Html("tr");
        Html td = new Html("td");
        if (labels == null) {
            labels = new LinkedList<>();
        }
        Html line = new Html("label");
        line.setCss("margin", "auto 0px");
        if (isWrapDisabled){
            line.setCss("white-space", "nowrap");
        }
        table.add(nrow);
        nrow.add(td);
        td.add(line);

        labels.add(line);

        return line;
    }
    
    public void setTextWrapDisabled(boolean disable) {
        if (disable!=isWrapDisabled){
            if (labels!=null){
                for (Html label: labels){
                    label.setCss("white-space", disable ? "nowrap" : null);
                }
            }
            isWrapDisabled = disable;
        }
    }
    
    public boolean isTextWrapDisabled(){
        return isWrapDisabled;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        if (objects != null) {
            for (UIObject obj : objects) {
                obj.visit(visitor);
            }
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        } else {
            if (objects == null) {
                return null;
            } else {
                for (UIObject obj : objects) {
                    result = obj.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
        }
    }
}