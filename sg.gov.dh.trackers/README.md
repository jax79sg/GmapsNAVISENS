
Upon importing of the Trackers module, you should be able to perform the following import of classes.

Do the following in your activity class
'''
import sg.gov.dh.utils.Coords;
import sg.gov.dh.trackers.Event;
import sg.gov.dh.trackers.NavisensLocalTracker;
import sg.gov.dh.trackers.TrackerListener;

# This is the only 2 lines you need to use the Tracker.
# Note that this NavisensLocalTracker class can only register XYZ offsets in metres, NOT lat/long.

        DHBFTLocalApplication.tracker = new NavisensLocalTracker(this);
        DHBFTLocalApplication.tracker.setTrackerListener(new TrackerListener() {
            @Override
            public void onNewCoords(Coords coords) {
                #You receive the coordinates here, you can do whatever you want with it.
                Log.d(TAG,"X:"+coords.getX());
                Log.d(TAG,"Y:"+coords.getY());
                Log.d(TAG,"Z:"+coords.getAltitude());
                Log.d(TAG,"bearing:"+coords.getBearing());
                Log.d(TAG,"Action:"+coords.getAction());
                updateMap(coords);
                sendCoords(coords);
            }

            @Override
            public void onNewEvent(Event event) {
                #This is deprecated in current version. Replaced by coords.getAction().
            }
        });
'''