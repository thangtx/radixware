
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.util.List;
import org.netbeans.api.visual.action.WidgetAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class ListViewWidget extends BaseWidget{

    public ListViewWidget(GraphSceneImpl scene, AdsItemWidgetDef node) {
        super(scene, node);
    }
    
    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        return super.getInitialActions(scene, node);
    }
}
