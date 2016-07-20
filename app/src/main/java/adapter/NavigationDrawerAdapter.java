package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.rutvik.bhagwatiholidays.App;
import com.rutvik.bhagwatiholidays.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.NavDrawerItem;

/**
 * Created by Rakshit on 03-03-2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    final List<NavDrawerItem> data=new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    private static final String TAG = App.APP_TAG + NavigationDrawerAdapter.class.getSimpleName();


    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        getData();
    }

    public void clearAndRefreshNavItems() {
        Log.i(TAG, "NOW CLEARING AND REFRESHING NAV DRAWER ADAPTER");
        data.clear();
        getData();
    }

    public void getData() {
        Log.i(TAG,"setting navigation drawer data....");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> navDrawerTitleSet = new LinkedHashSet<>();
        navDrawerTitleSet.add("Offers & Promotions");
        navDrawerTitleSet.add("My Bookings");
        navDrawerTitleSet.add("Book Buses");
        navDrawerTitleSet.add("Share");
        navDrawerTitleSet.add("Rate Us");

        if (sp.getBoolean("IS_NOTIFICATION_DISABLED", false)) {
            navDrawerTitleSet.add("Enable Notifications");
        } else {
            navDrawerTitleSet.add("Disable Notifications");
        }

        navDrawerTitleSet.add("Support");
        navDrawerTitleSet.add("Locate Us");
        navDrawerTitleSet.add("Send Feedback");


        // preparing navigation drawer items
        for (String t : navDrawerTitleSet) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(t);
            data.add(navItem);
        }
        notifyDataSetChanged();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
