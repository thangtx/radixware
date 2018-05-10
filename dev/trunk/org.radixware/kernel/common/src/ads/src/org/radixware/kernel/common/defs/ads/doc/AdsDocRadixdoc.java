package org.radixware.kernel.common.defs.ads.doc;

import org.radixware.kernel.common.defs.ads.radixdoc.AdsDefinitionRadixdoc;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;
import org.radixware.schemas.radixdoc.Page;

/**
 * Class for documentation RadixDoc2 definitions. Which will be used to create TechDoc.
 */
abstract class AdsDocRadixdoc<T extends AdsDocDef> extends AdsDefinitionRadixdoc<AdsDocDef> {

    public AdsDocRadixdoc(T source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    public AbstractDefDocItem buildDocItem(AbstractDefDocItem xDefDocItem) {

        xDefDocItem.setId(source.getId());
        xDefDocItem.setName(source.getName());
        xDefDocItem.setDefinitionType(source.getDefinitionType());

        if (source.getTitleId() != null) {
            xDefDocItem.setTitleId(source.getTitleId());
        }

        return xDefDocItem;
    }

    @Override
    public boolean isGenerateHtmlDoc() {
        return false;
    }
}
