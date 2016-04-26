
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class TreeViewItem extends Item{
    public static final TreeViewItem DEFAULT = new TreeViewItem();
    
    public TreeViewItem() {
        super(Group.GROUP_ITEM_WIDGETS, NbBundle.getMessage(TreeViewItem.class, "TreeView"), AdsMetaInfo.TREE_VIEW_CLASS);
    }
    
    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawShadePanel(gr, r, false, 1, DrawUtil.COLOR_BASE);
    }
    
}
