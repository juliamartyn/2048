import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Gameplay extends JPanel {

	 	enum State {
	        start, won, running, over
	    }
	 
	   final Color[] colorTable = {
		        new Color(0xCEDED3), new Color(0xA4B7AA), new Color(0x8BBB91),
		        new Color(0xDCB09D), new Color(0xF9ADA0), new Color(0xF9627D),
		        new Color(0xDB4366), new Color(0xB54065), new Color(0x924F78),
		        new Color(0x73456E), new Color(0x51293B), new Color(0xCEDED3)};
	    
	    final static int goal = 2048;
	 
	    static int score;
	    static int highest;
	 
	    private Random rand = new Random();
	 
	    private Tile[][] tiles;
	    private int tilesNumberInRow = 4;
	    public State gamestate = State.start;
	    private boolean checkingAvailableMoves;
	    
	    JButton newGame = new JButton("NEW GAME");
    	JButton exit = new JButton("EXIT");
    	JLabel label = new JLabel("2048");
	    
    	
	    public Gameplay() {
	    	setBackground(new Color(0x5b5b5b));	
	        setFont(new Font("SansSerif", Font.BOLD, 48));
	        setFocusable(true);
	        setLayout(null);
	        
	        label.setForeground(new Color(0xe5e5e5));
	        label.setBounds(30, 25,150, 50);
	        label.setFont(new Font("SansSerif", Font.BOLD, 60));
	        add(label);
	        
	    	newGame.setBackground(new Color(0x82ff9a));
	        newGame.setBounds(230, 30,130, 40);
	    	add(newGame);
	    	
	    	newGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	startGame();
	                repaint();
                }
            });
	    	
	    	exit.setPreferredSize(new Dimension(150,50));
	    	exit.setBackground(new Color(0xe98585));
	    	exit.setBounds(370, 30,130, 40);
	    	add(exit);
	    	
	    	exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	System.exit(1);
                }
            });
	    	
	    	//make game place active
	        newGame.setFocusable(false);
     	    startGame();

	        addKeyListener(new KeyAdapter() {
	            @Override
	            public void keyPressed(KeyEvent e) {
	                switch (e.getKeyCode()) {
	                    case KeyEvent.VK_UP:
	                        moveUp();
	                        break;
	                    case KeyEvent.VK_DOWN:
	                        moveDown();
	                        break;
	                    case KeyEvent.VK_LEFT:
	                        moveLeft();
	                        break;
	                    case KeyEvent.VK_RIGHT:
	                        moveRight();
	                        break;
	                }
	                repaint();
	            }
	        });
	    }
	 
	    @Override
	    public void paintComponent(Graphics gg) {
	        super.paintComponent(gg);
	        Graphics2D g = (Graphics2D) gg;
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        				   RenderingHints.VALUE_ANTIALIAS_ON);
	        drawGrid(g);
	    }
	 
	   void startGame() {
	            score = 0;
	            highest = 0;
	            gamestate = State.running;
	            tiles = new Tile[tilesNumberInRow][tilesNumberInRow];
	            addRandomTile();
	            addRandomTile();
	   }
	 
	    void drawGrid(Graphics2D g) {
	    	//background square
	        g.setColor(new Color(0xB89B90));
	        g.fillRoundRect(10, 110, 499, 499, 15, 15);

	           if(gamestate == State.running) { 
	        	   for (int r = 0; r < tilesNumberInRow; r++) {
	                for (int c = 0; c < tilesNumberInRow; c++) {
	                    if (tiles[r][c] == null) {
	                    	//empty map for tiles
	                        g.setColor(new Color(0xB8ADA2));
	                        g.fillRoundRect(25 + c * 121, 125 + r * 121, 106, 106, 7, 7);
	                    } else {
	                    	//tiles
	                        drawTile(g, r, c);
	                    }
	                }
	            }
	           } 
	            g.setFont(new Font("SansSerif", Font.BOLD, 20));
	 
	            if (gamestate == State.won) {
	            	//background square
	            	g.setColor(new Color(0xFFEBCD));
	            	g.fillRoundRect(25, 125, 469, 469, 7, 7);
	            	
	            	//message
	            	g.setColor(new Color(0xA89B90).darker());
	 	            g.setFont(new Font("SansSerif", Font.BOLD, 128));
	 	            g.drawString("you", 150, 350);  
	 	            g.drawString("win", 150, 450);
	 	            
	            } else if (gamestate == State.over) {
	            	//background square
	            	g.setColor(new Color(0xFFEBCD));
	            	g.fillRoundRect(25, 125, 469, 469, 7, 7);
	            	
	            	//message
	            	g.setColor(new Color(0xA89B90).darker());
	 	            g.setFont(new Font("SansSerif", Font.BOLD, 128));
	                g.drawString("game", 100, 350);  
	                g.drawString("over", 135, 450);    
	            }
	      
	        
	    }
	 
	    void drawTile(Graphics2D g, int r, int c) {
	        int value = tiles[r][c].getValue();
	 
	        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);
	        g.fillRoundRect(25 + c * 121, 125 + r * 121, 106, 106, 7, 7);
	        String s = String.valueOf(value);
	 
	        g.setColor(value < 128 ? colorTable[0] : colorTable[1]);
	 
	        FontMetrics fm = g.getFontMetrics();
	        int asc = fm.getAscent();
	        int dec = fm.getDescent();
	 
	        int x = 25 + c * 121 + (106 - fm.stringWidth(s)) / 2;
	        int y = 125 + r * 121 + (asc + (106 - (asc + dec)) / 2);
	 
	        g.drawString(s, x, y);
	    }
	 
	 
	    private void addRandomTile() {
	        int pos = rand.nextInt(tilesNumberInRow * tilesNumberInRow);
	        int row;
	        int col;
	        do {
	            pos = (pos + 1) % (tilesNumberInRow * tilesNumberInRow);
	            row = pos / tilesNumberInRow;
	            col = pos % tilesNumberInRow;
	        } while (tiles[row][col] != null);
	 
	        int val = rand.nextInt(10) == 0 ? 4 : 2;
	        tiles[row][col] = new Tile(val);
	    }
	 
	    private boolean move(int countDownFrom, int yIncr, int xIncr) {
	        boolean moved = false;
	 
	        for (int i = 0; i < tilesNumberInRow * tilesNumberInRow; i++) {
	            int j = Math.abs(countDownFrom - i);
	 
	            int r = j / tilesNumberInRow;
	            int c = j % tilesNumberInRow;
	 
	            if (tiles[r][c] == null)
	                continue;
	 
	            int nextR = r + yIncr;
	            int nextC = c + xIncr;
	 
	            while (nextR >= 0 && nextR < tilesNumberInRow && nextC >= 0 && nextC < tilesNumberInRow) {
	 
	                Tile next = tiles[nextR][nextC];
	                Tile curr = tiles[r][c];
	 
	                if (next == null) {
	                	
	                    if (checkingAvailableMoves)
	                        return true;
	 
	                    tiles[nextR][nextC] = curr;
	                    tiles[r][c] = null;
	                    r = nextR;
	                    c = nextC;
	                    nextR += yIncr;
	                    nextC += xIncr;
	                    moved = true;
	 
	                } else if (next.canMergeWith(curr)) {
	 
	                    if (checkingAvailableMoves)
	                        return true;
	 
	                    int value = next.mergeWith(curr);
	                    if (value > highest) 
	                    highest = value;
	                    score += value;
	                    tiles[r][c] = null;
	                    moved = true;
	                    break;
	                    
	                } else
	                    break;
	            }
	        }
	 
	        if (moved) {
	            if (highest < goal) {
	                clearMerged();
	                addRandomTile();
	                if (!movesAvailable()) {
	                    gamestate = State.over;
	                }
	            } else if (highest == goal)
	                gamestate = State.won;
	        }
	 
	        return moved;
	    }
	    
	    boolean gameOver() {
	    	if(gamestate == State.over) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	    }
	   
	    boolean moveUp() {
	        return move(0, -1, 0);
	    }
	 
	    boolean moveDown() {
	        return move(tilesNumberInRow * tilesNumberInRow - 1, 1, 0);
	    }
	 
	    boolean moveLeft() {
	        return move(0, 0, -1);
	    }
	 
	    boolean moveRight() {
	        return move(tilesNumberInRow * tilesNumberInRow - 1, 0, 1);
	    }
	 
	    void clearMerged() {
	        for (Tile[] row : tiles)
	            for (Tile tile : row)
	                if (tile != null)
	                    tile.setMerged(false);
	    }
	 
	    boolean movesAvailable() {
	        checkingAvailableMoves = true;
	        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
	        checkingAvailableMoves = false;
	        return hasMoves;
	    }
	    
	    
	    public static void main(String[] args) {
			JFrame frame = new JFrame();
			Gameplay gamePlay = new Gameplay(); 
			
			frame.setSize(535, 660);
			frame.setTitle("2048");
			frame.setResizable(false);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.add(gamePlay);

		}
}
 