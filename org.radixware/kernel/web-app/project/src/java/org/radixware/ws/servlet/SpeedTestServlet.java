package org.radixware.ws.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Servlet implementation class SpeedTestServlet</p>
 */
@WebServlet(name = "SpeedTestServlet", value = {"/speedTest.html"})
public class SpeedTestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    static final String MAX_PACKET_SIZE_KEY = "rdx.web.speed.test.max.packet.size";
    static final String MAX_PACKET_TTL_KEY = "rdx.web.speed.test.max.packet.ttl";
    static final int DEFAULT_MAX_PACKET_SIZE = 10;
    static final int DEFAULT_MAX_PACKET_TTL = 5;

    static final String SESSION_PACKET = "sessionPacket";
    static final String PACKET_SIZE_QUERY = "packetSize";

    private SpeedTestServletShadow shadow;
    private int maxPacketSizeMb;


    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);        
        final String packetSizeValue = 
            getParamValue(config, MAX_PACKET_SIZE_KEY, String.valueOf(DEFAULT_MAX_PACKET_SIZE));
        final String packetTTLValue = 
            getParamValue(config, MAX_PACKET_TTL_KEY, String.valueOf(DEFAULT_MAX_PACKET_TTL));
        
        final SpeedTestServletShadow.ExternalLogger logger = new SpeedTestServletShadow.ExternalLogger() {            
            public void log(final String data) {
                SpeedTestServlet.this.log(data);
            }
        };        
        
        maxPacketSizeMb = parseMaxPacketSize(packetSizeValue, logger);        

        this.shadow = new SpeedTestServletShadow(maxPacketSizeMb, parsePacketTtl(packetTTLValue, logger), logger);
    }
    
    private static String getParamValue(final ServletConfig config, final String paramName, final String defaultValue){
        final String configParamVal = config.getInitParameter(paramName);
        final String value = System.getProperty(paramName, configParamVal);
        return value==null ? defaultValue : value;
    }
    
    private static int parseMaxPacketSize(final String value, final SpeedTestServletShadow.ExternalLogger logger){
        if (value == null) {
            throw new IllegalArgumentException("Max packet time-to-live string can't be null");
        } else {
            try {
                return Integer.parseInt(value);                
            } catch (NumberFormatException exc) {
                logger.log("Max packet size for this servlet contains non-numeric value [" + value + "]. Default size [" + SpeedTestServlet.DEFAULT_MAX_PACKET_SIZE + "] MBytes will be used");
                return SpeedTestServlet.DEFAULT_MAX_PACKET_SIZE;
            }
        }
    }
    
    private static int parsePacketTtl(final String value, final SpeedTestServletShadow.ExternalLogger logger){
        if (value == null) {
            throw new IllegalArgumentException("Max packet size string can't be null");
        } else {
            try {
                return Integer.parseInt(value);                
            } catch (NumberFormatException exc) {
                logger.log("Max packet time-to-live for this servlet contains non-numeric value [" + value + "]. Default time [" + SpeedTestServlet.DEFAULT_MAX_PACKET_TTL + "]  minutes will be used");
                return SpeedTestServlet.DEFAULT_MAX_PACKET_SIZE;
            }
        }
    }    

    /**
     * @see Servlet#destroy()
     */
    public void destroy() {
        try {
            shadow.close();
        } catch (IOException e) {
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (maxPacketSizeMb<=0){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else{
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(String.format(loadResource("speedTest.html"), String.valueOf(maxPacketSizeMb*SpeedTestServletShadow.MEGABYTE)));
            response.getWriter().flush();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (maxPacketSizeMb<=0){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else{
            final HttpSession session = request.getSession();
            final Properties props = new Properties();
            final int packetSize;

            try (final Reader rdr = request.getReader()) {	// Read key/value content from source input stream
                props.load(rdr);
            } catch (IOException exc) {
            }

            if (props.containsKey(PACKET_SIZE_QUERY)) {
                try {
                    packetSize = Integer.parseInt(props.getProperty(PACKET_SIZE_QUERY));
                } catch (NumberFormatException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key [" + PACKET_SIZE_QUERY + "] contains non-numeric value [" + props.getProperty(PACKET_SIZE_QUERY) + "]");
                    return;
                }

                if (session.getAttribute(SESSION_PACKET) == null) {
                    try {
                        final PackageContent content = shadow.newPacket(packetSize);

                        session.setAttribute(SESSION_PACKET, content);
                        sendData(response, content.getContent());
                    } catch (Exception exc) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exc.getMessage());
                    }
                } else {
                    final PackageContent content = (PackageContent) session.getAttribute(SESSION_PACKET);
                    final byte[] data = content.getContent();

                    if (data == null || data.length != packetSize) {
                        try {
                            final PackageContent newContent = shadow.newPacket(packetSize);

                            session.setAttribute(SESSION_PACKET, newContent);
                            sendData(response, newContent.getContent());
                        } catch (Exception exc) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exc.getMessage());
                        }
                    } else {
                        sendData(response, data);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key [" + PACKET_SIZE_QUERY + "] is missing in the source input stream");
            }
        }
    }

    static String loadResource(final String resourceName) throws IOException {
        if (resourceName == null || resourceName.isEmpty()) {
            throw new IllegalArgumentException("Resource name can't be null or empty");
        } else {
            final StringBuilder sb = new StringBuilder();

            try (final InputStream is = SpeedTestServlet.class.getResourceAsStream(resourceName)) {
                if (is == null) {
                    throw new IllegalArgumentException("Resource name [" + resourceName + "] not found");
                } else {
                    try (final Reader rdr = new InputStreamReader(is, "UTF-8")) {
                        final char[] buffer = new char[8192];
                        int len;

                        while ((len = rdr.read(buffer)) > 0) {
                            sb.append(buffer, 0, len);
                        }
                    }
                    return sb.toString();
                }
            }
        }
    }

    static void sendData(final HttpServletResponse response, final byte[] content) throws IOException {
        response.setContentType("application/octet-stream");
        response.setContentLength(content.length);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
    }
}
