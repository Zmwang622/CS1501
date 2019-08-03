import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable,ActionListener
{
    public static final int PORT = 8765;
    
    ObjectInputStream myReader; // Reads in User message and then encodes it sends the encoding.
    ObjectOutputStream myWriter;
    String myName, serverName;
	Socket connection;

    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    JScrollBar scrollBar;
    JScrollPane scrollPane;
    JMenuBar menuBar; 

    BigInteger E;
    BigInteger N;
	SymCipher cipher;
    String encString;

    public SecureChatClient()
    {
    	try
        {
            //No different below
        	myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        	serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");

	        InetAddress addr = InetAddress.getByName(serverName);
	        connection = new Socket(addr, PORT);
            // Reader reads in data from the Socket
	        myReader = new ObjectInputStream(connection.getInputStream());              // Get Reader and Writer
            // Writer writes data to the Socket
	        myWriter = new ObjectOutputStream(connection.getOutputStream());

            E = (BigInteger) myReader.readObject(); // Objects are being sent down stream lets read them!
            N = (BigInteger) myReader.readObject();
            encString = (String) myReader.readObject(); // Read in the encryption string so we know 

            // Create cipher based off encryption string 
            if(encString.equals("Add")) 
            {
                cipher = new Add128();
                System.out.println("Add128 Cipher was chosen!");
            } else if(encString.equals("Sub"))
            {
                cipher = new Substitute();
                System.out.println("Substitution Cipher was chosen!");
            } else{
                System.out.println("Error: Unknown Cipher"); // This should never happen
                System.exit(1);
            }

            // Send the encrypted key down the stream
            myWriter.writeObject(new BigInteger(1, cipher.getKey()).modPow(E,N));
            myWriter.flush();

            // Print the key out
            byte[] byteKey = cipher.getKey();
            System.out.print("Key: ");
                    for (int i = 0; i < byteKey.length; i++)
                    {
                        System.out.print(byteKey[i] + " ");
                    }
            System.out.println();

	        myWriter.writeObject(cipher.encode(myName));   // Send encoded name to Server.  Server will need
	        myWriter.flush();                              // this to announce sign-on and sign-off
	                                                       // of clients

            //GUI starts here
			this.setTitle(myName);      // Set title to identify chatter

            Box b = Box.createHorizontalBox();
            outputArea = new JTextArea(8,30); 
            outputArea.setEditable(false);
            outputArea.setLineWrap(true); // for very long messages

            scrollPane = new JScrollPane(outputArea);
            b.add(scrollPane);
            scrollBar = scrollPane.getVerticalScrollBar();
            outputArea.append("Welcome to the RSA Chat Group, " + myName + "\n");

            inputField = new JTextField(""); // User types here
            inputField.addActionListener(this);

            // Creating the Menu Bar
            menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            // First Menu item resets menu bar to default colors.
            JMenu def = new JMenu("Reset to Default");
            menuBar.add(def); 
            def.addMenuListener(new MenuListener() {
                @Override
                public void menuCanceled(MenuEvent arg0) {}

                @Override
                public void menuDeselected(MenuEvent arg0) {}

                @Override
                public void menuSelected(MenuEvent arg0) {
                    getContentPane().setBackground(UIManager.getColor("Panel.background"));
                }
                
            });

            // Second Menu item allows the user to change the Jpanel's colors
            JMenu bc = new JMenu("Background Color");
            JMenuItem bc_Red = new JMenuItem("Red"); 
            JMenuItem bc_Blue = new JMenuItem("Blue"); 
            JMenuItem bc_Yellow = new JMenuItem("Yellow"); 
            JMenuItem bc_Black = new JMenuItem("Black"); 
            JMenuItem bc_White = new JMenuItem("White"); 
            JMenuItem bc_Green = new JMenuItem("Green"); 
            JMenuItem bc_Orange = new JMenuItem("ORANGE"); 
            JMenuItem bc_Cyan = new JMenuItem("Cyan"); 
            JMenuItem bc_Grey = new JMenuItem("Grey"); 
            JMenuItem bc_Pink = new JMenuItem("Pink"); 
            JMenuItem bc_Magenta = new JMenuItem("Magenta"); 
            bc.add(bc_Red);
            bc.add(bc_Blue);
            bc.add(bc_Yellow);
            bc.add(bc_Black);
            bc.add(bc_White);
            bc.add(bc_Green);
            bc.add(bc_Orange);
            bc.add(bc_Cyan);
            bc.add(bc_Grey);
            bc.add(bc_Pink);
            bc.add(bc_Magenta);

            menuBar.add(bc);

            bc_Red.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.RED); 
                }
            });

            bc_Blue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.BLUE); 
                }
            });

            bc_Yellow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.YELLOW); 
                }
            });

            bc_Black.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.BLACK); 
                }
            });

            bc_White.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.WHITE); 
                }
            });

            bc_Green.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.GREEN); 
                }
            });

            bc_Orange.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.ORANGE); 
                }
            });

            bc_Cyan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.CYAN); 
                }
            });

            bc_Grey.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.GRAY); 
                }
            });

            bc_Pink.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.PINK); 
                }
            });

            bc_Magenta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getContentPane().setBackground(Color.MAGENTA); 
                }
            });

            prompt = new JLabel("Type your messages below: ");
            Container c = getContentPane();

            c.add(b,BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this); // Thread receives info from Server
            outputThread.start();
    	    
            addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            try{
                                // Encode Client Closing
                                myWriter.writeObject(cipher.encode("CLIENT CLOSING"));
                                System.exit(0);
                            }catch(IOException ioexc){
                                System.out.println("Error exiting");
                            }
                        }
                    }
                ); 
            setSize(1000,400);
            setVisible(true);
        }
    	catch(Exception e)
    	{
    		System.out.println("Something wrong happened!");
    	}
    }

    public void run()
    {
        while (true)
        {
             try {
                // Read in the object from the stream
                byte[] receivedBytes = (byte[]) myReader.readObject();
                String currMsg = cipher.decode(receivedBytes); // decode the bytes
			    outputArea.append(currMsg+"\n"); //add the new message to the output 
                byte[] decryptedBytes = currMsg.getBytes(); // For the assignment

                // Print everything
                System.out.println("Received Bytes: ");
                for(int i =0; i < receivedBytes.length; i++)
                {
                    System.out.print(receivedBytes[i]);
                }
                System.out.println();

                System.out.println("Decrypted Bytes: ");
                for(int i = 0; i < decryptedBytes.length; i++)
                {
                    System.out.print(decryptedBytes[i]);
                }
                System.out.println();

                System.out.println("Corresponding String: " + currMsg);
             }
             catch (Exception e)
             {
                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        // Time to send message
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        try
        {
            // Encrypt and prepare everything
            String sendString = myName + ": " + currMsg;
            byte[] correspondingArray = sendString.getBytes();
            byte[] encryptedArray = cipher.encode(sendString);

            // Send the encrypted message down stream
            myWriter.writeObject(encryptedArray);
            myWriter.flush();

            System.out.println("Current Message: " + sendString);
            
            System.out.println("Corresponding Array: ");
            System.out.print("[");
            for(int i = 0; i<correspondingArray.length; i++)
            {
                System.out.print(correspondingArray[i]+",");
            }
            System.out.print("]");
            System.out.println();

            System.out.println("Encrypted Array: ");
            for(int i = 0; i<encryptedArray.length;i++)
            {
                System.out.print(encryptedArray[i]);
            }
            System.out.println();
        } catch(Exception exc)
        {
            outputArea.append("Message could not send\n");
        }
    }                                               

    public static void main(String[] args)
    {
    	SecureChatClient client = new SecureChatClient();
    	client.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}