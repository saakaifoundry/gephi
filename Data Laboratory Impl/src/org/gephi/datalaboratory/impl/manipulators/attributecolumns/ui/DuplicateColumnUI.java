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

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.datalaboratory.impl.manipulators.attributecolumns.DuplicateColumn;
import org.gephi.datalaboratory.spi.DialogControls;
import org.gephi.datalaboratory.spi.attributecolumns.AttributeColumnsManipulator;
import org.gephi.datalaboratory.spi.attributecolumns.AttributeColumnsManipulatorUI;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.NbBundle;

/**
 * UI for DuplicateColumn AttributeColumnsManipulator.
 * @author Eduardo Ramos <eduramiba@gmail.com>
 */
public class DuplicateColumnUI extends javax.swing.JPanel implements AttributeColumnsManipulatorUI {

    private DuplicateColumn manipulator;
    private AttributeType[] availableTypes;
    private AttributeTable table;
    private DialogControls dialogControls;

    /** Creates new form DuplicateColumnUI */
    public DuplicateColumnUI() {
        initComponents();
        titleTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                refreshOkButton();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshOkButton();
            }

            public void changedUpdate(DocumentEvent e) {
                refreshOkButton();
            }

            private void refreshOkButton(){
                String text=titleTextField.getText();
                dialogControls.setOkButtonEnabled(text!=null&&!text.isEmpty()&&!table.hasColumn(text));//Title not empty and not repeated.
            }
        });
    }

    public void setup(AttributeColumnsManipulator m, AttributeTable table, AttributeColumn column, DialogControls dialogControls) {
        this.table=table;
        this.dialogControls=dialogControls;
        this.manipulator = (DuplicateColumn) m;

        descriptionLabel.setText(NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.descriptionLabel.text", column.getTitle()));
        titleTextField.setText(NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.new.title", column.getTitle()));

        availableTypes = AttributeType.values();
        int oldColumnTypeIndex = 0;
        for (int i = 0; i < availableTypes.length; i++) {
            AttributeType type = availableTypes[i];
            typeComboBox.addItem(type.getTypeString());
            if (type == column.getType()) {
                oldColumnTypeIndex = i;
            }
        }
        typeComboBox.setSelectedIndex(oldColumnTypeIndex);
    }

    public void unSetup() {
        manipulator.setColumnType(availableTypes[typeComboBox.getSelectedIndex()]);
        manipulator.setTitle(titleTextField.getText());
    }

    public String getDisplayName() {
        return manipulator.getName();
    }

    public JPanel getSettingsPanel() {
        ValidationPanel validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(this);

        ValidationGroup group = validationPanel.getValidationGroup();

        group.add(titleTextField, new ColumnTitleValidator(this));

        return validationPanel;
    }

    public boolean isModal() {
        return true;
    }

    private static class ColumnTitleValidator implements Validator<String>{
        private DuplicateColumnUI ui;

        public ColumnTitleValidator(DuplicateColumnUI ui) {
            this.ui = ui;
        }

        public boolean validate(Problems prblms, String string, String t) {
            if(t==null||t.isEmpty()){
                prblms.add(NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.title.empty"), Severity.WARNING);
                return false;
            }else if(ui.table.hasColumn(t)){
                prblms.add(NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.title.repeated"), Severity.WARNING);
                return false;
            }
            else{
                return true;
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        descriptionLabel = new javax.swing.JLabel();

        titleLabel.setText(org.openide.util.NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.titleLabel.text")); // NOI18N

        titleTextField.setText(org.openide.util.NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.titleTextField.text")); // NOI18N

        typeLabel.setText(org.openide.util.NbBundle.getMessage(DuplicateColumnUI.class, "DuplicateColumnUI.typeLabel.text")); // NOI18N

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(typeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeComboBox, 0, 199, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeLabel)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JComboBox typeComboBox;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
