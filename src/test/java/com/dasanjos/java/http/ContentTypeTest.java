package com.dasanjos.java.http;

import org.junit.Assert;
import org.junit.Test;

//测试枚举类 ContentType 的功能，特别是根据扩展名获取正确的内容类型。
public class ContentTypeTest {

	@Test
	public void getCorrectContentTypeByExtension() {
		Assert.assertEquals(ContentType.CSS, ContentType.valueOf("CSS"));
		Assert.assertEquals(ContentType.GIF, ContentType.valueOf("GIF"));
		Assert.assertEquals(ContentType.HTM, ContentType.valueOf("HTM"));
		Assert.assertEquals(ContentType.HTML, ContentType.valueOf("HTML"));
		Assert.assertEquals(ContentType.ICO, ContentType.valueOf("ICO"));
		Assert.assertEquals(ContentType.JPG, ContentType.valueOf("JPG"));
		Assert.assertEquals(ContentType.JPEG, ContentType.valueOf("JPEG"));
		Assert.assertEquals(ContentType.PNG, ContentType.valueOf("PNG"));
		Assert.assertEquals(ContentType.TXT, ContentType.valueOf("TXT"));
		Assert.assertEquals(ContentType.XML, ContentType.valueOf("XML"));
	}

}
