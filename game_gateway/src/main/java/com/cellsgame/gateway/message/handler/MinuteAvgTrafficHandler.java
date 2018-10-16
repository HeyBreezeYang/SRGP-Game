package com.cellsgame.gateway.message.handler;

import io.netty.channel.ChannelHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File Description.
 *
 * @author Yang
 */
@ChannelHandler.Sharable
public class MinuteAvgTrafficHandler extends ChannelTrafficShapingHandler {
    private static final Logger log = LoggerFactory.getLogger(MinuteAvgTrafficHandler.class);

    public MinuteAvgTrafficHandler() {
        super(60000);
    }


    /**
     * Called each time the accounting is computed from the TrafficCounters.
     * This method could be used for instance to implement almost real time accounting.
     *
     * @param counter the TrafficCounter that computes its performance
     */
    @Override
    protected void doAccounting(TrafficCounter counter) {
        if (counter.cumulativeReadBytes() + counter.cumulativeWrittenBytes() > 0) {
            String buffer = "Monitor " + counter.name() +
                    " Current Speed Read: " + (counter.lastReadThroughput()) + " Bytes/s, " + (counter.lastReadThroughput() >> 10) + " KB/s, " +
                    "Current Speed Write: " + (counter.lastWriteThroughput()) + " Bytes/s, " + (counter.lastWriteThroughput() >> 10) + " KB/s, " +
                    "Current Read: " + (counter.cumulativeReadBytes()) + " Bytes, " + (counter.cumulativeReadBytes() >> 10) + " KB, " +
                    "Current Write: " + (counter.cumulativeWrittenBytes()) + " Bytes, " + (counter.cumulativeWrittenBytes() >> 10) + " KB";
            counter.resetCumulativeTime();
            if (log.isDebugEnabled()) log.debug("traffic info : {}", buffer);
        }
    }
}
