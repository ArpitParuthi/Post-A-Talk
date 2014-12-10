package com.example.cometandroidapp;

import java.io.ByteArrayOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity2 extends Activity implements OnClickListener {
	Bitmap bm;
	ImageView iv1;
	String url = "";
	MultiSelectionSpinner spinner;
	EditText edit1, edit2;
	public static String par ="com.example.cometandroidapp.Talk";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);
		Intent intent = getIntent();
		byte[] byteArray = getIntent().getByteArrayExtra("image");
		bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		url = intent.getStringExtra("url");
		Log.e("url",url);
		iv1 = (ImageView)findViewById(R.id.iv1);
		iv1.setImageBitmap(bm);
		edit1=(EditText)findViewById(R.id.editText1);
		edit2=(EditText)findViewById(R.id.editText2);
		Button b2 = (Button) findViewById(R.id.next);
		b2.setOnClickListener(this); 
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1); 
		Resources res = getResources();
		spinner.setItems(res.getStringArray(R.array.categories));  
	}
	
	public void PacelableMethod(){  
	        Talk talk= new Talk();
	        talk.setUrl(url);
	        talk.setSpeaker(edit1.getText().toString());
	        talk.setCategory(spinner.getSelectedItemsAsString());
	        Intent mIntent = new Intent(this,Activity3.class);  
	        Bundle mBundle = new Bundle();  
	        mBundle.putParcelable(par, talk);
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] bytes = stream.toByteArray();
	        mIntent.putExtra("image",bytes);
	        mIntent.putExtras(mBundle);  
	        startActivity(mIntent);  
	}

	public void onClick(View v) {
			 PacelableMethod(); 
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
	     }   
		return true;
	}
	
	private void actionsettings() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("Help")
		.setMessage("On this page, you will:\n"
				+ "1. Add the name of the speaker\n"
				+ "2. Add the title of the talk\n"
				+ "3. Choose one or more categories the talk falls into\n"
				+ "4. Proceed onto the next page\n")
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show(); 
		
	}

	private void aboutMenuitem() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("About")
		.setMessage("Develpers: Arpit, Jasmin, Vivek, Somi")
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show(); 
		
	}
}
