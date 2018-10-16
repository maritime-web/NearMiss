package dk.dma.nearmiss.pilotplugsimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
public class PilotPlugSimulatorApplication implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String USAGE = "--port==localPort [--connectTo==remoteHost:remotePort]...";
    private final static Pattern HOSTNAME_PORT_PATTERN = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<PrintWriter> outboundPrintWriters = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        SpringApplication.run(PilotPlugSimulatorApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Running PilotPlugSimulatorApplication");

        // Command line arguments

        checkArguments(args);

        List<String> portList = args.getOptionValues("port");
        int port = Integer.parseInt(portList.get(0));
        List<String> connectTo = args.getOptionValues("connect");

        logger.info("port: {}", port);
        logger.info("connect: {}", connectTo.stream().collect(Collectors.joining(", ")));

        // Configure line readers

        connectTo.forEach(remote -> {
            HostnamePortnumber hp = parseRemoteAddresses(remote);
            if (hp != null) {
                Thread lineReader = new Thread(new LineReader(messageQueue, hp.hostname, hp.port));
                lineReader.setDaemon(true);
                lineReader.start();
            }
        });

        // Configure listener for line writers

        Thread t = new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);

                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        logger.info("Outbound connection established: " + socket.toString());
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                        outboundPrintWriters.add(printWriter);
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        t.setDaemon(true);
        t.start();

        // Configure line distributor

        Thread lineDistributor = new Thread(new LineDistributor(messageQueue, outboundPrintWriters));
        lineDistributor.setDaemon(true);
        lineDistributor.start();

        // Just wait

        while (true)
            Thread.sleep(10000);
    }

    private class HostnamePortnumber {
        private final String hostname;
        private final int port;

        public HostnamePortnumber(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
        }
    }

    private HostnamePortnumber parseRemoteAddresses(String remoteAddress) {
        HostnamePortnumber hostnamePortnumber = null;

        Matcher m = HOSTNAME_PORT_PATTERN.matcher(remoteAddress);

        if (m.matches()) {
            String hostname = m.group(1);
            int port = Integer.parseInt(m.group(2));
            hostnamePortnumber = new HostnamePortnumber(hostname, port);
        }

        return hostnamePortnumber;
    }

    private void checkArguments(ApplicationArguments args) {
        if (!args.containsOption("port"))
            fail("Missing mandatory --port option.");
        if (args.getOptionValues("port").size() > 1)
            fail("There must be exacly one --port option.");
        if (!args.containsOption("connect"))
            fail("Missing one or more --connect option(s).");

        Predicate<String> hostnamePortnumberFormat = HOSTNAME_PORT_PATTERN.asPredicate();
        if (!args.getOptionValues("connect").stream().allMatch(hostnamePortnumberFormat))
            fail("Option values for 'connect' must match format {hostname}:{port}");
    }

    private static void fail(String message) {
        System.err.println(message);
        System.err.println(USAGE);
        System.exit(-1);
    }

}
