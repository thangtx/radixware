package org.radixware.kernel.common.defs;

import java.util.List;

public class VisitorFactory {

    private VisitorFactory() {
    }

    public static IVisitor newAppedingVisiter(final List<RadixObject> list) {
        return new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                list.add(radixObject);
            }
        };
    }

    public static IVisitor newAppedingDefinitionVisiter(final List<Definition> list) {
        return new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                if (!(radixObject instanceof Definition)) {
                    return;
                }
                Definition def = (Definition) radixObject;
                list.add(def);
            }
        };
    }

}
