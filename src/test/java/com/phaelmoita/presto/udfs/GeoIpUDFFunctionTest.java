package com.phaelmoita.presto.udfs;

import com.facebook.presto.operator.scalar.FunctionAssertions;
import com.facebook.presto.spi.type.Type;
import com.phaelmoita.presto.udfs.geoip.GeoIpUDFFunction;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

public class GeoIpUDFFunctionTest {

    private FunctionAssertions functionAssertions;

    private String geoIpPath;

    private void assertFunction(String projection, Type expectedType, Object expected)
    {
        functionAssertions.assertFunction(projection, expectedType, expected);
    }

    private String getPathGeoLite2DbPath() {
        File resourcesDirectory = new File("temp");
        for (File nextFile : resourcesDirectory.listFiles()) {
            if(nextFile.isDirectory()) {
                if(nextFile.getName().toLowerCase().contains("GeoLite2".toLowerCase())) {
                    return nextFile.getAbsolutePath() + "/GeoLite2-City.mmdb";
                }
            }
        }
        return "./GeoIP2-City.mmdb";
    }

    @BeforeClass
    public void setUp()
    {
        functionAssertions = new FunctionAssertions().addScalarFunctions(GeoIpUDFFunction.class);
        geoIpPath = getPathGeoLite2DbPath();
    }


    @Test
    public void testGeoipCountry()
    {
        assertFunction("geo_ip('192.64.147.150', '" + geoIpPath + "', '0')", VARCHAR, "US");
    }

    @Test
    public void testGeoipState()
    {
        assertFunction("geo_ip('192.64.147.150', '" + geoIpPath + "', '1')", VARCHAR, "Florida");
    }

    @Test
    public void testGeoipCity()
    {
        assertFunction("geo_ip('192.64.147.150', '" + geoIpPath + "', '2')", VARCHAR, "Tampa");
    }

}
