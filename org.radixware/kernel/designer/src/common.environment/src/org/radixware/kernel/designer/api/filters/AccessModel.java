package org.radixware.kernel.designer.api.filters;

import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;

@NbBundle.Messages({"ApiBrowserTopComponent_ApiFilterPanel=Access level"})
public class AccessModel extends GenericComboBox.Model {

    List<EAccess> accessLevel = new ArrayList<>();
    boolean onlyPublished = true;

    public AccessModel() {
        accessLevel.add(EAccess.PUBLIC);
        accessLevel.add(EAccess.PROTECTED);
    }

    @Override
    public String getName() {
        return Bundle.ApiBrowserTopComponent_ApiFilterPanel();
    }

    public String getToolTip() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (EAccess access : accessLevel) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(access.getName());
        }

        if (sb.length() > 0 && onlyPublished) {
            sb.append(", only published");
        }

        return sb.toString();
    }

    public List<EAccess> getAccessLevel() {
        return accessLevel;
    }

    public boolean isOnlyPublished() {
        return onlyPublished;
    }
}
