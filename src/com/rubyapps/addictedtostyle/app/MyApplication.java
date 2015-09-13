package com.rubyapps.addictedtostyle.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Application;

import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.helper.ParseUtils;
import com.rubyapps.addictedtostyle.model.GridItem;

public class MyApplication extends Application {

	private static MyApplication mInstance;

	private List<GridItem> items = new ArrayList<GridItem>(Arrays.asList(
			new GridItem(R.drawable.latest_fashion, "Latest Fashion","http://addictedtostyle.net/category/fashion/"),
			new GridItem(R.drawable.fashion_police, "Fashion Police","http://www.madivas.com/"),
			new GridItem(R.drawable.fashion_academy, "Fashion Academy","http://addictedtostyle.net/category/fashion-academy/"),
			new GridItem(R.drawable.makeup_training, "MakeUp Training","http://www.stellasaddiction.com/"),
			new GridItem(R.drawable.urban_chic, "Urban Style","http://www.kamdora.com/"),
			new GridItem(R.drawable.boss_lady, "Boss lady", "http://addictedtostyle.net/tag/boss-lady-camp/"),
			new GridItem(R.drawable.ankara_styles, "Ankara Styles", "http://ankarafestla.tumblr.com/"),
			new GridItem(R.drawable.hair, "Glam Hairstyles", "http://naturalhairvideos.tumblr.com/"),
			new GridItem(R.drawable.nails, "Nail Art", "http://www.thatigbochick.com/search/label/NAIL%20IT"),
			new GridItem(R.drawable.wedding, "Weddings","http://weddaily.com/"),
			new GridItem(R.drawable.celebrity_style, "Celebrity Styles","http://blog.glamafrica.com/category/celebs/"),
			new GridItem(R.drawable.cosmopolitian, "Cosmopolitan","http://www.cosmopolitan.ng/"),
			new GridItem(R.drawable.skincare, "Skincare","http://www.cloudywithachanceofwine.com/category/skin-care/"),
			new GridItem(R.drawable.men_styles, "Men Styles", "http://www.360nobs.com/category/fashion/fashion-men/"),
			new GridItem(R.drawable.big_and_beautiful, "Big and Beautiful", "http://www.trendycurvy.com"),
			new GridItem(R.drawable.fashion_junkie, "Fashion Junkie","http://www.fashionjunkie9ja.com/"),
			new GridItem(R.drawable.giveaway, "Giveaway","http://addictedtostyle.net/tag/giveaway/"),
			new GridItem(R.drawable.gele_styles, "Gele Styles","http://www.dezangozone.com/search/label/Gele"),
			new GridItem(R.drawable.your_wish_our_command, "Your Wish, Our Command","http://addictedtostyle.net/your-wish-our-command/"),
			new GridItem(R.drawable.rate_us, "Rate and Update","file:///android_asset/html.html"))); 

	public List<GridItem> getItemsList() {
		return items;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// register with parse
		ParseUtils.registerParse(this);
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}
}