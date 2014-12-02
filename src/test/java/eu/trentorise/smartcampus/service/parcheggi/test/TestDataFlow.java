package eu.trentorise.smartcampus.service.parcheggi.test;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.ServiceBusClient;
import it.sayservice.platform.client.jms.JMSServiceBusClient;
import it.sayservice.platform.core.message.Core.ActionInvokeParameters;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import junit.framework.TestCase;
import eu.trentorise.smartcampus.service.parcheggi.impl.GetParcheggiRoveretoDataFlow;

public class TestDataFlow extends TestCase {
	public void testRun() throws Exception {
		DataFlowTestHelper helper = new DataFlowTestHelper("test");
		Map<String, Object> parameters = new HashMap<String, Object>();
//		Map<String, Object> out = helper.executeDataFlow("smartcampus.service.parcheggi", "GetParcheggiTrento", new GetParcheggiTrentoDataFlow(), parameters);
		Map<String, Object> out = helper.executeDataFlow("smartcampus.service.parcheggi", "GetParcheggiRovereto", new GetParcheggiRoveretoDataFlow(), parameters);
		System.err.println(out);
		
	}
	
	public void testRemote() throws InvocationException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		ServiceBusClient client = new JMSServiceBusClient(connectionFactory);
		client.setClientId("test_subscriber");
		
		ActionInvokeParameters invokeService = client.invokeService("smartcampus.service.parcheggi", "GetParcheggiRovereto", parameters);
		System.err.println(invokeService.getDataCount());
	}

}
