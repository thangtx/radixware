package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef.PagePropertyRef;
import org.radixware.kernel.common.types.Id;

public interface IPagePropertyGroup {

    public PagePropertyRef findItemById(Id id);

    public int getColumnCount();

    public int getRowCount();

    public void collectDependences(List<Definition> list);
    
    public void add(PagePropertyRef pagePropertyRef);
    
    public boolean remove(PagePropertyRef pagePropertyRef);

    public PagePropertyRef addGroup(AdsProperiesGroupDef group);
    
    public boolean removeGroup(AdsProperiesGroupDef group);

    public List<PagePropertyRef> list();
    
    public List<PagePropertyRef> list(IFilter<PagePropertyRef> filter);

    public PagePropertyRef addPropId(Id id);

    public boolean removeByPropId(Id id);

    public void getIds(List<Id> ids);

    public boolean isEmpty();
}
