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
    
    private List<GridItem> items = new ArrayList<GridItem>(
			Arrays.asList(
					new GridItem(R.drawable.fashion, "Fashion", "http://www.fashionpolicenigeria.com/"),//TODO read-more
					new GridItem(R.drawable.beauty, "Beauty", "http://www.kamdora.com/"),
					new GridItem(R.drawable.hair, "Hair", "http://www.voiceofhair.com/category/hairspiration/"),
					new GridItem(R.drawable.wedding, "Weddings", "http://www.weddingdigestnaija.com/"),
					new GridItem(R.drawable.celebrity_style, "Celebrity Style", "http://blog.glamafrica.com/category/celebs/"),
					new GridItem(R.drawable.lifestyle_relationships, "Lifestyle: Relationship", "http://www.cosmopolitan.ng/relationships/"),
					new GridItem(R.drawable.diy_fashion_tips, "DIY Fashion Tips", "http://addictedtostyle.net/"),
					new GridItem(R.drawable.juicy_gist, "Juicy Gist", "http://yabaleftonline.com/"),
					new GridItem(R.drawable.giveaway, "Giveaway", "http://addictedtostyle.net/category/giveaway/"),
					new GridItem(R.drawable.rate_us, "Rate & Update", " https://play.google.com/store/apps/details?id=com.rubyapps.addictedtostyle")));
				
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