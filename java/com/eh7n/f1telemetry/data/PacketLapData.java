package com.eh7n.f1telemetry.data;

import java.util.List;

import com.eh7n.f1telemetry.F12018TelemetryUDPServer;
import com.eh7n.f1telemetry.data.elements.LapData;

public class PacketLapData extends Packet {

	private List<LapData> lapDataList;

	public PacketLapData() {}

	public List<LapData> getLapDataList() {
		return lapDataList;
	}

	public void setLapDataList(List<LapData> lapDataList) {
		this.lapDataList = lapDataList;
	}

	public String toJSON() {

		String json = "\nLastLapTime = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getLastLapTime()
				+ "\nCurrentLapTime = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getCurrentLapTime()
				+ "\nBestLapTime = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getBestLaptTime()
				+ "\nLapDistance = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getLapDistance()
				+ "\nTotalDistance = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getTotalDistance()
				+ "\nCarPosition = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getCarPosition()
				+ "\nCurrentLapNum = " + getLapDataList().get(getHeader().getPlayerCarIndex()).getCurrentLapNum()
				+ "\nCurrentInvalid = " + getLapDataList().get(getHeader().getPlayerCarIndex()).isCurrentLapInvalid();

		return json;
	}
	public void display(){
		float blt = getLapDataList().get(getHeader().getPlayerCarIndex()).getBestLaptTime();
		double bestlaptime = blt;
		int carpos = getLapDataList().get(getHeader().getPlayerCarIndex()).getCarPosition();
		int penal = getLapDataList().get(getHeader().getPlayerCarIndex()).getPenalties();
		int totaldist =  Math.abs(Math.round(getLapDataList().get(getHeader().getPlayerCarIndex()).getTotalDistance()));
		String result = ""+ getLapDataList().get(getHeader().getPlayerCarIndex()).getResultStatus();

		System.out.println("driverstats: bestLapTime" + bestlaptime);
		System.out.println("driverstats: carPosition" + carpos);
		System.out.println("driverstats: penalties" + penal);
		System.out.println("driverstats: totalDistance" +totaldist);
		System.out.println("driverstats: currentLap" + getLapDataList().get(getHeader().getPlayerCarIndex()).getCurrentLapNum());
		System.out.println("driverstats: result" + result);
		F12018TelemetryUDPServer.getDatabase().setLapData(bestlaptime,carpos,penal, totaldist, result);
		F12018TelemetryUDPServer.getBestLapMessage().gettext().setText("BestLapTime: " + Math.round(bestlaptime));
	}


}
