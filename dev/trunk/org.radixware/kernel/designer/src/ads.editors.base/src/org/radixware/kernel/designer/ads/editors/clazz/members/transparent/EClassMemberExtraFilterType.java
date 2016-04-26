
package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;


public enum EClassMemberExtraFilterType {
    
    OVERRIDEN_METHODS("overriden methods", EClassMemberType.METHOD);
    
    private final String name;
    private final EClassMemberType member;
    
    private EClassMemberExtraFilterType(String name,EClassMemberType member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public EClassMemberType getMember() {
        return member;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
