package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import games.PostFix;

class PostFixTest {

	@Test
	void testPostFix() {
		PostFix pf = new PostFix("6*5-(2+4)");
		Assert.assertEquals("6 5 * 2 4 + -", pf.toString());
		Assert.assertEquals(24, pf.evaluate(), 0.001);
	}

	@Test
	void testPostFix2() {
		PostFix pf = new PostFix("6/(A-3/4)");
		Assert.assertEquals("6 A 3 4 / - /", pf.toString());
		Assert.assertEquals(24, pf.evaluate(), 0.01);
	}


	@Test
	void testPostFix3() {
		PostFix pf = new PostFix("Q/4*7+3");
		Assert.assertEquals("Q 4 / 7 * 3 +", pf.toString());
		Assert.assertEquals(24, pf.evaluate(), 0.01);
	}

	
}
