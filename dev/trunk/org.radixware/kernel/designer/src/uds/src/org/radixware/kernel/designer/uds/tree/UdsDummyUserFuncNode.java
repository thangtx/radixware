package org.radixware.kernel.designer.uds.tree;

import java.util.List;
import java.util.Set;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.uds.userfunc.UdsDummyUserFuncDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.CompileCookie;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewServerSourceAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewServerSourceAction.ViewServerSourceCookie;

public class UdsDummyUserFuncNode extends RadixObjectNode{

    public UdsDummyUserFuncNode(UdsDummyUserFuncDef radixObject) {
        super(radixObject, Children.LEAF);
        Set<JavaSourceSupport.CodeType> types = radixObject.getJavaSourceSupport().getSeparateFileTypes(ERuntimeEnvironmentType.SERVER);
        addCookie(new ViewServerSourceCookie(radixObject, types));
        addCookie(new CompileCookie(radixObject));
    }
    
    @Override
    protected DefinitionRenameAction.RenameCookie createRenameCookie() {
        return null;
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(null);
        actions.add(SystemAction.get(CompileDefinitionAction.class));
        actions.add(SystemAction.get(ViewServerSourceAction.class));
        actions.add(null);

    }
    
        
    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<UdsDummyUserFuncDef> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(UdsDummyUserFuncDef radixObject) {
            return new UdsDummyUserFuncNode(radixObject);
        }
    }
}
