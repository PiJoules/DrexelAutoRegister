package com.example.autoregister;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.util.Log;
import android.view.View;

import java.lang.Runnable;
import java.lang.Thread;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity implements Runnable {
	/* This is hardocded for now and must change depending on where the server is running. */
	// private String ENDPOINT = "http://192.168.1.110:8080/test_post";
	private String ENDPOINT = "http://192.168.1.102:8080/register_user";

	private int n;  // This auto-incrementing variable is just for testing that the app will still run while asleep
	private TextView tv;
	private Handler handler;
	private Button submit;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private long timeTicket;
	private EditText drexelID, password, crns;
	private boolean sent = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /* Setup */
        drexelID = (EditText) findViewById(R.id.drexelID);
        password = (EditText) findViewById(R.id.password);
        crns = (EditText) findViewById(R.id.crns);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timeTicket = Long.MAX_VALUE;

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View view){
				Calendar calendar = new GregorianCalendar(
										datePicker.getYear(),
						                datePicker.getMonth(),
						                datePicker.getDayOfMonth(),
						                timePicker.getCurrentHour(),
						                timePicker.getCurrentMinute());
				timeTicket = calendar.getTimeInMillis();
				String datetime = String.format("Set new time ticket of %d%d%d at %d:%d",
										datePicker.getYear(),
						                datePicker.getMonth(),
						                datePicker.getDayOfMonth(),
						                timePicker.getCurrentHour(),
						                timePicker.getCurrentMinute());
				sent = false;
				((TextView) findViewById(R.id.status)).setText("");
				Toast.makeText(getApplicationContext(), datetime, Toast.LENGTH_SHORT).show();
        	}
        });

        handler = new Handler();
        n = 0;
        Thread thread = new Thread(this);
        thread.start();
    }

	@Override                    
	public void run() {       
		// tv = (TextView) findViewById(R.id.tv);
		while (true) {
	        try {
	        	// Asynchronously update the GUI on a thread separate from the
	        	// one updating the value.
	        	n++;
	        	handler.post(new Runnable(){
	        		@Override
	        		public void run(){
	        			// tv.setText(n + "");
	        			if (!sent && System.currentTimeMillis() > timeTicket){
	        				String id = drexelID.getText().toString();
	        				String psswd = password.getText().toString();
	        				JSONArray crnsJSON = new JSONArray();
	        				for (String crn : crns.getText().toString().split(",")){
	        					crnsJSON.put(crn);
	        				}

	        				post(id, psswd, crnsJSON.toString());
	        				sent = true;
	        			}
	        		}
	        	});
	            Thread.sleep(1000);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

    /**
     * Given an inputstream, create a buffer for the incoming data and read off
     * the buffer.
     * @param  Inputstream returned by the connection
     * @return The data received from the server as a string.
     * @throws IOException In case we are reading from a bad input stream.
     */
    private String handleStream(InputStream stream) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String buffer;
        StringBuffer builder = new StringBuffer();
        while ((buffer = reader.readLine()) != null){
            builder.append(buffer);
        }
        reader.close();
        return builder.toString();
    }

	private void post(String id, String password, String crnsJSON){
		try {
	        URL url = new URL(ENDPOINT);
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setRequestMethod("POST");
	        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        httpCon.setRequestProperty("charset", "utf-8");
	        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
	        String urlParameters = String.format("id=%s&password=%s&crns=%s", id, password, crnsJSON);
	        out.write(urlParameters);
	        out.close();

	        int respCode = httpCon.getResponseCode();
	        String resp = "";
	        if (respCode == 200){
	        	resp = new JSONObject(handleStream(httpCon.getInputStream())).getString("message");
	        }
	        else {
	        	resp = new JSONObject(handleStream(httpCon.getErrorStream())).getString("message");
	        }
	        Log.v("TAG", resp);
	        ((TextView) findViewById(R.id.status)).setText(resp);
	    }
	    catch (Exception e){
	    	Log.e("TAG", e.getMessage());
	    	e.printStackTrace();
	    }
	}
}
