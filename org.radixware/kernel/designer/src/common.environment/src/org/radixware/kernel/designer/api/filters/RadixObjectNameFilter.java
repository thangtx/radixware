package org.radixware.kernel.designer.api.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.components.selector.EmptyTextFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;

public class RadixObjectNameFilter extends ItemNameFilter<RadixObject>{
    public String getName(RadixObject value) {
        return value.getName().toLowerCase();
    }
}
