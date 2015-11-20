/*
 * @author Loc Truong
 */
package main;
public class MainFrame extends javax.swing.JFrame {
    private static MainFrame instance = null;
    public MainFrame(){
        initComponents();        
    }
    public static MainFrame getInstance(){
        if(MainFrame.instance == null){
            MainFrame.instance = new MainFrame();
        }
        return MainFrame.instance;
    }
    public static AgentPanel getAgentPanel(){
        return agentPanel1;
    }
    public static ControllerPanel getControllerPanel(){
        return controllerPanel1;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        controllerPanel1 = new main.ControllerPanel();
        agentPanel1 = new main.AgentPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setLeftComponent(controllerPanel1);
        jSplitPane1.setRightComponent(agentPanel1);
        jSplitPane1.setResizeWeight(.5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static main.AgentPanel agentPanel1;
    private static main.ControllerPanel controllerPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
}
