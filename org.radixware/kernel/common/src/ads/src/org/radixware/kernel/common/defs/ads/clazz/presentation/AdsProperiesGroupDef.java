package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef.PagePropertyRef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.EditorPageItem;
import org.radixware.schemas.adsdef.EditorPagePropertyGroup;

public class AdsProperiesGroupDef extends AdsPresentationsMember implements IPagePropertyGroup{

    List<PagePropertyRef> refs = new CopyOnWriteArrayList<>();;
    private boolean showFrame = true;

    public AdsProperiesGroupDef(){
        super(Id.Factory.newInstance(EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP), "NewPropertisGroup", null);
    }
    
    public AdsProperiesGroupDef(AdsProperiesGroupDef sourse){
        super(Id.Factory.newInstance(EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP), sourse.getName(), sourse.getTitleId());
        
        for (PagePropertyRef item : sourse.list()) {
            add(PagePropertyRef.Factory.copyPagePropertyRef(item));
        }
    }
    
    public AdsProperiesGroupDef(EditorPagePropertyGroup xGroup) {
        super(xGroup);
        if (xGroup.isSetShowFrame()) {
            showFrame = xGroup.getShowFrame();
        }

        for (EditorPageItem editorPageItem : xGroup.getEditorPageItemList()) {
            add(PagePropertyRef.Factory.loadFrom(editorPageItem));
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.PAGE_PROPERTY_GROUP;
    }

        public boolean isShowFrame() {
            return showFrame;
        }

        public void setShowFrame(boolean showFrame) {
            if (this.showFrame != showFrame){
                this.showFrame = showFrame;
                setEditState(EEditState.MODIFIED);
            }   
        }

    @Override
    public final void add(PagePropertyRef pagePropertyRef) {
        if (pagePropertyRef == null) {
            return;
        }

            if (refs.add(pagePropertyRef)) {
                try {
                    pagePropertyRef.setContainer(this);
                } catch (RadixError e) {
                    refs.remove(pagePropertyRef);
                    throw e;
                }
                setEditState(EEditState.MODIFIED);
            }
    }

    @Override
    public PagePropertyRef addGroup(AdsProperiesGroupDef group) {
        if (group == null) {
            return null;
        }
        PagePropertyRef ref = new PagePropertyRef(group, 0, 0);
        synchronized (this) {
            int row = getAutoRowNumver(0);
            if (refs.add(ref)) {
                try {
                    ref.setRow(row);
                    ref.setContainer(this);
                    setEditState(EEditState.MODIFIED);
                    return ref;
                } catch (RadixError e) {
                    refs.remove(ref);
                    throw e;
                }
            }
        }
        return null;
    }

    @Override
    public boolean removeGroup(AdsProperiesGroupDef group) {
        PagePropertyRef ref = group.getOwnerRef();
        if (ref != null){
            return remove(ref);
        }
        return false;
    }


    @Override
    public boolean remove(PagePropertyRef pagePropertyRef) {
        if (pagePropertyRef == null) {
            return false;
        }

        synchronized (this) {
            if (refs.remove(pagePropertyRef)) {
                pagePropertyRef.setContainer(null);
                setEditState(EEditState.MODIFIED);
                return true;
            }
        }

        return false;
    }

    public List<PagePropertyRef> list() {
        synchronized (this) {
            return new ArrayList<>(refs);
        }
    } 
    
    public List<PagePropertyRef> list(IFilter<PagePropertyRef> filter) {
        if (filter == null) {
            throw new NullPointerException();
        }
        final ArrayList<PagePropertyRef> res = new ArrayList<>(refs.size());
        for (PagePropertyRef obj : refs) {
            if (filter.isTarget(obj)) {
                res.add(obj);
            }
        }
        return res;
    }
    
    public boolean isPublished() {
        PagePropertyRef ref = getOwnerRef();
        if (ref != null){
            AdsEditorPageDef page = ref.getOwnerEditorPageDef();
            if (page != null){
                return page.isPublished();
            }
        }
        return true;
    }

    protected synchronized void appendTo(EditorPagePropertyGroup group, ESaveMode saveMode) {
        super.appendTo(group, saveMode);
        group.setName(getName());

        group.setShowFrame(showFrame);

        for (PagePropertyRef ref : refs) {
            ref.appendTo(group.addNewEditorPageItem(), saveMode);
        }
    }
         
        @Override
        public PagePropertyRef findItemById(Id id) {
            synchronized (this) {
                for (PagePropertyRef ref : refs) {
                     if (ref.getId() == id) {
                        return ref;
                    } else if (ref.getGroupDef() != null) {
                        AdsProperiesGroupDef group = ref.getGroupDef();
                        if (group.getId() == id) {
                            return ref;
                        } else {
                            PagePropertyRef pageProperty = ref.getGroupDef().findItemById(id);
                            if (pageProperty != null) {
                                return pageProperty;
                            }
                        }
                    }
                }
                return null;
            }
        }
        
        @Override
        public int getColumnCount() {
            int minColumn = -1;
            synchronized (this) {
                for (PagePropertyRef ref : refs) {
                    if (ref.getColumn() > minColumn) {
                        minColumn = ref.getColumn();
                    }
                }
            }
            return minColumn + 1;
        }

        @Override
        public int getRowCount() {
            int minRow = -1;
            synchronized (this) {
                for (PagePropertyRef ref : refs) {
                    if (ref.getRow() > minRow) {
                        minRow = ref.getRow();
                    }
                }
            }
            return minRow + 1;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            for (PagePropertyRef ref : refs) {
                final AdsDefinition def = ref.findItem();
                if (def != null) {
                    list.add(def);
                }
            }
        }
        
        private int getAutoRowNumver(int column) {
            boolean[] allownerRows = new boolean[getRowCount() + 1];
            Arrays.fill(allownerRows, false);
            for (PagePropertyRef ref : refs) {
                if (ref.getColumn() == column) {
                    allownerRows[ref.getRow()] = true;
                }
            }
            for (int i = 0; i < allownerRows.length; i++) {
                if (!allownerRows[i]) {
                    return i;
                }
            }
            return 0;
        }
        
        @Override
        public boolean removeByPropId(Id id) {
            synchronized (this) {
                PagePropertyRef ref = findItemById(id);
                if (ref == null) {
                    return false;
                } else {
                    return remove(ref);
                }
            }
        }

        @Override
        public PagePropertyRef addPropId(Id id) {
            synchronized (this) {
                PagePropertyRef ref = findItemById(id);
                if (ref != null) {
                    return ref;
                } else {
                    ref = PagePropertyRef.Factory.newInstance(id);
                    int row = getAutoRowNumver(0);
                    add(ref);
                    ref.setColumn(0);
                    ref.setRow(row);
                    return ref;
                }
            }
        }

        @Override
        public void getIds(List<Id> ids) {
            if (!refs.isEmpty()) {
                for (PagePropertyRef ref : refs) {
                    if (ref.getId() != null) {
                        ids.add(ref.getId());
                    } else if (ref.getGroupDef() != null) {
                        ref.getGroupDef().getIds(ids);
                    }
                }
            }
        }
        
        @Override        
        public boolean isEmpty() {
            return refs.isEmpty();
        }

    @Override
    public void setContainer(RadixObject container) {
        super.setContainer(container);
    }

    public PagePropertyRef getOwnerRef() {
        return getContainer() instanceof PagePropertyRef? (PagePropertyRef) getContainer(): null;
    }
    
    public AdsEditorPageDef getOwnerEditorPageDef() {
        PagePropertyRef ref = getOwnerRef();
        if (ref != null){
            return ref.getOwnerEditorPageDef();
        }
        return null;
    }
    
    public void collectProperiesGroups(List<AdsProperiesGroupDef> groups) {
        for (PagePropertyRef ref : refs) {
            if (ref.getGroupDef() != null) {
                groups.add(ref.getGroupDef());
                ref.getGroupDef().collectProperiesGroups(groups);
            }
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        
        for (PagePropertyRef ref : refs){
            ref.visit(visitor, provider);
        }
    }
    
    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PROPERTY_GROUP;
    }

}
