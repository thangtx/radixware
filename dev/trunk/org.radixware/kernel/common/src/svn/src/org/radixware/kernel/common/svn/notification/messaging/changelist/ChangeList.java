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

package org.radixware.kernel.common.svn.notification.messaging.changelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.notification.messaging.changelist.ChangeList.Revision.FileChange;
import org.radixware.schemas.product.Change;
import org.radixware.schemas.product.ChangeDocument;


public class ChangeList {

    protected Map<EIsoLanguage, String> comments = new HashMap<EIsoLanguage, String>();
    protected List<Revision> revisions = new LinkedList<Revision>();

    public static class Revision {

        public static class FileChange {

            private final char fileChange;
            private final String filePath;
            protected String definitionName = null;

            public FileChange(char fileChange, String filePath) {
                this.fileChange = fileChange;
                this.filePath = filePath;
            }

            public String getDefinitionName() {
                return definitionName;
            }

            public char getFileChange() {
                return fileChange;
            }

            public String getFilePath() {
                return filePath;
            }
        }
        protected String comment;
        protected String author;
        protected final long number;
        protected final Date created;
        protected final List<FileChange> fileChanges = new LinkedList<FileChange>();

        public Revision(long number, Date date) {
            this.number = number;
            this.created = date;
        }

        public List<FileChange> listFiles() {
            return new ArrayList<Revision.FileChange>(fileChanges);
        }

        public String getAuthor() {
            return author;
        }

        public String getComment() {
            return comment;
        }

        public Date getCreated() {
            return created;
        }

        public List<FileChange> getFileChanges() {
            return new ArrayList<FileChange>(fileChanges);
        }

        public long getNumber() {
            return number;
        }

        protected FileChange createFileChange(char fileChange, String filePath) {
            return new FileChange(fileChange, filePath);
        }
    }

    public Revision getRevision(long number, Date date) {
        for (Revision r : revisions) {
            if (r.number == number) {
                return r;
            }
        }
        Revision revision = createRevision(number, date);
        revisions.add(revision);
        return revision;
    }

    protected Revision createRevision(long number, Date date) {
        return new Revision(number, date);
    }

    public String getComment(EIsoLanguage language) {
        String c = comments.get(language);
        return c == null ? "" : c;
    }

    public boolean hasAnyComments() {
        for (EIsoLanguage l : comments.keySet()) {
            String c = comments.get(l);
            if (c != null && !c.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyFileChanges() {
        return revisions != null && !revisions.isEmpty();
    }

    public Set<EIsoLanguage> getLanguagesList() {
        return Collections.unmodifiableSet(comments.keySet());
    }

    public void setComment(EIsoLanguage language, String comment) {
        comments.put(language, comment);
    }

    public static final ChangeList loadFromXml(ChangeDocument xDoc) {
        ChangeList instance = new ChangeList();
        instance.loadFrom(xDoc);
        return instance;
    }

    public String getFileChangesHtml() {
        List<Revision> arr = new ArrayList<Revision>(revisions);
        Collections.sort(arr, new Comparator<Revision>() {

            @Override
            public int compare(Revision o1, Revision o2) {
                return o1.number == o2.number ? 0 : (o1.number > o2.number ? -1 : 1);
            }
        });
        if (arr.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<header>");
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        builder.append("</header>");
        builder.append("<body>");

        for (Revision r : arr) {
            builder.append("<p>");
            builder.append(r.number);
            builder.append("&nbsp;");
            if (r.comment != null) {
                builder.append("<b>");
                builder.append(r.comment.replace("<", "&lt;").replace(">", "&gt;"));
                builder.append("</b>");
                builder.append("&nbsp;");
            }
            builder.append(r.created.toString());
            builder.append("</p>");

            for (FileChange f : r.fileChanges) {
                builder.append("<br>&nbsp;&nbsp;&nbsp;");
                builder.append("<font color=");
                switch (f.fileChange) {
                    case 'A':
                        builder.append("\"green\"");
                        break;
                    case 'M':
                        builder.append("\"blue\"");
                        break;
                    case 'D':
                        builder.append("\"red\"");
                        break;
                }
                builder.append(">&nbsp;");
                builder.append(f.fileChange);
                builder.append("&nbsp;");
                if (f.getDefinitionName() != null) {
                    builder.append(f.getDefinitionName().replace("<", "&lt;").replace(">", "&gt;"));
                    builder.append("&nbsp;<font color=\"gray\" >(");
                    builder.append(f.filePath.replace("<", "&lt;").replace(">", "&gt;"));
                    builder.append(")</font>");
                } else {
                    builder.append(f.filePath.replace("<", "&lt;").replace(">", "&gt;"));
                }
                builder.append("</font>");

            }
        }
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

    private static class CommitModules {

        List<String> modules = new LinkedList<String>();
        List<String> comments;
        final String names;

        public CommitModules(String names) {
            this.names = names;
        }

        static void addRevision(Map<String, CommitModules> allModules, String layerUri, Revision r) {
            if (r.comment == null || r.comment.isEmpty()) {
                return;
            }

            List<FileChange> fileChanges = r.getFileChanges();

            List<String> pathes = new LinkedList<String>();
            for (FileChange c : fileChanges) {
                String modulePath = normalizePath(c.filePath, layerUri);
                if (!pathes.contains(modulePath)) {
                    pathes.add(modulePath);
                }
            }
            Collections.sort(pathes);
            String pathName = mergePathes(pathes);
            CommitModules existing = allModules.get(pathName);
            if (existing == null) {
                existing = new CommitModules(pathName);
                allModules.put(pathName, existing);
            }
            if (existing.comments == null || !existing.comments.contains(r.comment)) {
                if (existing.comments == null) {
                    existing.comments = new LinkedList<String>();
                }
                existing.comments.add(r.comment);
            }
        }

        static String mergePathes(List<String> pathes) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String s : pathes) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(s);
            }
            return builder.toString();
        }

        private static String normalizePath(String path, String layerUri) {
            String normalPath = path.replace("\\", "/");


            int layerUriIdx = normalPath.indexOf(layerUri);

            if (layerUriIdx < 0) {
                return normalPath;
            }

            String subLayerPath = normalPath.substring(layerUriIdx + layerUri.length());

            String[] components = subLayerPath.split("/");
            List<String> list = new LinkedList<String>();
            for (String c : components) {
                if (!c.isEmpty()) {
                    list.add(c);
                }
            }
            components = list.toArray(new String[list.size()]);

            if (components.length > 0) {//has segment
                components[0] = NameUtil.upperCaseFirstLetter(components[0].toLowerCase());
//            
            }
            if (components.length > 1) {//module name
                if ("Kernel".equals(components[0])) {
                    String[] names = components[1].split("\\.");
                    for (int i = 0; i < names.length; i++) {
                        names[i] = NameUtil.upperCaseFirstLetter(names[i].toLowerCase());
                    }
                    components[1] = mergePath(names, -1, '.');
                }
            }
            if (components.length > 2) {

                return mergePath(components, 2, '.');
            }

            return mergePath(components, -1, '.');

        }

        private static String mergePath(String[] elements, int count, char sep) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int i = 0, len = count < 0 ? elements.length : count; i < len; i++) {
                String e = elements[i];
                if (first) {
                    first = false;
                } else {
                    sb.append(sep);
                }
                sb.append(e);
            }
            return sb.toString();
        }

        static String generateComment(Map<String, CommitModules> allModules) {
            StringBuilder sb = new StringBuilder();
            List<String> allKeys = new ArrayList<String>(allModules.keySet());
            Collections.sort(allKeys, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    String c1, c2;
                    int comma = o1.indexOf(",");
                    if (comma > 0) {
                        c1 = o1.substring(0, comma);
                    } else {
                        c1 = o1;
                    }
                    comma = o2.indexOf(",");
                    if (comma > 0) {
                        c2 = o2.substring(0, comma);
                    } else {
                        c2 = o2;
                    }
                    int result = c1.compareTo(c2);
                    if (result == 0) {
                        return o1.compareTo(o2);
                    } else {
                        return result;
                    }

                }
            });
            for (String key : allKeys) {
                CommitModules modules = allModules.get(key);
                if (modules.comments != null) {
                    sb.append(modules.names);
                    sb.append("\n");
                    for (String comment : modules.comments) {
                        String[] commentLines = comment.split("\n");
                        for (String line : commentLines) {
                            sb.append("    ");
                            if (!line.startsWith("[")) {
                                sb.append("[ ] ");
                            } else {
                                int index = line.indexOf("]");
                                if (index > 0 && index + 1 < line.length()) {
                                    char c = line.charAt(index + 1);
                                    if (c != ' ') {
                                        line = line.substring(0, index+1) + " " + line.substring(index + 1);
                                    }
                                }
                            }
                            sb.append(line);
                            sb.append("\n");
                        }
                    }
                }
            }
            return sb.toString();
        }
    }

//    private static class CommentDir {
//
//        private List<CommentDir> children;
//        private List<String> comments;
//        private final String name;
//        private final String layerUri;
//        private final CommentDir parent;
//
//        public CommentDir(CommentDir parent, String name, String layerUri) {
//            this.name = name;
//            this.parent = parent;
//            this.layerUri = layerUri;
//        }
//
//        private String normalizePath(String path) {
//            String normalPath = path.replace("\\", "/");
//
//
//            int layerUriIdx = normalPath.indexOf(layerUri);
//
//            if (layerUriIdx < 0) {
//                return normalPath;
//            }
//
//            String subLayerPath = normalPath.substring(layerUriIdx + layerUri.length());
//
//            String[] components = subLayerPath.split("/");
//            List<String> list = new LinkedList<String>();
//            for (String c : components) {
//                if (!c.isEmpty()) {
//                    list.add(c);
//                }
//            }
//            components = list.toArray(new String[list.size()]);
//            //    ERepositorySegmentType st = null;
//            if (components.length > 0) {//has segment
//                //try {
//                //st = ERepositorySegmentType.getForValue(components[0]);
//                components[0] = NameUtil.upperCaseFirstLetter(components[0].toLowerCase());
////                } catch (NoConstItemWithSuchValueError e) {
////                    return subLayerPath;
////                }
//            }
//            if (components.length > 1) {//environment
//                components[1] = NameUtil.upperCaseFirstLetter(components[1].toLowerCase());
//            }
//            if (components.length > 2) {
////                if (st == ERepositorySegmentType.KERNEL) {
//                return mergePath(components, 2);
//            }
////                } else {
////                    return mergePath(components, 3);
////                }
////            }
//            return mergePath(components, -1);
//
//        }
//
//        private String mergePath(String[] elements, int count) {
//            StringBuilder sb = new StringBuilder();
//            boolean first = true;
//            for (int i = 0, len = count < 0 ? elements.length : count; i < len; i++) {
//                String e = elements[i];
//                if (first) {
//                    first = false;
//                } else {
//                    sb.append("/");
//                }
//                sb.append(e);
//            }
//            return sb.toString();
//        }
//
//        void addChild(Revision r) {
//            if (r.comment == null || r.comment.isEmpty()) {
//                return;
//            }
//            if (comments != null && comments.contains(r.comment)) {
//                return;
//            }
//            List<FileChange> fileChanges = r.getFileChanges();
//            List<List<CommentDir>> pathes = new LinkedList<List<CommentDir>>();
//            for (FileChange c : fileChanges) {
//                List<CommentDir> pp = new LinkedList<CommentDir>();
//                String path = normalizePath(c.filePath);
//                String[] components = path.split("/");
//                CommentDir parent = this;
//
//                for (int i = 0; i < components.length; i++) {
//                    pp.add(parent);
//                    String childName = components[i];
//                    if (childName.isEmpty()) {
//                        continue;
//                    }
//                    CommentDir child = parent.getChild(childName);
//                    if (child == null) {
//                        child = parent.addChild(childName);
//                    }
//                    parent = child;
//                }
//                pathes.add(pp);
//            }
//
//            CommentDir mostCommonParent = this;
//
//            for (int lookupElement = 0;; lookupElement++) {
//                CommentDir parent = null;
//                for (List<CommentDir> path : pathes) {
//                    if (path.size() <= lookupElement) {
//                        break;
//                    }
//                    if (parent == null) {
//                        parent = path.get(lookupElement);
//                    } else {
//                        CommentDir parentAtPoint = path.get(lookupElement);
//                        if (parentAtPoint != parent) {
//                            parent = null;
//                            break;
//                        }
//                    }
//                }
//                if (parent != null) {
//                    mostCommonParent = parent;
//                } else {
//                    break;
//                }
//            }
//            mostCommonParent.addComment(r.comment);
//        }
//
//        String getPath() {
//            StringBuilder sb = new StringBuilder();
//            CommentDir cd = this;
//            while (cd != null && cd.parent != null) {
//                sb.insert(0, cd.name);
//                sb.insert(0, "/");
//                cd = cd.parent;
//            }
//            return sb.toString();
//        }
//
//        CommentDir addChild(String name) {
//            CommentDir child = new CommentDir(this, name, layerUri);
//            if (children == null) {
//                children = new LinkedList<CommentDir>();
//            }
//            children.add(child);
//            return child;
//        }
//
//        CommentDir getChild(String name) {
//            if (children == null) {
//                return null;
//            } else {
//                for (CommentDir cd : children) {
//                    if (cd.name.equals(name)) {
//                        return cd;
//                    }
//                }
//            }
//            return null;
//        }
//
//        String generateComment(String layerURI) {
//            StringBuilder sb = new StringBuilder();
//            addComment(sb, 0, layerURI);
//            return sb.toString();
//        }
//
//        void appendOffset(StringBuilder sb, int level) {
//            for (int i = 0; i < level; i++) {
//                sb.append(' ');
//            }
//        }
//
//        void addComment(StringBuilder builder, int level, String layerURI) {
//            if (level != 0 && comments != null && !comments.isEmpty()) {
//                //appendOffset(builder, level);
//                String path = getPath();
//
//                int indexOfLayer = path.indexOf(layerURI);
//                builder.append('\n');
//                if (indexOfLayer > 0) {
//                    builder.append(path.substring(indexOfLayer));
//                } else {
//                    builder.append(path);
//                }
//                builder.append('\n');
//
//                for (String comment : comments) {
//                    String[] commentLines = comment.split("\n");
//                    for (String line : commentLines) {
//                        appendOffset(builder, 4);
//                        if (!line.startsWith("[")) {
//                            builder.append("[ ] ");
//                        }
//                        builder.append(line);
//                        builder.append("\n");
//                    }
//                }
//
//            }
//            if (children != null) {
//                List<CommentDir> sortedChildren = new LinkedList<CommentDir>(children);
//                Collections.sort(children, new Comparator<CommentDir>() {
//
//                    @Override
//                    public int compare(CommentDir o1, CommentDir o2) {
//                        return o2.name.compareTo(o1.name);
//                    }
//                });
//                for (CommentDir cd : sortedChildren) {
//                    cd.addComment(builder, level + 1, layerURI);
//                }
//            }
//
//        }
//
//        void addComment(String comment) {
//            if (comments == null) {
//                comments = new LinkedList<String>();
//            }
//            if (!comments.contains(comment)) {
//                comments.add(comment);
//            }
//        }
//    }
    /**
     * returns map&lt;layerUrl,layerComment&gt;
     */
    public static Map<Layer, String> getMessageTemplate(EIsoLanguage lang, Map<Layer, ChangeList> editablePartComments, Map<Layer, ChangeList> readonlyPartComments) {
        Map<Layer, String> result = new HashMap<Layer, String>();
        if (editablePartComments != null) {
            for (Map.Entry<Layer, ChangeList> e : editablePartComments.entrySet()) {
                // StringBuilder builder = new StringBuilder();
                List<Revision> arr = new ArrayList<Revision>(e.getValue().revisions);
                Collections.sort(arr, new Comparator<Revision>() {

                    @Override
                    public int compare(Revision o1, Revision o2) {
                        return o1.number == o2.number ? 0 : (o1.number > o2.number ? -1 : 1);
                    }
                });
                //CommentDir root = new CommentDir(null, "root", e.getKey().getURI());
                Map<String, CommitModules> allComments = new HashMap<String, CommitModules>();
                // builder.append(e.getKey() + " changes:\n");
                //  Set<String> comments = new HashSet<String>();
                final String layerUri = e.getKey().getURI();
                for (Revision r : arr) {
                    CommitModules.addRevision(allComments, layerUri, r);

                    //root.addChild(r);
//                    if (r.comment != null && !r.comment.isEmpty()) {
//                        if (comments.contains(r.comment)) {
//                            continue;
//                        }
//                        if (!r.comment.startsWith("[")) {
//                            builder.append("[ ] ");
//                        }
//                        builder.append(r.comment);
//                        builder.append("\n");
//                        comments.add(r.comment);
//                    }
                }
                // System.out.println();
                //result.put(e.getKey(), root.generateComment(e.getKey().getURI()));
                result.put(e.getKey(), CommitModules.generateComment(allComments));
            }
        }
        if (readonlyPartComments != null) {
            for (Map.Entry<Layer, ChangeList> e : readonlyPartComments.entrySet()) {
                StringBuilder builder = new StringBuilder();
                ChangeList rpc = e.getValue();
                if (rpc != null) {
                    String comment = rpc.getComment(lang);
                    boolean noComment = false;
                    if (comment == null || comment.isEmpty()) {
                        noComment = true;
                        for (EIsoLanguage l : rpc.getLanguagesList()) {
                            comment = rpc.getComment(l);
                            if (comment != null && !comment.isEmpty()) {
                                noComment = false;
                                break;
                            }
                        }
                    }
                    if (!noComment) {
                        if (comment != null && !comment.isEmpty()) {
                            //    builder.append(e.getKey() + " changes:");
                            builder.append("\n");
                            builder.append(comment);
                        }
                    }
                }
                result.put(e.getKey(), builder.toString());
            }
        }
        return result;
    }

    public void loadFrom(ChangeDocument xDoc) {
        Change xDef = xDoc.getChange();
        if (xDef != null) {
            if (xDef.getComments() != null) {
                for (Change.Comments.Comment xC : xDef.getComments().getCommentList()) {
                    try {
                        this.comments.put(EIsoLanguage.getForValue(xC.getLanguage()), xC.getStringValue());
                    } catch (NoConstItemWithSuchValueError e) {
                        //ignore
                    }
                }
            }
//            if (xDef.getRevisions() != null && xDef.getRevisions().getRevisionList() != null) {
//                for (Change.Revisions.Revision xR : xDef.getRevisions().getRevisionList()) {
//                    if (xR.getNumber() == 0 || xR.getCreated() == null) {
//                        continue;
//                    }
//                    Revision r = this.getRevision(xR.getNumber(), xR.getCreated().getTime());
//                    r.comment = xR.getComment();
//                    r.author = xR.getAuthor();
//                    xR.getCreated().getTimeInMillis();
//                    if (xR.getFiles() != null) {
//                        if (xR.getFiles().getModifyList() != null) {
//                            for (Change.Revisions.Revision.Files.Modify m : xR.getFiles().getModifyList()) {
//                                FileChange c = r.createFileChange('M', m.getFileName());
//                                c.definitionName = m.getDefinitionName();
//                                r.fileChanges.add(c);
//                            }
//                        }
//                        if (xR.getFiles().getAddList() != null) {
//                            for (Change.Revisions.Revision.Files.Add m : xR.getFiles().getAddList()) {
//                                FileChange c = r.createFileChange('A', m.getFileName());
//                                c.definitionName = m.getDefinitionName();
//                                r.fileChanges.add(c);
//                            }
//                        }
//                        if (xR.getFiles().getRemoveList() != null) {
//                            for (Change.Revisions.Revision.Files.Remove m : xR.getFiles().getRemoveList()) {
//                                FileChange c = r.createFileChange('D', m.getFileName());
//                                c.definitionName = m.getDefinitionName();
//                                r.fileChanges.add(c);
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}
