package GPP_Demos.imageProcessing

import java.awt.image.*
import javax.imageio.ImageIO

def imageFile = new File("Y:\\Exported Albums\\Easter Trip\\DSC_0120-002.jpg")
BufferedImage img = ImageIO.read(imageFile)

int width = img.getWidth()
int height = img.getHeight()

println "H = $height, w = $width"

0.upto(height-1) { y->
	0 .upto(width-1){ x->
		int p = img.getRGB(x,y)

		int a = (p>>24)&0xff
		int r = (p>>16)&0xff
		int g = (p>>8)&0xff
		int b = p&0xff

		//calculate average
		int avg = (r+g+b)/3

		//replace RGB value with avg
		p = (a<<24) | (avg<<16) | (avg<<8) | avg

		img.setRGB(x, y, p)
	}
}
def outImage = new File ("D:\\org_jcsp_gpp\\org_jcsp_gpp_01\\src\\gpp\\skeletons\\image\\DSC_0120-002GS.jpg")
ImageIO.write(img, "jpg", outImage)

println "finished"