package ku.decathlon.ui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;


@SuppressWarnings("serial")
public class DecathlonUI extends JFrame {

    private static final String[][] labels =new String[][] {
    		{"100 m jooks", "1"},
    		{"Kaugushüpe", "2"},
    		{"Kuulitõuge", "3"},
    		{"Kõrgushüpe", "4"},
    		{"400 m jooks", "5"},
    		{"110 m tõkkejooks", "6"},
    		{"Kettaheide", "7"},
    		{"Teivashüpe", "8"},
    		{"Odavise", "9"},
    		{"1500 m jooks", "10"}    		
    };
    
    private class PointsField{
    	private final JLabel label;
    	private final JTextField result;
    	private final JLabel status;
    	private final JTextField points;
		public PointsField(JLabel label,JTextField result, JLabel status,JTextField points) {
			this.label= label;
			this.result = result;
			this.status = status;
			this.points = points;
		}
    }
    
    private final PointsField[] pointFields = new PointsField[labels.length + 1];
    private JComboBox<String> sex;
    private JComboBox<String> timing;
    
    private final DecathlonCalculator calculator; 
    public DecathlonUI(DecathlonCalculator calculator) {
    	this.calculator = calculator;
        initUI();
    }
    private void initUI() {
		JMenuBar menuBar = new JMenuBar();
	
	    JMenu optionsMenu = new JMenu("Valikud");
	    optionsMenu.setMnemonic(KeyEvent.VK_L);
	
	    JMenuItem serverMenuItem = new JMenuItem("Server");
	    serverMenuItem.setMnemonic(KeyEvent.VK_S);
	    serverMenuItem.addActionListener(event -> createServerOptionsDialog());
	     
	    JMenuItem exitMenuItem = new JMenuItem("Välju");
	    exitMenuItem.setMnemonic(KeyEvent.VK_V);
	    exitMenuItem.addActionListener(event -> System.exit(0));
	
	    optionsMenu.add(serverMenuItem);
	    optionsMenu.addSeparator();
	    optionsMenu.add(exitMenuItem);
	    menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
         
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        JButton calcButton = new JButton("Arvuta");
        JSeparator separator =new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 1) );

        calcButton.addActionListener(event -> calculate());
        
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        
        sex = new JComboBox<>();
        sex.addItem("Mehed");
        sex.addItem("Naised");
        sex.addItemListener(e->{
        	if (e.getStateChange() == ItemEvent.SELECTED) {
                if("Naised".equals(e.getItem().toString())) {
                	pointFields[5].label.setText("100 m tõkkejooks");
                }else{
                	pointFields[5].label.setText(labels[5][0]);                	
                }
            }       
        });
        
        JLabel timingLabel = new JLabel("Ajavõtt");
        timing = new JComboBox<>();
        timing.addItem("Automaatne");
        timing.addItem("Käsitsi");

        Group labelGroup = gl.createParallelGroup(TRAILING);
        Group fieldGroup = gl.createParallelGroup(TRAILING);
        Group pointsGroup = gl.createParallelGroup(TRAILING);
        Group statusGroup = gl.createParallelGroup(LEADING);
        SequentialGroup verticalGroup = gl.createSequentialGroup();

        verticalGroup.addGroup(gl.createParallelGroup()
				.addComponent(timingLabel, Alignment.CENTER)
				.addComponent(timing)
				.addComponent(sex));
        
        for(int i = 0; i < labels.length; i++) {
        	pointFields[i] = addField(labels[i][0], gl, labelGroup,fieldGroup, pointsGroup, statusGroup, verticalGroup);
        }

        verticalGroup
			.addPreferredGap(RELATED,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addGroup(gl.createSequentialGroup()
        		.addComponent(separator));        
        pointFields[pointFields.length-1] = 
        		addField("Kokku", gl, labelGroup, fieldGroup, pointsGroup, statusGroup, verticalGroup);
        pointFields[pointFields.length-1].result.setVisible(false);
        
		gl.setHorizontalGroup(
				gl.createParallelGroup()
					.addGroup(gl.createSequentialGroup()
        				.addComponent(timingLabel)
        				.addComponent(timing)
        				.addComponent(sex))
					.addGroup(gl.createSequentialGroup()
        				.addGroup(labelGroup)
        				.addGroup(fieldGroup)
        				.addGroup(pointsGroup)
        				.addGroup(statusGroup))
					.addComponent(separator)
					.addGroup(gl.createSequentialGroup()
						.addComponent(calcButton))
        );
        
        gl.setVerticalGroup(verticalGroup
        		.addPreferredGap(RELATED,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(calcButton)
           		);

        pack();

        setTitle("Kümnevõistlus");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
	private void createServerOptionsDialog() {
		String res = JOptionPane.showInputDialog("Server",calculator.getServerAddress());
		calculator.setServerAddress(res);
	}
	
	private PointsField addField(String labelTxt, GroupLayout gl, Group labelGroup, Group fieldGroup, Group pointsGroup,
			Group statusGroup, SequentialGroup verticalGroup) {
        JLabel label = new JLabel(labelTxt);
        JTextField field = new JTextField(10);
        field.setHorizontalAlignment(JTextField.TRAILING);
        JLabel status = new JLabel("");
        JTextField points  = new JTextField(10);
        points.setHorizontalAlignment(JTextField.TRAILING);
        points.setEditable(false);
        points.setFocusable(false);
        status.setForeground(Color.red);
        status.setHorizontalAlignment(JLabel.LEFT);
        status.setHorizontalTextPosition(JLabel.LEFT);
        
        labelGroup.addComponent(label);
        fieldGroup.addComponent(field);
        pointsGroup.addComponent(points);
        statusGroup.addComponent(status);
   	
        verticalGroup.addGroup(gl.createParallelGroup(BASELINE)
                .addComponent(label)
                .addComponent(field)
                .addComponent(points)
                .addComponent(status));
		return new PointsField(label, field, status, points);
    }
    
    private void calculate() {
    	String[][] fields = new String[labels.length][2];
    	for(int i = 0; i < labels.length; i++) {
    		fields[i][0] = labels[i][1];
    		fields[i][1] = pointFields[i].result.getText();
    	}
		PointsResult[] result = calculator.calculate(fields, sex.getSelectedIndex(), timing.getSelectedIndex() );
		int sum = 0;
		for( int i = 0; i < result.length; i++) {
			if(result[i] == null) {
				pointFields[i].points.setText("");
				pointFields[i].status.setText("");
			} else {
				if(result[i].status != null) {
					pointFields[i].points.setText("");
					pointFields[i].status.setText(result[i].status);
				} else {
					sum += result[i].points;
					pointFields[i].points.setText(String.valueOf(result[i].points));
					pointFields[i].status.setText("");
				}
			}
		}
		pointFields[pointFields.length-1].points.setText(String.valueOf(sum));
		pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            DecathlonUI ex = new DecathlonUI(new DecathlonCalculator());
            ex.setVisible(true);
        });
    }
}
