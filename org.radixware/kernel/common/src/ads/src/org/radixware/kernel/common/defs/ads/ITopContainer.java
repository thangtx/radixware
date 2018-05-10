package org.radixware.kernel.common.defs.ads;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;

public interface ITopContainer {
    
    public File getSourceFile(AdsDefinition def, AdsDefinition.ESaveMode saveMode);
    
    public void save(AdsDefinition def, AdsDefinition.ESaveMode saveMode) throws IOException;
    
    /**
     * Returns definition for given id (if any) or null
     */
    public AdsDefinition findById(Id id);
    
    /**
     * Try to load definition from its file and replace the current definition,
     * if succesfull.
     *
     * @return loaded definition.
     */
    public RadixObject reload(AdsDefinition oldDef) throws IOException;

}
