package bhfragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rutvik.bhagwatiholidays.App;
import com.rutvik.bhagwatiholidays.LazyAdapter;
import com.rutvik.bhagwatiholidays.R;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jsonobj.PackageList;
import model.SimpleOffersAndPromotions;
import webservicehandler.PostHandler;

/**
 * Created by ACER on 10-Mar-16 at 11:03 AM.
 */
public class FragmentHotelPackages extends Fragment {

    private static final String TAG = App.APP_TAG + FragmentHotelPackages.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressDialog progressDialog;

    private final List<PackageList.Package> packages = new ArrayList<>();

    private final List<PackageList.Package> searchedPackages = new ArrayList<>();

    App app;

    //private int range = 20;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        app = (App) activity.getApplication();


    }

    /*public void countRangeAndLoadPackagesInAdapter() {

        Log.i(TAG, "RANGE IS: " + range);

        if (range >= packages.size()) {
            range = packages.size();
        }

        for (int i = mAdapter.getItemCount(); i < range; i++) {
            Log.i(TAG, "ADDING PACKAGE: " + i);
            ((LazyAdapter) mAdapter).addPackage(packages.get(i));
        }

    }
*/
    public FragmentHotelPackages() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void filterOffers(List<PackageList.Package> modelList, String query) {

        searchedPackages.clear();

        Log.i(TAG, "FILTERING OFFERS");

        query = query.toLowerCase();

        for (PackageList.Package model : modelList) {
            final String name = model.getPackage_name().toLowerCase();
            final String places = model.getPlaces().toLowerCase();
            final String days = model.getDays().toLowerCase();
            final String nights = model.getNights().toLowerCase();
            //Log.i(TAG, "model.getTitle(): " + );
            if (name.contains(query)) {
                Log.i(TAG, "MATCH FOUND ADDING MODEL");
                searchedPackages.add(model);
            }
        }

        Log.i(TAG, "MODEL ARRAY SIZE: " + searchedPackages.size());

    }

    public void searchForPackage(String inputText) {

        filterOffers(packages, inputText);


        ((LazyAdapter) mAdapter).animateTo(searchedPackages);




  /*      try {

        }
        catch (ArrayIndexOutOfBoundsException e){
            Log.i(TAG,"Searched Array Size: "+searchedPackages.size()+" Adapter Array Size: "+mAdapter.getItemCount());
        }*/

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_holiday_packages, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_hotelPackages);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLastItemDisplaying()) {

                    ((LazyAdapter) mAdapter).increaseRange(packages.size());

                }
            }
        });

        //loadOffersAsync();

        return rootView;
    }


    public void loadOffersAsync() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {

                progressDialog = ProgressDialog.show(FragmentHotelPackages.this.getActivity(), "Please Wait...", "Getting packages...", true, true);

            }

            @Override
            protected Void doInBackground(Void... params) {

                HashMap<String, String> postParams = new HashMap<>();

                postParams.put("method", "get_package_list");

                new PostHandler("BWT", 4, 2000).doPostRequest("http://bhagwatiholidays.com/admin/webservice/index.php", postParams, new PostHandler.ResponseCallback() {
                    @Override
                    public void response(int status, String response) {
                        if (status == HttpURLConnection.HTTP_OK) {
                            try {
                                PackageList packageList = new PackageList(response, "package_list");
                                packages.clear();
                                for (PackageList.Package p : packageList.getPackageList()) {
                                    packages.add(p);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                mAdapter = new LazyAdapter(FragmentHotelPackages.this.getActivity());

                mRecyclerView.setAdapter(mAdapter);

                //countRangeAndLoadPackagesInAdapter();

                for (PackageList.Package p : packages) {
                    ((LazyAdapter) mAdapter).addPackage(p);
                }

                Log.i(TAG, "INSIDE ON POST EXECUTE");


                try {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    progressDialog = null;
                }

            }
        }.execute();

    }


    public boolean isLastItemDisplaying() {
        if (mRecyclerView.getAdapter().getItemCount() != 0) {
            int lastItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastItem != RecyclerView.NO_POSITION && lastItem == mRecyclerView.getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }


}