package com.eh7n.f1telemetry.data;
import java.util.List;
//import com.eh7n.f1telemetry.Components.Track;
import com.eh7n.f1telemetry.F12018TelemetryUDPServer;
import com.eh7n.f1telemetry.data.elements.ButtonStatus;
import com.eh7n.f1telemetry.data.elements.CarTelemetryData;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PacketCarTelemetryData extends Packet {
	private List<CarTelemetryData> carTelemetryData;
	private ButtonStatus buttonStatus;
	public PacketCarTelemetryData() {}
	public List<CarTelemetryData> getCarTelemetryData() {
		return carTelemetryData;
	}
	public void setCarTelemetryData(List<CarTelemetryData> carTelemetryData) {
		this.carTelemetryData = carTelemetryData;
	}
	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}
	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}
	public String toJSON() {

		try {
			F12018TelemetryUDPServer.getStepmotor().setSpeed(getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSpeed());
			int speed1= getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSpeed();

			F12018TelemetryUDPServer.getFan().setDuty(speed1);

		} catch (InterruptedException ex) {
			Logger.getLogger(PacketCarTelemetryData.class.getName()).log(Level.SEVERE, null, ex);
		}
		//Track.setSpeed(getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSpeed());
		String json = "\nSpeed = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSpeed()
				+ "\nThrottle = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getThrottle()
				+ "\nSteer = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSteer()
				+ "\nBrake = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getBrake()
				+ "\nClutch = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getClutch()
				+ "\nEngineRPM = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getEngineRpm()
				+ "\nRevLightsPercent = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getRevLightsPercent()
				+ "\nEngineTemperature = " + getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getEngineTemperature();
		return json;
	}
	public void display(){
		F12018TelemetryUDPServer.getDashboard().setValue("" +getCarTelemetryData().get(getHeader().getPlayerCarIndex()).getSpeed());
		F12018TelemetryUDPServer.getDashboard().repaint();
	}
}
