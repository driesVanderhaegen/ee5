package com.eh7n.f1telemetry.data;

import java.util.List;

import com.eh7n.f1telemetry.F12018TelemetryUDPServer;
import com.eh7n.f1telemetry.data.elements.ParticipantData;

public class PacketParticipantsData extends Packet {

	private int numCars;
	private List<ParticipantData> participants;

	public PacketParticipantsData() {
	}

	public String[] teams = {"Mercedes", "Ferrari", "RedBull", "Williams", "ForceIndia", "Renault", "Toro", "Haas", "MCLaren", "Sauber", "MClare88"
			, "MCLaren91", "Williams92", "Ferrari95", "Williams96", "MClaren98", "Ferari02", "Ferrari04", "Renault06", "Ferrari07", "MCLaren08"
			, "REDBull10", "Ferari76", "MClaren76", "Lotus72", "Ferrari79", "MCLaren82", "Williams03", "Brawn09", "Lotus78"};
	public String[] nationalities = {"AM", "AR", "AU", "AUs", "AZ", "BA", "BE", "BOL", "BR", "GB", "BUL", "CaM", "CA", "CH", "CHI", "COL", "CR", "CRO", "CY", "CZ"};
	public String[] bestuurders = {"SAINZ", "Ricciardo", "Alonso", "Raikkonen", "Hamilton", "Ericson", "Verstappen", "Hulkenburg", "Magnussen", "Grosjean", "Vettel", "Perez", "Bottas", "Ocon", "Vandoorne"};

	public int getNumCars() {
		return numCars;
	}

	public void setNumCars(int numCars) {
		this.numCars = numCars;
	}

	public List<ParticipantData> getParticipants() {
		return participants;
	}

	public void setParticipants(List<ParticipantData> participants) {
		this.participants = participants;
	}

	public void display() {

		int nID = getParticipants().get(getHeader().getPlayerCarIndex()).getDriverId();
		if(nID>=6){nID= nID-3;}
		else{nID = nID-1;}
		if(nID<0){nID= 0;}
		String naam = "user";
		if(nID<=15){
			naam = bestuurders[nID];
		}

		int tID = getParticipants().get(getHeader().getPlayerCarIndex()).getTeamId();
		String Team = teams[tID];
		int nat = getParticipants().get(getHeader().getPlayerCarIndex()).getNationality() - 1;
		String nationality = "unknown";
		if (nat <= 19) {
			nationality = nationalities[nat];
		}

		//String nationality = nationalities[nat];
		System.out.println("Bestuurder:driver" + getParticipants().get(getHeader().getPlayerCarIndex()).getDriverId());
		System.out.println("Bestuurder:naamdriver" + naam);
		System.out.println("Bestuurder:team " + Team);
		System.out.println("Bestuurder:nationality" + nationality);
		F12018TelemetryUDPServer.getDatabase().setParticipants(naam, Team, nationality);
	}
}
