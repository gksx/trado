package org.trado;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.microhttp.LogEntry;
import org.microhttp.Logger;

public class TradoTraceLogger implements Logger, TradoLogger {
    private final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void log(LogEntry... entries) {
        long uptime = runtimeMXBean.getUptime();
        String text = Stream.of(entries)
                .map(e -> e.key() + "=" + e.value())
                .collect(Collectors.joining(", ", "[" + uptime + "] ", ""));
        System.out.println(text);
    }

    @Override
    public void log(Exception e, LogEntry... entries) {
        e.printStackTrace();
        log(entries);
    }

    @Override
    public void log(String entry){
        long uptime = runtimeMXBean.getUptime();
        System.out.println("[" + uptime + "] " + entry);
    }

    @Override
    public void log(Exception e, String entry){
        long uptime = runtimeMXBean.getUptime();
        e.printStackTrace();
        System.out.println("[" + uptime + "] " + entry);
    }
}
