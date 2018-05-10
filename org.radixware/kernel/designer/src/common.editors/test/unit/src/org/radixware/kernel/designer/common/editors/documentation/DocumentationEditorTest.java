/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.editors.documentation;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author dkurlyanov Тестилка запускаед редактор технической документации в
 * отдельном окне. В качестве параметра передается модуль с готовыми тестовыми
 * данными.
 */
public class DocumentationEditorTest extends JFrame {

//    public static void main(String[] args) {
//
//        // Load Module in Branch
//        Branch branch = null;
//        try {
//            File file = new File("C:\\RadixWare\\trunc");
//            branch = Branch.Factory.loadFromDir(file);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        Id id = Id.Factory.loadFrom("mdlBCHQPEV7LBEAPMJFSIUVTNZ5OM");
//        AdsModule mod = (AdsModule) (branch.getLayers().get(2).getAds().getModules().getById(id));
//
//        // Window
//        DocumentationEditor editor = new DocumentationEditor(mod);
//        DocumentationEditorTest window = new DocumentationEditorTest();
//        window.add(editor);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setSize(1000, 800);
//        window.setVisible(true);
//    }
}
