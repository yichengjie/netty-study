package com.yicj.study.proto;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

public class SubscribeReqTest {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req = createSubscribeReq() ;
		System.out.println("Before encode : " + req.toString());
		//
		SubscribeReqProto.SubscribeReq result = decode(encode(req)) ;
		System.out.println("decode content is : " + result.toString());
		//
		System.out.println(req.equals(result));
	}
	
	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray() ;
	}
	
	private static SubscribeReqProto.SubscribeReq decode(byte[] data) throws InvalidProtocolBufferException{
		return SubscribeReqProto.SubscribeReq.parseFrom(data) ;
	}
	
	private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
		SubscribeReqProto.SubscribeReq.Builder builder = 
				SubscribeReqProto.SubscribeReq.newBuilder() ;
		builder.setSubReqId(1) ;
		builder.setUserName("yicj") ;
		builder.setProductName("Netty Book") ;
		List<String> address = new ArrayList<>() ;
		address.add("北京") ;
		address.add("天津") ;
		address.add("河北") ;
		builder.addAllAddress(address) ;
		return builder.build() ;
	} 
	
}
