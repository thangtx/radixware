package org.radixware.kernel.common.mml;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.schemas.xscml.MmlType;

/**
 * 
 * @author dkurlyanov
 */
public class MmlTagId extends Mml.Tag {

    protected AdsPath path;
    private String moduleName;
    private String layerUri;
    private ERepositorySegmentType segment;

    MmlTagId(MmlType.Item.IdReference idRef) {
        super(idRef);
        this.path = new AdsPath(idRef.getPath());
        moduleName = idRef.getModuleName();
        layerUri = idRef.getLayerUri();
        segment = idRef.getSegment();
    }

    public MmlTagId(Definition def) {
        super(null);
        if (def == null) {
            throw new NullPointerException();
        }
        moduleName = def.getModule().getName();
        layerUri = def.getLayer().getURI();
        segment = def.getModule().getSegmentType();
        this.path = new AdsPath(def);
    }

    public Definition resolve(Definition referenceContext) {
        return resolveImpl(referenceContext).get();
    }

    private SearchResult<Definition> resolveImpl(Definition referenceContext) {
        if (referenceContext == null || path == null || path.isEmpty()) {
            return SearchResult.empty();
        } else {
            return this.path.resolveGlobal(referenceContext);
        }
    }

    public void appendTo(MmlType.Item item) {
        MmlType.Item.IdReference ref = item.addNewIdReference();
        appendTo(ref);
        ref.setPath(path.asList());
        ref.setLayerUri(layerUri);
        ref.setModuleName(moduleName);
        ref.setSegment(segment);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag id={0}]", path.toString());

    }

    @Override
    public String getDisplayName() {
        if (getOwnerMml() == null) {
            return "Unknown";
        }
        Definition context = getOwnerMml().getOwnerDefinition();
        Definition def = resolve(context);

        if (def != null) {
            return "[" + rememberDisplayName(def.getQualifiedName(context)) + "]";
        } else {
            return "unknownid[" + restoreDisplayName(AdsPath.toString(getPath())) + "]";
        }
    }

    @Override
    public void check(IProblemHandler problemHandler, Mml.IHistory h) {
        final Definition context = getOwnerMml().getOwnerDefinition();
        Definition def = resolveImpl(context).get(new SearchResult.CheckForDuplicatesAdvisor<Definition>(this, problemHandler));
        if (def == null) {
            if (path != null) {
                Definition something = path.resolveSomething(getOwnerMml().getOwnerDefinition());
                if (something != null) {
                    error(problemHandler, MessageFormat.format("Referenced subdefinition of {0} is not found {1}", something.getQualifiedName(), AdsPath.toString(path)));
                } else {
                    error(problemHandler, "Referenced definition not found " + AdsPath.toString(path));
                }
            } else {
                error(problemHandler, "Referenced definition not found " + AdsPath.toString(path));
            }
        } else {
            if (def instanceof AdsDefinition) {
                AdsUtils.checkAccessibility(this, (AdsDefinition) def, false, problemHandler);
            }
        }
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    public String getToolTip(EIsoLanguage language) {
        Definition def = null;
        final Mml mml = getOwnerMml();

        if (mml != null) {
            def = resolve(mml.getOwnerDefinition());
        } else {
            return "";
        }
        if (def == null) {
            StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("<b><font color=\"#FF0000\">Unresoved Reference</font></b>");
            if (path != null) {
                Definition something = path.resolveSomething(mml.getOwnerDefinition());
                if (something != null) {
                    b.append("<br>Last resolved owner: ");
                    b.append(something.getQualifiedName());
                }
                b.append("<br>Definition Path: ");
                b.append(path.toString().replace("<", "&lt;").replace(">", "&gt;"));
            }
            b.append("</html>");
            return b.toString();
        } else {
            return def.getToolTip(language, mml.getOwnerDefinition());
        }
    }

    @Override
    public String getMarkdown(MarkdownGenerationContext context) {
        //Definition context = getOwnerMml().getOwnerDefinition();
        //Definition def = resolve(context);
        //def.getQualifiedName(context)

        return "[" + path.toString() + "]("
                + context.getRadixDocDir().getPath() + File.separator
                + context.getLanguage().getValue() + File.separator
                + layerUri + File.separator
                + segment.getValue() + File.separator
                + moduleName + File.separator
                + path.getTargetId().toString() + ".html)";
    }

    public AdsPath getPath() {
        return path;
    }

    public void setPath(AdsPath path) {
        this.path = path;
        setEditState(RadixObject.EEditState.MODIFIED);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        Definition def = resolve(getOwnerMml().getOwnerDefinition());
        if (def != null) {
            list.add(def);
            if (def instanceof AdsEnumItemDef) {
                AdsEnumDef owner = ((AdsEnumItemDef) def).getOwnerEnum();
                if (owner != null) {
                    list.add(owner);
                }
            }
        }
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        super.collectDependences(list);
    }

}
