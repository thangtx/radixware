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

package org.radixware.kernel.explorer.text;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.text.IFont;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public final class ExplorerTextOptions extends TextOptions {
    
    public static final ExplorerTextOptions EMPTY = TextOptionsManager.getInstance().getOptions(null, null, null, null);
    
    public static class Factory{
        
        public static ExplorerTextOptions getDefault(){
            return (ExplorerTextOptions)DefaultTextOptionsProvider.getInstance().getDefaultOptions();
        }
        
        public static ExplorerTextOptions getOptions(final ExplorerFont font, final ETextAlignment alignment,final Color foreground, final Color background) {
            return TextOptionsManager.getInstance().getOptions(font, alignment, foreground, background);
        }
        
        public static ExplorerTextOptions getOptions(final ExplorerFont font, final ETextAlignment alignment,final Color foreground) {
            return getOptions(font,alignment,foreground,null);
        }

        
        public static ExplorerTextOptions getOptions(final  ExplorerFont font, final  ETextAlignment alignment) {
            return getOptions(font,alignment,null,null);
        }

        
        public static ExplorerTextOptions getOptions(final  ExplorerFont font, final Color foreground, final  Color background) {
            return getOptions(font,null,foreground,background);
        }

        
        public static ExplorerTextOptions getOptions(final ExplorerFont font, final Color foreground) {
            return getOptions(font,null,foreground,null);
        }

        
        public static ExplorerTextOptions getOptions(final  ExplorerFont font) {
            return getOptions(font,null,null,null);
        }

        
        public static ExplorerTextOptions getOptions(final  Color foreground, final Color background) {
            return getOptions(null,null,foreground,background);
        }

        
        public static ExplorerTextOptions getOptions(final Color foreground) {
            return getOptions(null,null,foreground,null);
        }

        
        public static ExplorerTextOptions getOptions(final ETextAlignment alignment) {
            return getOptions(null,alignment,null,null);
        }

        
        public static ExplorerTextOptions getOptions(final ETextAlignment alignment, final Color foreground, final Color background) {
            return getOptions(null,alignment,foreground,background);
        }
        
        public static ExplorerTextOptions getOptions(final ETextAlignment alignment, final Color foreground) {
            return getOptions(null,alignment,foreground,null);
        }
        
        public static ExplorerTextOptions getOptions(final ETextOptionsMarker...markers){
            return DefaultTextOptionsProvider.getInstance().getOptions(markers);
        }

        public static ExplorerTextOptions getLabelOptions(final ETextOptionsMarker...markers){
            EnumSet<ETextOptionsMarker> markersSet = EnumSet.of(ETextOptionsMarker.LABEL);
            markersSet.addAll(Arrays.asList(markers));
            return DefaultTextOptionsProvider.getInstance().getOptions(markersSet);
        }        
    }
    
    ExplorerTextOptions(final ExplorerFont fnt, 
                final ETextAlignment align, 
                final Color fg, 
                final Color bg){
        super(fnt,align,fg,bg);        
    }

    @Override
    public ExplorerFont getFont() {
        return (ExplorerFont)super.getFont();
    }
    
    protected TextOptionsManager textOptionsManager() {
        return TextOptionsManager.getInstance();
    }

    
    protected ExplorerFontManager fontManager() {
        return ExplorerFontManager.getInstance();
    }     
    
    public String getStyleSheet(){
        return getStyleSheet(null);
    }
    
    public String getStyleSheet(final String styleSheetSelector){
        final StringBuilder builder = new StringBuilder();
        //font string format is not the same as expected in stylesheet
        if (styleSheetSelector!=null){
            builder.append(styleSheetSelector);
            builder.append(" { ");
        }
        if (getForegroundColor() != null) {
            builder.append("color: ").append(TextOptions.color2Str(getForegroundColor())).append(";");
        }
        if (getBackgroundColor() != null) {
            builder.append("background-color: ").append(TextOptions.color2Str(getBackgroundColor())).append(";");
        }
        if (styleSheetSelector!=null){
            builder.append(" }");
        }
        return builder.toString();        
    }
    
    public QColor getForeground(){
        return awtColor2QColor(getForegroundColor());
    }
    
    public QColor getBackground(){
        return awtColor2QColor(getBackgroundColor());
    }    
    
    private static QColor awtColor2QColor(final Color color){
        if (color==null){
            return null;
        }
        return ExplorerSettings.getQColor(TextOptions.color2Str(color));
    }
    
    public QBrush getForegroundBrush(){
        return awtColor2QBrush(getForegroundColor());
    }
    
    public QBrush getBackgroundBrush(){
        return awtColor2QBrush(getBackgroundColor());
    }
    
    private static QBrush awtColor2QBrush(final Color color){
        if (color==null){
            return null;
        }
        return ExplorerSettings.getQBrush(TextOptions.color2Str(color));
    }
    
    public QFont getQFont(){
        return getFont()==null ? null : getFont().getQFont();
    }
    
    public Qt.AlignmentFlag getQAlignment(){
        final ETextAlignment alignment = getAlignment();
        if (alignment==null){
            return null;
        }
        switch(alignment){
            case CENTER:
                return Qt.AlignmentFlag.AlignCenter;
            case LEFT:
                return Qt.AlignmentFlag.AlignLeft;
            case RIGHT:
                return Qt.AlignmentFlag.AlignRight;
            default:
                throw new IllegalUsageError("Failed to convert \'"+alignment.name()+"\' to Qt alignment");
        }
    }
    
    public static Color qtColor2awtColor(final QColor color){
        return color==null ? null : Color.decode(color.name());
    }
    
    public static ExplorerTextOptions getDefault(){
        return Factory.getDefault();
    }
    
    @Override
    public ExplorerTextOptions changeForegroundColor(final Color color) {
        if (Objects.equals(getForegroundColor(), color)){
            return this;
        }
        return textOptionsManager().getOptions(getFont(), getAlignment(), color, getBackgroundColor());
    }

    @Override
    public ExplorerTextOptions changeBackgroundColor(final Color color) {
        if (Objects.equals(getBackgroundColor(), color)){
            return this;
        }
        return textOptionsManager().getOptions(getFont(), getAlignment(), getForegroundColor(), color);
    }

    @Override
    public ExplorerTextOptions changeAlignment(final ETextAlignment alignment) {
        if (Objects.equals(getAlignment(), alignment)){
            return this;
        }
        return textOptionsManager().getOptions(getFont(), alignment, getForegroundColor(), getBackgroundColor());
    }

    @Override
    public ExplorerTextOptions changeFont(final IFont font) {
        if (Objects.equals(getFont(), font)){
            return this;
        }
        return textOptionsManager().getOptions(font, getAlignment(), getForegroundColor(), getBackgroundColor());
    }

    @Override
    public ExplorerTextOptions changeFontFamily(final String newFamily) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getFont(newFamily), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else if (!getFont().getFamily().equals(newFamily)){
            return textOptionsManager().getOptions(getFont().changeFamily(newFamily), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return this;
        }
    }

    @Override
    public ExplorerTextOptions changeFontSize(final int newSize) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getFont(newSize), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else if (getFont().getSize()!=newSize){
            return textOptionsManager().getOptions(getFont().changeSize(newSize), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return this;
        }
    }

    @Override
    public ExplorerTextOptions changeFontWeight(final EFontWeight weight) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getFont(weight), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else if (getFont().getWeight()!=weight){
            return textOptionsManager().getOptions(getFont().changeWeight(weight), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return this;
        }
    }

    @Override
    public ExplorerTextOptions changeFontStyle(final EFontStyle style) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getFont(style), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else if (getFont().getStyle()!=style){
            return textOptionsManager().getOptions(getFont().changeStyle(style), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return this;
        }
    }

    @Override
    public ExplorerTextOptions changeFontDecoration(final EnumSet<ETextDecoration> decoration) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getFont(EFontStyle.NORMAL, decoration), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else if (getFont().getDecoration()!=decoration){
            return textOptionsManager().getOptions(getFont().changeDecoration(decoration), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return this;
        }
    }

    @Override
    public ExplorerTextOptions increaseFontSize(int size) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getDefaultFont().increaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return textOptionsManager().getOptions(getFont().increaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public ExplorerTextOptions decreaseFontSize(int size) {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getDefaultFont().decreaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return textOptionsManager().getOptions(getFont().decreaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public ExplorerTextOptions getWithBoldFont() {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getDefaultFont().getBold(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return textOptionsManager().getOptions(getFont().getBold(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public ExplorerTextOptions getWithItalicFont() {
        if (getFont()==null){
            return textOptionsManager().getOptions(fontManager().getDefaultFont().getItalic(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }else{
            return textOptionsManager().getOptions(getFont().getItalic(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }
    
    public static QColor getDarker(final Color color, final int factor){
        return awtColor2QColor(getDarkerColor(color, factor));
    }
    
    public void applyTo(final QWidget widget){
        WidgetUtils.applyTextOptions(this, widget);
    }    
    
    public void applyTo(final QTableWidgetItem item){
        WidgetUtils.applyTextOptions(this, item);
    }
    
    public void applyTo(final QTreeWidgetItem item, final int column){
        WidgetUtils.applyTextOptions(this, item, column);
    }
}
