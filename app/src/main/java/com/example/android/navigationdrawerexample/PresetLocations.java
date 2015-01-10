package com.example.android.navigationdrawerexample;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dan-silver on 12/23/14.
 */
public class PresetLocations {


    public static StreetViewLocationRecord getRandomLocation() {
        ArrayList<StreetViewLocationRecord> locations =
                new ArrayList<StreetViewLocationRecord>() {
                    {
                        add(
                                new StreetViewLocationRecord()
                                        .setLatitude(59.6288118)
                                        .setLongitude(-151.4854689)
                                        .setBearing(299.94)
                                        .setTilt(90)
                        );
                        add(
                                new StreetViewLocationRecord()
                                        .setLatitude(45.9225062)
                                        .setLongitude(9.2875653)
                                        .setBearing(41.21)
                                        .setTilt(82)
                        );
                        add(
                                new StreetViewLocationRecord()
                                        .setLatitude(40.7066332)
                                        .setLongitude(-74.0126046)
                                        .setBearing(210)
                                        .setTilt(90)
                        );
                        add(
                                new StreetViewLocationRecord()
                                        .setLatitude(51.5134075)
                                        .setLongitude(-0.0886053)
                                        .setBearing(256)
                                        .setTilt(90)
                        );
                    }
                };
        int index = (new Random()).nextInt(locations.size());

        return locations.get(index);
    }
}
