package ro.pub.cs.systems.eim.practicaltest02version2;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ClientThread extends Thread {

    //Socket socket = null;

    //SendThread sendThread = null;
    //ReceiveThread receiveThread = null;

    int port;
    String address;
    TextView messageTextView;
    String city;
    String getWeatherInfo;

    //private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(20);

    public ClientThread (int port, String address, String city, String getWeatherInfo, TextView messageTextView) {
        this.port = port;
        this.address = address;
        this.city = city;
        this.getWeatherInfo = getWeatherInfo;
        this.messageTextView = messageTextView;
        /*try {
            String form = city + "\n" + getWeatherInfo;
            messageQueue.put(form);
        } catch (InterruptedException interruptedException) {
            Log.e(Constants.TAG, "An exception has occurred: " + interruptedException.getMessage());
            if (Constants.DEBUG) {
                interruptedException.printStackTrace();
            }
        }*/

    }

    /*
    public void connect() {
        try {
            Log.i("TAG", "what the hell");
            this.socket = new Socket(address, port);
            if (socket == null) {
                Log.e("TAG", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            Log.i("TAG", "Creating connection with server");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred while creating the socket: " + ioException.getMessage());
            Log.i("TAG", "FUCK YOY");
            if (Constants.DEBUG) {

                ioException.printStackTrace();
            }
        }
        if (socket != null) {
            startThreads();
        }
    }

    public void startThreads() {
        sendThread = new SendThread();
        sendThread.run();

        receiveThread = new ReceiveThread();
        receiveThread.run();
        Log.i("DEF", "GOGOGOGOGOGO");
    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            try {
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (printWriter != null) {

                    String content = messageQueue.take();
                    if (content != null) {
                        printWriter.println(content);
                        printWriter.flush();
                    }

                }
            }catch (InterruptedException interruptedException) {
                Log.e(Constants.TAG, "An exception has occurred: " + interruptedException.getMessage());
                if (Constants.DEBUG) {
                    interruptedException.printStackTrace();
                }
            }catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred while creating the socket: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }

        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);

                if (bufferedReader != null) {
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        messageTextView.setText(line);
                    }
                }

            }catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred while creating the socket: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                        if (Constants.DEBUG) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

        }
    }*/

    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(address, port);

            if (socket == null) {
                Log.e("TAG", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            if(printWriter == null || bufferedReader == null) {
                Log.e("TAG", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(city);
            printWriter.flush();
            printWriter.println(getWeatherInfo);
            printWriter.flush();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                final String finalContent = content;
                messageTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        messageTextView.setText(finalContent);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
