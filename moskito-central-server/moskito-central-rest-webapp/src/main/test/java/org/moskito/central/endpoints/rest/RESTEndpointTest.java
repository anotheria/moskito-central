package org.moskito.central.endpoints.rest;

import com.sun.jersey.test.framework.JerseyTest;

/**
 * Test class for MoSKito Central REST-endpoint.
 *
 * @author Vladyslav Bezuhlyi
 */
public class RESTEndpointTest extends JerseyTest {

    /*@Test
    public void testGetSnapshot() throws Exception {
        WebResource webResource = resource();
        String responseString = webResource.path("/central/getSnapshot").accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("*************************************\r\nresponseString=" + responseString);

        Snapshot responseSnapshot = webResource.path("/central/getSnapshot").accept(MediaType.APPLICATION_JSON).get(Snapshot.class);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\r\nresponseSnapshot=" + responseSnapshot);
        assertNotNull(responseSnapshot);
        assertNotNull(responseSnapshot.getStats());

        HashMap<String, HashMap<String, String>> stats = responseSnapshot.getStats();
        assertNotNull(stats);
        assertEquals(3, stats.size());
        assertNotNull(stats.get("test"));
        assertNotNull(stats.get("test2"));
        assertNotNull(stats.get("test3"));

        HashMap<String, String> test = stats.get("test");
        assertNotNull(test);
        assertEquals(2, test.size());
        assertNotNull(test.get("firstname"));
        assertNotNull(test.get("lastname"));
    }*/

}
