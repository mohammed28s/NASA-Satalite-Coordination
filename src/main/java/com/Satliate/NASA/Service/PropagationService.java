package com.Satliate.NASA.Service;








import jakarta.annotation.PostConstruct;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;
import org.springframework.stereotype.Service;


import java.io.File;
import java.time.Instant;
import java.util.Date;

@Service
public class PropagationService {

    private OneAxisEllipsoid earth;

    @PostConstruct
    public void initOrekit() {
        try {
            File orekitData = new File("src/main/resources/orekit-data");
            if (!orekitData.exists() || !orekitData.isDirectory()) {
                throw new IllegalStateException("Orekit data folder not found: " + orekitData.getAbsolutePath());
            }

            // Use the global DataContext so all Orekit subsystems see the data
            DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
            manager.clearProviders(); // reset any defaults
            manager.addProvider(new DirectoryCrawler(orekitData));

            // Define Earth model (WGS84)
            earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                    Constants.WGS84_EARTH_FLATTENING,
                    FramesFactory.getITRF(org.orekit.utils.IERSConventions.IERS_2010, true));

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Orekit", e);
        }
    }

    public PositionDto computePosition(String line1, String line2, Instant when) {
        try {
            TLE tle = new TLE(line1, line2);
            TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);

            AbsoluteDate date = new AbsoluteDate(Date.from(when), TimeScalesFactory.getUTC());
            PVCoordinates pv = propagator.getPVCoordinates(date, FramesFactory.getEME2000());

            var geo = earth.transform(pv.getPosition(), FramesFactory.getEME2000(), date);

            double lat = Math.toDegrees(geo.getLatitude());
            double lon = Math.toDegrees(geo.getLongitude());
            double alt = geo.getAltitude();

            return new PositionDto(lat, lon, alt);
        } catch (Exception e) {
            return new PositionDto(0.0, 0.0, 0.0);
        }
    }

    public record PositionDto(double latitude, double longitude, double altitude) {}
}
