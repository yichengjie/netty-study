package com.yicj.study.codec;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yicj.study.vo.SubscribeReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonCodecTest {

	@Test
	public void testEncode() {
		SubscribeReq subReq = subReq(100);
		byte data[] = JSONObject.toJSONString(subReq).getBytes();
		SubscribeReq retObj = JSON.parseObject(new String(data)).toJavaObject(SubscribeReq.class);
		log.info(retObj.toString());
	}

	private SubscribeReq subReq(int i) {
		SubscribeReq req = new SubscribeReq();
		req.setUserName("yicj");
		req.setAddress("北京市朝阳区天辰东路奥林匹克公园");
		req.setPhoneNumber("157xxxxxxxx");
		req.setProductName("Netty for marshalling");
		req.setSubReqID(i);
		return req;
	}

}
