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

public class processcoordinator
{	
	private static Socket skt;
	private static int port = 4444;
	private String address = "localhost";
	private static final String dsproject = "COORDINATOR-3";
	private static final String ii = "INIT";
	private static final String	ww = "WAITING";
	private static final String	cc = "COMMMIT";
	private static final String	aa = "ABBORT";
	private static DataInputStream datain;
	private static DataOutputStream dataout;
	private ArrayList<String> listofusers;
	private HashMap<String,String> evm;
	private boolean sab = false; 
	private Timer timer;
	private String status;
	private JTextArea tt;
	private JTextField tf;
	private JFrame ff;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					processcoordinator disp = new processcoordinator();
					disp.ff.setVisible(true);
				}
				catch(Exception ex)
				{
				}
			}
		});
	}

	public processcoordinator()
	{
		start();
	}

	private void start()
	{
		ff = new JFrame();
		ff.setTitle("COORDINATOR3");
		ff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ff.setBounds(600,0,420,350);
		ff.getContentPane().setLayout(null);
		JLabel jl = new JLabel("Please enter any value to be asked to the participants:");
		jl.setBounds(40,15,350,30);
		ff.getContentPane().add(jl);
		tf = new JTextField();
		tf.setBounds(40,45,230,30);
		ff.getContentPane().add(tf);
		tf.setColumns(20);
		JButton jb1 = new JButton("ENTER");
		jb1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String ask = tf.getText();
				if(ask.equals(null)||ask.trim().isEmpty())
				{		
				} 
				else
				{
					try
					{
						StringBuilder sb1 = new StringBuilder();
						sb1.append("FINISHED /").append(ask+":ASKING_FOR_VOTE").append("\n");
						dataout.writeUTF(sb1.toString());
						status = ww;
						sab = false;
						timer = new Timer();
						TimerTask timertask = new TimerTask()
						{
							@Override
							public void run()
							{
								int zz = 0;
								for(String people:evm.keySet())
								{
									if(evm.get(people).equals("COMMMIT"))
									{
										zz++;
									}
									else
									{
										break;
									}
								}
								if(zz!=evm.size())
								{
									tt.append("Timer has passed so now GlobalAbort\n");
									StringBuilder sb2 = new StringBuilder();
									sb2.append("FINISHED /").append("GLOBALABORT:"+dsproject).append("\n");
									try
									{
										dataout.writeUTF(sb2.toString());
									}
									catch(IOException ex)
									{
									}
									for(String imm:evm.keySet())
									{
										evm.put(imm,"");
									}
									status = aa;
								}
							}
						};
						timer.schedule(timertask,20000);
					}
					catch(IOException ex)
					{
					}
					tf.setText("");
				}
			}
		});
		jb1.setBounds(280,45,80,30);
		ff.getContentPane().add(jb1);		
		JScrollPane scrolarea = new JScrollPane();
		scrolarea.setBounds(40,90,320,200);
		ff.getContentPane().add(scrolarea);
		tt = new JTextArea();
		scrolarea.setViewportView(tt);
		coordinate();
	}

	private void coordinate()
	{
		try
		{
			skt = new Socket(address,port);
			listofusers = new ArrayList<>();
			status = ii;
			evm = new HashMap<>();
			datain = new DataInputStream(skt.getInputStream());	
			dataout = new DataOutputStream(skt.getOutputStream());
			StringBuilder sb3 = new StringBuilder();
			sb3.append("JOINED /").append("{"+dsproject+"}").append("\n");
			dataout.writeUTF(sb3.toString());	
		} 		
		catch(IOException ex)
		{
		}
		new coordinating().start();
	}

	public class coordinating extends Thread
	{
		@Override
		public void run()
		{
			String view="",ray[],givinginput,imp;
			try
			{
				while((view=datain.readUTF())!=null)
				{
					ray = view.split("\n");
					givinginput = ray[0].split("/")[1];
					if(ray[0].contains("FINISHED"))
					{
						if(givinginput.contains("NEW_CONNECT"))
						{
							imp = givinginput.split(":")[1];
							if(!(imp.equalsIgnoreCase(dsproject)))
							{
								listofusers.add(imp);
								evm.put(imp,"");
								tt.append("New Participant Joined : "+imp+"\n");
							}	
						}
						else if(givinginput.contains("COMMMIT"))
						{
							int yy = 0;
							imp = givinginput.split(":")[1];
							evm.put(imp,"COMMMIT");
							tt.append("Participant Voted Commit: "+imp+"\n");
							for(String sss:evm.keySet())
							{
								if(evm.get(sss).equals("COMMMIT"))
								{
									yy++;
								}
								else
								{
									break;
								}
							}
							if(yy==(evm.size()))
							{
								status = cc;
								timer.cancel();
								tt.append("All participants voted Commit so now GlobalCommit\n");
								StringBuilder sb5 = new StringBuilder();
								sb5.append("FINISHED /").append("GLOBALCOMMIT:"+dsproject).append("\n");
								dataout.writeUTF(sb5.toString());
								for(String comp:evm.keySet())
								{
									evm.put(comp,"");
								}
							}
						}
						else if(givinginput.contains("ABBORT"))
						{
							imp = givinginput.split(":")[1];
							evm.put(imp,"ABBORT");
							status = aa;
							timer.cancel();
							tt.append("Participant Voted Abort: "+imp+"\n");
							tt.append("Any one client voted Abort so now GlobalAbort\n");
							StringBuilder sb4 = new StringBuilder();
							sb4.append("FINISHED /").append("GLOBALABORT:"+dsproject).append("\n");
							dataout.writeUTF(sb4.toString());
							for(String comp:evm.keySet())
							{
								evm.put(comp,"");
							}
						}
						else
						{
						}
					}
				}
			}
			catch(IOException ex)
			{
			}
		}
	}
}


