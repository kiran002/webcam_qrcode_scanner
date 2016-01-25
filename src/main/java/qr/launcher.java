package qr;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class launcher {

	public static void main(String[] args) {

		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		try {
			ImageIO.write(webcam.getImage(), "PNG", new File("f:\\hello-world.png"));
			String filePath = "f:\\hello-world.png";
			String charset = "UTF-8"; // or "ISO-8859-1"
			Map hintMap = new HashMap();

			BufferedImage image = webcam.getImage();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());

			BinaryBitmap binaryBitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(is))));
					// Result result = new
					// MultiFormatReader().decode(binaryBitmap,
					// hints)

			// ImageIO.write(image, "PNG", new File("f:\\hello-world.png"));

			BitMatrix bm = binaryBitmap.getBlackMatrix();

			MultiDetector detector = new MultiDetector(bm);
			DetectorResult dResult = detector.detect();

			if (dResult != null) {
				// found it :D
				System.out.println("found qr code");
				BitMatrix QRImageData = dResult.getBits();
				System.out.println(QRImageData.toString());
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				System.out.println("Data read from QR Code: " + readQRCode(filePath, charset, hintMap, QRImageData));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to read Qr code: Please dont shake your mobile!");
			// e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to read Qr code: Please dont shake your mobile!");
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to read Qr code: Please dont shake your mobile!");
		}
	}

	public static String readQRCode(String filePath, String charset, Map hintMap, BitMatrix qRImageData)
			throws FileNotFoundException, IOException, NotFoundException, FormatException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		Decoder dr = new Decoder();
		DecoderResult qrCodeResult = null;
		try {
			qrCodeResult = dr.decode(qRImageData);
		} catch (ChecksumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qrCodeResult.getText();
	}

}
