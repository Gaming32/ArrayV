package visuals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import dialogs.CustomImageDialog;
import dialogs.LoadingDialog;
import dialogs.SoundbankDialog;
import frames.ImageFrame;
import main.ArrayVisualizer;
import panes.JErrorPane;
import resources.image.ImgFetcher;
import utils.Highlights;
import utils.Renderer;

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
	private volatile BufferedImage img;
	private volatile int imgHeight;
    private volatile int imgWidth;
    
	private boolean imgImported;
	private boolean imgScaled;
	private boolean openImgMenu;
	
	private int windowHeight;
	private int windowWidth;
	
	private volatile ImageFrame pictureMenu;
	private volatile LoadingDialog infoMsg;
	
	final private String defaultArtwork = "Illustration 17 by aphitorite";
	private String currentImage;
	private File imageFile;
	
	public CustomImage(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        this.imgImported = false; // Don't load the image unless the user selects the
                                  // 'Custom Image' visual. Program initially boots up
                                  // faster this way.
        this.enableImgMenu();
        this.updateWindowDims(ArrayVisualizer);
        this.currentImage = this.defaultArtwork;
    }
	
	public BufferedImage getImage() {
	    return this.img;
	}
	public int getImgHeight() {
	    return this.imgHeight;
	}
	public int getImgWidth() {
	    return this.imgWidth;
	}
	
	public String getCurrentImageName() {
	    return this.currentImage;
	}
	
	public void enableImgMenu() {
	    this.openImgMenu = true;
	}
	
	private void updateImageDims() throws Exception {
	    this.imgHeight = this.img.getHeight();
	    this.imgWidth = this.img.getWidth();
	}
	
	private void updateWindowDims(ArrayVisualizer ArrayVisualizer) {
	    this.windowHeight = ArrayVisualizer.windowHeight();
        this.windowWidth = ArrayVisualizer.windowWidth();
	}
	
	private void refreshCustomImage(ImageFrame menu) {
	    menu.dispose();
	    this.imgImported = false;
	    this.imgScaled = false;
	    this.openImgMenu = true;
	}
	
	public void loadDefaultArtwork(ImageFrame menu) {
	    this.currentImage = this.defaultArtwork;
	    this.refreshCustomImage(menu);
	}
	public void loadCustomImage(ImageFrame menu) {
	    CustomImageDialog dialog = new CustomImageDialog();
        this.imageFile = dialog.getFile();
        if(this.imageFile != null) {
            this.currentImage = this.imageFile.getName();
            this.refreshCustomImage(menu);
        }
	}
	
	@SuppressWarnings("unused")
	private boolean fetchBufferedImage(boolean showInfoMsg, JFrame window) {
	    // New copy of image being imported; has not been scaled yet
	    this.imgScaled = false;
	    
	    boolean defaultImage = this.currentImage.equals(this.defaultArtwork);
	    
	    if(showInfoMsg) {
	        String message;
	        if(defaultImage) {
	            message = "resources/image/pic.jpg";
	        }
	        else {
	            message = this.currentImage;
	        }
	        this.infoMsg = new LoadingDialog(message, window);
	    }

	    boolean success = true;
	    
	    ImgFetcher imgFetcher;
	    if(defaultImage) {
            imgFetcher = new ImgFetcher();
        }
        else {
            imgFetcher = new ImgFetcher(this.imageFile);
        }
        BufferedInputStream stream = imgFetcher.getStream();
        
        try {
            this.img = ImageIO.read(stream);
        }
        catch (IOException e) {
            success = false;
            JErrorPane.invokeErrorMessage(e);
        }
        catch (IllegalArgumentException e) {
            success = false;
            if(defaultImage) {
                JErrorPane.invokeCustomErrorMessage("image/pic.jpg missing: Couldn't find the default image for the program's 'Custom Image' visual!");
            }
            else {
                JErrorPane.invokeCustomErrorMessage(this.currentImage + " missing: ArrayV couldn't find your picture at the given location!");
            }
        }
        finally {
            try {
                stream.close();
            }
            catch (NullPointerException e) {
                success = false; // A NullPointerException means a null stream, which would have already thrown an IllegalArgumentException
            }
            catch (Exception e) {
                success = false;
                JErrorPane.invokeErrorMessage(e);
            }
        }
        
        // Update the image dimensions. If this fails, the file wasn't a proper image
        if(success) {
            try {
                this.updateImageDims();
            }
            catch (Exception e) {
                success = false;
                if(defaultImage) {
                    JErrorPane.invokeCustomErrorMessage("image/pic.jpg invalid or corrupt: The file for the program's 'Custom Image' visual was not recognized as a valid image!");
                }
                else {
                    JErrorPane.invokeCustomErrorMessage(this.currentImage + " invalid or corrupt: Your picture was not recognized as a valid image!");
                }
            }
        }
        
        if(showInfoMsg) {
            this.infoMsg.closeDialog();
        }
        
        if(!success) {
            // If loading a custom file didn't work, then try loading the default artwork instead.
            if(!defaultImage) {
                this.currentImage = this.defaultArtwork;
                return this.fetchBufferedImage(true, window);
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
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
	
	public static void markCustomBar(ArrayVisualizer ArrayVisualizer, Graphics2D bar, Renderer Renderer, int width, boolean analysis) {
        if(analysis) {
            bar.setColor(new Color(0, 0, 1, .5f));
        }
        else {
            bar.setColor(new Color(1, 0, 0, .5f));
        }
        bar.fillRect(Renderer.getOffset() + 20, 0, width, ArrayVisualizer.windowHeight());
    }
	    
	@SuppressWarnings("fallthrough")
    //The longer the array length, the more bars marked. Makes the visual easier to see when bars are thinner.
    public static void colorCustomBars(int logOfLen, int index, Highlights Highlights, ArrayVisualizer ArrayVisualizer, Graphics2D bar, Renderer Renderer, int width, boolean analysis) {
        switch(logOfLen) {
        case 15: if(Highlights.containsPosition(index - 15)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 14)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 13)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 12)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 11)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        case 14: if(Highlights.containsPosition(index - 10)) { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 9))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 8))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        case 13: if(Highlights.containsPosition(index - 7))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 6))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 5))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        case 12: if(Highlights.containsPosition(index - 4))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
                 if(Highlights.containsPosition(index - 3))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        case 11: if(Highlights.containsPosition(index - 2))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        case 10: if(Highlights.containsPosition(index - 1))  { markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis); break; }
        default: if(Highlights.containsPosition(index))        markCustomBar(ArrayVisualizer, bar, Renderer, width, analysis);
        }
    }
	
    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        try {
            /*
             * Load the image on first use of the 'Custom Image' visual or if the program failed to read the image file previously.
             * Gives debuggers the ability to try another file without having to restart the program. This also is a safe way of
             * handling exceptions whenever the user clicks the 'Custom Image' button.
             */
            if(!this.imgImported) {
                if(!this.fetchBufferedImage(true, ArrayVisualizer.getMainWindow())) {
                    throw new Exception();
                }
                else {
                    this.imgImported = true;
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
            
            if(this.openImgMenu) {
                this.pictureMenu = new ImageFrame(this);
                this.pictureMenu.setVisible(true);
                this.pictureMenu.updatePreview(this);
                this.openImgMenu = false;
            }
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            ArrayVisualizer.setVisual(visuals.VisualStyles.BARS);
            return;
        }
        
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            int width = (int) (Renderer.getXScale() * (i + 1) - Renderer.getOffset());
            
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
            
            if(Highlights.fancyFinishActive()) {
                if(i < Highlights.getFancyFinishPosition()) {
                    this.extraRender.setColor(new Color(0, 1, 0, .5f));
                    this.extraRender.fillRect(Renderer.getOffset() + 20, 0, width, ArrayVisualizer.windowHeight());
                }
            }
            else CustomImage.colorCustomBars(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, ArrayVisualizer, this.extraRender, Renderer, width, ArrayVisualizer.analysisEnabled());
            
            Renderer.setOffset(Renderer.getOffset() + width);
        }
    }
}