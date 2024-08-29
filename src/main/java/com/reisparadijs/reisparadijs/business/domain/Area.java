package com.reisparadijs.reisparadijs.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Member;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

public class Area implements Comparable<Area> {
    private final Logger logger = LoggerFactory.getLogger(Member.class);
    private int zipcodeNumbers;
    private double zcLat;
    private double zcLon;



    public Area(int zipcodeNumbers, double zcLat, double zcLon) {
        this.zipcodeNumbers = zipcodeNumbers;
        this.zcLat = zcLat;
        this.zcLon = zcLon;
        // logger.info("Area created (for Area only zipcodeNumbers, Latitude and Longitude required)");
    }

    public int getZipcodeNumbers() {
        return zipcodeNumbers;
    }

    public double getZcLat() {
        return zcLat;
    }

    public double getZcLon() {
        return zcLon;
    }

    @Override
    public int compareTo(Area other) {
        return Integer.compare(this.zipcodeNumbers, other.zipcodeNumbers);
    }

    @Override
    public String toString() {
        return ("Zipcode_Numbers: " + zipcodeNumbers);
    }

}
