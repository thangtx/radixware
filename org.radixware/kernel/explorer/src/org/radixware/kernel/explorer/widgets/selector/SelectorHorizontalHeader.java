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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionButton;
import com.trolltech.qt.gui.QStyleOptionHeader;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.EQtStyle;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.HeaderWithCheckBox;


final class SelectorHorizontalHeader extends HeaderWithCheckBox implements IModelWidget{
    
    private final static String STATE_KEY = "headerState";
    private final static String FORMAT_VERSION_KEY = "formatVersion";
    private final static int CURRENT_FORMAT_VERSION = 1;    
    private final static String SECTIONS_KEY = "settings";    
        
    private final static Qt.Alignment ALIGNMENT_LEFT = new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter, Qt.AlignmentFlag.AlignLeft);
    private final static QCursor SPLIT_CURSOR = new QCursor(Qt.CursorShape.SplitHCursor);    
    
    public final QSignalEmitter.Signal2<Integer,Boolean> sectionVisibilityChanged = new QSignalEmitter.Signal2<>();
    public final QSignalEmitter.Signal1<Integer> resizeColumnByContent = new QSignalEmitter.Signal1<>();
    private final QStyleOptionHeader options = new QStyleOptionHeader();    
    private final SelectorHeaderStyle style;    
    private final ExplorerSettings settings;
    private final String settingsGroup;        
    private final boolean firstSelectorColumnMustBeVisible;
    private final int firstSelectorColumnIndex;
    private boolean columnsSizePolicyUpdateBlocked;
    private boolean wasBinded;
    private boolean forceStretchLastVisibleColumn;
    private int selectionColumnWidth = -1;    
    private int resizingSectionIndex=-1;    
    private int originalSize;
    private int firstPos;
        
    public SelectorHorizontalHeader(final QWidget parent, final SelectorModel model, final boolean firstColumnVisible, final boolean isSelectionEnabled){
        super(Qt.Orientation.Horizontal, parent);        
        options.setOrientation(Qt.Orientation.Horizontal);
        options.setTextAlignment(ALIGNMENT_LEFT);
        setHighlightSections(false);
        setTextElideMode(Qt.TextElideMode.ElideRight);//RADIX-407
        setStretchLastSection(false);
        setMovable(true);
        setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        style = new SelectorHeaderStyle(parent, model);        
        setStyle(style);
        settings = (ExplorerSettings)model.getEnvironment().getConfigStore();
        settingsGroup = 
            model.getRootGroupModel().getConfigStoreGroupName()+"/"+SettingNames.SYSTEM+"/"+SettingNames.Selector.COLUMNS_GROUP;
        firstSelectorColumnMustBeVisible  = firstColumnVisible;
        firstSelectorColumnIndex = isSelectionEnabled ? 1 : 0;
    }
    
    public SelectorHorizontalHeader(final QWidget parent, final SelectorModel model, final boolean firstColumnVisible){
        this(parent, model, firstColumnVisible, true);
    }
       
    @Override
    protected QSize sectionSizeFromContents(final int logicalIndex) {        
        final QSize size = super.sectionSizeFromContents(logicalIndex);
        if (logicalIndex<firstSelectorColumnIndex){
            size.setWidth(18);
        } else if (sortingIndicatorShown(logicalIndex)){
            options.setSection(logicalIndex);
            final QRect rect = style().subElementRect(QStyle.SubElement.SE_HeaderArrow, options, this);
            if (rect!=null && rect.isValid()){
                size.setWidth(size.width()+SelectorHeaderStyle.SORTING_INDICATOR_MARGIN+rect.width());
            }
        }
        return size;
    }

    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        final int columnCount = count();
        final int defaultSectionSize = defaultSectionSize();
        int width = frameWidth()*2;        
        for (int column = columnCount - 1; column >= 0; --column) {
            final int logical = logicalIndex(column);
            if (!isSectionHidden(logical)) {
                width += Math.max(sectionSizeFromContents(logical).width(), defaultSectionSize);
            }
        }
        if (width>size.width()){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(width, (int)sizeLimit.getWidth()));            
        }
        return size;
    }
                
    private boolean sortingIndicatorShown(final int logicalIndex){
        return model().headerData(logicalIndex, Qt.Orientation.Horizontal, SelectorModel.SORT_INDICATOR_ITEM_ROLE)!=null;
    }
    
    @Override
    protected void mousePressEvent(final QMouseEvent e) {
        final boolean leftButton = e.buttons().isSet(Qt.MouseButton.LeftButton);
        final int pos = e.x();
        if (resizingSectionIndex<0 && leftButton){
            final int handle = sectionHandleAt(pos);
            if (handle>-1){
                final int sectionToResize = findSectionToResize(handle);
                if (sectionToResize>-1){
                    originalSize = sectionSize(sectionToResize);
                    resizingSectionIndex = sectionToResize;
                    firstPos = pos;
                }
                e.accept();
                return;
            }else if (e.modifiers().value()==Qt.KeyboardModifier.ControlModifier.value()){
                final int sectionToResize = logicalIndexAt(pos);
                if (resizeSectionByContent(sectionToResize)){
                    e.accept();
                    return;
                }
            }
        }
        if (leftButton && firstSelectorColumnIndex>visualIndexAt(pos)){
            setMovable(false);
            setClickable(false);
            try{
                super.mousePressEvent(e);
            }finally{
                setMovable(true);
                setClickable(true);
            }
        }else{
            super.mousePressEvent(e);
        }
    }

    @Override
    protected void mouseDoubleClickEvent(final QMouseEvent event) {
        if (event.buttons().isSet(Qt.MouseButton.LeftButton)){
            final int pos = event.x();
            final int handle = sectionHandleAt(pos);
            resizeSectionByContent(handle);
            event.accept();
            return;
        }        
        super.mouseDoubleClickEvent(event);
    }
    
    private boolean resizeSectionByContent(final int section){
        if (section>=firstSelectorColumnIndex && resizeMode(section)!=QHeaderView.ResizeMode.Stretch){
            resizeColumnByContent.emit(section);
            return true;            
        }else{
            return false;
        }
    }

    @Override
    protected void mouseReleaseEvent(final QMouseEvent e) {
        if (resizingSectionIndex>-1){
            resizingSectionIndex = -1;
            e.accept();
        }else{
            super.mouseReleaseEvent(e);
        }
    }    
       
    @Override
    protected void mouseMoveEvent(final QMouseEvent e) {
        final boolean someButton = e.buttons().isSet(Qt.MouseButton.LeftButton);
        if (resizingSectionIndex>-1){
            if (someButton){
                final int pos = e.x();
                if (pos>=0){
                    int delta = isRightToLeft() ? firstPos - pos : pos - firstPos;
                    boolean sizePolicyWasChanged = false;
                    columnsSizePolicyUpdateBlocked = true;
                    try{
                        sizePolicyWasChanged = updateStretchPolicy();
                        sizePolicyWasChanged = updateSectionSizePolicy(resizingSectionIndex) || sizePolicyWasChanged;
                    }finally{
                        columnsSizePolicyUpdateBlocked = false;
                    }
                    if (sizePolicyWasChanged){
                        updateColumnsSizePolicy();
                    }
                    resizeSection(resizingSectionIndex, Math.max(originalSize+delta, minimumSectionSize()));
                }
            }else{
                resizingSectionIndex = -1;                
            }
            e.accept();
        }else{
            final int pos = e.x();
            final int handle = sectionHandleAt(pos);            
            if (handle>-1){
                final boolean hasCursor = testAttribute(Qt.WidgetAttribute.WA_SetCursor);
                if (someButton){
                    if (hasCursor){
                        setCursor(null);
                        setAttribute(Qt.WidgetAttribute.WA_SetCursor, false);                    
                    }                    
                }else{
                    if (findSectionToResize(handle)>-1){
                        if (!hasCursor){
                            setCursor(SPLIT_CURSOR);
                        }
                    }else if (hasCursor){
                        setCursor(null);
                        setAttribute(Qt.WidgetAttribute.WA_SetCursor, false);                    
                    }                    
                }
                e.accept();
                return;
            }else if (someButton && firstSelectorColumnIndex>visualIndexAt(pos)){
                e.accept();
                return;                
            }
            super.mouseMoveEvent(e);
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent closeEvent) {
        if (wasBinded){
            final List<SelectorColumnModelItem> columns = getSelectorColumns();
            for (SelectorColumnModelItem column: columns){
                column.unsubscribe(this);
            }
        }
        super.closeEvent(closeEvent);
    }
            
    private int sectionHandleAt(final int position){
        final int visual = visualIndexAt(position);
        if (visual == -1){
            return -1;
        }
        final int log = logicalIndex(visual);
        final int pos = sectionViewportPosition(log);
        final int grip = style().pixelMetric(QStyle.PixelMetric.PM_HeaderGripMargin, null, this);

        final boolean atLeft,atRight;
        if (isRightToLeft()){
            atLeft = (position > pos + sectionSize(log) - grip);
            atRight = position < pos + grip;
        }else{
            atLeft = position < pos + grip;
            atRight = (position > pos + sectionSize(log) - grip);            
        }     
        if (atLeft){
            return previousVisibleSection(log);
        }
        else if (atRight){
            return log;
        }else{
            return -1;
        }
    }
    
    private int previousVisibleSection(final int logicalIndex){
        int visual = visualIndex(logicalIndex);
        while(visual>-1){
            final int result = logicalIndex(--visual);
            if (!isSectionHidden(result)){
                return result;
            }
        }
        return -1;
    }
    
    private int nextVisibleSection(final int logicalIndex){
        int visual = visualIndex(logicalIndex);
        while(visual < this.count()) {
            final int result = logicalIndex(++visual);
            if (result<0){
                break;
            }
            if (!isSectionHidden(result)){
                return result;
            }
        }
        return -1;
    }
    
    private boolean updateStretchPolicy(){
        boolean sizePolicyWasChanged = false;
        for (int i=0,c=count(); i<c; i++){
            if (resizeMode(i)==QHeaderView.ResizeMode.Stretch){
                final SelectorColumnModelItem column  = getSelectorColumn(i);
                if (column!=null){
                    column.setSizePolicy(ESelectorColumnSizePolicy.MANUAL_RESIZE);
                    sizePolicyWasChanged = true;
                }
            }
        }
        forceStretchLastVisibleColumn = true;
        return sizePolicyWasChanged;
    }
    
    private boolean updateSectionSizePolicy(final int section){
        if (resizeMode(section)==QHeaderView.ResizeMode.ResizeToContents){
            final SelectorColumnModelItem column  = getSelectorColumn(section);
            if (column!=null){
                column.setSizePolicy(ESelectorColumnSizePolicy.MANUAL_RESIZE);
                if (column.getSizePolicy()==ESelectorColumnSizePolicy.MANUAL_RESIZE){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void refresh(final ModelItem item) {
        if (item instanceof SelectorColumnModelItem) {
            final List<SelectorColumnModelItem> columns = getSelectorColumns();
            final int columnIndex = columns.indexOf(item);
            final int sectionIndex = columnIndex + firstSelectorColumnIndex;
            boolean columnVisibleChanged = false;
            if (columnIndex >= 0) {
                final SelectorColumnModelItem column = columns.get(columnIndex);
                if (column.isVisible() == isSectionHidden(sectionIndex)) {
                    columnVisibleChanged = true;
                    setSectionHidden(sectionIndex, !column.isVisible());
                    sectionVisibilityChanged.emit(sectionIndex, column.isVisible());
                }
                if (columnVisibleChanged){
                    if (column.isVisible() && (sectionIndex==getLastVisibleColumnIndex() || sectionIndex==getFirstVisibleColumnIndex())){
                        fixSectionSize(sectionIndex);
                    }
                    if (forceStretchLastVisibleColumn){
                        columnsSizePolicyUpdateBlocked = true;
                        try{
                            updateStretchPolicy();
                        }finally{
                            columnsSizePolicyUpdateBlocked = false;
                        }
                    }                    
                    updateColumnsSizePolicy();
                }
                parentWidget().update();
            }
        }
    }
    
    private void fixSectionSize(final int sectionIndex){
        final int stretchColumnIndex = getStretchColumnIndex();
        if (stretchColumnIndex>-1){
            final int stretchColumnSize = sectionSize(stretchColumnIndex);
            resizeSection(sectionIndex, 100);
            resizeSection(stretchColumnIndex, stretchColumnSize-10);                                
        }        
    }

    @Override
    public boolean setFocus(final Property aThis) {
        return false;
    }

    @Override
    public void bind() {        
        setClickable(true);
        if (firstSelectorColumnIndex>0){
            ((SelectorModel)model()).enableSelection();
            setResizeMode(0,QHeaderView.ResizeMode.Fixed);
            resizeSection(0, getSelectionColumnWidth());
            setSectionUserCheckable(0, true);
            setSectionHidden(0, true);
        }
        
        {
            final List<SelectorColumnModelItem> columns = getSelectorColumns();
            boolean visible = false;            
            for (int i = 0; i < columns.size(); ++i) {
                if (!visible) {
                    visible = columns.get(i).isVisible();
                }
                setSectionHidden(i+firstSelectorColumnIndex, !columns.get(i).isVisible());
            }
            if ((firstSelectorColumnMustBeVisible || !visible) && !columns.isEmpty()) {
                final SelectorColumnModelItem firstColumn = columns.get(0);
                if (firstColumn.isForbidden()) {
                    firstColumn.setForbidden(false);
                }
                if (!firstColumn.isVisible()) {
                    firstColumn.setVisible(true);
                }
                setSectionHidden(firstSelectorColumnIndex, false);
            }
            for (int i = 0; i < columns.size(); ++i) {
                columns.get(i).subscribe(this);
            }
        }
        updateColumnsSizePolicy();
        updateFirstVisibleColumnIndex();
        customContextMenuRequested.connect(this, "createAndShowPopupMenu(QPoint)");
        sectionMoved.connect(this, "updateColumnsSizePolicy()");
        wasBinded = true;
    }        
    
    public int getSelectionColumnWidth(){
        if (selectionColumnWidth<0){
            final int hmargin = style.pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin, null, this)+1;
            final QStyleOptionButton opt = new QStyleOptionButton();
            final int width = style.subElementRect(QStyle.SubElement.SE_ViewItemCheckIndicator, opt).width();        
            final EQtStyle gStyle = EQtStyle.detectStyle(style);
            final int offset;
            if (gStyle==EQtStyle.Windows || gStyle==EQtStyle.Plastique){
            //This styles paints checkbox with offset
                offset = 1;
            }else{
                offset = 0;
            }
            selectionColumnWidth =  width+hmargin*4+offset+1;
        }
        return selectionColumnWidth;
    }    
    
    private boolean updateColumnsSizePolicy(){
        if (columnsSizePolicyUpdateBlocked){
            return false;
        }else{
            int stretchColumnIndex = -1;
            final List<SelectorColumnModelItem> columns = getSelectorColumns();
            final int columnsCount = columns.size()+firstSelectorColumnIndex;
            final List<QHeaderView.ResizeMode> newColumnsResizeMode = new ArrayList<>(columnsCount);
            for (int i=0; i<firstSelectorColumnIndex; i++){
                newColumnsResizeMode.add(null);
            }
            for (int i=firstSelectorColumnIndex; i<columnsCount; i++){
                final int colIndex = i - firstSelectorColumnIndex;
                final QHeaderView.ResizeMode columnResizeMode = getResizeMode(columns.get(colIndex));
                newColumnsResizeMode.add(resizeMode(i)==columnResizeMode ? null : columnResizeMode);                
                if (!isSectionHidden(i) && columnResizeMode==QHeaderView.ResizeMode.Stretch){
                    if (stretchColumnIndex>=0){
                        newColumnsResizeMode.set(stretchColumnIndex, QHeaderView.ResizeMode.Interactive);
                    }
                    stretchColumnIndex = i;
                }
            }
            if (stretchColumnIndex<0){
                int lastVisibleColumnIdx=-1;
                for (int i=columnsCount-1; i>=firstSelectorColumnIndex; i--){
                    final int logicalIndex=logicalIndex(i);
                    if (!isSectionHidden(logicalIndex)){
                        final int colIndex = logicalIndex - firstSelectorColumnIndex;
                        if (lastVisibleColumnIdx<0){
                            lastVisibleColumnIdx = logicalIndex;
                        }
                        if (forceStretchLastVisibleColumn 
                            || getResizeMode(columns.get(colIndex))==QHeaderView.ResizeMode.Interactive){
                            if (resizeMode(logicalIndex)==QHeaderView.ResizeMode.Stretch){
                                newColumnsResizeMode.set(logicalIndex, null);
                            }else{
                                newColumnsResizeMode.set(logicalIndex, QHeaderView.ResizeMode.Stretch);
                            }
                            stretchColumnIndex = i;
                            break;
                        }
                    }
                }
                if (stretchColumnIndex<0 && lastVisibleColumnIdx>=0){
                    if (resizeMode(lastVisibleColumnIdx)==QHeaderView.ResizeMode.Stretch){
                        newColumnsResizeMode.set(lastVisibleColumnIdx, null);                        
                    }else{
                        newColumnsResizeMode.set(lastVisibleColumnIdx, QHeaderView.ResizeMode.Stretch);
                    }
                }
            }
            boolean columnSizePolicyChanged = false;
            for (int i=0; i<columnsCount; i++){
                QHeaderView.ResizeMode columnResizeMode = newColumnsResizeMode.get(i);
                if (columnResizeMode!=null){
                    setResizeMode(i, columnResizeMode);
                    columnSizePolicyChanged = true;
                }
            }            
            return columnSizePolicyChanged;
        }
    }    
    
    public void updateFirstVisibleColumnIndex(){
        int idx;
        for (int col = 0, count=count(); col < count; col++) {
            idx = logicalIndex(col);
            if (idx >= 0 && !isSectionHidden(idx)) {
                ((SelectorModel)model()).setFirstVisibleColumnIndex(idx);
                break;
            }
        }
    }
    
    public int getFirstVisibleColumnIndex(){
        final int columnsCount = count();
        int idx;        
        for (int col = firstSelectorColumnIndex; col <= columnsCount; col++) {
            idx = logicalIndex(col);//logical index of column
            if (idx >= 0 && !isSectionHidden(idx)) {
                return idx;
            }
        }
        return 0;
    }
    
    public int getLastVisibleColumnIndex(){
        for (int i=count()-1; i>=firstSelectorColumnIndex; i--){
            final int logicalIndex=logicalIndex(i);
            if (!isSectionHidden(logicalIndex)){
                return i;
            }
        }
        return -1;
    }
    
    private int getStretchColumnIndex(){
        for (int i=count()-1; i>=firstSelectorColumnIndex; i--){
            final int logicalIndex=logicalIndex(i);
            if (resizeMode(logicalIndex)==QHeaderView.ResizeMode.Stretch){
                return i;
            }
        }
        return -1;   
    }
    
    public static QHeaderView.ResizeMode getResizeMode(final SelectorColumnModelItem column){
        final ESelectorColumnSizePolicy sizePolicy = getActualSizePolicy(column);
        switch(sizePolicy){
            case RESIZE_BY_CONTENT:
                return QHeaderView.ResizeMode.ResizeToContents;
            case STRETCH:
                return QHeaderView.ResizeMode.Stretch;
            default:
                return QHeaderView.ResizeMode.Interactive;
        }
    }    
    
    private SelectorColumnModelItem getSelectorColumn(final int index){
        return (SelectorColumnModelItem)model().headerData(index, Qt.Orientation.Horizontal, SelectorModel.COLUMN_ITEM_ROLE);
    }
    
    private int findSectionToResize(final int startFrom){
        return startFrom<firstSelectorColumnIndex ? -1 : startFrom;
    }
    
    @SuppressWarnings("unused")
    private void createAndShowPopupMenu(final QPoint point) {
        final QMenu menu = new QMenu(getEnvironment().getMessageProvider().translate("Selector", "Columns visibility"), this);
        QAction action;

        final List<SelectorColumnModelItem> columns = getSelectorColumns();
        int disabledColumnsCount = 0;        
        for (SelectorColumnModelItem column : columns) {
            if (!column.isVisible()) {
                ++disabledColumnsCount;
            }
        }

        boolean onlyOneColumnVisible = columns.size() - disabledColumnsCount == firstSelectorColumnIndex;
        boolean defaultsChanged = false;
        for (SelectorColumnModelItem column : columns) {
            if (!column.isForbidden()){
                action = new QAction(column.getTitle(), null);
                action.setCheckable(true);
                action.setChecked(column.isVisible());
                action.triggered.connect(column, "setVisible(boolean)");
                if (menu.isEmpty() && firstSelectorColumnMustBeVisible) {
                    action.setChecked(true);
                    action.setEnabled(false);
                    onlyOneColumnVisible = false;
                } else if (column.isVisible() && onlyOneColumnVisible) {
                    action.setEnabled(false);
                }
                menu.addAction(action);
            }
        }
        if (isDefaultSettingsChanged()){
            menu.addSeparator();
            action = new QAction(getEnvironment().getMessageProvider().translate("Selector", "Restore Default Settings"), null);
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CANCEL));
            action.triggered.connect(this, "resetToDefaultSettings()");
            menu.addAction(action);
        }
        menu.exec(mapToGlobal(point));
    }
    
    @SuppressWarnings("unused")
    private void resetToDefaultSettings(){
        SelectorColumnModelItem column;
        RadSelectorPresentationDef.SelectorColumn columnDef;
        int visualIndex;
        columnsSizePolicyUpdateBlocked = true;
        try{
            for (int i=firstSelectorColumnIndex, count=count(); i<count; i++){
                column = getSelectorColumn(i);
                if (column!=null){
                    columnDef = column.getColumnDef();
                    if (getDefaultSizePolicy(column)!=getActualSizePolicy(column)){
                        column.setSizePolicy(columnDef.getSizePolicy());
                    }
                    if (firstSelectorColumnMustBeVisible && i==firstSelectorColumnIndex){
                        column.setVisible(true);
                    }else {
                        final boolean isVisibleByDefault = isVisible(columnDef.getVisibility());
                        if (!column.isForbidden() && isVisibleByDefault!=column.isVisible()){
                            column.setVisible(isVisibleByDefault);
                        }
                    }
                }
                visualIndex = visualIndex(i);
                if (visualIndex!=i){
                    moveSection(visualIndex, i);
                }                
            }
        }finally{
            columnsSizePolicyUpdateBlocked = false;
        }
        updateColumnsSizePolicy();
    }
    
    private boolean isDefaultSettingsChanged(){
        SelectorColumnModelItem column;
        final int lastVisibleColIndex = getLastVisibleColumnIndex();
        for (int i=firstSelectorColumnIndex, count=count(); i<count; i++){
            if (visualIndex(i)!=i){
                return true;
            }
            column = getSelectorColumn(i);
            if (column==null){
                return true;
            }
            if (getDefaultSizePolicy(column)!=getActualSizePolicy(column)
                && (column.getSizePolicy()!=ESelectorColumnSizePolicy.STRETCH || i!=lastVisibleColIndex)){
                return true;
            }
            final boolean isVisibleByDefault = 
                (i==firstSelectorColumnIndex && firstSelectorColumnMustBeVisible) || isVisible(column.getColumnDef().getVisibility());
            if (!column.isForbidden() && isVisibleByDefault!=column.isVisible()){
                return true;
            }
        }
        return false;
    }
    
    private static ESelectorColumnSizePolicy getDefaultSizePolicy(final SelectorColumnModelItem column){
        final ESelectorColumnSizePolicy policy = column.getColumnDef().getSizePolicy();
        return policy==ESelectorColumnSizePolicy.AUTO ? column.getAutoSizePolicy() : policy;
    }
    
    private static ESelectorColumnSizePolicy getActualSizePolicy(final SelectorColumnModelItem column){
        final ESelectorColumnSizePolicy policy = column.getSizePolicy();
        return policy==ESelectorColumnSizePolicy.AUTO ? column.getAutoSizePolicy() : policy;
    }
    
    private static boolean isVisible(final ESelectorColumnVisibility visibility){
        return visibility==ESelectorColumnVisibility.INITIAL;
    }
    
    private IClientEnvironment getEnvironment(){
        return ((SelectorModel)model()).getEnvironment();
    }
    
    private List<SelectorColumnModelItem> getSelectorColumns(){
        return ((SelectorModel)model()).getSelectorColumns();
    }
    
    @Override
    protected void disposed() {
        WidgetUtils.CustomStyle.release(style);
        super.disposed();
    }
    
    public void applySettings(){
        selectionColumnWidth = -1;
    }
    
    public void saveSettings(){
        if (wasBinded){
            int logicalIndex;
            final ArrStr columnSettings = new ArrStr();
            for (int i=firstSelectorColumnIndex, count=count(); i<count; i++){
                logicalIndex = logicalIndex(i);                
                final SelectorColumnModelItem column = getSelectorColumn(logicalIndex);
                if (column!=null){
                    switch (resizeMode(logicalIndex)){
                        case Stretch:
                            columnSettings.add(column.getId().toString()+" -1");
                            break;
                        case ResizeToContents:
                            columnSettings.add(column.getId().toString());
                            break;
                        default:
                            columnSettings.add(column.getId().toString()+" "+sectionSize(logicalIndex));
                    }
                }                
            }
            settings.beginGroup(settingsGroup);
            try {
                settings.writeInteger(FORMAT_VERSION_KEY, CURRENT_FORMAT_VERSION);
                settings.writeString(SECTIONS_KEY, columnSettings.toString());
                settings.remove(STATE_KEY);
            }finally{
                settings.endGroup();
            }
        }
    }
    
    public void restoreSettings(){
        setStretchLastSection(false);
        setCascadingSectionResizes(false);        
        settings.beginGroup(settingsGroup);
        try {
            if (settings.contains(FORMAT_VERSION_KEY)){
                final int formatVersion = settings.readInteger(FORMAT_VERSION_KEY, 0);
                if (formatVersion>0 && restoreSettings(formatVersion)){
                    return;
                }
            }
            if (settings.contains(STATE_KEY)) {
                restoreState(settings.readQByteArray(STATE_KEY));
            }
        } finally {
            settings.endGroup();
        }
    }
    
    private boolean restoreSettings(final int formatVersion){        
        if (formatVersion==1){            
            final String columnSettingsAsStr = settings.readString(SECTIONS_KEY, "");
            if (columnSettingsAsStr==null || columnSettingsAsStr.isEmpty()){
                return true;
            }
            final ArrStr columnSettings;
            try{
                columnSettings = ArrStr.fromValAsStr(columnSettingsAsStr);
            }catch(WrongFormatError ex){
                final String messageTemplate = 
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Failed to restore column settings from string '%1$s'");
                getEnvironment().getTracer().debug(String.format(messageTemplate, columnSettingsAsStr), ex);
                return false;
            }
            if (columnSettings!=null && !columnSettings.isEmpty()){
                int visualIndex = 0;
                columnsSizePolicyUpdateBlocked = true;
                try{
                    for (String columnSetting: columnSettings){
                        if (restoreColumnSettings(columnSetting, visualIndex)){
                            visualIndex++;
                        }
                    }
                }finally{
                    columnsSizePolicyUpdateBlocked = false;
                }
                if (wasBinded){
                    updateColumnsSizePolicy();
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    private boolean restoreColumnSettings(final String columnSettings, final int visualIndex){
        if (columnSettings!=null && !columnSettings.isEmpty()){
            final int deviderIndex = columnSettings.indexOf(' ');
            final Id columnId;
            if (deviderIndex>0){
                columnId = Id.Factory.loadFrom(columnSettings.substring(0, deviderIndex));                        
            }else{
                columnId = Id.Factory.loadFrom(columnSettings);
            }
            if (columnId!=null){
                final int logicalIndex = findIndexByColumnId(columnId);
                if (logicalIndex>-1){
                    final SelectorColumnModelItem column = getSelectorColumn(logicalIndex);
                    if (column!=null){                        
                        final int currentVisualIndex = visualIndex(logicalIndex);
                        final boolean canMoveSection = !firstSelectorColumnMustBeVisible || (currentVisualIndex!=firstSelectorColumnIndex && visualIndex!=firstSelectorColumnIndex);
                        if (currentVisualIndex!=visualIndex && canMoveSection){
                            moveSection(currentVisualIndex, visualIndex);
                        }                        
                        if (deviderIndex<=0){
                            if (getDefaultSizePolicy(column)==ESelectorColumnSizePolicy.RESIZE_BY_CONTENT){
                                column.setSizePolicy(ESelectorColumnSizePolicy.RESIZE_BY_CONTENT);
                            }
                            return true;
                        }
                        final String widthAsStr = columnSettings.substring(deviderIndex+1);
                        final int width;
                        try{
                            width = Integer.parseInt(widthAsStr);
                        }catch(NumberFormatException ex){
                            final String messageTemplate = 
                                getEnvironment().getMessageProvider().translate("TraceMessage", "Failed to restore column width from string '%1$s'");
                            getEnvironment().getTracer().debug(String.format(messageTemplate, widthAsStr), ex);
                            return false;
                        }
                        if (width<0){
                            column.setSizePolicy(ESelectorColumnSizePolicy.STRETCH);
                            forceStretchLastVisibleColumn = column.getColumnDef().getSizePolicy()!=ESelectorColumnSizePolicy.STRETCH;
                        }else if (width>0){
                            column.setSizePolicy(ESelectorColumnSizePolicy.MANUAL_RESIZE);
                            resizeSection(logicalIndex, width);
                        }
                        return true;
                    }
                }
            }
            final String messageTemplate = 
                getEnvironment().getMessageProvider().translate("TraceMessage", "Unable to restore column settings from string '%1$s'");            
            getEnvironment().getTracer().debug(String.format(messageTemplate, columnSettings));
        }
        return false;
    }
    
    private int findIndexByColumnId(final Id columnId){
        for (int i=0,count=model().columnCount(); i<count; i++){
            if (columnId.equals( model().headerData(i, Qt.Orientation.Horizontal, Qt.ItemDataRole.UserRole) )){
                return i;
            }
        }
        return -1;
    }            
}