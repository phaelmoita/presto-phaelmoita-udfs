package com.phaelmoita.presto.udfs.geoip;

import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import org.apache.commons.io.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class GeoIpUDFFunction {

    static DatabaseReader reader;

    private static final String NOT_RESOLVED = "NOT_RESOLVED";

    public static ByteArrayInputStream reteriveByteArrayInputStream(File file) throws IOException {
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    }

    public static DatabaseReader getReader(String bucket, String loc) {

        DatabaseReader reader = null;
        try {
            reader = new DatabaseReader.Builder(reteriveByteArrayInputStream(new File(loc))).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reader;

    }


    @ScalarFunction("geo_ip")
    @Description("Returns the location of ip")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice geoIp(
            @SqlType(StandardTypes.VARCHAR) Slice ip,
            @SqlType(StandardTypes.VARCHAR) Slice path,
            @SqlType(StandardTypes.VARCHAR) Slice tp
    ) {

        try {
            if (reader == null) {
                reader = getReader("", path.toStringUtf8());
            }

            String _tp = tp.toStringUtf8();

            InetAddress ipAddress = InetAddress.getByName(ip.toStringUtf8());
            CityResponse response = reader.city(ipAddress);
            if (_tp.equals("0")) {
                if (response.getCountry() != null && response.getCountry().getName() != null) {
                    return Slices.utf8Slice(response.getCountry().getIsoCode());
                }
            } else if (_tp.equals("1")) {
                if (response.getMostSpecificSubdivision() != null && response.getMostSpecificSubdivision().getName() != null) {
                    return Slices.utf8Slice(response.getMostSpecificSubdivision().getName());
                }
            } else if (_tp.equals("2")) {
                if (response.getCity() != null && response.getCity().getName() != null) {
                    return Slices.utf8Slice(response.getCity().getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Slices.utf8Slice(NOT_RESOLVED);
    }

}
