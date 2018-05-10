package org.radixware.kernel.designer.ads.editors.pages;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsProperiesGroupDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IPagePropertyGroup;

public interface ICurrentGroupListener {
    public IPagePropertyGroup getCurrentPagePropertyGroup();
    
    public void removeChildPropertyGroup(AdsProperiesGroupDef group);
}
