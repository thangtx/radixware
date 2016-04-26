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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QShowEvent;
import java.awt.Dimension;
import java.util.EnumSet;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public final class DialogSizeManager {
    
    private final class DialogSizeListener extends QEventFilter{

        public DialogSizeListener() {
            super(dialog);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.Show, QEvent.Type.Resize, QEvent.Type.LayoutRequest));
        }                

        @Override
        public boolean eventFilter(final QObject target, final QEvent event) {
            if (event instanceof QShowEvent){
                processShowEvent();
            }else if (event instanceof QResizeEvent){
                processResizeEvent((QResizeEvent)event);
            }else if (event.type()==QEvent.Type.LayoutRequest){
                processLayoutRequest();
            }
            return false;
        }                        
    }
    
    private final QDialog dialog;
    private final ExplorerSettings settings;
    
    private String configPrefix;    
    private String geometryKey = "/geometry";
    //private String autoSizeKey = "/autoSize";
    
    private boolean sizeWasRestored;
    private boolean positionWasRestored;
    private boolean internalResize;
    private boolean autoResize;
    private boolean wasShown;
    private boolean sizeWasChanged;
    
    public DialogSizeManager(final ExplorerSettings settings, final QDialog dialog, final String configPrefix){        
        this.dialog = dialog;        
        autoResize = true;
        this.settings = settings;
        this.configPrefix = configPrefix;
        dialog.installEventFilter(new DialogSizeListener());
    }

    public void setConfigPrefix(final String configPrefix) {
        this.configPrefix = configPrefix;
    }

    public void setGeometryKey(final String geometryKey) {
        this.geometryKey = "/"+geometryKey;
    }

    public void setAutoSizeKey(final String autoSizeKey) {
        //this.autoSizeKey = "/"+autoSizeKey;
    }        
    
    public void loadGeometry(final int defaultSizeX, final int defaultSizeY){
        if (canUseSettings()){            
            final String geometryKeyPath = configPrefix+geometryKey;
            if (!settings.contains(geometryKeyPath)
                || !dialog.restoreGeometry(settings.readQByteArray(geometryKeyPath))){
                applyDefaultSize(defaultSizeX, defaultSizeY);
            }else{
                sizeWasRestored = true;
                positionWasRestored = true;                
                //autoResize = settings.readBoolean(configPrefix+autoSizeKey, true);                
            }
        }
    }
    
    private boolean canUseSettings(){
        return configPrefix!=null && !configPrefix.isEmpty() && settings!=null;
    }
    
    private void applyDefaultSize(final int defaultSizeX, final int defaultSizeY){
        if (defaultSizeX>0 || defaultSizeY>0){
            final QSize minSize = getTotalMinSize();
            final int actualSizeX = defaultSizeX>0 ? Math.max(defaultSizeX, minSize.width()) : dialog.geometry().width();
            final int actualSizeY = defaultSizeY>0 ? Math.max(defaultSizeY, minSize.height()) : dialog.geometry().height();
            resizeDialog(actualSizeX, actualSizeY);
            sizeWasRestored = true;
        }
    }
        
    public void saveGeometry(){
        if (canUseSettings()){
            final QSize autoSize = calcAutoSize(true);
            if (autoSize!=null){
                final QSize currentSize = dialog.size();
                if (autoSize.width()>=currentSize.width() && autoSize.height()>=currentSize.height()){
                    settings.remove(configPrefix+geometryKey);
                    return;
                }
            }
            if (sizeWasRestored || sizeWasChanged){
                settings.writeQByteArray(configPrefix+geometryKey, dialog.saveGeometry());
            }
        }
    }
    
    private void processResizeEvent(final QResizeEvent event){
        if (!internalResize && wasShown && dialog.isVisible()){
            final QSize autoSize = calcAutoSize(true);
            if (autoSize!=null){
                autoResize =  event.size().width()>=autoSize.width() &&
                              event.size().height()>=autoSize.height();
            }
            sizeWasChanged = true;
        }
    }
    
    private void processShowEvent(){
        if (!wasShown){
            wasShown = true;            
            if (autoResize) {
                resizeDialog( calcAutoSize(!sizeWasRestored) );                
            }
            if (canUseSettings() && !positionWasRestored){
                WidgetUtils.centerDialog(dialog);
            }
        }
    }
    
    private void processLayoutRequest(){
        if (autoResize && wasShown){
            resizeDialog( calcAutoSize(false) );
        }
    }
    
    private void resizeDialog(final QSize size){
        if (size!=null){
            resizeDialog(size.width(), size.height());
        }
    }
    
    private void resizeDialog(final int width, final int height){
        internalResize = true;
        try{
            dialog.resize(width, height);
        }finally{
            internalResize = false;
        }
    }
    
    private QSize calcAutoSize(final boolean forced){
        if (dialog.layout()!=null && dialog.layout().sizeConstraint()==QLayout.SizeConstraint.SetFixedSize){
            return null;
        }
        final QSize sizeHint = dialog.sizeHint();
        if (isValidSize(sizeHint)){
            final QSize minSize = getTotalMinSize();
            final QSize maxSize = getTotalMaxSize();
            final QSize curSize = forced ? null : dialog.size();
            return
                calcTotalSize(sizeHint, minSize, maxSize, curSize, WidgetUtils.getWndowMaxSize());
        }else{
            return null;
        }                
    }
    
    private QSize getTotalMinSize(){
        final QLayout layout = dialog.layout();
        if (layout==null){
            return minSize(dialog.minimumSizeHint(), dialog.minimumSize());
        }else{
            return minSize(layout.minimumSize(), minSize(dialog.minimumSizeHint(), dialog.minimumSize()));
        }
    }
    
    private QSize getTotalMaxSize(){
        final QLayout layout = dialog.layout();
        if (layout==null){
            return dialog.maximumSize();
        }else{
            return maxSize(layout.maximumSize(), dialog.maximumSize());
        }
    }
    
    private static QSize minSize(final QSize size1, final QSize size2){
        if (!isValidSize(size1)){
            return size2;
        }
        if (!isValidSize(size2)){
            return size1;
        }
        if (size1.height()==size2.height() && size1.width()==size2.width()){
            return size1;
        }
        return new QSize(Math.min(size1.height(), size2.height()), Math.min(size1.width(), size2.width()));
    }
    
    private static QSize maxSize(final QSize size1, final QSize size2){
        if (!isValidSize(size1)){
            return size2;
        }
        if (!isValidSize(size2)){
            return size1;
        }
        if (size1.height()==size2.height() && size1.width()==size2.width()){
            return size1;
        }
        
        return new QSize(Math.max(size1.height(), size2.height()), Math.max(size1.width(), size2.width()));
    }
    
    private static boolean isValidSize(final QSize size){
        return size!=null && !size.isNull() && size.isValid() && !size.isEmpty();
    }
    
    private static QSize calcTotalSize(final QSize sizeHint, 
                                       final QSize minSize, 
                                       final QSize maxSize, 
                                       final QSize currentSize,
                                       final Dimension sizeLimit){
        QSize result = sizeHint;
        if (isValidSize(currentSize) 
            && result.width()<=currentSize.width() 
            && result.height()<=currentSize.height()){
            return null;
        }
        if (result.width()>sizeLimit.getWidth() || result.height()>sizeLimit.getHeight()){
            result.setWidth(Math.min(result.width(), (int)sizeLimit.getWidth()));
            result.setHeight(Math.min(result.height(), (int)sizeLimit.getHeight()));
        }        
        if (isValidSize(minSize)){
            result = result.expandedTo(minSize);
        }
        if (isValidSize(maxSize)){
            result = result.boundedTo(maxSize);
        }
        if (isValidSize(currentSize)){
            if (result.width()<=currentSize.width() && result.height()<=currentSize.height()){
                return null;
            }
            if (result.height()<currentSize.height()){
                result.setHeight(currentSize.height());
            }
            if (result.width()<currentSize.width()){
                result.setWidth(currentSize.width());
            }            
        }
        return result;
    }        
}