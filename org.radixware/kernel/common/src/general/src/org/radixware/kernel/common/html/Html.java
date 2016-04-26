/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.html;

import java.io.UnsupportedEncodingException;
import java.util.*;
import org.radixware.kernel.common.html.Html.Visitor.VisitResult;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;


public class Html implements ICssStyledItem {

    private Object userObject;

    public enum TagNames {

        HTML("html"),
        BODY("body"),
        DIV("div"),
        BR("br"),
        B("b"),
        TABLE("table"),
        TR("tr"),
        TD("td"),
        TH("th"),
        INPUT("input"),
        BUTTON("button"),
        IMG("img"),
        UL("ul"),
        LI("li"),
        SL("sl"),
        P("p"),
        SPAN("span");
        private String name;

        private TagNames(String name) {
            this.name = name;
        }

        public static TagNames findTag(String name) {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String match = name.toLowerCase();
            for (TagNames tn : values()) {
                if (tn.name.equals(match)) {
                    return tn;
                }
            }
            return null;
        }
    }

    private class LayotFlow {

        private Map<String, Object> registry = null;

        public void register(Html element, String layoutFunc) {
            if (registry == null) {
                registry = new HashMap< String, Object>();
            }
            Object val = registry.get(layoutFunc);
            if (val == null) {
                registry.put(layoutFunc, element);
            } else {
                if (val instanceof Html) {
                    List list = new LinkedList();
                    list.add(val);
                    list.add(element);
                    registry.put(layoutFunc, list);
                } else if (val instanceof List) {
                    ((List) val).add(element);
                }
            }
        }

        public void appendTo(StringBuilder sb, int indent) {
            if (registry != null && !registry.isEmpty()) {

                for (Map.Entry<String, Object> e : registry.entrySet()) {
                    String funcName = e.getKey();
                    if (e.getValue() instanceof List) {
                        for (Object obj : (List) e.getValue()) {
                            appendElement(sb, (Html) obj, funcName, indent);
                        }
                    } else if (e.getValue() instanceof Html) {
                        appendElement(sb, (Html) e.getValue(), funcName, indent);
                    }

                }
            }
        }

        private void appendElement(StringBuilder sb, Html html, String func, int indent) {
            newLine(sb, indent);
            sb.append("<rwt-layout id=\"").
                    append(html.id).
                    append("\" func=\"").append(func).append("\"/>");
        }
    }

    public static class AttrStore {

        private final String name;
        private String value;
        protected boolean isModified = false;

        public AttrStore(String name) {
            this.name = name;
        }

        public AttrStore(final AttrStore source) {
            name = source.name;
            value = source.value;
            isModified = source.isModified;
        }

        public String getValue() {
            return value;
        }

        public boolean isModified() {
            return isModified;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            if (!Utils.equals(this.value, value)) {
                this.value = value;
                this.isModified = true;
            }
        }
    }

    public static class CSSRule extends AttrStore {

        public static final String WIDTH = "width";
        public static final String MIN_WIDTH = "min-width";
        public static final String HEIGHT = "height";

        public CSSRule(String name, String value) {
            super(name);
            setValue(value);
        }

        public CSSRule(final CSSRule source) {
            super(source);
        }
    }

    public static class Attribute extends AttrStore {

        private static final Attribute NO_ATTRIBUTE = new Attribute("UNDEFINED");

        public Attribute(String name) {
            super(name);
        }

        public Attribute(final Attribute source) {
            super(source);
        }
    }
    private List<Html> children;
    private Map<String, CSSRule> css = null;
    protected final String id;
    private static long ids = 0;
    private final String elementName;
    private Map<String, Attribute> attributes = null;
    private boolean cssModified = false;
    private boolean attrsModifeid = false;
    private String innerText = null;
    private boolean innerTextModified = false;
    private List<String> classes = null;
    private boolean classesModified = false;

    private enum State {

        NEW,
        RENDERED
    }
    private State state = State.NEW;

    public final String getId() {
        return id;
    }

    private static class ChildChange {

        public enum ChangeType {

            ADD,
            REMOVE
        }
        String childId;
        ChangeType type;
        private ChildChange prevChange;

        public ChildChange(String childId, ChangeType type) {
            this.childId = childId;
            this.type = type;
        }

        public void attach(ChildChange prevChange) {
            this.prevChange = prevChange;
        }
    }
    private Map<String, ChildChange> childChanges;

    private static String nextId() {
        if (ids > Long.MAX_VALUE - 100) {
            ids = 1;
        }
        return "wf_" + String.valueOf(ids++);
    }

    public Html(String elementName) {
        this.id = nextId();
        this.elementName = elementName;
    }

    public Html(final Html source) {
        this.id = nextId();
        this.elementName = source.elementName;
        if (source.children != null) {
            for (Html child : source.children) {
                add(child.createCopy());
            }
        }
        if (source.css != null) {
            css = new HashMap<String, CSSRule>();
            for (Map.Entry<String, CSSRule> entry : source.css.entrySet()) {
                css.put(entry.getKey(), new CSSRule(entry.getValue()));
            }
            cssModified = true;
        }
        if (source.attributes != null) {
            attributes = new HashMap<String, Attribute>();
            for (Map.Entry<String, Attribute> entry : source.attributes.entrySet()) {
                attributes.put(entry.getKey(), new Attribute(entry.getValue()));
            }
            attrsModifeid = true;
        }
        if (source.innerText != null) {
            setInnerText(source.innerText);
        }
        if (source.classes != null) {
            for (String clazz : source.classes) {
                addClass(clazz);
            }
        }
    }

    public String getElementName() {
        return elementName;
    }

    public Html getParent() {
        return parent;
    }

    public <T> T getUserObject() {
        return (T) userObject;
    }

    public <T> void setUserObject(T userObject) {
        this.userObject = userObject;
    }

    protected Html createCopy() {
        if (!this.getClass().getName().equals(Html.class.getName())) {
            final String message =
                    String.format("Method 'createCopy' is not defined for '%s' class", this.getClass().getName());
            throw new UnsupportedOperationException(message);
        }
        return new Html(this);
    }

    public void add(Html html) {
        add(-1, html);
    }
    private Html parent;

    public void remove() {
        if (this.parent != null) {
            this.parent.remove(this);
            renew();
        }
    }

    public void add(int index, Html html) {
        if (children == null) {
            children = new LinkedList<>();
        }
        if (index < 0 || index >= children.size()) {
            children.add(html);
        } else {
            children.add(index, html);
        }
        html.parent = this;
        childAdded(html);
    }

    public void remove(Html html) {
        if (children == null) {
            return;
        }
        if (children.remove(html)) {
            html.parent = null;
            childRemoved(html);
        }
    }

    private void childAdded(Html html) {
        if (childChanges == null) {
            childChanges = new HashMap<>();
        }
        ChildChange change = childChanges.get(html.id);
        if (change != null && change.type == ChildChange.ChangeType.REMOVE) {//was removed and added again
            ChildChange addition = new ChildChange(html.id, ChildChange.ChangeType.ADD);
            addition.attach(change);
            childChanges.put(html.id, addition);
        } else {
            childChanges.put(html.id, new ChildChange(html.id, ChildChange.ChangeType.ADD));
        }
    }

    private void childRemoved(Html html) {
        if (childChanges == null) {
            childChanges = new HashMap<>();
        }
        childChanges.put(html.id, new ChildChange(html.id, ChildChange.ChangeType.REMOVE));
    }

    public List<Html> children() {
        if (children == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(children);
        }
    }

    public int childCount() {
        return children == null ? 0 : children.size();
    }

    public Html getChildAt(int index) {
        if (children == null) {
            throw new IndexOutOfBoundsException("children == null");
        } else {
            return children.get(index);
        }
    }

    public int indexOfChild(Html html) {
        return children == null ? -1 : children.indexOf(html);
    }

    public void clear() {
        if (children != null) {
            while (!children.isEmpty()) {
                children.get(0).remove();
            }
            children = null;
        }
    }

    public void setCss(String name, boolean value) {
        setCss(name, String.valueOf(value));
    }

    public void setCss(String name, int value) {
        setCss(name, String.valueOf(value));
    }

    @Override
    public void resetCss() {
        if (css != null) {
            List<String> keys = new ArrayList<>(css.keySet());
            for (String key : keys) {
                setCss(key, null);
            }
        }
    }

    public void resetAttrs() {
        if (attributes != null) {
            List<String> keys = new ArrayList<>(attributes.keySet());
            for (String key : keys) {
                setAttr(key, null);
            }
        }
    }

    @Override
    public void setCss(String name, String value) {

        if (value == null) {
            if (css != null) {
                CSSRule rule = css.get(name);
                if (rule != null) {
                    rule.setValue(null);
                }
                cssModified = true;
            }
        } else {
            if (css == null) {
                css = new HashMap<>();
            }
            CSSRule rule = css.get(name);
            if (rule == null) {
                rule = new CSSRule(name, value);
                css.put(name, rule);
                cssModified = true;
            } else {
                rule.setValue(value);
                if (rule.isModified) {
                    cssModified = true;
                }
            }
        }
    }

    @Override
    public String getCss(String name) {
        if (css == null) {
            return null;
        }
        CSSRule rule = css.get(name);
        if (rule == null) {
            return null;
        }
        return rule.getValue();
    }
    private String scheduleLauout = null;

    public void layout(String type) {
        scheduleLauout = type;
        sentLayout = null;
    }

    public void setFocusSencitive(boolean sence) {
        if (sence) {
            this.setAttr("onfocus", "$RWT.events.focusedEvent");
            this.setAttr("onblur", "$RWT.events.defocusedEvent");
        } else {
            this.setAttr("onfocus", null);
            this.setAttr("onblur", null);
        }
    }

    public boolean isModified() {
        return state == State.NEW || cssModified || classesModified || attrsModifeid || innerTextModified || scheduleLauout != null || (childChanges != null && !childChanges.isEmpty());
    }

    @Override
    public String toString() {
        return toString(true, true, 0, true, new LayotFlow() {
            @Override
            public void register(Html element, String layoutFunc) {
            }
        });
    }

    public String toString(boolean closeEmptyTags) {
        return toString(true, true, 0, true, closeEmptyTags, new LayotFlow() {
            @Override
            public void register(Html element, String layoutFunc) {
            }
        });
    }

    public String toString(boolean prettyPrint, boolean printId, int indent, boolean isRoot, LayotFlow layoutFlow) {
        return toString(prettyPrint, printId, indent, isRoot, false, layoutFlow);
    }

    public String toString(boolean prettyPrint, boolean printId, int indent, boolean isRoot, boolean closeEmptyTags, LayotFlow layoutFlow) {
        StringBuilder sb = new StringBuilder();
        appendTo(sb, prettyPrint, printId, indent, isRoot, closeEmptyTags, layoutFlow);
        return sb.toString();
    }

    private void newLine(StringBuilder sb, int indent) {
        sb.append('\n');
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
    }

    public void saveChanges(final IHtmlContext context, final StringBuilder sb) {
        saveChanges(context, sb, false);
    }

    public void saveChanges(final IHtmlContext context, final StringBuilder sb, boolean closeEmptyTags) {
        final Html rootObj = this;
        final IHtmlUser exp = context.getExplicitChild();
        final Html explicit = exp == null ? null : exp.getHtml();

        this.visit(new Visitor() {
            @Override
            public VisitResult accept(Html html) {
                return html.doSaveChanges(rootObj == html, explicit, sb);
            }
        });
    }
    private String sentLayout = null;

    public void renew() {
        if (state != State.NEW) {
            visit(new Visitor() {
                @Override
                public VisitResult accept(Html html) {
                    if (html.state != State.NEW) {
                        html.state = State.NEW;
                        if (html.sentLayout != null) {
                            html.scheduleLauout = html.sentLayout;
                            html.sentLayout = null;
                        }
                    }
                    return VisitResult.DEFAULT;
                }
            });
        }
    }

    private Visitor.VisitResult doSaveChanges(boolean isRoot, Html explicit, StringBuilder sb) {
        if (!isRoot && explicit != null && explicit != this) {
            return Visitor.VisitResult.SKIP_CHILDREN;
        }
        switch (state) {
            case NEW:

                newLine(sb, 0);

                sb.append("<update-element id=\"").append(id).append("\" ");
                if (scheduleLauout != null) {
                    sb.append("layout=\"").append(scheduleLauout).append("\" ");
                }
                sb.append("type=\"init\">");
                LayotFlow layotFlow = new LayotFlow();
                this.appendTo(sb, true, true, 4, true, false, layotFlow);
                layotFlow.appendTo(sb, 4);
                sb.append("\n</update-element>");
                return Visitor.VisitResult.SKIP_CHILDREN;
            default:
                if (isModified()) {
                    newLine(sb, 0);
                    sb.append("<update-element id=\"").append(id).append("\" ");
                    if (scheduleLauout != null) {
                        sb.append("layout=\"").append(scheduleLauout).append("\" ");
                    }
                    sb.append("type=\"update\">");
                    this.appendChangesTo(sb, true, 4);
                    sb.append("\n</update-element>");
                }
                return Visitor.VisitResult.DEFAULT;
        }
    }

    private Html findChildById(String id) {
        if (children == null) {
            return null;
        }
        for (Html html : children) {
            if (html.id.equals(id)) {
                return html;
            }
        }
        return null;
    }

    private void appendChange(ChildChange c, boolean prettyPrint, int indent, StringBuilder sb) {
        if (c.prevChange != null) {
            appendChange(c.prevChange, prettyPrint, indent, sb);
        }

        if (c.type == ChildChange.ChangeType.ADD) {
            Html child = findChildById(c.childId);
            if (child != null) {
                if (prettyPrint) {
                    newLine(sb, indent);
                }
                sb.append("<child-add id=\"").append(c.childId).append("\" flow=\"").append(String.valueOf(children.indexOf(child))).append("\"/>");
            }
        } else if (c.type == ChildChange.ChangeType.REMOVE) {
            if (prettyPrint) {
                newLine(sb, indent);
            }
            sb.append("<child-remove id=\"").append(c.childId).append("\"/>");
        }
    }

    private void appendChangesTo(StringBuilder sb, boolean prettyPrint, int indent) {
        if (childChanges != null) {
            for (ChildChange c : childChanges.values()) {
                appendChange(c, prettyPrint, indent, sb);
            }
        }
        if (cssModified) {
            if (prettyPrint) {
                newLine(sb, indent);
            }
            for (CSSRule rule : this.css.values()) {
                if (rule.isModified()) {
                    if (rule.getValue() == null) {
                        sb.append("<style-change type=\"remove\" name=\"").append(rule.getName()).append("\"/>");
                    } else {
                        sb.append("<style-change type=\"modify\" name=\"").append(rule.getName()).append("\" value=\"").append(rule.getValue()).append("\"/>");
                    }
                }
            }
        }
        if (attrsModifeid) {
            for (String name : attributes.keySet()) {
                Attribute a = attributes.get(name);
                if (a == Attribute.NO_ATTRIBUTE) {
                    sb.append("<attr-remove name=\"").append(name).append("\"/>");
                } else {
                    if (a.isModified) {
                        if (prettyPrint) {
                            newLine(sb, indent);
                        }
                        final String value = a.getValue();
                        if (isXmlCompatibleString(value)) {
                            sb.append("<attr-change name=\"").append(name).append("\" value=\"").append(Html.string2HtmlString(value)).append("\"/>");
                        } else {
                            try {
                                final String encodedValue = Base64.encode(value.getBytes(FileUtils.XML_ENCODING));
                                sb.append("<attr-change-base64 name=\"").append(name).append("\" value=\"").append(encodedValue).append("\"/>");
                            } catch (UnsupportedEncodingException exception) {
                                throw new UnsupportedOperationException("Can't write attribute value", exception);
                            }
                        }
                    }
                }
            }
        }
        if (classesModified) {
            if (prettyPrint) {
                newLine(sb, indent);
            }
            sb.append("<class-change");
            appendClassesAttr(sb);
            sb.append("/>");
        }
        if (innerTextModified) {
            if (prettyPrint) {
                newLine(sb, indent);
            }
            if (innerText == null || innerText.isEmpty()) {
                sb.append("<text-change/>");
            } else {
                sb.append("<text-change>").append(Html.string2HtmlString(innerText)).append("</text-change>");
            }
        }
    }

    public interface Visitor {

        public enum VisitResult {

            DEFAULT,
            SKIP_CHILDREN;
        }

        public VisitResult accept(Html html);
    }

    public void visit(Visitor visitor) {
        switch (visitor.accept(this)) {
            case DEFAULT:
                if (children != null) {
                    for (Html child : new ArrayList<>(children)) {
                        child.visit(visitor);
                    }
                }
        }
    }

    private void appendTo(StringBuilder sb, boolean prettyPrint, boolean printId, int indent, boolean isRoot, boolean closeEmptyTags, LayotFlow layoutFlow) {
        if (prettyPrint) {
            newLine(sb, indent);
        }
        sb.append('<').append(elementName);
        if (printId) {
            sb.append(" id=\"").append(id).append("\"");
        }
        if (isRoot) {
            sb.append(" xmlns=\"").append("http://www.w3.org/1999/xhtml").append("\"");
        }

        if (attributes != null) {
            String value;
            for (Attribute a : attributes.values()) {
                value = a.getValue();
                if (value != null) {
                    sb.append(' ');
                    if (isXmlCompatibleString(value)) {
                        sb.append(a.getName()).append("=\"").append(Html.string2HtmlString(value));
                    } else {
                        sb.append(a.getName()).append("__in_base64__").append("=\"");
                        try {
                            sb.append(Base64.encode(value.getBytes(FileUtils.XML_ENCODING)));
                        } catch (UnsupportedEncodingException exception) {
                            throw new UnsupportedOperationException("Can't write attribute value", exception);
                        }
                    }
                    sb.append('"');
                }
            }
        }
        appendClassesAttr(sb);

        if (this.scheduleLauout != null) {
            layoutFlow.register(this, this.scheduleLauout);
            //sb.append(' ').append("rwt_layout=\"").append(this.scheduleLauout).append('"');
        }

        String style = getStyle();
        if (style != null) {
            sb.append(" style=\"").append(style).append('"');
        }

        int childIndent = indent + 4;
        if (children == null || children.isEmpty()) {
            if (innerText != null && !innerText.isEmpty()) {
                sb.append(">").append(Html.string2HtmlString(innerText)).append("</").append(elementName).append(">");
            } else {
                if (closeEmptyTags) {
                    sb.append("</").append(elementName).append(">");
                } else {
                    if ("meta".equals(elementName.toLowerCase())) {
                        sb.append(">");
                    } else {
                        sb.append("/>");
                    }
                }
            }
        } else {
            sb.append(">");
            for (Html html : children) {
                html.appendTo(sb, prettyPrint, printId, childIndent, false, closeEmptyTags, layoutFlow);
            }
            if (prettyPrint) {
                newLine(sb, indent);
            }
            sb.append("</").append(elementName).append(">");
        }
    }

    public void rendered(final IHtmlContext context) {
        final Html rootHtml = context.getHtml();
        final Html explicit = context == null || context.getExplicitChild() == null ? null : context.getExplicitChild().getHtml();
        visit(new Visitor() {
            @Override
            public Visitor.VisitResult accept(Html html) {
                boolean isRoot = html == rootHtml;
                if (!isRoot && explicit != null && explicit != html) {
                    return Visitor.VisitResult.SKIP_CHILDREN;
                }

                html.state = State.RENDERED;
                if (html.childChanges != null) {
                    html.childChanges.clear();
                }
                html.attrsModifeid = false;
                html.cssModified = false;
                if (html.attributes != null) {
                    List<String> removeKeys = new LinkedList<>();
                    for (Map.Entry<String, Attribute> e : html.attributes.entrySet()) {
                        Attribute a = e.getValue();
                        if (a == Attribute.NO_ATTRIBUTE) {
                            removeKeys.add(e.getKey());
                        } else {
                            a.isModified = false;
                        }
                    }
                    for (String n : removeKeys) {
                        html.attributes.remove(n);
                    }
                }
                if (html.css != null) {
                    List<String> toRemove = new LinkedList<String>();
                    for (CSSRule rule : html.css.values()) {
                        if (rule.getValue() == null) {
                            toRemove.add(rule.getName());
                        }
                        rule.isModified = false;
                    }
                    for (String s : toRemove) {
                        css.remove(s);
                    }
                }
                html.innerTextModified = false;
                if (html.scheduleLauout != null) {
                    html.sentLayout = html.scheduleLauout;
                    html.scheduleLauout = null;
                }
                html.classesModified = false;
                return Visitor.VisitResult.DEFAULT;
            }
        });
    }

    @Override
    public String getStyle() {
        if (css != null) {
            StringBuilder cssString = new StringBuilder();
            for (CSSRule rule : css.values()) {
                if (rule.getValue() != null) {
                    cssString.append(rule.getName()).append(':').append(rule.getValue()).append(';');
                }
            }
            return cssString.toString();
        } else {
            return null;
        }
    }

    public String getAttr(String name) {
        if (attributes == null) {
            return null;
        }
        Attribute a = attributes.get(name);
        if (a == null || a == Attribute.NO_ATTRIBUTE) {
            return null;
        }
        return a.getValue();
    }

    public boolean getBooleanAttr(String name) {
        String val = getAttr(name);
        if (val != null) {
            return Boolean.parseBoolean(val);
        } else {
            return false;
        }
    }

    public boolean getBooleanAttr(String name, boolean defaultVal) {
        String val = getAttr(name);
        if (val != null) {
            return Boolean.parseBoolean(val);
        } else {
            return defaultVal;
        }
    }

    public int getIntegerAttr(String name, int defaultVal) {
        String val = getAttr(name);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return defaultVal;
            }
        } else {
            return defaultVal;
        }
    }

    public void setAttr(String name, boolean value) {
        setAttr(name, String.valueOf(value));
    }

    public void setAttr(String name, float value) {
        setAttr(name, String.valueOf(value));
    }

    public void setAttr(String name, String value) {
        if (value != null) {
            if (attributes == null) {
                attributes = new HashMap<String, Attribute>();
            }
            Attribute a = attributes.get(name);
            if (a == null || a == Attribute.NO_ATTRIBUTE) {
                a = new Attribute(name);
                attributes.put(name, a);
            }

            a.setValue(value);
            if (a.isModified()) {
                attrsModifeid = true;
            }
        } else {
            if (attributes != null) {
                Attribute a = attributes.get(name);
                if (a != null && a != Attribute.NO_ATTRIBUTE) {
                    attributes.put(name, Attribute.NO_ATTRIBUTE);
                    attrsModifeid = true;
                }
            }
        }
    }

    public void setAttr(String name, int value) {
        setAttr(name, String.valueOf(value));
    }

    public String getInnerText() {
        return innerText;
    }

    public void setInnerText(String text) {
        if (!Utils.equals(text, innerText)) {
            innerText = text;
            innerTextModified = true;
        }
    }

    @Override
    public void addClass(String clazz) {
        if (classes == null) {
            classes = new LinkedList<String>();
        }
        if (!classes.contains(clazz)) {
            classes.add(clazz);
            classesModified = true;
        }
    }

    @Override
    public boolean containsClass(String clazz) {
        if (classes != null) {
            return classes.contains(clazz);
        } else {
            return false;
        }
    }

    @Override
    public void removeClass(String clazz) {
        if (classes != null && classes.remove(clazz)) {
            classesModified = true;
        }
    }

    @Override
    public List<String> getClasses() {
        if (classes == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(classes);
        }
    }

    private void appendClassesAttr(StringBuilder sb) {
        if (classes != null && !classes.isEmpty()) {
            sb.append(" class=\"");
            boolean first = true;
            for (String s : classes) {
                if (first) {
                    first = false;
                } else {
                    sb.append(' ');
                }
                sb.append(s);
            }
            sb.append("\"");
        }
    }

    private static boolean isXmlCompatibleString(final String s) {
        return s.matches("[\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD]*");
    }

    public static String string2HtmlString(final String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static String htmlString2String(final String s) {
        return s.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&amp;", "&");
    }

    public void markAsChoosable() {
        addClass("rwt-ui-choosable-pointed");
        
    }

    public void removeChoosableMarker() {
        removeClass("rwt-ui-choosable-pointed");
    }

    public void setContextMenuEnabled(final boolean enabled) {
        setAttr("oncontextmenu", enabled ? null : "return false;");
    }
}
