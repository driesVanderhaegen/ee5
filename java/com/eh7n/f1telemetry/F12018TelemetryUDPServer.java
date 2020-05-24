package com.eh7n.f1telemetry;

import com.eh7n.f1telemetry.Components.*;
import com.eh7n.f1telemetry.data.Packet;
import com.eh7n.f1telemetry.util.PacketDeserializer;
import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * The base class for the F1 2018 Telemetry app. Starts up a non-blocking I/O
 * UDP server to read packets from the F1 2018 video game and then hands those
 * packets off to a parallel thread for processing based on the lambda function
 * defined. Leverages a fluent API for initialization.
 *
 * Also exposes a main method for starting up a default server
 *
 * @author eh7n
 *
 */
public class F12018TelemetryUDPServer {

	private static final Logger log = LoggerFactory.getLogger(F12018TelemetryUDPServer.class);
	// het ip adress van deze computer
	private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
	// poort die gedefinieerd staat op F1
	private static final int DEFAULT_PORT = 20777;
	private static final int MAX_PACKET_SIZE = 1341;
	private static float value;
	private static StepMotor stepmotor;
	private static Fan fan;
	private static Database db;


	//private static GpioController gpio;
	//private static GpioPinDigitalOutput pin;

	private static final GpioController gpio1 = GpioFactory.getInstance();
	private static final GpioPinDigitalOutput pin = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.HIGH);

	private static final GpioPinPwmOutput pwm = gpio1.provisionPwmOutputPin(CommandArgumentParser.getPin(
			RaspiPin.class,    // pin provider class to obtain pin instance from
			RaspiPin.GPIO_01));


	private static final GpioPinDigitalOutput coil_A_2_pin = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.HIGH);//pink
	private static final GpioPinDigitalOutput coil_B_2_pin = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED", PinState.HIGH);//orange
	private static final GpioPinDigitalOutput coil_A_1_pin = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.HIGH);//blue
	private static final GpioPinDigitalOutput coil_B_1_pin = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.HIGH);//yellow


	private static final GpioPinDigitalInput myButton = gpio1.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);

	public static StepMotor getStepmotor() {
		return stepmotor;
	}
	public static Fan getFan() {
		return fan;
	}
	public static Database getDatabase() {
		return db;
	}

	public static GpioController getGpio1(){return gpio1;}
	public static GpioPinDigitalInput getMyButton() {
		return myButton;
	}

	public static GpioPinPwmOutput getPwm() {
		return pwm;
	}


	public static GpioPinDigitalOutput getCoil_A_2_pin() {
		return coil_A_2_pin;
	}

	public static GpioPinDigitalOutput getCoil_B_2_pin() {
		return coil_B_2_pin;
	}

	public static GpioPinDigitalOutput getCoil_A_1_pin() {
		return coil_A_1_pin;
	}

	public static GpioPinDigitalOutput getCoil_B_1_pin() {
		return coil_B_1_pin;
	}

	private static Indicator dashboard = new Indicator();
	private static carSymbol car = new carSymbol();
	private static Battery battery = new Battery();

	private static ProgressBarComponents FrontLeft = new ProgressBarComponents("FrontLeft tire damage");
	private static ProgressBarComponents FrontRight = new ProgressBarComponents("FrontRight tire damage");
	private static ProgressBarComponents RearLeft = new ProgressBarComponents("RearLeft tire damage");
	private static ProgressBarComponents RearRight = new ProgressBarComponents("RearRight tire damage");
	private static textMessage bestLapMessage = new textMessage("BestLap: Vettel Circuit 143.554");




	private String bindAddress;
	private int port;
	private Consumer<Packet> packetConsumer;

	private F12018TelemetryUDPServer() {
		bindAddress = DEFAULT_BIND_ADDRESS;
		port = DEFAULT_PORT;
	}

	public static Indicator getDashboard(){return dashboard;}
	public static carSymbol getCar(){return car;}
	public static Battery getBattery(){return battery;}

	public static Logger getLog() {
		return log;
	}

	public static ProgressBarComponents getFrontLeft() {
		return FrontLeft;
	}

	public static ProgressBarComponents getFrontRight() {
		return FrontRight;
	}

	public static ProgressBarComponents getRearLeft() {
		return RearLeft;
	}

	public static ProgressBarComponents getRearRight() {
		return RearRight;
	}
	public static textMessage getBestLapMessage() {
		return bestLapMessage;
	}

	public static void setvalue(float setted){
		value=setted;
	}
	/**
	 * Create an instance of the UDP server
	 *
	 * @return
	 */
	public static F12018TelemetryUDPServer create() {
		return new F12018TelemetryUDPServer();
	}

	/**
	 * Set the bind address
	 *
	 * @param bindAddress
	 * @return the server instance
	 */
	public F12018TelemetryUDPServer bindTo(String bindAddress) {
		this.bindAddress = bindAddress;
		return this;
	}

	/**
	 * Set the bind port
	 *
	 * @param port
	 * @return the server instance
	 */
	public F12018TelemetryUDPServer onPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Set the consumer via a lambda function
	 *
	 * @param consumer
	 * @return the server instance
	 */
	public F12018TelemetryUDPServer consumeWith(Consumer<Packet> consumer) {
		packetConsumer = consumer;
		return this;
	}

	/**
	 * Start the F1 2018 Telemetry UDP server
	 *
	 * @throws IOException           if the server fails to start
	 * @throws IllegalStateException if you do not define how the packets should be
	 *                               consumed
	 */
	public void start() throws IOException {

		if (packetConsumer == null) {
			throw new IllegalStateException("You must define how the packets will be consumed.");
		}

		log.info("F1 2018 - Telemetry UDP Server");

		// Create an executor to process the Packets in a separate thread
		// To be honest, this is probably an over-optimization due to the use of NIO,
		// but it was done to provide a simple way of providing back pressure on the
		// incoming UDP packet handling to allow for long-running processing of the
		// Packet object, if required.
		ExecutorService executor = Executors.newSingleThreadExecutor();

		try (DatagramChannel channel = DatagramChannel.open()) {
			channel.socket().bind(new InetSocketAddress(bindAddress, port));
			log.info("Listening on " + bindAddress + ":" + port + "...");
			ByteBuffer buf = ByteBuffer.allocate(MAX_PACKET_SIZE);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			while (true) {
				channel.receive(buf);
				final Packet packet = PacketDeserializer.read(buf.array());
				executor.submit(() -> {
					packetConsumer.accept(packet);
				});
				buf.clear();
				if(value>0){
					pin.low();
				}else{
					pin.high();
				}
			}
		} finally {
			executor.shutdown();
		}
	}

	private static void createGUI(){
		JFrame f = new JFrame("GUI");
		Container p = f.getContentPane();
		GridBagConstraints c = new GridBagConstraints();


		FrontLeft.setOpaque(true); //content panes must be opaque
		FrontLeft.setBackground(Color.BLACK);

		FrontRight.setOpaque(true); //content panes must be opaque
		FrontRight.setBackground(Color.BLACK);
		RearLeft.setOpaque(true); //content panes must be opaque
		RearLeft.setBackground(Color.BLACK);
		RearRight.setOpaque(true);
		RearRight.setBackground(Color.BLACK);
		bestLapMessage.setOpaque(true); //content panes must be opaque
		bestLapMessage.setBackground(Color.BLACK);



		//set the type
		dashboard.setType("line");
		dashboard.setUnit("M/S");
		dashboard.setValue("0");
		dashboard.setFrom(0);
		dashboard.setTo(350);
		dashboard.setMajor(50);
		dashboard.setMinor(10);

		dashboard.setForeground(Color.BLUE);
		dashboard.setBackground(Color.DARK_GRAY);
		//刻度盘上文字颜色
		dashboard.setTextColor(Color.WHITE);
		//刻度盘上当前数值的颜色
		dashboard.setValueColor(Color.WHITE);
		//刻度盘上指针的颜色
		// wordt niet gebruikt
		dashboard.setPointerColor(Color.BLUE);
		//刻度盘上长刻度的颜色
		dashboard.setMajorScaleColor(Color.WHITE);
		//刻度盘上短刻度的颜色
		dashboard.setMinorScaleColor(Color.GRAY);

		//解决控件较小问题
		dashboard.setPreferredSize(new Dimension(700, 100));

		car.setForeground(Color.BLUE);
		car.setBackground(Color.BLACK);
		car.setTireFLColor(new Color(0,153,0));
		car.setTireFRColor(new Color(0,153,0));
		car.setTireBLColor(new Color(0,153,0));
		car.setTireBRColor(new Color(0,153,0));
		car.setWingFLColor(new Color(0,204,0));
		car.setWingFLColor(new Color(0,204,0));
		car.setPreferredSize(new Dimension(200, 400));

		battery.setBackground(Color.BLACK);
		battery.setPreferredSize(new Dimension(200,200));
		battery.setfirst(new Color(0,204,0));
		battery.setsecond(new Color(0,204,0));
		battery.setthird(new Color(0,204,0));
		battery.setfourth(new Color(0,204,0));
		battery.setfift(new Color(0,204,0));

		JPanel panel = new JPanel();
		c.gridx = 600;
		c.gridy = 50;
		panel.setBackground(Color.BLACK);
		panel.add(dashboard);

		JPanel panel1 = new JPanel();
		c.gridx = 600;
		c.gridy = 50;
		panel1.setBackground(Color.BLACK);

		panel1.add(car,BorderLayout.PAGE_START);
		JPanel panel2 = new JPanel();
		panel2.add(FrontLeft);
		panel2.add(FrontRight);
		panel2.add(RearLeft);
		panel2.add(RearRight);
		panel2.setBackground(Color.BLACK);
		JPanel panel3 = new JPanel();
		panel3.add(battery);
		panel3.setBackground(Color.BLACK);
		JPanel panel4 = new JPanel();
		panel4.add(bestLapMessage);
		panel4.setBackground(Color.BLACK);

		//p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(panel,BorderLayout.NORTH);
		p.add(panel1,BorderLayout.CENTER);
		p.add(panel2,BorderLayout.SOUTH);
		p.add(panel3,BorderLayout.WEST);
		p.add(panel4,BorderLayout.EAST);


		//p.add(panel1,c);

		//p.setLayout(new FlowLayout(FlowLayout.RIGHT));

		p.setBackground(Color.BLACK);

		//f.setSize(800, 200);
		f.pack();
		f.setVisible(true);
	}
	/**
	 * Main class in case you want to run the server independently. Uses defaults
	 * for bind address and port, and just logs the incoming packets as a JSON
	 * object to the location defined in the logback config
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		//gpio = GpioFactory.getInstance();
		//pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.HIGH);
		pin.setShutdownOptions(true, PinState.LOW);
		coil_A_2_pin.setShutdownOptions(true, PinState.LOW);
		coil_B_2_pin.setShutdownOptions(true, PinState.LOW);
		coil_A_1_pin.setShutdownOptions(true, PinState.LOW);
		coil_B_1_pin.setShutdownOptions(true, PinState.LOW);
		createGUI();
		stepmotor = new StepMotor();
		fan = new Fan();
		db = new Database();

		F12018TelemetryUDPServer.create()
				.bindTo("0.0.0.0")
				.onPort(20777)
				.consumeWith((h) -> {
					h.toJSON();
					//System.out.println();
					h.display();
				})
				.start();
	}
}
