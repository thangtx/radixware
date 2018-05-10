package org.radixware.kernel.designer.uds.creation;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;

public class UdsDirectoryCreature extends NamedRadixObjectCreature<UdsDirectory>{

    public UdsDirectoryCreature(RadixObjects container) {
        super(container, "Uds Folder");
    }

    @Override
    public UdsDirectory createInstance(String name) {
        return new UdsDirectory(name);
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.FOLDER;
    }

}
