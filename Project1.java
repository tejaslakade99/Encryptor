import java.util.*;
import javax.swing.UIManager;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ProgressBarUI;
import java.awt.Color;

class Encrypt extends JFrame implements ActionListener{
	Image icon = Toolkit.getDefaultToolkit().getImage("Images\\encrypted.png");
	JLabel lbl;
	Font font,font1,font2;
	JTextArea input_data,output_data;
	JButton btn;
	JFileChooser fileChooser; 
	Data d = new Data();
	public Encrypt()
	{
		super("Encryption Project");
		this.setLayout(null);
		this.getContentPane().setBackground(Color.darkGray);
		
		font = new Font("TimesNewRoman",Font.BOLD,40);     //MAIN HEADING...
	    font1 = new Font("TimesNewRoman",Font.BOLD,16);    //SMALL HEADING...
	    font2 = new Font("TimesNewRoman",Font.BOLD,20);    //SUBMAIN HEADING...
	    
	    lbl = new JLabel("Input Data");
	    lbl.setForeground(Color.white);
	    lbl.setBounds(150,210,250,50);
	    lbl.setFont(font2);
	    this.add(lbl);

	    lbl = new JLabel("Output Data");
	    lbl.setForeground(Color.white);
	    lbl.setBounds(950,210,250,50);
	    lbl.setFont(font2);
	    this.add(lbl);

	    lbl = new JLabel(" Encryption / Decryption Project...");
	    lbl.setForeground(Color.white);
	    lbl.setBounds(360,73,650,60);
	    lbl.setFont(font);
	    lbl.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED,Color.decode("#34B7F1"),Color.yellow));
	    this.add(lbl);

	    input_data= new JTextArea();
	    input_data.setBorder(BorderFactory.createLineBorder(Color.GRAY,5));
	    input_data.setFont(font2);
	    input_data.setBounds(150,270,250,320);
	    input_data.setEditable(false);
	    input_data.setLineWrap(true);
	    this.add(input_data);

	    output_data= new JTextArea();
	    output_data.setBorder(BorderFactory.createLineBorder(Color.GRAY,5));
	    output_data.setFont(font2);
	    output_data.setBounds(950,270,250,320);
	    output_data.setEditable(false);
	    output_data.setLineWrap(true);
	    this.add(output_data);

	    int btn_y = 250;
	    String btn_arr[] = {"Select File","Encrypt","Decrypt","Quit"};
	    for(int i = 0; i < btn_arr.length;i++)
	    {
	    	btn = new JButton(btn_arr[i]);
	    	btn.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,18));
	    	btn.setBounds(610,btn_y,150,40);
	    	btn.setContentAreaFilled(false);
			btn.setForeground(Color.white);
			btn.setBorder(BorderFactory.createLineBorder(Color.lightGray,3));
	    	btn.addActionListener(this);
	    	this.add(btn);
	    	btn_y+=100;
	    }

	    this.addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent e) {

	    		if (JOptionPane.showInternalConfirmDialog(null,
	    			"Are you sure you want to close this window?", "Close Window?",
	    			JOptionPane.YES_NO_OPTION,
	    			JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
	    			System.exit(0);
	    	}
	    }
	});

	    this.setResizable(false);
	    this.setIconImage(icon);
	    this.setVisible(true);
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void actionPerformed(ActionEvent e)
	{
		String caption = e.getActionCommand();

		if(caption.equals("Select File"))
		{
			//JFileChooser fileChooser = new JFileChooser(); this line is in old program
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result;
			result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) 
			{
				File selectedFile = fileChooser.getSelectedFile();
				String path= selectedFile.getAbsolutePath();
				input_data.setText(d.getString(path));
				d.data_string=d.getString(path);
				d.data_path=path;
			}
			output_data.setText("");
		}
		else if(caption.equals("Encrypt"))
		{
			String secret_key = JOptionPane.showInputDialog(this,"Enter Secret Key:");
			if (d.encrypt_data(d.data_string, secret_key) != null)
			{
	                output_data.setText(d.encrypt_data(d.data_string, secret_key));  // return ecrypted string
	                input_data.setText("");
	                String newfilepath = d.data_path.replace(".txt", "(Encrypt).txt");
	                d.data_string = d.encrypt_data(d.data_string, secret_key);
	                try {
	                	Files.deleteIfExists(Paths.get(d.data_path));
	                	PrintWriter out = new PrintWriter(new File(newfilepath));
	                	out.println(d.data_string);
	                	out.close();
	                } catch (Exception e23) {}
	            }
	        }
	        else if(caption.equals("Decrypt"))
	        {
	        	String secret_key = JOptionPane.showInputDialog(this, "Enter Secret Key:");
	        	try {
	        		if (secret_key.length() != -1)
	        		{
	        			if (d.decrypt_data(d.data_string, secret_key) != null)
	        			{
	            output_data.setText(d.decrypt_data(d.data_string, secret_key));  // return ecrypted string
	            String newfilepath = d.data_path.replace("(Encrypt).txt", ".txt");
	            d.data_string = d.decrypt_data(d.data_string, secret_key);
	            try {
	            	Files.deleteIfExists(Paths.get(d.data_path));
	            	PrintWriter out = new PrintWriter(new File(newfilepath));
	            	out.println(d.data_string);
	            	out.close();
	            } catch (Exception ee) {}
	        }	else{
	        	JOptionPane.showMessageDialog(null, "Wrong Secret Key or File!", "Error", JOptionPane.ERROR_MESSAGE);
	        	input_data.setText("");			}
	        }
	    }catch(Exception lk){}
	}
	else if(caption.equals("Quit"))
	{
		if (JOptionPane.showConfirmDialog(this,"Confirm If you Want to Exit"," Encryption / Decryption Project...",
			JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			System.exit(0);
	}

}
} 

class Data
{
	private static SecretKeySpec secretKey;
	private static byte[] key;
	public String data_string;
	public String data_path;
	String getString(String s)
	{
		String st,ret_string2;
		StringBuffer ret_string= new StringBuffer("");
		try{
			FileReader file = new FileReader(s);
			BufferedReader br = new BufferedReader(file);
			while ((st = br.readLine()) != null)
				ret_string.append(st);

			file.close();

		}catch(Exception ex){}
		ret_string2=ret_string.toString();
		data_path=s;
		data_string=ret_string2;

		return ret_string2;
	}

	public static void setKey(String myKey)
	{
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		}
		catch (NoSuchAlgorithmException e){ e.printStackTrace();}
		catch (UnsupportedEncodingException e){ e.printStackTrace();}
	}

	//ENCRYPTION  ALGORITHM...
	public static String encrypt_data(String strToEncrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		}
		catch (Exception e){ System.out.println("Error while encrypting: " + e.toString());}
		return null;
	}

	//DECRYPTION ALGORITHM...
	public static String decrypt_data(String strToDecrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		}
		catch (Exception e){ System.out.println("Error while decrypting: " + e.toString());}
		return null;
	}

}


class SplashScreen extends JFrame implements ActionListener,Runnable{
	
	ImageIcon icon = new ImageIcon(new ImageIcon("Images\\encrypted.png").getImage().getScaledInstance(320, 320, Image.SCALE_DEFAULT));
	ImageIcon load = new ImageIcon(new ImageIcon("Images\\45.gif").getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT));
	Image icon1 = Toolkit.getDefaultToolkit().getImage("Images\\encrypted.png");
	JProgressBar progressBar;
	JTextField tf;
	JPasswordField tp;
	JLabel lbl,m1,stat;
	JButton btn;
	Thread t;
	public SplashScreen()
	{

		super("Encryption Project");
		this.getContentPane().setLayout(null);

		lbl = new JLabel();
		lbl.setIcon(icon);
		lbl.setBounds(170,173,320,360);
		this.add(lbl);

		lbl = new JLabel("Encryption / Decryption Project...");
		lbl.setFont(new Font("TimesNewRoman",Font.BOLD,40));
		lbl.setBounds(360,73,650,60);
		lbl.setForeground(Color.white);
		lbl.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED,Color.decode("#34B7F1"),Color.yellow));
		this.add(lbl);

		m1 = new JLabel();
		m1.setBounds(84,424,150,150);
		m1.setForeground(Color.white);
		m1.setFont(new Font("TimesNewRoman",Font.BOLD,16));
		this.add(m1);

		lbl = new JLabel(" AOX ACCOUNT ");
		lbl.setBounds(760,233,250,35);
		lbl.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,30));
		lbl.setForeground(Color.white);
		this.add(lbl);

		lbl = new JLabel("Username :");
		lbl.setBounds(720,313,200,25);
		lbl.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,24));
		lbl.setForeground(Color.white);
		this.add(lbl);

		tf = new JTextField();
		tf.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,17));
		tf.setBounds(870,313,200,25);
		this.add(tf);

		lbl = new JLabel("Password :");
		lbl.setBounds(720,363,200,25);
		lbl.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,24));
		lbl.setForeground(Color.white);
		this.add(lbl);

		tp = new JPasswordField();
		tp.setBounds(870,363,200,25);
		tp.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,24));
		this.add(tp);

		btn = new JButton("Log In");
		btn.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,18));
		btn.setBounds(820,423,120,45);
		//btn.setOpaque(true);
		btn.setContentAreaFilled(false);
		btn.setForeground(Color.white);
		btn.setBorder(BorderFactory.createLineBorder(Color.lightGray,3));
		btn.addActionListener(this);
		this.add(btn);

		stat = new JLabel("");
		stat.setBounds(550,520,300,30);
		stat.setFont(new Font("TimesNewRoman",Font.ITALIC+Font.BOLD,24));
		stat.setForeground(Color.white);
		this.add(stat);

		progressBar = new JProgressBar(SwingConstants.HORIZONTAL,0,100);
		progressBar.setBounds(0,550,1366,3);
		progressBar.setBorder(null);
		progressBar.setBorderPainted(false);
		progressBar.setBackground(Color.darkGray);
		progressBar.setForeground(Color.white);
		progressBar.setValue(0);
		this.add(progressBar);


		String arr1[] = {" Academic yr. :"," Guided By : "," Created By : "};
		int a1 = 553;
		for(int i = 0; i < arr1.length;i++)
		{
			lbl = new JLabel(arr1[i]);
			lbl.setBounds(70,a1,190,60);
			lbl.setFont(new Font("TimesNewRoman",Font.ITALIC,18));
			lbl.setForeground(Color.white);
			this.add(lbl);
			a1+=40;
		}

		String arr2[] = {" 2019-20"," Dr.Pravin Satav Sir ","Tejas Lakade"};
		a1 = 553;
		for(int i = 0; i < arr2.length;i++)
		{
			lbl = new JLabel(arr2[i]);
			lbl.setBounds(200,a1,190,60);
			lbl.setFont(new Font("TimesNewRoman",Font.ITALIC,18));
			lbl.setForeground(Color.white);
			this.add(lbl);
			a1+=40;
		}

		this.setUndecorated(true);
		this.setResizable(false);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.darkGray);
		this.setVisible(true);
		this.setIconImage(icon1);
		
	}

	public void actionPerformed(ActionEvent e)
	{
		String caption = e.getActionCommand();

		if(caption.equals("Log In"))
		{
			String username = tf.getText();
			String password = tp.getText();
			btn.setBorder(BorderFactory.createLineBorder(Color.white,4));
			if(username.equals("Username") && password.equals("Password"))
			{
				stat.setText("Login Successfull!!!");
				t = new Thread(this);
				t.start();
			//runningPBar();
			}
			else
			{
				stat.setText("Login Failed!!!");
				tf.setEditable(false);
				tp.setEditable(false);
				btn.setText("Retry");

			}
		}
		else if(caption.equals("Retry"))
		{
			stat.setText("");
			tf.setEditable(true);
			tp.setEditable(true);
			btn.setText("Log In");
		}

	}

	public void run()
	{

		lbl = new JLabel();
		lbl.setIcon(load);
		lbl.setBounds(70,425,150,150);
		this.add(lbl);
		runningPBar();
	}
	public void runningPBar(){
		int i=0;

		while( i<=100)
		{
			try{
				Thread.sleep(40);
				progressBar.setValue(i);
				m1.setText(Integer.toString(i)+"%");
				i++;
				if(i % 5 == 0)
				{
					stat.setText("");
				}
				if(i % 5 == 2)
				{
					stat.setText("Login Successfull !!!");
				}
				if(i == 99)
				{
					Encrypt e = new Encrypt();
				}
				if(i==100)
				{
					this.dispose();

				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}

public class Project1{

	public static void main(String[] args) {
	//	
		SplashScreen s = new SplashScreen();
	}

}
