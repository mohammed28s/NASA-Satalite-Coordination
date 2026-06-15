package com.Satliate.NASA.Service;





import com.Satliate.NASA.Entity.Satellite;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.IERSConventions;
import org.orekit.frames.FramesFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.utils.PVCoordinates;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class PropagationService {

    public GeodeticPoint propagate(Satellite sat) {
        TLE tle = new TLE(sat.getLine1(), sat.getLine2());
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);

        // Correct UTC usage
        AbsoluteDate date = new AbsoluteDate(Instant.now(), TimeScalesFactory.getUTC());

        PVCoordinates pv = propagator.getPVCoordinates(date, FramesFactory.getITRF(IERSConventions.IERS_2010, true));

        double latitude = Math.toDegrees(Math.asin(pv.getPosition().getZ() / pv.getPosition().getNorm()));
        double longitude = Math.toDegrees(Math.atan2(pv.getPosition().getY(), pv.getPosition().getX()));
        double altitude = pv.getPosition().getNorm() - 6378137.0; // Earth radius

        return new GeodeticPoint(latitude, longitude, altitude);
    }
}
