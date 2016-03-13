package com.example.autoregister;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.util.Log;

import java.lang.Runnable;
import java.lang.Thread;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity implements Runnable {
	private int n;
	private TextView tv;
	private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        handler = new Handler();
        n = 0;
        Thread thread = new Thread(this);
        thread.start();
    }

	@Override                    
	public void run() {       
		tv = (TextView) findViewById(R.id.tv);    
		while (true) {
	        try {
	        	// Asynchronously update the GUI on a thread separate from the
	        	// one updating the value.
	        	n++;
	        	handler.post(new Runnable(){
	        		@Override
	        		public void run(){
	        			tv.setText(n + "");
	        			post();
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

	private void post(){
		try {
	        URL url = new URL("http://192.168.1.110:8080/test_post");
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setRequestMethod("POST");
	        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        httpCon.setRequestProperty("charset", "utf-8");
	        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
	        String urlParameters  = "param1=a&param2=b";
	        out.write(urlParameters);
	        out.close();
	        Log.v("TAG", handleStream(httpCon.getInputStream()));
	    }
	    catch (Exception e){
	    	Log.e("TAG", e.getMessage());
	    	e.printStackTrace();
	    }
	}
}
