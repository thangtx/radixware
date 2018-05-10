package org.radixware.kernel.designer.uds.tree;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.designer.uds.tree.actions.UdsReloadAction;

public class UdsFileNode extends RadixObjectNode{

    protected UdsFileNode(UdsFile radixObject) {
        super(radixObject, Children.LEAF);
        initNode(radixObject);
    }

    protected UdsFileNode(UdsFile radixObject, Children children) {
        super(radixObject, children);
        initNode(radixObject);
    }
    
    private void initNode(UdsFile radixObject){
        addCookie(new UdsReloadAction.Cookie(radixObject));
        if (canCompile()){
            addCookie(new AbstractBuildAction.CompileCookie(radixObject));
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        if(getUdsFile().isDirectory()){
            return null;
        }
        return new UdsFileEditCookie(getUdsFile());
    }
    
    public UdsFile getUdsFile() {
        return (UdsFile) getRadixObject();
    }
    
    public boolean canCompile(){
        return false;
    }
    

    private static class UdsFileEditCookie extends RadixObjectEditCookie {

        public UdsFileEditCookie(UdsFile file) {
            super(file);
        }

        public UdsFile getUdsFile() {
            return (UdsFile) getRadixObject();
        }

        @Override
        public void edit() {
            final UdsFile udsFile = getUdsFile();
            final File file = udsFile.getFile();
            DialogUtils.editFile(file);
        }
    }
    
    
    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<UdsFile> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(UdsFile file) {
            if (file instanceof UdsXmlFile){
                return new UdsXmlNode((UdsXmlFile) file);
            }
            if (file instanceof UdsDirectory){
                return new UdsDirectoryNode((UdsDirectory) file);
            }
            return new UdsFileNode(file);
        }
    }
    
    @Override
    protected DefinitionRenameAction.RenameCookie createRenameCookie() {
        return new DefinitionRenameAction.RenameCookie(getRadixObject());
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(null);
        actions.add(SystemAction.get(DefinitionRenameAction.class));
        actions.add(SystemAction.get(UdsReloadAction.class));
        if (canCompile()){
            actions.add(SystemAction.get(CompileDefinitionAction.class));
        }
        actions.add(null);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
    }
    
    @Override
    public boolean canCopy() {
        return false;
    }
    
    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        return RadixMutex.readAccess(new Mutex.Action<Transferable>() {
            @Override
            public Transferable run() {
                if (canCopy()) {
                    try {
                        return UdsClipboardUtils.copy(getRadixObject());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        return RadixMutex.readAccess(new Mutex.Action<Transferable>() {
            @Override
            public Transferable run() {
                if (canCut()) {
                    try {
                        return UdsClipboardUtils.cut(getRadixObject());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                return null;
            }
        });
    }
    

}
