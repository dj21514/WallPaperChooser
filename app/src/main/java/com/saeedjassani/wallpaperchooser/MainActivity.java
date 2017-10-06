package com.saeedjassani.wallpaperchooser;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ImagesRecyclerAdapter.ClickListener {

	private static final int PERMISSION_CONSTANT = 0;
	RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = findViewById(R.id.recyler_view);
		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(layoutManager);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSION_CONSTANT);

		} else {
			recyclerView.setAdapter(new ImagesRecyclerAdapter(this, this));
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_CONSTANT: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					recyclerView.setAdapter(new ImagesRecyclerAdapter(this, this));
				} else {
					Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void onClick(String path) {
		WallpaperManager myWallpaperManager
				= WallpaperManager.getInstance(getApplicationContext());
		try {
			myWallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
