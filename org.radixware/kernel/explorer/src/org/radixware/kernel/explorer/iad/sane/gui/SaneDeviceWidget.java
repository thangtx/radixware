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

package org.radixware.kernel.explorer.iad.sane.gui;

import org.radixware.kernel.explorer.widgets.ImageViewWidget;
import org.radixware.kernel.explorer.iad.sane.ScanningTask;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.tuneology.sane.BooleanOption;
import com.tuneology.sane.GroupOption;
import com.tuneology.sane.OptionDescriptor;
import com.tuneology.sane.SaneException;
import com.tuneology.sane.SaneParameters;
import com.tuneology.sane.StringOption;
import java.util.Collections;
import org.radixware.kernel.explorer.iad.sane.SaneDevice;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.explorer.iad.sane.options.SaneOptionValueUtils;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.QExtTabWidget;


public class SaneDeviceWidget extends QWidget{

    private final static int SCALED_PREVIEW_MAX_SIDE = 400;
    private final static String CONFIG_STORE_KEY_NAME = "iad";
    
    private static class Icon extends ClientIcon.CommonOperations{
        
        private Icon(final String fileName){
            super(fileName);
        }
        
        public static final Icon PREVIEW = new Icon("classpath:images/preview.svg");
    }       
    
    private static class RereadValueEvent extends QEvent{    
        public RereadValueEvent(){
            super(QEvent.Type.User);
        }        
    }
        
    private static final EnumSet<EBasicSaneOption> KEEP_BEFORE_PREVIEW_SCAN = 
        EnumSet.of(EBasicSaneOption.BIT_DEPTH, EBasicSaneOption.SCAN_RESOLUTION, 
                   EBasicSaneOption.SCAN_X_RESOLUTION, EBasicSaneOption.SCAN_Y_RESOLUTION);
            
    private final IClientEnvironment environment;
    private final SaneDevice device;
    private final Map<String,SaneOptionWidget> optionWidgetsByName = new HashMap<>();    
    private final EnumMap<EBasicSaneOption, OptionDescriptor> virtualOptions = new EnumMap<>(EBasicSaneOption.class);
    private final List<SaneOptionWidget> pollingOptions = new LinkedList<>();
    private final EnumSet<EBasicSaneOption> virtualPollingOptions = EnumSet.noneOf(EBasicSaneOption.class);
    private final List<QImage> scannedImages = new LinkedList<>();
    private final Map<OptionDescriptor,Integer> storedOptionValues = new HashMap<>();
    private final TaskWaiter taskWaiter;
    
    private final QExtTabWidget twOptions = new QExtTabWidget(this);
    private final QWidget wtBasicOptions = new QWidget();
    private final QWidget wtSpecificOptions = new QWidget();
    private final QScrollArea saBasicOptions = new QScrollArea();
    private final QScrollArea saSpecificOptions = new QScrollArea(); 
    private final Splitter splitter;
    private final ImageViewWidget previewWidget;
    private final boolean singleImageMode;
    
    private QImage previewImage = new QImage();
    private int pollingTimer = -1;
    private int updatePreviewTimer = -1;
    private boolean rereadValsScheduled;
    
    private double previewWidth=0,previewHeight=0;
    private boolean isColorsInvered;
    private boolean isAutoSelect;    
    private int selIndex;
    
    private ScanningTask scanningTask;
            
    public SaneDeviceWidget(final SaneDevice device, 
                            final boolean singleImageMode,
                            final IClientEnvironment environment, 
                            final QWidget parentWidget){        
        super(parentWidget);
        this.environment = environment;
        this.device = device;    
        this.singleImageMode = singleImageMode;
        previewWidget = new ImageViewWidget(previewImage, environment.getMessageProvider(), singleImageMode, this);
        splitter = new Splitter(this,environment.getConfigStore());
        taskWaiter = new TaskWaiter(environment,parentWidget);
        setupUi();
        bindOptions();
        setInitialOptionValues();
    }
    
    private void setupUi(){
        final QWidget wtPreview = new QWidget();
        wtPreview.setObjectName("Rdx.SaneDeviceWidget.wtPreview");
        final QVBoxLayout ltPreview = WidgetUtils.createVBoxLayout(wtPreview);
        final QHBoxLayout ltZoomButtons = WidgetUtils.createHBoxLayout(null);
        ltZoomButtons.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        ltPreview.addLayout(ltZoomButtons);
        ltPreview.addWidget(previewWidget, 100);
                
        previewWidget.setMinimumWidth(300);
        previewWidget.newSelection.connect(this,"onNewSelection(QRectF)");
        ltZoomButtons.addWidget(createButtonForAction(previewWidget.zoomInAction));
        ltZoomButtons.addWidget(createButtonForAction(previewWidget.zoomOutAction));
        ltZoomButtons.addWidget(createButtonForAction(previewWidget.zoomSelAction));
        ltZoomButtons.addWidget(createButtonForAction(previewWidget.zoom2FitAction));
        
        final QPushButton pbPreview = new QPushButton(wtPreview);
        pbPreview.setToolTip(environment.getMessageProvider().translate("IAD", "Scan Preview Image"));
        pbPreview.setIcon(ExplorerIcon.getQIcon(Icon.PREVIEW));
        pbPreview.setText(environment.getMessageProvider().translate("IAD", "Preview"));
        pbPreview.clicked.connect(this,"previewScan()");
                        
        final QHBoxLayout ltPreviewButton = WidgetUtils.createHBoxLayout(null);
        ltPreviewButton.addWidget(pbPreview,0,Qt.AlignmentFlag.AlignCenter);
        ltPreview.addLayout(ltPreviewButton);
                
        saBasicOptions.setWidgetResizable(true);
        saBasicOptions.setFrameShape(QFrame.Shape.NoFrame);        
        twOptions.addTab(saBasicOptions, environment.getMessageProvider().translate("IAD", "Basic Options"));
        wtBasicOptions.setObjectName("Rdx.SaneDeviceWidget.wtBasicOptions");
        saBasicOptions.setWidget(wtBasicOptions);
        final QGridLayout ltBasicOptionsLayout = new QGridLayout();
        ltBasicOptionsLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        wtBasicOptions.setLayout(ltBasicOptionsLayout);        
        
        saSpecificOptions.setWidgetResizable(true);
        saSpecificOptions.setFrameShape(QFrame.Shape.NoFrame);
        twOptions.addTab(saSpecificOptions, environment.getMessageProvider().translate("IAD", "Specific Options"));
        wtSpecificOptions.setObjectName("Rdx.SaneDeviceWidget.wtSpecificOptions");
        saSpecificOptions.setWidget(wtSpecificOptions);
        final QVBoxLayout ltSpecificOptionsLayout = new QVBoxLayout();
        ltSpecificOptionsLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        wtSpecificOptions.setLayout(ltSpecificOptionsLayout);
                
        splitter.addWidget(twOptions);
        splitter.setStretchFactor(0,0);
        splitter.addWidget(wtPreview);
        splitter.setStretchFactor(1,100);
        splitter.addCollapser(0);
                        
        final QHBoxLayout mainLayout = WidgetUtils.createHBoxLayout(this);
        mainLayout.addWidget(splitter);
        setupOptions(wtBasicOptions, ltBasicOptionsLayout, wtSpecificOptions, ltSpecificOptionsLayout);
        {//create "Invert colors" checkBox
            final String label = environment.getMessageProvider().translate("IAD", "Invert colors");
            final QCheckBox cbInvertColors = new QCheckBox(label,wtBasicOptions);            
            cbInvertColors.clicked.connect(this,"invertColorsClicked(Boolean)");       
            final int row = ltBasicOptionsLayout.rowCount();
            ltBasicOptionsLayout.addWidget(cbInvertColors, row, 1,Qt.AlignmentFlag.AlignLeft);
        }
        updatePreviewSize();
    }
    
    private void setupOptions(final QWidget wtBasicOptions, 
                              final QGridLayout ltBasicOptionsLayout, 
                              final QWidget wtSpecificOptions,
                              final QVBoxLayout ltSpecificOptionsLayout){
       final OptionDescriptor[] options;
        try{
            options = device.getOptions();
        }catch(SaneException exception){
            environment.processException(exception);
            return;
        }
        
        String currentGroup = null;
        QGridLayout ltCurrentOptionsLayout = null; 
        boolean onlyBooleanOptions = true;
        for (int i=1; i<options.length; i++){//first option is total number of options
            if (options[i] instanceof GroupOption){
                currentGroup = options[i].getName();
                if (ltCurrentOptionsLayout!=null && onlyBooleanOptions){
                    ltCurrentOptionsLayout.setColumnStretch(0, 0);
                    ltCurrentOptionsLayout.setColumnStretch(1, 100);
                }
                ltCurrentOptionsLayout = null;                
            }else{
                final String optionName = options[i].getName();
                final EBasicSaneOption basicOption = EBasicSaneOption.findForOptionName(optionName);
                if (basicOption!=null){
                    if (basicOption.hasGui()){
                        final SaneOptionWidget optionWidget = 
                            SaneOptionWidget.Factory.createInstance(device,options[i], environment, wtBasicOptions);
                        if (optionWidget!=null){
                            optionWidgetsByName.put(optionName, optionWidget);
                        }
                    }else{
                        virtualOptions.put(basicOption, options[i]);
                    }
                }else{
                    final SaneOptionWidget optionWidget = 
                        SaneOptionWidget.Factory.createInstance(device,options[i], environment, wtSpecificOptions);
                    optionWidgetsByName.put(optionName, optionWidget);
                    if (optionWidget!=null){
                        if (options[i] instanceof BooleanOption==false){
                            onlyBooleanOptions = false;
                        }                        
                        if (currentGroup!=null && !currentGroup.isEmpty() && ltCurrentOptionsLayout==null){
                            final QGroupBox gbCurrentOptionsGroup = new QGroupBox(currentGroup, wtSpecificOptions);                            
                            ltCurrentOptionsLayout = new QGridLayout();
                            gbCurrentOptionsGroup.setLayout(ltCurrentOptionsLayout);
                            ltSpecificOptionsLayout.addWidget(gbCurrentOptionsGroup);
                            onlyBooleanOptions = true;
                        }
                        if (ltCurrentOptionsLayout==null){
                            ltCurrentOptionsLayout = new QGridLayout();
                            ltSpecificOptionsLayout.addLayout(ltCurrentOptionsLayout);
                            onlyBooleanOptions = true;
                        }
                        optionWidget.putIntoLayout(ltCurrentOptionsLayout);
                    }
                }
            }
        }
        if (ltCurrentOptionsLayout!=null && onlyBooleanOptions){
            ltCurrentOptionsLayout.setColumnStretch(0, 0);
            ltCurrentOptionsLayout.setColumnStretch(1, 100);
        }
        for (EBasicSaneOption basicOption: EBasicSaneOption.values()){
            final SaneOptionWidget optionWidget = optionWidgetsByName.get(basicOption.optionName());
            if (optionWidget!=null){
                optionWidget.putIntoLayout(ltBasicOptionsLayout);
            }
        }
        updateScanArea();
        updateWidthOfOptionsPanel();
    }
    
    private QToolButton createButtonForAction(final QAction action){
        final QToolButton button = new QToolButton(this);
        button.setIconSize(new QSize(24, 24));
        button.setAutoRaise(true);
        button.setDefaultAction(action);
        return button;
    }
        
    private void bindOptions(){
        device.addOptionsListener(new SaneDevice.IOptionsListener() {
            @Override
            public void needToRefreshOptions() {
                refreshWidgets();
            }

            @Override
            public void needToRereadOptionValues() {
                scheduleRereadValues();
            }
        });
        for (SaneOptionWidget optionWidget: optionWidgetsByName.values()){
            if (optionWidget.needsPolling()){
                pollingOptions.add(optionWidget);
            }
        }        
        for (EnumMap.Entry<EBasicSaneOption,OptionDescriptor> entry: virtualOptions.entrySet()){
            if (SaneOptionWidget.optionNeedsForPolling(entry.getValue())){
                virtualPollingOptions.add(entry.getKey());
            }
        }
        if (!pollingOptions.isEmpty() || !virtualPollingOptions.isEmpty()){
            pollingTimer = startTimer(100);
        }        
    }
    
    private void setInitialOptionValues(){
        final ClientSettings settings = environment.getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(CONFIG_STORE_KEY_NAME);
        settings.beginGroup(device.getDescription());
        try{    
            String defaultValue, storedValue;
            EBasicSaneOption basicOption;
            for(Map.Entry<String,SaneOptionWidget> entry: optionWidgetsByName.entrySet()){
                basicOption = EBasicSaneOption.findForOptionName(entry.getKey());
                defaultValue = basicOption==null ? null : basicOption.getDefaultValueAsString();
                storedValue = settings.readString(entry.getKey(), defaultValue);
                if (storedValue!=null && !storedValue.isEmpty()){
                    device.setOptionStringValue(entry.getValue().getOption(), storedValue);
                }
            }
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();            
        }
    }
    
    private void storeCurrentOptionValues(){
        final ClientSettings settings = environment.getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(CONFIG_STORE_KEY_NAME);
        settings.beginGroup(device.getDescription());
        try{
            String valAsStr;
            for(Map.Entry<String,SaneOptionWidget> entry: optionWidgetsByName.entrySet()){
                if (entry.getValue() instanceof SaneButtonOptionWidget==false){
                    valAsStr = device.getOptionStringValue(entry.getValue().getOption());
                    if (valAsStr!=null){
                        settings.setValue(entry.getKey(), valAsStr);
                    }
                }
            }
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }
    
    @SuppressWarnings("unused")
    private void refreshWidgets(){
        final OptionDescriptor[] options;
        try{
            options = device.getOptions();
        }catch(SaneException exception){
            environment.processException(exception);
            return;
        }        
        for (OptionDescriptor option: options){
            final SaneOptionWidget optionWidget = optionWidgetsByName.get(option.getName());
            if (optionWidget!=null){
                optionWidget.refresh(option);
            }
        }
        updateScanArea();
        updatePreviewSize();    
        updateWidthOfOptionsPanel();
        previewWidget.zoom2FitAction.trigger();
    }
    
    private void updateWidthOfOptionsPanel(){
        int min_width = wtBasicOptions.sizeHint().width();
        if (min_width < wtSpecificOptions.sizeHint().width()) {
            min_width = wtSpecificOptions.sizeHint().width();
        }

        twOptions.setMinimumWidth(min_width + saBasicOptions.verticalScrollBar().sizeHint().width() + 5);        
    }
    
    @SuppressWarnings("unused")
    private void scheduleRereadValues(){
        if (!rereadValsScheduled){
            rereadValsScheduled = true;
            QApplication.postEvent(this, new RereadValueEvent());            
        }
    }
    
    @SuppressWarnings("unused")
    private void invertColorsClicked(final Boolean isChecked){
        isColorsInvered = isChecked;
        previewImage.invertPixels();
        previewWidget.updateImage();        
    }
    
    @SuppressWarnings("unused")
    private void onNewSelection(final QRectF selectionRect){
        final OptionDescriptor optTlX = virtualOptions.get(EBasicSaneOption.SCAN_TL_X);
        final OptionDescriptor optTlY = virtualOptions.get(EBasicSaneOption.SCAN_TL_Y);
        final OptionDescriptor optBrX = virtualOptions.get(EBasicSaneOption.SCAN_BR_X);
        final OptionDescriptor optBrY = virtualOptions.get(EBasicSaneOption.SCAN_BR_Y);
        if (optTlX==null || optTlY==null || optBrX==null || optBrY==null) {
            // clear the selection since we can not set one
            previewWidget.setTLX(0);
            previewWidget.setTLY(0);
            previewWidget.setBRX(0);
            previewWidget.setBRY(0);
            return;
        }

        if ((previewImage.width()==0) || (previewImage.height()==0)){
            return;
        }
        final double max_x = SaneOptionValueUtils.getMaxOptionDoubleValue(optBrX);
        final double max_y = SaneOptionValueUtils.getMaxOptionDoubleValue(optBrY);
        
        double ftl_x = selectionRect.topLeft().x()*max_x;
        double ftl_y = selectionRect.topLeft().y()*max_y;
        double fbr_x = selectionRect.bottomRight().x()*max_x;
        double fbr_y = selectionRect.bottomRight().y()*max_y;

        device.setOptionDoubleValue(optTlX, ftl_x);
        device.setOptionDoubleValue(optTlY, ftl_y);
        device.setOptionDoubleValue(optBrX, fbr_x);
        device.setOptionDoubleValue(optBrY, fbr_y);
    }
            
    private void rereadValues(){
        for (SaneOptionWidget optionWidget: optionWidgetsByName.values()){
            optionWidget.rereadValue();
        }
        updateScanArea();
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof RereadValueEvent){
            event.accept();
            if (rereadValsScheduled){
                rereadValsScheduled = false;
                rereadValues();
            }
        }else{
            super.customEvent(event);
        }
    }    
    

    @Override
    protected void timerEvent(final QTimerEvent event) {
        final int timerId = event.timerId();
        if (timerId==pollingTimer){
            event.accept();
            for (SaneOptionWidget option: pollingOptions){
                option.rereadValue();
            }
            final EnumSet<EBasicSaneOption> scanAreaOptions = 
                EnumSet.of(EBasicSaneOption.SCAN_TL_X, EBasicSaneOption.SCAN_TL_Y, EBasicSaneOption.SCAN_BR_X, EBasicSaneOption.SCAN_BR_Y);
            for (EBasicSaneOption basicOption : virtualPollingOptions){
                if (scanAreaOptions.contains(basicOption)){
                    updateScanArea();
                    break;
                }
            }
        } else if (timerId==updatePreviewTimer){
            event.accept();
            updateScanProgress();
        } else{
            super.timerEvent(event);
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (pollingTimer>=0){
            killTimer(pollingTimer);
        }
        if (rereadValsScheduled){
            rereadValsScheduled = false;
            QApplication.removePostedEvents(this, QEvent.Type.User.value());
        }
        taskWaiter.close();
        splitter.close();
        super.closeEvent(event);
    }           
    
    private void updateScanArea(){
        if (!scanInProgress()){
            {
                final Double ratio = calcRatio(EBasicSaneOption.SCAN_TL_X, EBasicSaneOption.SCAN_BR_X);
                if (ratio!=null){
                    previewWidget.setTLX(ratio.doubleValue());
                }
            }
            {
                final Double ratio = calcRatio(EBasicSaneOption.SCAN_TL_Y, EBasicSaneOption.SCAN_BR_Y);
                if (ratio!=null){
                    previewWidget.setTLY(ratio.doubleValue());
                }
            }
            {
                final Double ratio = calcRatio(EBasicSaneOption.SCAN_BR_X, null);
                if (ratio!=null){
                    previewWidget.setBRX(ratio.doubleValue());
                }                
            }
            {
                final Double ratio = calcRatio(EBasicSaneOption.SCAN_BR_Y, null);
                if (ratio!=null){
                    previewWidget.setBRY(ratio.doubleValue());
                }                                
            }
        }
    }        
    
    private Double calcRatio(final EBasicSaneOption topOption, final EBasicSaneOption bottomOption){
        final OptionDescriptor topOptionDescriptor = virtualOptions.get(topOption);
        final OptionDescriptor bottomOptionDescriptor;
        if (bottomOption==null){
            bottomOptionDescriptor = topOptionDescriptor;
        }else{
            bottomOptionDescriptor = virtualOptions.get(bottomOption);
        }        
        if (topOptionDescriptor!=null && bottomOptionDescriptor!=null){
            final Double optionValue = device.getOptionDoubleValue(topOptionDescriptor);
            if (optionValue!=null){
                final double max = SaneOptionValueUtils.getMaxOptionDoubleValue(bottomOptionDescriptor);
                return Double.valueOf(optionValue.doubleValue() / max);
            }
        }
        return null;
    }
    
    private boolean scanInProgress(){
        return scanningTask!=null;
    }
    
    private void updatePreviewSize(){                
        final OptionDescriptor optBrX = virtualOptions.get(EBasicSaneOption.SCAN_BR_X);
        final OptionDescriptor optBrY = virtualOptions.get(EBasicSaneOption.SCAN_BR_Y);
        // check if an update is necessary
        final double max_x = optBrX==null ? 0 : SaneOptionValueUtils.getMaxOptionDoubleValue(optBrX);
        final double max_y = optBrY==null ? 0 : SaneOptionValueUtils.getMaxOptionDoubleValue(optBrY);
        if ((max_x == previewWidth) && (max_y == previewHeight)) {
            //kDebug() << "no preview size change";
            return;
        }

        // The preview size has changed 
        previewWidth  = max_x;
        previewHeight = max_y;

        // set the scan area to the whole area
        previewWidget.clrSelAction.trigger();
        final OptionDescriptor optTlX = virtualOptions.get(EBasicSaneOption.SCAN_TL_X);
        final OptionDescriptor optTlY = virtualOptions.get(EBasicSaneOption.SCAN_TL_Y);        
        if (optTlX != null) {
            device.setOptionDoubleValue(optTlX, 0);
        }
        if (optTlY != null) {
            device.setOptionDoubleValue(optTlY, 0);
        }
        if (optBrX != null) {
            device.setOptionDoubleValue(optBrX, max_x);
        }
        if (optBrY != null) {
            device.setOptionDoubleValue(optBrY, max_y);
        }

        // create a "scaled" image of the preview        
        final double ratio = max_x/max_y;
        final int x;
        final int y;
        if (ratio < 1) {
            x=SCALED_PREVIEW_MAX_SIDE;
            y=(int)(SCALED_PREVIEW_MAX_SIDE/ratio);
        }
        else {
            y=SCALED_PREVIEW_MAX_SIDE;
            x=(int)(SCALED_PREVIEW_MAX_SIDE/ratio);
        }

        previewImage = new QImage(x, y, QImage.Format.Format_RGB32);
        previewImage.fill(0xFFFFFFFF);

        // set the new image
        previewWidget.setQImage(previewImage);
    }
    
    public void scan(){
        startScan(false);
    }            
    
    public void previewScan(){
        startScan(true);
    }
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    private void startScan(final boolean preview){
        if (scanningTask==null){
            scannedImages.clear();
            scanningTask = new ScanningTask(device, isColorsInvered, environment.getMessageProvider());                
            selIndex = 0;
            if (!initScanRegion(preview) && preview){
                isAutoSelect = false;    
            }
            if (preview){                
                initPreviewOptions();
            }
            if (virtualOptions.containsKey(EBasicSaneOption.PREVIEW)) {
                device.setOptionIntegerValue(virtualOptions.get(EBasicSaneOption.PREVIEW), preview ? 1 : 0);
            }                
            
            forceRereadValues();
            
            if (preview){
                // clear the preview
                previewWidget.clearHighlight();
                previewWidget.clrSelAction.trigger();
                updatePreviewSize();                
                updatePreviewTimer = startTimer(200);
            }else{
                storeCurrentOptionValues();
            }
            try{
                do{
                    taskWaiter.runAndWait(scanningTask);                    
                }while(!isScanComplete(preview));
            }catch(InterruptedException exception){                
                return;
            }finally{
                if (preview){
                    killTimer(updatePreviewTimer);
                    updatePreviewTimer=0;
                }
                scanningTask = null;                
            }
        }
    }
    
    private void forceRereadValues(){// execute valReload if there is a pending value reload
        while (rereadValsScheduled) {
            rereadValsScheduled = false;
            QApplication.removePostedEvents(this, QEvent.Type.User.value());
            rereadValues();
        }        
    }
    
    private boolean hasNextImage(){
        if (scanningTask.getStatus()!=ScanningTask.ReadStatus.READ_READY){
            return false;
        }
        // now check if we should have automatic ADF batch scaning
        if (optionWidgetsByName.containsKey(EBasicSaneOption.SCAN_SOURCE.optionName())){
            final OptionDescriptor sourceOption = 
                optionWidgetsByName.get(EBasicSaneOption.SCAN_SOURCE.optionName()).getOption();
            if (sourceOption instanceof StringOption){                
                final String value = device.getOptionStringValue(sourceOption);
                if (value!=null && value.contains("Automatic Document Feeder")) {
                    // in batch mode only one area can be scanned per page
                    return true;
                }                    
            }
        }

        // Check if we have a "wait for button" batch scanning
        if (optionWidgetsByName.containsKey("wait-for-button")) {
            final OptionDescriptor waitForButtonOption = 
                optionWidgetsByName.get("wait-for-button").getOption();
            if (waitForButtonOption instanceof BooleanOption){
                final Integer optionValue = device.getOptionIntegerValue(waitForButtonOption);
                if (optionValue!=null && optionValue.intValue()!=0){
                    return true;
                }
            }
        }
        
        // not batch scan, call sane_cancel to be able to change parameters.
        device.cancelScan();
        
        if (previewWidget.selListSize() >= selIndex && initScanRegion(false)){
            forceRereadValues();
            return true;
        }        
        return false;
    }
        
    private boolean initScanRegion(final boolean preview){
        if (virtualOptions.containsKey(EBasicSaneOption.SCAN_TL_X) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_TL_Y) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_BR_X) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_BR_Y)
           ){
            // get maximums
            final double max_x;
            if (virtualOptions.get(EBasicSaneOption.SCAN_BR_X).getRange()==null){
                max_x = 0;
            }else{
                max_x= SaneOptionValueUtils.getMaxOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_X));
            }
            final double max_y;
            if (virtualOptions.get(EBasicSaneOption.SCAN_BR_Y).getRange()==null){
                max_y = 0;
            }else{
                max_y = SaneOptionValueUtils.getMaxOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_Y));
            }
            
            final double x1,y1, x2,y2;
            if (preview){// select the whole area
                x1 = 0;
                y1 = 0;
                x2 = max_x;
                y2 = max_y;
            }else{                
                // reread the selection from the viewer
                final QRectF selectionRect = new QRectF();
                if (!previewWidget.selectionAt(selIndex, selectionRect)){
                    return false;
                }
                previewWidget.setHighlightArea(selectionRect);
                selIndex++;
                x1 = selectionRect.topLeft().x() * max_x;
                y1 = selectionRect.topLeft().y() * max_y;
                x2 = selectionRect.bottomRight().x() * max_x;
                y2 = selectionRect.bottomRight().y() * max_y;
            }
            device.setOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_TL_X),x1);
            device.setOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_TL_Y),y1);
            device.setOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_X),x2);
            device.setOptionDoubleValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_Y),y2);
            return true;
        }else{
            return false;
        }              
    }
        
    private void initPreviewOptions(){
        // store the current settings of parameters to be changed
        for (EBasicSaneOption option: KEEP_BEFORE_PREVIEW_SCAN){
            final OptionDescriptor optionDescriptor;
            final SaneOptionWidget optionWidget = optionWidgetsByName.get(option.optionName());
            if (optionWidget!=null){
                optionDescriptor = optionWidget.getOption();
            }else{
                optionDescriptor = virtualOptions.get(option);
            }
            final Integer value = optionDescriptor==null ? null : device.getOptionIntegerValue(optionDescriptor);
            if (value!=null){
                storedOptionValues.put(optionDescriptor, value);
            }
        }

        // check if we can modify the selection
        if (virtualOptions.containsKey(EBasicSaneOption.SCAN_TL_X) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_TL_Y) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_BR_X) &&
            virtualOptions.containsKey(EBasicSaneOption.SCAN_BR_Y)
           ){
            // get maximums
            final int max_x;
            if (virtualOptions.get(EBasicSaneOption.SCAN_BR_X).getRange()==null){
                max_x = 0;
            }else{
                max_x= 
                    SaneOptionValueUtils.getMaxOptionIntValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_X));
            }
            final int max_y;
            if (virtualOptions.get(EBasicSaneOption.SCAN_BR_Y).getRange()==null){
                max_y = 0;
            }else{
                max_y = 
                    SaneOptionValueUtils.getMaxOptionIntValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_Y));
            }
            // select the whole area
            device.setOptionIntegerValue(virtualOptions.get(EBasicSaneOption.SCAN_TL_X),0);
            device.setOptionIntegerValue(virtualOptions.get(EBasicSaneOption.SCAN_TL_Y),0);
            device.setOptionIntegerValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_X),max_x);
            device.setOptionIntegerValue(virtualOptions.get(EBasicSaneOption.SCAN_BR_Y),max_y);            
        } else {
            // no use to try auto selections if you can not use them
            isAutoSelect = false;
        }
        final SaneOptionWidget resolutionOptionWidget;
        if (this.optionWidgetsByName.containsKey(EBasicSaneOption.SCAN_RESOLUTION.optionName())){
            resolutionOptionWidget = optionWidgetsByName.get(EBasicSaneOption.SCAN_RESOLUTION.optionName());
        }else{
            resolutionOptionWidget = optionWidgetsByName.get(EBasicSaneOption.SCAN_X_RESOLUTION.optionName());
        }
        final OptionDescriptor optRes = resolutionOptionWidget==null ? null : resolutionOptionWidget.getOption();
        if (optRes!=null) {
            final SaneOptionWidget resolutionYOptionWidget = 
                optionWidgetsByName.get(EBasicSaneOption.SCAN_Y_RESOLUTION.optionName());
            final OptionDescriptor optYRes = 
                resolutionYOptionWidget==null ? null : resolutionYOptionWidget.getOption();
            // set the resopution to getMinValue and increase if necessary
            SaneParameters params;
            double dpi =  
                Math.max(25.0, SaneOptionValueUtils.getMinOptionDoubleValue(optRes));
            do {
                device.setOptionDoubleValue(optRes, dpi);
                if (optYRes!=null
                    && EBasicSaneOption.SCAN_X_RESOLUTION.optionName().equals(optRes.getName())){
                    device.setOptionDoubleValue(optYRes, dpi);
                }
                //check what image size we would get in a scan
                try{
                    params = device.getFrameParameters();
                }catch(SaneException exception){
                    environment.processException(exception);
                    scanningTask = null;
                    return;
                }

                if (dpi > 600) 
                    break;

                // Increase the dpi value
                dpi += 25.0;
            }
            while ((params.pixels_per_line < 300) || ((params.lines > 0) && (params.lines < 300)));

            if (params.pixels_per_line == 0){
                // This is a security measure for broken backends
                final int min_res;
                if (optRes.getRange()!=null){
                    min_res = optRes.getRange().getMin();
                }else{
                    final Object[] values = optRes.getElements();
                    if (values!=null && values.length>0 && values[0] instanceof Integer){
                        min_res = ((Integer)values[0]).intValue();
                    }else{
                        min_res = -1;
                    }
                }
                if (min_res>0){
                    device.setOptionIntegerValue(optRes, min_res);
                }
            }
        }
    }
    
    private boolean isScanComplete(boolean preview){
        if (preview){
            // restore the original settings of the changed parameters
            device.cancelScan();
            for (Map.Entry<OptionDescriptor,Integer> entry : storedOptionValues.entrySet()){
                device.setOptionIntegerValue(entry.getKey(),entry.getValue());
            }            
            try{
                synchronized(scanningTask.semaphore){
                    previewImage = scanningTask.getImage();
                    if (scanningTask.imageWasResized()){                        
                        previewWidget.setQImage(previewImage);
                        previewWidget.zoom2FitAction.trigger();
                    }else{
                        previewWidget.updateImage();
                    }
                }
            }catch(Exception exception){
                environment.processException(exception);
            }            
            if (isAutoSelect){
                previewWidget.findSelections((float)10000.0);
            }
            return true;
        }else{
            try{
                synchronized(scanningTask.semaphore){
                    scannedImages.add(scanningTask.getImage());
                }
            }catch(Exception exception){
                environment.processException(exception);
                return true;
            }
            if (singleImageMode){
                device.cancelScan();
                return true;
            }else{
                return !hasNextImage();
            }
        }
    }  

    private void updateScanProgress(){
        if (scanningTask!=null) {
            final ScanningTask.ReadStatus status = scanningTask.getStatus();
            if (status == ScanningTask.ReadStatus.READ_ON_GOING) {
                synchronized(scanningTask.semaphore){
                    if (scanningTask.imageWasResized()) {
                        // the image size might have changed                    
                        try{
                            previewImage = scanningTask.getImage();
                        }catch(Exception exception){
                            environment.getTracer().error(exception);
                            return;
                        }
                        previewWidget.setQImage(previewImage);
                        previewWidget.zoom2FitAction.trigger();
                    }
                    else {
                        previewWidget.updateImage();
                    }
                }
                previewWidget.viewport().repaint();
            }
        }
    }  
    
    public List<QImage> getScannedImages(){
        return Collections.unmodifiableList(scannedImages);
    }
    
    public boolean someAreaWasSelected(){
        return previewWidget.selListSize()>0;
    }
    
}
