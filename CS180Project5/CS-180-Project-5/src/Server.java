import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
/**
 * Server
 * <p>
 * Creates the server
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 12/12/22
 */
public class Server implements Runnable {
    private final static String CLIENT_DONE = "Client done!";
    private static final Object gatekeeper = new Object();
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Server(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);
        System.out.printf("socket open, waiting for connections on %s\n", serverSocket);
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
            System.out.println("Started thread!");
        }
    }

    public void remoteMessageDisplay(String message, String title, int messageType) {
        try {
            oos.writeObject(RemoteDisplayOption.SHOW_MESSAGE);
            oos.writeObject(message);
            oos.writeObject(title);
            oos.writeObject(messageType);
            oos.flush();

            if (!((String) ois.readObject()).equals(CLIENT_DONE)) {
                System.err.println("Expected client done message");
            }
        } catch (Exception ignored) {
            int q = 0;
        }
    }

    public String remoteInputDisplay(String message, String title, int messageType) {
        try {
            oos.writeObject(RemoteDisplayOption.SHOW_INPUT);
            oos.writeObject(message);
            oos.writeObject(title);
            oos.writeObject(messageType);
            oos.flush();

            String result = (String) ois.readObject();
            if (!((String) ois.readObject()).equals(CLIENT_DONE)) {
                System.err.println("Expected client done message");
            }
            return result;
        } catch (Exception ignored) {
            int q = 0;
        }
        return null;
    }

    public String remoteInputDisplayArray(String message, String title, int messageType, String[] options) {
        try {
            oos.writeObject(RemoteDisplayOption.SHOW_INPUT_ARRAY);
            oos.writeObject(message);
            oos.writeObject(title);
            oos.writeObject(messageType);
            oos.writeObject(options);
            oos.flush();

            String result = (String) ois.readObject();
            if (!((String) ois.readObject()).equals(CLIENT_DONE)) {
                System.err.println("Expected client done message");
            }
            return result;
        } catch (Exception ignored) {
            int q = 0;
        }
        return null;
    }

    public int remoteOptionDisplay(String message, String title, int messageType, String[] options) {
        try {
            oos.writeObject(RemoteDisplayOption.SHOW_OPTION);
            oos.writeObject(message);
            oos.writeObject(title);
            oos.writeObject(messageType);
            oos.writeObject(options);
            oos.flush();

            int result = (Integer) ois.readObject();
            if (!((String) ois.readObject()).equals(CLIENT_DONE)) {
                System.err.println("Expected client done message");
            }
            return result;
        } catch (Exception ignored) {
            int q = 0;
        }
        return -1;
    }

    public int remoteConfirmDisplay(String message, String title, int messageType) {
        try {
            oos.writeObject(RemoteDisplayOption.SHOW_CONFIRM);
            oos.writeObject(message);
            oos.writeObject(title);
            oos.writeObject(messageType);
            oos.flush();

            int result = (Integer) ois.readObject();
            if (!((String) ois.readObject()).equals(CLIENT_DONE)) {
                System.err.println("Expected client done message");
            }
            return result;
        } catch (Exception ignored) {
            int q = 0;
        }
        return -1;
    }

    public void sendQuit() {
        try {
            oos.writeObject(RemoteDisplayOption.QUIT);
            oos.flush();
        } catch (Exception ignored) {
            int q = 0;
        }

    }

    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.flush();
            Login lg = new Login(this);

            Account account;
            account = lg.logInPrompt();

            do {
                try {
                    if (account instanceof Seller) {
                        lg.sellerPrompt((Seller) account, gatekeeper);
                    } else if (account instanceof Customer) {
                        lg.customerPrompt((Customer) account, gatekeeper);
                    } else if (account == null) {
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Encountered unexpected exception! Dumping stack trace");
                    e.printStackTrace();
                }
            } while (!lg.isLogout());

            synchronized (gatekeeper) {
                lg.exportAccounts();
            }

            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
