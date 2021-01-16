package bgu.spl.net.impl.BGRSServer;


import bgu.spl.net.api.*;
import bgu.spl.net.srv.Server;

public class ReactorMain {

	public static void main(String[] args) {
		Database.getInstance().initialize("courses.txt");
		Server.reactor(Integer.parseInt(args[1]),
				Integer.parseInt(args[0]),
				MessagingProtocolImpl::new,
				MessageEncoderDecoderImpl::new).serve();

	}

}
