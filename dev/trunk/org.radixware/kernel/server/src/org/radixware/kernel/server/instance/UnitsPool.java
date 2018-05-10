/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import org.radixware.kernel.server.units.Factory;
import org.radixware.kernel.server.units.Unit;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.units.UnitListener;
import org.radixware.kernel.server.units.UnitState;

final class UnitsPool extends CopyOnWriteArrayList<Unit> {

    private static final long UNITS_GROUP_START_WAIT_MILLIS = 10000;
    private static final int POSTPONED_UNITS_START_PERIOD_MILLIS = 60000;
    private static final long LENGTHY_STOPPING_UNITS_ABORT_PERIOD_MILLIS = SystemPropUtils.getLongSystemProp("rdx.unit.lengthy.stopping.abort.period.millis", 5 * 60 * 1000);
    private static final long LENGTHY_STOPPING_UNITS_MAINTENANCE_PERIOD_MILLIS = SystemPropUtils.getLongSystemProp("rdx.unit.lengthy.stopping.maintenance.period.millis", 30 * 1000);
    //

    private static final String clearStartedStmtSQL = "update rdx_unit set started = 0, postponed = 0 where instanceId=?";
    private static final Stmt clearStartedStmt = new Stmt(clearStartedStmtSQL, Types.BIGINT);

    private static final String selectUnitDescStmtSQL = "select id, type, title, aadctestmode from rdx_unit where instanceid = ? and use <> 0 " + Factory.getUnitLoadOrderBySql("type") + " , id";
    private static final Stmt selectUnitDescStmt = new Stmt(selectUnitDescStmtSQL, Types.BIGINT);

    //
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    private volatile boolean isRefreshUnitListScheduled = true;
    //maintenance
    private long lastPostponedUnitsStartMillis = 0;
    private long lastLengthyStoppingUnitsMaintenanceMillis = 0; // maintenance = abort and unload

    private static final class UnitInfo {

        public final long id;
        public final long type;
        public final String title;
        public final String qtitle;

        UnitInfo(final long id, final long type, final String title) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.qtitle = "\"#" + String.valueOf(id) + " - " + title + "\"";
        }
    }

    private static final class UnitList extends ArrayList<UnitInfo> {

        private static final long serialVersionUID = 7403210479254326101L;

        private boolean containsId(final long id) {
            for (UnitInfo info : this) {
                if (info.id == id) {
                    return true;
                }
            }
            return false;
        }
    }
    private final Instance instance;

    private UnitsPool() {
        this.instance = null;
    }

    UnitsPool(final Instance instance) {
        super();
        this.instance = instance;
    }

    void loadAll() throws InterruptedException {
        final UnitList list = readUnitList();

        for (UnitInfo info : list) {
            loadUnit(info);
        }
    }

    void requestMaintenance() {
        lastPostponedUnitsStartMillis = 0;
        isRefreshUnitListScheduled = true;
    }

    void scheduleRefreshUnitsList() {
        isRefreshUnitListScheduled = true;
    }

    public void maintenance() throws InterruptedException {
        final long curMillis = System.currentTimeMillis();

        boolean forcedPostponedStart = false;

        if (curMillis - lastPostponedUnitsStartMillis >= POSTPONED_UNITS_START_PERIOD_MILLIS) {
            forcedPostponedStart = true;
            lastPostponedUnitsStartMillis = curMillis;
        }

        if (isRefreshUnitListScheduled || forcedPostponedStart) {
            refreshUnitsList();
            isRefreshUnitListScheduled = false;
        }

        startPostponedUnits(Messages.AUTOSTART_POSTPONED_UNITS, forcedPostponedStart);
        releaseUnreleasedSingletoneLocks();
        abortAndUnloadLengthyStoppingUnits();
    }

    public List<List<Unit>> sortByPriorities(final List<Unit> units) {
        final List<List<Unit>> result = new ArrayList<>();
        if (units == null) {
            return result;
        }
        final List<Unit> temp = new ArrayList<>(units);

        result.add(new ArrayList<Unit>());
        BigDecimal prevPriority = BigDecimal.valueOf(-1);
        if (!temp.isEmpty()) {
            prevPriority = temp.get(0).getPriority();
        }
        for (Unit u : temp) {
            if (!Objects.equals(prevPriority, u.getPriority())) {
                result.add(new ArrayList<Unit>());
                prevPriority = u.getPriority();
            }
            result.get(result.size() - 1).add(u);
        }
        return result;
    }

    private void startUnits(final List<Unit> units, final String reason) throws InterruptedException {
        final List<List<Unit>> unitsByPriorities = sortByPriorities(units);
        for (List<Unit> group : unitsByPriorities) {
            final CountDownLatch latch = new CountDownLatch(group.size());
            for (Unit u : group) {
                final AtomicBoolean started = new AtomicBoolean(false);
                u.registerListener(new UnitListener() {
                    @Override
                    public void stateChanged(Unit unit, UnitState oldState, UnitState newState) {
                        if (newState != UnitState.STARTING) {
                            if (!started.getAndSet(true)) {
                                latch.countDown();
                            }
                            unit.unregisterListener(this);
                        }
                    }
                });
                startUnit(u, reason);
                if (u.getState() == UnitState.STARTED && !started.getAndSet(true)) {
                    latch.countDown();
                }
            }
            latch.await(UNITS_GROUP_START_WAIT_MILLIS, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Load unit if it exists.
     *
     * @param unitId
     * @return Unit or null if such unit doesn't exist.
     */
    Unit tryToLoadUnit(int unitId) throws InterruptedException {
        final UnitList list = readUnitList();
        for (UnitInfo info : list) {
            if (info.id == unitId) {
                return loadUnit(info);
            }
        }
        return null;
    }

    private Unit loadUnit(UnitInfo info) throws InterruptedException {
        instance.getTrace().debug("Loading unit " + info.qtitle, EEventSource.INSTANCE, false);
        Unit unit = null;
        try {
            unit = Factory.newUnit(instance, Long.valueOf(info.type), Long.valueOf(info.id), info.title);
            int idx = -1;
            BigDecimal thisPriority = unit.getPriority();
            for (int i = 0; i < size(); i++) {
                BigDecimal otherPriority = get(i).getPriority();
                if ((thisPriority != null && (otherPriority == null || thisPriority.compareTo(otherPriority) < 0)) || (Objects.equals(thisPriority, otherPriority) && unit.getId() < get(i).getId())) {
                    idx = i;
                    break;
                }
            }
            if (idx != -1) {
                add(idx, unit);
            } else {
                add(unit);
            }
            instance.getTrace().put(EEventSeverity.EVENT, Messages.UNIT_LOADED + info.qtitle, Messages.MLS_ID_UNIT_LOADED, new ArrStr(info.qtitle, instance.getFullTitle()), EEventSource.INSTANCE, false);
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.UNIT_LOAD_ERROR + info.qtitle + ": \n" + exStack, Messages.MLS_ID_UNIT_LOAD_ERROR, new ArrStr(info.qtitle, exStack), EEventSource.INSTANCE, false);
        }
        return unit;
    }

    boolean startUnit(final Unit unit, final String reason) throws InterruptedException {
        final String qUnitTitle = "\"" + unit.getFullTitle() + "\"";
        try {
            instance.getTrace().debug("Starting unit " + unit.getFullTitle(), EEventSource.INSTANCE, false);
            final Long lastWrittenSelfCheckTimeMillis = instance.getLastWrittenUnitCheckTimeMillis(unit.getId());
            if (lastWrittenSelfCheckTimeMillis != null) {
                instance.getDbQueries().clearOutdatedUnitStartedState(unit.getId(), lastWrittenSelfCheckTimeMillis);
            }
            return unit.start(reason);
        } catch (Throwable e) {
            if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_START + " " + qUnitTitle + ": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_START, new ArrStr("\"" + unit.getFullTitle() + "\"", exStack), EEventSource.INSTANCE, false);
            return false;
        }
    }

    void refreshUnitsList() throws InterruptedException {
        final UnitList currentlyUsedUnits = readUnitList();
        final List<Unit> stoppedUnitsToUnload = new ArrayList<>();
        for (Unit unit : this) {
            if (!currentlyUsedUnits.containsId(unit.getId())) {
                if (unit.getState() == UnitState.STOPPED) {
                    stoppedUnitsToUnload.add(unit);
                }
                if (unit.getState() == UnitState.START_POSTPONED && unit.stop("deleted from database")) {
                    stoppedUnitsToUnload.add(unit);
                }
            }
        }
        for (Unit unitToUnload : stoppedUnitsToUnload) {
            if (unitToUnload.stop(Messages.UNLOADING_STOPPED_UNIT)) {
                unloadUnit(unitToUnload);
            }
        }
        for (UnitInfo info : currentlyUsedUnits) {
            if (findUnit(info.id) == null) {
                loadUnit(info);
            }
        }
    }

    void startPostponedUnits(final String reason, boolean forced) throws InterruptedException {
        final List<Unit> unitsToStart = new ArrayList<>();
        for (Unit unit : this) {
            if (unit.getState() == UnitState.START_POSTPONED && (forced || unit.isPostponedStartRequiredImmediately())) {
                unitsToStart.add(unit);
            }
        }
        startUnits(unitsToStart, reason);
    }

    void releaseUnreleasedSingletoneLocks() {
        for (Unit unit : this) {
            if (unit.getState() == UnitState.START_POSTPONED 
                    || unit.getState() == UnitState.STOPPED
                    || unit.getState() == UnitState.STARTED) {
                unit.releaseAcquiredSingletoneUnitLock();
            }
        }
    }

    boolean stopUnit(final Unit unit, final String reason) {
        instance.getTrace().debug("Stopping unit \"" + unit.getFullTitle() + "\"", EEventSource.INSTANCE, false);
        boolean wasStoped = false;
        try {
            wasStoped = unit.stop(reason);
            if (wasStoped) {
                instance.getTrace().put(EEventSeverity.EVENT, Messages.UNIT_STOPPED + unit.getFullTitle(), Messages.MLS_ID_UNIT_STOPPED, new ArrStr(unit.getFullTitle()), EEventSource.INSTANCE, false);
            }
        } catch (InterruptedException e) {
            //continue unit unloading
        } catch (Throwable e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_STOP + " \"" + unit.getFullTitle() + "\": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_STOP, new ArrStr(unit.getFullTitle(), exStack), EEventSource.INSTANCE, false);
        }
        return wasStoped;
    }

    void unloadUnit(final Unit unit) {
        if (unit.getState() != UnitState.STOPPED) {
            throw new IllegalStateException("Can't unload not stopped unit");
        }
        final String unitTitle = "\"" + unit.getFullTitle() + "\"";
        unit.disposeView();
        remove(unit);
        instance.getTrace().put(EEventSeverity.EVENT, Messages.UNIT_UNLOADED + unitTitle, Messages.MLS_ID_UNIT_UNLOADED, new ArrStr(unitTitle), EEventSource.INSTANCE, false);
    }

    void setUnitsNotStartedInDb() {
        if (instance.getDbConnection() != null) {

            try (PreparedStatement st = ((RadixConnection) instance.getDbConnection()).prepareStatement(clearStartedStmt)) {
                st.setLong(1, instance.getId());
                st.executeUpdate();
                instance.getDbConnection().commit();
            } catch (SQLException ex) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": \n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
            }
        }
    }

    private UnitList readUnitList() {
        final UnitList list = new UnitList();
        try {
            final Connection dbConnection = instance.getDbConnection();
            if (dbConnection != null) {
                try (PreparedStatement qryReadUnitList = ((RadixConnection) dbConnection).prepareStatement(selectUnitDescStmt)) {
                    qryReadUnitList.setInt(1, instance.getId());
                    try (final ResultSet rs = qryReadUnitList.executeQuery()) {
                        while (rs.next()) {
                            Boolean unitTestMode = rs.getBoolean("aadctestmode");
                            if (rs.wasNull())
                                unitTestMode = null;
                            if (instance.isAadcTestedMember() &&  unitTestMode == false || 
                                    !instance.isAadcTestedMember() && unitTestMode == true) {
                                continue;
                            }
                            final long id = rs.getLong("id");
                            final String unitTitle =/*
                                     * Messages.UNIT + " #" + String.valueOf(id)
                                     * + " - " +
                                     */ rs.getString("title");
                            list.add(new UnitInfo(id, rs.getLong("type"), unitTitle));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(ex);
            instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_IN_DB_QRY + ": \n" + exStack, Messages.MLS_ID_ERR_IN_DB_QRY, new ArrStr(exStack), EEventSource.INSTANCE, false);
        }
        return list;
    }

    void unloadAll(final String reason) {
        while (!stopAll(true, reason)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    private boolean stopAll(final boolean bClear, final String reason) {
        boolean allOk = true;
        LinkedList<Unit> notStopped = null;
        final LinkedList<Unit> reverseList = new LinkedList<Unit>();
        for (Unit unit : this) {
            reverseList.addFirst(unit);
        }
        for (Unit unit : reverseList) {// stop in reverse order 
            final String unitTitle = "\"" + unit.getFullTitle() + "\"";
            if (bClear) {
                instance.getTrace().debug("Unloading unit " + unitTitle, EEventSource.INSTANCE, false);
            }
            try {
                final boolean wasStopped = stopUnit(unit, reason);
                if (!wasStopped) {
                    allOk = false;
                }
                if (bClear) {
                    if (wasStopped) {
                        unit.disposeView();
                        instance.getTrace().put(EEventSeverity.EVENT, Messages.UNIT_UNLOADED + unitTitle, Messages.MLS_ID_UNIT_UNLOADED, new ArrStr(unitTitle), EEventSource.INSTANCE, false);
                    } else {
                        if (notStopped == null) {
                            notStopped = new LinkedList<Unit>();
                        }
                        notStopped.add(unit);
                    }
                }
            } catch (Throwable e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_UNIT_UNLOAD + " " + unitTitle + ": \n" + exStack, Messages.MLS_ID_ERR_ON_UNIT_UNLOAD, new ArrStr(unitTitle, exStack), EEventSource.INSTANCE, false);
            }
        }
        if (bClear) {
            clear();
            if (notStopped != null) {
                addAll(notStopped); //��������������� �����������
            }
        }
        return allOk;
    }

    Unit findUnit(final long unitId) {
        for (Unit unit : this) {
            if (unit.getId() == unitId) {
                return unit;
            }
        }
        return null;//not found
    }

    void startAll(final String reason) throws InterruptedException {
        final UnitList units = readUnitList();
        final LinkedList<Long> loadedUnitIds = new LinkedList<>();
        final List<Unit> toStart = new ArrayList<>();
        for (Unit unit : this) {
            if (units.containsId(unit.getId())) {
                toStart.add(unit);
                loadedUnitIds.add(Long.valueOf(unit.getId()));
            } else if (stopUnit(unit, reason)) {
                unloadUnit(unit);
            }
        }
        for (UnitInfo info : units) {
            if (!loadedUnitIds.contains(Long.valueOf(info.id))) {
                final Unit unit = loadUnit(info);
                if (unit != null) {
                    toStart.add(unit);
                }
            }
        }
        startUnits(toStart, reason);
    }

    void startLoaded(final String reason) throws InterruptedException {
        startUnits(new ArrayList<>(this), reason);
    }

    boolean restartAll(final String reason) throws InterruptedException {
        final UnitList units = readUnitList();
        final LinkedList<Long> startedUnitIds = new LinkedList<>();
        final List<Unit> toStart = new ArrayList<>();
        boolean allOk = true;
        final List<Unit> reversedlist = new ArrayList<Unit>();
        for (Unit u : this) {
            reversedlist.add(0, u);
        }
        for (Unit unit : reversedlist) {
            if (stopUnit(unit, reason)) {
                unloadUnit(unit);
            } else {
                allOk = false;
                startedUnitIds.add(Long.valueOf(unit.getId()));
            }
        }
        for (UnitInfo info : units) {
            if (!startedUnitIds.contains(Long.valueOf(info.id))) {
                toStart.add(loadUnit(info));
            }
        }
        startUnits(toStart, reason);
        return allOk;
    }

    boolean stopAll(final String reason) {
        return stopAll(false, reason);
    }
    
    void abortAndUnloadLengthyStoppingUnits() {
        final long now = System.currentTimeMillis();
        if (now - lastLengthyStoppingUnitsMaintenanceMillis < LENGTHY_STOPPING_UNITS_MAINTENANCE_PERIOD_MILLIS) {
            return;
        }
        lastLengthyStoppingUnitsMaintenanceMillis = now;
        
        final List<Unit> toAbort = new ArrayList<>();
        for (final Unit unit: this) {
            if (unit.getState() == UnitState.STOPPING && now - unit.getLastStoppingStateTimeMillis() > LENGTHY_STOPPING_UNITS_ABORT_PERIOD_MILLIS) {
                toAbort.add(unit);
            }
        }
        
        for (final Unit unit: toAbort) {
            unit.setLastStoppingStateTimeMillis(Long.MAX_VALUE);
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        instance.getTrace().put(EEventSeverity.ERROR, "Initiating abort and unload for lengthy stopping " + unit.getFullTitle(), EEventSource.INSTANCE);
                        unit.abortAndUnload("stop timeout");
                    } catch (Throwable t) {
                        instance.getTrace().put(EEventSeverity.ERROR, "Exception on initiating abort and unload for lengthy stopping " + unit.getFullTitle() + ":\n" + ExceptionTextFormatter.exceptionStackToString(t), EEventSource.INSTANCE);
                    }
                }
            };
            new Thread(r, unit.getFullTitle() + " abort and unload thread").start();
        }
    }
    
}
