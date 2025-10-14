package utb.fai;

public class App {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("bad number of parameters. Usage: <ip> <port>");
			return;
		}

		String ip = args[0];
		int port = 23;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("port must be an integer");
			return;
		}

		TelnetClient telnetClient = new TelnetClient(ip, port);
		telnetClient.run();
	}
}
