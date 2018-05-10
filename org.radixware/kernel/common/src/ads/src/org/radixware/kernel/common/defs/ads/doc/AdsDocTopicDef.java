package org.radixware.kernel.common.defs.ads.doc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.DocumentationTopicDefinition;
import org.radixware.schemas.radixdoc.Page;

/**
 * DITA Topic. Объект содержащий в себе описание предмета которое можно
 * использовать в разных контекстах.
 * More info https://ru.wikipedia.org/wiki/DITA
 *
 * @author dkurlyanov
 */
public class AdsDocTopicDef extends AdsDocDef {

    Map<EIsoLanguage, DocTopicBody> bodyListCache = new HashMap<EIsoLanguage, DocTopicBody>();

    public static class Factory {

        public static AdsDocTopicDef loadFrom(DocumentationTopicDefinition classDef) {
            return new AdsDocTopicDef(classDef);
        }

        public static AdsDocTopicDef newInstation() {
            return new AdsDocTopicDef(Id.Factory.newInstance(EDefinitionIdPrefix.TECHNICAL_DOCUMENTATION_TOPIC));
        }

        public static AdsDocTopicDef newInstation(AdsModule owner) {
            AdsDocTopicDef topic = AdsDocTopicDef.Factory.newInstation();
            owner.getDefinitions().add(topic);
            return topic;
        }
    }

    protected AdsDocTopicDef(Id id) {
        super(id);
    }

    protected AdsDocTopicDef(DocumentationTopicDefinition classDef) {
        super(classDef);
    }

    // main
    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsDocTopicDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new AdsDocTopicRadixdoc(AdsDocTopicDef.this, page, options);
            }
        };
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        for (DocTopicBody body : bodyListCache.values()) {
            body.visit(visitor, provider);
        }
    }

    @Override
    public void save() throws IOException {

        AdsDefinitionDocument xDef = AdsDefinitionDocument.Factory.newInstance();
        appendTo(xDef.addNewAdsDefinition(), ESaveMode.NORMAL);

        for (DocTopicBody body : bodyListCache.values()) {
            body.save();
        }

        super.save();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        if (saveMode != ESaveMode.NORMAL) {
            return;
        }
        appendTo(xDefRoot.addNewAdsDocumentationTopicDefinition(), saveMode);
    }

    // del
    @Override
    public boolean delete() {
        deletaAllBody();
        return super.delete();
    }

    private void deletaAllBody() {
        for (EIsoLanguage lang : EIsoLanguage.values()) {
            if (DocTopicBody.existsFile(this, lang)) {
                DocTopicBody body = new DocTopicBody(this, lang);
                body.delete();
            }
        }
    }

    // get
    /**
     * @param language
     * @return loaded body. If it was not previously created, create it.
     */
    public DocTopicBody getBody(EIsoLanguage language) {
        DocTopicBody body = bodyListCache.get(language);
        if (body == null) {
            body = new DocTopicBody(this, language);
        }
        bodyListCache.put(language, body);
        return body;
    }

    public DocTopicBody getMainDocLanguageBody() {
        EIsoLanguage lang = getLayer().getMainDocLanguage();
        return getBody(lang);
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.TECHNICAL_DOCUMENTATION.TOPIC;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.TECHNICAL_DOCUMENTATION_TOPIC;
    }

}
