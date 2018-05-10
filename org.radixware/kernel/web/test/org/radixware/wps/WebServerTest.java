/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.wps;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.utils.FileUtils;
import static org.junit.Assert.*;
import org.radixware.ws.servlet.WPSBridge;


public class WebServerTest {

    public WebServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {


//        WpsEnvironment.sessionFactory = new WpsEnvironment.EasSessionFactory() {
//
//            @Override
//            public EasSession createSession(WpsEnvironment env) {
//                return new TestEasSession(env);
//            }
//        };
        WpsEnvironment.connectionsFactory = new WpsEnvironment.ConnectionsFactory() {

            @Override
            public ConnectionOptions createConnection(WpsEnvironment env) {
                ConnectionOptions c = new ConnectionOptions(env, null) {

                    @Override
                    protected int showSelectRootDialog(IClientEnvironment env, List<String> names, int currentSelection) {
                        return 0;
                    }

                    @Override
                    public boolean edit(List<String> existingConnections) {
                        return false;
                    }

                    @Override
                    public ConnectionOptions createUnmodifableCopy() {
                        return this;
                    }
                                        
                };
                c.setInitialAddresses(Collections.singletonList(new InetSocketAddress("localhost", 54321)));
                return c;
            }
        };
        WpsEnvironment.test_mode = true;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        WPSBridge.shutdown();
    }
    static final Random rnd = new Random();
    private static final ExecutorService wsExec = Executors.newFixedThreadPool(10, new ThreadFactory() {

        int counter = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Web server invocation thread #" + counter);
            counter++;
            t.setDaemon(true);
            return t;
        }
    });

    class ImageLoaderTask implements Runnable {

        private final String uri;
        private final List<String> images;
        final List<Throwable> exceptions = new LinkedList<Throwable>();
        private final CountDownLatch latch;
        private final UserThread userThread;

        public ImageLoaderTask(UserThread userThread, CountDownLatch latch, String uri, List<String> images) {
            this.uri = uri;
            this.images = images;
            this.latch = latch;
            this.userThread = userThread;
        }

        @Override
        public void run() {
            try {
                int idx = uri.indexOf("?");
                String path = uri;
                String query = "";
                if (idx > 0) {
                    path = uri.substring(0, idx);
                    query = uri.substring(idx + 1);
                }
                TestHttpServletRequest rq2 = new TestHttpServletRequest(userThread.initRq, path, query);
                TestHttpServletResponse rs = new TestHttpServletResponse();
                userThread.invokeWebServer(rq2, rs);
                byte[] imageBytes = rs.getServerOutputBytes();
                assertNotNull(imageBytes);
                assertTrue(imageBytes.length > 100);
                synchronized (images) {
                    images.remove(uri);
                }

            } catch (Throwable e) {
                exceptions.add(e);
            } finally {
                latch.countDown();
            }
        }
    }

    static class UserTask implements Runnable {

        private final CountDownLatch latch;
        private final List<Throwable> exceptions = new LinkedList<Throwable>();

        public UserTask(CountDownLatch latch) {

            this.latch = latch;
        }

        @Override
        public void run() {
            final UserThread userThread = (UserThread) Thread.currentThread();
            try {
                TestHttpServletResponse rs = new TestHttpServletResponse();
                userThread.invokeWebServer(userThread.initRq, rs);
                String rootId = userThread.getRootId(rs.getServerOutput());
                if (rootId != null) {
                    String response = userThread.notifyREndered(rootId, userThread.initRq, rootId);
                    for (int i = 0; i < 10; i++) {
                        response = userThread.clickButton(rootId, userThread.initRq, "Yes", response);
                        response = userThread.closeDialog(rootId, userThread.initRq, response);
                        //exception box may go here. if so exit test
                        String closeButton = userThread.findButton(response, "Close");
                        if (closeButton != null) {
                            response = userThread.clickButton(rootId, userThread.initRq, "Close", response);
                            response = userThread.closeDialog(rootId, userThread.initRq, rs.getServerOutput());
                            assertNotNull(response);
                            //as result, here should be body update
                            String newRootId = userThread.getRootId(rs.getServerOutput());
                            assertEquals(newRootId, rootId);
                            //try again 10 times
                        } else {
                            //login dialog is shown
                            response = userThread.clickButton(rootId, userThread.initRq, "Ok", response);
                            response = userThread.closeDialog(rootId, userThread.initRq, response);
                            assertNotNull(response);//body should goes here                                  
                            List<String> paragraphs = userThread.listParagraphs(response);

                            assertTrue(paragraphs.size() > 1);

                            //first paragraph is root, no we should open second paragraph
                            //and recieve list of group items
                            paragraphs.remove(0);
                            List<String> buttons = new LinkedList<String>();
                            List<String> titles = new LinkedList<String>();
                            for (String p : paragraphs) {
                                String eb = userThread.getTreeListItemExpandButton(p, response);
                                assertNotNull(eb);
                                buttons.add(eb);
                                titles.add(userThread.getTreeListItemTitle(response, p));
                            }
                            userThread.openParagraps(rootId, userThread.initRq, buttons, titles, "/");
                            break;
                        }
                    }
                }
            } catch (Throwable e) {
                exceptions.add(e);
            } finally {
                latch.countDown();
            }
        }
    }

    static class TimeConsumer {

        private int sum;
        private int count;
        private float average;
        int last = 0;

        public void addTime(long t) {
            sum += t;
            count++;
            average = (float) sum / (float) count;

        }

        void report() {
            System.out.println(" average response time: " + average);
        }
    }

    class UserThread extends Thread {

        private final Set<String> loadedImages = new HashSet<String>();
        final TestHttpServletRequest initRq;
        final ExecutorService imageLoader = Executors.newFixedThreadPool(3, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Image loader thread");
                t.setDaemon(true);
                return t;
            }
        });
        final CountDownLatch latch;
        final TimeConsumer consumer;

        public UserThread(TimeConsumer consumer, String name, final CountDownLatch latch, Runnable r) {
            super(r, name);
            this.initRq = new TestHttpServletRequest("explorer.html", "_w_e=_ae^_wid=root^_a=init^_p=ln=WpsTester,ls=WpsTest,ll=en");
            this.latch = latch;
            setDaemon(true);
            this.consumer = consumer;
        }

        void openParagraps(String rootId, TestHttpServletRequest rq, List<String> paragraphItemButtonIds, List<String> paragraphTitles, String parentPath) throws Throwable {

            int index = 0;



            for (String paragraph : paragraphItemButtonIds) {

                String ct = paragraphTitles.get(index);
                System.out.println("Entering paragraph: " + parentPath + ct);
                index++;
                String response = clickItem(rootId, rq, paragraph);
                assertNotNull(response);
                List<String> groups = listGroups(response);
                List<String> leafs = new LinkedList<String>();
                List<String> leafTitles = new LinkedList<String>();
                for (String group : groups) {
                    String leaf = getTreeListItemLeaf(response, group);
                    assertNotNull(leaf);
                    leafs.add(leaf);
                    leafTitles.add(getTreeListItemTitle(response, group));
                }
                List<String> paragraphs = listParagraphs(response);
                List<String> newParagraphTitles = new LinkedList<String>();

                List<String> buttons = new LinkedList<String>();

                for (String p : paragraphs) {
                    String b = getTreeListItemExpandButton(p, response);
                    if (b != null) {
                        buttons.add(b);
                        String title = getTreeListItemTitle(response, p);
                        newParagraphTitles.add(title);
                    }

                    String leaf = getTreeListItemLeaf(response, p);
                    assertNotNull(leaf);
                    leafs.add(leaf);
                    leafTitles.add(getTreeListItemTitle(response, p));
                    //    assertNotNull(b);
                }

                int i = 0;
                for (String leaf : leafs) {
                    String title = leafTitles.get(i);
                    i++;

                    System.out.print("Opening item      : " + parentPath + ct + "/" + title);
                    response = clickItem(rootId, rq, leaf);
                    int timeOut = rnd.nextInt(30000) + 2000;

                    System.out.println("............done, will wait " + ((float) timeOut) / 1000 + " seconds before continue");


                    Thread.sleep(timeOut);
                    assertNotNull(response);
                }

                openParagraps(rootId, rq, buttons, newParagraphTitles, parentPath + ct + "/");
            }
        }

        String expandListItem(String rootId, TestHttpServletRequest rq, String itemId, String response) throws Throwable {
            int itemLocation = response.indexOf("id=\"" + itemId + "\"");
            if (itemLocation < 0) {
                return null;
            }
            int searchStart = response.indexOf(">", itemLocation);
            if (searchStart < 0) {
                return null;
            }
            searchStart += 1;
            int index = response.indexOf("<img", searchStart);
            if (index > 0) {
                int end = response.indexOf(">", index);
                String itemStr = response.substring(index, end);
                String classes = getItemClasses(itemStr);
                if (classes.contains("rwt-tree-node-indicator")) {
                    String id = getItemId(itemStr);
                    if (id != null) {
                        return clickItem(rootId, rq, id);
                    }
                }
            }
            return null;
        }

        String getTreeListItemExpandButton(String itemId, String response) throws Throwable {
            int itemLocation = response.indexOf("id=\"" + itemId + "\"");
            if (itemLocation < 0) {
                return null;
            }
            int searchStart = response.indexOf(">", itemLocation);
            if (searchStart < 0) {
                return null;
            }
            searchStart += 1;
            int index = response.indexOf("<img", searchStart);
            if (index > 0) {
                int end = response.indexOf(">", index);
                String itemStr = response.substring(index, end);
                String classes = getItemClasses(itemStr);
                if (classes != null && classes.contains("rwt-tree-node-indicator")) {
                    return getItemId(itemStr);

                }
            }
            return null;
        }

        String getTreeListItemLeaf(String response, String itemId) {
            int itemLocation = response.indexOf("id=\"" + itemId + "\"");
            if (itemLocation < 0) {
                return null;
            }
            int searchStart = response.indexOf(">", itemLocation);
            if (searchStart < 0) {
                return null;
            }
            searchStart += 1;
            int index = response.indexOf("<div", searchStart);
            if (index > 0) {
                int end = response.indexOf(">", index);
                String itemStr = response.substring(index, end);
                String classes = getItemClasses(itemStr);
                if (classes.contains("rwt-ui-tree-node-leaf")) {
                    return getItemId(itemStr);
                }
            }
            return null;
        }

        String getTreeListItemTitle(String response, String itemId) {
            int itemLocation = response.indexOf("id=\"" + itemId + "\"");
            if (itemLocation < 0) {
                return null;
            }
            int searchStart = response.indexOf(">", itemLocation);
            if (searchStart < 0) {
                return null;
            }
            searchStart += 1;
            int index = response.indexOf("<a", searchStart);
            while (index > 0) {
                int end = response.indexOf(">", index);
                int start = response.indexOf("<", end);
                if (start > end) {
                    return response.substring(end + 1, start);
                }

            }
            return "_";
        }

        String clickButton(String rootId, TestHttpServletRequest rq, String button, String rs) throws Throwable {
            String yesButton = findButton(rs, button);
            assertNotNull(yesButton);
            rq = new TestHttpServletRequest(rq, createClickRq(rootId, yesButton));
            return getResponseStr(rq, rootId);
        }

        String clickItem(String rootId, TestHttpServletRequest rq, String itemId) throws Throwable {
            rq = new TestHttpServletRequest(rq, createClickRq(rootId, itemId));
            return getResponseStr(rq, rootId);
        }

        List<String> listParagraphs(String response) {
            List<String> result = new LinkedList<String>();
            listItems(response, result, null, null);
            return result;
        }

        List<String> listGroups(String response) {
            List<String> result = new LinkedList<String>();
            listItems(response, null, null, result);
            return result;
        }

        List<String> listEntities(String response) {
            List<String> result = new LinkedList<String>();
            listItems(response, null, result, null);
            return result;
        }

        void listItems(String response, List<String> paragraps, List<String> entities, List<String> groups) {
            int index = response.indexOf("<li");

            while (index > 0) {
                int end = response.indexOf(">", index + 1);

                String item = response.substring(index, end);

                int classIdx = item.indexOf("class=");
                if (classIdx > 0) {
                    int start = item.indexOf('"', classIdx + 1);
                    if (start > 0) {
                        int endQ = item.indexOf('"', start + 1);
                        if (endQ > 0) {
                            String classStr = item.substring(start + 1, endQ);
                            String[] classes = classStr.split(" ");
                            for (int i = 0; i < classes.length; i++) {
                                if ("rwt-ui-tree-radix-paragraph".equals(classes[i])) {
                                    String id = getItemId(item);
                                    if (id != null && id.startsWith("wf_") && paragraps != null) {
                                        paragraps.add(id);
                                    }
                                } else if ("rwt-ui-tree-radix-entity".equals(classes[i])) {
                                    String id = getItemId(item);
                                    if (id != null && id.startsWith("wf_") && entities != null) {
                                        entities.add(id);
                                    }
                                } else if ("rwt-ui-tree-radix-group".equals(classes[i])) {
                                    String id = getItemId(item);
                                    if (id != null && id.startsWith("wf_") && groups != null) {
                                        groups.add(id);
                                    }
                                }
                            }
                        }
                    }
                }

                index = response.indexOf("<li", end);
            }
        }

        String getItemId(String itemStr) {
            int index = itemStr.indexOf("id=\"");
            if (index > 0) {
                int start = index + "id=\"".length();
                int end = itemStr.indexOf('"', start + 1);
                if (end > start) {
                    return itemStr.substring(start, end);
                }
            }
            return null;
        }

        String getItemClasses(String itemStr) {
            int index = itemStr.indexOf("class=\"");
            if (index > 0) {
                int start = index + "class=\"".length();
                int end = itemStr.indexOf('"', start + 1);
                if (end > start) {
                    return itemStr.substring(start, end);
                }
            }
            return null;
        }

        String closeDialog(String rootId, TestHttpServletRequest rq, String rs) throws Throwable {
            String closeDialogId = getCloseDialogId(rs);
            assertNotNull(closeDialogId);
            rq = new TestHttpServletRequest(rq, createCloseDialogRq(rootId, closeDialogId));
            return getResponseStr(rq, rootId);
        }

        String notifyREndered(String rootId, TestHttpServletRequest rq, String widgetId) throws Throwable {
            rq = new TestHttpServletRequest(rq, createRenderedRq(rootId, widgetId));
            return getResponseStr(rq, rootId);
        }

        private String getResponseStr(TestHttpServletRequest rq, String rootId) throws Throwable {
            TestHttpServletResponse r = new TestHttpServletResponse();
            invokeWebServer(rq, r);
            String result = r.getServerOutput();
            while (result.startsWith("<waitbox command = ")) {
                TestHttpServletRequest rq2 = new TestHttpServletRequest(rq, "root=" + rootId + "&_w_e=_ae^_wid=wait-box^_a=updated");
                r = new TestHttpServletResponse();
                long ct = System.currentTimeMillis();
                invokeWebServer(rq2, r);
                ct = System.currentTimeMillis() - ct;
                consumer.addTime(ct);
                result = r.getServerOutput();
            }
            tryLoadImages(rq, result);
            return result;
        }

        private void tryLoadImages(final TestHttpServletRequest rq, String response) throws Throwable {
            int index = response.indexOf("<img");

            final List<String> images = new LinkedList<String>();
            while (index > 0) {
                int end = response.indexOf("/>", index);
                assertTrue(end > index);
                String test = response.substring(index, end);
                int srcPos = test.indexOf("src");
                if (srcPos > 0) {
                    int quote = test.indexOf('"', srcPos);
                    assertTrue(quote > 0);
                    int endQuote = test.indexOf('"', quote + 1);
                    assertTrue(endQuote > quote);
                    final String uri = test.substring(quote + 1, endQuote);
                    assertNotNull(uri);
                    if (!loadedImages.contains(uri)) {
                        images.add(uri);
                        loadedImages.add(uri);
                    }
                }
                index = response.indexOf("<img", end);
            }
            if (images.size() > 0) {
                final CountDownLatch latch = new CountDownLatch(images.size());


                List<String> start = new ArrayList<String>(images);
                List<ImageLoaderTask> tasks = new LinkedList<ImageLoaderTask>();
                for (int i = 0; i < start.size(); i++) {
                    final String uri = start.get(i);
                    assertNotNull(uri);
                    ImageLoaderTask task = new ImageLoaderTask(UserThread.this, latch, uri, images);
                    tasks.add(task);
                    this.imageLoader.submit(task);
                }
                try {
                    latch.await();
                    for (ImageLoaderTask task : tasks) {
                        if (!task.exceptions.isEmpty()) {
                            for (Throwable e : task.exceptions) {
                                e.printStackTrace();
                            }
                            throw task.exceptions.get(0);
                        }
                    }
                } catch (InterruptedException ex) {
                    fail();
                }
                assertTrue(images.isEmpty());
            }
        }

        private String createRenderedRq(String rootId, String widgetId) {
            return "root=" + rootId + "&_w_e=_re^_wid=" + widgetId + "&_rq_id_=" + System.currentTimeMillis();
        }

        private String createClickRq(String rootId, String widgetId) {
            return "root=" + rootId + "&_w_e=_ae^_a=click^_wid=" + widgetId + "&_rq_id_=" + System.currentTimeMillis();
        }

        private String createCloseDialogRq(String rootId, String widgetId) {
            return "root=" + rootId + "&_w_e=_re&" + widgetId + "=closed&_rq_id_=" + System.currentTimeMillis();
        }

        private String getRootId(String result) {
            int index = result.indexOf("<update-element id=\"");
            if (index >= 0) {
                int start = index + "<update-element id=\"".length();
                int end = result.indexOf('"', start);
                if (end > 0) {
                    return result.substring(start, end);
                }
            }
            return null;
        }

        private String getCloseDialogId(String result) {
            int index = result.indexOf("<child-remove id=\"");
            if (index >= 0) {
                int start = index + "<child-remove id=\"".length();
                int end = result.indexOf('"', start);
                if (end > 0) {
                    return result.substring(start, end);
                }
            }
            return null;
        }

        private String findButton(String result, String title) {
            int index = result.indexOf("<button");
            while (index >= 0) {
                int end = result.indexOf("</button>", index);
                if (end > 0) {
                    String test = result.substring(index, end);
                    if (test.indexOf(">" + title + "<") > 0) {
                        int id = test.indexOf(" id=\"");
                        if (id > 0) {
                            int start = id + " id=\"".length();
                            int idEnd = test.indexOf('"', start);
                            if (idEnd > 0) {
                                return test.substring(start, idEnd);
                            }
                        }
                    }
                } else {
                    return null;
                }
                index = result.indexOf("<button", end + 1);
            }
            return null;
        }

        protected void invokeWebServer(TestHttpServletRequest rq, TestHttpServletResponse rs) throws InterruptedException {
            class Invoker implements Runnable {

                final TestHttpServletRequest rq;
                final TestHttpServletResponse rs;
                final CountDownLatch latch;

                public Invoker(CountDownLatch latch, TestHttpServletRequest rq, TestHttpServletResponse rs) {
                    this.rq = rq;
                    this.rs = rs;
                    this.latch = latch;
                }

                @Override
                public void run() {
                    try {
                        WPSBridge.processRequest(rq, rs);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        fail();
                    } finally {
                        //WebServer.getInstance().processRequest(rq, rs);
                        latch.countDown();
                    }
                }
            }
            final CountDownLatch latch = new CountDownLatch(1);
            wsExec.submit(new Invoker(latch, rq, rs));
            latch.await();
        }
    }

    private void doLoadTest(ThreadFactory threadFactory, final TimeConsumer consumer, final CountDownLatch latch, final int CC) throws Throwable {



        ExecutorService service = Executors.newFixedThreadPool(CC, threadFactory);

        try {
            WPSBridge.wpsInit();
            long start = System.currentTimeMillis();
            List<UserTask> tasks = new LinkedList<UserTask>();
            for (int i = 0; i < CC; i++) {
                UserTask task = new UserTask(latch);
                tasks.add(task);
                service.submit(task);

            }
            try {
                latch.await();
                long end = System.currentTimeMillis() - start;
                System.out.println("Load test executed: " + end);
                consumer.report();
                for (UserTask task : tasks) {
                    Thread.sleep(rnd.nextInt(10000));
                    if (!task.exceptions.isEmpty()) {
                        for (Throwable e : task.exceptions) {
                            e.printStackTrace();
                        }
                        fail();
                    }
                }

            } catch (InterruptedException ex) {
            }
        } finally {
            WPSBridge.shutdown();
        }
    }

    @Ignore
    @Test
    public void loadTest() throws Throwable {
        final TimeConsumer consumer = new TimeConsumer();
        final CountDownLatch latch = new CountDownLatch(100);
        doLoadTest(new ThreadFactory() {

            int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new UserThread(consumer, "User #" + (counter++), latch, r);
            }
        }, consumer, latch, 100);
    }

    class NetworkUserThread extends UserThread {

        List<String> theCookies = null;
        boolean connected = false;
        final Object invokeLock = new Object();

        public NetworkUserThread(TimeConsumer consumer, String name, CountDownLatch latch, Runnable r) {
            super(consumer, name, latch, r);
        }

        @Override
        protected void invokeWebServer(TestHttpServletRequest rq, TestHttpServletResponse rs) throws InterruptedException {
            synchronized (invokeLock) {
                try {

//                if (!connected) {
//                    URL yahoo = new URL("http://localhost:8084");
//                    HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
//                    yc.setRequestMethod("GET");
//                    yc.connect();
//
//                    List<String> cookies = yc.getHeaderFields().get("Set-Cookie");
//
//                    FileUtils.copyStream(yc.getInputStream(), rs.getOutputStream());
//
////                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
////
////                    StringBuilder sb = new StringBuilder();
////                    try {
////
////                        String inputLine;
////                        while ((inputLine = in.readLine()) != null) {
////                            sb.append(inputLine).append("\n");
////                            //  System.out.println(inputLine);
////                        }
////                    } finally {
////                        in.close();
////                    }
//
//                    theCookies = cookies;
//                    connected = true;
//
//                } else {

                    URL yahoo = new URL("http://localhost:8084/" + rq.getContextPath() + "?" + rq.getQueryString());
                    HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();

                    yc.setRequestMethod("GET");
                    if (theCookies != null) {
                        for (String cookie : theCookies) {
                            yc.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
                        }
                    } else {
                        theCookies = yc.getHeaderFields().get("Set-Cookie");
                    }

                    FileUtils.copyStream(yc.getInputStream(), rs.getOutputStream());
//                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                    try {
////                        String inputLine;
////                        boolean first = false;
////                        StringBuilder sb = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            if (first) {
////                                if (inputLine.startsWith("<updates id=\"")) {
////                                    int idNumberEnd = inputLine.indexOf("\"", 13);
////                                    if (idNumberEnd < 0) {
////                                        throw new AssertionError("Unexpected output from Web Server");
////                                    } else {
////                                        String idStr = inputLine.substring(13, idNumberEnd);
////                                        try {
////                                            long id = Long.parseLong(idStr);
////                                            if (id != rqId) {
////                                                throw new AssertionError("Response id mismatch");
////                                            }
////                                        } catch (NumberFormatException e) {
////                                            throw new AssertionError("Unexpected output from Web Server");
////                                        }
////                                    }
////                                } else {
////                                    throw new AssertionError("Unexpected output from Web Server");
////                                }
////                                first = false;
////                            }
////                            sb.append(inputLine).append("\n");
////                            //  System.out.println(inputLine);
////                        }
////                        handler.processResponse(sb.toString(), theCookies);
//                        FileUtils.copyStream(yc.getInputStream(), rs.getOutputStream());
//                    } finally {
//                        in.close();
//                    }
//                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void networkLoadTest() throws Throwable {
        final TimeConsumer consumer = new TimeConsumer();
        int count = 50;
        final CountDownLatch latch = new CountDownLatch(count);
        doLoadTest(new ThreadFactory() {

            int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new NetworkUserThread(consumer, "User #" + (counter++), latch, r);
            }
        }, consumer, latch, count);

    }
}
