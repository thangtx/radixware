package org.radixware.kernel.designer.common.dialogs.actions.codegen;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.editor.BaseAction;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.modules.editor.MainMenuAction;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.netbeans.spi.editor.codegen.CodeGeneratorContextProvider;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
@EditorActionRegistration(
        name = "generate-code",
        shortDescription = "#desc-generate-code",
        popupText = "#popup-generate-code"
) 
public class RadixNbGenerateCodeAction  extends BaseAction{
    
    public RadixNbGenerateCodeAction(){
        putValue(ExtKit.TRIMMED_TEXT, NbBundle.getMessage(RadixNbGenerateCodeAction.class, "generate-code-trimmed")); //NOI18N
    }
    
    private static MimePath getFullMimePath(Document document, int offset) {
        String langPath = null;

        if (document instanceof AbstractDocument) {
            AbstractDocument adoc = (AbstractDocument)document;
            adoc.readLock();
            try {
                List<TokenSequence<?>> list = TokenHierarchy.get(document).embeddedTokenSequences(offset, true);
                if (list.size() > 1) {
                    langPath = list.get(list.size() - 1).languagePath().mimePath();
                }
            } finally {
                adoc.readUnlock();
            }
        }

        if (langPath == null) {
            langPath = NbEditorUtilities.getMimeType(document);
        }

        if (langPath != null) {
            return MimePath.parse(langPath);
        } else {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt, final JTextComponent target) {
        final Task task = new Task(getFullMimePath(target.getDocument(), target.getCaretPosition()));
        final AtomicBoolean cancel = new AtomicBoolean();
        ProgressUtils.runOffEventDispatchThread(new Runnable() {
            @Override
            public void run() {
                if (cancel != null && cancel.get())
                    return ;
                task.run(Lookups.singleton(target));
                if (cancel != null && cancel.get())
                    return ;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (task.codeGenerators.size() > 0) {
                            int altHeight = -1;
                            Point where = null;
                            try {
                                Rectangle carretRectangle = target.modelToView(target.getCaretPosition());
                                altHeight = carretRectangle.height;
                                where = new Point(carretRectangle.x, carretRectangle.y + carretRectangle.height);
                                SwingUtilities.convertPointToScreen(where, target);
                            } catch (BadLocationException ble) {
                            }
                            if (where == null) {
                                where = new Point(-1, -1);
                                
                            }
                            PopupUtil.showPopup(new GenerateCodePanel(target, task.codeGenerators), (Frame)SwingUtilities.getAncestorOfClass(Frame.class, target), where.x, where.y, true, altHeight);
                        } else {
                            target.getToolkit().beep();
                        }
                    }
                });
            }
        }, NbBundle.getMessage(RadixNbGenerateCodeAction.class, "generate-code-trimmed"), cancel, false);
        
        
    }
    
    private static class Task implements CodeGeneratorContextProvider.Task {
        private final MimePath mimePath;
        private final Iterator<? extends CodeGeneratorContextProvider> contextProviders;
        private final List<CodeGenerator> codeGenerators = new ArrayList<>(); 

        private Task(MimePath mimePath) {
            this.mimePath = mimePath;
            contextProviders = MimeLookup.getLookup(mimePath).lookupAll(CodeGeneratorContextProvider.class).iterator();
        }

        @Override
        public void run(Lookup context) {
            if (contextProviders.hasNext()) {
                contextProviders.next().runTaskWithinContext(context, this);
            } else {
                for (CodeGenerator.Factory factory : MimeLookup.getLookup(mimePath).lookupAll(CodeGenerator.Factory.class))
                    codeGenerators.addAll(factory.create(context));
            }
        }
    }

     public static final class GlobalAction extends MainMenuAction {
        public GlobalAction() {
            super();
            postSetMenu();
        }
        
        @Override
        protected String getMenuItemText() {
            return NbBundle.getMessage(GlobalAction.class, "generate-code-main-menu-source-item"); //NOI18N
        }

        @Override
        protected String getActionName() {
            return "generate-code";
        }
    } // End of GlobalAction class
}
