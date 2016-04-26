
package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.defs.ads.ui.enums.EViewMode;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.getIconById;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.getTextById;


public class DrawItemWidgets {
    public final static int     INDENT = 2;
    public final static int     ICON_TEXT_SPACING = 4;
    public static final int     COLUMN_HEIGHT = 23;
    public static final int     COLUMN_WIDTH = 120;
    public static final int     ROW_WIDTH = 120;
    public static final int     ROW_HEIGHT = 23;
    
    public static void drawText(Graphics2D gr, Rectangle rect, RadixObject obj, boolean enabled, boolean catText, EViewMode viewMode, boolean isGrid) {
        EAlignment ha = EAlignment.AlignLeft;
        EAlignment va = EAlignment.AlignVCenter;

        AdsUIProperty.LocalizedStringRefProperty textProperty = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(obj, "text");
        String text = getTextById(obj, textProperty.getStringId());

        AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(obj, "icon");
        RadixIcon itemImage = getIconById(obj, icon.getImageId());

        if (itemImage != null) {
            AdsUIProperty.SizeProperty iconSize = (AdsUIProperty.SizeProperty) AdsUIUtil.getUiProperty(obj, "iconSize");
            int iconDefaultSize = 16;
            if (iconSize != null) {
                iconDefaultSize = iconSize.width;
            }
            drawImage(gr, rect, iconDefaultSize, iconDefaultSize, itemImage, viewMode, isGrid);
        } else {
            rect.x = rect.x + INDENT;
        }
        if ((isGrid) && (viewMode == EViewMode.IconMode)) {
            ha = EAlignment.AlignHCenter;
            va = EAlignment.AlignTop;
        }
        if (catText) {
            text = calcText(gr, text, rect.width);
        }
        DrawUtil.drawText(gr, rect, ha, va, enabled, text);
    }
    
    private static String calcText(Graphics2D gr, String label, int width) {
        FontMetrics metrics = gr.getFontMetrics(DrawUtil.DEFAULT_FONT);
        int w = metrics.stringWidth(label);
        if (w > width) {
            char[] c = label.toCharArray();
            w = 0;
            int i;
            for (i = 0; i < c.length; i++) {
                w += metrics.charWidth(c[i]);
                if (w > width) {
                    break;
                }
            }
            if (i < 3) {
                i = 3;
            }
            return label.substring(0, i - 3) + "...";
        }
        return label;
    }

    private static void drawImage(Graphics2D gr, Rectangle itemRect, int iconWidth, int iconHeight, RadixIcon itemImage, EViewMode viewMode, boolean isGrid) {
        Image pixmap = itemImage.getImage(iconWidth, iconHeight);
        //pixmap = pixmap.getScaledInstance(icon_width, icon_height, 0);

        Rectangle iconRect = null;
        if (viewMode == EViewMode.IconMode) {//рисуем текст под иконкой
            iconRect = new Rectangle(itemRect.x + (itemRect.width - iconHeight) / 2 + INDENT,
                    itemRect.y,
                    iconWidth, iconHeight);
            itemRect.height = (itemRect.y + itemRect.height) - (iconRect.y + iconRect.height + ICON_TEXT_SPACING);
            if (!isGrid) {
                itemRect.width = Math.max(iconWidth, iconWidth);
            }
            itemRect.y = iconRect.y + iconRect.height + ICON_TEXT_SPACING;
        } else {//рисуем текст справа от иконки
            iconRect = new Rectangle(itemRect.x + INDENT,
                    itemRect.y + (itemRect.height - iconHeight) / 2,
                    iconWidth, iconHeight);
            itemRect.width = (itemRect.x + itemRect.width) - (iconRect.x + iconRect.width + ICON_TEXT_SPACING);
            itemRect.x = iconRect.x + iconRect.width + ICON_TEXT_SPACING;
        }
        gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
    }
    
    public static void paint(Graphics2D gr, Rectangle r, RadixObject node, boolean isTable) {
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        AdsItemWidgetDef def = (AdsItemWidgetDef) node;
        AdsItemWidgetDef.Items items = def.getItems();

        AdsUIProperty.IntProperty indentation_prop = (AdsUIProperty.IntProperty) AdsUIUtil.getUiProperty(node, "indentation");
        int indentation = 0;
        if (indentation_prop != null) {
            indentation = indentation_prop.value;
        }

        Rectangle rect = r.getBounds();
        rect.x = rect.x + indentation;
        rect.y = rect.y + COLUMN_HEIGHT;
        for (int i = 0, size = items.size(); i < size; i++) {
            AdsItemWidgetDef.WidgetItem node_item = items.get(i);
            int index = isTable ? node_item.row : i;
            Rectangle itemRect = getItemRect(rect.getBounds(), node_item, index);

            if (!isTable) {
                drawItemStateIcon(gr, itemRect, node_item, rect.x, i == size - 1);
            } else {
                itemRect.width = ROW_WIDTH;
            }

            Shape clip = gr.getClip();
            gr.clipRect(itemRect.x + 1, itemRect.y + 1, itemRect.width - 2, itemRect.height - 2);
            drawText(gr, itemRect, node_item, enabled.value, true, EViewMode.ListMode, false);
            gr.setClip(clip);
        }
    }
    
    private static Rectangle getItemRect(Rectangle itemRect, AdsItemWidgetDef.WidgetItem nodeItem, int index) {
        itemRect.height = COLUMN_HEIGHT;
        itemRect.y = itemRect.y + index * (COLUMN_HEIGHT);
        if (nodeItem.column > 0) {
            itemRect.x = itemRect.x + nodeItem.column * ROW_WIDTH;
        }
        
        return itemRect;
    }
    
    private static void drawItemStateIcon(Graphics2D gr, Rectangle itemRect, AdsItemWidgetDef.WidgetItem node_item, int indentation, boolean isLastItem) {
        RadixIcon image = null;
        if ((node_item.getItems() != null) && (node_item.getItems().size() > 0)) {
            image = isLastItem ? RadixWareIcons.WIDGETS.TREE_ITEM_HAS_CHILDS_LAST : RadixWareIcons.WIDGETS.TREE_ITEM_HAS_CHILDS;
        } else {
            image = isLastItem ? RadixWareIcons.WIDGETS.TREE_ITEM_LAST : RadixWareIcons.WIDGETS.TREE_ITEM;
        }
        int iconSize = ROW_HEIGHT;
        Image pixmap = image.getImage(iconSize, iconSize);
        //pixmap = pixmap.getScaledInstance(icon_width, icon_height, 0);
        Rectangle iconRect = new Rectangle(indentation, itemRect.y + (itemRect.height - iconSize) / 2 - 1, iconSize, iconSize);
        gr.drawImage(pixmap, iconRect.x, iconRect.y, null);
        if (node_item.column == 0) {
            itemRect.x = itemRect.x + iconSize;
        }
    }
    
    public static void drawBackgroundForTitle(Graphics2D gr, Rectangle rect, int pos_x, int pos_y) {
        Rectangle rect_gradient = rect.getBounds();
        rect_gradient.x = rect.x + pos_x;
        rect_gradient.y = rect.y + pos_y;
        rect_gradient.width = rect.width - pos_x;
        rect_gradient.height = rect.height - pos_y;
        gradientFillRect(gr, rect_gradient);
    }
    
    private static void gradientFillRect(Graphics2D gr, Rectangle rect) {
        Paint oldPaint = gr.getPaint();
        gr.setPaint(new GradientPaint(rect.x, rect.y, DrawUtil.COLOR_BUTTON, rect.x,
                rect.y + rect.height, DrawUtil.COLOR_MID_LIGHT/*Color.LIGHT_GRAY*/, false));
        gr.fill(rect);
        gr.setPaint(oldPaint);
    }
    
    public static void paintBackgroundWidgetsWithColumn(Graphics2D gr, Rectangle r, RadixObject node) {
        Shape clip = gr.getClip();
        gr.clipRect(r.x, r.y, r.width, r.height);
        DrawUtil.drawPlainRect(gr, r, DrawUtil.COLOR_DARK, 1, DrawUtil.COLOR_BASE);
        AdsItemWidgetDef def = (AdsItemWidgetDef) node;
        if ((def != null) && (def.getColumns() != null) && (def.getColumns().size() > 0)) {
            Rectangle rect = r.getBounds();
            rect.x = rect.x + 1;
            rect.y = rect.y + 1;
            rect.width = rect.width - 2;
            rect.height = rect.height - 1;
            drawColumnsTitle(gr, rect, node);
        }
        gr.setClip(clip);
    }
    
    private static void drawColumnsTitle(Graphics2D gr, Rectangle rect, RadixObject node) {
        AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
        int pos = rect.x;
        rect.height = COLUMN_HEIGHT;
        DrawUtil.drawPlainRect(gr, rect, DrawUtil.COLOR_BUTTON, 1, DrawUtil.COLOR_BUTTON);
        rect.x = rect.x - 1;
        rect.width = COLUMN_WIDTH;
        AdsItemWidgetDef widget = (AdsItemWidgetDef) node;
        for (int i = 0, size = widget.getColumns().size(); i < size; i++) {
            drawBackgroundForTitle(gr, rect, 2, 1);//рисуем фон колонки
            //рисуем заголовок колонки
            Rectangle textRect = rect.getBounds();
            Shape clip = gr.getClip();
            gr.clipRect(textRect.x + 1, textRect.y + 1, textRect.width - 2, textRect.height - 2);
            drawText(gr, textRect, widget.getColumns().get(i), enabled.value, false, EViewMode.ListMode, false);
            gr.setClip(clip);
            //рисуем разделяющую линию           
            rect.x = rect.x + COLUMN_WIDTH;
            DrawUtil.drawLine(gr, rect.x, rect.y + 4, rect.x, rect.y + rect.height - 4, DrawUtil.COLOR_DARK);
            DrawUtil.drawLine(gr, rect.x + 1, rect.y + 4, rect.x + 1, rect.y + rect.height - 4, DrawUtil.COLOR_BASE);
        }
        DrawUtil.drawLine(gr, pos, rect.y + COLUMN_HEIGHT - 1, rect.x, rect.y + COLUMN_HEIGHT - 1, DrawUtil.COLOR_DARK);//нижняя линия для колонок
    }
}
