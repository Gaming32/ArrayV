package visuals.image;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.ArrayVisualizer;
import panes.JErrorPane;
import resources.image.ImgFetcher;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2020 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/*
 *  CustomImage visual and sort bar graph artwork (image/pic.jpg) created by
 *  aphitorite (https://github.com/aphitorite/ArrayVisualizer)
 */
final public class CustomImage extends Visual {
	private BufferedImage img;
	private int imgHeight;
    private int imgWidth;
    
	private boolean imgImported;
	private boolean imgScaled;
	
	private int windowHeight;
	private int windowWidth;
	
	private JOptionPane info;
	private JDialog infoMsg;
	
	public CustomImage(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.imgImported = false; // Don't load the image unless the user selects the
                                  // 'Custom Image' visual. Program initially boots up
                                  // faster this way.
        this.updateWindowDims(ArrayVisualizer);
    }
	
	private void updateImageDims() throws Exception {
	    this.imgHeight = this.img.getHeight();
	    this.imgWidth = this.img.getWidth();
	}
	
	private void updateWindowDims(ArrayVisualizer ArrayVisualizer) {
	    this.windowHeight = ArrayVisualizer.windowHeight();
        this.windowWidth = ArrayVisualizer.windowWidth();
	}
	
	@SuppressWarnings("unused")
	private boolean fetchBufferedImage(boolean showInfoMsg, JFrame window) {
	    // New copy of pic.jpg being imported; has not been scaled yet
	    this.imgScaled = false;
	    
	    if(showInfoMsg) {
	        this.info = new JOptionPane("Loading image/pic.jpg...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
	        this.infoMsg = this.info.createDialog(window, "Info");
	        this.infoMsg.setModalityType(ModalityType.MODELESS);
	        this.infoMsg.setAlwaysOnTop(this.infoMsg.isAlwaysOnTopSupported());
	        this.infoMsg.pack();
	        this.infoMsg.setVisible(true);
	    }

	    boolean success = true;
	    
	    ImgFetcher imgFetcher = new ImgFetcher();
        InputStream stream = imgFetcher.getStream();
        
        try {
            this.img = ImageIO.read(stream);
        }
        catch (IOException e) {
            success = false;
            JErrorPane.invokeErrorMessage(e);
        }
        catch (IllegalArgumentException e) {
            success = false;
            JErrorPane.invokeCustomErrorMessage("image/pic.jpg missing: Couldn't find the default image for the program's 'Custom Image' visual!");
        }
        finally {
            try {
                stream.close();
            }
            catch (Exception e) {
                success = false;
                JErrorPane.invokeErrorMessage(e);
            }
        }
        
        // Update the image dimensions. If this fails, the file wasn't a jpg
        try {
            this.updateImageDims();
        }
        catch (Exception e) {
            success = false;
            JErrorPane.invokeCustomErrorMessage("image/pic.jpg invalid or corrupt: The file for the program's 'Custom Image' visual was not recognized as a proper JPEG!");
        }
        
        if(showInfoMsg) {
            this.infoMsg.setVisible(false);
            this.infoMsg.dispose();
        }
        return success;
	}
	
	// Many thanks to Jörn Horstmann for providing fast image scaling code.
	// https://stackoverflow.com/questions/3967731/how-to-improve-the-performance-of-g-drawimage-method-for-resizing-images/3967988#3967988
	@SuppressWarnings("unused")
	private boolean getScaledImage(int width, int height) throws Exception {
	    boolean success = true;
	    
	    // Only fetch a fresh copy of the image if it's been previously scaled.
	    if(this.imgScaled) {
	        if(!this.fetchBufferedImage(false, null)) {
	            throw new Exception();
	        }
	    }
	    
	    double scaleX = (double) width / this.imgWidth;
	    double scaleY = (double) height / this.imgHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

	    try {
	        this.img = bilinearScaleOp.filter(this.img, new BufferedImage(width, height, this.img.getType()));
	        
	        /*
	         *  We don't want to resize the cached copy of the image more than once as the quality degrades quickly.
	         *  Therefore, keep track of the image being scaled so we can import a fresh copy the next time the window
	         *  is resized.
	         */
	        this.imgScaled = true;
	        
	        this.updateImageDims();
	    }
	    catch (IllegalArgumentException e) {
	        JErrorPane.invokeCustomErrorMessage("CustomImage.getScaledImage() was called even though the image's dimensions haven't changed!");
	    }
	    catch (Exception e) {
	        success = false;
	        JErrorPane.invokeErrorMessage(e);
	    }
	    
	    return success;
	}
	
    @Override
    @SuppressWarnings("unused")
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        try {
            /*
             * Load the image on first use of the 'Custom Image' visual or if the program failed to read the image file previously.
             * Gives debuggers the ability to try another file without having to restart the program. This also is a safe way of
             * handling exceptions whenever the user clicks the 'Custom Image' button.
             */
            if(!this.imgImported) {
                this.imgImported = this.fetchBufferedImage(true, ArrayVisualizer.getMainWindow());
                if(!this.imgImported) {
                    throw new Exception();
                }
            }
            /*
             * Use a fast image scaling method if the window was resized. If an ImagingOpException is thrown, don't continue with
             * the 'Custom Image' visual.
             */
            if(this.windowHeight != ArrayVisualizer.currentHeight() || this.windowWidth != ArrayVisualizer.currentWidth()) {
                if(!this.getScaledImage(ArrayVisualizer.currentWidth(), ArrayVisualizer.currentHeight())) {
                    throw new Exception();
                }
                this.updateWindowDims(ArrayVisualizer);
            }
        }
        catch (Exception e) {
            ArrayVisualizer.setVisual(visuals.VisualStyles.BARS);
            return;
        }
        
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
			int width = (int) (Renderer.getXScale() * (i + 1) - Renderer.getOffset());
			
			if(i < Highlights.getFancyFinishPosition() || Highlights.containsPosition(i)) {
				if(ArrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.WHITE);
				else this.mainRender.setColor(Color.BLACK);
				
				this.mainRender.fillRect(Renderer.getOffset() + 20, 0, width, ArrayVisualizer.windowHeight());
			} else {
				//Cuts the image in respect to each item in the array
				this.mainRender.drawImage(
					this.img,
					
					Renderer.getOffset() + 20,
					0, 
					Renderer.getOffset() + 20 + width, 
					ArrayVisualizer.windowHeight(),
					
					(int) ((double) this.imgWidth / ArrayVisualizer.getCurrentLength() * array[i]),
					0, 
					(int) Math.ceil((double) this.imgWidth / ArrayVisualizer.getCurrentLength() * (array[i] + 1)),
					this.imgHeight,
					
					null
				);
			}
			
			Renderer.setOffset(Renderer.getOffset() + width);
		}
    }
}