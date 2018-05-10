package org.radixware.kernel.designer.uds.tree.actions;

import java.io.IOException;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public class UdsReloadAction extends CookieAction{
    
    public static class Cookie implements Node.Cookie {

        private final RadixObject radixObject;

        public Cookie(RadixObject udsFile) {
            this.radixObject = udsFile;
        }

        public void reloadDefifnition() {

            String message = "Reload " + " '" + radixObject.getQualifiedName() + "' ?";
            if (radixObject.getEditState() != RadixObject.EEditState.NONE) {
                message += "\nALL UNSAVED CHANGES WILL BE LOST!";
            }
            if (!DialogUtils.messageConfirmation(message)) {
                return;
            }

            RadixMutex.writeAccess(new Runnable() {

                @Override
                public void run() {
                    final Module module = radixObject.getModule();
                    if (module != null && module instanceof UdsModule) {
                        try {
                            final RadixObject newRadixObject = ((UdsModule) module).getUdsFiles().reload(radixObject);
                            SwingUtilities.invokeLater(new Runnable() {
                                
                                @Override
                                public void run() {
                                    NodesManager.selectInProjects(newRadixObject);
                                }
                            });
                        } catch (IOException ex) {
                            RadixObjectError error = new RadixObjectError("Unable to reload file.", radixObject, ex);
                            DialogUtils.messageError(error);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (Node node : nodes) {
            final Cookie cookie = node.getCookie(UdsReloadAction.Cookie.class);
            if (cookie != null) {
                cookie.reloadDefifnition();
            }
        }
    }

    @Override
    public String getName() {
        return "Reload"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock, because tree can be recreated
    }
}
