package com.rutvik.bhagwatiholidays;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Rakshit on 07-03-2016.
 */
public class SingleOfferViewActivity extends AppCompatActivity {

    private static final String TAG=App.APP_TAG+SingleOfferViewActivity.class.getSimpleName();

    ImageView offerImage;

    TextView offerType, offerValidity, offerTitle, offerDescription;

    public com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    public DisplayImageOptions options;

    private Toolbar mToolbar;

    private Button btnBookNow;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_offer_view);

        app=(App) getApplication();

        app.trackScreenView(SingleOfferViewActivity.class.getSimpleName());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Offer Details");

        offerImage = (ImageView) findViewById(R.id.iv_offerImage);

        offerTitle = (TextView) findViewById(R.id.tv_offerTitle);

        offerType = (TextView) findViewById(R.id.tv_applicableOn);

        offerValidity = (TextView) findViewById(R.id.tv_offerValidity);

        offerDescription = (TextView) findViewById(R.id.tv_offerDescription);

        btnBookNow = (Button) findViewById(R.id.btn_bookNow);

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                app.trackEvent(SingleOfferViewActivity.class.getSimpleName(),"BOOK NOW CLICKED","BUTTON");

                startActivity(new Intent(SingleOfferViewActivity.this, PaymentActivity.class));

            }
        });

        try {

            offerTitle.setText(getIntent().getStringExtra("offer_title"));

            offerDescription.setText(Html.fromHtml(getIntent().getStringExtra("offer_description")));

            offerType.setText(getIntent().getStringExtra("offer_type"));

            offerValidity.setText(getIntent().getStringExtra("offer_validity"));

            imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.loading_image)
                    .cacheInMemory()
                    .cacheOnDisc()
                    .displayer(new RoundedBitmapDisplayer(1))
                    .build();

            imageLoader.displayImage(getIntent().getStringExtra("offer_image"),
                    offerImage,
                    options);

        } catch (Exception e) {
            e.printStackTrace();
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(SingleOfferViewActivity.this,"Content shared Successfully.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SingleOfferViewActivity.this,"Sharing failed.",Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.package_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share_fb:
                shareContentOnFacebook();
                return true;

            case R.id.action_share:
                app.trackEvent(SingleOfferViewActivity.class.getSimpleName(),"SIMPLE SHARING","SHARE");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Watch out this new exciting offer from Bhagwati Holidays "+ getIntent().getStringExtra("offer_title")+" https://bnc.lt/m/q1UrTyr8Wr");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareContentOnFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            app.trackEvent(SingleOfferViewActivity.class.getSimpleName(),"FB SHARING","SHARE");
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(getIntent().getStringExtra("offer_title"))
                    .setContentDescription(getIntent().getStringExtra("offer_description") + " see more https://bnc.lt/m/q1UrTyr8Wr")
                    .setImageUrl(Uri.parse(getIntent().getStringExtra("offer_image")))
                    .setContentUrl(Uri.parse("https://bnc.lt/m/q1UrTyr8Wr"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
