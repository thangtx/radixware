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
package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOp;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.stack.StackTraceList;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemoverManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public class DialogUtils {

    private static boolean detailBox(String message, Throwable details, int messageType, JComponent additionalComponent) {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        final JOptionPane optionPane = new JOptionPane(message, messageType);
        final JPanel labelPanel = (JPanel) optionPane.getComponent(0);  // hack - get label and icon without buttons
        mainPanel.add(labelPanel, BorderLayout.NORTH);

        final String detailsText = ExceptionTextFormatter.throwableToString(details);
        final JTextArea edDetails = new JTextArea(detailsText);
        edDetails.setEditable(false);
        edDetails.setFont(new Font("Monospaced", Font.PLAIN, edDetails.getFont().getSize() + 1));
        edDetails.setForeground(UIManager.getColor("Label.foreground"));
        edDetails.setBackground(UIManager.getColor("Label.background"));

        final JScrollPane detailsPanel = new JScrollPane(edDetails);
        detailsPanel.setVisible(false);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        if (additionalComponent != null) {
            mainPanel.add(additionalComponent, BorderLayout.SOUTH);
        }

        final JButton btDetails = new JButton();
        Mnemonics.setLocalizedText(btDetails, "&Show Details");

        final DialogDescriptor descriptor = new DialogDescriptor(mainPanel, null);
        descriptor.setAdditionalOptions(new Object[]{btDetails});
        descriptor.setClosingOptions(new Object[]{DialogDescriptor.YES_OPTION, DialogDescriptor.NO_OPTION, DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION});

        switch (messageType) {
            case DialogDescriptor.QUESTION_MESSAGE:
                descriptor.setOptionType(DialogDescriptor.YES_NO_OPTION);
                descriptor.setTitle("Confirmation");
                break;
            default:
                descriptor.setOptions(new Object[]{DialogDescriptor.OK_OPTION});
                descriptor.setTitle("Error");
                break;
        }

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        final Dimension initialSize = dialog.getSize();
        initialSize.width = Math.max(initialSize.width, 400);
        dialog.setMinimumSize(initialSize);
        dialog.setResizable(false);

        btDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsPanel.setVisible(!detailsPanel.isVisible());
                if (detailsPanel.isVisible()) {
                    dialog.setSize(initialSize.width, initialSize.height + 140);
                    dialog.setResizable(true);
                    dialog.setMinimumSize(new Dimension(initialSize.width, initialSize.height + 40));
                    Mnemonics.setLocalizedText(btDetails, "&Hide Details");
                } else {
                    dialog.setMinimumSize(initialSize);
                    dialog.setSize(initialSize);
                    dialog.setResizable(false);
                    Mnemonics.setLocalizedText(btDetails, "&Show Details");
                }
            }
        });

        dialog.setVisible(true);


        final Object value = descriptor.getValue();
        return value.equals(DialogDescriptor.YES_OPTION) || value.equals(DialogDescriptor.OK_OPTION);
    }

    /**
     * Display error message in modal dialog with details.
     */
    public static void messageError(Throwable exception) {
        String message = exception.getLocalizedMessage();
        if (message == null || message.isEmpty()) { // NullPointerException
            message = exception.getClass().getSimpleName();
        }

        detailBox(message, exception, DialogDescriptor.ERROR_MESSAGE, null /*
                 * additionalComponent
                 */);
    }

    /**
     * Display error message in modal dialog.
     */
    public static void messageError(String message) {
        NotifyDescriptor d = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(d);
    }

    /**
     * Display error message with Ignore/Cancel choice in modal dialog. Returns
     * true if Ignore option was selected
     */
    public static boolean messageErrorWithIgnore(String message) {
        String ignore = "Ignore";
        String cancel = "Cancel";
        NotifyDescriptor d = new NotifyDescriptor(message, "Error", 0, NotifyDescriptor.ERROR_MESSAGE, new Object[]{ignore, cancel}, cancel);
        return ignore.equals(DialogDisplayer.getDefault().notify(d));
    }

    /**
     * Display some information in modal dialog.
     */
    public static void messageInformation(String message) {
        final NotifyDescriptor d = new NotifyDescriptor.Message(message, NotifyDescriptor.INFORMATION_MESSAGE);
        DialogDisplayer.getDefault().notify(d);
    }

    /**
     * Display confirmation message in modal dialog.
     *
     * @return true is YES button presed, false overwise.
     */
    public static boolean messageConfirmation(String message) {
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(message, NotifyDescriptor.Confirmation.YES_NO_OPTION);
        return DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.Confirmation.YES_OPTION;
    }

    /**
     * Display confirmation message in modal dialog.
     *
     * @return true is YES button presed, false overwise.
     */
    public static boolean messageConfirmation(String message, int type) {
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(message, NotifyDescriptor.Confirmation.YES_NO_OPTION, type);
        return DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.Confirmation.YES_OPTION;
    }

    /**
     * Display confirmation message in modal dialog with details. For example:
     * 'Unable to load definition, try again?'
     *
     * @return true is YES button presed, false overwise.
     */
    public static boolean messageConfirmation(String message, Throwable details, final JComponent additionalComponent) {
        return detailBox(message, details, DialogDescriptor.QUESTION_MESSAGE, additionalComponent);
    }

    public static JMenu createMenu(Action[] actions, String name) {
        JMenu menu = new JMenu(name);
        Mnemonics.setLocalizedText(menu, name);

        for (Action action : actions) {
            if (action == null) {
                menu.addSeparator();
            } else {
                JMenuItem menuItem = createMenuItem(action);
                menu.add(menuItem);
                menuItem.setIcon(null);
            }
        }

        return menu;
    }

    public static JMenuItem createMenuItem(Action action) {
        final JMenuItem item = new JMenuItem(action);
        Mnemonics.setLocalizedText(item, (String) action.getValue(Action.NAME));
        return item;
    }

    public static TopComponent findTopComponent(Component context) {
        for (Component component = context; component != null; component = component.getParent()) {
            if (component instanceof TopComponent) {
                return (TopComponent) component;
            }
        }
        return null;
    }

    public static JPopupMenu createPopupMenu(Component context) {
        final TopComponent tc = findTopComponent(context);
        final Node[] nodes = tc.getActivatedNodes();
        final Action[] actions = NodeOp.findActions(nodes);
        final JPopupMenu popupMenu = Utilities.actionsToPopup(actions, context);
        return popupMenu;
    }

    /**
     * Open file in Netbeans. Ask to create it if doesn't exist. Show error
     * dialog if any error occured.
     *
     * @return true if sussesfull, false otherwise (file not found, not
     * supported, etc).
     */
    public static boolean editFile(final File file) {
        final OpenCookie openCookie = getOpenCookie(file);
        if (openCookie == null) {
            DialogUtils.messageError("Unable to open file '" + file.getPath() + "' in editor: unsupported format.");
            return false;
        }

        openCookie.open();

        return true;
    }

    private static OpenCookie getOpenCookie(final File file) {
        if (file.isDirectory()) {
            DialogUtils.messageError("Unable to edit file '" + file.getAbsolutePath() + "' because this is directory.");
            return null;
        }

        if (!file.exists()) {
            if (!DialogUtils.messageConfirmation("File '" + file.getAbsolutePath() + "' doesn't exist, create it?")) {
                return null;
            }
            try {
                file.createNewFile();
            } catch (IOException cause) {
                DialogUtils.messageError(cause);
                return null;
            }
        }

        final FileObject fileObject = RadixFileUtil.toFileObject(file);
        final DataObject dataObject;
        try {
            dataObject = DataObject.find(fileObject);
        } catch (DataObjectNotFoundException cause) {
            final RadixError error = new RadixError("Unable to open file '" + file.getPath() + "' in editor: unsupported format.", cause);
            DialogUtils.messageError(error);
            return null;
        }

        return dataObject.getCookie(OpenCookie.class);
    }

    private static int calcCaretPosition(Document document, int lineNumber) {
        if (document != null) {
            final Element[] elements = document.getRootElements();

            if (elements.length > 0 && elements[0].getElementCount() >= lineNumber) {
                return elements[0].getElement(lineNumber - 1).getStartOffset();
            }
        }
        return -1;
    }

    private static boolean editJavaFile(final File file, int lineNumber) {
        final OpenCookie openCookie = getOpenCookie(file);
        if (openCookie != null) {
            if (openCookie instanceof CloneableEditorSupport) {
                final CloneableEditorSupport support = (CloneableEditorSupport) openCookie;
                final JEditorPane[] openedPanes = support.getOpenedPanes();

                if (openedPanes.length > 0 && lineNumber > 0) {
                    final int caretPosition = calcCaretPosition(openedPanes[0].getDocument(), lineNumber);
                    if (caretPosition != -1) {
                        openedPanes[0].setCaretPosition(caretPosition);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static boolean editJavaFile(String filename, final char[] content, int lineNumber) {
        try {
            ;
            final DummyFileObject obj = new DummyFileObject(filename, "java", new String(content).getBytes("UTF-8"));
            DataObject dataObject = DataObject.find(obj);
            OpenCookie openCookie = dataObject.getCookie(OpenCookie.class);
            if (openCookie != null) {
                openCookie.open();
            }
//            if (openCookie instanceof CloneableEditorSupport) {
//                final CloneableEditorSupport support = (CloneableEditorSupport) openCookie;
//                final JEditorPane[] openedPanes = support.getOpenedPanes();
//
//             
//                if (openedPanes.length > 0 && lineNumber > 0) {
//                    final int caretPosition = calcCaretPosition(openedPanes[0].getDocument(), lineNumber);
//                    if (caretPosition != -1) {
//                        openedPanes[0].setCaretPosition(caretPosition);
//                    }
//                }
//            }
            return true;
        } catch (UnsupportedEncodingException | DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        return false;
    }

    private static IJavaSource findTypeSource(RadixObject obj, ERuntimeEnvironmentType env) {
        for (RadixObject o = obj; o != null; o = o.getContainer()) {
            if (o instanceof IJavaSource) {
                if (((IJavaSource) o).getJavaSourceSupport().isSeparateFilesRequired(env)) {
                    return (IJavaSource) o;
                }
            }
        }
        return null;
    }

    public static void viewSource(RadixObject object, ERuntimeEnvironmentType environment, CodeType codeType, int lineNumber) {
        final IJavaSource source = findTypeSource(object, environment);

        if (source != null && codeType != null) {
            final CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
            final JavaSourceSupport support = source.getJavaSourceSupport();
            if (support.getCodeWriter(JavaSourceSupport.UsagePurpose.getPurpose(environment, codeType)).writeCode(printer)) {
                DialogUtils.editJavaFile(object.getQualifiedName() + "(" + JavaSourceSupport.UsagePurpose.getPurpose(environment, codeType).toString() + ")", printer.getContents(), lineNumber);
            }
//            try {
//                if (support.getSharedData() == null) {
//                    support.setSharedData(new JavaSourceSupport.SharedData());
//                }
//                Make.SessionDataDir sddir = support.getSharedData().findItemByClass(Make.SessionDataDir.class);
//                if (sddir == null) {
//                    sddir = new Make.SessionDataDir();
//                    support.getSharedData().registerItemByClass(sddir);
//                }
//
////                final File[] sourceFiles = javaFileSupport.writePackageContent(support.getSourceFileWriter(), false, codeType, new IProblemHandler() {
////                    @Override
////                    public void accept(RadixProblem problem) {
////                    }
////                });
////                if (sourceFiles.length == 0) {
////                    DialogUtils.messageInformation("No files found");
////                    return;
////                }
////                support.getSharedData().cleanup();
////                final File file = sourceFiles[0];
////                DialogUtils.editJavaFile(file, lineNumber);
//            } catch (IOException e) {
//                Logger.getLogger(StackTraceList.class.getName()).log(Level.INFO, null, e);
//            }
        } else {
            Logger.getLogger(DialogUtils.class.getName()).log(Level.INFO, "Source of object is not found");
        }
    }

    public static void goToObject(final RadixObject radixObject) {
        final OpenInfo openInfo = new OpenInfo(radixObject);
        goToObject(radixObject, openInfo);
    }

    public static void goToObject(final RadixObject radixObject, OpenInfo openInfo) {
        NodesManager.selectInProjects(radixObject);
        EditorsManager.getDefault().open(radixObject, openInfo);
    }

    public static void showText(final String text, final String name, final String ext) {
        try {
            final FileSystem fileSystem = FileUtil.createMemoryFileSystem();
            final FileObject fileObject = fileSystem.getRoot().createData(name.replace(' ', '_'), ext);

            final byte[] buffer = text.getBytes(Charset.defaultCharset());

            final FileLock lock = fileObject.lock();
            try {
                final OutputStream stream = fileObject.getOutputStream(lock);
                stream.write(buffer);
                stream.close();
            } finally {
                lock.releaseLock();
            }

            final DataObject dataObject = DataObject.find(fileObject);
            final OpenCookie cookie = dataObject.getCookie(OpenCookie.class);
            cookie.open();
        } catch (IOException cause) {
            throw new RadixError("Unable to view text.", cause);
        }
    }

    private static void deleteOnWriteAccess(final RadixObject radixObject) {
        RadixMutex.writeAccess(new Runnable() {
            @Override
            public void run() {

                ObjectRemoverManager.newObjectRemover(radixObject).removeObject();
//                if (radixObject.isInBranch() && radixObject.canDelete()) {
//                    radixObject.delete();
//                }
            }
        });
    }

    private static String getSaveableChildrenTypeTitles(Collection<? extends RadixObject> context) {
        final Set<RadixObject> radixObjects = RadixObjectsUtils.collectAllInside(context, VisitorProviderFactory.createDefaultVisitorProvider());
        final Set<String> typeTitles = new HashSet<String>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject.isSaveable() && !(radixObject instanceof AdsLocalizingBundleDef) && !context.contains(radixObject)) {
                final String plural = radixObject.getTypesTitle();
                if (!typeTitles.contains(plural)) {
                    final String singular = radixObject.getTypeTitle();
                    if (typeTitles.contains(singular)) {
                        typeTitles.remove(singular);
                        typeTitles.add(plural);
                    } else {
                        typeTitles.add(singular);
                    }
                }
            }
        }
        final List<String> list = new ArrayList<String>(typeTitles);
        Collections.sort(list);
        boolean first = true;
        final StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String typeTitle : list) {
            if (first) {
                first = false;
            } else {
                if (i == 3) {
                    sb.append(",\n");
                    i = 0;
                } else {
                    sb.append(", ");
                    i++;
                }
            }
            sb.append(typeTitle);
        }
        return sb.toString();
    }

    public static Future<Boolean> deleteObject(final RadixObject radixObject) {
        return deleteObjects(Collections.singletonList(radixObject));
    }
    private static final ExecutorService removerService = Executors.newFixedThreadPool(1);

    private static Future<Boolean> createFalse() {
        return new Future<Boolean>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public Boolean get() throws InterruptedException, ExecutionException {
                return false;
            }

            @Override
            public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return false;
            }
        };
    }

    public static Future<Boolean> deleteObjects(final List<? extends RadixObject> radixObjects) {
        if (radixObjects.isEmpty()) {
            return createFalse();

        }

        if (radixObjects.size() == 1) {
            final RadixObject radixObject = radixObjects.get(0);
            if (!DialogUtils.messageConfirmation("Delete " + radixObject.getTypeTitle() + " '" + radixObject.getQualifiedName() + "'?")) {
                return createFalse();
            }
        } else {
            final String commonTypeTitle = RadixObjectsUtils.getCommonTypeTitle(radixObjects).toLowerCase();
            if (!DialogUtils.messageConfirmation("Delete " + radixObjects.size() + " " + commonTypeTitle + "?")) {
                return createFalse();
            }
        }

        final String childrenTypeTitles = getSaveableChildrenTypeTitles(radixObjects);
        if (childrenTypeTitles != null && !childrenTypeTitles.isEmpty()) {
            if (!DialogUtils.messageConfirmation("Contained " + childrenTypeTitles + " will be deleted.\nContinue?")) {
                return createFalse();
            }
        }

        return removerService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final boolean[] cancelled = new boolean[]{false};
                final Cancellable cancellable = new Cancellable() {
                    @Override
                    public boolean cancel() {
                        cancelled[0] = true;
                        return true;
                    }
                };
                ProgressHandle handle = ProgressHandleFactory.createHandle("Deleting objects...", cancellable);
                handle.start();
                try {
                    for (RadixObject radixObject : radixObjects) {
                        if (cancelled[0]) {
                            break;
                        }
                        deleteOnWriteAccess(radixObject);
                    }
                } finally {
                    handle.finish();
                }
                return true;
            }
        });
    }

    public static Box createVerticalBox(Action[] actions) {
        final Box box = new Box(BoxLayout.Y_AXIS);
        for (Action action : actions) {
            if (action != null) {
                final JButton button = new JButton(action);
                button.setMaximumSize(new Dimension(Short.MAX_VALUE, button.getPreferredSize().height));
                button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                button.setIconTextGap(8);
                box.add(button);
            }
            box.add(Box.createRigidArea(new Dimension(4, 4)));
        }
        return box;
    }

    public static String millisToString(long msec) {
        long sec = msec / 1000;
        long mksecdec = (msec - sec * 1000) / 100;
        return String.valueOf(sec) + "." + String.valueOf(mksecdec) + " sec.";
    }

    public static String inputBox(String catpion) {
        return JOptionPane.showInputDialog(null, catpion);
    }

    public static String showCustomMessageBox(String mess, List<String> buttons, int messageType) {
        final String ret[] = new String[1];
        ret[0] = null;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());

        final JOptionPane optionPane = new JOptionPane(mess, messageType);

        final JPanel labelPanel = (JPanel) optionPane.getComponent(0);
        mainPanel.add(labelPanel, BorderLayout.NORTH);
        top.add(labelPanel, BorderLayout.CENTER);
        mainPanel.add(top, BorderLayout.CENTER);
        JPanel bottom1 = new JPanel();

        mainPanel.add(bottom1, BorderLayout.SOUTH);
        JPanel bottom = new JPanel();

        bottom1.setLayout(new BorderLayout());
        bottom1.add(bottom, BorderLayout.CENTER);

        final DialogDescriptor descriptor = new DialogDescriptor(mainPanel, null);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);

        for (int i = 0; i < buttons.size(); i++) {
            JToggleButton b = new JToggleButton(buttons.get(i));
            bottom.add(b);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JToggleButton b = (JToggleButton) e.getSource();
                    ret[0] = b.getText();
                    dialog.setVisible(false);
                }
            });
        }
        final Dimension initialSize = dialog.getSize();
        initialSize.width = Math.max(initialSize.width, 400);
        dialog.setMinimumSize(initialSize);
        descriptor.setOptions(new Object[]{});


        dialog.setResizable(false);
        dialog.setVisible(true);
        return ret[0];
    }
    
    public static void openFile(File file) {
        try {
            final FileObject fileObject = RadixFileUtil.toFileObject(file);
            NodesManager.selectInFiles(fileObject);
            //open editor
            final DataObject dataObject = DataObject.find(fileObject);
            Openable o = dataObject.getLookup().lookup(Openable.class);
            if (o != null) {
                o.open();
            }
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
