package org.radixware.kernel.common.defs.ads.doc;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.EDocRefType;
import org.radixware.kernel.common.enums.ENamingPolicy;
import static org.radixware.kernel.common.enums.ENamingPolicy.FREE;
import org.radixware.kernel.common.types.Id;

/**
 * Class containing pointer to {@link AdsDocDef}, use for the structure of
 * TechDoc. Use in {@link AdsDocMapDef}.
 *
 * @see AdsDocDef
 * @see AdsDocMapDef
 * @see DocReferences
 * @author dkurlyanov
 */
public final class DocReference extends RadixObject {

    private AdsPath path;
    private EDocRefType type;
    private AdsDocDef docDef;
    private boolean cache;

    public DocReference(AdsPath path, EDocRefType type) {
        super("ref:" + path.getTargetId().toString());
        this.path = path;
        this.type = type;
    }

    public DocReference(List<Id> ids, EDocRefType type) {
        this(new AdsPath(ids), type);
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return FREE;
    }

    public AdsPath getPath() {
        return path;
    }

    public EDocRefType getType() {
        return type;
    }

    public Id getId() {
        return path.getTargetId();
    }

    public AdsDocDef getDocDef() {
        if (!cache) {
            docDef = (AdsDocDef) getBranch().find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return (radixObject instanceof AdsDocDef) && (((AdsDocDef) radixObject).getId() == getId());
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return !(radixObject instanceof Definition) || (radixObject instanceof AdsModule);
                }

            });
        }
        return docDef;
    }

    public boolean isValid() {
        if (!cache) {
            getDocDef();
        }
        return docDef != null;
    }

}
