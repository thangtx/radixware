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

package org.radixware.kernel.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.exceptions.KernelVersionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.CompositeVersion;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
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

    public static RadixLoaderActualizer getInstance() {
        return instance;
    }

    /**
     *
     * @param db
     * @param ddsWarnings - if not null all warnings stored during this and
     * perhaps prev actualize will be moved to this list
     * @throws RadixLoaderException
     * @throws SQLException
     */
    public synchronized void actualize(final Connection db, final Collection<DdsVersionWarning> ddsWarnings, final boolean bForcePreload) throws RadixLoaderException {
        if (kernelVerErr != null) {
            throw kernelVerErr;
        }
        final RadixLoader loader = RadixLoader.getInstance();
        final long currentRevision = loader.getCurrentRevision();
        if (!actualizeControllerInitied) {
            loader.setActualizeController(new IActualizeController() {
                @Override
                public EActualizeAction canUpdateTo(RevisionMeta revisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> modifiedGroups) {
                    if (modifiedGroups != null
                            && (modifiedGroups.contains(GroupType.KERNEL_COMMON.toString())
                            || modifiedGroups.contains(GroupType.KERNEL_SERVER.toString()))) {
                        kernelVerErr = new KernelVersionError(currentRevision, revisionMeta.getNum());
                        return EActualizeAction.PRELOAD_AND_POSTPONE;
                    }
                    return EActualizeAction.PERFORM_ACTUALIZATION;
                }
            });
            actualizeControllerInitied = true;
        }

        final Set<String> preloadGroupSuffixes = new HashSet<>();
        if (bForcePreload) {
            preloadGroupSuffixes.addAll(RadixLoader.SERVER_GROUP_SUFFIXES);
        }

        loader.actualize(null, null, null, preloadGroupSuffixes);

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

    private void checkDdsVersions(final Connection db) throws SQLException {
        checkDdsVersions(db, RadixLoader.getInstance().getCurrentRevisionMeta(), ddsWarningsPool);
    }

    public static void checkDdsVersions(final Connection db, final RevisionMeta branch, final List<DdsVersionWarning> ddsWarnings) throws SQLException {
        final Map<String, String> layerMetaVerByUri = new HashMap<>(10);
        for (LayerMeta layer : branch.getAllLayersSortedFromBottom()) {
            layerMetaVerByUri.put(layer.getUri(), layer.getReleaseNumber());
        }
        final PreparedStatement st = db.prepareStatement("select layerUri, version, upgradeToVersion, upgradeStartTime, nvl(prevCompatibleVer, nvl(upgradeToVersion, version)) compatibleVer from RDX_DdsVersion");
        try {
            final ResultSet rs = st.executeQuery();
            try {
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
            } finally {
                rs.close();
            }
            for (Map.Entry<String, String> layerMetaVer : layerMetaVerByUri.entrySet()) {
                if (!layerMetaVer.getKey().contains(Layer.LOCALE_LAYER_SUFFIX)) {
                    final String metaVer = layerMetaVer.getValue();
                    if (metaVer != null) {
                        ddsWarnings.add(new DdsVersionWarning(layerMetaVer.getKey(), metaVer, null, null, null, null));
                    }
                }
            }
        } finally {
            st.close();
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
