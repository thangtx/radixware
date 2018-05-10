package org.radixware.kernel.common.defs.ads.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.DocumentationMapDefinition;
import org.radixware.schemas.adsdef.DocumentationReference;
import org.radixware.schemas.radixdoc.Page;

/**
 * DITA Map. Объект содрежить в себе ссылки на Topics и другие Maps.
 * Используется для иеархизации документации.
 * More info https://ru.wikipedia.org/wiki/DITA
 *
 * @author dkurlyanov
 */
public final class AdsDocMapDef extends AdsDocDef {

    private DocReferences references = new DocReferences(this);

    public static class Factory {

        public static AdsDocMapDef loadFrom(DocumentationMapDefinition classDef) {
            return new AdsDocMapDef(classDef);
        }

        public static AdsDocMapDef newInstation() {
            return new AdsDocMapDef(Id.Factory.newInstance(EDefinitionIdPrefix.TECHNICAL_DOCUMENTATION_MAP));
        }

        public static AdsDocMapDef newInstation(AdsModule owner) {
            AdsDocMapDef map = AdsDocMapDef.Factory.newInstation();
            owner.getDefinitions().add(map);
            return map;
        }
    }

    protected AdsDocMapDef(Id id) {
        super(id);
    }

    protected AdsDocMapDef(DocumentationMapDefinition classDef) {
        super(classDef);
        List<DocumentationReference> documentationReferenceList = classDef.getItemList();
        if (documentationReferenceList != null) {
            for (DocumentationReference documentationReference : documentationReferenceList) {
                DocReference refDoc = new DocReference(documentationReference.getPath(), documentationReference.getType());
                references.add(refDoc);
            }
        }
    }

    // main
    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsDocMapDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new AdsDocMapRadixdoc(AdsDocMapDef.this, page, options);
            }
        };
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        references.visit(visitor, provider);
        references.visitChildren(visitor, provider);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        if (saveMode != ESaveMode.NORMAL) {
            return;
        }

        appendTo(xDefRoot.addNewAdsDocumentationMapDefinition(), saveMode);

        for (DocReference ref : references) {
            DocumentationReference xDocRef = xDefRoot.getAdsDocumentationMapDefinition().addNewItem();
            xDocRef.setPath(ref.getPath().asList());
            xDocRef.setType(ref.getType());
        }
    }

    @Override
    public void save() throws IOException {
        AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
        appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);
        super.save();
    }

    // get
    public List<AdsDocDef> getDocObjects() {
        //NOTE: мы не можем кешировать docObjects т.к. мы должны постоянно проверять валидность ссылок.
        List<AdsDocDef> docObjects = new ArrayList<>();
        for (final DocReference ref : references) {
            AdsDocDef def = ref.getDocDef();
            if (def != null) {
                docObjects.add(def);
            }
        }
        return docObjects;
    }

    public DocReferences getReferences() {
        return references;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.TECHNICAL_DOCUMENTATION.MAP;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.TECHNICAL_DOCUMENTATION_MAP;
    }

}
