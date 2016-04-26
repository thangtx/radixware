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

package org.radixware.kernel.designer.ads.editors.creation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


class TestCaseCreature extends AdsModuleDefinitionCreature {
    
    private AdsEntityClassDef testCase = null;
    AdsEntityObjectClassDef customBase = null;
    boolean useCustomBase = false;
    
    TestCaseCreature(AdsModule module) {
        super(module, EDefType.CLASS);
        definitionName = "New.Test";
    }
    
    @Override
    public AdsDefinition createInstance() {
        AdsEntityClassDef tc = findTestCaseBase();
        if (tc != null) {
            AdsEntityObjectClassDef base = useCustomBase && customBase != null ? customBase : tc;
            return AdsApplicationClassDef.Factory.newInstance(base);
        } else {
            return null;
        }
    }
    private static final Id EXECUTE_ID = Id.Factory.loadFrom("mthG2ORZGNH27NRDISQAAAAAAAAAA");
    private static final String message = "/*This is an entry point to your test code. Following specific methods are available:\n"
            + "         void assertTrue(String message, boolean condition)\n"
            + "         void assertTrue(boolean condition)\n"
            + "         void assertFalse(String message, boolean condition)\n"
            + "         void assertFalse(boolean condition)\n"
            + "         void fail(String message)\n"
            + "         void fail()\n"
            + "         void assertEquals(String message, Object expected, Object actual)\n"
            + "         void assertEquals(Object expected, Object actual)\n"
            + "         void assertEquals(long expected, long actual)\n"
            + "         void assertEquals(String message, long expected, long actual)\n"
            + "         void assertNotNull(String message, Object object)\n"
            + "         void assertNotNull(Object object)\n"
            + "         void assertNull(String message, Object object)\n"
            + "         void assertNull(Object object)\n"
            + "Enjoy :)\n*/\n\nfail(\"Test not implemented!\");\n\nreturn ";
    
    @Override
    public void afterAppend(AdsDefinition object) {
        AdsEntityObjectClassDef base = useCustomBase && customBase != null ? customBase : findTestCaseBase();
        object.getModule().getDependences().add(base.getModule());
        AdsClassDef clazz = (AdsClassDef) object;
        AdsMethodDef method = clazz.getMethods().findById(EXECUTE_ID, EScope.LOCAL).get();
        if (method == null) {
            method = clazz.getMethods().findById(EXECUTE_ID, EScope.ALL).get();
            if (method != null) {
                try {
                    method = clazz.getMethods().override(method);
                    if (method != null) {
                        ((AdsUserMethodDef) method).getSource().getItems().clear();
                        ((AdsUserMethodDef) method).getSource().getItems().add(Scml.Text.Factory.newInstance(message));
                        AdsTypeDeclaration decl = method.getProfile().getReturnValue().getType();
                        AdsType type = decl.resolve(method).get();
                        AdsEnumItemDef itemDef = null;
                        if (type instanceof AdsEnumType) {
                            AdsEnumDef e = ((AdsEnumType) type).getSource();
                            for (AdsEnumItemDef item : e.getItems().list(EScope.ALL)) {
                                if ("event".equals(item.getName().toLowerCase())) {
                                    itemDef = item;
                                    break;
                                }
                            }
                        }
                        if (itemDef != null) {
                            ((AdsUserMethodDef) method).getSource().getItems().add(JmlTagInvocation.Factory.newInstance(itemDef));
                            ((AdsUserMethodDef) method).getSource().getItems().add(Scml.Text.Factory.newInstance(";"));
                        } else {
                            ((AdsUserMethodDef) method).getSource().getItems().add(Scml.Text.Factory.newInstance("null;"));
                        }
                        
                    }
                } catch (RadixObjectError ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }
    
    @Override
    public String getDescription() {
        return "Create new test case class for test purpose";
    }
    
    @Override
    public String getDisplayName() {
        return "Test Case";
    }
    
    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_TESTCASE;
    }
    private boolean find = true;
    
    private AdsEntityClassDef findTestCaseBase() {
        if (find) {
            testCase = (AdsEntityClassDef) module.getBranch().find(AdsVisitorProviders.newEntityTypeProvider(AdsApplicationClassDef.TEST_CASE_CLASS_ID));
        }
        return testCase;
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && findTestCaseBase() != null;
    }
    
    @Override
    public WizardInfo getWizardInfo() {
        return new org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature.WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                return new Step();
            }
            
            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
    
    private class Step extends CreatureSetupStep<TestCaseCreature, TestCaseCreatureVisual> {
        
        @Override
        public String getDisplayName() {
            return "Setup Test Case";
        }
        
        @Override
        protected TestCaseCreatureVisual createVisualPanel() {
            TestCaseCreatureVisual v = new TestCaseCreatureVisual(module);
            v.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                }
            });
            return v;
        }
        
        @Override
        public void open(TestCaseCreature creature) {
            getVisualPanel().open(creature);
        }
        
        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }
    }
}
