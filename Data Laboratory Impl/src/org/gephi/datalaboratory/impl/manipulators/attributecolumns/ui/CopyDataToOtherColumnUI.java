/*
Copyright 2008-2010 Gephi
Authors : Eduardo Ramos <eduramiba@gmail.com>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.datalaboratory.impl.manipulators.attributecolumns.ui;

import java.util.ArrayList;
import javax.swing.JPanel;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.datalaboratory.api.AttributeColumnsController;
import org.gephi.datalaboratory.impl.manipulators.attributecolumns.CopyDataToOtherColumn;
import org.gephi.datalaboratory.spi.DialogControls;
import org.gephi.datalaboratory.spi.attributecolumns.AttributeColumnsManipulator;
import org.gephi.datalaboratory.spi.attributecolumns.AttributeColumnsManipulatorUI;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * UI for CopyDataToOtherColumn AttributeColumnsManipulator
 * @author Eduardo Ramos <eduramiba@gmail.com>
 */
public class CopyDataToOtherColumnUI extends javax.swing.JPanel implements AttributeColumnsManipulatorUI{
    CopyDataToOtherColumn manipulator;
    AttributeColumn[] columns;

    /** Creates new form CopyDataToOtherColumnUI */
    public CopyDataToOtherColumnUI() {
        initComponents();
    }

    public void setup(AttributeColumnsManipulator m, AttributeTable table, AttributeColumn column, DialogControls dialogControls) {
        this.manipulator=(CopyDataToOtherColumn) m;

        sourceColumnLabel.setText(NbBundle.getMessage(CopyDataToOtherColumnUI.class, "CopyDataToOtherColumnUI.sourceColumnLabel.text",column.getTitle()));

        AttributeColumnsController ac=Lookup.getDefault().lookup(AttributeColumnsController.class);

        ArrayList<AttributeColumn> availableColumns=new ArrayList<AttributeColumn>();

        for(AttributeColumn c:table.getColumns()){
            if(ac.canChangeColumnData(c)&&c!=column){
                availableColumns.add(c);
                columnsComboBox.addItem(c.getTitle());
            }
        }

        columns=availableColumns.toArray(new AttributeColumn[0]);
    }

    public void unSetup() {
        if(columnsComboBox.getSelectedIndex()!=-1){
            manipulator.setTargetColumn(columns[columnsComboBox.getSelectedIndex()]);
        }else{
            manipulator.setTargetColumn(null);
        }
    }

    public String getDisplayName() {
        return manipulator.getName();
    }

    public JPanel getSettingsPanel() {
        return this;
    }

    public boolean isModal() {
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        descriptionLabel = new javax.swing.JLabel();
        columnsComboBox = new javax.swing.JComboBox();
        sourceColumnLabel = new javax.swing.JLabel();

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText(org.openide.util.NbBundle.getMessage(CopyDataToOtherColumnUI.class, "CopyDataToOtherColumnUI.descriptionLabel.text")); // NOI18N

        sourceColumnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sourceColumnLabel.setText(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(columnsComboBox, 0, 192, Short.MAX_VALUE)
                    .addComponent(sourceColumnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sourceColumnLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(descriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(columnsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox columnsComboBox;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel sourceColumnLabel;
    // End of variables declaration//GEN-END:variables

}
