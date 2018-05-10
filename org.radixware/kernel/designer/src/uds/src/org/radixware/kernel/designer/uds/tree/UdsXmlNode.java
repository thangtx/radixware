package org.radixware.kernel.designer.uds.tree;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;

public class UdsXmlNode extends UdsFileNode  {

    protected UdsXmlNode(UdsXmlFile xmlFile) {
        super(xmlFile, xmlFile.getUdsDefinitions().isEmpty()? Children.LEAF : new RadixObjectsNodeSortedChildren(xmlFile.getUdsDefinitions()));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);

    }
    
    public boolean canCompile(){
        return true;
    }
    
}
