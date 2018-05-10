package org.radixware.kernel.designer.environment.actions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import org.apache.xmlbeans.XmlObject;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.schemas.utils.NamedElement;
import org.radixware.schemas.utils.NamesDocument;


public class ExportClassNamesAction  extends CallableSystemAction {
    
    private InputOutputPrinter inputOutput;
    
    private File chooseDir() {
        String locations = NbPreferences.forModule(ExportClassNamesAction.class).get("dir.path", System.getProperty("user.home"));
        JFileChooser chooser = new JFileChooser(locations);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Choose location");

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            if (file != null && file.exists()) {
                NbPreferences.forModule(ExportClassNamesAction.class).put("dir.path", file.getAbsolutePath());
                return file;
            }
        }
        
        return null;
    }
    
    private void fillElement(NamedElement el, Definition def) {
        el.setId(def.getId());
        el.setName(def.getName());
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(FixupStdClassesAction.class, "CTL_ExportClassNamesAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        final Canceller canceller = new Canceller();
        ProgressHandle handle = ProgressHandleFactory.createHandle("Export names...", canceller);
        inputOutput = new InputOutputPrinter("Export Class Names");
        try {
            inputOutput.reset();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        inputOutput.select();
        try {
            final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
            File dir;
            while ((dir = chooseDir()) == null) {
                if (DialogUtils.messageConfirmation("Location is not chosen. Exit? ")) {
                    return;
                }
            }
            print("Export class names files to " + dir.getAbsolutePath());
            handle.start();
            for (Branch branch : branches) {
                File file =  new File(dir, branch.getName() + ".xml");
                handle.progress("Export " + file.getName() + "...");
                print("Export " + file.getName() + "...");
                NamesDocument doc = NamesDocument.Factory.newInstance();
                final NamesDocument.Names xNames = doc.addNewNames();
                final Map<RadixObject, XmlObject> objects = new HashMap<>();
                branch.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof Layer) {
                            NamesDocument.Names.Layer l = xNames.addNewLayer();
                            l.setUri(((Layer) radixObject).getURI());
                            objects.put(radixObject, l);
                        }
                        if (radixObject instanceof AdsModule) {
                            Layer l = radixObject.getLayer();
                            XmlObject xo = objects.get(l);
                            if (xo instanceof NamesDocument.Names.Layer) {
                                NamesDocument.Names.Layer.Module m = ((NamesDocument.Names.Layer) xo).addNewModule();
                                fillElement(m, (Definition) radixObject);
                                objects.put(radixObject, m);
                            }
                        }
                        if (radixObject instanceof AdsClassDef) {
                            AdsClassDef classDef = (AdsClassDef) radixObject;
                            Module m = radixObject.getModule();
                            XmlObject xo = objects.get(m);
                            if (xo instanceof NamesDocument.Names.Layer.Module) {
                                NamesDocument.Names.Layer.Module.Class c = ((NamesDocument.Names.Layer.Module) xo).addNewClass1();
                                fillElement(c, (Definition) radixObject);
                                c.setEnvironment(classDef.getUsageEnvironment());
                                objects.put(radixObject, c);
                            }
                        }
                        if (radixObject instanceof AdsMethodDef) {
                            AdsMethodDef method = (AdsMethodDef) radixObject;
                            AdsClassDef c = method.getOwnerClass();
                            XmlObject xo = objects.get(c);
                            if (xo instanceof NamesDocument.Names.Layer.Module.Class) {
                                NamesDocument.Names.Layer.Module.Class.Method m = ((NamesDocument.Names.Layer.Module.Class) xo).addNewMethod();
                                fillElement(m, (Definition) radixObject);
                                objects.put(radixObject, m);
                            }
                        }
                    }
                }, new VisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        if (canceller.isCancelled) {
                            setCancelled(true);
                        }
                        return radixObject instanceof Layer || radixObject instanceof AdsModule || radixObject instanceof AdsClassDef || radixObject instanceof AdsMethodDef;
                    }
                });
                
                if (canceller.isCancelled) {
                    return;
                }

                try {
                    XmlUtils.saveXmlPretty(doc, file);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } finally {
            try {
                inputOutput.printlnInfo("Export finish");
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            handle.finish();
        }
    }
    
    private void print(String message) {
        try {
            inputOutput.println(message);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    private class Canceller implements Cancellable {

        private boolean isCancelled = false;

        @Override
        public boolean cancel() {
            isCancelled = true;
            return true;
        }
    }
    
}
