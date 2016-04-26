
package org.radixware.kernel.designer.ads.localization;

import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;


public class MultilingualEditorProgressHandle {
    final private ProgressHandleFactoryDelegate factory = new ProgressHandleFactoryDelegate();
    final MultilingualEditor editor;
    IProgressHandle progress;

    public MultilingualEditorProgressHandle(MultilingualEditor editor) {
        this.editor = editor;
    }

    public void startProgress(Cancellable cancellable){
        String title = "Searching multilingual strings...";
        if (progress != null){
            finishProgress();
        }
        if (cancellable == null){
            progress = factory.createHandle(title); 
        } else {
            progress = factory.createHandle(title, cancellable); 
        }
        
        editor.firePropertyChange(MultilingualEditor.PROGRESS_HANDLE, false, true);
        progress.start();
    }
    
    
    public void finishProgress(){
        if (!isInProcess()) return;
        editor.firePropertyChange(MultilingualEditor.PROGRESS_HANDLE, true, false);
        progress.finish();
        progress = null;
    }
    
    boolean isInProcess(){
        return progress != null;
    }
}
