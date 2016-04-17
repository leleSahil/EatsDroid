package ServerGUI;

import com.company.client.Client;
import com.company.client.ResponseInterface;
import com.company.models.Menu;
import com.company.sockets.AuthenticationChecker;
import com.company.sockets.Request;
import com.company.sockets.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.List;
<<<<<<< HEAD
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.bson.types.ObjectId;

import com.company.client.Client;
import com.company.client.ResponseInterface;
import com.company.models.Menu.FoodItem;
import com.company.models.Menu;
import com.company.models.Restaurant;
import com.company.sockets.AuthenticationChecker;
import com.company.sockets.Request;
import com.company.sockets.Response;

public class ClientFrame extends JFrame {
	private static final long serialVersionUID = 9183816558021947333L;
	private static String configFile = "ClientConfig.txt";
	
	ClientFrame cf;
	private Request.RequestAuthentication auth;
	
	private JPanel buttons;
	private JButton login;
	private JButton guest;
	
	private JPanel loginFields;
	private JLabel userLabel;
	private JTextField loginUser;
	private JLabel passLabel;
	private JTextField loginPass;
	
	private JButton send;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	boolean showLogin = false;
	
	private JComboBox<UIDate> dateChooser;
	private JComboBox<Menu> restaurantChooser;
	private JComboBox foodChooser;
	private JButton removeButton;
	private JButton addButton;
	private JPanel buttonPanel;
	
	List<Menu> menus;
	
	private boolean loginSuccess = false;
	private boolean dateSuccess = false;
	
	public ClientFrame()
	{
		cf = this;
		setTitle("Server Access");
		setSize(640,480);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setMinimumSize(new Dimension(640,480));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initializeVariables();
		createGUI();
		addActionAdapters();
		
	}
	
	private void initializeVariables() {
		userLabel = new JLabel("Username");
		passLabel = new JLabel("Password");
		login = new JButton("Admin Login");
		guest = new JButton("Guest");
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
		
		dateChooser = new JComboBox<UIDate>();
		Date today = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(today);
		for (int i = 0; i<30; i++) {
			dateChooser.addItem(new UIDate(c.getTime()));
			c.add(Calendar.DATE, 1);
		}
		
		Calendar t = Calendar.getInstance();
		t.setTime(today);
		restaurantChooser = new JComboBox<Menu>();
		foodChooser = new JComboBox();
		removeButton = new JButton("Remove selected item");
		addButton = new JButton("Add an item for the selected date and restaurant");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
	}

	
	private void createGUI() {
		//buttons.add(userLabel);
		buttons.add(login);
		//buttons.add(passLabel);
		buttons.add(guest);
		add(buttons);
		
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
	}

	private void addActionAdapters() {
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (showLogin) return;
				
				loginFields = new JPanel();
				loginFields.setLayout(new BoxLayout(loginFields, BoxLayout.Y_AXIS));
				loginFields.setMaximumSize(new Dimension(150,100));
				loginFields.setAlignmentX(CENTER_ALIGNMENT);
				
				loginUser = new JTextField("admin");
				loginUser.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						loginUser.setText("");
					}
				});
				loginUser.setAlignmentX(CENTER_ALIGNMENT);
				loginFields.add(loginUser);
				
				loginPass = new JPasswordField("password");
				loginPass.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						loginPass.setText("");
					}
				});
				loginPass.setAlignmentX(CENTER_ALIGNMENT);
				loginFields.add(loginPass);
				
				send = new JButton("Send");
				loginFields.add(send);
				send.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Socket s = null;
						BufferedReader in = null;
						try {
							/*in = new BufferedReader(new FileReader(configFile));
							String temp = in.readLine();
							int port = Integer.parseInt(temp);
							temp = in.readLine();*/
							//s = new Socket(temp, port);
							
							//oos = new ObjectOutputStream(s.getOutputStream());
							//ois = new ObjectInputStream(s.getInputStream());
							//Credentials c = new Credentials();
							//c.user = loginUser.getText();
							//c.pass = (Integer.toString(loginPass.getText().hashCode()));
							
							auth = new Request.RequestAuthentication(loginUser.getText(), AuthenticationChecker.hash(loginPass.getText()));


					        // Login request
					        Request request = new Request(null, new Request.RequestCheckAuthentication(loginUser.getText(), AuthenticationChecker.hash(loginPass.getText())));
							new Client(request, new ResponseInterface() {

					            Response resp;
					            @Override
					            public void callback(Response resp) {
					               this.resp = resp;
					            }

					            @Override
					            public void run() {
					                System.out.println("SOCKETS: Callback called");
					                if(this.resp != null && this.resp.requestSuccess){
					                	cf.loginSuccess = true;
					                }else{
					                	cf.loginSuccess = false;
					                }

					            }
					        }).send();
							//(Boolean)ois.readObject();
							//System.out.println(success);
							if (loginSuccess) {
								cf.remove(loginFields);
								cf.remove(buttons);
								//cf.getContentPane().setLayout(mgr);
								cf.add(dateChooser);
								cf.add(restaurantChooser);
								cf.add(foodChooser);
								cf.add(buttonPanel);
								cf.revalidate();
								cf.repaint();
							}
							else {
								JOptionPane.showMessageDialog(getParent(),
															"Invalid Admin Username or Password",
															"Login Failed",
															JOptionPane.INFORMATION_MESSAGE);
								if (s != null) {
									s.close();
								}
							}
						} catch (IOException ioe) {
							JOptionPane.showMessageDialog(getParent(),
									"Server could not be reached",
									"Connection Failed",
									JOptionPane.INFORMATION_MESSAGE);
							ioe.printStackTrace();
						/*} catch (ClassNotFoundException cnfe) {
							cnfe.printStackTrace();*/
						} finally {
							try {
								if (in != null) {
									in.close();
								}
							} catch (IOException ioe) {
								System.out.println("ioe: " + ioe.getMessage());
							}
						}
						
						
					}
				});
				send.setAlignmentX(CENTER_ALIGNMENT);
				
				login.getParent().getParent().add(loginFields);
				login.getParent().getParent().revalidate();
				login.getParent().getParent().repaint();
				showLogin = true;
			}
		});
		
		guest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cf.remove(loginFields);
				cf.remove(buttons);
				
				//open as guest
				
				cf.revalidate();
				cf.repaint();
			}
		});
		
		dateChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UIDate curr = (UIDate) dateChooser.getSelectedItem();
				
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
				c.setTime(curr.getDate());
		        c.set(Calendar.HOUR, 0);
		        c.set(Calendar.MINUTE, 0);
		        c.set(Calendar.SECOND, 0);
		        c.set(Calendar.MILLISECOND, 0);
		        
		        //impossible to arrive here without auth already being set
				Request request = new Request(auth, new Request.RequestPullMenus(c));
		        new Client(request, new ResponseInterface() {

		            Response resp;
		            @Override
		            public void callback(Response resp) {
		                this.resp = resp;
		            }

		            @Override
		            public void run() {
		                System.out.println("SOCKETS: Callback called");
		                if(this.resp != null && this.resp.requestSuccess){
		                    menus = (List<Menu>)this.resp.data;
		                    for(Menu menu : menus){
		                        System.out.println("SOCKETS: menu availablilty -> "+menu.restaurant_availability);
		                        restaurantChooser.addItem(menu);//add to combo box
		                    }
		                    dateSuccess = true;
		                }else{
		                    dateSuccess = false;
		                }
		            }
		        }).send();
		        
		        if (dateSuccess) {
					restaurantChooser.setVisible(true);
		        }
		        else {
		        	//popup
		        }
				
			}
		});
		
		restaurantChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String curr = (String)restaurantChooser.getSelectedItem();
				for (Menu menu : menus){
<<<<<<< HEAD
					if (menu.restaurant_name == restaurantChooser.getSelectedItem()) {
=======
					if (menu.restaurant_name.equals(restaurantChooser.getSelectedItem())) {
>>>>>>> 836a16133d23b463f8750da1e9da8ab3ae5fe725
						for (Menu.Meal m : menu.meals) {
							foodChooser.addItem(m);
							for (Menu.MealSections s : m.meal_sections) {
								foodChooser.addItem(s);
								for (Menu.FoodItem f : s.section_items) {
									foodChooser.addItem(f);
								}
							}
							
						}
					}
                }
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//while (currMenu == null && )
				Request request = new Request(auth, new Request.RequestMenuAdd(((Menu)(restaurantChooser.getSelectedItem()))._id.toString(), "breakfast", "americana", "test_food", "Test FOOD"));
		        new Client(request, new ResponseInterface() {

		            Response resp;
		            @Override
		            public void callback(Response resp) {
		                this.resp = resp;
		            }

		            @Override
		            public void run() {
		                System.out.println("SOCKETS: Callback called");
		                if(this.resp != null && this.resp.requestSuccess){
		                    System.out.println("Success");
		                }else{
		                    System.out.println("Failure");
		                }
		            }
		        }).send();
				
				
			}
		});
		
	}
	
	public static void main (String [] args) {
		ClientFrame cf = new ClientFrame();
		cf.setVisible(true);
	}
}
