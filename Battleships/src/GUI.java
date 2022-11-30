import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
X --------->Y
^
|	ταμπλο πλοιων 
|
|

*/

public class GUI extends JFrame{
	private mainPanel mainPanel = new mainPanel();
	private startScreen startScreen;
	private Player winner;
	private mainPanel thisPanel;
	private JFrame frame;
//constructor
	public GUI(){
		frame=this;
		
		startScreen = new startScreen();
		
		this.setResizable(false);
		this.add(startScreen);
		this.setTitle("Battleships");
		this.setVisible(true);
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void startTheGame() {
		startScreen.setVisible(false);
		this.add(mainPanel);
		this.setContentPane(mainPanel);
	}
	
	class startScreen extends JPanel{
		@Override
		  protected void paintComponent(Graphics g) {
			Image startImage = null;
			try {                
				startImage = ImageIO.read(new File("images\\StartScreen.jpg"));
		       } catch (IOException ex) {
		            // handle exception...
		       }
			Image scaledImage = startImage.getScaledInstance(1000,565,Image.SCALE_SMOOTH);
		    super.paintComponent(g);
		        g.drawImage(scaledImage, 0, 0, null);
		}
		public startScreen() {
		JButton startGame = new JButton("START GAME");
		startGame.setBounds(550,350,200,80);
		startGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startTheGame();
			}
			
		});
		startGame.setBackground(Color.cyan);
		setLayout(null);
		add(startGame);
		}
	}
	
	//main Panel
	class mainPanel extends JPanel{
		 private int b = 0;
		 private HitBoard player1HitBoard,player2HitBoard;
		 private FriendlyBoard player1Board,player2Board;
		 private JButton PlaceShips;
		 private ImageIcon tempIcon;
		 private Image image;
		 private Player player1,player2,enemyPlayer,currentPlayer;
		 private Ship currentShip;
		 private JButton rotateShip = new JButton("Rotate");
		 private JLabel hit,miss;
		 
		 public Player getPlayer1() {
			return player1;
		}


		public Player getPlayer2() {
			return player2;
		}


		public Player getCurrentPlayer() {
			return currentPlayer;
		}

		private int btn = 0;
		 private boolean killDestroyer = false;
		 private Ship damagedShip;
		 private int locationx;
		 private int locationy;
		 private Ship selectedShip = null;
		 private JButton doublehit,kamikaze,heal,torpedo;
		 
		public mainPanel() {
		thisPanel=this;
		Player player1 = new Player();
		Player player2 = new Player();
		
		this.player1 = player1;
		this.player2 = player2;
		currentPlayer= player1;
		enemyPlayer = player2;
		//making players boards
		player1Board = new FriendlyBoard();
		player1Board.setBounds(50,50,300,300);
		player2Board = new FriendlyBoard();
		player2Board.setBounds(50,50,300,300);
		
		//making hit boards
		player1HitBoard = new HitBoard();
		player1HitBoard.setBounds(650,50,300,300);
		player2HitBoard = new HitBoard();
		player2HitBoard.setBounds(650,50,300,300);
		player1HitBoard.setVisible(false);
		
		player2HitBoard.setVisible(false);
		player2Board.setVisible(false);
		
		//stin arxi gia placement apo ta ploia
		PlaceShips = new JButton("Place");
		PlaceShips.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//stamataei tous mouselisteners gia ploia placement
				if(currentPlayer == player2) {
				player2Board.enabled = false;
				PlaceShips.setVisible(false);
				rotateShip.setVisible(false);
				}
				player1Board.enabled = false;
				changeTurn();
				currentPlayer.setPlacedShips(true);
				
				if(b==0)
					player2HitBoard.setVisible(false);
				b++;
			}
		});
		ImageIcon hitimg = new ImageIcon(new ImageIcon("images\\hit.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		hit = new JLabel(hitimg);
		hit.setBounds(400,200,150,150);
		hit.setVisible(false);
		
		ImageIcon missimg = new ImageIcon(new ImageIcon("images\\miss.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		miss = new JLabel(missimg);
		miss.setBounds(400,200,150,150);
		miss.setVisible(false);
		
		PlaceShips.setBounds(10,0,100,40);
		
		
		rotateShip.setBounds(200,0,100,40);
		JButton restart = new JButton("restart");
		restart.setBackground(Color.orange);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				restart(thisPanel);
			}
		});
		restart.setBounds(450,0,100,30);
		
		doublehit = new JButton("DOUBLE");
		doublehit.setBounds(500,500,100,40);
		doublehit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPlayer.setHasDouble(false);
				btn = 1;
			}
		});
		doublehit.setBackground(Color.pink);
		
		kamikaze = new JButton("KAMIKAZE");
		kamikaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPlayer.setHasKamikaze(false);
				btn = 2;
			}
			
		});
		kamikaze.setBounds(500,450,100,40);
		kamikaze.setBackground(Color.pink);
		
		
		heal = new JButton("Heal Ship");
		heal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPlayer.setHasHeal(false);
				btn = 3;
			}
			
			
		});
		heal.setBounds(500,400,100,40);
		heal.setBackground(Color.pink);
		
		torpedo = new JButton("Torpedo");
		torpedo.setBounds(500,350,100,40);
		torpedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPlayer.setHasTorpedo(false);
				btn = 4;
			}
		});
		torpedo.setBackground(Color.pink);
		
		add(torpedo);
		add(heal);
		add(kamikaze);
		add(doublehit);
		add(restart);
		add(hit);
		add(miss);
		add(rotateShip);
		setLayout(null);
		add(player1Board);
		add(player1HitBoard);
		add(player2Board);
		add(player2HitBoard);
		add(PlaceShips);
		}
		
		
		@Override
		  protected void paintComponent(Graphics g) {
			Image bgImage = null;
			try {                
		          bgImage = ImageIO.read(new File("images\\background.png"));
		       } catch (IOException ex) {
		            // handle exception...
		       }
			Image scaledImage = bgImage.getScaledInstance(1000,600,Image.SCALE_SMOOTH);
		    super.paintComponent(g);
		        g.drawImage(scaledImage, 0, 0, null);
		}
		
	
	
	//enemy Board
	class HitBoard extends JPanel{
		private boolean shipHit = false;
		private JButton[][] buttons;
		
		public HitBoard() {
		setLayout(new GridLayout(10,10));
		buttons = new JButton[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
			buttons[i][j] = new JButton();
			buttons[i][j].setBackground(Color.blue);
			buttons[i][j].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//finds coordinates
					for(int i=0; i<10; i++) {
						for(int j=0; j<10; j++) {
							if(buttons[i][j]==e.getSource()) {
							
								buttons[i][j].setEnabled(false);
								buttons[i][j].setBackground(Color.gray);
								
								if(btn==4)
								{
									for(int k=0;k<10;k++) {
										buttons[i][k].setBackground(Color.gray);
										buttons[i][k].setEnabled(false);
										for(Ship ship: enemyPlayer.getShips()) {
											if(ship.isHit(i, k))
												buttons[i][k].setBackground(Color.red);
								
									
									}
								}
									btn=0;
								}
								for(Ship ship: enemyPlayer.getShips()) {
									if(ship.isHit(i, j)) {
											buttons[i][j].setBackground(Color.red);
											ship.hp--;
										if(btn==2) {
											ship.hp=0;
											if(ship.vertical) {
												for(i=0; i<ship.getLength(); i++) {
													buttons[ship.Xpos+i][j].setBackground(Color.red);
													buttons[ship.Xpos+i][j].setEnabled(false);
												}
											}
											else {
												for(j=0; j<ship.getLength(); j++) {
													buttons[i][ship.Ypos+j].setBackground(Color.red);
													buttons[i][ship.Ypos+j].setEnabled(false);
												}
											}
											killDestroyer = true;
											btn=0;
										}
										
										shipHit = true;
										hit.setVisible(true);
									}
								}
								if(!shipHit)
									miss.setVisible(true);
								if(currentPlayer==player1) {
									
									player2Board.repaint();
									if(killDestroyer==true)
										player1Board.repaint();
								}
								else {
									player1Board.repaint();
									if(killDestroyer==true)
										player2Board.repaint();
								}
								//250 milisecond delay
								new Timer().schedule(
								        new TimerTask() {
								            @Override
								            public void run() {
								            	if(shipHit) 
								            		hit.setVisible(false);
								            	else
								            		miss.setVisible(false);
								            	shipHit=false;
								            	
								            	if(btn!=1)
								            		changeTurn(); 
								            	else 
								            		btn =0;
								            	
								            }
								        },
								        200
								);
								if(btn==2)
									btn = 0;
							}
							
						}
						
						}}				
			});
			add(buttons[i][j]);
			}
		}
		}
		public JButton getButton(int i,int j) {
			return buttons[i][j];
		}
		
	}
	
	public class FriendlyBoard extends JPanel{
		private boolean enabled = true;
		private int hit;
		@Override
		public void paint(Graphics g) {
			hit=0;
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					//conditions gia repaint
					if(currentPlayer==player1) {
						if(killDestroyer==true) {
							for(Ship frShips: currentPlayer.getShips()) {
								if(frShips.getName()=="Destroyer") {
									frShips.hp=0;
									if(frShips.vertical) {
										for(int x=0; x<frShips.getLength(); x++) {
											player2HitBoard.getButton(frShips.Xpos+x,frShips.Ypos).setBackground(Color.red);
											player2HitBoard.getButton(frShips.Xpos+x,frShips.Ypos).setEnabled(false);
										}
									}
									else {
										for(int y=0; y<frShips.getLength(); y++) {
											player2HitBoard.getButton(frShips.Xpos,frShips.Ypos+y).setBackground(Color.red);
											player2HitBoard.getButton(frShips.Xpos,frShips.Ypos+y).setEnabled(false);
										}
									}
								}
							}
							killDestroyer = false;
						}
						if(btn==3) {	
			            		
			            		for(int k=currentPlayer.getShip(1).x;k<currentPlayer.getShip(1).length;k++)
			            		{
			            			player1HitBoard.getButton(k,1).setBackground(Color.blue);
			            			player1HitBoard.getButton(k,1).setEnabled(true);
			            			
			            		}
			            		
			            	
			            	btn=0;
			            }	
					if(player2HitBoard.getButton(i,j).getBackground()== Color.blue) {
					g.setColor(Color.blue);
					}
					else if(player2HitBoard.getButton(i,j).getBackground()== Color.red){
						hit++;
						g.setColor(Color.red);
					}
					else {
						g.setColor(Color.gray);
					}
					g.fillRect(j*30, i*30, 30, 30);
					g.setColor(Color.black);
					g.drawRect(j*30, i*30, 30, 30);
					}
					else {
						if(btn==3) {	
			            	
			            		for(int k=currentPlayer.getShip(1).x;k<currentPlayer.getShip(1).length;k++)
			            		{
			            			player2HitBoard.getButton(k,1).setBackground(Color.blue);
			            			player2HitBoard.getButton(k,1)	.setEnabled(true);
			            			
			            		}
			            		
			            	
			            	btn=0;
			            }
						if(killDestroyer==true) {
							for(Ship frShips: currentPlayer.getShips()) {
								if(frShips.getName()=="Destroyer") {
									frShips.hp=0;
									if(frShips.vertical) {
										for(int x=0; x<frShips.getLength(); x++) {
											player1HitBoard.getButton(frShips.Xpos+x,frShips.Ypos).setBackground(Color.red);
											player1HitBoard.getButton(frShips.Xpos+x,frShips.Ypos).setEnabled(false);
										}
									}
									else {
										for(int y=0; y<frShips.getLength(); y++) {
											player1HitBoard.getButton(frShips.Xpos,frShips.Ypos+y).setBackground(Color.red);
											player1HitBoard.getButton(frShips.Xpos,frShips.Ypos+y).setEnabled(false);
										}
									}
								}
							}
							killDestroyer = false;
						}
						if(player1HitBoard.getButton(i,j).getBackground()== Color.blue) {
							g.setColor(Color.blue);
							}
							else if(player1HitBoard.getButton(i,j).getBackground()== Color.red){
								hit++;
								g.setColor(Color.red);
							}
							else {
								g.setColor(Color.gray);
							}
							g.fillRect(j*30, i*30, 30, 30);
							g.setColor(Color.black);
							g.drawRect(j*30, i*30, 30, 30);
					}
				}
			}
			
			if(hit==17) {
				GameOver();	
			}
			
			for(Ship ship: currentPlayer.getShips()) {
				if(ship.vertical==true)
			tempIcon = new ImageIcon("images\\" + ship.getName() + ".png");
				else
					tempIcon = new ImageIcon("images\\Rotated" + ship.getName() + ".png");
			image = tempIcon.getImage();
				if(ship.vertical==true)
			image = getScaledImage(image,30,ship.getLength()*30);
			else
				image = getScaledImage(image,ship.getLength()*30,30);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage(image,ship.getY(),ship.getX(),this);
			}
			addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					//+30 gia na kaluptei olo to block
					int Xpos=e.getY();//Gia na ginei katheta to X kai orizontia to Y
					int Ypos=e.getX();
				
				//An o xristis kanei klik panw se kouti pou brisketai ploio tha epilegetai to ploio
														//30(megethos koutiou)* twn arithmo twn koutiwn pou brisketai to ploio
														//analoga me to megethos tou ploiou(length)
				
				rotateShip.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(selectedShip!=null) {
					if(selectedShip.vertical==true) {
					
						selectedShip.vertical=false;
						if(Ypos>=selectedShip.getY() && Ypos<=selectedShip.getY()+30*selectedShip.getLength()) {
							
							if(selectedShip.getY()<=270 && selectedShip.getY()+30*(selectedShip.getLength()-1)<=270) {	
								selectedShip.setYpos(Ypos-(30*(selectedShip.getLength()-1)));
								selectedShip.move(selectedShip.getX()/30, selectedShip.getY()/30);
						}
					
						repaint();
						}
					
					}
					else if(selectedShip.vertical==false) {
						selectedShip.vertical=true;
						if(Xpos>=selectedShip.getX()*30 && Xpos<=selectedShip.getX()+30*selectedShip.getLength()) {
							selectedShip.setYpos(Ypos+(30*(selectedShip.getLength()-1)));
							selectedShip.move(selectedShip.getX()/30, selectedShip.getY()/30);
							
						}
						repaint();
						
					}
					
						}
										
					}
				
				});
				}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}

				@Override
				public void mousePressed(MouseEvent e) {
					if(enabled == true) {
					for(Ship ship: currentPlayer.getShips()) {
						if(ship.vertical==true) {
						if ((e.getX()>=ship.getY()) && (e.getX()<=ship.getY()+30) && e.getY()>=ship.getX() && e.getY()<=ship.getX() + (ship.getLength()*30)) {
							currentShip = ship;
							selectedShip = ship;
						}
					}
						if(ship.vertical==false) {
							if ((e.getX()>=ship.getY()) && (e.getX()<=ship.getY() + (ship.getLength()*30)) && e.getY()>=ship.getX() && e.getY()<=ship.getX() + 30) {
								currentShip = ship;
								selectedShip = ship;
							}
						}
					}
				}
				}
				
	

				@Override
				public void mouseReleased(MouseEvent e) {
					currentShip=null;
				}
				
			});
			addMouseMotionListener( new MouseMotionListener() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if(enabled == true) {
					if(currentShip!=null) {
					if(currentShip.vertical==true) {	
						if(e.getX()<300 && e.getY()<300-30*(currentShip.getLength()-1) && e.getX()>=0 && e.getY()>=0) {
						currentShip.move(e.getY()/30, e.getX()/30);
						repaint();
						}
					}
						//an to epilegmeno ploio einai orizontia den prepei na vgenei ektos tamplo
						
						if(currentShip.vertical==false)
						{  	
							if(e.getX()<300-30*(currentShip.getLength()-1) && e.getY()<300 && e.getX()>=0 && e.getY()>=0) {
							currentShip.move(e.getY()/30, e.getX()/30);
							repaint();
							}
						}
					}
					
				}
				}

				@Override
				public void mouseMoved(MouseEvent e) {}
			});
		}
	
		//kanei resize to image gia na xwraei sta cell tou board(1 cell = 30x30)
				private Image getScaledImage(Image srcImg, int w, int h){
				    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				    Graphics2D g2 = resizedImg.createGraphics();

				    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				    g2.drawImage(srcImg, 0, 0, w, h, null);
				    g2.dispose();

				    return resizedImg;
				}
				
	}
	
	private void changeTurn() {
		if(currentPlayer == player2) {
			player1HitBoard.setVisible(true);
			player1Board.setVisible(true);
			player2HitBoard.setVisible(false);
			player2Board.setVisible(false);
			currentPlayer = player1;
			enemyPlayer = player2;
			}
			else {
			player1HitBoard.setVisible(false);
			player1Board.setVisible(false);
			player2HitBoard.setVisible(true);
			player2Board.setVisible(true);
			currentPlayer = player2;
			enemyPlayer = player1;
			}
		
		
		if(currentPlayer.hasDouble==true)
			doublehit.setEnabled(true);
		else
			doublehit.setEnabled(false);
		
		if(currentPlayer.hasKamikaze==true)
			kamikaze.setEnabled(true);
		else
			kamikaze.setEnabled(false);
		
		if(currentPlayer.hasHeal==true)
			heal.setEnabled(true);
		else
			heal.setEnabled(false);
		
		if(currentPlayer.hasTorpedo==true)
			torpedo.setEnabled(true);
		else
			torpedo.setEnabled(false);
	}
	
	}
	
	
	class GameOverScreen extends JPanel{
		GameOverScreen gameover;
		@Override
		  protected void paintComponent(Graphics g) {
			Image gameOverImage = null;
			try {
				if(winner==thisPanel.getPlayer1())
				gameOverImage = ImageIO.read(new File("images\\P1gameOverScreen.jpg"));
				else 
					gameOverImage = ImageIO.read(new File("images\\P2gameOverScreen.jpg"));
		       } catch (IOException ex) {
		            // handle exception...
		       }
			Image scaledImage = gameOverImage.getScaledInstance(1000,565,Image.SCALE_SMOOTH);
		    super.paintComponent(g);
		        g.drawImage(scaledImage, 0, 0, null);
		        
		        addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {}
					@Override
					public void mouseExited(MouseEvent arg0) {}
					@Override
					public void mousePressed(MouseEvent arg0) {
						frame.remove(gameover);
						mainPanel newPanel = new mainPanel();
						frame.setContentPane(newPanel);
						frame.revalidate();
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {}
				});
		}
		
		public GameOverScreen() {
			gameover=this;
		}
	}
	
	
	private void GameOver() {
		if(thisPanel.getCurrentPlayer()==thisPanel.getPlayer2())
			winner = thisPanel.getPlayer1();
		else
			winner = thisPanel.getPlayer2();
		frame.remove(thisPanel);
		GameOverScreen gameOver = new GameOverScreen();
		frame.add(gameOver);
		this.setContentPane(gameOver);
		revalidate();
	}
	
	
	private void restart(mainPanel mainPanel) {
		this.remove(mainPanel);
		mainPanel newPanel = new mainPanel();
		this.setContentPane(newPanel);
		revalidate();
	}
	
}