# NAVISENS code sharing
We trialled NAVISENS and another solution about 2 years back and along the way developed some prototype codes and eventually the BFT prototype that was deployed. This repo function as a test app and also to share the entire source codes with you guys. 

## Dependancies
Please ensure that your app's gradle has the following dependancies, you can also find the aar files in the libs folder.<br>
```java
implementation  project(path: ':sg.gov.dh.utils')
implementation  project(path: ':sg.gov.dh.trackers')
implementation 'com.navisens:motiondnaapi:1.9.2'
```

Please ensure that the following dependancies are given to the app
```java
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## Important codes
To initialise the NAVISENS tracker in your main activity
```java
        NavisensLocalTracker tracker;
        tracker = new NavisensLocalTracker(this);
        tracker.setTrackerListener(new TrackerListener() {
            @Override
            public void onNewCoords(Coords coords) {
                //When the tracker receives new coordinates, it will be pushed in this onNewCoords method. 
                //The information is in Coords class instance.
                
                //E.g. Receive local coordinates
                Log.d(TAG,"X:"+coords.getX());
                Log.d(TAG,"Y:"+coords.getY());
                Log.d(TAG,"Z:"+coords.getLocalAltitude());
                Log.d(TAG,"bearing:"+coords.getLocalBearing());

                //E.g. Receive global coordinates (lat long)
                Log.d(TAG,"Lat:"+coords.getLatitude());
                Log.d(TAG,"Long:"+coords.getLongitude());
                Log.d(TAG,"Alt:"+coords.getGlobalAltitude());
                Log.d(TAG,"Heading:"+coords.getGlobalBearing());

                //E.g. Receive current action
                Log.d(TAG,"X:"+coords.getAction()); //Example, FORWARD, FIDGETING, STATIONARY
                Log.d(TAG,"Y:"+coords.getVerticalAction()); //Example, VERTICAL_STATUS_STAIRS_UP, VERTICAL_STATUS_STAIRS_DOWN

                //Do whatever you want with the above
                //E.g. Using the coordinates to plot, using the actions to show animation...etc.
                //In this case, i pump the coordinates into Google Map's LocationSource.
                Location newLoc = new Location("NAVISENS");
                newLoc.setLatitude(coords.getLatitude());
                newLoc.setLongitude(coords.getLongitude());
                newLoc.setAltitude(coords.getGlobalAltitude());
                newLoc.setBearing((float)coords.getGlobalBearing());
                navSource.listener.onLocationChanged(newLoc);
            }

            @Override
            public void onNewEvent(Event event) {

            }
```
Next, you need to give the tracker a location. If you give it a xy, it will initialise as a local cartesian tracker. If you give latlong, it will be global. The code in this repo does the latter by long press a position on the map.
```java
Coords coord = new Coords(....)
tracker.setManualLocation(coords);
```
With the above, you are set to go.
<br>
<br>
## Supporting modules explanation
You are free to change anything in following modules, would request that you change in this repo so everyone can benefit. Thanks.
<br>
### sg.gov.dh.utils
This is a module to host a utility Coordinate class. It came about as we were testing more than one BFT solution and needed a common way to address coordinates.
You may you any of the constructors, or even create your own constructors if necessary. This Coordinate class can handle both Local Cartesian coordinate and Global Lat Long.

### sg.gov.dh.trackers
This module is create to encapsulate the different BFT solutions we had. <br>
Tracker Interface. We will implement this interface if we wish to create a generic wrapper for any 3rd Party solution. <br>
TrackerListener Interface. We will implement this interface if we wish to create a generic wrapper to push out newly observed coordinates.<br>
NavisensLocalTracker. This is an implementation of the Tracker Interface, it also contains the logic for NAVISENS.
