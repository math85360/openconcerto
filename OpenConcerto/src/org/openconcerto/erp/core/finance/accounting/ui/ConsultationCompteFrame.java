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
 
 package org.openconcerto.erp.core.finance.accounting.ui;

import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.State;
import org.openconcerto.ui.state.WindowStateManager;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


public class ConsultationCompteFrame extends JFrame {
    // panel contenant la liste des ecritures
    private final ListPanelEcritures panelEcritures;
    // panel complet liste des ecritures + totaux
    private final ListeDesEcrituresPanel panel;

    private String titre;

    public ConsultationCompteFrame(ListeDesEcrituresPanel panel, String titre) {
        super();
        this.panel = panel;
        this.panelEcritures = panel.getListPanelEcritures();
        this.titre = titre;

        // rafraichir le titre à chaque changement de la liste
        this.panelEcritures.getListe().addListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                setTitle();
            }
        });

        this.uiInit();
        if (State.DEBUG) {
            State.INSTANCE.frameCreated();
            this.addComponentListener(new ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    State.INSTANCE.frameHidden();
                }

                public void componentShown(ComponentEvent e) {
                    State.INSTANCE.frameShown();
                }
            });
        }
    }

    private String getPlural(String s, int nb) {
        return nb + " " + s + (nb > 1 ? "s" : "");
    }

    private void setTitle(boolean displayRowCount, boolean displayItemCount) {

        String title = this.titre;

        if (displayRowCount) {
            final int rowCount = this.panelEcritures.getListe().getRowCount();
            title += ", " + getPlural("ligne", rowCount);
            final int total = this.panelEcritures.getListe().getTotalRowCount();
            if (total != rowCount)
                title += " / " + total;
        }
        if (displayItemCount) {
            int count = this.panelEcritures.getListe().getItemCount();
            if (count >= 0)
                title += ", " + this.getPlural("élément", count);
        }
        this.setTitle(title);
    }

    public void setTitle() {
        this.setTitle(true, true);
    }

    final private void uiInit() {
        this.setTitle();
        this.getContentPane().setLayout(new GridLayout());
        this.getContentPane().add(this.panel);
        this.setBounds();
        final File configFile = new File(Configuration.getInstance().getConfDir(), this.panelEcritures.getElement().getPluralName() + "-window.xml");
        new WindowStateManager(this, configFile).loadState();
    }

    final protected void setBounds() {
        // TODO use getMaximiumWindowBounds
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode dm = ge.getDefaultScreenDevice().getDisplayMode();

        final int topOffset = 50;
        if (dm.getWidth() <= 800 || dm.getHeight() <= 600) {
            this.setLocation(0, topOffset);
            this.setSize(dm.getWidth(), dm.getHeight() - topOffset);
        } else {
            this.setLocation(10, topOffset);
            this.setSize(dm.getWidth() - 50, dm.getHeight() - 20 - topOffset);
        }
    }

    public final ListPanelEcritures getPanel() {
        return this.panelEcritures;
    }

}
