package org.radixware.kernel.common.mml;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.doc.DocResources;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.xscml.DocResource;

public abstract class MmlTagMarkdownResource extends Mml.Tag {

    protected String title;
    protected String fileName;
    protected String moduleName;
    protected String layerUri;
    protected EIsoLanguage language;
    private AdsModule tempModule;

    public MmlTagMarkdownResource(AdsModule module, EIsoLanguage lang) {
        super(null);
        moduleName = module.getName();
        tempModule = module;
        layerUri = module.getLayer().getURI();
        language = lang;
    }

    public MmlTagMarkdownResource(DocResource markdownRef) {
        super(markdownRef);
        title = markdownRef.getTitle();
        fileName = markdownRef.getFileName();
        moduleName = markdownRef.getModuleName();
        layerUri = markdownRef.getLayerUri();
        language = markdownRef.getLanguage();
    }

    public void appendTo(DocResource markdownRef) {
        super.appendTo(markdownRef);
        markdownRef.setTitle(title);
        markdownRef.setFileName(fileName);
        markdownRef.setLayerUri(layerUri);
        markdownRef.setModuleName(moduleName);
        markdownRef.setLanguage(language);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag resource={0}]", fileName);
    }

    @Override
    public String getDisplayName() {
        if (getOwnerMml() == null) {
            return "Unknown";
        }
        return "[" + title + "]" + "(" + rememberDisplayName(fileName) + ")";
    }

    @Override
    public void check(IProblemHandler problemHandler, Mml.IHistory h) {
        // TODO: !!!
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    public String getToolTip(EIsoLanguage language) {
        return "";
    }

    @Override
    public String getMarkdown(MarkdownGenerationContext context) {
        return "[" + title + "]("
                + context.getTechDocDir().toPath().relativize(context.getResourceDir().toPath()).toString() + File.separator
                + layerUri + File.separator
                + moduleName + File.separator
                + context.getLanguage().getValue() + File.separator
                + fileName + ")";
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public ArrayList<RadixObject> getGoToDependencies() {
        AdsModule module = (AdsModule) getModule();
        DocResources docResources = module.getDocumentation().getResources(language);

        File file = new File(docResources.getDir(), fileName);
        org.radixware.kernel.common.defs.ads.doc.DocResource res = docResources.get(file);

        ArrayList<RadixObject> dependences = new ArrayList<>();
        if (res != null) {
            dependences.add(res);
        }

        return dependences;
    }

    @Override
    public Module getModule() {
        return Utils.nvlOf(super.getModule(), tempModule);
    }

}
