package org.radixware.kernel.designer.common.dialogs.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

public class StackAnalyzer implements Runnable {
    private static final ItemInfo EMPTY_INFO = new ItemInfo(null, -1, null, null);
    private static final Pattern RADIX_SOURCE_LINE = Pattern.compile(".+\\.ads\\.mdl[A-Z0-9_]{26}\\..+\\(.+\\.java\\:[0-9]+\\)");
    private static final Pattern RADIX_ID = Pattern.compile("^[a-z]{3,6}[A-Z0-9_]{26}$");
    RadixEventSource<StackChangeListener, StackEvent> source = new RadixEventSource<>();
    
    private Branch branch;
    private String stack;
    private ProgressHandle progressHandle;
    private Canceller cancellable;
    private List<String> stackList;
    private boolean needParse = true;
    

    
    public StackAnalyzer(){
    }
    
    public void open(final Branch branch, final String traceText) {
        if (this.branch != branch) {
            this.branch = branch;
            needParse = true;
        }
        if (!Utils.equals(this.stack, traceText)){
            this.stack = traceText;
            needParse = true;
        }
    }
    
    private void parseStackTrace() {

        if (stack == null || stack.isEmpty()) {
            stackList = Collections.EMPTY_LIST;
            return;
        }

        final String[] split = stack.split("(\\n|\\s)at\\s|\\n");
        stackList = new ArrayList<>();

        for (final String str : split) {
            if (str != null && !str.trim().isEmpty()) {
                stackList.add(str.trim());
            }
        }
    }

    @Override
    public void run() {
        source.fireEvent(new StackEvent(StackEventType.START));
        cancellable = new Canceller();
        progressHandle = ProgressHandleFactory.createHandle("Analyze Stack...", cancellable);
        defsMap.clear();
        cacheMap.clear();
        progressHandle.switchToIndeterminate();
        progressHandle.start();
        try {
            if (needParse || stackList == null || stackList.isEmpty()) {
                parseStackTrace();
            }
            if (!stackList.isEmpty()){
                progressHandle.switchToDeterminate(stackList.size());
            }
            int i = 0;
            for (final String line : stackList) {
                if (cancellable.isCancele) {
                    break;
                }
                if (line.contains("access$")) {
                    progressHandle.progress(i++);
                    continue;
                }

                final StackTreeModelItem item = new StackTreeModelItem();
                item.text = line;
                item.isStackString = true;
                ItemInfo info = cacheMap.get(line);
                if (info == null) {
                    info = getLineNumber(line, branch, cancellable);
                    if (cancellable.isCancele) {
                        break;
                    }
                    cacheMap.put(line, info);
                }
                if (info != null) {
                    item.lineNumber = info.line;
                    item.environment = info.environment;
                    item.defs.addAll(info.defs);
                    item.locations.addAll(info.locations.values());
                }
                
                progressHandle.progress(i++);
                source.fireEvent(new StackEvent(item));
            }
        } finally {
            progressHandle.finish();
            source.fireEvent(new StackEvent(StackEventType.STOP));
        }
    }
    
    public static interface StackChangeListener extends IRadixEventListener<StackEvent> {
    }
    
    public enum StackEventType {
        START, STOP, PROCESS
    }
    
    public static class StackEvent extends RadixEvent {
        final StackEventType type;
        final StackTreeModelItem stackItem;

        StackEvent(StackEventType type) {
            this.type = type;
            this.stackItem = null;
        }
        
        public StackEvent(StackTreeModelItem stackItems) {
            this.stackItem = stackItems;
            type = StackEventType.PROCESS;
        }

        public StackTreeModelItem getStackItem() {
            return stackItem;
        }

        public StackEventType getType() {
            return type;
        }
    }
    
    private final Map<Id, List<Definition>> defsMap = new HashMap<>();
    private final Map<String, ItemInfo> cacheMap = new HashMap<>();
    
    private ItemInfo getLineNumber(String string, Branch branch, Canceller c) {

        if (!RADIX_SOURCE_LINE.matcher(string).matches()) {
            return EMPTY_INFO;
        }
        final Map<RadixObject, SrcPositionLocator.SrcLocation> defs = new HashMap<>();
        final int lparen = string.indexOf("(");
        final int javaMarker = string.indexOf(".java", lparen + 1);
        final int colon = string.indexOf(":", javaMarker + 1);
        final int rparen = string.indexOf(")", colon + 1);
        final int moduleIdIndex = string.indexOf(".mdl");

        final String lineNumberStr = string.substring(colon + 1, rparen);
        final int lineNumber = Integer.decode(lineNumberStr);
        try {
            ERuntimeEnvironmentType environment = null;
            Definition targetDef = null;
            Set<Definition> targetDefs = new HashSet<>();
            if (branch == null) {
                return EMPTY_INFO;
            }
            if (moduleIdIndex < 0 || lparen < 0 || moduleIdIndex > lparen) {
                return EMPTY_INFO;
            }
            
            if (cancellable.isCancele) {
                return EMPTY_INFO;
            }

            final String invokedMethodStr = string.substring(moduleIdIndex + 1, lparen);
            final String[] ids = invokedMethodStr.split("\\.");
            if (ids.length > 1) {
                final Id moduleId = Id.Factory.loadFrom(ids[0]);
                final String envSelector = ids[1];
                final String asStr = ids[ids.length - 1];
                final Id methodId = Id.Factory.loadFrom(asStr);

                final boolean isId = RADIX_ID.matcher(asStr).matches();

                environment = getEnvironment(envSelector);
                
                if (c.isCancele) {
                    return EMPTY_INFO;
                }
                if (ids.length > 2) {
                    String definitionIdCandidate = ids[2];
                    final int dollarIdx = definitionIdCandidate.indexOf("$");
                    if (dollarIdx >= 0) {
                        definitionIdCandidate = definitionIdCandidate.substring(0, dollarIdx);
                    }
                    if (definitionIdCandidate.length() > 3) {
                        final Id definitionId = Id.Factory.loadFrom(definitionIdCandidate);
                        List<Definition> classCandidats = defsMap.get(definitionId);
                        if (classCandidats == null || classCandidats.isEmpty()){
                            List<Definition> modules = defsMap.get(moduleId);
                            if (modules == null || modules.isEmpty()) {
                                modules = new LinkedList<>();
                                if (cancellable.isCancele) {
                                    return EMPTY_INFO;
                                }
                                findModules(modules, moduleId);
                                defsMap.put(moduleId, modules);
                            }
                            if (cancellable.isCancele) {
                                return EMPTY_INFO;
                            }
                            final boolean isUserMode = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;
                            classCandidats = new LinkedList<>();
                            for (Definition module : modules) {
                                if (module instanceof Module) {
                                    if (cancellable.isCancele) {
                                        return EMPTY_INFO;
                                    }
                                    findClass(classCandidats, definitionId, (Module) module, isUserMode, string, lparen, javaMarker);
                                }
                            }
                            defsMap.put(definitionId, classCandidats);
                            if (c.isCancele) {
                                return EMPTY_INFO;
                            }
                        }
                        for (Definition def : classCandidats) {
                            if (c.isCancele) {
                                return EMPTY_INFO;
                            }
                            if (isId) {
                                targetDef = (Definition) def.find(new VisitorProvider() {
                                    @Override
                                    public boolean isTarget(RadixObject radixObject) {
                                        return radixObject instanceof Definition && ((Definition) radixObject).getId() == methodId;
                                    }
                                });
                            }

                            if (def instanceof IJavaSource && !(def instanceof IXmlDefinition)) {
                                JavaSourceSupport.CodeWriter writer = ((IJavaSource) def).getJavaSourceSupport().getCodeWriter(JavaSourceSupport.UsagePurpose.getPurpose(environment, JavaSourceSupport.CodeType.EXCUTABLE));
                                final char[] src;
                                if (writer != null) {
                                    CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
                                    if (writer.writeCode(printer)) {
                                        src = printer.getContents();
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                                if (c.isCancele) {
                                    return EMPTY_INFO;
                                }
                                final SrcPositionLocator locator = SrcPositionLocator.Factory.newInstance((IJavaSource) def, src);
                                final int position = JavaSourceSupport.lineNumber2Position(src, lineNumber);
                                final SrcPositionLocator.SrcLocation loc = locator.calc(position, position);

                                if (targetDef != null) {
                                    targetDefs.add(targetDef);
                                    
                                    if (loc.getRadixObject() == null || loc.getRadixObject().getOwnerDefinition() == null) {
                                        continue;
                                    }

                                    final Definition locDef = loc.getRadixObject().getOwnerDefinition();
                                    if (locDef.getId() != methodId) {
                                        continue;
                                    }
                                }
                                if (isId && loc.getRadixObject() != null && loc.getRadixObject().getOwnerDefinition() != null && loc.getRadixObject().getOwnerDefinition().getId() != methodId && asStr.startsWith(loc.getRadixObject().getOwnerDefinition().getId().getPrefix().getValue())) {
                                    continue;
                                }

                                if (!defs.containsKey(def)) {
                                    defs.put(loc.getRadixObject(), loc);
                                }

                                if (!isId || targetDefs.isEmpty()) {
                                    break;
                                }
                            }
                        }
                    }
                }

            }
            ItemInfo info = new ItemInfo(environment, lineNumber, targetDefs, defs);
            return info;
        } catch (NumberFormatException | NoConstItemWithSuchValueError e) {
            final String msg = String.format(e.getMessage() + " => %s", string);
            Logger.getLogger(StackTraceList.class.getName()).log(Level.INFO, msg);
            return EMPTY_INFO;
        }
    }
    
    private void findModules(final List<Definition> modulesList, final Id moduleId) {
        branch.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                modulesList.add((Module) radixObject);
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Module) {
                    String finalId = String.valueOf(JavaSourceSupport.getModulePackageName((Module) radixObject));
                    Id id = Id.Factory.loadFrom(finalId);
                    if (id == moduleId) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    private void findClass(final List<Definition> defsList, final  Id definitionId, Module m, final boolean isUserMode, String stackLine, final int lparen, final int javaMarker) {
        while (m != null) {
            Definition def = (AdsDefinition) m.find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsUserReportClassDef) {
                        AdsUserReportClassDef ur = (AdsUserReportClassDef) radixObject;
                        //load user report
                        if (isUserMode) {
                            UserReport userReport = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(ur.getId());
                            if (userReport != null) {
                                userReport.getVersions().getCurrent().findReportDefinition();
                            }
                        }
                        if (ur.getRuntimeId() == definitionId) {
                            return true;
                        }
                    }
                    return radixObject instanceof Definition && ((Definition) radixObject).getId() == definitionId;
                }
            });
            if (isUserMode && def == null) {
                String className = stackLine.substring(lparen + 1, javaMarker);
                if (RADIX_ID.matcher(className).matches()) {
                    final Id classId = Id.Factory.loadFrom(className);
                    def = (AdsDefinition) m.find(new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof Definition && ((Definition) radixObject).getId() == classId;
                        }
                    });
                }
            }
            if (def != null) {
                defsList.add(def);
                break;
            }
            m = m.findOverwritten();
        }

        }

    private static ERuntimeEnvironmentType getEnvironment(String name) {
        if ("common_client".equals(name)) {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }

        return ERuntimeEnvironmentType.getForValue(name);
    }
    
    public void addEventListener(StackChangeListener listener) {
        source.addEventListener(listener);
    }
    
    public void removeEventListener(StackChangeListener listener) {
        source.removeEventListener(listener);
    }
    
    private static class ItemInfo {

        final ERuntimeEnvironmentType environment;
        final int line;
        final Set<Definition> defs;
        final Map<RadixObject, SrcPositionLocator.SrcLocation> locations = new HashMap<>();

        public ItemInfo(ERuntimeEnvironmentType environment, int line, Set<Definition> defs, final Map<RadixObject, SrcPositionLocator.SrcLocation> locations) {
            this.environment = environment;
            this.line = line;
            if (defs == null) {
                this.defs = new HashSet<>();
            } else {
                this.defs = new HashSet<>(defs);
            }
            if (locations != null) {
                this.locations.putAll(locations);
            }
        }
        
    }
    
    private static class Canceller implements Cancellable {
        boolean isCancele = false;

        @Override
        public boolean cancel() {
            isCancele = true;
            return true;
        }
    };

}
