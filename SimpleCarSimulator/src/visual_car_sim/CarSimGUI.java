package visual_car_sim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import core_car_sim.LoadWorld;
import core_car_sim.Point;
import core_car_sim.WorldSim;
import examples.ExampleAICar;
import examples.ExampleTestingCar;
import core_car_sim.AbstractCar;
import core_car_sim.CarAddedListener;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;


public class CarSimGUI
{
	public class Simulate extends TimerTask
	{
		@Override
		public void run()
		{
			if (simulateLimit != 0)
			{
				if (currentlySimulated < simulateLimit)
				{
					currentlySimulated++;
					simworld.simulate(1);
					pnlWorld.revalidate();
					pnlWorld.repaint();
					lblNewLabel.setText("" + currentlySimulated);
				}
				else
				{
					simTimer.cancel();
				}
			}
			else
			{
				simworld.simulate(1);
				if (simworld.allFinished())
				{
					simTimer.cancel();
				}
			}
		}
		
	};
	
	private JFrame frame;
	private JLabel lblNewLabel;
	private JFileChooser loadWorldDialog = new JFileChooser();
	private WorldSim simworld;
	private JPanel pnlWorld = new JPanel();
	private Timer simTimer;
	private int currentlySimulated = 0;
	private int simulateLimit;
	private CarAddedListener cal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CarSimGUI window = new CarSimGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CarSimGUI() {
		initialize();
		cal = new CarAddedListener() {
			@Override
			public AbstractCar createCar(String name, Point startingLoca)
			{
				//AI controlled car (car not tested)
				return new ExampleAICar(startingLoca, System.getProperty("user.dir") + "\\resources\\bluecar.png");
			}
	
			@Override
			public AbstractCar createCar(String name, Point startingLoca, String[] information)
			{
				Point finishLocation = new Point(0,0);
				finishLocation.setX(Integer.parseInt(information[0]));
				finishLocation.setY(Integer.parseInt(information[1]));
				return new ExampleTestingCar(startingLoca, System.getProperty("user.dir") + "\\resources\\redcar.png", finishLocation);
			}
		};
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 966, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Load Simulation");
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Run Simulation");
		panel.add(btnNewButton_1);
		
		ButtonGroup bg = new ButtonGroup();
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("until finished");
		rdbtnNewRadioButton.setSelected(true);
		panel.add(rdbtnNewRadioButton);
		bg.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("set number");
		panel.add(rdbtnNewRadioButton_1);
		bg.add(rdbtnNewRadioButton_1);
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		panel.add(spinner);
		
		lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel);
	
		frame.getContentPane().add(pnlWorld, BorderLayout.CENTER);
		pnlWorld.setLayout(new GridLayout(3, 3, 0, 0));
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/*if (loadWorldDialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					{
						BufferedReader br = new BufferedReader(new FileReader(loadWorldDialog.getSelectedFile()));
						simworld = LoadWorld.loadWorldFromFile(br, cal);
						generateGUIWorld();
					}*/
					//While testing
					BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\bin\\examples\\ExampleWorldFile.txt"));
					simworld = LoadWorld.loadWorldFromFile(br, cal);
					generateGUIWorld();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (rdbtnNewRadioButton.isSelected())
				{
					simulateLimit = 0;
				}
				else
				{
					simulateLimit = (Integer)spinner.getValue();
				}
				currentlySimulated = 0;
				simTimer = new Timer();
				simTimer.schedule(new Simulate(), 0, 1000);//Once every second
			}
		});
	}
	
	private void generateGUIWorld()
	{
		pnlWorld.setLayout(new GridLayout(simworld.getHeight(), simworld.getWidth(), 1, 1));
		for (int y = 0; y < simworld.getHeight(); y++)
		{
			for (int x = 0; x < simworld.getWidth(); x++)
			{
				pnlWorld.add(simworld.getCell(x, y));
			}
		}
		for (AbstractCar car : simworld.getCars())
		{
			Point p = simworld.getCarPosition(car);
			JLabel icon = new JLabel(car.getCarIcon());
			icon.setSize(simworld.getCell(p.getX(), p.getY()).getWidth(), simworld.getCell(p.getX(), p.getY()).getHeight());
			simworld.getCell(p.getX(), p.getY()).add(icon);
		}
		pnlWorld.revalidate();
		pnlWorld.repaint();
	}

	

}
