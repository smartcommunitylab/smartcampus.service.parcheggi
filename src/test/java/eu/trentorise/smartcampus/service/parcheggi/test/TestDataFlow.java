package eu.trentorise.smartcampus.service.parcheggi.test;

import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import eu.trentorise.smartcampus.service.parcheggi.impl.GetParcheggiTrentoDataFlow;

public class TestDataFlow extends TestCase {
	public void testRun() throws Exception {
		DataFlowTestHelper helper = new DataFlowTestHelper();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Map<String, Object> out = helper.executeDataFlow("smartcampus.service.parcheggi", "GetParcheggiTrento", new GetParcheggiTrentoDataFlow(), parameters);
		System.err.println(out);
		
	}
}
