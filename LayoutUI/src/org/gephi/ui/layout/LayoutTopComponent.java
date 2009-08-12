/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.layout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import org.gephi.layout.api.Layout;
import org.gephi.layout.api.LayoutBuilder;
import org.gephi.layout.api.LayoutController;
import org.gephi.layout.api.LayoutControllerObserver;
import org.gephi.layout.api.LayoutProperty;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.explorer.view.ChoiceView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Node.PropertySet;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class LayoutTopComponent extends TopComponent
    implements LayoutControllerObserver, ExplorerManager.Provider,
               PropertyChangeListener {

    private static LayoutTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "LayoutTopComponent";
    private LayoutController layoutController;
    private final Action requestPlayAction = new RequestPlayAction();
    private final Action requestStopAction = new RequestStopAction();
    private final Action chooseLayoutAction = new ChooseLayoutAction();
    private final Action addLayoutAction = new AddLayoutAction();
    private final Action deleteLayoutAction = new DeleteLayoutAction();
    private final Action resetLayoutAction = new ResetLayoutAction();
    private final RootNode rootNode = new RootNode();
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final PropertySheet propertySheet = new PropertySheet();

    private LayoutTopComponent() {
        initLayoutController();
        initComponents();
        ActionMap map = this.getActionMap();
        explorerManager.setRootContext(new AbstractNode(rootNode));
        explorerManager.addPropertyChangeListener(this);

        // following line tells the top component which lookup should be associated with it
//        associateLookup(ExplorerUtils.createLookup(explorerManager, map));
        setName(NbBundle.getMessage(LayoutTopComponent.class, "CTL_LayoutTopComponent"));
        setToolTipText(NbBundle.getMessage(LayoutTopComponent.class, "HINT_LayoutTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        putClientProperty("netbeans.winsys.tc.keep_preferred_size_when_slided_in", Boolean.TRUE);
    }

    private void initLayoutController() {
        layoutController = Lookup.getDefault().lookup(LayoutController.class);
        layoutController.addObserver(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("selectedNodes".equals(evt.getPropertyName())) {
            propertySheet.setNodes(explorerManager.getSelectedNodes());
            layoutController.setLayout(getSelectedLayout());
        } else {
            System.out.println("evt: " + evt.getPropertyName());
        }
    }

    public Layout getSelectedLayout() {
        Node[] selectedNodes = explorerManager.getSelectedNodes();

        if (selectedNodes.length > 0) {
            LayoutNode node = (LayoutNode) selectedNodes[0];
            return node.getLayout();
        } else {
            return null;
        }

    }

    public class LayoutNode extends AbstractNode {

        private String valor,  valor2;
        private Layout layout;

        public void setValor(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public LayoutNode(Layout layout) {
            super(Children.LEAF);
            this.layout = layout;
            setName(layout.getBuilder().getName());
            setShortDescription(layout.getBuilder().getDescription());
            setValor(layout.getBuilder().getName());
        }

        @Override
        public PropertySet[] getPropertySets() {
            try {
                return layout.getPropertySets();
//            Sheet.Set set = Sheet.createPropertiesSet();
//
//            try {
//                set.put(LayoutProperty.createProperty(
//                    this, String.class, "Valor", "Valor DESC", "getValor",
//                    "setValor"));
//            } catch (NoSuchMethodException ex) {
//                ex.printStackTrace();
//            }
//            Sheet.Set set2 = Sheet.createPropertiesSet();
//            set2.setDisplayName("SET2");
//
//            Property indexProp;
//            try {
//                indexProp = new PropertySupport.Reflection(this, String.class, "getValor", "setValor");
//                indexProp.setName("valor");
//                set2.put(indexProp);
//            } catch (NoSuchMethodException ex) {
//                ex.printStackTrace();
//            }
//            return new PropertySet[]{set, set2};
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
                return null;
            }
//            Sheet.Set set = Sheet.createPropertiesSet();
//
//            try {
//                set.put(LayoutProperty.createProperty(
//                    this, String.class, "Valor", "Valor DESC", "getValor",
//                    "setValor"));
//            } catch (NoSuchMethodException ex) {
//                ex.printStackTrace();
//            }
//            Sheet.Set set2 = Sheet.createPropertiesSet();
//            set2.setDisplayName("SET2");
//
//            Property indexProp;
//            try {
//                indexProp = new PropertySupport.Reflection(this, String.class, "getValor", "setValor");
//                indexProp.setName("valor");
//                set2.put(indexProp);
//            } catch (NoSuchMethodException ex) {
//                ex.printStackTrace();
//            }
//            return new PropertySet[]{set, set2};
        }

        public Layout getLayout() {
            return layout;
        }
    }

    class RootNode extends Children.Keys<Layout> {

        private ArrayList<Layout> layouts;

        public RootNode() {
            layouts = new ArrayList<Layout>();
        }

        @Override
        protected void addNotify() {
            setKeys(layouts);
        }

        @Override
        protected void removeNotify() {
            setKeys(Collections.EMPTY_SET);
        }

        public void addLayout(Layout layout) {
            layouts.add(layout);
            setKeys(layouts);
        }

        public void removeLayout(Layout layout) {
            layouts.remove(layout);
            setKeys(layouts);
        }

        @Override
        protected Node[] createNodes(Layout layout) {
            return new Node[]{new LayoutNode(layout)};
        }
    }

    private void initLayoutComboBox() {
        layoutSourceComboBox.setAction(chooseLayoutAction);
        List<LayoutBuilder> layouts = layoutController.getLayouts();
        System.out.println("layouts: " + layouts.size());
        for (LayoutBuilder layoutBuilder : layouts) {
            layoutSourceComboBox.addItem(new LayoutBuilderWrapper(layoutBuilder));
            System.out.println(layoutBuilder.getClass().getName());
            System.out.println(layoutBuilder.getName());
            System.out.println(layoutBuilder.getDescription() + "\n");
        }
        chooseLayoutAction.actionPerformed(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topLabel = new javax.swing.JLabel();
        layoutSourcePanel = new javax.swing.JPanel();
        layoutSourceComboBox = new javax.swing.JComboBox();
        addLayoutButton = new javax.swing.JButton(addLayoutAction);
        layoutsPanel = new javax.swing.JPanel();
        layoutComboBox = new ChoiceView();
        propertySheetPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton(requestPlayAction);
        stopButton = new javax.swing.JButton(requestStopAction);
        deleteButton = new javax.swing.JButton(deleteLayoutAction);
        resetButton = new javax.swing.JButton(resetLayoutAction);

        org.openide.awt.Mnemonics.setLocalizedText(topLabel, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.topLabel.text")); // NOI18N

        layoutSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.layoutSourcePanel.border.title"))); // NOI18N

        layoutSourceComboBox.setFont(new java.awt.Font("Tahoma", 1, 14));
        initLayoutComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(addLayoutButton, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.addLayoutButton.text")); // NOI18N

        javax.swing.GroupLayout layoutSourcePanelLayout = new javax.swing.GroupLayout(layoutSourcePanel);
        layoutSourcePanel.setLayout(layoutSourcePanelLayout);
        layoutSourcePanelLayout.setHorizontalGroup(
            layoutSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutSourcePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layoutSourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addLayoutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layoutSourcePanelLayout.setVerticalGroup(
            layoutSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutSourcePanelLayout.createSequentialGroup()
                .addGroup(layoutSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addLayoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(layoutSourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.layoutsPanel.border.title"))); // NOI18N
        layoutsPanel.setFocusCycleRoot(true);

        propertySheetPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout propertySheetPanelLayout = new javax.swing.GroupLayout(propertySheetPanel);
        propertySheetPanel.setLayout(propertySheetPanelLayout);
        propertySheetPanelLayout.setHorizontalGroup(
            propertySheetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
        propertySheetPanelLayout.setVerticalGroup(
            propertySheetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        propertySheetPanel.setLayout(new FlowLayout());
        propertySheetPanel.add(propertySheet);

        org.openide.awt.Mnemonics.setLocalizedText(playButton, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.playButton.text")); // NOI18N
        playButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.playButton.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(stopButton, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.stopButton.text")); // NOI18N
        stopButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.stopButton.toolTipText")); // NOI18N
        stopButton.setActionCommand(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.stopButton.actionCommand")); // NOI18N
        stopButton.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.deleteButton.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(resetButton, org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.resetButton.text")); // NOI18N
        resetButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayoutTopComponent.class, "LayoutTopComponent.resetButton.toolTipText")); // NOI18N

        javax.swing.GroupLayout layoutsPanelLayout = new javax.swing.GroupLayout(layoutsPanel);
        layoutsPanel.setLayout(layoutsPanelLayout);
        layoutsPanelLayout.setHorizontalGroup(
            layoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layoutsPanelLayout.createSequentialGroup()
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(playButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton))
                    .addComponent(layoutComboBox, 0, 262, Short.MAX_VALUE)
                    .addComponent(propertySheetPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layoutsPanelLayout.setVerticalGroup(
            layoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(layoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(propertySheetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stopButton)
                    .addComponent(playButton)
                    .addComponent(deleteButton)
                    .addComponent(resetButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(layoutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(topLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(layoutSourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layoutSourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layoutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLayoutButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JComboBox layoutComboBox;
    private javax.swing.JComboBox layoutSourceComboBox;
    private javax.swing.JPanel layoutSourcePanel;
    private javax.swing.JPanel layoutsPanel;
    private javax.swing.JButton playButton;
    private javax.swing.JPanel propertySheetPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel topLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized LayoutTopComponent getDefault() {
        if (instance == null) {
            instance = new LayoutTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the LayoutTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized LayoutTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(LayoutTopComponent.class.getName()).warning(
                "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof LayoutTopComponent) {
            return (LayoutTopComponent) win;
        }
        Logger.getLogger(LayoutTopComponent.class.getName()).warning(
            "There seem to be multiple components with the '" + PREFERRED_ID +
            "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public void executeLayoutEvent() {
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    public void stopLayoutEvent() {
        stopButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return LayoutTopComponent.getDefault();
        }
    }

    class ResetLayoutAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Layout layout = getSelectedLayout();
            if (layout != null) {
                layout.resetPropertiesValues();
            }
        }
    }

    class DeleteLayoutAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Layout layout = getSelectedLayout();
            if (layout != null) {
                rootNode.removeLayout(layout);
                layoutController.setLayout(null);
            }
        }
    }

    class AddLayoutAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            LayoutBuilderWrapper selected = (LayoutBuilderWrapper) layoutSourceComboBox.getSelectedItem();
            Layout layout = selected.getLayoutBuilder().buildLayout();
            System.out.println(layout.getClass().getName());
            rootNode.addLayout(layout);
//            propertySheet.setNodes(explorerManager.getSelectedNodes());
//            if (explorerManager.getSelectedNodes().length > 0) {
//                Node node = explorerManager.getSelectedNodes()[0];
//                PropertySet prop = node.getPropertySets()[0];
//                System.out.println("Properties: ");
//                for (Property p : prop.getPropertySets()) {
//                    System.out.println(p.getDisplayName());
//                }
//            }
        }
    }

    class RequestPlayAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            layoutController.executeLayout();
        }
    }

    class RequestStopAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            layoutController.stopLayout();
        }
    }

    class ChooseLayoutAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            LayoutBuilderWrapper selected = (LayoutBuilderWrapper) layoutSourceComboBox.getSelectedItem();
            Layout layout = selected.getLayoutBuilder().buildLayout();
            System.out.println(layout.getClass().getName());
            layoutController.setLayout(layout);
        }
    }
}

class LayoutBuilderWrapper {

    private LayoutBuilder layoutBuilder;

    public LayoutBuilderWrapper(LayoutBuilder layoutBuilder) {
        this.layoutBuilder = layoutBuilder;
    }

    public LayoutBuilder getLayoutBuilder() {
        return layoutBuilder;
    }

    @Override
    public String toString() {
        return layoutBuilder.getName();
    }
}
