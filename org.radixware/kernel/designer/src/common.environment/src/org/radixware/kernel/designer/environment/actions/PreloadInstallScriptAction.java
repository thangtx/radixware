/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.script.ScriptDefinitionsCollector;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.kernel.designer.dds.script.ScriptGeneratorImpl;

/**
 *
 * @author achernomyrdin
 */
public class PreloadInstallScriptAction extends AbstractContextAwareAction implements ActionListener {

    private final Layer layer;

    public PreloadInstallScriptAction() {
        this.layer = null;
        setEnabled(false);
    }

    private PreloadInstallScriptAction(final Layer layer) {
        this.layer = layer;
        setEnabled(true);
    }

    @Override
    public Action createContextAwareInstance(final Lookup actionContext) {
        return new PreloadInstallScriptAction(actionContext.lookup(Layer.class));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (layer != null) {
            final Set<DdsDefinition> definitions = new HashSet<>();
            
            new Layer.HierarchyWalker().go(layer, new Layer.HierarchyWalker.Acceptor<Layer>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<Layer> controller, Layer radixObject) {
                        ScriptDefinitionsCollector.collect(radixObject, definitions);
                    }
                }
            );

//            final StringBuilder sb = new StringBuilder();
            final ScriptGenerator scriptGenerator = new ScriptGeneratorImpl(null,definitions);
            final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();

            scriptGenerator.generateCompactModificationScript(cp,null);
            
//            for (EDatabaseType item : EDatabaseType.class.getEnumConstants()) {
//                final CodePrinter cp = CodePrinter.Factory.newSqlPrinter(item);
//                
//                cp.print("\n#IF DB_TYPE == \"").print(item.toString()).println("\" THEN");
//                scriptGenerator.generateModificationScript(cp,item,null);
//                cp.println("\n#ENDIF");
//                sb.append(cp.toString());
//            }
            
            DialogUtils.showText(cp.toString().replace("&USER&_RUN_ROLE","TEST_ROLE"), "Fresh installation script", "sql");
        }
    }

    @Override
    public Object getValue(final String key) {
        if ("Name".equals(key)) {
            return "Make fresh installation script";
        } else {
            return super.getValue(key);
        }
    }
}

