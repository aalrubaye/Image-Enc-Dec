import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class EncDecImages {
	
	// The main class
	public static void main(String[] args) {
	
	    BufferedImage keyimg,msgimg;
	
	    try {
	    	keyimg = ImageIO.read(new File("key.bmp"));
	        msgimg = ImageIO.read(new File("msg.bmp"));
	         
	        // creating the encrypted and the decrypted images with initial values of key image
	        BufferedImage encryptedimage = ImageIO.read(new File("key.bmp"));
	        BufferedImage decryptedimage = ImageIO.read(new File("key.bmp"));
	        
	        int ww= keyimg.getWidth();
	        int hh= keyimg.getHeight();
	        
	        int[][] keypixel = new int[hh * ww][3];
	        int[][] msgpixel = new int[hh * ww][3];
	        int[][] crypixel = new int[hh * ww][3];
	        int[][] decpixel = new int[hh * ww][3];
	        
	        int[] keyrgb = null;
	        int[] msgrgb = null;
	        int[] cryrgb = null;
	        int[] decrgb = null;
	        int rrr;
	
	        int counter = 0;
	        
	        
	        for(int i = 0; i < ww; i++){
	            for(int j = 0; j < hh; j++){
	                keyrgb = getPixelData(keyimg, i, j);
	                msgrgb = getPixelData(msgimg, i, j);
	                cryrgb = getPixelData(keyimg, i, j);
	                decrgb = getPixelData(keyimg, i, j);
	                              
	
	                for(int k = 0; k < keyrgb.length; k++){
	                    keypixel[counter][k] = keyrgb[k];
	                    msgpixel[counter][k] = msgrgb[k];
	                    crypixel[counter][k] = cryrgb[k];
	                    decpixel[counter][k] = decrgb[k];
	                }
	
	                counter++;
	            }
	        }
	
	        
	        int ff=0;
	    
	        for (int i=0; i<= (ww-2); i=i+2)
	        	for (int j=0; j<= (hh-2); j=j+2)
	        	{
	        		int ptrn = pattern(msgpixel,ff);		
	        			if (ptrn>1)
	        				arrange(crypixel,ff);
	        		ff += 2;	
	        			if ((ff % hh) == 0) 
	        				ff += hh;		
	        	}
	        		
	        int kk= 0;
	        for(int i = 0; i < ww; i++){
	            for(int j = 0; j < hh; j++)
	            {
	            	cryrgb[0] = crypixel[kk][0]; cryrgb[1] = crypixel[kk][1]; cryrgb[2] = crypixel[kk][2];
	            	
	            	rrr = getIntt(cryrgb[0],cryrgb[1],cryrgb[2]);
	                encryptedimage.setRGB(i, j, rrr);
	                
	            	kk ++;    
	            }
	        }
	        
	        
	        File outputfile = new File("encrypted.bmp");
	        ImageIO.write(encryptedimage, "bmp", outputfile);
	        
	        
	        /// Decryption
	        
	        	        
	        for (int c=0; c<counter; c++)
	        {
	        	if (keypixel[c][0] != crypixel[c][0])
	        	{
	        		decpixel[c][0]= 0; decpixel[c][1]= 0; decpixel[c][2]= 0;
	        		//decpixel[c+1][0]= 0; decpixel[c+1][1]= 0; decpixel[c+1][2]= 0;
	        	}
	        }
	        
	        
	        kk= 0;
	        for(int i = 0; i < ww; i++){
	            for(int j = 0; j < hh; j++)
	            {
	            	decrgb[0] = decpixel[kk][0]; decrgb[1] = decpixel[kk][1]; decrgb[2] = decpixel[kk][2];
	            	
	            	rrr = getIntt(decrgb[0],decrgb[1],decrgb[2]);
	                decryptedimage.setRGB(i, j, rrr);
	                
	            	kk ++;    
	            }
	        }
	        
	        
	        outputfile = new File("decrypted.bmp");
	        ImageIO.write(decryptedimage, "bmp", outputfile);
	    } 
	    
	    catch (IOException e) 
	    {
	       e.printStackTrace();
	    }
	
	}
	
	//This class is responsible to rearrange the pattern with more than 1 black pixel
	private static void arrange(int[][] c, int ff){
		
		if (c[ff][0]== 0)
		{
			c[ff][0] = 255; c[ff][1] = 255; c[ff][2] = 255;
			c[ff+1][0] = 255; c[ff+1][1] = 255; c[ff+1][2] = 255;
			c[ff+480][0] = 255; c[ff+480][1] = 255; c[ff+480][2] = 255;
			c[ff+481][0] = 0; c[ff+481][1] = 0; c[ff+481][2] = 0;
			
		}
		else 
		{
			if (c[ff+1][0]==0)
			{
				c[ff][0] = 255; c[ff][1] = 255; c[ff][2] = 255;
				c[ff+1][0] = 255; c[ff+1][1] = 255; c[ff+1][2] = 255;
				c[ff+480][0] = 0; c[ff+480][1] = 0; c[ff+480][2] = 0;
				c[ff+481][0] = 255; c[ff+481][1] = 255; c[ff+481][2] = 255;
			}
			else
			{
				if (c[ff+480][0]==0)
				{
					c[ff][0] = 255; c[ff][1] = 255; c[ff][2] = 255;
					c[ff+1][0] = 0; c[ff+1][1] = 0; c[ff+1][2] = 0;
					c[ff+480][0] = 255; c[ff+480][1] = 255; c[ff+480][2] = 255;
					c[ff+481][0] = 255; c[ff+481][1] = 255; c[ff+481][2] = 255;
			
				} 
				else
				{		
					c[ff][0] = 0; c[ff][1] = 0; c[ff][2] = 0;
					c[ff+1][0] = 255; c[ff+1][1] = 255; c[ff+1][2] = 255;
					c[ff+480][0] = 255; c[ff+480][1] = 255; c[ff+480][2] = 255;
					c[ff+481][0] = 255; c[ff+481][1] = 255; c[ff+481][2] = 255;
				}
		
			}
		}
	}
	
	//This class is for getting the pixel data (Red, Green, and Blue values)
	private static int[] getPixelData(BufferedImage img, int x, int y) {
	int argb = img.getRGB(x, y);
	
	int rgb[] = new int[] {
	    (argb >> 16) & 0xff, //red
	    (argb >>  8) & 0xff, //green
	    (argb      ) & 0xff  //blue
	};
	return rgb;
	}
	
	//This class recognized whether the 2×2 pixels pattern is following the general distribution or not 
	private static int pattern(int[][] ke, int cc){
		
		int black=0; 
		
		if (ke[cc][0] == 0) black += 1;
		if (ke[cc+1][0] == 0) black += 1;
		if (ke[cc+480][0] == 0) black += 1;
		if (ke[cc+481][0] == 0) black += 1;
			
		return black;
	}
	
	// This class return an integer value from 3 values of R,G,B
	private static int getIntt(int Red, int Green, int Blue){
	    
		Red = (Red << 16) & 0x00FF0000; 
	    Green = (Green << 8) & 0x0000FF00; 
	    Blue = Blue & 0x000000FF;
	    return 0xFF000000 | Red | Green | Blue;
	}
	
}
// The end
