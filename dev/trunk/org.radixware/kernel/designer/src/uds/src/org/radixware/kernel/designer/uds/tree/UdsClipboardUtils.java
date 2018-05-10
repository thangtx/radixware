package org.radixware.kernel.designer.uds.tree;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.datatransfer.PasteType;
import org.radixware.kernel.common.defs.RadixObject;

public class UdsClipboardUtils {
        
    public static DataObject getFileDataObject(RadixObject radixObject) throws IOException {
        File file = radixObject.getFile();
        if (file != null) {
            FileObject obj = FileUtil.createData(file);
            return DataObject.find(obj);
        }
        return null;
    }
    
    public static Transferable copy(RadixObject radixObject) throws IOException {
        DataObject data = getFileDataObject(radixObject);
        Node node = data.getNodeDelegate();
        return node.clipboardCopy();
    }
    
    public static Transferable cut(RadixObject radixObject) throws IOException {
        DataObject data = getFileDataObject(radixObject);
        Node node = data.getNodeDelegate();
        return node.clipboardCut();
    }
    
    public static PasteType[] paste(RadixObject dest, Transferable t) throws IOException {
        DataObject data = getFileDataObject(dest);
        Node node = data.getNodeDelegate();
        return node.getPasteTypes(t);
    }
}
