package com.salesforce.utilities;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * system clip board
 * 
 */
public class ClipBoardUtil {
	/**
	 * Get the String residing on the clip board.
	 * 
	 * @return any text found on the Clip board; if none found, return an empty
	 *         String.
	 */
	public static String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Get the String residing on the clip board.
	 * 
	 * @return any text found on the Clip board; if none found, return an empty
	 *         String.
	 */
	public static Image getClipboardImage() {
		Image result = null;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.imageFlavor);
		if (hasTransferableText) {
			try {
				result = (Image) contents.getTransferData(DataFlavor.imageFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * put data into system clip board
	 * 
	 * @param srcData
	 *            data to put into clip board
	 */
	public static void setClipboardContents(String srcData) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(srcData), null);

	}

}
