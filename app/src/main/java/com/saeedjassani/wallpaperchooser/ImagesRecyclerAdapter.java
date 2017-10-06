package com.saeedjassani.wallpaperchooser;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeedjassani on 5/10/17.
 */

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.MyViewHolder> {

	Context context;
	List<String> imagesData;
	ClickListener clickListener;

	ImagesRecyclerAdapter(Context context, ClickListener clickListener) {
		this.context = context;
		imagesData = getCameraImages(context);
		this.clickListener = clickListener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.row_image, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Glide.with(context)
				.load(imagesData.get(position))
				.placeholder(R.color.colorAccent)
				.into(holder.imageView);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MainActivity.SCREEN_SIZE_WIDTH / 2, (MainActivity.SCREEN_SIZE_HEIGHT * 3) / 4);
		layoutParams.setMargins(1,1,1,1);
		holder.imageView.setLayoutParams(layoutParams);
	}

	@Override
	public int getItemCount() {
		return imagesData.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		ImageView imageView;

		MyViewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.image);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			clickListener.onClick(imagesData.get(getAdapterPosition()));
		}
	}

	interface ClickListener {
		void onClick(String path);
	}

	private static List<String> getCameraImages(Context context) {
		final String[] projection = {MediaStore.Images.Media.DATA};
		final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection,
				null,
				null,
				MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
		ArrayList<String> result = new ArrayList<String>(cursor.getCount());
		if (cursor.moveToFirst()) {
			final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			do {
				final String data = cursor.getString(dataColumn);
				result.add(data);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return result;
	}
}
