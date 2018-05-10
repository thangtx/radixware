package org.radixware.kernel.designer.uds.tree;

import java.util.List;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncLib;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;

public class UdsUserFuncLibNode extends RadixObjectsNode{

    public UdsUserFuncLibNode(UdsUserFuncLib radixObjects) {
        super(radixObjects, new RadixObjectsNodeSortedChildren(radixObjects));
        addCookie(new AbstractBuildAction.CompileCookie(radixObjects));
    }
    
    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(null);
        actions.add(SystemAction.get(CompileDefinitionAction.class));
        actions.add(null);
    }
    
    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<UdsUserFuncLib> {

        @Override // Registered in layer.xml
        public UdsUserFuncLibNode newInstance(UdsUserFuncLib radixObject) {
            return new UdsUserFuncLibNode(radixObject);
        }
    }
}
