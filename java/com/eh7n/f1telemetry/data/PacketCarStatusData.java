package com.eh7n.f1telemetry.data;

import java.util.List;
import javax.swing.*;
import java.awt.*;

import com.eh7n.f1telemetry.Components.ProgressBarComponents;
import com.eh7n.f1telemetry.F12018TelemetryUDPServer;
import com.eh7n.f1telemetry.data.elements.CarStatusData;

public class PacketCarStatusData extends Packet {

	private List<CarStatusData> carStatuses;

	public PacketCarStatusData() {}

	public List<CarStatusData> getCarStatuses() {
		return carStatuses;
	}

	public void setCarStatuses(List<CarStatusData> carStatuses) {
		this.carStatuses = carStatuses;
	}

	public String toJSON() {


		String json = "\nFuelInTank = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelInTank()
				+ "\nFuelCapacity = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelCapacity()
				+ "\nTireDamage(fl) = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getFrontLeft()
				+ "\nTireDamage(fr) = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getFrontRight()
				+ "\nTireDamage(rl) = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getRearLeft()
				+ "\nTireDamage(rr) = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getRearRight()
				+ "\nEngineDamage = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getEngineDamage()
				+ "\nGearBoxDamage = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getGearBoxDamage()
				+ "\nRightWingDamage = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontRightWingDamage()
				+ "\nLeftWingDamage = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontLeftWheelDamage()
				+ "\nExhaustDamage = " + getCarStatuses().get(getHeader().getPlayerCarIndex()).getExhaustDamage();

		return json;
	}

	@Override
	public void display() {
		int enginedamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getEngineDamage();
		int maxRPM = getCarStatuses().get(getHeader().getPlayerCarIndex()).getMaxRpm();
		int frwd = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontRightWingDamage();
		int brwd = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontLeftWheelDamage();
		int twfl = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresWear().getFrontLeft();
		int twfr = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresWear().getFrontRight();
		int twbl = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresWear().getRearLeft();
		int twbr = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresWear().getRearRight();
		System.out.println("sensoren: engineDamage" +enginedamage);
		System.out.println("sensoren: maxRPM" +maxRPM);
		System.out.println("sensoren: frontRightWingDamage" +frwd);
		System.out.println("sensoren: frontLeftWheelDamage" +brwd);
		System.out.println("sensoren: tireWearFL" +twfl);
		System.out.println("sensoren: tireWearFR" +twfr);
		System.out.println("sensoren: tireWearBL" +twbl);
		System.out.println("sensoren: tireWearBR" +twbr);
		F12018TelemetryUDPServer.getDatabase().setCarStatus(enginedamage,maxRPM,frwd,brwd,twfl,twfr,twbl,twbr);
//		float a=getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelInTank();
//		float b=getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelCapacity();
//		float c=a*100/b;
//		int d= (int)c;
		int FLDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getFrontLeft();
		int FRDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getFrontRight();
		int BLDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getRearLeft();
		int BRDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getTiresDamage().getRearRight();

		int FLWDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontLeftWheelDamage();
		int FRWDamage = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFrontRightWingDamage();

		float fuelCapacity = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelCapacity();
		float fuel = getCarStatuses().get(getHeader().getPlayerCarIndex()).getFuelInTank();

		F12018TelemetryUDPServer.getFrontLeft().getProgressBar().setValue(FLDamage);

		F12018TelemetryUDPServer.getFrontRight().getProgressBar().setValue(FRDamage);

		F12018TelemetryUDPServer.getRearLeft().getProgressBar().setValue(BLDamage);

		F12018TelemetryUDPServer.getRearRight().getProgressBar().setValue(BRDamage);

		int Z = map(75,50,100,0,150);
		//F12018TelemetryUDPServer.getCar().setBackground(new Color(255,Z,0));
		if (FRDamage>20){F12018TelemetryUDPServer.getCar().setTireFRColor(new Color(255,map(FRDamage,50,100,0,150),0));}
		if (FLDamage>20){F12018TelemetryUDPServer.getCar().setTireFLColor(new Color(255,map(FLDamage,50,100,0,150),0));}
		if (BLDamage>20){F12018TelemetryUDPServer.getCar().setTireBLColor(new Color(255,map(BLDamage,50,100,0,150),0));}
		if (BRDamage>50){F12018TelemetryUDPServer.getCar().setTireBRColor(new Color(255,map(BRDamage,50,100,0,150),0));}

		if (FLWDamage>20){F12018TelemetryUDPServer.getCar().setWingFLColor(new Color(255,map(FLWDamage,20,100,0,185),0));}
		if (FRWDamage>20){F12018TelemetryUDPServer.getCar().setWingFRColor(new Color(255,map(FRWDamage,20,100,0,185),0));}
		//F12018TelemetryUDPServer.getCar().setTireBLColor(new Color(r, g, b));

		F12018TelemetryUDPServer.getCar().repaint();
	//	System.out.println(FLDamage);
		//System.out.println("damageL"+ FLWDamage);
	//	System.out.println("damageR" +FRWDamage);
		int battery =Math.round(fuelCapacity/fuel)*100;
		if(battery<=80)
		{
			F12018TelemetryUDPServer.getBattery().setfift(Color.DARK_GRAY);
			if(battery<=60)
			{
				F12018TelemetryUDPServer.getBattery().setfourth(Color.DARK_GRAY);
				if(battery<=40)
				{
					F12018TelemetryUDPServer.getBattery().setthird(Color.DARK_GRAY);
					if(battery<=20)
					{
						F12018TelemetryUDPServer.getBattery().setsecond(Color.DARK_GRAY);

					}
				}
			}

		}
		F12018TelemetryUDPServer.getBattery().repaint();


	}
	int map(int x, int in_min, int in_max, int out_min, int out_max)
	{
		return out_max - ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
	}
}

