package com.ngenious.ibrahim.liny.foursquare.helpers;

/**
 * Created by ibrahim on 16/05/17.
 */

import com.ngenious.ibrahim.liny.foursquare.models.FoursquareVenue;

import java.util.ArrayList;
import java.util.List;

public class FoursquareResponse {

    // A group object within the response.
    public FoursquareGroup group;
    public List<FoursquareVenue> venues = new ArrayList<>();

}