
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EViewMode;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class ListViewItem extends Item{
    
    public static final ListViewItem DEFAULT = new ListViewItem();
    
    public ListViewItem() {
        super(Group.GROUP_ITEM_WIDGETS, NbBundle.getMessage(ListViewItem.class, "ListView"), AdsMetaInfo.LIST_VIEW_CLASS);
    }
    
    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        DrawUtil.drawShadePanel(gr, r, false, 1, DrawUtil.COLOR_BASE);
    }
    
}
