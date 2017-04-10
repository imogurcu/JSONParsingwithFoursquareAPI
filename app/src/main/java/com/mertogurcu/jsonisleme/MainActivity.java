package com.mertogurcu.jsonisleme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RunnableFuture;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements LocationListener  {

    TextView tvlocation;
    ListView listView;
    LocationManager locationManager;
    String koordinantenlem = "40.99",koordinantboylam = "39.77";
    Double yeniBakilanEnlem, yeniBakilanBoylam;;


 //   String url = "https://api.foursquare.com/v2/venues/search?v=20170316&ll="+ koordinantenlem + "%2C%20"+ koordinantboylam+"&intent=checkin&client_id=GK5CUS554AAVLWYR4D1YDXE3EJRY50ZQ5EMIKR1PAAPNI12L&client_secret=JVWRRTQGQ52525YYFEPPKS5A1FO0CSRRP05GCP35T342U0GN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lvname);
        tvlocation = (TextView) findViewById(R.id.tv2);




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mekanAdi = (String) ((TextView) view.findViewById(R.id.tvname)).getText();
                String bakilanEnlem = (String) ((TextView) view.findViewById(R.id.textView2)).getText();
                String bakilanBoylam = (String) ((TextView) view.findViewById(R.id.tvboylam)).getText();

                yeniBakilanEnlem = Double.parseDouble(bakilanEnlem);
                yeniBakilanBoylam = Double.parseDouble(bakilanBoylam);



                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("bakilanEnlem",yeniBakilanEnlem);
                intent.putExtra("bakilanBoylam",yeniBakilanBoylam);
                intent.putExtra("mekan",mekanAdi);
                intent.putExtra("koordinatEnlem",koordinantenlem);
                intent.putExtra("koordinatBoylam",koordinantenlem);

                startActivity(intent);
            }


        });




    }

    @Override
    public void onLocationChanged(Location location) {
        double enlem = location.getLatitude();
        double boylam = location.getLongitude();

        koordinantenlem = enlem + "";
        koordinantboylam = boylam + "";

        tvlocation.setText("Konumunuz Enlem: "+ enlem + " - Boylam: " + boylam);

        new JSONTask().execute("https://api.foursquare.com/v2/venues/search?v=20170316&ll="+ koordinantenlem + "%2C%20"+ koordinantboylam+"&intent=checkin&client_id=GK5CUS554AAVLWYR4D1YDXE3EJRY50ZQ5EMIKR1PAAPNI12L&client_secret=JVWRRTQGQ52525YYFEPPKS5A1FO0CSRRP05GCP35T342U0GN");
        Toast.makeText(getApplicationContext(), "Konumunuz Değişti ve Mekanlar Güncellendi!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i("Status Changed !",s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("Provider Enabled !",s);
    }

    @Override
    public void onProviderDisabled(String s) {
        if(s=="gps"){
            Toast.makeText(getApplicationContext(), "GPS is off", Toast.LENGTH_LONG).show();

            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        Log.i("lm_disabled",s);
    }



    public class JSONTask extends AsyncTask<String, String, List<KonumModel>> {


        @Override
        protected List<KonumModel> doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                // https://api.foursquare.com/v2/venues/search?v=20161016&ll=41.878114%2C%20-87.629798&query=coffee&intent=checkin&client_id=GK5CUS554AAVLWYR4D1YDXE3EJRY50ZQ5EMIKR1PAAPNI12L&client_secret=JVWRRTQGQ52525YYFEPPKS5A1FO0CSRRP05GCP35T342U0GN
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer =new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject parentArray =  parentObject.getJSONObject("response");
                JSONArray parentArray2 = parentArray.getJSONArray("venues");


                final List<KonumModel> konumModelList = new ArrayList<>();

                for (int i = 0; i <parentArray2.length(); i++) {

                    JSONObject finalObject  = parentArray2.getJSONObject(i);
                    final KonumModel konumModel = new KonumModel();
                    konumModel.setName(finalObject.getString("name"));

                    final JSONObject finalObject2 = finalObject.getJSONObject("location");


                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                konumModel.setEnlem(finalObject2.getDouble("lat"));
                                konumModel.setBoylam(finalObject2.getDouble("lng"));
                                konumModel.setUzaklik(  finalObject2.getString("distance"));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            konumModelList.add(konumModel);
                        }
                    });

                }


                return konumModelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection !=null) {
                    connection.disconnect();
                }
                try {
                    if (reader !=null) {

                        reader.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(List<KonumModel> s) {
            super.onPostExecute(s);

            final KonumAdapter adapter = new KonumAdapter(getApplicationContext(),R.layout.row,s);
            listView.setAdapter(adapter);




        }
    }



    public class KonumAdapter extends ArrayAdapter{

        private List<KonumModel> konumModelList;
        private int resource;
        private LayoutInflater inflater;

        public KonumAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<KonumModel> objects) {
            super(context, resource, objects);

            konumModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater ) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }

            TextView tv_name,tv_koordinatenlem,tv_koordinatboylam,tv_uzaklik;

            tv_name = (TextView) convertView.findViewById(R.id.tvname);
            tv_name.setText(konumModelList.get(position).getName());

            tv_koordinatenlem = (TextView) convertView.findViewById(R.id.textView2);
            tv_koordinatenlem.setText(""+konumModelList.get(position).getEnlem());


           tv_koordinatboylam = (TextView) convertView.findViewById(R.id.tvboylam);
           tv_koordinatboylam.setText(""+konumModelList.get(position).getBoylam());


            tv_uzaklik = (TextView) convertView.findViewById(R.id.tvuzaklik);
            tv_uzaklik.setText("Uzaklık : " +konumModelList.get(position).getUzaklik() + "m");



            return convertView;

        }



    }



}



