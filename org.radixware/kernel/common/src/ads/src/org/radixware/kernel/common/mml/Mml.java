/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.mml;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlProcessor;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.DocumentationTopicBody;
import org.radixware.schemas.xscml.MmlTagItem;
import org.radixware.schemas.xscml.MmlType;

/**
 *
 * @author dkurlyanov
 */
public class Mml extends Scml {

    public static final class Factory {

        public static final Mml loadFrom(RadixObject context, MmlType xMml, String name) {
            if (xMml != null) {
                Mml mml = new Mml(context, name);
                mml.loadFrom(xMml);
                return mml;
            }
            return null;
        }

        public static final Mml loadFrom(RadixObject context, DocumentationTopicBody xDocumentationTopicBody, String name) {
            return loadFrom(context, xDocumentationTopicBody.getContent(), name);
        }

        public static final Mml newInstance(RadixObject context, String name) {
            return new Mml(context, name);
        }

        public static final Mml newCopy(RadixObject context, Mml source) {
            if (source == null) {
                return null;
            }
            MmlType xMml = MmlType.Factory.newInstance();
            source.appendTo(xMml, AdsDefinition.ESaveMode.NORMAL);
            return loadFrom(context, xMml, null);
        }
    }

    protected Mml(RadixObject context, String name) {
        super(name);
        setContainer(context);
    }

    public void appendTo(MmlType xDef, AdsDefinition.ESaveMode mode) {
        synchronized (this) {
            if (mode == AdsDefinition.ESaveMode.NORMAL) {
                for (Scml.Item item : getItems()) {
                    if (item instanceof Mml.Tag) {
                        Mml.Tag tag = (Mml.Tag) item;
                        tag.appendTo(xDef.addNewItem());
                    } else {
                        Scml.Text text = (Scml.Text) item;
                        xDef.addNewItem().setMarkdown(text.getText());
                    }
                }
            }
        }
    }

    protected void loadFrom(MmlType xMml) {
        if (xMml != null) {
            getItems().clear();
            List<MmlType.Item> items = xMml.getItemList();
            if (items != null && !items.isEmpty()) {

                for (MmlType.Item item : items) {
                    Scml.Item newItem = null;
                    if (item.isSetMarkdown()) {
                        String java = item.getMarkdown();
                        if (java.isEmpty()) {
                            continue;
                        }
                        newItem = Scml.Text.Factory.newInstance(java);
                    } else {
                        newItem = Mml.Tag.Factory.loadFrom(item);
                    }
                    if (newItem != null) {
                        getItems().add(newItem);
                    }
                }
            }

        }
    }

    public boolean equals(Mml obj) {
        return Utils.equals(toString(), obj.toString());
    }

    public void check(IProblemHandler problemHandler, Map<Object, Object> history) {
        Checker c = new Checker(problemHandler);
        c.setHistory(history);
        c.process(this);
    }

    @Override
    public ClipboardSupport<? extends Mml> getClipboardSupport() {
        return new MmlClipboardSupport();
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null) {
            return "Mml";
        } else {
            return name;
        }
    }

    public AdsDefinition getOwnerDef() {
        Definition def = getOwnerDefinition();
        if (def instanceof AdsDefinition) {
            return (AdsDefinition) def;
        } else {
            return null;
        }
    }

    public String getMarkdown(MarkdownGenerationContext context) {
        final StringBuilder sb = new StringBuilder();

        for (Item item : getItems()) {
            if (item instanceof Tag) {
                Tag tag = (Tag) item;
                sb.append(tag.getMarkdown(context));

            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    private class MmlClipboardSupport extends AdsClipboardSupport<Mml> {

        public MmlClipboardSupport() {
            super(Mml.this);
        }

        @Override
        protected XmlObject copyToXml() {
            MmlType xMml = MmlType.Factory.newInstance();
            Mml.this.appendTo(xMml, AdsDefinition.ESaveMode.NORMAL);
            return xMml;
        }

        @Override
        protected Mml loadFrom(XmlObject xmlObject) {
            MmlType xMml = (MmlType) xmlObject;
            Mml mml = Mml.Factory.loadFrom(null, xMml, null);
            return mml;
        }
    }

    // TODO: ???
    public interface IHistory {

        public Map<Object, Object> getHistory();

        public void setHistory(Map<Object, Object> h);
    }

    public static abstract class Tag extends Scml.Tag {

        private String displayString = null;

        public static final class Factory {

            public static final Tag loadFrom(MmlType.Item item) {
                if (item.isSetIdReference()) {
                    return new MmlTagId(item.getIdReference());
                }
                if (item.isSetMarkdownRef()) {
                    return new MmlTagMarkdownRef(item.getMarkdownRef());
                }
                if (item.isSetMarkdownImage()) {
                    return new MmlTagMarkdownImage(item.getMarkdownImage());
                }
                return null;
            }
        }

        protected Tag(MmlTagItem xTag) {
            if (xTag != null) {
                this.displayString = xTag.getPresentation();
            }
        }

        public abstract void appendTo(MmlType.Item item);

        protected void appendTo(MmlTagItem xItem) {
            if (displayString != null) {
                xItem.setPresentation(displayString);
            }
        }

        protected String rememberDisplayName(String displayName) {
            return displayString = displayName;
        }

        protected String restoreDisplayName(String displayName) {
            return displayString == null ? displayName : displayString;
        }

        public abstract void check(IProblemHandler problemHandler, IHistory checker);

        @Override
        public ClipboardSupport<? extends Mml.Tag> getClipboardSupport() {
            return new AdsClipboardSupport<Mml.Tag>(this) {

                @Override
                protected XmlObject copyToXml() {
                    MmlType.Item xDef = MmlType.Item.Factory.newInstance();
                    Mml.Tag.this.appendTo((MmlTagItem) xDef);
                    return xDef;
                }

                @Override
                protected Tag loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof MmlType.Item) {
                        MmlType.Item xDef = (MmlType.Item) xmlObject;
                        if (xDef.isSetMarkdown()) {
                            return super.loadFrom(xmlObject);
                        } else {
                            return Tag.Factory.loadFrom(xDef);
                        }
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }
            };
        }

        protected void error(IProblemHandler problemHandler, String message) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newError(this, message));
            }
        }

        protected void warning(IProblemHandler problemHandler, String message) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newWarning(this, message));
            }
        }

        public Mml getOwnerMml() {
            return (Mml) super.getOwnerScml();
        }

        public abstract String getDisplayName();

        public abstract String getMarkdown(MarkdownGenerationContext context);
    }

    private class Checker extends ScmlProcessor implements IHistory {

        IProblemHandler problemHandler;

        private Checker(IProblemHandler problemHandler) {
            this.problemHandler = problemHandler;
        }

        @Override
        protected void processTagInComment(Scml.Tag tag) {
        }

        @Override
        protected CommentsAnalizer getCommentsAnalizer() {
            return null;
        }

        @Override
        protected void processText(Text text) {
        }

        @Override
        protected void processTag(Scml.Tag tag) {
            if (tag instanceof Mml.Tag) {
                ((Mml.Tag) tag).check(problemHandler, this);
            }
        }
        private Map<Object, Object> history;

        @Override
        public Map<Object, Object> getHistory() {
            return history;
        }

        @Override
        public void setHistory(Map<Object, Object> h) {
            history = h;
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

}
