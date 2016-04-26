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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class LazyComboBox<T> extends QComboBox{
    
    public static class Item<T>{
        
        private final String title;
        private final Icon icon;
        private final ClientIcon clientIcon;
        private final Id iconId;
        private final T value;
        
        public Item(final String title, final Icon icon, final T value){
            this.title = title;
            this.icon = icon;
            clientIcon = null;
            iconId = null;
            this.value = value;
        }
        
        public Item(final String title, final ClientIcon icon, final T value){
            this.title = title;
            this.icon = null;
            clientIcon = icon;
            iconId = null;
            this.value = value;
        }
        
        public Item(final String title, final Id iconId, final T value){
            this.title = title;
            this.iconId = iconId;
            icon = null;
            clientIcon = null;
            this.value = value;
        }        

        public QIcon getIcon(final DefManager defManager) {
            if (icon==null){
                if (clientIcon==null){
                    return iconId==null ? null : ExplorerIcon.getQIcon(defManager.getImage(iconId));
                }else{
                    return ExplorerIcon.getQIcon(clientIcon);
                }                
            }else{
                return ExplorerIcon.getQIcon(icon);
            }            
        }

        public String getTitle() {
            return title;
        }

        public T getValue() {
            return value;
        }        
    }
    
    public static interface ItemsProvider<T>{
        List<Item<T>> getItems();
    }
    
    private static class LoadItemsTask<T> implements Callable<List<Item<T>>>{
        private final ItemsProvider<T> provider;
        
        public LoadItemsTask(final ItemsProvider<T> provider){
            this.provider = provider;
        }

        @Override
        public List<Item<T>> call() throws Exception {
            return provider.getItems();
        }
    }
    
    private final LoadItemsTask<T> loadItemsTask;
    private final Item<T> initialItem;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "LazyComboBox Thread");
        }
    });;
    private final QListWidget popupList = new QListWidget(this);
    private final QLineEdit lineEdit = new QLineEdit(this);
    private final DefManager defManager;
    private String waitingText = "Please wait...";
    private Future<List<Item<T>>> loadingItemsFuture;
    private int loadingTimer;
    private long popupTime;

    public final com.trolltech.qt.QSignalEmitter.Signal0 loadingFinished = new Signal0();
    
    public LazyComboBox(final ItemsProvider<T> itemsProvider, final Item<T> initialItem, final boolean instantLoad, final DefManager defManager, final QWidget parentWidget){
        super(parentWidget);
        loadItemsTask = new LoadItemsTask<>(itemsProvider);
        this.defManager = defManager;
        this.initialItem = initialItem;
        lineEdit.setReadOnly(true);
        setLineEdit(lineEdit);
        setModel(popupList.model());
        setView(popupList);        
        addInitialItem();
        if (instantLoad){
            startLoading();
        }
    }
    
    private QListWidgetItem addItem(final Item<T> item){
        final QListWidgetItem listItem = new QListWidgetItem(item.getIcon(defManager), item.getTitle());
        listItem.setData(Qt.ItemDataRole.UserRole, item);
        popupList.addItem(listItem);
        return listItem;
    }
    
    private void addInitialItem(){
        if (initialItem!=null){
            addItem(initialItem).setForeground(new QBrush(QColor.gray));
            setCurrentIndex(0);
        }
    }
    
    private void initPleaseWaitItems(){
        clear();
        addPleaseWaitItem("");
        addPleaseWaitItem("");
        addPleaseWaitItem(waitingText);
        addPleaseWaitItem("");
        addPleaseWaitItem("");        
        setCurrentIndex(-1);
    }
    
    private void addPleaseWaitItem(final String text){
        final QListWidgetItem item = new QListWidgetItem(text);
        item.setTextAlignment(Qt.AlignmentFlag.AlignCenter.value());
        item.setFlags(Qt.ItemFlag.NoItemFlags);
        popupList.addItem(item);
    }
    
    private void startLoading(){
        loadingItemsFuture = executor.submit(loadItemsTask);
        loadingTimer = startTimer(100);
    }
    
    public boolean itemsLoaded(){
        return loadingItemsFuture!=null && loadingItemsFuture.isDone();
    }
    
    @SuppressWarnings("unchecked")
    public T getCurrentItemValue(){
        final int idx = currentIndex();
        if (idx>0){
            final Item<T> item = (Item<T>)itemData(idx);
            if (item!=null){
                return item.getValue();
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public T getItemValue(int index){
        return ((Item<T>)itemData(index)).getValue();
    }

    public String getWaitingText() {
        return waitingText;
    }

    public void setWaitingText(final String waitingText) {
        this.waitingText = waitingText;
    }
    
    public boolean isPopupActive(){
        return popupTime>0;
    }

    @Override
    public void showPopup() {
        if (!itemsLoaded()){
            initPleaseWaitItems();
            if (loadingItemsFuture==null){
                startLoading();
            }
        }
        popupTime = System.currentTimeMillis();
        super.showPopup();
    }        

    @Override
    public void hidePopup() {
        popupTime = 0;
        super.hidePopup();
        if (!itemsLoaded()){
            popupList.clear();
            addInitialItem();
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent closeEvent) {
        if (loadingTimer!=0){
            killTimer(loadingTimer);
            loadingTimer = 0;
        }
        executor.shutdownNow();        
        super.closeEvent(closeEvent);
    }

    @Override
    protected void timerEvent(final QTimerEvent timerEvent) {
        if (timerEvent.timerId()==loadingTimer){
            timerEvent.accept();
            if (itemsLoaded()){
                executor.shutdown();//dispose thread
                if (popupTime==0 || System.currentTimeMillis()-popupTime>1000){                
                    killTimer(loadingTimer);
                    loadingTimer = 0;
                    List<Item<T>> loadedItems;
                    try{
                        loadedItems = loadingItemsFuture.get();
                    }
                    catch(InterruptedException|ExecutionException exception){
                        loadedItems = Collections.<Item<T>>emptyList();
                    }
                    popupList.clear();
                    if (loadedItems.isEmpty()){
                        addInitialItem();
                        if (isPopupActive()){
                            hidePopup();
                        }
                        loadingFinished.emit();
                    }else{
                        final boolean needForPopup = isPopupActive();
                        if (needForPopup){
                            setUpdatesEnabled(false);
                        }
                        try{
                            for (Item<T> item: loadedItems){
                                addItem(item);
                            }
                            setCurrentIndex(0);                            
                            loadingFinished.emit();
                        }
                        finally{
                            if (needForPopup){
                                hidePopup();
                                setUpdatesEnabled(true);
                                showPopup();
                            }
                        }
                    }
                }
            }
        }else{
            super.timerEvent(timerEvent);
        }
    }
    
    
}
