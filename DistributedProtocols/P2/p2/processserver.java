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

public class processserver
{
	private static Socket skt;
	private static ServerSocket socketserver;
	private static int portnumber = 4444;
	private static final String dsproject = "COORDINATOR-3";
	private ArrayList<String> lu;
	private ArrayList<DataOutputStream> ls;
	private static DataOutputStream dataout;
	private DataOutputStream central;
	private JTextArea tt;
	private JFrame ff;
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					processserver disp = new processserver();
					disp.ff.setVisible(true);
				}
				catch(Exception ex)
				{
				}
			}
		});
	}

	public processserver()
	{
		start();
	}

	private void start()
	{
		ff = new JFrame();
		ff.setTitle("SERVER3");
		ff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ff.setBounds(0,0,600,700);
		ff.getContentPane().setLayout(null);		
		JScrollPane scrolarea = new JScrollPane();
		scrolarea.setBounds(20,20,545,600);
		ff.getContentPane().add(scrolarea);
		tt = new JTextArea();
		scrolarea.setViewportView(tt);
		Thread thread = new Thread ()
		{
			public void run()
			{
				serverstart();
			};
		};
		thread.start();
	}

	protected void serverstart()
	{
		try
		{
			ls = new ArrayList<>();
			lu = new ArrayList<>();
			socketserver = new ServerSocket(portnumber);			
			tt.append("SERVER STARTED SUCCESSFULLY...\n");
			while(true)
			{
				skt = socketserver.accept();
				dataout = new DataOutputStream(skt.getOutputStream());
				ls.add(dataout);
				Coordination coord = new Coordination(skt,dataout);
				coord.start();
			}
		}
		catch(IOException ex)
		{
		}
	}

	private void gotop(String carry)
	{
		StringBuilder rr = new StringBuilder();
		for(DataOutputStream dataOutputStream:ls)
		{
			try 
			{
				rr.append("FINISHED /").append(carry).append("\n");
				dataOutputStream.writeUTF(rr.toString());
			} 
			catch(Exception ex)
			{
			}
		}
	}

	private void gotoc(String darry)
	{
		StringBuilder qq = new StringBuilder();
		try
		{
			qq.append("FINISHED /").append(darry).append("\n");
			central.writeUTF(qq.toString());
		}
		catch(IOException ex)
		{
		}
	}

	public class Coordination extends Thread
	{
		private String yy;
		private Socket sktc;
		private DataInputStream inputs;
		public Coordination(Socket skt,DataOutputStream outputs)
		{
			this.sktc = skt;
			try
			{
				inputs = new DataInputStream(sktc.getInputStream());
			}
			catch(Exception ex)
			{
			}
		}
		@Override
		public void run()
		{
			String givinginput="",ray[],back;
			try
			{
				while((givinginput=inputs.readUTF())!=null)
				{
					tt.append(givinginput);
					ray = givinginput.split("\n");
					back = ray[0].split("/")[1];
					if(ray[0].contains("JOINED"))
					{
						if(back.contains("{"))
						{
							yy = back.split("\\{")[1];
							yy = yy.replace(yy.substring(yy.length()-1),"");
							if(!yy.equalsIgnoreCase(dsproject))
							{
								lu.add(yy);
							} 
							else
							{
								if(ls.size()==1)
								{
									central = new DataOutputStream(ls.remove(0));
								}
								else
								{
									central = new DataOutputStream(ls.remove(ls.size()-1));
								}
							}
						}
					}
					else if(ray[0].contains("FINISHED"))
					{
						if(back.contains("ASKING_FOR_VOTE"))
						{	
							gotop(back);
						}
						else if(back.equals("COMMMIT"))
						{
							gotoc("COMMMIT"+":"+yy+":");
						}
						else if(back.equals("ABBORT"))
						{
							gotoc("ABBORT"+":"+yy+":");
						}
						else if(back.contains("GLOBALCOMMIT"))
						{
							String cmt = back.split(":")[0];
							gotop(cmt);
						}
						else if(back.contains("GLOBALABORT"))
						{
							String abt = back.split(":")[0];
							gotop(abt);
						}	
					}
				}
			}
			catch(IOException ex)
			{
				if(!yy.equals(dsproject))
				{
					lu.remove(yy);
				}
			}
		}
	}
}



