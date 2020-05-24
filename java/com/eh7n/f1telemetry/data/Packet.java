package com.eh7n.f1telemetry.data;

import com.eh7n.f1telemetry.data.elements.Header;


public abstract class Packet {

	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String toJSON() {

		String json = "";

		return json;
	}
	public void display(){
	}

}