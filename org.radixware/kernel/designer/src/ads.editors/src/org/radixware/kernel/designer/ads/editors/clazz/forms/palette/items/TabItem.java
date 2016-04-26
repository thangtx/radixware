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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.ETabPosition;
import org.radixware.kernel.common.defs.ads.ui.enums.ETabShape;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;

public class TabItem extends Item {

    public static final TabItem DEFAULT = new TabItem();

   
    private final int TAB_WIDTH = 60;
    private final int TAB_HEIGHT = 22;

    public TabItem() {
        this(Group.GROUP_CONTAINERS, AdsMetaInfo.TAB_WIDGET_CLASS);
    }

    protected TabItem(Group group, String clazz) {
        super(group, NbBundle.getMessage(TabItem.class, "Tab_Widget"), clazz);
    }

    @Override
    public Dimension adjustHintSize(RadixObject node, Dimension defaultSize) {
        Dimension sz = defaultSize.getSize();
        sz.height += TAB_HEIGHT;
        return sz;
    }

    @Override
    public Rectangle adjustLayoutGeometry(RadixObject node, Rectangle defaultRect) {
        Rectangle r = super.adjustLayoutGeometry(node, defaultRect);
        AdsUIProperty.EnumValueProperty tabPosition = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabPosition");
        ETabPosition position = (ETabPosition) tabPosition.value;
        final int offs = 22;
        switch (position) {
            case North:
                r.y += offs;
                r.height -= offs;
                break;
            case South:
                r.height -= offs;
                break;
            case West:
                r.x += offs;
                r.width -= offs;
                break;
            case East:
                r.width -= offs;
                break;
        }
        return r;
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsWidgetDef tab;
        AdsWidgetDef widget = (AdsWidgetDef) super.createObjectUI(context);

        tab = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
        tab.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "Tab 1", "Tab 1")));
        tab.setName("tab");
        widget.getWidgets().add(tab);

        tab = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
        tab.getProperties().add(new AdsUIProperty.LocalizedStringRefProperty("title", AdsUIUtil.createStringDef(context, "Tab 2", "Tab 2")));
        tab.setName("tab_2");
        widget.getWidgets().add(tab);

        widget.getProperties().add(new AdsUIProperty.IntProperty("currentIndex", 0));
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        paint(gr, r.getBounds(), node, true);
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        paint(gr, r, node, false);
    }

    public void paint(Graphics2D gr, Rectangle rect, RadixObject node, boolean paintBack) {
        AdsUIProperty.EnumValueProperty tab_pos_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabPosition");
        ETabPosition tab_pos = tab_pos_prop == null ? ETabPosition.North : (ETabPosition) tab_pos_prop.value;

        if (paintBack) {
            drawBaseRect(gr, rect.getBounds(), tab_pos);
        }

        drawTabs(gr, rect, node, paintBack, tab_pos/*, pos*/);
        int index = getTabIndex(gr, rect, node, 70, 10);
        int n = index;
    }

    private void drawTabs(Graphics2D gr, Rectangle rect, RadixObject node, boolean paintBack, ETabPosition tab_pos) {
        AdsUIItemDef widget = (AdsUIItemDef) node;
        Definitions<? extends AdsUIItemDef> tabs = null;
        if (widget instanceof AdsWidgetDef) {
            tabs = ((AdsWidgetDef) widget).getWidgets();
        } else if (widget instanceof AdsRwtWidgetDef) {
            tabs = ((AdsRwtWidgetDef) widget).getWidgets();
        }

        int pos;//позиция tab
        if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
            pos = rect.x;
        } else {
            pos = rect.y;
        }

        if (tabs != null) {
            AdsUIProperty.IntProperty currentIndex = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "currentIndex");
            if (currentIndex == null) {
                currentIndex = new AdsUIProperty.IntProperty("currentIndex", 0);
            }

            for (int i = 0, size = tabs.size(); i < size; i++) {
                Rectangle rect_tab_title = rect.getBounds();
                drawTab(gr, rect_tab_title, (RadixObject) tabs.get(i), node, (i == currentIndex.value), paintBack, tab_pos, pos);
                if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
                    pos = rect_tab_title.x + rect_tab_title.width;
                } else {
                    pos = rect_tab_title.y + rect_tab_title.height;
                }
            }
        }

        /*AdsUIProperty.BooleanProperty usesScrollButtons = (AdsUIProperty.BooleanProperty)AdsUtil.getUiProperty(node, "usesScrollButtons");
         if((usesScrollButtons.value)&&(pos>rect.width)){
         drawScrollButton(gr,rect,tab_pos);
         }*/
    }

    /*private void drawScrollButton(Graphics2D gr, Rectangle rect, ETabPosition tab_pos){
     Rectangle buttonRect=rect.getBounds();
     if((tab_pos==ETabPosition.North)||(tab_pos==ETabPosition.South)){
     buttonRect.height=TAB_HEIGHT;
     buttonRect.width=15;
     buttonRect.x=rect.width-buttonRect.width+1;
     if(tab_pos==ETabPosition.North)
     buttonRect.y=rect.y;
     else
     buttonRect.y=rect.height-buttonRect.height;
     gradientFillRect( gr,  buttonRect, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT);
     buttonRect.x=buttonRect.x-buttonRect.width;
     gradientFillRect( gr,  buttonRect, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT);
     }else if(tab_pos==ETabPosition.West){
     buttonRect.width=TAB_HEIGHT;
     buttonRect.height=15;
    
     }else if(tab_pos==ETabPosition.East){
     buttonRect.width=TAB_HEIGHT;
     buttonRect.height=15;
     }
     }
    
     private void gradientFillRect(Graphics2D gr, Rectangle rect,Color c1,Color c2){
     Paint oldPaint = gr.getPaint();
     gr.setPaint(new GradientPaint(rect.x, rect.y, c1, rect.x,rect.y+rect.height, c2,false));
     gr.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 7, 7);
     DrawUtil.drawPlainRoundRect( gr,  rect, DrawUtil.COLOR_DARK ,  1,  null);
     gr.setPaint(oldPaint);
     }*/
    private void drawBaseRect(Graphics2D gr, Rectangle rect, ETabPosition tab_pos) {
        if (tab_pos == ETabPosition.North) {
            rect.y = rect.y + 21;
            rect.height = rect.height - 21;
        } else if (tab_pos == ETabPosition.South) {
            rect.height = rect.height - 21;
        } else if (tab_pos == ETabPosition.West) {
            rect.x = rect.x + 21;
            rect.width = rect.width - 21;
        } else if (tab_pos == ETabPosition.East) {
            rect.width = rect.width - 21;
        }
        DrawUtil.drawPlainRect(gr, rect, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BUTTON);
    }

    private void drawTab(Graphics2D gr, Rectangle rect, RadixObject tab_node, RadixObject node, boolean isCur, boolean paintBack, ETabPosition tab_pos, int pos) {
        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        AdsUIProperty.BooleanProperty enabled_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsUIProperty.EnumValueProperty tab_shape_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabShape");
        boolean enabled = enabled_prop.value;
        ETabShape tab_shape = tab_shape_prop == null ? ETabShape.Rounded : (ETabShape) tab_shape_prop.value;
        AdsUIProperty.LocalizedStringRefProperty text_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(tab_node, "title");

        String label = "";
        if (text_prop != null) {
            label = getTextById(tab_node, text_prop.getStringId());
        }
        //считаем длинну текста
        int label_width = getTextWidth(gr, tab_node, iconSize, label, tab_shape);
        //рисуем заголовок для TabPage
        Rectangle textRect = getTextRect(rect, tab_pos, isCur, label_width, pos);//узнаем размеры и положение заголовка TabPage
        if (paintBack) {
            drawTabBackground(gr, textRect.getBounds(), isCur, tab_pos, tab_shape);
        } else {
            drawTabTitle(gr, tab_node, iconSize, textRect.getBounds(), enabled, tab_pos, label);
        }
    }

    private int getTextWidth(Graphics2D gr, RadixObject tab_node, AdsUIProperty.SizeProperty iconSize, String label, ETabShape tab_shape) {
        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(tab_node, "icon");
        int icon_width = 0;
        if (icon != null) {
            RadixIcon image = getIconById(tab_node, icon.getImageId());
            if (image != null) {
                if (iconSize != null) {
                    icon_width = iconSize.width + 4;
                } else {
                    icon_width = 20;
                }
            }
        }
        FontMetrics fm = gr.getFontMetrics(DrawUtil.DEFAULT_FONT);
        int label_width = fm.stringWidth(label) + 4 + icon_width;
        if (tab_shape == ETabShape.Triangular) {
            label_width = label_width + 20;
        }
        return label_width;
    }

    private void drawTabTitle(Graphics2D gr, RadixObject tab_node, AdsUIProperty.SizeProperty iconSize, Rectangle textRect, boolean enabled, ETabPosition tab_pos, String label) {
        EAlignment ha = EAlignment.AlignHCenter;
        EAlignment va = EAlignment.AlignVCenter;

        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(tab_node, "icon");
        RadixIcon image = null;
        if (icon != null) {
            image = getIconById(tab_node, icon.getImageId());
        }

        if (image != null) {
            drawImage(gr, textRect, iconSize, image, tab_pos, label);
            if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
                ha = EAlignment.AlignLeft;
            } else if (tab_pos == ETabPosition.West) {
                va = EAlignment.AlignBottom;
            } else if (tab_pos == ETabPosition.East) {
                va = EAlignment.AlignTop;
            }
        }
        drawText(gr, textRect, enabled, tab_pos, label, ha, va);
    }

    private void drawText(Graphics2D gr, Rectangle textRect, boolean enabled, ETabPosition tab_pos, String label, EAlignment ha, EAlignment va) {
        int drawMode = getDrawMode(tab_pos);
        Shape clip = gr.getClip();
        gr.clipRect(textRect.x + 1, textRect.y + 1, textRect.width - 2, textRect.height - 2);
        DrawUtil.drawText(gr, textRect, ha, va, enabled, drawMode, null, label);
        gr.setClip(clip);
    }

    private int getDrawMode(ETabPosition tab_pos) {
        int drawMode = DrawUtil.DRAW_LEFT_TO_RIGHT;
        if (tab_pos == ETabPosition.West) {
            drawMode = DrawUtil.DRAW_BOTTOM_TO_TOP;
        } else if (tab_pos == ETabPosition.East) {
            drawMode = DrawUtil.DRAW_TOP_TO_BOTTOM;
        }
        return drawMode;
    }

    private void drawImage(Graphics2D gr, Rectangle textRect, AdsUIProperty.SizeProperty iconSize, RadixIcon image, ETabPosition tab_pos, String label) {
        int icon_width = 16;
        int icon_height = 16;
        if (iconSize != null) {
            icon_width = iconSize.width;
            icon_height = iconSize.height;
        }
        int spacing = 4;

        Image pixmap = image.getImage(icon_width, icon_height);
        //pixmap = pixmap.getScaledInstance(icon_width, icon_height, Image.SCALE_DEFAULT);

        Dimension textSize = DrawUtil.textSize(gr, label);
        Rectangle iconRect;
        if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
            int width = icon_width;
            if (!label.isEmpty()) {
                width += (textSize.width + spacing);
            }
            iconRect = new Rectangle(textRect.x + (textRect.width - width) / 2,
                    textRect.y + (textRect.height - icon_height) / 2, icon_width, icon_height);
            textRect.x = iconRect.x + iconRect.width + spacing;
            textRect.width = (textRect.x + textRect.width) - (iconRect.x + iconRect.width + spacing);
        } else {
            int height = icon_height;
            if (!label.isEmpty()) {
                height += (textSize.width + spacing);
            }
            if (tab_pos == ETabPosition.West) {
                iconRect = new Rectangle(textRect.x + (textRect.width - icon_width) / 2,
                        textRect.y + textRect.height - (textRect.height - height) / 2 - icon_height, icon_width, icon_height);
                textRect.y = iconRect.y - spacing - textRect.height;
            } else {
                iconRect = new Rectangle(textRect.x + (textRect.width - icon_width) / 2,
                        textRect.y + (textRect.height - height) / 2, icon_width, icon_height);
                textRect.y = iconRect.y + iconRect.height + spacing;
            }
        }
        //поворачиваем иконку
        rotateIcon(gr, iconRect, pixmap, tab_pos);
    }

    private void rotateIcon(Graphics2D gr, Rectangle iconRect, Image pixmap, ETabPosition tab_pos) {
        AffineTransform oldAT = gr.getTransform();
        AffineTransform af = new AffineTransform(oldAT);
        if (tab_pos == ETabPosition.East) {
            af.rotate(Math.PI / 2, iconRect.x + iconRect.width / 2.0, iconRect.y + iconRect.height / 2.0);
        } else if (tab_pos == ETabPosition.West) {
            af.rotate(Math.PI * 3 / 2, iconRect.x + iconRect.width / 2.0, iconRect.y + iconRect.height / 2.0);
        }
        gr.setTransform(af);
        gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
        gr.setTransform(oldAT);
    }

    private Rectangle getTextRect(Rectangle textRect, ETabPosition tab_pos, boolean isCur, int label_width, int pos) {
        if (tab_pos == ETabPosition.North) {
            getTextRect_Noth(textRect, isCur, label_width, pos);
        } else if (tab_pos == ETabPosition.South) {
            getTextRect_South(textRect, isCur, label_width, pos);
        } else if (tab_pos == ETabPosition.West) {
            getTextRect_West(textRect, isCur, label_width, pos);
        } else if (tab_pos == ETabPosition.East) {
            getTextRect_East(textRect, isCur, label_width, pos);
        }
        return textRect;
    }

    private Rectangle getTextRect_Noth(Rectangle textRect, boolean isCur, int label_width, int pos) {
        textRect.x = pos;
        textRect.height = TAB_HEIGHT;
        if (!isCur) {
            textRect.y = textRect.y + 1;
            textRect.height = textRect.height - 1;
        } else {
            textRect.height = textRect.height + 1;
        }
        if (TAB_WIDTH < label_width) {
            textRect.width = label_width;
        } else {
            textRect.width = TAB_WIDTH;
        }
        return textRect;
    }

    private Rectangle getTextRect_South(Rectangle textRect, boolean isCur, int label_width, int pos) {
        textRect.x = pos;
        textRect.y = textRect.y + textRect.height - TAB_HEIGHT;
        textRect.height = TAB_HEIGHT;
        /*if(isCur){
         textRect.height=textRect.height+1;
         }*/
        if (!isCur) {
            textRect.y = textRect.y + 1;
            textRect.height = textRect.height - 1;
        } else {
            textRect.height = textRect.height + 1;
        }
        if (TAB_WIDTH < label_width) {
            textRect.width = label_width;
        } else {
            textRect.width = TAB_WIDTH;
        }
        return textRect;
    }

    private Rectangle getTextRect_West(Rectangle textRect, boolean isCur, int label_width, int pos) {
        textRect.y = pos;
        textRect.width = TAB_HEIGHT;
        if (!isCur) {
            textRect.x = textRect.x + 1;
            textRect.width = textRect.width - 1;
        } else {
            textRect.width = textRect.width + 1;
        }
        if (TAB_WIDTH < label_width) {
            textRect.height = label_width;
        } else {
            textRect.height = TAB_WIDTH;
        }
        return textRect;
    }

    private Rectangle getTextRect_East(Rectangle textRect, boolean isCur, int label_width, int pos) {
        textRect.x = textRect.x + textRect.width - TAB_HEIGHT;
        textRect.width = TAB_HEIGHT;
        textRect.y = pos;
        if (!isCur) {
            textRect.x = textRect.x + 1;
            textRect.width = textRect.width - 1;
        }//else{
        //textRect.width=textRect.width+1;
        //}
        if (TAB_WIDTH < label_width) {
            textRect.height = label_width;
        } else {
            textRect.height = TAB_WIDTH;
        }
        if (isCur) {
            textRect.width = textRect.width + 1;
        }
        return textRect;
    }

    private void drawTabBackground(Graphics2D gr, Rectangle rect, boolean isCur, ETabPosition tab_pos, ETabShape tab_shape) {
        if (tab_shape == ETabShape.Rounded) {
            drawRoundTabShape(gr, rect, tab_pos, isCur);
        } else {
            drawTriangularTabShape(gr, rect, tab_pos, isCur);
        }
    }

    private void drawRoundTabShape(Graphics2D gr, Rectangle rect, ETabPosition tab_pos, boolean isCur) {
        Paint savedPaint = gr.getPaint();
        gr.setPaint(DrawUtil.COLOR_DARK);
        GeneralPath shape = getRoundShape(rect, tab_pos);
        if (isCur) {
            gradientFill(gr, rect, shape, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_MID_LIGHT, tab_pos);
            drawOrangeLine(gr, rect.getBounds(), tab_pos);
        } else {
            gradientFill(gr, rect, shape, DrawUtil.COLOR_BUTTON, Color.LIGHT_GRAY, tab_pos);
        }
        gr.draw(shape);
        gr.setPaint(savedPaint);
    }

    private GeneralPath getRoundShape(Rectangle rect, ETabPosition tab_pos) {
        GeneralPath shape = new GeneralPath();
        if (tab_pos == ETabPosition.North) {
            shape.moveTo(rect.x, rect.y + rect.height - 1);
            shape.lineTo(rect.x, rect.y + 5);
            shape.curveTo(rect.x, rect.y + 5, rect.x, rect.y, rect.x + 5, rect.y);
            shape.lineTo(rect.x + rect.width - 5, rect.y);
            shape.curveTo(rect.x + rect.width - 5, rect.y, rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + 5);
            shape.lineTo(rect.x + rect.width, rect.y + rect.height - 1);
        } else if (tab_pos == ETabPosition.South) {
            shape.moveTo(rect.x, rect.y);
            shape.lineTo(rect.x, rect.y + rect.height - 6);
            shape.curveTo(rect.x, rect.y + rect.height - 6, rect.x, rect.y + rect.height - 1, rect.x + 5, rect.y + rect.height - 1);
            shape.lineTo(rect.x + rect.width - 5, rect.y + rect.height - 1);
            shape.curveTo(rect.x + rect.width - 5, rect.y + rect.height - 1, rect.x + rect.width, rect.y + rect.height - 1, rect.x + rect.width, rect.y + rect.height - 6);
            shape.lineTo(rect.x + rect.width, rect.y);
        } else if (tab_pos == ETabPosition.West) {
            shape.moveTo(rect.x + rect.width - 1, rect.y);
            shape.lineTo(rect.x + 5, rect.y);
            shape.curveTo(rect.x + 5, rect.y, rect.x, rect.y, rect.x, rect.y + 5);
            shape.lineTo(rect.x, rect.y + rect.height - 5);
            shape.curveTo(rect.x, rect.y + rect.height - 5, rect.x, rect.y + rect.height, rect.x + 5, rect.y + rect.height);
            shape.lineTo(rect.x + rect.width - 1, rect.y + rect.height);
        } else if (tab_pos == ETabPosition.East) {
            shape.moveTo(rect.x, rect.y);
            shape.lineTo(rect.x + rect.width - 6, rect.y);
            shape.curveTo(rect.x + rect.width - 6, rect.y, rect.x + rect.width - 1, rect.y, rect.x + rect.width - 1, rect.y + 5);
            shape.lineTo(rect.x + rect.width - 1, rect.y + rect.height - 5);
            shape.curveTo(rect.x + rect.width - 1, rect.y + rect.height - 5, rect.x + rect.width - 1, rect.y + rect.height, rect.x + rect.width - 6, rect.y + rect.height);
            shape.lineTo(rect.x, rect.y + rect.height);
        }
        return shape;
    }

    private void drawOrangeLine(Graphics2D gr, Rectangle orangeLine, ETabPosition tab_pos) {
        if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
            if (tab_pos == ETabPosition.South) {
                orangeLine.y = orangeLine.y + orangeLine.height - 3;
            }
            orangeLine.height = 3;
            orangeLine.x = orangeLine.x + 2;
            orangeLine.width = orangeLine.width - 4;
        } else {
            if (tab_pos == ETabPosition.East) {
                orangeLine.x = orangeLine.x + orangeLine.width - 3;
            }
            orangeLine.width = 3;
            orangeLine.y = orangeLine.y + 2;
            orangeLine.height = orangeLine.height - 4;
        }
        DrawUtil.drawPlainRect(gr, orangeLine, new Color(255, 177, 108), 0, new Color(255, 177, 108));
    }

    private void drawTriangularTabShape(Graphics2D gr, Rectangle rect, ETabPosition tab_pos, boolean isCur) {
        Paint savedPaint = gr.getPaint();
        gr.setPaint(DrawUtil.COLOR_DARK);
        GeneralPath shape = getTriangularShape(rect, tab_pos);
        if (isCur) {
            gradientFill(gr, rect, shape, DrawUtil.COLOR_BASE, DrawUtil.COLOR_BASE, tab_pos);
        } else {
            gradientFill(gr, rect, shape, DrawUtil.COLOR_BUTTON, DrawUtil.COLOR_BUTTON, tab_pos);
        }
        gr.draw(shape);
        gr.setPaint(savedPaint);
    }

    private GeneralPath getTriangularShape(Rectangle rect, ETabPosition tab_pos) {
        GeneralPath shape = new GeneralPath();
        if (tab_pos == ETabPosition.North) {
            shape.moveTo(rect.x, rect.y + rect.height - 1);
            shape.lineTo(rect.x + 10, rect.y);
            shape.lineTo(rect.x + rect.width - 10, rect.y);
            shape.lineTo(rect.x + rect.width, rect.y + rect.height - 1);
        } else if (tab_pos == ETabPosition.South) {
            shape.moveTo(rect.x, rect.y);
            shape.lineTo(rect.x + 10, rect.y + rect.height - 1);
            shape.lineTo(rect.x + rect.width - 10, rect.y + rect.height - 1);
            shape.lineTo(rect.x + rect.width, rect.y);
        } else if (tab_pos == ETabPosition.West) {
            shape.moveTo(rect.x + rect.width - 1, rect.y);
            shape.lineTo(rect.x, rect.y + 10);
            shape.lineTo(rect.x, rect.y + rect.height - 10);
            shape.lineTo(rect.x + rect.width - 1, rect.y + rect.height);
        } else if (tab_pos == ETabPosition.East) {
            shape.moveTo(rect.x, rect.y);
            shape.lineTo(rect.x + rect.width - 1, rect.y + 10);
            shape.lineTo(rect.x + rect.width - 1, rect.y + rect.height - 10);
            shape.lineTo(rect.x, rect.y + rect.height);
        }
        return shape;
    }

    private void gradientFill(Graphics2D gr, Rectangle rect, Shape shape, Color c1, Color c2, ETabPosition tab_pos) {
        Paint oldPaint = gr.getPaint();
        if (tab_pos == ETabPosition.North) {
            gr.setPaint(new GradientPaint(rect.x, rect.y, c1, rect.x, rect.y + rect.height, c2, false));
        } else if (tab_pos == ETabPosition.South) {
            gr.setPaint(new GradientPaint(rect.x, rect.y + rect.height, c1, rect.x, rect.y, c2, false));
        } else if (tab_pos == ETabPosition.West) {
            gr.setPaint(new GradientPaint(rect.x, rect.y, c1, rect.x + rect.width, rect.y, c2, false));
        } else if (tab_pos == ETabPosition.East) {
            gr.setPaint(new GradientPaint(rect.x + rect.width, rect.y, c1, rect.x, rect.y, c2, false));
        }
        gr.fill(shape);
        gr.setPaint(oldPaint);
    }

    //return tab index by point
    public int getTabIndex(Graphics2D gr, Rectangle rect, RadixObject node, int x, int y) {
        AdsUIProperty.EnumValueProperty tab_pos_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabPosition");
        ETabPosition tab_pos = tab_pos_prop == null ? ETabPosition.North : (ETabPosition) tab_pos_prop.value;
        return getTabIndex(gr, rect, node, tab_pos, x, y);
    }

    private int getTabIndex(Graphics2D gr, Rectangle rect, RadixObject node, ETabPosition tab_pos, int x, int y) {
        AdsUIItemDef widget = (AdsUIItemDef) node;
        Definitions<? extends AdsUIItemDef> tabs;
        if (widget instanceof AdsWidgetDef) {
            tabs = ((AdsWidgetDef) widget).getWidgets();
        } else if (widget instanceof AdsRwtWidgetDef) {
            tabs = ((AdsRwtWidgetDef) widget).getWidgets();
        } else {
            return -1;
        }

        AdsUIProperty.EnumValueProperty tab_shape_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabShape");
        ETabShape tab_shape = tab_shape_prop == null ? ETabShape.Rounded : (ETabShape) tab_shape_prop.value;

        int pos;//позиция tab
        if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
            pos = rect.x;
        } else {
            pos = rect.y;
        }

        for (int i = 0, size = tabs.size(); i < size; i++) {
            Rectangle rect_tab_title = rect.getBounds();

            rect_tab_title = getTabRect(gr, rect_tab_title, (RadixObject) tabs.get(i), node, false, tab_pos, pos);
            if (tab_shape == ETabShape.Rounded) {
                if ((x > rect_tab_title.x) && (x < (rect_tab_title.x + rect_tab_title.width))
                        && (y > rect_tab_title.y) && (y < (rect_tab_title.y + rect_tab_title.height))) {
                    return i;
                }
            } else if (tab_shape == ETabShape.Triangular) {
                if (isInTreangleShapeArea(rect_tab_title, tab_pos, x, y)) {
                    return i;
                }
            }
            if ((tab_pos == ETabPosition.North) || (tab_pos == ETabPosition.South)) {
                pos = rect_tab_title.x + rect_tab_title.width;
            } else {
                pos = rect_tab_title.y + rect_tab_title.height;
            }
        }
        return -1;
    }

    private boolean isInTreangleShapeArea(Rectangle rect, ETabPosition tab_pos, int x, int y) {
        if (tab_pos == ETabPosition.North) {
            if ((y > rect.y) && (y < (rect.y + rect.height))
                    && checkLeftSide(rect.x, rect.y + rect.height, rect.x + 10, rect.y, x, y, true)
                    && checkRiteSide(rect.x + rect.width - 10, rect.y, rect.x + rect.width, rect.y + rect.height, x, y, true)) {
                return true;
            }
        } else if (tab_pos == ETabPosition.South) {
            if ((y > rect.y) && (y < (rect.y + rect.height))
                    && checkLeftSide(rect.x, rect.y, rect.x + 10, rect.y + rect.height, x, y, true)
                    && checkRiteSide(rect.x + rect.width - 10, rect.y + rect.height, rect.x + rect.width, rect.y, x, y, true)) {
                return true;
            }
        } else if (tab_pos == ETabPosition.West) {
            if ((x > rect.x) && (x < (rect.x + rect.width))
                    && checkLeftSide(rect.x + rect.width, rect.y, rect.x, rect.y + 10, x, y, false)
                    && checkRiteSide(rect.x, rect.y + rect.height - 10, rect.x + rect.width, rect.y + rect.height, x, y, false)) {
                return true;
            }
        } else if (tab_pos == ETabPosition.East) {
            if ((x > rect.x) && (x < (rect.x + rect.width))
                    && checkLeftSide(rect.x, rect.y, rect.x + rect.width, rect.y + 10, x, y, false)
                    && checkRiteSide(rect.x + rect.width, rect.y + rect.height - 10, rect.x, rect.y + rect.height, x, y, false)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeftSide(double x1, double y1, double x2, double y2, double x, double y, boolean isHorixontal) {
        if (isHorixontal) {
            double vx = varX(x1, y1, x2, y2, y);
            if (vx <= x) {
                return true;
            }
        } else {
            double vy = varY(x1, y1, x2, y2, x);
            if (vy <= y) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRiteSide(double x1, double y1, double x2, double y2, double x, double y, boolean isHorixontal) {
        if (isHorixontal) {
            double vx = varX(x1, y1, x2, y2, y);
            if (vx >= x) {
                return true;
            }
        } else {
            double vy = varY(x1, y1, x2, y2, x);
            if (vy >= y) {
                return true;
            }
        }
        return false;
    }

    private double varX(double x1, double y1, double x2, double y2, double y) {
        double a = (y2 - y1) / (x2 - x1);
        double b = y1 - x1 * a;
        return (y - b) / a;
    }

    private double varY(double x1, double y1, double x2, double y2, double x) {
        double a = (y2 - y1) / (x2 - x1);
        double b = y1 - x1 * a;
        return a * x + b;
    }

    private Rectangle getTabRect(Graphics2D gr, Rectangle rect, RadixObject tab_node, RadixObject node, boolean isCur, ETabPosition tab_pos, int pos) {
        AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(node, "iconSize");
        AdsUIProperty.EnumValueProperty tab_shape_prop = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, "tabShape");
        ETabShape tab_shape = tab_shape_prop == null ? ETabShape.Rounded : (ETabShape) tab_shape_prop.value;
        AdsUIProperty.LocalizedStringRefProperty text_prop = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(tab_node, "title");

        String label = "";
        if (text_prop != null) {
            label = getTextById(tab_node, text_prop.getStringId());
        }
        int label_width = getTextWidth(gr, tab_node, iconSize, label, tab_shape);//считаем длинну текста
        return getTextRect(rect, tab_pos, isCur, label_width, pos);//узнаем размеры и положение заголовка TabPage
    }
}
