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
package org.radixware.kernel.server.instance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.KernelVersionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.CompositeVersion;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.StarterAgent;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.meta.RevisionMetaDiff;
import org.radixware.kernel.starter.radixloader.EActualizeAction;
import org.radixware.kernel.starter.radixloader.IActualizeController;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType;

public class RadixLoaderActualizer {

    private static final RadixLoaderActualizer instance = new RadixLoaderActualizer();
    private KernelVersionError kernelVerErr = null;
    private boolean actualizeControllerInitied = false;
    private final List<DdsVersionWarning> ddsWarningsPool = new LinkedList<DdsVersionWarning>();
    private boolean onlyPreloadNext = false;
    private AtomicBoolean busy = new AtomicBoolean(false);
    private Set<Long> hotswappedRevisions = new HashSet<>();

    private static final String readDdsVersionStmtSQL = "select layerUri, version, upgradeToVersion, upgradeStartTime, nvl(prevCompatibleVer, nvl(upgradeToVersion, version)) compatibleVer from RDX_DdsVersion";
    private static final Stmt readDdsVersionStmt = new Stmt(readDdsVersionStmtSQL);

    private final IDbQueries delegate = new DelegateDbQueries(this, null);

    public static RadixLoaderActualizer getInstance() {
        return instance;
    }

    public void preloadNextAsync() {
        new Thread(Instance.get().getFullTitle() + " async preloader thread " + (new Date())) {

            @Override
            public void run() {
                doPreloadNext();
                Instance.get().scheduleActualizeVer();
            }

        }.start();
    }

    public void doPreloadNext() {
        try {
            if (busy.compareAndSet(false, true)) {
                onlyPreloadNext = true;
                try {
                    ensureControllerIntied();
                    RadixLoader.getInstance().actualize(null, null, null, new HashSet<>(RadixLoader.SERVER_GROUP_SUFFIXES));
                } finally {
                    onlyPreloadNext = false;
                    busy.set(false);
                }
            }
        } catch (Exception ex) {
            Instance.get().getTrace().put(EEventSeverity.ERROR, "Error while preloading next version: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.INSTANCE);
        }
    }

    private void ensureControllerIntied() throws RadixLoaderException {
        final RadixLoader loader = RadixLoader.getInstance();
        final long currentRevision = loader.getCurrentRevision();

        if (!actualizeControllerInitied) {
            loader.setActualizeController(new IActualizeController() {
                @Override
                public EActualizeAction canUpdateTo(RevisionMeta revisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> modifiedGroups) {
                    if (SrvRunParams.getIsHotSwapMode()) {
                        if (hotswappedRevisions.contains(revisionMeta.getNum())) {
                            return EActualizeAction.POSTPONE;
                        }
                        try {
                            final long newRevision = revisionMeta.getNum(); //RadixLoader.getInstance().getLatestRevision();
                            final long oldRevision = RadixLoader.getInstance().getCurrentRevision();

                            if (oldRevision != newRevision) {
                                final RevisionMetaDiff diff = revisionMeta.getDiff(RadixLoader.getInstance().getRevisionMeta(oldRevision));

                                if (!diff.hasAddedClasses() && !diff.hasRemovedClasses()) {
                                    final Instrumentation instr = StarterAgent.installInstrumentation();
                                    int count = 0;

                                    final List<ClassDefinition> toRefresh = new ArrayList<>();
                                    final Map<String, List<Class>> name2Class = new HashMap<>();

                                    for (Class cl : instr.getAllLoadedClasses()) {
                                        if (!name2Class.containsKey(cl.getName())) {
                                            name2Class.put(cl.getName(), new ArrayList<Class>());
                                        }
                                        name2Class.get(cl.getName()).add(cl);
                                    }

                                    for (String file : diff.modifiedJars()) {
                                        try (final ByteArrayInputStream bais = new ByteArrayInputStream(loader.readFileData(revisionMeta.findFile(file), revisionMeta));
                                                final ZipInputStream zis = new ZipInputStream(bais)) {
                                            ZipEntry ze;

                                            while ((ze = zis.getNextEntry()) != null) {
                                                if (ze.getName().endsWith(".class")) {
                                                    final String classeReplace = RevisionMetaDiff.partName2ClassName(ze.getName());
                                                    final byte[] content = loadClassContent(zis);

                                                    List<Class> loadedClasses = name2Class.get(classeReplace);
                                                    if (loadedClasses != null) {
                                                        for (Class cl : loadedClasses) {
                                                            toRefresh.add(new ClassDefinition(cl, content));
                                                            count++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        RadixLoader.getInstance().clearFileCache();

                                        try {
                                            instr.redefineClasses(toRefresh.toArray(new ClassDefinition[toRefresh.size()]));
                                        } catch (UnmodifiableClassException | ClassNotFoundException ex) {
                                            LogFactory.getLog(RadixLoaderActualizer.class).error("HOTSWAP mode: exception " + ex);
                                        }
                                    }

                                    LogFactory.getLog(RadixLoaderActualizer.class).warn("HOTSWAP: New revision [" + newRevision + "] was detected, [" + count + "] classes were hot swapped");
                                }
                                hotswappedRevisions.add(revisionMeta.getNum());
                                return EActualizeAction.POSTPONE;
                            }
                        } catch (IOException ex) {
                            LogFactory.getLog(RadixLoaderActualizer.class).error("HOTSWAP: exception " + ex.getLocalizedMessage(), ex);
                        }
                    }

                    Integer instAadcMemberId = Instance.get().getAadcInstMemberId();
                    if (instAadcMemberId != null && instAadcMemberId > 0) {
                        final long aadcStickedRevision;
                        try {
                            aadcStickedRevision = Starter.readAadcStickedVersions()[instAadcMemberId - 1];
                        } catch (RadixLoaderException ex) {
                            LogFactory.getLog(RadixLoaderActualizer.class).error("Cancelling update because of unable to read AADC sticked revisions: " + ex.getMessage(), ex);
                            return EActualizeAction.POSTPONE;
                        }
                        if (aadcStickedRevision != -1 && aadcStickedRevision != revisionMeta.getNum()) {
                            LogFactory.getLog(RadixLoaderActualizer.class).warn("Update to revision " + revisionMeta.getNum() + " can not be performed because AADC sticked revision is " + aadcStickedRevision);
                            return EActualizeAction.POSTPONE;
                        }
                    }

                    if (modifiedGroups != null
                            && (modifiedGroups.contains(GroupType.KERNEL_COMMON.toString())
                            || modifiedGroups.contains(GroupType.KERNEL_SERVER.toString()))) {
                        kernelVerErr = new KernelVersionError(currentRevision, revisionMeta.getNum());
                        return EActualizeAction.PRELOAD_AND_POSTPONE;
                    }
                    if (onlyPreloadNext) {
                        return EActualizeAction.PRELOAD_AND_POSTPONE;
                    }
                    return EActualizeAction.PERFORM_ACTUALIZATION;
                }
            });
            actualizeControllerInitied = true;
        }
    }

    private static byte[] loadClassContent(final InputStream is) throws IOException {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[8192];
            int len;

            while ((len = is.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        }
    }

    /**
     *
     * @param db
     * @param ddsWarnings - if not null all warnings stored during this and
     * perhaps prev actualize will be moved to this list
     * @throws RadixLoaderException
     * @throws SQLException
     */
    public void actualize(final Connection db, final Collection<DdsVersionWarning> ddsWarnings, final boolean tryUpdateVersion, final boolean bForcePreload) throws RadixLoaderException {
        if (busy.compareAndSet(false, true)) {
            try {
                synchronized (this) {
                    if (kernelVerErr != null) {
                        throw kernelVerErr;
                    }
                    final RadixLoader loader = RadixLoader.getInstance();
                    ensureControllerIntied();
                    final Set<String> preloadGroupSuffixes = new HashSet<>();
                    if (bForcePreload) {
                        preloadGroupSuffixes.addAll(RadixLoader.SERVER_GROUP_SUFFIXES);
                    }

                    if (tryUpdateVersion) {
                        loader.actualize(null, null, null, preloadGroupSuffixes);
                    } else {
                        loader.actualizeAuxInfo();
                    }

                    if (kernelVerErr != null) {
                        throw kernelVerErr;
                    }

                    if (db != null) {
                        try {
                            checkDdsVersions(db);
                        } catch (SQLException e) {
                            // will be checked next time
                        }
                        if (ddsWarnings != null) {
                            ddsWarnings.addAll(ddsWarningsPool);
                            ddsWarningsPool.clear();
                        }
                    }
                }
            } finally {
                busy.set(false);
            }
        }
    }

    public boolean isBusy() {
        return busy.get();
    }

    private void checkDdsVersions(final Connection db) throws SQLException {
        checkDdsVersions(db, RadixLoader.getInstance().getCurrentRevisionMeta(), ddsWarningsPool);
    }

    public static void checkDdsVersions(final Connection db, final RevisionMeta branch, final List<DdsVersionWarning> ddsWarnings) throws SQLException {
        final Map<String, String> layerMetaVerByUri = new HashMap<>(10);

        for (LayerMeta layer : branch.getAllLayersSortedFromBottom()) {
            if (!layer.isEmpty()) {
                layerMetaVerByUri.put(layer.getUri(), layer.getReleaseNumber());
            }
        }

        try (final PreparedStatement st = ((RadixConnection) db).prepareStatement(readDdsVersionStmt)) {
            try (final ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    final String uri = rs.getString("layerUri");
                    final String dbVer = rs.getString("version");
                    if ("0".equals(dbVer)) {
                        continue;//development version
                    }
                    if (layerMetaVerByUri.containsKey(uri)) {
                        final String metaVer = layerMetaVerByUri.get(uri);
                        final String upgradeToVer = rs.getString("upgradeToVersion");
                        final String compatibleVer = rs.getString("compatibleVer");
                        final Timestamp upgradeStartTime = rs.getTimestamp("upgradeStartTime");
                        if (upgradeStartTime != null || upgradeToVer != null
                                || !Utils.equals(dbVer, metaVer)
                                || new CompositeVersion(compatibleVer).compareTo(new CompositeVersion(metaVer)) > 0) {
                            ddsWarnings.add(new DdsVersionWarning(uri, metaVer, dbVer, compatibleVer, upgradeToVer, upgradeStartTime));
                        }
                        layerMetaVerByUri.remove(uri);
                    }
                }
            }

            for (Map.Entry<String, String> layerMetaVer : layerMetaVerByUri.entrySet()) {
                if (!layerMetaVer.getKey().contains(Layer.LOCALE_LAYER_SUFFIX)) {
                    final String metaVer = layerMetaVer.getValue();
                    if (metaVer != null) {
                        ddsWarnings.add(new DdsVersionWarning(layerMetaVer.getKey(), metaVer, null, null, null, null));
                    }
                }
            }
        }
    }

    public static List<DdsVersionWarning> checkDbStructCompatibility(final Connection db) throws SQLException {
        final List<DdsVersionWarning> warnings = new LinkedList<>();
        RadixLoaderActualizer.checkDdsVersions(db, RadixLoader.getInstance().getCurrentRevisionMeta(), warnings);
        final Iterator<DdsVersionWarning> i = warnings.iterator();
        while (i.hasNext()) {
            if (i.next().isDbStructCompatible()) {
                i.remove();
            }
        }
        return warnings;
    }

    public static final class DdsVersionWarning {

        private final String layerUri;
        private final String metaVer;
        private final String dbVer;
        private final String upgradeToVer;
        private final Timestamp upgrateStartTime;
        private final String compatibleVer;

        public DdsVersionWarning(
                final String layerUri, final String srvVer, final String dbVer, final String compatibleVer, final String upgradeToVer, final Timestamp upgradeStartTime) {
            this.layerUri = layerUri;
            this.metaVer = srvVer;
            this.dbVer = dbVer;
            this.upgradeToVer = upgradeToVer;
            this.upgrateStartTime = upgradeStartTime;
            this.compatibleVer = compatibleVer;
        }

        public String getLayerUri() {
            return layerUri;
        }

        public String getDbVer() {
            return dbVer;
        }

        public String getServerVer() {
            return metaVer;
        }

        public String getUpgradeToVer() {
            return upgradeToVer;
        }

        public String getDbCompatibleVersions() {
            return compatibleVer + ".." + (upgradeToVer == null ? dbVer : upgradeToVer);
        }

        public String getUpgrateStartTime() {
            if (upgrateStartTime == null) {
                return null;
            } else {
                return new SimpleDateFormat("HH:mm:ss dd/MM/yy").format(upgrateStartTime);
            }
        }

        public final boolean isDbStructCompatible() {
            final CompositeVersion srvVer = new CompositeVersion(metaVer);
            return new CompositeVersion(compatibleVer).compareTo(srvVer) <= 0 && //srvVer >= prevCompatibleVer
                    srvVer.compareTo(new CompositeVersion((upgradeToVer == null ? dbVer : upgradeToVer))) <= 0; //srvVer <= curVer or updateToVer
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DdsVersionWarning other = (DdsVersionWarning) obj;
            if ((this.layerUri == null) ? (other.layerUri != null) : !this.layerUri.equals(other.layerUri)) {
                return false;
            }
            if ((this.metaVer == null) ? (other.metaVer != null) : !this.metaVer.equals(other.metaVer)) {
                return false;
            }
            if ((this.dbVer == null) ? (other.dbVer != null) : !this.dbVer.equals(other.dbVer)) {
                return false;
            }
            if ((this.upgradeToVer == null) ? (other.upgradeToVer != null) : !this.upgradeToVer.equals(other.upgradeToVer)) {
                return false;
            }
            if (this.upgrateStartTime != other.upgrateStartTime && (this.upgrateStartTime == null || !this.upgrateStartTime.equals(other.upgrateStartTime))) {
                return false;
            }
            if ((this.compatibleVer == null) ? (other.compatibleVer != null) : !this.compatibleVer.equals(other.compatibleVer)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.layerUri != null ? this.layerUri.hashCode() : 0);
            hash = 83 * hash + (this.metaVer != null ? this.metaVer.hashCode() : 0);
            hash = 83 * hash + (this.dbVer != null ? this.dbVer.hashCode() : 0);
            hash = 83 * hash + (this.upgradeToVer != null ? this.upgradeToVer.hashCode() : 0);
            hash = 83 * hash + (this.upgrateStartTime != null ? this.upgrateStartTime.hashCode() : 0);
            hash = 83 * hash + (this.compatibleVer != null ? this.compatibleVer.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "{\"" + getLayerUri() + "\" version is " + getServerVer() + ", DB compatible versions are " + getDbCompatibleVersions() + '}';
        }
    }
}
