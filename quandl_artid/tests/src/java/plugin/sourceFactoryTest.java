package plugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import plugin.sourceFactory;

public class sourceFactoryTest {
	
	@Test
    public void testGetNrNodeViews() {
		sourceFactory factory = new sourceFactory();
		
		int nrviews = factory.getNrNodeViews();
		
		assertEquals(1, nrviews);
	}
}