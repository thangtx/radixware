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

package org.radixware.kernel.server.algo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.AlgorithmDefinitionError;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.types.Algorithm;

import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;
import org.radixware.schemas.algo.ContextDocument;
import org.radixware.schemas.algo.ContextDocument.Context;
import org.radixware.schemas.algo.ContextDocument.Context.AlgoClasses;
import org.radixware.schemas.algo.ContextDocument.Context.Data;
import org.radixware.schemas.algo.ContextDocument.Context.Discriminators;
import org.radixware.schemas.algo.ContextDocument.Context.Threads;
import org.radixware.schemas.algo.ContextDocument.Context.AlgoClasses.AlgoClassItem;
import org.radixware.schemas.algo.ContextDocument.Context.Discriminators.DiscriminatorItem;
import org.radixware.schemas.algo.ContextDocument.Context.Threads.ThreadItem;
import org.radixware.schemas.algo.ContextDocument.Context.Threads.ThreadItem.AlgoStack;
import org.radixware.schemas.algo.ContextDocument.Context.Threads.ThreadItem.AlgoStack.AlgoItem.MergeStack;
import org.radixware.schemas.algo.ContextDocument.Context.Threads.ThreadItem.ExceptionStack;
import org.radixware.schemas.algo.ContextDocument.Context.Threads.ThreadItem.Waits;

// workflow executor
public final class AlgorithmExecutor {

    private static final String PATH_SEPARATOR = "-";

    private final Arte arte;
    public final Arte getArte() {
        return arte;
    }

    public class AlgoStackItem {
        public Id algoId; // algorithm id.
        public Id blockId; // block id.
        public Stack<MergeItem> mergeStack = new Stack<MergeItem>(); // merge stack

        public AlgoStackItem(Id algoId, Id blockId) {
            this.algoId = algoId;
            this.blockId = blockId;
        }
        public AlgoStackItem(AlgoStackItem asi) {
            this.algoId = asi.algoId;
            this.blockId = asi.blockId;
            for (MergeItem mi : asi.mergeStack)
                mergeStack.add(new MergeItem(mi.id, mi.finish));
        }
    }

    public class WaitItem {
        public Long id;
        public Id strobId;
        public Timestamp time;
        public boolean clear;

        public WaitItem(Long id, Id strobId, Timestamp time, boolean clear) {
            this.id = id;
            this.strobId = strobId;
            this.time = time;
            this.clear = clear;
        }
    }

    public class MergeItem {
        public Id id;
        public boolean finish;

        public MergeItem(Id id) {
            this.id = id;
            finish = false;
        }
        public MergeItem(Id id, boolean finish) {
            this.id = id;
            this.finish = finish;
        }
    }

    public class Thread {

        static public final int ACTIVE   = 1;
        static public final int WAITING	 = 2;
        static public final int RESUME   = 3;
        static public final int FINISHED = 9;

        public int state = ACTIVE;
        public Id scopeId = null;
        public Id blockId = null;
        public int input = 0;
		
        public Stack<AlgoStackItem> algoStack = new Stack<AlgoStackItem>();
        public Stack<Throwable> exceptionStack = new Stack<Throwable>();
        public List<WaitItem> waits = new ArrayList<WaitItem>();

        public Long resumedWaitId = null;
        public Id resumedStrobId = null;
        public Object clientData = null;

        public Thread() {
        }
		
        public Thread(Id algoId, Object clientData) {
            newAlgo(algoId);
            this.clientData = clientData;
        }

        @SuppressWarnings("unchecked")
        public Thread(Thread t) {  // for fork
            exceptionStack = (Stack<Throwable>)t.exceptionStack.clone();
            for (AlgoStackItem asi : t.algoStack) {
                algoStack.add(new AlgoStackItem(asi));
            }
            clientData = t.clientData;
            scopeId = t.scopeId;
        }

        private void checkException(Id algoId, Id blockId) {
            throw new AlgorithmDefinitionError("Algorithm " + algoId + " was modified, unable to find block #" + blockId);
        }

        public void check() throws AlgorithmDefinitionError {
            Id prevAlgoId = null;
            Set<Id> prevIds = null;
            for (AlgoStackItem asi: algoStack) {
                final Algorithm algo = (Algorithm)getArte().newObject(asi.algoId);
                final Set<Id> ids = algo.getBlockIds();
                if (prevIds != null && asi.blockId != null && !prevIds.contains(asi.blockId))
                    checkException(prevAlgoId, asi.blockId);
                for (MergeItem mi: asi.mergeStack) {
                    if (mi.id != null && !ids.contains(mi.id))
                        checkException(asi.algoId, mi.id);
                }
                prevIds = ids;
                prevAlgoId = asi.algoId;
            }
        }

        public void registerWait(Long id, Id strobId, Timestamp t, boolean clear) {
            waits.add(new WaitItem(id, strobId, t, clear));
        }

        public void registerMerge(Id mergeId) {
            algoStack.peek().mergeStack.push(new MergeItem(mergeId));
        }

        public Algorithm newAlgo(Id algoId) {
            Algorithm algo = AlgorithmExecutor.this.getAlgo(algoId);
            AlgoStackItem asi = new AlgoStackItem(algoId, blockId);
            algoStack.push(asi);
            scopeId = null;
            blockId = algo.getFirstScopeBlock(scopeId);
            input = algo.getFirstScopeBlockInput(scopeId);
            return algo;
        }

        public Algorithm getAlgo() {
            return AlgorithmExecutor.this.getAlgo(algoStack.peek().algoId);
        }

        public String getPath() {
            String path = "";
            for (int i = 1; i < algoStack.size(); i++)
                path = PATH_SEPARATOR + algoStack.get(i).blockId + path;
            return path;
        }

        public AlgoStackItem getByPath(String path) {
            String s[] = path.split(PATH_SEPARATOR);
            Iterator<AlgoStackItem> it = algoStack.iterator();
            AlgoStackItem asi = it.next();
            for (int i = s.length-1; i>=0; i--) {
                if (Utils.emptyOrNull(s[i]))
                    continue;
                Id id = Id.Factory.loadFrom(s[i]);
                if (!it.hasNext())
                    return null;
                asi = it.next();
                if (asi.blockId != id)
                    return null;
            }
            return asi;
        }

    }

    static public final int PROGRAM_BLOCK_TYPE   = 1;
    static public final int SWITCH_BLOCK_TYPE    = 2;
    static public final int EMPTY_BLOCK_TYPE     = 3;
    static public final int TRACE_BLOCK_TYPE     = 4;
    static public final int RETURN_BLOCK_TYPE    = 7;
    static public final int FINISH_BLOCK_TYPE    = 8;
    static public final int TERMINATE_BLOCK_TYPE = 9;
    static public final int SCOPE_BLOCK_TYPE     = 11;
    static public final int THROW_BLOCK_TYPE     = 21;
    static public final int CATCH_BLOCK_TYPE     = 22;
    static public final int FORK_BLOCK_TYPE      = 31;
    static public final int MERGE_BLOCK_TYPE     = 32;
    static public final int INCLUDE_BLOCK_TYPE   = 41;
    static public final int APP_BLOCK_TYPE       = 101;

    static public final double WAIT_TIMEOUT_BOUNDARY = 5.0;

    // Fields
    public Entity process = null;
    public boolean syncExecution = false;
    public long lastWaitId = 0;

    public class DataItem {
        public EValType type;
        public String clazz;
        public boolean persistent;
        public boolean lost;
        public Object val;

        public DataItem(EValType type, String clazz, Object val, boolean persistent) {
            this.type = type;
            this.clazz = clazz;
            this.persistent = persistent;
            this.lost = false;
            this.val = val;
        }

        public DataItem(EValType type, String clazz, Object val, boolean persistent, boolean lost) {
            this.type = type;
            this.clazz = clazz;
            this.persistent = persistent;
            this.lost = lost;
            this.val = val;
        }
    }

    private List<Thread> threads = new ArrayList<Thread>();               // <Thread>
    private Map<Id, DataItem> data = new HashMap<Id, DataItem>();         // <String id, Object val>
    private Map<Id, Integer> discriminators = new HashMap<Id, Integer>(); // <Stribg blockId, Integer count>
    private Map<Id, Id> algoClasses = new HashMap<Id, Id>();              // <String referencedClass, String workingClass>

    private Thread currentThread = null;
    private Algorithm currentAlgo = null;
    private Timestamp simulatedTime;

    private Entity processType = null;
    public Entity getProcessType() {
        return processType;
    }

    // Construtor for create
    public AlgorithmExecutor(Arte arte, Entity process, Entity processType, boolean syncExecution, Id referencedId, Id workingId, Object clientData) {
        this.arte = arte;
        this.process = process;
        this.syncExecution = syncExecution;
        this.processType = processType;
        registerAlgo("", referencedId, workingId);
        registerData("", AdsAlgoClassDef.EXCEPTION_VAR_ID, EValType.JAVA_CLASS, null, null, true);
        Thread t = new Thread(referencedId, clientData);
        threads.add(t);
	}

    // Construtor for resume
    public AlgorithmExecutor(Arte arte, Entity process, Timestamp simulatedTime) {
        this.arte = arte;
        this.process = process;
        this.simulatedTime = simulatedTime;
    }

    private Class<?> getClassForName(String name) throws ClassNotFoundException {
        Pattern p = Pattern.compile("^(.*)\\.impl\\.(.*)Impl\\$Factory$");
        Matcher m = p.matcher(name);
        if (m.find()) {
            name = m.group(1) + "." + m.group(2) + "$Factory";
        }
/*
        if (name.endsWith("impl.RequestImpl$Factory")) {
            name = name.substring(0, name.length() - "impl.RequestImpl$Factory".length());
            name += "Request$Factory";
        }
*/
        try {
            return arte.getDefManager().getClassLoader().loadClass(name);
        } catch(ClassNotFoundException ex) {
            // do nothing
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return getClass().getClassLoader().loadClass(name);
    }

    class ContextInputStream extends ObjectInputStream {
        ContextInputStream(InputStream n) throws IOException {
            super(n);
        }
        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            Class<?> c;
            try {
                c = arte.getDefManager().getClassLoader().loadClass(desc.getName());
            } catch(ClassNotFoundException cnfe) {
                c = null;
            }
            if (c != null)
                return c;
            return super.resolveClass(desc);
        }
    }


    public Clob getContext() {
        HashMap<Object, Id> val2Id = new HashMap<Object, Id>();

        ContextDocument doc = ContextDocument.Factory.newInstance();
        Context xContext = doc.addNewContext();
        xContext.setLastWaitId(lastWaitId);
		
        Threads xThreads = xContext.addNewThreads();
        for (Thread t : threads) {
            ThreadItem xThread = xThreads.addNewThreadItem();
            xThread.setState(t.state);
            xThread.setScopeId(t.scopeId);
            xThread.setBlockId(t.blockId);
            xThread.setInput(t.input);
            Waits xWaits = xThread.addNewWaits();
            for (int i = 0; i< t.waits.size(); i++ ) {
                WaitItem w = t.waits.get(i);
                Waits.WaitItem xWait = xWaits.addNewWaitItem();
                if (w.id != null)
                    xWait.setId(w.id.longValue());
                if (w.strobId != null)
                    xWait.setStrobId(w.strobId);
                if (w.time != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(w.time);
                    xWait.setTime(c);
                }
                xWait.setClear(w.clear);
            }
            AlgoStack xAlgoStack = xThread.addNewAlgoStack();
            for (AlgoStackItem asi: t.algoStack) {
                AlgoStack.AlgoItem xAlgo = xAlgoStack.addNewAlgoItem();
                xAlgo.setBlockId(asi.blockId);
                xAlgo.setAlgoId(asi.algoId);
                MergeStack xMergeStack = xAlgo.addNewMergeStack();
                for (MergeItem mi: asi.mergeStack) {
                    MergeStack.MergeItem xMerge = xMergeStack.addNewMergeItem();
                    xMerge.setId(mi.id);
                    xMerge.setFinish(mi.finish);
                }
            }
            ExceptionStack xExceptionStack = xThread.addNewExceptionStack();
            for (Throwable ex: t.exceptionStack) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream;
                try {
                    objectStream = new ObjectOutputStream(byteStream);
                    objectStream.writeObject(ex);
                    objectStream.flush();
                } catch (IOException e) {
                    byteStream = null;
                    trace(EEventSeverity.WARNING, "Exception save error: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""));
                }
                ExceptionStack.ExceptionItem xException = xExceptionStack.addNewExceptionItem();
                xException.setByteArrayValue(byteStream == null ? null : byteStream.toByteArray());
            }
        }
		
        Data xData = xContext.addNewData();
        for (Id key : data.keySet()) {
            DataItem item = data.get(key);
            Data.DataItem xDataItem = xData.addNewDataItem();

            if (item.type == EValType.USER_CLASS && (item.val instanceof Entity)) // bug !!!
                item.type = EValType.OBJECT;

            xDataItem.setId(key);
            xDataItem.setValType(item.type.getValue());
            xDataItem.setValClass(item.clazz);
            xDataItem.setPersistent(item.persistent);
            xDataItem.setLost(item.lost);

            xDataItem.setRef(false);
            if (item.val == null) {
                xDataItem.setByteArrayValue(null);
                continue;
            }
            
            for (Entry<Object, Id> entry: val2Id.entrySet()) {
                if (entry.getKey() == item.val) {
                    xDataItem.setRef(true);
                    xDataItem.setByteArrayValue(entry.getValue().toString().getBytes());
                    break;
                }
            }
            if (xDataItem.getRef())
                continue;
            
            val2Id.put(item.val, key);
            switch (item.type) {
                case BIN:
		case BOOL:
		case CHAR:
		case DATE_TIME:
		case INT:
		case NUM:
		case STR:					
		case BLOB:					
		case CLOB:					
		case ARR_BIN:
		case ARR_BOOL:
		case ARR_CHAR:
		case ARR_DATE_TIME:
		case ARR_INT:
		case ARR_NUM:
		case ARR_STR:
		case JAVA_TYPE:
		{
                    Object value = item.val;
                    if (value instanceof IKernelEnum)
                        value = ((IKernelEnum)value).getValue();
                    String asStr = SrvValAsStr.toStr(arte, value, item.type);
                    xDataItem.setByteArrayValue(asStr != null && item.persistent ? asStr.getBytes() : null);
                    break;
                }
                case XML:
                {
                    if (item.val instanceof Clob) {
                        String asStr = SrvValAsStr.toStr(arte, item.val, EValType.CLOB);
                        xDataItem.setByteArrayValue(asStr != null && item.persistent ? asStr.getBytes() : null);
                        break;
                    }
                    XmlObject xml = (XmlObject)item.val;
                    if (item.val != null && item.persistent) {
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        try {
                            xml.save(byteStream);
                            byteStream.flush();
                        } catch (IOException e) {
                            throw new RadixError("Unexpected exception: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""), e);
                        }
                        xDataItem.setByteArrayValue(byteStream.toByteArray());
                    } else
                        xDataItem.setByteArrayValue(null);
                    break;
                }
                case OBJECT:
                case PARENT_REF:
                {
                    String asStr = null;
                    if (item.val instanceof Entity) {
                        Entity entity = (Entity)item.val;
                        if (item.persistent)
                            asStr = SrvValAsStr.toStr(arte, entity.getPid(), EValType.OBJECT);
                    }
                    xDataItem.setByteArrayValue(asStr != null ? asStr.getBytes() : null);
                    break;
                }
                case JAVA_CLASS:
                case USER_CLASS:
                default:
                {
                    xDataItem.setLost(!(item.val instanceof Serializable) && item.persistent);
                    if (item.val instanceof Serializable && item.persistent)
                        try {
                            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
                            objectStream.writeObject(item.val);
                            objectStream.flush();
                            xDataItem.setByteArrayValue(byteStream.toByteArray());
                        } catch (IOException e) {
                            xDataItem.setByteArrayValue(null);
                            trace(EEventSeverity.WARNING, "Object " + key + " save error: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""));
                        }
                    else
                        xDataItem.setByteArrayValue(null);
                    break;
                }
            }
        }

        
        AlgoClasses xClasses = xContext.addNewAlgoClasses();
        for (Id key: algoClasses.keySet()) {
            Id val = algoClasses.get(key);
            AlgoClassItem xClass = xClasses.addNewAlgoClassItem();
            xClass.setReferencedClass(key);
            xClass.setWorkingClass(val);
        }
		
        Discriminators xDiscriminators = xContext.addNewDiscriminators();
        for (Id key: discriminators.keySet() ) {
            Integer val = discriminators.get(key);
            DiscriminatorItem xDiscriminator = xDiscriminators.addNewDiscriminatorItem();
            xDiscriminator.setBlockId(key);
            xDiscriminator.setCount(val.intValue());
        }

//        trace(EEventSeverity.DEBUG, "Saved context:\n" + doc.xmlText());
        
        Clob clob = null;
        try {
            clob = getArte().getDbConnection().createTemporaryClob();
            //OutputStream out = clob.setAsciiStream(1L);
            Writer out = clob.setCharacterStream(1L);
            doc.save(out); out.flush(); out.close();
        } catch (SQLException e) {
            throw new DatabaseError("Can't write context: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
        } catch (IOException e) {
            throw new DatabaseError("Can't write context: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
        }
        
        return clob;
    }

    public void setContext(Clob clob) throws IOException, SQLException, XmlException, ClassNotFoundException, EntityObjectNotExistsError {
        ContextDocument doc = ContextDocument.Factory.parse(clob.getCharacterStream());
        Context xContext = doc.getContext();
        lastWaitId = xContext.getLastWaitId();

        Threads xThreads = xContext.getThreads();
        for (ThreadItem xThread: xThreads.getThreadItemList()) {
            Thread t = new Thread();
            t.state = xThread.getState();
            t.scopeId = xThread.getScopeId();
            t.blockId = xThread.getBlockId();
            t.input = xThread.getInput();
            Waits xWaits = xThread.getWaits();
            for (Waits.WaitItem xWait: xWaits.getWaitItemList()) {
                WaitItem w = new WaitItem(null, null, null, true);
                if (xWait.isSetId())
                    w.id = Long.valueOf(xWait.getId());
                if (xWait.isSetStrobId())
                    w.strobId = xWait.getStrobId();
                if (xWait.getTime() != null)
                    w.time = new Timestamp(xWait.getTime().getTimeInMillis());
                if (xWait.isSetClear())
                    w.clear = xWait.getClear();
                else
                    w.clear = true;
                t.waits.add(w);
            }
            AlgoStack xAlgoStack = xThread.getAlgoStack();
            for (AlgoStack.AlgoItem xAlgo: xAlgoStack.getAlgoItemList()) {
                AlgoStackItem asi = new AlgoStackItem(xAlgo.getAlgoId(), xAlgo.getBlockId());
                MergeStack xMergeStack = xAlgo.getMergeStack();
                for (MergeStack.MergeItem xMerge: xMergeStack.getMergeItemList()) {
                    MergeItem merge = new MergeItem(xMerge.getId(), xMerge.getFinish());
                    asi.mergeStack.push(merge);
                }
                t.algoStack.push(asi);
            }
            ExceptionStack xExceptionStack = xThread.getExceptionStack();
            for (ExceptionStack.ExceptionItem xException: xExceptionStack.getExceptionItemList()) {
                if (xException.getByteArrayValue() == null)
                    t.exceptionStack.push(null);
                else {
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(xException.getByteArrayValue());
                    ObjectInputStream objectStream = new ContextInputStream(byteStream);
                    t.exceptionStack.push((Throwable)objectStream.readObject());
                }
            }
            threads.add(t);
        }

        Data xData = xContext.getData();
        for (Data.DataItem xDataItem : xData.getDataItemList()) {
/*
            DataItem item = data.get(xDataItem.getId());
                
            if (item != null) {
                item.lost = xDataItem.getLost();
            } else {
                item = new DataItem(EValType.getForValue(xDataItem.getValType()), xDataItem.getValClass(), null, xDataItem.getPersistent(), xDataItem.getLost());
                data.put(xDataItem.getId(), item);
            }
*/
            Id key = xDataItem.getId();
            DataItem item = new DataItem(EValType.getForValue(xDataItem.getValType()), xDataItem.getValClass(), null, xDataItem.getPersistent(), xDataItem.getLost());
            data.put(key, item);

            if (xDataItem.getRef()) {
                Id id = Id.Factory.loadFrom(new String(xDataItem.getByteArrayValue()));
                if (data.containsKey(id))
                    item.val = data.get(id).val;
                continue;
            }

            if (xDataItem.getByteArrayValue() == null)
                continue;
            
            switch (item.type) {
                case BIN:
                case BOOL:
                case CHAR:
                case DATE_TIME:
                case INT:
                case NUM:
                case STR:
                case BLOB:
                case CLOB:
                case ARR_BIN:
                case ARR_BOOL:
                case ARR_CHAR:
                case ARR_DATE_TIME:
                case ARR_INT:
                case ARR_NUM:
                case ARR_STR:
                case JAVA_TYPE:
                {
                    String asStr = new String(xDataItem.getByteArrayValue());
                    item.val = SrvValAsStr.fromStr(arte, asStr, item.type);
                    break;
                }
                case XML:
                {
                    try {
                        Class<?> clazz = getClassForName(item.clazz + "$Factory");
                        java.lang.reflect.Method m = clazz.getMethod("parse", new Class<?>[] { String.class });
                        item.val = m.invoke(null, new Object[] { new String(xDataItem.getByteArrayValue(), "UTF-8") });
                    } catch (Exception e) {
                        throw new RadixError("Unexpected exception: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""), e);
                    }
                    break;
                }
                case OBJECT:
                case PARENT_REF:
                {
                    String asStr = new String(xDataItem.getByteArrayValue());
                    Pid pid = (Pid)SrvValAsStr.fromStr(arte, asStr, EValType.OBJECT);
                    if (pid != null)
                        try {
                            item.val = getArte().getEntityObject(pid);
                        } catch (EntityObjectNotExistsError e) {
                            item.val = null;
                            trace(EEventSeverity.WARNING, "Object " + key + " not exists (pid = " + pid.toString() + "): " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""));
                        }
                    break;
                }
                case JAVA_CLASS:
                case USER_CLASS:
                default:
                {
                    try {
                        ByteArrayInputStream byteStream = new ByteArrayInputStream(xDataItem.getByteArrayValue());
                        ContextInputStream objectStream = new ContextInputStream(byteStream);
                        item.val = objectStream.readObject();
                    } catch (Exception e) {
                        throw new RadixError("Unexpected exception: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : ""), e);
                    }
                    break;
                }
            }
        }

        AlgoClasses xClasses = xContext.getAlgoClasses();
        for (AlgoClassItem xClassItem: xClasses.getAlgoClassItemList()) {
            algoClasses.put(xClassItem.getReferencedClass(), xClassItem.getWorkingClass());
        }

        Discriminators xDiscriminators = xContext.getDiscriminators();
        for (DiscriminatorItem xDiscriminatorItem: xDiscriminators.getDiscriminatorItemList()) {
            discriminators.put(xDiscriminatorItem.getBlockId(), Integer.valueOf(xDiscriminatorItem.getCount()));
        }
        
//        trace(EEventSeverity.DEBUG, "Restored context:\n" + doc.xmlText());
    }

    public boolean execute() throws Throwable  {
        for (;;) {
            Thread thread = null;
            for (Iterator<Thread> ti = threads.iterator(); ti.hasNext();) {
                Thread t = ti.next();
                if (t.state == Thread.FINISHED) {
                    ti.remove();
                    continue;
                }
                if (t.state == Thread.ACTIVE || t.state == Thread.RESUME) {
                    thread = t;
                    break;
                }
            }
            if (thread == null)
                return threads.isEmpty();

            trace(EEventSeverity.DEBUG, "Start/continue thread execution");
            execute(thread);
            trace(EEventSeverity.DEBUG, "Finish thread execution");
        }
    }

    public boolean resume(Long waitId, Id strobId, Object clientData) {
        for (Thread thread: threads) {
            if (thread.state == Thread.WAITING) {
                for (Iterator<WaitItem> wi = thread.waits.iterator(); wi.hasNext();) {
                    WaitItem w = wi.next();
                    if (Utils.equalsNotNull(w.id, waitId) || Utils.equalsNotNull(w.strobId, strobId) || (simulatedTime != null && w.time != null && !w.time.after(getCurrentTime()))) {
                        thread.state = Thread.RESUME;
                        thread.resumedWaitId = (simulatedTime != null && w.time != null && !w.time.after(getCurrentTime()) ? w.id : waitId);
                        thread.resumedStrobId = strobId;
                        thread.clientData = clientData;
                        if (w.clear)
                            thread.waits.clear();
                        else
                            thread.waits.remove(w);
                        trace(EEventSeverity.DEBUG, "Thread resumed, waitId = " + waitId);
                        return true;
                    }
                }
            }
        }
        trace(EEventSeverity.DEBUG, "No thread to resume, " + (strobId != null ? "strobId " + strobId : "waitId " + waitId));
        return false;
    }
	
    public void check() {
        for (Thread thread: threads)
            thread.check();
    }

    protected void execute(Thread thread) throws Throwable {
        currentThread = thread;
        currentAlgo = thread.getAlgo();
        setException(thread.exceptionStack.empty()? null : thread.exceptionStack.peek());
        for (;;) {
            switch (currentAlgo.getBlockType(thread.blockId)) {
                case SCOPE_BLOCK_TYPE:
                case CATCH_BLOCK_TYPE:
                    thread.scopeId = thread.blockId;
                    thread.input = currentAlgo.getFirstScopeBlockInput(thread.blockId);
                    thread.blockId = currentAlgo.getFirstScopeBlock(thread.blockId);
                    break;
                case EMPTY_BLOCK_TYPE:
                case PROGRAM_BLOCK_TYPE:
                case SWITCH_BLOCK_TYPE:
                case TRACE_BLOCK_TYPE:
                case THROW_BLOCK_TYPE:
                case APP_BLOCK_TYPE:
                case INCLUDE_BLOCK_TYPE:
                    try {
                        int output = currentAlgo.executeBlock(thread.blockId);
                        if (
                            thread.state != Thread.ACTIVE &&
                            thread.state != Thread.RESUME
                        )
                           return;

                        if (currentAlgo.getBlockType(thread.blockId) == INCLUDE_BLOCK_TYPE) {
                            currentAlgo.writeIncludeBlockParameters(thread.blockId);
                            Id algoId = currentAlgo.getIncludeBlockAlgorithm(thread.blockId);
                            currentAlgo = thread.newAlgo(algoId);
                            currentAlgo.beforeStart();
                        } else {
                            thread.input = currentAlgo.getNextBlockInput(thread.blockId, output);
                            thread.blockId = currentAlgo.getNextBlock(thread.blockId, output);
                        }
                    } catch (Throwable e) {
                        //e.printStackTrace();
                        trace(EEventSeverity.DEBUG, "Exception " + e + " in block " + thread.blockId);
                        trace(EEventSeverity.DEBUG, "Exception stack:\n" + getArte().getTrace().exceptionStackToString(e));

                        for (;;) {
                            try {
                                thread.state = Thread.ACTIVE; //TWRBS-10192                                
                                thread.input = 0;
                                thread.blockId = currentAlgo.findCatch(thread.scopeId, e);
                                setException(e);
                                thread.exceptionStack.push(e);
                                break;
                            } catch(Throwable e2) {
                                trace(EEventSeverity.DEBUG,"Exception is not handled at scope " + thread.scopeId);
                                // exclude parent catch blocks to avoid recursive handling
                                for (;;) {
                                    Id scopeId = thread.scopeId;
                                    if (!leaveScope(false, 0))
                                        throw e2;
                                    if (scopeId == null || currentAlgo.getBlockType(scopeId) != CATCH_BLOCK_TYPE)
                                        break;
                                }
                            }
                        }
/*                        
//                        for (;;) {
                            try {
                                thread.input = 0;
                                thread.blockId = currentAlgo.findCatch(thread.scopeId, e);
                                setException(e);
                                thread.exceptionStack.push(e);
//                                break;
                            } catch(Throwable e2) {
                                trace(EEventSeverity.DEBUG,"Exception is not handled at scope " + thread.scopeId);
//                                if (!leaveScope(false, 0))
                                    throw e2;
                            }
//                        }
*/
                    }
                    break;
                case TERMINATE_BLOCK_TYPE:
                    for (Iterator<Thread> ti = threads.iterator(); ti.hasNext();)
                        ti.next().state = Thread.FINISHED;
                    return;
                case FINISH_BLOCK_TYPE:
                    while(leaveAlgo(true, 0)) {;}
                    return;
                case RETURN_BLOCK_TYPE:
                    int output = currentAlgo.getReturn(thread.blockId);
                    if (!leaveScope(true, output))
                        return;
                    break;
                case FORK_BLOCK_TYPE:
                    Id[] nextBlocks = currentAlgo.getForkNextBlocks(thread.blockId);
                    int[] nextBlocksInput = currentAlgo.getForkNextBlocksInput(thread.blockId);
                    thread.registerMerge(currentAlgo.getMergeBlock(thread.blockId));
                    thread.blockId = nextBlocks[0];
                    thread.input = nextBlocksInput[0];
                    for (int i = 1; i < nextBlocks.length; i++) {
                        Thread t = new Thread(thread);
                        t.blockId = nextBlocks[i];
                        t.input = nextBlocksInput[i];
                        threads.add(t);
                    }
                    break;
                case MERGE_BLOCK_TYPE:
                    if (leaveThread()) {
                        thread.state = Thread.FINISHED;
                        return;
                    }
                    Id blockId = thread.blockId;
                    int input = thread.input;
                    thread.blockId = currentAlgo.getNextBlock(blockId, input);
                    thread.input = currentAlgo.getNextBlockInput(blockId, input);
                    break;
                default:
                    throw new AlgorithmDefinitionError("Unknown type of block " + thread.blockId);
            }

//            while (thread.blockId == null) // ???
//                if (!leaveScope(true, 0))
//                    return;
        }
    }
	
    private boolean leaveThread() {
        Stack<MergeItem> mergeStack = currentThread.algoStack.peek().mergeStack;
        while (!mergeStack.empty()) {
            MergeItem merge = mergeStack.peek();
            if (Utils.equals(currentAlgo.getBlockScope(merge.id), currentThread.scopeId)) {
                mergeStack.pop();
                if (merge.finish)
                    return true;
                int m = currentAlgo.getMergeBlockM(merge.id);
                int count = getDiscriminator(merge.id) + 1;
                if (count == m) {
                    setDiscriminator(merge.id, 0);
                    for (Thread t: threads)
                        if (t != currentThread && t.state != Thread.FINISHED) {
                            AlgoStackItem asi = t.getByPath(currentThread.getPath());
                            if (asi != null)
                                for (MergeItem mi: asi.mergeStack) {
                                    if (Utils.equals(mi.id, merge.id))
                                        mi.finish = true;
                                }
                        }
                    return false;
                } else {
                    setDiscriminator(merge.id, count);
                    return true;
                }
            } else
                break;
        }
/*
        while (!currentThread.mergeStack.empty()) {
            MergeItem merge = currentThread.mergeStack.peek();
            Id mergeId = parseId(merge.id);
            String mergePath = parsePath(merge.id);
            if (Utils.equals(mergePath, currentThread.getPath()) && Utils.equals(currentAlgo.getBlockScope(mergeId), currentThread.scopeId)) {
                currentThread.mergeStack.pop();
                if (!merge.syncWithOther)
                    return true;
                int m = currentAlgo.getMergeBlockM(mergeId);
                int count = getDiscriminator(mergeId) + 1;
                if (count == m) {
                    setDiscriminator(mergeId, 0);
                    for (Thread t: threads)
                        if (t != currentThread && t.state != Thread.FINISHED)
                            for (MergeItem mrg: t.mergeStack) {
                                if (Utils.equals(mrg.id, merge.id))
                                    mrg.syncWithOther = false;
                            }
                            return false;
                } else {
                    setDiscriminator(mergeId, count);
                    return true;
                }
            } else
                break;
        }
*/
        return false;
    }

    private boolean leaveScope(boolean toRestoreException,  int output) throws AppException {
        if (currentThread.scopeId == null)
            return leaveAlgo(toRestoreException, output);
        if (leaveThread()) {
            currentThread.state = Thread.FINISHED;
            return false;
        }
        if (currentAlgo.getBlockType(currentThread.scopeId) == CATCH_BLOCK_TYPE) {
            currentThread.exceptionStack.pop();
            if (toRestoreException)
                setException(currentThread.exceptionStack.empty() ? null : currentThread.exceptionStack.peek());
        }
        currentThread.blockId = currentAlgo.getNextBlock(currentThread.scopeId, output);
        currentThread.input = currentAlgo.getNextBlockInput(currentThread.scopeId, output);
        currentThread.scopeId = currentAlgo.getBlockScope(currentThread.scopeId);
        return true;
    }

    private boolean leaveAlgo(boolean toRestoreException, int output) throws AppException {
        while (currentThread.scopeId != null) {
            if (!leaveScope(toRestoreException, 0))
                return false;
        }
        if (leaveThread()) {
            currentThread.state = Thread.FINISHED;
            return false;
        }
        if (currentThread.algoStack.size() == 1) {
            currentThread.state = Thread.FINISHED;
            return false;
        }
        currentAlgo.beforeFinish();
        Id blockId = currentThread.algoStack.peek().blockId;
        currentThread.algoStack.pop();
        currentAlgo = currentThread.getAlgo();
        currentAlgo.readIncludeBlockParameters(blockId);
        currentThread.scopeId = currentAlgo.getBlockScope(blockId);
        currentThread.blockId = currentAlgo.getNextBlock(blockId, output);
        currentThread.input = currentAlgo.getNextBlockInput(blockId, output);
        return true;
    }
    
    public Algorithm getAlgo(Id algoId) {
        Id workingId = algoClasses.get(algoId);
        if (workingId == null)
            throw new AlgorithmDefinitionError("Not registered algorithm class " + algoId);
        Algorithm algo = (Algorithm)getArte().newObject(workingId);
        algo.setExecutor(this);
        return algo;
    }

    public void registerData(String path, Id id, EValType valType, String clazz, Object val, boolean persistent) {
        data.put(Id.Factory.append(id, path), new DataItem(valType, clazz, val, persistent));
    }

    public void registerAlgo(String path, Id referencedId, Id workingId) {
        algoClasses.put(referencedId, workingId);
        getAlgo(referencedId).registerInExecutor(this, path);
    }

    public EValType getDataType(Id id) {
        id = getCurrentId(id);
        if (!data.containsKey(id))
            throw new AlgorithmDefinitionError("Data object with id = #" + id + " not found");
        return data.get(id).type;
    }

    public Object getData(Id id) {
        id = getCurrentId(id);
        if (!data.containsKey(id))
            throw new AlgorithmDefinitionError("Data object with id = #" + id + " not found");
        if (data.get(id).lost)
            throw new RadixError("Data object with id = #" + id + " was lost after context saving");
        if (data.get(id).val instanceof IKernelEnum)
            return ((IKernelEnum)data.get(id).val).getValue();
        return data.get(id).val;
    }

    public void setData(Id id, Object val) {
        id = getCurrentId(id);
        if (!data.containsKey(id))
            throw new AlgorithmDefinitionError("Data object with id = #" + id + " not found");
        data.get(id).lost = false;
        data.get(id).val = val;
    }

    public EValType getPropertyType(String name) {
        return getDataType(currentAlgo.getBlockPropId(currentThread.blockId, name));
    }

    public Object getProperty(String name) {
        return getData(currentAlgo.getBlockPropId(currentThread.blockId, name));
    }

    public void setProperty(String name, Object val) {
        setData(currentAlgo.getBlockPropId(currentThread.blockId, name), val);
    }

    public Id getPropertyId(String name) {
        return currentAlgo.getBlockPropId(currentThread.blockId, name);
    }
	
    public int getDiscriminator(Id mergeId) {
        Integer count = discriminators.get(getCurrentId(mergeId));
        return count == null ? 0 : count;
    }

    public void setDiscriminator(Id mergeId, int count) {
        discriminators.put(getCurrentId(mergeId), Integer.valueOf(count));
    }

    public Throwable getException() {
        return (Throwable)data.get(AdsAlgoClassDef.EXCEPTION_VAR_ID).val;
    }

    public void setException(Throwable e) {
        data.get(AdsAlgoClassDef.EXCEPTION_VAR_ID).val = e;
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    public Algorithm getCurrentAlgo() {
        return currentAlgo;
    }

    public Timestamp getCurrentTime() {
        return simulatedTime == null ? new Timestamp(System.currentTimeMillis()) : simulatedTime;
    }
    
    public boolean isSimulated() {
        return simulatedTime != null;
    }

    public Timestamp getCurrentTime(long addSec) {
        return new Timestamp(getCurrentTime().getTime() + addSec*1000);
    }

    public Id getMainAlgoId() {
        if (getCurrentThread() != null)
            return getCurrentThread().algoStack.get(0).algoId;
        return threads.get(0).algoStack.get(0).algoId;
    }

    private Id getCurrentId(final Id id) {
        String path = "";
        final Thread thread = getCurrentThread();
        if (thread != null)
            path = thread.getPath();
        return Id.Factory.append(id, path);
    }

    public final void trace(EEventSeverity severity, String mess) {
        getArte().getTrace().put(severity, mess, EEventSource.ALGO_EXECUTOR);
    }
}