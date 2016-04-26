
package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

public enum EEntityPresentationInheritance implements IKernelIntEnum {

     SINGULAR_TITLE(1, "Title for singular"),
     PLURAL_TITLE(2, "Title for plural"),
     OBJECT_TITLE_FORMAT(4, "Object Title Format");
    
    private final long value;
    private final String name;
    
    EEntityPresentationInheritance(long value, String name){
        this.value = value;
        this.name = name;
    }
    
    @Override
    public String getName() {
       return name; 
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static EEntityPresentationInheritance getForValue(final long val) {
        for (EEntityPresentationInheritance e : EEntityPresentationInheritance.values()) {
            if (e.value == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError(EPropAttrInheritance.class.getSimpleName() + " has no item with value: " + String.valueOf(val),val);
    }
    
}
