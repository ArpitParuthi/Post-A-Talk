package com.example.cometandroidapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Activity1 extends Activity  {
	Talk TALK_OBJECT;
	public final static String OBJECT = "com.example.cometandroidapp.object1";
	ImageView iv;
	Bitmap bm;
	Button b1, b2;
	String link = "";
	byte[] bytes;
	TextView tv1,tv2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			setContentView(R.layout.activity1);
			iv = (ImageView)findViewById(R.id.imageView1);
			tv1 = (TextView)findViewById(R.id.textView1);
			tv2 = (TextView)findViewById(R.id.textView2);
			TALK_OBJECT = getIntent().getParcelableExtra(LoginActivity.OBJECT);
				tv2.setText("Hello, "+TALK_OBJECT.getName()+"!");
			
			Button b = (Button) findViewById(R.id.take);
			b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tv1.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent,0);	
				}
			});
			b2= (Button) findViewById(R.id.next);
			b2.setVisibility(View.GONE);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	     super.onOptionsItemSelected(item);
	     switch(item.getItemId()){
	     case R.id.about:
	    	 aboutMenuitem();
	    	 break;
	     case R.id.action_settings:
	    	 actionsettings();
	    	 break;
	     case R.id.logout:
	    	 logout();
	    	 break;
	     }   
		return true;
	}
	
	private void actionsettings() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("Help")
		.setMessage("On this page, you can:\n"
				+ "1. Take a picture\n"
				+ "2. Retake it if you wish to\n"
				+ "3. A next button will appear once you take a picture to go the next page\n"
				+ "4. Your image will be posted onto a server\n")
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub	
			}
		}).show(); 	
	}
	
	private void logout(){	
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return;	
	}

	private void aboutMenuitem() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("About")
		.setMessage(Html.fromHtml("<b><u>Developers</b></u><br>Arpit Paruthi<br>Jasmin Dhamelia<br>Somi Laad<br>Vivekchander Chandhira Sekaran"
				+ "<br><b><u>Contact</b></u>:<br> pittcomet@gmail.com"))
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show(); 	
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			bm = (Bitmap) data.getExtras().get("data");
			iv.setImageBitmap(bm);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bm.compress(Bitmap.CompressFormat.PNG,100, baos);
			bytes = baos.toByteArray();
			String imageEncoded = Base64.encodeToString(bytes,Base64.DEFAULT);
			new Imgur().execute(imageEncoded);
		}
	}
	
	class Imgur extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			String Image = params[0];
			String key = "8ca9d84ac6bcf3a";
			try {
				URL url = new URL("https://api.imgur.com/3/image");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				String da = URLEncoder.encode("image", "UTF-8") + "="
		            + URLEncoder.encode(Image, "UTF-8");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Client-ID " + key);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type",
		            "application/x-www-form-urlencoded");
				conn.connect();
				StringBuilder stb = new StringBuilder();
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(da);
				wr.flush();
				BufferedReader rd = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					stb.append(line).append("\n");
				}
				wr.close();
				rd.close();
				JSONObject finalResult = new JSONObject(stb.toString());
				link = finalResult.getJSONObject("data").getString("link");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			return link;
		}
		
		@Override
	    protected void onPostExecute(String output) {
				link = output;
		        TALK_OBJECT.setUrl(link);
		        Log.e("url",TALK_OBJECT.getUrl());
		        b2 = (Button) findViewById(R.id.next);
		        b1 = (Button) findViewById(R.id.take);
		        b1.setText("Retake");
		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)b1.getLayoutParams();
		        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		        b1.setLayoutParams(params);
				b2.setVisibility(View.VISIBLE);
		        b2.setOnClickListener (new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(Activity1.this,Activity2.class);
						intent.putExtra("image", bytes);
						intent.putExtra(OBJECT, TALK_OBJECT);
						startActivity(intent);	
					}
		        });
	    }
	}
	
	public void onBackPressed(){	
		return;
	}	
}


