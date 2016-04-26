
package org.radixware.kernel.designer.ads.localization;

import org.radixware.kernel.common.defs.ads.build.Cancellable;


public class CancellableImpl implements Cancellable {

    MultilingualEditor editor;

    public CancellableImpl(MultilingualEditor editor) {
        this.editor = editor;
    }
    
    private volatile boolean cancel = false;

    @Override
    public boolean cancel() {
        cancel = true;
        afterCanceled();
        return cancel;
    }

    @Override
    public boolean wasCancelled() {
        return cancel;
    }
    
    public void afterCanceled(){
        if (editor != null){
            editor.resetStringVersion();
        }
    }
}
