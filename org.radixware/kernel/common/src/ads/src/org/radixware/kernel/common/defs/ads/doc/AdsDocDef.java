package org.radixware.kernel.common.defs.ads.doc;

import org.radixware.kernel.common.defs.IPropertyModule;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.TitledAdsDefinition;

/**
 * Class for TechDoc documentation.
 *
 * @author dkurlyanov
 */
public abstract class AdsDocDef extends AdsTitledDefinition implements IRadixdocProvider, IPropertyModule {

    protected AdsDocDef(Id id) {
        super(id, null, null);
    }

    protected AdsDocDef(TitledAdsDefinition classDef) {
        super(classDef);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public String toString() {
        String additionalTitle = "";
        if (getMainDocLanguage() != null
                && getTitleId() != null
                && !Utils.emptyOrNull(getTitle(getMainDocLanguage()))) {
            additionalTitle = " (" + getTitle(getMainDocLanguage()) + ")";
        }
        return getName() + additionalTitle;
    }

    public EIsoLanguage getMainDocLanguage() {
        return getLayer().getMainDocLanguage();
    }

    // TODO: !!! need del
    @Override
    public boolean isPublished() {
        return true;
    }

}
