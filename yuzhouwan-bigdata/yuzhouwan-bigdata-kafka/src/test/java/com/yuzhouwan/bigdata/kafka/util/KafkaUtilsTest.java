package com.yuzhouwan.bigdata.kafka.util;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Kafka Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public class KafkaUtilsTest {

    //    @Test
    public void testSendMessage() throws Exception {

        KafkaUtils k = KafkaUtils.getInstance();
        k.sendMessageToKafka("yuzhouwan");
    }
}
