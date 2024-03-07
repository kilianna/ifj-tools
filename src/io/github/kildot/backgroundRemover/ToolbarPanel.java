/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Panel.java to edit this template
 */
package io.github.kildot.backgroundRemover;

/**
 *
 * @author Dominik
 */
public class ToolbarPanel extends java.awt.Panel implements Params.Listener {

    Params globalParams;

    /**
     * Creates new form ToolbarPanel
     */
    public ToolbarPanel(Params params) {
        globalParams = params;
        initComponents();
        globalParams.addListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectionTypeGroup = new javax.swing.ButtonGroup();
        pointsSelection = new javax.swing.JToggleButton();
        noiseSelection = new javax.swing.JToggleButton();

        selectionTypeGroup.add(pointsSelection);
        pointsSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/io/github/kildot/backgroundRemover/res/Point.png"))); // NOI18N
        pointsSelection.setText("Points selection");
        pointsSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointsSelectionparamActionPerformed(evt);
            }
        });
        add(pointsSelection);

        selectionTypeGroup.add(noiseSelection);
        noiseSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/io/github/kildot/backgroundRemover/res/Noise.png"))); // NOI18N
        noiseSelection.setSelected(true);
        noiseSelection.setText("Noise selection");
        noiseSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noiseSelectionparamActionPerformed(evt);
            }
        });
        add(noiseSelection);
    }// </editor-fold>//GEN-END:initComponents

    private void pointsSelectionparamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointsSelectionparamActionPerformed
        updateState();
    }//GEN-LAST:event_pointsSelectionparamActionPerformed

    private void noiseSelectionparamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noiseSelectionparamActionPerformed
        updateState();
    }//GEN-LAST:event_noiseSelectionparamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton noiseSelection;
    private javax.swing.JToggleButton pointsSelection;
    private javax.swing.ButtonGroup selectionTypeGroup;
    // End of variables declaration//GEN-END:variables

    void updateState() {
        Params copy = globalParams.copy();
        copy.selectNoise = noiseSelection.isSelected();
        globalParams.set(copy, false, this);
    }

    @Override
    public void parametersChanged(long fields, boolean self) {
        if (!self && (fields & Params.SELECT_NOISE) != 0) {
            noiseSelection.setSelected(globalParams.selectNoise);
            pointsSelection.setSelected(!globalParams.selectNoise);
        }
    }

    @Override
    public void eventTriggered(Params.EventData event) {
    }
}