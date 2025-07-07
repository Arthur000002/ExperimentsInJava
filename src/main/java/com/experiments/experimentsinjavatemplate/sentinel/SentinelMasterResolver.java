package com.experiments.experimentsinjavatemplate.sentinel;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * Utility to resolve Redis master address from Sentinel with authentication support.
 */
public class SentinelMasterResolver {

    private final String masterName;
    private final Set<String> sentinels;
    private final String sentinelUsername;
    private final String sentinelPassword;

    public SentinelMasterResolver(String masterName, Set<String> sentinels, String sentinelUsername, String sentinelPassword) {
        this.masterName = masterName;
        this.sentinels = sentinels;
        this.sentinelUsername = sentinelUsername;
        this.sentinelPassword = sentinelPassword;
    }

    public HostAndPort resolve() {
        for (String sentinel : sentinels) {
            String[] parts = sentinel.split(":");
            if (parts.length != 2) {
                continue;
            }
            String host = parts[0];
            int port;
            try {
                port = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            try (Jedis jedis = new Jedis(host, port)) {
                if (sentinelPassword != null && !sentinelPassword.isBlank()) {
                    if (sentinelUsername != null && !sentinelUsername.isBlank()) {
                        jedis.auth(sentinelUsername, sentinelPassword);
                    } else {
                        jedis.auth(sentinelPassword);
                    }
                }
                List<String> addr = jedis.sentinelGetMasterAddrByName(masterName);
                if (addr != null && addr.size() == 2) {
                    int masterPort = Integer.parseInt(addr.get(1));
                    return new HostAndPort(addr.get(0), masterPort);
                }
            } catch (Exception e) {
                // ignore and try next sentinel
            }
        }
        throw new IllegalStateException("Unable to resolve master address from sentinels");
    }
}
