package finalllsssss;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Tools {
	public static int within256(int n){
		if (n > 255)
			return 255;
		else if (n < 0)
			return 0;
		else
			return n;
	}
	
	/** counts the pixels that are black in an array**/
	public static int countPixels(int[] pixels){
		int count = 0;
		for (int i = 0; i < 8; i++){
			if(pixels[i] == 0){
				count++;
			}
		}
		return count;
	}
	
	/** calculates the percentage black of a subsection of the image**/
	public static float percentBlack(int x, int y, int width, int height, BufferedImage img){
		int b = 0;
		int w = 0;
		
		int tempw = x+width;
		int temph = y+height;
		
		if(img.getWidth()-tempw < 10)
			tempw = img.getWidth();
		if(img.getHeight()-temph < 10)
			temph = img.getHeight(); // ensures every pixel is captured
		for(int i = x; i < tempw; i++){
			for (int j = y; j < temph; j++){
				Color c= new Color(img.getRGB(i, j));
				if( c.getRed() == 255)
					w++; 
				if( c.getRed() == 0)
					b++; 
			}
		}
		float total = w + b;
		float res = b/total;
		return res;
	}

	/** removes the border of an image**/
	public static BufferedImage chopBorder(BufferedImage bimg){
		BufferedImage res = new BufferedImage(bimg.getWidth()-2,bimg.getHeight()-2,BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < res.getHeight();y++){
			for (int x = 0; x < res.getWidth();x++){
				res.setRGB(x, y, bimg.getRGB(x+1, y+1));
			}
		}
		return res;
	}
	
	/** sets the pixels to either white or black **/
	public static double setBlack(BufferedImage bimg){
		double black = 0;
		for (int x = 1; x < bimg.getWidth()-1; x++){
			for (int y = 1; y < bimg.getHeight()-1; y++){
				Color c= new Color(bimg.getRGB(x, y));
				if( c.getRed() <= 126){
					//black
					black++;
					bimg.setRGB(x, y, Color.black.getRGB());
				}else{
					bimg.setRGB(x, y, Color.white.getRGB());
				}
			}
		}
		return black;
	}
	
	/** counts the number of occurrences of 1,0 in the array **/
	public static int count01s(int[] pixels){
		int i = 0;
		for(int j = 0; j < 7;j++){
			if(pixels[j] == 0 && pixels[j+1] ==1) 
				i++;
		}
		if (pixels[7] == 0 && pixels[0] == 1) i++;
		return i;
	}
	
	/** gets an array containing the pixels surrounding the image at x, y**/
	public static int[] getBoundaryPixels(BufferedImage bimg,int x, int y){	
		if(bimg == null)
			return null;
		int [] temp = new int[9];
		int k = 0;
		for (int j = 0; j < 3; j++){
			for (int i = 1; i <= 3; i++){
			
				Color c= new Color(bimg.getRGB(x+i-2, y+j-1));
				if( c.getRed() == 255)
					temp[k]= 1; 
				if( c.getRed() == 0)
					temp[k]=0;
				k++;
			}
		}
		
		int [] temp2= new int[]{temp[0],temp[1],temp[2],temp[5],temp[8],temp[7],temp[6],temp[3],temp[4]}; //rearrange
		return temp2;
	}

	/** creates a black border around the image**/
	public static BufferedImage resize(BufferedImage bimg){
		//resize bimg
		BufferedImage temp = new BufferedImage(bimg.getWidth()+2, bimg.getHeight()+2, BufferedImage.TYPE_INT_RGB);
		
		for (int j = 0; j < bimg.getHeight(); j++){
			for (int i = 0; i < bimg.getWidth(); i++){
				temp.setRGB(i+1, j+1, bimg.getRGB(i, j));
			}
		}
		return temp;
	}
	
	/** crops the image **/
	public static BufferedImage autoCrop(BufferedImage img){
		int earliestX=img.getWidth(),earliestY=img.getHeight();
		int latestX=1,latestY=1;
		for (int x = 1; x < img.getWidth()-1; x++){
			for (int y = 1; y < img.getHeight()-1; y++){
				Color c= new Color(img.getRGB(x, y));

				if( c.getRed() == 0){
					//black
					if (x < earliestX)
						earliestX = x;
					if (y < earliestY)
						earliestY = y;
					if (x > latestX)
						latestX = x;
					if (y > latestY)
						latestY = y;
					
				}
			}
		}
		try{
		BufferedImage temp = new BufferedImage(latestX-earliestX+1, latestY-earliestY+1, BufferedImage.TYPE_INT_RGB);
		for (int x = earliestX; x <= latestX; x++){
			for (int y = earliestY; y <= latestY; y++){
				temp.setRGB(x-earliestX,y-earliestY,img.getRGB(x, y));
			}
		}
		return temp;
		}
		catch(Exception e){return img;}
	}
}
