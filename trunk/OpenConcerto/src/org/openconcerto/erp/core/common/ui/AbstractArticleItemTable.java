/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 OpenConcerto, by ILM Informatique. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the GNU General Public License Version 3
 * only ("GPL"). You may not use this file except in compliance with the License. You can obtain a
 * copy of the License at http://www.gnu.org/licenses/gpl-3.0.html See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each file.
 */
 
 package org.openconcerto.erp.core.common.ui;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.core.sales.product.element.ReferenceArticleSQLElement;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.element.SQLElement;
import org.openconcerto.sql.model.SQLField;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.view.list.RowValuesTable;
import org.openconcerto.sql.view.list.RowValuesTableControlPanel;
import org.openconcerto.sql.view.list.RowValuesTableModel;
import org.openconcerto.sql.view.list.RowValuesTableRenderer;
import org.openconcerto.sql.view.list.SQLTableElement;
import org.openconcerto.ui.DefaultGridBagConstraints;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class AbstractArticleItemTable extends JPanel {
    protected RowValuesTable table;
    protected SQLTableElement totalHT;
    protected SQLTableElement tableElementTotalTTC;
    protected SQLTableElement service, qte, ha;
    protected SQLTableElement tableElementPoidsTotal;
    protected RowValuesTableModel model;
    protected SQLRowValues defaultRowVals;
    private List<JButton> buttons = null;
    protected RowValuesTableControlPanel control = null;

    public AbstractArticleItemTable() {
        init();
        uiInit();
    }

    public AbstractArticleItemTable(List<JButton> buttons) {
        this.buttons = buttons;
        init();
        uiInit();
    }

    /**
     * 
     */
    abstract protected void init();

    protected File getConfigurationFile() {
        return new File(Configuration.getInstance().getConfDir(), "Table/" + getConfigurationFileName());
    }

    /**
     * 
     */
    protected void uiInit() {
        // Ui init
        setLayout(new GridBagLayout());
        final GridBagConstraints c = new DefaultGridBagConstraints();

        c.weightx = 1;

        control = new RowValuesTableControlPanel(this.table, this.buttons);

        this.add(control, c);

        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        final JScrollPane comp = new JScrollPane(this.table);
        this.add(comp, c);
        this.table.setDefaultRenderer(Long.class, new RowValuesTableRenderer());
    }

    /**
     * @return the coniguration file to store pref
     */
    protected abstract String getConfigurationFileName();

    public abstract SQLElement getSQLElement();

    public void updateField(final String field, final int id) {
        this.table.updateField(field, id);
    }

    public RowValuesTable getRowValuesTable() {
        return this.table;
    }

    public void insertFrom(final String field, final int id) {
        this.table.insertFrom(field, id);

    }

    public RowValuesTableModel getModel() {
        return this.table.getRowValuesTableModel();
    }

    public SQLTableElement getPrixTotalHTElement() {
        return this.totalHT;
    }

    public SQLTableElement getPoidsTotalElement() {
        return this.tableElementPoidsTotal;
    }

    public SQLTableElement getPrixTotalTTCElement() {
        return this.tableElementTotalTTC;
    }

    public SQLTableElement getPrixServiceElement() {
        return this.service;
    }

    public SQLTableElement getQteElement() {
        return this.qte;
    }

    public SQLTableElement getHaElement() {
        return this.ha;
    }

    public void deplacerDe(final int inc) {
        final int rowIndex = this.table.getSelectedRow();

        final int dest = this.model.moveBy(rowIndex, inc);
        this.table.getSelectionModel().setSelectionInterval(dest, dest);
    }

    /**
     * @return le poids total de tous les éléments du tableau
     */
    public float getPoidsTotal() {

        float poids = 0.0F;
        final int poidsTColIndex = this.model.getColumnIndexForElement(this.tableElementPoidsTotal);
        if (poidsTColIndex >= 0) {
            for (int i = 0; i < this.table.getRowCount(); i++) {
                final Number tmp = (Number) this.model.getValueAt(i, poidsTColIndex);
                if (tmp != null) {
                    poids += tmp.floatValue();
                }
            }
        }
        return poids;
    }

    public void refreshTable() {
        this.table.repaint();
    }

    public void createArticle(final int id, final SQLElement eltSource) {

        final SQLElement eltArticleTable = getSQLElement();

        final SQLTable tableArticle = ((ComptaPropsConfiguration) Configuration.getInstance()).getRootSociete().getTable("ARTICLE");

        // On récupére les articles qui composent la table
        final List<SQLRow> listElts = eltSource.getTable().getRow(id).getReferentRows(eltArticleTable.getTable());
        final SQLRowValues rowArticle = new SQLRowValues(tableArticle);
        final Set<SQLField> fields = tableArticle.getFields();

        for (final SQLRow rowElt : listElts) {
            final Set<String> fieldsName = rowElt.getTable().getFieldsName();
            // on récupére l'article qui lui correspond

            for (final SQLField field : fields) {

                final String name = field.getName();
                if (fieldsName.contains(name) && !field.isPrimaryKey()) {
                    rowArticle.put(name, rowElt.getObject(name));
                }
            }
            // crée les articles si il n'existe pas
            ReferenceArticleSQLElement.getIdForCNM(rowArticle, true);
        }
    }


    public SQLRowValues getDefaultRowValues() {
        return this.defaultRowVals;
    }
}
