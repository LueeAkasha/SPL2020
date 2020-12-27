package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import com.google.gson.Gson;
import bgu.spl.mics.application.services.*;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch countDownLatch = new CountDownLatch(4);
	private static Input input;

	public static void main(String[] args) {

		try {
			/*
			* initialize the resources
			*/
			init(args[0]);

			/*
			* Allocate the threads
			*/
			Thread Hansolo = new Thread(new HanSoloMicroservice());
			Thread Lando = new Thread(new LandoMicroservice(input.getLando()));
			Thread Leia = new Thread(new LeiaMicroservice(input.getAttacks()));
			Thread R2D2 = new Thread(new R2D2Microservice(input.getR2D2()));
			Thread C3PO = new Thread(new C3POMicroservice());


			/*
			* run the threads
			*/
			Hansolo.start();
			C3PO.start();
			Lando.start();
			R2D2.start();
			countDownLatch.await();
			Leia.start();


			/*
			* wait for threads to finish their work
			*/
			Hansolo.join();
			C3PO.join();
			R2D2.join();
			Lando.join();
			Leia.join();


			/*
			* write to json
			*/
			setOutputToJson(Diary.getInstance(), args[1]);
		}
		catch (Exception exception){
			exception.printStackTrace();
		}








	}

	/**
	 * @param filepath path of input json
	 * @throws IOException io_exception
	 */
	private static void init(String filepath) throws IOException {
		input = getInputFromJson(filepath);
		Ewoks ewoks = Ewoks.getInstance();
		for (int i = 1 ;i <= input.getEwoks() ; i++){
			ewoks.addEwok(new Ewok(i));
		}
	}

	/**
	 * @param filePath path of input json
	 * @return Input object to init the threads with
	 * @throws IOException io_exception
	 */
	private static Input getInputFromJson(String filePath) throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(filePath)) {
			return gson.fromJson(reader, Input.class);
		}
	}

	/**
	 * @param diary diary object to get info during the war.
	 * @param filepath path of the output json
	 * @throws IOException io_exception
	 */
	private static  void setOutputToJson(Diary diary, String filepath) throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fileWriter = new FileWriter(filepath);
		gson.toJson(diary, fileWriter);
		fileWriter.flush();
		fileWriter.close();
	}
}
