package com.cardiologosguadalajara.wmsi.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.ui.BottomSheetDialog;
import com.cardiologosguadalajara.wmsi.ui.BottomSheetPrivacyDialog;
import com.cardiologosguadalajara.wmsi.ui.Security;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.billingclient.api.BillingClient.SkuType.SUBS;


public class SubscriptionActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener,PurchasesUpdatedListener {

    BillingProcessor bp;


    private BillingClient billingClient;

    public static final String PREF_FILE= "MyPref";
    public static final String SUBSCRIBE_KEY= "subscribe";
    public static final String ITEM_SKU_SUBSCRIBE= "yearly_subscription";

    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvysxS0aOFqgh3I2TGLQZlo11IA99uPcB9i/ZNAyaw6/TtM73ogzfUgoCg2Zum1unimEB5OFdN3whoGEoaKsGW8kfVAYE4Wmk1hNxQ1Q3u8mRNlR047BGSOgS9/UQFQmMTZ/qmBm7O1fRjiJOusOIWStbg4xZmLdFYL+8Pm9Wb3FyBbJ2WVUqMSWyV6LPQICmy50klOrfe0TwbgrOx9M3eLipsnqjaEe8vRrOLfNnk7rkkC1W/z8cN10ibnAmPPrCQiWfjOUrn5NDR77xbo3czT3Nmia25+PtVw/R0kTSENsYFR7xUvMhA06ApMsPN0iQr/ZwsxPv8j6NtG9KJBG8aQIDAQAB";
    public static final String MERCHANT_ID = "07426637350088858568";
    public static final String PRODUCT_ID = "yearly_subscription";

    @BindView(R.id.btn_subscription)
    Button btn_subscription;

    @BindView(R.id.tv_terms_of_use)
    TextView tv_terms_of_use;
    @BindView(R.id.tv_policy)
    TextView tv_policy;
    @BindView(R.id.tv_restore_purchase)
    TextView tv_restore_purchase;

    @BindView(R.id.ib_close_main)
    ImageButton ib_close_main;

    SharedPreferences mPrefSubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


        

        // Establish connection to billing client
        //check subscription status from google play store cache
        //to check if item is already Subscribed or subscription is not renewed and cancelled
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(SUBS);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if(queryPurchases!=null && queryPurchases.size()>0){
                        handlePurchases(queryPurchases);
                    }
                    //if no item in purchase list means subscription is not subscribed
                    //Or subscription is cancelled and not renewed for next month
                    // so update pref in both cases
                    // so next time on app launch our premium content will be locked again
                    else{
                        saveSubscribeValueToPref(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getApplicationContext(),"Service Disconnected",Toast.LENGTH_SHORT).show();
            }
        });



        ButterKnife.bind(this);

        mPrefSubscribe = getSharedPreferences(AppConstant.PREF_SUBSCRIBE, MODE_PRIVATE);


        bp = new BillingProcessor(this, LICENSE_KEY, this);
        bp.initialize();

        btn_subscription.setOnClickListener(this);
        tv_terms_of_use.setOnClickListener(this);
        tv_policy.setOnClickListener(this);
        tv_restore_purchase.setOnClickListener(this);
        ib_close_main.setOnClickListener(this);

    }

    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }
    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }
    private boolean getSubscribeValueFromPref(){
        return getPreferenceObject().getBoolean( SUBSCRIBE_KEY,false);
    }
    private void saveSubscribeValueToPref(boolean value){
        getPreferenceEditObject().putBoolean(SUBSCRIBE_KEY,value).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
        if(billingClient!=null){
            billingClient.endConnection();
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_subscription.getId()) {


            //check if service is already connected
            if (billingClient.isReady()) {
                initiatePurchase();
            }
            //else reconnect service
            else{
                billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            initiatePurchase();
                        } else {
                            Toast.makeText(getApplicationContext(),"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onBillingServiceDisconnected() {
                        Toast.makeText(getApplicationContext(),"Service Disconnected ",Toast.LENGTH_SHORT).show();
                    }
                });
            }



//            bp.subscribe(this, "");
//            mPrefSubscribe.edit().putBoolean(AppConstant.IS_SUBSCRIBE, true).apply();
//            startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
//            finish();

        } else if (v.getId() == tv_terms_of_use.getId()) {
            BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
            bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");

        } else if (v.getId() == tv_policy.getId()) {

            BottomSheetPrivacyDialog bottomSheetDialog = BottomSheetPrivacyDialog.getInstance();
            bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");

        } else if (v.getId() == tv_restore_purchase.getId()) {
            Toast.makeText(SubscriptionActivity.this, "No implemented yet!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == ib_close_main.getId()) {
            mPrefSubscribe.edit().putBoolean(AppConstant.IS_SUBSCRIBE, false).apply();
            startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(ITEM_SKU_SUBSCRIBE);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(SUBS);
        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    billingClient.launchBillingFlow(SubscriptionActivity.this, flowParams);
                                } else {
                                    //try to add subscription item "sub_example" in google play console
                                    Toast.makeText(getApplicationContext(), "Item not Found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(),
                    "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvysxS0aOFqgh3I2TGLQZlo11IA99uPcB9i/ZNAyaw6/TtM73ogzfUgoCg2Zum1unimEB5OFdN3whoGEoaKsGW8kfVAYE4Wmk1hNxQ1Q3u8mRNlR047BGSOgS9/UQFQmMTZ/qmBm7O1fRjiJOusOIWStbg4xZmLdFYL+8Pm9Wb3FyBbJ2WVUqMSWyV6LPQICmy50klOrfe0TwbgrOx9M3eLipsnqjaEe8vRrOLfNnk7rkkC1W/z8cN10ibnAmPPrCQiWfjOUrn5NDR77xbo3czT3Nmia25+PtVw/R0kTSENsYFR7xUvMhA06ApMsPN0iQr/ZwsxPv8j6NtG9KJBG8aQIDAQAB";

            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    void handlePurchases(List<Purchase>  purchases) {
        for(Purchase purchase:purchases) {
            //if item is purchased
            if (ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
            {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(getApplicationContext(), "Error : invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if(!getSubscribeValueFromPref()){
                        saveSubscribeValueToPref(true);
                        mPrefSubscribe.edit().putBoolean(AppConstant.IS_SUBSCRIBE, true).apply();
                        startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
                        finish();


                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        this.recreate();
                    }
                }
            }
            //if purchase is pending
            else if( ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
            {
                Toast.makeText(getApplicationContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown mark false
            else if(ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE)
            {
                saveSubscribeValueToPref(false);
                Toast.makeText(getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                saveSubscribeValueToPref(true);
                mPrefSubscribe.edit().putBoolean(AppConstant.IS_SUBSCRIBE, true).apply();
                startActivity(new Intent(SubscriptionActivity.this, MainActivity.class));
                finish();
            }
        }
    };

    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //if item subscribed
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already subscribed then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(SUBS);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if(alreadyPurchases!=null){
                handlePurchases(alreadyPurchases);
            }
        }
        //if Purchase canceled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getApplicationContext(),"Purchase Canceled",Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(getApplicationContext(),"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}