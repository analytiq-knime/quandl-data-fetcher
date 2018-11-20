package plugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActivatorTest {

    @Test
    public void veryStupidTest() {
        assertEquals("quandl_artid.plugin", Activator.PLUGIN_ID);
    }
}
