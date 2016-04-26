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

package org.radixware.kernel.common.scml;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;


public class LineMatcher {

    public interface ILocationDescriptor {

        public Id[] getPath();

        public String getDetails();

        public Id getModuleId();

        public String getLayerURI();
    }

    public static class DefaultLocationDescriptor implements ILocationDescriptor {

        private final Definition definition;

        public DefaultLocationDescriptor(Definition definition) {
            this.definition = definition;
        }

        @Override
        public Id[] getPath() {
            return definition.getIdPath();
        }

        @Override
        public String getDetails() {
            return null;
        }

        @Override
        public Id getModuleId() {
            final Module module = definition.getModule();
            if (module == null) {
                return null;
            } else {
                return module.getId();
            }
        }

        @Override
        public String getLayerURI() {
            final Module module = definition.getModule();
            if (module == null) {
                return null;
            }
            final Segment segment = module.getSegment();
            if (segment == null) {
                return null;
            }
            final Layer layer = segment.getLayer();
            if (layer == null) {
                return null;
            }
            return layer.getURI();
        }
    }
    private LineMatcher parent;
    private ILocationDescriptor name;
    private int line = 0;
    private int startLine = 0;
    private static final ILocationDescriptor defaultDescriptor = new ILocationDescriptor() {

        @Override
        public Id[] getPath() {
            return new Id[0];
        }

        @Override
        public String getDetails() {
            return "root";
        }

        @Override
        public Id getModuleId() {
            return null;
        }

        @Override
        public String getLayerURI() {
            return null;
        }
    };
    private List<LineMatcher> children = null;

    LineMatcher() {
        this(defaultDescriptor, null);
    }

    LineMatcher(ILocationDescriptor name, LineMatcher parent) {
        this.parent = parent;
        this.name = name;
    }

    LineMatcher getParent() {
        return parent;
    }

    ILocationDescriptor getName() {
        return name;
    }

    public LineMatcher addChild(ILocationDescriptor name) {
        LineMatcher child = new LineMatcher(name, this);
        if (children == null) {
            children = new LinkedList<LineMatcher>();
        }
        children.add(child);
        child.startLine = line;
        return child;
    }

    void newLine() {
        if (parent != null) {
            parent.newLine();
        }
        line++;
    }

    private static String encode_layer_uri(String uri) {
        if (uri == null) {
            return "null";
        } else {
            return uri;
        }
    }

    private static String encode_id(Id id) {
        return id == null ? "null" : id.toString();
    }

    private static String encode_ids(Id[] ids) {

        if (ids == null) {
            return "null";
        } else {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (Id id : ids) {
                if (first) {
                    first = false;
                } else {
                    builder.append('-');
                }
                builder.append(id.toString());
            }
            return builder.toString();
        }
    }

    public static class LocationInfo {

        public final String layerUri;
        public final Id moduleId;
        public final Id[] definitionPath;
        public final String suffix1;
        public final String suffix2;

        protected LocationInfo(String layerUri, Id moduleId, Id[] definitionPath, String suffix1, String suffix2) {
            this.layerUri = layerUri;
            this.moduleId = moduleId;
            this.definitionPath = definitionPath;
            this.suffix1 = suffix1;
            this.suffix2 = suffix2;
        }

        public Layer findLayer(Branch context) {
            return context.getLayers().findByURI(layerUri);
        }

        public Module findModule(Branch context, ERepositorySegmentType segmentTypeHint) {
            Layer layer = findLayer(context);
            if (layer == null) {
                return null;
            }

            return layer.getSegmentByType(segmentTypeHint).getModules().findById(moduleId);
        }

        protected static String encode(String layerUri, Id moduleId, Id[] definitionPath, String suffix1, String suffix2) {
            StringBuilder code = new StringBuilder();
            code.append(encode_layer_uri(layerUri));
            code.append('\uFFFF');
            code.append(encode_id(moduleId));
            code.append('\uFFFF');
            code.append(encode_ids(definitionPath));
            code.append('\uFFFF');
            if (suffix1 != null) {
                code.append(suffix1);
            }
            code.append('\uFFFF');
            if (suffix2 != null) {
                code.append(suffix2);
            }
            return code.toString();
        }
    }

    public static LocationInfo decode(String locationDescriptor) {
        if (locationDescriptor == null) {
            return null;
        }
        String[] parts = locationDescriptor.split("\uFFFF");
        if (parts.length < 3) {
            return null;
        }
        String layerUri = parts[0];
        Id moduleId = Id.Factory.loadFrom(parts[1]);
        String[] idsAsStr = parts[2].split("-");
        Id[] ids = new Id[idsAsStr.length];

        for (int i = 0; i < idsAsStr.length; i++) {
            ids[i] = Id.Factory.loadFrom(idsAsStr[i]);
        }
        String suffix1 = null, suffix2 = null;
        if (parts.length > 3) {
            suffix1 = parts[3].isEmpty() ? null : parts[3];
        }
        if (parts.length > 4) {
            suffix2 = parts[4].isEmpty() ? null : parts[4];
        }
        return new LocationInfo(layerUri, moduleId, ids, suffix1, suffix2);
    }

    public static String encode(ILocationDescriptor descritor) {
        return LocationInfo.encode(descritor.getLayerURI(), descritor.getModuleId(), descritor.getPath(), descritor.getDetails(), null);
    }

    private void debug(StringBuilder builder) {
        builder.append(encode(name));
        builder.append(" start=");
        builder.append(startLine);
        builder.append(", count=");
        builder.append(line + 1);
        builder.append("\n");
        if (children != null) {
            for (LineMatcher c : children) {
                c.debug(builder);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        debug(builder);
        return builder.toString();
    }

    private void saveAsJSR045SMAP(String finalSourceName, String defaultStratum, StringBuilder sb) {
        if (children != null && !children.isEmpty()) {
            sb.append("SMAP\n");
            sb.append(finalSourceName).append('\n');
            sb.append(defaultStratum).append('\n');
            sb.append("*S Jml\n");
            sb.append("*F\n");

            for (int index = 0; index < children.size(); index++) {
                LineMatcher m = children.get(index);
                sb.append(index + 1).append(' ').append(encode(m.name)).append('\n');
            }
            sb.append("*L\n");
            for (int index = 0; index < children.size(); index++) {
                LineMatcher m = children.get(index);
                int fileId = index + 1;
                sb.append(1).append('#').append(fileId).append(',').append(m.line + 1).append(':').append(m.startLine + 1).append('\n');
            }
            sb.append("*E\n");
        }
    }

    public String getJSR045(String finalSourceName) {
        StringBuilder builder = new StringBuilder();
        saveAsJSR045SMAP(finalSourceName, "Java", builder);
        return builder.toString();
    }

    public boolean isEmpty() {
        return children == null || children.isEmpty();
    }
}
