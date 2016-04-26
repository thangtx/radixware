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
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import org.radixware.kernel.common.html.Html;


public class SvgImage extends AbstractContainer {

    public static class Group extends AbstractContainer {
        private String scale = null;
        private String translate = null;
        private String rotate = null;
        
        public Group() {
            super(new Html("g"));
            html.removeClass(getDefaultClassName());
            html.removeClass("rwt-ui-background");
        }
        public void scale(double x, double y) {
            scale = "scale(" + x + "," + y + ")";
            transform();
        }
        public void translate(double x, double y) {
            translate = "translate(" + x + "," + y + ")";
            transform();
        }
        public void rotate(double x, double y, double z) {
            rotate = "rotate(" + x + "," + y + "," + z + ")";
            transform();
        }
        private void transform() {
            String t = "";
            if (scale != null)
                t += scale + " ";
            if (translate != null)
                t += translate + " ";
            if (rotate != null)
                t += rotate + " ";
            if (!t.isEmpty()) {
                html.setAttr("transform", t);
            }
        }
    }

    public static class Item extends UIObject {
        public Item(String elementName) {
            super(new Html(elementName));
            html.removeClass(getDefaultClassName());
        }
    }

    public static class Rect extends Item {
        public Rect() {
            super("rect");
        }
        
        public void setRect(Rectangle r) {
            html.setAttr("x", r.x);
            html.setAttr("y", r.y);
            html.setAttr("width", r.width);
            html.setAttr("height", r.height);
        }
        
        public void setRx(int rx) {
            html.setAttr("rx", rx);
        }
        
        public void setRy(int ry) {
            html.setAttr("ry", ry);
        }
        
        public void setFill(Color c) {
            html.setCss("fill", "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
        }
        
        public void setStroke(Color c) {
            html.setCss("stroke", "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
        }
        
        public void setStrokeWidth(int w) {
            html.setCss("stroke-width", w);
        }
    }
    
    public static class Path extends Item {
        public Path() {
            super("path");
            html.setCss("fill", "none");
        }
        
        public void setPath(List<Point> ps) {
            String d = "";
            boolean first = true;
            for (Point p: ps) {
                if (first) {
                    d += "M";
                    first = false;
                } else
                    d += "L";
                d += p.x + " " + p.y + " ";
            }
            html.setAttr("d", d);
        }
        
        public void setStroke(Color c) {
            html.setCss("stroke", "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
        }
        
        public void setStrokeWidth(int w) {
            html.setCss("stroke-width", w);
        }
    }

    public static class Text extends Item {
        public Text() {
            super("text");
            html.setCss("stroke", "none");
        }
        
        public void setText(int x, int y, String text) {
            html.setAttr("x", x);
            html.setAttr("y", y);
            html.setInnerText(text);
        }
        
        public void setFill(Color c) {
            html.setCss("fill", "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
        }
        
        public void setFont(Font font) {
            html.setAttr("font-family", font.getFamily());
            html.setAttr("font-weight", "normal");
            html.setAttr("font-size", font.getSize());
        }
    }
    
    public SvgImage() {
        super(new Html("svg"));
        html.removeClass(getDefaultClassName());
        html.removeClass("rwt-ui-background");
/*        
        html.setCss("fill-opacity", "1");
        
        html.setCss("color-rendering", "auto");
        html.setCss("color-interpolation", "auto");
        html.setCss("text-rendering", "auto");
        html.setCss("shape-rendering", "auto");
        html.setCss("image-rendering", "auto");
        
        html.setCss("stroke", "black");
        html.setCss("stroke-linecap", "square");
        html.setCss("stroke-miterlimit", "10");
        html.setCss("stroke-opacity", "1");
        html.setCss("stroke-dasharray", "none");
        html.setCss("stroke-width", "1");
        html.setCss("stroke-linejoin", "miter");
        html.setCss("stroke-dashoffset", "0");
        
        html.setCss("font-weight", "normal");
        html.setCss("font-style", "normal");
*/
    }


    @Override
    public final void setHeight(int h) {
        super.setHeight(h);
    }

    @Override
    public final void setWidth(int w) {
        super.setWidth(w);
    }
}