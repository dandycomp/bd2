/**
 * Created by Ivan on 2015-06-15.
 */

package optymalizacja;

import przeglady.JDBCConnector;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.lang.Math;

public class DataFulFillment {
    private JDBCConnector connector = new JDBCConnector();
    private Connection connection;

    public DataFulFillment() throws SQLException {
        try {
            connection = connector.getConnection();
            connection.setAutoCommit(false);
            System.out.println("JDBC driver version is " + connection.getMetaData().getDriverVersion());
        }
        catch(SQLException e)
        {
            System.out.println("SQLError: " + e.getSQLState());
        }
    }

    public void insertKierowcaField(ArrayList<String> list) throws SQLException
    {
        PreparedStatement insert = null;
        String insertString = "Insert INTO \"Kierowca\" Values(?,?,?,?,?)";

        Savepoint sp = connection.setSavepoint();
        try{

            insert = connection.prepareStatement(insertString);
            //insert.setInt(1, Integer.parseInt(list.get(0)));
            insert.setInt(1, Integer.parseInt(list.get(0)));
            insert.setString(2, list.get(1));
            insert.setString(3, list.get(2));
            insert.setString(4, list.get(3));
            insert.setInt(5, Integer.parseInt(list.get(4)));
            insert.executeUpdate();
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Transaction is being rollen back");
            connection.rollback(sp);
        }
        finally
        {
            connection.commit();
            insert.close();
        }
    }


    public void generatePozycjeGrafiku() throws SQLException {
        int ILOSC_PRACOWNIKOW = 10000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int id_pracownika = 9999; id_pracownika <= ILOSC_PRACOWNIKOW; id_pracownika++)
        {
            Random rn = new Random();
            int ilosc_terminow = Math.abs(rn.nextInt() % 50);
            Calendar c = Calendar.getInstance();
            String date = new String();//sdf.format(c.getInstance().getTime());
            for(int i =1; i < ilosc_terminow; i++){
                Random rn2 = new Random();
                String status = rn2.nextInt() % 2 == 0 ? "wolny" : "zajety";
                c.add(Calendar.DATE, 1);  // number of days to add
                date = sdf.format(c.getTime());  // dt is now the new date
                System.out.println("Date: " + date + " " + status);

                ArrayList<String> list = new ArrayList<String>();
                list.add(String.valueOf(id_pracownika));
                list.add(date);
                list.add(status);

                this.insertPozycjaGrafiku(list);
            }
        }
    }

    public void insertPozycjaGrafiku(ArrayList<String> list) throws SQLException
    {
        PreparedStatement insert = null;
        String insertString = "Insert INTO \"Pozycja_grafiku\" Values(?,?,?)";

        Savepoint sp = connection.setSavepoint();
        try{

            insert = connection.prepareStatement(insertString);
            insert.setInt(1, Integer.parseInt(list.get(0)));
            insert.setDate(2, java.sql.Date.valueOf(list.get(1)));
            insert.setString(3, list.get(2));
            insert.executeUpdate();
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Transaction is being rollen back");
            connection.rollback(sp);
        }
        finally
        {
            connection.commit();
            insert.close();
        }
    }
    public ArrayList<String> generateKierowca(String str_names, String str_surnames, Integer max_names, Integer max_surnames) throws FileNotFoundException {
        ArrayList<String> arrList = new ArrayList<String>();

        try {
            BufferedReader br_imiona = new BufferedReader(new InputStreamReader(new FileInputStream(str_names)));
            BufferedReader br_nazwiska = new BufferedReader(new InputStreamReader(new FileInputStream(str_surnames)));

            int counter = 0;
            String name_final = "xxx";
            String surname_final = "xxx";
            String strLine;
            Random r = new Random();
            int name_nr = Math.abs(r.nextInt()%max_names);
            int surname_nr = Math.abs(r.nextInt() % max_surnames);
            //System.out.println("Numbers " + name_nr + " " + surname_nr);

            while ( (strLine = br_imiona.readLine()) != null)   {
                counter++;
                if(counter == name_nr)
                {
                    name_final = strLine;
                    break;
                }
            }
            counter = 0;
            while ((strLine = br_nazwiska.readLine()) != null)   {
                counter++;
                if(counter == surname_nr)
                {
                    surname_final = strLine;
                    break;
                }
            }

            int id = (int)(Math.random()*1000);
            arrList.add("0");
            arrList.add(name_final);
            arrList.add(surname_final);
            arrList.add(new StringBuilder().append(name_final).append(".").append(surname_final).append("@trans.com").toString());
            arrList.add(String.valueOf(100000000 + Math.abs(r.nextInt() ) % 899999999));
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("\nPlik nie zostal znaleziony, error: " + fnf.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrList;
    }


    public static void main (String[] args) throws SQLException
    {
        DataFulFillment dff = new DataFulFillment();

        for( int i = 20001; i <= 20001; i++) {
            ArrayList<String> kierowca;// = new ArrayList<String>();
            try {
                kierowca = dff.generateKierowca("imiona.txt", "nazwiska.txt", 1200, 20000);
                kierowca.set(0, String.valueOf(i));
                System.out.println(kierowca.get(0) + " " +
                        kierowca.get(1) + " " + kierowca.get(2) + " " +
                        kierowca.get(3) + " " + kierowca.get(4) + " \n");
                dff.insertKierowcaField(kierowca);
            } catch (FileNotFoundException fnf) {
                System.out.println("File not found: " + fnf.getMessage());
            }
        }
        dff.generatePozycjeGrafiku();
    }
}