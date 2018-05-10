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

package org.radixware.kernel.explorer.utils;

import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QAbstractSpinBox;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QLayout;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleHintReturn;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QStyleOptionComplex;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QIcon.Mode;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLine;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStyle.PrimitiveElement;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.widgets.EmbeddedView;

public class WidgetUtils {
    
    private final static EnumSet<Qt.AlignmentFlag> HORIZONTAL_ALIGNMENTS = 
        EnumSet.of(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignRight, Qt.AlignmentFlag.AlignHCenter, Qt.AlignmentFlag.AlignJustify, Qt.AlignmentFlag.AlignAbsolute);
    private final static EnumSet<Qt.AlignmentFlag> VERTICAL_ALIGNMENTS = 
        EnumSet.of(Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignBottom, Qt.AlignmentFlag.AlignVCenter);    
    private final static Qt.Alignment TMP__ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignCenter);
    
    public final static QPoint ZERO_POINT = new QPoint(0,0);

    public final static WindowType MODAL_DIALOG_WINDOW_TYPE = getModalDailogWindowType();
    public final static WindowFlags WINDOW_FLAGS_FOR_DIALOG = calcWindowFlags();
    public final static int MODEL_ITEM_ROW_NAME_DATA_ROLE = Qt.ItemDataRole.UserRole*3 + 2;
    public final static int MODEL_ITEM_CELL_NAME_DATA_ROLE = Qt.ItemDataRole.UserRole*3 + 3;
    public final static int MODEL_ITEM_CELL_VALUE_DATA_ROLE = Qt.ItemDataRole.UserRole*3 + 4;
    public final static int MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE = Qt.ItemDataRole.UserRole*3 + 5;
    public final static int MAXIMUM_SIZE = 16777215;
    
    private final static QSize shrinkedDialogSize = new QSize();
    
    public static final class UpArrow{
        
        private final int topX;
        private final int topY;
        private final List<QLine> lines = new ArrayList<>();
        
        public UpArrow(final int centerX, final int top, final int height){
            topX = centerX;
            topY = top;
            for (int i=1; i<height; i++){
                lines.add(new QLine(centerX - i, top + i, centerX + i, top + i));
            }
        }
        
        public void draw(final QPainter painter){
            painter.drawPoint(topX, topY);
            painter.drawLines(lines);
        }
    }
    
    public static final class DownArrow{
        
        private final int downX;
        private final int downY;
        private final List<QLine> lines = new ArrayList<>();
        
        public DownArrow(final int centerX, final int top, final int height){
            downX = centerX;
            downY = top+height;
            for (int y=1, x=height-1; y<height; y++, x--){
                lines.add(new QLine(centerX - x, top + y, centerX + x, top + y));
            }
        }
        
        public void draw(final QPainter painter){
            painter.drawLines(lines);
            painter.drawPoint(downX, downY);
        }
    }    
    
    private static WindowType getModalDailogWindowType(){
        final String windowTypeParam = RunParams.getForceDialogWindowType();
        if ("window".equalsIgnoreCase(windowTypeParam)){
            return WindowType.Window;
        }else if ("dialog".equalsIgnoreCase(windowTypeParam)){
            return WindowType.Dialog;
        }else if (isGnome3Classic()){
            return WindowType.Window;
        }else{
            return WindowType.Dialog;
        }
    }
    
    private static WindowFlags calcWindowFlags(){
        final WindowFlags flags = new WindowFlags(MODAL_DIALOG_WINDOW_TYPE,WindowType.WindowSystemMenuHint,WindowType.WindowTitleHint);
        if (isGnome3Classic()){
            flags.set(WindowType.WindowCloseButtonHint);
        }        
        return flags;
    }
    
    public static boolean isGnome3Classic(){
        return "classic".equals(System.getenv("GNOME_SHELL_SESSION_MODE"));
    }

    public static QHBoxLayout createHBoxLayout(final QWidget parent) {
        final QHBoxLayout layout = new QHBoxLayout(parent);
        layout.setSpacing(0);
        layout.setMargin(0);
        return layout;
    }

    public static QVBoxLayout createVBoxLayout(final QWidget parent) {
        final QVBoxLayout layout = new QVBoxLayout(parent);
        layout.setSpacing(0);
        layout.setMargin(0);
        return layout;
    }

    public static QToolButton createEditorButton(final QWidget parent, final QAction action) {
        final QToolButton button = new QToolButton(parent);
        button.setDefaultAction(action);
        button.setFocusPolicy(FocusPolicy.NoFocus);
        button.setFixedWidth(20);
        button.setSizePolicy(new QSizePolicy(Policy.Expanding, Policy.Expanding));
        button.setAttribute(WidgetAttribute.WA_DeleteOnClose);
        if (action!=null && action.objectName()!=null && !action.objectName().isEmpty()){
            button.setObjectName("rx_tbtn_"+action.objectName());
        }
        return button;
    }

    public static WindowFlags createWindowFlagsForDialog() {
        final WindowFlags f = new WindowFlags();
        f.set(new WindowType[]{});
        return f;
    }

    public static void centerDialog(final QDialog dialog) {
        final QWidget parent;
        if (dialog.parentWidget() != null) {
            parent = dialog.parentWidget().window();
        } else if (Application.getMainWindow() != null) {
            parent = Application.getMainWindow();
        } else {
            parent = QApplication.desktop();
        }
        final QRect rec = dialog.rect();
        rec.moveCenter(parent.frameGeometry().center());
        dialog.move(rec.topLeft());
    }

    public static void removeChildrenWidgets(final QWidget parent) {
        final QLayout layout = parent.layout();
        if (layout != null) {
            for (int i = layout.count() - 1; i >= 0; i--) {
                if (layout.itemAt(i).widget() != null) {
                    layout.removeWidget(layout.itemAt(i).widget());
                }
            }
        }

        QWidget widget;
        for (int i = parent.children().size() - 1; i >= 0; i--) {
            if (parent.children().get(i) instanceof QWidget) {
                widget = (QWidget) parent.children().get(i);
                widget.close();
                widget.setParent(null);
            }
        }
    }

    public static void closeChildrenWidgets(final QWidget parent) {
        QWidget widget;
        for (int i = parent.children().size() - 1; i >= 0; i--) {
            if (parent.children().get(i) instanceof QWidget) {
                widget = (QWidget) parent.children().get(i);
                if (widget instanceof IModelWidget) {
                    widget.close();
                } else {
                    closeChildrenWidgets(widget);
                }
            }
        }
    }
    
    public static QRect calcPopupGeometry(final QWidget parent, final int height){
        final QRect screen = 
            QApplication.desktop().availableGeometry(QApplication.desktop().screenNumber(parent));        
        QPoint bottomLeft = parent.visibleRegion().boundingRect().bottomLeft();
        final int width = parent.width() - bottomLeft.x();
        bottomLeft = parent.mapToGlobal(bottomLeft);
        final QRect result = new QRect(bottomLeft, new QSize(width, height));
        if (screen.bottom()<result.bottom()){
            final QPoint topLeft = parent.mapToGlobal(parent.visibleRegion().boundingRect().topLeft());                
            result.moveTop(topLeft.y()-height);
        }
        return result;            
    }

    /*
     * Класс-обертка над qt-стилем для задания собственной отрисовки элементов
     */
    public static abstract class CustomStyle extends QStyle {
        
        private final static Collection<CustomStyle> CUSTOM_STYLES = new LinkedList<>();

        private final QWidget sourceWidget;

        public CustomStyle(final QWidget sourceWidget) {
            super();
            this.sourceWidget = sourceWidget;
            CUSTOM_STYLES.add(this);
        }
        
        private QStyle style() {
            return sourceWidget.style();
        }
        
        @Override
        public void drawComplexControl(final ComplexControl arg0, final QStyleOptionComplex arg1, final QPainter arg2, final QWidget arg3) {
            style().drawComplexControl(arg0, arg1, arg2, arg3);
        }

        @Override
        public void drawControl(final ControlElement element, final QStyleOption options, final QPainter p, final QWidget widget) {
            style().drawControl(element, options, p, widget);
        }

        @Override
        public void drawPrimitive(final PrimitiveElement primitive, final QStyleOption options, final QPainter painter, final QWidget widget) {
            style().drawPrimitive(primitive, options, painter, widget);
        }

        @Override
        public QPixmap generatedIconPixmap(final Mode arg0, final QPixmap arg1, final QStyleOption arg2) {
            return style().generatedIconPixmap(arg0, arg1, arg2);
        }

        @Override
        public int hitTestComplexControl(final ComplexControl arg0, final QStyleOptionComplex arg1, final QPoint arg2, final QWidget arg3) {
            return style().hitTestComplexControl(arg0, arg1, arg2, arg3);
        }

        @Override
        public int pixelMetric(final PixelMetric arg0, final QStyleOption arg1, final QWidget arg2) {
            if (arg0 == PixelMetric.PM_SpinBoxFrameWidth) {
                return 0;
            }
            return style().pixelMetric(arg0, arg1, arg2);
        }

        @Override
        public QSize sizeFromContents(final ContentsType arg0, final QStyleOption arg1, final QSize arg2, final QWidget arg3) {
            return style().sizeFromContents(arg0, arg1, arg2, arg3);
        }

        @Override
        public int styleHint(final StyleHint arg0, final QStyleOption arg1, final QWidget arg2, final QStyleHintReturn arg3) {
            return style().styleHint(arg0, arg1, arg2, arg3);
        }

        @Override
        public QRect subControlRect(final ComplexControl control, final QStyleOptionComplex option, final int subControl, final QWidget widget) {
            return style().subControlRect(control, option, subControl, widget);
        }

        @Override
        public QRect subElementRect(final SubElement arg0, final QStyleOption arg1, final QWidget arg2) {
            return style().subElementRect(arg0, arg1, arg2);
        }
        
        public static void release(final CustomStyle style){
            if (style!=null){
                CUSTOM_STYLES.remove(style);
                if (style.nativeId()!=0){
                    style.dispose();
                }
            }
        }
        
        public static void releaseAll(){
            for (CustomStyle style: CUSTOM_STYLES){
                if (style.nativeId()!=0){
                    style.dispose();
                }
            }
            CUSTOM_STYLES.clear();
        }
    }
    
    public static int calcTextWidth(final String text, final QFontMetrics fontMetrics, final boolean approximately){
        if (text==null || text.isEmpty()){
            return 0;
        }else{
            if (approximately){
                return text.length()*text.length();
            }else{
                return fontMetrics.width(text);
            }
        }
    }

    public static QColor mergedColors(final QColor colorA, final QColor colorB, final int factor) {
        final int maxFactor = 100;
        QColor tmp = colorA;
        tmp.setRed((tmp.red() * factor) / maxFactor + (colorB.red() * (maxFactor - factor)) / maxFactor);
        tmp.setGreen((tmp.green() * factor) / maxFactor + (colorB.green() * (maxFactor - factor)) / maxFactor);
        tmp.setBlue((tmp.blue() * factor) / maxFactor + (colorB.blue() * (maxFactor - factor)) / maxFactor);
        return tmp;
    }
    public final static int MAXIMUM_DARKER_COLOR_FACTOR = 100;

    @SuppressWarnings("PMD.AssignmentInOperand")
    public static QColor getDarkerColor(final QColor color, final int factor) {
        int red = color.red();
        int green = color.green();
        int blue = color.blue();

        final int sub = (125 * factor) / MAXIMUM_DARKER_COLOR_FACTOR;

        if ((red -= sub) > 255) {
            red %= 255;
        } else if (red < 0) {
            red = java.lang.Math.abs(red);
        }
        if ((green -= sub) > 255) {
            green %= 255;
        } else if (green < 0) {
            green = java.lang.Math.abs(green);
        }
        if ((blue -= sub) > 255) {
            blue %= 255;
        } else if (blue < 0) {
            blue = java.lang.Math.abs(blue);
        }

        return new QColor(red, green, blue);
    }

    public static void updateActionToolTip(IClientEnvironment environment, final QAction action) {
        action.setToolTip(ClientValueFormatter.capitalizeIfNecessary(environment, action.text()));
    }

    public static void updateActionToolTip(final QAction action) {
        updateActionToolTip(Application.getInstance().getEnvironment(), action);
    }

    public static void closeChildrenEmbeddedViews(final Model owner, final QWidget parent) {
        final List<QObject> embeddedViews = parent.findChildren(EmbeddedView.class);
        EmbeddedView embeddedView;
        for (QObject obj : embeddedViews) {
            embeddedView = (EmbeddedView) obj;
            if (embeddedView.isOpened()) {
                final String message = owner.getEnvironment().getMessageProvider().translate("TraceMessage", "Embedded view '%s' was not closed in custom view for '%s' (#%s) model: closing forcedly");
                owner.getEnvironment().getTracer().warning(String.format(message, embeddedView.objectName(), owner.getTitle(), owner.getDefinition().getId()));
                embeddedView.close(true);
            } else {
                embeddedView.cancelAsyncActions();
            }
        }
    }
    
    public static List<IEmbeddedView> findExplorerViews(final QWidget widget){
        final List<IEmbeddedView> result = new LinkedList<>();
        final List<EmbeddedView> openedViews = EmbeddedView.findOpenedEmbeddedViews(widget, false);
        for (EmbeddedView embeddedView: openedViews){
            result.add(embeddedView);
        }
        return result;
    }

    public static List<QObject> findChildren(final QObject qobject, final Class<?> cl, final String name) {
        final List<QObject> children = qobject.children();
        final List<QObject> matching = new ArrayList<>();
        for (QObject current : children) {
            if (current != null) {
                if ((name == null || name.equals(current.objectName()))
                        && (cl == null || cl.isAssignableFrom(current.getClass()))) {
                    matching.add(current);
                }
                matching.addAll(findChildren(current, cl, name));
            }
        }
        return matching;
    }

    public static List<QObject> findChildren(final QObject qobject, final Class<?> cl) {
        return findChildren(qobject, cl, null);
    }

    public static void disconnectSignalRecursively(final QSignalEmitter.AbstractSignal signal, final QWidget parentWidget) {
        signal.disconnect(parentWidget);
        final List<QObject> children = findChildren(parentWidget, null, null);
        for (QObject child : children) {
            signal.disconnect(child);
        }
    }
    
    public static void applyTextOptions(final ExplorerTextOptions options, final QWidget widget){
        final QFont optionsFont = options.getFont()==null ? null : options.getFont().getQFont();
        if (optionsFont!=null){
            widget.setFont(optionsFont);
        }
        if (widget instanceof QAbstractSpinBox){          
            final QLineEdit editor = (QLineEdit)widget.findChild(QLineEdit.class, "qt_spinbox_lineedit");
            if (editor!=null){
                applyTextOptions(options, editor);
                if (optionsFont!=null && widget.styleSheet().isEmpty()){
                    widget.setStyleSheet(options.getStyleSheet("QLineEdit"));
                    widget.setFont(optionsFont);
                    widget.setStyleSheet("");
                }
                return;
            }
        }
        if (widget.isEnabled()){
            final QFont font = optionsFont==null ? widget.font() : optionsFont;
            widget.setStyleSheet(options.getStyleSheet());
            if (!font.toString().equals(widget.font().toString())){
                widget.setStyleSheet("");
                widget.setFont(font);
                widget.setStyleSheet(options.getStyleSheet());
            }
        }else{
            widget.setStyleSheet("");
        }
    }
    
    public static void applyTextOptions(final ExplorerTextOptions options, final QTreeWidgetItem item, final int column){
        if (options.getQFont()!=null){
            item.setFont(column, options.getQFont());
        }
        if (!item.isDisabled()){
            if (options.getForegroundBrush()!=null){
                item.setForeground(column, options.getForegroundBrush());
            }
            if (options.getBackgroundBrush()!=null){
                item.setBackground(column, options.getBackgroundBrush());
            }
        }
    }
    
    public static void applyTextOptions(final ExplorerTextOptions options, final QTableWidgetItem item){
        if (options.getQFont()!=null){
            item.setFont(options.getQFont());
        }
        final Qt.AlignmentFlag alignment = options.getQAlignment();
        if (alignment!=null){
            item.setTextAlignment(alignment.value());
        }
        if (item.flags().isSet(Qt.ItemFlag.ItemIsEnabled)){
            if (options.getBackgroundBrush()!=null){
                item.setBackground(options.getBackgroundBrush());
            }
            if (options.getForegroundBrush()!=null){
                item.setForeground(options.getForegroundBrush());
            }
        }        
    }
    
    public static void applyTextOptions(final QWidget widget, final ETextOptionsMarker...markers){
        if (widget instanceof QLabel){
            applyLabelOptions((QLabel)widget,markers);
        }else if (widget instanceof QCheckBox){
            applyTextOptions(ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE), widget);
        }else{
            applyTextOptions(ExplorerTextOptions.Factory.getOptions(markers), widget);
        }
    }
    
    public static void applyDefaultTextOptions(final QWidget widget){
        if (widget instanceof QLabel){
            applyLabelOptions((QLabel)widget, ETextOptionsMarker.LABEL);
        }else if (widget instanceof QCheckBox){
            applyTextOptions(ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE), widget);
        }else{
            applyTextOptions(ExplorerTextOptions.Factory.getDefault(), widget);
        }
    }
    
    public static void applyLabelOptions(final QLabel label, final ETextOptionsMarker...markers){
        applyTextOptions(ExplorerTextOptions.Factory.getLabelOptions(markers), label);
    }    
    
    public static void applyTextOptions(final QLabel label, final ETextOptionsMarker...markers){
        applyLabelOptions(label, markers);
    }           
    
    public static QColor awtColor2qtColor(final Color color){
        return QColor.fromRgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static Dimension getWndowMaxSize(){
        final QSize maxSize = shrinkWindowSize(MAXIMUM_SIZE, MAXIMUM_SIZE);
        return new Dimension(maxSize.width(), maxSize.height());
    }
    
    public static Model findNearestModel(final QWidget widget){
        for (QWidget w = widget; w!=null; w = w.parentWidget()){
            if (w instanceof IView){
                return ((IView)w).getModel();
            }
        }
        return null;
    }
    
    public static QRect awtRect2QRect(final Rectangle rect, final QRect result){
        final QRect qRect = result==null ? new QRect() : result;
        qRect.setRect(rect.x, rect.y, rect.width, rect.height);
        return qRect;
    }
    
    public static QRectF awtRect2TmpRectF(final Rectangle rect, final QRectF result){
        final QRectF qRect = result==null ? new QRectF() : result;
        qRect.setRect(rect.x, rect.y, rect.width, rect.height);
        return qRect;
    }    
    
    public static boolean isParentIndex(final QModelIndex parent, final QModelIndex child) {
        if (child == null) {
            return false;
        }
        if (parent == null) {
            return true;
        }
        if (parent.internalId() == child.internalId()) {
            return true;
        }
        for (QModelIndex index = child.parent(); index != null; index = index.parent()) {
            if (parent.internalId() == index.internalId()) {
                return true;
            }
        }
        return false;
    }    
    
    public static String getQtFileDialogFilter(final EnumSet<EMimeType> types){
        return getQtFileDialogFilter(types, null);
    }
    
    public static String getQtFileDialogFilter(final EnumSet<EMimeType> types, final MessageProvider messageProvider){
        if (types==null || types.isEmpty()){
            return null;
        }else{
            final StringBuilder filterBuilder = new StringBuilder();            
            for (EMimeType type: types){
                final String qtFilter;
                if (type==EMimeType.ALL_SUPPORTED){
                    final String allFormats = getAllSupportedFormatsFilter(types);
                    if (allFormats!=null && !allFormats.isEmpty()){
                        qtFilter = "("+allFormats+")";
                    }else{
                        continue;
                    }
                }else{
                    qtFilter = type.getQtFilter();
                }
                
                if (filterBuilder.length()>0){
                    filterBuilder.append(";;");
                }
                if (messageProvider==null){
                    filterBuilder.append(type.getTitle());
                }else{
                    filterBuilder.append(getLocalizedQtFileFilter(type, messageProvider));
                }
                filterBuilder.append(' ');
                filterBuilder.append(qtFilter);
            }
            return filterBuilder.toString();
        }
    }
    
    private static String getLocalizedQtFileFilter(final EMimeType type, final MessageProvider provider){
        /*switch(type){
            case ALL_SUPPORTED:
                return provider.translate("ExplorerDialog", "All Supported Formats");
            case ALL_FILES:
                return provider.translate("ExplorerDialog", "All Files");
            default:
                return type.getTitle();
        }*/
        return type.getTitle();
    }
    
    private static String getAllSupportedFormatsFilter(final EnumSet<EMimeType> types){
        final StringBuilder qtFilterBuilder = new StringBuilder();
        String filter;
        for (EMimeType type: types){
            if (type!=EMimeType.ALL_SUPPORTED && type!=EMimeType.ALL_FILES){
                filter = type.getQtFilter();
                if (filter!=null && !filter.isEmpty()){
                    if (qtFilterBuilder.length()>0){
                        qtFilterBuilder.append(' ');
                    }
                    qtFilterBuilder.append(filter.replace("(", "").replace(")", ""));
                }
            }
        }
        return qtFilterBuilder.toString();
    }
    
    public static void pauseAsyncGroupModelReadersBeforeShowDropDownList(final QWidget component){
        for (QWidget widget=component; widget!=null && !widget.isWindow(); widget=widget.parentWidget()){
            if (widget instanceof ISelector){
                final GroupModel groupModel = (GroupModel)((ISelector)widget).getModel();
                if (groupModel!=null){
                    groupModel.getAsyncReader().block();
                }
            }
        }
    }
    
    public static void resumeAsyncGroupModelReadersAfterHideDropDownList(final QWidget component){
        for (QWidget widget=component; widget!=null && !widget.isWindow(); widget=widget.parentWidget()){
            if (widget instanceof ISelector){
                final GroupModel groupModel = (GroupModel)((ISelector)widget).getModel();
                if (groupModel!=null){
                    groupModel.getAsyncReader().unblock();
                }
            }
        }        
    }
    
    public static boolean isSetHorizontalAlignmentFlag(final Qt.Alignment alignment){
        for (Qt.AlignmentFlag alignmentFlag: HORIZONTAL_ALIGNMENTS){
            if (alignment.isSet(alignmentFlag)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSetVerticalAlignmentFlag(final Qt.Alignment alignment){
        for (Qt.AlignmentFlag alignmentFlag: VERTICAL_ALIGNMENTS){
            if (alignment.isSet(alignmentFlag)){
                return true;
            }
        }
        return false;
    }    
    
    public static int calcWindowHeaderWidth(final QDialog dialog){
        return dialog.fontMetrics().width(dialog.windowTitle()) + 200;
    }
    
    public static QSize shrinkWindowSize(final int width, final int height){
        final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();        
        final int screenWidth = screenSize.width();
        if (screenWidth<=1024){
            shrinkedDialogSize.setWidth(Math.min(width, screenWidth));
        }else{
            shrinkedDialogSize.setWidth(Math.min(width, screenWidth*3/4));
        }
        final int screenHeight = screenSize.height();
        if (screenHeight<=800){
            shrinkedDialogSize.setWidth(Math.min(width, screenHeight - 50));
        }else{
            shrinkedDialogSize.setHeight(Math.min(height, screenHeight*3/4));
        }
        return shrinkedDialogSize;
    }
    
    public static String calcWidgetXPath(final QWidget widget){
        final StringBuilder xpathBuilder = new StringBuilder();
        String part;
        for (QWidget w=widget; w!=null; w=w.parentWidget()){
            xpathBuilder.insert(0, getWidgetIndex(w));
            xpathBuilder.insert(0,'/');
        }
        return xpathBuilder.toString();
    }
    
    public static void updateToolButtonObjectName(final QToolBar toolBar, final QAction action){
        final QWidget button = toolBar.widgetForAction(action);
        if (button!=null){
            final String buttonName = button.objectName();
            final String actionName = action.objectName();
            if ((buttonName==null || buttonName.isEmpty())
                && actionName!=null && !actionName.isEmpty()){
                button.setObjectName("rx_tbtn_" + actionName);
            }
        }
    }
    
    public static int getQtAlignmentValue(Qt.AlignmentFlag... flags){
        TMP__ALIGNMENT.clearAll();
        TMP__ALIGNMENT.set(flags);
        return TMP__ALIGNMENT.value();
    }
    
    private static String getWidgetIndex(final QWidget widget){
        final QWidget parentWidget = widget.parentWidget();
        final String tagName = widget.getClass().getSimpleName();
        if (parentWidget==null){
            return tagName;
        }else{            
            final String objectName = widget.objectName();
            int index = 0;
            boolean byIndex = objectName==null || objectName.isEmpty();
            final List<QObject> children = parentWidget.children();            
            for (QObject child: children){
                if (widget==child){
                    break;
                }
                if (child.nativeId()==0){
                    continue;
                }
                if (tagName.equals(child.getClass().getSimpleName())){
                    index++;
                    if (Objects.equals(objectName, child.objectName())){
                        byIndex = true;
                    }
                }
            }
            if (byIndex){
                return tagName+"["+String.valueOf(index+1)+"]";
            }else{
                return tagName+"[@objectName=\'"+objectName+"\']";
            }
        }
    }
}
