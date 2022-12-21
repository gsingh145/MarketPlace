import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * FileInput
 * <p>
 * methods to import and export files
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class ClientFileInput {

    public static ArrayList<Product> sellerImportFile(File file) {
        ArrayList<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] productArray = line.split(",");
                products.add(new Product(productArray[0], productArray[1], productArray[2],
                        Integer.parseInt(productArray[3]), Double.parseDouble(productArray[4])));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File format issue",
                    "Error", RemoteDisplayConstants.ERROR_MESSAGE, null);
        }
        return products;
    }

    public void sellerExportFile(File file, ArrayList<Product> array) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Product product : array) {
                bw.write(product.toString());
                bw.write("\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File does not exist",
                    "Error", RemoteDisplayConstants.ERROR_MESSAGE, null);
        }
    }
}
