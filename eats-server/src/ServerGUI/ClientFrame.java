package ServerGUI;

import com.company.client.Client;
import com.company.client.ResponseInterface;
import com.company.models.Menu;
import com.company.sockets.AuthenticationChecker;
import com.company.sockets.Request;
import com.company.sockets.Response;

import javax.swing.*;

import org.bson.types.ObjectId;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.List;

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
	
	private JLabel dateLabel;
	private JLabel resLabel;
	private JLabel mealLabel;
	private JLabel secLabel;
	private JLabel foodLabel;
	
	private JComboBox<UIDate> dateChooser;
	private JComboBox<Menu> restaurantChooser;
	private JComboBox mealChooser;
	private JComboBox sectionChooser;
	private JList<Menu.FoodItem> foodChooser;
	private JScrollPane listScroller;
	
	private JButton removeButton;
	private JButton addButton;
	private JPanel buttonPanel;
	private JPanel fieldPanel;
	private JPanel foodPanel;
	
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
		login = new JButton("Admin Login");
		guest = new JButton("Guest");
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
		
		dateChooser = new JComboBox<UIDate>();
		Date today = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(today);
		for (int i = 0; i<3; i++) {
			dateChooser.addItem(new UIDate(c.getTime()));
			c.add(Calendar.DATE, 1);
		}
		
		Calendar t = Calendar.getInstance();
		t.setTime(today);
		restaurantChooser = new JComboBox<Menu>();
		mealChooser = new JComboBox();
		sectionChooser = new JComboBox();
		foodChooser = new JList<Menu.FoodItem>();
		foodChooser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foodChooser.setLayoutOrientation(JList.VERTICAL);
		foodChooser.setVisibleRowCount(-1);
		listScroller = new JScrollPane(foodChooser);
		listScroller.setPreferredSize(new Dimension(250, 80));
		removeButton = new JButton("Remove selected item");
		addButton = new JButton("Add an item to this section");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		
		fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		foodPanel = new JPanel();
		foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
		
		dateLabel = new JLabel("Date:");
		resLabel = new JLabel("Restaurant:");
		mealLabel = new JLabel("Meal:");
		secLabel = new JLabel("Section:");
		foodLabel = new JLabel("Food Items:");
	}

	
	private void createGUI() {
		//buttons.add(userLabel);
		buttons.add(login);
		//buttons.add(passLabel);
		buttons.add(guest);
		add(buttons);
		
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		
		fieldPanel.add(dateLabel);
		fieldPanel.add(dateChooser);
		
		fieldPanel.add(resLabel);
		fieldPanel.add(restaurantChooser);
		
		fieldPanel.add(mealLabel);
		fieldPanel.add(mealChooser);
		
		fieldPanel.add(secLabel);
		fieldPanel.add(sectionChooser);
		
		fieldPanel.add(buttonPanel);
		
		foodPanel.add(foodLabel);
		foodPanel.add(listScroller);
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
						
						auth = new Request.RequestAuthentication(loginUser.getText(), AuthenticationChecker.hash(loginPass.getText()));

				        // Login request
				        Request request = new Request(null, new Request.RequestCheckAuthentication(loginUser.getText(), AuthenticationChecker.hash(loginPass.getText())));
				        System.out.println(loginUser.getText());
				        System.out.println(loginPass.getText());
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
				                	cf.getContentPane().setLayout(new BoxLayout(cf.getContentPane(), BoxLayout.X_AXIS));
				                	cf.remove(loginFields);
									cf.remove(buttons);
									//cf.getContentPane().setLayout(mgr);
									cf.add(fieldPanel);
									cf.add(foodPanel);
									cf.revalidate();
									cf.repaint();
				                }else if (this.resp == null){
				                	JOptionPane.showMessageDialog(getParent(),
											"The Server could not be reached",
											"Login Failed",
											JOptionPane.INFORMATION_MESSAGE);
				                }
				                else {
				                	JOptionPane.showMessageDialog(getParent(),
											"Invalid Admin Username or Password",
											"Login Failed",
											JOptionPane.INFORMATION_MESSAGE);
				                }

				            }
				        }).send();
						//(Boolean)ois.readObject();
						//System.out.println(success);
					
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
				cf.getContentPane().setLayout(new BoxLayout(cf.getContentPane(), BoxLayout.X_AXIS));
            	if (showLogin) cf.remove(loginFields);
				cf.remove(buttons);
				fieldPanel.remove(buttonPanel);
				cf.add(fieldPanel);
				cf.add(foodPanel);
				cf.revalidate();
				cf.repaint();
			}
		});
		
		dateChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restaurantChooser.removeAllItems();
            	mealChooser.removeAllItems();
            	sectionChooser.removeAllItems();
            	foodChooser.removeAll();
            	
				UIDate curr = (UIDate) dateChooser.getSelectedItem();
		        
		        //impossible to arrive here without auth already being set
				String JSONFormattedDate = (curr.getDate().getYear()+1900) + "-" + (curr.getDate().getMonth()+1) + "-" +  curr.getDate().getDate();
				Request request = new Request(auth, new Request.RequestPullMenus(JSONFormattedDate));
				System.out.println(JSONFormattedDate);
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
		                        if (menu.restaurant_availability.equals("open")) restaurantChooser.addItem(menu);//add to combo box
		                    }
		                    dateSuccess = true;
		                    System.out.println("true");
		                }else{
		                    dateSuccess = false;
		                    System.out.println("false");
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
				if (restaurantChooser.getItemCount() == 0) return;
				if (restaurantChooser.getSelectedIndex() < 0) return;
				mealChooser.removeAllItems();
				sectionChooser.removeAllItems();
				foodChooser.removeAll();
				//String curr = (String)restaurantChooser.getSelectedItem();
				for (Menu.Meal m : ((Menu)restaurantChooser.getSelectedItem()).meals) {
					mealChooser.addItem(m);
				}
			}
		});
		
		mealChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mealChooser.getItemCount() == 0) return;
				if (mealChooser.getSelectedIndex() < 0) return;
				sectionChooser.removeAllItems();
				foodChooser.removeAll();
				//String curr = (String)restaurantChooser.getSelectedItem();
				for (Menu.MealSections s : ((Menu.Meal)mealChooser.getSelectedItem()).meal_sections) {
					sectionChooser.addItem(s);
				}
			}
		});
		
		sectionChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (sectionChooser.getItemCount() == 0) return;
				if (sectionChooser.getSelectedIndex() < 0) return;
				foodChooser.removeAll();
				Vector<Menu.FoodItem> foods = new Vector<Menu.FoodItem>();
				//String curr = (String)restaurantChooser.getSelectedItem();
				for (Menu.FoodItem f : ((Menu.MealSections)sectionChooser.getSelectedItem()).section_items) {
					foods.addElement(f);
				}
				foodChooser.setListData(foods);;
			}
		});
		
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dateChooser.getSelectedItem() == null || restaurantChooser.getSelectedItem() == null 
					 || mealChooser.getSelectedItem() == null || sectionChooser.getSelectedItem() == null) {
					//popup
					return;
				}
				ObjectId menuid = ((Menu)(restaurantChooser.getSelectedItem()))._id;
				Request request = new Request(auth, new Request.RequestMenuDelete(menuid.toString(), ((Menu.Meal)mealChooser.getSelectedItem()).meal_identifier, 
						((Menu.MealSections)sectionChooser.getSelectedItem()).section_identifier, foodChooser.getSelectedValue().food_identifier));
		        new Client(request, new ResponseInterface() {

		            Response resp;
		            @Override
		            public void callback(Response resp) {
		                this.resp = resp;
		            }

		            @Override
		            public void run() {
		                System.out.println("SOCKETS: Callback called delete");
		                if(this.resp != null && this.resp.requestSuccess) {
		                	if (foodChooser.getModel().getSize() == 1) {
		                		//category does not exist anymore
		                	}
		                	else {
			                    int d = dateChooser.getSelectedIndex();
			                    int r = restaurantChooser.getSelectedIndex();
			                    int m = mealChooser.getSelectedIndex();
			                    int s = sectionChooser.getSelectedIndex();
			                    
			                    if (d>=0) {
			                    	dateChooser.setSelectedIndex(d);
			                    	if (r>=0) {
			                    		restaurantChooser.setSelectedIndex(r);
			                    		if (m>=0) {
			                    			mealChooser.setSelectedIndex(m);
			                    			if (s>=0) {
			    			                    sectionChooser.setSelectedIndex(s);
			                    			}
			                    		}
			                    	}
			                    }
		                	}
		                }else{
		                    //popup
		                }
		            }
		        }).send();
				
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dateChooser.getSelectedItem() == null || restaurantChooser.getSelectedItem() == null 
				 || mealChooser.getSelectedItem() == null || sectionChooser.getSelectedItem() == null) {
					//popup
					return;
				}
				ObjectId menuid = ((Menu)(restaurantChooser.getSelectedItem()))._id;
				
				String foodIdentifier = null;
				String foodName = null;
				
				foodName = (String)JOptionPane.showInputDialog("Item Name:");
				if (foodName.equals(null));
				
				for (int i=0; i<foodName.length(); i++) {
					if (foodName.charAt(i) == ' ') {
						foodIdentifier += '_';
					} else
						foodIdentifier += Character.toLowerCase(foodName.charAt(i));
				}
				
				Request request = new Request(auth, new Request.RequestMenuAdd(menuid.toString(), ((Menu.Meal)mealChooser.getSelectedItem()).meal_identifier, 
																((Menu.MealSections)sectionChooser.getSelectedItem()).section_identifier, foodIdentifier, foodName));
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
				
		        
                int d = dateChooser.getSelectedIndex();
                int r = restaurantChooser.getSelectedIndex();
                int m = mealChooser.getSelectedIndex();
                int s = sectionChooser.getSelectedIndex();
                
                if (d>=0) {
                	dateChooser.setSelectedIndex(d);
                	if (r>=0) {
                		restaurantChooser.setSelectedIndex(r);
                		if (m>=0) {
                			mealChooser.setSelectedIndex(m);
                			if (s>=0) {
			                    sectionChooser.setSelectedIndex(s);
                			}
                		}
                	}
                }
        	}
			
		});
		
	}
	
	public static void main (String [] args) {
		ClientFrame cf = new ClientFrame();
		cf.setVisible(true);
	}
}
