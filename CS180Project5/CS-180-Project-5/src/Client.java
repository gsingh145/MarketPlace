import javax.swing.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client
 * <p>
 * Creates the client
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 12/12/22
 */

public class Client {
    private final static String CLIENT_DONE = "Client done!";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            RemoteDisplayOption display;

            // Remote I/O:
            // Format is RemoteGUIDisplayOption -> data
            while (true) {
                display = (RemoteDisplayOption) ois.readObject();
                switch (display) {
                    case SHOW_MESSAGE -> {
                        String message = (String) ois.readObject();
                        String title = (String) ois.readObject();
                        int messageType = (int) ois.readObject();

                        JOptionPane.showMessageDialog(null, message, title, messageType, null);

                    }
                    case SHOW_INPUT -> {
                        String message = (String) ois.readObject();
                        String title = (String) ois.readObject();
                        int messageType = (int) ois.readObject();

                        String output = JOptionPane.showInputDialog(null, message, title, messageType);
                        oos.writeObject(output);
                    }
                    // this appears to do the same thing as SHOW_INPUT
                    case SHOW_INPUT_ARRAY -> {
                        String message = (String) ois.readObject();
                        String title = (String) ois.readObject();
                        int messageType = (int) ois.readObject();
                        String[] options = (String[]) ois.readObject();

                        String result = (String) JOptionPane.showInputDialog(null, message, title,
                                messageType, null, options, options[0]);
                        oos.writeObject(result);
                    }
                    case SHOW_OPTION -> {
                        Object message = ois.readObject();
                        String title = (String) ois.readObject();
                        int messageType = (int) ois.readObject();
                        Object[] options = (Object[]) ois.readObject();
                        Object defaultOption = options[0];

                        Integer option = JOptionPane.showOptionDialog(null, message, title,
                                JOptionPane.DEFAULT_OPTION, messageType, null, options, defaultOption);

                        oos.writeObject(option);
                    }
                    case SHOW_CONFIRM -> {
                        Object message = ois.readObject();
                        String title = (String) ois.readObject();
                        int messageType = (int) ois.readObject();

                        Integer confirmOutput = JOptionPane.showConfirmDialog(null, message,
                                title, JOptionPane.YES_NO_OPTION, messageType);
                        oos.writeObject(confirmOutput);
                    }

                    case QUIT -> {
                        return;
                    }
                }
                oos.writeObject(CLIENT_DONE);
                oos.flush();
            }
        } catch (ConnectException e) {

            JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(),
                    "ConnectException",
                    RemoteDisplayConstants.ERROR_MESSAGE, null);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Encountered exception: " + e.getMessage(),
                    "Error",
                    RemoteDisplayConstants.ERROR_MESSAGE, null);
            e.printStackTrace();
        }
    }
}
