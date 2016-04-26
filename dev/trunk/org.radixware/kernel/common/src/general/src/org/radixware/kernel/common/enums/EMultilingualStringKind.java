
package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EMultilingualStringKind  implements IKernelIntEnum {
    
    TITLE("Title",1),
    EVENT_CODE("Event code",2),
    DESCRIPTION("Description",3),
    TOOLTIP("Tooltip",4),
    CODE("Code",5);
    
    
    private final int val;
     private final String name;
    
    private EMultilingualStringKind(String name,int val) {
        this.name = name;
        this.val = val;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return Long.valueOf(val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    public static EMultilingualStringKind getForValue(final int val) {
        for (EMultilingualStringKind e : EMultilingualStringKind.values()) {
            if (e.val == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ELocalizedStringKind has no item with value: " + String.valueOf(val),val);
    }
    
    public static EMultilingualStringKind getForName(String name){
        for (EMultilingualStringKind item : EMultilingualStringKind.values()){
            if (item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
