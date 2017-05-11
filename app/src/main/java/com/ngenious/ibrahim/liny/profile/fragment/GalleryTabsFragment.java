package com.ngenious.ibrahim.liny.profile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.profile.SimpleProfileActivity;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.adapter.GalleryAdapter;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.app.AppController;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryTabsFragment extends Fragment {
    private String TAG = GalleryTabsFragment.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
//    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private Fragment myContext;
    private GalleryTabsFragmentListener mListener;

    public GalleryTabsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        SimpleProfileActivity a;

        if (context instanceof SimpleProfileActivity){
            a=(SimpleProfileActivity) context;
        }
        if (!(context instanceof GalleryTabsFragmentListener)) throw new AssertionError();
        mListener=(GalleryTabsFragmentListener)context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

View view = inflater.inflate(R.layout.fragment_gallery_tabs, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        /* recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

        SimpleProfileActivity simpleProfileActivity = (SimpleProfileActivity)getActivity();
        simpleProfileActivity.showImage(recyclerView,images);
        fetchImages();
        return view;
    }

    private void fetchImages() {

        //pDialog.setMessage("Downloading json...");
        //pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
               // pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }


    private void done(){
        if (mListener==null){
            throw new AssertionError();
        }
        //
        //blabla
        //
        //  User user = new User(name, email, password);

        //mListener.onDescriptionFragmentListenerFinish(user);

    }
    public interface GalleryTabsFragmentListener{
        void onGalleryTabsFragmentListenerFinish(User user);
    }

}