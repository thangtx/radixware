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

package org.radixware.kernel.designer.common.editors.sqml;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.scml.ITaskTag;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.*;
import org.radixware.kernel.common.sqml.translate.SqmlTranslator;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.ErrorVTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.TagEditorFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTagFactory;

import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapperListener;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.dialogs.scmlnb.PopupActionCodeGeneratorFactory;
import org.radixware.kernel.designer.common.editors.sqml.editors.ConstValueTagEditPanel;
import org.radixware.kernel.designer.common.editors.sqml.editors.DbFuncCallTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.EntityRefParamTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.JoinTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.PropSqlNameTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.SequenceDbNameTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.ThisTableRefTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.*;
import org.radixware.kernel.designer.common.editors.jml.editors.TaskEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.IfTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.TargetDbPreprocessorTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.editors.XPathTagEditor;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ConstValueVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.DataVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.DbFuncCallVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.DbNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ElseIfVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.EndIfVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.EntityRefParameterVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.IdVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.IfParamVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.IndexDbNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.JoinVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ParameterVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ParentConditionVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ParentRefPropSqlNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.PropSqlNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.SequenceDbNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.TableSqlNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.TargetDbPreprocessorVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.TaskVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ThisTableIdVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ThisTableRefVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.ThisTableSqlNameVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.TypifiedValueVTag;
import org.radixware.kernel.designer.common.editors.sqml.vtag.XPathVTag;

/**
 * SQML editor component panel.
 *
 */
public class SqmlEditorPanel extends ScmlEditor<Sqml> {

    protected SqmlEditorPanel(String mimeType) {
        super(mimeType);
        if (getPane() != null) {
            ScmlDocument sdoc = getPane().getScmlDocument();
            if (sdoc != null) {
                sdoc.getTagMapper().addListener(new TagMapperListener() {
                    @Override
                    public void tagRemoved(TagBounds tagBounds) {
                    }

                    @Override
                    public void tagAdded(TagBounds tagBounds) {
                        actualize();
                    }

                    @Override
                    public void tagUpdated(TagBounds tagBounds) {
                        actualize();
                    }
                });
            }
        }
        getPane().getDocument().putProperty(Lookup.class, getLookup());
    }

    public SqmlEditorPanel() {
        this(SQML_MIME_TYPE);
    }

    @Override
    protected ScmlToolBarAction[] createScmlToolBarActions() {
        ScmlToolBarAction[] actions = new ScmlToolBarAction[]{
            new InsertIdAction(this),
            new InsertDbNameAction(this),
            new InsertPropertySqlNameAction(this),
            new InsertDataTagAction(this),
            new InsertTableSqlNameAction(this),
            new InsertEnumerationItemValueTag(this),
            new InsertDbFuncCallAcion(this),
            new InsertInputParameterTag(this),
            new InsertThisTableRefAction(this),
            new InsertXPathTagAction(this),
            new InsertPreprocessorAction(this),
            new PreviewSqlAction(this)};

        return actions;
    }

    @Override
    public void update() {
        actualize();
        super.update();
    }

    public void open(Sqml source) {
        super.open(source, null);
        actualize();
    }

    private void actualize() {
        Sqml sqml = getSource();
        if (!sqml.isReadOnly()) {
            SqmlTranslator.Factory.newInstance().actualize(sqml);
        }
    }

    public static class SqmlVTagFactory implements VTagFactory {

        @SuppressWarnings("rawtypes")
        @Override
        public VTag createVTag(Tag tag) {
            if (tag instanceof ConstValueTag) {
                return new ConstValueVTag((ConstValueTag) tag);
            } else if (tag instanceof DbFuncCallTag) {
                return new DbFuncCallVTag((DbFuncCallTag) tag);
            } else if (tag instanceof ElseIfTag) {
                return new ElseIfVTag((ElseIfTag) tag);
            } else if (tag instanceof EndIfTag) {
                return new EndIfVTag((EndIfTag) tag);
            } else if (tag instanceof IdTag) {
                return new IdVTag((IdTag) tag);
            } else if (tag instanceof DataTag) {
                return new DataVTag((DataTag) tag);
            } else if (tag instanceof IfParamTag) {
                return new IfParamVTag((IfParamTag) tag);
            } else if (tag instanceof IndexDbNameTag) {
                return new IndexDbNameVTag((IndexDbNameTag) tag);
            } else if (tag instanceof JoinTag) {
                return new JoinVTag((JoinTag) tag);
            } else if (tag instanceof ParameterTag) {
                return new ParameterVTag((ParameterTag) tag);
            } else if (tag instanceof ParentConditionTag) {
                return new ParentConditionVTag((ParentConditionTag) tag);
            } else if (tag instanceof ParentRefPropSqlNameTag) {
                return new ParentRefPropSqlNameVTag((ParentRefPropSqlNameTag) tag);
            } else if (tag instanceof PropSqlNameTag) {
                return new PropSqlNameVTag((PropSqlNameTag) tag);
            } else if (tag instanceof SequenceDbNameTag) {
                return new SequenceDbNameVTag((SequenceDbNameTag) tag);
            } else if (tag instanceof TableSqlNameTag) {
                return new TableSqlNameVTag((TableSqlNameTag) tag);
            } else if (tag instanceof ThisTableIdTag) {
                return new ThisTableIdVTag((ThisTableIdTag) tag);
            } else if (tag instanceof ThisTableSqlNameTag) {
                return new ThisTableSqlNameVTag((ThisTableSqlNameTag) tag);
            } else if (tag instanceof TypifiedValueTag) {
                return new TypifiedValueVTag((TypifiedValueTag) tag);
            } else if (tag instanceof DbNameTag) {
                return new DbNameVTag((DbNameTag) tag);
            } else if (tag instanceof EntityRefParameterTag) {
                return new EntityRefParameterVTag((EntityRefParameterTag) tag);
            } else if (tag instanceof ThisTableRefTag) {
                return new ThisTableRefVTag((ThisTableRefTag) tag);
            } else if (tag instanceof XPathTag) {
                return new XPathVTag((XPathTag) tag);
            } else if (tag instanceof TaskTag) {
                return new TaskVTag((TaskTag) tag);
            } else if (tag instanceof TargetDbPreprocessorTag) {
                return new TargetDbPreprocessorVTag((TargetDbPreprocessorTag) tag);
            } else {
                return new ErrorVTag(tag);
            }
        }
    }

    protected static class SqmlTagEditorFactory implements TagEditorFactory {

        private final SqmlEditorPanel sqmlEditor;

        public SqmlTagEditorFactory(SqmlEditorPanel sqmlEditor) {
            this.sqmlEditor = sqmlEditor;
        }

        @Override
        public TagEditor createTagEditor(Scml.Tag tag) {
            if (tag instanceof ConstValueTag) {
                return new ConstValueTagEditPanel();
            } else if (tag instanceof JoinTag) {
                return new JoinTagEditor();
            } else if (tag instanceof SequenceDbNameTag) {
                return new SequenceDbNameTagEditor();
            } else if (tag instanceof DbFuncCallTag) {
                return new DbFuncCallTagEditor();
            } else if (tag instanceof PropSqlNameTag) {
                PropSqlNameTag propSqlNameTag = (PropSqlNameTag) tag;
                if (propSqlNameTag.getOwnerType() == PropSqlNameTag.EOwnerType.NONE || propSqlNameTag.getOwnerType() == PropSqlNameTag.EOwnerType.TABLE) {
                    return new PropSqlNameTagEditor();
                } else {
                    return null;
                }
            } else if (tag instanceof EntityRefParameterTag) {
                return new EntityRefParamTagEditor();
            } else if (tag instanceof ThisTableRefTag) {
                return new ThisTableRefTagEditor();
            } else if (tag instanceof ITaskTag) {
                return new TaskEditor();
            } else if (tag instanceof XPathTag) {
                return new XPathTagEditor();
            } else if (tag instanceof TargetDbPreprocessorTag) {
                return new TargetDbPreprocessorTagEditor();
            } else if (tag instanceof IfParamTag) {
                return new IfTagEditor();
            } else {
                return null;
            }
        }

        @Override
        public EditorOpenInfo createOpenInfo(Tag tag) {
            return new EditorOpenInfo(sqmlEditor.getPane().isReadOnly(), Lookups.fixed(sqmlEditor, sqmlEditor.getSource()));
        }
    }

    @Override
    protected Collection<Object> getObjectsForLookup() {
        return Arrays.asList(new Object[]{new SqmlEditorPanelPreprocessorConfigProvider()});
    }

    @Override
    protected TagEditorFactory createTagEditorFactory() {
        return new SqmlTagEditorFactory(this);
    }

    @Override
    protected VTagFactory createVTagFactory() {
        return new SqmlVTagFactory();
    }

  
}
