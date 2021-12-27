package domain.models.entities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.IOException;
import java.nio.file.Paths;


public class GeneradorQR {


    public static void generarQR(String data, String path, String format,
                                 int height, int width)
            throws WriterException, IOException {

        BitMatrix matrix = new MultiFormatWriter()
                .encode(data, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToPath(matrix, format, Paths.get(path));

    }

}
