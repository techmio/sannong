package com.stats.tools.gif;

import android.graphics.Bitmap;

public class GifFrame {
	/**ͼƬ*/
	public Bitmap image;
	/**��ʱ*/
	public int delay;
	/**��һ֡*/
	public GifFrame nextFrame = null;
	
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}
	
}
