package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.*;
import bgu.spl.net.srv.Server;

public class TPCMain {

	public static void main(String[] args) {
		Database.getInstance().initialize("./courses.txt");
		Server.threadPerClient(Integer.parseInt(args[0]),
				MessagingProtocolImpl::new,
				MessageEncoderDecoderImpl::new).serve();

	}

}
