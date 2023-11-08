package com.professionalandroid.apps.airport;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AirportViewModel extends AndroidViewModel {

    private static final String TAG = "AirportUpdate";
    private LiveData<List<Airport>> airports;
    private String key = "xk5yi2SpRGjDAOnbnwSu2axdygFyIf8qFb0lotCTXD7SZ8OO7xGR48r0LVDj/6b2I4D5OE8QndU1Sqi9V6rx3w==";
    private String row = "1000";
    private String page = "10";

    public AirportViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Airport>> getAirports() {
        if (airports == null) {
            airports = AirportDatabaseAccessor.getInstance(getApplication()).airportDAO().loadAllAirports();
        loadAirports();
        }
        return airports;
    }

    public void loadAirports(){
        new AsyncTask<Void, Void, List<Airport>>(){

            @Override
            protected List<Airport> doInBackground(Void... voids) {
                ArrayList<Airport> airports = new ArrayList<>(0);

                URL url;
                try{
                    String urlAddress = "http://apis.data.go.kr/B551177/StatusOfPassengerWorldWeatherInfo/getPassengerArrivalsWorldWeather?type=json&serviceKey=" + key + "&numOfRows" + row + "&pageNo=1" + page;

                    url = new URL(urlAddress);

                    URLConnection connection;
                    connection = url.openConnection();

                    HttpURLConnection httpConnection = (HttpURLConnection)connection;
                    int responseCode = httpConnection.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream in = httpConnection.getInputStream();

                        airports = (ArrayList) parseJson(in);

                    }
                    httpConnection.disconnect();
                } catch (MalformedURLException e){
                    Log.e(TAG, "MalformedURLException", e);
                } catch (IOException e){
                    Log.e(TAG, "IOException", e);
                }

                AirportDatabaseAccessor
                        .getInstance(getApplication())
                        .airportDAO()
                        .insertAirports(airports);


                return airports;
            }

            @Override
            protected void onPostExecute(List<Airport> data) {
            }
        }.execute();
    }

    private List<Airport> parseJson(InputStream in) throws IOException{

        JsonReader reader =
                new JsonReader(new InputStreamReader(in, "UTF-8"));

        try{
            List<Airport> airports = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();

                if(name.equals("response")) {

                    airports = readBody(reader);

                }else{

                    reader.skipValue();
                }
            }
            reader.endObject();

            return airports;
        } finally {
            reader.close();
        }
    }

    public List<Airport> readBody(JsonReader reader) throws IOException{
        List<Airport> airports = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("body")) {
                airports = readItems(reader);
            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return airports;

    }

    public List<Airport> readItems(JsonReader reader) throws IOException{
        List<Airport> airports = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("items")) {
                airports = readAirportArray(reader);
            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return airports;

    }


    private List<Airport> readAirportArray(JsonReader reader) throws IOException{
        List<Airport> airports = new ArrayList<Airport>();

        reader.beginArray();
        while (reader.hasNext()){
            airports.add(readAirport(reader));
        }
        reader.endArray();

        return airports;
    }

    public Airport readAirport(JsonReader reader) throws IOException{
        String airline = null;
        String airport = null;
        String flightId = null;
        String estimatedDateTime = null;
        String terminalid = null;
        String yoil = null;
        double wind = 0;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("airline")) {
                airline = reader.nextString();
                Log.i("airline", airline);
            } else if(name.equals("airport")) {
                airport = reader.nextString();
                Log.i("airport", airport);
            } else if(name.equals("flightId")){
                flightId = reader.nextString();
                Log.i("flightId", ""+flightId);
            }else if(name.equals("estimatedDateTime")){
                estimatedDateTime = reader.nextString();
                Log.i("flightId", ""+estimatedDateTime);
            }else if(name.equals("terminalid")){
                terminalid = reader.nextString();
                Log.i("flightId", ""+terminalid);
            }else if(name.equals("yoil")){
                yoil = reader.nextString();
                Log.i("yoil", ""+yoil);
            }else if(name.equals("wind")){
                wind = reader.nextDouble();
                Log.i("wind", ""+wind);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Airport(airline,airport,flightId,estimatedDateTime,terminalid,yoil,wind);

    }



    public List<Double> readDoublesArray(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList<Double>();
        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }
}