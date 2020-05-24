package com.eh7n.f1telemetry.Components;

        import com.eh7n.f1telemetry.F12018TelemetryUDPServer;

        import javax.xml.crypto.Data;
        import java.sql.*;

public class Database
{


    int IDs = 0;
    int IDr = 0;
    int IDds = 0;
    int IDd = 0;
    int EngineDamage = 0;
    int MRPM = 0;
    int FRWIDA = 0;
    int BWIDA = 0;
    int TIREWearFL = 0;
    int TIREWearFR = 0;
    int TIREWearBL = 0;
    int TIREWearBR = 0;


    String NAME = "Unknown";
    String team = "unknown";
    String nationality = "unknown";

    String CIRCUIT = "unknown";
    String SESSION = "unknown";
    int totalLaps = 0;

    double bestlapt = 0.0;
    int endpos = 0;
    int penal = 0;
    int totaldist = 0;
    int finiched = 0;
    int RID = 0;
    int SID = 0;
    int DID = 0;
    String bestDriver;
    double bestTime;

    public boolean write= false;
    public boolean writer= true;
    public boolean writed= true;
    public int write1 = 0;
    public String currentCircuit;
    public Database()
    {

    }

    public void setParticipants(String naam, String Team, String national) {
        NAME = naam;
        team = Team;
        nationality = national;
    }
    public void setSessionData(int laps, String sessionT, String circuit) {
        totalLaps = laps;
        SESSION = sessionT;
        CIRCUIT = circuit;
    }
    public void setLapData(double bestlapT, int carpos, int penalties, int totalDist, String result) {
        bestlapt = bestlapT;
        endpos = carpos;
        penal = penalties;
        totaldist = totalDist;
        if(result.equals("finished")){ finiched = 1;}
    }
    public void setCarStatus(int enginedamage, int maxRpm, int frwd, int flwd, int FTl, int FTR, int BTL, int BTR) {
        EngineDamage = enginedamage;
        MRPM = maxRpm;
        FRWIDA = frwd;
        BWIDA = flwd;
        TIREWearFL = FTl;
        TIREWearFR = FTR;
        TIREWearBL = BTL;
        TIREWearBR = BTR;
    }
    public void setWrite(boolean event){
        // SSTA of SEND
        if(event)
        {
            IDs = getIDds() +1 ;
            IDd = getIDd() ;
            IDds = getIDds() +1;
            IDr = getIDr();
            Connection c = null;
            Statement stmt =null;

            try {
                String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ee5.db";
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");





                stmt = c.createStatement();
                String sql = "INSERT INTO SENSOREN (CID, ENGINEDAMAGE ,MAXPRM,FRWINGDAMAGE,FLWHEELDAMAGE,TIREWEARFL,TIREWEARFR, TIREWEARBL, TIREWEARBR) " +
                        "VALUES (" + IDs + "," + EngineDamage + "," + MRPM + "," + FRWIDA + "," + BWIDA + "," + TIREWearFL + "," + TIREWearFR + "," + TIREWearBL + "," + TIREWearBR  +");";

                stmt.executeUpdate(sql);
                stmt.close();
                // werkt
                if(writed)
                {
                    IDd++;
                    stmt = c.createStatement();
                    String sq2 = "INSERT INTO DRIVERS (SID, NAME ,TEAM,NATIONALITY) " +
                            "VALUES (" + IDd + ", '" + NAME + "' , '" + team + "' , '" + nationality + "' " + ");";
                    stmt.executeUpdate(sq2);


                    stmt.close();
                }

                // werkt
                // /*
                if(writer)
                {
                    IDr++;
                    stmt = c.createStatement();

                    String sq3 = "INSERT INTO RACE (RID, CIRCUIT  ,SESSIONTYPE,TOTALLAPS) " +
                            "VALUES (" + IDr + ", '" + CIRCUIT + "' , '" + SESSION + "' , " + totalLaps +  ");";

                    stmt.executeUpdate(sq3);
                    stmt.close();
                }

                // deze werkt
                stmt = c.createStatement();
                SID = IDs;
                if (writer)
                {
                    RID = IDr;
                }
                if (writed)
                {
                    DID = IDd;
                }
                String sq4 = "INSERT INTO DRIVERSTAT (DSID, BESTLAPTIME ,ENDPOSITION,PENALTIES,TOTALDISTANCE,FINICHED,RID, SID, BID) " +
                        "VALUES (" + IDds + "," + bestlapt + "," + endpos + "," + penal + "," + totaldist + "," + finiched + "," + RID + "," + SID + "," + DID  +");";

                stmt.executeUpdate(sq4);


                stmt.close();
                c.commit();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }

            System.out.println("Records created successfully");
            getQuery();
            F12018TelemetryUDPServer.getBestLapMessage().gettext().setText("" + bestDriver + "has fastestLap: " + bestTime);


        }
    }
    public int getIDs(){
        Connection c = null;
        Statement stmt = null;
        try {
            String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ServerSQLite.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM SENSOREN;" );
            while ( rs.next() ) {
                 IDs = rs.getInt("CID");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("ReadIDs successfully");
        return IDs;

    }
    public int getIDr(){
        Connection c = null;
        Statement stmt = null;
        try {
            String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ServerSQLite.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM RACE;" );
            while ( rs.next() ) {
                 IDr = rs.getInt("RID");
                String  cir = rs.getString("CIRCUIT");
                String type = rs.getString("SESSIONTYPE");
                if(cir.equals(CIRCUIT) && type.equals(SESSION)){
                    writer = false;
                    RID = IDr;
                }
                currentCircuit = cir;

                System.out.println( "ID = " + IDr );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("ReadIDr successfully");
        return IDr;

    }
    public int getIDds(){
        Connection c = null;
        Statement stmt = null;
        try {
            String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ServerSQLite.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM DRIVERSTAT;" );
            while ( rs.next() ) {
                 IDds = rs.getInt("DSID");
                System.out.println( "ID = " + IDds );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("ReadIDds successfully");
        return IDds;

    }
    public int getIDd(){
        Connection c = null;
        Statement stmt = null;
        try {
            String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ServerSQLite.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM DRIVERS;" );
            while ( rs.next() ) {
                IDd = rs.getInt("SID");
                String  name = rs.getString("NAME");
                if(name.equals(NAME)){
                    writed = false;
                    DID = IDd;
                }

                System.out.println( "ID = " + IDd );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Read IDd successfully");
        return IDd;

    }
    public int getQuery(){
        Connection c = null;
        Statement stmt = null;
        try {
            String dbLocation = "jdbc:sqlite:" + System.getProperty("user.home") + "/ServerSQLite.db";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/pi/Desktop/sqliteTest/ee5.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "select DRIVERS.NAME, DRIVERSTAT.BESTLAPTIME FROM DRIVERS INNER JOIN DRIVERSTAT ON DRIVERS.SID = DRIVERSTAT.BID WHERE DRIVERSTAT.BESTLAPTIME = (SELECT MIN(BESTLAPTIME) FROM (SELECT BESTLAPTIME FROM (SELECT DRIVERSTAT.BESTLAPTIME FROM DRIVERSTAT INNER JOIN RACE ON DRIVERSTAT.RID = RACE.RID WHERE RACE.CIRCUIT = '" + currentCircuit +"' ) WHERE BESTLAPTIME IS NOT 0.0  AND BID IS NOT 0)); " );
            while ( rs.next() ) {
                //= "Melbourne")
                bestDriver = rs.getString("NAME");
                bestTime = rs.getDouble("BESTLAPTIME");


                //System.out.println( "ID = " + IDd );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Read query successfully");
        return IDd;

    }

}


