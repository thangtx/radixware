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
package org.radixware.kernel.common.builder.release;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;

abstract class RepositoryNode {

    protected final SVNRepositoryAdapter repository;
    protected final long sourceRevision;
    protected final String name;
    protected final RepositoryDir parent;
    private RepositoryDir destination;

    public RepositoryNode(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        this.repository = repository;
        this.sourceRevision = sourceRevision;
        this.name = name;
        this.parent = parent;
    }

    public String getPath() {
        return SvnPath.append(parent.getPath(), getName());
    }

    public String getName() {
        return name;
    }

    public RepositoryNode copyTo(RepositoryDir location) {
        RepositoryNode copy = newCopy(parent);
        copy.destination = location;
        return copy;
    }

    protected abstract RepositoryNode newCopy(RepositoryDir parent);

    public abstract boolean isDirectory();
}

class RepositoryFile extends RepositoryNode {

    private File sourceFile = null;

    public RepositoryFile(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    protected RepositoryNode newCopy(RepositoryDir parent) {
        return new RepositoryFile(parent, repository, sourceRevision, name);
    }

    public static RepositoryFile newInstance(RepositoryDir parent, File src) {
        RepositoryFile file = new RepositoryFile(parent, parent.repository, -1, src.getName());
        file.sourceFile = src;
        return file;
    }

    public byte[] getData() throws RadixSvnException {
        return SVN.getFile(repository, getPath(), sourceRevision);
    }
}

interface RepositoryDirVisitor {

    public void enterNode(RepositoryNode node);

    public void exitNode(RepositoryNode node);
}

class RepositoryDir extends RepositoryNode {

    private class ChildrenByRevision {

        private final long revision;
        private List<RepositoryNode> children = new LinkedList<RepositoryNode>();

        public ChildrenByRevision(long revision) {
            this.revision = revision;
        }

        public RepositoryNode findByName(String name) {
            for (RepositoryNode node : children) {
                if (node.getName().equals(name)) {
                    return node;
                }
            }
            return null;
        }
    }
    private List<ChildrenByRevision> revisions = null;

    public RepositoryDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryNode newCopy(RepositoryDir parent) {
        RepositoryDir dir = createCopy(parent);
        ChildrenByRevision rev = findRevision(-1, false);
        if (rev != null && !rev.children.isEmpty()) {
            ChildrenByRevision rev2 = dir.findRevision(-1, true);
            for (RepositoryNode node : rev.children) {
                rev2.children.add(node.newCopy(dir));
            }
        }
        return dir;
    }

    private ChildrenByRevision findRevision(long number, boolean force) {
        if (revisions != null) {
            for (ChildrenByRevision rev : revisions) {
                if (rev.revision == number) {
                    return rev;
                }
            }
            if (!force) {
                return null;
            }
        } else {
            if (force) {
                revisions = new LinkedList<ChildrenByRevision>();
            } else {
                return null;
            }
        }
        ChildrenByRevision rev = new ChildrenByRevision(number);
        revisions.add(rev);
        return rev;
    }

    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryDir(parent, repository, sourceRevision, name);
    }

    public List<RepositoryNode> getChildren(final long revision) throws RadixSvnException {

        if (revisions != null) {
            for (ChildrenByRevision r : revisions) {
                if (r.revision == revision) {
                    return r.children;
                }
            }
        }
        final ChildrenByRevision rev = new ChildrenByRevision(revision);
        repository.getDir(getPath(), revision, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                RepositoryNode node = createChildNode(entry, revision);
                if (node != null) {
                    rev.children.add(node);
                }
            }
        });
        if (revisions == null) {
            revisions = new LinkedList<>();
        }
        revisions.add(rev);
        return rev.children;
    }

    public RepositoryNode findChildByName(final long revision, String name) {
        if (revisions == null) {
            return null;
        } else {
            for (ChildrenByRevision r : revisions) {
                if (r.revision == revision) {
                    return r.findByName(name);
                }
            }
            return null;
        }
    }

    public RepositoryNode addChild(RepositoryNode node) {
        ChildrenByRevision rev = null;
        if (revisions == null) {
            revisions = new LinkedList<ChildrenByRevision>();
        } else {
            for (ChildrenByRevision r : revisions) {
                if (r.revision == -1) {
                    rev = r;
                    break;
                }
            }
        }
        if (rev == null) {
            rev = new ChildrenByRevision(-1);
            revisions.add(rev);
        }
        RepositoryNode ex = rev.findByName(node.getName());
        if (ex == null) {
            rev.children.add(node);
            return node;
        } else {
            return ex;
        }
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.FILE) {
            return new RepositoryFile(this, repository, revision, svnde.getName());
        } else if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            return new RepositoryDir(this, repository, revision, svnde.getName());
        } else {
            return null;
        }
    }

    protected RepositoryFile findFile(String name, long revision) {
        ChildrenByRevision rev = findRevision(revision, false);
        if (rev == null) {
            return null;
        }
        RepositoryNode node = rev.findByName(name);
        if (node instanceof RepositoryFile) {
            return (RepositoryFile) node;
        }
        return null;
    }

    public Directory findDirectoryXml() {
        RepositoryFile dirIndex = findFile(FileUtils.DIRECTORY_XML_FILE_NAME, sourceRevision);
        if (dirIndex == null) {
            return null;
        }
        try {
            byte[] data = dirIndex.getData();
            DirectoryDocument xDoc = DirectoryDocument.Factory.parse(new ByteArrayInputStream(data));
            return xDoc.getDirectory();
        } catch (XmlException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (RadixSvnException ex) {
            return null;
        }
    }

    private RepositoryNode resolvePath(String path, long revision) {
        String fileName = path.replace("\\", "/");
        String[] components = fileName.split("/");
        RepositoryNode root = this;
        for (String n : components) {
            if (!root.isDirectory()) {
                return null;
            }
            root = ((RepositoryDir) root).findChildByName(revision, n);
            if (root == null) {
                return null;
            }
        }
        return root;
    }
}

class RepositoryBranch extends RepositoryDir {

    public RepositoryBranch(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);

    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            String possibleLayerXmlPath = SvnPath.append(SvnPath.append(getPath(), svnde.getName()), Layer.LAYER_XML_FILE_NAME);
            if (repository.checkPath(possibleLayerXmlPath, revision) == SvnEntry.Kind.FILE) {
                return new RepositoryLayer(this, repository, revision, svnde.getName());
            }
        }
        return super.createChildNode(svnde, revision);
    }

    public RepositoryLayer findLayerByURI(String uri) {
        RepositoryNode node = findChildByName(sourceRevision, uri);
        if (node instanceof RepositoryLayer) {
            return (RepositoryLayer) node;
        }
        return null;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryBranch(parent, repository, sourceRevision, name);
    }
}

abstract class RepositorySegment extends RepositoryDir {

    public RepositorySegment(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    protected abstract RepositoryModule createModule(String name) throws RadixSvnException;

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            String possibleModuleXmlPath = SvnPath.append(SvnPath.append(getPath(), svnde.getName()), Layer.LAYER_XML_FILE_NAME);
            if (repository.checkPath(possibleModuleXmlPath, revision) == SvnEntry.Kind.FILE) {
                return createModule(svnde.getName());
            }
        }
        return super.createChildNode(svnde, revision);
    }

    protected void linkWithSegment(Segment segment) {
        List<Module> modules = segment.getModules().list();
        for (Module module : modules) {
            RepositoryModule repModule = findModuleByName(module.getName());
            if (repModule != null) {
                repModule.linkWithModule(module);
            }
        }
    }

    RepositoryModule findModuleByName(String name) {
        RepositoryNode node = findChildByName(sourceRevision, name);
        if (node instanceof RepositoryModule) {
            return (RepositoryModule) node;
        }
        return null;
    }
}

class RepositoryAdsSegment extends RepositorySegment {

    public RepositoryAdsSegment(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryModule createModule(String name) {
        return new RepositoryAdsModule(this, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryAdsSegment(parent, repository, sourceRevision, name);
    }
}

class RepositoryDdsSegment extends RepositorySegment {

    public RepositoryDdsSegment(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryModule createModule(String name) {
        return new RepositoryDdsModule(this, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryDir(parent, repository, sourceRevision, name);
    }
}

class RepositoryModule extends RepositoryDir {

    public RepositoryModule(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    protected void linkWithModule(Module module) {
    }
}

class RepositoryAdsModule extends RepositoryModule {

    public RepositoryAdsModule(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryAdsModule(parent, repository, sourceRevision, name);
    }

    @Override
    protected void linkWithModule(Module module) {
        File apiXml = new File(module.getDirectory(), AdsModule.API_FILE_NAME);
        if (apiXml.isFile()) {
            addChild(RepositoryFile.newInstance(parent, apiXml));
        }
        File defsXml = new File(module.getDirectory(), FileUtils.DEFINITIONS_XML_FILE_NAME);
        if (defsXml.isFile()) {
            addChild(RepositoryFile.newInstance(parent, defsXml));
        }
        File sqmlDefsXml = new File(module.getDirectory(), FileUtils.SQML_DEFINITIONS_XML_FILE_NAME);
        if (sqmlDefsXml.isFile()) {
            addChild(RepositoryFile.newInstance(parent, sqmlDefsXml));
        }
        File usagesXml = new File(module.getDirectory(), AdsModule.USAGES_FILE_NAME);
        if (usagesXml.isFile()) {
            addChild(RepositoryFile.newInstance(parent, usagesXml));
        }
        File dirXml = new File(module.getDirectory(), FileUtils.DIRECTORY_XML_FILE_NAME);
        if (dirXml.isFile()) {
            addChild(RepositoryFile.newInstance(parent, dirXml));
        }
    }
}

class RepositoryDdsModule extends RepositoryModule {

    public RepositoryDdsModule(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryDdsModule(parent, repository, sourceRevision, name);
    }
}

class RepositoryReleasesDir extends RepositoryDir {

    private final boolean isSrc;

    public RepositoryReleasesDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, boolean src) {
        super(parent, repository, sourceRevision, src ? "releases.src" : "releases");
        this.isSrc = src;
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            return new RepositoryRelease(this, repository, revision, svnde.getName());
        } else {
            return super.createChildNode(svnde, revision);
        }
    }

    public RepositoryRelease findReleaseByNumber(String number) throws RadixSvnException {
        for (RepositoryNode node : getChildren(sourceRevision)) {
            if (node instanceof RepositoryRelease && node.getName().equals(number)) {
                return (RepositoryRelease) node;
            }
        }
        return null;
    }

    public RepositoryRelease appendRelease(String number) {
        RepositoryRelease release = new RepositoryRelease(this, repository, -1, number);
        addChild(release);
        return release;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryReleasesDir(parent, repository, sourceRevision, isSrc);
    }
}

class RepositoryScriptsDir extends RepositoryDir {

    public RepositoryScriptsDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision) {
        super(parent, repository, sourceRevision, "scripts");
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        return super.createChildNode(svnde, revision);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryScriptsDir(parent, repository, sourceRevision);
    }
}

class RepositoryRelease extends RepositoryBranch {

    public RepositoryRelease(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryDir(parent, repository, sourceRevision, name);
    }
}

class RepositoryDevDir extends RepositoryDir {

    public RepositoryDevDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    public RepositoryBranch getBranch(String name, long revision) throws RadixSvnException {
        for (RepositoryNode node : getChildren(revision)) {
            if (node instanceof RepositoryBranch && node.getName().equals(name)) {
                return (RepositoryBranch) node;
            }
        }
        return null;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryDevDir(parent, repository, sourceRevision, name);
    }
}

class RepositoryPatch extends RepositoryRelease {

    public RepositoryPatch(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryPatch(parent, repository, sourceRevision, name);
    }
}

class RepositoryClientPatchesDir extends RepositoryDir {

    public RepositoryClientPatchesDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision) {
        super(parent, repository, sourceRevision, "patches");
    }

    public RepositoryRelease findPatch(String number) {
        RepositoryNode node = findChildByName(sourceRevision, number);
        if (node == null) {
            node = findChildByName(-1, number);
        }
        if (node instanceof RepositoryRelease) {
            return (RepositoryRelease) node;
        } else {
            return null;
        }
    }

    public RepositoryRelease appendPatch(String number) {
        RepositoryPatch patch = new RepositoryPatch(this, repository, -1, number);
        addChild(patch);
        return patch;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryClientPatchesDir(parent, repository, sourceRevision);
    }
}

class RepositoryClient extends RepositoryDir {

    public RepositoryClient(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision, String name) {
        super(parent, repository, sourceRevision, name);
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY && svnde.getName().equals("patches")) {
            return new RepositoryClientPatchesDir(this, repository, revision);
        }
        return super.createChildNode(svnde, revision);
    }

    public RepositoryClientPatchesDir findPatchesDir() {
        RepositoryNode node = findChildByName(sourceRevision, "patches");
        if (node == null) {
            node = findChildByName(-1, "patches");
        }
        if (node instanceof RepositoryClientPatchesDir) {
            return (RepositoryClientPatchesDir) node;
        } else {
            return null;
        }
    }

    public RepositoryClientPatchesDir appendPatchesDir() {
        RepositoryClientPatchesDir dir = new RepositoryClientPatchesDir(this, repository, -1);
        addChild(dir);
        return dir;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryClient(parent, repository, sourceRevision, name);
    }
}

class RepositoryClientsDir extends RepositoryDir {

    public RepositoryClientsDir(RepositoryDir parent, SVNRepositoryAdapter repository, long sourceRevision) {
        super(parent, repository, sourceRevision, "clients");
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            String possibleClientXmlPath = SvnPath.append(SvnPath.append(getPath(), svnde.getName()), "client.xml");
            if (repository.checkPath(possibleClientXmlPath, revision) == SvnEntry.Kind.FILE) {
                return new RepositoryClient(this, repository, revision, svnde.getName());
            }
        }
        return super.createChildNode(svnde, revision);
    }

    public RepositoryClient findClientByName(String name) {
        RepositoryNode node = findChildByName(sourceRevision, name);
        if (node instanceof RepositoryClient) {
            return (RepositoryClient) node;
        } else {
            return null;
        }
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        return new RepositoryClientsDir(parent, repository, sourceRevision);
    }
}

class RepositoryRoot extends RepositoryDir {

    private String path;

    public RepositoryRoot(SVNRepositoryAdapter repository, long sourceRevision, String path) {
        super(null, repository, sourceRevision, SvnPath.tail(path));
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    protected RepositoryNode createChildNode(SvnEntry svnde, long revision) throws RadixSvnException {
        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
            if ("dev".equals(svnde.getName())) {
                return new RepositoryDevDir(this, repository, revision, svnde.getName());
            } else if ("releases".equals(svnde.getName())) {
                return new RepositoryReleasesDir(this, repository, revision, false);
            } else if ("releases.src".equals(svnde.getName())) {
                return new RepositoryReleasesDir(this, repository, revision, true);
            } else if ("scripts".equals(svnde.getName())) {
                return new RepositoryScriptsDir(this, repository, revision);
            } else if ("clients".equals(svnde.getName())) {
                return new RepositoryClientsDir(this, repository, revision);
            }
        }
        return super.createChildNode(svnde, revision);
    }

    public RepositoryDevDir findDevelopmentDir() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "dev");
        if (node == null) {
            node = findChildByName(-1, "dev");
        }
        if (node instanceof RepositoryDevDir) {
            return (RepositoryDevDir) node;
        }
        return null;
    }

    public RepositoryReleasesDir findReleasesDir() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "releases");
        if (node == null) {
            node = findChildByName(-1, "releases");
        }
        if (node instanceof RepositoryReleasesDir) {
            return (RepositoryReleasesDir) node;
        }
        return null;
    }

    public RepositoryScriptsDir findScriptsDir() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "scripts");
        if (node == null) {
            node = findChildByName(-1, "scripts");
        }
        if (node instanceof RepositoryScriptsDir) {
            return (RepositoryScriptsDir) node;
        }
        return null;
    }

    public RepositoryClientsDir findClientsDir() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "clients");
        if (node == null) {
            node = findChildByName(-1, "clients");
        }
        if (node instanceof RepositoryClientsDir) {
            return (RepositoryClientsDir) node;
        }
        return null;
    }

    public RepositoryReleasesDir findReleasesSrcDir() throws RadixSvnException {
        RepositoryNode node = findChildByName(sourceRevision, "releases.src");
        if (node == null) {
            node = findChildByName(-1, "releases.src");
        }
        if (node instanceof RepositoryReleasesDir) {
            return (RepositoryReleasesDir) node;
        }
        return null;
    }

    public RepositoryReleasesDir appendReleasesDir() {
        RepositoryReleasesDir dir = new RepositoryReleasesDir(this, repository, -1, false);
        addChild(dir);
        return dir;
    }

    public RepositoryReleasesDir appendReleasesSrcDir() {
        RepositoryReleasesDir dir = new RepositoryReleasesDir(this, repository, -1, true);
        addChild(dir);
        return dir;
    }

    public RepositoryScriptsDir appendScripts() {
        RepositoryScriptsDir release = new RepositoryScriptsDir(this, repository, -1);
        addChild(release);
        return release;
    }

    @Override
    protected RepositoryDir createCopy(RepositoryDir parent) {
        throw new IllegalStateException("Repository root can not be copied");
    }
}
