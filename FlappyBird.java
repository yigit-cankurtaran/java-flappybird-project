package proje2;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener,KeyListener{//Oyunu başlatmak ve devam ettirmek için kullanılan interface
	
	public static FlappyBird flappyBird;

	public final int genislik = 1280, yukseklik = 720;
	
	public Renderer renderer;
	
	public Rectangle kus;//kuşu oluşturmak için Rectangle sınıfını kullandık.
	
	public ArrayList<Rectangle> sutunlar;
	
	public int tikla,yHareket,puan;
	
	public boolean oyunBitti , basla;
	
	
	
	public Random rand;
	
	public FlappyBird() {
		//constructer oluşturduk.
		
		JFrame jframe = new JFrame();//arayüz oluşturmak için JFrame sınıfını kullandık.
		Timer timer = new Timer(20,this);//zamanlayıcı
		
		renderer = new Renderer();
		rand = new Random();
		
		
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");//oyunun başlığı
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(genislik, yukseklik);//boyut ayarlandı.
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setVisible(true);// 
		jframe.setResizable(false);//pencerenin yeniden boyutlandırılmasını önledik.
		
		kus = new Rectangle(genislik/2 -10,yukseklik/2 -10,20,20);
		sutunlar = new ArrayList<Rectangle>();
		
		sutunOlustur(true);
		sutunOlustur(true);
		sutunOlustur(true);
		sutunOlustur(true);
		
		timer.start();
	}
	
	public void sutunOlustur(boolean basla) {
		int bosluk = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if(basla) {
			sutunlar.add(new Rectangle(genislik +  width + sutunlar.size() * 300, yukseklik - height - 120, width, height));
			sutunlar.add(new Rectangle(genislik + width + (sutunlar.size() - 1) * 300, 0, width, yukseklik - height - bosluk));
		}
		else {
			sutunlar.add(new Rectangle(sutunlar.get(sutunlar.size() - 1).x + 600, yukseklik - height - 120, width, height));
			sutunlar.add(new Rectangle(sutunlar.get(sutunlar.size() - 1).x, 0, width, yukseklik - height - bosluk));
		
		}
			
	
	}
	
	
	
	public void paintColumn(Graphics g ,Rectangle sutun) {//Sutun oluşturan metod.
		g.setColor(Color.green.darker());//Sutun rengi.
		g.fillRect(sutun.x, sutun.y, sutun.width, sutun.height);
	}
	
	public void zipla() {
		if(oyunBitti) {
			
			kus = new Rectangle(genislik/2 -10,yukseklik/2 -10,20,20);
			sutunlar.clear();
			yHareket = 0;
			puan = 0;
			
			
			sutunOlustur(true);
			sutunOlustur(true);
			sutunOlustur(true);
			sutunOlustur(true);
			
			oyunBitti = false;
		}
		if(!basla) {
			basla = true;
		}
		else if(!oyunBitti){
			if(yHareket > 0) {
				yHareket = 0;
			}
			yHareket -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 
		int hiz = 10;
		
		tikla++;
		
		if(basla) {//oyun başlayana kadar kuşun hareket etmemesini sağlar.
		
		for(int i = 0; i < sutunlar.size(); i++) {//sütunları kaydırma
			Rectangle sutun = sutunlar.get(i);
			sutun.x -= hiz;
		}
		
		if(tikla % 2 == 0 && yHareket < 15) {//kuşun y ekseninde hareketi.
			yHareket += 2;
		}
		
		for(int i = 0; i < sutunlar.size(); i++) {
			Rectangle sutun = sutunlar.get(i);
			
			if(sutun.x + sutun.width < 0) {
				sutunlar.remove(sutun);
				
				if(sutun.y == 0) {
					sutunOlustur(false);
				}
				
			}
		}
		
		
		kus.y += yHareket;
		
		for(Rectangle sutun: sutunlar) {
			
			if(sutun.y == 0 && kus.x + kus.width / 2 > sutun.x + sutun.width / 2 - 10 && kus.x + kus.width / 2 < sutun.x + sutun.width / 2 + 10) {
				//kuş sütundan geçtiğinde puanı artırır.
				
				puan++;
				
			}
			
			
			if(sutun.intersects(kus)) {//kuşun sütuna çarpmasını kontrol ediyoruz.
				oyunBitti = true;
				
				if(kus.x <= sutun.x) {
					
					kus.x = sutun.x - kus.width;
				}
				else {
					if(sutun.y != 0 ) {
						kus.y = sutun.y - kus.height;
					}
					else if(kus.y < sutun.height) {
						
						kus.y = sutun.height;
					}
				}
				
				
			}
			
		}
		
		if(kus.y > yukseklik-120 || kus.y < 0) {
			
			oyunBitti = true;
		}
		if(kus.y + yHareket >= yukseklik - 120) { //kuşu yürütme
			kus.y = yukseklik - 120 - kus.height;
		}
	}
		
		renderer.repaint();
	}
	
	
	public void repaint(Graphics g) {
		g.setColor(Color.cyan);//Arka plan rengi
		g.fillRect(0, 0, genislik, yukseklik);//Arka planı renklendirmeye yarayan metod.
		
		g.setColor(Color.orange);//Zemin rengi
		g.fillRect(0, yukseklik - 150, genislik, 150);
		
		g.setColor(Color.green);//zemin üstündeki çimen rengi
		g.fillRect(0, yukseklik - 150, genislik, 20);
		
		g.setColor(Color.yellow);//kuşun rengi.
		g.fillRect(kus.x, kus.y, kus.width, kus.height);
		
		for(Rectangle sutun : sutunlar) {//sütun oluşturdu ve boyadı.
			paintColumn(g, sutun);
		}
		g.setColor(Color.white);
		g.setFont(new Font ("Palatino Linotype", 1, 90));//Yazı tipini belirler.
		if(!basla) {
			g.drawString("Başlamak için tıklayın.", 75, yukseklik / 2 - 50);
		}
		if(oyunBitti) {//game over yazısını ekrana yazdırır.
			g.drawString("Oyun bitti", 400, yukseklik / 2 - 50);
		}
		
		if(!oyunBitti && basla) {
			g.drawString(String.valueOf(puan), genislik / 2 - 25, 100);
			}
		}
	

	
	public static void main(String[] args) {
		flappyBird = new FlappyBird();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		zipla();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			zipla();
		}
		
	}
}