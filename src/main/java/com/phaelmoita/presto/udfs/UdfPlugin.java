package com.phaelmoita.presto.udfs;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;
import com.phaelmoita.presto.udfs.geoip.GeoIpUDFFunction;

import java.util.Set;

public class UdfPlugin implements Plugin
{
    @Override
    public Set<Class<?>> getFunctions()
    {
        return ImmutableSet.<Class<?>>builder()
                .add(GeoIpUDFFunction.class)
                .build();
    }
}
