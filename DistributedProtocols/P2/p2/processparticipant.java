package p2;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class processparticipant
{
	private Socket skt;
	private int port = 4444;
	private String address = "localhost";
	private String status = "ININ";
	private static final String	cc = "COMMMIT";
	private static final String	aa = "ABBORT";
	private String what = "";
	private String imp = "";
	private static String machine;
	private static DataInputStream datain;
	private DataOutputStream dataout;
	private boolean sab = false;
	private Timer timer;
	private BufferedReader boor;
	private BufferedWriter boow;
	private FileReader foor;
	private FileWriter foow;
	private static JTextArea tt;
	private JTextField tf;
	private JFrame ff;
	private JLabel jl;
	private JButton jb;
	private JButton jc;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					processparticipant disp = new processparticipant();
					disp.ff.setVisible(true);
				}
				catch (Exception ex)
				{
				}
			}
		});
	}

	public processparticipant()
	{
		start();
	}

	private void start()
	{
		ff = new JFrame();
		ff.setTitle("PARTICIPANT3");
		ff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ff.setBounds(600,350,420,350);
		ff.getContentPane().setLayout(null);
		JLabel jll = new JLabel("Please enter name of the participant:");
		jll.setBounds(40,15,250,30);
		ff.getContentPane().add(jll);
		tf = new JTextField();
		tf.setBounds(40,45,230,30);
		ff.getContentPane().add(tf);
		tf.setColumns(20);
		JButton jb1 = new JButton("ENTER");
		jb1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(sab==true)
				{
				}
				else if(sab==false)
				{
					machine = tf.getText().trim();
					if(machine.equals(null)||machine.trim().isEmpty())
					{
					}
					else
					{
						participate();
						timer = new Timer();
						TimerTask timertask = new TimerTask()
						{
							@Override
							public void run()
							{
								if(imp.equals("")&&status.equals("ININ"))
								{
									tt.append("Timer has passed so Aborting here\n");
									status="ABBORT";
								}
							}
						};
						timer.schedule(timertask,20000);
					}
				}
			}
		});
		jb1.setBounds(280,45,80,30);
		ff.getContentPane().add(jb1);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40,90,320,150);
		ff.getContentPane().add(scrollPane);
		tt = new JTextArea();
		scrollPane.setViewportView(tt);
		tt.setEditable(false);
		jc = new JButton("Commit It!");
		jc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if((status.equals("TON")||(imp.trim().isEmpty()||imp.equals(""))))
				{
				}
				else
				{
					try
					{
						StringBuilder sb1 = new StringBuilder();
						sb1.append("FINISHED /").append("COMMMIT").append("\n");
						dataout.writeUTF(sb1.toString());
						tt.append("Your vote is Commit\n");
						what = "";
						status="TON";
						timer = new Timer();
						TimerTask timertask = new TimerTask()
						{
							@Override
							public void run() 
							{
								if(!(what.equals("xx")||what.equals("yy")))
								{
									tt.append("Timer has passed so Aborting here\n");
									status="ABBORT";
									imp = "";
								}
							}
						};
						timer.schedule(timertask,20000);
					}
					catch(IOException ex)
					{
					}
				}
			}
		});
		jc.setBounds(40,250,100,30);
		ff.getContentPane().add(jc);
		jb = new JButton("Abort It!");
		jb.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(!(imp.equals("")||imp.trim().isEmpty()))
				{
					try
					{
						StringBuilder sb2 = new StringBuilder();
						sb2.append("FINISHED /").append("ABBORT").append("\n");
						dataout.writeUTF(sb2.toString());
						tt.append("Your vote is Abort\n");
						status = "ABBORT";
						imp = "";
					}
					catch(IOException ex)
					{
					}
				}
				else
				{
				}
			}
		});
		jb.setBounds(260,250,100,30);
		ff.getContentPane().add(jb);
	}

	private void participate()
	{
		try
		{
			skt = new Socket(address,port);
			datain = new DataInputStream(skt.getInputStream());	
			dataout = new DataOutputStream(skt.getOutputStream());
			String str = "";
			File file = new File(machine);
			file.createNewFile();
			StringBuilder sb3 = new StringBuilder();
			sb3.append("JOINED /").append("{"+machine+"}").append("\n");
			dataout.writeUTF(sb3.toString());
			tt.append("You are joined...\n");
			sab = true;
			foor = new FileReader(file);
			boor = new BufferedReader(foor);
			while(!((str=boor.readLine())==null))
			{
			}
			boor.close();
			foow = new FileWriter(file,true);
			boow = new BufferedWriter(foow);
		} 	
		catch(FileNotFoundException ex)
		{
		}
		catch(IOException ex)
		{
		}
		new participation().start();
	}

	public class participation extends Thread
	{
		@Override
		public void run()
		{
			String find ="",ray[],givinginput;
			try
			{
				while(true)
				{
					find = datain.readUTF();
					ray = find.split("\n");
					givinginput = ray[0].split("/")[1];
					if(ray[0].contains("FINISHED"))
					{
						if(givinginput.equalsIgnoreCase("GLOBALCOMMIT"))
						{
							what = "xx";
							status="COMMMIT";
							tt.append("Global Committing!\n");
							boow.write(imp);
							boow.newLine();
							boow.flush();
							imp = "";
						}
						else if(givinginput.equalsIgnoreCase("GLOBALABORT"))
						{
							what = "yy";
							status="ABBORT";
							tt.append("Global Aborting!\n");
							imp = "";
						}
						else 
						{
							imp = givinginput.split(":")[0];
							tt.append("Please vote now for: "+imp+"\n");
							timer.cancel();
						}
					}	
				}
			}
			catch(Exception ex)
			{
			}
		}
	}
}

