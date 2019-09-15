package com.yicj.study.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubscribeResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private int subReqID;
	private int respCode;
	private String desc;
}
