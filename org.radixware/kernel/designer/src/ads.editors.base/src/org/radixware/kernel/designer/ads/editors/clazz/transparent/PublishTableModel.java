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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;


final class PublishTableModel extends AbstractTableModel {

    static final int TREE_COLUMN = 0;
    static final int PUBLISH_COLUMN = 1;
    static final int DELETE_COLUMN = 2;
    static final int CORRECT_COLUMN = 3;
    private static final String TREE_COLUMN_NAME = NbBundle.getMessage(PublishTableModel.class, "TreeTableModel-TreeColumn");
    private static final String PUBLISH_COLUMN_NAME = NbBundle.getMessage(PublishTableModel.class, "TreeTableModel-AddColumn");
    private static final String CORRECT_COLUMN_NAME = NbBundle.getMessage(PublishTableModel.class, "TreeTableModel-CorrectColumn");
    private static final String DELETE_COLUMN_NAME = NbBundle.getMessage(PublishTableModel.class, "TreeTableModel-DelColumn");
    private static final String[] columnsNames = new String[]{ TREE_COLUMN_NAME, PUBLISH_COLUMN_NAME, DELETE_COLUMN_NAME, CORRECT_COLUMN_NAME };

    // fields
    private final List<ClassMemberPresenter> presenters;
    private final Map<ClassMemberPresenter, Link> linkMap = new HashMap<>();

    public PublishTableModel(Collection<ClassMemberPresenter> content) {
        super();

        final TreeSet<ClassMemberPresenter> set = new TreeSet<>(ClassMemberPresenter.COMPARATOR);
        set.addAll(content);

        presenters = new ArrayList<>(set);

        for (final ClassMemberPresenter presenter : content) {

            if (presenter.isPublished()) {
                linkMap.put(presenter, new Link(presenter, this));
            }
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int col) {
        final ClassMemberPresenter presentation = presenters.get(row);
        switch (col) {
            case TREE_COLUMN:
                return presentation;
            case PUBLISH_COLUMN:
                return presentation.isPublished() || presentation.isMarkedToBePublished();
            case DELETE_COLUMN:
                return presentation.getState() == EPresenterState.DELETE;
            case CORRECT_COLUMN:
                return linkMap.get(presentation);
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        final ClassMemberPresenter presenter = presenters.get(row);
        switch (col) {
            case PUBLISH_COLUMN:
                if (presenter.isCorrect()) {
                    presenter.setState((Boolean) value ? EPresenterState.PUBLISH : null);
                }
                break;
            case DELETE_COLUMN:
                presenter.setState((Boolean) value ? EPresenterState.DELETE : null);
                break;
            case CORRECT_COLUMN:
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        assert (column >= TREE_COLUMN && column <= CORRECT_COLUMN);
        return columnsNames[column];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case TREE_COLUMN:
                return ClassMemberPresenter.class;
            case PUBLISH_COLUMN:
            case DELETE_COLUMN:
                return Boolean.class;
            case CORRECT_COLUMN:
                return Link.class;
            default:
                return super.getColumnClass(col);
        }
    }

    @Override
    public int getRowCount() {
        return presenters.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        final ClassMemberPresenter presentation = presenters.get(row);
        switch (col) {
            case TREE_COLUMN:
                return false;
            case PUBLISH_COLUMN:
                return !presentation.isPublished() && presentation.isCorrect();
            case DELETE_COLUMN:
                return presentation.isPublished();
            case CORRECT_COLUMN:
                return linkMap.containsKey(presentation);
            default:
                return false;
        }
    }

    public Collection<ClassMemberPresenter> getNotPublished() {
        final Collection<ClassMemberPresenter> notPublished = new HashSet<>();

        for (final ClassMemberPresenter presenter : presenters) {
            if (!presenter.isPublished() && !presenter.isMarkedToBePublished()) {
                notPublished.add(presenter);
            }
        }

        return notPublished;
    }

    void apply(AdsClassDef transparentClass) {
        for (final ClassMemberPresenter presenter : presenters) {
            presenter.apply(linkMap.get(presenter), transparentClass);
        }
    }
}
