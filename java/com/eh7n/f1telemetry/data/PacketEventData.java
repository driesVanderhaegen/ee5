package com.eh7n.f1telemetry.data;

import com.eh7n.f1telemetry.F12018TelemetryUDPServer;

import java.awt.*;

public class PacketEventData extends Packet {

	private String eventCode;

	public PacketEventData() {}
	
	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public void display(){
		String event = getEventCode();
		System.out.println("eventCode" + getEventCode());
		if(event.equals("SEND"))
		{
			System.out.println("eventCode" + getEventCode() + "JJJJJAAAAAAAAAAA");
			F12018TelemetryUDPServer.getDatabase().setWrite(true);
		}
	}

}
