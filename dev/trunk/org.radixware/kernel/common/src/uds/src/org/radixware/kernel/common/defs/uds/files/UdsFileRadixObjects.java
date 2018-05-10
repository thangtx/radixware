package org.radixware.kernel.common.defs.uds.files;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IDefinitionContainer;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.types.Id;

public class UdsFileRadixObjects extends RadixObjects<RadixObject> implements IDefinitionContainer{
    private Map<String, IUdsFileDefinition> xPath2RadixObject = null;
    
    public UdsFileRadixObjects(RadixObject container) {
        super(container);
    }

    public UdsFileRadixObjects(String name) {
        super(name);
    }
    
    @Override
    protected void onAdd(RadixObject udsFileDef) {
        if (udsFileDef instanceof IUdsFileDefinition) {
            IUdsFileDefinition udsFileDefinition = (IUdsFileDefinition) udsFileDef;
            if (xPath2RadixObject != null) {
                xPath2RadixObject.put(udsFileDefinition.getXPath(), udsFileDefinition);
            }
        }
    }
    
    @Override
    protected void onRemove(RadixObject udsFileDef) {
        if (udsFileDef instanceof IUdsFileDefinition){
                if (xPath2RadixObject != null) {
                    xPath2RadixObject.remove(((IUdsFileDefinition)udsFileDef).getXPath());
                }
        }
    }

    public IUdsFileDefinition findByXPath(String xPath) {
        if (xPath == null || isEmpty()) {
            return null;
        }
        if (xPath2RadixObject != null) {
            return xPath2RadixObject.get(xPath);
        }

        if (xPath2RadixObject == null) {
            xPath2RadixObject = new ConcurrentHashMap<>();
            for (RadixObject radixObject : this) {
                if (radixObject instanceof IUdsFileDefinition) {
                    IUdsFileDefinition udsFileDefinition = (IUdsFileDefinition) radixObject;
                    if (udsFileDefinition.getXPath() != null) {
                        xPath2RadixObject.put(udsFileDefinition.getXPath(), udsFileDefinition);
                    }
                }
            }
        }
        return xPath2RadixObject.get(xPath);
    }
    
    public AdsDefinition findById(Id id) {
        if (id == null || isEmpty()) {
            return null;
        }

        for (RadixObject radixObject : this) {
            if (radixObject instanceof AdsDefinition) {
                AdsDefinition def = (AdsDefinition) radixObject;
                if (id.compareTo(def.getId()) == 0) {
                    return def;
                }
            } else if (radixObject instanceof UdsFileRadixObjects){
                AdsDefinition result = ((UdsFileRadixObjects) radixObject).findById(id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
