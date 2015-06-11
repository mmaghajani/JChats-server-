import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Server {

	private ArrayList<User> info;
	private HashMap<String, Socket> ID;
	private int clientNum ;
	public Server() {

		try {

			System.out.println("server");
			ServerSocket ss = new ServerSocket(5000);

			info = new ArrayList<User>();
			load();
			ID = new HashMap<String, Socket>();

			while (true) {
				System.out.println( "injam  " +clientNum ) ;
				if (clientNum < 3) {
					Socket client = ss.accept();
					clientNum++ ;
					// Runnable r = (Runnable) new Handler(client) ;
					// System.out.println("client");
					new Thread() {
						public void run() {
							try {
								PrintWriter pw = new PrintWriter(
										client.getOutputStream());
								pw.println("ok") ;
								pw.flush();
								
								BufferedReader br = new BufferedReader(
										new InputStreamReader(
												client.getInputStream()));
								String myUserName = "";
								String message;
								do {
									message = (String) br.readLine();
									try {
										// Start of primitive command
										if (message.equals("Check user name")) {
											String user = null;
											user = br.readLine();
											int flag = 0;

											for (User userName : info) {

												if (user.equals(userName
														.getUserName())) {
													pw.println("exist");
													pw.flush();
													flag = 1;
												}

											}

											if (flag == 0) {
												pw.println("ok");
												pw.flush();
											}

										}

										if (message.equals("Add user")) {
											User myUser = new User();
											myUser.setUserName(br.readLine());
											myUser.setPass(br.readLine());
											myUser.setName(br.readLine());
											myUser.setLastName(br.readLine());
											myUser.setDegree(br.readLine());
											myUser.setBirthDay(br.readLine());

											if (info.add(myUser))
												pw.println("Added");
											else
												pw.println("Unsuccessful add");

											pw.flush();
										}

										if (message.equals("Get user")) {
											String userName = br.readLine();

											User tempUser = new User(userName);
											User user = info.get(info
													.indexOf(tempUser));

											pw.println(user.getName());
											pw.flush();
											pw.println(user.getLastName());
											pw.flush();
											pw.println(user.getBirthDay());
											pw.flush();
											pw.println(user.getDegree());
											pw.flush();
											pw.println(user.getPass());
											pw.flush();
											pw.println(user.getUserName());
											pw.flush();
											pw.println(user.getStatus());
											pw.flush();
											pw.println(user.getNumOfFreinds());
											pw.flush();
											for (int i = 0; i < user
													.getNumOfFreinds(); i++) {
												pw.println(user.getFreinds()
														.get(i));
												pw.flush();
											}

										}

										if (message
												.equals("Is exist this user")) {
											String userName = br.readLine();

											User tempUser = new User(userName);
											if (info.contains(tempUser)) {
												pw.println("yes");
												pw.flush();
											} else {
												pw.println("no");
												pw.flush();
											}
										}

										if (message.equals("Is valid password")) {
											String userName = br.readLine();
											String pass = br.readLine();

											User tempUser = new User(userName);
											User user = info.get(info
													.indexOf(tempUser));

											if (pass.equals(user.getPass())) {
												pw.println("yes");
												pw.flush();
												myUserName = userName;
												ID.put(userName, client);
												System.out.println(userName);
												System.out.println( ID.size() ) ;
											} else {
												pw.println("no");
												pw.flush();
											}

										}
										// End of primitive command
										// Start of complex command
										if (message.equals("Change status")) {
											String userName = br.readLine();
											String status = br.readLine();

											System.out.println(status);
											for (User user : info) {
												if (user.getUserName().equals(
														userName))
													user.setStatus(status);
											}

										}

										if (message.equals("Add contact")) {
											String contact = br.readLine();

											User user = new User(contact);
											if (info.contains(user)) {

												pw.println("exist");
												pw.flush();
												pw.println("existing contact");
												pw.flush();

												PrintWriter pw1 = new PrintWriter(
														ID.get(contact)
																.getOutputStream());
												pw1.flush();
												pw1.println("Request for add");
												pw1.flush();
												pw1.println("from server");
												pw1.flush();
												pw1.println(myUserName);
												pw1.flush();
											} else {
												pw.println("no");
												pw.flush();
												pw.println("existing contact");
												pw.flush();
											}

										}

										if (message.equals("I accept adding")) {
											String user = br.readLine();

											User me = new User(myUserName);
											for (User user1 : info)
												if (user1.equals(me)) {
													user1.addFreind(user);
													user1.setNumOfFreinds(user1
															.getFreinds()
															.size());
												}

											PrintWriter pw1 = new PrintWriter(
													ID.get(user)
															.getOutputStream());
											pw1.flush();
											pw1.println("You are accepted");
											pw1.flush();
											pw1.println("from server");
											pw1.flush();
											pw1.println(myUserName);
											pw1.flush();
										}

										if (message.equals("Add successfuly")) {
											String contact = br.readLine();

											User me = new User(myUserName);
											for (User user1 : info)
												if (user1.equals(me)) {
													user1.addFreind(contact);
													user1.setNumOfFreinds(user1
															.getFreinds()
															.size());
												}

										}

										if (message.equals("Send message")) {
											String dist = br.readLine();
											String mess = br.readLine();
											PrintWriter pw1 = new PrintWriter(
													ID.get(dist)
															.getOutputStream());
											pw1.flush();
											pw1.println("Receive message");
											pw1.flush();
											pw1.println("from server");
											pw1.flush();
											pw1.println(mess);
											pw1.flush();
											pw1.println(myUserName);
											pw1.flush();

										}

										if (message.equals("Delivery")) {
											String to = br.readLine();
											// JOptionPane.showMessageDialog(null,"salamdel");
											PrintWriter pw1 = new PrintWriter(
													ID.get(to)
															.getOutputStream());
											pw1.flush();
											pw1.println("Delivered");
											pw1.flush();
											pw1.println("from server for you");
											pw1.flush();
										}
									} catch (NullPointerException e) {

									}
								} while (!message.equals("terminate"));

								// pw.close();
								// br.close();
								// client.close();
								System.out.println( "ghabl az amal " + ID.size()) ;
								ID.remove(myUserName);
								clientNum-- ;
								System.out.println("baad az amal" + ID.size()) ;
								ObjectOutputStream oos = new ObjectOutputStream(
										new FileOutputStream(
												"C:\\Users\\mahdi\\workspace\\JChats(server)\\Users\\info.txt"));

								oos.writeObject(info);
								oos.close();

								pw.println("terminate");
								pw.flush();
								pw.println("terminate");
								pw.flush();
							} catch (IOException ex) {
								Logger.getLogger(Handler.class.getName()).log(
										Level.SEVERE, null, ex);
							}

						};
					}.start();

				}
				else{
					Socket client = ss.accept() ;
					PrintWriter pw = new PrintWriter(
							client.getOutputStream());
					pw.println("busy") ;
					pw.flush();
					client.close();
				}
			}
			

		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName())
					.log(Level.SEVERE, null, ex);
		}

	}

	private void load() {
		try {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(
								"C:\\Users\\mahdi\\workspace\\JChats(server)\\Users\\info.txt"));

				try {
					info = (ArrayList<User>) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ois.close();
			} catch (EOFException e) {
				// e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
