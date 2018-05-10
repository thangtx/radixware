package org.radixware.kernel.designer.uds.tree;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.uds.tree.actions.UdsReloadAction;

public class UdsExchangeReportNode extends RadixObjectsNode {
    
     public UdsExchangeReportNode(UdsExchangeReport radixObjects) {
        super(radixObjects, new RadixObjectsNodeSortedChildren(radixObjects));
        addCookie(new AbstractBuildAction.CompileCookie(radixObjects));
        addCookie(new UdsReloadAction.Cookie(radixObjects));
    }
    
    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(null);
        actions.add(SystemAction.get(UdsReloadAction.class));
        actions.add(SystemAction.get(CompileDefinitionAction.class));
        actions.add(null);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
    }
    
    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<UdsExchangeReport> {

        @Override // Registered in layer.xml
        public UdsExchangeReportNode newInstance(UdsExchangeReport radixObject) {
            return new UdsExchangeReportNode(radixObject);
        }
    }

    @Override
    public Image getIcon(int type) {
        return AdsDefinitionIcon.CLASS_USER_REPORT.getImage();
    }
}
