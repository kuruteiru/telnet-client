package utb.fai;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TelnetClient {

	private final String serverIp;
	private final int port;

	public TelnetClient(String serverIp, int port) {
		this.serverIp = serverIp;
		this.port = port;
	}

	public void run() {
		try (Socket socket = new Socket(serverIp, port)) {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

			BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

			Thread readerThread = new Thread(() -> {
				try {
					while (true) {
						if (in.available() > 0) {
							String line = socketReader.readLine();
							if (line == null)
								break;
							System.out.println(line);
						} else {
							Thread.sleep(20);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			Thread writerThread = new Thread(() -> {
				try {
					String buffer;
					while ((buffer = stdinReader.readLine()) != null) {
						if (buffer.equals("/QUIT")) {
							socket.close();
							break;
						}
						socketWriter.write(buffer + "\r\n");
						socketWriter.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			readerThread.start();
			writerThread.start();

			readerThread.join();
			writerThread.join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
