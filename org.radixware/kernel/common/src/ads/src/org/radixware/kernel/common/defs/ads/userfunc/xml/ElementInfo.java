package org.radixware.kernel.common.defs.ads.userfunc.xml;

import org.w3c.dom.Element;

public class ElementInfo {
    private final Element element;
    private final UserFuncLibInfo lib;

    public ElementInfo(Element element, UserFuncLibInfo lib) {
        this.element = element;
        this.lib = lib;
    }

    public Element getElement() {
        return element;
    }

    public UserFuncLibInfo getLib() {
        return lib;
    }
}
